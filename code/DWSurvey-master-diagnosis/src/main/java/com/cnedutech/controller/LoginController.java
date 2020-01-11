package com.cnedutech.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.key.common.base.service.AccountManager;
import com.key.common.plugs.security.FormAuthenticationWithLockFilter;
import com.key.common.utils.web.Struts2Utils;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class LoginController {

	@Autowired
	private FormAuthenticationWithLockFilter formAuthFilter;
	@Autowired
	protected AccountManager accountManager;
	
	@Autowired  
    private ImageCaptchaService imageCaptchaService;  
	
	@RequestMapping("login")
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("redirect:/design/my-survey");
		System.out.println("username1-1");
		
		Subject subject = SecurityUtils.getSubject();
		boolean isAuth = subject.isAuthenticated();
		// 返回成功与否
		String error="";
		Long resetnum=0L;
		if (!isAuth) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password);
			if(!formAuthFilter.checkIfAccountLocked(request)){
				try {
					subject.login(token);
					formAuthFilter.resetAccountLock(username);
					isAuth = true;
				}catch (IncorrectCredentialsException e) {
					formAuthFilter.decreaseAccountLoginAttempts(request);
		            isAuth = false;
		            error="IncorrectCredentialsException";
		            resetnum=formAuthFilter.getAccountLocked(username);
		        } catch (AuthenticationException e) {
		            isAuth = false;
		            error="AuthenticationException";
		        }
			}else{
				//ExcessiveAttemptsException超过登录次数
				 error="ExcessiveAttemptsException";
			}
		}
		//PrintWriter writer = response.getWriter();    
		//writer.write(isAuth + ","+error);//此种方式，在$.getJson()进行仿问时会出现不执行回调函数
//		System.out.println(isAuth+","+error);
		response.setContentType("text/plain");// 1.设置返回响应的类型
		//2. 01 一定要注意：要包括jsoncallback参数，不然回调函数不执行。
		//2. 02 返回的数据一定要是严格符合json格式 ，不然回调函数不执行。
        response.getWriter().write( request.getParameter("jsoncallback") + "({isAuth:'"+isAuth+"',error:'"+error+"',resetnum:'"+resetnum+"'})" );
		return modelAndView;
	}

	@RequestMapping("/jcaptcha")
	@ResponseBody
	public String jcaptcha(HttpServletRequest request,HttpServletResponse response) throws Exception {
        byte[] captchaChallengeAsJpeg = null;  
        // the output stream to render the captcha image as jpeg into  
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();  
        try {  
            // get the session id that will identify the generated captcha.  
            // the same id must be used to validate the response, the session id  
            // is a good candidate!
            String captchaId = request.getSession().getId();  
            // call the ImageCaptchaService getChallenge method  
            BufferedImage challenge = imageCaptchaService.getImageChallengeForID(captchaId, request.getLocale());
            // a jpeg encoder
/*** jdk1.7之后默认不支持了 **/
//            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
//            jpegEncoder.encode(challenge);

//            换成新版图片api
            ImageIO.write(challenge, "jpg", jpegOutputStream);

        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();    
        // flush it in the response    
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
        return null;
	}  
	

}
