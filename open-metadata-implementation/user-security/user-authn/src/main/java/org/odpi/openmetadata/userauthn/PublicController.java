/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


/**
 * PublicController provides the standard information about this application.
 */
@RestController

@Tag(name="API: User Security Services", description="The user security services provide user authentication services to a client program, plus identification details about this runtime implementation.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/features/metadata-security/overview"))

@RequestMapping("/api/public")
public class PublicController
{
    @Autowired
    AppInfoBean appInfoBean;

    /**
     * Return information about this runtime.
     *
     * @param request the http servlet request
     * @return an AppInfoBean that contains the app build information
     */
    @GetMapping( path = "/app/info")

    @Operation(summary="getApplicationInformation",
               description="Return the title and description of the logical application to display to the caller.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/features/metadata-security/overview"))

    public AppInfoBean getApplicationInformation(HttpServletRequest request)
    {
        return appInfoBean;
    }
}
