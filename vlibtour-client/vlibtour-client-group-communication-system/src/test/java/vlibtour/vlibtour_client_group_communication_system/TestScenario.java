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
// CHECKSTYLE:OFF
package vlibtour.vlibtour_client_group_communication_system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.http.client.Client;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;

public class TestScenario {

	private static Client c;

	@BeforeClass
	public static void setUp() throws IOException, InterruptedException, URISyntaxException {
		try {
			new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
		}
		Thread.sleep(1000);
		new ProcessBuilder("rabbitmq-server", "-detached").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "reset").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "start_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_vhost" ,"tour1_user1").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_vhost" ,"tour3_user3").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user" ,"user1", "password").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user" ,"user2", "password").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user" ,"user3", "password").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","tour1_user1", "user1", ".*", ".*", ".*").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","tour1_user1","user2", ".*", ".*", ".*").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","tour3_user3","user3", ".*", ".*", ".*").inheritIO().start().waitFor();

		c = new Client ("http://127.0.0.1:15672/api/" , "guest", "guest" );
	}
	//@Ignore
	@Test
	public void test() throws IOException, TimeoutException, InterruptedException, ExecutionException, InAMQPPartException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {

		VLibTourGroupCommunicationSystemClient v1 = new VLibTourGroupCommunicationSystemClient("amqp://user1:password@localhost:5672/tour1_user1");
		VLibTourGroupCommunicationSystemClient v2 = new VLibTourGroupCommunicationSystemClient("amqp://user2:password@localhost:5672/tour1_user1");
		VLibTourGroupCommunicationSystemClient v3 = new VLibTourGroupCommunicationSystemClient("amqp://user3:password@localhost:5672/tour3_user3");
		
		Consumer c1 = new DefaultConsumer(v1.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws UnsupportedEncodingException {
				String message = new String(body, "UTF-8");
				System.out.println("user1 Received '" + envelope.getRoutingKey() + "':'" + message + "'");
				v1.incrementNbMsgReceived();
			}
		};
		Consumer c2 = new DefaultConsumer(v2.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws UnsupportedEncodingException {
				String message = new String(body, "UTF-8");
				System.out.println("user2 Received '" + envelope.getRoutingKey() + "':'" + message + "'");
				v2.incrementNbMsgReceived();
			}
		};
		Consumer c3 = new DefaultConsumer(v3.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws UnsupportedEncodingException {
				String message = new String(body, "UTF-8");
				System.out.println("user3 Received '" + envelope.getRoutingKey() + "':'" + message + "'");
				v3.incrementNbMsgReceived();
			}
		};
		v1.addConsumer(c1);
		v2.addConsumer(c2);
		v3.addConsumer(c3);

		v1.startConsumption();
		v2.startConsumption();
		v3.startConsumption();
		
		Thread.sleep(1000);
		v1.publish("hello from v1");
		v2.publish("hello from v2");
		Thread.sleep(5000);
		
		System.out.println("user1 has"+ v1.getNbMsgReceived());
		System.out.println("user2 has"+ v2.getNbMsgReceived());
		System.out.println("user3 has"+ v3.getNbMsgReceived());
		System.out.println("v1.exchange_name="+ v1.EXCHANGE_NAME);
		System.out.println("v2.exchange_name="+ v2.EXCHANGE_NAME);
		 Assert.assertEquals(2, v1.getNbMsgReceived());
		 Assert.assertEquals(2, v2.getNbMsgReceived());
		 Assert.assertEquals(0, v3.getNbMsgReceived());
		 
		 v1.close();
		 v2.close();
		 v3.close();
	}

	@AfterClass
	public static void tearDown() throws InterruptedException, IOException {
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
	}
}
