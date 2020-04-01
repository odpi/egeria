/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.designmodel.server.DesignModelRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DesignModelResource provides the server-side implementation of the Design Model Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-model/users/{userId}")

@Tag(name="Design Model", description="The open metadata conformance suite provides a testing framework to help the developers integrate a specific technology into the open metadata ecosystem.", externalDocs=@ExternalDocumentation(description="Design Model Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/design-model/"))

public class DesignModelResource
{
    private DesignModelRESTServices restAPI = new DesignModelRESTServices();

    /**
     * Default constructor
     */
    public DesignModelResource()
    {
    }

}
