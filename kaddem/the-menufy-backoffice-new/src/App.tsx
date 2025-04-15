import { ModeToggle } from "./components/mode-toggle";
import { AppRoutes } from "./routes";
import { ThemeProvider } from "@/components/theme-provider";
import { ToastContainer } from "react-toastify";
function App() {
	return (
		<ThemeProvider defaultTheme="light" storageKey="vite-ui-theme">
			<main>
				<div className="flex justify-end p-4">
					<ModeToggle />
				</div>
				{/* Routing  */}
				<AppRoutes />
			</main>
			<ToastContainer />
		</ThemeProvider>
	);
}
export default App;
