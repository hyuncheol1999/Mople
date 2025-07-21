<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="reply-list-container">
	<c:if test="${empty listReply}">
		<div class="no-reply">등록된 댓글이 없습니다.</div>
	</c:if>

	<c:forEach var="reply" items="${listReply}">
		<div class="reply-box">
			<div class="reply-header">
				<strong>${reply.userNickName}</strong> <span>${reply.reg_date}</span>
				<!-- 문자열 그대로 출력 -->
			</div>
			<div class="reply-content">${reply.content}</div>
			<div class="reply-actions">
				<c:if test="${reply.memberIdx eq memberIdx or isLeader}">
					<button class="btn btn-outline-danger btn-sm btn-delete"
						data-id="${reply.replyNum}">삭제</button>
				</c:if>
				<c:if test="${isLeader and reply.memberIdx ne memberIdx}">
					<button class="btn btn-outline-primary btn-sm btn-approve"
						data-id="${reply.memberIdx}" data-meetingid="${bungaeMeetingIdx}">승인</button>
				</c:if>
			</div>
		</div>
	</c:forEach>


</div>

<style>
.reply-list-container {
	padding: 20px;
	border-top: 1px solid #ddd;
	background-color: #f9f9f9;
	border-radius: 8px;
}

.reply-box {
	border-bottom: 1px solid #eee;
	padding: 12px 0;
}

.reply-header {
	display: flex;
	justify-content: space-between;
	font-size: 0.95em;
	color: #555;
	margin-bottom: 5px;
}

.reply-content {
	font-size: 1em;
	color: #222;
	margin-bottom: 8px;
}

.reply-actions {
	display: flex;
	gap: 10px;
}

.btn-delete, .btn-approve {
	padding: 4px 12px;
	font-size: 0.85em;
	cursor: pointer;
}

.no-reply {
	text-align: center;
	color: #888;
	padding: 20px 0;
}

.paging {
	text-align: center;
	margin-top: 15px;
}
</style>
