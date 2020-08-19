package it.kappa.gestionecorsiserver.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "S_AUTORIZZAZIONE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Autorizzazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authorityId;

    @Column(length = 100, unique = true)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Roles authorityName;

    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "autorizzazione", cascade = CascadeType.ALL)
    private List<Utente> utenti;

    public GrantedAuthority grantedAuthority() {
	return new SimpleGrantedAuthority(this.authorityName.name());
    }

}
