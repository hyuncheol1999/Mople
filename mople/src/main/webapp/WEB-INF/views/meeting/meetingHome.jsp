<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<div class="meeting-description">
    <h3>모임 소개</h3>
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
        <p>${memberCount}</p>
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
            >
            <span>${dto.memberName}
            </span>
            <c:if test="${dto.role == 0}">
	           	<img class="managerLogo" src="${pageContext.request.contextPath}/dist/images/manager.png" alt="Manager">
	        </c:if>
        </div>
	</c:forEach>
    </div>
    <c:if test="${userStatus eq 'HOST'}">
    	<br>
	    <h3>승인 대기 목록</h3>
	    <c:choose>
		    <c:when test="${not empty waitingList}">
			    <div class="members-grid waiting-grid">
			    <c:forEach var="dto" items="${waitingList}" varStatus="status">
			        <div class="member-card">
			            <div class="waiting-body">
			            <img src=
			            <c:choose>					
							<c:when test="${not empty dto.memberProfilePhoto}">						
								'${pageContext.request.contextPath}/uploads/memberProfilePhoto/${dto.memberProfilePhoto}'
							</c:when>
							<c:otherwise>
								'${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhoto.png'	
							</c:otherwise>
						</c:choose>
			             >
			            	<span>${dto.memberName}</span>
				        	<button class="icon-button approve-button" onclick="approveMember(${dto.meetingIdx}, ${dto.memberIdx})"><i class="fas fa-check"></i></button>
			    			<button class="icon-button reject-button" onclick="rejectMember(${dto.meetingIdx}, ${dto.memberIdx})"><i class="fas fa-times"></i></button>
			        	</div>
			        </div>
				</c:forEach>
			    </div>
	    	</c:when>
	    	<c:otherwise>
	    		<p>현재 승인 대기 목록이 없습니다.</p>
	    	</c:otherwise>
    	</c:choose>
    	<div class="">
			<c:if test="${userStatus eq 'HOST'}">
				<button type="button" class="btn btn-primary btn-small" onclick="location.href='${pageContext.request.contextPath}/meeting/meetingDelete?meetingIdx=${meetingIdx}'">모임 해체</button>
				<button type="button" class="btn btn-primary btn-small" onclick="location.href='${pageContext.request.contextPath}/meeting/leaveMeeting?meetingIdx=${meetingIdx}'">모임 탈퇴</button>
			</c:if>
		</div>
    </c:if>
</div>