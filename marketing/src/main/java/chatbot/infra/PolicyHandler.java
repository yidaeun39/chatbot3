package chatbot.infra;

import chatbot.config.kafka.KafkaProcessor;
import chatbot.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    MarketingRepository marketingRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Trained'"
    )
    public void wheneverTrained_TrainData(@Payload Trained trained) {
        Trained event = trained;
        System.out.println(
            "\n\n##### listener TrainData : " + trained + "\n\n"
        );

        // Sample Logic //
        Marketing.trainData(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
