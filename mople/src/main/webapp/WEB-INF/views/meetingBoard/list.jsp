<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모임 소식</title>
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

		<h3 class="board-title">모임 소식</h3>

		<div class="category-tabs">
			<a href="?meetingIdx=${meetingIdx}"
				class="${empty param.filter ? 'tab active' : 'tab'}">전체</a> <a
				href="?meetingIdx=${meetingIdx}&filter=공지"
				class="${param.filter == '공지' ? 'tab active' : 'tab'}">공지</a> <a
				href="?meetingIdx=${meetingIdx}&filter=후기"
				class="${param.filter == '후기' ? 'tab active' : 'tab'}">후기</a> <a
				href="?meetingIdx=${meetingIdx}&filter=소통"
				class="${param.filter == '소통' ? 'tab active' : 'tab'}">소통</a> <a
				href="?meetingIdx=${meetingIdx}&filter=건의"
				class="${param.filter == '건의' ? 'tab active' : 'tab'}">건의</a> <a
				href="?meetingIdx=${meetingIdx}&filter=기타"
				class="${param.filter == '기타' ? 'tab active' : 'tab'}">기타</a>
		</div>

		<c:choose>
			<c:when test="${not empty list}">
				<div class="board-list-wrapper">
					<c:forEach var="dto" items="${list}">
						<div class="board-item"
							onclick="location.href='${pageContext.request.contextPath}/meetingBoard/view?num=${dto.num}&meetingIdx=${meetingIdx}'">

							<!-- 썸네일이 있을 경우에만 이미지 출력 -->
							<c:if test="${not empty dto.thumbnail}">
								<div class="thumbnail">
									<img
										src="${pageContext.request.contextPath}/uploads/photo/${dto.thumbnail}"
										alt="썸네일" width="100">
								</div>
							</c:if>

							<!-- 제목/내용/작성자 등 텍스트 정보 -->
							<div class="board-content">
								<div class="board-item-title">${dto.subject}</div>
								<c:choose>
									<c:when test="${empty dto.content}">
										<div class="board-item-text">내용 없음</div>
									</c:when>
									<c:when test="${fn:length(dto.content) > 100}">
										<div class="board-item-text">${fn:substring(dto.content, 0, 100)}...</div>
									</c:when>
									<c:otherwise>
										<div class="board-item-text">${dto.content}</div>
									</c:otherwise>
								</c:choose>

								<div class="board-item-meta">${dto.userNickName}·
									${dto.reg_date}</div>
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
				onclick="location.href='${pageContext.request.contextPath}/meetingBoard/write?meetingIdx=${meetingIdx}'">
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
