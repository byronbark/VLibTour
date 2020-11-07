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
package vlibtour.vlibtour_bikestation.client;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import vlibtour.vlibtour_bikestation.client.generated_from_json.Station;

import javax.ws.rs.core.MediaType;
/**
 * The bike station REST client example.
 */
public final class BikeStationClient {
	
	/**
	 * the URI.
	 */
	private static String restURI;

	private BikeStationClient() {
		
	}
	
	/**
	 * the main method.
	 * 
	 * @param args
	 *            there is no command line arguments.
	 * @throws IOException
	 *             problem with HTTP connection.
	 */
	public static void main(final String[] args) throws IOException {
		
		Properties properties = new Properties();
		FileInputStream input = new FileInputStream("src/main/resources/rest.properties");
		properties.load(input);
		restURI = properties.getProperty("jcdecaux.rooturl");
		Client client = ClientBuilder.newClient();
		URI uri = UriBuilder.fromUri(restURI).build();
		WebTarget service = client.target(uri);
		//Station station = service.path("/stations").queryParam("contract","lyon").queryParam("apiKey","e282afdd13aca4cba4fb50.5603d5224d4a9cad03").queryParam("number","2010").request().accept(MediaType.APPLICATION_JSON).get(Station.class);
		Station station = service.path("stations/station/").queryParam("contract_name","Paris").queryParam("name","31705 - CHAMPEAUX (BAGNOLET)").request().accept(MediaType.APPLICATION_JSON).get().readEntity(Station.class);
		System.out.println("station infos : \n"+ station.toString());
	}
	
	
}
