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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<jsp:include page="/WEB-INF/views/layout/login.jsp" />
	<div class="wrap">
		<main class="main">
			<div class="container">
				<div class="meeting-detail"
					data-contextPath="${pageContext.request.contextPath}"
					data-meetingIdx="${meetingIdx}">
					<div class="meeting-header">
						<div class="meeting-info">
							<img
								src=
								<c:choose>					
									<c:when test="${not empty meetingProfilePhoto}">						
										'${pageContext.request.contextPath}/uploads/meetingProfilePhoto/${meetingProfilePhoto}'
									</c:when>
									<c:otherwise>
										'${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhoto.png'	
									</c:otherwise>
								</c:choose>
								 alt="Group Profile" class="group-avatar">
							<div class="meeting-meta">
								<h2 class="meetingTitle" id="meetingTitle">${meetingName}</h2>
								<div class="meeting-tags">
									<span class="tag" id="sportTag">${sportName}</span> <span class="tag"
										id="regionTag">${regionName}</span> <span class="tag"
										id="membersTag">인원수 ${memberCount}명</span>
								</div>
							</div>
						</div>
						<c:choose>
						    <c:when test="${userStatus eq 'NOT_JOINED'}">
						        <button type="button" class="btn btn-primary" onclick="joinMeeting(${meetingIdx})">모임 참여</button>
						    </c:when>
						    <c:when test="${userStatus eq 'HOST'}">
						    </c:when>
						    <c:when test="${userStatus eq 'JOINED'}">
						    </c:when>
						    <c:otherwise>
						    </c:otherwise>
						</c:choose>
					</div>

					<nav class="meeting-nav">
						<button class="nav-tab" onclick="showTabAjax('meetingHome')">홈</button>
						<button class="nav-tab" onclick="showTabAjax('meetingSchedule')">정모&nbsp;일정</button>
						<button class="nav-tab" onclick="showTabAjax('meetingAlbum')">사진첩</button>
						<button class="nav-tab"
							onclick="location.href='${pageContext.request.contextPath}/main'">모임&nbsp;게시판</button>
					</nav>
					<div class="meeting-content"></div>
				</div>
			</div>
		</main>

		<footer class="footer">
			<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
		</footer>
		<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/dist/js/meeting-detail.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/dist/js/meeting-album.js"></script>
	</div>
</body>
</html>
