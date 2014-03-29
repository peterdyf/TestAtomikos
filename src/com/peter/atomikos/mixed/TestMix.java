package com.peter.atomikos.mixed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/peter/atomikos/mixed/*.xml")
public class TestMix {

	@Autowired
	@Qualifier("recieverJms")
	private JmsTemplate recieverJms;

	@Autowired
	private MixService mixService;

	private BrokerService broker;

	@Before
	public void openJmsServer() throws Exception {
		broker = new BrokerService();
		broker.addConnector("tcp://localhost:61616");
		broker.start();
	}

	@After
	public void closeJmsServer() throws Exception {
		broker.stop();
	}


	@Test
	public void makeOrder() {
		
		final int id1=new Random().nextInt(100);
		final int id2=new Random().nextInt(100);
		
		try {
			mixService.doMixed(id1,id2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNull((OrderInMixed) recieverJms.receiveAndConvert());
		assertEquals(0, mixService.search(id2).size());
		assertEquals(0, mixService.search(id1).size());
		
		
	}
}
