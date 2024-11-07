import React, {useState} from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import {useNavigate} from "react-router-dom";

function Signup() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');  // 오류 메시지 상태 추가

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 비밀번호 길이 검증 (예: 최소 8자 이상)
        if (password.length < 8) {
            setErrorMessage('비밀번호는 최소 8자 이상이어야 합니다.');
            return;
        }

        try {
            // /register API에 POST 요청
            const response = await axios.post('http://localhost:8080/api/v1/user/register', {
                username,
                password,
                email
            });

            // 회원가입 성공 시 대시보드로 이동
            if (response.status === 200) {
                localStorage.setItem('token', response.data.token);
                navigate('/dashboard');
            }
        } catch (error) {
            console.error('register failed:', error);
            setErrorMessage('회원가입 실패: ' + error.response?.data?.message || '서버 오류');
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card shadow-sm p-4" style={{width: '100%', maxWidth: '400px'}}>
                <h3 className="text-center mb-4">회원가입</h3>
                <form onSubmit={handleSubmit}>
                    {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">이메일</label>
                        <input
                            type="email"
                            className="form-control"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">비밀번호</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="username" className="form-label">성명</label>
                        <input
                            type="text"
                            className="form-control"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary w-100">
                        회원가입
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Signup;
