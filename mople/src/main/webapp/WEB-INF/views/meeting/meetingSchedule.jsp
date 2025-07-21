<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<c:set var="canInteract" value="${isLogin and meetingMember}" />
<div id="scheduleTab" class="tab-content">
	<div class="schedule-section">
		<div class="title">
			<h3>
				이번달 정모 일정
				<c:if test="${isLeader}">
					<button type="button" class="btn btn-outline btn-small"
						onclick="location.href='${pageContext.request.contextPath}/meeting/regularMeetingCreate?mode=create&meetingIdx=${meetingIdx}'">+</button>
				</c:if>
			</h3>
		</div>
		<c:if test="${empty scheduleList}">
			<p>📆 등록된 일정이 없습니다.</p>
		</c:if>
		<c:if test="${not empty scheduleList}">
			<div class="schedule-list">
				<c:forEach var="schedule" items="${scheduleList}">
					<c:set var="closed"
						value="${schedule.currentCnt >= schedule.capacity}" />
					<div class="schedule-item"
						data-schedule-id="${schedule.regularMeetingIdx}">
						<div class="schedule-date">
							<span class="day">${schedule.startDateDow}</span> 
							<span class="date">${schedule.startDateDay}</span>
						</div>
						<div class="schedule-info">
							<h4>${schedule.subject}</h4>
							<p>
								장소 : ${schedule.place}<br />
								<c:if test="${not empty schedule.startTimeStr}">
                    시간 : ${schedule.startTimeStr}
                    <c:if test="${not empty schedule.endTimeStr}">
                        ~ ${schedule.endTimeStr}
                    </c:if>
								</c:if>
							</p>
						</div>

						<span class="status ${closed ? 'closed' : 'open'}"> <c:choose>
								<c:when test="${closed}">모집완료</c:when>
								<c:otherwise>모집중</c:otherwise>
							</c:choose>
						</span>

						<div class="capacity-box">
							<span class="current-count">${schedule.currentCnt}</span> / <span
								class="max-count">${schedule.capacity}</span>
						</div>

						<div class="button-wrapper">
							<c:choose>
								<c:when test="${isLeader}">
									<button type="button"
										class="btn btn-outline btn-small update-btn"
										onclick="updateSchedule('${schedule.regularMeetingIdx}')">수정</button>
									<button type="button"
										class="btn btn-outline btn-small delete-btn"
										onclick="deleteSchedule('${schedule.regularMeetingIdx}')">삭제</button>
									<button type="button"
								      class="btn btn-outline btn-small bungae-btn"
								      onclick="changeBungae('${schedule.regularMeetingIdx}')">번개모임 전환</button>
									<c:choose>
										<c:when test="${schedule.joined}">
											<button type="button"
												class="btn btn-outline btn-small btn-join"
												onclick="cancelSchedule('${schedule.regularMeetingIdx}')">참여취소</button>
										</c:when>
										<c:otherwise>
											<button type="button"
												class="btn btn-outline btn-small btn-join"
												${closed ? 'disabled' : ''}
												onclick="joinSchedule('${schedule.regularMeetingIdx}')">참여</button>
										</c:otherwise>
									</c:choose>
								</c:when>
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
										onclick="Swal.fire('로그인 후 모임에 가입해야 참여가능','','info');">참여
										불가</button>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>

			</div>
		</c:if>
	</div>
</div>

<script>
	const contextPath = '${pageContext.request.contextPath}';
	const meetingIdx = '${meetingIdx}';
</script>