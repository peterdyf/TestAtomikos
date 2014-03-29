package com.peter.atomikos.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peter.atomikos.Order;

@Service
public class JmsService {
	@Autowired
	@Qualifier("sendJms")
    private JmsTemplate sendJms;
	
	@Transactional(rollbackFor=Exception.class)
	public void send(int id1,int id2){
		
		sendJms.convertAndSend(new Order(id1));
		sendJms.convertAndSend(new Order(id2));
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void sendWithException(int id1,int id2){
		
		sendJms.convertAndSend(new Order(id1));
		sendJms.convertAndSend(new Order(id2));
		if(true){
			throw new RuntimeException("123");
		}
		
	}
}
