import { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setIsLoggedIn(true); // 자동 로그인 처리
        }
    }, []);

    const login = (token) => {
        localStorage.setItem('token', token); // 토큰 저장
        setIsLoggedIn(true);
    };

    const logout = (navigate) => {
        localStorage.removeItem("token"); // 토큰 제거
        setIsLoggedIn(false);
        if (navigate) {
            navigate("/login"); // 로그인 페이지로 이동
        }
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
