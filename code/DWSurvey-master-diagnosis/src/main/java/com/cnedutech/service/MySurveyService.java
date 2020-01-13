package com.cnedutech.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cnedutech.controller.HtmllController;
import com.cnedutech.utils.HTTPUtils;

@Component
public class MySurveyService {

	@Autowired
	HtmllController htmllController ;
	
	public void postJspToHtml(String postUrl, String filePath, String fileName, HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		String reqTarget = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
		reqTarget =reqTarget+"/toHtml";
		json.put("url", postUrl);
		json.put("filePath", filePath);
		json.put("fileName", fileName);
		Map<String,String > params = new HashMap<String,String>();
		params.put("url", postUrl);
		params.put("filePath", filePath);
		params.put("fileName", fileName);
		//HTTPUtils.postJosnContent(reqTarget,json.toString(),params);
		HTTPUtils.doPostForJson(reqTarget,json.toString());
	}
}
