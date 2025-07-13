<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
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
				<c:choose>
					<c:when test="${not empty list}">
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
					</c:when>
				</c:choose>
			</div>
			<c:if test="${empty list}">
				<div class="meeting-empty-container">
		    		<h3>앗! 등록된 모임이 없어요... 😢</h3>
		    		<p>
		        		다른 흥미로운 모임을 찾아보시거나, <br>새로운 모임을 직접 만들어보는 건 어떠세요?
		    		</p>
		    		<div class="button-group">
		        		<button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/meeting/meetingList'">다른 모임 찾아보기</button>
		        		<button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/meeting/meetingCreate'">새 모임 만들기</button>
		    		</div>
				<img src="${pageContext.request.contextPath}/dist/images/notMeetingList.png">
	    		</div>						
			</c:if>
		</div>
	</div>
</div>
<div class="page-navigation">${dataCount == 0 ? "" : paging}</div>