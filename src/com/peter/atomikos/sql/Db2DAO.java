package com.peter.atomikos.sql;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Db2DAO{
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("dataSource1")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void doSql(int id) {
		
		String sql=String.format("insert into USER_ORDER(ID) values(%s)",id);
		jdbcTemplate.execute(sql);
	}
	
	public List<Map<String, Object>> search(int id){
		return jdbcTemplate.queryForList("select * from USER_ORDER where id=?", id);
	}
}
