import Dashboard from "@/pages/dashboard/Dashboard";
import { Route, Routes } from "react-router";
import { HomePage } from "@/pages/dashboard/Home/Home";
import { SuppliersPage } from "@/pages/dashboard/suppliers";
import { UniversitesPage } from "@/pages/dashboard/universites";
export const DashboardRoutes = () => {
	return (
		<>
			<Routes>
				<Route path="/dashboard" element={<Dashboard />}>
					<Route index element={<HomePage />} />
					<Route path="suppliers" element={<SuppliersPage />} />
					<Route path="universites" element={<UniversitesPage />} />
				</Route>
			</Routes>
		</>
	);
};
