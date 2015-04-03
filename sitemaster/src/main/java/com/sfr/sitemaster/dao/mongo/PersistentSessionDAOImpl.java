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
import com.sfr.sitemaster.dao.PersistentSessionDao;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.injection.guice.DBName;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * PersistentSession domainservices impl using mongo
 * 
 * @author tuxbear
 * 
 */
public class PersistentSessionDAOImpl extends BaseMongoDao<PersistentSession> implements PersistentSessionDao {

    @Inject
    public PersistentSessionDAOImpl(final Mongo mongo, final Morphia morphia, final @DBName String dbName) {
        super(PersistentSession.class, mongo, morphia, dbName);
    }

    @Override
    protected void saveEagerRelations(final PersistentSession entity) throws DBException {
        // no eager relationship here
    }

    @Override
    protected void removeEagerRelations(final PersistentSession entity) throws DBException {
        // no eager relationship here
    }

    @Override
    public PersistentSession findFromSessionId(final String sessionKey) throws DBException {
        return findOneBasedOnField("sessionId", sessionKey, true);
    }
}
