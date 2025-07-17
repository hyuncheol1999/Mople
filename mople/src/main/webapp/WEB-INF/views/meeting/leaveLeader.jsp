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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />

		<main class="main">
			<div class="meetingContainer">
		    <h2>모임 탈퇴</h2>
			<form name="leaveForm" method="post">
				<div class="form-group">
					<label for="memberCategory">모임장을 위임할 모임원을 선택해주세요.</label>
					<div class="category-select-wrapper">
						<input name="memberIdx" type="text" id="memberList"
							class="category-search-input"
							placeholder="모임장을 위임할 모임원을 검색 또는 선택해주세요."> <input
							type="hidden" id="memberCategoryNo" name="memberCategoryNo">
						<div class="category-list" id="memberCategoryList">
							<ul>
								<c:forEach var="sDto" items="${list}"
									varStatus="status">
									<c:if test="${sDto.memberIdx!=sessionScope.member.memberIdx}">
										<li data-category-no="${sDto.memberIdx}">${sDto.memberName}</li>
										<input name="meetingIdx" type="hidden" value="${sDto.meetingIdx}">
									</c:if>
								</c:forEach>
							</ul>
						</div>
					</div>
					<small class="error-message" id="memberCategoryError">
						위임할 모임장을 다시 선택/입력해주세요.</small>
				</div>
				<div class="button-group">
	                <button type="submit" class="btn btn-outline btn-large">모임 탈퇴</button>
	                <button class="btn btn-outline btn-large" type="button" onclick="history.back();">취소</button>
		        	<input type="hidden" name="contextPath" value="${pageContext.request.contextPath}">
		        </div>
			</form>
			</div>
		</main>
	</div>
	<!-- 아래는 footer 필요시 사용 -->
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/leaveLeader.js"></script>
</body>
</html>
