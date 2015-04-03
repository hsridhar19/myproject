/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import com.google.inject.AbstractModule;
import com.sfr.sitemaster.dao.PasswordResetTokenDao;
import com.sfr.sitemaster.dao.PersistentSessionDao;
import com.sfr.sitemaster.dao.StationDao;
import com.sfr.sitemaster.dao.UserDao;
import com.sfr.sitemaster.dao.mongo.PasswordResetTokenDAOImpl;
import com.sfr.sitemaster.dao.mongo.PersistentSessionDAOImpl;
import com.sfr.sitemaster.dao.mongo.StationDAOImpl;
import com.sfr.sitemaster.dao.mongo.UserDAOImpl;

public class DAOModule extends AbstractModule {
    @Override
    protected void configure() {
        // Engage DAO Objects that shields client code from Morphia and Mongo
        bind(UserDao.class).to(UserDAOImpl.class);
        bind(PasswordResetTokenDao.class).to(PasswordResetTokenDAOImpl.class);
        bind(PersistentSessionDao.class).to(PersistentSessionDAOImpl.class);
        bind(StationDao.class).to(StationDAOImpl.class);
    }
}
