<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<title>信息提示</title>
<link href="${ctx }/css/response.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

function showMsgJG(){
	alert('111111');
	return '1111111';
}
</script>
</head>
<body style="background: rgb(245, 245, 245);">
	<div class="root-body" style="padding-top: 80px;">
		<div class="middle-body" style="padding-top:10px;">
			
			<div class="middle-body-content" style="text-align: center;">
				<p class="msg1" style="font-size: 26px;">
					问卷发布成功！<br/>
				</p>
			</div>
		</div>
		
		<div class="footer-copyright" style="color: gray;">
			<%--尊重开源、保留声明，感谢您的大力支持--%>
			<img src="${ctx }/images/schoolLogo.png" height="50"/>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/page/layouts/other.jsp"%>
</body>
</html>