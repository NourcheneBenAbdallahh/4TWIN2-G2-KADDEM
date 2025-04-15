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
import { Pencil } from "lucide-react";
import { useUniversiteStore, Universite } from "@/store/universiteStore";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { toast } from "react-toastify";
import { useState } from "react";

const universiteSchema = yup.object().shape({
  nomUniv: yup.string().required("University name is required"),
});

export function EditUniversite({ universite }: { universite: Universite }) {
  const { updateUniversite } = useUniversiteStore();
  const [isOpen, setIsOpen] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(universiteSchema),
    defaultValues: {
      nomUniv: universite.nomUniv,
    },
  });

  const onSubmit = (data: any) => {
    try {
      const updatedUniversite = {
        ...universite,
        nomUniv: data.nomUniv,
      };
      updateUniversite(updatedUniversite);
      toast.success("University updated successfully", {
        position: "top-right",
        autoClose: 2000,
      });
      reset();
      setIsOpen(false);
    } catch (error) {
      toast.error("Error updating university", {
        position: "top-right",
        autoClose: 2000,
      });
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button className="bg-cyan-500 text-white" variant="outline">
          <Pencil />
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px] p-10">
        <DialogHeader>
          <DialogTitle>Edit {universite.nomUniv}</DialogTitle>
          <DialogDescription>
            Make changes to the university here. Click save when you're done.
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col space-y-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="nomUniv" className="text-right">
              Name
            </Label>
            <Input
              id="nomUniv"
              {...register("nomUniv")}
              className="col-span-3"
            />
            {errors.nomUniv && (
              <p className="text-red-500 col-span-3">{errors.nomUniv.message}</p>
            )}
          </div>
          <DialogFooter>
            <Button type="submit">Save changes</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}