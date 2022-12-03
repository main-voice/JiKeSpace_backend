package com.tjsse.jikespace.utils;

import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Component
public class JwtUtil {


    public static final long JWT_TTL = 60 * 60 * 1000L * 24 * 14;  // 有效期14天
    public static final String JWT_KEY = "SDFKjhdsfals375HFdsjkdsfds12gkst131af695fac";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createJWT(String subject, String role) {
        JwtBuilder builder = getJwtBuilder(subject, role, null, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, String role, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }

        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        // self-define jwt body
        Map<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("subject", subject);

        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setClaims(map)
                .setIssuer("sg")
                .signWith(secretKey, signatureAlgorithm)
                .setIssuedAt(now)
                .setExpiration(expDate);
    }

    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static Integer getUserIdFromToken(String JK_Token) {

        if (!StringUtils.hasText(JK_Token) || !JK_Token.startsWith("Bearer ")) {
            return null;
        }

        JK_Token = JK_Token.substring(7);

        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(JK_Token);
            Map<String, Object> map = new HashMap<>(claims);
            userId = map.get("subject").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Integer.parseInt(userId);
    }
}