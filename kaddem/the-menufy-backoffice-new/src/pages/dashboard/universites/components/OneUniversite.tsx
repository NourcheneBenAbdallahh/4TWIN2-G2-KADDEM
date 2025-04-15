import { Button } from "@/components/ui/button";
import {
	Card,
	CardContent,
	CardDescription,
	CardFooter,
	CardHeader,
	CardTitle,
} from "@/components/ui/card";
import { Eye, Trash } from "lucide-react";
import { EditUniversite } from "./EditUniversite";
import { useUniversiteStore } from "@/store/universiteStore";
import type { Universite } from "@/store/universiteStore";
export const OneUniversite = ({ universite }: { universite: Universite }) => {
	const { deleteUniversite } = useUniversiteStore();
	return (
		<Card>
			<CardHeader>
				<CardTitle>{universite.nomUniv}</CardTitle>
				<CardDescription>University ID: {universite.idUniv}</CardDescription>
			</CardHeader>
			<CardContent>
				<div className="flex flex-col space-y-2">
					<div className="text-sm text-gray-500">
						Number of Departments: {universite.departements?.length || 0}
					</div>
				</div>
			</CardContent>
			<CardFooter className="flex justify-end space-x-2">
				<Button variant="outline">
					<Eye />
				</Button>
				<EditUniversite universite={universite} />
				<Button
					className="bg-red-500 text-white"
					variant="outline"
					onClick={() => deleteUniversite(universite.idUniv)}
				>
					<Trash />
				</Button>
			</CardFooter>
		</Card>
	);
};
