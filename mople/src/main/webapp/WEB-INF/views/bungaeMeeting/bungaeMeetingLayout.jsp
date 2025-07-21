<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="meetings-layout">
	<div class="list">
		<section class="hero meeting-hero">
			<div class="container">
				<p class="hero-subtitle">정기모임 인원수 부족할 땐? 운영자가 번개모임으로 등록!</p>
				<h2>🔥 정기모임 임박</h2>

				<form action="${pageContext.request.contextPath}/" method="get">
					<input type="text" name="keyword" placeholder="제목+내용 검색">
					<button type="submit">검색</button>
				</form>
			</div>
		</section>
			<div class="meetings-grid" id="meetingsGrid">
				<c:choose>
					<c:when test="${not empty regularList}">
						<c:forEach var="dto" items="${regularList}" varStatus="status">
							<div class="meeting-card" 
							onclick="location.href='${articleUrl}&regularMeetingIdx=${dto.regularMeetingIdx}'">
								<div class="meeting-card-header"
									style="background-image: url('${pageContext.request.contextPath}/dist/imagesdefaultMeetingProfilePhoto.png')">

									<p class="subject">${dto.subject}</p>
									<p class="place">${dto.place}</p>
									<p class="startDate">${dto.startDate}</p>
									<div class="meeting-tags">
										<span class="tag">👥 ${dto.currentCnt}</span>
									</div>
								</div>
							</div>
						</c:forEach>
					</c:when>
				</c:choose>
			</div>
	</div>
</div>

<div class="meetings-content">
	<div class="meetings-header">
		<h1 class="meetingHead">번개모임</h1>
	</div>
	<div class="meetings-grid" id="meetingsGrid">
		<c:choose>
			<c:when test="${not empty regularList}">
				<c:forEach var="dto" items="${regularList} varStatus="status">
					<div class="meeting-card" 
						onclick="location.href='${articleUrl}&regularMeetingIdx=${dto.regularMeetingIdx}'">
						<div class="meeting-card-header"
							style="background-image: url('${pageContext.request.contextPath}/dist/imagesdefaultMeetingProfilePhoto.png')">

							<p class="subject">${dto.subject}</p>
							<p class="place">${dto.place}</p>
							<p class="startDate">${dto.startDate}</p>
							<div class="meeting-tags">
								<span class="tag">👥 ${dto.currentCnt}</span>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>
</div>
<c:if test="${empty regularList}">
	<p>현재 번개로 전환된 정기모임이 없습니다.</p>
</c:if>

<h2>📅 이번 주 번개모임</h2>
<button class="btn btn-outline btn-small btn-join"
	onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/create'">번개모임
	생성</button>
<c:if test="${not empty bungaeList}">
	<div class="meetings-grid">
		<c:forEach var="dto" items="${bungaeList}">
			<div class="meeting-card"
				onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/bungaeMeetingDetail?bungaeMeetingIdx=${dto.bungaeMeetingIdx}'">
				<p class="subject">${dto.subject}</p>
				<p class="content">${dto.content}</p>
				<div class="meeting-tags">
					<span class="tag">${dto.startDate}</span> <span class="tag">${dto.sportName}</span>
					<span class="tag">${dto.regionName}</span> <span class="tag">👥
						${dto.currentMembers}</span>
				</div>
			</div>
		</c:forEach>
	</div>
</c:if>
<c:if test="${empty bungaeList}">
	<div class="meeting-empty-container">
		<h3>앗! 등록된 번개모임이 없어요... 😢</h3>
		<p>
			다른 흥미로운 번개모임을 찾아보시거나, <br>새로운 번개모임을 직접 만들어보는 건 어떠세요?
		</p>
		<div class="button-group">
			<button type="button" class="btn btn-primary"
				onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/bungaeMeetingList?sportCategory=0&regionCategory=0'">다른
				번개모임 찾아보기!</button>
			<button type="button" class="btn btn-primary"
				onclick="location.href='${pageContext.request.contextPath}/bungaeMeeting/bungaeMeetingCreate'">새
				번개모임 만들기!</button>
		</div>
		<img
			src="${pageContext.request.contextPath}/dist/images/notMeetingList.png">
	</div>
</c:if>


<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
<script src="${pageContext.request.contextPath}/dist/js/meeting.js"></script>
</body>
</html>
