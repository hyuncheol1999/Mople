<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:forEach var="vo" items="${listReplyAnswer}">	
	<div class="border-bottom m-1">
		<div class="row p-1">
			<div class="col-auto">
				<div class="row reply-writer">
					<div class="col-1"><i class="bi bi-person-circle text-muted icon"></i></div>
					<div class="col ms-2 align-self-center">
						<div class="name">${vo.userNickName}</div>
						<div class="date">${vo.reg_date }</div>
					</div>
				</div>
			</div>
			<div class="col align-self-center text-end">
				<span class="reply-dropdown"><i class="bi bi-three-dots-vertical"></i></span>
				<div class="reply-menu d-none">
					<c:choose>
						<c:when test="${sessionScope.member.memberIdx == vo.memberIdx}">
							<div class="deleteReplyAnswer reply-menu-item" data-replyNum="${vo.replyNum}" data-parentNum="${vo.parentNum}">삭제</div>
							<div class="hideReplyAnswer reply-menu-item" data-replyNum="${vo.replyNum}" data-showReply="${vo.showReply}">${vo.showReply ==1?"비공개":"공개"}</div>						
						</c:when>
						<c:when test="${sessionScope.member.role < 0}">
							<div class="deleteReplyAnswer reply-menu-item" data-replyNum="${vo.replyNum}" data-parentNum="${vo.parentNum}">삭제</div>
							<div class="blocknotifyReplyAnswer reply-menu-item">숨김</div>
						</c:when>
					</c:choose>
				</div>
			</div>
		</div>

		<div class="p-2 ${vo.showReply ==0?'text-primary text-opacity-50':''}">
			${vo.content}
		</div>
	</div>
</c:forEach>
