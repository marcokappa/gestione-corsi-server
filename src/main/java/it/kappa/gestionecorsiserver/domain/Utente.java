package it.kappa.gestionecorsiserver.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import it.kappa.gestionecorsiserver.dto.UtenteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "S_UTENTE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 100, unique = true)
    @NotNull
    @Email
    private String email;

    @Column(length = 200)
    private String password;

    @NotNull
    private Boolean isEnabled;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_Id", nullable = false)
    private Autorizzazione autorizzazione;

    public UtenteDto getUtenteDto() {
	return UtenteDto.builder().email(getEmail()).build();
    }

}
