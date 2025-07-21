<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모플 - 운동으로 만나다</title>
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/home.css"
	type="text/css">
</head>
<body>
	<div class="wrap">
		<header class="header">
			<jsp:include page="/WEB-INF/views/layout/header.jsp" />
		</header>
		<jsp:include page="/WEB-INF/views/layout/login.jsp" />

		<main class="main">
			<section class="hero">
				<div class="container">
					<h1 class="hero-title">스포츠로 하나 되는 우리</h1>
					<p class="hero-subtitle">우리 동네 스포츠 모임에 참여하고, 새로운 친구를 사귀며 활기찬
						커뮤니티 활동을 시작하세요!</p>
					<div class="hero-actions">
						<a href="${pageContext.request.contextPath}/meeting/meetingList"
							class="btn btn-outline btn-large">모임 둘러보기</a>
						<button class="btn btn-outline btn-large">모임 시작하기</button>
					</div>
				</div>
			</section>

			<section class="popular-meetings">
				<div class="container">
					<h2 class="section-title">실시간 인기 모임</h2>

					<div id="popular-list" class="popular-slider">
						<c:forEach var="dto" items="${popularMeetings}">
							<div class="meeting-card">
								<a
									href="${pageContext.request.contextPath}/meeting/meetingDetail?meetingIdx=${dto.meetingIdx}">
									<c:choose>
										<c:when test="${empty dto.meetingProfilePhoto}">
											<img
												src="${pageContext.request.contextPath}/dist/images/defaultMeetingProfilePhoto.png" />
										</c:when>
										<c:otherwise>
											<img
												src="${pageContext.request.contextPath}/uploads/meeting/${dto.meetingProfilePhoto}"
												alt="${dto.meetingName}" />
										</c:otherwise>
									</c:choose>
								</a>
								<h3>
									<a
										href="${pageContext.request.contextPath}/meeting/meetingDetail?meetingIdx=${dto.meetingIdx}">
										${dto.meetingName} </a>
								</h3>
								<p>${dto.currentMembers}명 참여 중</p>

								<div class="meeting-tags">
									<span class="tag">#${dto.regionName}</span> <span class="tag">#${dto.sportName}</span>
								</div>
							</div>
						</c:forEach>
					</div>
					<!--
					<button id="btn-more" data-offset="4">더보기</button> -->
				</div>
			</section>


			<section class="features">
				<div class="container">
					<h2 class="section-title">모플을 선택해야 하는 이유?</h2>
					<div class="features-grid">
						<div class="feature-card">
							<div class="feature-icon">🏀</div>
							<h3>다양한 스포츠</h3>
							<p>농구, 축구, 야구, 테니스 등 다양한 종목의 모임을 만나보세요.</p>
						</div>
						<div class="feature-card">
							<div class="feature-icon">👥</div>
							<h3>내 주변 커뮤니티</h3>
							<p>우리 동네에서 함께 운동할 친구들을 쉽게 찾을 수 있어요.</p>
						</div>
						<div class="feature-card">
							<div class="feature-icon">⚡</div>
							<h3>번개처럼 빠른 만남</h3>
							<p>갑자기 운동이 하고 싶을 때, 번개 모임으로 즉시 게임에 참여하세요.</p>
						</div>
						<div class="feature-card">
							<div class="feature-icon">📅</div>
							<h3>간편한 일정 관리</h3>
							<p>모임 일정 등록부터 참여까지, 손쉽게 관리할 수 있습니다.</p>
						</div>
					</div>
				</div>
			</section>
		</main>
	</div>

	<footer class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<jsp:include page="/WEB-INF/views/layout/footerResources.jsp" />

	<script type="text/javascript">
	const contextPath = "${pageContext.request.contextPath}";

	$("#btn-more").on("click", function () {
		const btn = $(this);
		let offset = Number(btn.attr("data-offset")) || 0;

		$.ajax({
			url: contextPath + "/main/morePopularMeetings",
			method: "GET",
			data: { offset: offset, limit: 4 },
			success: function (res) {
				if (res.success && res.popularMeetings && res.popularMeetings.length > 0) {
					res.popularMeetings.forEach(dto => {
						const meetingIdx = dto.meetingIdx ?? 0;
						const meetingName = dto.meetingName ?? "이름 없음";
						const currentMembers = dto.currentMembers ?? 0;
						const profilePhoto = dto.meetingProfilePhoto;

						const imgSrc = profilePhoto
							? contextPath + "/uploads/meeting/" + profilePhoto
							: contextPath + "/dist/images/defaultMeetingProfilePhoto.png";

						const card = `
							<div class="meeting-card">
								<a href="${contextPath}/meeting/meetingDetail?meetingIdx=${meetingIdx}">
									<img src="${imgSrc}" alt="${meetingName}">
								</a>
								<h3>
									<a href="${contextPath}/meeting/meetingDetail?meetingIdx=${meetingIdx}">
										${meetingName}
									</a>
								</h3>
								<p>${currentMembers}명 참여 중</p>
							</div>
						`;

						$("#popular-list").append(card);
					});

					btn.attr("data-offset", offset + 4);
				} else {
					btn.hide();
				}
			},
			error: function () {
				alert("데이터 로딩 실패!");
			}
		});
	});
</script>

</body>
</html>
