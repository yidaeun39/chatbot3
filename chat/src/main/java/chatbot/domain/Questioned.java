package chatbot.domain;

import chatbot.domain.*;
import chatbot.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Questioned extends AbstractEvent {

    private Long id;
    private String productId;
    private String questionMsg;
    private String requestMsg;
    private String requestType;
    private String userId;

    public Questioned(Chat aggregate) {
        super(aggregate);
    }

    public Questioned() {
        super();
    }
}
//>>> DDD / Domain Event
