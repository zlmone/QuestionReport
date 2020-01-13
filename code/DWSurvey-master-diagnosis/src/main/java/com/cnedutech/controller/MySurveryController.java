package com.cnedutech.controller;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cnedutech.service.MySurveyService;
import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.common.plugs.page.Page;
import com.key.common.utils.DiaowenProperty;
import com.key.common.utils.web.Struts2Utils;
import com.key.dwsurvey.entity.Question;
import com.key.dwsurvey.entity.SurveyDetail;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.entity.SurveyStyle;
import com.key.dwsurvey.service.QuestionManager;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import com.key.dwsurvey.service.SurveyReqUrlManager;
import com.key.dwsurvey.service.SurveyStyleManager;


@Controller
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
	
	@Autowired
	private SurveyDirectoryManager directoryManager;
	
	
	@Autowired
	MySurveyService mySurveyService ;
	
	@RequestMapping(value="/design/my-survey")
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
	@RequestMapping(value="/design/my-survey-design")
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
	@RequestMapping(value="/design/my-collect")
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
	
	
	/**
	 * 我的问卷预览页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/design/my-survey-design!previewDev")
	public ModelAndView previewDev(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView("content/diaowen-design/survey_preview_dev");
		String surveyId=request.getParameter("surveyId");
		buildSurvey( surveyId, modelAndView);
		return modelAndView;
	}
	
	@RequestMapping("/design/previewDev")
	public ModelAndView previewDev(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		String surveyId=request.getParameter("surveyId");
		buildSurvey(surveyId,modelAndView);
		
		return modelAndView;
	}
	
	private void buildSurvey(String surveyId,ModelAndView modelAndView) {
		//判断是否拥有权限
		User user= accountManager.getCurUser();
		if(user!=null){
			String userId=user.getId();
			SurveyDirectory surveyDirectory=surveyDirectoryManager.getSurveyByUser(surveyId, userId);
			if(surveyDirectory!=null){
				surveyDirectoryManager.getSurveyDetail(surveyId, surveyDirectory);
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
	}
	
	@RequestMapping("/design/my-survey-create!save")
	public ModelAndView save(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView =  null;
		String surveyName=request.getParameter("surveyName");
		SurveyDirectory survey = new SurveyDirectory();
	    try{
	    	survey.setDirType(2);
	    	if(surveyName==null || "".equals(surveyName.trim())){
	    		surveyName="请输入问卷标题";
	    	}else{
	    		surveyName=URLDecoder.decode(surveyName,"utf-8");
	    	}
	 	    survey.setSurveyName(surveyName);
	 	    directoryManager.save(survey);
	 	    String  surveyId = survey.getId();
	 		modelAndView = new ModelAndView("redirect:my-survey-design?surveyId="+surveyId);//modelandview 重定向
	 	   modelAndView.addObject("surveyId", surveyId);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return modelAndView;
	}
	
	/**
	 * 删除文件调查接口
	 * @param reponse
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/design/my-survey!delete")
	@ResponseBody
	public String mySurveyDelete(HttpServletResponse reponse,HttpServletRequest request) throws Exception {
	    String result="false";
	    String id = request.getParameter("id");
	    try{
		User user = accountManager.getCurUser();
		System.err.println(user.getLoginName());
		if(user!=null){
		    String userId=user.getId();
		    SurveyDirectory surveyDirectory=surveyDirectoryManager.getSurveyByUser(id,userId);
		    if(surveyDirectory!=null){
		    	surveyDirectoryManager.delete(id);
		    	result="true";
		    }
		}
	    }catch (Exception e) {
	    	e.printStackTrace();
			result="false";
	    }
	    return result;
	}
	
	@RequestMapping("/design/my-survey-design!devSurvey")
	public ModelAndView devSurvey(HttpServletRequest request ,HttpServletResponse reponse) throws Exception {
		ModelAndView modelAndView = new ModelAndView("content/diaowen-design/release-success");
		String surveyId = request.getParameter("surveyId");
		SurveyDirectory survey=surveyDirectoryManager.get(surveyId);
		Date createDate=survey.getCreateDate();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd/");
		try{
			String url="/survey!answerSurvey?surveyId="+surveyId;
			String filePath="WEB-INF/wjHtml/"+dateFormat.format(createDate);
			String fileName=surveyId+".html";
			//new JspToHtml().postJspToHtml(url, filePath, fileName,request);
			mySurveyService.postJspToHtml(url, filePath, fileName,request,reponse);
			survey.setHtmlPath(filePath+fileName);

			url="/survey!answerSurveryMobile?surveyId="+surveyId;
			filePath="WEB-INF/wjHtml/"+dateFormat.format(createDate);
			fileName="m_"+surveyId+".html";
			//new JspToHtml().postJspToHtml(url, filePath, fileName,request);
			mySurveyService.postJspToHtml(url, filePath, fileName,request,reponse);
			survey.setSurveyState(1);
			surveyDirectoryManager.save(survey);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView; //转向成功提示页面
	}
	


	@RequestMapping("/design/ajaxSave")
	@ResponseBody
	public String ajaxSave(HttpServletRequest request ,HttpServletResponse response) throws Exception {
		String surveyId = request.getParameter("surveyId");
		String svyName=request.getParameter("svyName");
		String svyNote=request.getParameter("svyNote");
		//属性
		String effective=request.getParameter("effective");
		String effectiveIp=request.getParameter("effectiveIp");
		String rule=request.getParameter("rule");
		String ruleCode=request.getParameter("ruleCode");
		String refresh=request.getParameter("refresh");
		String mailOnly=request.getParameter("mailOnly");
		String ynEndNum=request.getParameter("ynEndNum");
		String endNum=request.getParameter("endNum");
		String ynEndTime=request.getParameter("ynEndTime");
		String endTime=request.getParameter("endTime");
		String showShareSurvey=request.getParameter("showShareSurvey");
		String showAnswerDa=request.getParameter("showAnswerDa");
		SurveyDirectory survey=surveyDirectoryManager.getSurvey(surveyId);
		SurveyDetail surveyDetail=survey.getSurveyDetail();
		User user= accountManager.getCurUser();
		if(user!=null && survey!=null){
			String userId=user.getId();
			if(userId.equals(survey.getUserId())){
				
				if( svyNote!=null){
					svyNote=URLDecoder.decode(svyNote,"utf-8");
					surveyDetail.setSurveyNote(svyNote);
				}
				if(svyName!=null && !"".equals(svyName)){
					svyName=URLDecoder.decode(svyName,"utf-8");
					survey.setSurveyName(svyName);
				}

				//保存属性
				if(effective!=null && !"".equals(effective)){
				    surveyDetail.setEffective(Integer.parseInt(effective));
				}
				if(effectiveIp!=null && !"".equals(effectiveIp)){
				    surveyDetail.setEffectiveIp(Integer.parseInt(effectiveIp));
				}
				if(rule!=null && !"".equals(rule)){
				    surveyDetail.setRule(Integer.parseInt(rule));
				    surveyDetail.setRuleCode(ruleCode);
				}
				if(refresh!=null && !"".equals(refresh)){
				    surveyDetail.setRefresh(Integer.parseInt(refresh));
				}
				if(mailOnly!=null && !"".equals(mailOnly)){
				    surveyDetail.setMailOnly(Integer.parseInt(mailOnly));
				}
				if(ynEndNum!=null && !"".equals(ynEndNum)){
				    surveyDetail.setYnEndNum(Integer.parseInt(ynEndNum));
				    //surveyDetail.setEndNum(Integer.parseInt(endNum));
				    if(endNum!=null && endNum.matches("\\d*")){
					surveyDetail.setEndNum(Integer.parseInt(endNum));			
				    }
				}
				if(ynEndTime!=null && !"".equals(ynEndTime)){
				    surveyDetail.setYnEndTime(Integer.parseInt(ynEndTime));
//				    surveyDetail.setEndTime(endTime);
				    surveyDetail.setEndTime(new Date());
				}
				if(showShareSurvey!=null && !"".equals(showShareSurvey)){
				    surveyDetail.setShowShareSurvey(Integer.parseInt(showShareSurvey));
				    survey.setIsShare(Integer.parseInt(showShareSurvey));
				}
				if(showAnswerDa!=null && !"".equals(showAnswerDa)){
				    surveyDetail.setShowAnswerDa(Integer.parseInt(showAnswerDa));
				    survey.setViewAnswer(Integer.parseInt(showAnswerDa));
				}
				
				surveyDirectoryManager.save(survey);

				response.getWriter().write("true");
				
			}
		}
		
		return "none";
	}
	
	@RequestMapping("/design/copySurvey")
	public ModelAndView copySurvey(HttpServletRequest request) throws Exception {
		
		String surveyId = null;
		String fromBankId=request.getParameter("fromBankId");
		String surveyName=request.getParameter("surveyName");
		surveyName=URLDecoder.decode(surveyName,"utf-8");
		String tag=request.getParameter("tag");
		tag="2";
		
		SurveyDirectory directory=surveyDirectoryManager.createBySurvey(fromBankId,surveyName,tag);
		surveyId=directory.getId();
		ModelAndView modelAndView = new ModelAndView("redirect:my-survey-design?surveyId="+surveyId);
		return modelAndView;
	}
	
	private void buildSurveyHtml() throws Exception{
		HttpServletRequest request=Struts2Utils.getRequest();
		HttpServletResponse response=Struts2Utils.getResponse();
		String url = "";
		String name = "";
		ServletContext sc = ServletActionContext.getServletContext();

		String file_name = request.getParameter("file_name");
		url = "/design/my-collect?surveyId=402880ea4675ac62014675ac7b3a0000";
		// 这是生成的html文件名,如index.htm.
		name = "/survey.htm";
		name = sc.getRealPath(name);
		
		RequestDispatcher rd = sc.getRequestDispatcher(url);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		final ServletOutputStream stream = new ServletOutputStream() {
			public void write(byte[] data, int offset, int length) {
				os.write(data, offset, length);
			}

			public void write(int b) throws IOException {
				os.write(b);
			}
		};
		
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os,"utf-8"));

		HttpServletResponse rep = new HttpServletResponseWrapper(response) {
			public ServletOutputStream getOutputStream() {
				return stream;
			}

			public PrintWriter getWriter() {
				return pw;
			}
		};

//		rd.include(request, rep);
		rd.forward(request,rep);
		pw.flush();
		
		// 把jsp输出的内容写到xxx.htm
		File file = new File(name);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		
		os.writeTo(fos);
		fos.close();
	}

	
}
