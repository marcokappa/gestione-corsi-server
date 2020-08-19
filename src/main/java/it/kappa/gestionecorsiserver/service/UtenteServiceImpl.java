package it.kappa.gestionecorsiserver.service;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.kappa.gestionecorsiserver.domain.Autorizzazione;
import it.kappa.gestionecorsiserver.domain.Roles;
import it.kappa.gestionecorsiserver.domain.Utente;
import it.kappa.gestionecorsiserver.dto.LoginRequestDto;
import it.kappa.gestionecorsiserver.dto.LoginResponseDto;
import it.kappa.gestionecorsiserver.dto.TokenDto;
import it.kappa.gestionecorsiserver.dto.UtenteDto;
import it.kappa.gestionecorsiserver.repository.AutorizzazioneRepository;
import it.kappa.gestionecorsiserver.repository.UtenteRepository;
import it.kappa.gestionecorsiserver.security.CustomUserDetails;
import it.kappa.gestionecorsiserver.security.TokenProvider;
import it.kappa.gestionecorsiserver.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {
    private final UtenteRepository utenteRepository;
    private final AutorizzazioneRepository autorizzazioneRepository;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequest, String accessToken, String refreshToken) {
	String email = loginRequest.getEmail();
	Utente utente = utenteRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Utente non trovato con email " + email));

	Boolean accessTokenValid = accessToken != null ? tokenProvider.validateToken(accessToken) : false;
	Boolean refreshTokenValid = refreshToken != null ? tokenProvider.validateToken(refreshToken) : false;

	HttpHeaders responseHeaders = new HttpHeaders();
	TokenDto newAccessToken;
	TokenDto newRefreshToken;
	if (!accessTokenValid && !refreshTokenValid) {
	    newAccessToken = tokenProvider.generateAccessToken(utente.getEmail());
	    newRefreshToken = tokenProvider.generateRefreshToken(utente.getEmail());
	    addAccessTokenCookie(responseHeaders, newAccessToken);
	    addRefreshTokenCookie(responseHeaders, newRefreshToken);
	}

	if (!accessTokenValid && refreshTokenValid) {
	    newAccessToken = tokenProvider.generateAccessToken(utente.getEmail());
	    addAccessTokenCookie(responseHeaders, newAccessToken);
	}

	if (accessTokenValid && refreshTokenValid) {
	    newAccessToken = tokenProvider.generateAccessToken(utente.getEmail());
	    newRefreshToken = tokenProvider.generateRefreshToken(utente.getEmail());
	    addAccessTokenCookie(responseHeaders, newAccessToken);
	    addRefreshTokenCookie(responseHeaders, newRefreshToken);
	}

	LoginResponseDto loginResponse = new LoginResponseDto(LoginResponseDto.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
	return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);

    }

    @Override
    public ResponseEntity<LoginResponseDto> refresh(String accessToken, String refreshToken) {
	Boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
	if (!refreshTokenValid) {
	    throw new IllegalArgumentException("Refresh Token is invalid!");
	}

	String currentUserEmail = tokenProvider.getUsernameFromToken(accessToken);

	TokenDto newAccessToken = tokenProvider.generateAccessToken(currentUserEmail);
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

	LoginResponseDto loginResponse = new LoginResponseDto(LoginResponseDto.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
	return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @Override
    public UtenteDto getUtenteDto() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

	Utente utente = utenteRepository.findByEmail(customUserDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found with email " + customUserDetails.getUsername()));
	return utente.getUtenteDto();
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, TokenDto token) {
	httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, TokenDto token) {
	httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    @Override
    @Transactional
    public UtenteDto registaAllievo(UtenteDto allievo) {
	String email = Optional.ofNullable(allievo.getEmail()).orElseThrow(() -> new IllegalArgumentException("Utente con email vuota"));
	String password = Optional.ofNullable(allievo.getPassword()).orElseThrow(() -> new IllegalArgumentException("Utente con password vuota"));

	if (utenteRepository.findByEmail(email).isPresent()) {
	    throw new IllegalArgumentException("Utente gia' presente nel sistema con email " + email);
	}

	Autorizzazione autorizzazione = autorizzazioneRepository.findByAuthorityName(Roles.ALLIEVO).orElseThrow(() -> new IllegalArgumentException("Ruolo 'ALLIEVO' non trovato"));

	PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	Utente utente = Utente.builder()//
		.autorizzazione(autorizzazione)//
		.email(allievo.getEmail())//
		.password(bcryptEncoder.encode(password))//
		.isEnabled(true)//
		.build();

	return utenteRepository.saveAndFlush(utente).getUtenteDto();
    }
}
