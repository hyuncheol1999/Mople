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

		<!-- 돌아가기 -->
		<div class="view-footer back-btn-left">
			<a
				href="${pageContext.request.contextPath}/meetingBoard/list?meetingIdx=${meetingIdx}"
				class="btn btn-outline"> ← </a>
		</div>


		<!-- 제목 -->
		<h1 class="view-title">${dto.subject}</h1>

		<!-- 작성자 정보 -->
		<div class="writer-info">
			<div class="profile-circle"></div>
			<div>
				<div class="nickname">${dto.userNickName}</div>
				<div class="meta">${dto.reg_date}·조회10</div>
			</div>
		</div>

		<!-- 본문 -->
		<div class="view-content">
			<c:out value="${dto.content}" escapeXml="false" />
		</div>

		<!-- 카테고리 -->
		<div class="hashtags">
			<span>#${dto.filter}</span>
		</div>

		<!-- 좋아요 버튼 -->
		<button type="button" class="btnSendBoardLike like-icon-btn"
			data-liked="${liked}" data-num="${dto.num}">
			<i class="bi ${liked ? 'bi-heart-fill heart-liked' : 'bi-heart'}"></i>
			<span id="boardLikeCount">${likeCount}</span>
		</button>

		<!-- 수정/삭제 버튼 -->
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
			<form name="replyForm" method="post">
				<table class="table table-borderless reply-form">
					<tr>
						<td><textarea class="form-control" name="content"
								placeholder="댓글을 입력하세요."></textarea></td>
					</tr>
					<tr>
						<td align="right">
							<button type="button" class="btn btn-light btnSendReply">댓글
								등록</button>
						</td>
					</tr>
				</table>
			</form>

			<!-- 댓글 리스트 출력 위치 -->
			<div id="listReply"></div>
		</div>


		<!-- 이전 글 -->
		<div class="nav-item">
			이전&nbsp;
			<c:choose>
				<c:when test="${not empty prevDto}">
					<a
						href="${pageContext.request.contextPath}/meetingBoard/view?${query}&num=${prevDto.num}">
						${prevDto.subject} </a>
				</c:when>
				<c:otherwise>
					<span>이전 글 없음</span>
				</c:otherwise>
			</c:choose>
		</div>

		<!-- 다음 글 -->
		<div class="nav-item">
			다음&nbsp;
			<c:choose>
				<c:when test="${not empty nextDto}">
					<a
						href="${pageContext.request.contextPath}/meetingBoard/view?${query}&num=${nextDto.num}">
						${nextDto.subject} </a>
				</c:when>
				<c:otherwise>
					<span>다음 글 없음</span>
				</c:otherwise>
			</c:choose>
		</div>

	</main>

	<!-- 삭제 확인 스크립트 -->
	<c:if test="${sessionScope.member.memberIdx==dto.memberIdx}">
		<script type="text/javascript">
			function deleteOk() {
				if (confirm('게시글을 삭제하시겠습니까?')) {
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

	// 좋아요 처리
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
					alert('이미 좋아요를 누른 게시글입니다.');
				} else {
					alert('좋아요 처리 중 오류가 발생했습니다.');
				}
			};

			sendAjaxRequest(url, 'post', params, 'json', fn);
		});
	});
	
	// 댓글 불러오기
	$(function() {
		listPage(1);
	});
	
	function listPage(page) {
		let url = '${pageContext.request.contextPath}/meetingBoard/listReply';
		let num = '${dto.num}';
		let params = {num:num, pageNo:page};
		let selector = '#listReply';
		
		const fn = function(data) {
			$(selector).html(data);
		};
		
		sendAjaxRequest(url, 'get', params, 'text', fn);
	}
	
	// 댓글 등록
	$(function() {
		$('.btnSendReply').click(function() {
			let num = '${dto.num}';
			const $tb = $(this).closest('table');
			
			let content = $tb.find('textarea').val().trim();
			if(! content) {
				alert('댓글 내용을 입력하세요.');
			return;
		}
		
		let params = {num:num, content:content, parentNum:0};
		
		let url = '${pageContext.request.contextPath}/meetingBoard/insertReply';
		
		const fn = function(data) {
			$tb.find('textarea').val('');
			
			let state = data.state;
			if(state === 'loginFail') {
				alert('로그인 후 댓글 작성이 가능합니다.');
				login();
				return;
			} 
			
			if (state === 'true') {
				listPage(1);
			} else {
				alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
			}
		};
		sendAjaxRequest(url, 'post', params, 'json', fn);
		});
	});
</script>


</body>
</html>
