package it.kappa.gestionecorsiserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.kappa.gestionecorsiserver.dto.UtenteDto;
import it.kappa.gestionecorsiserver.service.UtenteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;

    @GetMapping("/me")
    public ResponseEntity<UtenteDto> me() {
	return ResponseEntity.ok(utenteService.getUtenteDto());
    }

}
