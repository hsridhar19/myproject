/**
 * 
 */
package com.sfr.sitemaster.dao;

/**
 * Facade for station Entity
 * @author sourabh
 *
 */

import java.util.List;

import com.sfr.apicore.dao.BasicDao;
import com.sfr.apicore.pojo.exception.DBException;
import com.sfr.sitemaster.entities.Station;

public interface StationDao extends BasicDao<Station> {

	 void createStation(Station station) throws DBException;
	 void updateStation(Station station) throws DBException;
	 void removeStation(String stationID) throws DBException;
	 List<Station> listAllStations() throws DBException;
	 Station findStationFromID(String stationID) throws DBException;

}


