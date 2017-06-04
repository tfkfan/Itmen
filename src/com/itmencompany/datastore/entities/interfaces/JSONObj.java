package com.itmencompany.datastore.entities.interfaces;

import com.google.appengine.labs.repackaged.org.json.JSONException;

public interface JSONObj {
	String toJSON() throws JSONException;
}
