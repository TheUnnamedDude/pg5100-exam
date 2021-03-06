package no.westerdals.news.frontend;

import lombok.Getter;
import lombok.Setter;
import no.westerdals.news.ejb.CommentEJB;
import no.westerdals.news.ejb.PostEJB;
import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.Vote;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Named
public class PostController {
    @EJB
    private PostEJB postEJB;

    @EJB
    private CommentEJB commentEJB;

    @Setter
    @Getter
    private String newPostContent;

    @Getter
    @Setter
    private boolean orderByDate = false;

    @Getter
    @Setter
    private Long displayedPostId;

    public Post getPost() {
        return postEJB.resolveFullPost(displayedPostId);
    }

    public List<Post> getPosts() {
        List<Post> posts;
        if (orderByDate) {
            posts = postEJB.getNewestPosts(0);
        } else {
            posts = postEJB.getHighestScoredPosts(0);
        }
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

    public String createComment() {
        HttpSession session = SessionHelper.getSession(false);
        if (session == null) {
            return "show_post.jsf?postId=" + displayedPostId;
        }
        return "show_post.jsf?postId=" + displayedPostId;
    }

    public String changeVote(Long postId, Long voteValue) {
        HttpSession session = SessionHelper.getSession(false);
        Post post = postEJB.findPost(postId);
        if (post == null || session == null) {
            return "index.jsf";
        }

        String userId = (String) session.getAttribute("userId");
        if (voteValue > 0) {
            postEJB.upvote(post.getId(), userId);
        } else if (voteValue < 0) {
            postEJB.downvote(post.getId(), userId);
        } else {
            postEJB.resetVote(post.getId(), userId);
        }
        return "index.jsf";
    }

    public String refresh() {
        return "index.jsf";
    }

    public String cropForFrontpage(String in) {
        if (in.length() <= 30)
            return in;
        return in.substring(0, 26) + "...";
    }
}
