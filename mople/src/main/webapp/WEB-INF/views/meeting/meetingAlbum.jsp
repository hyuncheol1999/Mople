<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div id="photosTab" class="tab-content">
	<div class="photos-section">
		<div class="photos-header">
			<h3>사진첩</h3>
			<c:forEach var="dto" items="${memberOfMeetingList}" varStatus="status">
				<c:if test="${sessionScope.member.memberIdx==dto.memberIdx}">
					<button class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/meeting/meetingAlbumUpload?meetingIdx=${meetingIdx}'">사진 등록</button>
				</c:if>
			</c:forEach>
		</div>
		<div class="photos-grid">
		<c:forEach var="dto" items="${meetingAlbumList}" varStatus="status">
			<img src=
			'${pageContext.request.contextPath}/uploads/meetingAlbum/${dto.imageFileName}'
				onclick="imageDetail(this);" 
				data-contextPath='${pageContext.request.contextPath}'
				data-photoNum='${dto.photoNum}'
				data-imageFileName='${dto.imageFileName}'
				data-content='${dto.content}'
				data-meetingIdx='${dto.meetingIdx}'
				data-uploadMemberIdx='${dto.memberIdx}'
				data-userNickName='${dto.userNickName}'
				data-currUserIdx='${sessionScope.member.memberIdx}'
				class="photo"
			>
			<button id="modalBtn" type="button" hidden="" data-bs-toggle="modal" data-bs-target="#exampleModal"></button>
		</c:forEach>
		</div>
	</div>
</div>

<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered"> <div class="modal-content">
      <div class="modal-body text-center"> <img src='' class="albumPhotoInModal img-fluid rounded mb-3"> 
      <p class="albumModal-userNickName fw-bold"></p>
      <p class="albumModal-content text-break"></p> </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary btn-small" onclick="">수정</button>
      </div>
    </div>
  </div>
</div>