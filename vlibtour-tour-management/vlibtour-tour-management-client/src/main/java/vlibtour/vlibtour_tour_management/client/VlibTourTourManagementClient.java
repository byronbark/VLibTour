/**
This file is part of the course CSC5002.

Copyright (C) 2017-2019 Télécom SudParis

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

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_tour_management.client;

import javax.naming.InitialContext;


import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.Tour;

/**
 * This class defines the client that manages POI and tour for testing concerns.
 * 
 * @author Nada Touil
 * @author Farah Kilani
 */
public class VlibTourTourManagementClient {
	/**
	 * utility class with no instance.
	 */
	private VlibTourTourManagementClient() {
	}

	
	/**
	 * the main of the client.
	 * 
	 * @param args
	 *            no command line arguments.
	 */
	public static void main(final String[] args) {
		VlibTourTourManagement sb;
		Tour t;
		try {
			InitialContext ic = new InitialContext();
			sb = (VlibTourTourManagement) ic.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
			System.out.println("Inserting tour and pois... " + sb.testInsert());
			// Test query and navigation
			System.out.println("Verifying that all are inserted... " + sb.verifyInsert());
			// Get a detached instance
			t = sb.findAllToursWithName("Paris Tour").iterator().next();
			// Remove entity Tour
			System.out.println("Removing entity Tour... " + sb.testDeleteTour(t));
			// Query the results
			System.out.println("Verifying that all tours are removed... " + sb.verifyDeleteTour());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
