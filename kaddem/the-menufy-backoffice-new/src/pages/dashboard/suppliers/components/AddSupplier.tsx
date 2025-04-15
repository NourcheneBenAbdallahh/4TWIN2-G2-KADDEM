import { Button } from "@/components/ui/button";
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Plus } from "lucide-react";
import { useSupplierStore } from "@/store/supplierStore";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { toast } from "react-toastify";
import { useState } from "react";
// Define Yup validation schema
const supplierSchema = yup.object().shape({
	name: yup.string().required("Name is required"),
	phone: yup.string().required("Phone is required"),
	email: yup.string().email("Invalid email").required("Email is required"),
	description: yup.string(),
});
export function AddSupplier() {
	const { addSupplier, error, loading } = useSupplierStore();
	const [isOpen, setIsOpen] = useState(false);
	// Initialize react-hook-form with Yup resolver
	const {
		register,
		handleSubmit,
		control,
		getValues,
		reset,
		formState: { errors },
	} = useForm({
		resolver: yupResolver(supplierSchema),
		defaultValues: {
			name: "",
			phone: "",
			email: "",
			description: "",
		},
	});
	// Handle form submission
	const onSubmit = (data: any) => {
		try {
			addSupplier(data);
			toast.success("Supplier added successfully", {
				position: "top-right",
				autoClose: 2000,
			});
			reset();
			setIsOpen(false);
		} catch (error) {
			toast.error("Error updating supplier", {
				position: "top-right",
				autoClose: 2000,
			});
		}
	};
	return (
		<Dialog defaultOpen={isOpen} open={isOpen} onOpenChange={setIsOpen}>
			<DialogTrigger asChild>
				<Button className="bg-green-500  text-white" variant="outline">
					<Plus />
				</Button>
			</DialogTrigger>
			<DialogContent className="sm:max-w-[425px] p-10">
				<DialogHeader>
					<DialogTitle>Add New Supplier !</DialogTitle>
					<DialogDescription>
						Make changes to your supplier here. Click save when you're done.
					</DialogDescription>
				</DialogHeader>
				<form
					onSubmit={handleSubmit(onSubmit)}
					className="flex flex-col space-y-4 w-full"
				>
					{Object.entries(getValues()).map(([key, value]) => {
						const isNotEditable = ["id", "image"].includes(key);
						const isString = ["name", "phone", "email", "description"].includes(key);
						if (isNotEditable) return null;
						return (
							<div className="grid grid-cols-4 items-center gap-4" key={key}>
								<Label htmlFor={key} className="">
									{key.charAt(0).toUpperCase() + key.slice(1)}
								</Label>
								{isString && (
									<>
										<Input
											id={key}
											{...register(key)}
											defaultValue={value}
											className="col-span-3"
										/>
										{errors[key] && (
											<p className="text-red-500 col-span-3">{errors[key]?.message}</p>
										)}
									</>
								)}
							</div>
						);
					})}
					<DialogFooter>
						<Button type="submit">Save changes</Button>
					</DialogFooter>
				</form>
			</DialogContent>
		</Dialog>
	);
}
