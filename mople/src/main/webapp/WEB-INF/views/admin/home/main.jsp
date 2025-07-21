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
			            <div class="body-main stats-grid">
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
			            <table class="table-meeting"  cellpadding="10" cellspacing="0" width="100%">
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
