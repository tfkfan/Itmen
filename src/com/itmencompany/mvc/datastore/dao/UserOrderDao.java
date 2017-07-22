package com.itmencompany.mvc.datastore.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.itmencompany.mvc.datastore.entities.UserOrder;

@Repository("orderDao")
public class UserOrderDao extends BaseDao<UserOrder> {

	public UserOrderDao(){
		super(UserOrder.class);
	}
	
	public UserOrderDao(Class<UserOrder> clazz) {
		super(clazz);
	}

	public List<UserOrder> getOrdersByUserId(Long id) {
		return this.getByPropertyAsList("userId", id);
	}
}
