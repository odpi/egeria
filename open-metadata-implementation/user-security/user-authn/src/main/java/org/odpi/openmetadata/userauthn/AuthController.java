/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.userauthn.auth.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


/**
 * AuthController provides the simple token service that can be used to log a user into open metadata.
 * It uses the Spring framework to provide the authentication token.  The user
 */
@RestController

@Tag(name="API: User Security Services", description="The user security services provide user authentication services to a client program, plus identification details about this runtime implementation.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/features/metadata-security/overview"))

public class AuthController
{
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    /**
     * Constructor for the token service.
     *
     * @param tokenService implementation of the token generation service
     * @param authenticationManager implementation of the pluggable authentication manager
     */
    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager)
    {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }


    /**
     * External service that provides an encrypted token that act as a bearer token for a REST API request.
     * THe userId and password are sent in the request body
     *
     * @param userLogin request body that contains the userId and password
     * @return token
     * @throws AuthenticationException The user and password do not match the values in the user directory.
     */
    @PostMapping("/api/token")

    @Operation(summary="generateUserToken",
               description="Validate the user's password and return a bearer token for the user.  This is passed on subsequent API requests.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/features/metadata-security/overview"))

    public String token(@RequestBody LoginRequest userLogin) throws AuthenticationException
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.userId(), userLogin.password()));

        return tokenService.generateToken(authentication);
    }


    /**
     * External service that provides an encrypted token that act as a bearer token for a REST API request.
     * THe userId and password are sent in the request body
     *
     * @param userLogin request body that contains the userId and password
     * @return token
     * @throws AuthenticationException The user and password do not match the values in the user directory.
     */
    @PostMapping("/api/servers/{serverName}/token")

    @Operation(summary="generateUserToken",
            description="Validate the user's password and return a bearer token for the user.  This is passed on subsequent API requests.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/metadata-security/overview"))

    public String serverToken(@PathVariable String       serverName,
                              @RequestBody  LoginRequest userLogin) throws AuthenticationException
    {
        /*
         * Currently the platform security connector is used to provide tokens for specific servers
         */
        return this.token(userLogin);
    }
}
