/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.collectionmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.collectionmanager.server.CollectionManagerRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The CollectionManagerResource provides the Spring API endpoints of the Collection Manager Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/collection-manager")

@Tag(name="API: Collection Manager OMVS",
     description="Maintain and explore the contents of nested collections. These collections can be used to represent digital products, or collections of resources for a particular project or team. They can be used to organize assets and other resources into logical groups.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/collection-manager/overview/"))

public class CollectionManagerResource
{

    private final CollectionManagerRESTServices restAPI = new CollectionManagerRESTServices();


    /**
     * Default constructor
     */
    public CollectionManagerResource()
    {
    }


}

