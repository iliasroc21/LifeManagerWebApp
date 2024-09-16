import { Navigate , useLocation , Outlet } from "react-router-dom"
import { useSelector, UseSelector } from "react-redux"
import { selectCurrentToken } from "./authSlice"
const RequireAuth = () => {
  const token = useSelector(selectCurrentToken);
  const location = useLocation();
  return (
    token ? <Outlet/> : <Navigate to="/login" state={{from:location}} replace/>
  )
}

export default RequireAuth
