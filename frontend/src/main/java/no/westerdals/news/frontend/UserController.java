package no.westerdals.news.frontend;

import lombok.Getter;
import lombok.Setter;
import no.westerdals.news.ejb.UserEJB;
import no.westerdals.news.entities.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class UserController {
    @EJB
    private UserEJB userEJB;

    @Getter
    @Setter
    private String lookupUserId;

    public User getUserDetails() {
        return userEJB.findUser(lookupUserId);
    }
}
