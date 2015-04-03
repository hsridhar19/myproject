/**
 * This is a station service interface 
 */
package com.sfr.sitemaster.domainservices;

import java.util.List;

import com.sfr.apicore.pojo.exception.DBException;
import com.sfr.sitemaster.entities.Station;

/**
 * @author Padmashree on 31-Mar-2015
 *
 */

public interface StationService {

	 void createNewStation(String stationID, String stationLocation, String stationName) throws DBException;
	 
	 void updateExistingStation(String stationID) throws DBException;
	 
	 void removeExistingStation(String stationID) throws DBException;
	 
	 List<Station> listAllExistingStations() throws DBException;
	 
	 Station findExistingStationFromID(String stationID) throws DBException;
	
}
