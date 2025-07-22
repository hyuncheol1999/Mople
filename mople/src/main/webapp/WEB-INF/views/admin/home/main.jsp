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
			        <div class="card" style="padding: 10px;">
			            <h4> 📊 요약 통계 </h4>
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
			
					<div class="card" style="padding: 10px;">
					    <h4 style="margin-bottom: 10px;"> 🔥 최근 생성된 정모 </h4>
					    <table class="table-meeting" style="width: 100%; border-collapse: collapse; text-align: center; background-color: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
					        <thead style="background-color: #f9f9f9;">
					            <tr style="border-bottom: 2px solid #eee;">
					                <th style="padding: 12px;">정모 제목</th>
					                <th style="padding: 12px;">시작 날짜</th>
					                <th style="padding: 12px;">종료 날짜</th>
					                <th style="padding: 12px;">장소</th>
					                <th style="padding: 12px;">정원</th>
					            </tr>
					        </thead>
					        <tbody>
					            <c:forEach var="list" items="${list}">
					                <tr style="border-bottom: 1px solid #f0f0f0;">
					                    <td style="padding: 10px;">${list.subject}</td>
					                    <td style="padding: 10px;">${list.startDate}</td>
										<td style="padding: 10px;">${list.endDate}</td>
					                    <td style="padding: 10px;">${list.place}</td>
					                    <td style="padding: 10px;">${list.capacity}명</td>
					                </tr>
					            </c:forEach>
					        </tbody>
					    </table>
					</div>			    
				</div>			
			</div>
		</div>	
    </main>

</div>
</body>
</html>
