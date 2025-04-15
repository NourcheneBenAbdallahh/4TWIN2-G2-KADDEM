import { useEffect } from "react";
import { OneUniversite } from "./components/OneUniversite";
import { useUniversiteStore } from "@/store/universiteStore";
import { AddUniversite } from "./components/AddUniversite";

export const UniversitesPage = () => {
  const { universites, fetchUniversites } = useUniversiteStore();

  useEffect(() => {
    fetchUniversites();
  }, []);

  return (
    <div className="h-full w-full space-y-4 p-4">
      <AddUniversite />
      <div className="grid auto-rows-min gap-4 lg:grid-cols-2 xl:grid-cols-3">
        {universites.length > 0 &&
          universites.map((universite) => (
            <OneUniversite key={universite.idUniv} universite={universite} />
          ))}
      </div>
    </div>
  );
};