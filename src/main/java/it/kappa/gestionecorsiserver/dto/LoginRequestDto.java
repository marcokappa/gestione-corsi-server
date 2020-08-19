package it.kappa.gestionecorsiserver.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "L'indirizzo e-mail non può essere vuoto")
    @Email(message = "Fornire un indirizzo e-mail valido")
    private String email;

    @NotBlank(message = "La password non può essere vuota")
    private String password;
}
