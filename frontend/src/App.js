import {BrowserRouter as Router, Link, Route, Routes, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import { jwtDecode } from "jwt-decode";

import Login from "./pages/Login";
import Signup from "./pages/Signup";
import MyPage from "./pages/MyPage";
import Dashboard from "./pages/Dashboard";
import ProtectedRoute from "./ProtectedRoute";
import Main from "./pages/Main";
import KakaoAuthRedirect from "./pages/KakaoAuthRedirect";
import HospitalReservationHistory from "./pages/HospitalReservationHistory";

import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리
    const [username, setUsername] = useState(""); // 로그인된 유저의 이름

    // 유저 정보 갱신 함수
    const fetchUserInfo = async (token) => {
        try {
            const decodedToken = jwtDecode(token);
            const providerId = decodedToken.userId;

            const response = await axios.get(
                `${process.env.REACT_APP_API_URL}/v1/user/${providerId}`,
                {
                    headers: {Authorization: `Bearer ${token}`},
                    withCredentials: true,
                }
            );

            const userData = response.data.data || {};
            setUsername(userData.username || "사용자");
            setIsLoggedIn(true);
        } catch (error) {
            console.error("유저 정보 로드 실패:", error);
        }
    };

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            fetchUserInfo(token);
            setIsLoggedIn(true);
        }
    }, []);

    const handleLogout = async (e) => {
        e.preventDefault();
        try {
            localStorage.removeItem("token"); // 로컬 스토리지에서 토큰 삭제
            setIsLoggedIn(false); // 상태 초기화
            setUsername(""); // 유저 이름 초기화
        } catch (error) {
            alert("로그아웃 실패");
        }
    };

    const handleKakaoLogin = () => {
        window.location.href = `${process.env.REACT_APP_API_URL}/oauth2/authorization/kakao`;
    };

    // 네비게이션 바 숨기기 조건 처리
    const Navbar = () => {
        const location = useLocation();
        const hideNavbar = location.pathname === "/login" || location.pathname === "/signup";

        if (hideNavbar) return null;

        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container">
                    <Link className="navbar-brand" to="/dashboard">Hospital Go</Link>
                    <div className="collapse navbar-collapse">
                        <ul className="navbar-nav ms-auto">
                            {!isLoggedIn ? (
                                <li className="nav-item">
                                    <button
                                        className="btn btn-warning"
                                        onClick={handleKakaoLogin}
                                    >
                                        카카오로 로그인
                                    </button>
                                </li>
                            ) : (
                                <>
                                    <li className="nav-item">
                                        <Link className="btn btn-success" to="/mypage">
                                            {username || "마이페이지"}
                                        </Link>
                                    </li>
                                    <li className="nav-item">
                                        <button
                                            className="btn btn-danger"
                                            onClick={handleLogout}
                                        >
                                            로그아웃
                                        </button>
                                    </li>
                                </>
                            )}
                        </ul>
                    </div>
                </div>
            </nav>
        );
    };

    return (
        <Router>
            <div>
                {/* 네비게이션 바 */}
                <Navbar/>

                {/* 페이지 라우팅 */}
                <div className="container mt-5">
                    <Routes>
                        <Route path="/" element={<Main/>}/>
                        <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn}/>}/>
                        <Route path="/signup" element={<Signup/>}/>
                        <Route
                            path="/login/oauth2/code/kakao"
                            element={<KakaoAuthRedirect fetchUserInfo={fetchUserInfo}/>}
                        />
                        <Route
                            path="/dashboard"
                            element={
                                <ProtectedRoute>
                                    <Dashboard/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/hospitalReservationHistory"
                            element={
                                <ProtectedRoute>
                                    <HospitalReservationHistory/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/mypage"
                            element={
                                <ProtectedRoute>
                                    <MyPage fetchUserInfo={fetchUserInfo}/>
                                </ProtectedRoute>
                            }
                        />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
