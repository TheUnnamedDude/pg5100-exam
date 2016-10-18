package no.westerdals.news.frontend;

import lombok.Getter;
import lombok.Setter;
import no.westerdals.news.ejb.UserEJB;
import no.westerdals.news.entities.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class AuthenticationController implements Serializable {
    @EJB
    private UserEJB userEJB;
    private String userId;
    @Getter
    @Setter
    private String loginUserId;
    @Getter
    @Setter
    private String loginPassword;

    public User getUser() {
        if (userId == null)
            return null;
        return userEJB.findUser(userId);
    }
}
