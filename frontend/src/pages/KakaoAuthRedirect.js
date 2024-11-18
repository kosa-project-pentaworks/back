import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';

function KakaoAuthRedirect({setIsLoggedIn}) {
    const navigate = useNavigate();

    useEffect(() => {
        const code = new URL(window.location.href).searchParams.get("code");

        if (!code) {
            console.error('카카오 인증 코드가 없습니다.');
            return;
        }

        axios.post(`${process.env.REACT_APP_API_URL}/v1/auth/callback`, {code})
            .then(response => {
                console.log('카카오 로그인 성공:', response);

                const token = response.data.data;
                if (!token) {
                    console.error('토큰이 반환되지 않았습니다.');
                    return;
                }

                // JWT 토큰 저장
                const accessToken = typeof token === 'object' ? token.accessToken : token;
                localStorage.setItem('token', accessToken);

                // 페이지 리디렉션
                navigate('/dashboard');

                // 상태 업데이트
                setTimeout(() => setIsLoggedIn(true), 0);
            })
            .catch(error => {
                console.error('카카오 로그인 실패:', error.response || error.message || error);
            });
    }, [navigate]);

    // 페이지가 로드될 때 상태 확인
    useEffect(() => {
        if (localStorage.getItem('token')) {
            setIsLoggedIn(true);
        }
    }, []);

    return (
        <div>카카오 로그인 처리 중...</div>
    );
}

export default KakaoAuthRedirect;
