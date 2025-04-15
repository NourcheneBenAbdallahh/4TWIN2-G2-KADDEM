import { Button } from "@/components/ui/button";
import {
	Card,
	CardContent,
	CardDescription,
	CardFooter,
	CardHeader,
	CardTitle,
} from "@/components/ui/card";
import { Eye, Mail, Phone, Trash } from "lucide-react";
import { EditSupplier } from "./EditSupplier";
import { useSupplierStore } from "@/store/supplierStore";
import { suppliersApi } from "../actions";
import { EditSupplierImage } from "./EditSupplierImage";
export interface Supplier {
	id: number;
	name: string;
	phone: string;
	email: string;
	description: string;
	image: string;
}
export const OneSupplier = ({ supplier }: { supplier: Supplier }) => {
	const { deleteSupplier } = useSupplierStore();
	return (
		<Card>
			<CardHeader className="relative">
				{supplier.image ? (
					<img src={suppliersApi + "/" + supplier.image} />
				) : (
					<img src="https://images.pexels.com/photos/4483861/pexels-photo-4483861.jpeg?auto=compress&cs=tinysrgb&w=600" />
				)}
				<div className="grid place-items-end absolute top-2 right-2">
					<EditSupplierImage
						currentImage={supplier.image}
						supplierId={supplier.id}
					/>
				</div>
				<CardTitle>{supplier.name}</CardTitle>
				<CardDescription>{supplier.description}</CardDescription>
			</CardHeader>
			<CardContent>
				<div className="flex flex-col space-y-2">
					<div className="flex space-x-2 items-center text-sm text-gray-500">
						{" "}
						<Mail size={20} /> <p> {supplier.email} </p>
					</div>
					<div className="flex space-x-2 items-center text-sm  text-gray-500">
						{" "}
						<Phone size={20} /> <p> {supplier.phone} </p>
					</div>
				</div>
			</CardContent>
			<CardFooter className="flex justify-end space-x-2">
				<Button variant="outline">
					<Eye />
				</Button>
				<EditSupplier supplier={supplier} />
				<Button
					className="bg-red-500 text-white"
					variant="outline"
					onClick={async () => deleteSupplier(supplier.id)}
				>
					<Trash />
				</Button>
			</CardFooter>
		</Card>
	);
};
