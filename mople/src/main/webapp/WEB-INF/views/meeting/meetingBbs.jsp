<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div id="bbsTab" class="tab-content">
	<div class="bbs-section">
		<h3>모임&nbsp;게시판</h3>
		<div class="post-form">
			<textarea placeholder="Share your thoughts..."></textarea>
			<button class="btn btn-primary">Post</button>
		</div>
		<div class="posts">
			<div class="post">
				<div class="post-header">
					<img src="${pageContext.request.contextPath}/dist/images/test1.png"
						alt="User">
					<div>
						<strong>김철수</strong> <span class="post-time">2 hours
							ago</span>
					</div>
				</div>
				<p>안녕하세요.</p>
			</div>
		</div>
	</div>
</div>