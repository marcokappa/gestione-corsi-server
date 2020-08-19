package it.kappa.gestionecorsiserver.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.kappa.gestionecorsiserver.domain.Utente;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1226476291915371640L;
    private final Utente utente;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	List<GrantedAuthority> autorizzazioni = new ArrayList<>();
	autorizzazioni.add(utente.getAutorizzazione().grantedAuthority());
	return autorizzazioni;
    }

    @Override
    public String getPassword() {
	return utente.getPassword();
    }

    @Override
    public String getUsername() {
	return utente.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    @Override
    public boolean isEnabled() {
	return utente.getIsEnabled();
    }
}
