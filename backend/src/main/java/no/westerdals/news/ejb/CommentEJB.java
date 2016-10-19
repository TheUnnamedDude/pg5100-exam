package no.westerdals.news.ejb;

import no.westerdals.news.entities.Comment;
import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

@Stateless
@Transactional
public class CommentEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PostEJB postEJB;
    @EJB
    private UserEJB userEJB;

    public Comment createComment(Long parentId, String userId, String content) {
        Post post = postEJB.findPost(parentId);
        User user = userEJB.findUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username.");
        }

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(content);
        comment.setParent(post);
        comment.setCreated(new Date());
        comment.setScore(0L);
        post.getComments().add(comment);
        user.getPosts().add(comment);

        em.persist(comment);
        return comment;
    }

    public Comment findComment(Long commentId) {
        return em.find(Comment.class, commentId);
    }

    public Comment setModerated(Long commentId, boolean moderated) {
        Comment comment = findComment(commentId);
        comment.setModerated(moderated);

        postEJB.updateScore(comment);

        return comment;
    }
}
