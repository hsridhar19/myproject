/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.db.dao;

import static org.junit.Assert.assertEquals;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.mongo.PersistentSessionDAOImpl;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * PersistentSession integration test. writes to db. so make sure tear down
 * occurs.
 *
 * @author
 *
 */
public class PersistentSessionDaoTests extends DaoTestBase<PersistentSession> {

    @Inject
    private PersistentSessionDAOImpl persistentSessionDao;

    @Before
    public void init() {
        persistentSessionDao.dropCollection();
    }

    @Override
    protected BaseMongoDao<PersistentSession> getMainTestDao() {
        return persistentSessionDao;
    }

    private PersistentSession createEntity() throws DBException {
        final PersistentSession pojo = new PersistentSession();

        pojo.setSessionId("12345");
        pojo.setUserId("682749r9-asfw32-23f-aef-23f");

        return pojo;
    }

    @Test
    public void basicSaveAndRetreivePersistentSession() throws DBException { // NOPMD
        // pmd complains that one assert one test case.
        // lets just ignore that one in this case.
        assertEquals("count not equal", 0, persistentSessionDao.count());
        final PersistentSession entity = createEntity();
        persistentSessionDao.save(entity);
        assertEquals("count not equal after adding one", 1, persistentSessionDao.count());
        final PersistentSession retreived = persistentSessionDao.findAll().get(0);
        assertEquals("entity not equal", entity, retreived);
    }

    @Override
    protected Class<PersistentSession> getCoreEntityClass() {
        return PersistentSession.class;
    }

    @Override
    protected ObjectId getObjectId(final PersistentSession t) {
        return t.getId();
    }

    @Override
    protected String getOneFieldName() {
        return "sessionId";
    }

    @Override
    protected String getOneSetterMethodName() {
        return "setSessionId";
    }

    @Override
    protected String getOneOrderCondition() {
        return "-sessionId";
    }

    @Override
    protected String getOneFilterCondition() {
        return "sessionId ==";
    }

    @Override
    protected String getOneMethodParamInString() {
        return "12345";
    }

    @Override
    protected Object getOneFilterValue() {
        return getOneMethodParamInString();
    }

    @Override
    public void eagerSaveTest() throws DBException {
        // no eager tests yet
    }

    @Override
    public void eagerRemoveTest() throws DBException {
        // no eager tests yet
    }
}
