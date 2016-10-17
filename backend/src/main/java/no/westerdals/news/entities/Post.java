package no.westerdals.news.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Post {
    @GeneratedValue
    @Id
    private Long id;
    @NotNull
    @ManyToOne
    private User author;
    @Size(min = 2, max = 1024)
    private String content;
    @ManyToMany
    private List<User> upvotes;
    @ManyToMany
    private List<User> downvotes;
    private Date created;
    @OneToMany
    private List<Comment> comments;
}
