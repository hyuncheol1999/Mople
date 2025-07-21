<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp"/>  
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css" type="text/css">
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

.board-list-footer .form-select{
	width: 110px;
}      
  
.form-select,
.form-control,
.btn {
    height: 40px;
    font-size: 16px;
    border-radius: 6px;
    padding: 6px 12px;
}

.btn i {
    vertical-align: middle;
}


input[type="checkbox"].form-check-input {
    display: inline-block;
}
</style>
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
							<h4> 🚨 공지사항 </h4>
		    			</div>
		    			
		   			 	<div class="stats-grid row">
							<div class="col-md-12 text-start">
								<form name="listForm" method="post">
									<div class="row board-list-header">
										<div class="col-auto me-auto dataCount">
											<button type="button" class="btn btn-light" id="btnDeleteList" title="삭제"><i class="bi bi-trash"></i></button>
										</div>
										<div class="col-auto">
											<input type="hidden" name="size" value="10">
											<input type="hidden" name="page" value="${page}">
											<input type="hidden" name="schType" value="${schType}">
											<input type="hidden" name="kwd" value="${kwd}">
										</div>
									</div>				
				
									<table class="table table-hover board-list">
										<thead class="table-light">
											<tr>
												<th width="40">
													<input type="checkbox" class="form-check-input" name="chkAll" id="chkAll">        
												</th>
												<th width="60">번호</th>
												<th>제목</th>
												<th width="100">작성자</th>
												<th width="100" class="d-none d-md-table-cell">작성일</th>
												<th width="70" class="d-none d-md-table-cell">조회수</th>
											</tr>
										</thead>
										
										<tbody>
											<c:forEach var="dto" items="${listNotice}">
												<tr>
													<td>
														<input type="checkbox" class="form-check-input" name="nums" value="${dto.num}">
													</td>								
													<td><span class="badge bg-primary">공지</span></td>
													<td class="left">
														<div class="text-wrap">
															<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.subject}</a>
														</div>
													</td>
													<td>${dto.userNickName}</td>
													<td class="d-none d-md-table-cell">${dto.reg_date}</td>
													<td class="d-none d-md-table-cell">${dto.hitCount}</td>
												</tr>
											</c:forEach>
											
											<c:forEach var="dto" items="${list}" varStatus="status">
												<tr>
													<td>
														<input type="checkbox" class="form-check-input" name="nums" value="${dto.num}">
													</td>								
													<td>${dataCount - (page-1) * size - status.index}</td>
													<td class="left">
														<div class="text-wrap">
															<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.subject}</a>
														</div>
														<c:if test="${dto.gap<1}">
															<img class="align-middle" src="${pageContext.request.contextPath}/dist/images/new.gif">
														</c:if>
													</td>
													<td>${dto.userNickName}</td>
													<td class="d-none d-md-table-cell">${dto.reg_date}</td>
													<td class="d-none d-md-table-cell">${dto.hitCount}</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</form>
								
								<div class="page-navigation">
									${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
								</div>
					
								<div class="row board-list-footer">
									<div class="col">
										<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list';" title="새로고침"><i class="bi bi-arrow-counterclockwise"></i></button>
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
												<input type="hidden" name="size" value="10">
											</div>
											<div class="col-auto p-1">
												<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
											</div>
										</form>
									</div>
									<div class="col text-end">
										<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/write?size=10';">글올리기</button>
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
