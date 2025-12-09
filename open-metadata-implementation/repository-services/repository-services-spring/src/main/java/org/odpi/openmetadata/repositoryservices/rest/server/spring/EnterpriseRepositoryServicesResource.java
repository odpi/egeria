/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * EnterpriseRepositoryServicesResource provides the server-side support for the OMRS Repository REST Services API
 * calls that are directed to an enterprise connector.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/users/{userId}/enterprise")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Repository Services - Enterprise",
        description="The Enterprise Services are part of the Open Metadata Repository Services (OMRS). They provide the " +
                "federated query capability used by the access services to work with metadata from any server in " +
                "the connected Open Metadata Repository Cohorts.  They also consolidate events from these cohorts and " +
                "distribute them to the access services running on the server.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/services/omrs/"))


public class EnterpriseRepositoryServicesResource
{
    private final OMRSRepositoryRESTServices  restAPI = new OMRSRepositoryRESTServices(false);

    /**
     * Default constructor
     */
    public EnterpriseRepositoryServicesResource()
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
     * @param serverName unique identifier for requested server.
     * @param userId calling user
     * @return String metadata collection id
     * or RepositoryErrorException if there is a problem communicating with the metadata repository.
     */
    @GetMapping(path = "/metadata-collection-id")
    @SecurityRequirement(name = "BearerAuthorization")

    public MetadataCollectionIdResponse getMetadataCollectionId(@PathVariable String   serverName,
                                                                @PathVariable String   userId)
    {
        return restAPI.getMetadataCollectionId(serverName, userId);
    }


    /* ==============================
     * Group 2: Working with typedefs
     */


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/all")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefGalleryResponse getAllTypes(@PathVariable String   serverName,
                                              @PathVariable String   userId)
    {
        return restAPI.getAllTypes(serverName, userId);
    }


    /**
     * Returns a list of type definitions that have the specified name.  Type names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an
     * arbitrary string of characters and ampersand for an arbitrary character.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param name name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation or
     * InvalidParameterException the name of the TypeDef is null.
     */
    @GetMapping(path = "/types/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefGalleryResponse findTypesByName(@PathVariable String   serverName,
                                                  @PathVariable String   userId,
                                                  @RequestParam String   name)
    {
        return restAPI.findTypesByName(serverName, userId, name);
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param category find parameters used to limit the returned results.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/typedefs/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefListResponse findTypeDefsByCategory(@PathVariable String           serverName,
                                                      @PathVariable String           userId,
                                                      @RequestBody  TypeDefCategory  category)
    {
        return restAPI.findTypeDefsByCategory(serverName, userId, category);
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param category find parameters used to limit the returned results.
     * @return AttributeTypeDefListResponse:
     * AttributeTypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/attribute-typedefs/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    public AttributeTypeDefListResponse findAttributeTypeDefsByCategory(@PathVariable String                   serverName,
                                                                        @PathVariable String                   userId,
                                                                        @RequestBody  AttributeTypeDefCategory category)
    {
        return restAPI.findAttributeTypeDefsByCategory(serverName, userId, category);
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties a list of property names.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the matchCriteria is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/typedefs/by-property")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefListResponse findTypeDefsByProperty(@PathVariable String            serverName,
                                                      @PathVariable String            userId,
                                                      @RequestBody  TypeDefProperties matchCriteria)
    {
        return restAPI.findTypeDefsByProperty(serverName, userId, matchCriteria);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier identifier of the element in the standard null means any.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/typedefs/by-external-id")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefListResponse findTypesByExternalID(@PathVariable                   String    serverName,
                                                     @PathVariable                   String    userId,
                                                     @RequestParam(required = false) String    standard,
                                                     @RequestParam(required = false) String    organization,
                                                     @RequestParam(required = false) String    identifier)
    {
        return restAPI.findTypesByExternalID(serverName, userId, standard, organization, identifier);
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the searchCriteria is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/typedefs/by-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefListResponse searchForTypeDefs(@PathVariable String serverName,
                                                 @PathVariable String userId,
                                                 @RequestParam String searchCriteria)
    {
        return restAPI.searchForTypeDefs(serverName, userId, searchCriteria);
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException The requested TypeDef is not known in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/typedef/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefResponse getTypeDefByGUID(@PathVariable String    serverName,
                                            @PathVariable String    userId,
                                            @PathVariable String    guid)
    {
        return restAPI.getTypeDefByGUID(serverName, userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef
     * @return AttributeTypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException The requested TypeDef is not known in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/attribute-typedef/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    public AttributeTypeDefResponse getAttributeTypeDefByGUID(@PathVariable String    serverName,
                                                              @PathVariable String    userId,
                                                              @PathVariable String    guid)
    {
        return restAPI.getAttributeTypeDefByGUID(serverName, userId, guid);
    }



    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/typedef/name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    public TypeDefResponse getTypeDefByName(@PathVariable String    serverName,
                                            @PathVariable String    userId,
                                            @PathVariable String    name)
    {
        return restAPI.getTypeDefByName(serverName, userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return AttributeTypeDefResponse:
     * AttributeTypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/types/attribute-typedef/name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    public  AttributeTypeDefResponse getAttributeTypeDefByName(@PathVariable String    serverName,
                                                               @PathVariable String    userId,
                                                               @PathVariable String    name)
    {
        return restAPI.getAttributeTypeDefByName(serverName, userId, name);
    }


    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return BooleanResponse:
     * boolean true means the TypeDef matches the local definition false means the TypeDef is not known or
     * InvalidParameterException the TypeDef is null.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * InvalidTypeDefException the new TypeDef has invalid contents.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/typedef/compatibility")
    @SecurityRequirement(name = "BearerAuthorization")

    public BooleanResponse verifyTypeDef(@PathVariable String    serverName,
                                         @PathVariable String    userId,
                                         @RequestBody  TypeDef   typeDef)
    {
        return restAPI.verifyTypeDef(serverName, userId, typeDef);
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef TypeDef structure describing the TypeDef to test.
     * @return BooleanResponse:
     * boolean true means the TypeDef matches the local definition false means the TypeDef is not known or
     * InvalidParameterException the TypeDef is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/attribute-typedef/compatibility")
    @SecurityRequirement(name = "BearerAuthorization")

    public  BooleanResponse verifyAttributeTypeDef(@PathVariable String            serverName,
                                                   @PathVariable String            userId,
                                                   @RequestBody  AttributeTypeDef  attributeTypeDef)
    {
        return restAPI.verifyAttributeTypeDef(serverName, userId, attributeTypeDef);
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns a boolean indicating if the entity is stored in the metadata collection.  This entity may be a full
     * entity object, or an entity proxy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * InvalidParameterException the guid is null.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/entity/{guid}/existence")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse isEntityKnown(@PathVariable String     serverName,
                                              @PathVariable String     userId,
                                              @PathVariable String     guid)
    {
        return restAPI.isEntityKnown(serverName, userId, guid);
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return EntitySummary structure or
     * InvalidParameterException the guid is null.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/entity/{guid}/summary")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntitySummaryResponse getEntitySummary(@PathVariable String     serverName,
                                                  @PathVariable String     userId,
                                                  @PathVariable String     guid)
    {
        return restAPI.getEntitySummary(serverName, userId, guid);
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
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
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse getEntityDetail(@PathVariable String    serverName,
                                                @PathVariable String    userId,
                                                @PathVariable String    guid)
    {
        return restAPI.getEntityDetail(serverName, userId, guid);
    }


    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param historyRangeRequest detailing the range of times and paging for the results
     * @return EntityList structure or
     * InvalidParameterException the guid or date is null or fromTime is after the toTime
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * FunctionNotSupportedException the repository does not support history.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{guid}/history/all")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse getEntityDetailHistory(@PathVariable String              serverName,
                                                      @PathVariable String              userId,
                                                      @PathVariable String              guid,
                                                      @RequestBody(required = false)  HistoryRangeRequest historyRangeRequest)
    {
        return restAPI.getEntityDetailHistory(serverName, userId, guid, historyRangeRequest);
    }


    /**
     * Return all historical versions of an entity's classification within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity's classification, set both the 'fromTime' and 'toTime' to null.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param classificationName name of the classification within entity
     * @param historyRangeRequest detailing the range of times and paging for the results
     * @return EntityList structure or
     * InvalidParameterException the guid or date is null or fromTime is after the toTime
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * FunctionNotSupportedException the repository does not support history.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{guid}/classification/{classificationName}/history/all")
    @SecurityRequirement(name = "BearerAuthorization")

    public  ClassificationListResponse getClassificationHistory(@PathVariable String              serverName,
                                                                @PathVariable String              userId,
                                                                @PathVariable String              guid,
                                                                @PathVariable String              classificationName,
                                                                @RequestBody(required = false)  HistoryRangeRequest historyRangeRequest)
    {
        return restAPI.getClassificationHistory(serverName, userId, guid, classificationName, historyRangeRequest);
    }


    /**
     * Return a historical version of an entity includes the header, classifications and properties of the entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EntityDetailResponse:
     * EntityDetail structure or
     * InvalidParameterException the guid or date is null or the asOfTime property is for a future time or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested or
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{guid}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityDetailResponse getEntityDetail(@PathVariable String         serverName,
                                                 @PathVariable String         userId,
                                                 @PathVariable String         guid,
                                                 @RequestBody  HistoryRequest asOfTime)
    {
        return restAPI.getEntityDetail(serverName, userId, guid, asOfTime);
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * Relationships list.  Null means no relationships associated with the entity or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * PropertyErrorException the sequencing property is not valid for the attached classifications or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/relationships")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipListResponse getRelationshipsForEntity(@PathVariable String                     serverName,
                                                              @PathVariable String                     userId,
                                                              @PathVariable String                     entityGUID,
                                                              @RequestBody  TypeLimitedFindRequest     findRequestParameters)
    {
        return restAPI.getRelationshipsForEntity(serverName, userId,
                                                 entityGUID,
                                                 findRequestParameters);
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * Relationships list.  Null means no relationships associated with the entity or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * PropertyErrorException the sequencing property is not valid for the attached classifications or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/relationships/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipListResponse getRelationshipsForEntityHistory(@PathVariable String                            serverName,
                                                                     @PathVariable String                            userId,
                                                                     @PathVariable String                            entityGUID,
                                                                     @RequestBody  TypeLimitedHistoricalFindRequest  findRequestParameters)
    {
        return restAPI.getRelationshipsForEntityHistory(serverName, userId,
                                                        entityGUID,
                                                        findRequestParameters);
    }


    /**
     * Return a list of entities that match the supplied conditions.  The results can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntities(@PathVariable String            serverName,
                                            @PathVariable String            userId,
                                            @RequestBody  EntityFindRequest findRequestParameters)
    {
        return restAPI.findEntities(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of entities that match the supplied conditions.  The results can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByHistory(@PathVariable String                      serverName,
                                                     @PathVariable String                      userId,
                                                     @RequestBody  EntityHistoricalFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByHistory(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/by-property")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByProperty(@PathVariable String                    serverName,
                                                      @PathVariable String                    userId,
                                                      @RequestBody  EntityPropertyFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByProperty(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/by-property/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByPropertyHistory(@PathVariable String                              serverName,
                                                             @PathVariable String                              userId,
                                                             @RequestBody  EntityPropertyHistoricalFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByPropertyHistory(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName name of the classification a null is not valid.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria, null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * ClassificationErrorException the classification request is not known to the metadata collection.
     * PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/by-classification/{classificationName}")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByClassification(@PathVariable String                   serverName,
                                                            @PathVariable String                   userId,
                                                            @PathVariable String                   classificationName,
                                                            @RequestBody  PropertyMatchFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByClassification(serverName, userId, classificationName, findRequestParameters);
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName name of the classification a null is not valid.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria, null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * ClassificationErrorException the classification request is not known to the metadata collection.
     * PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/by-classification/{classificationName}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByClassificationHistory(@PathVariable String                             serverName,
                                                                   @PathVariable String                             userId,
                                                                   @PathVariable String                             classificationName,
                                                                   @RequestBody  PropertyMatchHistoricalFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByClassificationHistory(serverName, userId, classificationName, findRequestParameters);
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria, null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/by-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByPropertyValue(@PathVariable String                    serverName,
                                                           @PathVariable String                    userId,
                                                           @RequestParam String                    searchCriteria,
                                                           @RequestBody  EntityPropertyFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByPropertyValue(serverName, userId, searchCriteria, findRequestParameters);
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria, null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/by-property-value/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse findEntitiesByPropertyValueHistory(@PathVariable String                              serverName,
                                                                  @PathVariable String                              userId,
                                                                  @RequestParam String                              searchCriteria,
                                                                  @RequestBody  EntityPropertyHistoricalFindRequest findRequestParameters)
    {
        return restAPI.findEntitiesByPropertyValueHistory(serverName, userId, searchCriteria, findRequestParameters);
    }


    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return RelationshipResponse:
     * relationship details if the relationship is found in the metadata collection; otherwise return null or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/relationship/{guid}/existence")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse  isRelationshipKnown(@PathVariable String     serverName,
                                                     @PathVariable String     userId,
                                                     @PathVariable String     guid)
    {
        return restAPI.isRelationshipKnown(serverName, userId, guid);
    }


    /**
     * Return a requested relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return RelationshipResponse:
     * a relationship structure or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/relationship/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse getRelationship(@PathVariable String     serverName,
                                                @PathVariable String     userId,
                                                @PathVariable String     guid)
    {
        return restAPI.getRelationship(serverName, userId, guid);
    }


    /**
     * Return a historical version of a relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return RelationshipResponse:
     * a relationship structure or
     * InvalidParameterException the guid or date is null or the asOfTime property is for a future time or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{guid}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipResponse getRelationship(@PathVariable String         serverName,
                                                 @PathVariable String         userId,
                                                 @PathVariable String         guid,
                                                 @RequestBody  HistoryRequest asOfTime)
    {
        return restAPI.getRelationship(serverName, userId, guid, asOfTime);
    }


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all
     * historical versions of a relationship, set both the 'fromTime' and 'toTime' to null.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param historyRangeRequest detailing the range of times and paging for the results
     * @return RelationshipList structure or
     * InvalidParameterException the guid or date is null or fromTime is after the toTime
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * RelationshipNotKnownException the requested relationship instance is not known in the metadata collection
     *                                   at the time requested.
     * FunctionNotSupportedException the repository does not support history.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{guid}/history/all")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse getRelationshipHistory(@PathVariable String              serverName,
                                                            @PathVariable String              userId,
                                                            @PathVariable String              guid,
                                                            @RequestBody(required = false)
                                                            HistoryRangeRequest historyRangeRequest)
    {
        return restAPI.getRelationshipHistory(serverName, userId, guid, historyRangeRequest);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse findRelationships(@PathVariable String              serverName,
                                                       @PathVariable String              userId,
                                                       @RequestBody  InstanceFindRequest findRequestParameters)
    {
        return restAPI.findRelationships(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse findRelationshipsByHistory(@PathVariable String                        serverName,
                                                                @PathVariable String                        userId,
                                                                @RequestBody  InstanceHistoricalFindRequest findRequestParameters)
    {
        return restAPI.findRelationshipsByHistory(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/by-property")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse findRelationshipsByProperty(@PathVariable String                   serverName,
                                                                 @PathVariable String                   userId,
                                                                 @RequestBody  PropertyMatchFindRequest findRequestParameters)
    {
        return restAPI.findRelationshipsByProperty(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/by-property/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse findRelationshipsByPropertyHistory(@PathVariable String                             serverName,
                                                                        @PathVariable String                             userId,
                                                                        @RequestBody  PropertyMatchHistoricalFindRequest findRequestParameters)
    {
        return restAPI.findRelationshipsByPropertyHistory(serverName, userId, findRequestParameters);
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * PropertyErrorException there is a problem with one of the other parameters  or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/by-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse findRelationshipsByPropertyValue(@PathVariable String                    serverName,
                                                                      @PathVariable String                    userId,
                                                                      @RequestParam String                    searchCriteria,
                                                                      @RequestBody  TypeLimitedFindRequest    findRequestParameters)
    {
        return restAPI.findRelationshipsByPropertyValue(serverName, userId, searchCriteria, findRequestParameters);
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * PropertyErrorException there is a problem with one of the other parameters  or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/by-property-value/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  RelationshipListResponse findRelationshipsByPropertyValueHistory(@PathVariable String                              serverName,
                                                                             @PathVariable String                              userId,
                                                                             @RequestParam String                              searchCriteria,
                                                                             @RequestBody  TypeLimitedHistoricalFindRequest    findRequestParameters)
    {
        return restAPI.findRelationshipsByPropertyValueHistory(serverName, userId, searchCriteria, findRequestParameters);
    }


    /**
     * Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return InstanceGraphResponse:
     * the sub-graph that represents the returned linked entities and their relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by either the startEntityGUID or the endEntityGUID
     *                                   is not found in the metadata collection or
     * PropertyErrorException there is a problem with one of the other parameters or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/from-entity/{startEntityGUID}/by-linkage")
    @SecurityRequirement(name = "BearerAuthorization")

    public  InstanceGraphResponse getLinkingEntities(@PathVariable String             serverName,
                                                     @PathVariable String             userId,
                                                     @PathVariable String             startEntityGUID,
                                                     @RequestParam String             endEntityGUID,
                                                     @RequestBody  OMRSAPIFindRequest findRequestParameters)
    {
        return restAPI.getLinkingEntities(serverName, userId, startEntityGUID, endEntityGUID, findRequestParameters);
    }


    /**
     * Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return InstanceGraphResponse:
     * the sub-graph that represents the returned linked entities and their relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by either the startEntityGUID or the endEntityGUID
     *                                   is not found in the metadata collection or
     * PropertyErrorException there is a problem with one of the other parameters or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/from-entity/{startEntityGUID}/by-linkage/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  InstanceGraphResponse getLinkingEntitiesHistory(@PathVariable String                        serverName,
                                                            @PathVariable String                        userId,
                                                            @PathVariable String                        startEntityGUID,
                                                            @RequestParam String                        endEntityGUID,
                                                            @RequestBody  OMRSAPIHistoricalFindRequest  findRequestParameters)
    {
        return restAPI.getLinkingEntitiesHistory(serverName, userId, startEntityGUID, endEntityGUID, findRequestParameters);
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return InstanceGraphResponse
     * the sub-graph that represents the returned linked entities and their relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException one of the type guids passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection or
     * PropertyErrorException there is a problem with one of the other parameters or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/from-entity/{entityGUID}/by-neighborhood")
    @SecurityRequirement(name = "BearerAuthorization")

    public  InstanceGraphResponse getEntityNeighborhood(@PathVariable String                        serverName,
                                                        @PathVariable String                        userId,
                                                        @PathVariable String                        entityGUID,
                                                        @RequestParam int                           level,
                                                        @RequestBody  EntityNeighborhoodFindRequest findRequestParameters)
    {
        return restAPI.getEntityNeighborhood(serverName, userId, entityGUID, level, findRequestParameters);
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return InstanceGraphResponse
     * the sub-graph that represents the returned linked entities and their relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException one of the type guids passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection or
     * PropertyErrorException there is a problem with one of the other parameters or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/from-entity/{entityGUID}/by-neighborhood/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  InstanceGraphResponse getEntityNeighborhoodHistory(@PathVariable String                                  serverName,
                                                               @PathVariable String                                  userId,
                                                               @PathVariable String                                  entityGUID,
                                                               @RequestParam int                                     level,
                                                               @RequestBody  EntityNeighborhoodHistoricalFindRequest findRequestParameters)
    {
        return restAPI.getEntityNeighborhoodHistory(serverName, userId, entityGUID, level, findRequestParameters);
    }


    /**
     * Return the list of entities that are of the types listed in instanceTypes and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * list of entities either directly or indirectly connected to the start entity or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException one of the type guids passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection or
     * EntityNotKnownException the entity identified by the startEntityGUID
     *                                   is not found in the metadata collection or
     * PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/from-entity/{startEntityGUID}/by-relationship")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse getRelatedEntities(@PathVariable String                      serverName,
                                                  @PathVariable String                      userId,
                                                  @PathVariable String                      startEntityGUID,
                                                  @RequestBody  RelatedEntitiesFindRequest  findRequestParameters)
    {
        return restAPI.getRelatedEntities(serverName, userId, startEntityGUID, findRequestParameters);
    }


    /**
     * Return the list of entities that are of the types listed in instanceTypes and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * list of entities either directly or indirectly connected to the start entity or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException one of the type guids passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection or
     * EntityNotKnownException the entity identified by the startEntityGUID
     *                                   is not found in the metadata collection or
     * PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/from-entity/{startEntityGUID}/by-relationship/history")
    @SecurityRequirement(name = "BearerAuthorization")

    public  EntityListResponse getRelatedEntitiesHistory(@PathVariable String                                serverName,
                                                         @PathVariable String                                userId,
                                                         @PathVariable String                                startEntityGUID,
                                                         @RequestBody  RelatedEntitiesHistoricalFindRequest  findRequestParameters)
    {
        return restAPI.getRelatedEntitiesHistory(serverName, userId, startEntityGUID, findRequestParameters);
    }

    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody parameters for the new entity
     * @return EntityDetailResponse:
     * EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type or
     * ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse addEntity(@PathVariable String               serverName,
                                          @PathVariable String               userId,
                                          @RequestBody  EntityCreateRequest  requestBody)
    {
        return restAPI.addEntity(serverName, userId, requestBody);
    }


    /**
     * Save a new entity that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new entity is assigned a new GUID and put
     * in the requested state.  The new entity is returned.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody parameters for the new entity
     * @return EntityDetailResponse:
     * EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type or
     * ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/external")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse addExternalEntity(@PathVariable String               serverName,
                                                  @PathVariable String               userId,
                                                  @RequestBody  EntityCreateRequest  requestBody)
    {
        return restAPI.addExternalEntity(serverName, userId, requestBody);
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity proxy is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type or
     * ClassificationErrorException one or more of the requested classifications are either not known or
     *                                         not defined for this entity type or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status or
     * FunctionNotSupportedException the repository does not support entity proxies as first class elements or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity-proxy")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse addEntityProxy(@PathVariable String      serverName,
                                       @PathVariable String      userId,
                                       @RequestBody  EntityProxy entityProxy)
    {
        return restAPI.addEntityProxy(serverName, userId, entityProxy);
    }


    /**
     * Update the status for a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @return EntityDetailResponse:
     * EntityDetail showing the current entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                      the requested status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/status")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse updateEntityStatus(@PathVariable String           serverName,
                                                   @PathVariable String           userId,
                                                   @PathVariable String           entityGUID,
                                                   @RequestBody  InstanceStatus   newStatus)
    {
        return restAPI.updateEntityStatus(serverName, userId, entityGUID, newStatus);
    }


    /**
     * Update selected properties in an entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param propertiesRequestBody a list of properties to change.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse updateEntityProperties(@PathVariable String                      serverName,
                                                       @PathVariable String                      userId,
                                                       @PathVariable String                      entityGUID,
                                                       @RequestBody  InstancePropertiesRequest   propertiesRequestBody)
    {
        return restAPI.updateEntityProperties(serverName, userId, entityGUID, propertiesRequestBody);
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support undo or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/entity/{entityGUID}/previous")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse undoEntityUpdate(@PathVariable String  serverName,
                                                 @PathVariable String  userId,
                                                 @PathVariable String  entityGUID)
    {
        return restAPI.undoEntityUpdate(serverName, userId, entityGUID);
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph, but it is no longer returned
     * on queries.  All relationships to the entity are also soft-deleted and will no longer be usable.
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete() call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return EntityDetailResponse
     * details of the deleted entity or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       soft-deletes (use purgeEntity() to remove the entity permanently) or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{obsoleteEntityGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse  deleteEntity(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        obsoleteEntityGUID,
                                              @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.deleteEntity(serverName, userId, obsoleteEntityGUID, typeDefValidationForRequest);
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * EntityNotDeletedException the entity is not in DELETED status and so can not be purged or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{deletedEntityGUID}/purge")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse purgeEntity(@PathVariable String                        serverName,
                                    @PathVariable String                        userId,
                                    @PathVariable String                        deletedEntityGUID,
                                    @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.purgeEntity(serverName, userId, deletedEntityGUID, typeDefValidationForRequest);
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return EntityDetailResponse:
     * EntityDetail showing the restored entity header, properties and classifications or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * EntityNotDeletedException the entity is currently not in DELETED status and so it can not be restored or
     * FunctionNotSupportedException the repository does not support soft-delete or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/entity/{deletedEntityGUID}/restore")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse restoreEntity(@PathVariable String    serverName,
                                              @PathVariable String    userId,
                                              @PathVariable String    deletedEntityGUID)
    {
        return restAPI.restoreEntity(serverName, userId, deletedEntityGUID);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param propertiesRequestBody list of properties to set in the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/classification/{classificationName}")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse classifyEntity(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      entityGUID,
                                               @PathVariable String                      classificationName,
                                               @RequestBody  InstancePropertiesRequest   propertiesRequestBody)
    {
        return restAPI.classifyEntity(serverName, userId, entityGUID, classificationName, propertiesRequestBody);
    }



    /**
     * Add the requested classification to a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName String name for the classification.
     * @param requestBody list of properties to set in the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/classification/{classificationName}")
    @SecurityRequirement(name = "BearerAuthorization")

    public ClassificationResponse classifyEntity(@PathVariable String                      serverName,
                                                 @PathVariable String                      userId,
                                                 @PathVariable String                      classificationName,
                                                 @RequestBody  ProxyClassificationRequest  requestBody)
    {
        return restAPI.classifyEntity(serverName, userId, classificationName, requestBody);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationRequestBody values for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/classification/{classificationName}/detailed")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse  classifyEntity(@PathVariable String                serverName,
                                                @PathVariable String                userId,
                                                @PathVariable String                entityGUID,
                                                @PathVariable String                classificationName,
                                                @RequestBody  ClassificationRequest classificationRequestBody)
    {
        return restAPI.classifyEntity(serverName, userId, entityGUID, classificationName, classificationRequestBody);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName String name for the classification.
     * @param requestBody values for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/classification/{classificationName}/detailed")
    @SecurityRequirement(name = "BearerAuthorization")

    public ClassificationResponse  classifyEntity(@PathVariable String                     serverName,
                                                  @PathVariable String                     userId,
                                                  @PathVariable String                     classificationName,
                                                  @RequestBody  ClassificationProxyRequest requestBody)
    {
        return restAPI.classifyEntity(serverName, userId, classificationName, requestBody);
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param requestBody empty request body
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * ClassificationErrorException the requested classification is not set on the entity or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/classification/{classificationName}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @SuppressWarnings(value = "unused")
    public EntityDetailResponse declassifyEntity(@PathVariable                  String          serverName,
                                                 @PathVariable                  String          userId,
                                                 @PathVariable                  String          entityGUID,
                                                 @PathVariable                  String          classificationName,
                                                 @RequestBody(required = false) OMRSAPIRequest  requestBody)
    {
        return restAPI.declassifyEntity(serverName, userId, entityGUID, classificationName);
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName String name for the classification.
     * @param requestBody entity proxy request body
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * ClassificationErrorException the requested classification is not set on the entity or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/classification/{classificationName}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public ClassificationResponse declassifyEntity(@PathVariable String       serverName,
                                                   @PathVariable String       userId,
                                                   @PathVariable String       classificationName,
                                                   @RequestBody  EntityProxy  requestBody)
    {
        return restAPI.declassifyEntity(serverName, userId, classificationName, requestBody);
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param propertiesRequestBody list of properties for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is not attached to the classification or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/classification/{classificationName}/properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public EntityDetailResponse updateEntityClassification(@PathVariable String                      serverName,
                                                           @PathVariable String                      userId,
                                                           @PathVariable String                      entityGUID,
                                                           @PathVariable String                      classificationName,
                                                           @RequestBody  InstancePropertiesRequest   propertiesRequestBody)
    {
        return restAPI.updateEntityClassification(serverName, userId, entityGUID, classificationName, propertiesRequestBody);
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName String name for the classification.
     * @param requestBody list of properties for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is not attached to the classification or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/classification/{classificationName}/properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public ClassificationResponse updateEntityClassification(@PathVariable String                      serverName,
                                                             @PathVariable String                      userId,
                                                             @PathVariable String                      classificationName,
                                                             @RequestBody  ProxyClassificationRequest  requestBody)
    {
        return restAPI.updateEntityClassification(serverName, userId,classificationName, requestBody);
    }


    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param createRequestParameters parameters used to fill out the new relationship
     * @return RelationshipResponse:
     * Relationship structure with the new header, requested entities and properties or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * EntityNotKnownException one of the requested entities is not known in the metadata collection or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse addRelationship(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @RequestBody  RelationshipCreateRequest createRequestParameters)
    {
        return restAPI.addRelationship(serverName, userId, createRequestParameters);
    }


    /**
     * Save a new relationship that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new relationship is assigned a new GUID and put
     * in the requested state.  The new relationship is returned.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param createRequestParameters parameters used to fill out the new relationship
     * @return RelationshipResponse:
     * Relationship structure with the new header, requested entities and properties or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * EntityNotKnownException one of the requested entities is not known in the metadata collection or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/external")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse addExternalRelationship(@PathVariable String                    serverName,
                                                        @PathVariable String                    userId,
                                                        @RequestBody  RelationshipCreateRequest createRequestParameters)
    {
        return restAPI.addExternalRelationship(serverName, userId, createRequestParameters);
    }


    /**
     * Update the status of a specific relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param newStatus new InstanceStatus for the relationship.
     * @return RelationshipResponse:
     * Resulting relationship structure with the new status set or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{relationshipGUID}/status")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse updateRelationshipStatus(@PathVariable String           serverName,
                                                         @PathVariable String           userId,
                                                         @PathVariable String           relationshipGUID,
                                                         @RequestBody  InstanceStatus   newStatus)
    {
        return restAPI.updateRelationshipStatus(serverName, userId, relationshipGUID, newStatus);
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param propertiesRequestBody list of the properties to update.
     * @return RelationshipResponse:
     * Resulting relationship structure with the new properties set or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{relationshipGUID}/properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse updateRelationshipProperties(@PathVariable String                      serverName,
                                                             @PathVariable String                      userId,
                                                             @PathVariable String                      relationshipGUID,
                                                             @RequestBody  InstancePropertiesRequest   propertiesRequestBody)
    {
        return restAPI.updateRelationshipProperties(serverName, userId, relationshipGUID, propertiesRequestBody);
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @return RelationshipResponse:
     * Relationship structure with the new current header, requested entities and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * FunctionNotSupportedException the repository does not support undo or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/relationship/{relationshipGUID}/previous")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse undoRelationshipUpdate(@PathVariable String  serverName,
                                                       @PathVariable String  userId,
                                                       @PathVariable String  relationshipGUID)
    {
        return restAPI.undoRelationshipUpdate(serverName, userId, relationshipGUID);
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED, and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return RelationshipResponse:
     * Updated relationship or
     * InvalidParameterException one of the parameters is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     soft-deletes or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{obsoleteRelationshipGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse deleteRelationship(@PathVariable String                        serverName,
                                                   @PathVariable String                        userId,
                                                   @PathVariable String                        obsoleteRelationshipGUID,
                                                   @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.deleteRelationship(serverName, userId, obsoleteRelationshipGUID, typeDefValidationForRequest);
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * RelationshipNotDeletedException the requested relationship is not in DELETED status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{deletedRelationshipGUID}/purge")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse purgeRelationship(@PathVariable String                        serverName,
                                          @PathVariable String                        userId,
                                          @PathVariable String                        deletedRelationshipGUID,
                                          @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.purgeRelationship(serverName, userId, deletedRelationshipGUID, typeDefValidationForRequest);
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return RelationshipResponse:
     * Relationship structure with the restored header, requested entities and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * RelationshipNotDeletedException the requested relationship is not in DELETED status or
     * FunctionNotSupportedException the repository does not support soft-deletes
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/instances/relationship/{deletedRelationshipGUID}/restore")
    @SecurityRequirement(name = "BearerAuthorization")

    public RelationshipResponse restoreRelationship(@PathVariable String    serverName,
                                                    @PathVariable String    userId,
                                                    @PathVariable String    deletedRelationshipGUID)
    {
        return restAPI.restoreRelationship(serverName, userId, deletedRelationshipGUID);
    }



    /* ======================================================================
     * Remote Enterprise OMRS Topic Connection
     */

    /**
     * Return the connection for remote access to the enterprise topic connector.
     * May be null if remote access to this topic is not configured in the OMAG Server.
     *
     * @param serverName unique identifier for requested server
     * @param userId unique identifier for requesting server
     * @return null or connection object or
     *  InvalidParameterException unknown servername
     *  UserNotAuthorizedException unsupported userId
     *  RepositoryErrorException null local repository
     */
    @GetMapping(path = "/remote-topic-connection")
    @SecurityRequirement(name = "BearerAuthorization")

    public ConnectionResponse getEnterpriseOMRSTopicConnection(@PathVariable String serverName,
                                                               @PathVariable String userId)
    {
        return restAPI.getEnterpriseOMRSTopicConnection(serverName, userId);
    }
}
