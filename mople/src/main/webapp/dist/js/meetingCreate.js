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
	
	// 카테고리 선택 (스포츠, 지역 공통 함수)
	function setupCategorySelection(categoryId, listId, hiddenInputId) {
		const searchInputEL = document.getElementById(categoryId);
		const categoryListEL = document.getElementById(listId);
		const hiddenInputEL = document.getElementById(hiddenInputId);
		const listItemsELS = categoryListEL.querySelectorAll('li');

		// 카테고리 선택 시 카테고리 목록 표시/숨김
		searchInputEL.addEventListener('click', () => {
			categoryListEL.style.display = categoryListEL.style.display === 'block' ? 'none' : 'block';
		});

		// 리스트 외부 클릭 시 숨김
		document.addEventListener('click', (event) => {
			if (!searchInputEL.contains(event.target) && !categoryListEL.contains(event.target)) {
				categoryListEL.style.display = 'none';
			}
		});

		// 카데고리 리스트 아이템 클릭 시 선택
		listItemsELS.forEach(item => {
			item.addEventListener('click', () => {
				const categoryNo = item.dataset.categoryNo;
				const categoryName = item.textContent;

				hiddenInputEL.value = categoryNo; // 히든에 번호 저장
				searchInputEL.value = categoryName; // 검색창에 이름 표시
				categoryListEL.style.display = 'none'; // 리스트 숨김

				// 유효성 검사 에러 메시지 제거 (선택 시)
				const errorDiv = document.getElementById(categoryId + 'Error');
				if (errorDiv) {
					errorDiv.textContent = '';
				}
			});
		});

		// 검색어를 직접 입력 시 리스트에 있는지 필터링
		searchInputEL.addEventListener('keyup', (event) => {
			const searchTerm = searchInputEL.value.trim();

			// 목록을 다시 보이게 함 (검색 시작 시)
			categoryListEL.style.display = 'block';

			// 모든 리스트 아이템을 순회하며 필터링
			listItemsELS.forEach(item => {
				const itemText = item.textContent.trim(); // 리스트 아이템 텍스트
				
				// 아이템 텍스트에 검색어가 포함되어 있으면
				if (itemText.includes(searchTerm)) { 
					item.style.display = 'block'; // 해당 아이템 보이기
				} else {
					item.style.display = 'none'; // 포함되어 있지 않으면 숨기기
				}
				
				// 검색어와 리스트 아이템이 일치하면
				if(itemText === searchTerm) {
					// 카테고리 번호 저장
					hiddenInputEL.value = item.dataset.categoryNo;
					hiddenInputEL.closest('.form-group').lastElementChild.classList.remove('active');
				} else {
					hiddenInputEL.value = '';
				}
				
			});

		});
	}

	// 스포츠 카테고리 설정
	setupCategorySelection('sportCategory', 'sportCategoryList', 'sportCategoryNo');
	// 지역 카테고리 설정
	setupCategorySelection('regionCategory', 'regionCategoryList', 'regionCategoryNo');

	// 폼 제출 시 유효성 검사
	const meetingForm = document.meetingForm;
	meetingForm.addEventListener('submit', (event) => {
		event.preventDefault();

		// 모임명 검사
		const meetingName = meetingForm.meetingName;
		const meetingNameError = document.getElementById('meetingNameError');
		if (meetingName.value.trim() === '') {
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
});