import { AuthRoutes } from "./AuthRoutes";
import { DashboardRoutes } from "./DashboardRoutes";
export const AppRoutes = () => {
	return (
		<>
			<AuthRoutes />
			<DashboardRoutes />
		</>
	);
};
