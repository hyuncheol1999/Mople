<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

	<div class="reply-list">
		<c:if test="${empty listReply}">
			<p class="text-muted">등록된 댓글이 없습니다.</p>
		</c:if>

		<c:forEach var="reply" items="${listReply}">
			<div class="reply-item">
				<div class="reply-header">
					<strong class="nickname">${reply.userNickName}</strong> <span
						class="meta">${reply.reg_date}</span>
				</div>
				<div class="reply-content">${reply.content}</div>
			</div>
		</c:forEach>
	</div>

	<!-- 댓글 페이징 -->
	<div class="reply-pagination">${paging}</div>

</body>
</html>