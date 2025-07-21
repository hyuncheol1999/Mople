// 수정
function updateSchedule(regularMeetingIdx) {
  const url = `${contextPath}/meeting/regularMeetingCreate?mode=update&meetingIdx=${meetingIdx}&regularMeetingIdx=${regularMeetingIdx}`;
  window.location.href = url;
}

// 삭제
function deleteSchedule(regularMeetingIdx) {
  if (!confirm('정말로 이 일정을 삭제하시겠습니까?')) return;

  const form = document.createElement('form');
  form.method = 'post';
  form.action = `${contextPath}/meeting/regularMeetingDelete`;

  const meetingInput = document.createElement('input');
  meetingInput.type = 'hidden';
  meetingInput.name = 'meetingIdx';
  meetingInput.value = meetingIdx;
  form.appendChild(meetingInput);

  const scheduleInput = document.createElement('input');
  scheduleInput.type = 'hidden';
  scheduleInput.name = 'regularMeetingIdx';
  scheduleInput.value = regularMeetingIdx;
  form.appendChild(scheduleInput);

  document.body.appendChild(form);
  form.submit();
}

// 번개모임 전환
function changeBungae(regularMeetingIdx) {
  Swal.fire({
    title: '번개모임으로 전환하시겠습니까?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '전환',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      fetch(`${contextPath}/meeting/changeBungae`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `regularMeetingIdx=${regularMeetingIdx}`
      })
      .then(res => res.json())
      .then(json => {
        if (json.success) {
          Swal.fire('전환 완료', '정기모임이 번개모임으로 전환되었습니다.', 'success')
            .then(() => location.reload());
        } else {
          Swal.fire('오류', json.message || '전환에 실패했습니다.', 'error');
        }
      })
      .catch(() => {
        Swal.fire('오류', '네트워크 오류', 'error');
      });
    }
  });
}

