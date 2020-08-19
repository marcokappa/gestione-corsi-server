package it.kappa.gestionecorsiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.kappa.gestionecorsiserver.domain.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByEmail(String email);

    Boolean existsByEmail(String email);
}
