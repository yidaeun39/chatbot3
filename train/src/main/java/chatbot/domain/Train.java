package chatbot.domain;

import chatbot.TrainApplication;
import chatbot.domain.Accepted;
import chatbot.domain.Refused;
import chatbot.domain.Trained;
import chatbot.domain.Trained;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Train_table")
@Data
//<<< DDD / Aggregate Root
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;

    private Integer questionCnt;

    private Integer requestCnt;

    private String trainId;

    @PostPersist
    public void onPostPersist() {
        Accepted accepted = new Accepted(this);
        accepted.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        Refused refused = new Refused(this);
        refused.publishAfterCommit();

        Trained trained = new Trained(this);
        trained.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        Trained trained = new Trained(this);
        trained.publishAfterCommit();
    }

    public static TrainRepository repository() {
        TrainRepository trainRepository = TrainApplication.applicationContext.getBean(
            TrainRepository.class
        );
        return trainRepository;
    }

    //<<< Clean Arch / Port Method
    public static void searchChat(Chated chated) {
        //implement business logic here:

        /** Example 1:  new item 
        Train train = new Train();
        repository().save(train);

        */

        /** Example 2:  finding and process
        
        repository().findById(chated.get???()).ifPresent(train->{
            
            train // do something
            repository().save(train);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void questionChat(Questioned questioned) {
        //implement business logic here:

        /** Example 1:  new item 
        Train train = new Train();
        repository().save(train);

        Trained trained = new Trained(train);
        trained.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(questioned.get???()).ifPresent(train->{
            
            train // do something
            repository().save(train);

            Trained trained = new Trained(train);
            trained.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void requestChat(Requested requested) {
        //implement business logic here:

        /** Example 1:  new item 
        Train train = new Train();
        repository().save(train);

        Refused refused = new Refused(train);
        refused.publishAfterCommit();
        Accepted accepted = new Accepted(train);
        accepted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(requested.get???()).ifPresent(train->{
            
            train // do something
            repository().save(train);

            Refused refused = new Refused(train);
            refused.publishAfterCommit();
            Accepted accepted = new Accepted(train);
            accepted.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
