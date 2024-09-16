import { createApi, fetchBaseQuery, BaseQueryFn } from "@reduxjs/toolkit/query/react";
import { setCredentials, logOut } from "@/features/auth/authSlice";
import { RootState } from "../store"; 

// Define base query
const baseQuery = fetchBaseQuery({
  baseUrl: "http://localhost:8080/api/v1",
  credentials: "include",
  prepareHeaders: (headers: Headers, { getState }) => {
    const token = (getState() as RootState).auth.token;
    if (token) {
      headers.set("Authorization", `Bearer ${token}`);
    }
    headers.set('Content-Type', 'application/json');
    console.log(headers)

    return headers;
  },
});

//Define base Query After Token expiration
const baseQueryWithReAuth: BaseQueryFn = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  if (result?.error?.status === 403) {
    console.log("Sending refresh token");
    // Send Refresh Token to get new Access Token
    const refreshResult = await baseQuery("/auth/refresh-token", api, extraOptions);
    console.log(refreshResult);
    if (refreshResult?.data) {
      const user = (api.getState() as RootState).auth.user;
      api.dispatch(setCredentials({ user, ...refreshResult.data }));
      result = await baseQuery(args, api, extraOptions);
    } else {
      // If refresh fails, log out the user
      api.dispatch(logOut());
    }
  }

  return result;
};

export const apiSlice = createApi({
    baseQuery : baseQueryWithReAuth , 
    endpoints : builder => ({
      //I will inject Them in the each section Slice
    })
})