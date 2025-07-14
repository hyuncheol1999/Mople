<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Spring</title>
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
            resize: vertical;
        }
        .option {
            margin-top: 20px;
        }
        .option label {
            display: inline-block;
            margin-right: 20px;
            font-weight: normal;
        }
        .btn-box {
            margin-top: 30px;
            text-align: right;
        }
        .btn {
            background-color: #6A0DAD;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn:hover {
            background-color: #580a9e;
        }
        .btn.cancel {
            background-color: #ccc;
            color: black;
            margin-right: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>공지사항 등록</h2>

    <form name="noticeForm" method="post" onsubmit="return check();">
        <label for="title">제목</label>
        <input type="text" id="title" name="title" required />

        <label for="content">내용</label>
        <textarea id="content" name="content" required></textarea>
        
        <label for="selectFile">파일 첨부</label>
        <input type="file" id="selectFile" name="selectFile" multiple />
        
        <!-- 체크박스 옵션 -->
        <div class="option">
            <label><input type="checkbox" name="isImportant" value="true"> 중요 공지로 등록</label>
            <label><input type="checkbox" name="isSecret" value="true"> 비밀글로 등록</label>
        </div>

        <table class="table table-borderless">
		 	<tr>
				<td class="text-center">
					<button type="submit" class="btn btn-dark">${mode=='update'?'수정완료':'등록완료'}&nbsp;<i class="bi bi-check2"></i></button>
					<button type="reset" class="btn btn-light">다시입력</button>
					<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
					<c:if test="${mode=='update'}">
						<input type="hidden" name="num" value="${dto.num}">
						<input type="hidden" name="page" value="${page}">
					</c:if>
				</td>
			</tr>
		</table>
    </form>
</div>
						

<script type="text/javascript">
function check() {
	const f = document.noticeForm;
	let str;
	
	str = f.subject.value.trim();
	if( ! str ) {
		alert('제목을 입력하세요. ');
		f.subject.focus();
		return false;
	}

	str = f.content.value.trim();
	if( ! str || str === '<p><br></p>' ) {
		alert('내용을 입력하세요. ');
		return false;
	}

	f.action = '${pageContext.request.contextPath}/admin/notice/${mode}';
	
	return true;
}

<c:if test="${mode=='update'}">
	function deleteFile(fileNum) {
		if(! confirm('파일을 삭제 하시겠습니까 ? ')) {
			return;
		}
		
		let params = 'num=${dto.num}&fileNum=' + fileNum + '&page=${page}&size=${size}';
		let url = '${pageContext.request.contextPath}/admin/notice/deleteFile?' + params;
		location.href = url;

	}
</c:if>
</script>

</body>
</html>