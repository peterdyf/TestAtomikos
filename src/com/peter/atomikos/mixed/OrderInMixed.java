package com.peter.atomikos.mixed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderInMixed {

	@XmlElement(name = "Id", required = true)
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OrderInMixed(long id) {
		this.id = id;
	}

	public OrderInMixed() {
	}
	
	
}