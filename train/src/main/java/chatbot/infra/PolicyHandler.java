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
    TrainRepository trainRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Chated'"
    )
    public void wheneverChated_SearchChat(@Payload Chated chated) {
        Chated event = chated;
        System.out.println(
            "\n\n##### listener SearchChat : " + chated + "\n\n"
        );

        // Sample Logic //
        Train.searchChat(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Questioned'"
    )
    public void wheneverQuestioned_QuestionChat(
        @Payload Questioned questioned
    ) {
        Questioned event = questioned;
        System.out.println(
            "\n\n##### listener QuestionChat : " + questioned + "\n\n"
        );

        // Sample Logic //
        Train.questionChat(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Requested'"
    )
    public void wheneverRequested_RequestChat(@Payload Requested requested) {
        Requested event = requested;
        System.out.println(
            "\n\n##### listener RequestChat : " + requested + "\n\n"
        );

        // Sample Logic //
        Train.requestChat(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
