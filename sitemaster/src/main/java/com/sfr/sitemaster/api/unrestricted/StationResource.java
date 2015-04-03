package com.sfr.sitemaster.api.unrestricted;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.pojo.exception.DBException;
import com.sfr.sitemaster.domainservices.StationService;

@Path("/station")
public class StationResource extends SFRBaseResource {
	
	@Inject
	StationService facade;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/create/")
	public Response createform(@FormParam("sid") String sid,
			@FormParam("sname") String sname, 
			@FormParam("sloc") String sloc) {
 
		try {
			facade.createNewStation(sid, sname, sloc);
		} catch (DBException e) {
			return Response.status(500).entity("Unable to perform creation.")
                    .build();
		}
		
		return Response.status(200).entity("station with id : " + sid + " " + sname + " " + sloc +
				" was created successfully").build();
 
	}

}
