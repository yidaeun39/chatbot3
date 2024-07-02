package chatbot.domain;

import chatbot.domain.*;
import chatbot.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class Trained extends AbstractEvent {

    private Long id;
    private String productId;
    private Integer questionCnt;
    private Integer requestCnt;
    private String trainId;
}
