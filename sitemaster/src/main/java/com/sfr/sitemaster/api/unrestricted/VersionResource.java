/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.unrestricted;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.sfr.sitemaster.domainservices.VersionService;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;

/**
 * The root url for the unrestricted API
 * 
 * @author yves
 * 
 */
@Path("/")
public class VersionResource extends SFRBaseResource {

    @Inject
    private VersionService facade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("versionjson")
    public Response version_json() {
        final JSONObject obj = new JSONObject();
        try {
            obj.put("version", facade.getAppVersion());
            obj.put("name", facade.getAppName());
            obj.put("timestamp", facade.getAppTimestamp());
            obj.put("api_version", facade.getAPIVersion());
            return Response.status(200).entity(obj.toString()).build();
        } catch (JSONException ex) {
            return INVALID_JSON_SERVERSIDE;
        } catch (IOException ex) {
            return buildJSONResponse(500, "IO error. Missing property files? "
                    + ex.getMessage());
        } catch (Exception ex) {
            return buildJSONResponse(500, ex.getMessage());
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("version")
    public Response version() {
        final StringBuilder builder = new StringBuilder(41);
        try {
            builder.append("version=" + facade.getAppVersion() + ", name="
                    + facade.getAppName() + ", timestamp="
                    + facade.getAppTimestamp() + ", api_version="
                    + facade.getAPIVersion());
            return Response.status(200).entity(builder.toString()).build();
        } catch (IOException ex) {
            return buildJSONResponse(500, "IO error. Missing property files? "
                    + ex.getMessage());
        } catch (Exception ex) {
            return buildJSONResponse(500, ex.getMessage());
        }
    }
}
