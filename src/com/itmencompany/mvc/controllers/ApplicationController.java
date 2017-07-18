package com.itmencompany.mvc.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.itmencompany.common.ServerUtils;

@Controller
public class ApplicationController {
	final static Logger log = Logger.getLogger(ApplicationController.class.getName());
	final private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showPrivateOffice() {
		return "privateOffice";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public void getUploadedResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
		blobstoreService.serve(blobKey, response);
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void handleUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("serving..");
		try {
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

			List<BlobKey> blobKeys = blobs.get(blobs.keySet().iterator().next());

			BlobKey blobKey = blobKeys.get(0);
	
			String servingUrl = ImagesServiceFactory.getImagesService().getServingUrl(
					ServingUrlOptions.Builder.withBlobKey(blobKey).secureUrl(true));

			response.getWriter().print(servingUrl);
			response.getWriter().close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			response.getWriter().close();
		}
	}
	@RequestMapping(value = "/get_upload", method = RequestMethod.GET)
	public void getUploadLink(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadURL = blobstoreService.createUploadUrl(ServerUtils.UPLOAD_PATH);
		
		// change the computer name to standard localhost ip address, if in dev mode
		uploadURL = uploadURL.replace("Àðòåì-ÏÊ", "127.0.0.1");
		
		response.setContentType("text");
		response.getWriter().println(uploadURL);
		response.getWriter().close();
	}

}