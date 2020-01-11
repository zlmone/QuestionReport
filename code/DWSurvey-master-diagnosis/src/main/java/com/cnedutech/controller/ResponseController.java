package com.cnedutech.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.key.common.base.service.AccountManager;
import com.key.common.plugs.ipaddr.IPService;
import com.key.common.utils.CookieUtils;
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
	public String ajaxCheckSurvey(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String surveyId =request.getParameter("surveyId");
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

}
