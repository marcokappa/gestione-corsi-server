package it.kappa.gestionecorsiserver.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // @Value("${security.cors.AllowedOrigins}")
    // String allowedOrigins;
    //
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**")//
    // .allowedOrigins("*")//
    // .allowedMethods("POST, PUT, GET, OPTIONS, DELETE")//
    // .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")//
    // .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")//
    // .allowCredentials(true)//
    // .maxAge(3600);
    // }
}
