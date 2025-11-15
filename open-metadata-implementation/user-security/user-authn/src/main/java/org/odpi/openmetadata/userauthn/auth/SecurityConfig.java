/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration of HttpSecurity for Spring security.
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig
{
    @Value("${cors.allowed-origins}")
    List<String> allowedOrigins;


    /**
     * Return the authentication manager.
     *
     * @param authProvider details of the expected provider
     * @return manager
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationProvider authProvider)
    {
        return new ProviderManager(authProvider);
    }

    private RSAKey rsaKey = RSAGenerator.generateRSAKeyPair();

    /**
     * Get the jwk source.
     *
     * @return source
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource()
    {
        rsaKey = RSAGenerator.generateRSAKeyPair();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException
    {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    /**
     * Returns WebMvcConfigurer for the cors configuration.
     * The bean is based on springboot configuration property cors.allowed-origins
     *
     * @return corsConfigurer
     */
    @Bean
    @ConditionalOnProperty(value = "cors.allowed-origins")
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings( CorsRegistry registry ) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins.toArray(new String[]{}))
                        .allowedMethods("GET","POST","PUT","PATCH","DELETE")
                        .allowedHeaders("Authorization","Content-type");
            }
        };
    }


    /**
     * Define the types of URLs that will be permitted to be called without security.
     *
     * @param httpSecurity security object to configure
     * @return configured HTTP security object
     * @throws Exception something went wrong
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.cors(); // replaced by corsConfigurationSource in a later version of Spring

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests( auth -> auth
                        .requestMatchers("/api/about").permitAll()
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers("/api/servers/*/token").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/open-metadata/**").permitAll() // platform level services
                        .requestMatchers("/servers/*/open-metadata/**").permitAll() // OMAG Server services
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt); // replaced by jwtBearer in a later version of Spring

        return httpSecurity.build();
    }
}


