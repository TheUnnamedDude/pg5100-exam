package no.westerdals.news.entities;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@ToString(doNotUseGetters = true)
@Entity
public class Comment extends Post {
    @ManyToOne
    private Post parent;
    private boolean moderated;
}
