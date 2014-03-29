package com.peter.atomikos.sql;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SqlService {
	
	@Autowired
	private Db1DAO db1;
	
	@Autowired
	private Db2DAO db2;
	
	@Transactional(rollbackFor=Exception.class)
	public void doSthInDB1And2InOneTransaction(int id1,int id2) throws Exception{
		db1.doSql(id1);
		db2.doSql(id2);	
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void doSthInDB1And2InOneTransactionWithException(int id1,int id2) throws Exception{
		db1.doSql(id1);
		db2.doSql(id2);	
		//will throw an exception to roll back the change in both two data sources (db1 and db2)
		if (true) {
			throw new RuntimeException();
		}
	}
	
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> searchInDB1(int id) throws Exception{
		return db1.search(id);
	}
	
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> searchInDB2(int id) throws Exception{
		return db2.search(id);
	}
	
	
	
	
}