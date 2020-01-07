<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/WEB-INF/page/layouts/other.jsp"%>
<div id="header">
	<div id="headerCenter" class="bodyCenter" >
		<div class="header_Item header_logo">

			<%@ include file="logo-img.jsp"%>
		</div>
		<shiro:guest>
			<div class="header_Item header_menu">
				<ul>
				</ul>
			</div>
			<div class="header_Item header_user" style="float: right;">
				<a href="${ctx }/login.jsp" class="btn-a-1">登录</a>
			</div>
		</shiro:guest>

		<shiro:user>
			<div class="header_Item header_menu">
				<ul>
					<%-- <li><a href="${ctx }/" >首页</a></li> --%>
					<li><a href="${ctx }/design/my-survey.action" id="mysurvey">问卷</a></li>
					<shiro:hasRole name="admin">
						<li><a href="${ctx }/sy/user/user-admin.action"
							id="usermanager">用户</a></li>
						<li><a href="${ctx }/sy/system/sys-property!input.action"
							id="systemset">设置</a></li>
					</shiro:hasRole>
				</ul>
			</div>
			<div class="header_Item header_user"
				style="float: right; margin-top: 12px; position: relative; zoom: 1; z-index: 165;">
				<a href="#" class="clickHideUserMenu"> <span
					class="head_use_name"> <shiro:principal></shiro:principal>
				</span> <span class="header_user_icon">&nbsp;</span>
				</a>
				<div class="a-w-sel a-w-sel-head" style="">
					<div class="w-sel" style="margin-top: 16px;">
						<div class="selc">
							<div class="selcc tbtag">
								<div class="seli">
									<a class="nx-1" href="${ctx }/ic/user!myaccount.action">修改密码</a>
								</div>
								<div class="seli">
									<a class="nx-8" href="${ctx }/login!logout.action">退出</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</shiro:user>
	</div>
	<div style="clear: both;"></div>
</div>