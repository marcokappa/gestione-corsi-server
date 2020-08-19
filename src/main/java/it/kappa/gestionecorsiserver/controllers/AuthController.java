package it.kappa.gestionecorsiserver.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.kappa.gestionecorsiserver.dto.LoginRequestDto;
import it.kappa.gestionecorsiserver.dto.LoginResponseDto;
import it.kappa.gestionecorsiserver.dto.UtenteDto;
import it.kappa.gestionecorsiserver.service.UtenteService;
import it.kappa.gestionecorsiserver.util.SecurityCipher;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtenteService utenteService;

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    ResponseEntity<?> collectionOptions() {
	return ResponseEntity.ok().allow( HttpMethod.POST, HttpMethod.OPTIONS).build();
    }

    

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDto> login(//
	    @CookieValue(name = "accessToken", required = false) //
	    String accessToken, //

	    @CookieValue(name = "refreshToken", required = false) //
	    String refreshToken, //

	    @Valid //
	    @RequestBody //
	    LoginRequestDto loginRequest) {
	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authentication);

	String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
	String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
	return utenteService.login(loginRequest, decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDto> refreshToken(//
	    @CookieValue(name = "accessToken", required = false) //
	    String accessToken, //

	    @CookieValue(name = "refreshToken", required = false) //
	    String refreshToken) {
	String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
	String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
	return utenteService.refresh(decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping(value = "/register/allievo")
    public ResponseEntity<?> saveUser(@Valid @NotNull @RequestBody UtenteDto allievo) throws Exception {
	return ResponseEntity.ok(utenteService.registaAllievo(allievo));
    }
}
