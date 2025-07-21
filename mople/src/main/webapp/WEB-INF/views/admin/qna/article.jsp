<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Spring</title>
<jsp:include page="/WEB-INF/views/admin/layout/headerResources.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/adminList.css" type="text/css">

</head>
<body>

 <div class="wrap">
    <header class="header">
		<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
    </header>
    
	<main class="main">
		<aside class="sidebar">
			<jsp:include page="/WEB-INF/views/admin/layout/sidebar.jsp" />
		</aside>
		<div class="main-content">
			<div class="meetings-layout">
			    <div class="main-content">
			        <div class="card">
		
					    <div class="body-title">
							<h3> ❓ 문의하기 </h3>
					    </div>
					    
					    <div class="body-main">
					    	<div class="col-xxl-8">
					    	
								<div class="row p-2">
									<div class="col-md-12 ps-0 pb-1">
										<span class="sm-title fw-bold">문의사항</span>
									</div>
									
									<div class="col-md-2 text-center bg-light border-top border-bottom p-2">
										제 목
									</div>
									<div class="col-md-10  border-top border-bottom p-2">
										${dto.subject}
									</div>
			
									<div class="col-md-2 text-center bg-light border-bottom p-2">
										이 름
									</div>
									<div class="col-md-4 border-bottom p-2">
										${dto.userNickName}
									</div>
									<div class="col-md-2 text-center bg-light border-bottom p-2">
										문의일자
									</div>
									<div class="col-md-4 border-bottom p-2">
										${dto.reg_date}
									</div>
									
									<div class="col-md-12 border-bottom min-h-150">
										<div class="row h-100">
											<div class="col-md-2 text-center bg-light p-2 h-100 d-none d-md-block">
												내 용
											</div>
											<div class="col-md-10 p-2 h-100">
												${dto.content}
											</div>
										</div>
									</div>
			
									<c:if test="${not empty dto.answer}">
										<div class="col-md-12 ps-0 pt-3 pb-1">
											<span class="sm-title fw-bold">답변내용</span>
										</div>
			
										<div class="col-md-2 text-center bg-light border-top border-bottom p-2">
											담당자
										</div>
										<div class="col-md-4 border-top border-bottom p-2">
											${dto.answerNickName}
										</div>
										<div class="col-md-2 text-center bg-light border-top border-bottom p-2">
											답변일자
										</div>
										<div class="col-md-4 border-top border-bottom p-2">
											${dto.answer_date}
										</div>
										
										<div class="col-md-12 border-bottom min-h-150">
											<div class="row h-100">
												<div class="col-md-2 text-center bg-light p-2 h-100 d-none d-md-block">
													답 변
												</div>
												<div class="col-md-10 p-2 h-100">
													${dto.answer}
												</div>
											</div>
										</div>
									</c:if>
			
									<div class="col-md-2 text-center bg-light border-bottom p-2">
										이전글
									</div>
									<div class="col-md-10 border-bottom p-2">
										<c:if test="${not empty prevDto}">
											<a href="${pageContext.request.contextPath}/admin/qna/article?num=${prevDto.num}&${query}">${prevDto.subject}</a>
										</c:if>
									</div>
			
									<div class="col-md-2 text-center bg-light border-bottom p-2">
										다음글
									</div>
									<div class="col-md-10 border-bottom p-2">
										<c:if test="${not empty nextDto}">
											<a href="${pageContext.request.contextPath}/admin/qna/article?num=${nextDto.num}&${query}">${nextDto.subject}</a>
										</c:if>
									</div>
			
									<div class="col-md-6 p-2 ps-0">
						    			<button type="button" class="btn btn-outline rounded" onclick="deleteOk('question');">문의삭제</button>
							    		
										<c:if test="${not empty dto.answer and sessionScope.member.memberIdx==dto.answerIdx}">
											<button type="button" class="btn btn-outline rounded btnUpdateAnswer" data-mode="update">답변수정</button>
										</c:if>
										<c:if test="${not empty dto.answer && (sessionScope.member.memberIdx==dto.answerIdx || sessionScope.member.role == 0)}">
											<button type="button" class="btn btn-outline rounded" onclick="deleteOk('answer');">답변삭제</button>
										</c:if>
									</div>
									<div class="col-md-6 p-2 pe-0 text-end">
										<button type="button" class="btn btn-outline rounded" onclick="location.href='${pageContext.request.contextPath}/admin/qna/list?${query}';">리스트</button>
									</div>
								</div>
								
								<form name="answerForm" class="answer-container d-none" method="post">
									<div class="row p-2">
											<div class="col-md-12 ps-0 pt-3 pb-1">
												<span class="sm-title fw-bold">문의에 대한 답변</span>
											</div>
											
											<div class="col-md-2 d-flex align-items-center justify-content-center bg-light border-top border-bottom p-2 min-h-70">
												답 변
											</div>
											<div class="col-md-10 border-top border-bottom p-2 min-h-70">
												<textarea class="form-control" name="answer">${dto.answer}</textarea>
											</div>
											
											<div class="col-md-12 p-2 pe-0 text-end">
										   		<input type="hidden" name="num" value="${dto.num}">	
										   		<input type="hidden" name="page" value="${page}">
										   		
								       			<button type="button" class="btn btn-outline rounded btnSendAnswer">답변등록</button>
											</div>
									</div>
								</form>
							</div>
				    	</div>
				    	
					</div>
			    </div>			
			</div>
		</div>	
    </main>
</div>

<script type="text/javascript">
	function deleteOk(mode) {
		let s = mode === 'question' ? '질문' : '답변';
		
		if(confirm(s + '을 삭제 하시 겠습니까 ? ')) {
			let query = 'num=${dto.num}&${query}&mode=' + mode;
			let url = '${pageContext.request.contextPath}/admin/qna/delete?' + query;
			location.href = url;
		}
	}
	
	window.addEventListener('DOMContentLoaded', ev => {
		const answerEL = document.querySelector('.answer-container');
		const btnSendEL = document.querySelector('.btnSendAnswer');
		const btnUpdateEL = document.querySelector('.btnUpdateAnswer');
		const answerIdx = '${dto.answerIdx}';

		btnSendEL.addEventListener('click', e => {
			const f = document.answerForm;
			if(! f.answer.value.trim()) {
				f.answer.focus();
				return false;
			}
			
			f.action = '${pageContext.request.contextPath}/admin/qna/answer';
			f.submit();
		});
		
		if( answerIdx==0 ) {
			answerEL.classList.remove('d-none');
			// $('.answer-container').removeClass('d-none');
		} else {
			btnUpdateEL.addEventListener('click', function(e) {
				let mode = btnUpdateEL.dataset.mode;
				
				if(mode === 'update') {
					// $('.answer-container').show();
					// $('.answer-container').removeClass('d-none');
					
					answerEL.classList.remove('d-none');
					btnUpdateEL.textContent = '답변수정 취소'
					btnUpdateEL.dataset.mode = 'cancel';
				} else {
					// $('.answer-container').hide();
					// $('.answer-container').addClass('d-none');
					
					answerEL.classList.add('d-none');
					btnUpdateEL.textContent = '답변수정'
					btnUpdateEL.dataset.mode = 'update';
				}
			});
		}
	});
</script>


<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>

<jsp:include page="/WEB-INF/views/admin/layout/footerResources.jsp"/>

</body>
</html>