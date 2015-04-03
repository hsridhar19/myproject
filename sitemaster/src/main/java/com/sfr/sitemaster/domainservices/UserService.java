/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.sfr.sitemaster.entities.PasswordResetToken;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.pojo.exception.DBException;

import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.LoginException;

/**
 * Facade for User entity
 * 
 * @author yves
 * 
 */
public interface UserService {

    User login(String email, String password) throws LoginException, DBException;

    User changePassword(String email, String password) throws DBException;

    PasswordResetToken requestPasswordReset(String user) throws DBException, CredentialNotFoundException;

    void changePasswordWithToken(String tokenString, String newPassword) throws DBException, CredentialExpiredException, CredentialNotFoundException;
}
