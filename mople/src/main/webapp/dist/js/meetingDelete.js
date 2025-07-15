document.addEventListener('DOMContentLoaded', () => {
	// 카테고리 선택
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

	// 멤버 리스트 설정
	setupCategorySelection('memberList', 'regionCategoryList', 'regionCategoryNo');

	// 폼 제출
	const meetingForm = document.deleteForm;
	meetingForm.addEventListener('submit', (event) => {
		event.preventDefault();

		meetingForm.submit();
	});
});	
	
// 차기 모임장으로 선택된 멤버 가져오기
function getSelectedRadioValue(radioName) {
    const radios = document.querySelectorAll(`input[name="\${radioName}"]`);
    let selectedValue = '';
    radios.forEach(radio => {
        if (radio.checked) {
            selectedValue = radio.value;
        }
    });
    return selectedValue;
}

function deleteMeeting() {
	const memberIdx = getSelectedRadioValue('sportCategory');
	console.log(memberIdx);
	
}