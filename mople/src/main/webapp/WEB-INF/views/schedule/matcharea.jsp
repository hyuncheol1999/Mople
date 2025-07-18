<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="match-container">
  <c:forEach var="entry" items="${map}">
    <c:set var="date" value="${entry.key}" />
    <c:set var="games" value="${entry.value}" />

    <div class="day-section">
      <div class="day-header">${date}</div>
      <c:forEach var="game" items="${games}">
        <div class="match-row">
          <div class="match-time">${game.time} | ${game.place}</div>
          <div class="match-teams">
            <div class="team-block">
              <c:if test="${not empty game.logo2}">
                <img src="${game.logo2}" alt="Home Logo" class="team-logo" />
              </c:if>
              <div class="team-name">${game.home}</div>
            </div>

            <div class="match-score">
              <c:choose>
                <c:when test="${not empty game.homeScore && not empty game.awayScore}">
                  ${game.homeScore} : ${game.awayScore}
                </c:when>
                <c:otherwise>
                  -
                </c:otherwise>
              </c:choose>
            </div>

            <div class="team-block">
              <c:if test="${not empty game.logo1}">
                <img src="${game.logo1}" alt="Away Logo" class="team-logo" />
              </c:if>
              <div class="team-name">${game.away}</div>
            </div>
          </div>
          <div class="match-status">${game.state}</div>
        </div>
      </c:forEach>
    </div>
  </c:forEach>
</div>