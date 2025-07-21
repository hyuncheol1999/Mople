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
					    <h2 style="margin-bottom: 20px;">모임 관리</h2>
					
					    <div class="table-responsive">
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
					                        <td>${list.meetingIdx}</td>
					                        <td>${list.meetingName}</td>
					                        <td>${list.createdDate}</td>
					                        <td>${list.regionName}</td>
					                        <td>${list.sportName}</td>
					                        <td>
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
