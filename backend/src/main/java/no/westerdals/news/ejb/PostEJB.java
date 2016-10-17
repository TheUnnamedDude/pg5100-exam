package no.westerdals.news.ejb;

import no.westerdals.news.entities.Post;
import no.westerdals.news.entities.User;
import no.westerdals.news.entities.Vote;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class PostEJB {
    @PersistenceContext
    private EntityManager em;

    @EJB UserEJB userEJB;

    public Post createPost(String userId, String content) {
        User user = userEJB.findUser(userId);
        Post post = new Post();
        post.setAuthor(user);
        post.setContent(content);
        post.setCreated(new Date());
        post.setScore(0L);
        user.getPosts().add(post);
        em.persist(post);
        return post;
    }

    public Post changeVote(Long postId, String userId, boolean upvote) {
        Post post = findPost(postId);

        if (post.getId() == null) {
            throw new IllegalArgumentException("Post not found.");
        }

        User user = userEJB.findUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Username not found.");
        }

        post.getVotes().removeIf(vote -> vote.getUser().getUserId().equals(user.getUserId()));
        Vote vote = new Vote();
        vote.setUpvote(upvote);
        vote.setUser(user);
        post.getVotes().add(vote);
        updateScore(post);
        em.persist(vote);
        return post;
    }

    public Post upvote(Long postId, String userId) {
        return changeVote(postId, userId, true);
    }

    public Post downvote(Long postId, String userId) {
        return changeVote(postId, userId, false);
    }

    public Post resetVote(Long postId, String userId) {
        Post post = findPost(postId);
        if (post == null) {
            throw new IllegalArgumentException("Could not find post.");
        }

        User user = userEJB.findUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Could not find user.");
        }

        post.getVotes().removeIf(vote -> vote.getUser().getUserId().equals(user.getUserId()));
        updateScore(post);

        return post;
    }

    public List<Post> getHighestScoredPosts(int page) {
        return getHighestScoredPosts(page * 20, 20);
    }

    public List<Post> getHighestScoredPosts(int start, int results) {
        TypedQuery<Post> query = em.createNamedQuery(Post.GET_ORDERED_BY_SCORE, Post.class);
        query.setFirstResult(start);
        query.setMaxResults(results);
        return query.getResultList();
    }

    public List<Post> getNewestPosts(int page) {
        return getNewestPosts(page * 20, 20);
    }

    public List<Post> getNewestPosts(int start, int results) {
        TypedQuery<Post> query = em.createNamedQuery(Post.GET_ORDERED_BY_TIME, Post.class);
        query.setFirstResult(start);
        query.setMaxResults(results);
        return query.getResultList();
    }

    public Post findPost(Long postId) {
        return em.find(Post.class, postId);
    }

    public Post resolveFullPost(Long postId) {
        Post post = findPost(postId);
        traversePost(post);
        return post;
    }

    public void updateScore(Post post) {
        post.setScore(post.getVotes().stream().mapToLong(v -> v.isUpvote() ? 1 : -1).sum());
    }

    public void traversePost(Post post) {
        post.getComments().forEach(this::traversePost);
        post.getVotes().size(); // Resolve the size of the votes
    }
}
