package chatbot.infra;

import chatbot.config.kafka.KafkaProcessor;
import chatbot.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class DashboardViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private DashboardRepository dashboardRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenTrained_then_CREATE_1(@Payload Trained trained) {
        try {
            if (!trained.validate()) return;

            // view 객체 생성
            Dashboard dashboard = new Dashboard();
            // view 객체에 이벤트의 Value 를 set 함
            dashboard.setProductId(trained.getProductId());
            dashboard.setQuestionCnt(trained.getQuestionCnt());
            dashboard.setRequestCnt(trained.getRequestCnt());
            // view 레파지 토리에 save
            dashboardRepository.save(dashboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPatched_then_UPDATE_1(@Payload Patched patched) {
        try {
            if (!patched.validate()) return;
            // view 객체 조회

            List<Dashboard> dashboardList = dashboardRepository.findByProductId(
                patched.getProductId()
            );
            for (Dashboard dashboard : dashboardList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                dashboard.setMarketerId(patched.getMarketerId());
                // view 레파지 토리에 save
                dashboardRepository.save(dashboard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
