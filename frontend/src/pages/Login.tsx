import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import thumbnail from '../assets/thumbnail.png'
import {useState , useRef , useEffect, FormEvent} from 'react'
import { useNavigate } from "react-router-dom"
import { useDispatch, UseDispatch } from "react-redux"
import { setCredentials  } from "@/features/auth/authSlice"
import { useLoginMutation } from "@/features/auth/authApiSlice"

const Login = () => {
  const userRef = useRef<HTMLInputElement>(null);
  const errRef = useRef();
  const [email ,setEmail ] = useState('');
  const [password  ,setPassword ]= useState('')
  const navigate = useNavigate();
  const [login , { isLoading}] = useLoginMutation();
  const dispatch = useDispatch();
  useEffect(()=>{
    userRef?.current?.focus()
  },[])
  const handleSubmit = async(e : FormEvent)=>{
    e.preventDefault();
    try{
      const userData = await login({email, password }).unwrap();
      dispatch(setCredentials({user: userData.user , ...userData}))
      setEmail("")
      setPassword("")
      navigate("/")
    }
    catch(err){
      console.log(err);
    }

  }
  const content = isLoading ? <h1>Loading ....</h1> : 
  (
    <div className="w-full lg:grid h-screen lg:grid-cols-2 ">
      <div className="flex items-center justify-center py-12">
        <div className="mx-auto grid w-[350px] gap-6">
          <div className="grid gap-2 text-center">
            <h1 className="text-3xl font-bold">Login</h1>
            <p className="text-balance text-muted-foreground">
              Enter your email below to login to your account
            </p>
          </div>
          <div className="grid gap-4">
            <div className="grid gap-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="m@example.com"
                required
                ref={userRef}
                onChange={(e)=>setEmail(e.target.value)}
              />
            </div>
            <div className="grid gap-2">
              <div className="flex items-center">
                <Label htmlFor="password">Password</Label>
                <Link
                  to="/forgot-password"
                  className="ml-auto inline-block text-sm underline"
                >
                  Forgot your password?
                </Link>
              </div>
              <Input id="password" type="password" onChange={(e)=>setPassword(e.target.value)} required />
            </div>
            <Button onClick={handleSubmit} type="submit" className="w-full">
              Login
            </Button>
            <Button variant="outline" className="w-full">
              Login with Google
            </Button>
          </div>
          <div className="mt-4 text-center text-sm">
            Don&apos;t have an account?{" "}
            <Link to="/signup" className="underline">
              Sign up
            </Link>
          </div>
        </div>
      </div>
      <div className="hidden bg-muted lg:block">
        <img
          src={thumbnail}
          alt="Image"
          className="h-full w-full object-cover dark:brightness-[0.2] dark:grayscale"
        />
      </div>
    </div>
  )
  return content ;
}

export default Login
