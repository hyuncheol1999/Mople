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
	href="${pageContext.request.contextPath}/dist/css/meetingBoard.css"
	type="text/css">
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp" />

	<main class="board-wrapper mt-5">

		<h3 class="board-title">자유 게시판</h3>

		<div class="category-tabs">
			<a href="${pageContext.request.contextPath}/bbs/list" class="tab active">전체</a>
		</div>

		<c:choose>
			<c:when test="${not empty list}">
				<div class="board-list-wrapper">
					<c:forEach var="dto" items="${list}" varStatus="status">
						<div class="board-item"
							onclick="location.href='${pageContext.request.contextPath}/bbs/article?num=${dto.num}'">
							<div class="board-content">
								<div class="board-item-title">${dto.subject}</div>
								<div class="board-item-text">${dto.userNickName} · ${dto.reg_date} · 조회 ${dto.hitCount}</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<div class="text-empty">등록된 게시물이 없습니다.</div>
			</c:otherwise>
		</c:choose>

		<div class="write-btn">
			<button class="btn btn-outline-dark"
				onclick="location.href='${pageContext.request.contextPath}/bbs/write'">
				글쓰기</button>
		</div>

		<c:if test="${not empty paging}">
			<div class="page-navigation">
				<div class="paginate">
					<c:out value="${paging}" escapeXml="false" />
				</div>
			</div>
		</c:if>
	</main>

	<footer class="footer">
		<div class="footer-wrapper">ⓒ 2025 Mople. All rights reserved.</div>
	</footer>
</body>

	
</html>