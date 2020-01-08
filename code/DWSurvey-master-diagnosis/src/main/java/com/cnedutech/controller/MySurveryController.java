package com.cnedutech.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.common.plugs.page.Page;
import com.key.common.utils.DiaowenProperty;
import com.key.dwsurvey.entity.Question;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.entity.SurveyStyle;
import com.key.dwsurvey.service.QuestionManager;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import com.key.dwsurvey.service.SurveyReqUrlManager;
import com.key.dwsurvey.service.SurveyStyleManager;


@Controller
@RequestMapping(value="/design")
@SuppressWarnings("unchecked")
public class MySurveryController {

	@Autowired
	private SurveyDirectoryManager surveyDirectoryManager;
	
	@Autowired
	private QuestionManager questionManager;
	@Autowired
	private SurveyStyleManager surveyStyleManager;
	@Autowired
	private SurveyReqUrlManager surveyReqUrlManager;
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value="/my-survey")
	public ModelAndView my_survey(HttpServletRequest request,HttpServletResponse response,SurveyDirectory entity,Page page) throws Exception {
		String surveyState = request.getParameter("surveyState");
		if(surveyState==null||"".equals(surveyState)){
			entity.setSurveyState(null);
		}
		ModelAndView modelAndView = new ModelAndView("content/diaowen-design/list");
	    page=surveyDirectoryManager.findByUser(page,entity);
	    modelAndView.addObject(page);
		return modelAndView;
	}
	
	/**
	 * 设计页面跳转
	 * @param request
	 * @param response
	 * @param entity
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/my-survey-design")
	public ModelAndView my_survey_design(HttpServletRequest request,HttpServletResponse response,SurveyDirectory entity,Page page) throws Exception {
		ModelAndView modelAndView = new ModelAndView("content/diaowen-design/survey");
		String surveyId = request.getParameter("surveyId");
		//判断是否拥有权限
		User user= accountManager.getCurUser();
		if(user!=null){
			String userId=user.getId();
			SurveyDirectory surveyDirectory=surveyDirectoryManager.getSurveyByUser(surveyId, userId);
			if(surveyDirectory!=null){
				surveyDirectoryManager.getSurveyDetail(surveyId, surveyDirectory);
//				SurveyDirectory survey=surveyDirectoryManager.getSurvey(surveyId);
				List<Question> questions=questionManager.findDetails(surveyId, "2");
				surveyDirectory.setQuestions(questions);
				surveyDirectory.setSurveyQuNum(questions.size());
				surveyDirectoryManager.save(surveyDirectory);
				modelAndView.addObject("survey", surveyDirectory);
				SurveyStyle surveyStyle=surveyStyleManager.getBySurveyId(surveyId);
				modelAndView.addObject("surveyStyle", surveyStyle);
				
				modelAndView.addObject("prevHost", DiaowenProperty.STORAGE_URL_PREFIX);
			}else{
				modelAndView.addObject("msg", "未登录或没有相应数据权限");
			}
		}else{
			modelAndView.addObject("msg", "未登录或没有相应数据权限");
		}
		return modelAndView;
	}
	
	

	

	/**
	 * 数据收集页面跳转
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/my-collect")
	public ModelAndView my_collect(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String COLLECT1="collect1";
		String IFRAME="iframe";
		String SITECOMP="sitecomp";
		String WEIXIN="weixin";
		String SHARE="share";
		Map<String,String> url = new HashMap<String,String>();
		url.put(COLLECT1, "content/diaowen-collect/collect_1");
		url.put(IFRAME, "content/diaowen-collect/collect_iframe");
		url.put(SITECOMP, "content/diaowen-collect/collect_website");
		url.put(WEIXIN, "content/diaowen-collect/collect_weixin");
		url.put(SHARE, "content/diaowen-collect/collect_2");
		
		String surveyId = request.getParameter("surveyId");
		String tabId=request.getParameter("tabId");

		String baseUrl = "";
		baseUrl = request.getScheme() +"://" + request.getServerName()  
						+ (request.getServerPort() == 80 ? "" : ":" +request.getServerPort())
                        + request.getContextPath();
		User user=accountManager.getCurUser();
    	if(user!=null){
    		SurveyDirectory surveyDirectory=surveyDirectoryManager.getSurveyByUser(surveyId, user.getId());
    		if(surveyDirectory!=null){
    			ModelAndView modelAndView =  null;
    			if(surveyDirectory!=null){
        			request.setAttribute("survey", surveyDirectory);
        			System.out.println("过来了MyCollectAction");
        			if(IFRAME.equals(tabId)){
        				modelAndView=new  ModelAndView(url.get(tabId));
        			}else if(SITECOMP.equals(tabId)){
        				modelAndView=new  ModelAndView(url.get(tabId));
        			}else if(WEIXIN.equals(tabId)){
        				modelAndView=new  ModelAndView(url.get(tabId));
        			}else if(SHARE.equals(tabId)){
        				modelAndView=new  ModelAndView(url.get(tabId));
        			}else {
        				modelAndView=new  ModelAndView(url.get(COLLECT1));
        			}
        			
        		}
    			modelAndView.addObject("surveyId", surveyId);
    			modelAndView.addObject("baseUrl", baseUrl);
    			modelAndView.addObject("survey", surveyDirectory);
    			return modelAndView;
    		}
    	}
		return null;
	}
	
	
}
