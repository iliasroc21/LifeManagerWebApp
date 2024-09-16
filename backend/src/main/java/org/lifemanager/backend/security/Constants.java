package org.lifemanager.backend.security;

public class Constants {
    public static  final Long MAX_AGE = 3600L;
    public static final  int CORS_FILTER_ORDER =-102;
    public static final String[] WHITE_LIST = {
            "/api/v1/auth/register" ,
            "/api/v1/auth/refresh-token"
    };
    public static final String AUTHENTICATE_PATH = "/api/v1/auth/authenticate";
    public static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    public static final long JWT_EXPIRATION = 60000; // 60 seconds in milliseconds
    public static final long REFRESH_EXPIRATION= 900000;
    public static final long VERIFICATION_EXPIRATION = 15 ;
    public static final long RESEND_VERIFICATION_EXPIRATION = 1;
}
