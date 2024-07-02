package chatbot.domain;

import chatbot.domain.*;
import chatbot.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Patched extends AbstractEvent {

    private Long id;
    private String productId;
    private String marketerId;
    private String userId;

    public Patched(Marketing aggregate) {
        super(aggregate);
    }

    public Patched() {
        super();
    }
}
//>>> DDD / Domain Event
