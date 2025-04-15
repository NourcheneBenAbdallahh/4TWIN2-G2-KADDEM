import { useEffect } from "react";
import { OneSupplier } from "./components/oneSupplier";
import { useSupplierStore } from "@/store/supplierStore";
import { AddSupplier } from "./components/AddSupplier";
export const SuppliersPage = () => {
  const { suppliers, fetchSuppliers } = useSupplierStore();
  console.log(suppliers);
  useEffect(() => {
    fetchSuppliers();
  }, []);
  return (
    <>
      <div className="h-full w-full space-y-4 p-4">
        <AddSupplier />
        <div className="grid auto-rows-min gap-4 lg:grid-cols-2 xl:grid-cols-3   ">
          {suppliers.length > 0 &&
            suppliers.map((each, index) => (
              <OneSupplier supplier={each} key={index} />
            ))}
        </div>
      </div>
    </>
  );
};
