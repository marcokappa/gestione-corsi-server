package it.kappa.gestionecorsiserver.dto;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UtenteDto {
    @Email
    private String email;
    private String password;
    private String ruolo;
}
