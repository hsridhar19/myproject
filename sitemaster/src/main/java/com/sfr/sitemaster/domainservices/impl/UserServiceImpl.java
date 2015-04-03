/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import java.util.Date;

import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.time.DateUtils;

import com.google.inject.Inject;
import com.sfr.sitemaster.commons.Guard;
import com.sfr.sitemaster.dao.PasswordResetTokenDao;
import com.sfr.sitemaster.dao.UserDao;
import com.sfr.sitemaster.domainservices.EmailSenderService;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.PasswordResetToken;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.commons.HashUtils;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * User domainservices impl using mongo
 * 
 * @author yves
 * 
 */
public class UserServiceImpl implements UserService {
    static int PASSWORD_TOKEN_EXPIRY_IN_HOURS = 1;

    private final UserDao userDao;

    private final EmailSenderService emailSenderService;

    private final PasswordResetTokenDao passwordResetTokenDao;

    @Inject
    public UserServiceImpl(final UserDao userDao, final EmailSenderService emailSenderService,
            final PasswordResetTokenDao passwordResetTokenDao) {
        this.userDao = userDao;
        this.emailSenderService = emailSenderService;
        this.passwordResetTokenDao = passwordResetTokenDao;
    }

    @Override
    public User login(final String email, final String password) throws LoginException, DBException {
        final String salt = getSalt(email);
        final String hashedPassword = HashUtils.md5_salted(password, salt);
        final User user = userDao.findUserWithLoginCredentials(email, hashedPassword);
        if (user == null) {
            throw new LoginException();
        }
        return user;
    }

    private String getSalt(final String email) throws DBException {
        final User user = userDao.findUserFromEmail(email);
        if (user == null || user.getSalt() == null) {
            return "";
        }
        return user.getSalt();
    }

    @Override
    public User changePassword(final String email, final String password) throws DBException {
        final User user = userDao.findUserFromEmail(email);
        if (user == null || password == null) {
            return null;
        }
        user.setPassword(HashUtils.md5_salted(password, user.getSalt()));
        return userDao.save(user);
    }

    @Override
    public PasswordResetToken requestPasswordReset(final String userEmail) throws DBException,
            CredentialNotFoundException {
        Guard.throwIfNull(userEmail, IllegalArgumentException.class);

        final User userExists = userDao.findUserFromEmail(userEmail);
        if (userExists == null) {
            throw new CredentialNotFoundException("The user does not exist");
        }

        final PasswordResetToken token = new PasswordResetToken(DateUtils.addHours(new Date(),
                PASSWORD_TOKEN_EXPIRY_IN_HOURS), userEmail);

        passwordResetTokenDao.removeTokensForUser(userEmail);

        passwordResetTokenDao.save(token);

        final String message = "You have requested a password reset. Your token is "
                + token.getToken();

        emailSenderService.sendEmail(userEmail, "noreply@statoilfuelandretail.no", message);

        return token;
    }

    @Override
    public void changePasswordWithToken(final String tokenString, final String newPassword)
            throws DBException, CredentialExpiredException, CredentialNotFoundException {
        final PasswordResetToken tokenObject = passwordResetTokenDao
                .findFromTokenString(tokenString);

        if (tokenObject == null) {
            throw new CredentialNotFoundException("The tokenString does not exist");
        }

        if (!tokenObject.isValidAt(new Date())) {
            throw new CredentialExpiredException(
                    "The token had expired. A fresh token is required.");
        }

        changePassword(tokenObject.getUsername(), newPassword);
        passwordResetTokenDao.remove(tokenObject);
    }
}