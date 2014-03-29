package com.peter.atomikos.mixed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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
@ContextConfiguration(locations = "classpath:/com/peter/atomikos/mixed/*.xml")
public class MixedTest {

	@Autowired
	@Qualifier("recieverJms")
	private JmsTemplate recieverJms;

	@Autowired
	private MixedService mixService;

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
		
		final int id1=new Random().nextInt(100);
		final int id2=new Random().nextInt(100);
		
		try {
			mixService.doMixed(id1,id2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int jmsResult1 = ((Order) recieverJms.receiveAndConvert()).getId();
		int jmsResult2 = ((Order) recieverJms.receiveAndConvert()).getId();

		Set<Integer> expected = new HashSet<Integer>();
		expected.add(id1);
		expected.add(id2);

		Set<Integer> actual = new HashSet<Integer>();
		actual.add(jmsResult1);
		actual.add(jmsResult2);

		assertEquals(expected, actual);
		
		List<Map<String, Object>> dbResult1 = mixService.search(id1);
		assertEquals(1, dbResult1.size());
		assertEquals(id1, dbResult1.get(0).get("ID"));
		
		List<Map<String, Object>> dbResult2 = mixService.search(id2);
		assertEquals(1, dbResult2.size());
		assertEquals(id2, dbResult2.get(0).get("ID"));
		
		
		
	}
	
	@Test
	public void testException() throws Exception{
		
		broker.deleteAllMessages();
		
		final int id1=new Random().nextInt(100);
		final int id2=new Random().nextInt(100);
		
		try {
			mixService.doMixedWithException(id1,id2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNull((Order) recieverJms.receiveAndConvert());
		assertEquals(0, mixService.search(id2).size());
		assertEquals(0, mixService.search(id1).size());
		
		
	}
}
