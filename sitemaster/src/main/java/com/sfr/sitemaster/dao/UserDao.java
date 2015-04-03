/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao;

import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.dao.BasicDao;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * Facade for User entity
 * 
 * @author yves
 * 
 */
public interface UserDao extends BasicDao<User> {

    User findUserWithLoginCredentials(String email, String hashedPassword);
    User findUserFromEmail(String email) throws DBException;
}
