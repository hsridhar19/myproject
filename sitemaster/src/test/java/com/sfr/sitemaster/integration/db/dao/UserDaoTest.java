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
import com.sfr.sitemaster.dao.mongo.UserDAOImpl;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.commons.HashUtils;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * User integration test. writes to db. so make sure tear down occurs.
 * 
 * @author yves
 * 
 */
public class UserDaoTest extends DaoTestBase<User> {

    @Inject
    private UserDAOImpl userDao;

    @Before
    public void init() {
        userDao.dropCollection();
    }

    @Override
    protected BaseMongoDao<User> getMainTestDao() {
        return userDao;
    }

    @Test
    public void addNewUserTest() throws DBException {
        final String name = "mr andersen";
        assertEquals(0, userDao.count());
        final User cus = new User();
        cus.setName(name);
        userDao.save(cus);
        assertEquals(1, userDao.count());
        assertEquals(name, userDao.findAll().get(0).getName());
    }

    @Test
    public void hashTest() {
        // passwd is foobar
        final String salt = "c28771e1-aaa2-42ae-8fd6-c65794ac23ec";
        final String hash = "ae0b3eb4927f61b0289ec5f16ff7747b";
        final String check = HashUtils.md5_salted("foobar", salt);
        assertEquals(hash, check);
    }

    @Override
    protected Class<User> getCoreEntityClass() {
        return User.class;
    }

    @Override
    protected ObjectId getObjectId(final User t) {
        return t.getId();
    }

    @Override
    protected String getOneFieldName() {
        return "name";
    }

    @Override
    protected String getOneSetterMethodName() {
        return "setName";
    }

    @Override
    protected String getOneMethodParamInString() {
        return "mr andersen";
    }

    @Override
    protected String getOneOrderCondition() {
        return "-name";
    }

    @Override
    protected String getOneFilterCondition() {
        return "name ==";
    }

    @Override
    protected Object getOneFilterValue() {
        return getOneMethodParamInString();
    }

    @Override
    public void eagerSaveTest() throws DBException {
        // no eager tests
    }

    @Override
    public void eagerRemoveTest() throws DBException {
        // no eager tests
    }
}
