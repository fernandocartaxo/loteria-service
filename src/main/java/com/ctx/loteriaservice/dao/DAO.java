package com.ctx.loteriaservice.dao;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.KeyFactory;

public abstract class DAO<VO> {
	
	protected Datastore datastore;
	protected KeyFactory keyFactory;
	  
	public DAO() {
		datastore = DatastoreOptions.getDefaultInstance().getService();
	    keyFactory = datastore.newKeyFactory().setKind(getKind());
	}
	
	public abstract String getKind();

}
