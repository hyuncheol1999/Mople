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
      background-color: #f5f7fa;
      margin: 0;
      padding: 0;
    }

    .main {
      flex: 1;
      padding: 140px 20px 60px;
    }

    .sport-selection {
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      gap: 12px;
      margin-bottom: 30px;
    }

    .radio-btn {
      border: 2px solid #333;
      border-radius: 24px;
      padding: 10px 20px;
      background-color: #fff;
      cursor: pointer;
      transition: 0.2s ease-in-out;
      font-size: 15px;
      font-weight: 500;
      color: #333;
    }

    .radio-btn input[type="radio"] {
      display: none;
    }

    .radio-btn input[type="radio"]:checked + span {
      background-color: #333;
      color: #fff;
      padding: 6px 14px;
      border-radius: 18px;
    }

    .monthPageing {
      display: flex;
      justify-content: center;
      margin-bottom: 40px;
    }

    .month-list {
      list-style: none;
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      padding: 0;
      margin: 0;
    }

    .month-list li {
      padding: 10px 18px;
      border-radius: 20px;
      background-color: #ddd;
      cursor: pointer;
      font-size: 15px;
      font-weight: 500;
      transition: all 0.3s ease;
    }

    .month-list li:hover {
      background-color: #444;
      color: white;
    }

    .month-list li.active {
      background-color: #222;
      color: white;
      border: 2px solid #444;
    }

    .month-list li.disabled {
      background-color: #eee;
      color: #aaa;
      pointer-events: none;
    }
    .match-container {
  padding: 24px;
  background-color: #f7f9fc;
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.day-section {
  background: #fff;
  border-radius: 10px;
  padding: 16px 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.day-header {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  border-bottom: 1px solid #e5e5e5;
  padding-bottom: 8px;
  margin-bottom: 16px;
}

.match-row {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.match-time {
  font-size: 14px;
  color: #888;
}

.match-teams {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.team-block {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 120px;
}

.team-logo {
  width: 28px;
  height: 28px;
  object-fit: contain;
}

.team-name {
  font-size: 15px;
  font-weight: 500;
  color: #222;
}

.match-score {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  text-align: center;
  min-width: 60px;
}

.match-status {
  font-size: 13px;
  color: #666;
  text-align: right;
}
  </style>
</head>
<body>
  <div class="wrap">
    <header class="header">
      <jsp:include page="/WEB-INF/views/layout/header.jsp" />
    </header>

    <main class="main">
      <form action="/schedule/matcharea">
        <section class="sport-selection">
          <label class="radio-btn">
            <input type="radio" name="sports" value="kbaseball" checked> <span>야구</span>
          </label>
          <label class="radio-btn">
            <input type="radio" name="sports" value="kfootball"> <span>축구</span>
          </label>
          <label class="radio-btn">
            <input type="radio" name="sports" value="basketball"> <span>농구</span>
          </label>
          <label class="radio-btn">
            <input type="radio" name="sports" value="volleyball"> <span>배구</span>
          </label>
          <input type="hidden" id="selectedSports" name="sports" value="kbaseball">
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

      <section class="schedule-area"></section>
    </main>

    <footer class="footer">
      <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
    </footer>
    <jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
  </div>

  <script>
    function sendAjaxRequest(url, method, requestParams, responseType, fn) {
      $.ajax({
        type: method,
        url: url,
        data: requestParams,
        dataType: responseType,
        beforeSend: function(xhr) {
          xhr.setRequestHeader('AJAX', true);
        },
        success: fn,
        error: function(xhr) {
          if(xhr.status === 403) {
            alert('권한이 없습니다.');
          } else {
            console.log(xhr.responseText);
          }
        }
      });
    }

    document.querySelectorAll(".month-list li").forEach(item => {
      item.addEventListener("click", () => {
        const month = item.dataset.month;
        document.getElementById("selectedMonth").value = month;
        const sports = document.querySelector("input[name='sports']:checked").value;

        const url = '${pageContext.request.contextPath}/schedule/matcharea';
        const params = { month: month, sports: sports };

        sendAjaxRequest(url, 'post', params, 'html', data => {
          document.querySelector(".schedule-area").innerHTML = data;
        });
      });
    });
  </script>
</body>
</html>