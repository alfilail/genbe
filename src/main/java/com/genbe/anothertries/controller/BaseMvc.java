package com.genbe.anothertries.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BaseMvc {
	
	@GetMapping("/inputbiodata")
	public String inputbiodata() {
		return "dashboard/inputbiodata";
	}
	
	@GetMapping("/inputeducation")
	public String inputeducation() {
		return "dashboard/inputeducation";
	}
	
	@GetMapping("/getdata")
	public String getdata() {
		return "dashboard/getdata";
	}
}
