// 처음 로딩
showTabAjax('meetingHome');

function sendAjaxRequest(url, method, params, responseType, fn, file=false){
  const settings = {
    url,                    
    type: method,
    data: params,
    dataType: responseType,
    success: fn,
    beforeSend: xhr => xhr.setRequestHeader('AJAX', true),
    error: xhr => {
      console.log('AJAX error', xhr.status, xhr.responseText);  
      if(xhr.status === 403) { login(); return; }
      Swal.fire('요청 실패', '', 'error');
    }
  };
  if(file){ settings.processData=false; settings.contentType=false; }                    
  $.ajax(settings);
}

function showTabAjax(tabName) {
    const contextPath = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');
    const meetingIdx = document.querySelector('div.meeting-detail').getAttribute('data-meetingIdx');

    const url = `${contextPath}/meeting/${tabName}?meetingIdx=${meetingIdx}`;
    sendAjaxRequest(url, 'GET', null, 'html', function(data) {
        const meetingContentArea = document.querySelector(".meeting-content");
        if (meetingContentArea) {
            meetingContentArea.innerHTML = data;
        }
		// 이미지 에러처리
		if (tabName === 'meetingHome') {
			handleImageErrors('.member-card img');
		} else if (tabName === 'meetingAlbum') {
			handleImageErrors('img.photo');
		}
    }, false);
	
}


// 일정 참여하기
function joinSchedule(id) {
  const ctx = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');

  Swal.fire({
    title: '정모에 참여하시겠습니까?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '참여',
    cancelButtonText: '취소'
	}).then(r=>{
	      if(!r.isConfirmed) return;
	      sendAjaxRequest(
	          ctx + '/schedule/join',
	          'POST',
	          { regularMeetingIdx:id },
	          'json',
	          res=>{
	              if(res && res.success){
	                  Swal.fire('참여 완료!','','success');
	                  refreshScheduleTab();  
	              }else{
	                  Swal.fire('실패','잠시 후 다시 시도해 주세요','error');
	              }
	          }
	      );
	  });
	}	
	
// 일정참여 취소하기
function cancelSchedule(id) {
  const ctx = document
               .querySelector('div.meeting-detail')
               .getAttribute('data-contextPath');

  Swal.fire({
    title: '참여를 취소하시겠습니까?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '취소하기',
    cancelButtonText: '닫기'
  }).then(r => {
      if (!r.isConfirmed) return;          

	  // 요청URL, 메소드, 파라미터, 응답타입, 콜백 순
      sendAjaxRequest(
        ctx + '/schedule/cancel',            
        'POST',                              
        { regularMeetingIdx: id },            
        'json',                              
        res => {                              
          if (res && res.success) {
            Swal.fire('취소 완료!', '', 'success')
                 .then(()=> refreshScheduleTab());  
          } else {
            Swal.fire('실패', '잠시 후 다시 시도해 주세요', 'error');
          }
        }
      );
  });
}

// 일정 수정하기
function updateSchedule(regularMeetingIdx) {
  Swal.fire({
    title: '정말 수정하시겠습니까?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '수정하기',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      const url = `${contextPath}/meeting/regularMeetingCreate?mode=update&meetingIdx=${meetingIdx}&regularMeetingIdx=${regularMeetingIdx}`;
      window.location.href = url;
    }
  });
}


// 일정 삭제하기
function deleteSchedule(regularMeetingIdx) {
  const contextPath = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');
  const meetingIdx   = document.querySelector('div.meeting-detail').getAttribute('data-meetingIdx');

  Swal.fire({
    title: '일정을 삭제할까요?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '삭제',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      fetch(`${contextPath}/meeting/regularMeetingDelete`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `meetingIdx=${meetingIdx}&regularMeetingIdx=${regularMeetingIdx}`
      })
      .then(response => {
        if (response.redirected) {
          window.location.href = response.url;
        } else {
          Swal.fire('삭제 완료!', '', 'success').then(() => {
            refreshScheduleTab();
          });
        }
      });
    }
  });
}



// 일정 탭 새로고침
function refreshScheduleTab() {
	showTabAjax('meetingSchedule');
}

// 모임 참여하기
function joinMeeting(meetingIdx) {
  const ctx = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');

  Swal.fire({
    title: '모임에 신청하시겠습니까?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '신청',
    cancelButtonText: '취소'
	}).then(r=>{
	      if(!r.isConfirmed) return;
	      sendAjaxRequest(
	          ctx + '/meeting/join',
	          'POST',
	          { meetingIdx:meetingIdx },
	          'json',
	          res=>{
	              if(res && res.success){
	                  Swal.fire('신청 완료!','','success')
	                  	.then(() => window.location.href = `${ctx}/meeting/meetingDetail?meetingIdx=${meetingIdx}`);  
	              }else{
	                  Swal.fire('실패','잠시 후 다시 시도해 주세요','error');
	              }
	          }
	      );
	  });
}

// 모임 승인
function approveMember(meetingIdx, memberIdx) {
  const ctx = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');

  Swal.fire({
    title: '신청을 승인하시겠습니까?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '승인',
    cancelButtonText: '취소'
	}).then(r=>{
	      if(!r.isConfirmed) return;
	      sendAjaxRequest(
	          ctx + '/meeting/approve',
	          'POST',
	          { meetingIdx:meetingIdx, memberIdx:memberIdx},
	          'json',
	          res=>{
	              if(res && res.success){
	                  Swal.fire('승인 완료!','','success')
	                  	.then(() => window.location.href = `${ctx}/meeting/meetingDetail?page=1&sportCategory=0&regionCategory=0&sortBy=latest&meetingIdx=${meetingIdx}`); 
	              }else{
	                  Swal.fire('실패','잠시 후 다시 시도해 주세요','error');
	              }
	          }
	      );
	  });
}

// 모임 거절
function rejectMember(meetingIdx, memberIdx) {
  const ctx = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');

  Swal.fire({
    title: '승인을 거절하시겠습니까?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '거절',
    cancelButtonText: '취소'
	}).then(r=>{
	      if(!r.isConfirmed) return;
	      sendAjaxRequest(
	          ctx + '/meeting/reject',
	          'POST',
	          { meetingIdx:meetingIdx, memberIdx:memberIdx},
	          'json',
	          res=>{
	              if(res && res.success){
	                  Swal.fire('모임 신청을 거절하였습니다.','','success')
	                  	.then(() => window.location.href = `${ctx}/meeting/meetingDetail?page=1&sportCategory=0&regionCategory=0&sortBy=latest&meetingIdx=${meetingIdx}`);  
	              }else{
	                  Swal.fire('실패','잠시 후 다시 시도해 주세요','error');
	              }
	          }
	      );
	  });
}

// 모임 탈퇴 - 모임원
function leaveMeeting(meetingIdx, memberIdx) {
  const ctx = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');

  Swal.fire({
    title: '정말 모임을 탈퇴하시겠어요?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '탈퇴',
    cancelButtonText: '취소'
	}).then(r=>{
	      if(!r.isConfirmed) return;
	      sendAjaxRequest(
	          ctx + '/meeting/reject',
	          'POST',
	          { meetingIdx:meetingIdx, memberIdx:memberIdx},
	          'json',
	          res=>{
	              if(res && res.success){
	                  Swal.fire('모임을 탈퇴하였습니다.','','success')
	                  	.then(() => window.location.href = `${ctx}/meeting/meetingDetail?page=1&sportCategory=0&regionCategory=0&sortBy=latest&meetingIdx=${meetingIdx}`);  
	              }else{
	                  Swal.fire('실패','잠시 후 다시 시도해 주세요','error');
	              }
	          }
	      );
	  });
}

// 모임 해체
function deleteMeeting(meetingIdx) {
  const ctx = document.querySelector('div.meeting-detail').getAttribute('data-contextPath');

  Swal.fire({
    title: '정말 모임을 해체하시겠어요?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '해체',
    cancelButtonText: '취소'
	}).then(r=>{
	      if(!r.isConfirmed) return;
	      sendAjaxRequest(
	          ctx + '/meeting/meetingDelete',
	          'POST',
	          { meetingIdx:meetingIdx},
	          'json',
	          res=>{
	              if(res && res.success){
	                  Swal.fire('모임을 해체하였습니다.','','success')
	                  	.then(() => window.location.href = `${ctx}/meeting/meetingList?page=1&sportCategory=0&regionCategory=0&sortBy=latest`);  
	              }else{
	                  Swal.fire('실패','잠시 후 다시 시도해 주세요','error');
	              }
	          }
	      );
	  });
}
