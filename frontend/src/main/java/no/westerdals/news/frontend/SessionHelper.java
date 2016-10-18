package no.westerdals.news.frontend;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionHelper {
    public static HttpSession getSession(boolean createIfNotExists) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return (HttpSession) externalContext.getSession(createIfNotExists);
    }
}
