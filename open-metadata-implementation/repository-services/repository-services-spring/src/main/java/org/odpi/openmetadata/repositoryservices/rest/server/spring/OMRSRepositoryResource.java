/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * OMRSRepositoryRESTServices provides the server-side support for the OMRS Repository REST Services API.
 * It is a minimal wrapper around the OMRSRepositoryConnector for the local server's metadata collection.
 * If localRepositoryConnector is null when a REST call is received, the request is rejected.
 *
 * The REST services are based around the OMRSMetadataInstanceStore interface.
 * * <p>
 *     OMRSMetadataInstanceStore is the common interface for working with the contents of a metadata repository.
 *     Within a metadata collection are the type definitions (TypeDefs) and metadata instances (Entities and
 *     Relationships).  OMRSMetadataCollectionBase provides empty implementation of the the abstract methods of
 *     OMRSMetadataInstanceStore.
 *
 *     The methods on OMRSMetadataInstanceStore are in the following major groups:
 * </p>
 * <ul>
 *     <li><b>Methods to retrieve information about the metadata repository</b> -
 *         Used to retrieve or confirm the identity of the metadata repository
 *     </li>
 *     <li><b>Methods for working with typedefs</b> -
 *         Typedefs are used to define the type model for open metadata.
 *         The open metadata support had a comprehensive set of typedefs implemented, and these can be augmented by
 *         different vendors or applications.  The typedefs can be queried, created, updated and deleted though the
 *         metadata collection.
 *     </li>
 *
 *     <li><b>Methods for querying Entities and Relationships</b> -
 *         The metadata repository stores instances of the typedefs as metadata instances.
 *         Principally these are entities (nodes in the graph) and relationships (links between nodes).
 *         Both the entities and relationships can have properties.
 *         The entity may also have structured properties called structs and classifications attached.
 *         This second group of methods supports a range of queries to retrieve these instances.
 *     </li>
 *
 *     <li><b>Methods for maintaining the instances</b> -
 *         The fourth group of methods supports the maintenance of the metadata instances.  Each instance as a status
 *         (see InstanceStatus) that allows an instance to be proposed, drafted and approved before it becomes
 *         active.  The instances can also be soft-deleted and restored or purged from the metadata
 *         collection.
 *     </li>
 *     <li>
 *         <b>Methods for repairing the metadata collections of the cohort</b> -
 *         The fifth group of methods are for editing the control information of entities and relationships to
 *         manage changes in the cohort.  These methods are advanced methods and are rarely used.
 *     </li>
 *     <li>
 *         <b>Methods for local maintenance of a metadata collection</b>
 *         The final group of methods are for removing reference copies of the metadata instances.  These updates
 *         are not broadcast to the rest of the Cohort as events.
 *     </li>
 * </ul>
 */
@RestController
@RequestMapping("/open-metadata/repository-services")
public class OMRSRepositoryResource
{
    private OMRSRepositoryRESTServices  restAPI = new OMRSRepositoryRESTServices();

    /**
     * Default constructor
     */
    public OMRSRepositoryResource()
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
     * @return String metadata collection id.
     * or RepositoryErrorException if there is a problem communicating with the metadata repository.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/metadata-collection-id")

    public MetadataCollectionIdResponse      getMetadataCollectionId()
    {
        return restAPI.getMetadataCollectionId();
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
     * @param userId unique identifier for requesting user.
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/all")

    public TypeDefGalleryResponse getAllTypes(@PathVariable String   userId)
    {
        return restAPI.getAllTypes(userId);
    }


    /**
     * Returns a list of type definitions that have the specified name.  Type names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an
     * arbitrary string of characters and ampersand for an arbitrary character.
     *
     * @param userId unique identifier for requesting user.
     * @param name name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation or
     * InvalidParameterException the name of the TypeDef is null.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/by-name")

    public TypeDefGalleryResponse findTypesByName(@PathVariable String userId,
                                                  @RequestParam String name)
    {
        return restAPI.findTypesByName(userId, name);
    }


    /**
     * Returns all of the TypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category enum value for the category of TypeDef to return.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedefs/by-category")

    public TypeDefListResponse findTypeDefsByCategory(@PathVariable String          userId,
                                                      @RequestParam TypeDefCategory category)
    {
        return restAPI.findTypeDefsByCategory(userId, category);
    }


    /**
     * Returns all of the AttributeTypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category enum value for the category of an AttributeTypeDef to return.
     * @return AttributeTypeDefListResponse:
     * AttributeTypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/attribute-typedefs/by-category")

    public AttributeTypeDefListResponse findAttributeTypeDefsByCategory(@PathVariable String                   userId,
                                                                        @RequestParam AttributeTypeDefCategory category)
    {
        return restAPI.findAttributeTypeDefsByCategory(userId, category);
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties a list of property names and values.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the matchCriteria is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedefs/by-property")

    public TypeDefListResponse findTypeDefsByProperty(@PathVariable String            userId,
                                                      @RequestParam TypeDefProperties matchCriteria)
    {
        return restAPI.findTypeDefsByProperty(userId, matchCriteria);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedefs/by-external-id")

    public TypeDefListResponse findTypesByExternalID(@PathVariable String    userId,
                                                     @RequestParam String    standard,
                                                     @RequestParam String    organization,
                                                     @RequestParam String    identifier)
    {
        return restAPI.findTypesByExternalID(userId, standard, organization, identifier);
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the searchCriteria is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedefs/by-property-value")

    public TypeDefListResponse searchForTypeDefs(@PathVariable String userId,
                                                 @RequestParam String searchCriteria)
    {
        return restAPI.searchForTypeDefs(userId, searchCriteria);
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedef/{guid}")

    public TypeDefResponse getTypeDefByGUID(@PathVariable String    userId,
                                            @PathVariable String    guid)
    {
        return restAPI.getTypeDefByGUID(userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/attribute-typedef/{guid}")

    public AttributeTypeDefResponse getAttributeTypeDefByGUID(@PathVariable String    userId,
                                                              @PathVariable String    guid)
    {
        return restAPI.getAttributeTypeDefByGUID(userId, guid);
    }



    /**
     * Return the TypeDef identified by the unique name.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedef/name/{name}")

    public TypeDefResponse getTypeDefByName(@PathVariable String    userId,
                                            @PathVariable String    name)
    {
        return restAPI.getTypeDefByName(userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/attribute-typedef/name/{name}")

    public  AttributeTypeDefResponse getAttributeTypeDefByName(@PathVariable String    userId,
                                                               @PathVariable String    name)
    {
        return restAPI.getAttributeTypeDefByName(userId, name);
    }


    /**
     * Create a collection of related types.
     *
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/types")

    public  VoidResponse addTypeDefGallery(@PathVariable String         userId,
                                           @RequestParam TypeDefGallery newTypes)
    {
        return restAPI.addTypeDefGallery(userId, newTypes);
    }


    /**
     * Create a definition of a new TypeDef.
     *
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/types/typedef")

    public VoidResponse addTypeDef(@PathVariable String  userId,
                                   @RequestParam TypeDef newTypeDef)
    {
        return restAPI.addTypeDef(userId, newTypeDef);
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/types/attribute-typedef")

    public  VoidResponse addAttributeTypeDef(@PathVariable String             userId,
                                             @RequestParam AttributeTypeDef   newAttributeTypeDef)
    {
        return restAPI.addAttributeTypeDef(userId, newAttributeTypeDef);
    }


    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/typedef/compatibility")

    public BooleanResponse verifyTypeDef(@PathVariable String       userId,
                                         @RequestParam TypeDef      typeDef)
    {
        return restAPI.verifyTypeDef(userId, typeDef);
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/types/attribute-typedef/compatibility")

    public  BooleanResponse verifyAttributeTypeDef(@PathVariable String            userId,
                                                   @RequestParam AttributeTypeDef  attributeTypeDef)
    {
        return restAPI.verifyAttributeTypeDef(userId, attributeTypeDef);
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/types/typedef")

    public TypeDefResponse updateTypeDef(@PathVariable String       userId,
                                         @RequestParam TypeDefPatch typeDefPatch)
    {
        return restAPI.updateTypeDef(userId, typeDefPatch);
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName String unique name for the TypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the one of TypeDef identifiers is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * TypeDefInUseException the TypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 TypeDef can be deleted or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/types/typedef/{obsoleteTypeDefGUID}")

    public VoidResponse deleteTypeDef(@PathVariable String    userId,
                                      @PathVariable String    obsoleteTypeDefGUID,
                                      @RequestParam String    obsoleteTypeDefName)
    {
        return restAPI.deleteTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteTypeDefName String unique name for the AttributeTypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the one of AttributeTypeDef identifiers is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the requested AttributeTypeDef is not found in the metadata collection.
     * TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 AttributeTypeDef can be deleted or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/types/attribute-typedef/{obsoleteTypeDefGUID}")

    public VoidResponse deleteAttributeTypeDef(@PathVariable String    userId,
                                               @PathVariable String    obsoleteTypeDefGUID,
                                               @RequestParam String    obsoleteTypeDefName)
    {
        return restAPI.deleteAttributeTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalTypeDefGUID the original guid of the TypeDef.
     * @param originalTypeDefName the original name of the TypeDef.
     * @param newTypeDefGUID the new identifier for the TypeDef.
     * @param newTypeDefName new name for this TypeDef.
     * @return TypeDefResponse:
     * typeDef new values for this TypeDef, including the new guid/name or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the TypeDef identified by the original guid/name is not found
     *                                    in the metadata collection or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/types/typedef/{originalTypeDefGUID}/identifier")

    public  TypeDefResponse reIdentifyTypeDef(@PathVariable String     userId,
                                              @PathVariable String     originalTypeDefGUID,
                                              @RequestParam String     originalTypeDefName,
                                              @RequestParam String     newTypeDefGUID,
                                              @RequestParam String     newTypeDefName)
    {
        return restAPI.reIdentifyTypeDef(userId,
                                         originalTypeDefGUID,
                                         originalTypeDefName,
                                         newTypeDefGUID,
                                         newTypeDefName);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID the original guid of the AttributeTypeDef.
     * @param originalAttributeTypeDefName the original name of the AttributeTypeDef.
     * @param newAttributeTypeDefGUID the new identifier for the AttributeTypeDef.
     * @param newAttributeTypeDefName new name for this AttributeTypeDef.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/types/attribute-typedef/{originalAttributeTypeDefGUID}/identifier")

    public  AttributeTypeDefResponse reIdentifyAttributeTypeDef(@PathVariable String     userId,
                                                                @PathVariable String     originalAttributeTypeDefGUID,
                                                                @RequestParam String     originalAttributeTypeDefName,
                                                                @RequestParam String     newAttributeTypeDefGUID,
                                                                @RequestParam String     newAttributeTypeDefName)
    {
        return restAPI.reIdentifyAttributeTypeDef(userId,
                                                  originalAttributeTypeDefGUID,
                                                  originalAttributeTypeDefName,
                                                  newAttributeTypeDefGUID,
                                                  newAttributeTypeDefName);
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns a boolean indicating if the entity is stored in the metadata collection.  This entity may be a full
     * entity object, or an entity proxy.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * InvalidParameterException the guid is null.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entity/{guid}/existence")

    public EntityDetailResponse isEntityKnown(@PathVariable String     userId,
                                              @PathVariable String     guid)
    {
        return restAPI.isEntityKnown(userId, guid);
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return EntitySummary structure or
     * InvalidParameterException the guid is null.
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entity/{guid}/summary")

    public EntitySummaryResponse getEntitySummary(@PathVariable String     userId,
                                                  @PathVariable String     guid)
    {
        return restAPI.getEntitySummary(userId, guid);
    }


    /**
     * Return the header, classifications and properties of a specific entity.  This method supports anonymous
     * access to an instance.  The call may fail if the metadata is secured.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/instances/entity/{guid}")

    public EntityDetailResponse getEntityDetail(@PathVariable String     guid)
    {
        return restAPI.getEntityDetail(null, guid);
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entity/{guid}")

    public EntityDetailResponse getEntityDetail(@PathVariable String     userId,
                                                @PathVariable String     guid)
    {
        return restAPI.getEntityDetail(userId, guid);
    }


    /**
     * Return a historical version of an entity includes the header, classifications and properties of the entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EnityDetailResponse:
     * EntityDetail structure or
     * InvalidParameterException the guid or date is null or the asOfTime property is for a future time or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested or
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection or
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entity/{guid}/history")

    public  EntityDetailResponse getEntityDetail(@PathVariable String     userId,
                                                 @PathVariable String     guid,
                                                 @RequestParam Date       asOfTime)
    {
        return restAPI.getEntityDetail(userId, guid, asOfTime);
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return RelationshipListResponse:
     * Relationships list.  Null means no relationships associated with the entity or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * PropertyErrorException the sequencing property is not valid for the attached classifications or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation or
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entity/{entityGUID}/relationships")

    public RelationshipListResponse getRelationshipsForEntity(@PathVariable                   String                     userId,
                                                              @PathVariable                   String                     entityGUID,
                                                              @RequestParam(required = false) String                     relationshipTypeGUID,
                                                              @RequestParam(required = false) int                        fromRelationshipElement,
                                                              @RequestParam(required = false) List<InstanceStatus>       limitResultsByStatus,
                                                              @RequestParam(required = false) Date                       asOfTime,
                                                              @RequestParam(required = false) String                     sequencingProperty,
                                                              @RequestParam(required = false) SequencingOrder sequencingOrder,
                                                              @RequestParam(required = false) int                        pageSize)
    {
        return restAPI.getRelationshipsForEntity(userId,
                                                 entityGUID,
                                                 relationshipTypeGUID,
                                                 fromRelationshipElement,
                                                 limitResultsByStatus,
                                                 asOfTime,
                                                 sequencingProperty,
                                                 sequencingOrder,
                                                 pageSize);
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties List of entity properties to match to (null means match on entityTypeGUID only).
     * @param matchCriteria Enum defining how the properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
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
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entities/by-property")

    public  EntityListResponse findEntitiesByProperty(@PathVariable                   String                    userId,
                                                      @RequestParam(required = false) String                    entityTypeGUID,
                                                      @RequestParam(required = false) InstanceProperties        matchProperties,
                                                      @RequestParam(required = false) MatchCriteria             matchCriteria,
                                                      @RequestParam(required = false) int                       fromEntityElement,
                                                      @RequestParam(required = false) List<InstanceStatus>      limitResultsByStatus,
                                                      @RequestParam(required = false) List<String>              limitResultsByClassification,
                                                      @RequestParam(required = false) Date                      asOfTime,
                                                      @RequestParam(required = false) String                    sequencingProperty,
                                                      @RequestParam(required = false) SequencingOrder           sequencingOrder,
                                                      @RequestParam(required = false) int                       pageSize)
    {
        return restAPI.findEntitiesByProperty(userId,
                                              entityTypeGUID,
                                              matchProperties,
                                              matchCriteria,
                                              fromEntityElement,
                                              limitResultsByStatus,
                                              limitResultsByClassification,
                                              asOfTime,
                                              sequencingProperty,
                                              sequencingOrder,
                                              pageSize);
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null mans any type of entity.
     * @param classificationName name of the classification a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search.
     * @param matchCriteria Enum defining how the properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
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
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entities/by-classification/{classificationName}")

    public  EntityListResponse findEntitiesByClassification(@PathVariable                   String                    userId,
                                                            @RequestParam(required = false) String                    entityTypeGUID,
                                                            @PathVariable                   String                    classificationName,
                                                            @RequestParam(required = false) InstanceProperties        matchClassificationProperties,
                                                            @RequestParam(required = false) MatchCriteria             matchCriteria,
                                                            @RequestParam(required = false) int                       fromEntityElement,
                                                            @RequestParam(required = false) List<InstanceStatus>      limitResultsByStatus,
                                                            @RequestParam(required = false) Date                      asOfTime,
                                                            @RequestParam(required = false) String                    sequencingProperty,
                                                            @RequestParam(required = false) SequencingOrder           sequencingOrder,
                                                            @RequestParam(required = false) int                       pageSize)
    {
        return restAPI.findEntitiesByClassification(userId,
                                                    entityTypeGUID,
                                                    classificationName,
                                                    matchClassificationProperties,
                                                    matchCriteria,
                                                    fromEntityElement,
                                                    limitResultsByStatus,
                                                    asOfTime,
                                                    sequencingProperty,
                                                    sequencingOrder,
                                                    pageSize);
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
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
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entities/by-property-value")

    public  EntityListResponse findEntitiesByPropertyValue(@PathVariable                   String                  userId,
                                                           @RequestParam(required = false) String                  entityTypeGUID,
                                                           @RequestParam                   String                  searchCriteria,
                                                           @RequestParam(required = false) int                     fromEntityElement,
                                                           @RequestParam(required = false) List<InstanceStatus>    limitResultsByStatus,
                                                           @RequestParam(required = false) List<String>            limitResultsByClassification,
                                                           @RequestParam(required = false) Date                    asOfTime,
                                                           @RequestParam(required = false) String                  sequencingProperty,
                                                           @RequestParam(required = false) SequencingOrder         sequencingOrder,
                                                           @RequestParam(required = false) int                     pageSize)
    {
        return restAPI.findEntitiesByPropertyValue(userId,
                                                   entityTypeGUID,
                                                   searchCriteria,
                                                   fromEntityElement,
                                                   limitResultsByStatus,
                                                   limitResultsByClassification,
                                                   asOfTime,
                                                   sequencingProperty,
                                                   sequencingOrder,
                                                   pageSize);
    }


    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return RelationshipResponse:
     * relationship details if the relationship is found in the metadata collection; otherwise return null or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/relationship/{guid}/existence")

    public RelationshipResponse  isRelationshipKnown(@PathVariable String     userId,
                                                     @PathVariable String     guid)
    {
        return restAPI.isRelationshipKnown(userId, guid);
    }


    /**
     * Return a requested relationship.  This is the anonymous form for repository.  The call may fail if security is
     * required.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/instances/relationship/{guid}")

    public RelationshipResponse getRelationship(@PathVariable String    guid)
    {
        return restAPI.getRelationship(null, guid);
    }


    /**
     * Return a requested relationship.
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/relationship/{guid}")

    public RelationshipResponse getRelationship(@PathVariable String    userId,
                                                @PathVariable String    guid)
    {
        return restAPI.getRelationship(userId, guid);
    }


    /**
     * Return a historical version of a relationship.
     *
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
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/relationship/{guid}/history")

    public  RelationshipResponse getRelationship(@PathVariable String    userId,
                                                 @PathVariable String    guid,
                                                 @RequestParam Date      asOfTime)
    {
        return restAPI.getRelationship(userId, guid, asOfTime);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
     * @param userId unique identifier for requesting user
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param matchProperties list of  properties used to narrow the search.
     * @param matchCriteria Enum defining how the properties should be matched to the relationships in the repository.
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/relationships/by-property")

    public  RelationshipListResponse findRelationshipsByProperty(@PathVariable                   String                    userId,
                                                                 @RequestParam(required = false) String                    relationshipTypeGUID,
                                                                 @RequestParam(required = false) InstanceProperties        matchProperties,
                                                                 @RequestParam(required = false) MatchCriteria             matchCriteria,
                                                                 @RequestParam(required = false) int                       fromRelationshipElement,
                                                                 @RequestParam(required = false) List<InstanceStatus>      limitResultsByStatus,
                                                                 @RequestParam(required = false) Date                      asOfTime,
                                                                 @RequestParam(required = false) String                    sequencingProperty,
                                                                 @RequestParam(required = false) SequencingOrder           sequencingOrder,
                                                                 @RequestParam(required = false) int                       pageSize)
    {
        return restAPI.findRelationshipsByProperty(userId,
                                                   relationshipTypeGUID,
                                                   matchProperties,
                                                   matchCriteria,
                                                   fromRelationshipElement,
                                                   limitResultsByStatus,
                                                   asOfTime,
                                                   sequencingProperty,
                                                   sequencingOrder,
                                                   pageSize);
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier of a relationship type (or null for all types of relationship.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param fromRelationshipElement Element number of the results to skip to when building the results list
     *                                to return.  Zero means begin at the start of the results.  This is used
     *                                to retrieve the results over a number of pages.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * PropertyErrorException there is a problem with one of the other parameters  or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/relationships/by-property-value")

    public  RelationshipListResponse findRelationshipsByPropertyValue(@PathVariable                   String                    userId,
                                                                      @RequestParam(required = false) String                    relationshipTypeGUID,
                                                                      @RequestParam                   String                    searchCriteria,
                                                                      @RequestParam(required = false) int                       fromRelationshipElement,
                                                                      @RequestParam(required = false) List<InstanceStatus>      limitResultsByStatus,
                                                                      @RequestParam(required = false) Date                      asOfTime,
                                                                      @RequestParam(required = false) String                    sequencingProperty,
                                                                      @RequestParam(required = false) SequencingOrder           sequencingOrder,
                                                                      @RequestParam(required = false) int                       pageSize)
    {
        return restAPI.findRelationshipsByPropertyValue(userId,
                                                        relationshipTypeGUID,
                                                        searchCriteria,
                                                        fromRelationshipElement,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        pageSize);
    }




    /**
     * Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @return InstanceGraphResponse:
     * the sub-graph that represents the returned linked entities and their relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by either the startEntityGUID or the endEntityGUID
     *                                   is not found in the metadata collection or
     * PropertyErrorException there is a problem with one of the other parameters or
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entities/from-entity/{startEntityGUID}/by-linkage")

    public  InstanceGraphResponse getLinkingEntities(@PathVariable                   String                    userId,
                                                     @PathVariable                   String                    startEntityGUID,
                                                     @RequestParam                   String                    endEntityGUID,
                                                     @RequestParam(required = false) List<InstanceStatus>      limitResultsByStatus,
                                                     @RequestParam(required = false) Date                      asOfTime)
    {
        return restAPI.getLinkingEntities(userId, startEntityGUID, endEntityGUID, limitResultsByStatus, asOfTime);
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param entityTypeGUIDs list of entity types to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipTypeGUIDs list of relationship types to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @return InstanceGraphResponse
     * the sub-graph that represents the returned linked entities and their relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException one of the type guids passed on the request is not known by the metadata collection or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection or
     * PropertyErrorException there is a problem with one of the other parameters or
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entities/from-entity/{entityGUID}/by-neighborhood")

    public  InstanceGraphResponse getEntityNeighborhood(@PathVariable                   String               userId,
                                                        @PathVariable                   String               entityGUID,
                                                        @RequestParam(required = false) List<String>         entityTypeGUIDs,
                                                        @RequestParam(required = false) List<String>         relationshipTypeGUIDs,
                                                        @RequestParam(required = false) List<InstanceStatus> limitResultsByStatus,
                                                        @RequestParam(required = false) List<String>         limitResultsByClassification,
                                                        @RequestParam(required = false) Date                 asOfTime,
                                                        @RequestParam                   int                  level)
    {
        return restAPI.getEntityNeighborhood(userId,
                                             entityGUID,
                                             entityTypeGUIDs,
                                             relationshipTypeGUIDs,
                                             limitResultsByStatus,
                                             limitResultsByClassification,
                                             asOfTime,
                                             level);
    }



    /**
     * Return the list of entities that are of the types listed in instanceTypes and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param instanceTypes list of types to search for.  Null means any type.
     * @param fromEntityElement starting element for results list.  Used in paging.  Zero means first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
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
     * FunctionNotSupportedException the repository does not support satOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/instances/entities/from-entity/{startEntityGUID}/by-relationship")

    public  EntityListResponse getRelatedEntities(@PathVariable                   String               userId,
                                                  @PathVariable                   String               startEntityGUID,
                                                  @RequestParam(required = false) List<String>         instanceTypes,
                                                  @RequestParam(required = false) int                  fromEntityElement,
                                                  @RequestParam(required = false) List<InstanceStatus> limitResultsByStatus,
                                                  @RequestParam(required = false) List<String>         limitResultsByClassification,
                                                  @RequestParam(required = false) Date                 asOfTime,
                                                  @RequestParam(required = false) String               sequencingProperty,
                                                  @RequestParam(required = false) SequencingOrder      sequencingOrder,
                                                  @RequestParam(required = false) int                  pageSize)
    {
        return restAPI.getRelatedEntities(userId,
                                          startEntityGUID,
                                          instanceTypes,
                                          fromEntityElement,
                                          limitResultsByStatus,
                                          limitResultsByClassification,
                                          asOfTime,
                                          sequencingProperty,
                                          sequencingOrder,
                                          pageSize);
    }


    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/instances/entity")

    public EntityDetailResponse addEntity(@PathVariable                   String                     userId,
                                          @RequestParam                   String                     entityTypeGUID,
                                          @RequestParam(required = false) InstanceProperties         initialProperties,
                                          @RequestParam(required = false) List<Classification>       initialClassifications,
                                          @RequestParam                   InstanceStatus             initialStatus)
    {
        return restAPI.addEntity(userId, entityTypeGUID, initialProperties, initialClassifications, initialStatus);
    }



    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/instances/entity-proxy")

    public VoidResponse addEntityProxy(@PathVariable String      userId,
                                       @RequestParam EntityProxy entityProxy)
    {
        return restAPI.addEntityProxy(userId, entityProxy);
    }


    /**
     * Update the status for a specific entity.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/status")

    public EntityDetailResponse updateEntityStatus(@PathVariable String           userId,
                                                   @PathVariable String           entityGUID,
                                                   @RequestParam InstanceStatus   newStatus)
    {
        return restAPI.updateEntityStatus(userId, entityGUID, newStatus);
    }


    /**
     * Update selected properties in an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type or
     * UserNotAuthorizedException the userId is not permitted to perform this operation or
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/properties")

    public EntityDetailResponse updateEntityProperties(@PathVariable String               userId,
                                                       @PathVariable String               entityGUID,
                                                       @RequestParam InstanceProperties   properties)
    {
        return restAPI.updateEntityProperties(userId, entityGUID, properties);
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/undo")

    public EntityDetailResponse undoEntityUpdate(@PathVariable String  userId,
                                                 @PathVariable String  entityGUID)
    {
        return restAPI.undoEntityUpdate(userId, entityGUID);
    }


    /**
     * Delete an entity.  The entity is soft deleted.  This means it is still in the graph but it is no longer returned
     * on queries.  All relationships to the entity are also soft-deleted and will no longer be usable.
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{obsoleteEntityGUID}/delete")

    public EntityDetailResponse  deleteEntity(@PathVariable String    userId,
                                              @RequestParam String    typeDefGUID,
                                              @RequestParam String    typeDefName,
                                              @PathVariable String    obsoleteEntityGUID)
    {
        return restAPI.deleteEntity(userId, typeDefGUID, typeDefName, obsoleteEntityGUID);
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to purge.
     * @param typeDefName unique name of the type of the entity to purge.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * EntityNotDeletedException the entity is not in DELETED status and so can not be purged or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{deletedEntityGUID}/purge")

    public VoidResponse purgeEntity(@PathVariable String    userId,
                                    @RequestParam String    typeDefGUID,
                                    @RequestParam String    typeDefName,
                                    @PathVariable String    deletedEntityGUID)
    {
        return restAPI.purgeEntity(userId, typeDefGUID, typeDefName, deletedEntityGUID);
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{deletedEntityGUID}/restore")

    public EntityDetailResponse restoreEntity(@PathVariable String    userId,
                                              @PathVariable String    deletedEntityGUID)
    {
        return restAPI.restoreEntity(userId, deletedEntityGUID);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/classification/{classificationName}")

    public EntityDetailResponse classifyEntity(@PathVariable                   String               userId,
                                               @PathVariable                   String               entityGUID,
                                               @PathVariable                   String               classificationName,
                                               @RequestParam(required = false) InstanceProperties   classificationProperties)
    {
        return restAPI.classifyEntity(userId, entityGUID, classificationName, classificationProperties);
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * ClassificationErrorException the requested classification is not set on the entity or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/classification/{classificationName}/delete")

    public EntityDetailResponse declassifyEntity(@PathVariable String  userId,
                                                 @PathVariable String  entityGUID,
                                                 @PathVariable String  classificationName)
    {
        return restAPI.declassifyEntity(userId, entityGUID, classificationName);
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/classification/{classificationName}/properties")

    public EntityDetailResponse updateEntityClassification(@PathVariable String               userId,
                                                           @PathVariable String               entityGUID,
                                                           @PathVariable String               classificationName,
                                                           @RequestParam InstanceProperties   properties)
    {
        return restAPI.updateEntityClassification(userId, entityGUID, classificationName, properties);
    }


    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/instances/relationship")

    public RelationshipResponse addRelationship(@PathVariable                   String               userId,
                                                @RequestParam                   String               relationshipTypeGUID,
                                                @RequestParam(required = false) InstanceProperties   initialProperties,
                                                @RequestParam                   String               entityOneGUID,
                                                @RequestParam                   String               entityTwoGUID,
                                                @RequestParam                   InstanceStatus       initialStatus)
    {
        return restAPI.addRelationship(userId,
                                       relationshipTypeGUID,
                                       initialProperties,
                                       entityOneGUID,
                                       entityTwoGUID,
                                       initialStatus);
    }


    /**
     * Update the status of a specific relationship.
     *
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
     * UserNotAuthorizedException the userId is not permitted to perform this operation or
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{relationshipGUID}/status")

    public RelationshipResponse updateRelationshipStatus(@PathVariable String           userId,
                                                         @PathVariable String           relationshipGUID,
                                                         @RequestParam InstanceStatus   newStatus)
    {
        return restAPI.updateRelationshipStatus(userId, relationshipGUID, newStatus);
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{relationshipGUID}/properties")

    public RelationshipResponse updateRelationshipProperties(@PathVariable String               userId,
                                                             @PathVariable String               relationshipGUID,
                                                             @RequestParam InstanceProperties   properties)
    {
        return restAPI.updateRelationshipProperties(userId, relationshipGUID, properties);
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{relationshipGUID}/undo")

    public RelationshipResponse undoRelationshipUpdate(@PathVariable String  userId,
                                                       @PathVariable String  relationshipGUID)
    {
        return restAPI.undoRelationshipUpdate(userId, relationshipGUID);
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{obsoleteRelationshipGUID}/delete")

    public RelationshipResponse deleteRelationship(@PathVariable String    userId,
                                                   @RequestParam String    typeDefGUID,
                                                   @RequestParam String    typeDefName,
                                                   @PathVariable String    obsoleteRelationshipGUID)
    {
        return restAPI.deleteRelationship(userId, typeDefGUID, typeDefName, obsoleteRelationshipGUID);
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to purge.
     * @param typeDefName unique name of the type of the relationship to purge.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * RelationshipNotDeletedException the requested relationship is not in DELETED status or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{deletedRelationshipGUID}/purge")

    public VoidResponse purgeRelationship(@PathVariable String    userId,
                                          @RequestParam String    typeDefGUID,
                                          @RequestParam String    typeDefName,
                                          @PathVariable String    deletedRelationshipGUID)
    {
        return restAPI.purgeRelationship(userId, typeDefGUID, typeDefName, deletedRelationshipGUID);
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{deletedRelationshipGUID}/restore")

    public RelationshipResponse restoreRelationship(@PathVariable String    userId,
                                                    @PathVariable String    deletedRelationshipGUID)
    {
        return restAPI.restoreRelationship(userId, deletedRelationshipGUID);
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the entity used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity used to verify the entity identity.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID new unique identifier for the entity.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new guid or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-identification or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/identity")

    public EntityDetailResponse reIdentifyEntity(@PathVariable String     userId,
                                                 @RequestParam String     typeDefGUID,
                                                 @RequestParam String     typeDefName,
                                                 @PathVariable String     entityGUID,
                                                 @RequestParam String     newEntityGUID)
    {
        return restAPI.reIdentifyEntity(userId, typeDefGUID, typeDefName, entityGUID, newEntityGUID);
    }


    /**
     * Change the type of an existing entity.  Typically this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/type")

    public EntityDetailResponse reTypeEntity(@PathVariable String         userId,
                                             @PathVariable String         entityGUID,
                                             @RequestParam TypeDefSummary currentTypeDefSummary,
                                             @RequestParam TypeDefSummary newTypeDefSummary)
    {
        return restAPI.reTypeEntity(userId, entityGUID, currentTypeDefSummary, newTypeDefSummary);
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param typeDefGUID the guid of the TypeDef for the entity used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity used to verify the entity identity.
     * @param homeMetadataCollectionId the existing identifier for this entity's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new home information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-homing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entity/{entityGUID}/home/{homeMetadataCollectionId}")

    public EntityDetailResponse reHomeEntity(@PathVariable String         userId,
                                             @PathVariable String         entityGUID,
                                             @RequestParam String         typeDefGUID,
                                             @RequestParam String         typeDefName,
                                             @PathVariable String         homeMetadataCollectionId,
                                             @RequestParam String         newHomeMetadataCollectionId)
    {
        return restAPI.reHomeEntity(userId,
                                    entityGUID,
                                    typeDefGUID,
                                    typeDefName,
                                    homeMetadataCollectionId,
                                    newHomeMetadataCollectionId);
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param relationshipGUID the existing identifier for the relationship.
     * @param newRelationshipGUID  the new unique identifier for the relationship.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{relationshipGUID}/identity")

    public RelationshipResponse reIdentifyRelationship(@PathVariable String     userId,
                                                       @RequestParam String     typeDefGUID,
                                                       @RequestParam String     typeDefName,
                                                       @PathVariable String     relationshipGUID,
                                                       @RequestParam String     newRelationshipGUID)
    {
        return restAPI.reIdentifyRelationship(userId, typeDefGUID, typeDefName, relationshipGUID, newRelationshipGUID);
    }


    /**
     * Change the type of an existing relationship.  Typically this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship used to verify the relationship identity.
     * @param newTypeDefSummary details of this relationship's new TypeDef.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{relationshipGUID}/type")

    public RelationshipResponse reTypeRelationship(@PathVariable String         userId,
                                                   @PathVariable String         relationshipGUID,
                                                   @RequestParam TypeDefSummary currentTypeDefSummary,
                                                   @RequestParam TypeDefSummary newTypeDefSummary)
    {
        return restAPI.reTypeRelationship(userId, relationshipGUID, currentTypeDefSummary, newTypeDefSummary);
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationship/{relationshipGUID}/home")

    public RelationshipResponse reHomeRelationship(@PathVariable String   userId,
                                                   @PathVariable String   relationshipGUID,
                                                   @RequestParam String   typeDefGUID,
                                                   @RequestParam String   typeDefName,
                                                   @RequestParam String   homeMetadataCollectionId,
                                                   @RequestParam String   newHomeMetadataCollectionId)
    {
        return restAPI.reHomeRelationship(userId,
                                          relationshipGUID,
                                          typeDefGUID,
                                          typeDefName,
                                          homeMetadataCollectionId,
                                          newHomeMetadataCollectionId);
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entities/reference-copy")

    public VoidResponse saveEntityReferenceCopy(@PathVariable String       userId,
                                                @RequestParam EntityDetail entity)
    {
        return restAPI.saveEntityReferenceCopy(userId, entity);
    }


    /**
     * Remove a reference copy of the the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID the unique identifier for the entity.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entities/reference-copy/{entityGUID}/purge")

    public VoidResponse purgeEntityReferenceCopy(@PathVariable String   userId,
                                                 @PathVariable String   entityGUID,
                                                 @RequestParam String   typeDefGUID,
                                                 @RequestParam String   typeDefName,
                                                 @RequestParam String   homeMetadataCollectionId)
    {
        return restAPI.purgeEntityReferenceCopy(userId, entityGUID, typeDefGUID, typeDefName, homeMetadataCollectionId);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity.
     * @param typeDefGUID unique identifier of requested entity's TypeDef.
     * @param typeDefName unique name of requested entity's TypeDef.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/entities/reference-copy/{entityGUID}/refresh")

    public VoidResponse refreshEntityReferenceCopy(@PathVariable String   userId,
                                                   @PathVariable String   entityGUID,
                                                   @RequestParam String   typeDefGUID,
                                                   @RequestParam String   typeDefName,
                                                   @RequestParam String   homeMetadataCollectionId)
    {
        return restAPI.refreshEntityReferenceCopy(userId,
                                                  entityGUID,
                                                  typeDefGUID,
                                                  typeDefName,
                                                  homeMetadataCollectionId);
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationships/reference-copy")

    public VoidResponse saveRelationshipReferenceCopy(@PathVariable String         userId,
                                                      @RequestParam Relationship   relationship)
    {
        return restAPI.saveRelationshipReferenceCopy(userId, relationship);
    }




    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationships/reference-copy/{relationshipGUID}/purge")

    public VoidResponse purgeRelationshipReferenceCopy(@PathVariable String   userId,
                                                       @PathVariable String   relationshipGUID,
                                                       @RequestParam String   typeDefGUID,
                                                       @RequestParam String   typeDefName,
                                                       @RequestParam String   homeMetadataCollectionId)
    {
        return restAPI.purgeRelationshipReferenceCopy(userId,
                                                      relationshipGUID,
                                                      typeDefGUID,
                                                      typeDefName,
                                                      homeMetadataCollectionId);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID unique identifier of the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
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
    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{userId}/instances/relationships/reference-copy/{relationshipGUID}/refresh")

    public VoidResponse refreshRelationshipReferenceCopy(@PathVariable String userId,
                                                         @PathVariable String relationshipGUID,
                                                         @RequestParam String typeDefGUID,
                                                         @RequestParam String typeDefName,
                                                         @RequestParam String homeMetadataCollectionId)
    {
        return restAPI.refreshRelationshipReferenceCopy(userId,
                                                        relationshipGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        homeMetadataCollectionId);
    }
}
