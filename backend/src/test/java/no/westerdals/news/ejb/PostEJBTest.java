package no.westerdals.news.ejb;

import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
        assertEquals(2, fullyResolvedPost.getVotes().size());
        assertEquals(2, fullyResolvedPost.getScore().longValue());
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
        assertEquals(2, fullyResolvedPost.getVotes().size());
        assertEquals(-2, fullyResolvedPost.getScore().longValue());
    }

    @Test
    public void testRemoveVote() throws Exception {
        User user = userEJB.createUser("TestRemoveVote1", "Firstname", null, "Surname", "password");
        User upvoter1 = userEJB.createUser("TestRemoveVote2", "Firstname", null, "Surname", "password");
        User upvoter2 = userEJB.createUser("TestRemoveVote3", "Firstname", null, "Surname", "password");

        Post post = postEJB.createPost(user.getUserId(), "Quality content");

        postEJB.upvote(post.getId(), upvoter1.getUserId());
        postEJB.upvote(post.getId(), upvoter2.getUserId());

        postEJB.resetVote(post.getId(), upvoter2.getUserId());

        em.clear();

        Post fullyResolvedPost = postEJB.resolveFullPost(post.getId());
        assertEquals(1, fullyResolvedPost.getVotes().size());
        assertEquals(1, fullyResolvedPost.getScore().longValue());
    }

    @Test
    public void getNewestPosts() throws Exception {
        User user = userEJB.createUser("NewestPost", "Firstname", null, "Surname", "password");
        Thread.sleep(2000); // Make sure the next post is newer then whats inserted before
        Post post1 = postEJB.createPost(user.getUserId(), "Post1");
        Thread.sleep(2000);
        Post post2 = postEJB.createPost(user.getUserId(), "Post2");

        List<Post> newestPosts = postEJB.getNewestPosts(0);
        assertEquals(post2.getId().longValue(), newestPosts.get(0).getId().longValue());
        assertEquals(post1.getId().longValue(), newestPosts.get(1).getId().longValue());
    }

    @Test
    public void testGetHighestScoredPosts() throws Exception {
        User[] users = new User[20];
        for (int i = 0; i < users.length; i++) {
            users[i] = userEJB.createUser("HighestScoredPosts" + (i + 1), "Firstname", null, "Surname", "password");
        }
        Post highest = postEJB.createPost(users[0].getUserId(), "Top post!");
        upvote(highest, users, users.length);
        Post high = postEJB.createPost(users[0].getUserId(), "Second top post!");
        upvote(high, users, 15);
        Post low = postEJB.createPost(users[0].getUserId(), "....");
        upvote(low, users, 5);
        Post minus = postEJB.createPost(users[1].getUserId(), "pls gief karma");
        postEJB.downvote(minus.getId(), users[0].getUserId());

        List<Post> topPosts = postEJB.getHighestScoredPosts(0);
        assertEquals(highest.getId().longValue(), topPosts.get(0).getId().longValue());
        assertEquals(high.getId().longValue(), topPosts.get(1).getId().longValue());

        long lastValue = Long.MAX_VALUE;
        // Make sure its correctly ordered
        for (Post post : topPosts) {
            assertTrue(lastValue >= post.getScore());
            lastValue = post.getScore();
        }
    }

    private void upvote(Post post, User[] users, int numberOfUpvotes) {
        assertTrue("You just failed to create a test!", users.length >= numberOfUpvotes && numberOfUpvotes > 0);
        for (int i = 0; i < numberOfUpvotes; i++) {
            postEJB.upvote(post.getId(), users[i].getUserId());
        }
    }
}
