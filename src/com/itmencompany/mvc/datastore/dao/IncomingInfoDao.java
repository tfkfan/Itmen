package com.itmencompany.mvc.datastore.dao;

import java.util.HashMap;
import java.util.List;

import com.itmencompany.mvc.datastore.entities.IncomingInfo;
public class IncomingInfoDao extends BaseDao<IncomingInfo> {

	public IncomingInfoDao() {
		super(IncomingInfo.class);
	}

	public List<IncomingInfo> getInfoByUserId(Long id) {
		return this.getByPropertyAsList("userId", id);
	}
	
	public List<IncomingInfo> getUserFavorites(Long userId, Integer limit, Integer pageNum){
		HashMap<String, Object> props = new HashMap<String, Object>();
		props.put("userId", userId);
		props.put("isFavorite", true);
		return this.getWithOffsetAndProperties(pageNum, limit, props);
	}
}
