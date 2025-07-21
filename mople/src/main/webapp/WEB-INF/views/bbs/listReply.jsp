<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:forEach var="reply" items="${listReply}">
	<div class="reply-item" style="position: relative;">
		<div class="reply-header">
			<div class="writer-info">
				<div class="nickname">${reply.userNickName}</div>
			</div>

			<!-- 토글 버튼과 메뉴는 항상 출력 -->
			<div class="reply-actions">
				<span class="reply-dropdown">⋮</span>
				<div class="reply-menu d-none">
					<!-- 답댓글: 모든 사용자에게 노출 -->
					<button class="reply-menu-item btnReplyAnswerToggle"
						data-replynum="${reply.replyNum}">답댓글</button>

					<!-- 삭제: 작성자만 노출 -->
					<c:if test="${sessionScope.member.memberIdx == reply.memberIdx}">
						<button class="reply-menu-item deleteReply"
							data-replyNum="${reply.replyNum}" data-pageNo="${pageNo}">
							삭제</button>
					</c:if>
				</div>
			</div>
		</div>

		<div class="reply-content">
			${reply.content}
			<div class="meta">${reply.reg_date}</div>
		</div>

		<!-- 답댓글 표시 영역 -->
		<div id="listReplyAnswer${reply.replyNum}" class="reply-answer"></div>

		<!-- 답댓글 입력폼 (기본 숨김) -->
		<div class="reply-form d-none">
			<form method="post">
				<div class="comment-input-wrapper">
					<textarea class="form-control reply-textarea reply-answer-text"
						placeholder="답글을 입력하세요."></textarea>
					<button type="button" class="btn btnReplyAnswerSubmit"
						data-replyNum="${reply.replyNum}">등록</button>
				</div>
			</form>
		</div>
	</div>
</c:forEach>

<!-- 댓글 페이징 -->
<c:if test="${not empty paging}">
	<div class="reply-pagination">
		<div class="paginate">${paging}</div>
	</div>
</c:if>