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
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meeting.css" type="text/css">
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp"/>

	<main class="main">
		<aside class="sidebar">
			<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />
		</aside>
		<div class="container">
			<div class="meetings-layout">
				<div class="list">
					<div class="meetings-content">
						<div>
							<h1>모임</h1>
							<button class="btn btn-primary">Create New Meeting</button>
						</div>

						<div class="meetings-grid" id="meetingsGrid">
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
							<div class="meeting-card"
								onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail'">
								<div class="meeting-card-header">
									<img
										src="${pageContext.request.contextPath}/dist/images/test1.png"
										alt="모임명" class="meeting-avatar">
									<div>
										<h4>힐링런</h4>
									</div>
								</div>
								<p>소개</p>
								<div class="meeting-tags">
									<span class="tag">축구</span> <span class="tag">서울시 강서구</span> <span
										class="tag">👥 12/20</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
	
    <jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/meeting.js"></script>
</body>
</html>
