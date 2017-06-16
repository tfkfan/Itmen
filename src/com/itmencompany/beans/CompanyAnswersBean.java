package com.itmencompany.beans;

import java.util.List;

import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.entities.IncomingInfo;

public class CompanyAnswersBean {
	
	public List<IncomingInfo> getAnswers(Long chosenUserId, Integer limit, Integer answerPageNum, IncomingInfoDao answerDao){
		List<IncomingInfo> res = null;
		if (chosenUserId == null)
			res = answerDao.getWithOffset(answerPageNum, limit);
		else 
			res = answerDao.getWithOffsetAndProperty(answerPageNum, limit, "userId",  chosenUserId);
		
		return res;	
	}
}
