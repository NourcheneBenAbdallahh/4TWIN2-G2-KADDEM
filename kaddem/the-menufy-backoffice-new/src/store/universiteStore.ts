import { create } from "zustand";
import axios from "axios";
// Define the Universite interface
export interface Universite {
	idUniv: number;
	nomUniv: string;
	departements?: any[]; // We'll keep this simple for now
}
// Backend API URL
const api: string = import.meta.env.VITE_BACKEND_URL;
// Create the universiteStore
export const useUniversiteStore = create<{
	universites: Universite[];
	loading: boolean;
	error: string | null;
	fetchUniversites: () => Promise<void>;
	addUniversite: (data: Universite) => Promise<void>;
	updateUniversite: (data: Universite) => Promise<void>;
	deleteUniversite: (id: number) => Promise<void>;
}>((set) => ({
	universites: [],
	loading: false,
	error: null,
	// Fetch all universites
	fetchUniversites: async () => {
		set({ loading: true, error: null });
		try {
			const response = await axios.get<Universite[]>(
				`${api}/universite/retrieve-all-universites`
			);
			set({ universites: response.data, loading: false });
		} catch (err: any) {
			set({ error: err.message || "Failed to fetch universites", loading: false });
		}
	},
	// Add a new universite
	addUniversite: async (data: Universite) => {
		set({ loading: true, error: null });
		try {
			const response = await axios.post<Universite>(
				`${api}/universite/add-universite`,
				data
			);
			set((state) => ({
				universites: [...state.universites, response.data],
				loading: false,
			}));
		} catch (err: any) {
			set({ error: err.message || "Failed to add universite", loading: false });
		}
	},
	// Update an existing universite
	updateUniversite: async (data: Universite) => {
		set({ loading: true, error: null });
		try {
			const response = await axios.put<Universite>(
				`${api}/universite/update-universite`,
				data
			);
			set((state) => ({
				universites: state.universites.map((univ) =>
					univ.idUniv === data.idUniv ? response.data : univ
				),
				loading: false,
			}));
		} catch (err: any) {
			set({ error: err.message || "Failed to update universite", loading: false });
		}
	},
	// Delete a universite
	deleteUniversite: async (id: number) => {
		set({ loading: true, error: null });
		try {
			await axios.delete(`${api}/universite/remove-universite/${id}`);
			set((state) => ({
				universites: state.universites.filter((univ) => univ.idUniv !== id),
				loading: false,
			}));
		} catch (err: any) {
			set({ error: err.message || "Failed to delete universite", loading: false });
		}
	},
}));
