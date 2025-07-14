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
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meeting.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meetingCreate.css" type="text/css">
</head>
<body>
	<div class="wrap">
	    <header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	    </header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp"/>
	
	    <main class="main">
	    	<div class="meetingContainer">
		        <h2>사진 등록 📸</h2>
		        <form name="albumForm" method="post" enctype="multipart/form-data">
		            <div class="form-group">
		                <label for="meetingAlbumImage">사진</label>
		                <input type="file" id="meetingAlbumImage" name="meetingAlbumImage" accept="image/*">
		                <div class="file-name-display" id="fileNameDisplay">선택된 파일 없음 (필수)</div>
		                
		                <small class="error-message" id="meetingAlbumImageError">사진은 필수 사항입니다!</small>
		            </div>
		            <div class="form-group">
		                <label for="content">사진 설명</label>
		                <textarea id="content" name="content" placeholder="사진 설명을 입력해주세요"></textarea>
		                <small class="error-message" id="contentError">사진 설명</small>
		            </div>
		
		            <div class="button-group">
		                <button type="submit" class="btn btn-outline btn-large">사진 등록</button>
		                <button class="btn btn-outline btn-large" type="button" onclick="history.back()">취소</button>
		            </div>
		            <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
           			<input type="hidden" id="mode" value="${mode}">
		        </form>   
	        </div>
	    </main>
	</div>
    <footer class="footer">
        <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    </footer>
    <jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
    <script>const contextPath = "${pageContext.request.contextPath}";</script>
    <script>const mode = "${mode}";</script>
    <script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/meetingAlbumUpload.js"></script>
</body>
</html>
