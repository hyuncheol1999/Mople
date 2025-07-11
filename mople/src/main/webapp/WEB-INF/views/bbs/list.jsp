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
								<i class="bi bi-app"></i> 게시판
							</h3>
						</div>

						<div class="body-main">

							<div class="row board-list-header">
								<div class="col-auto me-auto dataCount">
									${dataCount}개(${page}/${total_page} 페이지)</div>
								<div class="col-auto">&nbsp;</div>
							</div>

							<table class="table table-hover board-list">
								<thead class="table-light">
									<tr>
										<th width="60">번호</th>
										<th>제목</th>
										<th width="100">작성자</th>
										<th width="100">작성일</th>
										<th width="70">조회수</th>
									</tr>
								</thead>

								<tbody>
									<c:forEach var="dto" items="${list}" varStatus="status">
										<tr>
											<td>${dataCount - (page-1) * size - status.index}</td>
											<td class="left">
												<div class="text-wrap">
													<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.subject}</a>
												</div>
											</td>
											<td>${dto.userNickName}</td>
											<td>${dto.reg_date}</td>
											<td>${dto.hitCount}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

							<div class="page-navigation">${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
							</div>

							<div class="row board-list-footer">
								<div class="col">
									<button type="button" class="btn btn-light"
										onclick="location.href='${pageContext.request.contextPath}/bbs/list';"
										title="새로고침">
										<i class="bi bi-arrow-counterclockwise"></i>
									</button>
								</div>
								<div class="col-6 d-flex justify-content-center">
									<form class="row" name="searchForm">
										<div class="col-auto p-1">
											<select name="schType" class="form-select">
												<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
												<option value="userNickName"
													${schType=="userNickName"?"selected":""}>작성자</option>
												<option value="reg_date"
													${schType=="reg_date"?"selected":""}>등록일</option>
												<option value="subject" ${schType=="subject"?"selected":""}>제목</option>
												<option value="content" ${schType=="content"?"selected":""}>내용</option>
											</select>
										</div>
										<div class="col-auto p-1">
											<input type="text" name="kwd" value="${kwd}"
												class="form-control">
										</div>
										<div class="col-auto p-1">
											<button type="button" class="btn btn-light"
												onclick="searchList()">
												<i class="bi bi-search"></i>
											</button>
										</div>
									</form>
								</div>
								<div class="col text-end">
									<button type="button" class="btn btn-light"
										onclick="location.href='${pageContext.request.contextPath}/bbs/write';">글올리기</button>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</main>
	</div>
<script type="text/javascript">
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
	
	let url = '${pageContext.request.contextPath}/bbs/list';
	location.href = url + '?' + params;
}
</script>
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>