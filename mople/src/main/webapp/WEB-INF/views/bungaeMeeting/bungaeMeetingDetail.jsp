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
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/meeting.css" type="text/css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
	const contextPath = '${pageContext.request.contextPath}';
	const meetingIdx = '${meetingIdx}';
</script>
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp" />
	<div class="main">
		<div class="bungaeMeeting-detail"
			data-contextPath="${pageContext.request.contextPath}"
			data-bungaeMeetingIdx="${bungaeMeetingIdx}">
			<div class="bungaeMeeting-header">
				<div class="title">
						<h3>번개모임 소개</h3>
					</div>
				<div class="BungaeMeeting-info">
					
					<p id="subject">${subject}</p>
					<p id="startDate">${startDate}</p>
					<p id="content">${content}</p>
				</div>
			</div>
		</div>
	</div>
	<div class="members-section">
		<h3>멤버</h3>
		<div class="members-grid">
			<c:forEach var="dto" items="${memberOfBungaeMeetingList}"
				varStatus="status">
				<div class="member-card">
					<img
						src=<c:choose>					
				<c:when test="${not empty dto.memberProfilePhoto}">						
					'${pageContext.request.contextPath}/uploads/member/${dto.memberProfilePhoto}'
				</c:when>
				<c:otherwise>
					'${pageContext.request.contextPath}/dist/images/defaultPerson.png'	
				</c:otherwise>
			</c:choose>>
					<span>${dto.memberNickName} </span>
					<c:if test="${dto.role == 0}">
						<img class="managerLogo"
							src="${pageContext.request.contextPath}/dist/images/manager.png"
							alt="Manager">
					</c:if>
				</div>
			</c:forEach>
		</div>

		<div class="reply">
			<form name="replyForm" method="post">
				<div class="form-header">
					<span class="bold">댓글</span><span> - 번개모임 참여를 원하시면 댓글을
						남겨주세요.</span>
				</div>
				
				<table class="table table-borderless reply-form">
					<tr>
						<td><textarea class="form-control" name="content"></textarea>
						</td>
					</tr>
					<tr>
						<td align="right">
							<button type="button" class="btn btn-light btnSendReply">댓글
								등록</button>
						</td>
					</tr>
				</table>
			</form>

			<div id="listReply"></div>
		</div>



	</div>