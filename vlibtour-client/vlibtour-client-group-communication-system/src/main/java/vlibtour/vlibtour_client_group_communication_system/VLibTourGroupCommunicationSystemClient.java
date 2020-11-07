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
package vlibtour.vlibtour_client_group_communication_system;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;
/**
 * This class is the client application of the tourists.
 * 
 * @author Denis Conan
 */
public class VLibTourGroupCommunicationSystemClient {
	/**
	 * the name of the exchange
	 */
	public  String EXCHANGE_NAME= null; 
	/**
	 * the name of the queue
	 */
	private Channel channel;
	private String queueName;
	private String routingKey ;
	private String bindingKey;
	private Consumer consumer;
	private Connection connection;
	private int nbMsgReceived = 0;
	
	/**
	 * creates the lobby room server and the corresponding JSON server object.
	 * @param url
	 * 				the url for the access to the group
	 */
	
	public VLibTourGroupCommunicationSystemClient (String url) throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(url);
		String userId = factory.getUsername();
		String groupId = VLibTourLobbyService .computeGroupId(url);		
		String tourId = groupId.split("_")[0];
		connection = factory.newConnection();
	    channel = connection.createChannel(); 
	    EXCHANGE_NAME= tourId ;
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	    routingKey = userId+"."+"all.#";
	    bindingKey = "*.all.#";
  	    queueName= channel.queueDeclare().getQueue();
  	    channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);

	    
	}
	
	public void publish (String message) throws UnsupportedEncodingException, IOException {
		
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
	}

    public  void addConsumer(Consumer consumer) throws IOException {
    	
    	  this.consumer=consumer;
    }
    
    public void startConsumption () throws IOException {
    	boolean autoAck = true;
    	channel.basicConsume(queueName, autoAck,consumer);
    }
    
    
    public Channel getChannel() {
    	return channel;
    }
    
    
    /**
	 * gets the number of messages received by this consumer.
	 * 
	 * @return the number of messages.
	 */
	public int getNbMsgReceived() {
		return nbMsgReceived;
	}
	
	public void incrementNbMsgReceived() {
		nbMsgReceived++;
	}
    /**
	 * closes the channel and the connection with the broker.
	 * 
	 * @throws IOException
	 *             communication problem.
	 * @throws TimeoutException
	 *             broker to long to communicate with.
	 */
	public void close() throws IOException, TimeoutException {
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
	
}
