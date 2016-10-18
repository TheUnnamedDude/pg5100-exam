package no.westerdals.news.ejb;

import no.westerdals.news.entities.Comment;
import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

// Didnt feel like this should be in the user test as we don't really have any logic that messes with posts there
@RunWith(Arquillian.class)
public class KarmaTest extends BaseEJBTest {

    @Test
    public void testKarmaForOnePostWithUpvote() throws Exception {
        User user = userEJB.createUser("OnePostWithUpvote1", "Firstname", null, "Surname", "passsword");
        User upvoter = userEJB.createUser("OnePostWithUpvote2", "Firstname", null, "Surname", "password");
        Post post = postEJB.createPost(user.getUserId(), "Post");

        postEJB.upvote(post.getId(), upvoter.getUserId());

        User found = userEJB.findUser(user.getUserId());

        assertEquals(1, found.getKarmaPoints().longValue());
    }

    @Test
    public void testNegativeKarmaForOnePostWithDownvote() throws Exception {
        User user = userEJB.createUser("OnePostWithDownvote1", "Firstname", null, "Surname", "password");
        User downvoter = userEJB.createUser("OnePostWithDownvote2", "Firstname", null, "Surname", "password");

        Post post = postEJB.createPost(user.getUserId(), "Repost");
        postEJB.downvote(post.getId(), downvoter.getUserId());

        User found = userEJB.findUser(user.getUserId());

        assertEquals(-1, found.getKarmaPoints().longValue());
    }

    @Test
    public void testNegativeKarmaForModeratedPost() throws Exception {
        User user = createUser("KarmaForModeratedPost");

        Post post = postEJB.createPost(user.getUserId(), "Post");
        Comment comment = commentEJB.createComment(post.getId(), user.getUserId(), "Oops");
        commentEJB.setModerated(comment.getId(), true);

        User found = userEJB.findUser(user.getUserId());
        assertEquals(-10, found.getKarmaPoints().longValue());
    }

    @Test
    public void testKarmaWithModeration() throws Exception {
        User user1 = createUser("KarmaWithModeration1");

        Post post = postEJB.createPost(user1.getUserId(), "Post");
        Comment comment1 = commentEJB.createComment(post.getId(), user1.getUserId(), "Comment1");
        Comment comment2 = commentEJB.createComment(post.getId(), user1.getUserId(), "Comment2");
        Comment comment3 = commentEJB.createComment(post.getId(), user1.getUserId(), "Comment3");
        Comment comment4 = commentEJB.createComment(post.getId(), user1.getUserId(), "Comment4");

        postEJB.downvote(post.getId(), user1.getUserId());
        postEJB.upvote(comment1.getId(), user1.getUserId());
        postEJB.upvote(comment2.getId(), user1.getUserId());
        commentEJB.setModerated(comment3.getId(), true);
        commentEJB.setModerated(comment4.getId(), true);

        User user2 = createUser("KarmaWithModeration2");

        postEJB.upvote(comment2.getId(), user2.getUserId());

        User found = userEJB.findUser(user1.getUserId());
        assertEquals(-18, found.getKarmaPoints().longValue());

    }
}
