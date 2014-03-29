package com.peter.atomikos.sql;

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
	public void test1() throws Exception {
		int id1 = new Random().nextInt(1000);
		int id2 = new Random().nextInt(1000);

		try {
			service.doSthInDB1And2InOneTransaction(id1, id2);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		assertEquals(0, service.searchInDB2(id2).size());
		assertEquals(0, service.searchInDB1(id1).size());

	}

}
