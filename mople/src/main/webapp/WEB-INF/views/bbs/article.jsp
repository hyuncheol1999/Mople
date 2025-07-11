<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/paginate.css" type="text/css">
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />

		<main class="main">
			<div class="container">
				<div class="body-container row justify-content-center">
					<div class="col-md-10 my-3 p-3">
						<div class="body-title">
							<h3>
								<i class="bi bi-app"></i> 자유 게시판
							</h3>
						</div>

						<div class="body-main">

							<table class="table board-article">
								<thead>
									<tr>
										<td colspan="2" align="center">${dto.subject}</td>
									</tr>
								</thead>

								<tbody>
									<tr>
										<td width="50%">작성자 : ${dto.userNickName}</td>
										<td align="right">${dto.reg_date} | 조회 ${dto.hitCount}</td>
									</tr>

									<tr>
										<td colspan="2" valign="top" height="200"
											style="border-bottom: none;">${dto.content}</td>
									</tr>

									<tr>
										<td colspan="2" class="text-center p-3">
											<button type="button"
												class="btn btn-outline-primary btnSendBoardLike" title="좋아요">
												<i
													class="bi ${isUserLiked ? 'bi-hand-thumbs-up-fill':'bi-hand-thumbs-up'}"></i>&nbsp;&nbsp;<span
													id="boardLikeCount">${dto.boardLikeCount}</span>
											</button>
										</td>
									</tr>

									<tr>
										<td colspan="2">이전글 : <c:if test="${not empty prevDto}">
												<a
													href="${pageContext.request.contextPath}/bbs/article?${query}&num=${prevDto.num}">${prevDto.subject}</a>
											</c:if>
										</td>
									</tr>
									<tr>
										<td colspan="2">다음글 : <c:if test="${not empty nextDto}">
												<a
													href="${pageContext.request.contextPath}/bbs/article?${query}&num=${nextDto.num}">${nextDto.subject}</a>
											</c:if>
										</td>
									</tr>
								</tbody>
							</table>

							<table class="table table-borderless mb-2">
								<tr>
									<td width="50%"><c:choose>
											<c:when test="${sessionScope.member.memberIdx==dto.memberIdx}">
												<button type="button" class="btn btn-light"
													onclick="location.href='${pageContext.request.contextPath}/bbs/update?num=${dto.num}&page=${page}';">수정</button>
											</c:when>
											<c:otherwise>
												<button type="button" class="btn btn-light" disabled>수정</button>
											</c:otherwise>
										</c:choose> <c:choose>
											<c:when
												test="${sessionScope.member.memberIdx==dto.memberIdx || sessionScope.member.role < 1}">
												<button type="button" class="btn btn-light"
													onclick="deleteOk();">삭제</button>
											</c:when>
											<c:otherwise>
												<button type="button" class="btn btn-light" disabled>삭제</button>
											</c:otherwise>
										</c:choose></td>
									<td class="text-end">
										<button type="button" class="btn btn-light"
											onclick="location.href='${pageContext.request.contextPath}/bbs/list?${query}';">리스트</button>
									</td>
								</tr>
							</table>

							<div class="reply">
								<form name="replyForm" method="post">
									<div class="form-header">
										<span class="bold">댓글</span><span> - 타인을 비방하거나 개인정보를
											유출하는 글의 게시를 삼가해 주세요.</span>
									</div>

									<table class="table table-borderless reply-form">
										<tr>
											<td><textarea class="form-control" name="content"></textarea>
											</td>
										</tr>
										<tr>
											<td align="right">
												<button type="button" class="btn btn-light btnSendReply">댓글
													등록</button>
											</td>
										</tr>
									</table>
								</form>

								<div id="listReply"></div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</main>
	</div>
<c:if test="${sessionScope.member.memberIdx==dto.memberIdx || sessionScope.member.role < 1}">
	<script type="text/javascript">
		function deleteOk() {
			if(confirm('게시글을 삭제 하시 겠습니까 ? ')) {
				let params = 'num=${dto.num}&${query}';
				let url = '${pageContext.request.contextPath}/bbs/delete?' + params;
				location.href = url;
			}
		}
	</script>
</c:if>
<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function sendAjaxRequest(url, method, params, responseType, fn, file=false){
	const settings = {
			type: method,
			data: params,
			dataType: responseType,
			success: function(data) {
				fn(data);
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader('AJAX',true); // 로그인필터에서 AJAX 요청인지 확인
			},
			complete : function() {
			},
			error: function(xhr){
				// 로그인 필터에서 로그인이 되어 있지 않으면 403 에러를 던짐
				if(xhr.status === 403) {
					login();
					return false ;
				}else if(xhr.status === 406){
					alert('요청 처리가 실패했습니다.');
					return false;
				}
				
				console.log(xhr.responseText);
			}
			
	};
	
	if(file){
		settings.processData = false;
		settings.contentType = false;
	}
	
	$.ajax(url,settings);
}

// 게시글 공감 여부
$(function(){
	$('.btnSendBoardLike').click(function(){
		const $i = $(this).find('i');
		let userLiked = $i.hasClass('bi-hand-thumbs-up-fill');
		let msg = userLiked ? '게시글 공감을 취소하시겠습니까?' : '게시글에 공감하십니까';
		
		if(! confirm(msg)){
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/bbs/insertBoardLike';
		let num = ${dto.num};
		let params = {num: num, userLiked: userLiked};
		
		const fn = function (data) {
			let state = data.state;
			if(state === 'true'){
				if(userLiked){
					$i.removeClass('bi-hand-thumbs-up-fill').addClass('bi-hand-thumbs-up');
				}else{
					$i.removeClass('bi-hand-thumbs-up').addClass('bi-hand-thumbs-up-fill');
				}
				
				let count = data.boardLikeCount;
				$('#boardLikeCount').text(count);
				
			}else if(state === 'liked'){
				alert('게시글 공감은 한번만 가능합니다');
			}else {
				alert('게시글 공감 처리가 실패했습니다.');
			}
		};
		
		sendAjaxRequest(url,'post',params,'json',fn);
		
	});
});

// 댓글 리스트
$(function(){
	listPage(1);
});

function listPage(page) {
	let url = '${pageContext.request.contextPath}/bbs/listReply';
	let num = '${dto.num}';
	let params = {num:num, pageNo:page};
	let selector = '#listReply';
	
	const fn = function(data){
		$(selector).html(data);
	};
	
	sendAjaxRequest(url,'get',params,'text',fn);
	
}

// 댓글 등록
$(function(){
	$('.btnSendReply').click(function(){
		let num = '${dto.num}';
		const $tb = $(this).closest('table');
		
		let content = $tb.find('textarea').val().trim();
		if(! content){
			$tb.find('textarea').focus();
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/bbs/insertReply';
		let params = {num: num, content:content, parentNum:0};
		
		const fn = function(data){
			$tb.find('textarea').val('');
			
			let state = data.state;
			if(state === 'true'){
				listPage(1);
			}else{
				alert('댓글을 등록하지 못했습니다');
			}
		};
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});

// 삭제 , 신고 메뉴
$(function(){
	$('.reply').on('click','.reply-dropdown',function(){
		const $menu = $(this).next('.reply-menu');
		let isHidden = $menu.hasClass('d-none');
		
		if(isHidden) {
			$('.reply-menu').not('.d-none').addClass('d-none');
			$menu.removeClass('d-none');
			
			let pos = $(this).offset();
			$menu.offset({letft:pos.left-70, top:pos.top+20});
		}else {
			$menu.addClass('d-none');
		}
	});
	
	$('body').on('click',function(evt){
		if($(evt.target.parentNode).hasClass('reply-dropdown')){ // 드롭다운을 클릭하면 드롭다운메뉴바에 d-none을주지않고 계속 볼수있게둠
			return false; 
		}
		
		$('.reply-menu').not('.d-none').addClass('d-none'); 
	});
});

// 댓글 삭제
$(function(){
	$('.reply').on('click', '.deleteReply',function(){
		if(! confirm('댓글을 삭제하시겠습니까')){
			return false
		}
		
		let replyNum = $(this).attr('data-replyNum');
		let page = $(this).attr('data-pageNo');
		
		let url = '${pageContext.request.contextPath}/bbs/deleteReply';
		let params = {replyNum: replyNum};
		
		const fn = function(data){
			listPage(page);
		};
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});


// 답글 버튼 (댓글별 답글 등록 폼 및 답글 리스트)
$(function(){
	$('.reply').on('click','.btnReplyAnswerLayout',function(){
		const $tr = $(this).closest('tr').next();
		
		let replyNum = $(this).attr('data-replyNum');
		
		if($tr.hasClass('d-none')){
			// 답글 리스트
			listReplyAnswer(replyNum);
			// 답글 개수
			countReplyAnswer(replyNum);
		}
		
		$tr.toggleClass('d-none');
	});
	
});

// 댓글별 답글 리스트
function listReplyAnswer(parentNum) {
	let url = '${pageContext.request.contextPath}/bbs/listReplyAnswer';
	let params = {parentNum: parentNum};
	let selector = $('#listReplyAnswer'+ parentNum);
	
	const fn = function(data){
		$(selector).html(data);
	};
	sendAjaxRequest(url,'get',params,'text',fn);
}

// 댓글별 답글 개수
function countReplyAnswer(parentNum) {
	let url = '${pageContext.request.contextPath}/bbs/countReplyAnswer';
	let params = {parentNum: parentNum};
	
	const fn = function(data) {
		let count = data.count;
		let selector = '#answerCount'+parentNum;
		$(selector).html(count);
	};
	sendAjaxRequest(url,'post',params,'json',fn);
}

// 댓글별 답글 등록
$(function(){
	$('.reply').on('click','.btnSendReplyAnswer',function(){
		let num = '${dto.num}';
		let replyNum = $(this).attr('data-replyNum');
		const $td = $(this).closest('td');
		
		let content = $td.find('textarea').val().trim();
		if(! content){
			$td.find('textarea').focus();
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/bbs/insertReply';
		let params = {num:num, content:content , parentNum:replyNum};
		
		const fn = function(data){
			$td.find('textarea').val('');
			let state = data.state;
			if(state === 'true'){
				listReplyAnswer(replyNum);
				countReplyAnswer(replyNum);
			}
		};
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});

// 답글 삭제
$(function(){
	$('.reply').on('click','.deleteReplyAnswer',function(){
		if(! confirm('게시물을 삭제하시겠습니까 ?')){
			return false;
		}
		
		let replyNum = $(this).attr('data-replyNum');
		let parentNum = $(this).attr('data-parentNum');
		
		let url = '${pageContext.request.contextPath}/bbs/deleteReply';
		let params = {replyNum:replyNum};
		
		const fn = function(data){
			listReplyAnswer(parentNum);
			countReplyAnswer(parentNum);
		};
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});

// 댓글 좋아요 / 싫어요
$(function(){
	$('.reply').on('click','.btnSendReplyLike',function(){
		const $btn = $(this);
		let replyNum = $btn.attr('data-replyNum');
		let replyLike = $btn.attr('data-replyLike');
		let isUserLiked = $btn.parent('td').attr('data-userReplyLike') !== '-1';
		
		if(isUserLiked){
			alert('게시글에 이미 공감하셨습니다.');
			return false;
		}
		
		let msg ='게시글에 싫어요를 남기시겠습니까?';
		if(replyLike === '1'){
			msg = '게시글에 좋아요를 남길까요?';
		}
		
		if(! confirm(msg)){
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/bbs/insertReplyLike';
		let params = {replyNum:replyNum , replyLike:replyLike};
		
		const fn = function(data){
			let state = data.state;
			if(state === 'true'){
				let likeCount = data.likeCount;
				let disLikeCount = data.disLikeCount;
				
				$btn.parent('td').children().eq(0).find('span').html(likeCount);
				$btn.parent('td').children().eq(1).find('span').html(disLikeCount);
				
				$btn.parent('td').attr('data-userReplyLike',ReplyLike);
				$btn.find('i').css('color', 'red');
			
				
				
			}else if(state === 'liked'){
				alert('게시글 공감여부는 한번만 가능합니다.');
			}else {
				alert('게시글 공감여부 처리가 실패했습니다.');
			}
		};
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});

// 댓글 숨김
$(function(){
	$('.reply').on('click','.hideReply',function(){
		const $menu = $(this);
		let replyNum = $(this).attr('data-replyNum');
		let showReply = $(this).attr('data-showReply');
		let msg = '댓글을 비공개로 전환하겠습니까?';
		
		if(showReply === '0'){
			msg = '댓글을 공개로 전환하겠습니까??';
		}
		
		if(! confirm(msg)){
			return false
		}
		
		showReply = showReply === '1' ?'0':'1';
		
		let url = '${pageContext.request.contextPath}/bbs/replyShowHide';
		let params = {replyNum:replyNum, showReply: showReply};
		
		const fn = function(data) {
			
			if(data.state === 'true'){
				let $item = $($menu).closest('tr').next('tr').find('td');
				if(showReply === '1'){
					$item.removeClass('text-primary').removeClass('text-opacity-50');
					$menu.attr('data-showReply','1');
					$menu.text('비공개');
				}else{
					$item.addClass('text-primary').addClass('text-opacity-50');
					$menu.attr('data-showReply','0');
					$menu.text('공개');
				}
				
			}
			
		}
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});

$(function(){
	$('.reply').on('click','.hideReplyAnswer',function(){
		const $menu = $(this);
		let replyNum = $(this).attr('data-replyNum');
		let showReply = $(this).attr('data-showReply');
		let msg = '대댓글을 비공개로 전환하겠습니까?';
		
		if(showReply === '0'){
			msg = '대댓글을 공개로 전환하겠습니까??';
		}
		
		if(! confirm(msg)){
			return false
		}
		
		showReply = showReply === '1' ?'0':'1';
		
		let url = '${pageContext.request.contextPath}/bbs/replyShowHide';
		let params = {replyNum:replyNum, showReply: showReply};
		
		const fn = function(data) {
			
			if(data.state === 'true'){
				let $item = $($menu).closest('.row').next('div');
				if(showReply === '1'){
					$item.removeClass('text-primary').removeClass('text-opacity-50');
					$menu.attr('data-showReply','1');
					$menu.text('비공개');
				}else{
					$item.addClass('text-primary').addClass('text-opacity-50');
					$menu.attr('data-showReply','0');
					$menu.text('공개');
				}
				
			}
			
		}
		
		sendAjaxRequest(url,'post',params,'json',fn);
	});
});


</script>
	
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>