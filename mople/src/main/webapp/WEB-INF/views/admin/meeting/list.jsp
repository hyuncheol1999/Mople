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
					    <h2 style="margin: 10px 20px;"> 👥 모임 관리</h2>
					
					    <div class="table-responsive" style="padding:10px 30px;">
					        <table class="table table-hover align-middle text-center">
					            <thead class="table-light">
					                <tr>
					                    <th>모임번호</th>
					                    <th>모임명</th>
					                    <th>생성일</th>
					                    <th>지역명</th>
					                    <th>카테고리명</th>
					                    <th>관리</th>
					                </tr>
					            </thead>
					            <tbody>
					                <c:forEach var="list" items="${list}">
					                    <tr data-id="${list.meetingIdx}">
					                        <td width="80px;">${list.meetingIdx}</td>
					                        <td>${list.meetingName}</td>
					                        <td width="120px;">${list.createdDate}</td>
					                        <td width="120px;">${list.regionName}</td>
					                        <td width="120px;">${list.sportName}</td>
					                        <td width="150px;">
					                            <button type="button" class="btn btn-outline rounded" onclick="deleteMeeting('${list.meetingIdx}')">삭제</button>
					                        </td>
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

<script type="text/javascript">
function deleteMeeting(meetingIdx) {
    if (confirm('해당 모임을 삭제하시겠습니까?')) {
        let url = '${pageContext.request.contextPath}/admin/meeting/delete?meetingIdx=' + meetingIdx;
        location.href = url;
    }
}

</script>
</body>
</html>
