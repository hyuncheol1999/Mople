document.addEventListener('DOMContentLoaded', () => {

  const regularMeetingForm = document.regularMeetingForm;

  // 날짜 유효성 검사 함수
  function isValidDate(startDate) {
    if(startDate.length !== 8 && startDate.length !== 10) return false;

    let p = /(\.)|(\-)|(\/)/g;
    startDate = startDate.replace(p, "");

    let format = /^[12][0-9]{7}$/;
    if(! format.test(startDate)) return false;

    let y = parseInt(startDate.substring(0,4));
    let m = parseInt(startDate.substring(4,6));
    let d = parseInt(startDate.substring(6));

    if(m<1 || m>12) return false;

    let lastDay = (new Date(y,m,0)).getDate();
    if(d<1 || d>lastDay) return false;

    return true;
  }

  // 숫자 유효성 검사 함수
  function isNumber(capacity) {
    const regex = /^[0-9]+$/;
    return regex.test(capacity);
  }

  // 제출 이벤트 처리
  regularMeetingForm.addEventListener('submit', (event) => {
    // 정모명 검사
    const subject = regularMeetingForm.subject;
    const subjectError = document.getElementById('subjectError');
    if (subject.value.trim() === '') {
      event.preventDefault();
      subjectError.classList.add('active');
      subject.focus();
      Swal.fire('정모명을 입력해주세요.', '', 'warning');
      return;
    } else {
      subjectError.classList.remove('active');
    }

    // 정모날짜 검사
    const startDate = regularMeetingForm.startDate;
    if (!isValidDate(startDate.value.trim())) {
      event.preventDefault();
      startDate.focus();
      Swal.fire('정모 날짜 형식을 확인해주세요. (YYYY-MM-DD)', '', 'warning');
      return;
    }

    // 정모장소 검사
    const place = regularMeetingForm.place;
    if (place.value.trim() === '') {
      event.preventDefault();
      place.focus();
      Swal.fire('정모 장소를 입력해주세요.', '', 'warning');
      return;
    }

    // 인원수 검사
    const capacity = regularMeetingForm.capacity;
    if (!isNumber(capacity.value.trim())) {
      event.preventDefault();
      capacity.focus();
      Swal.fire('인원수는 숫자만 입력 가능합니다.', '', 'warning');
      return;
    }
  });

});
