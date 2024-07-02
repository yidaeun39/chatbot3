package chatbot.domain;

import chatbot.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "trains", path = "trains")
public interface TrainRepository
    extends PagingAndSortingRepository<Train, Long> {}
