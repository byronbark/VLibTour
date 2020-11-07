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
package vlibtour.vlibtour_client_scenario;

import static vlibtour.vlibtour_visit_emulation.Log.EMULATION;
import static vlibtour.vlibtour_visit_emulation.Log.LOG_ON;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;


import javax.naming.NamingException;

import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;


import vlibtour.vlibtour_client_emulation_visit.VLibTourVisitEmulationClient;
import vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient;
import vlibtour.vlibtour_client_lobby_room.VLibTourLobbyRoomClient;
import vlibtour.vlibtour_client_scenario.map_viewer.BasicMap;
import vlibtour.vlibtour_client_scenario.map_viewer.MapHelper;
import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;
import vlibtour.vlibtour_visit_emulation.GPSPosition;
import vlibtour.vlibtour_visit_emulation.Position;

/**
 * This class is the client application of the tourists. The access to the lobby
 * room server, to the group communication system, and to the visit emulation
 * server visit should be implemented using the design pattern Delegation (see
 * https://en.wikipedia.org/wiki/Delegation_pattern).
 * 
 * A client creates two queues to receive messages from the broker; the binding
 * keys are of the form "{@code *.*.identity}" and "{@code *.*.location}" while
 * the routing keys are of the form "{@code sender.receiver.identity|location}".
 * 
 * This class uses the classes {@code MapHelper} and {@code BasicMap} for
 * displaying the tourists on the map of Paris. Use the attributes for the
 * color, the map, the map marker dot, etc.
 * 
 * @author Denis Conan
 */
public class VLibTourVisitTouristApplication {
	/**
	 * the colour onto the map of the user identifier of the first tourist.
	 */
	private static final Color colorJoe = Color.RED;
	/**
	 * the colour onto the map of the user identifier of the second tourist.
	 */
	private static final Color colorAvrel = Color.GREEN;
	/**
	 * the map to manipulate. Not all the clients need to have a map; therefore we
	 * use an optional.
	 */
	private Optional<BasicMap> map = Optional.empty();
	/**
	 * the dot on the map for the first tourist.
	 */
	@SuppressWarnings("unused")
	private MapMarkerDot dot1;
	/**
	 * the dot on the map for the second tourist.
	 */
	private MapMarkerDot dot;
	/**
	 * the list of the users in the same tour.
	 */
	private  Map <String,MapMarkerDot> users ;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_emulation_visit.VLibTourVisitEmulationClient}.
	 */
	private static VLibTourVisitEmulationClient emulationVisitClient;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient}.
	 */
	
	private VLibTourLobbyRoomClient lobbyRoomClient;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient}.
	 * The identifier of the user is obtained from this reference.
	 */
	@SuppressWarnings("unused")
	private VLibTourGroupCommunicationSystemClient groupCommClient;

	/**
	 * creates a client application, which will join a group that must already
	 * exist. The group identifier is optional whether this is the first user of the
	 * group ---i.e. the group identifier is built upon the user identifier.
	 * Concerning the tour identifier, it must be provided by the calling method.
	 * 
	 * @param tourId  the tour identifier of this application.
	 * @param groupId the group identifier of this client application.
	 * @param userId  the user identifier of this client application.
	 * @throws InAMQPPartException             the exception thrown in case of a
	 *                                         problem in the AMQP part.
	 * @throws VlibTourTourManagementException problem in the name or description of
	 *                                         POIs.
	 * @throws IOException                     problem when setting the
	 *                                         communication configuration with the
	 *                                         broker.
	 * @throws TimeoutException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws JsonRpcException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws InterruptedException            thread interrupted in call sleep.
	 * @throws NamingException                 the EJB server has not been found
	 *                                         when getting the tour identifier.
	 */
	public VLibTourVisitTouristApplication(final String tourId, final Optional<String> groupId, final String userId)
			throws InAMQPPartException, VlibTourTourManagementException, IOException, JsonRpcException,
			TimeoutException, InterruptedException, NamingException {
	    emulationVisitClient = new VLibTourVisitEmulationClient();
	    users = new HashMap <String,MapMarkerDot>() ;
	}

	/**
	 * executes the tourist application. <br>
	 * We prefer insert comments in the method instead of detailing the method here.
	 * 
	 * @param args the command line arguments.
	 * @throws Exception all the potential problems (since this is a demonstrator,
	 *                   apply the strategy "fail fast").
	 */
	public static void main(final String[] args) throws Exception {
		String usage = "USAGE: " + VLibTourVisitTouristApplication.class.getCanonicalName()
				+ " userId (either Joe or Avrel)   tourId  <optional> groupId"  ;
		if ((args.length != 2)&&(args.length != 3)) {
			throw new IllegalArgumentException(usage);
		}
		String userId = args[0];
		String tourId = args[1];
		String url="";
		final VLibTourVisitTouristApplication client = new VLibTourVisitTouristApplication("The unusual Paris",Optional.empty(),userId) ;
		if (args.length == 3) {
			String groupId = args[2];
			client.lobbyRoomClient = new VLibTourLobbyRoomClient(groupId, tourId, userId);
			url =  client.lobbyRoomClient.joinAGroup(groupId, userId);
		}
		else if (args.length == 2) {	
			client.lobbyRoomClient = new VLibTourLobbyRoomClient(tourId, userId);
			url =  client.lobbyRoomClient.createGroupAndJoinIt(tourId, userId);
		}
		client.groupCommClient = new VLibTourGroupCommunicationSystemClient(url);
		
		if (LOG_ON && EMULATION.isInfoEnabled()) {
			EMULATION.info(userId + "'s application is starting");
		}
		// the tour is empty, this client gets it from the data base (first found)
		// start the VLibTourVisitTouristApplication applications
		// set the map viewer of the scenario (if this is this client application that
		// has created the group [see #VLibTourVisitTouristApplication(...)])
		// the following code should be completed
	
		/*if (client == null) {
			throw new UnsupportedOperationException("No implemented, yet");
		}*/
	
		client.map = Optional.of(MapHelper.createMapWithCenterAndZoomLevel(48.851412, 2.343166, 14));
	

		Font font = new Font("name", Font.BOLD, 20);
		client.map.ifPresent(m -> {
			MapHelper.addMarkerDotOnMap(m, 48.871799, 2.342355, Color.BLACK, font, "Musée Grevin");
			MapHelper.addMarkerDotOnMap(m, 48.860959, 2.335757, Color.BLACK, font, "Pyramide du Louvres");
			MapHelper.addMarkerDotOnMap(m, 48.833566, 2.332416, Color.BLACK, font, "Les catacombes");
			Position oldPosition = emulationVisitClient.getCurrentPosition(userId);
			client.dot = MapHelper.addTouristOnMap(client.map.get(), Color.ORANGE, font,userId,oldPosition);
			client.users.put(userId, client.dot);
			client.map.get().repaint();
	
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		
		// start the consumption of messages (e.g. positions of group members) from the group communication system 
		
		Consumer consumer = new DefaultConsumer(client.groupCommClient .getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws UnsupportedEncodingException {
				
				String message = new String(body, "UTF-8");
				String longitude , latitude;
				longitude = message.split(" ")[0].split("=")[1];
				latitude = message.split(" ")[2].split("=")[1];
				GPSPosition gps = new GPSPosition (Double.parseDouble(latitude), Double.parseDouble(longitude));
				Position position = new Position ("position", gps, "");
				String otherUserId = envelope.getRoutingKey().split("\\.")[0];
				if (!client.users.containsKey(otherUserId)) {
					MapMarkerDot  dotOther = MapHelper.addTouristOnMap(client.map.get(), Color.BLUE, font,otherUserId, position);
					client.users.put(otherUserId, dotOther);
				}
				else {
					MapHelper.moveTouristOnMap(client.users.get(otherUserId),position);
				}
				client.map.get().repaint();
				System.out.println("ReceiveLogsDirect2 Received '" + envelope.getRoutingKey() + "':'" + "otherUserId=" + otherUserId);
			}
		};
		
		client.groupCommClient .addConsumer(consumer);
		client.groupCommClient .startConsumption();
		
		
		while (true) {

			Position nextStep = emulationVisitClient.stepsInVisit(userId);
			Position position = emulationVisitClient.stepInCurrentPath(userId);
			String json = position.getGpsPosition().toString();
			MapHelper.moveTouristOnMap(client.dot, position);
			client.groupCommClient.publish(json);
			client.map.get().repaint();
			Thread.sleep(1000);
		
			if (position.getName().equals(nextStep.getName())) {
				if (LOG_ON && EMULATION.isInfoEnabled()) {
					EMULATION.info(userId + "has ended the trip");
				}
				break;
		   }		
		}
		
		// closes the channel and the connection.
		// TODO
		// exit this main
		// e.g. System.exit...
		//System.exit(0);
		System.exit(0);
		
	}
}

