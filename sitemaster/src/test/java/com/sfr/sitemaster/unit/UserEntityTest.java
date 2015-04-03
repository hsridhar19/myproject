/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.unit;

import com.sfr.sitemaster.entities.User;

public class UserEntityTest extends EntityUnitTestBase<User> {
    @Override
    protected Class<User> getCoreEntityClass() {
        return User.class;
    }
}
