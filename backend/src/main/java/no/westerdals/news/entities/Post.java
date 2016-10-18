package no.westerdals.news.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = Post.GET_ORDERED_BY_TIME,
                query = "select p from Post as p order by p.created desc"
        ),
        @NamedQuery(
                name = Post.GET_ORDERED_BY_SCORE,
                query = "select p from Post as p order by p.score desc"
        ),
})
@Getter
@Setter
@Entity
public class Post {
    public static final String GET_ORDERED_BY_TIME = "Post#getOrderedByTime";
    public static final String GET_ORDERED_BY_SCORE = "Post#getOrderedByScore";
    public static final String GET_POSTS = "Post#getPosts";

    @Setter(AccessLevel.NONE)
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
