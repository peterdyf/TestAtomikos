package com.peter.atomikos.hibernate;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.peter.atomikos.hibernate.entity.UserOrder;

@Service
public class HibernateInDb1 {
	
	@Resource
	@Qualifier("sessionFactory1")
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void doSql(int id){
		UserOrder p=new UserOrder();
		p.setId(id);
		getSession().save(p);
	}
	
	
	public UserOrder search(int id){
		
		return (UserOrder)getSession().get(UserOrder.class, id);
	}
}
