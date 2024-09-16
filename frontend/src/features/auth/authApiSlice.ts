import { apiSlice } from "@/app/api/apiSlice";

export const authApiSlice = apiSlice.injectEndpoints({
    endpoints : builder =>({
        login : builder.mutation({
            query : credentials =>({
                url : '/auth/authenticate',
                method:'POST' ,
                body :{ ...credentials}
            })
        }),
        register : builder.mutation({
            query : credentials =>({
                url : '/auth/register',
                method: 'POST',
                body : {...credentials}
            })
        }),
        verifyUser : builder.mutation({
            query : credentials =>({
                url :'/auth/verify',
                method: 'POST',
                body : {...credentials}
            })
        }),
        resendVerifcation : builder.mutation({
            query : credentials =>({
                url : `/auth/resend-verification?email=${encodeURIComponent(credentials.email)}`,
                method : 'POST',
                body  :{...credentials}
            })
        })
    
    })
})
export const {useLoginMutation  , useRegisterMutation  , useVerifyUserMutation , useResendVerifcationMutation} = authApiSlice