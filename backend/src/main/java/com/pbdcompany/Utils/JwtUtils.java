package com.pbdcompany.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    // 判断是否是管理员
    public boolean isAdmin(String token) {
        String userType = extractUserType(token);
        return "admin".equalsIgnoreCase(userType);
    }

    // 判断是否是商户
    public boolean isMerchant(String token) {
        String userType = extractUserType(token);
        return "merchant".equalsIgnoreCase(userType);
    }

    // 判断是否是普通用户
    public boolean isCustomer(String token) {
        String userType = extractUserType(token);
        return "customer".equalsIgnoreCase(userType);
    }


    // 从配置文件中注入密钥（推荐）
    @Value("${jwt.secret}")
    private static String SECRET_KEY;

    // 设置过期时间（24小时）
    private final long JWT_EXPIRATION = 86400000;

    // 提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 提取用户 ID（假设你在 token 中存储了 customerId）
    public static int extractCustomerId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("customerId", Integer.class);
    }

    // 提取指定声明
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // 生成 Token（带自定义声明）
    public static String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 校验 Token 是否有效
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
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
    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 获取签名密钥
    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public int extractMerchantId(String token) {
        return extractClaim(token, claims -> claims.get("merchantId", Integer.class));
    }

}
