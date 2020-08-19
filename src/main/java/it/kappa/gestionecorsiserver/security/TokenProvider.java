package it.kappa.gestionecorsiserver.security;

import java.time.LocalDateTime;

import it.kappa.gestionecorsiserver.dto.TokenDto;

public interface TokenProvider {
    TokenDto generateAccessToken(String subject);

    TokenDto generateRefreshToken(String subject);

    String getUsernameFromToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}
