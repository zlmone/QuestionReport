package com.cnedutech.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.key.common.base.service.AccountManager;
import com.key.common.plugs.ipaddr.IPService;
import com.key.common.utils.CookieUtils;
import com.key.common.utils.HttpRequestDeviceUtils;
import com.key.common.utils.NumberUtils;
import com.key.dwsurvey.entity.SurveyDetail;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.service.EParametersManager;
import com.key.dwsurvey.service.SurveyAnswerManager;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class ResponseController {

	@Autowired
	private SurveyAnswerManager surveyAnswerManager;
	@Autowired
	private SurveyDirectoryManager directoryManager;

	@Autowired
	private IPService ipService;
	@Autowired
	private AccountManager accountManager;
	// @Autowired
	// private GenericManageableCaptchaService captchaService;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private EParametersManager eParametersManager;

	@RequestMapping("response!ajaxCheckSurvey")
	@ResponseBody
	public String ajaxCheckSurvey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String surveyId = request.getParameter("surveyId");
		// 0 1 2
		String ajaxResult = "0";
		try {
			SurveyDirectory directory = directoryManager.getSurvey(surveyId);
			SurveyDetail surveyDetail = directory.getSurveyDetail();
			int effective = surveyDetail.getEffective();
			int rule = surveyDetail.getRule();
			request.setAttribute("directory", null);
			// request.setAttribute("e_uid", "fasdfas");
			// 调查规则
			String surveyStatus = "0";
			// cookie
			Cookie cookie = CookieUtils.getCookie(request, surveyId);
			// 根据 源IP
			String ip = ipService.getIp(request);
			Long ipNum = 0L;
			if (effective > 1) {
				// 根据 cookie过滤
				if (cookie != null) {
					String cookieValue = cookie.getValue();
					if (cookieValue != null && NumberUtils.isNumeric(cookieValue)) {
						ipNum = Long.parseLong(cookieValue);
					}
					surveyStatus = "1";
				} else {
					/*
					 * SurveyAnswer surveyAnswer = surveyAnswerManager.getTimeInByIp(surveyDetail,
					 * ip); if (surveyAnswer != null) { request.setAttribute("msg",
					 * 2);//表示在有效性验证，间隔时间内 surveyStatus="1"; }
					 */
				}
			}

			ipNum = surveyAnswerManager.getCountByIp(surveyId, ip);
			if (ipNum == null) {
				ipNum = 0L;
			}
			Integer effectiveIp = surveyDetail.getEffectiveIp();
			if (effectiveIp != null && effectiveIp == 1 && ipNum > 0) {
				surveyStatus = "2";
			}

			String isCheckCode = "0";
			// 启用验证码
			int refreshNum = surveyDetail.getRefreshNum();
			if (ipNum >= refreshNum) {
				isCheckCode = "3";
			}
			ajaxResult = "{surveyStatus:\"" + surveyStatus + "\",isCheckCode:\"" + isCheckCode + "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write(ajaxResult);
		return null;
	}

	@RequestMapping("response")
	public ModelAndView  execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String surveyId;
		String sid =request.getParameter("sid");
		ModelAndView modelAndView  =null;
		SurveyDirectory directory = directoryManager.getSurveyBySid(sid);
		if (directory != null) {
			surveyId = directory.getId();
			String filterStatus = filterStatus(directory, request);
			if (filterStatus != null) {

				if (filterStatus.contains("answerError")) {
					modelAndView= new ModelAndView("redirect:"+filterStatus+surveyId);
					return modelAndView;
				}else {
					modelAndView= new ModelAndView(filterStatus);
					return modelAndView;
				}
			}
			
			if (HttpRequestDeviceUtils.isMobileDevice(request)) {
				modelAndView=  new ModelAndView("response!answerMobile?surveyId="+surveyId);
				return modelAndView;
			} else {
				String htmlPath = directory.getHtmlPath();
				request.getRequestDispatcher("/" + htmlPath).forward(request, response);
			}
			
		}
		return modelAndView;
	}

	private String filterStatus(SurveyDirectory directory, HttpServletRequest request) {
		SurveyDetail surveyDetail = directory.getSurveyDetail();
		int rule = surveyDetail.getRule();
		Integer ynEndNum = surveyDetail.getYnEndNum();
		Integer endNum = surveyDetail.getEndNum();
		Integer ynEndTime = surveyDetail.getYnEndTime();
		Date endTime = surveyDetail.getEndTime();
		Integer anserNum = directory.getAnswerNum();

		if (directory.getSurveyQuNum() <= 0 || directory.getSurveyState() != 1
				|| (anserNum != null && ynEndNum == 1 && anserNum > endNum)
				|| (endTime != null && ynEndTime == 1 && endTime.getTime() < (new Date().getTime()))) {
			request.setAttribute("surveyName", "目前该问卷已暂停收集，请稍后再试");
			request.setAttribute("msg", "目前该问卷已暂停收集，请稍后再试");
			return "content/diaowen-answer/response-msg";
		}
		if (2 == rule) {
			request.setAttribute("msg", "rule2");
			return "response!answerError?surveyId=";
		} else if (3 == rule) {
			String ruleCode = request.getParameter("ruleCode");
			String surveyRuleCode = surveyDetail.getRuleCode();
			if (ruleCode == null || !ruleCode.equals(surveyRuleCode)) {
				return "content/diaowen-answer/response-input-rule";
			}
		}
		return null;
	}
}
