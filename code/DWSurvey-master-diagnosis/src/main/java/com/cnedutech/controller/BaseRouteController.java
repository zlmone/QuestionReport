package com.cnedutech.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.key.common.utils.twodimension.TwoDimensionCode;

@Controller
public class BaseRouteController {

	
	@RequestMapping("survey!answerTD")
	public void answerTD(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String surveyId = request.getParameter("surveyId");
		String down = request.getParameter("down");

		String baseUrl = "";
		baseUrl = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();

		String encoderContent = baseUrl + "/response!answerMobile.action?surveyId=" + surveyId;
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

}
