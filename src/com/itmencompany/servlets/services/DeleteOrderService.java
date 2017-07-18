package com.itmencompany.servlets.services;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.UserOrder;
import com.itmencompany.common.AppUserHelper;

@WebServlet("/delete_order")
public class DeleteOrderService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ORDER_ID_KEY = "order_id";

	public DeleteOrderService() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppUser appUser = AppUserHelper.getUserFromRequest(request);
		if(appUser == null){
			response.sendRedirect("/login");
			return;
		}
		String order_id = request.getParameter(ORDER_ID_KEY);

		response.setCharacterEncoding("utf-8");
		response.setContentType("text");
		PrintWriter pw = response.getWriter();
		UserOrderDao dao = new UserOrderDao(UserOrder.class);
		String res = "ok";
		try {
			if (order_id != null && appUser.getIsAdmin()) {
				UserOrder userOrder = dao.get(Long.parseLong(order_id));
				dao.delete(userOrder);
			}
		} catch (Exception e) {
			res = "Some error occured during deleting order";
		}
		pw.println(res);
		pw.close();
	}
}
