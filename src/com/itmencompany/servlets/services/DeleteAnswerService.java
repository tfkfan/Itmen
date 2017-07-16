package com.itmencompany.servlets.services;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.IncomingInfo;
import com.itmencompany.mvc.application.AppUserHelper;

@WebServlet("/delete_answer")
public class DeleteAnswerService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ANSWER_ID_KEY = "answer_id";

	public DeleteAnswerService() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String res = "ok";
		AppUser appUser = AppUserHelper.getUserFromRequest(request);
		if(appUser == null){
			response.sendRedirect("/login");
			return;
		}
	
		String answer_id = request.getParameter(ANSWER_ID_KEY);

		response.setCharacterEncoding("utf-8");
		response.setContentType("text");
		PrintWriter pw = response.getWriter();
		IncomingInfoDao dao = new IncomingInfoDao();
		
		try {
			if (answer_id != null && appUser.getIsAdmin()){
				IncomingInfo info = dao.get(Long.parseLong(answer_id));
				dao.delete(info);
			}
		} catch (Exception e) {
			res = "Some error occured during deleting answer";
		}
		pw.println(res);
		pw.close();
	}
}
