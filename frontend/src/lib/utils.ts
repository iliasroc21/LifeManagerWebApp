import { type ClassValue, clsx } from "clsx"
import {Toast} from '@/hooks/use-toast'
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export type User =  {
  email : string ,
  firstname :string , 
  lastname :string ,
  
  
}

export const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
export const PWD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/;

export const verificationEmailAlert : Toast ={
  variant: "default",
  description: "Friday, February 10, 2023 at 5:57 PM",
  className:"bg-primary",
  style: {
    position: "fixed",
    top: "20px", 
    width : "33.33vw", 
    left: "50%",
    transform: "translateX(-50%)", 
    zIndex: 9999, 
  }
}
export const errorAlert = (message  :string) : Toast => {
  return {
    description: message,
    variant: "destructive",
    style: {
      position: "fixed",
      top: "20px", 
      width : "33.33vw", 
      left: "50%",
      transform: "translateX(-50%)", 
      zIndex: 9999, 
    }  
  };
}
export const successAlert = (message: string): Toast => {
  return {
    description: message,
    style: {
      position: "fixed",
      top: "20px",
      width: "33.33vw",
      left: "50%",
      transform: "translateX(-50%)",
      zIndex: 9999,
      backgroundColor: "#4caf50", // Optional: set success color (green)
      color: "#fff", // Optional: text color to make it stand out
    },
  };
};
