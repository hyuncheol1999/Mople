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