import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
	Dialog,
	DialogContent,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog";
import { useSupplierStore } from "@/store/supplierStore";
import { toast } from "react-toastify";
import { Camera } from "lucide-react";
import { suppliersApi } from "../actions";
// Define Yup validation schema
const imageSchema = yup.object().shape({
	image: yup
		.mixed()
		.required("Image is required")
		.test("fileType", "Only images are allowed", (value) => {
			return (
				value && ["image/jpeg", "image/png", "image/gif"].includes(value.type)
			);
		}),
});
type FormData = {
	image: File | null;
};
export function EditSupplierImage({
	supplierId,
	currentImage,
}: {
	supplierId: number;
	currentImage: string;
}) {
	const [loading, setLoading] = useState(false);
	const { updateSupplierImage } = useSupplierStore();
	// Initialize react-hook-form with Yup resolver
	const {
		control,
		handleSubmit,
		reset,
		formState: { errors },
	} = useForm<FormData>({
		resolver: yupResolver(imageSchema),
		defaultValues: {
			image: null,
		},
	});
	// Handle form submission
	const onSubmit = async (data: FormData) => {
		setLoading(true);
		try {
			const formData = new FormData();
			if (data.image) {
				formData.append("image", data.image);
			}
			// Call the backend API to update the supplier image
			updateSupplierImage(supplierId, formData);
			// Optionally, you can show a success message or perform other actions here
			toast.success("Supplier image updated successfully", {
				position: "top-right",
				autoClose: 2000,
			});
			reset(); // Reset the form
		} catch (error) {
			console.error("Failed to update supplier image:", error);
		} finally {
			setLoading(false);
		}
	};
	return (
		<Dialog>
			<DialogTrigger asChild>
				<Button variant="ghost" className="bg-cyan-500 text-white">
					<Camera />
				</Button>
			</DialogTrigger>
			<DialogContent className="sm:max-w-[425px]">
				<DialogHeader>
					<DialogTitle>Edit Supplier Image</DialogTitle>
				</DialogHeader>
				<form onSubmit={handleSubmit(onSubmit)} className="flex flex-col space-y-4">
					{/* Display Current Image */}
					{currentImage && (
						<div className="flex justify-center">
							<img
								src={suppliersApi + "/" + currentImage}
								alt="Current Supplier"
								className="h-32 w-32 object-cover rounded-md"
							/>
						</div>
					)}
					{/* Image Upload Field */}
					<div className="grid grid-cols-4 items-center gap-4">
						<Label htmlFor="image" className="text-right">
							New Image
						</Label>
						<Controller
							name="image"
							control={control}
							render={({ field: { onChange } }) => (
								<Input
									id="image"
									type="file"
									accept="image/*"
									className="col-span-3 p-0"
									onChange={(e) => onChange(e.target.files?.[0])}
								/>
							)}
						/>
						{errors.image && (
							<p className="text-red-500 col-span-3">{errors.image.message}</p>
						)}
					</div>
					{/* Submit Button */}
					<Button type="submit" disabled={loading}>
						{loading ? "Uploading..." : "Save Changes"}
					</Button>
				</form>
			</DialogContent>
		</Dialog>
	);
}
