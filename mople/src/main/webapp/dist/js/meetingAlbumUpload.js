document.addEventListener('DOMContentLoaded', () => {
	// 파일 입력 필드 변경 시 파일명 표시
	const imageInput = document.getElementById('meetingAlbumImage'); // <input type='file' ...>
	const fileNameDisplay = document.getElementById('fileNameDisplay');
	imageInput.addEventListener('change', (event) => {
		if (event.target.files.length > 0) {
			fileNameDisplay.textContent = event.target.files[0].name;
		} else {
			fileNameDisplay.textContent = '선택된 파일 없음';
		}
	});

	// 파일 입력 필드 클릭 시
	document.querySelector('#fileNameDisplay').addEventListener('click', (event) => {
		// input click 트리거
		imageInput.click();
	});
	
	// 폼 제출 시 유효성 검사
	const albumForm = document.albumForm;
	albumForm.addEventListener('submit', (event) => {
		event.preventDefault();

		// 사진 검사
		const meetingAlbumImage = albumForm.meetingAlbumImage;
		const meetingAlbumImageError = document.getElementById('meetingAlbumImageError');
		if (meetingAlbumImage.value === '') {
			meetingAlbumImageError.classList.add('active');
			albumForm.meetingAlbumImage.focus();
			return;
		} else {
			meetingAlbumImageError.classList.remove('active');
		}


		albumForm.submit();
	});
});