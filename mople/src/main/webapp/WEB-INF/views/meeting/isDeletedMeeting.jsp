<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 삭제된 모임</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/deletedMeeting.css" type="text/css">
</head>
<body>
	<div class="wrap">
	    <header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	    </header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp"/>
	
	    <main class="main">
	    	<div class="deleted-meeting-container">
		        <h3>앗! 이미 삭제된 모임이에요 😢</h3>
		        <p>
		            죄송합니다. 이전에 찾으시던 모임은 <br>운영자의 판단 또는 다른 사유로 인해 이미 삭제되었어요.<br>
		            다른 흥미로운 모임을 찾아보시거나, <br>새로운 모임을 직접 만들어보는 건 어떠세요?
		        </p>
		        <div class="button-group">
		            <button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/meeting/meetingList?sportCategory=0&regionCategory=0'">다른 모임 찾아보기</button>
		            <button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/meeting/meetingCreate'">새 모임 만들기</button>
		        </div>
	        </div>
	    </main>
	</div>
	<footer class="footer">
        <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    </footer>
    <jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>