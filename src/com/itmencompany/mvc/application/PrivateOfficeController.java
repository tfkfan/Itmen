package com.itmencompany.mvc.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/private")
public class PrivateOfficeController {

	@RequestMapping(method = RequestMethod.GET)
	public String showPrivateOffice() {
		return "privateOffice";
	}
	
	

}