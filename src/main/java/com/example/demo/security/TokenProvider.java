package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

// 토큰 생성 및 검증하는 기능 제공 (빈으로 등록)
@Slf4j
@Service
public class TokenProvider {

    // 전자 서명에 필요한 시크릿키 생성하기
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 토큰 생성하는 메서드
    public String create(UserEntity userEntity) {
        // 기한은 지금부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                .plus(1, ChronoUnit.DAYS));

        /*
        { // header
            "alg":"HS512"
        }.
        { // payload
            "sub": "토큰의 주인인 사용자 식별자 아이디",
            "iss": "demo app",
            "iat": 1595733657,
            "exp": 1596597657
        }.
        // SECRET_KEY를 이용해 서명한 부분
        JFISJEIFiji4KJFIDFiji...
         */
        log.info("secret key: {}", SECRET_KEY);
        // 사용자 정보를 이용하여 JWT Toekn 생성하기
        return Jwts.builder()
                // payload의 일부로 원하는 key:value 형태로 커스터마이징해서 넣을 수 있다.
                .claim("id", userEntity.getId()) // 사용자 PK ID
                .issuer("demo app")  // 발행자
                .issuedAt(new Date()) // 발행한 날짜
                .expiration(expiryDate) // 만료 날짜
                .signWith(SECRET_KEY, Jwts.SIG.HS512) // 서명할 때 쓰이는 비밀키와 HMAC-SHA512 해쉬 알고리즘
                .compact(); // jwt token 생성
    }

    /*
        사용자로부터 받은 토큰을 검증하는 메서드이다.
        1. 사용자의 토큰 디코딩 (알고리즘 추론)
        2. {header}.{payload} 시크릿키와 알고리즘으로 전자서명 수행
        3. 사용자의 토큰 서명과 비교해보기
        4. 틀리면 에러 던진다.
    */
    public String validateAndGetUserId(String token) {
        // 검증과정
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(SECRET_KEY).build()
                .parseSignedClaims(token);
        // payload 추출
        Claims claims = claimsJws.getBody();
        // 페이로드에서 id 클레임 리턴
        return claims.get("id", String.class);
    }
}
