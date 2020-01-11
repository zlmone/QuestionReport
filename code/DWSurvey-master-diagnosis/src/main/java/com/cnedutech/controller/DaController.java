package com.cnedutech.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.common.plugs.page.Page;
import com.key.dwsurvey.entity.Question;
import com.key.dwsurvey.entity.SurveyAnswer;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.entity.SurveyStats;
import com.key.dwsurvey.service.SurveyAnswerManager;
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
	@Autowired
	private SurveyAnswerManager surveyAnswerManager;
	
	@RequestMapping(value="/survey-report!defaultReport")
	public ModelAndView defaultReport(HttpServletRequest request) throws Exception {
		SurveyStats surveyStats = new SurveyStats();
		ModelAndView modelAndView  = new ModelAndView("content/diaowen-da/default-report");
		String surveyId = request.getParameter("surveyId");
		// 得到频数分析数据
		User user = accountManager.getCurUser();
		SurveyDirectory survey =null;
		if(user!=null){
			survey=directoryManager.getSurveyByUser(surveyId, user.getId());
			if(survey!=null){
				List<Question> questions = surveyStatsManager.findFrequency(survey);
				surveyStats.setQuestions(questions);
				modelAndView.addObject(survey);
			}
		}
		modelAndView.addObject("survey", survey);
		modelAndView.addObject("surveyId", surveyId);
		modelAndView.addObject("surveyStats",surveyStats);
		return modelAndView;
	}
	
	@RequestMapping(value="/my-survey-answer")
	public ModelAndView mySurveyAnswer(HttpServletRequest request) {
		ModelAndView modelAndView = new  ModelAndView("content/diaowen-da/survey-answer-data");
		Page<SurveyAnswer> page=new Page<SurveyAnswer>();
		int pageNo=0;
		if(request.getParameter("page.pageNo")!=null) {
			pageNo=Integer.valueOf(request.getParameter("page.pageNo"));
		}
		page.setPageNo(Integer.valueOf(pageNo));
		User user=accountManager.getCurUser();
		String surveyId = request.getParameter("surveyId");
    	if(user!=null){
    		SurveyDirectory survey=directoryManager.getSurveyByUser(surveyId, user.getId());
    		if(survey!=null){
    			page=surveyAnswerManager.answerPage(page, surveyId);
    			modelAndView.addObject("survey", survey);
    			modelAndView.addObject("page", page);
    		}
    	}
    	modelAndView.addObject("surveyId", surveyId);
		return modelAndView;
	}
	@RequestMapping(value="/survey-report!chartData")
	public String chartData(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//取柱状图数据
		User user = accountManager.getCurUser();
		if(user!=null){
			String questionId=request.getParameter("quId");
			Question question=new Question();
			question.setId(questionId);
			surveyStatsManager.questionDateCross(question);
			response.getWriter().write(question.getStatJson());
		}
		return null;
	}
}
