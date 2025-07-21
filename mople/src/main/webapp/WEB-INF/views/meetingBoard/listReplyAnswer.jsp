<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:forEach var="dto" items="${listReplyAnswer}">
	<div class="reply-answer-item">
		<div class="reply-header">
			<span class="nickname">⤷ ${dto.userNickName}</span>

			<c:if
				test="${not empty sessionScope.member and sessionScope.member.memberIdx == dto.memberIdx}">
				<div class="reply-actions">
					<span class="reply-dropdown">⋮</span>
					<div class="reply-menu d-none">
						<div class="reply-menu-item deleteReplyAnswer"
							data-replyNum="${dto.replyNum}" data-parentNum="${dto.parentNum}">
							삭제</div>
					</div>
				</div>
			</c:if>
		</div>

		<div class="reply-content">
			${dto.content}
			<div class="meta">${dto.reg_date}</div>
		</div>
	</div>
</c:forEach>

