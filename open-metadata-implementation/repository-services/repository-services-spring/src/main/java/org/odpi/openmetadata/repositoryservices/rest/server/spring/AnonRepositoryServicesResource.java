/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.repositoryservices.rest.properties.EntityDetailResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.MetadataCollectionIdResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.RelationshipResponse;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AnonRepositoryServicesResource provides the server-side support for the OMRS Repository REST Services API
 * that do not include a userId.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services")

@Tag(name="Repository Services - Anonymous",
        description="The Open Metadata Repository Services (OMRS) enable metadata repositories to " +
        "exchange metadata irrespective of the technology, or technology supplier.  The anonymous " +
                "services do not take a user id in the URL.  These may be disabled if server " +
                "security is turned on.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/services/omrs/"))

public class AnonRepositoryServicesResource
{
    private final OMRSRepositoryRESTServices  restAPI = new OMRSRepositoryRESTServices(true);

    /**
     * Default constructor
     */
    public AnonRepositoryServicesResource()
    {
    }


    /* ======================================================================
     * Group 1: Confirm the identity of the metadata repository being called.
     */

    /**
     * Returns the identifier of the metadata repository.  This is the identifier used to register the
     * metadata repository with the metadata repository cohort.  It is also the identifier used to
     * identify the home repository of a metadata instance.
     *
     * This method has been deprecated as it does not work on a server that has security enabled.
     *
     * @param serverName unique identifier for requested server.
     * @return String metadata collection id.
     * or RepositoryErrorException if there is a problem communicating with the metadata repository.
     */
    @GetMapping(path = "/metadata-collection-id")

    public MetadataCollectionIdResponse getMetadataCollectionId(@PathVariable String   serverName)
    {
        @SuppressWarnings(value = "deprecation")
        MetadataCollectionIdResponse response = restAPI.getMetadataCollectionId(serverName);
        return response;
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Return the header, classifications and properties of a specific entity.  This method supports anonymous
     * access to an instance.  The call may fail if the metadata is secured.
     *
     * @param serverName unique identifier for requested server.
     * @param guid String unique identifier for the entity.
     * @return EntityDetailResponse:
     * EntityDetail structure or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/entity/{guid}")

    public EntityDetailResponse getEntityDetail(@PathVariable String    serverName,
                                                @PathVariable String    guid)
    {
        return restAPI.getEntityDetail(serverName,null, guid);
    }


    /**
     * Return a requested relationship.  This is the anonymous form for repository.  The call may fail if security is
     * required.
     *
     * @param serverName unique identifier for requested server.
     * @param guid String unique identifier for the relationship.
     * @return RelationshipResponse:
     * A relationship structure or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/relationship/{guid}")

    public RelationshipResponse getRelationship(@PathVariable String     serverName,
                                                @PathVariable String     guid)
    {
        return restAPI.getRelationship(serverName, null, guid);
    }
}
