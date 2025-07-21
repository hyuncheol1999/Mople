<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>번개 모임 홈</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/bungaeHome.css"
	type="text/css">
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp" />
	<main class="main">
		<div class="container bungae-home">

			<!-- 히어로 -->
			<section class="bungae-hero">
				<div>
					<h1>🔥 지금 바로 참여할 번개 모임</h1>
					<p>번개로 전환된 정기모임도 즐기고, 그냥 번개도 즐겨봐요!!</p>
				</div>
				<div class="hero-actions">
					<button type="button" class="btn btn-outline btn-large"
						onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/bungaeMeetingCreate'">
						번개모임 생성⚡</button>
				</div>
			</section>
			<br>
			<!-- 검색 -->
			<section class="search-bar">
				<form method="get"
					action="${pageContext.request.contextPath}/bungaeMeeting/home"
					class="search-form">
					<input type="hidden" name="page" value="1">
					
					<div class="search-input-group">
						<input type="text" name="q" placeholder="지금 참여할 번개 검색" value="${fn:escapeXml(q)}">
						<button type="submit" class="btn btn-outline">검색</button>
					</div>
				</form>
			</section>
			<br><br>
			<!-- 정기모임 (번개 전환) -->
			<section class="module-block">
				<div class="module-header">
					<h2>🗓 정기모임(번개 전환)</h2>
					<br><br>
					<a href="${pageContext.request.contextPath}/bungaeMeeting/list"
						class="more-link" style="text-decoration: underline;">전체보기</a>
				</div>
				<div class="card-row">
					<c:choose>
						<c:when test="${empty urgentRegularMeetings}">
							<div class="empty-box">번개로 전환된 정모가 없습니다.</div>
						</c:when>
						<c:otherwise>
							<c:forEach var="r" items="${urgentRegularMeetings}">
								<div class="bungae-card"
									onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDetail?meetingIdx=${r.meetingIdx}'">
									<div class="card-body">
										<h3 class="card-title">${r.subject}</h3>
										<p class="card-text">
											일시 : <c:if test="${not empty r.startDateOnly}">      
						                    <c:out value="${fn:substring(r.startDateOnly,5,7)}/${fn:substring(r.startDateOnly,8,10)}"/>&nbsp;${r.startTimeStr}
						                    <c:if test="${not empty r.endDateOnly}">~${r.endTimeStr}</c:if>
						                    <br/>
						                  </c:if>
											모임장소 : ${r.place}
										</p>
										<div class="card-footer">
											<c:choose>
												<c:when test="${r.joined}">
													<button class="btn btn-primary" onclick="cancelSchedule(${r.regularMeetingIdx})">참여취소</button>
												</c:when>
												<c:otherwise>
													<button class="btn btn-primary" onclick="joinSchedule(${r.regularMeetingIdx});" class="btn">참여</button>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="tags">
											<span class="tag capacity">👥
												${r.currentCnt}/${r.capacity}명</span>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</section>
			<br>
			<!-- 이번주 번개 -->
			<section class="module-block">
				<div class="module-header">
					<h2>⚡ 이번주 번개</h2>
				</div>
				<div class="card-row">
					<c:choose>
						<c:when test="${empty weeklyBungaeMeetings}">
							<div class="empty-box">등록된 번개가 없습니다.</div>
						</c:when>
						<c:otherwise>
							<c:forEach var="b" items="${weeklyBungaeMeetings}">
								<c:set var="curr"
									value="${b.currentCnt != null ? b.currentCnt : 0}" />
								<div class="bungae-card"
									onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/detail?bungaeMeetingIdx=${b.bungaeMeetingIdx}'">
									<div class="card-body">
										<h3 class="card-title">${b.subject}</h3>
										<p class="card-text"> 날짜 :
											<c:if test="${b.startDateAsDate ne null}">
												<fmt:formatDate value="${b.startDateAsDate}"
													pattern="MM/dd HH:mm" />
												<c:if test="${b.endDateAsDate ne null}">~<fmt:formatDate
														value="${b.endDateAsDate}" pattern="HH:mm" />
												</c:if>
												<br />
											</c:if>
											모임장소 : ${b.place}
										</p>
										<div class="tags">
											<span class="tag capacity">👥 ${curr}/${b.capacity}명</span>
											<c:if test="${b.multiDay}">
												<span class="tag warn">Multi-day</span>
											</c:if>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</section>

		</div>
	</main>

	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script src="${pageContext.request.contextPath}/dist/js/bungaeHome.js"></script>
</body>
</html>
