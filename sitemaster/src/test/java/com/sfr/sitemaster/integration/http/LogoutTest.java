/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.http;

import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.apicore.http.Curl;
import com.sfr.apicore.pojo.exception.DBException;

import org.junit.Test;

import static org.junit.Assert.*;   //NOPMD

/**
 * Logout tests
 * 
 * @author yves
 * 
 */
public class LogoutTest extends IntegrationTestBase {

    private static final String SOME_DATA = "somebody";
    private static final String SOME_COOKIE = "Cookie: somecookie";

    @Test
    public void successfullogout() throws DBException {
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        // now try to login
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGOUT_URL, null));
        assertNull(curl.getCookieValue());
    }

    @Test
    public void logoutwithoutlogginginfirst() throws DBException {
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGOUT_URL, SOME_DATA, SOME_COOKIE));
        assertNull(curl.getCookieValue());
    }

    @Test
    public void logout_withbody() throws DBException {
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGOUT_URL, SOME_DATA));
        assertNull(curl.getCookieValue());
    }

    @Test
    public void logout_withcookie_invalid() throws DBException {
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGOUT_URL, null, SOME_COOKIE));
        assertNull(curl.getCookieValue());
    }

    @Test
    public void logout_withinvalidcookie_andbody() throws DBException {
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGOUT_URL, SOME_DATA, SOME_COOKIE));
        assertNull(curl.getCookieValue());
    }

    @Test
    public void logout_withvalidcookie_andbody() throws DBException {
        createAndSaveUser();
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        // now try to login
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String validcookie = curl.getCookieValue();
        assertNotNull(validcookie);
        assertTrue(validcookie.startsWith("JSESSION"));
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGOUT_URL, SOME_DATA, COOKIE_HEADER + validcookie));
        assertNull(curl.getCookieValue());
    }
}
