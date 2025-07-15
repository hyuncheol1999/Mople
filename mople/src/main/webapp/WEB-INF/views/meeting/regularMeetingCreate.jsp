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
				<h2>정모 만들기 😊</h2>
				<form name="meetingForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="meetingIdx" value="${dto.meetingIdx}">
				<input type="hidden" name="regionIdx" value="${dto.regionIdx}">
				<input type="hidden" name="sportIdx" value="${dto.sportIdx}">
				
					<div class="form-group">
						<label class="title" for="subject">정모명</label> 
						<input type="text" id="subject" name="subject" placeholder="정모 이름을 입력해주세요 (필수)">${dto.subject}
					</div>
					<div class="form-group">
						<label for="startDate">정모날짜</label> 
						<input type="text" id="startDate" name="startDate" placeholder="정모 날짜를 입력해주세요 ('형식은 YYYY-MM-DD'입니다)">${dto.startDate}
					</div>
					<div class="form-group">
						<label for="place">장소</label> 
						<input type="text" name="place" id="place" placeholder="장소를 입력해주세요 (필수)">${dto.place}
					</div>
					<div class="form-group">
						<label for="capacity">정원수</label> 
						<input type="text" name="capacity" id="capacity" placeholder="숫자만 입력해주세요 (필수)">${dto.capacity}
					</div>
					<div class="button-group">
						<button type="submit" class="btn btn-outline btn-large">정모생성</button>
						<button class="btn btn-outline btn-large" type="button"
							onclick="history.back()">취소</button>
					</div>
					<input type="hidden" id="contextPath"
						value="${pageContext.request.contextPath}"> 
						<input type="hidden" id="mode" value="${mode}">
				</form>
			</div>
		</main>
	</div>
	<!-- 아래는 footer 필요시 사용 -->
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>
	<script>
		const mode = "${mode}";
	</script>
</body>
</html>
