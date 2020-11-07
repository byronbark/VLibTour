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
package vlibtour.vlibtour_tour_management.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;

/**
 * This class defines the EJB Bean of the VLibTour tour management.
 * 
 * @author Denis Conan
 */
@Stateless
public class VlibTourTourManagementBean implements VlibTourTourManagement {
	
	/**
	 * the reference to the entity manager, which persistence context is "pu1".
	 */
	@PersistenceContext(unitName = "pu1")
	private EntityManager em;
	
	public Collection<Tour> listTours(){
		Query q = em.createQuery("select t from Tour t");
		@SuppressWarnings("unchecked")
		Collection<Tour> results = q.getResultList();
		return results;		
	}
	
	public Collection<POI> listPOIs(){
		Query q = em.createQuery("select p from POI p");
		@SuppressWarnings("unchecked")
		Collection<POI> results = q.getResultList();
		return results;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Tour> findAllToursWithName (String name) {
		Query q = em.createQuery("select t from Tour t where t.name = :name");
		q.setParameter("name", name);
		return (Collection<Tour>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<POI> findAllPOIsWithName(String name) {
		Query q = em.createQuery("select p from POI p where p.name = :name");
		q.setParameter("name", name);
		return (Collection<POI>)  q.getResultList();
	}

	@Override
	public Tour createTour (String name, Collection<POI> pois) throws VlibTourTourManagementException {
		/*if (getTour(name)!= null)
			throw new VlibTourTourManagementException ("Tour already exists");*/
		if (name.equals("") || name.isEmpty())
			throw new VlibTourTourManagementException ("Tour name is empty");
		
		Tour tour = new Tour();
		tour.setName(name);
		tour.setPois(pois);
		em.persist(tour);
		return tour;	
	}
	@Override
	public POI createPOI (String name, double latitude,double longitude) throws VlibTourTourManagementException {
		if (name.equals("") || name.isEmpty()|| latitude==0 || longitude==0)
			throw new VlibTourTourManagementException ("POI attributes must be valid empty");
		POI poi = new POI();
		poi.setName(name);
		poi.setLatitude(latitude);
		poi.setLongitude(longitude);
		em.persist(poi);
		return poi;	
	}
	@Override
	public void removePOI (POI poi) {
		POI p0 = em.merge(poi);
		em.remove(p0);
	}
	
	@Override
	public void removePOIFromTour (POI poi, Tour tour) {
		tour.getPois().remove(poi);
	}
	
	@Override
	public void removeTour (Tour tour) {
		Tour t0 = em.merge(tour);
		em.remove(t0);
	}
	
	
	@Override
	public POI findPOIWithPID(int id) throws VlibTourTourManagementException {
		if (id==0)
			throw new VlibTourTourManagementException ("POI id isempty");
		Query q = em.createQuery("select p from POI p where p.id = :id");
		q.setParameter("id", id);
		return (POI) q.getSingleResult();
	}
	
	@Override
	public Tour findTourWithTPID(int id) throws VlibTourTourManagementException {
		if (id == 0)
			throw new VlibTourTourManagementException ("Tour id is empty");
		Query q = em.createQuery("select t from Tour t where t.id = :id");
		q.setParameter("id", id);
		return (Tour) q.getSingleResult();
	}

	@Override
	public String testInsert() {
		// Create a new tour
		Tour tour1 = new Tour();
		tour1.setName("Paris Tour");
		// Persist the tour
		em.persist(tour1);
		// Create 2 POIs
		POI poi1 = new POI();
		poi1.setName("1 Champs-Elysees, Paris, France");
		POI poi2 = new POI();
		poi2.setName("99 Main Street, London, UK");
		em.persist(poi1);
		em.persist(poi2);
        Collection<POI> c= new ArrayList<POI>();
        c.add(poi1);
        c.add(poi2);
		tour1.setPois(c);
		
		return "OK";	
	}

	@Override
	public String verifyInsert() throws VlibTourTourManagementException {
	
		Collection<Tour> c = findAllToursWithName("Paris Tour");
		if (c.size() == 0)
			throw new VlibTourTourManagementException ("Unexpected number of tours , Tour Paris Tour should be created ");
		Tour tour1 = c.iterator().next();
		Collection<POI> orders = tour1.getPois();
		if (orders == null || orders.size() != 2) {
			throw new RuntimeException(
					"Unexpected number of orders: " + ((orders == null) ? "null" : "" + orders.size()));
		}
		return "OK";
	}

	@Override
	public String testDeleteTour(Tour t) {
		Tour t0 = em.merge(t);
		em.remove(t0);
		
		return "OK";
	}
	
	@Override
	public String testDeletePoi(POI p) {
		POI p0 = em.merge(p);
		// Delete all records.
		
		em.remove(p0);
		
		return "OK";
	}
	
	@Override
	public String verifyDeleteTour() {
		// TODO Auto-generated method stub
		Query q = em.createQuery("select t from Tour t");
		@SuppressWarnings("rawtypes")
		List results = q.getResultList();
		if (results == null || results.size() != 0) {
			throw new RuntimeException("Unexpected number of tours after delete results : " + results.size());
		}
		return "OK";	
	}
	
	@Override
	public String verifyDeletePoi() {
		Query q = em.createQuery("select p from POI p");
		  @SuppressWarnings("rawtypes")
		List results = q.getResultList();
		if (results == null || results.size() != 0) {
			throw new RuntimeException("Unexpected number of pois after delete");
		}
		return "OK";
	}
	
}
