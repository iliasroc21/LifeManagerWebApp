import { createBrowserRouter , RouterProvider } from "react-router-dom";
import {Login , Register , Error , HomeLayout ,Dashboard, VerifyUser} from './pages'
import { ErrorElement } from "./components";
const routes = createBrowserRouter([
  {
    path:"/",
    element : <HomeLayout/>,
    errorElement :<Error/>,
    children : [
      {
        index : true ,  
        element : <Dashboard/>, 
        errorElement : <ErrorElement/>
      }
    ]
  },
  {
    path:'/login',
    element: <Login/>,
    errorElement : <Error/>
  },
  {
    path:"/register",
    element:<Register/>,
    errorElement :<Error/>
  },
  {
    path :"/verify",
    element : <VerifyUser/>,
    errorElement : <Error/>
  }
])
function App() {
  return <RouterProvider router={routes}/>
}

export default App
