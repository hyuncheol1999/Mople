<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/meeting.css"
	type="text/css">
<script>
	const contextPath = "${pageContext.request.contextPath}";
</script>
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
			<div class="meetings-layout">
				<div class="list">
					<section class="hero meeting-hero">
						<div class="container">
							<h1 class="hero-title">
								함께 만드는 즐거움!<br> <br>당신의 스포츠 모임을 찾아보세요!
							</h1>
							<br>
							<p class="hero-subtitle">초보자도 환영! 숙련자도 환영! 모두가 함께 땀 흘리고 웃을 수
								있는 우리 동네 모임을 지금 바로 탐색해보세요.</p>
						</div>
					</section>
					<div class="meetings-content">
						<div class="meetings-header">
							<h1 class="meetingHead">모임</h1>
							<button class="btn btn-primary"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingCreate'">Create
								New Meeting</button>
						</div>
						<div class="meetings-grid" id="meetingsGrid">
							<c:forEach var="dto" items="${list}" varStatus="status">
								<div class="meeting-card"
									onclick="location.href='${articleUrl}&meetingIdx=${dto.meetingIdx}'">
									<div class="meeting-card-header"
										style="background-image: url(
												<c:choose>					
												<c:when test="${not empty dto.meetingProfilePhoto}">						
													'${pageContext.request.contextPath}/uploads/meetingProfilePhoto/${dto.meetingProfilePhoto}'
												</c:when>
												<c:otherwise>
													'${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhoto.png'	
												</c:otherwise>
												</c:choose>
											);"></div>
									<p class="meetingName">${dto.meetingName}</p>
									<p class="meetingDesc">${dto.meetingDesc}</p>
									<div class="meeting-tags">
										<span class="tag">${dto.sportName}</span> <span class="tag">${dto.regionName}</span>
										<span class="tag">👥 ${dto.currentMembers}</span>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="page-navigation">${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/meeting.js"></script>
</body>
</html>
