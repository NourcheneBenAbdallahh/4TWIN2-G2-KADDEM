import { create } from "zustand";
import axios from "axios";
import { Supplier } from "@/pages/dashboard/suppliers/components/oneSupplier";
// Backend API URL
const api: string = import.meta.env.VITE_BACKEND_URL;
// Create the supplierStore
export const useSupplierStore = create<{
	suppliers: Supplier[]; // State: List of suppliers
	loading: boolean; // State: Loading indicator
	error: string | null; // State: Error message
	fetchSuppliers: () => Promise<void>; // Action: Fetch suppliers from the backend
	addSupplier: (data: Supplier) => Promise<void>; // Action: Add a new supplier
	updateSupplier: (id: number, data: Supplier) => Promise<void>; // Action: Update an existing supplier
	deleteSupplier: (id: number) => Promise<void>; // Action: Delete a supplier
}>((set) => ({
	suppliers: [], // Initial state: Empty list of suppliers
	loading: false, // Initial state: Not loading
	error: null, // Initial state: No error
	// Fetch all suppliers from the backend
	fetchSuppliers: async () => {
		set({ loading: true, error: null });
		try {
			const response = await axios.get<Supplier[]>(`${api}/suppliers`);
			set({ suppliers: response.data, loading: false });
		} catch (err: any) {
			set({ error: err.message || "Failed to fetch suppliers", loading: false });
		}
	},
	// Add a new supplier
	addSupplier: async (data: Supplier) => {
		set({ loading: true, error: null });
		try {
			const response = await axios.post<Supplier>(`${api}/suppliers`, data);
			set((state) => ({
				suppliers: [...state.suppliers, response.data],
				loading: false,
			}));
		} catch (err: any) {
			set({ error: err.message || "Failed to add supplier", loading: false });
		}
	},
	// Update an existing supplier
	updateSupplier: async (id: number, data: Supplier) => {
		set({ loading: true, error: null });
		try {
			const response = await axios.put<Supplier>(`${api}/suppliers/${id}`, data);
			set((state) => ({
				suppliers: state.suppliers.map((supplier) =>
					supplier.id === id ? response.data : supplier
				),
				loading: false,
			}));
		} catch (err: any) {
			set({ error: err.message || "Failed to update supplier", loading: false });
		}
	},
	// Delete a supplier
	deleteSupplier: async (id: number) => {
		set({ loading: true, error: null });
		try {
			await axios.delete(`${api}/suppliers/${id}`);
			set((state) => ({
				suppliers: state.suppliers.filter((supplier) => supplier.id !== id),
				loading: false,
			}));
		} catch (err: any) {
			set({ error: err.message || "Failed to delete supplier", loading: false });
		}
	},
	updateSupplierImage: async (id: number, data: FormData) => {
		set({ loading: true, error: null }); // Set loading state and clear previous errors
		try {
			// Call the backend API to update the supplier image
			const response = await axios.put<String>(
				`${api}/suppliers/${id}/image`,
				data,
				{
					headers: {
						"Content-Type": "multipart/form-data",
					},
				}
			);
			console.log("Image updated successfully:", response.data);
			// Update the supplier in the store with the new data from the response
			set((state) => ({
				suppliers: state.suppliers.map((supplier) => {
					if (supplier.id === id) {
						return { ...supplier, image: `${response.data}` }; // Update the image field
					}
					return supplier; // Return the supplier unchanged if not the one being updated
				}),
				loading: false, // Reset loading state
			}));
		} catch (err: any) {
			// Handle errors gracefully
			console.error("Error updating supplier image:", err);
			// Extract meaningful error message from the response or fallback to a generic message
			const errorMessage =
				err.response?.data?.message ||
				err.message ||
				"Failed to update supplier image";
			set({
				error: errorMessage, // Set the error message
				loading: false, // Reset loading state
			});
		}
	},
}));
