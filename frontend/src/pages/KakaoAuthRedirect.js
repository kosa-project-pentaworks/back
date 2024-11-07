// KakaoAuthRedirect.js
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../AuthContext';

function KakaoAuthRedirect() {
    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        // 현재 URL에서 카카오 인증 후 리턴된 authorization code 추출
        const code = new URL(window.location.href).searchParams.get("code");

        const fetchToken = async () => {
            try {
                // 백엔드에 인증 코드 전송하여 JWT 토큰 받기
                const response = await axios.post('http://localhost:8080/api/v1/auth/callback', { code });

                // 받은 토큰을 login 함수를 통해 전역 상태로 설정
                const token = response.data.data;  // 백엔드에서 받은 JWT 토큰
                login(token);  // login 함수를 통해 전역 상태 업데이트

                // 로그인 후 대시보드로 리디렉션
                navigate('/dashboard');
            } catch (error) {
                console.error('카카오 로그인 실패:', error);
                alert('카카오 로그인에 실패했습니다.');
                navigate('/login');
            }
        };

        // 인증 코드가 있을 경우 토큰 요청
        if (code) {
            fetchToken();
        } else {
            alert('카카오 로그인에 실패했습니다.');
            navigate('/login');
        }
    }, [navigate, login]);

    return (
        <div>
            카카오 로그인 처리 중...
        </div>
    );
}

export default KakaoAuthRedirect;
