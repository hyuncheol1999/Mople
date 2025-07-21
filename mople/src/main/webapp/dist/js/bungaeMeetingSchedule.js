$(document).ready(function () {
  $('.join-btn').on('click', function (e) {
    e.preventDefault();

    const $button = $(this);
    const scheduleId = $button.data('schedule-id');
    const type = 'regular'; 
    const url = contextPath + '/meetingAll/join';

    Swal.fire({
      title: '이 모임에 참여하시겠습니까?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: '참여하기',
      cancelButtonText: '취소'
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          url: url,
          type: 'POST',
          data: { scheduleId, type },
          success: function (res) {
            if (res.status === 'joined') {
              Swal.fire('참여 완료!', '', 'success');
              $button.text('참여취소');
            } else if (res.status === 'cancelled') {
              Swal.fire('참여 취소되었습니다.', '', 'info');
              $button.text('참여');
            }

            // 인원 수 갱신
            const $countElement = $('#count-' + scheduleId);
            if ($countElement.length > 0 && res.currentCount !== undefined) {
              const text = $countElement.text();
              const totalMatch = text.match(/\/(\d+)명/);
              const total = totalMatch ? totalMatch[1] : '명';
              $countElement.text(`👥 ${res.currentCount}/${total}명`);
            }
          },
          error: function () {
            Swal.fire('오류가 발생했습니다.', '', 'error');
          }
        });
      }
    });
  });
});

