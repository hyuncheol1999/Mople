showTabAjax('meetingHome');

/*
function sendAjaxRequest(url, method, params, responseType, fn, file = false) {
	const settings = {
		type: method,
		data: params,
		dataType: responseType,
		success: function(data) {
			fn(data);
		},
		beforeSend: function(xhr) {
			// 로그인 필터에서 AJAX 요청인지 확인
			xhr.setRequestHeader('AJAX', true);
		},
		complete: function() {
		},
		error: function(xhr) {
			// 로그인 필터에서 로그인이 되어있지 않으면 403 에러를 던짐
			if(xhr.status === 403) {
				login();
				return false;
			} else if(xhr.status === 406) {
				alert('요청 처리가 실패했습니다.');
				return false;
			}
			
			console.log(xhr.responseText);
		}
	};
	
	if(file) {
		settings.processData = false;
		settings.contentType = false;
	}
	
	$.ajax(url, settings);
}
*/

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
function updateSchedule(id) {
  const ctx = document
               .querySelector('div.meeting-detail')
               .getAttribute('data-contextPath');

  Swal.fire({
    title: '일정을 수정하시겠습니까 ? ',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '수정',
    cancelButtonText: '취소'
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
            Swal.fire('수정 완료!', '', 'success')
                 .then(()=> refreshScheduleTab());  
          } else {
            Swal.fire('실패', '잠시 후 다시 시도해 주세요', 'error');
          }
        }
      );
  });
}

// 일정 삭제하기
function deleteSchedule(id) {
  const ctx = document
               .querySelector('div.meeting-detail')
               .getAttribute('data-contextPath');

  Swal.fire({
    title: '일정을 삭제하시겠습니까?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '삭제',
    cancelButtonText: '취소'
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
            Swal.fire('삭제 완료!', '', 'success')
                 .then(()=> refreshScheduleTab());  
          } else {
            Swal.fire('실패', '잠시 후 다시 시도해 주세요', 'error');
          }
        }
      );
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
