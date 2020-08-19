package it.kappa.gestionecorsiserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.kappa.gestionecorsiserver.domain.Utente;
import it.kappa.gestionecorsiserver.repository.UtenteRepository;
import it.kappa.gestionecorsiserver.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UtenteRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Utente user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email " + username));
	return new CustomUserDetails(user);
    }

}
