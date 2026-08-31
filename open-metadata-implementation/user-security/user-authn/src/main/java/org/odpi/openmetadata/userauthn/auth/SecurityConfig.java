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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Value("${rsa.key-id:}") // Default value is zero length string
    String rsaKeyId;


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

    private RSAKey rsaKey = RSAGenerator.generateRSAKeyPair(rsaKeyId);

    /**
     * Get the jwk source.
     *
     * @return source
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource()
    {
        rsaKey = RSAGenerator.generateRSAKeyPair(rsaKeyId);
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
        // The no-argument cors() and jwt() were deprecated for removal in Spring Security 6.  Both are
        // defined as the Customizer.withDefaults() form below, so this is the same configuration said in
        // the way that survives - cors() still picks up the CorsRegistry configured by corsConfigurer().
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests( auth -> auth
                        .requestMatchers("/api/about").permitAll()
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers("/servers/*/api/token").permitAll()
                        .requestMatchers("/open-metadata/platform-services/server-platform/origin").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }
}


