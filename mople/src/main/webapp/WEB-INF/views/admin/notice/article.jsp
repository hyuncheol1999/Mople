<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            margin: 0;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 800px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .info-box {
            margin-bottom: 20px;
            font-size: 14px;
            color: #666;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
        }
        .info-box span {
            margin-right: 20px;
        }
        .content-box {
            font-size: 16px;
            line-height: 1.6;
            color: #333;
        }
        .btn-box {
            margin-top: 30px;
            text-align: right;
        }
        .btn {
            background-color: #6A0DAD;
            color: white;
            padding: 10px 18px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn:hover {
            background-color: #580a9e;
        }
        .btn.light {
            background-color: #ccc;
            color: black;
            margin-left: 8px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>${dto.subject}</h2>

    <div class="info-box">
        <span><strong>작성일:</strong> <fmt:formatDate value="${dto.createdDate}" pattern="yyyy-MM-dd HH:mm" /></span>
        <span><strong>중요공지:</strong> <c:choose>
            <c:when test="${dto.isImportant}">예</c:when>
            <c:otherwise>아니오</c:otherwise>
        </c:choose></span>
        <span><strong>비밀글:</strong> <c:choose>
            <c:when test="${dto.isSecret}">예</c:when>
            <c:otherwise>아니오</c:otherwise>
        </c:choose></span>
    </div>

    <div class="content-box">
        ${dto.content}
    </div>

    <div class="btn-box">
        <button class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?num=${dto.num}&page=${page}'">수정</button>
        <button class="btn light" onclick="if(confirm('정말 삭제하시겠습니까?')) location.href='${pageContext.request.contextPath}/admin/notice/delete?num=${dto.num}&page=${page}'">삭제</button>
        <button class="btn light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list?page=${page}'">목록</button>
    </div>
</div>

</body>
</html>