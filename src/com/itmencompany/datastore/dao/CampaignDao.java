package com.itmencompany.datastore.dao;

import java.util.List;
import com.itmencompany.datastore.entities.Campaign;

public class CampaignDao extends BaseDao<Campaign>{
	
	public CampaignDao(Class<Campaign> clazz) {
		super(clazz);
	}

	public List<Campaign> getCampaignsByUserId(String createdBy) {
		return this.getByPropertyAsList("createdBy", createdBy);
	}
}
