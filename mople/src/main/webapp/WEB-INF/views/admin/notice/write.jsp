<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>공지사항 등록</title>
<jsp:include page="/WEB-INF/views/admin/layout/headerResources.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css" type="text/css">
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
    label {
        display: block;
        margin-top: 20px;
        font-weight: bold;
    }
    input[type="text"],
    textarea {
        width: 100%;
        padding: 12px;
        border: 1px solid #ccc;
        border-radius: 6px;
        margin-top: 8px;
        font-size: 14px;
    }
    textarea {
        height: 200px;
        resize: none; /* 크기 조절 비활성화 */
    }
    input[type="file"] {
        display: block;
        margin-top: 8px;
        padding: 8px;
        font-size: 14px;
        width: 100%;
        border: 1px solid #ccc;
        border-radius: 6px;
        background-color: #fefefe;
    }
    .option {
        display: flex;
        justify-content: flex-start;
        gap: 30px;
        margin-top: 20px;
        padding-left: 4px;
    }
    .option label {
        font-weight: normal;
        font-size: 14px;
        display: flex;
        align-items: center;
        gap: 6px;
    }
    .option input[type="checkbox"] {
        transform: scale(1.1);
    }
    .btn-box {
        margin-top: 30px;
        text-align: right;
    }
    .btn {
        padding: 10px 20px;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
        margin-left: 10px;
    }

</style>
</head>
<body>

<div class="container">
    <h2>공지사항 등록</h2>

    <form name="noticeForm" method="post" onsubmit="return check();" enctype="multipart/form-data">
        <label for="subject">제목</label>
        <input type="text" id="subject" name="subject" maxlength="100" class="form-control" value="${dto.subject}" />

        <label for="content">내용</label>
        <textarea name="content" id="content" class="form-control" style="width: 99%; height: 300px;">${dto.content}</textarea>
        
        <label for="selectFile">파일 첨부</label>
        <input type="file" id="selectFile" name="selectFile" multiple />
        
        <!-- 체크박스 옵션 -->
        <div class="option">
			<input type="checkbox" class="form-check-input" name="notice" id="notice" value="1" ${dto.notice==1 ? "checked ":"" } >
			<label class="form-check-label" for="notice"> 공지</label>
        </div>

        <div class="btn-box">
            <button type="button" class="btn btn-outline rounded" onclick="sendOk();">${mode=='update'?'수정완료':'등록완료'}&nbsp;<i class="bi bi-check2"></i></button>
            <button type="reset" class="btn cancel btn-outline rounded">다시입력</button>
            <button type="button" class="btn cancel btn-outline rounded" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list';">
                ${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i>
            </button>
        </div>

        <input type="hidden" name="size" value="10">
        <c:if test="${mode=='update'}">
            <input type="hidden" name="num" value="${dto.num}">
            <input type="hidden" name="page" value="${page}">
            <input type="hidden" name="size" value="10">
        </c:if>
    </form>
</div>

<script type="text/javascript">
function sendOk() {
    const f = document.noticeForm;
    let str;
    
    str = f.subject.value.trim();
    if (!str) {
        alert('제목을 입력하세요.');
        f.title.focus();
        return false;
    }

    str = f.content.value.trim();
    if (!str || str === '<p><br></p>') {
        alert('내용을 입력하세요.');
        f.content.focus();
        return false;
    }

    f.action = '${pageContext.request.contextPath}/admin/notice/${mode}';
    f.submit();
    return true;
}

<c:if test="${mode=='update'}">
    function deleteFile(fileNum) {
        if (!confirm('파일을 삭제 하시겠습니까?')) return;
        let params = 'num=${dto.num}&fileNum=' + fileNum + '&page=${page}&size=10';
        let url = '${pageContext.request.contextPath}/admin/notice/deleteFile?' + params;
        location.href = url;
    }
</c:if>
</script>

</body>
</html>
