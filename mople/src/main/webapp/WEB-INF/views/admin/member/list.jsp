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
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/adminList.css" type="text/css">

<style>

.table-box { margin-top: 20px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 10px; border: 1px solid #ccc; text-align: center; }
th { background-color: #f1f1f1; }

.table thead th {
background-color: #f5f6fa;
color: #333;
}

.table tbody tr:hover {
background-color: #f0f0f0;
cursor: pointer;
}

.btn-outline-danger {
transition: 0.2s ease;
}

.btn-outline-danger:hover {
background-color: #dc3545;
color: white;
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

					    <h2 style="margin: 10px 20px;"> 👤 회원 관리</h2>
					
	   					<div class="table-responsive" style="padding:10px 30px;">
					        <table class="table table-hover align-middle text-center">
					            <thead class="table-light">
					                <tr>
					                    <th width="120px;">회원번호</th>
					                    <th>아이디</th>
					                    <th width="120px;">이름</th>
					                    <th width="120px;">닉네임</th>
					                    <th width="180px;">가입일</th>
					                </tr>
					            </thead>
					            <tbody>
					                <c:forEach var="list" items="${list}">
					                    <tr>
					                        <td>${list.memberIdx}</td>
					                        <td>${list.userId}</td>
					                        <td>${list.userName}</td>
					                        <td>${list.userNickName}</td>
					                        <td>${list.reg_date}</td>
					                    </tr>
					                </c:forEach>
					            </tbody>
					        </table>
					    </div>
				    </div>	
			    </div>			
			</div>
		</div>	
    </main>


</div>
</body>
</html>
