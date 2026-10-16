package com.smartbank.config;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import javax.crypto.SecretKey; import org.slf4j.LoggerFactory; import org.springframework.beans.factory.annotation.Value; import org.springframework.core.env.Environment; import org.springframework.core.env.Profiles; import org.springframework.stereotype.Service; import java.nio.charset.StandardCharsets; import java.security.SecureRandom; import java.util.Date;
@Service public class JwtService {
 private final SecretKey key; private final long expiration;
 public JwtService(@Value("${app.jwt.secret:}") String secret,@Value("${app.jwt.expiration-ms}") long expiration,Environment environment){
  if(secret==null||secret.isBlank()){
   if(environment.acceptsProfiles(Profiles.of("prod")))throw new IllegalStateException("JWT_SECRET must be set to a random value of at least 32 characters in the prod profile.");
   byte[] ephemeral=new byte[32];new SecureRandom().nextBytes(ephemeral);this.key=Keys.hmacShaKeyFor(ephemeral);LoggerFactory.getLogger(JwtService.class).warn("JWT_SECRET is unset; using a temporary development key. Tokens expire when this server restarts.");
  }else{byte[] bytes=secret.getBytes(StandardCharsets.UTF_8);if(bytes.length<32)throw new IllegalStateException("JWT_SECRET must contain at least 32 bytes.");this.key=Keys.hmacShaKeyFor(bytes);}
  this.expiration=expiration;
 }
 public String create(String email,String role){Date now=new Date();return Jwts.builder().subject(email).claim("role",role).issuedAt(now).expiration(new Date(now.getTime()+expiration)).signWith(key).compact();}
 public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
}
