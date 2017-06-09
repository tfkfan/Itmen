package com.itmencompany.servlets.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.IncomingInfo;
import com.itmencompany.datastore.entities.UserOrder;
import com.itmencompany.servlets.IncomingMailServlet;

@WebServlet("/get_answer")
public class GetAnswerService extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(GetAnswerService.class.getName());
	
	private static final long serialVersionUID = 1L;
	private static final String ANSWER_ID_KEY = "answer_id";

	public GetAnswerService() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String answer_id = request.getParameter(ANSWER_ID_KEY);

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		IncomingInfoDao dao = new IncomingInfoDao();
		String res = null;
		if (answer_id == null) {
			res = "null";
		} else {
			IncomingInfo answer = dao.get(Long.parseLong(answer_id));
			try {
				res = answer.toJSON();
				log.info(res);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		pw.println(res);
		pw.close();

	}
}
