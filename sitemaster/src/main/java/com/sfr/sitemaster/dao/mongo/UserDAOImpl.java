/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao.mongo;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import com.mongodb.Mongo;
import com.sfr.sitemaster.dao.UserDao;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.injection.guice.DBName;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * User domainservices impl using mongo
 * 
 * @author yves
 * 
 */
public class UserDAOImpl extends BaseMongoDao<User> implements UserDao {

    @Inject
    public UserDAOImpl(final Mongo mongo, final Morphia morphia, final @DBName String dbName) {
        super(User.class, mongo, morphia, dbName);
    }

    @Override
    protected void saveEagerRelations(final User entity) throws DBException {
        // no eager relationship here
    }

    @Override
    protected void removeEagerRelations(final User entity) throws DBException {
        // no eager relationship here
    }

    @Override
    public User findUserWithLoginCredentials(final String email, final String hashedPassword) {
        final Query<User> query = createQuery().field("email").equal(email).field("password")
                .equal(hashedPassword);
        final User user = query.get();

        return user;
    }

    @Override
    public User findUserFromEmail(final String email) throws DBException {
        return findOneBasedOnField("email", email, true);
    }
}
