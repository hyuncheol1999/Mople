<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
href="${pageContext.request.contextPath}/dist/css/meeting.css"
	type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/paginate.css" type="text/css">
<script type="text/javascript">
	const contextPath = "${pageContext.request.contextPath}";
	const sportCategoryList = "${sportCategoryList}";
	const regionCategoryList = "${regionCategoryList}";
</script>
</head>

<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>
	<jsp:include page="/WEB-INF/views/layout/login.jsp" />

	<main class="main">
		<aside class="sidebar">
			<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />
		</aside>
		<div class="container">
			
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>
