/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import com.sfr.apicore.injection.guice.AbstractDBModule;

/**
 * Configuration of the DB module
 * 
 * @author yves
 */
public class DBModule extends AbstractDBModule {
    @Override
    public String getMongoDBNameDefault() {
        return "sitemaster";
    }
}
