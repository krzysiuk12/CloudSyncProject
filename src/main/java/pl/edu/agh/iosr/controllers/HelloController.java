package pl.edu.agh.iosr.controllers;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;

@RestController
@RequestMapping("/")
public class HelloController {

    @RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}


    @RequestMapping(value = "rest", method = RequestMethod.GET)
    public @ResponseBody
    ResponseSerializer<String> printRest() {
        return new ResponseSerializer<String>("hello");
    }
}