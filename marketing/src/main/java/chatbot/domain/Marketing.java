package chatbot.domain;

import chatbot.MarketingApplication;
import chatbot.domain.Patched;
import chatbot.domain.Recommended;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Marketing_table")
@Data
//<<< DDD / Aggregate Root
public class Marketing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;

    private String marketerId;

    private String userId;

    @PostPersist
    public void onPostPersist() {
        Patched patched = new Patched(this);
        patched.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        Recommended recommended = new Recommended(this);
        recommended.publishAfterCommit();
    }

    public static MarketingRepository repository() {
        MarketingRepository marketingRepository = MarketingApplication.applicationContext.getBean(
            MarketingRepository.class
        );
        return marketingRepository;
    }

    //<<< Clean Arch / Port Method
    public static void trainData(Trained trained) {
        //implement business logic here:

        /** Example 1:  new item 
        Marketing marketing = new Marketing();
        repository().save(marketing);

        */

        /** Example 2:  finding and process
        
        repository().findById(trained.get???()).ifPresent(marketing->{
            
            marketing // do something
            repository().save(marketing);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
