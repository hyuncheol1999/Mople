<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:forEach var="entry" items="${map}">
					<div class="schedule-box">
						<!-- 날짜 (key) 출력 -->
						<h3 class="schedule-date">${entry.key}</h3>

						<c:choose>
							<c:when test="${not empty entry.value}">
								<div class="game-list">
									<c:forEach var="game" items="${entry.value}">
										<div class="game-row">
											<!-- 경기 시간 -->
											<div class="game-time">${game.time}</div>

											<!-- 경기 장소 -->
											<div class="game-place">${game.place}</div>

											<!-- 어웨이팀 -->
											<div class="team">
												<img src="${game.logo1}" alt="${game.away}"
													class="team-logo" /> <span class="team-name">${game.away}</span>
											</div>

											<!-- 스코어 (종료일 때만 표시) -->
											<div class="game-score">
												<c:if test="${game.state eq '종료'}">
													<span class="score">${game.awayScore}</span>
									:
									<span class="score">${game.homeScore}</span>
												</c:if>
											</div>

											<!-- 홈팀 -->
											<div class="team">
												<img src="${game.logo2}" alt="${game.home}"
													class="team-logo" /> <span class="team-name">${game.home}</span>
											</div>

											<!-- 경기 상태 -->
											<div class="game-state">${game.state}</div>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<p style="text-align: center;">경기 일정이 없습니다.</p>
							</c:otherwise>
						</c:choose>
					</div>
				</c:forEach>