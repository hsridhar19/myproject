/**
 * Copyright (c) 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.unrestricted.passwordreset;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.pojo.exception.DBException;
import org.apache.log4j.Logger;

import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tuxbear on 06/01/15.
 *
 * Accepts a request for a password reset for a given username.
 *
 * Password reset tokens are sent by email to the user in question. The token can then be used to reset the user's password.
 *
 */
@Path("/password")
public class PasswordResetResource extends SFRBaseResource {
    Logger LOGGER = Logger.getLogger(PasswordResetResource.class);

    @Inject
    private UserService userService;

    @Inject
    private Gson gson;

    /**
     * Accepts a request for a password reset token. If the username is found a token is created, and a link is sent to the user.
     * The API responds the same way for existing and non-existing users to prevent information leakage of users.
     *
     * @param req
     * @param jsonRegisterResetRequest A JSON serialized {@link com.sfr.api.unrestricted.passwordreset.RegisterResetRequest}
     * @return
     */
    @Path("/reset-request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response requestPasswordReset(final @Context HttpServletRequest req, final String jsonRegisterResetRequest) {
        try {
            final RegisterResetRequest resetRequest = gson.fromJson(jsonRegisterResetRequest, RegisterResetRequest.class);

            userService.requestPasswordReset(resetRequest.getUsername());
            return Response.ok("password reset token generated if user exists").build();

        } catch (DBException e) {
            return DB_EXCEPTION;
        } catch (CredentialNotFoundException e) {
            return Response.ok("password reset token generated if user exists").build();
        }
    }

    /**
     * Accepts a request for a password reset given the token. If the token is valid the user it belongs to will have it's password reset to newPassword
     *
     * @param req
     * @param jsonResetRequest A JSON serialized {@link com.sfr.api.unrestricted.passwordreset.ResetRequest}
     * @return Status codes: 406 -> Token expired
     */
    @Path("/reset")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response resetPassword(final @Context HttpServletRequest req, final String jsonResetRequest) throws CredentialNotFoundException {
        try {

            final ResetRequest request = gson.fromJson(jsonResetRequest, ResetRequest.class);
            userService.changePasswordWithToken(request.getToken(), request.getNewPassword());

            return Response.ok("password reset").build();
        } catch (DBException e) {
            return DB_EXCEPTION;
        } catch (CredentialExpiredException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("The token has expired.").build();
        }
    }
}
