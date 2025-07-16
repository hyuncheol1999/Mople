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
