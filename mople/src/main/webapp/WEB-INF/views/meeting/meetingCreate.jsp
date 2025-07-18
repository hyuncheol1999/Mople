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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
	<div class="wrap">
	    <header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	    </header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp"/>
	
	    <main class="main">
	    	<div class="meetingContainer">
	    		<c:choose>
	    		<c:when test="${mode == 'meetingUpdate'}">
			        <h2>모임 정보 수정 🚀</h2>	        
		        </c:when>
		        <c:otherwise>
			        <h2>새로운 스포츠 모임 만들기 🚀</h2>	        
		        </c:otherwise>
		        </c:choose>
		        <form name="meetingForm" method="post" enctype="multipart/form-data">
		            <div class="form-group">
		                <label class="title" for="meetingName">모임명</label>
		                <input type="text" id="meetingName" name="meetingName" placeholder="모임 이름을 입력해주세요 (최대 50글자) (필수)" value="${dto.meetingName}">
		                <div class="error-message" id="meetingNameError">모임명을 다시 입력해주세요.</div>
		            </div>
		
		            <div class="form-group">
		                <label for="meetingDescription">모임 소개글</label>
		                <textarea id="meetingDescription" name="meetingDesc" placeholder="모임에 대한 자세한 내용을 입력해주세요 (필수)" >${dto.meetingDesc}</textarea>
		                <small class="error-message" id="meetingDescError">모임 소개글을 입력해주세요.</small>
		            </div>
		
		            <div class="form-group">
		                <label for="meetingImage">모임 대표 사진</label>
		                <input type="file" id="meetingImage" name="meetingProfilePhoto" accept="image/*">
		                
		                	<div class="file-name-display" id="fileNameDisplay">
		                	<c:choose>
		               			<c:when test="${dto.meetingProfilePhoto == null}">
				                	선택된 파일 없음 (328 x 148 픽셀 권장)
		               			</c:when>
		               			<c:otherwise>
		               				image.png          			
		               			</c:otherwise>
		               		</c:choose>
		                	</div>
		                
		                
		                <small class="error-message" id="meetingImageError"></small>
		            </div>
		
		            <div class="form-group">
		                <label for="sportCategory">스포츠 카테고리</label>
		                <div class="category-select-wrapper">
		                	<c:choose>
	    					<c:when test="${mode != 'meetingUpdate'}">
			                    <input name="sportIdx" type="text" id="sportCategory" class="category-search-input" placeholder="스포츠 카테고리를 검색 또는 선택해주세요! (필수)">
			                    <input type="hidden" id="sportCategoryNo" name="sportCategoryNo">
			                    <div class="category-list" id="sportCategoryList">
			                        <ul>
			                        	<c:forEach var="sDto" items="${sportCategoryList}" varStatus="status">
				                            <li data-category-no="${sDto.sportIdx}">${sDto.sportName}</li>
			                            </c:forEach>
			                        </ul>
			                    </div>
		                    </c:when>
		                    <c:otherwise>
		                    	<input name="sportIdx" type="text" id="sportCategory" class="category-search-input" value="${dto.sportName}" disabled="disabled">
		                    	<input type="hidden" id="sportCategoryNo" name="sportCategoryNo" value="${dto.sportIdx}">
		                    </c:otherwise>
		                    </c:choose>
		                </div>
		                <small class="error-message" id="sportCategoryError">스포츠 카테고리를 다시 선택/입력해주세요.</small>
		            </div>
		
		            <div class="form-group">
		                <label for="regionCategory">지역 카테고리</label>
		                <div class="category-select-wrapper">
		               		 <c:choose>
	    					<c:when test="${mode != 'meetingUpdate'}">
			                    <input name="regionIdx" type="text" id="regionCategory" class="category-search-input" placeholder="지역 카테고리를 검색 또는 선택해주세요! (필수)">
			                    <input type="hidden" id="regionCategoryNo" name="regionCategoryNo">
			                    <div class="category-list" id="regionCategoryList">
			                        <ul>
			                        	<c:forEach var="rDto" items="${regionCategoryList}" varStatus="status">
				                            <li data-category-no="${rDto.regionIdx}">${rDto.regionName}</li>
			                            </c:forEach>
			                        </ul>
			                    </div>
		                    </c:when>
		                    <c:otherwise>
		                    	<input name="regionIdx" type="text" id="regionCategory" class="category-search-input" value="${dto.regionName}" disabled="disabled">
		                    	<input type="hidden" id="regionCategoryNo" name="regionCategoryNo" value="${dto.regionIdx}">
		                    </c:otherwise>
		                    </c:choose>
		                </div>
		                <small class="error-message" id="regionCategoryError">지역 카테고리를 다시 선택/입력해주세요.</small>
		            </div>
		
		            <div class="button-group">
		                <button type="submit" class="btn btn-outline btn-large">모임 ${mode == "meetingUpdate" ? "수정" : "생성"}</button>
		                <button class="btn btn-outline btn-large" type="button" onclick="history.back()">취소</button>
		            </div>
		            <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
           			<input type="hidden" id="mode" value="${mode}">
           			<c:if test="${mode == 'meetingUpdate'}">
						<input type="hidden" name="meetingIdx" value="${dto.meetingIdx}">
					</c:if>
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
	<c:if test="${mode == 'meetingCreate'}">
	    <script type="text/javascript"
			src="${pageContext.request.contextPath}/dist/js/meetingCreate.js"></script>
	</c:if>
	<c:if test="${mode == 'meetingUpdate'}">
	    <script type="text/javascript"
			src="${pageContext.request.contextPath}/dist/js/meetingUpdate.js"></script>
	</c:if>
</body>
</html>
