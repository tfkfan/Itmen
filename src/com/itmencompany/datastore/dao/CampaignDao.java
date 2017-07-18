package com.itmencompany.datastore.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itmencompany.datastore.entities.Campaign;

@Component("campaignDao")
public class CampaignDao extends BaseDao<Campaign>{
	
	public CampaignDao(){
		super(Campaign.class);
	}
	
	public CampaignDao(Class<Campaign> clazz) {
		super(clazz);
	}

	public List<Campaign> getCampaignsByUserId(String createdBy) {
		return this.getByPropertyAsList("createdBy", createdBy);
	}
}
