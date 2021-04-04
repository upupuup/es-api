package com.and.andesjd.controller;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 13:42
 */
@Controller
public class IndexController {
	@GetMapping({"/", "/index"})
	public String index() {
		return "index";
	}
}
