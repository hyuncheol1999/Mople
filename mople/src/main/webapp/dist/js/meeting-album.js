function imageDetail(imgEL) {
	const contextPath = $(imgEL).attr('data-contextPath');
	const photoNum = $(imgEL).attr('data-photoNum');
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
	$('.modal-body img').attr('data-photoNum', photoNum);
	$('.modal-body img').attr('data-contextPath', contextPath);

	document.querySelector('.modal-body .albumModal-userNickName').setAttribute('data-uploadMemberIdx', uploadMemberIdx);
	document.querySelector('.modal-body .albumModal-userNickName').setAttribute('data-currUserIdx', currUserIdx);

	document.querySelector('.modal-footer button').style.display = 'none';
	// 모임장 / 작성자만 삭제 가능
	if (uploadMemberIdx === currUserIdx || userStatus === 'HOST') {
		document.querySelector('.modal-footer button').style.display = 'block';
	}

	// 모달 버튼 트리거
	$('#modalBtn').click();
	handleImageErrors('.modal-body img');
}

function deleteImage(btnEL) {
	const contextPath = $(btnEL).closest('.modal-dialog').find('.modal-body').find('img').attr('data-contextPath');
	const photoNum = $(btnEL).closest('.modal-dialog').find('.modal-body').find('img').attr('data-photoNum');

	Swal.fire({
		title: '사진을 삭제하시겠습니까?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '삭제',
		cancelButtonText: '닫기'
	}).then(r => {
		if (!r.isConfirmed) return;

		$.ajax({
			url: 'albumImageDelete',
			type: 'POST',
			data: { photoNum: photoNum },
			dataType: 'json',
			success: function(response) {
				if (response.status === 'success') {
					$('#modalBtn').click();
					Swal.fire('사진을 삭제하였습니다.', '', 'success')
						.then(() => window.location.href = `${contextPath}/meeting/meetingDetail?page=1&sportCategory=0&regionCategory=0&sortBy=latest&meetingIdx=${meetingIdx}`);
				} else {
					Swal.fire('실패', '잠시 후 다시 시도해 주세요', 'error');
				}
			},
			error: function(xhr, status, error) {
				console.log('AJAX error', xhr.status, xhr.responseText);
				if (xhr.status === 403) { login(); return; }
				Swal.fire('요청 실패', '', 'error');
			}
		});
	});

}