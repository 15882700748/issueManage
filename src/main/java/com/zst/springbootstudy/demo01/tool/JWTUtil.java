package com.zst.springbootstudy.demo01.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    public static final String SECRET = "JKKLJOoasdlfj";

    public static final Long EXPIRE = 5 * 60 * 1000L;

    public static String createToken(Long userId) throws Exception {
        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP").withClaim("user_id", null == userId ? null : userId.toString())
                //.withIssuedAt() // sign time
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE)) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }

    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //token 校验失败, 抛出Token验证非法异常
            e.printStackTrace();
        }
        return jwt.getClaims();
    }

    public static Long getUserId(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim userIdClaim = claims.get("user_id");
        if (null == userIdClaim || StringUtils.isEmpty(userIdClaim.asString()))       {
            // token 校验失败, 抛出Token验证非法异常
        }
        return Long.valueOf(userIdClaim.asString());
    }
}
