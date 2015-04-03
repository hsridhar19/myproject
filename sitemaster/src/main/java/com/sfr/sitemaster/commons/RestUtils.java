/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.commons;

import java.io.UnsupportedEncodingException;
import java.nio.charset.MalformedInputException;

import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.pojo.exception.DBException;
import com.sun.jersey.core.util.Base64;

/**
 * Rest utils, common auth services etc.
 * 
 * @author yves
 * 
 */
public final class RestUtils {

    public static final String USERNAME_PARAM = "username";
    public static final String PASSWD_PARAM = "password";
    public static final String SESSION_KEY = "engage_user";

    private RestUtils() {
        // private constructor for utility class
    }

    public static User authoriseBasicAuth(final HttpServletRequest httpRequest,
            final Logger logger, final UserService loginService) throws MalformedInputException,
            UnsupportedEncodingException, LoginException, DBException {
        final String authorizationHeaderValue = StringUtils.trimToEmpty(httpRequest
                .getHeader("Authorization"));
        if (StringUtils.isBlank(authorizationHeaderValue)) {
            logger.debug("No Authorization header value provided");
            return null;
        }
        if (!authorizationHeaderValue.startsWith("Basic")) {
            logger.debug("Unsupported authorization scheme. Enage supports Basic authentication");
            return null;
        }

        final int index = authorizationHeaderValue.indexOf(' ');
        if (index <= 0) {
            throw new MalformedInputException(index);
        }

        final String encodedCredential = authorizationHeaderValue.substring(index + 1);
        final String decodedCredential = new String(Base64.decode(encodedCredential
                .getBytes("UTF-8")), "UTF-8");

        // Basic authentication does not allow ':' in username, so we are safe
        // in the following line.
        final String[] credentialParts = decodedCredential.split(":", 2);
        if (credentialParts == null || credentialParts.length != 2 || "".equals(credentialParts[0])
                || "".equals(credentialParts[1])) {
            logger.debug("Malformed Authorization header value.");
            return null;
        }

        return loginService.login(credentialParts[0], credentialParts[1]);
    }

    public static String getSessionIdFromRequest(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
