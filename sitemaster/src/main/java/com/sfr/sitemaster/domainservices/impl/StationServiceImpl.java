/**
 * This a implementation class of StationService interface
 */
package com.sfr.sitemaster.domainservices.impl;

import java.util.List;

import com.google.inject.Inject;
import com.sfr.apicore.pojo.exception.DBException;
import com.sfr.sitemaster.dao.StationDao;
import com.sfr.sitemaster.domainservices.StationService;
import com.sfr.sitemaster.entities.Station;

/**
 * @author Padmashree on 31-Mar-2015
 *
 */

public class StationServiceImpl implements StationService{

	private final StationDao stationDao;

	@Inject
	public StationServiceImpl(final StationDao stationDao) {
		this.stationDao = stationDao;
	}

	@Override
	public void createNewStation(String stationID, String stationLocation,
			String stationName) throws DBException {
		final Station newStation = new Station(); 
		newStation.setStationID(stationID);
		newStation.setStationLocation(stationLocation);
		newStation.setStationName(stationName);
		stationDao.save(newStation);
	}

	@Override
	public void updateExistingStation(String stationID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeExistingStation(String stationID) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Station> listAllExistingStations() throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Station findExistingStationFromID(String stationID) {
		// TODO Auto-generated method stub
		return null;
	}

}
