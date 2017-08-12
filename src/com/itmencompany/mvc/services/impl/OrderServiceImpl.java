package com.itmencompany.mvc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itmencompany.mvc.datastore.dao.UserOrderDao;
import com.itmencompany.mvc.datastore.entities.UserOrder;
import com.itmencompany.mvc.services.OrderService;

@Service("orderService")
public class OrderServiceImpl implements OrderService{

	@Autowired
	UserOrderDao orderDao;
	
	public OrderServiceImpl(){
		
	}
	
	@Override
	public void delete(Long id) {
		if(id != null)
			orderDao.delete(id);
	}

	@Override
	public void delete(UserOrder obj) {
		if(obj != null)
			orderDao.delete(obj);
	}

	@Override
	public UserOrder get(Long id) {
		if(id != null)
			return orderDao.get(id);
		return null;
	}

	@Override
	public void save(UserOrder obj) {
		if(obj != null)
			orderDao.save(obj);
	}

	@Override
	public List<UserOrder> getAll() {
		return orderDao.listAll();
	}

}
