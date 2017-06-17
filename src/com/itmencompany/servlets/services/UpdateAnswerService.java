package com.itmencompany.servlets.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.entities.IncomingInfo;

@WebServlet("/update_answer")
public class UpdateAnswerService extends HttpServlet {

	private static final Logger log = Logger.getLogger(UpdateAnswerService.class.getName());

	private static final long serialVersionUID = 1L;
	private static final String ANSWER_ID_KEY = "answer_id";
	private static final String SETFAVORITE_KEY = "set_favorite";

	public UpdateAnswerService() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String answer_id = request.getParameter(ANSWER_ID_KEY);
			String set_favorite_str = request.getParameter(SETFAVORITE_KEY);
			Boolean set_favorite = set_favorite_str == null ? false : Boolean.parseBoolean(set_favorite_str);

			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			IncomingInfoDao dao = new IncomingInfoDao();
			String res = null;
			if (answer_id == null) {
				res = "error";
			} else {
				IncomingInfo answer = dao.get(Long.parseLong(answer_id));
				answer.setIsFavorite(set_favorite);
				dao.save(answer);
				JSONObject resObj = new JSONObject();
				resObj.put("isFavorite", answer.getIsFavorite());
				res = resObj.toString();
			}

			pw.println(res);
			pw.close();
		} catch (Exception e) {
			log.info("Some error occured updating answer " + e.getMessage());
		}
	}
}
