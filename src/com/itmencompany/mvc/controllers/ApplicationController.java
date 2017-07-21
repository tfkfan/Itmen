package com.itmencompany.mvc.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.itmencompany.common.ServerUtils;
import com.itmencompany.mvc.datastore.dao.IncomingInfoDao;
import com.itmencompany.mvc.datastore.entities.IncomingInfo;

@Controller
public class ApplicationController {
	final static Logger log = Logger.getLogger(ApplicationController.class.getName());
	final private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showPrivateOffice() {
		return "privateOffice";
	}

	@RequestMapping(value = ServerUtils.UPLOAD_PATH, method = RequestMethod.GET)
	public void getUploadedResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
		blobstoreService.serve(blobKey, response);
	}

	@RequestMapping(value = ServerUtils.UPLOAD_PATH, method = RequestMethod.POST)
	@ResponseBody
	public String handleUpload(HttpServletRequest request) throws IOException {
		log.info("serving..");
		try {
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

			List<BlobKey> blobKeys = blobs.get(blobs.keySet().iterator().next());

			BlobKey blobKey = blobKeys.get(0);

			String servingUrl = ImagesServiceFactory.getImagesService()
					.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey).secureUrl(true));

			return servingUrl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/get_upload", method = RequestMethod.GET)
	@ResponseBody
	public String getUploadLink() {
		String uploadURL = blobstoreService.createUploadUrl(ServerUtils.UPLOAD_PATH);
		// change the computer name to standard localhost ip address, if in dev
		// mode
		uploadURL = uploadURL.replace("Àðòåì-ÏÊ", "127.0.0.1");
		return uploadURL;
	}

	@RequestMapping(value = "/edit_answer", method = RequestMethod.POST)
	@ResponseBody
	public String editAnswer(@RequestParam Long answer_id, @RequestParam(required = false) Boolean set_favorite) {
		String res = "";
		try {
			IncomingInfoDao dao = new IncomingInfoDao();
			if (answer_id != null) {
				IncomingInfo answer = dao.get(answer_id);
				answer.setIsFavorite(set_favorite);
				answer = dao.saveAndReturn(answer);
				JSONObject resObj = new JSONObject();
				resObj.put("isFavorite", answer.getIsFavorite());
				res = resObj.toString();
			} else
				res = "error";
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return res;
	}

	@RequestMapping(value = "/get_answer", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getAnswer(@RequestParam Long answer_id) throws JSONException {
		IncomingInfoDao dao = new IncomingInfoDao();
		if (answer_id != null) {
			IncomingInfo answer = dao.get(answer_id);
			String val = answer.toJSON();
			log.info(val);
			return val;
		}
		return null;
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test() {

	}
}