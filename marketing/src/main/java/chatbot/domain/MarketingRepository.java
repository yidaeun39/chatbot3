package chatbot.domain;

import chatbot.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "marketings",
    path = "marketings"
)
public interface MarketingRepository
    extends PagingAndSortingRepository<Marketing, Long> {}
