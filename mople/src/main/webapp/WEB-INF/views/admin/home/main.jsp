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
			            <h4>요약 통계</h4>
			            <div class="stats-grid">
			                <div class="stat-box">
			                    <strong>총 회원 수</strong>
			                    <p>${countMember}명</p>
			                </div>
			                <div class="stat-box">
			                    <strong>활성 모임 수</strong>
			                    <p>${countMeeting}개</p>
			                </div>
			                <div class="stat-box">
			                    <strong>오늘 가입자 수</strong>
			                    <p>${countTodayMember}명</p>
			                </div>
			                <div class="stat-box">
			                    <strong>문의 내역</strong>
			                    <p>${countQna}건</p>
			                </div>
			            </div>
			        </div>
			
			        <div class="card">
			            <h4>최근 생성된 정모</h4>
			            <table border="1" cellpadding="10" cellspacing="0" width="100%">
			                <tr>
			                    <th>정모제목</th>
			                    <th>시작날짜</th>
			                    <th>종료날짜</th>
			                    <th>장소</th>
			                    <th>정원</th>
			                </tr>
			                <c:forEach var="list" items="${list}">
			                    <tr>
			                        <td>${list.subject}</td>
			                        <td>${list.startDate}</td>
			                        <td>${list.endDate}</td>
			                        <td>${list.place}</td>
			                        <td>${list.capacity}</td>
			                    </tr>
			                </c:forEach>
			            </table>
			        </div>	
			    </div>			
			</div>
		</div>	
    </main>




</div>
</body>
</html>
