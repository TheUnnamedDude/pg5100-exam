package no.westerdals.news.ejb;

import no.westerdals.news.entities.Comment;
import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CommentEJBTest extends BaseEJBTest {

    @Test
    public void testCreateComment() throws Exception {
        User user = createUser("CreateComment");
        Post post = postEJB.createPost(user.getUserId(), "Post!");
        Comment comment = commentEJB.createComment(post.getId(), user.getUserId(), "Comment!");
        assertNotNull(comment.getId());
        assertFalse(comment.isModerated());
    }

    @Test
    public void testCommentIsNotAPost() throws Exception {
        User user = createUser("CommentIsNotAPost");
        Post post = postEJB.createPost(user.getUserId(), "Post!");
        Comment comment = commentEJB.createComment(post.getId(), user.getUserId(), "UniqueComment" + System.currentTimeMillis());

        assertEquals(0, postEJB.getHighestScoredPosts(0).stream().filter(p -> p.getContent().equals(comment.getContent())).count());
    }
}
