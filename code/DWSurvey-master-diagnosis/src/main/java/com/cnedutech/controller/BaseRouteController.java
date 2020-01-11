package com.cnedutech.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.key.common.plugs.ipaddr.IPService;
import com.key.common.utils.DiaowenProperty;
import com.key.common.utils.twodimension.TwoDimensionCode;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.entity.SurveyStyle;
import com.key.dwsurvey.service.QuestionManager;
import com.key.dwsurvey.service.SurveyAnswerManager;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import com.key.dwsurvey.service.SurveyStyleManager;

@Controller
public class BaseRouteController {
	@Autowired
	private SurveyDirectoryManager surveyDirectoryManager;
	@Autowired
	private QuestionManager questionManager;
	@Autowired
	private SurveyStyleManager surveyStyleManager;
	@Autowired
	private IPService ipService;
	@Autowired
	private SurveyAnswerManager surveyAnswerManager;
	
	@RequestMapping("survey!answerTD")
	public void answerTD(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String surveyId = request.getParameter("surveyId");
		String down = request.getParameter("down");

		String baseUrl = "";
		baseUrl = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();

		String encoderContent = baseUrl + "/response!answerMobile?surveyId=" + surveyId;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		BufferedImage twoDimensionImg = new TwoDimensionCode().qRCodeCommon(encoderContent, "jpg", 7);

		ImageIO.write(twoDimensionImg, "jpg", jpegOutputStream);

		if (down == null) {
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			ServletOutputStream responseOutputStream = response.getOutputStream();
			responseOutputStream.write(jpegOutputStream.toByteArray());
			responseOutputStream.flush();
			responseOutputStream.close();
		} else {
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(("diaowen_" + surveyId + ".jpg").getBytes()));
			byte[] bys = jpegOutputStream.toByteArray();
			response.addHeader("Content-Length", "" + bys.length);
			ServletOutputStream responseOutputStream = response.getOutputStream();
			response.setContentType("application/octet-stream");
			responseOutputStream.write(bys);
			responseOutputStream.flush();
			responseOutputStream.close();
		}
	}

	
	//问卷动态访问-移动端
	@RequestMapping("survey!answerSurveryMobile")
	public ModelAndView answerSurveryMobile(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView("content/diaowen-design/answer-survey-mobile");
		String surveyId = request.getParameter("surveyId");
		SurveyDirectory survey=surveyDirectoryManager.getSurvey(surveyId);
		buildSurvey(survey,surveyId,modelAndView);
	    return modelAndView;
	}
	
	private void buildSurvey(SurveyDirectory survey,String surveyId,ModelAndView modelAndView) {
		if (survey==null)
		survey=surveyDirectoryManager.getSurvey(surveyId);
		survey.setQuestions(questionManager.findDetails(surveyId, "2"));
		modelAndView.addObject("survey", survey);
		SurveyStyle surveyStyle=surveyStyleManager.getBySurveyId(surveyId);
		modelAndView.addObject("surveyStyle", surveyStyle);
		modelAndView.addObject("prevHost", DiaowenProperty.STORAGE_URL_PREFIX);
	}

}
