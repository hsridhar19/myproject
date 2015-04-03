/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.db.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.entities.SFREntityObject;
import com.sfr.apicore.pojo.PagedListResult;
import com.sfr.apicore.pojo.exception.DBException;

public abstract class DaoTestBase<T extends SFREntityObject> extends IntegrationTestBase {

    protected static final int MAX_BATCH_SIZE = 30;
    protected static final int SUBSTRING_START = 1;
    protected static final int SUBSTRING_END = 5;
    protected static final int LIMIT = 3;
    protected static final String ID_FIELD = "_id";

    @Before
    public void init() {
        // start with a clean state
        getMainTestDao().dropCollection();
    }

    @After
    public void tearDown() {
        // back to a clean state, cos we are meticulous like that
        getMainTestDao().dropCollection();
    }

    protected abstract BaseMongoDao<T> getMainTestDao();

    protected abstract Class<T> getCoreEntityClass();

    protected abstract ObjectId getObjectId(T t);

    protected abstract String getOneFieldName();

    protected abstract String getOneSetterMethodName();

    protected abstract String getOneMethodParamInString();

    protected abstract String getOneOrderCondition();

    protected abstract String getOneFilterCondition();

    protected abstract Object getOneFilterValue();

    @Test
    public abstract void eagerSaveTest() throws DBException;

    @Test
    public abstract void eagerRemoveTest() throws DBException;

    private static Method getSingleMethod(final Object o, final String methodName) {
        final Class<?> c = o.getClass();
        final Method[] methods = c.getMethods();
        for (final Method m : methods) {
            if (m.getName().startsWith(methodName)) {
                return m;
            }
        }
        return null;
    }

    protected List<T> createAndSaveBatches() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, DBException {
        final List<T> list = new ArrayList<>();
        for (int i = 0; i < MAX_BATCH_SIZE; i++) {
            final T t = getCoreEntityClass().newInstance();
            final Method m = getSingleMethod(t, getOneSetterMethodName());
            m.setAccessible(true);
            m.invoke(t, getOneMethodParamInString());
            list.add(getMainTestDao().save(t));
        }
        Assert.assertEquals(MAX_BATCH_SIZE, list.size());
        return list;
    }

    @Test
    public void save() throws DBException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        Assert.assertTrue(getMainTestDao().findAll().contains(t));
        // tear down
        getMainTestDao().remove(t);
    }

    @Test
    public void find() throws DBException, InstantiationException, IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        final T _t = getMainTestDao().find(getObjectId(t));
        Assert.assertEquals(t, _t);
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findOneBaseOnField_exactMatchTrue() throws DBException, InstantiationException,
            IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        final T _t = getMainTestDao().findOneBasedOnField(ID_FIELD, getObjectId(t), true);
        Assert.assertEquals(_t, t);
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findOneBasedOnField_exactMatchFalse() throws DBException, InstantiationException,
            IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        final T _t = getMainTestDao().findOneBasedOnField(ID_FIELD, getObjectId(t), false);
        Assert.assertEquals(_t, t);
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findBasedOnField_exactMatchTrue() throws DBException, InstantiationException,
            IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        final List<T> _t = getMainTestDao().findBasedOnField(ID_FIELD, getObjectId(t), true);
        Assert.assertTrue(_t.contains(t));
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findBasedOnField_exactMatchFalse() throws DBException, InstantiationException,
            IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        final List<T> _t = getMainTestDao().findBasedOnField(ID_FIELD, getObjectId(t), false);
        Assert.assertTrue(_t.contains(t));
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findBasedOnFieldWithLimit_exactMatchTrue() throws InstantiationException,
            IllegalAccessException, DBException, IllegalArgumentException,
            InvocationTargetException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findBasedOnFieldWithLimit(getOneFieldName(),
                getOneMethodParamInString(), LIMIT, true);
        Assert.assertEquals(LIMIT, _list.size());
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithLimit_exactMatchFalse() throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            DBException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao()
                .findBasedOnFieldWithLimit(getOneFieldName(),
                        getOneMethodParamInString().substring(SUBSTRING_START, SUBSTRING_END),
                        LIMIT, false);
        Assert.assertEquals(_list.size(), LIMIT);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithOrder_exactMatchTrue() throws InstantiationException,
            IllegalAccessException, DBException, IllegalArgumentException,
            InvocationTargetException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findBasedOnFieldWithOrder(getOneFieldName(),
                getOneMethodParamInString(), getOneOrderCondition(), true);
        Assert.assertEquals(_list.size(), MAX_BATCH_SIZE);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithOrder_exactMatchFalse() throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            DBException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findBasedOnFieldWithOrder(getOneFieldName(),
                getOneMethodParamInString().substring(SUBSTRING_START, SUBSTRING_END),
                getOneOrderCondition(), false);
        Assert.assertEquals(_list.size(), MAX_BATCH_SIZE);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithOrder_exactMatchFalse_withfullstring()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, DBException {
        // full string matches should work by default
        final List<T> list = createAndSaveBatches();
        // since this is regex based. lets escape any meta characters.
        final List<T> _list = getMainTestDao().findBasedOnFieldWithOrder(getOneFieldName(),
                getMainTestDao().escapeRegexMetaChars(getOneMethodParamInString()),
                getOneOrderCondition(), false);
        Assert.assertEquals(_list.size(), MAX_BATCH_SIZE);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithOrder_exactMatchFalse_withObjectId()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, DBException {
        // id field shoudl work.
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findBasedOnFieldWithOrder(ID_FIELD,
                getObjectId(list.get(0)), getOneOrderCondition(), false);
        Assert.assertEquals(_list.size(), 1);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithLimitAndOrder_exactMatchTrue() throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            DBException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findBasedOnFieldWithLimitAndOrder(getOneFieldName(),
                getOneMethodParamInString(), LIMIT, getOneOrderCondition(), true);
        Assert.assertEquals(_list.size(), LIMIT);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findBasedOnFieldWithLimitAndOrder_exactMatchFalse() throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            DBException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findBasedOnFieldWithLimitAndOrder(getOneFieldName(),
                getOneMethodParamInString().substring(SUBSTRING_START, SUBSTRING_END), LIMIT,
                getOneOrderCondition(), false);
        Assert.assertEquals(_list.size(), LIMIT);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findAll() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, DBException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findAll();
        Assert.assertEquals(_list.size(), MAX_BATCH_SIZE);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findOrderedList() throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, DBException, InstantiationException {
        final List<T> list = createAndSaveBatches();
        final List<T> _list = getMainTestDao().findOrderedList(getOneOrderCondition());
        Assert.assertEquals(_list.size(), MAX_BATCH_SIZE);
        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findOrderRange() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        final List<T> list = createAndSaveBatches();
        final Map<String, Object> filters = new HashMap<>();
        filters.put(getOneFilterCondition(), getOneFilterValue());

        // lets say 7 items a page. that should give us 5 pages with the last
        // page having 2 items.
        final PagedListResult<T> _list = getMainTestDao().findOrderedRange(5, 7,
                getOneOrderCondition(), filters);
        Assert.assertEquals(2, _list.getPagedResult().size());

        final PagedListResult<T> __list = getMainTestDao().findOrderedRange(1, 7,
                getOneOrderCondition(), filters);
        Assert.assertEquals(7, __list.getPagedResult().size());

        // 0 is not a page number, so it should not have fetched anything.
        final PagedListResult<T> badPage = getMainTestDao().findOrderedRange(0, 7,
                getOneOrderCondition(), filters);
        Assert.assertEquals(0, badPage.getPagedResult().size());

        // -1 is an even worse page number, so it should not have fetched
        // anything
        final PagedListResult<T> realyBadPage = getMainTestDao().findOrderedRange(-1, 7,
                getOneOrderCondition(), filters);
        Assert.assertEquals(0, realyBadPage.getPagedResult().size());

        // 0 items on the page should give us 0 result also
        final PagedListResult<T> zeroItems = getMainTestDao().findOrderedRange(1, 0,
                getOneOrderCondition(), filters);
        Assert.assertEquals(0, zeroItems.getPagedResult().size());

        // -1 items on the page should give us 0 result too
        final PagedListResult<T> badItems = getMainTestDao().findOrderedRange(1, -1,
                getOneOrderCondition(), filters);
        Assert.assertEquals(0, badItems.getPagedResult().size());

        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void findRange() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        final List<T> list = createAndSaveBatches();

        // 30 items, we can go from 0 to 29 items.
        final List<T> _list = getMainTestDao().findRange(0, 29);
        Assert.assertEquals(list, _list);
        Assert.assertEquals(list.size(), _list.size());

        // just get 10 items
        final List<T> ten = getMainTestDao().findRange(0, 9);
        Assert.assertEquals(10, ten.size());

        // go out of range by -1, return 0
        final List<T> outOfRange = getMainTestDao().findRange(-1, 9);
        Assert.assertEquals(0, outOfRange.size());

        // out of range completely
        final List<T> _outOfRange = getMainTestDao().findRange(50, 60);
        Assert.assertEquals(0, _outOfRange.size());

        // out of range by 10
        final List<T> __outOfRange = getMainTestDao().findRange(0, 39);
        Assert.assertEquals(MAX_BATCH_SIZE, __outOfRange.size());

        // from > end
        final List<T> bad = getMainTestDao().findRange(29, 0);
        Assert.assertEquals(0, bad.size());

        // just generally stupid
        final List<T> _bad = getMainTestDao().findRange(-29, 0);
        Assert.assertEquals(0, _bad.size());

        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void count() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        final List<T> list = createAndSaveBatches();
        Assert.assertEquals(list.size(), getMainTestDao().count());

        // tear down
        getMainTestDao().remove(list);
    }

    @Test
    public void remove() throws DBException, InstantiationException, IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        final T _t = getMainTestDao().find(getObjectId(t));
        Assert.assertEquals(t, _t);

        getMainTestDao().remove(_t);
        final T __t = getMainTestDao().find(getObjectId(t));
        Assert.assertNull(__t);
    }

    @Test
    public void removeList() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        final List<T> list = createAndSaveBatches();
        getMainTestDao().remove(list);
        final List<T> _list = getMainTestDao().findAll();
        Assert.assertEquals(0, _list.size());
    }

    @Test
    public void removeBasedOnFieldValue() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        createAndSaveBatches();
        getMainTestDao().remove(getOneFieldName(), getOneMethodParamInString());
        final List<T> _list = getMainTestDao().findAll();
        Assert.assertEquals(0, _list.size());
    }

    @Test
    public void dropCollection() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        createAndSaveBatches();
        getMainTestDao().dropCollection();
        final List<T> _list = getMainTestDao().findAll();
        Assert.assertEquals(0, _list.size());
    }

    @Test
    public void serializeToJSONAndPerformUpdate() throws DBException, InstantiationException,
            IllegalAccessException, JSONException, SecurityException, IllegalArgumentException,
            NoSuchFieldException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        Assert.assertTrue(getMainTestDao().findAll().contains(t));
        // serialize to json
        final JSONObject obj = getMainTestDao().serializeEntityToJSON(t);
        Assert.assertNotNull(obj);
        final String id = obj.getJSONObject(ID_FIELD).getString("$oid");
        Assert.assertEquals(t.getId().toString(), id);
        // serialize back to entity
        final T check = getMainTestDao().serializeJSONToEntity(obj, getCoreEntityClass());
        Assert.assertNotNull(check);
        final T check2 = getMainTestDao().save(check);
        Assert.assertEquals(1, getMainTestDao().count());
        Assert.assertEquals(t, check2);
        // tear down
        getMainTestDao().remove(t);
    }

    @Test
    public void serializeToJSONAndPerformInsert() throws DBException, SecurityException,
            IllegalArgumentException, JSONException, NoSuchFieldException, IllegalAccessException,
            InstantiationException {
        // build up
        final T t = getMainTestDao().save(getCoreEntityClass().newInstance());
        // test assertion
        Assert.assertTrue(getMainTestDao().findAll().contains(t));
        // serialize to json
        final JSONObject obj = getMainTestDao().serializeEntityToJSON(t);
        Assert.assertNotNull(obj);
        final String id = obj.getJSONObject(ID_FIELD).getString("$oid");
        Assert.assertEquals(t.getId().toString(), id);
        // serialize back to entity
        obj.remove(ID_FIELD);
        final T check = getMainTestDao().serializeJSONToEntity(obj, getCoreEntityClass());
        Assert.assertNotNull(check);
        final T check2 = getMainTestDao().save(check);
        Assert.assertEquals(2, getMainTestDao().count());
        // tear down
        getMainTestDao().remove(t);
        getMainTestDao().remove(check2);
    }
}
