/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.google.inject.Inject;
import com.sfr.sitemaster.dao.PersistentSessionDao;
import com.sfr.sitemaster.domainservices.PersistentSessionService;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.User;
import com.sfr.apicore.pojo.exception.DBException;

/**
 * PersistentSession domainservices impl using mongo
 * 
 * @author tuxbear
 * 
 */
public class PersistentSessionServiceImpl implements PersistentSessionService {

	private final PersistentSessionDao dao;

	@Inject
	public PersistentSessionServiceImpl(final PersistentSessionDao dao) {
		this.dao = dao;
	}

	@Override
	public void createNewPersistenSession(final User user, final String sessionId) throws DBException {
		final String userId = user.getId().toStringMongod();

		final PersistentSession newPersistentSession = new PersistentSession();
		newPersistentSession.setUserId(userId);
		newPersistentSession.setSessionId(sessionId);
		dao.save(newPersistentSession);
	}

	@Override
	public PersistentSession findSessionFromKey(final String sessionId) throws DBException {
		return dao.findFromSessionId(sessionId);
	}
}
