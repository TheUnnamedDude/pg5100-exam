package no.westerdals.news.frontend;

import lombok.Getter;
import lombok.Setter;
import no.westerdals.news.ejb.UserEJB;
import no.westerdals.news.entities.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@SessionScoped
@Named
public class AuthenticationController implements Serializable {

    @EJB
    private UserEJB userEJB;

    @Getter
    @Setter
    private String loginUserId;
    @Getter
    @Setter
    private String loginPassword;

    @Getter
    @Setter
    private String registerUsername;
    @Getter
    @Setter
    private String registerFirstName;
    @Getter
    @Setter
    private String registerMiddleName;
    @Getter
    @Setter
    private String registerSurname;
    @Getter
    @Setter
    private String registerPassword;
    @Getter
    @Setter
    private String registerPasswordConfirmation;

    @Getter
    @Setter
    private String newPostContent;

    public User getUser() {
        HttpSession session = SessionHelper.getSession(false);
        if (session == null)
            return null;
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            return null;
        return userEJB.findUser(userId);
    }

    public String authenticate() {
        if (loginUserId == null || loginPassword == null
                || loginUserId.trim().isEmpty() || loginPassword.trim().isEmpty()) {
            return "login.jsf";
        }
        User user = userEJB.validatePassword(loginUserId, loginPassword);
        if (user != null) {
            HttpSession session = SessionHelper.getSession(true);
            session.setAttribute("userId", user.getUserId());
            return "index.jsf";
        }
        return "login.jsf";
    }

    public String registerUser() {
        if (registerMiddleName.trim().isEmpty()) {
            registerMiddleName = null;
        }
        if (!registerPassword.equals(registerPasswordConfirmation)) {
            return "register.jsf";
        }

        User user = userEJB.createUser(registerUsername, registerFirstName, registerMiddleName,
                registerSurname, registerPassword);
        if (user != null) {
            HttpSession session = SessionHelper.getSession(true);
            session.setAttribute("userId", user.getUserId());
            return "index.jsf";
        }
        return "register.jsf";
    }

    public String invalidate() {
        SessionHelper.getSession(false).invalidate();
        return "index.jsf";
    }
}
