<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>모플 - 운동으로 만나다</title>
  <jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/meeting.css" type="text/css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <style>
    .main {
      padding: 30px;
      max-width: 900px;
      margin: auto;
    }
    .bungaeMeeting-header h3 {
      font-size: 1.8rem;
      border-bottom: 2px solid #ddd;
      padding-bottom: 10px;
      margin-bottom: 20px;
    }
    .BungaeMeeting-info p {
      margin: 5px 0;
      color: #444;
      font-size: 1.05rem;
    }
    .members-section h3 {
      margin-top: 40px;
      font-size: 1.5rem;
      border-bottom: 2px solid #eee;
      padding-bottom: 10px;
    }
    .members-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      margin-top: 20px;
    }
    .member-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 120px;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 10px;
      box-shadow: 0 2px 5px rgba(0,0,0,0.05);
      background: #fff;
    }
    .member-card img {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      object-fit: cover;
      margin-bottom: 8px;
    }
    .managerLogo {
      width: 20px;
      margin-top: 5px;
    }
    .reply {
      margin-top: 40px;
    }
    .form-header {
      font-size: 1rem;
      margin-bottom: 10px;
      color: #666;
    }
    .reply-form textarea {
      width: 100%;
      height: 80px;
      resize: none;
      border: 1px solid #ccc;
      border-radius: 5px;
      padding: 10px;
      font-size: 1rem;
    }
    .reply-card {
      background: #fff;
      border: 1px solid #e0e0e0;
      padding: 16px;
      margin-bottom: 16px;
      border-radius: 10px;
      box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    }
    .reply-header {
      display: flex;
      justify-content: space-between;
      font-weight: bold;
      margin-bottom: 8px;
    }
    .reply-content {
      margin-bottom: 10px;
      color: #444;
    }
    .reply-actions button {
      margin-right: 10px;
      padding: 6px 12px;
      font-size: 0.85rem;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    .btn-delete {
      background-color: #ffe6e6;
    }
    .btn-approve {
      background-color: #e6f7ff;
    }
  
  </style>
  <script>
    const contextPath = '${pageContext.request.contextPath}';
    const bungaeMeetingIdx = '${bungaeMeetingIdx}';
  </script>
</head>
<body>
  <header class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />
  </header>
  <jsp:include page="/WEB-INF/views/layout/login.jsp" />
  <div class="main">
    <div class="bungaeMeeting-detail" data-contextPath="${pageContext.request.contextPath}" data-bungaeMeetingIdx="${bungaeMeetingIdx}">
      <div class="bungaeMeeting-header">
        <h3>번개모임 소개</h3>
        <div class="BungaeMeeting-info">
          <p><strong>제목:</strong> ${subject}</p>
          <p><strong>날짜:</strong> ${fn:replace(startDate, 'T', ' ')}</p>
          <p><strong>내용:</strong> ${content}</p>
        </div>
      </div>
    </div>

    <div class="reply">
      <form name="replyForm" method="post">
        <div class="form-header">
          <span class="bold">댓글</span> - 번개모임 참여를 원하시면 댓글을 남겨주세요.
        </div>
        <textarea name="content" class="form-control"></textarea>
        <div style="text-align:right; margin-top:10px">
          <button type="button" class="btn btn-light btnSendReply">댓글 등록</button>
        </div>
      </form>
    </div>

    <div id="listReply"></div>
  </div>

  <script>
    function loadReplyList(page) {
      $.ajax({
        url: contextPath + '/bungaeMeeting/replyList',
        method: 'GET',
        data: { bungaeMeetingIdx, pageNo: page },
        success: data => $('#listReply').html(data),
        error: () => $('#listReply').html('<div>댓글을 불러오는 데 실패했습니다.</div>')
      });
    }

    $(function () {
      loadReplyList(1);
      $('.btnSendReply').click(function () {
        const content = $('textarea[name="content"]').val().trim();
        if (!content) return Swal.fire('댓글을 입력해주세요.', '', 'warning');

        $.post(contextPath + '/bungaeMeeting/insertReply', { bungaeMeetingIdx, content })
          .done(res => {
            if (res.state === 'true') {
              $('textarea[name="content"]').val('');
              loadReplyList(1);
            } else {
              Swal.fire('댓글 등록 실패', '', 'error');
            }
          });
      });
    });

    $(document).on("click", ".btn-delete", function () {
      const replyNum = $(this).data("id");
      if (!confirm("정말 삭제하시겠습니까?")) return;

      fetch(contextPath + "/bungaeMeeting/deleteReply", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ replyNum })
      })
      .then(res => res.json())
      .then(data => {
        if (data.state === "true") {
          alert("삭제되었습니다.");
          loadReplyList(1);
        } else {
          alert("삭제 실패");
        }
      });
    });

    $(document).on("click", ".btn-approve", function () {
      const memberIdx = $(this).data("id");
      const bungaeMeetingIdx = $(this).data("meetingid");

      if (!confirm("이 사용자를 승인하시겠습니까?")) return;

      fetch(contextPath + "/bungaeMeeting/approve", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({
          bungaeMeetingIdx,
          memberIdx
        })
      })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          alert("승인되었습니다.");
          loadReplyList(1);
        } else {
          alert("승인 실패");
        }
      });
    });
  </script>
</body>
</html>
