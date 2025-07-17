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
		
						<div class="body-main">
						
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
										관리자
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
										<c:choose>
											<c:when test="${prevDto.secret==1}">
												<c:if test="${sessionScope.member.memberIdx==prevDto.memberIdx || sessionScope.member.role == 0}">
													<a href="${pageContext.request.contextPath}/qna/article?num=${prevDto.num}&${query}">${prevDto.subject}</a>
												</c:if>
												<c:if test="${sessionScope.member.memberIdx!=prevDto.memberIdx && sessionScope.member.role != 0}">
													비밀글 입니다.
												</c:if>
												<i class="bi bi-file-lock2"></i>
											</c:when>
											<c:otherwise>
												<a href="${pageContext.request.contextPath}/qna/article?num=${prevDto.num}&${query}">${prevDto.subject}</a>
											</c:otherwise>
										</c:choose>
									</c:if>
								</div>
		
								<div class="col-md-2 text-center bg-light border-bottom p-2">
									다음글
								</div>
								<div class="col-md-10 border-bottom p-2">
									<c:if test="${not empty nextDto}">
										<c:choose>
											<c:when test="${nextDto.secret==1}">
												<c:if test="${sessionScope.member.memberIdx==nextDto.memberIdx || sessionScope.member.role == 0}">
													<a href="${pageContext.request.contextPath}/qna/article?num=${nextDto.num}&${query}">${nextDto.subject}</a>
												</c:if>
												<c:if test="${sessionScope.member.memberIdx!=nextDto.memberIdx && sessionScope.member.role != 0}">
													비밀글 입니다.
												</c:if>
												<i class="bi bi-file-lock2"></i>
											</c:when>
											<c:otherwise>
												<a href="${pageContext.request.contextPath}/qna/article?num=${nextDto.num}&${query}">${nextDto.subject}</a>
											</c:otherwise>
										</c:choose>
									</c:if>
								</div>
		
								<div class="col-md-6 p-2 ps-0">
									<c:if test="${sessionScope.member.memberIdx==dto.memberIdx && empty dto.answer}">
										<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/update?num=${dto.num}&page=${page}';">수정</button>
									</c:if>
							    	
						    		<c:if test="${sessionScope.member.memberIdx==dto.memberIdx || sessionScope.member.role == 0}">
						    			<button type="button" class="btn btn-light" onclick="deleteOk('question');">삭제</button>
						    		</c:if>
								</div>
								<div class="col-md-6 p-2 pe-0 text-end">
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/list?${query}';">리스트</button>
								</div>
							</div>

				</div>
			</div>
		</div>
	</div>
</main>

<c:if test="${sessionScope.member.memberIdx==dto.memberIdx || sessionScope.member.role == 0}">
	<script type="text/javascript">
		function deleteOk(mode) {
			if(confirm('질문을 삭제 하시 겠습니까 ? ')) {
				let query = 'num=${dto.num}&${query}&mode=' + mode;
				let url = '${pageContext.request.contextPath}/qna/delete?' + query;
				location.href = url;
			}
		}
	</script>
</c:if>


<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>

<jsp:include page="/WEB-INF/views/admin/layout/footerResources.jsp"/>

</body>
</html>