<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<div class="meeting-info-top">
    <div class="meeting-description-area">
        <h3>모임 소개</h3>
        <p id="meetingDescription">
            ${meetingDesc}
        </p>
        <div class="meeting-stats">
		    <div class="stat-card">
		        <img width="40" height="40" src="https://img.icons8.com/ios-filled/50/region-code.png" alt="region-code"/><h4>지역</h4>
		        <p>${regionName}</p>
		    </div>
		    <div class="stat-card">
		        <img width="40" height="40" src="https://img.icons8.com/fluency-systems-filled/48/user.png" alt="user"/><h4>멤버</h4>
		        <p>${memberCount}명</p>
		    </div>
		    <div class="stat-card">
		        <img width="40" height="40" src="https://img.icons8.com/ios/50/calendar--v1.png" alt="calendar--v1"/><h4>생성일</h4>
		        <p>${createdDate}</p>
		    </div>
		</div>	
	</div>

    <div class="latest-posts-section">
        <h3>최신 게시글</h3>
        <c:choose>
            <c:when test="${not empty meetingBoardList}">
                <ul>
                    <c:forEach var="dto" items="${meetingBoardList}" varStatus="status">
                        <li>
                            <a href="${pageContext.request.contextPath}/meetingBoard/view?num=${dto.num}&meetingIdx=${meetingIdx}">${dto.subject}</a>
                            <div class="post-date">${dto.userNickName}&nbsp;·&nbsp;${dto.reg_date}</div>
                        </li>
                    </c:forEach>
                </ul>
                <a href="${pageContext.request.contextPath}/board/list?meetingIdx=${meetingIdx}" class="view-more-posts">게시글 전체보기 <i class="fas fa-chevron-right"></i></a>
            </c:when>
            <c:otherwise>
                <p>아직 작성된 게시글이 없습니다.</p>
                <a href="${pageContext.request.contextPath}/board/write?meetingIdx=${meetingIdx}" class="view-more-posts">첫 게시글 작성하기 <i class="fas fa-edit"></i></a>
            </c:otherwise>
        </c:choose>
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
    </c:if>
	<div>
		<c:choose>
			<c:when test="${userStatus eq 'HOST'}">
				<br>
				<button type="button" class="btn btn-primary btn-small" onclick="deleteMeeting(${meetingIdx})">모임 해체</button>
				<c:if test="${memberCount > 1}">
					<button type="button" class="btn btn-primary btn-small" onclick="location.href='${pageContext.request.contextPath}/meeting/leaveLeader?meetingIdx=${meetingIdx}'">모임 탈퇴</button>
				</c:if>
			</c:when>
			<c:when test="${userStatus eq 'JOINED'}">
				<br>
				<c:if test="${memberCount > 1}">
					<button type="button" class="btn btn-primary btn-small" onclick="leaveMeeting(${meetingIdx}, ${currentUserIdx})">모임 탈퇴</button>
				</c:if>
			</c:when>
		</c:choose>
	</div>
</div>