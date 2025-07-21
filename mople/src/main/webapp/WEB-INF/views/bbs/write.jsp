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
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meetingBoard.css" type="text/css">
</head>
<body>
<div class="wrap">
    <header class="header">
        <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
    </header>
    <jsp:include page="/WEB-INF/views/layout/login.jsp"/>

    <main class="board-wrapper">
        <h1 class="board-title">${mode == 'update' ? '글 수정' : '새 글 작성'}</h1>

        <form name="postForm" method="post">
            <div class="form-group">
                <label for="subject">제목</label>
                <input type="text" id="subject" name="subject" maxlength="100" class="form-control" value="${dto.subject}" placeholder="제목을 입력하세요">
            </div>

            <div class="form-group">
                <label for="writer">작성자명</label>
                <p class="form-control-plaintext">${sessionScope.member.userNickName}</p>
            </div>

            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" rows="10" class="form-control" placeholder="내용을 입력하세요">${dto.content}</textarea>
            </div>

            <div class="write-btn">
                <button type="button" class="btn btn-primary" onclick="sendOk();">
                    ${mode == "update" ? "수정 완료" : "등록 완료"} <i class="bi bi-check2"></i>
                </button>
                <button type="reset" class="btn btn-outline">다시 입력</button>
                <button type="button" class="btn btn-outline" onclick="location.href='${pageContext.request.contextPath}/bbs/list';">
                    ${mode == "update" ? "수정 취소" : "등록 취소"} <i class="bi bi-x"></i>
                </button>
                <c:if test="${mode=='update'}">
                    <input type="hidden" name="num" value="${dto.num}">
                    <input type="hidden" name="page" value="${page}">
                </c:if>
            </div>
        </form>
    </main>
</div>

<script type="text/javascript">
function sendOk() {
    const f = document.postForm;
    let str;

    str = f.subject.value.trim();
    if (!str) {
        alert('제목을 입력하세요.');
        f.subject.focus();
        return;
    }

    str = f.content.value.trim();
    if (!str) {
        alert('내용을 입력하세요.');
        f.content.focus();
        return;
    }

    f.action = '${pageContext.request.contextPath}/bbs/${mode}';
    f.submit();
}
</script>

<footer class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>
<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>