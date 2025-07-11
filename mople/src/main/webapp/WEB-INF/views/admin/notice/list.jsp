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
            margin: 0;
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f8f9fa;
        }
        .nav-bar {
            background-color: white;
            padding: 16px 32px;
            display: flex;
            justify-content: space-between;
            border-bottom: 1px solid #ddd;
        }
        .container {
            display: flex;
        }
        .sidebar {
            width: 200px;
            background: #ffffff;
            border-right: 1px solid #ddd;
            padding: 20px;
        }
        .sidebar h3 {
            margin-bottom: 20px;
        }
        .sidebar ul {
            list-style: none;
            padding: 0;
        }
        .sidebar ul li {
            margin-bottom: 10px;
        }
        .sidebar ul li a {
            text-decoration: none;
            color: #333;
        }
        .main-content {
            flex: 1;
            padding: 30px;
        }
        .main-content h2 {
        	margin-left: 220px;
            margin-bottom: 20px;
        }
        .notice-table {
            width: 85%;
            border-collapse: collapse;
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            margin-left: 220px;
        }
        .notice-table th, .notice-table td {
            padding: 14px 16px;
            border-bottom: 1px solid #eee;
            text-align: left;
        }
        .notice-table th {
            background-color: #f1f3f5;
        }
        .btn {
            padding: 10px 16px;
            background-color: #6A0DAD;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            margin-bottom: 20px;
            margin-left: 220px; 
        }
        .btn:hover {
            background-color: #580a9e;
        }
        .action-btns a {
            margin-right: 8px;
            color: #007bff;
            text-decoration: none;
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

	    <!-- 메인 콘텐츠 -->
	    <div class="main-content">
	    	<div class="meetings-layout">
	    		<div class="main-content">
	    		
			        <h2>공지사항 관리</h2>
			
			        <a href="/admin/notice/write"><button class="btn">+ 새 공지사항 등록</button></a>
			
			        <table class="notice-table">
			            <thead>
			                <tr>
			                    <th>번호</th>
			                    <th>제목</th>
			                    <th>작성일</th>
			                    <th>관리</th>
			                </tr>
			            </thead>
			            <tbody>
			                <c:forEach var="notice" items="${noticeList}">
			                    <tr>
			                        <td>${notice.id}</td>
			                        <td><a href="/admin/notice/view?id=${notice.id}">${notice.title}</a></td>
			                        <td>${notice.createdDate}</td>
			                        <td class="action-btns">
			                            <a href="/admin/notice/edit?id=${notice.id}">수정</a>
			                            <a href="/admin/notice/delete?id=${notice.id}" onclick="return confirm('삭제하시겠습니까?');">삭제</a>
			                        </td>
			                    </tr>
			                </c:forEach>
			
			                <c:if test="${empty noticeList}">
			                    <tr>
			                        <td colspan="4">등록된 공지사항이 없습니다.</td>
			                    </tr>
			                </c:if>
			            </tbody>
			        </table>
	    
	    		</div>
	    	</div>
	    </div>
		

	</main>
</div>
</body>
</html>