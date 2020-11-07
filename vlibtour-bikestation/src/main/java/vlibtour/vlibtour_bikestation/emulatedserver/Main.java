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
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * Main class.
 *
 */
public final class Main {
	/**
	 * Base URI the Grizzly HTTP server will listen on.
	 */
	private static String baseURI;

	/**
	 * default private constructor to avoid instantiation.
	 */
	private Main() {
	}

	/**
	 * starts Grizzly HTTP server exposing JAX-RS resources.
	 * 
	 * @return Grizzly HTTP server.
	 * @throws IOException
	 *             problem with HTTP connection.
	 */
	public static HttpServer startServer() throws IOException {
		// server address defined in a property file
		Properties properties = new Properties();
		FileInputStream input = new FileInputStream("src/main/resources/rest.properties");
		properties.load(input);
		baseURI = properties.getProperty("jcdecaux.rooturl");

		// create a resource config that scans for JAX-RS resources and providers
		// in the server package
		final ResourceConfig rc = new ResourceConfig().packages("vlibtour.vlibtour_bikestation.emulatedserver");
		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
	}

	/**
	 * the main method.
	 * 
	 * @param args
	 *            there is no command line arguments.
	 * @throws IOException
	 *             problem when reading {@code stdin}.
	 */
	public static void main(final String[] args) throws IOException {
		final HttpServer server = startServer();
		System.out.println(String.format(
				"Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...", baseURI));
		System.in.read();
		server.shutdownNow();
	}
}
