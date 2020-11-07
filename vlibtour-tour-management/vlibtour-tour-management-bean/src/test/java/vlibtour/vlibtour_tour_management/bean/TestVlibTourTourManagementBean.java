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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.validation.constraints.AssertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;

public class TestVlibTourTourManagementBean {
	
	private static EJBContainer ec;
	private static Context ctx;
	private static VlibTourTourManagement sb;
	
	@BeforeClass
	public static void setUp() throws Exception {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put(EJBContainer.MODULES, new File("target/classes"));
	    ec = EJBContainer.createEJBContainer(properties);
	    ctx = ec.getContext();
	    sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
	}

	
	@Test(expected = VlibTourTourManagementException.class)
	public void createPOITest1() throws Exception {
		sb.createPOI("", 48.864824, 2.334595);	
	}
	
	
	@Test(expected = VlibTourTourManagementException.class)
	public void findPOIWithPIDTest1() throws Exception {
		sb.findPOIWithPID(0);
	}

	
	@Test
	public void findAllPOIsWithNameTest1() throws Exception {
		sb.createPOI("1 Champs-Elysees",48.864824, 2.334595);
		Collection<POI> c=sb.findAllPOIsWithName("1 Champs-Elysees");
		Assert.assertEquals(c.size(),1);
		Assert.assertEquals(c.iterator().next().getName(),"1 Champs-Elysees");
	}

	
	@Test
	public void findAllPOIsTest1() throws Exception {
		Collection <POI> c = new ArrayList <POI>();
		Collection <POI> c1 = new ArrayList <POI>();

		POI poi1= sb.createPOI("1 Champs-Elysees, Paris, France",48.864824, 2.334595);
		POI poi2= sb.createPOI("99 Main Street, London, UK",486.864824, 2.334595);
		c.add(poi1);
		c.add(poi2);
		//list pois
		c1=new ArrayList<POI>(sb.listPOIs());
		Assert.assertArrayEquals(c.toArray(),c1.toArray());
	}

   
	@Test(expected = VlibTourTourManagementException.class)
	public void createTourTest1() throws Exception {
		//test tour creation

		POI poi1 = new POI();
		poi1.setName("1 Champs-Elysees, Paris, France");
		POI poi2 = new POI();
		poi2.setName("99 Main Street, London, UK");
		Collection <POI> c = new ArrayList <POI>();
		c.add(poi1);
		c.add(poi2);
		
	    sb.createTour("",c);
		
	}


	@Test(expected = VlibTourTourManagementException.class)
	public void findTourWithTIDTest1() throws Exception {
		sb.findTourWithTPID(0);
	}

    
	@Test
	public void findAllToursWithNameTest1() throws Exception {
		POI poi1= sb.createPOI("1 Champs-Elysees, Paris, France",48.864824, 2.334595);
		POI poi2= sb.createPOI("99 Main Street, London, UK",486.864824, 2.334595);
		Collection<POI> c= new ArrayList<POI>();
		Collection<Tour> c1= new ArrayList<Tour>();

		c.add(poi1);
		c.add(poi2);
		Tour tour1= sb.createTour("Paris Tour", c);
		c1=sb.findAllToursWithName("Paris Tour");

		Assert.assertEquals(c1.iterator().next().getName(),tour1.getName());
		
	}

	
	@Test
	public void findAllToursTest1() throws Exception {
		POI poi1= sb.createPOI("1 Champs-Elysees, Paris, France",48.864824, 2.334595);
		POI poi2= sb.createPOI("99 Main Street, London, UK",486.864824, 2.334595);
		Collection<POI> c= new ArrayList<POI>();
		Collection<Tour> c1= new ArrayList<Tour>();

		c.add(poi1);
		c.add(poi2);
		Tour tour1= sb.createTour("Tour1", c);
		Tour tour2= sb.createTour("Tour2", c);
		c1=sb.listTours();
		Assert.assertTrue(c1.size()==2);
		Iterator<Tour> it =c1.iterator();
		Assert.assertEquals(it.next(),tour1);
		if (it.hasNext()) {
		Assert.assertEquals(it.next(),tour2);
		}
		
	}

	@After
	public void tearDown() throws Exception {
		
		// remove the created attributes 
				Collection<POI> pois =sb.listPOIs();
				Collection<Tour> tours=sb.listTours();
				
				for (POI poi:pois) {
					sb.removePOI(poi);
				}
				for (Tour tour:tours) {
					sb.removeTour(tour);
				}
			}
	
	

	@AfterClass
	public static void tearDownClass() throws Exception {
		if (ec != null) {
			ec.close();
		}
	}
}
