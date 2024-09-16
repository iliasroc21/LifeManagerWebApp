import { RootState } from "@/app/store";
import { createSlice } from "@reduxjs/toolkit";
import {User} from 'src/lib/utils'
type UserSession = {
    user: User |null  ,
    token :string |null 
}
const defaultState : UserSession = {
    user  : null , 
    token : null 
}
const authSlice = createSlice({
    name : "auth" , 
    initialState : defaultState , 
    reducers : {
        setCredentials : (state , action)=>{
            
            const {user , accessToken } = action.payload ; 
            state.user = user as User ;
            state.token = accessToken as string;
             
        }, 
        logOut : (state )=>{
            state.user =null ; 
            state.token = null ; 
        }
    }
})
export const {setCredentials , logOut}  =  authSlice.actions;
export default authSlice.reducer ;  
export const selectCurrentUser = (state : RootState ) : User | null =>state.auth.user 
export const selectCurrentToken = (state :RootState) : string | null =>state.auth.token 
