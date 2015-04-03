/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.mongo.UserDAOImpl;
import com.sfr.sitemaster.entities.User;
import com.sfr.sitemaster.injection.guice.DAOModule;
import com.sfr.sitemaster.injection.guice.DomainServiceModule;
import com.sfr.sitemaster.shared.TestBase;
import com.sfr.sitemaster.shared.TestDBModule;
import com.sfr.apicore.commons.HashUtils;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.injection.SFRInjector;
import com.sfr.apicore.injection.guice.GuiceSFRInjector;
import com.sfr.apicore.pojo.exception.DBException;

public class IntegrationTestBase extends TestBase {
    protected static final int API_PORT = 8888;
    protected static final String API_HOST = "localhost";
    protected static final String DEFAULT_TEST_USER = "bob@sfr.com";
    protected static final String DEFAULT_TEST_PASSWD = "foobar";
    protected static final String DEFAULT_TEST_SALT = HashUtils.salt();
    protected static final String DEFAULT_TEST_HASH = HashUtils.md5_salted(DEFAULT_TEST_PASSWD,
            DEFAULT_TEST_SALT);
    protected static final String GOODCRED = "username=" + DEFAULT_TEST_USER + "&password=" + DEFAULT_TEST_PASSWD;
    protected static final String BADCRED = "username=" + DEFAULT_TEST_USER + "&password=" + "BAD_BAD_PASSWD";
    protected static final String LOGIN_URL = "/api/v1/unres/login";
    protected static final String LOGOUT_URL = "/api/v1/unres/logout";
    protected static final String POST_METHOD = "POST";
    protected static final String GET_METHOD = "GET";
    protected static final String COOKIE_PREFIX = "JSESSIONID";
    protected static final String USER_URL = "/api/v1/res/user";
    protected static final String COOKIE_HEADER = "Cookie: ";
    protected static final String CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED = "Content-type: application/x-www-form-urlencoded";

    @Inject
    private UserDAOImpl userDao;

    static {
        final SFRInjector injector = new GuiceSFRInjector(new TestDBModule(), new DAOModule(),
                new DomainServiceModule());

        InjectionService.registerServiceInjector(injector);
    }

    protected User createAndSaveUser() throws DBException {
        // drop the user table and create a new user
        userDao.dropCollection();
        // create a new user
        final User tmp = new User();
        tmp.setEmail(DEFAULT_TEST_USER);
        tmp.setPassword(DEFAULT_TEST_HASH);
        tmp.setSalt(DEFAULT_TEST_SALT);
        userDao.save(tmp);

        return tmp;
    }
}
