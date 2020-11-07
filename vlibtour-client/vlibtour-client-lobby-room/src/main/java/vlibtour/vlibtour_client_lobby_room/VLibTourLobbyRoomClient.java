/**
This file is part of the course CSC5002.

Copyright (C) 2019 Télécom SudParis

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
package vlibtour.vlibtour_client_lobby_room;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcClient;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;
import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;

/**
 * This class is the client application of the tourists.
 * 
 * @author Denis Conan
 */
public class VLibTourLobbyRoomClient {
	/**
	 * the connection to the RabbitMQ broker.
	 */
	private Connection connection;
	/**
	 * the channel for producing and consuming.
	 */
	private Channel channel;
	/**
	 * the RabbitMQ JSON RPC client.
	 */
	private JsonRpcClient jsonRpcClient;
	/**
	 * the lobbyroom service.
	 */
	private VLibTourLobbyService client;
	/**
	 * the group Id
	 */
	private String groupId;

	/**
	 * default constructor of the RPC client that will create a group.
	 * 
	 * @param  tourId
	 *                 the id of the tour
	 * @param  userId
	 * 					the id of the user           
	 * @throws IOException
	 *             the communication problems.
	 * @throws TimeoutException
	 *             broker to long to connect to.
	 * @throws JsonRpcException
	 *             JSON problem in marshaling or un-marshaling.
	 */
	
	public VLibTourLobbyRoomClient(String tourId, String userId) throws IOException, TimeoutException, JsonRpcException {
		if (userId==null || userId.equals("")) {
			throw new IllegalArgumentException("userId cannot be empty");
		}
		
		if (tourId==null || tourId.equals("")){
			throw new IllegalArgumentException(" tourId cannot be empty");
		}
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		jsonRpcClient = new JsonRpcClient(channel,VLibTourLobbyService.EXCHANGE_NAME, "lobby");
		client = (VLibTourLobbyService) jsonRpcClient.createProxy(VLibTourLobbyService.class);
		groupId=tourId+'_'+userId;
	}

	/**
	 * default constructor of the RPC client that will join an existing group.
	 * 
	 * @param  groupId
	 *                 the id of the group
	 * @param  tourId
	 *                 the id of the tour
	 * @param  userId
	 * 					the id of the user    
	 * @throws IOException
	 *             the communication problems.
	 * @throws TimeoutException
	 *             broker to long to connect to.
	 * @throws JsonRpcException
	 *             JSON problem in marshaling or un-marshaling.
	 */
	public VLibTourLobbyRoomClient(String groupId, String tourId, String userId) throws IOException, TimeoutException, JsonRpcException {
		if (userId==null || userId.equals("")) {
			throw new IllegalArgumentException("userId cannot be empty");
		}
		
		if (tourId==null || tourId.equals("")){
			throw new IllegalArgumentException(" tourId cannot be empty");
		}
		
		if (groupId==null || groupId.equals("")){
			throw new IllegalArgumentException(" groupId cannot be empty");
		}
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		jsonRpcClient = new JsonRpcClient(channel,VLibTourLobbyService.EXCHANGE_NAME, "lobby");
		client = (VLibTourLobbyService) jsonRpcClient.createProxy(VLibTourLobbyService.class);
		this.groupId= groupId;
	}

	/**
	 * calls the create group service.
	 * 
	 * @param tourId
	 *            the id of the group to create.
	 * @param userId
	 *            the id of the creator.
	 * @return the url to connect to communication system
	 */
	public String createGroupAndJoinIt(final String tourId, final String userId ) {
		System.out.println(" [x] Requesting createGroupAndJoinIt (" + tourId + userId+ ")");
		String result = client.createGroupAndJoinIt(tourId, userId);
		System.out.println(" [.] Got '" + result + "'");
		return result;
	}

	/**
	 * calls the join group service.
	 * 
	 * @param groupId
	 *            the id of the group to create.
	 * @param userId
	 *            the id of the creator.
	 * @return the url to connect to communication system
	 */
	public String joinAGroup(final String groupId, final String userId ) {
		System.out.println(" [x] Requesting join (" + groupId + userId+ ")");
		String result = client.joinAGroup(groupId, userId);
		System.out.println(" [.] Got '" + result + "'");
		return result;
	}
	
	
	static String computeGroupId(String url) throws UnsupportedEncodingException, URISyntaxException {
		String result = VLibTourLobbyService.computeGroupId(url);
		return result;
	}
	/**
	 * closes the JSON RPC client, the channel and the connection with the broker.
	 * 
	 * @throws IOException
	 *             communication problem.
	 * @throws TimeoutException
	 *             broker to long to communicate with.
	 */
	public void close() throws IOException, TimeoutException {
		jsonRpcClient.close();
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * the main method of the example.
	 * 
	 * @param argv
	 *            the command line argument is the number of the call.
	 * @throws Exception
	 *             communication problem with the broker.
	 */
	public static void main(final String[] argv) throws Exception {
		
		String userId = argv [0];
		String tourId = argv[1];
		String groupId = tourId+'_'+userId;
		VLibTourLobbyRoomClient rpcClient = new VLibTourLobbyRoomClient(tourId, userId);
		String url=rpcClient.createGroupAndJoinIt(argv[0], argv[1]);
		System.out.println("L'url vers le group créé est :"+ url);
		VLibTourLobbyRoomClient rpcClient1 = new VLibTourLobbyRoomClient(groupId,tourId, userId);
		String url1 =rpcClient.joinAGroup(argv[0], argv[1]);
		System.out.println("L'url vers votre groupe est :"+ url1);
		rpcClient.close();
		rpcClient1.close();
	}
}
