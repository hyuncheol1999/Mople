// 모임 프로필 이미지 에러 처리
if(document.querySelector('img.group-avatar')) {
	handleImageErrors('img.group-avatar');
} else if(document.querySelector('img.profile-avatar')) {
	handleImageErrors('img.profile-avatar');	
}

// DB에 이미지 파일명은 존재하지만 서버에 실제 파일은 존재하지 않는 경우 처리
function handleImageErrors(targetEL) {
    // groupProfilePhoto ELS
	
    const imgELS = document.querySelectorAll(targetEL);
    imgELS.forEach(imgEL => { 
	    imgEL.onerror = function() {
			// 무한 루프 방지
			this.onerror = null;
			
			if(targetEL === 'img.photo' || targetEL === '.modal-body img') {
			    this.src = `${contextPath}/dist/images/defaultPhoto.png`;				
			} else if(targetEL === '.member-card img') {
			    this.src = `${contextPath}/dist/images/defaultPerson.png`;								
			} else {
			    this.src = `${contextPath}/dist/images/defaultMeetingProfilePhotoMini.png`;				
			}
		}
		
	});	

}