/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.unrestricted;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sfr.sitemaster.commons.RestUtils;
import com.sfr.sitemaster.domainservices.PersistentSessionService;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * Login API support
 * 
 * @author yves
 * 
 */
@Path("/login")
public class LoginResource extends SFRBaseResource {
    @Inject
    private UserService facade;

    @Inject
    private PersistentSessionService persistentSessionService;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(final @Context HttpServletRequest req,
            final MultivaluedMap<String, String> form) {
        final String email = form.getFirst(RestUtils.USERNAME_PARAM);
        final String password = form.getFirst(RestUtils.PASSWD_PARAM);

        try {
            if (email == null || password == null) {
                return Response.status(401)
                        .entity("No username or password supplied").build();
            }
            final User user = facade.login(email, password);

            final HttpSession existingLocalSession = req.getSession(false);
            if (existingLocalSession == null) {
                final HttpSession newLocalSession = req.getSession(true);
                persistentSessionService.createNewPersistenSession(user,
                        newLocalSession.getId());
            }

            return Response.ok().build();
        } catch (DBException e) {
            return Response.status(500).entity("Unable to perform login.")
                    .build();
        } catch (LoginException e) {
            return Response.status(401).entity("Credential failed.").build();
        }
    }
}
