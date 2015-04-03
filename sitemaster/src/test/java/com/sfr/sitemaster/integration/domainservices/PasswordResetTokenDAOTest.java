/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.domainservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.mongo.PasswordResetTokenDAOImpl;
import com.sfr.sitemaster.dao.mongo.UserDAOImpl;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.PasswordResetToken;
import com.sfr.sitemaster.entities.User;
import com.sfr.sitemaster.integration.db.dao.DaoTestBase;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * PasswordResetToken integration test. writes to db. so make sure tear down
 * occurs.
 *
 * @author
 *
 */
public class PasswordResetTokenDAOTest extends DaoTestBase<PasswordResetToken> {

    private static String token = "87h3f-23f2f3g4-43g34g34g-34gseæ@--";

    @Inject
    private UserDAOImpl userDao;

    @Inject
    private PasswordResetTokenDAOImpl resetTokenFacade;

    @Inject
    private UserService userService;

    @Before
    public void init() {
        userDao.dropCollection();
        resetTokenFacade.dropCollection();
    }

    private PasswordResetToken createEntity() throws DBException {
        final PasswordResetToken pojo = new PasswordResetToken();

        pojo.setToken(token);
        pojo.setUsername("oleandrejohansen@gmail.com");
        pojo.setValidUntil(DateUtils.addHours(new Date(), 10));

        return pojo;
    }

    @Test
    public void testResetPasswordForUser() throws DBException, LoginException {
        final User newUser = createAndSaveUser();
        assertEquals(1, userDao.count());
        final PasswordResetToken tokenObject = userService.requestPasswordReset(newUser.getEmail());
        final String newPassword = "my new password";
        userService.changePasswordWithToken(tokenObject.getToken(), newPassword);
        final User loggedInWithNewPassword = userService.login(newUser.getEmail(), newPassword);
        assertNotNull(loggedInWithNewPassword);
    }

    @Test(expected = CredentialNotFoundException.class)
    public void testNonExistingTokenThrowsWhenChangingPassword() throws DBException,
            CredentialExpiredException, CredentialNotFoundException {
        userService.changePasswordWithToken("this is not a token", "this is not a password");
    }

    @Test(expected = CredentialExpiredException.class)
    public void expiredTokensAreRejected() throws DBException, CredentialExpiredException,
            CredentialNotFoundException {
        final Date thirtyMinutesAgo = DateUtils.addMinutes(new Date(), -30);
        final PasswordResetToken expiredToken = new PasswordResetToken(thirtyMinutesAgo,
                "username@test.com");

        resetTokenFacade.save(expiredToken);
        userService.changePasswordWithToken(expiredToken.getToken(), "this should throw");
    }

    @Test
    public void oldExpiredTokensAreRemovedWhenCreatingToken() throws DBException,
            CredentialNotFoundException {
        final User createdUser = createAndSaveUser();
        final PasswordResetToken oldToken = new PasswordResetToken(new Date(),
                createdUser.getEmail());
        resetTokenFacade.save(oldToken);

        final PasswordResetToken newToken = userService
                .requestPasswordReset(createdUser.getEmail());

        assertNotNull(newToken);
        assertEquals(1, resetTokenFacade.count());
    }

    @Test
    public void basicSaveAndRetreivePasswordResetToken() throws DBException {
        assertEquals(0, resetTokenFacade.count());
        final PasswordResetToken entity = createEntity();
        resetTokenFacade.save(entity);
        assertEquals(1, resetTokenFacade.count());
        final PasswordResetToken fetchedToken = resetTokenFacade.findAll().get(0);
        assertFalse(entity == fetchedToken); // NOPMD, we dont want the same
                                             // reference
        assertEquals(entity, fetchedToken); // we want the object content to be
                                            // equal. This forces an override of
                                            // Equals() in the entity
    }

    @Override
    protected BaseMongoDao<PasswordResetToken> getMainTestDao() {
        return resetTokenFacade;
    }

    @Override
    protected Class<PasswordResetToken> getCoreEntityClass() {
        return PasswordResetToken.class;
    }

    @Override
    protected ObjectId getObjectId(final PasswordResetToken t) {
        return t.getId();
    }

    @Override
    protected String getOneFieldName() {
        return "token";
    }

    @Override
    protected String getOneSetterMethodName() {
        return "setToken";
    }

    @Override
    protected String getOneOrderCondition() {
        return "-token";
    }

    @Override
    protected String getOneFilterCondition() {
        return "token ==";
    }

    @Override
    protected String getOneMethodParamInString() {
        return token;
    }

    @Override
    protected Object getOneFilterValue() {
        return getOneMethodParamInString();
    }

    @Override
    public void eagerSaveTest() throws DBException {
        // no eager tests yet
    }

    @Override
    public void eagerRemoveTest() throws DBException {
        // no eager tests yet
    }
}
