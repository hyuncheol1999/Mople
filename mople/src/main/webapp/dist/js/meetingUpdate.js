document.addEventListener('DOMContentLoaded', () => {
	// 파일 입력 필드 변경 시 파일명 표시
	const meetingImageInput = document.getElementById('meetingImage'); // <input type='file' ...>
	const fileNameDisplay = document.getElementById('fileNameDisplay');
	meetingImageInput.addEventListener('change', (event) => {
		if (event.target.files.length > 0) {
			fileNameDisplay.textContent = event.target.files[0].name;
		} else {
			fileNameDisplay.textContent = '선택된 파일 없음';
		}
	});

	// 파일 입력 필드 클릭 시
	document.querySelector('#fileNameDisplay').addEventListener('click', (event) => {
		// input click 트리거
		meetingImageInput.click();
	});
});
	

// 폼 제출 시 유효성 검사
const meetingForm = document.meetingForm;
meetingForm.addEventListener('submit', (event) => {
	event.preventDefault();

	// 모임명 검사
	const meetingName = meetingForm.meetingName;
	const meetingNameError = document.getElementById('meetingNameError');
	if (meetingName.value.trim() === '' || meetingName.value.length > 50) {
		console.log(meetingName.value.length);
		meetingNameError.classList.add('active');
		meetingName.focus();
		return;
	} else {
		meetingNameError.classList.remove('active');
	}

	// 모임 소개글 검사
	const meetingDesc = meetingForm.meetingDesc;
	const meetingDescriptionError = document.getElementById('meetingDescError');
	if (meetingDesc.value.trim() === '') {
		meetingDescriptionError.classList.add('active');
		meetingDesc.focus();
		return;
	} else {
		meetingDescriptionError.classList.remove('active');
	}

	// 스포츠 카테고리 검사
	const sportCategoryNo = meetingForm.sportCategoryNo;
	const sportCategoryError = document.getElementById('sportCategoryError');
	if (sportCategoryNo.value === '') {
		sportCategoryError.classList.add('active');
		meetingForm.sportIdx.focus();
		return;
	} else {
		sportCategoryError.classList.remove('active');
	}

	// 지역 카테고리 검사
	const regionCategoryNo = meetingForm.regionCategoryNo;
	const regionCategoryError = document.getElementById('regionCategoryError');
	if (regionCategoryNo.value === '') {
		regionCategoryError.classList.add('active');
		meetingForm.regionIdx.focus();
		return;
	} else {
		regionCategoryError.classList.remove('active');
	}

	meetingForm.submit();
});