<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="container">
	<div class="logo">
		<a href="${pageContext.request.contextPath}/admin">  
			<img src="${pageContext.request.contextPath}/dist/images/logo.png" alt="모플">
		</a>
	</div>
	<div class="auth-section">
		<c:choose>
			<c:when test="${not empty sessionScope.member.profilePhoto}">
				<span class="img-person" style="background-image: url('${pageContext.request.contextPath}/uploads/member/${sessionScope.member.profilePhoto}');"></span>
			</c:when>
			<c:otherwise>
				<span class="img-person" style="background-image: url('${pageContext.request.contextPath}/dist/images/avatar.png');"></span>
			</c:otherwise>
		</c:choose>
		<span>|</span>
		<a class="nav-link"
			href="${pageContext.request.contextPath}/main"
			title="로그아웃">로그아웃</a>
	</div>
</div>

