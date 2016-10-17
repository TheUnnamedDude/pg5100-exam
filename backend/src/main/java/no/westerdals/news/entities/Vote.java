package no.westerdals.news.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class Vote {
    @GeneratedValue
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;
    private boolean upvote;
}
