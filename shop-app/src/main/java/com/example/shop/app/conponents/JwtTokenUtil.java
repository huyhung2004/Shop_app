package com.example.shop.app.conponents;

import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(User user) throws InvalidParamException {
        Map<String,Object> claims=new HashMap<>();
        claims.put("phoneNumber",user.getPhoneNumber());
//        generateSecretKey();
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                    .signWith(getSignInKey())
                    .compact();
        }catch (Exception e){
            throw new InvalidParamException("Can't create jwt token,error: "+e.getMessage());
        }
    }
    private SecretKey getSignInKey(){
        byte[] bytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
//    // Phương thức để tạo khóa bí mật
//    public static String generateSecretKey() {
//        try {
//            // Tạo đối tượng SecureRandom để tạo số ngẫu nhiên an toàn
//            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
//
//            // Tạo mảng byte để lưu trữ khóa bí mật
//            byte[] keyBytes = new byte[32]; // 256 bits
//            secureRandom.nextBytes(keyBytes);
//
//            // Mã hóa khóa bí mật thành chuỗi Base64
//            return Base64.getEncoder().encodeToString(keyBytes);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error while generating secret key", e);
//        }
//    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey()) // Thiết lập khóa bí mật để xác minh chữ ký
                .build() // Xây dựng JwtParser
                .parseClaimsJws(token) // Phân tích JWT và lấy JWS
                .getBody(); // Lấy phần body chứa các claims
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
    public String extractPhoneNumber(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber=extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))&&!isTokenExpired(token);
    }
}
