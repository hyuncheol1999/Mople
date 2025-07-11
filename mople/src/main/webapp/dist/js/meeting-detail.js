showTabAjax('meetingHome');

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

function showTabAjax(tabName) {
    const url = `${contextPath}/meeting/${tabName}`;
    sendAjaxRequest(url,'GET',null,'html', function(data) {
            const meetingContentArea = document.querySelector(".meeting-content");
            if (meetingContentArea) {
                meetingContentArea.innerHTML = data;
            }
        },
        false 
    );
}