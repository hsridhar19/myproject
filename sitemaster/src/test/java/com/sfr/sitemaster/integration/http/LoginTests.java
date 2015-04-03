/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.http;

import static org.junit.Assert.*;   //NOPMD

import java.net.URISyntaxException;

import org.json.JSONException;
import org.junit.Test;

import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.apicore.http.Curl;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * Test for logging in.
 * 
 * @author yves
 * 
 */
public class LoginTests extends IntegrationTestBase {
    
    @Test
    public void firsttime_login_success_nocookies_provided() throws DBException, URISyntaxException, JSONException {
        createAndSaveUser();
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        // now try to login
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String sessionCookie = curl.getCookieValue();
        assertNotNull(sessionCookie);
        assertTrue(sessionCookie.startsWith(COOKIE_PREFIX));

        curl.clearCookies(); // we dont use curl's internal cookie handling here

        // try do curl someting in the restricted api
        assertEquals(200, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "", COOKIE_HEADER + sessionCookie));
        // try with the bad cookie
        assertEquals(401, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "Cookie: JSESSIONID=thisisabadcookie"));

        // and if we retry with the first cookie, it'll work still
        assertEquals(200, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "", COOKIE_HEADER + sessionCookie));
    }

    @Test
    public void secondattempt_login_success_reusecookie() throws DBException, URISyntaxException, JSONException {
        createAndSaveUser();
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        // try do curl someting in the restricted api, should get 401
        assertEquals(401, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, null));
        createAndSaveUser();

        // now try to login
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String sessionCookie = curl.getCookieValue();
        assertNotNull(sessionCookie);
        assertTrue(sessionCookie.startsWith(COOKIE_PREFIX));
        curl.clearCookies();

        // reuse the same session
        // only run ths test when using cookie. since basic auth is idempotent and login should always yeild a new session.
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, COOKIE_HEADER + sessionCookie, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String second_sessionCookie = curl.getCookieValue();
        assertNull(second_sessionCookie);
        curl.clearCookies();

        // try do curl someting in the restricted api
        assertEquals(200, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "", COOKIE_HEADER + sessionCookie));

        assertEquals(401, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "Cookie: JSESSIONID=thisisabadcookie"));

        // and if we retry with the first cookie, it'll work still
        assertEquals(200, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "", COOKIE_HEADER + sessionCookie));
    }

    @Test
    public void secondattempt_login_success_doublelogin() throws DBException, URISyntaxException, JSONException {
        // drop the user table and create a new user
        createAndSaveUser();
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        // try do curl someting in the restricted api, should get 401
        assertEquals(401, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, null));

        // now try to login
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String sessionCookie = curl.getCookieValue();
        assertNotNull(sessionCookie);
        assertTrue(sessionCookie.startsWith(COOKIE_PREFIX));
        curl.clearCookies();

        // reuse the same session
        assertEquals(200, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, GOODCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String second_sessionCookie = curl.getCookieValue();
        assertNotNull(second_sessionCookie);
        assertTrue(second_sessionCookie.startsWith(COOKIE_PREFIX));
        assertNotSame(sessionCookie, second_sessionCookie);
        curl.clearCookies();

        // try do curl someting in the restricted api
        assertEquals(200, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "", COOKIE_HEADER + sessionCookie));
        // try with the bad cookie
        assertEquals(401, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "Cookie: JSESSIONID=thisisabadcookie"));

        // and if we retry with the first cookie, it'll work still
        assertEquals(200, curl.issueRequestWithHeaders(GET_METHOD, USER_URL, "", COOKIE_HEADER + sessionCookie));
    }

    @Test
    public void unsuccessful_login() throws DBException {
        createAndSaveUser();
        final Curl curl = new Curl(API_HOST, API_PORT, false, false);

        // now try to login
        assertEquals(401, curl.issueRequestWithHeaders(POST_METHOD, LOGIN_URL, BADCRED, CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED));
        final String sessionCookie = curl.getCookieValue();
        assertNull(sessionCookie);
    }
}
