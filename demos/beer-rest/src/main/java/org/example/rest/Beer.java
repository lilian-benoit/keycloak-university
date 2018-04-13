package org.example.rest;

import org.apache.commons.io.IOUtils;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Path("/beers")
public class Beer {

	@GET
	@Produces("application/json")
	public String doGet() throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream("/beers.json"), StandardCharsets.UTF_8.name());
	}
}