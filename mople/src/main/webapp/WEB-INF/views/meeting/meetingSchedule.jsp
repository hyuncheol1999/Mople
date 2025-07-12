<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!-- 로그인 + 모임멤버 이면 참여/참여취소 버튼 보여줌 아니면 참여불가버튼 보여줌-->
<c:set var="canInteract" value="${isLogin and meetingMember}" />
<div id="scheduleTab" class="tab-content">
	<div class="schedule-section">
		<h3>이번달 정모 일정</h3>

		<div class="schedule-list">
			<c:forEach var="schedule" items="${scheduleList}">
				<c:set var="closed"
					value="${schedule.currentCnt >= schedule.capacity}" />
				<fmt:parseDate value="${schedule.startDate}" pattern="yyyy-MM-dd"
					var="parsedStartDate" />

				<div class="schedule-item"
					data-schedule-id="${schedule.regularMeetingIdx}">

					<!-- 날짜 -->
					<div class="schedule-date">
						<span class="day"><fmt:formatDate
								value="${parsedStartDate}" pattern="EEE" /></span> <span class="date"><fmt:formatDate
								value="${parsedStartDate}" pattern="dd" /></span>
					</div>

					<!-- 정모정보(제목이랑 장소)-->
					<div class="schedule-info">
						<h4>${schedule.subject}</h4>
						<p>장소 : ${schedule.place}</p>
					</div>

					<!-- 모집상태 -->
					<span class="status ${closed ? 'closed' : 'open'}"> 
						<c:choose>
							<c:when test="${closed}">모집완료</c:when>
							<c:otherwise>모집중</c:otherwise>
						</c:choose>
					</span>

					<!-- 인원(현재인원수/정원수) -->
					<div class="capacity-box">
						<span class="current-count">${schedule.currentCnt}</span> / <span
							class="max-count">${schedule.capacity}</span>
					</div>


					<!-- 버튼 -->
					<div class="button-wrapper">
						<c:choose>
							<c:when test="${canInteract}">
								<c:choose>
									<c:when test="${schedule.joined}">
										<button type="button"
											class="btn btn-outline btn-small cancel-btn"
											onclick="cancelSchedule('${schedule.regularMeetingIdx}')">참여취소</button>
									</c:when>
									<c:otherwise>
										<button type="button"
											class="btn btn-outline btn-small join-btn"
											${closed ? 'disabled' : ''}
											onclick="joinSchedule('${schedule.regularMeetingIdx}')">참여</button>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<button class="btn btn-outline btn-small" disabled
									onclick="Swal.fire('로그인 후 모임에 가입해야 참여가능','','info');">
									참여 불가</button>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:forEach>

		</div>
	</div>
</div>