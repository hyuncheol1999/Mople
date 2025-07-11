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
</head>
<body>
	<div class="wrap">
	    <header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	    </header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp"/>
	
	    <main class="main">
	    	<div class="container">
		        <h2>새로운 스포츠 모임 만들기 🚀</h2>
		        <form name="meetingForm" method="post" enctype="multipart/form-data">
		            <div class="form-group">
		                <label class="title" for="meetingName">모임명:</label>
		                <input type="text" id="meetingName" name="meetingName" placeholder="모임 이름을 입력해주세요 (필수)" required>
		                <div class="error-message" id="meetingNameError"></div>
		            </div>
		
		            <div class="form-group">
		                <label for="meetingDescription">모임 소개글:</label>
		                <textarea id="meetingDescription" name="meetingDesc" placeholder="모임에 대한 자세한 내용을 입력해주세요 (필수)" required></textarea>
		                <div class="error-message" id="meetingDescriptionError"></div>
		            </div>
		
		            <div class="form-group">
		                <label for="meetingImage">모임 대표 사진:</label>
		                <input type="file" id="meetingImage" name="meetingProfilePhoto" accept="image/*">
		                <div class="file-name-display" id="fileNameDisplay">선택된 파일 없음</div>
		                <div class="error-message" id="meetingImageError"></div>
		            </div>
		
		            <div class="form-group">
		                <label for="sportCategory">스포츠 카테고리:</label>
		                <div class="category-select-wrapper">
		                    <input name="sportIdx" type="text" id="sportCategorySearch" class="category-search-input" placeholder="스포츠 카테고리를 검색 또는 선택 (필수)" required>
		                    <input type="hidden" id="sportCategoryNo" name="sportCategoryNo">
		                    <div class="category-list" id="sportCategoryList">
		                        <ul>
		                            <li data-category-no="1">축구 ⚽</li>
		                            <li data-category-no="2">농구 🏀</li>
		                            <li data-category-no="3">야구 ⚾</li>
		                            <li data-category-no="4">배드민턴 🏸</li>
		                            <li data-category-no="5">테니스 🎾</li>
		                            <li data-category-no="6">등산 ⛰️</li>
		                            <li data-category-no="7">요가 🧘‍♀️</li>
		                            <li data-category-no="8">수영 🏊</li>
		                            <li data-category-no="9">사이클 🚴</li>
		                            <li data-category-no="10">볼링 🎳</li>
		                        </ul>
		                    </div>
		                </div>
		                <div class="selected-category" id="selectedSportCategory"></div>
		                <div class="error-message" id="sportCategoryError"></div>
		            </div>
		
		            <div class="form-group">
		                <label for="regionCategory">지역 카테고리:</label>
		                <div class="category-select-wrapper">
		                    <input name="regionIdx" type="text" id="regionCategorySearch" class="category-search-input" placeholder="지역 카테고리를 검색 또는 선택 (필수)" required>
		                    <input type="hidden" id="regionCategoryNo" name="regionCategoryNo">
		                    <div class="category-list" id="regionCategoryList">
		                        <ul>
		                            <li data-category-no="1001">서울 강남구</li>
		                            <li data-category-no="1002">서울 마포구</li>
		                            <li data-category-no="1003">서울 송파구</li>
		                            <li data-category-no="1004">경기 성남시</li>
		                            <li data-category-no="1005">경기 수원시</li>
		                            <li data-category-no="1006">부산 해운대구</li>
		                            <li data-category-no="1007">대전 유성구</li>
		                            <li data-category-no="1008">대구 수성구</li>
		                        </ul>
		                    </div>
		                </div>
		                <div class="selected-category" id="selectedRegionCategory"></div>
		                <div class="error-message" id="regionCategoryError"></div>
		            </div>
		
		            <div class="button-group">
		                <button class="btn btn-outline btn-large" onclick="sendOk();">모임 생성</button>
		                <button class="btn btn-outline btn-large" type="button" onclick="history.back()">취소</button>
		            </div>
		            <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
           			<input type="hidden" id="mode" value="${mode}">
		        </form>   
	        </div>
	    </main>
	</div>
	<!-- 아래는 footer 필요시 사용 -->
    <footer class="footer">
        <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    </footer>
    <jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
    <script>const contextPath = "${pageContext.request.contextPath}";</script>
    <script>const mode = "${mode}";</script>
    <script type="text/javascript"
		src="${pageContext.request.contextPath}/dist/js/meeting.js"></script>
</body>
</html>
