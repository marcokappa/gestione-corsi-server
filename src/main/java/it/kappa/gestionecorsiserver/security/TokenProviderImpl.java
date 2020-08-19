package it.kappa.gestionecorsiserver.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import it.kappa.gestionecorsiserver.dto.TokenDto;

@Service
class TokenProviderImpl implements TokenProvider {

    @Value("${authentication.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${authentication.auth.tokenExpirationMsec}")
    private Long tokenExpirationMsec;

    @Value("${authentication.auth.refreshTokenExpirationMsec}")
    private Long refreshTokenExpirationMsec;

    @Override
    public TokenDto generateAccessToken(String subject) {
	Date now = new Date();
	Long duration = now.getTime() + tokenExpirationMsec;
	Date expiryDate = new Date(duration);
	String token = Jwts.builder().setSubject(subject).setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
	return new TokenDto(TokenDto.TokenType.ACCESS, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public TokenDto generateRefreshToken(String subject) {
	Date now = new Date();
	Long duration = now.getTime() + refreshTokenExpirationMsec;
	Date expiryDate = new Date(duration);
	String token = Jwts.builder().setSubject(subject).setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
	return new TokenDto(TokenDto.TokenType.REFRESH, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public String getUsernameFromToken(String token) {
	Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
	return claims.getSubject();
    }

    @Override
    public LocalDateTime getExpiryDateFromToken(String token) {
	Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
	return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    @Override
    public boolean validateToken(String token) {
	try {
	    Jwts.parser().setSigningKey(tokenSecret).parse(token);
	    return true;
	} catch (MalformedJwtException ex) {
	    ex.printStackTrace();
	} catch (ExpiredJwtException ex) {
	    ex.printStackTrace();
	} catch (UnsupportedJwtException ex) {
	    ex.printStackTrace();
	} catch (IllegalArgumentException ex) {
	    ex.printStackTrace();
	}
	return false;
    }

}
