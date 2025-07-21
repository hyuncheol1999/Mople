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

.btn {
    height: 40px;
    font-size: 16px;
    border-radius: 6px;
    padding: 6px 12px;
}
.btn i {
    vertical-align: middle;
}

.body-title,
.body-main {
	padding-left: 350px;
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
							<h3><i class="bi bi-app"></i> 공지사항 </h3>
					    </div>
					    
					    <div class="body-main row">
							<div class="col-xxl-9">
								<table class="table board-article">
									<thead>
										<tr>
											<td colspan="2" align="center">
												${dto.subject}
											</td>
										</tr>
									</thead>
									
									<tbody>
										<tr>
											<td width="50%">
												이름 : ${dto.userNickName}
											</td>
											<td align="right">
												${dto.reg_date} | 조회 ${dto.hitCount}
											</td>
										</tr>
										
										<tr>
											<td colspan="2" valign="top" height="200" style="border-bottom:none;">
												${dto.content}
											</td>
										</tr>
				
										<tr>
											<td colspan="2">
												<c:if test="${listFile.size() != 0}">
													<p class="border text-secondary mb-1 p-2">
														<i class="bi bi-folder2-open"></i>
														<c:forEach var="vo" items="${listFile}" varStatus="status">
															<a href="${pageContext.request.contextPath}/admin/notice/download?fileNum=${vo.fileNum}" class="text-reset">${vo.originalFilename}</a>
															<c:if test="${not status.last}">|</c:if>
														</c:forEach>
													</p>
												</c:if>
											</td>
										</tr>
										
										<tr>
											<td colspan="2">
												이전글 :
												<c:if test="${not empty prevDto}">
													<a href="${pageContext.request.contextPath}/admin/notice/article?${query}&num=${prevDto.num}">${prevDto.subject}</a>
												</c:if>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												다음글 :
												<c:if test="${not empty nextDto}">
													<a href="${pageContext.request.contextPath}/admin/notice/article?${query}&num=${nextDto.num}">${nextDto.subject}</a>
												</c:if>
											</td>
										</tr>
									</tbody>
								</table>
								
								<table class="table table-borderless mb-2">
									<tr>
										<td width="50%">
											<c:choose>
												<c:when test="${sessionScope.member.userId==dto.userId}">
													<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?num=${dto.num}&page=${page}&size=${size}';">수정</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="btn btn-light" disabled>수정</button>
												</c:otherwise>
											</c:choose>
				
											<button type="button" class="btn btn-light" onclick="deleteOk();">삭제</button>
										</td>
										<td class="text-end">
											<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list?${query}';">리스트</button>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
			    </div>			
			</div>
		</div>	
    </main>
</div>

<script type="text/javascript">
	function deleteOk() {
	    if(confirm('게시글을 삭제 하시겠습니까 ? ')) {
		    let params = 'num=${dto.num}&${query}';
		    let url = '${pageContext.request.contextPath}/admin/notice/delete?' + params;
	    	location.href = url;
	    }
	}
</script>

<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>

<jsp:include page="/WEB-INF/views/admin/layout/footerResources.jsp"/>

</body>
</html>