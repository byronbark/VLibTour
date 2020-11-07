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
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;

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
	
	@BeforeClass
	public static void setUp() throws Exception {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put(EJBContainer.MODULES, new File("target/classes"));
	    ec = EJBContainer.createEJBContainer(properties);
	    ctx = ec.getContext();
	}

	@Ignore
	@Test(expected = VlibTourTourManagementException.class)
	public void createPOITest1() throws Exception {
		
	}
	
	@Ignore
	@Test(expected = VlibTourTourManagementException.class)
	public void findPOIWithPIDTest1() throws Exception {
	}

	@Ignore
	@Test
	public void findAllPOIsWithNameTest1() throws Exception {
	}

	@Ignore
	@Test
	public void findAllPOIsTest1() throws Exception {
	}

 
	@Test(expected = VlibTourTourManagementException.class)
	public void createTourTest1() throws Exception {
		//test tour creation
		VlibTourTourManagement sb = (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");

		POI poi1 = new POI();
		poi1.setName("1 Champs-Elysees, Paris, France");
		POI poi2 = new POI();
		poi2.setName("99 Main Street, London, UK");
		Collection <POI> c = new ArrayList <POI>();
		c.add(poi1);
		c.add(poi2);
		
	    //sb.createTour("",c);
		
	}

	@Ignore
	@Test(expected = VlibTourTourManagementException.class)
	public void findTourWithTIDTest1() throws Exception {
	}

	@Ignore
	@Test
	public void findAllToursWithNameTest1() throws Exception {
	}

	@Ignore
	@Test
	public void findAllToursTest1() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		if (ec != null) {
			ec.close();
		}
	}
	

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

}
