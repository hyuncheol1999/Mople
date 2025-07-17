<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="filter-section">
	<span class="title">카테고리</span>
	<label class="filter-item"> 
		<input name="sportCategory"
			type="radio" value="0" checked>
		<span>전체</span>
	</label> 
	<c:forEach var="sDto" items="${sportCategoryList}" varStatus="status">
	<label class="filter-item"> 
		<input name="sportCategory"
			type="radio" value="${sDto.sportIdx}"> 
		<span>${sDto.sportName}</span>
	</label> 
	</c:forEach>
	<span class="title">지역</span>
	<label class="filter-item"> 
		<input name="regionCategory"
			type="radio" value="0" checked>
		<span>전체</span>
	</label>
	<c:forEach var="rDto" items="${regionCategoryList}" varStatus="status">
	<label class="filter-item"> 
		<input name="regionCategory"
			type="radio" value="${rDto.regionIdx}">
		<span>${rDto.regionName}</span>
	</label>
	</c:forEach>
	<span class="title">정렬</span>
	<label class="filter-item"> 
		<input name="sortCategory"
			type="radio" value=latest checked>
			<span>최신순</span>
	</label>
	<label class="filter-item"> 
		<input name="sortCategory"
			type="radio" value="popular">
			<span>인기순</span>
	</label>
</div>

<script type="text/javascript">
$(function() {
	listPage(1);
	
    // 카테고리 변경 시 모임 리스트 새로 불러오기
    $('.filter-section').on('change', 'input[type=radio]', function() { // delegate 방식으로 변경
        listPage(1);
    });
});

function sendAjaxRequest(url, method, params, responseType, fn, file = false) {
	const settings = {
		type: method,
		data: params,
		dataType: responseType,
		success: function(data) {
			fn(data);
			handleImageErrors(); 
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

// 모임 리스트
function listPage(page) {
	let url = '${pageContext.request.contextPath}/meeting/meetingLayout';
	const sportCategory = getSelectedRadioValue('sportCategory');
    const regionCategory = getSelectedRadioValue('regionCategory');
    const sortBy = getSelectedRadioValue('sortCategory');
	
	let params = {page:page, sportCategory:sportCategory, regionCategory:regionCategory, sortBy:sortBy};
	
	const fn = function(data) {
		$('main .container').html(data);
	}
	
	sendAjaxRequest(url, 'get', params, 'text', fn);
}

// 카테고리 변경 시 모임 리스트 새로 불러오기
$(function() {
	$('.filter-section').on('change', $('input[type=radio]'), function() {
		listPage(1);
	});
});

// 선택된 카테고리 값 가져오기
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

// DB에 이미지 파일명은 존재하지만 서버에 실제 파일은 존재하지 않는 경우 처리
function handleImageErrors() {
    const defaultMeetingPhotoUrl = `\${contextPath}/dist/images/defaultMeetingProfilePhoto.png`;

    // 배경 divELS
    const meetingHeaders = document.querySelectorAll('.meeting-card-header');
    
    meetingHeaders.forEach(headerDiv => {
        const currentStyle = headerDiv.style.backgroundImage;
        const match = currentStyle.match(/url\(['"]?(.*?)['"]?\)/);
        const imageUrl = match ? match[1] : '';

        // URL 유효성 검사 && 이미 기본 이미지 경로가 아닌 경우에만 처리
        if (imageUrl && imageUrl !== defaultMeetingPhotoUrl) {
            const img = new Image();
            img.src = imageUrl;

            // 이미지 객체의 onerror 속성으로 확인
            img.onerror = function() {
                // console.warn(`이미지 로드 실패: ${imageUrl}. 기본 이미지로 대체`);
                headerDiv.style.backgroundImage = `url('\${defaultMeetingPhotoUrl}')`;
            };
        } else if (!imageUrl) { // URL 자체가 비어있는 경우
             // console.warn('background-image URL이 비어있습니다. 기본 이미지로 설정');
             headerDiv.style.backgroundImage = `url('\${defaultMeetingPhotoUrl}')`;
        }
    });
}
</script>