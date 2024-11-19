import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

function KakaoAuthRedirect({setIsLoggedIn, setUsername}) {
    const navigate = useNavigate();

    useEffect(() => {
        const code = new URL(window.location.href).searchParams.get("code");

        if (!code) {
            console.error('카카오 인증 코드가 없습니다.');
            return;
        }

        axios.post(`${process.env.REACT_APP_API_URL}/v1/auth/callback`, {code})
            .then(response => {
                console.log(response);

                const token = response.data.data.accessToken;
                localStorage.setItem('token', token);

                // JWT 디코딩 후 유저네임 추출
                try {
                    const decodedToken = jwtDecode(token);
                    setUsername(decodedToken.username || '사용자');
                } catch (error) {
                    console.error('JWT 디코딩 실패:', error);
                }

                navigate('/dashboard');
                setTimeout(() => setIsLoggedIn(true), 0);
            })
            .catch(error => {
                console.error('카카오 로그인 실패:', error);
            });
    }, [navigate, setIsLoggedIn, setUsername]);

    return (
        <div>카카오 로그인 처리 중...</div>
    );
}

export default KakaoAuthRedirect;
