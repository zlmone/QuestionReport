package com.cnedutech.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.dwsurvey.entity.Question;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.entity.SurveyStats;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import com.key.dwsurvey.service.SurveyStatsManager;

@Controller
@RequestMapping("/da")
public class DaController {
	
	@Autowired
	private SurveyStatsManager surveyStatsManager;
	@Autowired
	private SurveyDirectoryManager directoryManager;
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value="/survey-report!defaultReport")
	public ModelAndView defaultReport(HttpServletRequest request) throws Exception {
		SurveyDirectory directory = new SurveyDirectory();
		SurveyStats surveyStats = new SurveyStats();
		ModelAndView modelAndView  = new ModelAndView("content/diaowen-da/default-report");
		String surveyId = request.getParameter("surveyId");
		// 得到频数分析数据
		User user = accountManager.getCurUser();
		if(user!=null){
			directory=directoryManager.getSurveyByUser(surveyId, user.getId());
			if(directory!=null){
				List<Question> questions = surveyStatsManager.findFrequency(directory);
				surveyStats.setQuestions(questions);
			}
		}
		modelAndView.addObject(surveyStats);
		return modelAndView;
	}
}
