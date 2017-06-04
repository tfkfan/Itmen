package com.itmencompany.datastore.dao;

import java.util.List;
import com.itmencompany.datastore.entities.UserOrder;

public class UserOrderDao extends BaseDao<UserOrder> {

	public UserOrderDao(Class<UserOrder> clazz) {
		super(clazz);
	}

	public List<UserOrder> getOrdersByUserId(Long id) {
		return this.getByPropertyAsList("userId", id);
	}
}
