<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${mode == 'update' ? '글 수정' : '글쓰기'}-모임 소식</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/meetingBoard.css">
</head>
<body>

	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp" />

	<main class="board-wrapper">

		<!-- 타이틀 -->
		<h1 class="board-title">${mode == 'update' ? '글 수정' : '새 글 작성'}</h1>

		<!-- 폼 -->
		<form method="post"
			action="${pageContext.request.contextPath}/meetingBoard/${mode == 'update' ? 'update' : 'write'}">

			<input type="hidden" name="meetingIdx" value="${meetingIdx}">
			<c:if test="${mode == 'update'}">
				<input type="hidden" name="num" value="${dto.num}">
				<input type="hidden" name="page" value="${page}">
			</c:if>

			<!-- 카테고리 -->
			<div class="form-group">
				<label for="filter">카테고리</label> <select name="filter" id="filter"
					class="form-control" required>
					<option value="">-- 선택하세요 --</option>
					<option value="공지" ${dto.filter == '공지' ? 'selected' : ''}>공지</option>
					<option value="후기" ${dto.filter == '후기' ? 'selected' : ''}>후기</option>
					<option value="소통" ${dto.filter == '소통' ? 'selected' : ''}>소통</option>
					<option value="건의" ${dto.filter == '건의' ? 'selected' : ''}>건의</option>
					<option value="기타" ${dto.filter == '기타' ? 'selected' : ''}>기타</option>
				</select>
			</div>

			<!-- 제목 -->
			<div class="form-group">
				<label for="subject">제목</label> <input type="text" id="subject"
					name="subject" required class="form-control"
					placeholder="제목을 입력하세요" value="${dto.subject}">
			</div>

			<!-- 내용 -->
			<div class="form-group">
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="10" required
					class="form-control" placeholder="내용을 입력하세요">${dto.content}</textarea>
			</div>

			<!-- 버튼 -->
			<div class="write-btn">
				<button type="submit" class="btn btn-primary">${mode == 'update' ? '수정 완료' : '등록 완료'}
				</button>
				<button type="reset" class="btn btn-outline">다시 입력</button>
				<button type="button" class="btn btn-outline"
					onclick="location.href='${pageContext.request.contextPath}/meetingBoard/list?meetingIdx=${meetingIdx}&page=${page}'">
					${mode == 'update' ? '수정 취소' : '등록 취소'}</button>
			</div>

		</form>

	</main>

	<!-- 푸터 -->
	<footer>
		<div class="footer-wrapper">ⓒ 2025 Mople. All rights reserved.</div>
	</footer>

</body>
</html>
