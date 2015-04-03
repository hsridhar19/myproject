package com.sfr.sitemaster.integration.db.domainservices;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.mongo.UserDAOImpl;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.User;
import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.apicore.commons.HashUtils;
import com.sfr.apicore.pojo.exception.DBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tuxbear on 13/01/15.
 */
public class UserServiceIntegrationTests extends IntegrationTestBase {

    private static String email = "boo@boo.com";
    private static String password = "foobar";
    private final String salt = HashUtils.salt();
    private final String hash = HashUtils.md5_salted("foobar", salt);

    @Inject
    private UserDAOImpl userDao;

    @Inject
    private UserService userService;

    @Before
    public void init() {
        userDao.dropCollection();
    }

    private User createEntity() throws DBException {
        final User pojo = new User();
        pojo.setName("jon");
        pojo.setEmail(email);
        pojo.setSalt(salt);
        pojo.setPassword(hash);

        final User user = userDao.save(pojo);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertNotEquals(password, user.getPassword());
        assertEquals(hash, user.getPassword());
        assertEquals(salt, user.getSalt());
        return user;
    }

    @Test
    public void loginSuccessTest() throws DBException, LoginException {
        final User user = createEntity();
        final User check = userService.login(email, password);
        assertNotNull(check);
        assertEquals(email, check.getEmail());
        assertNotEquals(password, check.getPassword());
        assertEquals(hash, check.getPassword());
        assertEquals(salt, check.getSalt());
        assertEquals(user, check);
    }

    @Test(expected = LoginException.class)
    public void loginUnsuccessfulTest_wrongpassword() throws DBException, LoginException {
        createEntity();
        userService.login(email, "bad password");
    }

    @Test(expected = LoginException.class)
    public void loginUnsuccessfulTest_wrongusername() throws DBException, LoginException {
        createEntity();
        userService.login("whatever", password);
    }

    @Test(expected = LoginException.class)
    public void loginUnsuccessfulTest_wrongusernameandpassword() throws DBException, LoginException {
        createEntity();
        userService.login("whatever", "boo");
    }

    @Test
    public void findingNonExistentUserReturnsNull() throws DBException {
        final User returnedValue = userDao.findOneBasedOnField("email", "this is not an email", true);
        Assert.assertNull(returnedValue);
    }
}
