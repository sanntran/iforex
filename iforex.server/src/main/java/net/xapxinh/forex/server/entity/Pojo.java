package net.xapxinh.forex.server.entity;

import java.io.Serializable;

public class Pojo implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final long DEFAULT_ID = 0L;
	
	private long id;
	private String name;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSId() {
		return "#" + id;
	}

	public String getNameId() {
		return name + " #" + id;
	}
	
	public boolean isNew() {
		return id != DEFAULT_ID;
	}
}
