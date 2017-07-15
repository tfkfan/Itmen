package com.itmencompany.server.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

	@RequestMapping(value = "/test")
	@ResponseBody
	public String sayHello() {
		return "Hello World dummy";
	}
}