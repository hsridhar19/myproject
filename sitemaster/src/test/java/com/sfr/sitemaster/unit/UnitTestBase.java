/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.unit;

import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.injection.SFRInjector;
import com.sfr.apicore.injection.guice.GuiceSFRInjector;
import com.sfr.sitemaster.shared.TestBase;

public class UnitTestBase extends TestBase {
    static {
        final SFRInjector injector = new GuiceSFRInjector();
        InjectionService.registerServiceInjector(injector);
    }
}
