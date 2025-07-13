<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${dto.subject}-모임소식</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/meetingBoard.css">
</head>
<body>

	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp" />

	<main class="view-container">

		<!-- 제목 -->
		<h1 class="view-title">${dto.subject}</h1>

		<!-- 작성자 정보 -->
		<div class="writer-info">
			<div class="profile-circle"></div>
			<div>
				<div class="nickname">${dto.userNickName}</div>
				<div class="meta">${dto.reg_date}·조회수10</div>
			</div>
		</div>

		<!-- 본문 -->
		<div class="view-content">
			<c:out value="${dto.content}" escapeXml="false" />
		</div>

		<!-- 해시태그 / 카테고리 -->
		<div class="hashtags">
			<span>#${dto.filter}</span>
		</div>

		<!-- 좋아요 버튼 -->
		<button type="button" class="btnSendBoardLike like-icon-btn"
			data-liked="${liked}" data-num="${dto.num}">
			<i class="bi ${liked ? 'bi-heart-fill heart-liked' : 'bi-heart'}"></i> <span
				id="boardLikeCount">${likeCount}</span>
		</button>


		<!-- 수정/삭제 -->
		<c:if test="${dto.userNickName == sessionScope.member.userNickName}">
			<div class="view-footer">
				<a
					href="${pageContext.request.contextPath}/meetingBoard/update?num=${dto.num}&meetingIdx=${meetingIdx}&page=${page}"
					class="btn btn-light">수정</a> <a
					href="${pageContext.request.contextPath}/meetingBoard/delete?num=${dto.num}&meetingIdx=${meetingIdx}&page=${page}"
					class="btn btn-light" onclick="deleteOk(); return false;">삭제</a>
			</div>
		</c:if>

		<!-- 댓글 영역 -->
		<div class="comment-section">
			<p>아직 댓글이 없습니다.</p>
			<button class="btn comment-write">댓글 쓰기</button>
		</div>

		<!-- 예: meetingBoard/view.jsp -->
		<div class="view-footer">
			<a
				href="${pageContext.request.contextPath}/meetingBoard/list?meetingIdx=${meetingIdx}"
				class="btn btn-outline"> ← 글 리스트로 </a>
		</div>


	</main>

	<c:if test="${sessionScope.member.memberIdx==dto.memberIdx}">
		<script type="text/javascript">
			function deleteOk() {
				if (confirm('게시물을 삭제하시겠습니까?')) {
					let params = 'num=${dto.num}&${query}';
					let url = '${pageContext.request.contextPath}/meetingBoard/delete?'
							+ params;
					location.href = url;
				}
			}
		</script>
	</c:if>

	<script type="text/javascript">
	function login() {
		location.href = '${pageContext.request.contextPath}/member/login';
	}

	function sendAjaxRequest(url, method, params, responseType, fn, file = false) {
		const settings = {
			type: method,
			data: params,
			dataType: responseType,
			success: function (data) {
				fn(data);
			},
			beforeSend: function (xhr) {
				xhr.setRequestHeader('AJAX', true);
			},
			error: function (xhr) {
				if (xhr.status === 403) {
					login();
				} else if (xhr.status === 406) {
					alert('요청 처리에 실패했습니다.');
				} else {
					console.log(xhr.responseText);
				}
			}
		};

		if (file) {
			settings.processData = false;
			settings.contentType = false;
		}

		$.ajax(url, settings);
	}

	// 좋아요 버튼 클릭 이벤트
	$(function () {
		$('.btnSendBoardLike').click(function () {
			const $btn = $(this);
			const $i = $btn.find('i');
			let userLiked = $i.hasClass('bi-heart-fill');
			let msg = userLiked ? '좋아요를 취소하시겠습니까?' : '이 게시글에 좋아요를 누르시겠습니까?';

			if (!confirm(msg)) return;

			let url = '${pageContext.request.contextPath}/meetingBoardLike';
			let num = $btn.data('num');
			let params = { num: num, userLiked: userLiked };

			const fn = function (data) {
				let state = data.state;

				if (state === 'success') {
					if (userLiked) {
						$i.removeClass('bi-heart-fill heart-liked').addClass('bi-heart');
					} else {
						$i.removeClass('bi-heart').addClass('bi-heart-fill heart-liked');
					}
					$('#boardLikeCount').text(data.meetingBoardLikeCount);
					$btn.attr('data-liked', !userLiked);
				} else if (state === 'liked') {
					alert('이미 공감한 게시글입니다.');
				} else {
					alert('공감 처리에 실패했습니다.');
				}
			};

			sendAjaxRequest(url, 'post', params, 'json', fn);
		});
	});
</script>


</body>
</html>
