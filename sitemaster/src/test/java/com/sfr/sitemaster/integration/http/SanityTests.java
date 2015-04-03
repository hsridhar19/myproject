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
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SanityTests extends IntegrationTestBase {

    @Test
    public void curl_sanity() {
        final Curl curl = new Curl("www.google.no", 80, true, false);
        assertEquals(200, curl.issueRequestWithHeaders("GET", "/", null));
    }

    @Test
    public void curl_version() {
        final Curl curl = new Curl(API_HOST, API_PORT, true, false);
        assertEquals(200, curl.issueRequestWithHeaders("GET", "/api/v1/unres/version", null));
        assertTrue(curl.getResponseBodyString().contains("version"));
        assertTrue(curl.getResponseBodyString().contains("name"));
        assertTrue(curl.getResponseBodyString().contains("timestamp"));
        assertTrue(curl.getResponseBodyString().contains("api_version"));
    }

    @Test
    public void curl_json_version() {
        final Curl curl = new Curl(API_HOST, API_PORT, true, false);
        assertEquals(200, curl.issueRequestWithHeaders("GET", "/api/v1/unres/versionjson", null));
        final JSONObject json = new JSONObject(curl.getResponseBodyString());
        assertNotNull(json.getString("version"));
        assertNotNull(json.getString("name"));
        assertNotNull(json.getString("timestamp"));
        assertNotNull(json.getString("api_version"));
    }
}