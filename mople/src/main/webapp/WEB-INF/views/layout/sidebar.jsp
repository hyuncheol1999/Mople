<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="filter-section">
	<span class="title">카테고리</span>
	<label class="filter-item"> <input name="sportCategory"
		type="radio" value="all" checked onchange="filterMeetings()">
		<span>📑&nbsp;&nbsp;전체</span>
	</label> <label class="filter-item"> <input name="sportCategory"
		type="radio" value="soccer" onchange="filterMeetings()"> <span>⚽&nbsp;&nbsp;&nbsp;축구</span>
	</label> <label class="filter-item"> <input name="sportCategory"
		type="radio" value="baseball" onchange="filterMeetings()"> <span>⚾&nbsp;&nbsp;&nbsp;야구</span>
	</label> <label class="filter-item"> <input name="sportCategory"
		type="radio" value="basketball" onchange="filterMeetings()"> <span>🏀&nbsp;&nbsp;농구</span>
	</label> <label class="filter-item"> <input name="sportCategory"
		type="radio" value="tennis" onchange="filterMeetings()"> <span>🎾&nbsp;&nbsp;테니스</span>
	</label> <label class="filter-item"> <input name="sportCategory"
		type="radio" value="volleyball" onchange="filterMeetings()"> <span>🏐&nbsp;&nbsp;배구</span>
	</label> 
	<label class="filter-item"> <input name="sportCategory"
		type="radio" value="cycling" onchange="filterMeetings()"> <span>🚴&nbsp;&nbsp;스키/보드</span>
	</label>
	<label class="filter-item"> <input name="sportCategory"
		type="radio" value="cycling" onchange="filterMeetings()"> <span>🚴&nbsp;&nbsp;골프</span>
	</label>
	<label class="filter-item"> <input name="sportCategory"
		type="radio" value="cycling" onchange="filterMeetings()"> <span>🚴&nbsp;&nbsp;클라이밍</span>
	</label>
	<label class="filter-item"> <input name="sportCategory"
		type="radio" value="cycling" onchange="filterMeetings()"> <span>🚴&nbsp;&nbsp;탁구</span>
	</label>
	<label class="filter-item"> <input name="sportCategory"
		type="radio" value="cycling" onchange="filterMeetings()"> <span>🚴&nbsp;&nbsp;당구/포켓볼</span>
	</label>
	<span class="title">지역</span>
	<label class="filter-item"> <input name="regionCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>서울특별시</span>
	</label>
	<label class="filter-item"> <input name="regionCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>경기도</span>
	</label>
	<label class="filter-item"> <input name="regionCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>인천광역시</span>
	</label>
	<label class="filter-item"> <input name="regionCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>부산광역시</span>
	</label>
	<label class="filter-item"> <input name="regionCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>대구광역시</span>
	</label>
	<label class="filter-item"> <input name="regionCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>울산광역시</span>
	</label>
	<span class="title">정렬</span>
	<label class="filter-item"> <input name="sortCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>최신순</span>
	</label>
	<label class="filter-item"> <input name="sortCategory"
		type="radio" value="seoul" onchange="filterMeetings()"> <span>추천순</span>
	</label>
</div>