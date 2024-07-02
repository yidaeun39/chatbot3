package chatbot.domain;

import chatbot.domain.*;
import chatbot.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Refused extends AbstractEvent {

    private Long id;
    private String productId;
    private Integer questionCnt;
    private Integer requestCnt;
    private String trainId;

    public Refused(Train aggregate) {
        super(aggregate);
    }

    public Refused() {
        super();
    }
}
//>>> DDD / Domain Event
