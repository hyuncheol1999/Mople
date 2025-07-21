<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모플 - 전체 번개모임</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meeting.css" type="text/css">
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<jsp:include page="/WEB-INF/views/layout/login.jsp" />

	<main class="main">
		<aside class="sidebar">
			<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />
		</aside>

		<div class="container">
			<h2>🔥 전체 번개모임 </h2>

			<!-- 검색창 -->
			<form method="get" action="${pageContext.request.contextPath}/bungaeMeeting/bungaeListAll" style="margin-bottom: 20px;">
				<input type="text" name="search" value="${param.search}" placeholder="제목 또는 내용을 입력하세요" />
				<button type="submit">검색</button>
			</form>

			<!-- 카드 리스트 출력 -->
			<div class="meetings-grid">
				<c:forEach var="dto" items="${allBungaeList}">
					<div class="meeting-card" onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/detail?meetingIdx=${dto.meetingIdx}'">
						<div class="meeting-card-header" style="background-image: url(
							<c:choose>
								<c:when test='${not empty dto.meetingProfilePhoto}'>
									'${pageContext.request.contextPath}/uploads/meetingProfilePhoto/${dto.meetingProfilePhoto}'
								</c:when>
								<c:otherwise>
									'${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhoto.png'
								</c:otherwise>
							</c:choose>
						);">
						</div>
						<p class="meetingName">${dto.subject}</p>
						<p class="meetingDesc">${dto.content}</p>
						<div class="meeting-tags">
							<span class="tag">${dto.place}</span>
							<span class="tag">
								<fmt:formatDate value="${dto.startDate}" pattern="yyyy-MM-dd" />
							</span>
							<span class="tag">👥 ${dto.capacity}명</span>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
	<script src="${pageContext.request.contextPath}/dist/js/meeting.js"></script>
</body>
</html>
