<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<style type="text/css">
body {
	font-family: 'Noto Sans KR', sans-serif;
	margin: 0;
	padding: 0;
	background-color: #fff;
}

.wrap {
	display: flex;
	flex-direction: column;
	min-height: 100vh;
}

.main {
	flex: 1;
	padding: 180px 20px 40px; /* ← 상단 여백 확보 */
	text-align: center;
}

/* 종목 선택 버튼 */
.sport-selection {
	margin-bottom: 40px;
}

.sport-btn {
	border: 2px solid #444;
	background-color: white;
	border-radius: 25px;
	padding: 10px 30px;
	margin: 0 10px;
	font-size: 16px;
	cursor: pointer;
	transition: all 0.3s ease;
}

.sport-btn:hover {
	background-color: #444;
	color: #fff;
}

/* 경기 일정 영역 */
.schedule-area {
	display: flex;
	justify-content: center;
}

.schedule-box {
	border: 2px solid #444;
	border-radius: 16px;
	width: 80%;
	min-height: 200px; /* 최소 높이만 주고 */
	padding: 40px 20px; /* 내부 여백 */
	font-size: 18px;
	color: #555;
	background-color: #fafafa;
	/* 가변 높이용 설정 */
	display: flex;
	flex-direction: column; /* 내용이 세로로 쌓이도록 */
	align-items: center;
	justify-content: flex-start; /* 위에서부터 내용 시작 */
	gap: 20px; /* 항목 간 여백 */
}
.radio-btn {
    display: inline-flex;
    align-items: center;
    border: 2px solid #444;
    border-radius: 25px;
    padding: 10px 20px;
    margin: 0 10px;
    cursor: pointer;
    font-size: 16px;
    transition: all 0.3s ease;
    background-color: white;
}

.radio-btn input[type="radio"] {
    display: none;
}

.radio-btn span {
    pointer-events: none;
}

/* 선택된 항목 강조 */
.radio-btn input[type="radio"]:checked + span {
    color: #fff;
    background-color: #444;
    padding: 8px 16px;
    border-radius: 20px;
}

.sport-submit-btn {
    margin-top: 20px;
    padding: 10px 30px;
    font-size: 16px;
    border-radius: 25px;
    border: none;
    background-color: #444;
    color: #fff;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.sport-submit-btn:hover {
    background-color: #222;
}
</style>
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />

		<main class="main">
			<section class="sport-selection">
			    <form id="sportForm" action="/schedule/main" method="get">
			        <label class="radio-btn">
			            <input type="radio" name="sports" value="kbaseball" checked>
			            <span>야구</span>
			        </label>
			        <label class="radio-btn">
			            <input type="radio" name="sports" value="kfootball">
			            <span>축구</span>
			        </label>
			        <label class="radio-btn">
			            <input type="radio" name="sports" value="basketball">
			            <span>농구</span>
			        </label>
			        <label class="radio-btn">
			            <input type="radio" name="sports" value="volleyball">
			            <span>배구</span>
			        </label>
			        <br><br>
			        <button type="submit" class="sport-submit-btn">종목 선택</button>
			    </form>
			</section>

			<section class="schedule-area">
				<div class="schedule-box">
					경기일정
					<c:choose>
						<c:when test="${not empty list}">
							<c:forEach var="game" items="${list}">
								<div class="schedule-item">
									<div>
										<strong>시간:</strong> ${game.time}
									</div>
									<div>
										<strong>장소:</strong> ${game.place}
									</div>
									<div>
										<strong>경기:</strong> ${game.team1} vs ${game.team2}
									</div>
									<div>
										<strong>상태:</strong> ${game.status}
									</div>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<p style="text-align: center;">경기 일정이 없습니다.</p>
						</c:otherwise>
					</c:choose>
				</div>
			</section>
		</main>
	</div>
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>