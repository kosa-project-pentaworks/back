import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

import axios from 'axios';

function KakaoAuthRedirect({fetchUserInfo}) {
    const navigate = useNavigate();

    useEffect(() => {
        const handleKakaoLogin = async () => {
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
            }
        };

        handleKakaoLogin();
    }, [fetchUserInfo, navigate]);

    return (
        <div>카카오 로그인 처리 중...</div>
    );
}

export default KakaoAuthRedirect;
