<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="filter-section">
    <div class="side-bar">
		<span class="title">관리자 메뉴</span>
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/member/list">회원 관리</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/meeting/list">모임 관리</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/qna/list">문의 내역</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/notice/list">공지사항 관리</a></li>
        </ul>
    </div>
</div>