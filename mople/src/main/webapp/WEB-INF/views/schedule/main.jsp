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
.radio-btn input[type="radio"]:checked+span {
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

.game-list {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.game-row {
	display: grid;
	grid-template-columns: 80px 100px 1fr 80px 1fr 60px;
	align-items: center;
	gap: 10px;
	padding: 10px 0;
	border-bottom: 1px solid #ccc;
}

.team {
	display: flex;
	align-items: center;
	gap: 8px;
	font-weight: bold;
}

.team-logo {
	width: 28px;
	height: 28px;
	object-fit: contain;
}

.team-name {
	font-size: 16px;
}

.game-score {
	font-size: 18px;
	font-weight: bold;
	text-align: center;
}

.score {
	display: inline-block;
	width: 20px;
}

.vs {
	font-weight: bold;
	color: #555;
}

.game-time, .game-place, .game-state {
	font-size: 14px;
	color: #888;
	text-align: center;
}

.game-score {
	font-size: 18px;
	font-weight: bold;
	text-align: center;
	min-width: 60px; /* 스코어 없는 상태에서도 자리 유지 */
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
			<form action="/schdule/matcharea">
				<section class="sport-selection">
					<label class="radio-btn"> <input type="radio" name="sports"
						value="kbaseball" checked> <span>야구</span>
					</label> <label class="radio-btn"> <input type="radio"
						name="sports" value="kfootball"> <span>축구</span>
					</label> <label class="radio-btn"> <input type="radio"
						name="sports" value="basketball"> <span>농구</span>
					</label> <label class="radio-btn"> <input type="radio"
						name="sports" value="volleyball"> <span>배구</span>
					</label> <br> <br>
				</section>
				<div class="monthPageing">
					<ul class="month-list">
						<li data-month="02">2월</li>
						<li data-month="03">3월</li>
						<li data-month="04">4월</li>
						<li data-month="05">5월</li>
						<li data-month="06">6월</li>
						<li data-month="07" class="active">7월</li>
						<li data-month="08">8월</li>
						<li data-month="09">9월</li>
						<li data-month="10">10월</li>
						<li data-month="11" class="disabled">11월</li>
						<li data-month="12" class="disabled">12월</li>
						<li data-month="1" class="disabled">1월</li>
					</ul>
					<input type="hidden" name="month" id="selectedMonth" value="7">
				</div>
			</form>

			<section class="schedule-area">
			
			</section>
		</main>
	</div>
	<script type="text/javascript">
function sendAjaxRequest(url, method, requestParams, responseType, fn) {
	const settings = {
			type: method, 
			data: requestParams,
			dataType: responseType,
			success: function(data) {
				fn(data);
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader('AJAX', true);
			},
			complete: function () {
			},
			error: function(xhr) {
				if(xhr.status === 403) {
					login();
					return false;
				} else if(xhr.status === 406) {
					alert('요청 처리가 실패 했습니다.');
					return false;
		    	}
		    	
				console.log(xhr.responseText);
			}
	};
	
	$.ajax(url, settings);
}
document.querySelectorAll(".month-list li").forEach(item => {
    item.addEventListener("click", () => {
        const month = item.dataset.month;
        document.getElementById("selectedMonth").value = month;
        let url = '${pageContext.request.contextPath}/schdule/matcharea.jsp';
        
        let params={}
        const fn = function(data){
        	
        }
        
        sendAjaxRequest(url,'post',params,'json',fn);
    });
});

</script>
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
</body>
</html>