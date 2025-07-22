<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${dto.subject}-모임소식</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/meetingBoard.css"
	type="text/css">
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
				<div class="meta">${dto.reg_date}</div>
			</div>
		</div>

		<div class="post-content">
			<div class="post-text">${dto.content}</div>

			<c:if test="${not empty imageList}">
				<div class="post-images">
					<c:forEach var="img" items="${imageList}">
						<img src="${pageContext.request.contextPath}/uploads/photo/${img}" alt="이미지" />
					</c:forEach>
				</div>
			</c:if>
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

			<!-- 댓글 리스트 출력 위치 -->
			<div id="listReply"></div>

			<!-- 댓글 입력 폼 -->
			<form name="replyForm" method="post">
				<div class="comment-input-wrapper">
					<textarea class="form-control reply-textarea" name="content"
						placeholder="댓글을 입력하세요."></textarea>
					<button type="button" class="btn btnSendReply">등록</button>
				</div>
			</form>
		</div>


		<!-- 이전 글 -->
		<div class="post-nav-item">
			<strong>이전</strong>&nbsp;
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
		<div class="post-nav-item">
			<strong>다음</strong>&nbsp;
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
		let params = { num: num, pageNo: page };
		let selector = '#listReply';

		const fn = function (data) {
			$(selector).html(data);

			$('.reply-item').each(function () {
				const replyNum = $(this).find('.btnReplyAnswerSubmit').data('replynum');
				if (replyNum) {
					// AJAX로 답댓글 가져온 후 그 안에서 d-none 제거
					listReplyAnswerAndShow(replyNum);
				}
			});
		};

		sendAjaxRequest(url, 'get', params, 'text', fn);
	}

	function listReplyAnswerAndShow(parentNum) {
		let url = "${pageContext.request.contextPath}/meetingBoard/listReplyAnswer";
		let params = { parentNum: parentNum };

		const fn = function (data) {
			const $target = $('#listReplyAnswer' + parentNum);
			$target.html(data);
			$target.removeClass('d-none'); // AJAX 응답 받고 나서 d-none 제거
		};

		sendAjaxRequest(url, 'get', params, 'text', fn);
	}

	
	// 댓글 등록
	$(document).on('click', '.btnSendReply', function() {
			const $form = $(this).closest('form[name="replyForm"]');
			let num = '${dto.num}';
			let content = $form.find('textarea').val().trim();
			
			if(! content) {
				alert('댓글 내용을 입력하세요.');
				$form.find('textarea').focus();
			return;
		}
		
		let params = {num:num, content:content, parentNum:0};
		let url = '${pageContext.request.contextPath}/meetingBoard/insertReply';
		
		const fn = function (data) {
			if (data.state === 'loginFail') {
				alert('로그인 후 댓글 작성이 가능합니다.');
				login();
				return;
			}

			if (data.state === 'true') {
				$form.find('textarea').val('');
				listPage(1);
			} else {
				alert('댓글 등록에 실패했습니다.');
			}
		};
		
		sendAjaxRequest(url, 'post', params, 'json', fn);
	});
	
	// 댓글 삭제 메뉴 토글
	$(document).on('click', '.reply-dropdown', function (e) {
	  e.stopPropagation();

	  const $menu = $(this).siblings('.reply-menu');
	  const isHidden = $menu.hasClass('d-none');

	  $('.reply-menu').addClass('d-none');

	  if (isHidden) {
	    $menu.removeClass('d-none');
	  }
	});

	$(document).on('click', '.reply-menu', function (e) {
	  e.stopPropagation();
	});

	$(document).on('click', function () {
	  $('.reply-menu').addClass('d-none');
	});

	// 댓글 삭제
		$(document).on('click', '.deleteReply', function() {
			if(! confirm('댓글을 삭제하시겠습니까?')) {
				return;
			}
			
			const replyNum = $(this).data('replynum');
			const page = $(this).data('pageno');
			
			const url = '${pageContext.request.contextPath}/meetingBoard/deleteReply';
			const params = {replyNum:replyNum};
			
			sendAjaxRequest(url, 'post', params, 'json', function() {
				listPage(page);
		});
	});
	
	// 답댓글 입력 폼
	$(document).on('click', '.btnReplyAnswerToggle', function (e) {
		e.stopPropagation(); 
		
		$('.reply-form').addClass('d-none');
		
		const replyNum = $(this).data('replynum');
		const $form = $(this).closest('.reply-item').find('.reply-form');
		$form.removeClass('d-none');
		
		$('.reply-menu').addClass('d-none');
	});
	
	// 대댓글 리스트
	function listReplyAnswer(parentNum) {
		let url = "${pageContext.request.contextPath}/meetingBoard/listReplyAnswer";
		let params = {parentNum: parentNum};
		
		const fn = function(data) {
			$('#listReplyAnswer' + parentNum).html(data);
		};
		sendAjaxRequest(url, 'get', params, 'text', fn);
	}
	
	// 대댓글 카운트
	function countReplyAnswer(parentNum) {
		let url = '${pageContext.request.contextPath}/meetingBoard/countReplyAnswer';
		let params = {parentNum: parentNum};
		
		const fn = function(data) {
			$('#answerCount' + parentNum).html(data.count);
		};
		sendAjaxRequest(url, 'post', params, 'json', fn);
	}
	
	// 대댓글 등록
	$('#listReply').on('click', '.btnReplyAnswerSubmit', function() {
			const $form = $(this).closest('.reply-form');
			let content = $form.find('textarea').val().trim();
			let replyNum = $(this).data('replynum');
			let num = '${dto.num}';
			
			if(!content) {
				$form.find('textarea').focus();
				return;
			}
			
			let url = '${pageContext.request.contextPath}/meetingBoard/insertReply';
			let params = {num:num, content:content, parentNum:replyNum};
			
			const fn = function(data){
				if(data.state === 'true') {
					$form.find('textarea').val('');
					$('#listReplyAnswer' + replyNum).removeClass('d-none');
					listReplyAnswer(replyNum);
					countReplyAnswer(replyNum);
				} else {
					alert('답글을 등록하지 못했습니다.');
				}
			};
			sendAjaxRequest(url, 'post', params, 'json', fn);
		});
	
	// 대댓글 삭제
	$(function() {
		$('#listReply').on('click', '.deleteReplyAnswer', function() {
			if(! confirm('답글을 삭제하시겠습니까?')) {
				return;
			}
			
			let replyNum = $(this).data('replyNum');
			let parentNum = $(this).data('parentNum');
			
			let url = '${pageContext.request.contextPath}/meetingBoard/deleteReply';
			let params = {replyNum:replyNum};
			
			const fn = function() {
				listReplyAnswer(parentNum);
				countReplyAnswer(parentNum);
			};
			
			sendAjaxRequest(url, 'post', params, 'json', fn);
		});
	});
</script>


</body>
</html>
