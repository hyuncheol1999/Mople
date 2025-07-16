<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<c:forEach var="dto" items="${listReplyAnswer}">
		<div class="reply-answer-item">
			<div class="reply-header">
				<span class="nickname">${dto.userNickName}</span> <span class="meta">${dto.reg_date}</span>

				<c:if test="${sessionScope.member.memberIdx == dto.memberIdx}">
					<span class="reply-dropdown" style="cursor: pointer;">⋮</span>
					<div class="reply-menu d-none">
						<div class="deleteReplyAnswer reply-menu-item"
							data-replyNum="${dto.replyNum}" data-parentNum="${dto.parentNum}">삭제</div>
					</div>
				</c:if>
			</div>

			<div class="reply-content">${dto.content}</div>
		</div>
	</c:forEach>
</body>
</html>