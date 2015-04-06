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

public class StationServiceImpl implements StationService {

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
		stationDao.createStation(newStation);
	}

	@Override
	public void removeExistingStation(String stationID) {
		try {
			stationDao.removeStation(stationID);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateExistingStation(String stationID, String sname,
			String sloc) throws DBException {
		
		final Station newStation = new Station();
		
		newStation.setStationID(stationID);
		newStation.setStationLocation(sloc);
		newStation.setStationName(sname);
		
		stationDao.updateStation(newStation);
	}

	@Override
	public Station findExistingStationFromID(String stationID) {
		Station station = null;
		try {
			station = stationDao.findStationFromID(stationID);
		} catch (DBException e) {
			e.printStackTrace();
		}
		
		return station;
	}

	@Override
	public List<Station> listAllExistingStations() throws DBException {
		List<Station> listOfAllStations = null;
		listOfAllStations =	stationDao.listAllStations();
		return listOfAllStations;
	}
}
