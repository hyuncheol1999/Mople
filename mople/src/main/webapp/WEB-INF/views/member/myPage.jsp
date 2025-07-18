<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/meeting.css"
	type="text/css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/myPage.css"
	type="text/css">
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/myPage-nav.css"
	type="text/css">
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />
		<main class="main">
			<div class="container">
				<div class="body-container row justify-content-center">
					<div class="col-md-10 my-3 p-3">

						<div class="body-title mb-4">
							<h3>마이페이지</h3>
						</div>

						<div class="body-main">
							<div class="profile-section">
								<img
									src=
									<c:choose>					
										<c:when test="${not empty dto.profilePhoto}">						
											'${pageContext.request.contextPath}/uploads/member/${dto.profilePhoto}'
										</c:when>
										<c:otherwise>
											'${pageContext.request.contextPath}/dist/images/defaultPerson.png'	
										</c:otherwise>
									</c:choose>
									class="profile-avatar" alt="프로필 사진">
									
								<div class="profile-info">
									<h4>${dto.userNickName}(${dto.userName})</h4>
									<p>${dto.userId}| ${dto.email}</p>
									<p>${dto.gender == 0 ? '남자' : '여자'}
										|
										<fmt:parseDate value="${dto.birth}" pattern="yyyy-MM-dd"
											var="birthDate" />
										<fmt:formatDate value="${birthDate}" pattern="yyyy년 MM월 dd일" />
									</p>
									<button type="button"
										class="btn btn-small btn-outline"
										onclick="location.href='${pageContext.request.contextPath}/member/update';">정보
										수정</button>
								</div>
							</div>

						<ul class="mypage-nav" id="myPageTab">
							<li class="nav-item">
								<a class="nav-link active" id="my-groups-tab" data-target="#my-groups">내 모임</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" id="my-meetups-tab" data-target="#my-meetups">내 정모</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" id="my-posts-tab" data-target="#my-posts">내 게시글/댓글</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" id="my-albums-tab" data-target="#my-albums">내 사진첩</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" id="settings-tab" data-target="#settings">설정</a>
							</li>
						</ul>

							<div class="tab-content" id="myPageTabContent">
								<div class="tab-pane fade show active" id="my-groups"
									role="tabpanel" aria-labelledby="my-groups-tab">
									<h4>내가 가입한 모임</h4>
									<p>수빈님이 현재 활동 중인 모임 목록입니다.</p>

									<c:choose>
										<c:when test="${not empty myGroups}">
											<div class="row row-cols-1 row-cols-md-2 g-4">
												<c:forEach var="group" items="${myGroups}">
													<div class="col">
														<div class="card h-100">
															<img
																src="${pageContext.request.contextPath}/uploads/group/${group.groupPhoto}"
																class="card-img-top" alt="${group.groupName}"
																style="height: 180px; object-fit: cover;">
															<div class="card-body">
																<h5 class="card-title">${group.groupName}</h5>
																<p class="card-text text-muted">${group.sportsType}
																	| ${group.location}</p>
																<p class="card-text">${group.groupDescription}</p>
																<a
																	href="${pageContext.request.contextPath}/group/view?groupId=${group.groupId}"
																	class="btn btn-sm btn-primary">모임 상세 보기</a>
															</div>
															<div class="card-footer">
																<small class="text-muted">멤버 수:
																	${group.memberCount}명</small>
															</div>
														</div>
													</div>
												</c:forEach>
											</div>
										</c:when>
										<c:otherwise>
											<div class="alert alert-info" role="alert">아직 가입된 모임이
												없습니다. 새로운 모임을 찾아보세요!</div>
											<a href="${pageContext.request.contextPath}/group/list"
												class="btn btn-primary">모임 찾아보기</a>
										</c:otherwise>
									</c:choose>
								</div>

								<div class="tab-pane fade" id="my-meetups" role="tabpanel"
									aria-labelledby="my-meetups-tab">
									<h4>내가 참여할 정모 / 내가 만든 정모</h4>
									<p>수빈님이 참여 예정이거나 생성한 정모 목록입니다.</p>

									<c:choose>
										<c:when test="${not empty myMeetups}">
											<div class="list-group">
												<c:forEach var="meetup" items="${myMeetups}">
													<a
														href="${pageContext.request.contextPath}/meetup/view?meetupId=${meetup.meetupId}"
														class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
														<div>
															<h5 class="mb-1">${meetup.meetupTitle}</h5>
															<small class="text-muted">${meetup.groupName} | <fmt:formatDate
																	value="${meetup.meetupDate}" pattern="yyyy.MM.dd HH:mm" /></small>
														</div> <span class="badge bg-primary rounded-pill">${meetup.status}</span>
													</a>
												</c:forEach>
											</div>
										</c:when>
										<c:otherwise>
											<div class="alert alert-info" role="alert">예정된 정모가
												없습니다. 모임에 가입하고 정모에 참여해보세요!</div>
											<a href="${pageContext.request.contextPath}/meetup/list"
												class="btn btn-primary">정모 찾아보기</a>
										</c:otherwise>
									</c:choose>
								</div>

								<div class="tab-pane fade" id="my-posts" role="tabpanel"
									aria-labelledby="my-posts-tab">
									<h4>내가 작성한 게시글</h4>
									<p>수빈님이 커뮤니티 게시판에 작성한 글 목록입니다.</p>
									<c:choose>
										<c:when test="${not empty myPosts}">
											<ul class="list-group mb-4">
												<c:forEach var="post" items="${myPosts}">
													<li
														class="list-group-item d-flex justify-content-between align-items-center">
														<a
														href="${pageContext.request.contextPath}/board/view?boardId=${post.boardId}"
														class="text-decoration-none">${post.title}</a> <span
														class="badge bg-secondary">${post.views} 조회</span>
													</li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<div class="alert alert-info" role="alert">작성한 게시글이
												없습니다.</div>
										</c:otherwise>
									</c:choose>

									<h4>내가 작성한 댓글</h4>
									<p>수빈님이 작성한 댓글 목록입니다.</p>
									<c:choose>
										<c:when test="${not empty myComments}">
											<ul class="list-group">
												<c:forEach var="comment" items="${myComments}">
													<li class="list-group-item">
														<p class="mb-1">${comment.content}</p> <small
														class="text-muted">게시글: <a
															href="${pageContext.request.contextPath}/board/view?boardId=${comment.boardId}"
															class="text-decoration-none">${comment.postTitle}</a> | <fmt:formatDate
																value="${comment.regDate}" pattern="yyyy.MM.dd" /></small>
													</li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<div class="alert alert-info" role="alert">작성한 댓글이
												없습니다.</div>
										</c:otherwise>
									</c:choose>
								</div>

								<div class="tab-pane fade" id="my-albums" role="tabpanel"
									aria-labelledby="my-albums-tab">
									<h4>내가 업로드한 모임 사진</h4>
									<p>수빈님이 참여 모임 앨범에 올린 사진들입니다.</p>

									<c:choose>
										<c:when test="${not empty myPhotos}">
											<div class="row row-cols-2 row-cols-md-3 row-cols-lg-4 g-3">
												<c:forEach var="photo" items="${myPhotos}">
													<div class="col">
														<div class="card h-100">
															<img
																src="${pageContext.request.contextPath}/uploads/album/${photo.fileName}"
																class="card-img-top" alt="모임 사진"
																style="height: 150px; object-fit: cover;">
															<div class="card-body p-2">
																<small class="card-title text-truncate d-block">${photo.description != null && photo.description ne '' ? photo.description : '설명 없음'}</small>
																<small class="text-muted">${photo.groupName}</small>
															</div>
															<div class="card-footer p-2">
																<small class="text-muted"><fmt:formatDate
																		value="${photo.uploadDate}" pattern="yyyy.MM.dd" /></small>
															</div>
														</div>
													</div>
												</c:forEach>
											</div>
										</c:when>
										<c:otherwise>
											<div class="alert alert-info" role="alert">아직 업로드한 사진이
												없습니다.</div>
										</c:otherwise>
									</c:choose>
								</div>

								<div class="tab-pane fade" id="settings" role="tabpanel"
									aria-labelledby="settings-tab">
									<h4>계정 설정</h4>
									<p>계정 관련 설정을 관리할 수 있습니다.</p>
									<div class="list-group">
										<button type="button"
											class="list-group-item list-group-item-action"
											onclick="location.href='${pageContext.request.contextPath}/member/update';">
											개인 정보 수정</button>
										<button type="button"
											class="list-group-item list-group-item-action"
											onclick="location.href='${pageContext.request.contextPath}/member/changePwd';">
											비밀번호 변경</button>
										<button type="button"
											class="list-group-item list-group-item-action text-danger"
											onclick="confirmWithdrawal();">회원 탈퇴</button>
									</div>
								</div>
							</div>

						</div>

					</div>
				</div>

			</div>
		</main>
	</div>
	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />
    <script type="text/javascript">
        function confirmWithdrawal() {
            if (confirm('정말로 회원 탈퇴를 하시겠습니까? 탈퇴 시 모든 정보가 삭제되며 복구할 수 없습니다.')) {
                location.href = '${pageContext.request.contextPath}/member/withdrawal';
            }
        }
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/dist/js/myPage.js"></script>
</body>
</html>