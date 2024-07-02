package chatbot.domain;

import chatbot.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class Patched extends AbstractEvent {

    private Long id;
    private String productId;
    private String marketerId;
    private String userId;
}
