package chatbot.domain;

import chatbot.TrainApplication;
import chatbot.domain.Accepted;
import chatbot.domain.Refused;
import chatbot.domain.Trained;
import chatbot.domain.Trained;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Train_table")
@Data
//<<< DDD / Aggregate Root
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;

    private Integer questionCnt;

    private Integer requestCnt;

    private String trainId;

    @PostPersist
    public void onPostPersist() {
        Accepted accepted = new Accepted(this);
        accepted.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        Refused refused = new Refused(this);
        refused.publishAfterCommit();

        Trained trained = new Trained(this);
        trained.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        Trained trained = new Trained(this);
        trained.publishAfterCommit();
    }

    public static TrainRepository repository() {
        TrainRepository trainRepository = TrainApplication.applicationContext.getBean(
            TrainRepository.class
        );
        return trainRepository;
    }

    public static void searchChat(Chated chated) {
        // 검색과 동시에 train 데이터 생성
        Train train = new Train();
        train.setId(chated.getId());
        train.setProductId(chated.getProductId());
        repository().save(train);
    }


    public static void questionChat(Questioned questioned) {
        repository().findById(Long.valueOf(questioned.getId())).ifPresentOrElse(train->{

            // 현재 질문 카운트에 수에 + 1
            train.setQuestionCnt(train.getQuestionCnt() + 1);
            repository().save(train);
        }, 
        ()->{
            Train train = new Train();
            train.setId(questioned.getId());
            train.setProductId(questioned.getProductId());
            train.setQuestionCnt(1);
            repository().save(train);
        });
    }

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

}
//>>> DDD / Aggregate Root
