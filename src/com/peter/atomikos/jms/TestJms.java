package com.peter.atomikos.jms;


import static org.junit.Assert.*;

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
@ContextConfiguration(locations = "classpath:/com/peter/atomikos/jms/*.xml")
public class TestJms {

	@Autowired
	@Qualifier("recieverJms")
	private JmsTemplate recieverJms;

	@Autowired
	private JmsService jmsService;
	
	private BrokerService broker;
	
	@Before
	public void openJmsServer() throws Exception{
		broker = new BrokerService();
		broker.addConnector("tcp://localhost:61616");
		broker.start();
	}
	
	@After
	public void closeJmsServer() throws Exception{
		broker.stop();
	}


	@Test
	public void test() {
		
		final int id1=new Random().nextInt(100);
		final int id2=new Random().nextInt(100);
		
		try {
			jmsService.send(id1,id2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertNull((Order) recieverJms.receiveAndConvert());
	
	}
}
