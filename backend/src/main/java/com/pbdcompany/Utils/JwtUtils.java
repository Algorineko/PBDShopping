package com.pbdcompany.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    // 提取用户类型
    public String extractUserType(String token) {
        return extractClaim(token, claims -> (String) claims.get("userType"));
    }

    // 从配置文件中注入密钥（推荐）
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // 设置过期时间（24小时）
    private static final long JWT_EXPIRATION = 86400000;



    // 提取指定声明
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 生成 Token（带自定义声明）
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 从请求中提取 Token
    public static String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // 去除 "Bearer " 前缀
        }
        return null;
    }

    public int extractCustomerId(HttpServletRequest request) {
        String token = parseJwt(request); // 从请求头中提取 JWT
        if (token == null || !isTokenValid(token)) {
            throw new RuntimeException("Invalid or missing token");
        }
        return extractCustomerId(token);
    }

    // 提取 customerId
    public int extractCustomerId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("customerId", Integer.class)
                ;
    }

    // 校验 Token 是否有效
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 检查 Token 是否过期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 提取过期时间
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 提取所有声明
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 获取签名密钥
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public int extractMerchantId(String token) {
        return extractClaim(token, claims -> claims.get("merchantId", Integer.class));
    }


    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }


}
