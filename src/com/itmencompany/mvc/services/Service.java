package com.itmencompany.mvc.services;

import java.util.List;

public interface Service<Type> {
	public void delete(Long id);
	public void delete(Type obj);
	public Type get(Long id);
	public void save(Type obj);
	public List<Type> getAll();
}
