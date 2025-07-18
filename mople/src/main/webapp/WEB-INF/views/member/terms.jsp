<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>이용약관</title>
<%-- Bootstrap CSS는 메인 페이지에서 이미 로드되었다고 가정합니다. --%>
<%-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"> --%>
<style>
    /* 약관 내용 스크롤을 위한 스타일 */
    .terms-content {
        max-height: 400px; /* 적절한 높이로 조절 */
        overflow-y: auto; /* 내용이 넘치면 스크롤 바 생성 */
        border: 1px solid #e0e0e0;
        padding: 15px;
        background-color: #f9f9f9;
        margin-bottom: 20px;
        border-radius: 5px;
    }
    .terms-content h4 {
        margin-top: 20px;
        margin-bottom: 10px;
        font-weight: bold;
        color: #333;
    }
    .terms-content p, .terms-content ul {
        line-height: 1.6;
        margin-bottom: 10px;
    }
    .terms-content ul {
        list-style-type: decimal;
        padding-left: 20px;
    }
</style>
</head>
<body>

<div class="modal fade" id="termsModal" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="termsModalLabel">모플 이용약관</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
            </div>
            <div class="modal-body">
                <div class="terms-content">
                    <h4>제1장 총칙</h4>
                    <p><strong>제1조 목적</strong></p>
                    <p>본 약관은 모플 (이하 "회사" 또는 "서비스")이 제공하는 운동 파트너 매칭 및 관련 제반 서비스의 이용과 관련하여 회사와 회원 간의 권리, 의무 및 책임 사항, 기타 필요한 사항을 규정함을 목적으로 합니다.</p>

                    <p><strong>제2조 용어의 정의</strong></p>
                    <p>이 약관에서 사용하는 용어의 정의는 다음과 같습니다.</p>
                    <ul>
                        <li>"서비스"라 함은 구현되는 단말기(PC, 휴대용 단말기 등의 각종 유무선 장치를 포함)와 상관없이 회원이 이용할 수 있는 모플 및 관련 제반 서비스를 의미합니다.</li>
                        <li>"회원"이라 함은 회사의 서비스에 접속하여 이 약관에 따라 회사와 이용계약을 체결하고 회사가 제공하는 서비스를 이용하는 고객을 말합니다.</li>
                        <li>"게시물"이라 함은 회원이 서비스를 이용함에 있어 서비스 상에 게시한 부호, 문자, 음성, 음향, 그림, 사진, 동영상 등의 정보 형태의 글, 사진 및 각종 파일과 링크 등을 의미합니다.</li>
                        <li>"닉네임"이라 함은 회원의 식별을 위해 사용되는 문자를 의미합니다.</li>
                    </ul>

                    <p><strong>제3조 약관의 효력 발생 및 변경</strong></p>
                    <ul>
                        <li>이 약관은 서비스를 이용하고자 하는 모든 회원에 대하여 그 효력을 발생합니다.</li>
                        <li>회사는 서비스의 안정적인 제공을 위하여 이 약관을 변경할 수 있으며, 변경된 약관은 적용일자 및 개정사유를 명시하여 그 적용일자 7일 전부터 서비스 내에 공지합니다. 다만, 회원에게 불리한 약관의 변경인 경우에는 30일 전부터 공지하며, 이메일, SMS 등으로 개별 통지합니다.</li>
                        <li>회원이 개정된 약관의 적용에 동의하지 않는 경우 회사는 개정 약관의 내용을 적용할 수 없으며, 이 경우 회원은 이용계약을 해지할 수 있습니다. 단, 기존 약관을 적용할 수 없는 특별한 사정이 있는 경우에는 회사는 이용계약을 해지할 수 있습니다.</li>
                    </ul>

                    <h4>제2장 서비스 이용 계약</h4>
                    <p><strong>제4조 회원가입</strong></p>
                    <ul>
                        <li>회원가입은 서비스 이용을 희망하는 자가 약관의 내용에 동의하고, 회사가 정한 가입 양식에 따라 정보를 기입한 후 회원가입 신청을 하고, 회사가 이러한 신청을 승낙함으로써 체결됩니다.</li>
                        <li>회사는 다음 각 호에 해당하는 신청에 대하여는 승낙을 하지 않거나 사후에 이용계약을 해지할 수 있습니다.
                            <ul>
                                <li>가입 신청자가 이 약관에 의하여 이전에 회원자격을 상실한 적이 있는 경우 (단, 회사의 회원 재가입 승낙을 얻은 경우에는 예외)</li>
                                <li>실명이 아니거나 타인의 명의를 이용한 경우</li>
                                <li>허위 정보를 기재하거나, 회사가 제시하는 내용을 기재하지 않은 경우</li>
                                <li>만 14세 미만인 경우</li>
                                <li>기타 관련 법령에 위배되거나 부당한 이용신청으로 판단되는 경우</li>
                            </ul>
                        </li>
                    </ul>

                    <p><strong>제5조 회원 정보의 변경</strong></p>
                    <p>회원은 언제든지 본인의 개인정보를 열람하고 수정할 수 있습니다. 회원은 회원가입 신청 시 기재한 사항이 변경되었을 경우 온라인으로 수정을 하거나 전자우편 기타 방법으로 회사에 대하여 그 변경 사항을 알려야 합니다.</p>

                    <h4>제3장 서비스 이용</h4>
                    <p><strong>제6조 서비스의 제공</strong></p>
                    <ul>
                        <li>회사는 회원에게 아래와 같은 서비스를 제공합니다.
                            <ul>
                                <li>a. 매칭 서비스 (운동 파트너 찾기)</li>
                                <li>b. 커뮤니티 게시판</li>
                                <li>c. 운동 기록 및 통계 관리</li>
                                <li>d. 기타 회사가 추가 개발하거나 제휴 계약 등을 통해 회원에게 제공하는 일체의 서비스</li>
                            </ul>
                        </li>
                        <li>서비스는 연중무휴, 1일 24시간 제공함을 원칙으로 합니다. 다만, 회사의 업무상 또는 기술상의 이유로 서비스가 일시 중지되거나, 운영상의 목적으로 회사가 정한 기간 동안 서비스가 중지될 수 있습니다.</li>
                    </ul>

                    <h4>제4장 책임</h4>
                    <p><strong>제7조 회사의 의무</strong></p>
                    <ul>
                        <li>회사는 관련 법령과 이 약관이 금지하거나 미풍양속에 반하는 행위를 하지 않으며, 계속적이고 안정적으로 서비스를 제공하기 위하여 최선을 다하여 노력합니다.</li>
                        <li>회사는 회원이 안전하게 서비스를 이용할 수 있도록 개인정보 보호를 위한 보안 시스템을 구축하며 개인정보처리방침을 공시하고 준수합니다.</li>
                    </ul>

                    <p><strong>제8조 회원의 의무</strong></p>
                    <ul>
                        <li>회원은 다음 행위를 하여서는 안 됩니다.
                            <ul>
                                <li>다른 회원의 아이디 및 비밀번호를 도용하는 행위</li>
                                <li>회사 또는 타인의 저작권 등 지식재산권을 침해하는 행위</li>
                                <li>외설 또는 폭력적인 메시지, 화상, 음성, 기타 공서양속에 반하는 정보를 서비스에 공개 또는 게시하는 행위</li>
                                <li>서비스의 운영을 방해하거나 다른 회원의 서비스 이용을 방해하는 행위</li>
                                <li>기타 불법적이거나 부당한 행위</li>
                            </ul>
                        </li>
                        <li>회원은 이 약관 및 관계 법령을 준수하여야 하며, 회사의 업무에 방해되는 행위를 하여서는 안 됩니다.</li>
                    </ul>

                    <h4>제5장 기타</h4>
                    <p><strong>제9조 개인정보 보호</strong></p>
                    <p>회사는 정보통신망법 등 관계 법령이 정하는 바에 따라 회원의 개인정보를 보호하기 위해 노력합니다. 개인정보의 보호 및 사용에 대해서는 관련 법령 및 회사의 개인정보처리방침이 적용됩니다.</p>

                    <p><strong>제10조 분쟁 해결</strong></p>
                    <p>서비스 이용과 관련하여 회사와 회원 간에 분쟁이 발생한 경우, 회사와 회원은 분쟁의 해결을 위해 성실히 협의합니다.</p>

                    <p><strong>제11조 준거법 및 재판관할</strong></p>
                    <ul>
                        <li>회사와 회원 간에 발생한 분쟁에 대하여는 대한민국법을 준거법으로 합니다.</li>
                        <li>회사와 회원 간 발생한 소송의 관할법원은 민사소송법상 관할 규정에 따릅니다.</li>
                    </ul>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<%-- Bootstrap JS는 메인 페이지에서 이미 로드되었다고 가정합니다. --%>
<%-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script> --%>
</body>
</html>