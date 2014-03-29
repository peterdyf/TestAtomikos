package com.peter.atomikos.sql;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import  static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/peter/atomikos/sql/applicationContext.xml")
public class SqlTest {

	@Autowired
	SqlService service;

	@Test
	public void testNormal() throws Exception {
		int id1 = new Random().nextInt(1000);
		int id2 = new Random().nextInt(1000);

		try {
			service.doSthInDB1And2InOneTransaction(id1, id2);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		
		List<Map<String, Object>> result1 = service.searchInDB1(id1);
		assertEquals(1, result1.size());
		assertEquals(id1, result1.get(0).get("ID"));
		
		List<Map<String, Object>> result2 = service.searchInDB2(id2);
		assertEquals(1, result2.size());
		assertEquals(id2, result2.get(0).get("ID"));
		

	}
	
	@Test
	public void testException() throws Exception {
		int id1 = new Random().nextInt(1000);
		int id2 = new Random().nextInt(1000);

		try {
			service.doSthInDB1And2InOneTransactionWithException(id1, id2);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		assertEquals(0, service.searchInDB2(id2).size());
		assertEquals(0, service.searchInDB1(id1).size());

	}

}
