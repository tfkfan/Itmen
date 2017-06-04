package com.itmencompany.common;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.itmencompany.datastore.entities.interfaces.JSONObj;

public class UserInfo implements Serializable, JSONObj{
	private static final long serialVersionUID = 1L;
	
	private List<String> files;
	private String length;
	private String fasade_material;
	private Boolean is_parlor;
	private String wishes;
	private String height;
	private String additional_wishes;
	private Long updated;

	public UserInfo(List<String> files, String length, String fasade_material, Boolean is_parlor, String wishes, String height,
			String additional_wishes) {
		rewrite(files, length, fasade_material, is_parlor, wishes, height, additional_wishes);
	}
	
	public void rewrite(List<String> files, String length, String fasade_material, Boolean is_parlor, String wishes, String height,
			String additional_wishes) {
		setFiles(files);
		setLength(length);
		setHeight(height);
		setIs_parlor(is_parlor);
		setFasade_material(fasade_material);
		setWishes(wishes);
		setAdditional_wishes(additional_wishes);
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getFasade_material() {
		return fasade_material;
	}

	public void setFasade_material(String fasade_material) {
		this.fasade_material = fasade_material;
	}

	public Boolean getIs_parlor() {
		return is_parlor;
	}

	public void setIs_parlor(Boolean is_parlor) {
		this.is_parlor = is_parlor;
	}

	public String getWishes() {
		return wishes;
	}

	public void setWishes(String wishes) {
		this.wishes = wishes;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getAdditional_wishes() {
		return additional_wishes;
	}

	public void setAdditional_wishes(String additional_wishes) {
		this.additional_wishes = additional_wishes;
	}
	
	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}

	public UserInfo() {

	}

	public String toString() {
		return "[files:" + (files != null ? files.toString() : "") + "], [length:" + length + "],[fasade_material: " + fasade_material + "], [is_parlor:"
				+ is_parlor + "], [wishes: " + wishes + "], [height: " + height + "], [additional_wishes: "
				+ additional_wishes + "]";

	}

	public String toJSON() throws JSONException {
		JSONObject res = new JSONObject();
		if(files != null && !files.isEmpty())
			res.put("images", new JSONArray(files));
		if(length != null)
			res.put("length", length);
		if(fasade_material != null)
			res.put("fasade_material", fasade_material);
		if(is_parlor != null)
			res.put("is_parlor", is_parlor);
		if(wishes != null)
			res.put("wishes", wishes);
		if(height != null)
			res.put("height", height);
		if(additional_wishes != null)
			res.put("additional_wishes", additional_wishes);
		return res.toString();
	}
}
