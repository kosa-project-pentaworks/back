import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../AuthContext';
import 'bootstrap/dist/css/bootstrap.min.css';

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username) {
            setErrorMessage('아이디를 입력해주세요.');
            return;
        }
        if (!password) {
            setErrorMessage('비밀번호를 입력해주세요.');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/v1/auth/login', {
                email: username,
                password
            });

            if (!response.data.success) {
                setErrorMessage('로그인 실패: ' + response.data.code);
            } else {
                login(response.data.data);  // 로그인 성공 시 전역 상태 업데이트
                navigate('/dashboard');
            }
        } catch (error) {
            console.error('Login failed:', error);
            setErrorMessage('로그인 실패: 사용자 정보가 일치하지 않습니다.');
        }
    };

    const handleKakaoLogin = () => {
        window.location.href = `http://localhost:8080/oauth2/authorization/kakao`; // 카카오 로그인 페이지로 리디렉션
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card shadow-sm p-4" style={{ width: '100%', maxWidth: '400px' }}>
                <h3 className="text-center mb-4">로그인</h3>
                <form onSubmit={handleSubmit}>
                    {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
                    <div className="mb-3">
                        <label htmlFor="username" className="form-label">Username</label>
                        <input
                            type="text"
                            className="form-control"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="btn btn-primary w-100">로그인</button>
                </form>
                <button onClick={handleKakaoLogin} className="btn btn-warning w-100 mt-3">
                    카카오로 로그인
                </button>
            </div>
        </div>
    );
}

export default Login;
