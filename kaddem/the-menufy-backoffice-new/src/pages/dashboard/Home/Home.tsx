import { CustomAreaChart } from "@/components/charts/CustomAreaChart";
import { CustomBarChart } from "@/components/charts/CustomBarChart";
import { CustomPieChart } from "@/components/charts/CustomePieChart";
export const HomePage = () => {
	return (
		<div className="h-full w-full space-y-4 p-4">
			<div className="grid auto-rows-min gap-4 lg:grid-cols-2 xl:grid-cols-3   ">
				<CustomAreaChart />
				<CustomPieChart />
				<CustomBarChart />
			</div>
			<CustomAreaChart />
		</div>
	);
};
