package com.itmencompany.datastore.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

public abstract class BaseDao<T> {
	private final Class<T> clazz;

	protected BaseDao(final Class<T> clazz) {
		this.clazz = clazz;
	}

	public List<T> listAll() {
		return ObjectifyService.ofy().load().type(clazz).list();
	}

	public void save(T entity) {
		saveAndReturn(entity);
	}

	public T saveAndReturn(T entity) {
		ObjectifyService.ofy().save().entity(entity).now();
		return entity;
	}

	public Key<T> saveAndReturnKey(T entity) {
		return ObjectifyService.ofy().save().entity(entity).now();
	}

	public Collection<T> saveAndReturn(Iterable<T> entities) {
		return ObjectifyService.ofy().save().entities(entities).now().values();
	}

	public T get(Key<T> key) {
		return ObjectifyService.ofy().load().key(key).now();
	}

	public T get(Long id) {
		// TODO probably it could be fixed by parameters of
		// work around for objectify cacheing and new query not having the
		// latest
		// data
		ObjectifyService.ofy().clear();
		return ObjectifyService.ofy().load().type(clazz).id(id).now();
	}

	public T getByProperty(String propName, Object propValue) throws TooManyResultsException {
		Query<T> q = ObjectifyService.ofy().load().type(clazz);
		q = q.filter(propName, propValue);
		Iterator<T> fetch = q.limit(2).list().iterator();
		if (!fetch.hasNext()) {
			return null;
		}
		T obj = fetch.next();
		if (fetch.hasNext()) {
			throw new TooManyResultsException();
		}
		return obj;
	}
	
	public List<T> getByPropertyAsList(String propName, Object propValue) {
		Query<T> q = ObjectifyService.ofy().load().type(clazz);
		q = q.filter(propName, propValue);
		return q.list();
	}

	public List<T> getWithOffset(Integer offset, Integer limit) {
		int prevPage = (offset - 1) != -1 ? offset - 1 : 0;
		return ObjectifyService.ofy().load().type(clazz).limit(limit).offset(prevPage * limit).list();
	}
	
	public List<T> getWithOffsetAndProperty(Integer offset, Integer limit, String propName, Object propValue){
		int prevPage = (offset - 1) != -1 ? offset - 1 : 0;
		return ObjectifyService.ofy().load().type(clazz).filter(propName, propValue).limit(limit).offset(prevPage * limit).list();
	}

	public int getCount() {
		return ObjectifyService.ofy().load().type(clazz).count();
	}

	public Boolean exists(Key<T> key) {
		return get(key) != null;
	}

	public Boolean exists(Long id) {
		return get(id) != null;
	}

	public List<T> getSubset(List<Long> ids) {
		return new ArrayList<T>(ObjectifyService.ofy().load().type(clazz).ids(ids).values());
	}

	public Map<Long, T> getSubsetMap(List<Long> ids) {
		return new HashMap<Long, T>(ObjectifyService.ofy().load().type(clazz).ids(ids));
	}

	public void delete(T object) {
		ObjectifyService.ofy().delete().entity(object);
	}

	public void delete(Long id) {
		ObjectifyService.ofy().delete().type(clazz).id(id);
	}

	public void delete(List<T> objects) {
		ObjectifyService.ofy().delete().entities(objects);
	}

	protected LoadType<T> query() {
		return ObjectifyService.ofy().load().type(clazz);
	}
}
