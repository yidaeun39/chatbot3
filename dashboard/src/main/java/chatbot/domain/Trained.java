package chatbot.domain;

import chatbot.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class Trained extends AbstractEvent {

    private Long id;
    private String productId;
    private Integer questionCnt;
    private Integer requestCnt;
    private String trainId;
}
