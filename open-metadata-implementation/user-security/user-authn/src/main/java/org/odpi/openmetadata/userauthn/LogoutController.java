/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.odpi.openmetadata.userauthn.auth.TokenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * LogoutController provides the REST API to log out a user.
 */
@RestController

@Tag(name="API: User Security Services", description="The user security services provide user authentication services to a client program, plus identification details about this runtime implementation.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/features/metadata-security/overview"))

public class LogoutController
{

    @Autowired(required = false)
    TokenClient tokenClient;


    /**
     * Remove details of the token.
     *
     * @param request HTTP request
     * @throws HttpClientErrorException problem with the request
     */
    @GetMapping("/api/token/logout")

    @Operation(summary="platformLogout",
               description="Invalidate the user's token supplied in the request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/features/metadata-security/overview"))

    public void platformLogout(HttpServletRequest request) throws HttpClientErrorException
    {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        if (tokenClient != null && token != null)
        {
           tokenClient.del(token);
        }
    }


    /**
     * Remove details of the token.
     *
     * @param serverName HTTP request
     * @throws HttpClientErrorException problem with the request
     */
    @GetMapping("/servers/{serverName}/api/token/logout")

    @Operation(summary="serverLogout",
            description="Invalidate the user's token supplied in the request.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/metadata-security/overview"))

    public void serverLogout(@PathVariable String serverName) throws HttpClientErrorException
    {
        if (RequestContextHolder.getRequestAttributes() != null)
        {
            String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

            if (tokenClient != null && token != null)
            {
                tokenClient.del(token);
            }
        }
    }
}
