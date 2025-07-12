<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="modal fade" id="loginModal" tabindex="-1"
	data-bs-backdrop="static" data-bs-keyboard="false"
	aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="loginViewerModalLabel">Login</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<div class="p-3">
					<form name="modalLoginForm" action="" method="post" class="row g-3">
						<div class="mt-0">
							<p class="form-control-plaintext">계정으로 로그인 하세요</p>
						</div>
						<div class="mt-0">
							<input type="text" name="userId" class="form-control"
								placeholder="아이디">
						</div>
						<div>
							<input type="password" name="userPwd" class="form-control"
								autocomplete="off" placeholder="패스워드">
						</div>
						<div>
							<input type="checkbox" id="rememberMeModal"> <label
								class="form-check-label" for="rememberMeModal"> 아이디 저장</label>
						</div>
						<div>
							<button type="button" class="btn btn-primary w-100"
								onclick="sendModalLogin();">Login</button>
						</div>
						<div>
							<p class="form-control-plaintext text-center">
								<a href="#" class="text-decoration-none me-2">패스워드를 잊으셨나요 ?</a>
							</p>
						</div>
					</form>
					<hr class="mt-3">
					<div>
						<p class="form-control-plaintext mb-0">
							아직 회원이 아니세요 ? <a
								href="${pageContext.request.contextPath}/member/account"
								class="text-decoration-none">회원가입</a>
						</p>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<!-- Login Modal -->
<script type="text/javascript">
	function dialogLogin() {
		$('form[name=modalLoginForm] input[name=userId]').val('');
		$('form[name=modalLoginForm] input[name=userPwd]').val('');

		const loginModalElement = document.getElementById('loginModal');
		const loginModal = new bootstrap.Modal(loginModalElement);
		loginModal.show();

		$('form[name=modalLoginForm] input[name=userId]').focus();
	}

	function sendModalLogin() {
		const f = document.modalLoginForm;
		let str;

		str = f.userId.value;
		if (!str) {
			f.userId.focus();
			return;
		}

		str = f.userPwd.value;
		if (!str) {
			f.userPwd.focus();
			return;
		}

		f.action = '${pageContext.request.contextPath}/member/login';
		f.submit();
	}
</script>