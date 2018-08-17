/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;


import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * OMRSRepositoryRESTServices provides the server-side support for the OMRS Repository REST Services API.
 * It is a minimal wrapper around the OMRSRepositoryConnector for the local server's metadata collection.
 * If localRepositoryConnector is null when a REST call is received, the request is rejected.
 *
 * It is itself wrapped by classes that provide REST annotations: OMRSRepositoryResource from the omag-server module
 * uses spring boot annotations; OMRepositoryServicesREST in the web-app module provides the Jersey/spring framework
 * annotations used in the Atlas server.
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
public class OMRSRepositoryRESTServices
{
    private static OMRSMetadataCollection       localMetadataCollection  = null;
    private static String                       localServerURL           = null;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param localRepositoryConnector link to the local repository responsible for servicing the REST calls.
     *                                 If localRepositoryConnector is null when a REST calls is received, the request
     *                                 is rejected.
     * @param localServerURL URL of the local server
     */
    public static void setLocalRepository(LocalOMRSRepositoryConnector    localRepositoryConnector,
                                          String                          localServerURL)
    {
        try
        {
            OMRSRepositoryRESTServices.localMetadataCollection = localRepositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            OMRSRepositoryRESTServices.localMetadataCollection = null;
        }

        OMRSRepositoryRESTServices.localServerURL = localServerURL;
    }


    /**
     * Return the URL for the requested instance.
     *
     * @param guid unique identifier of the instance
     * @return url
     */
    public static String  getEntityURL(String...   guid)
    {
        final String   urlTemplate = "/instances/entity/{0}";

        MessageFormat mf = new MessageFormat(urlTemplate);

        return localServerURL + mf.format(guid);
    }


    /**
     * Return the URL for the requested instance.
     *
     * @param guid unique identifier of the instance
     * @return url
     */
    public static String  getRelationshipURL(String...   guid)
    {
        final String   urlTemplate = "/instances/relationship/{0}";

        MessageFormat mf = new MessageFormat(urlTemplate);

        return localServerURL + mf.format(guid);
    }


    /**
     * Default constructor
     */
    public OMRSRepositoryRESTServices()
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
     * or RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    public MetadataCollectionIdResponse getMetadataCollectionId()
    {
        final  String   methodName = "getMetadataCollectionId";

        MetadataCollectionIdResponse response = new MetadataCollectionIdResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setMetadataCollectionId(localMetadataCollection.getMetadataCollectionId());
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }

        return response;
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
    public TypeDefGalleryResponse getAllTypes(String   userId)
    {
        final  String   methodName = "getAllTypes";

        TypeDefGalleryResponse  response = new TypeDefGalleryResponse();

        try
        {
            validateLocalRepository(methodName);

            TypeDefGallery typeDefGallery = localMetadataCollection.getAllTypes(userId);
            if (typeDefGallery != null)
            {
                response.setAttributeTypeDefs(typeDefGallery.getAttributeTypeDefs());
                response.setTypeDefs(typeDefGallery.getTypeDefs());
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        return response;
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
    public TypeDefGalleryResponse findTypesByName(String userId,
                                                  String name)
    {
        final  String   methodName = "findTypesByName";

        TypeDefGalleryResponse  response = new TypeDefGalleryResponse();

        try
        {
            validateLocalRepository(methodName);

            TypeDefGallery typeDefGallery = localMetadataCollection.findTypesByName(userId, name);
            if (typeDefGallery != null)
            {
                response.setAttributeTypeDefs(typeDefGallery.getAttributeTypeDefs());
                response.setTypeDefs(typeDefGallery.getTypeDefs());
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
    }


    /**
     * Returns all of the TypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category find parameters used to limit the returned results.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse findTypeDefsByCategory(String                     userId,
                                                      TypeDefCategory            category)
    {
        final  String   methodName = "findTypeDefsByCategory";

        TypeDefListResponse response = new TypeDefListResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDefs(localMetadataCollection.findTypeDefsByCategory(userId, category));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
    }


    /**
     * Returns all of the AttributeTypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category find parameters used to limit the returned results.
     * @return AttributeTypeDefListResponse:
     * AttributeTypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public AttributeTypeDefListResponse findAttributeTypeDefsByCategory(String                   userId,
                                                                        AttributeTypeDefCategory category)
    {
        final  String   methodName = "findAttributeTypeDefsByCategory";

        AttributeTypeDefListResponse response = new AttributeTypeDefListResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setAttributeTypeDefs(localMetadataCollection.findAttributeTypeDefsByCategory(userId, category));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties a list of property names.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the matchCriteria is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse findTypeDefsByProperty(String            userId,
                                                      TypeDefProperties matchCriteria)
    {
        final  String   methodName = "findTypeDefsByProperty";

        TypeDefListResponse response = new TypeDefListResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDefs(localMetadataCollection.findTypeDefsByProperty(userId, matchCriteria));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
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
    public TypeDefListResponse findTypesByExternalID(String    userId,
                                                     String    standard,
                                                     String    organization,
                                                     String    identifier)
    {
        final  String   methodName = "findTypesByExternalID";

        TypeDefListResponse  response = new TypeDefListResponse();

        try
        {
            validateLocalRepository(methodName);

            List<TypeDef> typeDefs = localMetadataCollection.findTypesByExternalID(userId,
                                                                                   standard,
                                                                                   organization,
                                                                                   identifier);
            response.setTypeDefs(typeDefs);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
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
    public TypeDefListResponse searchForTypeDefs(String userId,
                                                 String searchCriteria)
    {
        final  String   methodName = "searchForTypeDefs";

        TypeDefListResponse response = new TypeDefListResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDefs(localMetadataCollection.searchForTypeDefs(userId, searchCriteria));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
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
    public TypeDefResponse getTypeDefByGUID(String    userId,
                                            String    guid)
    {
        final  String   methodName = "getTypeDefByGUID";

        TypeDefResponse response = new TypeDefResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDef(localMetadataCollection.getTypeDefByGUID(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnownException(response, error);
        }

        return response;
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
    public AttributeTypeDefResponse getAttributeTypeDefByGUID(String    userId,
                                                              String    guid)
    {
        final  String   methodName = "getAttributeTypeDefByGUID";

        AttributeTypeDefResponse response = new AttributeTypeDefResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setAttributeTypeDef(localMetadataCollection.getAttributeTypeDefByGUID(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnown(response, error);
        }

        return response;
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
    public TypeDefResponse getTypeDefByName(String    userId,
                                            String    name)
    {
        final  String   methodName = "getTypeDefByName";

        TypeDefResponse response = new TypeDefResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDef(localMetadataCollection.getTypeDefByName(userId, name));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnownException(response, error);
        }

        return response;
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
    public  AttributeTypeDefResponse getAttributeTypeDefByName(String    userId,
                                                               String    name)
    {
        final  String   methodName = "getAttributeTypeDefByName";

        AttributeTypeDefResponse response = new AttributeTypeDefResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setAttributeTypeDef(localMetadataCollection.getAttributeTypeDefByName(userId, name));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnown(response, error);
        }

        return response;
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
    public  VoidResponse addTypeDefGallery(String          userId,
                                           TypeDefGallery  newTypes)
    {
        final  String   methodName = "addTypeDefGallery";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.addTypeDefGallery(userId, newTypes);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotSupportedException error)
        {
            captureTypeDefNotSupportedException(response, error);
        }
        catch (TypeDefKnownException error)
        {
            captureTypeDefKnownException(response, error);
        }
        catch (TypeDefConflictException error)
        {
            captureTypeDefConflictException(response, error);
        }
        catch (InvalidTypeDefException error)
        {
            captureInvalidTypeDefException(response, error);
        }

        return response;
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
    public VoidResponse addTypeDef(String       userId,
                                   TypeDef      newTypeDef)
    {
        final  String   methodName = "addTypeDef";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.addTypeDef(userId, newTypeDef);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotSupportedException error)
        {
            captureTypeDefNotSupportedException(response, error);
        }
        catch (TypeDefKnownException error)
        {
            captureTypeDefKnownException(response, error);
        }
        catch (TypeDefConflictException error)
        {
            captureTypeDefConflictException(response, error);
        }
        catch (InvalidTypeDefException error)
        {
            captureInvalidTypeDefException(response, error);
        }

        return response;
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
    public  VoidResponse addAttributeTypeDef(String             userId,
                                             AttributeTypeDef   newAttributeTypeDef)
    {
        final  String   methodName = "addAttributeTypeDef";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.addAttributeTypeDef(userId, newAttributeTypeDef);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotSupportedException error)
        {
            captureTypeDefNotSupportedException(response, error);
        }
        catch (TypeDefKnownException error)
        {
            captureTypeDefKnownException(response, error);
        }
        catch (TypeDefConflictException error)
        {
            captureTypeDefConflictException(response, error);
        }
        catch (InvalidTypeDefException error)
        {
            captureInvalidTypeDefException(response, error);
        }

        return response;
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
    public BooleanResponse verifyTypeDef(String       userId,
                                         TypeDef      typeDef)
    {
        final  String   methodName = "verifyTypeDef";

        BooleanResponse response = new BooleanResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setFlag(localMetadataCollection.verifyTypeDef(userId, typeDef));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotSupportedException error)
        {
            captureTypeDefNotSupportedException(response, error);
        }
        catch (TypeDefConflictException error)
        {
            captureTypeDefConflictException(response, error);
        }
        catch (InvalidTypeDefException error)
        {
            captureInvalidTypeDefException(response, error);
        }

        return response;
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
    public  BooleanResponse verifyAttributeTypeDef(String            userId,
                                                   AttributeTypeDef  attributeTypeDef)
    {
        final  String   methodName = "verifyAttributeTypeDef";

        BooleanResponse response = new BooleanResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setFlag(localMetadataCollection.verifyAttributeTypeDef(userId, attributeTypeDef));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotSupportedException error)
        {
            captureTypeDefNotSupportedException(response, error);
        }
        catch (TypeDefConflictException error)
        {
            captureTypeDefConflictException(response, error);
        }
        catch (InvalidTypeDefException error)
        {
            captureInvalidTypeDefException(response, error);
        }

        return response;
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
    public TypeDefResponse updateTypeDef(String       userId,
                                         TypeDefPatch typeDefPatch)
    {
        final  String   methodName = "updateTypeDef";

        TypeDefResponse response = new TypeDefResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDef(localMetadataCollection.updateTypeDef(userId, typeDefPatch));
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnownException(response, error);
        }
        catch (PatchErrorException error)
        {
            response.setRelatedHTTPCode(error.getReportedHTTPCode());
            response.setExceptionClassName(PatchErrorException.class.getName());
            response.setExceptionErrorMessage(error.getErrorMessage());
            response.setExceptionSystemAction(error.getReportedSystemAction());
            response.setExceptionUserAction(error.getReportedUserAction());
        }

        return response;
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
    public VoidResponse deleteTypeDef(String    userId,
                                      String    obsoleteTypeDefGUID,
                                      String    obsoleteTypeDefName)
    {
        final  String   methodName = "deleteTypeDef";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.deleteTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnownException(response, error);
        }
        catch (TypeDefInUseException error)
        {
            captureTypeDefInUseException(response, error);
        }

        return response;
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
    public VoidResponse deleteAttributeTypeDef(String    userId,
                                               String    obsoleteTypeDefGUID,
                                               String    obsoleteTypeDefName)
    {
        final  String   methodName = "deleteAttributeTypeDef";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.deleteAttributeTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnownException(response, error);
        }
        catch (TypeDefInUseException error)
        {
            captureTypeDefInUseException(response, error);
        }

        return response;
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
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
    public  TypeDefResponse reIdentifyTypeDef(String                    userId,
                                              String                    originalTypeDefGUID,
                                              TypeDefReIdentifyRequest  requestParameters)
    {
        final  String   methodName = "reIdentifyTypeDef";

        String originalTypeDefName = null;
        String newTypeDefGUID = null;
        String newTypeDefName = null;

        TypeDefResponse response = new TypeDefResponse();

        if (requestParameters != null)
        {
            originalTypeDefName = requestParameters.getOriginalTypeDefName();
            newTypeDefGUID = requestParameters.getNewTypeDefGUID();
            newTypeDefName = requestParameters.getNewTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setTypeDef(localMetadataCollection.reIdentifyTypeDef(userId,
                                                                          originalTypeDefGUID,
                                                                          originalTypeDefName,
                                                                          newTypeDefGUID,
                                                                          newTypeDefName));
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID the original guid of the AttributeTypeDef.
     * @param requestParameters the original name of the AttributeTypeDef and the new identifier for the AttributeTypeDef
     *                          and the new name for this AttributeTypeDef.
     * @return AttributeTypeDefResponse:
     * attributeTypeDef: new values for this AttributeTypeDef, including the new guid/name or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the AttributeTypeDef identified by the original guid/name is not
     *                                    found in the metadata collection or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  AttributeTypeDefResponse reIdentifyAttributeTypeDef(String                   userId,
                                                                String                   originalAttributeTypeDefGUID,
                                                                TypeDefReIdentifyRequest requestParameters)
    {
        final  String   methodName = "reIdentifyAttributeTypeDef";

        String originalAttributeTypeDefName = null;
        String newAttributeTypeDefGUID = null;
        String newAttributeTypeDefName = null;

        AttributeTypeDefResponse response = new AttributeTypeDefResponse();

        if (requestParameters != null)
        {
            originalAttributeTypeDefName = requestParameters.getOriginalTypeDefName();
            newAttributeTypeDefGUID = requestParameters.getNewTypeDefGUID();
            newAttributeTypeDefName = requestParameters.getNewTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setAttributeTypeDef(localMetadataCollection.reIdentifyAttributeTypeDef(userId,
                                                                                            originalAttributeTypeDefGUID,
                                                                                            originalAttributeTypeDefName,
                                                                                            newAttributeTypeDefGUID,
                                                                                            newAttributeTypeDefName));
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeDefNotKnownException error)
        {
            captureTypeDefNotKnown(response, error);
        }

        return response;
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
    public EntityDetailResponse isEntityKnown(String     userId,
                                              String     guid)
    {
        final  String   methodName = "isEntityKnown";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.isEntityKnown(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
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
    public EntitySummaryResponse getEntitySummary(String     userId,
                                                  String     guid)
    {
        final  String   methodName = "getEntitySummary";

        EntitySummaryResponse response = new EntitySummaryResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.getEntitySummary(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
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
    public EntityDetailResponse getEntityDetail(String     guid)
    {
        return this.getEntityDetail(null, guid);
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
    public EntityDetailResponse getEntityDetail(String     userId,
                                                String     guid)
    {
        final  String   methodName = "getEntityDetail";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.getEntityDetail(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (EntityProxyOnlyException error)
        {
            captureEntityProxyOnlyException(response, error);
        }

        return response;
    }


    /**
     * Return a historical version of an entity.  This includes the header, classifications and properties of the entity.
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
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityDetailResponse getEntityDetail(String     userId,
                                                 String     guid,
                                                 Date       asOfTime)
    {
        final  String   methodName = "getEntityDetail";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.getEntityDetail(userId, guid, asOfTime));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (EntityProxyOnlyException error)
        {
            captureEntityProxyOnlyException(response, error);
        }

        return response;
    }



    /**
     * Return the relationships for a specific entity.
     *
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
    public RelationshipListResponse getRelationshipsForEntity(String                     userId,
                                                              String                     entityGUID,
                                                              TypeLimitedFindRequest     findRequestParameters)
    {
        final  String   methodName = "getRelationshipsForEntity";

        String               relationshipTypeGUID    = null;
        int                  fromRelationshipElement = 0;
        List<InstanceStatus> limitResultsByStatus    = null;
        String               sequencingProperty      = null;
        SequencingOrder      sequencingOrder         = null;
        int                  pageSize                = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (findRequestParameters != null)
        {
            relationshipTypeGUID    = findRequestParameters.getTypeGUID();
            fromRelationshipElement = findRequestParameters.getOffset();
            limitResultsByStatus    = findRequestParameters.getLimitResultsByStatus();
            sequencingProperty      = findRequestParameters.getSequencingProperty();
            sequencingOrder         = findRequestParameters.getSequencingOrder();
            pageSize                = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<Relationship> relationships = localMetadataCollection.getRelationshipsForEntity(userId,
                                                                                                 entityGUID,
                                                                                                 relationshipTypeGUID,
                                                                                                 fromRelationshipElement,
                                                                                                 limitResultsByStatus,
                                                                                                 null,
                                                                                                 sequencingProperty,
                                                                                                 sequencingOrder,
                                                                                                 pageSize);
            response.setRelationships(relationships);
            if (relationships != null)
            {
                response.setOffset(fromRelationshipElement);
                response.setPageSize(pageSize);
                if (response.getRelationships().size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entity/{1}/relationships";

                    TypeLimitedFindRequest nextFindRequestParameters = new TypeLimitedFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromRelationshipElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              entityGUID));
                }
            }

        }
        catch (RepositoryErrorException error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return the relationships for a specific entity.
     *
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
    public RelationshipListResponse getRelationshipsForEntityHistory(String                               userId,
                                                                     String                               entityGUID,
                                                                     TypeLimitedHistoricalFindRequest     findRequestParameters)
    {
        final  String   methodName = "getRelationshipsForEntityHistory";

        String               relationshipTypeGUID    = null;
        int                  fromRelationshipElement = 0;
        List<InstanceStatus> limitResultsByStatus    = null;
        Date                 asOfTime                = null;
        String               sequencingProperty      = null;
        SequencingOrder      sequencingOrder         = null;
        int                  pageSize                = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (findRequestParameters != null)
        {
            relationshipTypeGUID    = findRequestParameters.getTypeGUID();
            fromRelationshipElement = findRequestParameters.getOffset();
            limitResultsByStatus    = findRequestParameters.getLimitResultsByStatus();
            asOfTime                = findRequestParameters.getAsOfTime();
            sequencingProperty      = findRequestParameters.getSequencingProperty();
            sequencingOrder         = findRequestParameters.getSequencingOrder();
            pageSize                = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<Relationship> relationships = localMetadataCollection.getRelationshipsForEntity(userId,
                                                                                                 entityGUID,
                                                                                                 relationshipTypeGUID,
                                                                                                 fromRelationshipElement,
                                                                                                 limitResultsByStatus,
                                                                                                 asOfTime,
                                                                                                 sequencingProperty,
                                                                                                 sequencingOrder,
                                                                                                 pageSize);
            response.setRelationships(relationships);
            if (relationships != null)
            {
                response.setOffset(fromRelationshipElement);
                response.setPageSize(pageSize);
                if (response.getRelationships().size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entity/{1}/relationships/history";

                    TypeLimitedHistoricalFindRequest nextFindRequestParameters = new TypeLimitedHistoricalFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromRelationshipElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              entityGUID));
                }
            }

        }
        catch (RepositoryErrorException error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
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
    public  EntityListResponse findEntitiesByProperty(String                    userId,
                                                      EntityPropertyFindRequest findRequestParameters)
    {
        final  String   methodName = "findEntitiesByProperty";

        String                    entityTypeGUID                    = null;
        InstanceProperties        matchProperties                   = null;
        MatchCriteria             matchCriteria                     = null;
        int                       fromEntityElement                 = 0;
        List<InstanceStatus>      limitResultsByStatus              = null;
        List<String>              limitResultsByClassification      = null;
        String                    sequencingProperty                = null;
        SequencingOrder           sequencingOrder                   = null;
        int                       pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUID                    = findRequestParameters.getTypeGUID();
            matchProperties                   = findRequestParameters.getMatchProperties();
            matchCriteria                     = findRequestParameters.getMatchCriteria();
            fromEntityElement                 = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            limitResultsByClassification      = findRequestParameters.getLimitResultsByClassification();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.findEntitiesByProperty(userId,
                                                                                          entityTypeGUID,
                                                                                          matchProperties,
                                                                                          matchCriteria,
                                                                                          fromEntityElement,
                                                                                          limitResultsByStatus,
                                                                                          limitResultsByClassification,
                                                                                          null,
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/by-property";

                    EntityPropertyFindRequest nextFindRequestParameters = new EntityPropertyFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
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
    public  EntityListResponse findEntitiesByPropertyHistory(String                              userId,
                                                             EntityPropertyHistoricalFindRequest findRequestParameters)
    {
        final  String   methodName = "findEntitiesByPropertyHistory";

        String                    entityTypeGUID                    = null;
        InstanceProperties        matchProperties                   = null;
        MatchCriteria             matchCriteria                     = null;
        int                       fromEntityElement                 = 0;
        List<InstanceStatus>      limitResultsByStatus              = null;
        List<String>              limitResultsByClassification      = null;
        Date                      asOfTime                          = null;
        String                    sequencingProperty                = null;
        SequencingOrder           sequencingOrder                   = null;
        int                       pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUID                    = findRequestParameters.getTypeGUID();
            matchProperties                   = findRequestParameters.getMatchProperties();
            matchCriteria                     = findRequestParameters.getMatchCriteria();
            fromEntityElement                 = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            limitResultsByClassification      = findRequestParameters.getLimitResultsByClassification();
            asOfTime                          = findRequestParameters.getAsOfTime();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.findEntitiesByProperty(userId,
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
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/by-property";

                    EntityPropertyFindRequest nextFindRequestParameters = new EntityPropertyFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param userId unique identifier for requesting user.
     * @param classificationName name of the classification a null is not valid.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
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
    public  EntityListResponse findEntitiesByClassification(String                    userId,
                                                            String                    classificationName,
                                                            PropertyMatchFindRequest  findRequestParameters)
    {
        final  String   methodName = "findEntitiesByClassification";

        String                    entityTypeGUID                  = null;
        InstanceProperties        matchClassificationProperties   = null;
        MatchCriteria             matchCriteria                   = null;
        int                       fromEntityElement               = 0;
        List<InstanceStatus>      limitResultsByStatus            = null;
        String                    sequencingProperty              = null;
        SequencingOrder           sequencingOrder                 = null;
        int                       pageSize                        = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUID                    = findRequestParameters.getTypeGUID();
            matchClassificationProperties     = findRequestParameters.getMatchProperties();
            matchCriteria                     = findRequestParameters.getMatchCriteria();
            fromEntityElement                 = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.findEntitiesByClassification(userId,
                                                                                                entityTypeGUID,
                                                                                                classificationName,
                                                                                                matchClassificationProperties,
                                                                                                matchCriteria,
                                                                                                fromEntityElement,
                                                                                                limitResultsByStatus,
                                                                                                null,
                                                                                                sequencingProperty,
                                                                                                sequencingOrder,
                                                                                                pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/by-classification/{1}";

                    PropertyMatchFindRequest nextFindRequestParameters = new PropertyMatchFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);
                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              classificationName));
                }
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param userId unique identifier for requesting user.
     * @param classificationName name of the classification a null is not valid.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
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
    public  EntityListResponse findEntitiesByClassificationHistory(String                              userId,
                                                                   String                              classificationName,
                                                                   PropertyMatchHistoricalFindRequest  findRequestParameters)
    {
        final  String   methodName = "findEntitiesByClassificationHistory";

        String                    entityTypeGUID                  = null;
        InstanceProperties        matchClassificationProperties   = null;
        MatchCriteria             matchCriteria                   = null;
        int                       fromEntityElement               = 0;
        List<InstanceStatus>      limitResultsByStatus            = null;
        Date                      asOfTime                        = null;
        String                    sequencingProperty              = null;
        SequencingOrder           sequencingOrder                 = null;
        int                       pageSize                        = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUID                    = findRequestParameters.getTypeGUID();
            matchClassificationProperties     = findRequestParameters.getMatchProperties();
            matchCriteria                     = findRequestParameters.getMatchCriteria();
            fromEntityElement                 = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            asOfTime                          = findRequestParameters.getAsOfTime();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.findEntitiesByClassification(userId,
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
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/by-classification/{1}/history";

                    PropertyMatchHistoricalFindRequest nextFindRequestParameters = new PropertyMatchHistoricalFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);
                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              classificationName));
                }
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }

        return response;
    }

    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
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
    public  EntityListResponse findEntitiesByPropertyValue(String                    userId,
                                                           String                    searchCriteria,
                                                           EntityPropertyFindRequest findRequestParameters)
    {
        final  String   methodName = "findEntitiesByPropertyValue";

        String                  entityTypeGUID                    = null;
        int                     fromEntityElement                 = 0;
        List<InstanceStatus>    limitResultsByStatus              = null;
        List<String>            limitResultsByClassification      = null;
        String                  sequencingProperty                = null;
        SequencingOrder         sequencingOrder                   = null;
        int                     pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUID                    = findRequestParameters.getTypeGUID();
            fromEntityElement                 = findRequestParameters.getOffset();
            limitResultsByClassification      = findRequestParameters.getLimitResultsByClassification();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.findEntitiesByPropertyValue(userId,
                                                                                               entityTypeGUID,
                                                                                               searchCriteria,
                                                                                               fromEntityElement,
                                                                                               limitResultsByStatus,
                                                                                               limitResultsByClassification,
                                                                                               null,
                                                                                               sequencingProperty,
                                                                                               sequencingOrder,
                                                                                               pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/by-property-value?&searchCriteria={1}";
                    EntityPropertyFindRequest nextFindRequestParameters = new EntityPropertyFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              searchCriteria));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String expression of the characteristics of the required relationships.
     * @param findRequestParameters find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
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
    public  EntityListResponse findEntitiesByPropertyValueHistory(String                              userId,
                                                                  String                              searchCriteria,
                                                                  EntityPropertyHistoricalFindRequest findRequestParameters)
    {
        final  String   methodName = "findEntitiesByPropertyValueHistory";

        String                  entityTypeGUID                    = null;
        int                     fromEntityElement                 = 0;
        List<InstanceStatus>    limitResultsByStatus              = null;
        List<String>            limitResultsByClassification      = null;
        Date                    asOfTime                          = null;
        String                  sequencingProperty                = null;
        SequencingOrder         sequencingOrder                   = null;
        int                     pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUID                    = findRequestParameters.getTypeGUID();
            fromEntityElement                 = findRequestParameters.getOffset();
            limitResultsByClassification      = findRequestParameters.getLimitResultsByClassification();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            asOfTime                          = findRequestParameters.getAsOfTime();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.findEntitiesByPropertyValue(userId,
                                                                                               entityTypeGUID,
                                                                                               searchCriteria,
                                                                                               fromEntityElement,
                                                                                               limitResultsByStatus,
                                                                                               limitResultsByClassification,
                                                                                               asOfTime,
                                                                                               sequencingProperty,
                                                                                               sequencingOrder,
                                                                                               pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/by-property-value?&searchCriteria={1}/history";
                    EntityPropertyHistoricalFindRequest nextFindRequestParameters = new EntityPropertyHistoricalFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              searchCriteria));
                }
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
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
    public RelationshipResponse  isRelationshipKnown(String     userId,
                                                     String     guid)
    {
        final  String   methodName = "isRelationshipKnown";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.isRelationshipKnown(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
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
    public RelationshipResponse getRelationship(String    guid)
    {
        return this.getRelationship(null, guid);
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
    public RelationshipResponse getRelationship(String    userId,
                                                String    guid)
    {
        final  String   methodName = "getRelationship";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.getRelationship(userId, guid));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
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
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipResponse getRelationship(String    userId,
                                                 String    guid,
                                                 Date      asOfTime)
    {
        final  String   methodName = "getRelationship";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.getRelationship(userId, guid, asOfTime));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
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
    public  RelationshipListResponse findRelationshipsByProperty(String                    userId,
                                                                 PropertyMatchFindRequest  findRequestParameters)
    {
        final  String   methodName = "findRelationshipsByProperty";

        String                    relationshipTypeGUID     = null;
        InstanceProperties        matchProperties          = null;
        MatchCriteria             matchCriteria            = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (findRequestParameters != null)
        {
            relationshipTypeGUID              = findRequestParameters.getTypeGUID();
            matchProperties                   = findRequestParameters.getMatchProperties();
            matchCriteria                     = findRequestParameters.getMatchCriteria();
            fromRelationshipElement           = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<Relationship>  relationships = localMetadataCollection.findRelationshipsByProperty(userId,
                                                                                                    relationshipTypeGUID,
                                                                                                    matchProperties,
                                                                                                    matchCriteria,
                                                                                                    fromRelationshipElement,
                                                                                                    limitResultsByStatus,
                                                                                                    null,
                                                                                                    sequencingProperty,
                                                                                                    sequencingOrder,
                                                                                                    pageSize);
            response.setRelationships(relationships);
            if (relationships != null)
            {
                response.setOffset(fromRelationshipElement);
                response.setPageSize(pageSize);
                if (response.getRelationships().size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/relationships/by-property";

                    PropertyMatchFindRequest nextFindRequestParameters = new PropertyMatchFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromRelationshipElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
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
    public  RelationshipListResponse findRelationshipsByPropertyHistory(String                              userId,
                                                                        PropertyMatchHistoricalFindRequest  findRequestParameters)
    {
        final  String   methodName = "findRelationshipsByPropertyHistory";

        String                    relationshipTypeGUID     = null;
        InstanceProperties        matchProperties          = null;
        MatchCriteria             matchCriteria            = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        Date                      asOfTime                 = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (findRequestParameters != null)
        {
            relationshipTypeGUID              = findRequestParameters.getTypeGUID();
            matchProperties                   = findRequestParameters.getMatchProperties();
            matchCriteria                     = findRequestParameters.getMatchCriteria();
            fromRelationshipElement           = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            asOfTime                          = findRequestParameters.getAsOfTime();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<Relationship>  relationships = localMetadataCollection.findRelationshipsByProperty(userId,
                                                                                                    relationshipTypeGUID,
                                                                                                    matchProperties,
                                                                                                    matchCriteria,
                                                                                                    fromRelationshipElement,
                                                                                                    limitResultsByStatus,
                                                                                                    asOfTime,
                                                                                                    sequencingProperty,
                                                                                                    sequencingOrder,
                                                                                                    pageSize);
            response.setRelationships(relationships);
            if (relationships != null)
            {
                response.setOffset(fromRelationshipElement);
                response.setPageSize(pageSize);
                if (response.getRelationships().size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/relationships/by-property/history";

                    PropertyMatchHistoricalFindRequest nextFindRequestParameters = new PropertyMatchHistoricalFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromRelationshipElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
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
    public  RelationshipListResponse findRelationshipsByPropertyValue(String                    userId,
                                                                      String                    searchCriteria,
                                                                      TypeLimitedFindRequest    findRequestParameters)
    {
        final  String   methodName = "findRelationshipsByPropertyValue";

        String                    relationshipTypeGUID     = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (findRequestParameters != null)
        {
            relationshipTypeGUID              = findRequestParameters.getTypeGUID();
            fromRelationshipElement           = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<Relationship>  relationships = localMetadataCollection.findRelationshipsByPropertyValue(userId,
                                                                                                         relationshipTypeGUID,
                                                                                                         searchCriteria,
                                                                                                         fromRelationshipElement,
                                                                                                         limitResultsByStatus,
                                                                                                         null,
                                                                                                         sequencingProperty,
                                                                                                         sequencingOrder,
                                                                                                         pageSize);
            response.setRelationships(relationships);
            if (relationships != null)
            {
                response.setOffset(fromRelationshipElement);
                response.setPageSize(pageSize);
                if (response.getRelationships().size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/relationships/by-property-value?searchCriteria={1}";

                    TypeLimitedFindRequest nextFindRequestParameters = new TypeLimitedFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromRelationshipElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              searchCriteria));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
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
    public  RelationshipListResponse findRelationshipsByPropertyValueHistory(String                              userId,
                                                                             String                              searchCriteria,
                                                                             TypeLimitedHistoricalFindRequest    findRequestParameters)
    {
        final  String   methodName = "findRelationshipsByPropertyValueHistory";

        String                    relationshipTypeGUID     = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        Date                      asOfTime                 = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (findRequestParameters != null)
        {
            relationshipTypeGUID              = findRequestParameters.getTypeGUID();
            fromRelationshipElement           = findRequestParameters.getOffset();
            limitResultsByStatus              = findRequestParameters.getLimitResultsByStatus();
            asOfTime                          = findRequestParameters.getAsOfTime();
            sequencingProperty                = findRequestParameters.getSequencingProperty();
            sequencingOrder                   = findRequestParameters.getSequencingOrder();
            pageSize                          = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<Relationship>  relationships = localMetadataCollection.findRelationshipsByPropertyValue(userId,
                                                                                                         relationshipTypeGUID,
                                                                                                         searchCriteria,
                                                                                                         fromRelationshipElement,
                                                                                                         limitResultsByStatus,
                                                                                                         asOfTime,
                                                                                                         sequencingProperty,
                                                                                                         sequencingOrder,
                                                                                                         pageSize);
            response.setRelationships(relationships);
            if (relationships != null)
            {
                response.setOffset(fromRelationshipElement);
                response.setPageSize(pageSize);
                if (response.getRelationships().size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/relationships/by-property-value/history?searchCriteria={1}";

                    TypeLimitedHistoricalFindRequest nextFindRequestParameters = new TypeLimitedHistoricalFindRequest(findRequestParameters);
                    nextFindRequestParameters.setOffset(fromRelationshipElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              searchCriteria));
                }
            }

        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }

        return response;
    }


    /**
     * Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
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
    public  InstanceGraphResponse getLinkingEntities(String                    userId,
                                                     String                    startEntityGUID,
                                                     String                    endEntityGUID,
                                                     OMRSAPIFindRequest        findRequestParameters)
    {
        final  String   methodName = "getLinkingEntities";

        List<InstanceStatus>      limitResultsByStatus = null;

        InstanceGraphResponse response = new InstanceGraphResponse();

        if (findRequestParameters != null)
        {
            limitResultsByStatus = findRequestParameters.getLimitResultsByStatus();
        }

        try
        {
            validateLocalRepository(methodName);

            InstanceGraph instanceGraph = localMetadataCollection.getLinkingEntities(userId,
                                                                                     startEntityGUID,
                                                                                     endEntityGUID,
                                                                                     limitResultsByStatus,
                                                                                     null);
            if (instanceGraph != null)
            {
                response.setEntityElementList(instanceGraph.getEntities());
                response.setRelationshipElementList(instanceGraph.getRelationships());
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
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
    public  InstanceGraphResponse getLinkingEntitiesHistory(String                         userId,
                                                            String                         startEntityGUID,
                                                            String                         endEntityGUID,
                                                            OMRSAPIHistoricalFindRequest   findRequestParameters)
    {
        final  String   methodName = "getLinkingEntitiesHistory";

        List<InstanceStatus>      limitResultsByStatus = null;
        Date                      asOfTime = null;

        InstanceGraphResponse response = new InstanceGraphResponse();

        if (findRequestParameters != null)
        {
            limitResultsByStatus = findRequestParameters.getLimitResultsByStatus();
            asOfTime = findRequestParameters.getAsOfTime();
        }

        try
        {
            validateLocalRepository(methodName);

            InstanceGraph instanceGraph = localMetadataCollection.getLinkingEntities(userId,
                                                                                     startEntityGUID,
                                                                                     endEntityGUID,
                                                                                     limitResultsByStatus,
                                                                                     asOfTime);
            if (instanceGraph != null)
            {
                response.setEntityElementList(instanceGraph.getEntities());
                response.setRelationshipElementList(instanceGraph.getRelationships());
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param findRequestParameters find parameters used to limit the returned results.
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
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  InstanceGraphResponse getEntityNeighborhood(String                         userId,
                                                        String                         entityGUID,
                                                        int                            level,
                                                        EntityNeighborhoodFindRequest  findRequestParameters)
    {
        final  String   methodName = "getEntityNeighborhood";

        List<String>         entityTypeGUIDs                = null;
        List<String>         relationshipTypeGUIDs          = null;
        List<InstanceStatus> limitResultsByStatus           = null;
        List<String>         limitResultsByClassification   = null;

        InstanceGraphResponse response = new InstanceGraphResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUIDs = findRequestParameters.getEntityTypeGUIDs();
            relationshipTypeGUIDs = findRequestParameters.getRelationshipTypeGUIDs();
            limitResultsByStatus = findRequestParameters.getLimitResultsByStatus();
            limitResultsByClassification = findRequestParameters.getLimitResultsByClassification();
        }

        try
        {
            validateLocalRepository(methodName);

            InstanceGraph instanceGraph = localMetadataCollection.getEntityNeighborhood(userId,
                                                                                        entityGUID,
                                                                                        entityTypeGUIDs,
                                                                                        relationshipTypeGUIDs,
                                                                                        limitResultsByStatus,
                                                                                        limitResultsByClassification,
                                                                                        null,
                                                                                        level);
            if (instanceGraph != null)
            {
                response.setEntityElementList(instanceGraph.getEntities());
                response.setRelationshipElementList(instanceGraph.getRelationships());
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }

        return response;
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
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
    public  InstanceGraphResponse getEntityNeighborhoodHistory(String                                   userId,
                                                               String                                   entityGUID,
                                                               int                                      level,
                                                               EntityNeighborhoodHistoricalFindRequest  findRequestParameters)
    {
        final  String   methodName = "getEntityNeighborhoodHistory";

        List<String>         entityTypeGUIDs                = null;
        List<String>         relationshipTypeGUIDs          = null;
        List<InstanceStatus> limitResultsByStatus           = null;
        List<String>         limitResultsByClassification   = null;
        Date                 asOfTime                       = null;

        InstanceGraphResponse response = new InstanceGraphResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUIDs = findRequestParameters.getEntityTypeGUIDs();
            relationshipTypeGUIDs = findRequestParameters.getRelationshipTypeGUIDs();
            limitResultsByStatus = findRequestParameters.getLimitResultsByStatus();
            asOfTime = findRequestParameters.getAsOfTime();
            limitResultsByClassification = findRequestParameters.getLimitResultsByClassification();
        }

        try
        {
            validateLocalRepository(methodName);

            InstanceGraph instanceGraph = localMetadataCollection.getEntityNeighborhood(userId,
                                                                                        entityGUID,
                                                                                        entityTypeGUIDs,
                                                                                        relationshipTypeGUIDs,
                                                                                        limitResultsByStatus,
                                                                                        limitResultsByClassification,
                                                                                        asOfTime,
                                                                                        level);
            if (instanceGraph != null)
            {
                response.setEntityElementList(instanceGraph.getEntities());
                response.setRelationshipElementList(instanceGraph.getRelationships());
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }

        return response;
    }


    /**
     * Return the list of entities that are of the types listed in instanceTypes and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
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
    public  EntityListResponse getRelatedEntities(String                     userId,
                                                  String                     startEntityGUID,
                                                  RelatedEntitiesFindRequest findRequestParameters)
    {
        final  String   methodName = "getRelatedEntities";

        List<String>         entityTypeGUIDs               = null;
        int                  fromEntityElement             = 0;
        List<InstanceStatus> limitResultsByStatus          = null;
        List<String>         limitResultsByClassification  = null;
        String               sequencingProperty            = null;
        SequencingOrder      sequencingOrder               = null;
        int                  pageSize                      = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUIDs = findRequestParameters.getEntityTypeGUIDs();
            fromEntityElement = findRequestParameters.getOffset();
            limitResultsByStatus = findRequestParameters.getLimitResultsByStatus();
            limitResultsByClassification = findRequestParameters.getLimitResultsByClassification();
            sequencingProperty = findRequestParameters.getSequencingProperty();
            sequencingOrder = findRequestParameters.getSequencingOrder();
            pageSize = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.getRelatedEntities(userId,
                                                                                      startEntityGUID,
                                                                                      entityTypeGUIDs,
                                                                                      fromEntityElement,
                                                                                      limitResultsByStatus,
                                                                                      limitResultsByClassification,
                                                                                      null,
                                                                                      sequencingProperty,
                                                                                      sequencingOrder,
                                                                                      pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/from-entity/{1}/by-relationship";

                    RelatedEntitiesFindRequest  nextFindRequestParameters = new RelatedEntitiesFindRequest(findRequestParameters);

                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              startEntityGUID));
                }
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Return the list of entities that are of the types listed in instanceTypes and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
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
    public  EntityListResponse getRelatedEntitiesHistory(String                               userId,
                                                         String                               startEntityGUID,
                                                         RelatedEntitiesHistoricalFindRequest findRequestParameters)
    {
        final  String   methodName = "getRelatedEntitiesHistory";

        List<String>         entityTypeGUIDs               = null;
        int                  fromEntityElement             = 0;
        List<InstanceStatus> limitResultsByStatus          = null;
        List<String>         limitResultsByClassification  = null;
        Date                 asOfTime                      = null;
        String               sequencingProperty            = null;
        SequencingOrder      sequencingOrder               = null;
        int                  pageSize                      = 0;

        EntityListResponse response = new EntityListResponse();

        if (findRequestParameters != null)
        {
            entityTypeGUIDs = findRequestParameters.getEntityTypeGUIDs();
            fromEntityElement = findRequestParameters.getOffset();
            limitResultsByStatus = findRequestParameters.getLimitResultsByStatus();
            limitResultsByClassification = findRequestParameters.getLimitResultsByClassification();
            asOfTime = findRequestParameters.getAsOfTime();
            sequencingProperty = findRequestParameters.getSequencingProperty();
            sequencingOrder = findRequestParameters.getSequencingOrder();
            pageSize = findRequestParameters.getPageSize();
        }

        try
        {
            validateLocalRepository(methodName);

            List<EntityDetail>  entities = localMetadataCollection.getRelatedEntities(userId,
                                                                                      startEntityGUID,
                                                                                      entityTypeGUIDs,
                                                                                      fromEntityElement,
                                                                                      limitResultsByStatus,
                                                                                      limitResultsByClassification,
                                                                                      asOfTime,
                                                                                      sequencingProperty,
                                                                                      sequencingOrder,
                                                                                      pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
                if (entities.size() == pageSize)
                {
                    final String urlTemplate = "{0}/instances/entities/from-entity/{1}/by-relationship/history";

                    RelatedEntitiesHistoricalFindRequest  nextFindRequestParameters = new RelatedEntitiesHistoricalFindRequest(findRequestParameters);

                    nextFindRequestParameters.setOffset(fromEntityElement + pageSize);

                    response.setNextPageURL(formatNextPageURL(localServerURL + urlTemplate,
                                                              nextFindRequestParameters,
                                                              userId,
                                                              startEntityGUID));
                }
            }
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (PagingErrorException error)
        {
            capturePagingErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
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
    public EntityDetailResponse addEntity(String                userId,
                                          EntityCreateRequest   requestBody)
    {
        final  String   methodName = "addEntity";

        String                     entityTypeGUID = null;
        InstanceProperties         initialProperties = null;
        List<Classification>       initialClassifications = null;
        InstanceStatus             initialStatus = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (requestBody != null)
        {
            entityTypeGUID = requestBody.getEntityTypeGUID();
            initialProperties = requestBody.getInitialProperties();
            initialClassifications = requestBody.getInitialClassifications();
            initialStatus = requestBody.getInitialStatus();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.addEntity(userId,
                                                                 entityTypeGUID,
                                                                 initialProperties,
                                                                 initialClassifications,
                                                                 initialStatus));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (StatusNotSupportedException error)
        {
            captureStatusNotSupportedException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }

        return response;
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
    public VoidResponse addEntityProxy(String       userId,
                                       EntityProxy  entityProxy)
    {
        final  String   methodName = "addEntityProxy";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.addEntityProxy(userId, entityProxy);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }

        return response;
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
    public EntityDetailResponse updateEntityStatus(String           userId,
                                                   String           entityGUID,
                                                   InstanceStatus   newStatus)
    {
        final  String   methodName = "updateEntityStatus";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.updateEntityStatus(userId, entityGUID, newStatus));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (StatusNotSupportedException error)
        {
            captureStatusNotSupportedException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Update selected properties in an entity.
     *
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
    public EntityDetailResponse updateEntityProperties(String                      userId,
                                                       String                      entityGUID,
                                                       InstancePropertiesRequest   propertiesRequestBody)
    {
        final  String   methodName = "updateEntityProperties";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.updateEntityProperties(userId, entityGUID, propertiesRequestBody.getInstanceProperties()));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }

        return response;
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
    public EntityDetailResponse undoEntityUpdate(String  userId,
                                                 String  entityGUID)
    {
        final  String   methodName = "undoEntityUpdate";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.undoEntityUpdate(userId, entityGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Delete an entity.  The entity is soft deleted.  This means it is still in the graph but it is no longer returned
     * on queries.  All relationships to the entity are also soft-deleted and will no longer be usable.
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use.
     *
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
     *                                       soft-deletes (use purgeEntity() to remove the entity permanently)
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse  deleteEntity(String                        userId,
                                              String                        obsoleteEntityGUID,
                                              TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "deleteEntity";

        String    typeDefGUID = null;
        String    typeDefName = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.deleteEntity(userId, typeDefGUID, typeDefName, obsoleteEntityGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (FunctionNotSupportedException error)
        {
            captureFunctionNotSupportedException(response, error);
        }

        return response;
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
     *
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
    public VoidResponse purgeEntity(String                        userId,
                                    String                        deletedEntityGUID,
                                    TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "purgeEntity";

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.purgeEntity(userId, typeDefGUID, typeDefName, deletedEntityGUID);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotDeletedException error)
        {
            captureEntityNotDeletedException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
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
    public EntityDetailResponse restoreEntity(String    userId,
                                              String    deletedEntityGUID)
    {
        final  String   methodName = "restoreEntity";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.restoreEntity(userId, deletedEntityGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (EntityNotDeletedException error)
        {
            captureEntityNotDeletedException(response, error);
        }

        return response;
    }


    /**
     * Add the requested classification to a specific entity.
     *
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
    public EntityDetailResponse classifyEntity(String                      userId,
                                               String                      entityGUID,
                                               String                      classificationName,
                                               InstancePropertiesRequest   propertiesRequestBody)
    {
        final  String   methodName = "classifyEntity";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.classifyEntity(userId,
                                                                      entityGUID,
                                                                      classificationName,
                                                                      propertiesRequestBody.getInstanceProperties()));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }

        return response;
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
    public EntityDetailResponse declassifyEntity(String  userId,
                                                 String  entityGUID,
                                                 String  classificationName)
    {
        final  String   methodName = "declassifyEntity";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.declassifyEntity(userId,
                                                                        entityGUID,
                                                                        classificationName));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }

        return response;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
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
    public EntityDetailResponse updateEntityClassification(String                      userId,
                                                           String                      entityGUID,
                                                           String                      classificationName,
                                                           InstancePropertiesRequest   propertiesRequestBody)
    {
        final  String   methodName = "updateEntityClassification";

        EntityDetailResponse response = new EntityDetailResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.updateEntityClassification(userId,
                                                                                  entityGUID,
                                                                                  classificationName,
                                                                                  propertiesRequestBody.getInstanceProperties()));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }

        return response;
    }


    /**
     * Add a new relationship between two entities to the metadata collection.
     *
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
    public RelationshipResponse addRelationship(String                     userId,
                                                RelationshipCreateRequest  createRequestParameters)
    {
        final  String   methodName = "addRelationship";

        String             relationshipTypeGUID = null;
        InstanceProperties initialProperties = null;
        String             entityOneGUID = null;
        String             entityTwoGUID = null;
        InstanceStatus     initialStatus = null;

        RelationshipResponse response = new RelationshipResponse();

        if (createRequestParameters != null)
        {
            relationshipTypeGUID = createRequestParameters.getRelationshipTypeGUID();
            initialProperties = createRequestParameters.getInitialProperties();
            entityOneGUID = createRequestParameters.getEntityOneGUID();
            entityTwoGUID = createRequestParameters.getEntityTwoGUID();
            initialStatus = createRequestParameters.getInitialStatus();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.addRelationship(userId,
                    relationshipTypeGUID,
                    initialProperties,
                    entityOneGUID,
                    entityTwoGUID,
                    initialStatus));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (StatusNotSupportedException error)
        {
            captureStatusNotSupportedException(response, error);
        }

        return response;
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
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse updateRelationshipStatus(String           userId,
                                                         String           relationshipGUID,
                                                         InstanceStatus   newStatus)
    {
        final  String   methodName = "updateRelationshipStatus";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.updateRelationshipStatus(userId,
                                                                                      relationshipGUID,
                                                                                      newStatus));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }
        catch (StatusNotSupportedException error)
        {
            captureStatusNotSupportedException(response, error);
        }

        return response;
    }


    /**
     * Update the properties of a specific relationship.
     *
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
    public RelationshipResponse updateRelationshipProperties(String                      userId,
                                                             String                      relationshipGUID,
                                                             InstancePropertiesRequest   propertiesRequestBody)
    {
        final  String   methodName = "updateRelationshipProperties";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.updateRelationshipProperties(userId,
                                                                                          relationshipGUID,
                                                                                          propertiesRequestBody.getInstanceProperties()));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }

        return response;
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
    public RelationshipResponse undoRelationshipUpdate(String  userId,
                                                       String  relationshipGUID)
    {
        final  String   methodName = "undoRelationshipUpdate";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.undoRelationshipUpdate(userId, relationshipGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
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
    public RelationshipResponse deleteRelationship(String                        userId,
                                                   String                        obsoleteRelationshipGUID,
                                                   TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "deleteRelationship";

        String    typeDefGUID = null;
        String    typeDefName = null;

        RelationshipResponse response = new RelationshipResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.deleteRelationship(userId,
                                                                                typeDefGUID,
                                                                                typeDefName,
                                                                                obsoleteRelationshipGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
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
    public VoidResponse purgeRelationship(String                        userId,
                                          String                        deletedRelationshipGUID,
                                          TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "purgeRelationship";

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.purgeRelationship(userId, typeDefGUID, typeDefName, deletedRelationshipGUID);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotDeletedException error)
        {
            captureRelationshipNotDeletedException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
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
    public RelationshipResponse restoreRelationship(String    userId,
                                                    String    deletedRelationshipGUID)
    {
        final  String   methodName = "restoreRelationship";

        RelationshipResponse response = new RelationshipResponse();

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.restoreRelationship(userId, deletedRelationshipGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }
        catch (RelationshipNotDeletedException error)
        {
            captureRelationshipNotDeletedException(response, error);
        }

        return response;
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
    public EntityDetailResponse reIdentifyEntity(String                        userId,
                                                 String                        entityGUID,
                                                 String                        newEntityGUID,
                                                 TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "reIdentifyEntity";

        String    typeDefGUID = null;
        String    typeDefName = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.reIdentifyEntity(userId,
                                                                        typeDefGUID,
                                                                        typeDefName,
                                                                        entityGUID,
                                                                        newEntityGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Change the type of an existing entity.  Typically this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
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
    public EntityDetailResponse reTypeEntity(String               userId,
                                             String               entityGUID,
                                             TypeDefChangeRequest typeDefChangeRequest)
    {
        final  String   methodName = "reTypeEntity";

        TypeDefSummary currentTypeDefSummary = null;
        TypeDefSummary newTypeDefSummary = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (typeDefChangeRequest != null)
        {
            currentTypeDefSummary = typeDefChangeRequest.getCurrentTypeDef();
            newTypeDefSummary = typeDefChangeRequest.getNewTypeDef();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.reTypeEntity(userId,
                                                                    entityGUID,
                                                                    currentTypeDefSummary,
                                                                    newTypeDefSummary));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (ClassificationErrorException error)
        {
            captureClassificationErrorException(response, error);
        }

        return response;
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param homeMetadataCollectionId the existing identifier for this entity's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
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
    public EntityDetailResponse reHomeEntity(String                        userId,
                                             String                        entityGUID,
                                             String                        homeMetadataCollectionId,
                                             String                        newHomeMetadataCollectionId,
                                             TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "reHomeEntity";

        String    typeDefGUID = null;
        String    typeDefName = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setEntity(localMetadataCollection.reHomeEntity(userId,
                                                                    entityGUID,
                                                                    typeDefGUID,
                                                                    typeDefName,
                                                                    homeMetadataCollectionId,
                                                                    newHomeMetadataCollectionId));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
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
    public RelationshipResponse reIdentifyRelationship(String                        userId,
                                                       String                        relationshipGUID,
                                                       String                        newRelationshipGUID,
                                                       TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "reIdentifyRelationship";

        String    typeDefGUID = null;
        String    typeDefName = null;

        RelationshipResponse response = new RelationshipResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.reIdentifyRelationship(userId,
                                                                                    typeDefGUID,
                                                                                    typeDefName,
                                                                                    relationshipGUID,
                                                                                    newRelationshipGUID));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Change the type of an existing relationship.  Typically this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
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
    public RelationshipResponse reTypeRelationship(String               userId,
                                                   String               relationshipGUID,
                                                   TypeDefChangeRequest typeDefChangeRequest)
    {
        final  String   methodName = "reTypeRelationship";

        TypeDefSummary currentTypeDefSummary = null;
        TypeDefSummary newTypeDefSummary = null;

        RelationshipResponse response = new RelationshipResponse();

        if (typeDefChangeRequest != null)
        {
            currentTypeDefSummary = typeDefChangeRequest.getCurrentTypeDef();
            newTypeDefSummary = typeDefChangeRequest.getNewTypeDef();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.reTypeRelationship(userId,
                                                                                relationshipGUID,
                                                                                currentTypeDefSummary,
                                                                                newTypeDefSummary));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }

        return response;
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param homeMetadataCollectionId the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
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
    public RelationshipResponse reHomeRelationship(String                        userId,
                                                   String                        relationshipGUID,
                                                   String                        homeMetadataCollectionId,
                                                   String                        newHomeMetadataCollectionId,
                                                   TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "reHomeRelationship";

        String    typeDefGUID = null;
        String    typeDefName = null;

        RelationshipResponse response = new RelationshipResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            response.setRelationship(localMetadataCollection.reHomeRelationship(userId,
                                                                                relationshipGUID,
                                                                                typeDefGUID,
                                                                                typeDefName,
                                                                                homeMetadataCollectionId,
                                                                                newHomeMetadataCollectionId));
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }


        return response;
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
    public VoidResponse saveEntityReferenceCopy(String         userId,
                                                EntityDetail   entity)
    {
        final  String   methodName = "saveEntityReferenceCopy";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.saveEntityReferenceCopy(userId, entity);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeDefErrorException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (HomeEntityException error)
        {
            captureHomeEntityException(response, error);
        }
        catch (EntityConflictException error)
        {
            captureEntityConflictException(response, error);
        }
        catch (InvalidEntityException error)
        {
            captureInvalidEntityException(response, error);
        }

        return response;
    }


    /**
     * Remove a reference copy of the the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
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
    public VoidResponse purgeEntityReferenceCopy(String                        userId,
                                                 String                        entityGUID,
                                                 String                        homeMetadataCollectionId,
                                                 TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "purgeEntityReferenceCopy";

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.purgeEntityReferenceCopy(userId,
                                                             entityGUID,
                                                             typeDefGUID,
                                                             typeDefName,
                                                             homeMetadataCollectionId);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (HomeEntityException error)
        {
            captureHomeEntityException(response, error);
        }

        return response;
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
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
    public VoidResponse refreshEntityReferenceCopy(String                        userId,
                                                   String                        entityGUID,
                                                   String                        homeMetadataCollectionId,
                                                   TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "refreshEntityReferenceCopy";

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.refreshEntityReferenceCopy(userId,
                                                               entityGUID,
                                                               typeDefGUID,
                                                               typeDefName,
                                                               homeMetadataCollectionId);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (HomeEntityException error)
        {
            captureHomeEntityException(response, error);
        }

        return response;
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
    public VoidResponse saveRelationshipReferenceCopy(String         userId,
                                                      Relationship   relationship)
    {
        final  String   methodName = "saveRelationshipReferenceCopy";

        VoidResponse response = new VoidResponse();

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.saveRelationshipReferenceCopy(userId, relationship);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (InvalidRelationshipException error)
        {
            captureInvalidRelationshipException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (HomeRelationshipException error)
        {
            captureHomeRelationshipException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeDefErrorException(response, error);
        }
        catch (RelationshipConflictException error)
        {
            captureRelationshipConflictException(response, error);
        }

        return response;
    }




    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
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
    public VoidResponse purgeRelationshipReferenceCopy(String                        userId,
                                                       String                        relationshipGUID,
                                                       String                        homeMetadataCollectionId,
                                                       TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "purgeRelationshipReferenceCopy";

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.purgeRelationshipReferenceCopy(userId,
                                                                   relationshipGUID,
                                                                   typeDefGUID,
                                                                   typeDefName,
                                                                   homeMetadataCollectionId);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }
        catch (HomeRelationshipException error)
        {
            captureHomeRelationshipException(response, error);
        }

        return response;
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
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
    public VoidResponse refreshRelationshipReferenceCopy(String                        userId,
                                                         String                        relationshipGUID,
                                                         String                        homeMetadataCollectionId,
                                                         TypeDefValidationForRequest   typeDefValidationForRequest)
    {
        final  String   methodName = "refreshRelationshipReferenceCopy";

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (typeDefValidationForRequest != null)
        {
            typeDefGUID = typeDefValidationForRequest.getTypeDefGUID();
            typeDefName = typeDefValidationForRequest.getTypeDefName();
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.refreshRelationshipReferenceCopy(userId,
                                                                     relationshipGUID,
                                                                     typeDefGUID,
                                                                     typeDefName,
                                                                     homeMetadataCollectionId);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (RelationshipNotKnownException error)
        {
            captureRelationshipNotKnownException(response, error);
        }
        catch (HomeRelationshipException error)
        {
            captureHomeRelationshipException(response, error);
        }

        return response;
    }


    /**
     * Save the entities and relationships supplied in the instance graph as a reference copies.
     * The id of the home metadata collection is already set up in the instances.
     * Any instances from the home metadata collection are ignored.
     *
     * @param userId unique identifier for requesting server.
     * @param instances instances to save or
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
    public VoidResponse  saveInstanceReferenceCopies(String                 userId,
                                                     InstanceGraphRequest   instances)
    {
        final  String   methodName = "saveInstanceReferenceCopies";

        InstanceGraph instanceGraph = new InstanceGraph();

        VoidResponse response = new VoidResponse();

        if (instances != null)
        {
            instanceGraph.setEntities(instances.getEntityElementList());
            instanceGraph.setRelationships(instances.getRelationshipElementList());
        }

        try
        {
            validateLocalRepository(methodName);

            localMetadataCollection.saveInstanceReferenceCopies(userId, instanceGraph);
        }
        catch (RepositoryErrorException  error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (FunctionNotSupportedException  error)
        {
            captureFunctionNotSupportedException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (InvalidEntityException error)
        {
            captureInvalidEntityException(response, error);
        }
        catch (InvalidRelationshipException error)
        {
            captureInvalidRelationshipException(response, error);
        }
        catch (EntityNotKnownException error)
        {
            captureEntityNotKnownException(response, error);
        }
        catch (PropertyErrorException error)
        {
            capturePropertyErrorException(response, error);
        }
        catch (TypeErrorException error)
        {
            captureTypeDefErrorException(response, error);
        }
        catch (EntityConflictException error)
        {
            captureEntityConflictException(response, error);
        }
        catch (RelationshipConflictException error)
        {
            captureRelationshipConflictException(response, error);
        }

        return response;
    }


    /*
     * =============================================================
     * Private methods
     */


    /**
     * Validate that the local repository is available.
     *
     * @param methodName method being called
     * @throws RepositoryErrorException null local repository
     */
    private void validateLocalRepository(String methodName) throws RepositoryErrorException
    {
        /*
         * If the local repository is not set up then do not attempt to process the request.
         */
        if (localMetadataCollection == null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NO_LOCAL_REPOSITORY;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(methodName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(OMRSAPIResponse response, UserNotAuthorizedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureFunctionNotSupportedException(OMRSAPIResponse response, FunctionNotSupportedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureRepositoryErrorException(OMRSAPIResponse response, RepositoryErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(OMRSAPIResponse response, InvalidParameterException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureInvalidTypeDefException(OMRSAPIResponse response, InvalidTypeDefException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefConflictException(OMRSAPIResponse response, TypeDefConflictException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefNotSupportedException(OMRSAPIResponse response, TypeDefNotSupportedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureHomeRelationshipException(OMRSAPIResponse response, HomeRelationshipException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureRelationshipNotKnownException(OMRSAPIResponse response, RelationshipNotKnownException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureInvalidRelationshipException(OMRSAPIResponse response, InvalidRelationshipException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureRelationshipConflictException(OMRSAPIResponse response, RelationshipConflictException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefErrorException(OMRSAPIResponse response, TypeErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void capturePropertyErrorException(OMRSAPIResponse response, PropertyErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureEntityNotKnownException(OMRSAPIResponse response, EntityNotKnownException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureHomeEntityException(OMRSAPIResponse response, HomeEntityException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefNotKnownException(TypeDefResponse response, TypeDefNotKnownException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefNotKnown(OMRSAPIResponse response, TypeDefNotKnownException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefKnownException(OMRSAPIResponse response, TypeDefKnownException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefInUseException(OMRSAPIResponse response, TypeDefInUseException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeDefNotKnownException(OMRSAPIResponse response, TypeDefNotKnownException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureEntityProxyOnlyException(OMRSAPIResponse response, EntityProxyOnlyException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureClassificationErrorException(OMRSAPIResponse response, ClassificationErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void capturePagingErrorException(OMRSAPIResponse response, PagingErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureTypeErrorException(OMRSAPIResponse response, TypeErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureStatusNotSupportedException(OMRSAPIResponse response, StatusNotSupportedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureEntityNotDeletedException(OMRSAPIResponse response, EntityNotDeletedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }



    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureRelationshipNotDeletedException(OMRSAPIResponse response, RelationshipNotDeletedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }



    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureInvalidEntityException(OMRSAPIResponse response, InvalidEntityException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureEntityConflictException(OMRSAPIResponse response, EntityConflictException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse response,
                                         OMRSCheckedExceptionBase error,
                                         String                   exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Format the url for the next page of a request that includes paging.
     *
     * @param urlTemplate template of the request
     * @param parameters parameters to include in the url
     * @return formatted string
     */
    private String  formatNextPageURL(String    urlTemplate,
                                      Object    request,
                                      Object... parameters)
    {
        MessageFormat mf     = new MessageFormat(urlTemplate);

        return mf.format(parameters) + "{" + request + "}";
    }
}
