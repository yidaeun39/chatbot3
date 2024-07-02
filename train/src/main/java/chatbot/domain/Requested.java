package chatbot.domain;

import chatbot.domain.*;
import chatbot.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class Requested extends AbstractEvent {

    private Long id;
    private String productId;
    private String questionMsg;
    private String requestMsg;
    private String requestType;
    private String userId;
}
