package it.kappa.gestionecorsiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private SuccessFailure status;
    private String message;

    public enum SuccessFailure {
	SUCCESS, FAILURE
    }
}
