/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.injection.SFRInjector;
import com.sfr.apicore.injection.guice.GuiceSFRInjector;

/**
 * Injector for modules.
 * 
 * @author yves
 */
public class InjectionServiceConfig implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        final SFRInjector injector = new GuiceSFRInjector(new DBModule(), new DAOModule(),
                new DomainServiceModule());

        InjectionService.registerServiceInjector(injector);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        // nothing needs destroying really.
    }
}
