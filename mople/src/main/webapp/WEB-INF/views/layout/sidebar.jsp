<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="filter-section">
	<span class="title">카테고리</span>
	<label class="filter-item"> 
		<input name="sportCategory"
			type="radio" value="0" checked onchange="filterMeetings()">
		<span>📑&nbsp;&nbsp;전체</span>
	</label> 
	<label class="filter-item"> 
		<input name="sportCategory"
			type="radio" value="" onchange="filterMeetings()"> 
		<span>test</span>
	</label> 
	
	<span class="title">지역</span>
	<label class="filter-item"> 
		<input name="regionCategory"
			type="radio" value="0" onchange="filterMeetings()" checked>
		<span>전체</span>
	</label>
	<label class="filter-item"> 
		<input name="regionCategory"
			type="radio" value="" onchange="filterMeetings()">
		<span>test</span>
	</label>
	
	<span class="title">정렬</span>
	<label class="filter-item"> 
		<input name="sortCategory"
			type="radio" value=latest onchange="filterMeetings()" checked>
			<span>최신순</span>
	</label>
	<label class="filter-item"> 
		<input name="sortCategory"
			type="radio" value="popular" onchange="filterMeetings()">
			<span>인기순</span>
	</label>
</div>