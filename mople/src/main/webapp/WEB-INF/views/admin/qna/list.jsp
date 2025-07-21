<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />  
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/paginate.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meeting.css" type="text/css">
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
							<h4> ❓ 문의하기 </h4>
						</div>
		    
		   			 	<div class="body-main stats-grid row">
		   			 	<div class="col-md-12 text-start">
							<div class="col-md-4 text-start">
								<form name="searchForm" class="input-group">
									<input type="text" name="kwd" value="${kwd}" class="form-control rounded me-1" placeholder="검색어를 입력하세요">
									<button type="button" class="btn btn-outline rounded" onclick="searchList();"><i class="bi bi-search"></i></button>
								</form>							
							</div>
							<div class="col-auto">&nbsp;</div>
						</div>				

						<table class="table table-hover board-list">
							<thead class="table-light">
								<tr>
									<th width="60">번호</th>
									<th>제목</th>
									<th width="100">작성자</th>
									<th width="100">질문일자</th>
									<th width="80">처리결과</th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach var="dto" items="${list}" varStatus="status">
									<tr>
										<td>${dataCount - (page-1) * size - status.index}</td>
										<td class="left">
											<div class="text-wrap">
												<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.subject}</a>
												<c:if test="${dto.secret==1}">
													<i class="bi bi-file-lock2"></i>
												</c:if>													
											</div>
										</td>
										<td>${dto.userNickName}</td>
										<td>${dto.reg_date}</td>
										<td>${dto.answerIdx!=0?"답변완료":"답변대기"}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						
						<div class="page-navigation">
							${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
						</div>
					</div>
					</div>
				</div>
			</div>
		</div>	
    </main>

<script type="text/javascript">
function changeList() {
    const f = document.listForm;
    f.page.value = '1';
    
	const formData = new FormData(f);
	let params = new URLSearchParams(formData).toString();
	
	let url = '${pageContext.request.contextPath}/admin/notice/list';
	location.href = url + '?' + params;    
}

window.addEventListener('DOMContentLoaded', () => {
	const btnDeleteEL = document.querySelector('button#btnDeleteList');
	const chkAllEL = document.querySelector('input#chkAll');
	const numsELS = document.querySelectorAll('form input[name=nums]');
	
	btnDeleteEL.addEventListener('click', () => {
		const f = document.listForm;
		const checkedELS = document.querySelectorAll('form input[name=nums]:checked');
		
		if(checkedELS.length === 0) {
			alert('삭제할 게시물을 먼저 선택하세요');
			return;
		}
		
		if( confirm('선택한 게시글을 삭제하시겠습니까 ? ') ) {
			const f = document.listForm;
			f.action = '${pageContext.request.contextPath}/admin/notice/deleteList';
			f.submit();
		}		
	});
	
	chkAllEL.addEventListener('click', () => {
		numsELS.forEach( inputEL => inputEL.checked = chkAllEL.checked );		
	});
	
	for(let el of numsELS) {
		el.addEventListener('click', () => {
			const checkedELS = document.querySelectorAll('form input[name=nums]:checked');
			chkAllEL.checked = numsELS.length === checkedELS.length;
		});
	}
});

// 검색 키워드 입력란에서 엔터를 누른 경우 서버 전송 막기 
window.addEventListener('DOMContentLoaded', () => {
	const inputEL = document.querySelector('form input[name=kwd]'); 
	inputEL.addEventListener('keydown', function (evt) {
	    if(evt.key === 'Enter') {
	    	evt.preventDefault();
	    	
	    	searchList();
	    }
	});
});

function searchList() {
	const f = document.searchForm;
	if(! f.kwd.value.trim()) {
		return;
	}
	
	// form 요소는 FormData를 이용하여 URLSearchParams 으로 변환
	const formData = new FormData(f);
	let params = new URLSearchParams(formData).toString();
	
	let url = '${pageContext.request.contextPath}/admin/notice/list';
	location.href = url + '?' + params;
}
</script>

<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>

<jsp:include page="/WEB-INF/views/admin/layout/footerResources.jsp"/>


</div>
</body>
</html>
