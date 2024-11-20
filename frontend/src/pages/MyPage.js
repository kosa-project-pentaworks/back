import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

function MyPage() {
    const [userInfo, setUserInfo] = useState({
        username: '사용자 이름 없음',
        phone: '',
        address: {
            street: '',
            city: '',
            state: '',
            zipCode: '',
        },
    });
    const [loading, setLoading] = useState(true);
    const [editMode, setEditMode] = useState(false);

    const [formValues, setFormValues] = useState({
        phone: '',
        street: '',
        city: '',
        state: '',
        zipCode: '',
    });

    const fetchUserInfo = async () => {
        const token = localStorage.getItem('token');

        if (!token) {
            console.error('토큰이 존재하지 않습니다.');
            setLoading(false);
            return;
        }

        try {
            const decodedToken = jwtDecode(token);
            const providerId = decodedToken.userId;

            const response = await axios.get(
                `${process.env.REACT_APP_API_URL}/v1/user/${providerId}`,
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );

            const data = response.data.data || {};
            const address = data.address || {};

            setUserInfo({
                username: data.username || '사용자 이름 없음',
                phone: data.phone || '',
                address: {
                    street: address.street || '',
                    city: address.city || '',
                    state: address.state || '',
                    zipCode: address.zipCode || '',
                },
            });

            setFormValues({
                phone: data.phone || '',
                street: address.street || '',
                city: address.city || '',
                state: address.state || '',
                zipCode: address.zipCode || '',
            });
        } catch (error) {
            console.error('유저 정보 로드 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUserInfo();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormValues((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('token');

        if (!token) {
            console.error('토큰이 존재하지 않습니다.');
            return;
        }

        try {
            const decodedToken = jwtDecode(token);
            const providerId = decodedToken.userId;

            const updatedData = {
                phone: formValues.phone,
                address: {
                    street: formValues.street,
                    city: formValues.city,
                    state: formValues.state,
                    zipCode: formValues.zipCode,
                },
            };

            await axios.put(
                `${process.env.REACT_APP_API_URL}/v1/user/${providerId}`,
                updatedData,
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );

            // 서버에서 최신 데이터 재조회
            await fetchUserInfo();

            setEditMode(false);
        } catch (error) {
            console.error('정보 업데이트 실패:', error);
        }
    };

    const handleDeleteAccount = async () => {
        const token = localStorage.getItem('token');

        if (!token) {
            console.error('토큰이 존재하지 않습니다.');
            return;
        }

        if (!window.confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) {
            return;
        }

        try {
            const decodedToken = jwtDecode(token);
            const providerId = decodedToken.userId;

            await axios.delete(
                `${process.env.REACT_APP_API_URL}/v1/user/${providerId}`,
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );

            // 토큰 삭제 및 리디렉션
            localStorage.removeItem('token');
            alert('회원 탈퇴가 완료되었습니다.');
            window.location.href = '/'; // 메인 페이지로 리디렉션
        } catch (error) {
            console.error('회원 탈퇴 실패:', error);
            alert('회원 탈퇴 중 문제가 발생했습니다. 다시 시도해주세요.');
        }
    };

    if (loading) return <div>로딩 중...</div>;

    return (
        <div>
            <h1>마이페이지</h1>

            {!editMode ? (
                <div>
                    <p>유저명: {userInfo.username}</p>
                    <p>전화번호: {userInfo.phone}</p>
                    <p>
                        주소:{" "}
                        {userInfo.address.street
                            ? `${userInfo.address.street}, ${userInfo.address.city}, ${userInfo.address.state}, ${userInfo.address.zipCode}`
                            : "주소 없음"}
                    </p>
                    <button onClick={() => setEditMode(true)} className="btn btn-primary">
                        수정
                    </button>
                    <button onClick={handleDeleteAccount} className="btn btn-danger ms-2">
                        회원 탈퇴
                    </button>
                </div>
            ) : (
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="phone" className="form-label">전화번호</label>
                        <input
                            type="text"
                            className="form-control"
                            id="phone"
                            name="phone"
                            value={formValues.phone}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="street" className="form-label">거리</label>
                        <input
                            type="text"
                            className="form-control"
                            id="street"
                            name="street"
                            value={formValues.street}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="city" className="form-label">도시</label>
                        <input
                            type="text"
                            className="form-control"
                            id="city"
                            name="city"
                            value={formValues.city}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="state" className="form-label">주/도</label>
                        <input
                            type="text"
                            className="form-control"
                            id="state"
                            name="state"
                            value={formValues.state}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="zipCode" className="form-label">우편번호</label>
                        <input
                            type="text"
                            className="form-control"
                            id="zipCode"
                            name="zipCode"
                            value={formValues.zipCode}
                            onChange={handleChange}
                        />
                    </div>
                    <button type="submit" className="btn btn-success">
                        저장
                    </button>
                    <button
                        type="button"
                        className="btn btn-secondary ms-2"
                        onClick={() => setEditMode(false)}
                    >
                        취소
                    </button>
                </form>
            )}
        </div>
    );
}

export default MyPage;
