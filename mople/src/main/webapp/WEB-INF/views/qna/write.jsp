<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Spring</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css" type="text/css">
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
            margin: 0px 150px;
            
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
.btn {
    height: 40px;
    font-size: 16px;
    border-radius: 6px;
    padding: 6px 12px;
}
.btn i {
    vertical-align: middle;
}
</style>
    
<style type="text/css">
.min-h-150 {
   min-height: 150px;
}

.min-h-70 {
   min-height: 70px !important;
}

.answer-container textarea {
   width: 100%;
   height: 70px;
   resize: none;
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

					<div class="body-container row justify-content-center">
						<div class="col-md-10 my-3 p-3">
							<div class="body-title">
								<h3> ❓ 문의하기 </h3>
							</div>
							
							<div class="body-main">
			
								<form name="questionForm" method="post">
									<table class="table mt-5 write-form">
										<tr>
											<td class="bg-light col-sm-2" scope="row">제 목</td>
											<td>
												<input type="text" name="subject" maxlength="100" class="form-control" value="${dto.subject}">
											</td>
										</tr>
					        
										<tr>
											<td class="bg-light col-sm-2" scope="row">작성자명</td>
					 						<td>
												<p class="form-control-plaintext">${sessionScope.member.userNickName}</p>
											</td>
										</tr>
					
										<tr>
											<td class="bg-light col-sm-2" scope="row">공개여부</td>
											<td class="py-3"> 
												<input type="radio" name="secret" id="secret1" class="form-check-input" 
													value="0" ${empty dto || dto.secret==0?"checked='checked'":"" }>
												<label class="form-check-label" for="secret1">공개</label>
												<input type="radio" name="secret" id="secret2" class="form-check-input"
													value="1" ${dto.secret==1?"checked='checked'":"" }>
												<label class="form-check-label" for="secret2">비공개</label>
											</td>
										</tr>
									
										<tr>
											<td class="bg-light col-sm-2" scope="row">내 용</td>
											<td>
												<textarea name="content" class="form-control">${dto.content}</textarea>
											</td>
										</tr>
									</table>
									
									<table class="table table-borderless">
					 					<tr>
											<td class="text-center">
												<button type="button" class="btn btn-outline rounded" onclick="sendOk();">${mode=='update'?'수정완료':'등록완료'}&nbsp;<i class="bi bi-check2"></i></button>
												<button type="reset" class="btn btn-outline rounded">다시입력</button>
												<button type="button" class="btn btn-outline rounded" onclick="location.href='${pageContext.request.contextPath}/qna/list';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
												<c:if test="${mode=='update'}">
													<input type="hidden" name="num" value="${dto.num}">
													<input type="hidden" name="page" value="${page}">
												</c:if>
											</td>
										</tr>
									</table>
								</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
</div>
<script type="text/javascript">
function sendOk() {
	const f = document.questionForm;
	let str;
	
	str = f.subject.value.trim();
	if( ! str ) {
		alert('제목을 입력하세요. ');
		f.subject.focus();
		return;
	}

	str = f.content.value.trim();
	if( ! str ) {
		alert('내용을 입력하세요. ');
		f.content.focus();
		return;
	}

	f.action = '${pageContext.request.contextPath}/qna/${mode}';
	f.submit();
}
</script>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/footerResources.jsp"/>

</body>
</html>