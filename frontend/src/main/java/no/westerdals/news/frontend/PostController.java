package no.westerdals.news.frontend;

import lombok.Getter;
import lombok.Setter;
import no.westerdals.news.ejb.PostEJB;
import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.Vote;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
@Named
public class PostController {
    @EJB
    private PostEJB postEJB;

    @Setter
    @Getter
    private String newPostContent;

    public List<Post> getTopPosts() {
        List<Post> posts = postEJB.getHighestScoredPosts(0);
        HttpSession session = SessionHelper.getSession(false);
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            posts.forEach(p -> {
                Optional<Vote> vote = p.getVotes().stream().filter(v -> v.getUser().getUserId().equals(userId)).findAny();
                if (vote.isPresent()) {
                    p.setSelfVote(vote.get().isUpvote() ? 1L : -1L);
                } else {
                    p.setSelfVote(0L);
                }
            });
        }
        return posts;
    }

    public String createPost() {
        HttpSession session = SessionHelper.getSession(false);
        if (session == null) {
            return "index.jsf";
        }

        postEJB.createPost((String) session.getAttribute("userId"), newPostContent);
        return "index.jsf";
    }

    public String changeVote(Post post) {
        HttpSession session = SessionHelper.getSession(false);
        if (post == null || session == null) {
            return "index.jsf";
        }
        System.out.println("Changing vote for " + post.getId());

        String userId = (String) session.getAttribute("userId");
        if (post.getSelfVote() > 0) {
            postEJB.upvote(post.getId(), userId);
        } else if (post.getSelfVote() < 0) {
            postEJB.downvote(post.getId(), userId);
        } else {
            postEJB.resetVote(post.getId(), userId);
        }
        return "index.jsf";
    }
}
