package com.itmencompany.servlets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.Transform;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final static Logger log = Logger.getLogger(UploadServlet.class.getName());

	final private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public UploadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
		blobstoreService.serve(blobKey, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

}
