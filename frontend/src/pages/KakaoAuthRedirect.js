import React, {useEffect, useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import {useAuth} from '../AuthContext';

function KakaoAuthRedirect() {
    const navigate = useNavigate();
    const {login} = useAuth();
    const isFetching = useRef(false);

    useEffect(() => {
        const code = new URL(window.location.href).searchParams.get('code');

        const fetchToken = async () => {
            if (isFetching.current) return;
            isFetching.current = true;

            try {
                const response = await axios.post('http://43.201.102.48:80/api/v1/auth/callback', {code});

                if (response.data.success) {
                    login(response.data.token); // 토큰 저장
                    navigate('/dashboard');
                } else {
                    throw new Error('카카오 인증 실패');
                }
            } catch (error) {
                console.error('카카오 로그인 실패:', error);
                alert('카카오 로그인에 실패했습니다.');
                navigate('/login');
            } finally {
                isFetching.current = false;
            }
        };

        if (code) {
            fetchToken();
        } else {
            alert('카카오 로그인에 실패했습니다.');
            navigate('/login');
        }
    }, [navigate, login]);

    return <div>카카오 로그인 처리 중...</div>;
}

export default KakaoAuthRedirect;
