import LoginPage from "@/pages/auth/login/Login";
import { Route, Routes } from "react-router";
export const AuthRoutes = () => {
	return (
		<>
			<Routes>
				<Route path="/" element={<LoginPage />} />
				<Route path="/login" element={<LoginPage />} />
			</Routes>
		</>
	);
};
