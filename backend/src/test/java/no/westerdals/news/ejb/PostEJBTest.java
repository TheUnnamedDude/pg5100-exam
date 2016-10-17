package no.westerdals.news.ejb;

import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PostEJBTest extends BaseEJBTest {

    @Test
    public void testCreatePost() throws Exception {
        User user = userEJB.createUser("CreatePost", "Firstname", null, "Surname", "password");
        Post post = postEJB.createPost(user.getUserId(), "Quality content");
        assertNotNull(post.getId());
    }

    @Test
    public void testInvalidPostContent() throws Exception {
        User user = userEJB.createUser("InvalidPostContent", "Firstname", null, "Surname", "password");
        try {
            postEJB.createPost(user.getUserId(), "");
        } catch (Exception e) {
            return;
        }
        fail();
    }

    @Test
    public void testNullPostContent() throws Exception {
        User user = userEJB.createUser("NullPostContent", "Firstname", null, "Surname", "password");
        try {
            postEJB.createPost(user.getUserId(), null);
        } catch (Exception e) {
            return;
        }
        fail();
    }

    @Test
    public void testEqualUpAndDownVotes() throws Exception {
        User user = userEJB.createUser("EqualUpAndDownVotes1", "Firstname", null, "Surname", "password");
        User upvoter1 = userEJB.createUser("EqualUpAndDownVotes2", "Firstname", null, "Surname", "password");
        User upvoter2 = userEJB.createUser("EqualUpAndDownVotes3", "Firstname", null, "Surname", "password");

        Post post = postEJB.createPost(user.getUserId(), "Quality content");

        postEJB.upvote(post.getId(), upvoter1.getUserId());
        postEJB.downvote(post.getId(), upvoter2.getUserId());

        em.clear();

        Post fullyResolvedPost = postEJB.resolveFullPost(post.getId());
        assertEquals(2, fullyResolvedPost.getVotes().size());
        assertEquals(0, fullyResolvedPost.getScore().longValue());
    }

    @Test
    public void testUpvote() throws Exception {
        User user = userEJB.createUser("TestUpvote1", "Firstname", null, "Surname", "password");
        User upvoter1 = userEJB.createUser("TestUpvote2", "Firstname", null, "Surname", "password");
        User upvoter2 = userEJB.createUser("TestUpvote3", "Firstname", null, "Surname", "password");

        Post post = postEJB.createPost(user.getUserId(), "Quality content");

        postEJB.upvote(post.getId(), upvoter1.getUserId());
        postEJB.upvote(post.getId(), upvoter2.getUserId());

        em.clear();

        Post fullyResolvedPost = postEJB.resolveFullPost(post.getId());
        assertEquals(fullyResolvedPost.getVotes().size(), 2);
        assertEquals(fullyResolvedPost.getScore().longValue(), 2);
    }

    @Test
    public void testDownvote() throws Exception {
        User user = userEJB.createUser("TestDownvote1", "Firstname", null, "Surname", "password");
        User upvoter1 = userEJB.createUser("TestDownvote2", "Firstname", null, "Surname", "password");
        User upvoter2 = userEJB.createUser("TestDownvote3", "Firstname", null, "Surname", "password");

        Post post = postEJB.createPost(user.getUserId(), "Quality content");

        postEJB.downvote(post.getId(), upvoter1.getUserId());
        postEJB.downvote(post.getId(), upvoter2.getUserId());

        em.clear();

        Post fullyResolvedPost = postEJB.resolveFullPost(post.getId());
        assertEquals(fullyResolvedPost.getVotes().size(), 2);
        assertEquals(fullyResolvedPost.getScore().longValue(), -2);
    }

    @Test
    public void testRemoveVote() throws Exception {
        User user = userEJB.createUser("TestRemoveVote1", "Firstname", null, "Surname", "password");
        User upvoter1 = userEJB.createUser("TestRemoveVote2", "Firstname", null, "Surname", "password");
        User upvoter2 = userEJB.createUser("TestRemoveVote3", "Firstname", null, "Surname", "password");

        Post post = postEJB.createPost(user.getUserId(), "Quality content");

        postEJB.resetVote(post.getId(), upvoter2.getUserId());

        em.clear();

        Post fullyResolvedPost = postEJB.resolveFullPost(post.getId());
        assertEquals(fullyResolvedPost.getVotes().size(), 1);
        assertEquals(fullyResolvedPost.getScore().longValue(), 1);
    }
}
