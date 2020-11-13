/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataprivacy.server.DataPrivacyRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DataPrivacyResource provides the server-side implementation of the Data Privacy Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-privacy/users/{userId}")

@Tag(name="Data Privacy OMAS", description="The Data Privacy OMAS provides APIs and events for tools that play a role in a DevOps pipeline.", externalDocs=@ExternalDocumentation(description="Data Privacy Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-privacy/"))

public class DataPrivacyResource
{
    private DataPrivacyRESTServices restAPI = new DataPrivacyRESTServices();

    /**
     * Default constructor
     */
    public DataPrivacyResource()
    {
    }

}
