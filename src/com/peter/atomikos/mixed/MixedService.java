package com.peter.atomikos.mixed;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peter.atomikos.Order;

@Service
public class MixedService {

	@Autowired
	@Qualifier("sendJms")
	private JmsTemplate sendJms;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("dataSource1")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void doSql(int id){
		String sql=String.format("insert into USER_ORDER(ID) values(%s)",id);
		jdbcTemplate.execute(sql);
	}

	public void send(int id) {
		sendJms.convertAndSend(new Order(id));
	}
	
	public List<Map<String, Object>> search(int id){
		return jdbcTemplate.queryForList("select * from USER_ORDER where id=?", id);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void doMixed(int id1,int id2) {
		send(id1);
		doSql(id1);
		send(id2);
		doSql(id2);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void doMixedWithException(int id1,int id2) {
		send(id1);
		doSql(id1);
		send(id2);
		doSql(id2);
		if(true){
			throw new RuntimeException();
		}
		
	}
}
