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
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />
		<main class="main">
			<div class="container">
				<div class="meeting-detail"
					data-contextPath="${pageContext.request.contextPath}"
					data-meetingIdx="${meetingIdx}">
					<div class="meeting-header">
						<div class="meeting-info">
							<img
								src=<c:choose>					
									<c:when test="${not empty meetingProfilePhoto}">						
										'${pageContext.request.contextPath}/uploads/meetingProfilePhoto/${meetingProfilePhoto}'
									</c:when>
									<c:otherwise>
										'${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhotoMini.png'	
									</c:otherwise>
								</c:choose>
								alt="Group Profile" class="group-avatar">
							<script
								src="${pageContext.request.contextPath}/dist/js/handleImageErrors.js"></script>
							<div class="meeting-meta">
								<h2 class="meetingTitle" id="meetingTitle">${meetingName}</h2>
								<div class="meeting-tags">
									<span class="tag" id="sportTag">${sportName}</span> <span
										class="tag" id="regionTag">${regionName}</span> <span
										class="tag" id="membersTag">${memberCount}</span>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${userStatus eq 'NOT_JOINED'}">
								<button type="button" class="btn btn-primary"
									onclick="joinMeeting(${meetingIdx})">모임 신청</button>
							</c:when>
							<c:when test="${userStatus eq 'HOST'}">
								<button type="button" class="btn btn-primary"
									onclick="location.href='${pageContext.request.contextPath}/meeting/meetingUpdate?meetingIdx=${meetingIdx}'">모임
									정보 수정</button>
							</c:when>
							<c:when test="${userStatus eq 'JOINED'}">
							</c:when>
							<c:when test="${userStatus eq 'WAITING'}">
								<div class="btn-wait">승인 대기중</div>
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
							onclick="location.href='${pageContext.request.contextPath}/meetingBoard/list?meetingIdx=${meetingIdx}'">모임&nbsp;게시판</button>
					</nav>
					<div class="meeting-content">
						<div class="meeting-info-top">
							<div class="meeting-description-area">
								<h3>a</h3>
								<p id="meetingDescription">${meetingDesc}</p>
								<div class="meeting-stats">
									<div class="stat-card">
										<img width="36" height="38"
											src="https://img.icons8.com/ios-filled/50/region-code.png"
											alt="region-code" />
										<h4>지역</h4>
										<p>${regionName}</p>
									</div>
									<div class="stat-card">
										<img width="40" height="40"
											src="https://img.icons8.com/fluency-systems-filled/48/user.png"
											alt="user" />
										<h4>멤버</h4>
										<p>${memberCount}명</p>
									</div>
									<div class="stat-card">
										<img width="40" height="40"
											src="https://img.icons8.com/ios/50/calendar--v1.png"
											alt="calendar--v1" />
										<h4>생성일</h4>
										<p>${createdDate}</p>
									</div>
								</div>
							</div>

							<div class="latest-posts-section">
								<h3>c</h3>
								<p>text</p>
								<a
									href="${pageContext.request.contextPath}/meetingBoard/write?meetingIdx=${meetingIdx}"
									class="view-more-posts">첫 게시글 작성하기 <i class="fas fa-edit"></i></a>
							</div>
						</div>
						<div class="members-section">
							<h3>b</h3>
							<div class="members-grid">
								<c:forEach var="dto" items="${memberOfMeetingList}"
									varStatus="status">
									<div class="member-card">
										<span>${dto.memberNickName} </span>
										<c:if test="${dto.role == 0}">
											<img class="managerLogo"
												src="${pageContext.request.contextPath}/dist/images/manager.png"
												alt="Manager">
										</c:if>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
			</div>
		</main>
	</div>
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>
