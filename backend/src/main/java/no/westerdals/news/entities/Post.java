package no.westerdals.news.entities;

import lombok.Getter;
import lombok.Setter;

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
    @NotNull
    @Size(min = 2, max = 1024)
    private String content;
    @OneToMany
    public List<Vote> votes;
    private Date created;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;
    private Long score;
}
