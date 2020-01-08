package com.cnedutech.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnedutech.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class TestController {

	@Autowired
	UserMapper userMapper;
	
	@RequestMapping(value="test")
	public Object  test() {
		PageHelper.startPage(1, 10);
		List<Map<String, Object>> user = userMapper.getUser();
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(user);
		System.err.println(pageInfo);
		return "login";
	}
}
