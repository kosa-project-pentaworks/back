import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

function MyPage() {
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                const providerId = decodedToken.userId;
                const usernameFromToken = decodedToken.username || '사용자 이름 없음';

                axios.get(`${process.env.REACT_APP_API_URL}/v1/user/${providerId}`, {
                    headers: {Authorization: `Bearer ${token}`}, // 인증 헤더 추가
                    withCredentials: true, // CORS 인증 설정
                })
                    .then((response) => {
                        console.log('유저 데이터:', response.data.data); // 응답 확인
                        const data = response.data.data || {}; // 데이터가 없을 경우 빈 객체로 처리
                        setUserInfo({
                            username: usernameFromToken,
                            phone: data.phone || '전화번호 없음',
                            address: {
                                street: data.address?.street || '주소 없음',
                                city: data.address?.city || '',
                                state: data.address?.state || '',
                                zipCode: data.address?.zipCode || '',
                            },
                        });
                        setLoading(false);
                    })
                    .catch((error) => {
                        console.error('프로필 로드 실패:', error);
                        setUserInfo({
                            username: usernameFromToken,
                            phone: '전화번호 없음',
                            address: {
                                street: '주소 없음',
                                city: '',
                                state: '',
                                zipCode: '',
                            },
                        });
                        setLoading(false);
                    });
            } catch (error) {
                console.error('JWT 디코딩 실패:', error);
                setLoading(false);
            }
        } else {
            console.error('토큰이 존재하지 않습니다.');
            setLoading(false);
        }
    }, []);

    if (loading) return <div>로딩 중...</div>; // 로딩 중 처리

    return (
        <div>
            <h1>마이페이지</h1>
            <p>유저명: {userInfo?.username}</p>
            <p>전화번호: {userInfo?.phone}</p>
            <p>주소: {userInfo?.address?.street}</p>
        </div>
    );
}

export default MyPage;
