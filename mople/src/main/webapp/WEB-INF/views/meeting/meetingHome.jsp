<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="meeting-description">
    <h3>미팅 소개</h3>
    <p id="meetingDescription">
		${meetingDesc}
    </p>
</div>

<div class="meeting-stats">
    <div class="stat-card">
        <h4>지역</h4>
        <p>${regionName}</p>
    </div>
    <div class="stat-card">
        <h4>인원</h4>
        <p>${currentMembers}</p>
    </div>
</div>

<div class="members-section">
    <h3>멤버</h3>
    <div class="members-grid">
    <c:forEach var="dto" items="${memberOfMeetingList}" varStatus="status">
        <div class="member-card">
            <img src=
            <c:choose>					
				<c:when test="${not empty dto.memberProfilePhoto}">						
					'${pageContext.request.contextPath}/uploads/memberProfilePhoto/${dto.memberProfilePhoto}'
				</c:when>
				<c:otherwise>
					'${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhoto.png'	
				</c:otherwise>
			</c:choose>
             alt="Member">
            <span>${dto.memberName}
            </span>
            <c:if test="${dto.role == 0}">
	           	<img class="managerLogo" src="${pageContext.request.contextPath}/dist/images/manager.png" alt="Manager">
	        </c:if>
        </div>
	</c:forEach>
    </div>
</div>