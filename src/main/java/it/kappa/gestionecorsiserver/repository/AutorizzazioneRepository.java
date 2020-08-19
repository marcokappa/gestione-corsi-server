package it.kappa.gestionecorsiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.kappa.gestionecorsiserver.domain.Autorizzazione;
import it.kappa.gestionecorsiserver.domain.Roles;

public interface AutorizzazioneRepository extends JpaRepository<Autorizzazione, Long> {
    Optional<Autorizzazione> findByAuthorityName(Roles authorityName);
}
