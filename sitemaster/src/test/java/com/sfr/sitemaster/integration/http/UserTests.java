/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.http;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.mongo.UserDAOImpl;
import com.sfr.sitemaster.entities.User;
import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.apicore.http.Curl;
import com.sfr.apicore.pojo.exception.DBException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Basic user tests.
 * 
 * @author yves
 * 
 */
public class UserTests extends IntegrationTestBase {
    @Inject
    private UserDAOImpl facade;

    @Before
    public void init() {
        facade.dropCollection();
    }

    @Test
    public void getUserList() throws DBException, JSONException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        assertEquals(0, facade.count());
        createAndSaveUser();
        assertEquals(1, facade.count());
        final Curl curl = new Curl(API_HOST, API_PORT, true, true);
        curl.setUsernamePassword(DEFAULT_TEST_USER, DEFAULT_TEST_PASSWD);
        assertEquals(200, curl.issueRequestWithHeaders("GET", "/api/v1/res/user", null));
        final String responseBody = curl.getResponseBodyString();
        assertNotNull(responseBody);
        final JSONObject obj = new JSONObject(responseBody);
        final JSONArray array = obj.getJSONArray("list");
        assertEquals(1, array.length());
        final User check = facade.serializeJSONToEntity(array.getJSONObject(0), User.class);
        assertEquals(DEFAULT_TEST_USER, check.getEmail());
        assertEquals(200, curl.issueRequestWithHeaders("GET", "/api/v1/res/user/count", null));
    }
}
