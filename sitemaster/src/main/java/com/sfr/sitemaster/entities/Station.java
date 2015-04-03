
package com.sfr.sitemaster.entities;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.sfr.apicore.entities.SFREntityObject;

/**
 * Station Entity
 * @author sourabh
 *
 */

@Entity(value = "stations", noClassnameStored = true)
public class Station extends SFREntityObject {
	
	private static final long serialVersionUID = 1L;

	@Property("sid")
    private String stationID;

	@Property("sloc")
	private String stationLocation;
	
	@Property("sname")
	private String stationName;
	
	
	public Station()
	{
		super();
	}


	public String getStationID() {
		return stationID;
	}


	public void setStationID(String stationID) {
		this.stationID = stationID;
	}


	public String getStationLocation() {
		return stationLocation;
	}


	public void setStationLocation(String stationLocation) {
		this.stationLocation = stationLocation;
	}


	public String getStationName() {
		return stationName;
	}


	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	
    @Override
    public boolean equals(final Object obj) { //NOPMD
        return super.equals(obj);
    }

    @Override
    public int hashCode() {     //NOPMD
        return super.hashCode();
    }
	
}
