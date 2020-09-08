package com.genbe.anothertries.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ApiHtml {
	
	@GetMapping("/dashboard1")
	public String dashboard1() {
		return "dashboard/personbiodata";
	}
	
	@GetMapping("/dashboard2")
	public String dashboard2() {
		return "dashboard/getdata";
	}
	
	@GetMapping("/dashboard3")
	public String dashboard3() {
		return "dashboard/pendidikan";
	}
	
	@GetMapping("/dashboard4")
	public String dashboard4() {
		return "dashboard/pbmodal";
	}
	
	@GetMapping("/dashboard5")
	public String dashboard5() {
		return "dashboard/pendmodal";
	}
}
