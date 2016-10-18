package no.westerdals.news.ejb;

import no.westerdals.news.entities.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseEJBTest {
    @EJB
    UserEJB userEJB;

    @EJB
    PostEJB postEJB;

    @EJB
    CommentEJB commentEJB;

    @PersistenceContext
    EntityManager em;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.westerdals.news")
                .addAsResource("META-INF/persistence.xml");
    }

    User createUser(String username) {
        return userEJB.createUser(username, "Firstname", null, "Surname", "password");
    }
}
