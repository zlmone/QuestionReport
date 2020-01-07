<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>404页面找不到，在线调查问卷</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="${ctx }/css/templatemo_style.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/css/default.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/css/dw-user.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/css/dw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.divH404{
	font-size: 28px;
	color: #0081CC;
	padding: 10px 0px;
}
#loginContent{
	min-height: 360px;
}
</style>
</head>
<body>
<input type="hidden" name="ctx" value="${ctx }">

<div id="body_wrapper" style="margin-bottom: 10px;">
<div class="wrapper" style="width: 1000px;">
				
   				<div id="loginContent" style="margin-top: 60px;">
   				<div class="loginContentBody">
   					<div class="loginContentLeft" style="padding:0px 50px;">
								<div style="padding: 30px 0px;">
									<img alt="" src="${ctx }/images/404.jpg">
								</div>
		   				</div>
		   				<div class="loginContentRight">
		   						<div class="m-content-right-body" style="padding:15px 20px;">
		   							<div class="divH404">从前有座山，山里有座庙，</div>
		   							<div class="divH404">庙里有个页面，现在找不到。</div>
		   							<div style="padding-top: 30px;">构筑调查新模式，探索企业的未来。&nbsp;&nbsp;<a href="${ctx }/" class="btn001">返回首页</a></div>
								</div>
		   				</div>
		   				<div style="clear:both;"></div>
   					</div>
   					<!-- <div class="loginContentTool"><input type="submit"  value=" 立 即 注 册 " class="btnGreen" /></div> -->
   					<div style="clear:both;"></div>
  				</div>
  
  </div>
  </div>
 <%@ include file="/WEB-INF/page/layouts/footer.jsp"%>  
</body>
</html>