<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>번개모임 생성</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meeting.css">
<style>
.form-wrap {max-width:780px;margin:40px auto;padding:32px;background:#fff;
  border:1px solid #eee;border-radius:16px;}
.form-wrap h2 {font-weight:700;margin-bottom:28px;}
.form-group {margin-bottom:20px;}
.form-group label {font-weight:600;display:block;margin-bottom:6px;}
.form-group input[type=text],
.form-group input[type=datetime-local],
.form-group input[type=number],
.form-group textarea,
.form-group select {
  width:100%;padding:14px 16px;border:1px solid #d9dbe0;
  border-radius:10px;font-size:15px;transition:.2s;
}
.form-group input:focus, .form-group textarea:focus, .form-group select:focus {
  outline:none;border-color:#f08979;box-shadow:0 0 0 3px rgba(240,137,121,.15);
}
.inline-2 {display:flex;gap:16px;}
.inline-2 .form-group {flex:1;margin-bottom:0;}
.action-bar {display:flex;gap:12px;justify-content:flex-end;margin-top:32px;}
</style>
<script>
function validateBungae() {
  const f = document.bungaeForm;
  if(!f.subject.value.trim()){alert('제목을 입력하세요');f.subject.focus();return false;}
  if(!f.startDate.value){alert('시작일시를 선택하세요');f.startDate.focus();return false;}
  if(!f.endDate.value){alert('종료일시를 선택하세요');f.endDate.focus();return false;}
  if(new Date(f.endDate.value) < new Date(f.startDate.value)){
      alert('종료일시는 시작일시 이후여야 합니다.');
      f.endDate.focus();return false;
  }
  if(!f.place.value.trim()){alert('장소를 입력하세요');f.place.focus();return false;}
  if(!f.capacity.value || parseInt(f.capacity.value) < 1){
      alert('정원은 1 이상');f.capacity.focus();return false;
  }
  return true;
}
</script>
</head>
<body>
<header class="header">
  <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<main class="main">
  <div class="form-wrap">
    <h2>번개모임 생성 😍</h2>
	<br>
    <form name="bungaeForm" method="post"
          action="${pageContext.request.contextPath}/bungaeMeeting/bungaeMeetingCreate"
          onsubmit="return validateBungae();">
      <div class="form-group">
        <label for="subject">제목</label>
        <input type="text" name="subject" id="subject" maxlength="100">
      </div>

      <div class="inline-2">
        <div class="form-group">
      	  <p>
          <label for="startDate">시작 일시</label>
          <input type="datetime-local" name="startDate" id="startDate">
          </p>
        </div>
        <div class="form-group">
        
          <label for="endDate">종료 일시</label>
          <input type="datetime-local" name="endDate" id="endDate">
        </div>
      </div>

      <div class="form-group">
        <label for="place">장소</label>
        <input type="text" name="place" id="place" maxlength="150">
      </div>

      <div class="inline-2">
        <div class="form-group">
          <p>
          <label for="capacity">정원</label>
          <input type="number" name="capacity" id="capacity" min="1" value="10">
          </p>
        </div>
        <div class="form-group">
          <label for="categoryIdx">종목</label>
          <select name="categoryIdx" id="categoryIdx">
            <c:forEach var="c" items="${sportCategoryList}">
              <option value="${c.sportIdx}">${c.sportName}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label for="regionIdx">지역</label>
          <select name="regionIdx" id="regionIdx">
            <c:forEach var="r" items="${regionCategoryList}">
              <option value="${r.regionIdx}">${r.regionName}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea name="content" id="content" rows="6" maxlength="2000" style="resize:none";></textarea>
      </div>

      <div class="action-bar">
        <button type="button" class="btn btn-outline btn-large"
                onclick="history.back()">취소</button>
        <button type="submit" class="btn btn-primary btn-large">생성</button>
      </div>
    </form>
  </div>
</main>
<footer class="footer">
  <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>
<jsp:include page="/WEB-INF/views/layout/footerResources.jsp"/>
</body>
</html>
