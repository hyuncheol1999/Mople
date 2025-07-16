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
	href="${pageContext.request.contextPath}/dist/css/meetingCreate.css"
	type="text/css">
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />

		<main class="main">
			<div class="meetingContainer">
				<h2>
					<c:choose>
						<c:when test="${mode eq 'update'}">정모 수정하기 🛠️</c:when>
						<c:otherwise>정모 만들기 😊</c:otherwise>
					</c:choose>
				</h2>

				<form name="regularMeetingForm" method="post" enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/meeting/regularMeetingCreate">

					<input type="hidden" name="mode" value="${mode}">
					<input type="hidden" name="meetingIdx" value="${meetingIdx}">
					<input type="hidden" name="regularMeetingIdx" value="${dto.regularMeetingIdx}">

					<div class="form-group">
						<label class="title" for="subject">정모명</label>
						<input type="text" id="subject" name="subject"
							placeholder="정모 이름을 입력해주세요 (필수)"
							value="${dto.subject}" required>
						<div class="error-message" id="subjectError">정모명을 입력해주세요.</div>
					</div>

					<div class="form-group">
						<label for="startDate">정모날짜</label>
						<input type="date" id="startDate" name="startDate"
							value="${dto.startDate}" required>
					</div>

					<div class="form-group">
						<label for="place">장소</label>
						<input type="text" name="place" id="place"
							placeholder="장소를 입력해주세요 (필수)"
							value="${dto.place}" required>
					</div>

					<div class="form-group">
						<label for="capacity">정원수</label>
						<input type="number" name="capacity" id="capacity"
							value="${dto.capacity}" required>
					</div>

					<div class="button-group">
						<c:choose>
							<c:when test="${mode eq 'update'}">
								<button type="submit" class="btn btn-outline btn-large">수정하기</button>
							</c:when>
							<c:otherwise>
								<button type="submit" class="btn btn-outline btn-large">정모생성</button>
							</c:otherwise>
						</c:choose>
						<button class="btn btn-outline btn-large" type="button" onclick="history.back()">취소</button>
					</div>

				</form>
			</div>
		</main>
	</div>

	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />

	<script>
		const contextPath = "${pageContext.request.contextPath}";
		const mode = "${mode}";
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/regularMeetingCreate.js"></script>
</body>
</html>
