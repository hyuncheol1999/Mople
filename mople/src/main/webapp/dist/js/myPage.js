
// 탭 기능 JavaScript (순수 JavaScript로 구현)
document.addEventListener('DOMContentLoaded', function() {
    const tabLinks = document.querySelectorAll('.mypage-nav .nav-link');
    const tabPanes = document.querySelectorAll('.tab-content .tab-pane');

    // 페이지 로드 시, active 클래스가 있는 탭 컨텐츠를 보이도록 설정
    const initialActiveTabLink = document.querySelector('.mypage-nav .nav-link.active');
    if (initialActiveTabLink) {
        const initialTargetId = initialActiveTabLink.getAttribute('data-target');
        const initialTargetPane = document.querySelector(initialTargetId);
        if (initialTargetPane) {
            initialTargetPane.classList.add('active');
            initialTargetPane.classList.add('show');
        }
    }


    tabLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // 기본 링크 동작 방지

            // 모든 탭 링크에서 active 클래스 제거
            tabLinks.forEach(nav => nav.classList.remove('active'));
            // 클릭된 탭 링크에 active 클래스 추가
            this.classList.add('active');

            // 모든 탭 컨텐츠에서 active 및 show 클래스 제거
            tabPanes.forEach(pane => {
                pane.classList.remove('active');
                pane.classList.remove('show');
            });

            // 클릭된 탭 링크의 data-target에 해당하는 컨텐츠에 active 및 show 클래스 추가
            const targetId = this.getAttribute('data-target');
            const targetPane = document.querySelector(targetId);
            if (targetPane) {
                targetPane.classList.add('active');
                targetPane.classList.add('show');
            }
        });
    });

    function confirmWithdrawal() {
        if (confirm('정말로 회원 탈퇴를 하시겠습니까? 탈퇴 시 모든 정보가 삭제되며 복구할 수 없습니다.')) {
            location.href = '${pageContext.request.contextPath}/member/withdrawal';
        }
    }
    // confirmWithdrawal 함수를 전역 스코프에서 호출할 수 있도록 window 객체에 할당 (선택 사항)
    // window.confirmWithdrawal = confirmWithdrawal;
});
// confirmWithdrawal 함수를 DOMContentLoaded 밖으로 빼서 버튼 클릭 시 바로 접근 가능하도록 합니다.
function confirmWithdrawal() {
    if (confirm('정말로 회원 탈퇴를 하시겠습니까? 탈퇴 시 모든 정보가 삭제되며 복구할 수 없습니다.')) {
        location.href = '${pageContext.request.contextPath}/member/withdrawal';
    }
}
