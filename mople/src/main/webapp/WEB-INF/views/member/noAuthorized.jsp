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
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>

<main>
	<div class="container">
		<div class="body-container row justify-content-center">
			<div class="col-md-6 my-5 p-3">

	            <div class="border mt-5 p-5">
	                <h4 class="text-center fw-bold text-danger mb-4">
	                    <i class="bi bi-exclamation-triangle"></i> 경고 !
	                </h4>
	
	                <div class="d-grid pt-3">
	                    <p class="text-center mb-4">
	                        <strong>해당 정보를 접근 할 수 있는 권한 이 없습니다.</strong><br>
	                        메인화면으로 이동하시기 바랍니다.
	                    </p>
	                </div>
	
	                <div class="d-grid">
	                    <button type="button" class="btn btn-lg btn-primary" onclick="location.href='${pageContext.request.contextPath}/';">
	                        메인화면으로 이동 <i class="bi bi-arrow-counterclockwise"></i>
	                    </button>
	                </div>
	            </div>

			</div>
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/footerResources.jsp"/>

</body>
</html>