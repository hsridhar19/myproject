package com.sfr.sitemaster.integration.http;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sfr.sitemaster.api.unrestricted.passwordreset.RegisterResetRequest;
import com.sfr.sitemaster.api.unrestricted.passwordreset.ResetRequest;
import com.sfr.sitemaster.dao.mongo.PasswordResetTokenDAOImpl;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.PasswordResetToken;
import com.sfr.sitemaster.entities.User;
import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.apicore.http.Curl;
import com.sfr.apicore.pojo.exception.DBException;
import org.json.JSONException;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tuxbear on 08/01/15.
 */
public class PasswordResetTests extends IntegrationTestBase {

    @Inject
    PasswordResetTokenDAOImpl passwordResetTokenDao;

    @Inject
    UserService userService;

    @Inject
    Gson gson;

    @Test
    public void request_token_and_using_it_changes_password() throws DBException, URISyntaxException, JSONException, LoginException {
        createAndSaveUser();
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        final RegisterResetRequest registerResetRequest = new RegisterResetRequest();
        registerResetRequest.setUsername(DEFAULT_TEST_USER);
        final String REQUEST_INPUT = gson.toJson(registerResetRequest);

        assertEquals(200, curl.issueRequestWithHeaders("POST", "/api/v1/unres/password/reset-request", REQUEST_INPUT, "Content-Type: application/json"));

        final PasswordResetToken token = passwordResetTokenDao.findOneBasedOnField("username", DEFAULT_TEST_USER, true);

        assertNotNull(token);
        assertEquals(DEFAULT_TEST_USER, token.getUsername());

        final ResetRequest resetRequest = new ResetRequest();
        resetRequest.setNewPassword("password235/");
        resetRequest.setToken(token.getToken());

        final String RESET_INPUT = gson.toJson(resetRequest);

        assertEquals(200, curl.issueRequestWithHeaders("POST", "/api/v1/unres/password/reset", RESET_INPUT, "Content-type: application/json"));

        final User loggedInWithNewPassword = userService.login(DEFAULT_TEST_USER, resetRequest.getNewPassword());

        assertNotNull(loggedInWithNewPassword);
    }
}
