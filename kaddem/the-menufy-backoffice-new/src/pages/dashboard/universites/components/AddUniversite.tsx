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
import { useUniversiteStore } from "@/store/universiteStore";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { toast } from "react-toastify";
import { useState } from "react";
const universiteSchema = yup.object().shape({
	nomUniv: yup.string().required("University name is required"),
});
export function AddUniversite() {
	const { addUniversite } = useUniversiteStore();
	const [isOpen, setIsOpen] = useState(false);
	const {
		register,
		handleSubmit,
		reset,
		formState: { errors },
	} = useForm({
		resolver: yupResolver(universiteSchema),
		defaultValues: {
			nomUniv: "",
		},
	});
	const onSubmit = (data: any) => {
		try {
			addUniversite(data);
			toast.success("University added successfully", {
				position: "top-right",
				autoClose: 2000,
			});
			reset();
			setIsOpen(false);
		} catch (error) {
			toast.error("Error adding university", {
				position: "top-right",
				autoClose: 2000,
			});
		}
	};
	return (
		<Dialog open={isOpen} onOpenChange={setIsOpen}>
			<DialogTrigger asChild>
				<Button className="bg-green-500 text-white" variant="outline">
					<Plus />
				</Button>
			</DialogTrigger>
			<DialogContent className="sm:max-w-[425px] p-10">
				<DialogHeader>
					<DialogTitle>Add New University</DialogTitle>
					<DialogDescription>
						Enter the details of the new university here.
					</DialogDescription>
				</DialogHeader>
				<form onSubmit={handleSubmit(onSubmit)} className="flex flex-col space-y-4">
					<div className="grid grid-cols-4 items-center gap-4">
						<Label htmlFor="nomUniv" className="text-right">
							Name
						</Label>
						<Input id="nomUniv" {...register("nomUniv")} className="col-span-3" />
						{errors.nomUniv && (
							<p className="text-red-500 col-span-3">{errors.nomUniv.message}</p>
						)}
					</div>
					<DialogFooter>
						<Button type="submit">Add University</Button>
					</DialogFooter>
				</form>
			</DialogContent>
		</Dialog>
	);
}
