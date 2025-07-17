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
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            margin: 0;
            background-color: #f8f9fa;
        }
        .nav-bar {
            background-color: white;
            display: flex;
            justify-content: space-between;
            padding: 16px 32px;
            border-bottom: 1px solid #e0e0e0;
        }
        .nav-bar .logo {
            font-size: 24px;
            font-weight: bold;
            color: #6A0DAD;
        }
        .nav-bar .menu {
            display: flex;
            gap: 20px;
        }
        .side-bar {
            width: 200px;
            background: white;
            height: 100vh;
            padding: 20px;
            border-right: 1px solid #e0e0e0;
        }
        .side-bar h3 {
            margin-bottom: 20px;
        }
        .side-bar ul {
            list-style: none;
            padding: 0;
        }
        .side-bar ul li {
            padding: 10px 0;
            cursor: pointer;
        }
        .container {
            display: flex;
        }
        .main-content {
            flex: 1;
            padding: 30px;
        }
        .card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-bottom: 20px;
			margin-left: 220px;
        }
        .card h4 {
            margin-top: 0;
        }
        .stats-grid {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }
        .stat-box {
            flex: 1;
            min-width: 200px;
            background: #f1f3f5;
            padding: 16px;
            border-radius: 8px;
            text-align: center;
        }
    </style>
</head>
<body>
 <div class="wrap">
    <header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
    </header>
    
	<main class="main">
		<div class="main-content">
			<div class="meetings-layout">
			    <div class="main-content">
			        <div class="card">
						<h4><i class="bi bi-whatsapp"></i> 문의하기 </h4>
		    
		   			 	<div class="stats-grid row">
						<div class="body-main">
							
							<div class="row board-list-header">
								<div class="col-auto me-auto dataCount">
									${dataCount}개(${page}/${total_page} 페이지)
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
												<c:choose>
													<c:when test="${dto.secret==1}">
														<c:if test="${sessionScope.member.memberIdx==dto.memberIdx || sessionScope.member.role == 0}">
															<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.subject}</a>
														</c:if>
														<c:if test="${sessionScope.member.memberIdx!=dto.memberIdx && sessionScope.member.role != 0}">
															비밀글 입니다.
														</c:if>
														<i class="bi bi-file-lock2"></i>
													</c:when>
													<c:otherwise>
														<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.subject}</a>
													</c:otherwise>
												</c:choose>
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
				
							<div class="row board-list-footer">
								<div class="col">
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/list';" title="새로고침"><i class="bi bi-arrow-counterclockwise"></i></button>
								</div>
								<div class="col-6 d-flex justify-content-center">
									<form class="row" name="searchForm">
										<div class="col-auto p-1">
											<select name="schType" class="form-select">
												<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
												<option value="userNickName" ${schType=="userNickName"?"selected":""}>작성자</option>
												<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
												<option value="subject" ${schType=="subject"?"selected":""}>제목</option>
												<option value="content" ${schType=="content"?"selected":""}>내용</option>
											</select>
										</div>
										<div class="col-auto p-1">
											<input type="text" name="kwd" value="${kwd}" class="form-control">
										</div>
										<div class="col-auto p-1">
											<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
										</div>
									</form>
								</div>
								<div class="col text-end">
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/write';">문의등록</button>
								</div>
							</div>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</main>

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
	
	let url = '${pageContext.request.contextPath}/qna/list';
	location.href = url + '?' + params;
}
</script>


<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>

<jsp:include page="/WEB-INF/views/admin/layout/footerResources.jsp"/>


</div>
</body>
</html>
