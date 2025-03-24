package org.orh.jwt;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Date;

public class JwtExample {

    // 生成一个密钥
    private static final String SECRET_KEY = "your-256-bit-secret-key-here-must-be-long-enough";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(String username, String phoneNumber) {
        return Jwts.builder()
                .compressWith(Jwts.ZIP.DEF)
                .claim("username", username)
                .claim("phoneNumber", phoneNumber)
                .issuedAt(new Date())
                .expiration(DateUtil.offsetSecond(new Date(), 10))
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static void main(String[] args) throws InterruptedException {
        String token = generateToken("张三", "13800138000");
        System.out.println("生成的Token: " + token);

        Claims claims = parseToken(token);
        System.out.println("用户名: " + claims.get("username"));
        System.out.println("手机号: " + claims.get("phoneNumber"));

        PasswordEncoder pe = new BCryptPasswordEncoder();
        //加密
        String encode = pe.encode("hello");
        System.out.println(encode);
        //比较密码
        boolean matches = pe.matches("hello",encode);
        System.out.println("===================================");
        System.out.println(matches);

        Thread.sleep(10000);
        claims = parseToken(token);
        System.out.println("用户名: " + claims.get("username"));
        System.out.println("手机号: " + claims.get("phoneNumber"));
    }


}
