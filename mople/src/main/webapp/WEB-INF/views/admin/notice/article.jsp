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
							<h3> 🚨 공지사항 </h3>
					    </div>
					    
					    <div class="body-main row">
							<div class="col-xxl-12">
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
													<button type="button" class="btn btn-outline rounded" onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?num=${dto.num}&page=${page}&size=${size}';">수정</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="btn btn-outline rounded" disabled>수정</button>
												</c:otherwise>
											</c:choose>
				
											<button type="button" class="btn btn-outline rounded" onclick="deleteOk();">삭제</button>
										</td>
										<td class="text-end">
											<button type="button" class="btn btn-outline rounded" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list?${query}';">리스트</button>
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