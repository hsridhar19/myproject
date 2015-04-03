/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.unit.domainservices;
import com.sfr.sitemaster.dao.PasswordResetTokenDao;
import com.sfr.sitemaster.dao.UserDao;
import com.sfr.sitemaster.domainservices.EmailSenderService;
import com.sfr.sitemaster.domainservices.impl.UserServiceImpl;
import com.sfr.sitemaster.entities.User;
import com.sfr.sitemaster.unit.UnitTestBase;
import com.sfr.apicore.pojo.exception.DBException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.security.auth.login.CredentialNotFoundException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests extends UnitTestBase {

    @Mock
    private UserDao userDaoMock;

    @Mock
    private PasswordResetTokenDao passwordResetTokenDaoMock;

    @Mock
    private EmailSenderService emailSenderServiceMock;

    private UserServiceImpl userService;

    @Before
    public void reset() {
        Mockito.reset(userDaoMock);
        userService = new UserServiceImpl(userDaoMock, emailSenderServiceMock, passwordResetTokenDaoMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserFails() throws DBException, CredentialNotFoundException {
        userService.requestPasswordReset(null);
    }

    @Test(expected = CredentialNotFoundException.class)
    public void userNotFoundYieldsCorrectException() throws DBException, CredentialNotFoundException {
        when(userDaoMock.findUserFromEmail(anyString())).thenReturn(null);
        userService.requestPasswordReset("i don't exist");
    }

    @Test
    public void when_requestToken_remove_old_tokens() throws DBException, CredentialNotFoundException {
        final String userEmail = "user@email.com";
        final User user = new User();
        when(userDaoMock.findUserFromEmail(userEmail)).thenReturn(user);

        userService.requestPasswordReset(userEmail);
    }
}
