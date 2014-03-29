package com.peter.atomikos.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.peter.atomikos.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/peter/atomikos/jms/*.xml")
public class JmsTest {

	@Autowired
	@Qualifier("recieverJms")
	private JmsTemplate recieverJms;

	@Autowired
	private JmsService jmsService;

	private static BrokerService broker;

	@BeforeClass
	public static void openJmsServer() throws Exception {
		broker = new BrokerService();
		broker.addConnector("tcp://localhost:61616");
		broker.start();
	}

	@AfterClass
	public static void closeJmsServer() throws Exception {
		broker.stop();
	}

	@Test
	public void testNormal() throws Exception{
		broker.deleteAllMessages();

		final int id1 = new Random().nextInt(100);
		final int id2 = new Random().nextInt(100);

		try {
			jmsService.send(id1, id2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int result1 = ((Order) recieverJms.receiveAndConvert()).getId();
		int result2 = ((Order) recieverJms.receiveAndConvert()).getId();

		Set<Integer> expected = new HashSet<Integer>();
		expected.add(id1);
		expected.add(id2);

		Set<Integer> actual = new HashSet<Integer>();
		actual.add(result1);
		actual.add(result2);

		assertEquals(expected, actual);
	}

	@Test
	public void testException() throws Exception {
		broker.deleteAllMessages();

		final int id1 = new Random().nextInt(100);
		final int id2 = new Random().nextInt(100);

		try {
			jmsService.sendWithException(id1, id2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNull((Order) recieverJms.receiveAndConvert());
	}
}
