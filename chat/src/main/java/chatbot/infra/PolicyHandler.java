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
    ChatRepository chatRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Refused'"
    )
    public void wheneverRefused_ChangeRequestType(@Payload Refused refused) {
        Refused event = refused;
        System.out.println(
            "\n\n##### listener ChangeRequestType : " + refused + "\n\n"
        );

        // Sample Logic //
        Chat.changeRequestType(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Patched'"
    )
    public void wheneverPatched_GetUserId(@Payload Patched patched) {
        Patched event = patched;
        System.out.println(
            "\n\n##### listener GetUserId : " + patched + "\n\n"
        );

        // Sample Logic //
        Chat.getUserId(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
