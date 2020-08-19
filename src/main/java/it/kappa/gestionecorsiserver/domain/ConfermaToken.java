package it.kappa.gestionecorsiserver.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "S_CONFERMA_TOKEN")
@Data
@NoArgsConstructor
public class ConfermaToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tokenId;

    @Column(unique = true)
    private String tokenAttivazione;

    private LocalDateTime dataCreazione;

    @OneToOne(targetEntity = Utente.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Utente utente;

    public ConfermaToken(Utente utente) {
	this.utente = utente;
	dataCreazione = LocalDateTime.now();
	tokenAttivazione = UUID.randomUUID().toString();
    }

}