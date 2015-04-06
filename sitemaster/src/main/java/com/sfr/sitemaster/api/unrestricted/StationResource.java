package com.sfr.sitemaster.api.unrestricted;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.pojo.exception.DBException;
import com.sfr.sitemaster.domainservices.StationService;
import com.sfr.sitemaster.entities.Station;

@Path("/station")
public class StationResource extends SFRBaseResource {

	@Inject
	StationService facade;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/create/")
	public Response createStation(@FormParam("sid") String sid,
			@FormParam("sname") String sname, @FormParam("sloc") String sloc) {

		try {
			facade.createNewStation(sid, sname, sloc);
		} catch (DBException e) {
			return Response.status(500).entity("Unable to perform creation.")
					.build();
		}

		return Response
				.status(201)
				.entity("station with id : " + sid + " " + sname + " " + sloc
						+ " was created successfully").build();

	}

	@DELETE
	@Path("/delete/{sid}")
	public Response deleteStation(@PathParam("sid") String sid) {

		try {
			facade.removeExistingStation(sid);
		} catch (DBException e) {
			return Response.status(500).entity("Unable to perform creation.")
					.build();
		}

		return Response
				.status(200)
				.entity("station with id : " + sid
						+ " was deleted successfully").build();

	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/update/")
	public Response updateStation(@FormParam("sid") String sid,
			@FormParam("sname") String sname, @FormParam("sloc") String sloc) {

		try {
			facade.updateExistingStation(sid, sname, sloc);
		} catch (DBException e) {
			return Response.status(500).entity("Unable to perform creation.")
					.build();
		}

		return Response
				.status(200)
				.entity("station with id : " + sid + " " + sname + " " + sloc
						+ " updated successfully").build();

	}
	
	@GET
	@Path("/findstation/{sid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStationById(@PathParam("sid") String sid) {
		final JSONObject obj = new JSONObject();

		try {
			Station station = facade.findExistingStationFromID(sid); 
			obj.put("staionLocation",station.getStationLocation());
			obj.put("stationName",station.getStationName());
		} catch (DBException e) {
			return Response.status(500).entity("Unable to perform creation.")
					.build();
		}

		return Response
				.status(200)
				.entity("following are the station details with the sid : " + sid+""+obj).build();

	}
	
	@GET
	@Path("/findAllStation")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllStation() {
		 JSONArray masterJson = new JSONArray();
		try {
			
			 List<Station> stationList = facade.listAllExistingStations();
			 for(Station station : stationList){
				 JSONObject obj = new JSONObject();
			  obj.put("stationId", station.getStationID());
			  obj.put("stationLocation", station.getStationLocation());
			  obj.put("stationName", station.getStationName());
			 masterJson.put(obj);
			  
			 } 
			  return Response.status(200).entity("following are the station details:"+masterJson).build();
		} catch (JSONException ex) {
			ex.printStackTrace();
            return INVALID_JSON_SERVERSIDE;
        } catch (Exception ex) {
        	ex.printStackTrace();
            return buildJSONResponse(500, ex.getMessage());
        }
		
	}
}
