/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * LocalRepositoryServicesResource provides the server-side support for the OMRS Repository REST Services API
 * that are directed to the local repository.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/users/{userId}")

@Tag(name="Repository Services - Local Repository",
        description="The Local Repository Services are part of the Open Metadata Repository Services (OMRS). They provide the " +
                "interface to the local repository that is used by remote servers to query locally stored metadata.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/services/omrs/"))


public class LocalRepositoryServicesResource
{
    private final OMRSRepositoryRESTServices  restAPI = new OMRSRepositoryRESTServices(true);

    /**
     * Default constructor
     */
    public LocalRepositoryServicesResource()
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

    public  AttributeTypeDefResponse getAttributeTypeDefByName(@PathVariable String    serverName,
                                                               @PathVariable String    userId,
                                                               @PathVariable String    name)
    {
        return restAPI.getAttributeTypeDefByName(serverName, userId, name);
    }


    /**
     * Create a collection of related types.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param newTypes TypeDefGalleryResponse structure describing the new AttributeTypeDefs and TypeDefs.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the new TypeDef is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefKnownException the TypeDef is already stored in the repository or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types")

    public  VoidResponse addTypeDefGallery(@PathVariable String         serverName,
                                           @PathVariable String         userId,
                                           @RequestBody  TypeDefGallery newTypes)
    {
        return restAPI.addTypeDefGallery(serverName, userId, newTypes);
    }


    /**
     * Create a definition of a new TypeDef.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the new TypeDef is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefKnownException the TypeDef is already stored in the repository or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/typedef")

    public VoidResponse addTypeDef(@PathVariable String    serverName,
                                   @PathVariable String    userId,
                                   @RequestBody  TypeDef   newTypeDef)
    {
        return restAPI.addTypeDef(serverName, userId, newTypeDef);
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the new TypeDef is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefKnownException the TypeDef is already stored in the repository or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/attribute-typedef")

    public  VoidResponse addAttributeTypeDef(@PathVariable String             serverName,
                                             @PathVariable String             userId,
                                             @RequestBody  AttributeTypeDef   newAttributeTypeDef)
    {
        return restAPI.addAttributeTypeDef(serverName, userId, newAttributeTypeDef);
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

    public  BooleanResponse verifyAttributeTypeDef(@PathVariable String            serverName,
                                                   @PathVariable String            userId,
                                                   @RequestBody  AttributeTypeDef  attributeTypeDef)
    {
        return restAPI.verifyAttributeTypeDef(serverName, userId, attributeTypeDef);
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch TypeDef patch describing change to TypeDef.
     * @return TypeDefResponse:
     * updated TypeDef or
     * InvalidParameterException the TypeDefPatch is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * PatchErrorException the TypeDef can not be updated because the supplied patch is incompatible
     *                               with the stored TypeDef or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/typedef/update")

    public TypeDefResponse updateTypeDef(@PathVariable String       serverName,
                                         @PathVariable String       userId,
                                         @RequestBody  TypeDefPatch typeDefPatch)
    {
        return restAPI.updateTypeDef(serverName, userId, typeDefPatch);
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the TypeDef.
     * @param name String unique name for the TypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the one of TypeDef identifiers is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * TypeDefInUseException the TypeDef can not be deleted because there are instances of this type in
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 TypeDef can be deleted or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/typedef/{guid}/delete")

    public VoidResponse deleteTypeDef(@PathVariable String    serverName,
                                      @PathVariable String    userId,
                                      @PathVariable String    guid,
                                      @RequestParam String    name)
    {
        return restAPI.deleteTypeDef(serverName, userId, guid, name);
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the AttributeTypeDef.
     * @param name String unique name for the AttributeTypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the one of AttributeTypeDef identifiers is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the requested AttributeTypeDef is not found in the metadata collection.
     * TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 AttributeTypeDef can be deleted or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/attribute-typedef/{guid}/delete")

    public VoidResponse deleteAttributeTypeDef(@PathVariable String    serverName,
                                               @PathVariable String    userId,
                                               @PathVariable String    guid,
                                               @RequestParam String    name)
    {
        return restAPI.deleteAttributeTypeDef(serverName, userId, guid, name);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param originalTypeDefGUID the original guid of the TypeDef.
     * @param requestParameters the original name of the TypeDef, the new identifier for the TypeDef and the
     *                         new name for this TypeDef.
     * @return TypeDefResponse:
     * typeDef: new values for this TypeDef, including the new guid/name or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the TypeDef identified by the original guid/name is not found
     *                                    in the metadata collection or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/typedef/{originalTypeDefGUID}/identifier")

    public  TypeDefResponse reIdentifyTypeDef(@PathVariable String                   serverName,
                                              @PathVariable String                   userId,
                                              @PathVariable String                   originalTypeDefGUID,
                                              @RequestBody  TypeDefReIdentifyRequest requestParameters)
    {
        return restAPI.reIdentifyTypeDef(serverName, userId,
                                         originalTypeDefGUID,
                                         requestParameters);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID the original guid of the AttributeTypeDef.
     * @param requestParameters the original name of the AttributeTypeDef and the new identifier for the AttributeTypeDef
     *                          and the new name for this AttributeTypeDef.
     * @return AttributeTypeDefResponse:
     * attributeTypeDef new values for this AttributeTypeDef, including the new guid/name or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the AttributeTypeDef identified by the original guid/name is not
     *                                    found in the metadata collection or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/types/attribute-typedef/{originalAttributeTypeDefGUID}/identifier")

    public  AttributeTypeDefResponse reIdentifyAttributeTypeDef(@PathVariable String                    serverName,
                                                                @PathVariable String                    userId,
                                                                @PathVariable String                    originalAttributeTypeDefGUID,
                                                                @RequestBody  TypeDefReIdentifyRequest  requestParameters)
    {
        return restAPI.reIdentifyAttributeTypeDef(serverName, userId,
                                                  originalAttributeTypeDefGUID,
                                                  requestParameters);
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

    public EntityDetailResponse getEntityDetail(@PathVariable String    serverName,
                                                @PathVariable String    userId,
                                                @PathVariable String    guid)
    {
        return restAPI.getEntityDetail(serverName, userId, guid);
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

    public  EntityDetailResponse getEntityDetail(@PathVariable String         serverName,
                                                 @PathVariable String         userId,
                                                 @PathVariable String         guid,
                                                 @RequestBody  HistoryRequest asOfTime)
    {
        return restAPI.getEntityDetail(serverName, userId, guid, asOfTime);
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
    public  ClassificationListResponse getClassificationHistory(@PathVariable String              serverName,
                                                      @PathVariable String              userId,
                                                      @PathVariable String              guid,
                                                      @PathVariable String              classificationName,
                                                      @RequestBody(required = false)  HistoryRangeRequest historyRangeRequest)
    {
        return restAPI.getClassificationHistory(serverName, userId, guid, classificationName, historyRangeRequest);
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

    public ClassificationResponse updateEntityClassification(@PathVariable String                      serverName,
                                                             @PathVariable String                      userId,
                                                             @PathVariable String                      classificationName,
                                                             @RequestBody  ProxyClassificationRequest  requestBody)
    {
        return restAPI.updateEntityClassification(serverName, userId, classificationName, requestBody);
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

    public RelationshipResponse restoreRelationship(@PathVariable String    serverName,
                                                    @PathVariable String    userId,
                                                    @PathVariable String    deletedRelationshipGUID)
    {
        return restAPI.restoreRelationship(serverName, userId, deletedRelationshipGUID);
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID new unique identifier for the entity.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new guid or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-identification or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/identity")

    public EntityDetailResponse reIdentifyEntity(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        entityGUID,
                                                 @RequestParam String                        newEntityGUID,
                                                 @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.reIdentifyEntity(serverName, userId, entityGUID, newEntityGUID, typeDefValidationForRequest);
    }


    /**
     * Change an existing entity's type.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param typeDefChangeRequest the details of the current and new type.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new type information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException the instance's properties are not valid for the new type.
     * ClassificationErrorException the instance's classifications are not valid for the new type.
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-typing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/type")

    public EntityDetailResponse reTypeEntity(@PathVariable String                serverName,
                                             @PathVariable String                userId,
                                             @PathVariable String                entityGUID,
                                             @RequestBody  TypeDefChangeRequest  typeDefChangeRequest)
    {
        return restAPI.reTypeEntity(serverName, userId, entityGUID, typeDefChangeRequest);
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param homeMetadataCollectionId the existing identifier for this entity's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository (optional).
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new home information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-homing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/home/{homeMetadataCollectionId}")

    public EntityDetailResponse reHomeEntity(@PathVariable String                        serverName,
                                             @PathVariable String                        userId,
                                             @PathVariable String                        entityGUID,
                                             @PathVariable String                        homeMetadataCollectionId,
                                             @RequestParam String                        newHomeMetadataCollectionId,
                                             @RequestParam(required=false) String        newHomeMetadataCollectionName,
                                             @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.reHomeEntity(serverName,
                                    userId,
                                    entityGUID,
                                    homeMetadataCollectionId,
                                    newHomeMetadataCollectionId,
                                    newHomeMetadataCollectionName,
                                    typeDefValidationForRequest);
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the existing identifier for the relationship.
     * @param newRelationshipGUID  the new unique identifier for the relationship.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return RelationshipResponse:
     * relationship: new values for this relationship, including the new guid or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-identification or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{relationshipGUID}/identity")

    public RelationshipResponse reIdentifyRelationship(@PathVariable String                        serverName,
                                                       @PathVariable String                        userId,
                                                       @PathVariable String                        relationshipGUID,
                                                       @RequestParam String                        newRelationshipGUID,
                                                       @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.reIdentifyRelationship(serverName, userId, relationshipGUID, newRelationshipGUID, typeDefValidationForRequest);
    }


    /**
     * Change an existing relationship's type.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefChangeRequest the details of the current and new type.
     * @return RelationshipResponse:
     * relationship: new values for this relationship, including the new type information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException the instance's properties are not valid for th new type.
     * RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-typing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{relationshipGUID}/type")

    public RelationshipResponse reTypeRelationship(@PathVariable String                serverName,
                                                   @PathVariable String                userId,
                                                   @PathVariable String                relationshipGUID,
                                                   @RequestBody  TypeDefChangeRequest  typeDefChangeRequest)
    {
        return restAPI.reTypeRelationship(serverName, userId, relationshipGUID, typeDefChangeRequest);
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param homeMetadataCollectionId the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName unique name for the new home metadata collection/repository.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return RelationshipResponse:
     * relationship: new values for this relationship, including the new home information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-homing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationship/{relationshipGUID}/home/{homeMetadataCollectionId}")

    public RelationshipResponse reHomeRelationship(@PathVariable String                        serverName,
                                                   @PathVariable String                        userId,
                                                   @PathVariable String                        relationshipGUID,
                                                   @PathVariable String                        homeMetadataCollectionId,
                                                   @RequestParam String                        newHomeMetadataCollectionId,
                                                   @RequestParam(required=false) String        newHomeMetadataCollectionName,
                                                   @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.reHomeRelationship(serverName,
                                          userId,
                                          relationshipGUID,
                                          homeMetadataCollectionId,
                                          newHomeMetadataCollectionId,
                                          newHomeMetadataCollectionName,
                                          typeDefValidationForRequest);
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entity details of the entity to save.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * EntityConflictException the new entity conflicts with an existing entity or
     * InvalidEntityException the new entity has invalid contents or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/reference-copy")

    public VoidResponse saveEntityReferenceCopy(@PathVariable String       serverName,
                                                @PathVariable String       userId,
                                                @RequestBody  EntityDetail entity)
    {
        return restAPI.saveEntityReferenceCopy(serverName, userId, entity);
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @return list of all the classifications for this entity that are homed in this repository or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity is not recognized by this repository or
     * UserNotAuthorizedException to calling user is not authorized to retrieve this metadata or
     * FunctionNotSupportedException this method is not supported
     */
    @GetMapping(path = "/instances/entity/{entityGUID}/home-classifications")

    public ClassificationListResponse getHomeClassifications(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String entityGUID)
    {
        return restAPI.getHomeClassifications(serverName, userId, entityGUID);
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @param requestBody the time used to determine which version of the entity that is desired.
     * @return list of all the classifications for this entity that are homed in this repository or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity is not recognized by this repository or
     * UserNotAuthorizedException to calling user is not authorized to retrieve this metadata or
     * FunctionNotSupportedException this method is not supported
     */
    @PostMapping(path = "/instances/entity/{entityGUID}/home-classifications/history")

    public ClassificationListResponse getHomeClassifications(@PathVariable String         serverName,
                                                             @PathVariable String         userId,
                                                             @PathVariable String         entityGUID,
                                                             @RequestBody  HistoryRequest requestBody)
    {
        return restAPI.getHomeClassifications(serverName, userId, entityGUID, requestBody);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param entity the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/reference-copy/delete")

    public VoidResponse deleteEntityReferenceCopy(@PathVariable String                        serverName,
                                                  @PathVariable String                        userId,
                                                  @RequestBody  EntityDetail                  entity)
    {
        return restAPI.deleteEntityReferenceCopy(serverName, userId, entity);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param entity the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * EntityConflictException the new entity conflicts with an existing entity or
     * InvalidEntityException the new entity has invalid contents or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/reference-copy/purge")

    public VoidResponse purgeEntityReferenceCopy(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @RequestBody  EntityDetail                  entity)
    {
        return restAPI.purgeEntityReferenceCopy(serverName, userId, entity);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param entityGUID the unique identifier for the entity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/reference-copy/{entityGUID}/purge")

    public VoidResponse purgeEntityReferenceCopy(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        entityGUID,
                                                 @RequestParam String                        homeMetadataCollectionId,
                                                 @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.purgeEntityReferenceCopy(serverName, userId, entityGUID, homeMetadataCollectionId, typeDefValidationForRequest);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/entities/reference-copy/{entityGUID}/refresh")

    public VoidResponse refreshEntityReferenceCopy(@PathVariable String                        serverName,
                                                   @PathVariable String                        userId,
                                                   @PathVariable String                        entityGUID,
                                                   @RequestParam String                        homeMetadataCollectionId,
                                                   @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.refreshEntityReferenceCopy(serverName, userId,
                                                  entityGUID,
                                                  homeMetadataCollectionId,
                                                  typeDefValidationForRequest);
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody entity that the classification is attached to and classification to save.
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or null.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                          the metadata collection is stored.
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                        characteristics in the TypeDef for this classification type.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                    hosting the metadata collection.
     * EntityConflictException the new entity conflicts with an existing entity.
     * InvalidEntityException the new entity has invalid contents.
     * FunctionNotSupportedException the repository does not support reference copies of instances.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "instances/entities/classifications/reference-copy")

    public VoidResponse saveClassificationReferenceCopy(@PathVariable String                          serverName,
                                                        @PathVariable String                          userId,
                                                        @RequestBody  ClassificationWithEntityRequest requestBody)
    {
        return restAPI.saveClassificationReferenceCopy(serverName, userId, requestBody);
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody entity that the classification is attached to and classification to purge.
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or null.
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                        characteristics in the TypeDef for this classification type.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                          the metadata collection is stored.
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                    hosting the metadata collection.
     * EntityConflictException the new entity conflicts with an existing entity.
     * InvalidEntityException the new entity has invalid contents.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @PostMapping(path = "instances/entities/classifications/reference-copy/purge")

    public  VoidResponse purgeClassificationReferenceCopy(@PathVariable String                          serverName,
                                                          @PathVariable String                          userId,
                                                          @RequestBody  ClassificationWithEntityRequest requestBody)
    {
        return restAPI.purgeClassificationReferenceCopy(serverName, userId, requestBody);
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting userId.
     * @param relationship relationship to save.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * RelationshipConflictException the new relationship conflicts with an existing relationship.
     * InvalidRelationshipException the new relationship has invalid contents or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/reference-copy")

    public VoidResponse saveRelationshipReferenceCopy(@PathVariable String         serverName,
                                                      @PathVariable String         userId,
                                                      @RequestBody  Relationship   relationship)
    {
        return restAPI.saveRelationshipReferenceCopy(serverName, userId, relationship);
    }



    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param relationship the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * RelationshipConflictException the new relationship conflicts with an existing relationship.
     * InvalidRelationshipException the new relationship has invalid contents or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/reference-copy/delete")

    public VoidResponse deleteRelationshipReferenceCopy(@PathVariable String                        serverName,
                                                        @PathVariable String                        userId,
                                                        @RequestBody  Relationship                  relationship)
    {
        return restAPI.deleteRelationshipReferenceCopy(serverName, userId, relationship);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param relationship the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * RelationshipConflictException the new relationship conflicts with an existing relationship.
     * InvalidRelationshipException the new relationship has invalid contents or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/reference-copy/purge")

    public VoidResponse purgeRelationshipReferenceCopy(@PathVariable String                        serverName,
                                                       @PathVariable String                        userId,
                                                       @RequestBody  Relationship                  relationship)
    {
        return restAPI.purgeRelationshipReferenceCopy(serverName, userId, relationship);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identifier is not recognized or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/reference-copy/{relationshipGUID}/purge")

    public VoidResponse purgeRelationshipReferenceCopy(@PathVariable String                        serverName,
                                                       @PathVariable String                        userId,
                                                       @PathVariable String                        relationshipGUID,
                                                       @RequestParam String                        homeMetadataCollectionId,
                                                       @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.purgeRelationshipReferenceCopy(serverName, userId,
                                                      relationshipGUID,
                                                      homeMetadataCollectionId,
                                                      typeDefValidationForRequest);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID unique identifier of the relationship.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @param typeDefValidationForRequest information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identifier is not recognized or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances/relationships/reference-copy/{relationshipGUID}/refresh")

    public VoidResponse refreshRelationshipReferenceCopy(@PathVariable String                        serverName,
                                                         @PathVariable String                        userId,
                                                         @PathVariable String                        relationshipGUID,
                                                         @RequestParam String                        homeMetadataCollectionId,
                                                         @RequestBody  TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        return restAPI.refreshRelationshipReferenceCopy(serverName, userId,
                                                        relationshipGUID,
                                                        homeMetadataCollectionId,
                                                        typeDefValidationForRequest);
    }


    /**
     * Save the entities and relationships supplied in the instance graph as a reference copies.
     * The id of the home metadata collection is already set up in the instances.
     * Any instances from the home metadata collection are ignored.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param instances instances to save
     * @return void response or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException  there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * EntityConflictException the new entity conflicts with an existing entity or
     * InvalidEntityException the new entity has invalid contents or
     * RelationshipConflictException the new relationship conflicts with an existing relationship or
     * InvalidRelationshipException the new relationship has invalid contents or
     * FunctionNotSupportedException the repository does not support reference copies of instances or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @PostMapping(path = "/instances")

    public VoidResponse  saveInstanceReferenceCopies(@PathVariable String                 serverName,
                                                     @PathVariable String                 userId,
                                                     @RequestBody  InstanceGraphRequest   instances)
    {
        return restAPI.saveInstanceReferenceCopies(serverName, userId, instances);
    }
}
