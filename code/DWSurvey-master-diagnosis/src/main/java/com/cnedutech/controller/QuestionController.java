package com.cnedutech.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.key.dwsurvey.service.QuestionManager;

@Controller
public class QuestionController {

	
	@Autowired
	private QuestionManager questionManager;
	/**
	 * ajax删除
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("design/question!ajaxDelete")
	public void ajaxDelete(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String responseStr="";
		try{
			String delQuId=request.getParameter("quId");
			questionManager.delete(delQuId);	
			responseStr="true";
		}catch (Exception e) {
			responseStr="false";
		}
		response.getWriter().write(responseStr);
	}
	
}
