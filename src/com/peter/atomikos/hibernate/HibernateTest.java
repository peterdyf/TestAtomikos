package com.peter.atomikos.hibernate;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import  static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/peter/atomikos/hibernate/applicationContext.xml")
public class HibernateTest {

	@Autowired
	HibernateService service;

	@Test
	public void test1() throws Exception {
		int id1 = new Random().nextInt(1000);
		int id2 = new Random().nextInt(1000);

		try {
			service.doSthInDB1And2InOneTransaction(id1, id2);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		assertNull(service.searchInDB2(id2));
		assertNull(service.searchInDB2(id1));

	}

}
