import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

function Login({setIsLoggedIn}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

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
            // login API 에 POST 요청
            const response = await axios.post(`${process.env.REACT_APP_API_URL}/v1/auth/login`, {
                email: username,
                password,
            });

            console.log(response);

            if (response.data.success) {
                // 토큰 처리: 객체이든 문자열이든 저장 가능하도록 처리
                const token = typeof response.data.data === 'object'
                    ? JSON.stringify(response.data.data) // 객체라면 JSON 문자열로 변환
                    : response.data.data; // 문자열이라면 그대로 사용

                localStorage.setItem('token', token);

                // 이후 페이지 이동 또는 로그인 처리 로직 추가
                setIsLoggedIn(true)

                navigate("/"); // 관리자 배포 주소로 수정
            } else {
                setErrorMessage('로그인 실패: 사용자 정보가 일치하지 않습니다.');
            }
        } catch (error) {
            console.error('로그인 실패:', error);
            setErrorMessage('로그인 실패: ' + (error.response?.data?.message || '서버 오류'));
        }
    };

    const handleAdminSignup = () => {
        navigate('/admin-signup');
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card shadow-sm p-4" style={{width: '100%', maxWidth: '400px'}}>
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
                <button onClick={handleAdminSignup} className="btn btn-warning w-100 mt-3">회원가입</button>
            </div>
        </div>
    );
}

export default Login;
