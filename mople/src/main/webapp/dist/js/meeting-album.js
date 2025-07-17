function imageDetail(imgEL) {
	const contextPath = $(imgEL).attr('data-contextPath');
	const imageFileName = $(imgEL).attr('data-imageFileName');
	const content = $(imgEL).attr('data-content');
	const userNickName = $(imgEL).attr('data-userNickName');
	const uploadMemberIdx = $(imgEL).attr('data-memberIdx');
	const currUserIdx = $(imgEL).attr('data-currUserIdx');
	const userStatus = $(imgEL).attr('data-userStatus');
	
	document.querySelector('.modal-body .albumModal-userNickName').textContent = '작성자 : ' + userNickName;
	document.querySelector('.modal-body .albumModal-content').textContent = content;
	document.querySelector('.modal-body img').src = 
	  `${contextPath}/uploads/meetingAlbum/${imageFileName}`;
	
	document.querySelector('.modal-body .albumModal-userNickName').setAttribute('data-uploadMemberIdx', uploadMemberIdx);
	document.querySelector('.modal-body .albumModal-userNickName').setAttribute('data-currUserIdx', currUserIdx);
	
	document.querySelector('.modal-footer button').style.display = 'none';	
	// 모임장 / 작성자만 삭제 가능
	if(uploadMemberIdx === currUserIdx || userStatus === 'HOST') {
		document.querySelector('.modal-footer button').style.display = 'block';	
	}
	
	console.log(currUserIdx);
	
	// 모달 버튼 트리거
	$('#modalBtn').click();
}
