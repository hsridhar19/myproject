/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.restricted;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.UserDao;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.api.restricted.CRUDBaseResource;
import com.sfr.apicore.dao.BasicDao;

import javax.ws.rs.Path;

/**
 * The root url for the unrestricted API
 * 
 * @author yves
 * 
 */
@Path("/user/")
public class UserResource extends CRUDBaseResource<User> {

    @Inject
    private UserDao facade;

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected BasicDao<User> getFacade() {
        return facade;
    }
}
