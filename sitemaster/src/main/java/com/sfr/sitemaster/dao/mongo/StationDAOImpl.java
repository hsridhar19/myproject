package com.sfr.sitemaster.dao.mongo;

import java.util.List;

import com.google.code.morphia.Morphia;
import com.google.inject.Inject;
import com.mongodb.Mongo;
import com.sfr.apicore.dao.mongo.BaseMongoDao;
import com.sfr.apicore.injection.guice.DBName;
import com.sfr.apicore.pojo.exception.DBException;
import com.sfr.sitemaster.dao.StationDao;
import com.sfr.sitemaster.entities.Station;

/**
 * Station services impl using mongo
 * 
 * @author sourabh
 * 
 */

public class StationDAOImpl extends BaseMongoDao<Station> implements StationDao {

	@Inject
	public StationDAOImpl(final Mongo mongo, final Morphia morphia, final @DBName String dbName) {
		super(Station.class, mongo, morphia, dbName);
	}
    
	@Override
	public void updateStation(String stationID) throws DBException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeStation(String stationID) throws DBException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Station> listAllStations() throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Station findStationFromID(String stationID) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void saveEagerRelations(Station paramT) throws DBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeEagerRelations(Station paramT) throws DBException {
		// TODO Auto-generated method stub
		
	}

}
