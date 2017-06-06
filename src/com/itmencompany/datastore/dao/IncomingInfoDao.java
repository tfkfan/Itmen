package com.itmencompany.datastore.dao;

import java.util.ArrayList;
import java.util.List;
import com.itmencompany.datastore.entities.IncomingInfo;
import com.itmencompany.datastore.entities.UserOrder;
public class IncomingInfoDao extends BaseDao<IncomingInfo> {

	public IncomingInfoDao() {
		super(IncomingInfo.class);
	}

	public List<IncomingInfo> getInfoByUserId(Long id) {
		return this.getByPropertyAsList("userId", id);
	}
}
