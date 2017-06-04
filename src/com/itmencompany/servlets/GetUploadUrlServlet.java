package com.itmencompany.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.itmencompany.common.ServerUtils;

@WebServlet("/get_upload")
public class GetUploadUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
    public GetUploadUrlServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadURL = blobstoreService.createUploadUrl(ServerUtils.UPLOAD_PATH);
		
		// change the computer name to standard localhost ip address, if in dev mode
		//uploadURL = uploadURL.replace("Acer-14", "127.0.0.1");
		uploadURL = uploadURL.replace("Àðòåì-ÏÊ", "127.0.0.1");
		
		response.setContentType("text");
		response.getWriter().println(uploadURL);
		response.getWriter().close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
