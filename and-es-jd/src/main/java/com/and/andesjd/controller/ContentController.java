package com.and.andesjd.controller;

import com.and.andesjd.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 14:11
 */
@RestController
public class ContentController {
	@Autowired
	private ContentService contentService;

	@GetMapping("/parse/{keywords}")
	public Boolean parse(@PathVariable String keywords) throws Exception {
		return contentService.parseContent(keywords);
	}

	@GetMapping("/search/{keywords}/{pageNo}/{pageSize}")
	public List<Map<String, Object>> parse(@PathVariable String keywords, @PathVariable int pageNo, @PathVariable int pageSize) throws Exception {
		return contentService.searchPage(keywords, pageNo, pageSize);
	}
}
