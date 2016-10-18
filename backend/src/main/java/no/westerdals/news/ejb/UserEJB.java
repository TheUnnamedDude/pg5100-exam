package no.westerdals.news.ejb;

import no.westerdals.news.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Transactional
@Stateless
public class UserEJB {
    public static final int SALT_SIZE = 32;

    @PersistenceContext
    private EntityManager em;

    public User createUser(@NotNull String username, @NotNull String firstName, String middleName,
                           @NotNull String surname, @NotNull String password) {
        if (findUser(username) != null) {
            // Avoid doing expensive operations if the user exists
            throw new IllegalArgumentException("Username already exists");
        }
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        User user = new User();
        user.setUserId(username);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setSurname(surname);
        user.setHashedPassword(hashedPassword);
        user.setRegistrationDate(new Date());
        user.setSalt(Base64.getEncoder().encodeToString(salt));
        user.setKarmaPoints(0L);

        em.persist(user);

        return user;
    }

    /**
     * Find a user by its name, returns null if none is found
     * @param username the name to look for
     * @return a user or if none were found null
     */
    public User findUser(String username) {
        List<User> user =  em.createNamedQuery(User.GET_BY_USERNAME, User.class)
                .setParameter("userId", username)
                .setMaxResults(1)
                .getResultList();
        if (user.isEmpty()) {
            return null;
        }
        return user.get(0);
    }

    public User validatePassword(String username, String password) {
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        User user = findUser(username);
        if (user == null) {
            return null;
        }
        byte[] salt = Base64.getDecoder().decode(user.getSalt());
        if (user.getHashedPassword().equals(hashPassword(password, salt))) {
            return user;
        }
        return null;
    }

    public String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(password.getBytes("UTF-8"));
            sha256.update(salt);
            return new String(sha256.digest(), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            // After java 6 SHA256 should be guaranteed, we only run on java 8 so we'll ignore this error
        } catch (UnsupportedEncodingException e) {
            // UTF-8 should also be supported.
        }
        return null;
    }

    public byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return salt;
    }

}
