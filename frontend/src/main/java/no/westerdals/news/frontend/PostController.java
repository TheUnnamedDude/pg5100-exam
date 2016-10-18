package no.westerdals.news.frontend;

import no.westerdals.news.ejb.PostEJB;
import no.westerdals.news.entities.Post;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;

@ApplicationScoped
@Named
public class PostController {
    @EJB
    private PostEJB postEJB;

    public List<Post> getTopPosts() {
        return postEJB.getHighestScoredPosts(0);
    }
}
