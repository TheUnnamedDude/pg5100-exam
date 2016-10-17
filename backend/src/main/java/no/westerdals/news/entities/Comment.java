package no.westerdals.news.entities;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Entity
public class Comment extends Post {
    @ManyToOne
    private Post parent;
    private boolean moderated;
}
