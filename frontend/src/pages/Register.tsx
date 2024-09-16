import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import {Info} from 'lucide-react'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useState, useRef, useEffect, FormEvent } from 'react'
import { EMAIL_REGEX, PWD_REGEX } from "@/lib/utils"
import { useRegisterMutation } from "@/features/auth/authApiSlice"
import { useDispatch } from "react-redux"
import {useNavigate} from 'react-router-dom'
import { Toast, useToast } from "@/hooks/use-toast"
import { verificationEmailAlert , errorAlert } from "@/lib/utils"
import { setCredentials } from "@/features/auth/authSlice"

const Register = () => {
    //states
    
    const [firstname , setFirstname]= useState('');
    const [lastname ,setLastname] = useState('')
    const [email ,setEmail ] = useState('');
    const [pwd , setPwd] = useState('')
    const [confirmedPwd , setConfirmedPwd] = useState('')
    const [validEmail , setValidEmail] = useState(false)
    const [validPwd  , setValidPwd] = useState(false)
    const [validMatch , setValidMatch] = useState(false)
    const [emailFocus , setEmailFocus] = useState(false)
    const [pwdFocus ,setPwdFocus] = useState(false)
    const [matchFocus , setMatchFocus] = useState(false)
    //refs
    const emailRef = useRef(null)
    const errRef= useRef(null)
    //RTQ Query for registration
    const [register , {isLoading , isError , isSuccess  ,error}] = useRegisterMutation();
    const dispatch = useDispatch();
    const navigate =useNavigate()
    const {toast} = useToast()
    const handleAlert =(alert : Toast)=>{
        toast(alert)
    }
    const handleSubmit = async(event : FormEvent)=>{
        event.preventDefault();
        try{
            const userData = await register({firstname, lastname , email , password : pwd}).unwrap();
            console.log(userData);
            const userEmail = userData.user.email  ;
            const verificationToken = userData.accessToken;
            //Now I should redirect the user to another form to verify the email account 
            dispatch(setCredentials({user : userData.user ,accessToken : userData.accessToken}))
            navigate(`/verify?email=${encodeURIComponent(email)}`);
        }
        catch(err:any){
            console.log(err);
            handleAlert(errorAlert(err.data))
        }
    }
    useEffect(()=>{
        setValidEmail(EMAIL_REGEX.test(email))
    } , [email])
    useEffect(()=>{
        setValidPwd(PWD_REGEX.test(pwd))
        setValidMatch(pwd===confirmedPwd)
    },[pwd , confirmedPwd])

    const content =  (
        <Card className="h-full bg-accent mx-auto my-20 max-w-sm">
            <CardHeader>
                <CardTitle className="text-xl">Sign Up</CardTitle>
                <CardDescription>
                    Enter your information to create an account
                </CardDescription>
            </CardHeader>
            <CardContent>
                <div className="grid gap-4">
                    <div className="grid grid-cols-2 gap-4">
                        <div className="grid gap-2">
                            <Label htmlFor="first-name">First name</Label>
                            <Input id="first-name" value={firstname} onChange={(e) => setFirstname(e.target.value)} placeholder="Max" required />
                            
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="last-name">Last name</Label>
                            <Input id="last-name" placeholder="Robinson" required value={lastname} onChange={(e) => setLastname(e.target.value)} />
                        </div>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="email">Email</Label>
                        <Input
                            id="email"
                            type="email"
                            placeholder="user@example.com"
                            required
                            autoComplete="off"
                            aria-invalid={!validEmail && email  ? "true" : "false"}
                            ref={emailRef}
                            value={email} onChange={(e) => setEmail(e.target.value)}
                            onFocus={() => setEmailFocus(true)}
                            onBlur={() => setEmailFocus(false)}
                        />
                        <p className={`flex justify-self-center align-top gap-3  font-semibold  p-2 rounded-sm text-sm text-primary-content bg-primary w-full ${!validEmail &&email && emailFocus  ? "":"hidden"}`}>
                            <Info className="w-1/6" /> Email Invalid
                        </p>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="password">Password</Label>
                        <Input id="password" type="password" value={pwd} onChange={(e) => setPwd(e.target.value)}
                        aria-invalid={!validPwd && pwd  ? "true" : "false"}
                        onFocus={()=>setPwdFocus(true)}
                        onBlur={()=>setPwdFocus(false)} />
                        <p className={`flex justify-start align-center gap-3 text-sm font-semibold text-primary-content bg-primary p-2 rounded-sm text-md ${!validPwd && pwd && pwdFocus  ? "":"hidden"}`}>
                            <Info className="w-1/3" /> Password must be 8-24 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character.
                        </p>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="confirm-password">Confirm Password</Label>
                        <Input id="confirm-password" type="password" value={confirmedPwd} onChange={(e) => setConfirmedPwd(e.target.value)}
                       aria-invalid={!validMatch && confirmedPwd ? "true" : "false"}
                        onFocus={()=>setMatchFocus(true)}
                        onBlur={()=>setMatchFocus(false)} />
                        
                    </div>
                    <Button type="submit" onClick={handleSubmit}  
                    /* disabled={!validEmail || !validPwd || !validMatch || firstname.trim() === "" ||lastname.trim() === ""} */ className="w-full" >
                        {isLoading ? "Loading ...." : 'Create an account'}
                    </Button>
                    <Button variant="outline" className="w-full">
                        Sign up with GitHub
                    </Button>
                </div>
                <div className="mt-4 text-center text-sm">
                    Already have an account?{" "}
                    <Link to="/login" className="underline" >
                        Sign in
                    </Link>
                </div>
            </CardContent>
        </Card>
    )
    return content  
}

export default Register
