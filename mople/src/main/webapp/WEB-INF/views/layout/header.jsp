<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="container">
	<div class="logo">
		<a href="${pageContext.request.contextPath}/main"> <img
			src="${pageContext.request.contextPath}/dist/images/logo.png"
			alt="모플">
		</a>
	</div>
	<nav class="nav">
		<a href="${pageContext.request.contextPath}/meeting/meetingList?sportCategory=0&regionCategory=0"
			class="nav-link">모임</a>
			 <a href="${pageContext.request.contextPath}/bungaeMeeting/home" class="nav-link">번개&nbsp;모임</a>
			 <a href="${pageContext.request.contextPath}/schedule/main" class="nav-link">경기&nbsp;일정</a> <a href="${pageContext.request.contextPath}/bbs/list" class="nav-link">자유&nbsp;게시판</a>
		<a href="${pageContext.request.contextPath}/sports/facilities" class="nav-link">체육&nbsp;시설&nbsp;정보</a>
		<ul class="navbar-nav flex-nowrap">
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
								고객센터
				</a>
				<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
					<li><a class="dropdown-item" href="${pageContext.request.contextPath}/notice/list">공지사항</a></li>
					<li><a class="dropdown-item" href="${pageContext.request.contextPath}/qna/list">문의하기</a></li>
				</ul>
			</li>
		</ul>
	</nav>
	<div class="auth-section">
		<c:choose>
			<c:when test="${empty sessionScope.member}">
				<a class="nav-link" href="javascript:dialogLogin();" title="로그인">로그인</a>
				<span>|</span>
				<a class="nav-link"
					href="${pageContext.request.contextPath}/member/account"
					title="회원가입">회원가입</a>
			</c:when>
			<c:otherwise>
				<a class="nav-link"
					href="${pageContext.request.contextPath}/member/logout"
					title="로그아웃">로그아웃</a>
					<span>|</span>
				<c:choose>
					<c:when test="${sessionScope.member.role == '0'}">
						<a class="nav-link" href="${pageContext.request.contextPath}/admin"
							title="관리자">관리자</a>			
					</c:when>
					<c:otherwise>
						<a class="nav-link" href="${pageContext.request.contextPath}/member/myPage"
							title="마이페이지">마이페이지</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
</div>

