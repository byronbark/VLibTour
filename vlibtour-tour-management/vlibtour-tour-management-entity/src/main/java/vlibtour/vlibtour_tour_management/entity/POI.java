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
package vlibtour.vlibtour_tour_management.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The entity bean defining a point of interest (POI). A {@link Tour} is a
 * sequence of points of interest.
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Denis Conan
 */
@Entity
@Table(name="poi")
public class POI implements Serializable {
	
	/**
	 * the serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id of the POI.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	/**
	 * the name of the POI.
	 */
	private String name;
	/**
	 * the description of the POI.
	 */
	private String description;
	/**
	 * the collection of corresponding tour.
	 */
	@ManyToMany(mappedBy = "pois")
	private Collection<Tour> tours ;
	/**
	 * the latitude.
	 */
	private double latitude;
	/**
	 * the longitude.
	 */
	private double longitude;
	/**
	 * POI constructor.
	 */
    public POI() {
		
	}
	
    public POI(String name, String description,double latitude, double longitude) {
		this.name=name;
		this.description=description;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public String getName() {
		return name;
	}
	/**
	 * sets the name.
	 * 
	 * @param name
	 *            the new name.
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * gets the description.
	 * 
	 * @return the description.
	 */


	public String getDescription() {
		return description;
	}
	/**
	 * sets the description.
	 * 
	 * @param description
	 *            the new description.
	 */
	public void setDescription(final String description) {
		this.description= description;
	}
	/**
	 * gets the latitude.
	 * 
	 * @return the latitude.
	 */


	public double getLatitude() {
		return latitude;
	}
	/**
	 * gets the longitude.
	 * 
	 * @return the longitude.
	 */


	public double getLongitude() {
		return latitude;
	}
	/**
	 * sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude.
	 */
	public void setLatitude(final double latitude) {
		this.latitude=latitude;
	}
	/**
	 * sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude.
	 */
	public void setLongitude(final double longitude) {
		this.longitude=longitude;
	}
	/**
	 * gets the identifier.
	 * 
	 * @return the identifier.
	 */

	@Id
	public int getId() {
		return id;
	}

	/**
	 * gets the collection of tours.
	 * 
	 * @return the tour.
	 */

	
	public Collection<Tour> getTours() {
		return tours;
	}
	/**
	 * sets the corresponding tour.
	 * 
	 * @param tours
	 *            the new corresponding tour.
	 */
	public void setTour(final Collection<Tour> tours) {
		this.tours = tours;
	}
	
	public String toString() {
	      return "[ POI, id: "+ id + ",name: " + name +",latitude: "+latitude+",longitude: "+longitude+"]";
	   }
	
	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        POI poi = (POI) obj;
        return id == poi.id
                && ((name != null) && (name.equals(poi.getName()))
                 && (latitude == poi.latitude)
                && (longitude == poi.longitude));
    }



}
