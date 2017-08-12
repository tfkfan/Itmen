package com.itmencompany.mvc.datastore.entities;

import java.io.Serializable;
import java.util.Date;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

public class DatabaseObject implements Serializable {

	@Id
	protected Long Id;

	private static final long serialVersionUID = 1L;
	@Index
	protected Date doModified;

	public DatabaseObject() {

	}

	public Date getDoModified() {
		return doModified;
	}

	public void setDoModified(Date doModified) {
		this.doModified = doModified;
	}

	@OnSave
	protected void onSave() {
		this.doModified = new Date();
	}
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}
}
