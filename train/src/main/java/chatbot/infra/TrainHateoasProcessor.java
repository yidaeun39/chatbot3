package chatbot.infra;

import chatbot.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class TrainHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Train>> {

    @Override
    public EntityModel<Train> process(EntityModel<Train> model) {
        return model;
    }
}
