/**
This file is part of the course CSC5002.

Copyright (C) 2017 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Chantal Taconet
Contributor(s): Denis Conan
 */
package vlibtour.vlibtour_bikestation.emulatedserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

import vlibtour.vlibtour_bikestation.client.generated_from_json.Station;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 * The bike stations emulated REST server.
 */
@Path("/stations")
public final class StationsRest {
		
	/**
	 * the collection of stations.
	 */
	private Stations stations;
	/**
	 * the file name of the data base.
	 */
	private String fileName = "src/main/resources/paris.json";


	/**
	 * reads the collection of stations from a file.
	 * 
	 * @param fileName
	 *            the name of the file.
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	private void getStationsFromFile(final String fileName) throws JAXBException, IOException {
		ObjectMapper mapper = new ObjectMapper(); //jackson class for converting from json to java 
		List<Station> stationList; //JSON from file to Object
		stationList = Arrays.asList(mapper.readValue(new File(fileName), Station[].class)); // get the array of stations and convert to a list
	    stations=new Stations(stationList);// create a Stations instance 
	}



	/**
	 * gets all the stations from a JSON document.
	 * 
	 * @return the collection of stations
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Stations allSkiers() throws JAXBException, IOException {
		getStationsFromFile(fileName);
		return stations;
	}

	/**
	 * gets all the stations as plain text.
	 * 
	 * @return the collection of stations
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	@GET
	@Path("/alltxt")
	@Produces(MediaType.TEXT_PLAIN)
	public String allTextStations() throws JAXBException, IOException {
		getStationsFromFile(fileName);
		return stations.toString();
	}


	/**
	 * gets all the stations of a given city as an JSON document.
	 * 
	 * @param contract
	 *            the criterion for the selection.
	 * @return the collection of stations
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/contract")

	public Stations agexml(@QueryParam("contract") final String contract) throws JAXBException, IOException {
		getStationsFromFile(fileName);
		return stations.lookupCity(contract);
	}

	/**
	 * gets a station of a given city as a JSON text.
	 * 
	 * @param contract_name
	 *            the criterion for the selection.
	 * @param name
	 *            the criterion for the selection.
	 * @return the station
	 * @throws JAXBException
	 *             the problem when un-marshaling.
	 * @throws IOException
	 *             the problem when reading to the file.
	 */
	@GET
	@Path("/station")
	@Produces({ MediaType.APPLICATION_JSON })
	public Station stationjson(@QueryParam("contract_name") final String contract_name,@QueryParam("name") final String name) throws JAXBException, IOException {
		getStationsFromFile(fileName);
		return stations.lookupCity(contract_name).lookupName(name);
	}
	
}
