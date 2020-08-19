package it.kappa.gestionecorsiserver.service;

import org.springframework.http.ResponseEntity;

import it.kappa.gestionecorsiserver.dto.LoginRequestDto;
import it.kappa.gestionecorsiserver.dto.LoginResponseDto;
import it.kappa.gestionecorsiserver.dto.UtenteDto;

public interface UtenteService {
    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponseDto> refresh(String accessToken, String refreshToken);

    UtenteDto getUtenteDto();

    UtenteDto registaAllievo(UtenteDto allievo);
}
