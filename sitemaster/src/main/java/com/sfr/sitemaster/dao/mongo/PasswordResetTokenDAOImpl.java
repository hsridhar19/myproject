/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao.mongo;

import com.google.code.morphia.Morphia;
import com.google.inject.Inject;
import com.mongodb.Mongo;
import com.sfr.sitemaster.dao.PasswordResetTokenDao;
import com.sfr.sitemaster.entities.PasswordResetToken;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.injection.guice.DBName;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * PasswordResetToken data access impl using mongo
 *
 * @author tuxbear
 *
 */
public class PasswordResetTokenDAOImpl extends BaseMongoDao<PasswordResetToken> implements
        PasswordResetTokenDao {

    @Inject
    public PasswordResetTokenDAOImpl(final Mongo mongo, final Morphia morphia,
            final @DBName String dbName) {
        super(PasswordResetToken.class, mongo, morphia, dbName);
    }

    @Override
    protected void saveEagerRelations(final PasswordResetToken entity) throws DBException {
        // no eager relationship here
    }

    @Override
    protected void removeEagerRelations(final PasswordResetToken entity) throws DBException {
        // no eager relationship here
    }

    @Override
    public void removeTokensForUser(final String username) throws DBException {
        remove("username", username);
    }

    @Override
    public PasswordResetToken findFromTokenString(final String tokenString) throws DBException {
        return findOneBasedOnField("token", tokenString, true);
    }
}
