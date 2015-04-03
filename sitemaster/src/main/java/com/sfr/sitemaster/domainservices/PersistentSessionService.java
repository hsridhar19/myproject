/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * Facade for PersistentSession entity
 *
 * @author tuxbear
 *
 */
public interface PersistentSessionService {
    void createNewPersistenSession(User user, String sessionId) throws DBException;

    PersistentSession findSessionFromKey(String sessionId) throws DBException;
}
