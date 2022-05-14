package com.fifteen.webproject.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 什锦
 * @Package com.epoch.digitalfarming.utils.token
 * @since 2022/1/26 0:26
 * token create util
 */

public class TokenUtils {


    /**
     * token should have a expire time
     */
    private static final long EXPIRE_DATE=1000 * 60 * 60 * 24;

    /**
     * token secret value
     */
    private static final String TOKEN_SECRET = "ZCfasfhuaUUHufguGuwu2020BQWE";


    /**
     * create a token by jwt
     * @param  userId user's Id
     * @param username username
     * @return token
     */
    public static String getToken (String username,Integer userId){
        String token = "";
        try {
            //set expire time
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //secret key and secret method
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //set header params
            Map<String,Object> header = new HashMap<>(4);
            header.put("typ","JWT");
            header.put("alg","HS256");
            //use userId and username create autograph
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("userId",userId)
                    .withClaim("username",username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000 * 60 * 12 * 4000))
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    /**
     * Verify whether the token is legal
     * @param token token
     * @return boolean
     */
    public static boolean verifyToken(String token){
        /**
         * @desc   verify token，if not exist error will return true
         * @params [token]
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * Check whether this token is valid
     * @param request request
     * @return boolean
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");

            // 'isempty (Java. Lang. object)' is out of date,You can choose hasLength or hasText instead
            if(!StringUtils.hasLength(jwtToken)) {
                return false;
            }
            if (!verifyToken(jwtToken)) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Obtain user information according to the token
     * @param request
     * @return
     */
    public static Integer getUserIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(!StringUtils.hasLength(jwtToken)) {
            return 0;
        }
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(jwtToken);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("userId");
        return claim.asInt();
    }

    /**
     * Generate 4-bit random string
     */
    public static String getCheckCode() {
        String base = "0123456789ABCDEFGabcdefg";
        int size = base.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 4; i++) {
            //产生0到size-1的随机值
            int index = r.nextInt(size);
            //在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            //将c放入到StringBuffer中去
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        boolean b = verifyToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NDU2MTU2MjksInVzZXJJZCI6OCwidXNlcm5hbWUiOiJ0aW5nIn0.WGkMiiaDiPYkZPnyW2HKElKTXULQkoOHx0EvIecZG94\n");
        boolean jwtToken = !StringUtils.hasLength("jwtToken");
        System.out.println(jwtToken);
        System.out.println(b);
    }
}
