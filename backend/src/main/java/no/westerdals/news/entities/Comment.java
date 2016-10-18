package no.westerdals.news.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Comment extends Post {
    @ManyToOne
    private Post parent;
    private boolean moderated;
}
