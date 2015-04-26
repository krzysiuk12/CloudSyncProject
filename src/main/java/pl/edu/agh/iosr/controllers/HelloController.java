package pl.edu.agh.iosr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/")
	public String getIndexPage() {
		return "static/index.html";
	}

}