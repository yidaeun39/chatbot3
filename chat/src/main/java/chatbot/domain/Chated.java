package chatbot.domain;

import chatbot.domain.*;
import chatbot.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Chated extends AbstractEvent {

    private Long id;
    private String productId;
    private String questionMsg;
    private String requestMsg;
    private String requestType;
    private String userId;

    public Chated(Chat aggregate) {
        super(aggregate);
    }

    public Chated() {
        super();
    }
}
//>>> DDD / Domain Event
