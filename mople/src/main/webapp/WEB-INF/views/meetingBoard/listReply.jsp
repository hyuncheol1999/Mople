<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="comment-section">
	<h3 class="board-title"></h3>

	<c:forEach var="reply" items="${listReply}">
		<div class="reply-item" style="position: relative;">
			<div class="reply-header">
				<div class="writer-info">
					<div class="nickname">${reply.userNickName}</div>
					<div class="meta">${reply.reg_date}</div>
				</div>

				<c:if test="${sessionScope.member.memberIdx == reply.memberIdx}">
					<div class="reply-actions">
						<span class="reply-dropdown">⋮</span>
						<div class="reply-menu d-none">
							<div class="deleteReply reply-menu-item"
								data-replyNum="${reply.replyNum}" data-pageNo="${pageNo}">
								삭제</div>
						</div>
					</div>
				</c:if>
			</div>


			<div class="reply-content">${reply.content}</div>

			<!-- 답글 보기 버튼 -->
			<c:if test="${reply.answerCount > 0}">
				<button type="button" class="btnReplyAnswerLayout"
					data-replyNum="${reply.replyNum}">답글 ${reply.answerCount}개
					보기</button>
			</c:if>

			<!-- 대댓글 출력 영역 (Ajax로 로딩됨) -->
			<div id="listReplyAnswer${reply.replyNum}"
				class="reply-answer d-none"></div>

			<!-- 대댓글 작성 폼 -->
			<div class="reply-form">
				<textarea class="form-control reply-answer-text" rows="2"
					placeholder="답글을 입력하세요."></textarea>
				<button type="button" class="comment-write btnReplyAnswerSubmit"
					data-replyNum="${reply.replyNum}">답글 등록</button>
			</div>
		</div>
	</c:forEach>
</div>