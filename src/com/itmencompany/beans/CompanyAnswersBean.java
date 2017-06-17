package com.itmencompany.beans;

import java.util.List;
import java.util.logging.Logger;

import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.entities.IncomingInfo;

public class CompanyAnswersBean {

	final static Logger log = Logger.getLogger(CompanyAnswersBean.class.getName());
	
	private IncomingInfoDao answerDao;
	
	public CompanyAnswersBean(){
		answerDao = new IncomingInfoDao();
	}
	
	public Integer getAnswersCount(){
		return answerDao.getCount();
	}
	
	public List<IncomingInfo> getAnswers(Long chosenUserId, Integer limit, Integer answerPageNum){
		List<IncomingInfo> res = null;
		if (chosenUserId == null)
			res = answerDao.getWithOffset(answerPageNum, limit);
		else 
			res = answerDao.getWithOffsetAndProperty(answerPageNum, limit, "userId",  chosenUserId);
		log.info("Num answers: " + res.size() + " page:" + answerPageNum + " limit:" + limit);
		return res;	
	}
	
	public List<IncomingInfo> getFavoriteUserAnswers(Long chosenUserId, Integer limit, Integer answerPageNum){
		if(chosenUserId == null)
			return null;
		return answerDao.getUserFavorites(chosenUserId, limit, answerPageNum);
	}
}
