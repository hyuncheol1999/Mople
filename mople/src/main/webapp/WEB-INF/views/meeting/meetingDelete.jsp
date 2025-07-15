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
				<h2>모임 해체</h2>
				<form name="deleteForm" method="post" enctype="multipart/form-data">
					<div class="filter-section">
						<span class="title">모임장을 위임해주세요.</span>
						<c:forEach var="sDto" items="${list}"
							varStatus="status">
							<label class="filter-item"> <input name="memberList"
								type="radio" value="${sDto.memberName}"> <span>${sDto.memberName}</span>
							</label>
						</c:forEach>
					</div>
					<div class="button-group">
						<button type="button" class="btn btn-outline btn-large" onclick="deleteMeeting()">모임
							삭제</button>
						<button class="btn btn-outline btn-large" type="button"
							onclick="history.back()">취소</button>
					</div>
					<input type="hidden" id="contextPath"
						value="${pageContext.request.contextPath}">
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
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/meetingDelete.js"></script>
</body>
</html>
