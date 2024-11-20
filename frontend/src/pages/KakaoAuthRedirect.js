import React, {useEffect, useRef} from 'react';
import {useNavigate} from 'react-router-dom';

import axios from 'axios';

function KakaoAuthRedirect({fetchUserInfo}) {
    const navigate = useNavigate();
    const isProcessing = useRef(false);

    useEffect(() => {
        const handleKakaoLogin = async () => {
            if (isProcessing.current) return;
            isProcessing.current = true;

            const code = new URL(window.location.href).searchParams.get("code");
            if (!code) {
                console.error("카카오 인증 코드가 없습니다.");
                return;
            }

            try {
                const response = await axios.post(`${process.env.REACT_APP_API_URL}/v1/auth/callback`, { code });
                const token = response.data.data.accessToken;

                // 토큰 저장
                localStorage.setItem("token", token);

                // 유저 정보 갱신
                await fetchUserInfo(token);

                navigate("/dashboard");
            } catch (error) {
                console.error("카카오 로그인 실패:", error);
            } finally {
                isProcessing.current = false; // 처리 완료 후 상태 초기화
            }
        };

        handleKakaoLogin();
    }, [fetchUserInfo, navigate]);

    return (
        <div>카카오 로그인 처리 중...</div>
    );
}

export default KakaoAuthRedirect;
