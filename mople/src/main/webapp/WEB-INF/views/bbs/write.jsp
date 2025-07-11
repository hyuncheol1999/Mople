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
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css" type="text/css">
</head>
<body>
<div class="wrap">
    <header class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
    </header>
<jsp:include page="/WEB-INF/views/layout/login.jsp"/>

    <main class="main"> 
     	<div class="container">
		<div class="body-container row justify-content-center">
			<div class="col-md-10 my-3 p-3">
				<div class="body-title">
					<h3><i class="bi bi-app"></i> 게시판 </h3>
				</div>
				
				<div class="body-main">

					<form name="postForm" method="post">
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
								<td class="bg-light col-sm-2" scope="row">내 용</td>
								<td>
									<textarea name="content" class="form-control">${dto.content}</textarea>
								</td>
							</tr>
						</table>
						
						<table class="table table-borderless">
		 					<tr>
								<td class="text-center">
									<button type="button" class="btn btn-dark" onclick="sendOk();">${mode =="update"?"수정완료":"등록완료"}&nbsp;<i class="bi bi-check2"></i></button>
									<button type="reset" class="btn btn-light">다시입력</button>
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/bbs/list';">${mode =="update"?"수정취소":"등록취소"}&nbsp;<i class="bi bi-x"></i></button>
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
    </main>
</div>
<script type="text/javascript">
function sendOk() {
	const f = document.postForm;
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

	f.action = '${pageContext.request.contextPath}/bbs/${mode}';
	f.submit();
}
</script>
    <footer class="footer">
        <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    </footer>
    <jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>