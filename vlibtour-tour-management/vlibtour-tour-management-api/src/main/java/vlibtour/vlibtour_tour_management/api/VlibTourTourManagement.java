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
package vlibtour.vlibtour_tour_management.api;

import java.util.Collection;

import javax.ejb.Remote;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;

/**
 * This interface defines the operation for managing POIs and Tours.
 * 
 * @author Denis Conan
 */
@Remote
public interface VlibTourTourManagement {
	
	/**
	 * Insert Tour and POIs.
	 * 
	 * @return the string "OK" if there is no problem.
	 */
	String testInsert();

	/**
	 * verifies the insertion.
	 * 
	 * @return the string "OK" if there is no problem.
	 * @throws VlibTourTourManagementException 
	 */
	String verifyInsert() throws VlibTourTourManagementException;
	/**
	 * deletes the given tour.
	 * 
	 * @param t  the tour.
	 * @return the string "OK" if there is no problem.
	 */
	String testDeleteTour(Tour t);
	/**
	 * deletes the given poi.
	 * 
	 * @param p  the poi.
	 * @return the string "OK" if there is no problem.
	 */
	String testDeletePoi(POI  p);

	/**
	 * verifies the deletion of the tour.
	 * 
	 * @return the string "OK" if there is no problem.
	 */
	String verifyDeleteTour();
	/**
	 * verifies the deletion of the poi.
	 * 
	 * @return the string "OK" if there is no problem.
	 */
	String verifyDeletePoi();
	/**
	 * list existing tours.
	 * 
	 * @return the collection of the existing tours .
	 */
	Collection<Tour> listTours();
	/**
	 * list existing pois.
	 * 
	 * @return the collection of the existing pois .
	 */
	Collection<POI> listPOIs();
	/**
	 * list existing tours with a specific name.
	 * @param name name of the searched tour.
	 * @return the collection of the existing tours with a specific name .
	 */
	Collection<Tour> findAllToursWithName(String name);
	/**
	 * list existing pois with a specific name.
	 * @param name name of the searched poi.
	 * @return the collection of the existing pois with a specific name .
	 */
	Collection<POI> findAllPOIsWithName(String name);

	/**
	 *  create a tour .
	 * @param name name of the tour to create.
	 * @param pois collection of pois that constitute the tour.
	 * @return tour created
	 * @throws VlibTourTourManagementException exception.
	 */
	Tour createTour(String name, Collection<POI> pois) throws VlibTourTourManagementException;	
	/**
	 * create a poi .
	 * @param name name of the poi.
	 * @param latitude  latitude de poi.
	 * @param longitude  longitude de poi.
	 * @return the created tour .
	 * @throws VlibTourTourManagementException exception.
	 */
	POI createPOI(String name, double latitude, double longitude) throws VlibTourTourManagementException;
	/**
	 * remove a poi .
	 * @param  poi to be removed.
	 */
	void removePOI(POI poi);
	/**
	 * remove a poi  from a tour .
	 * @param  poi to be removed.
	 * @param tour related to the poi to be removed
	 */
	void removePOIFromTour(POI poi, Tour tour);
	/**
	 * remove a tour .
	 * @param  tour to be removed.
	 */
	void removeTour(Tour tour);
	/**
	 * find a poi with its id .
	 * @param id  id of the poi to be removed.
	 * @throws VlibTourTourManagementException exception.
	 * @return poi found with specified id.
	 */
	POI findPOIWithPID(int id) throws VlibTourTourManagementException;
	/**
	 * find a tour with its id .
	 * @param id  id of the tour to be removed.
	 * @throws VlibTourTourManagementException exception.
	 * @return tour found with specified id.
	 */
	Tour findTourWithTPID(int id) throws VlibTourTourManagementException;
}
