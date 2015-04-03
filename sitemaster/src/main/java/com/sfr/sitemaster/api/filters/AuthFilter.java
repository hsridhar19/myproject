/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.filters;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.sfr.sitemaster.commons.RestUtils;
import com.sfr.sitemaster.domainservices.PersistentSessionService;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.api.filters.patterns.ChainedFilter;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * BasicAuthentication filter.
 * 
 * @author yves
 * 
 */
public class AuthFilter extends ChainedFilter {
    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class);

    @Inject
    private UserService userService;

    @Inject
    private PersistentSessionService persistentSessionService;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        InjectionService.getInjector().injectMembers(this);
    }

    @Override
    public boolean filterIncomingRequest(final ServletRequest request,
            final ServletResponse response) throws IOException,
            ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final StringBuilder errorMsg = new StringBuilder(21);
        errorMsg.append("Authetication failed.");
        try {
            final String sessionId = RestUtils
                    .getSessionIdFromRequest(httpRequest);

            if (sessionId == null) {
                // no session, fallback to basic auth
                if (RestUtils.authoriseBasicAuth(httpRequest, LOGGER,
                        userService) != null) {
                    // no need to create the session.
                    return true;
                }
            } else {
                // client claims to have a session
                final PersistentSession persistentSession = persistentSessionService
                        .findSessionFromKey(sessionId);

                if (persistentSession == null) {
                    // no session found, fallback on basic authentication.
                    // uses basic authentication
                    final User authenticatedUser = RestUtils
                            .authoriseBasicAuth(httpRequest, LOGGER,
                                    userService);
                    if (authenticatedUser != null) {
                        // basic auth success - creating session
                        createAndPersistNewSession(httpRequest,
                                authenticatedUser);
                        return true;
                    }
                } else {
                    // the client's claim is true
                    return true;
                }
            }
            // httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"" +
            // realm + "\"");
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
        }
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                errorMsg.toString());
        return false;
    }

    private void createAndPersistNewSession(
            final HttpServletRequest httpRequest, final User authenticatedUser)
            throws DBException {
        final HttpSession newSession = httpRequest.getSession(true);
        persistentSessionService.createNewPersistenSession(authenticatedUser,
                newSession.getId());
    }

    @Override
    public void filterOutgoingResponse(final ServletRequest request,
            final ServletResponse response) throws IOException,
            ServletException {
        // not needed.
    }

    @Override
    public void destroy() {
        // not needed
    }
}
