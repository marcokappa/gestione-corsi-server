package it.kappa.gestionecorsiserver.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import it.kappa.gestionecorsiserver.service.CustomUserDetailsServiceImpl;
import it.kappa.gestionecorsiserver.util.SecurityCipher;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${authentication.auth.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
	try {
	    String jwt = getJwtToken(httpServletRequest, true);
	    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
		String username = tokenProvider.getUsernameFromToken(jwt);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
	String bearerToken = request.getHeader("Authorization");
	if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	    String accessToken = bearerToken.substring(7);
	    if (accessToken == null)
		return null;

	    return SecurityCipher.decrypt(accessToken);
	}
	return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {
	Cookie[] cookies = request.getCookies();
	for (Cookie cookie : cookies) {
	    if (accessTokenCookieName.equals(cookie.getName())) {
		String accessToken = cookie.getValue();
		if (accessToken == null)
		    return null;

		return SecurityCipher.decrypt(accessToken);
	    }
	}
	return null;
    }

    private String getJwtToken(HttpServletRequest request, boolean fromCookie) {
	if (fromCookie && request.getCookies() != null) {

	    return getJwtFromCookie(request);
	}

	return getJwtFromRequest(request);
    }

}
