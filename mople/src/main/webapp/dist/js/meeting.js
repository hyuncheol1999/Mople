function sendOk() {
	const f = document.meetingForm;
	let str;
	
	str = f.meetingName.value;
	if(! str) {
		f.meetingName.focus();
		return;
	}
	
	str = f.meetingDesc.innerText;
	if(! str) {
		f.meetingDesc.focus();
		return;
	}
	
	str = f.sportCategory.value;
	if(! str) {
		f.sportCategory.focus();
		return;
	}
	
	str = f.regionCategory.value;
	if(! str) {
		f.regionCategory.focus();
		return;
	}
	
	const contextPath = document.getElementById('contextPath').value;
	const mode = document.getElementById('mode').value;
	
	f.action = `${contextPath}/meeting/${mode}`;
	f.submit();
}