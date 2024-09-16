import { FormEvent, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/components/ui/input-otp";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { useResendVerifcationMutation, useVerifyUserMutation } from "@/features/auth/authApiSlice";
import { useNavigate } from "react-router-dom";
import { Toast, useToast } from "@/hooks/use-toast"
import { errorAlert , successAlert } from "@/lib/utils";

const VerifyUser = () => {
  const [searchParams] = useSearchParams();
  const email = searchParams.get("email");
  const [otpValue, setOtpValue] = useState(); // For handling OTP input
  const [verifyUser ] = useVerifyUserMutation();
  const [resendVerifcation] = useResendVerifcationMutation();
  const navigate = useNavigate();
  const {toast} = useToast()
    const handleAlert =(alert : Toast)=>{
        toast(alert)
  }
  const handleVerificationSubmit = async(event: FormEvent) => {
    event.preventDefault();
    try{
      const response = await verifyUser({email , verificationCode : otpValue}).unwrap();
      console.log(response);
      handleAlert(successAlert("Account Verified Successfully"))
      navigate("/");
      
    }
    catch(err: any){
      console.log(err)
      handleAlert(errorAlert((err)?.data?.message || 'An unknown error occurred.'));
    }
    console.log("OTP Submitted:", otpValue);
  };
  const resendVerificationCode = async(event : FormEvent)=>{
    try{
      const response= await resendVerifcation({email : email});
      console.log(response)
      handleAlert(successAlert("new Code Send to your Email"))
    }
    catch(err: any){
      console.log(err);
      handleAlert(errorAlert(err?.error?.data?.message || 'An unknown error occurred.'));
    }
  }

  return (
    <Card className="h-full bg-accent mx-auto flex flex-col  justify-items-center  my-20 max-w-sm">
      <CardHeader>
        <CardTitle className="text-xl">Email Verification</CardTitle>
        <CardDescription className="text-center text-md font-semibold">
          We’ve sent a verification code to: {email}
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleVerificationSubmit} className="space-y-4 mx-auto w-3/4">
          <InputOTP className="w-full mx-auto" maxLength={6} value={otpValue} onChange={(value :any) => setOtpValue(value)}>
            <InputOTPGroup>
              <InputOTPSlot index={0} />
              <InputOTPSlot index={1} />
              <InputOTPSlot index={2} />
              <InputOTPSlot index={3} />
              <InputOTPSlot index={4} />
              <InputOTPSlot index={5} />
            </InputOTPGroup>
          </InputOTP>

          <Button type="submit" className="w-full">
            Verify Email
          </Button>
        </form>
        <div className="mt-4 text-center text-sm">
          Didn’t receive a code?
          <Button className="mt-4 text-center text-sm" onClick={resendVerificationCode} variant="link">Resend</Button>
        </div>
        
      </CardContent>
    </Card>
  );
};

export default VerifyUser;
