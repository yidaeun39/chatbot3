
# KTDS 3차 Final Assessment 수행 리포트

# Table of contents

- [예제 - 챗봇 시스템](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [클라우드 아키텍처 구성도](#클라우드-아키텍처-구성도)
  - [MSA 아키텍처 구성도](#MSA-아키텍처-구성도)
  - [Event Storming](#Event-Storming)
  - [구현:](#구현-)
    - [DDD의 적용](#DDD의-적용)
    - [Saga](#Saga)
    - [Compensation Transaction](#Compensation-Transaction)
    - [Gateway](#Gateway)
    - [Dashboard](#Dashboard)
  - [운영](#운영)
    - [클라우드 배포](#클라우드-배포---Container-운영)
    - [컨테이너 자동확장 - HPA](#컨네이터-자동확장---HPA)
    - [ConfigMap](#ConfigMap)
    - [PVC활용](#PVC활용)
    - [Rediness 무정지 재배포](#Rediness-무정지-재배포)
    - [서비스 메쉬](#서비스-메쉬)
    - [Loggregation](#Loggregation)


# 서비스 시나리오

상품 구매/장바구니/조회수 별 추천 시스템 작성

기능적 요구사항
1. 사용자가 ChatBot에 상품을 검색한다
2. 검색한 상품으로 Train 데이터를 생성한다
3. 사용자가 검색했던 상품에 대한 질문을 작성한다
4. 질문을 바탕으로 Train 서비스가 데이터를 학습한다
5. 사용자가 검색했던 상품에 요청사항을 작성한다
6. 요청사항을 분석하고 승인/거절 여부에 따라 데이터를 변경한다 (승인 : 1 / 거절 : 0)
7. 학습 데이터를 마케팅 데이터로 전송한다
8. 마케터가 해당하는 사용자를 조회하여 데이터를 패치한다
9. 마케터가 추천 상품을 카카오로 전송한다
 
비기능적 요구사항
1. 트랜잭션
    1. 검색이 되지 않은 데이터는 분석 데이터가 생성되지 않는다 Sync 호출
1. 장애격리
    1. 데이터 분석 시스템에 장애가 발생하더라도 마케팅 시스템은 추천 및 유저 데이터 패치가 이루어져야 한다. Async (event-driven), Eventual Consistency
    1. 챗봇 시스템이 과중되면 검색 및 요청을 잠시후에 하도록 유도한다 Circuit breaker, fallback
1. 성능
    1. 대시보드를 통해 해당 상품의 질문 건 수와 요청 건 수를 확인 할 수 있다


# Event Storming
![image](https://github.com/yidaeun39/chatbot/assets/47437659/caeb38b1-a7a0-408b-90fc-7bd4506da6f5)
![image](https://github.com/yidaeun39/chatbot/assets/47437659/bc5fbd57-f803-4540-a6db-f8fa43b65295)

# 클라우드 아키텍처 구성도
 ## EDA 구성
 이벤트 드리븐 아키텍처에 따라 각 서비스 호출 시 비동기 방식으로 이루어질 수 있도록 구상하였다.

# MSA 아키텍처 구성도

*********

# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트와 JAVA로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8082 ~ 808n 이다)

```
cd chat
mvn spring-boot:run

cd train
mvn spring-boot:run 

cd dashboard
mvn spring-boot:run

cd marketing
mvn spring-boot:run 
```

## DDD의 적용

- 각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다. 

```
package chatbot.domain;

@Entity
@Table(name = "Chat_table")
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;

    private String questionMsg;

    private String requestMsg;

    private String requestType;

    private String userId;
}

```

## Saga
- Event Shunting 비동기 호출을 위한 EDA로 클러스터 내에 Apache Kafka를 설치한다. 사용자가 chat 서비스에 상품을 검색/질문/요청 했을 때 이벤트 드리븐하게 로직이 실행되고, 이벤트가 카프카에서 확인된다.
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install my-kafka bitnami/kafka --version 23.0.5

docker-compose exec -it kafka /bin/bash
cd /bin

./kafka-console-consumer --bootstrap-server localhost:9092 --topic labshoppubsub  --from-beginning

```
- chatbot에 상품을 초기 검색한다. 검색과 동시에 train 서비스에 분석을 위한 동일한 데이터가 생성된 것을 확인 할 수 있다. 
```
http localhost:8082/chats id=1 productId=001
http localhost:8083/trains
```
![image](https://github.com/yidaeun39/chatbot/assets/47437659/c5f07b0f-c80f-448d-8871-4cd514fe5c6b)
![image](https://github.com/yidaeun39/chatbot/assets/47437659/10bd0a66-612a-45d6-ab45-a26906960b00)
 

## Compensation Transaction
- 사용자가 Chat 서비스에서 상품의 요청 사항을 작성 할 경우 Train 서비스에서 해당 요청이 가능한 요청인지 판단한다.
```
  public static void requestChat(Requested requested) {
      repository().findById(Long.valueOf(requested.getId())).ifPresent(train->{
          if(requested.getRequestMsg().equals("불가능")){
              Refused refused = new Refused(train);
              refused.setRequestType("0"); 
              refused.publishAfterCommit();
          }else{
              train.setRequestCnt(train.getRequestCnt() + 1);
              repository().save(train);
          }
          
      });
  }
```
- Refused 이벤트가 발생 할 때 changeRequestType을 통해 requsetType을 0으로 변경한다.
```
  public static void changeRequestType(Refused refused) {
      repository().findById(refused.getId()).ifPresent(v ->{
          // 승인되지 않을 경우 request type을 0으로 변경
          v.setRequestType("0");
          repository().save(v);
      });
  }
```
- 여기서 카프카 토픽 캡처 나오면 될듯

## Gateway
- 상단 서비스 목록에서는 언급하지 않았지만 msaez에서 제공하는 gateway 서비스를 통해 트래픽 라우팅 룰을 정의한다.
- 키알리 캡처

## Dashboard
데이터 정합성을 위한 Read Model인 CQRS 서비스를 생성한다.


*********

# 운영

## 클라우드 배포 - Container 운영

각 서비스들은 메이븐 빌드하여 docker hub에 업로드한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/ce35facd-8cd5-45a7-8cab-96df301504b2)

yaml 배포 방식을 통해 도커 이미지 주소를 설정하고 컨테이너에 배포한다. 
```
spec:
      containers:
        - name: chat
          image: "yidaeun39/chat:v1"

kubectl apply -f kubernetes/deployment.yaml
```

각 구현체들은 각자의 source repository 에 구성되었고, 사용한 CI/CD 플랫폼은 CodeBuild를 사용하였으며, pipeline build script 는 각 프로젝트 폴더 이하에 buildspec.yml 에 포함되었다.
* 세팅 시 런타임 버전 확인 필요
* https://docs.aws.amazon.com/ko_kr/codebuild/latest/userguide/build-env-ref-available.html

1. Amazon ECR에 각 프로젝트 명으로 리포지토리를 생성한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/4db86dd4-e026-4fba-9268-d7f16ba91b29)
2. AWS CodeBuild를 통해 프로젝트를 구성한다. github를 사용하였기 때문에 소스공급자로 github를 선택한다. github 계정의 OAuth 요청 처리 후 프로젝트를 선택한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/277cd7d1-61e9-41dc-89b3-899a85d274bd)
3. ServiceAcount 생성 후 어드민 토큰을 발급하여 필요한 환경변수를 추가한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/ad30e6be-693b-42e8-be61-066f3c22a257)
4. ECR에 이미지가 배포되었는지 확인.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/d989dada-8f28-4caf-b376-b4e7ebdd5604)
6. git repository에서 push 작업 수행 후 정상적으로 자동 배포되는지 확인.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/11c2ad4f-6ae7-4ae0-a617-856587f19fb8)
7. S3에 Pipeline cache 한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/f819c477-40fb-4564-8d05-ec9d3f6fd6e8)

### 컨테이너 자동확장 - HPA

- 유저가 급증하는 상황을 대비하여 ChatBot 서비스에 대한 replica를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려주게 지지정하였다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/7d6b06d5-24a6-4f9b-9b75-8c86607e8a49)
deployment.yaml에도 CPU 스펙을 추가하고 재배포한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/d0a51f66-d3f0-4980-ab3e-b8f814faf4ae)
pod가 확장되며, CPU가 늘어난 것을 확인 할 수 있다.
```
gitpod /workspace/chatbot3 (main) $ kubectl get hpa
NAME   REFERENCE         TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
chat   Deployment/chat   6%/15%    1         10        10         15m
```

## ConfigMap
- 환경설정 정보를 바꾸기 위해 전체 CI/CD 파이프라인을 태워 배포하는 것을 방지하기 위해 ConfgMap을 생성한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/05ea50f6-b855-4101-919b-6a629fa8a262)

## PVC활용
- Amazon EFS를 활용하여 스토리지 클래스를 생성한다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/b74a7fb4-4f43-4011-83c3-51630c396fea)
![image](https://github.com/yidaeun39/chatbot/assets/47437659/4b8e6f73-86b2-45ff-a1b6-2888db5168d3)

## Rediness 무정지 재배포
- yaml에 readinessProbe 설정 후 seige로 배포작업 도중 워크로드를 모니터링 함으로서, Availability가 떨어지지 않고 100% 유지됐는지 확인한다. 
![image](https://github.com/yidaeun39/chatbot/assets/47437659/dc242394-7a04-4d95-9716-5d4e0703676d)

## 서비스 메쉬
- Istio를 통해 안정된 서비스와 배포전략을 시행한다. Istio를 통해 배포된 파드는 sidecar가 injection되어 POD가 (2/2)로 노출되는 것을 확인할 수 있다.
![image](https://github.com/yidaeun39/chatbot/assets/47437659/4b21d230-a49a-484c-8ed3-a32a8d2a8815)

## Loggregation
- 키바나 로그인을 위해서 ID/PW 정보를 미리 수집해둔다.
```
id : elastic
pw : kubectl get secrets --namespace=logging elasticsearch-master-credentials -ojsonpath='{.data.password}' | base64 -d
```
![image](https://github.com/yidaeun39/chatbot/assets/47437659/6a7e3c64-8051-47d5-9b6f-fcb43ad30807)
