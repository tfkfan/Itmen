package com.itmencompany.servlets.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.UserOrder;

@WebServlet("/get_order")
public class GetOrderService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ORDER_ID_KEY = "order_id";

	public GetOrderService() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String order_id = request.getParameter(ORDER_ID_KEY);

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		UserOrderDao dao = new UserOrderDao(UserOrder.class);
		String res = null;
		if (order_id == null) {
			res = "null";
		} else {
			UserOrder userOrder = dao.get(Long.parseLong(order_id));
			try {
				res = userOrder.toJSON();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		pw.println(res);
		pw.close();

	}
}
