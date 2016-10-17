package no.westerdals.news.ejb;

import no.westerdals.news.entities.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest extends BaseEJBTest {

    @Test
    public void testCreateUser() throws Exception {
        User user = userEJB.createUser("CreateUser", "Firstname", "Middlename", "Surname", "password");
        assertNotNull(user.getUserId());
        assertNotNull(user.getHashedPassword());
        assertNotNull(user.getSalt());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getMiddleName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getRegistrationDate());
    }

    @Test
    public void testCreateUserDuplicateUserId() throws Exception {
        User user = userEJB.createUser("DuplicateUserId", "Firstname", null, "Surname", "password");
        try {
            userEJB.createUser(user.getUserId(), "Firstname", null, "Surname", "password");
        } catch (Exception e) {
            return;
        }
        fail();
    }

    // TODO: Test constraints

    @Test
    public void testGenerateSalt() throws Exception {
        byte[] salt1 = userEJB.generateSalt();
        byte[] salt2 = userEJB.generateSalt();
        assertNotEquals(Arrays.asList(salt1), Arrays.asList(salt2));
    }

    @Test
    public void testHashNotEqualResult() throws Exception {
        String hashedPassword1 = userEJB.hashPassword("TestPassword", userEJB.generateSalt());
        String hashedPassword2 = userEJB.hashPassword("TestPassword", userEJB.generateSalt());
        assertNotEquals(hashedPassword1, hashedPassword2);
    }

    @Test
    public void testNotEqualsSameSalt() throws Exception {
        byte[] salt = userEJB.generateSalt();
        String hashedPassword1 = userEJB.hashPassword("TestPassword1", salt);
        String hashedPassword2 = userEJB.hashPassword("TestPassword2", salt);
        assertNotEquals(hashedPassword1, hashedPassword2);
    }

    @Test
    public void testValidatePassword() throws Exception {
        User user = userEJB.createUser("ValidatePassword", "Firstname", null, "Surname", "password");

        em.clear();

        User authenticated = userEJB.validatePassword(user.getUserId(), "password");
        assertNotNull(authenticated);
    }

    @Test
    public void testReturnNullOnEmptyPassword() throws Exception {
        User user = userEJB.createUser("NullOnEmptyPassword", "Firstname", null, "Surname", "password");
        assertNull(userEJB.validatePassword(user.getUserId(), ""));
    }

    @Test
    public void testReturnNullOnInvalidUsername() throws Exception {
        assertNull(userEJB.validatePassword("NullOnInvalidUsername", "password"));
    }

    @Test
    public void testReturnNullOnInvalidPassword() throws Exception {
        User user = userEJB.createUser("NullOnInvalidPassword", "Firstname", null, "Surname", "password");
        assertNull(userEJB.validatePassword(user.getUserId(), "invalid"));
    }
}
