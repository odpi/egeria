/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.clients.LocalRepositoryServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The OMRSRESTMetadataCollection represents a remote metadata repository that supports the OMRS REST API.
 * Requests to this metadata collection are translated one-for-one to requests to the remote repository since
 * the OMRS REST API has a one-to-one correspondence with the metadata collection.
 */
public class OMRSRESTMetadataCollection extends OMRSMetadataCollection
{
    static final private String defaultRepositoryName = "REST-connected Repository ";

    private final LocalRepositoryServicesClient omrsClient;   /* Initialized in constructor */
    private final AuditLog                      auditLog; /* Initialized in constructor */
    private final List<String>                  unsupportedFunctionList = new ArrayList<>();

    private String                        errorMessage = null;
    private String                        remoteMetadataCollectionId = null;



    /**
     * Default constructor.
     *
     * @param parentConnector      connector that this metadata collection supports.  The connector has the information
     *                             to call the metadata repository.
     * @param serverName           name of the remote server.
     * @param repositoryName       name of the repository used for logging.
     * @param repositoryHelper     class used to build type definitions and instances.
     * @param repositoryValidator  class used to validate type definitions and instances.
     * @param auditLog             optional logging destination
     * @param metadataCollectionId unique identifier for the metadata collection
     * @throws RepositoryErrorException problem creating the REST client
     */
    OMRSRESTMetadataCollection(OMRSRESTRepositoryConnector parentConnector,
                               String                      serverName,
                               String                      repositoryName,
                               OMRSRepositoryHelper        repositoryHelper,
                               OMRSRepositoryValidator     repositoryValidator,
                               AuditLog                    auditLog,
                               String                      metadataCollectionId) throws RepositoryErrorException
    {
        /*
         * The metadata collection id is the unique id for the metadata collection.  It is managed by the super class.
         */
        super(parentConnector, repositoryName, metadataCollectionId, repositoryHelper, repositoryValidator);

        final String  methodName = "OMRSMetadataCollection constructor";

        this.auditLog = auditLog;

        /*
         * The name of the repository comes from the connection
         */
        ConnectionProperties connection      = parentConnector.getConnection();
        String               endpointAddress = null;
        String               localServerUserId = null;
        String               localServerPassword = null;

        if (connection != null)
        {
            localServerUserId = connection.getUserId();
            localServerPassword = connection.getClearPassword();
            EndpointProperties endpoint = connection.getEndpoint();

            if (endpoint != null)
            {
                endpointAddress = endpoint.getAddress();
            }
        }

        if (endpointAddress == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.REPOSITORY_URL_NULL.getMessageDefinition(),
                                               this.getClass().getName(),
                                               methodName);
        }

        /*
         * Override the repository name
         */
        super.repositoryName = defaultRepositoryName + endpointAddress;

        try
        {
            this.omrsClient = new LocalRepositoryServicesClient(serverName,
                                                                endpointAddress,
                                                                localServerUserId,
                                                                localServerPassword);
        }
        catch (Exception error)
        {
            this.errorMessage = error.getMessage();
            throw new RepositoryErrorException(OMRSErrorCode.NO_REST_CLIENT.getMessageDefinition(repositoryName,
                                                                                                 this.errorMessage),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Detect if the connector failed to initialize.
     *
     * @param methodName calling method
     * @throws RepositoryErrorException thrown if the connector is not initialized
     */
    private void validateClient(String methodName)  throws RepositoryErrorException
    {
        if (this.omrsClient == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NO_REST_CLIENT.getMessageDefinition(repositoryName,
                                                                                                 this.errorMessage),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Test whether a function is supported.
     *
     * @param methodName method name of function
     * @return boolean flag indicating whether the function should be called
     */
    private synchronized boolean isfunctionSupported(String methodName)
    {
        return ! unsupportedFunctionList.contains(methodName);
    }


    /**
     * Mark that a function has been reported as unsupported by the remote repository.
     *
     * @param methodName calling method
     */
    private synchronized void markFunctionUnsupported(String methodName)
    {
        final String localMethodName = "markFunctionUnsupported";

        if (! unsupportedFunctionList.contains(methodName))
        {
            if (auditLog != null)
            {
                auditLog.logMessage(localMethodName, OMRSAuditCode.UNSUPPORTED_REMOTE_FUNCTION.getMessageDefinition(repositoryName,
                                                                                                                    metadataCollectionId,
                                                                                                                    methodName));
            }

            unsupportedFunctionList.add(methodName);
        }
    }


    /**
     * Validate that the metadata collection id from the remote server matches the one expected
     * locally.
     *
     * @param receivedMetadataCollectionId identifier from the remote repository
     * @param methodName calling method
     * @throws RepositoryErrorException thrown if there is any mismatch
     */
    private void validateMetadataCollectionId(String receivedMetadataCollectionId,
                                              String methodName) throws RepositoryErrorException
    {
        if (receivedMetadataCollectionId != null)
        {
            if (! receivedMetadataCollectionId.equals(super.metadataCollectionId))
            {
                throw new RepositoryErrorException(OMRSErrorCode.METADATA_COLLECTION_ID_MISMATCH.getMessageDefinition(repositoryName,
                                                                                                                      receivedMetadataCollectionId,
                                                                                                                      super.metadataCollectionId),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
        else
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION_ID.getMessageDefinition(repositoryName,
                                                                                                              super.metadataCollectionId),
                                               this.getClass().getName(),
                                               methodName);
        }
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
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    @SuppressWarnings("deprecation")
    @Override
    public String getMetadataCollectionId() throws RepositoryErrorException
    {
        if (remoteMetadataCollectionId == null)
        {
            final String methodName = "getMetadataCollectionId (deprecated)";

            validateClient(methodName);
            remoteMetadataCollectionId = omrsClient.getMetadataCollectionId();
            validateMetadataCollectionId(remoteMetadataCollectionId, methodName);
        }

        return remoteMetadataCollectionId;
    }


    /**
     * Returns the identifier of the metadata repository.  This is the identifier used to register the
     * metadata repository with the metadata repository cohort.  It is also the identifier used to
     * identify the home repository of a metadata instance.
     *
     * @param userId calling user
     * @return String metadata collection id.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    @Override
    public String getMetadataCollectionId(String   userId) throws RepositoryErrorException
    {
        final String methodName  = "getMetadataCollectionId";

        if (remoteMetadataCollectionId == null)
        {
            validateClient(methodName);
            remoteMetadataCollectionId = omrsClient.getMetadataCollectionId(userId);
            validateMetadataCollectionId(remoteMetadataCollectionId, methodName);
        }

        return remoteMetadataCollectionId;
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
     * @return TypeDefGalleryResponse List of different categories of type definitions.
     * @throws InvalidParameterException the userId is null
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery getAllTypes(String userId) throws InvalidParameterException,
                                                            RepositoryErrorException,
                                                            UserNotAuthorizedException
    {
        final String methodName  = "getAllTypes";

        validateClient(methodName);
        return omrsClient.getAllTypes(userId);
    }


    /**
     * Returns a list of type definitions that have the specified name.  Type names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an
     * arbitrary string of characters and ampersand for an arbitrary character.
     *
     * @param userId unique identifier for requesting user.
     * @param name   name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefGallery list.
     * @throws InvalidParameterException  the name of the TypeDef is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery findTypesByName(String userId,
                                          String name) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              UserNotAuthorizedException
    {
        final String methodName  = "findTypesByName";

        validateClient(methodName);
        return omrsClient.findTypesByName(userId, name);
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param userId   unique identifier for requesting user.
     * @param category enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> findTypeDefsByCategory(String          userId,
                                                TypeDefCategory category) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName  = "findTypeDefsByCategory";

        validateClient(methodName);
        return omrsClient.findTypeDefsByCategory(userId, category);
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param userId   unique identifier for requesting user.
     * @param category enum value for the category of an AttributeTypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String                   userId,
                                                                  AttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                            RepositoryErrorException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName  = "findAttributeTypeDefsByCategory";

        validateClient(methodName);
        return omrsClient.findAttributeTypeDefsByCategory(userId, category);
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId        unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties a list of property names.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the matchCriteria is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> findTypeDefsByProperty(String            userId,
                                                TypeDefProperties matchCriteria) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName  = "findTypeDefsByProperty";

        validateClient(methodName);
        return omrsClient.findTypeDefsByProperty(userId, matchCriteria);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId       unique identifier for requesting user.
     * @param standard     name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier   identifier of the element in the standard null means any.
     * @return TypeDefs list each entry in the list contains a typedef.  This is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> findTypesByExternalID(String userId,
                                               String standard,
                                               String organization,
                                               String identifier) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "findTypesByExternalID";

        validateClient(methodName);
        return omrsClient.findTypesByExternalID(userId, standard, organization, identifier);
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId         unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefs list each entry in the list contains a typedef.  This is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException  the searchCriteria is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> searchForTypeDefs(String userId,
                                           String searchCriteria) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "searchForTypeDefs";

        validateClient(methodName);
        return omrsClient.searchForTypeDefs(userId, searchCriteria);
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique id of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef getTypeDefByGUID(String userId,
                                    String guid) throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        TypeDefNotKnownException,
                                                        UserNotAuthorizedException
    {
        final String methodName  = "getTypeDefByGUID";

        validateClient(methodName);
        return omrsClient.getTypeDefByGUID(userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public AttributeTypeDef getAttributeTypeDefByGUID(String userId,
                                                      String guid) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          TypeDefNotKnownException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "getAttributeTypeDefByGUID";

        validateClient(methodName);
        return omrsClient.getAttributeTypeDefByGUID(userId, guid);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name   String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the name is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef getTypeDefByName(String userId,
                                    String name) throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        TypeDefNotKnownException,
                                                        UserNotAuthorizedException
    {
        final String methodName  = "getTypeDefByName";

        validateClient(methodName);
        return omrsClient.getTypeDefByName(userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name   String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the name is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public AttributeTypeDef getAttributeTypeDefByName(String userId,
                                                      String name) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          TypeDefNotKnownException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "getAttributeTypeDefByName";

        validateClient(methodName);
        return omrsClient.getAttributeTypeDefByName(userId, name);
    }


    /**
     * Create a collection of related types.
     *
     * @param userId   unique identifier for requesting user.
     * @param newTypes TypeDefGalleryResponse structure describing the new AttributeTypeDefs and TypeDefs.
     * @throws InvalidParameterException     the new TypeDef is null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotSupportedException  the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException         the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException      the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException       the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public void addTypeDefGallery(String         userId,
                                  TypeDefGallery newTypes) throws InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  TypeDefNotSupportedException,
                                                                  TypeDefKnownException,
                                                                  TypeDefConflictException,
                                                                  InvalidTypeDefException,
                                                                  FunctionNotSupportedException,
                                                                  UserNotAuthorizedException
    {
        final String methodName  = "addTypeDefGallery";

        validateClient(methodName);
        omrsClient.addTypeDefGallery(userId, newTypes);
    }


    /**
     * Create a definition of a new TypeDef.
     *
     * @param userId     unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException     the new TypeDef is null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotSupportedException  the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException         the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException      the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException       the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public void addTypeDef(String  userId,
                           TypeDef newTypeDef) throws InvalidParameterException,
                                                      RepositoryErrorException,
                                                      TypeDefNotSupportedException,
                                                      TypeDefKnownException,
                                                      TypeDefConflictException,
                                                      InvalidTypeDefException,
                                                      FunctionNotSupportedException,
                                                      UserNotAuthorizedException
    {
        final String methodName  = "addTypeDef";

        validateClient(methodName);
        omrsClient.addTypeDef(userId, newTypeDef);
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId              unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException     the new TypeDef is null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotSupportedException  the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException         the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException      the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException       the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public void addAttributeTypeDef(String           userId,
                                    AttributeTypeDef newAttributeTypeDef) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 TypeDefNotSupportedException,
                                                                                 TypeDefKnownException,
                                                                                 TypeDefConflictException,
                                                                                 InvalidTypeDefException,
                                                                                 FunctionNotSupportedException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName  = "addAttributeTypeDef";

        validateClient(methodName);
        omrsClient.addAttributeTypeDef(userId, newAttributeTypeDef);
    }


    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId  unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
     * @throws InvalidParameterException    the TypeDef is null.
     * @throws RepositoryErrorException     there is a problem communicating with the metadata repository where
     *                                      the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException     the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException      the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException   the userId is not permitted to perform this operation.
     */
    @Override
    public boolean verifyTypeDef(String userId,
                                 TypeDef typeDef) throws InvalidParameterException,
                                                         RepositoryErrorException,
                                                         TypeDefNotSupportedException,
                                                         TypeDefConflictException,
                                                         InvalidTypeDefException,
                                                         UserNotAuthorizedException
    {
        final String methodName  = "verifyTypeDef";

        validateClient(methodName);
        return omrsClient.verifyTypeDef(userId, typeDef);
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param userId           unique identifier for requesting user.
     * @param attributeTypeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
     * @throws InvalidParameterException    the TypeDef is null.
     * @throws RepositoryErrorException     there is a problem communicating with the metadata repository where
     *                                      the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException     the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException      the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException   the userId is not permitted to perform this operation.
     */
    @Override
    public boolean verifyAttributeTypeDef(String           userId,
                                          AttributeTypeDef attributeTypeDef) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeDefNotSupportedException,
                                                                                    TypeDefConflictException,
                                                                                    InvalidTypeDefException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName  = "verifyAttributeTypeDef";

        validateClient(methodName);
        return omrsClient.verifyAttributeTypeDef(userId, attributeTypeDef);
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param userId       unique identifier for requesting user.
     * @param typeDefPatch TypeDef patch describing change to TypeDef.
     * @return updated TypeDef
     * @throws InvalidParameterException     the TypeDefPatch is null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotKnownException      the requested TypeDef is not found in the metadata collection.
     * @throws PatchErrorException           the TypeDef can not be updated because the supplied patch is incompatible
     *                                       with the stored TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   TypeDefNotKnownException,
                                                                   PatchErrorException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String methodName  = "updateTypeDef";

        validateClient(methodName);
        return omrsClient.updateTypeDef(userId, typeDefPatch);
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param userId              unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName String unique name for the TypeDef.
     * @throws InvalidParameterException     the one of TypeDef identifiers is null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotKnownException      the requested TypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException         the TypeDef can not be deleted because there are instances of this type in
     *                                       the metadata collection.  These instances need to be purged before the
     *                                       TypeDef can be deleted.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public void deleteTypeDef(String userId,
                              String obsoleteTypeDefGUID,
                              String obsoleteTypeDefName) throws InvalidParameterException,
                                                                 RepositoryErrorException,
                                                                 TypeDefNotKnownException,
                                                                 TypeDefInUseException,
                                                                 FunctionNotSupportedException,
                                                                 UserNotAuthorizedException
    {
        final String methodName  = "deleteTypeDef";

        validateClient(methodName);
        omrsClient.deleteTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param userId              unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteTypeDefName String unique name for the AttributeTypeDef.
     * @throws InvalidParameterException     the one of AttributeTypeDef identifiers is null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotKnownException      the requested AttributeTypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException         the AttributeTypeDef can not be deleted because there are instances of this type in
     *                                       the metadata collection.  These instances need to be purged before the
     *                                       AttributeTypeDef can be deleted.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public void deleteAttributeTypeDef(String userId,
                                       String obsoleteTypeDefGUID,
                                       String obsoleteTypeDefName) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          TypeDefNotKnownException,
                                                                          TypeDefInUseException,
                                                                          FunctionNotSupportedException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "deleteAttributeTypeDef";

        validateClient(methodName);
        omrsClient.deleteAttributeTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId              unique identifier for requesting user.
     * @param originalTypeDefGUID the original guid of the TypeDef.
     * @param originalTypeDefName the original name of the TypeDef.
     * @param newTypeDefGUID      the new identifier for the TypeDef.
     * @param newTypeDefName      new name for this TypeDef.
     * @return typeDef new values for this TypeDef, including the new guid/name.
     * @throws InvalidParameterException     one of the parameters is invalid or null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotKnownException      the TypeDef identified by the original guid/name is not found
     *                                       in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef reIdentifyTypeDef(String userId,
                                     String originalTypeDefGUID,
                                     String originalTypeDefName,
                                     String newTypeDefGUID,
                                     String newTypeDefName) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   TypeDefNotKnownException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String methodName  = "reIdentifyTypeDef";

        validateClient(methodName);
        return omrsClient.reIdentifyTypeDef(userId, originalTypeDefGUID, originalTypeDefName, newTypeDefGUID, newTypeDefName);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId                       unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID the original guid of the AttributeTypeDef.
     * @param originalAttributeTypeDefName the original name of the AttributeTypeDef.
     * @param newAttributeTypeDefGUID      the new identifier for the AttributeTypeDef.
     * @param newAttributeTypeDefName      new name for this AttributeTypeDef.
     * @return attributeTypeDef new values for this AttributeTypeDef, including the new guid/name.
     * @throws InvalidParameterException     one of the parameters is invalid or null.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws TypeDefNotKnownException      the AttributeTypeDef identified by the original guid/name is not
     *                                       found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public AttributeTypeDef reIdentifyAttributeTypeDef(String userId,
                                                       String originalAttributeTypeDefGUID,
                                                       String originalAttributeTypeDefName,
                                                       String newAttributeTypeDefGUID,
                                                       String newAttributeTypeDefName) throws InvalidParameterException,
                                                                                              RepositoryErrorException,
                                                                                              TypeDefNotKnownException,
                                                                                              FunctionNotSupportedException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName  = "reIdentifyAttributeTypeDef";

        validateClient(methodName);
        return omrsClient.reIdentifyAttributeTypeDef(userId,
                                                     originalAttributeTypeDefGUID,
                                                     originalAttributeTypeDefName,
                                                     newAttributeTypeDefGUID,
                                                     newAttributeTypeDefName);
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail isEntityKnown(String userId,
                                      String guid) throws InvalidParameterException,
                                                          RepositoryErrorException,
                                                          UserNotAuthorizedException
    {
        final String methodName  = "isEntityKnown";

        validateClient(methodName);
        return omrsClient.isEntityKnown(userId, guid);
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique identifier for the entity
     * @return EntitySummary structure
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException    the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntitySummary getEntitySummary(String userId,
                                          String guid) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              EntityNotKnownException,
                                                              UserNotAuthorizedException
    {
        final String methodName  = "getEntitySummary";

        validateClient(methodName);
        return omrsClient.getEntitySummary(userId, guid);
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException    the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException   the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid) throws InvalidParameterException,
                                                            RepositoryErrorException,
                                                            EntityNotKnownException,
                                                            EntityProxyOnlyException,
                                                            UserNotAuthorizedException
    {
        final String methodName  = "getEntityDetail";

        validateClient(methodName);
        return omrsClient.getEntityDetail(userId, guid);
    }


    /**
     * Return a historical version of an entity includes the header, classifications and properties of the entity.
     *
     * @param userId   unique identifier for requesting user.
     * @param guid     String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EntityDetail structure.
     * @throws InvalidParameterException     the guid or date is null, or the asOfTime property is for a future time
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws EntityNotKnownException       the requested entity instance is not known in the metadata collection
     *                                       at the time requested.
     * @throws EntityProxyOnlyException      the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid,
                                        Date   asOfTime) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                EntityNotKnownException,
                                                                EntityProxyOnlyException,
                                                                FunctionNotSupportedException,
                                                                UserNotAuthorizedException
    {
        final String methodName  = "getEntityDetail";

        validateClient(methodName);
        return omrsClient.getEntityDetail(userId, guid, asOfTime);
    }


    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<EntityDetail>} of each historical version of the entity detail within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<EntityDetail> getEntityDetailHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startFromElement,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    EntityNotKnownException,
                                                                                                    EntityProxyOnlyException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetailHistory";

        validateClient(methodName);
        return omrsClient.getEntityDetailHistory(userId, guid, fromTime, toTime, startFromElement, pageSize, sequencingOrder);
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param userId                  unique identifier for requesting user.
     * @param entityGUID              String unique identifier for the entity.
     * @param relationshipTypeGUID    String GUID of the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus    By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                                to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                                status values except DELETED.
     * @param asOfTime                Requests a historical query of the relationships for the entity.  Null means return the
     *                                present values.
     * @param sequencingProperty      String name of the property that is to be used to sequence the results.
     *                                Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder         Enum defining how the results should be ordered.
     * @param pageSize                -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                                unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException     a parameter is invalid or null.
     * @throws TypeErrorException            the type guid passed on the request is not known by the
     *                                       metadata collection.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws EntityNotKnownException       the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException        the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException          the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException    the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipsForEntity(String               userId,
                                                        String               entityGUID,
                                                        String               relationshipTypeGUID,
                                                        int                  fromRelationshipElement,
                                                        List<InstanceStatus> limitResultsByStatus,
                                                        Date                 asOfTime,
                                                        String               sequencingProperty,
                                                        SequencingOrder      sequencingOrder,
                                                        int                  pageSize) throws InvalidParameterException,
                                                                                              TypeErrorException,
                                                                                              RepositoryErrorException,
                                                                                              EntityNotKnownException,
                                                                                              PropertyErrorException,
                                                                                              PagingErrorException,
                                                                                              FunctionNotSupportedException,
                                                                                              UserNotAuthorizedException
    {
        final String             methodName = "getRelationshipsForEntity";

        validateClient(methodName);
        return omrsClient.getRelationshipsForEntity(userId,
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
     * Return a list of entities that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of entity property conditions to match.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param matchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support this optional method.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<EntityDetail> findEntities(String                    userId,
                                           String                    entityTypeGUID,
                                           List<String>              entitySubtypeGUIDs,
                                           SearchProperties          matchProperties,
                                           int                       fromEntityElement,
                                           List<InstanceStatus>      limitResultsByStatus,
                                           SearchClassifications     matchClassifications,
                                           Date                      asOfTime,
                                           String                    sequencingProperty,
                                           SequencingOrder           sequencingOrder,
                                           int                       pageSize) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      PagingErrorException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String       methodName = "findEntities";

        validateClient(methodName);
        return omrsClient.findEntities(userId,
                                       entityTypeGUID,
                                       entitySubtypeGUIDs,
                                       matchProperties,
                                       fromEntityElement,
                                       limitResultsByStatus,
                                       matchClassifications,
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
     * @param matchProperties Optional list of entity properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public List<EntityDetail> findEntitiesByProperty(String                    userId,
                                                     String                    entityTypeGUID,
                                                     InstanceProperties        matchProperties,
                                                     MatchCriteria             matchCriteria,
                                                     int                       fromEntityElement,
                                                     List<InstanceStatus>      limitResultsByStatus,
                                                     List<String>              limitResultsByClassification,
                                                     Date                      asOfTime,
                                                     String                    sequencingProperty,
                                                     SequencingOrder           sequencingOrder,
                                                     int                       pageSize) throws InvalidParameterException,
                                                                                                RepositoryErrorException,
                                                                                                TypeErrorException,
                                                                                                PropertyErrorException,
                                                                                                PagingErrorException,
                                                                                                FunctionNotSupportedException,
                                                                                                UserNotAuthorizedException
    {
        final String       methodName = "findEntitiesByProperty";

        validateClient(methodName);
        return omrsClient.findEntitiesByProperty(userId,
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
     * Return a list of entities that have the requested type of classifications attached.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null means any type of entity
     *                       (but could be slow so not recommended).
     * @param classificationName name of the classification, note a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search (where any String
     *                                      property's value should be defined as a Java regular expression, even if it
     *                                      should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<EntityDetail> findEntitiesByClassification(String                    userId,
                                                            String                    entityTypeGUID,
                                                            String                    classificationName,
                                                            InstanceProperties        matchClassificationProperties,
                                                            MatchCriteria             matchCriteria,
                                                            int                       fromEntityElement,
                                                            List<InstanceStatus>      limitResultsByStatus,
                                                            Date                      asOfTime,
                                                            String                    sequencingProperty,
                                                            SequencingOrder           sequencingOrder,
                                                            int                       pageSize) throws InvalidParameterException,
                                                                                                       TypeErrorException,
                                                                                                       RepositoryErrorException,
                                                                                                       ClassificationErrorException,
                                                                                                       PropertyErrorException,
                                                                                                       PagingErrorException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       UserNotAuthorizedException
    {
        final String       methodName  = "findEntitiesByClassification";

        validateClient(methodName);
        return omrsClient.findEntitiesByClassification(userId,
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
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within entity instances of the specified type(s).
     *                       This parameter must not be null.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     * @see OMRSRepositoryHelper#getContainsRegex(String)
     */
    @Override
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize) throws InvalidParameterException,
                                                                                                  TypeErrorException,
                                                                                                  RepositoryErrorException,
                                                                                                  PropertyErrorException,
                                                                                                  PagingErrorException,
                                                                                                  FunctionNotSupportedException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName  = "findEntitiesByPropertyValue";

        validateClient(methodName);
        return omrsClient.findEntitiesByPropertyValue(userId,
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
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship  isRelationshipKnown(String     userId,
                                             String     guid) throws InvalidParameterException,
                                                                     RepositoryErrorException,
                                                                     UserNotAuthorizedException
    {
        final String methodName  = "isRelationshipKnown";

        validateClient(methodName);
        return omrsClient.isRelationshipKnown(userId, guid);
    }


    /**
     * Return a requested relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship getRelationship(String    userId,
                                        String    guid) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               RelationshipNotKnownException,
                                                               UserNotAuthorizedException
    {
        final String methodName  = "getRelationship";

        validateClient(methodName);
        return omrsClient.getRelationship(userId, guid);
    }


    /**
     * Return a historical version of a relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or the asOfTime property is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  Relationship getRelationship(String    userId,
                                         String    guid,
                                         Date      asOfTime) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    RelationshipNotKnownException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String methodName  = "getRelationship";

        validateClient(methodName);
        return omrsClient.getRelationship(userId, guid, asOfTime);
    }


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all
     * historical versions of a relationship, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<Relationship>} of each historical version of the relationship within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship instance is not known in the metadata collection
     *                                       at the time requested.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startFromElement,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    RelationshipNotKnownException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipHistory";

        validateClient(methodName);
        return omrsClient.getRelationshipHistory(userId, guid, fromTime, toTime, startFromElement, pageSize, sequencingOrder);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationships(String                    userId,
                                                 String                    relationshipTypeGUID,
                                                 List<String>              relationshipSubtypeGUIDs,
                                                 SearchProperties          matchProperties,
                                                 int                       fromRelationshipElement,
                                                 List<InstanceStatus>      limitResultsByStatus,
                                                 Date                      asOfTime,
                                                 String                    sequencingProperty,
                                                 SequencingOrder           sequencingOrder,
                                                 int                       pageSize) throws InvalidParameterException,
                                                                                            TypeErrorException,
                                                                                            RepositoryErrorException,
                                                                                            PropertyErrorException,
                                                                                            PagingErrorException,
                                                                                            FunctionNotSupportedException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName  = "findRelationships";

        validateClient(methodName);
        return omrsClient.findRelationships(userId,
                                            relationshipTypeGUID,
                                            relationshipSubtypeGUIDs,
                                            matchProperties,
                                            fromRelationshipElement,
                                            limitResultsByStatus,
                                            asOfTime,
                                            sequencingProperty,
                                            sequencingOrder,
                                            pageSize);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be received as a series of pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param matchProperties Optional list of relationship properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the properties should be matched to the relationships in the repository.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties        matchProperties,
                                                           MatchCriteria             matchCriteria,
                                                           int                       fromRelationshipElement,
                                                           List<InstanceStatus>      limitResultsByStatus,
                                                           Date                      asOfTime,
                                                           String                    sequencingProperty,
                                                           SequencingOrder           sequencingOrder,
                                                           int                       pageSize) throws InvalidParameterException,
                                                                                                      TypeErrorException,
                                                                                                      RepositoryErrorException,
                                                                                                      PropertyErrorException,
                                                                                                      PagingErrorException,
                                                                                                      FunctionNotSupportedException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName  = "findRelationshipsByProperty";

        validateClient(methodName);
        return omrsClient.findRelationshipsByProperty(userId,
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
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the relationship instances of the specified type(s).
     *                       This parameter must not be null.
     * @param fromRelationshipElement Element number of the results to skip to when building the results list
     *                                to return.  Zero means begin at the start of the results.  This is used
     *                                to retrieve the results over a number of pages.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     * @see OMRSRepositoryHelper#getContainsRegex(String)
     */
    @Override
    public  List<Relationship> findRelationshipsByPropertyValue(String                    userId,
                                                                String                    relationshipTypeGUID,
                                                                String                    searchCriteria,
                                                                int                       fromRelationshipElement,
                                                                List<InstanceStatus>      limitResultsByStatus,
                                                                Date                      asOfTime,
                                                                String                    sequencingProperty,
                                                                SequencingOrder           sequencingOrder,
                                                                int                       pageSize) throws InvalidParameterException,
                                                                                                           TypeErrorException,
                                                                                                           RepositoryErrorException,
                                                                                                           PropertyErrorException,
                                                                                                           PagingErrorException,
                                                                                                           FunctionNotSupportedException,
                                                                                                           UserNotAuthorizedException
    {
        final String             methodName  = "findRelationshipsByPropertyValue";

        validateClient(methodName);
        return omrsClient.findRelationshipsByPropertyValue(userId,
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
     * Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by either the startEntityGUID or the endEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  InstanceGraph getLinkingEntities(String                    userId,
                                             String                    startEntityGUID,
                                             String                    endEntityGUID,
                                             List<InstanceStatus>      limitResultsByStatus,
                                             Date                      asOfTime) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        EntityNotKnownException,
                                                                                        PropertyErrorException,
                                                                                        FunctionNotSupportedException,
                                                                                        UserNotAuthorizedException
    {
        final String          methodName  = "getLinkingEntities";

        validateClient(methodName);
        return omrsClient.getLinkingEntities(userId, startEntityGUID, endEntityGUID, limitResultsByStatus, asOfTime);
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
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException one or more of the type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  InstanceGraph getEntityNeighborhood(String               userId,
                                                String               entityGUID,
                                                List<String>         entityTypeGUIDs,
                                                List<String>         relationshipTypeGUIDs,
                                                List<InstanceStatus> limitResultsByStatus,
                                                List<String>         limitResultsByClassification,
                                                Date                 asOfTime,
                                                int                  level) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   EntityNotKnownException,
                                                                                   PropertyErrorException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String          methodName  = "getEntityNeighborhood";

        validateClient(methodName);
        return omrsClient.getEntityNeighborhood(userId,
                                                entityGUID,
                                                entityTypeGUIDs,
                                                relationshipTypeGUIDs,
                                                limitResultsByStatus,
                                                limitResultsByClassification,
                                                asOfTime,
                                                level);
    }


    /**
     * Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param entityTypeGUIDs list of types to search for.  Null means any type.
     * @param fromEntityElement starting element for results list.  Used in paging.  Zero means first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return list of entities either directly or indirectly connected to the start entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException one of the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws EntityNotKnownException the entity identified by the startEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> getRelatedEntities(String               userId,
                                                  String               startEntityGUID,
                                                  List<String>         entityTypeGUIDs,
                                                  int                  fromEntityElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String>         limitResultsByClassification,
                                                  Date                 asOfTime,
                                                  String               sequencingProperty,
                                                  SequencingOrder      sequencingOrder,
                                                  int                  pageSize) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        TypeErrorException,
                                                                                        EntityNotKnownException,
                                                                                        PropertyErrorException,
                                                                                        PagingErrorException,
                                                                                        FunctionNotSupportedException,
                                                                                        UserNotAuthorizedException
    {
        final String       methodName  = "getRelatedEntities";

        validateClient(methodName);
        return omrsClient.getRelatedEntities(userId,
                                             startEntityGUID,
                                             entityTypeGUIDs,
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
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addEntity(String                     userId,
                                  String                     entityTypeGUID,
                                  InstanceProperties         initialProperties,
                                  List<Classification>       initialClassifications,
                                  InstanceStatus             initialStatus) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   ClassificationErrorException,
                                                                                   StatusNotSupportedException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName  = "addEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.addEntity(userId, entityTypeGUID, initialProperties, initialClassifications, initialStatus);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Save a new entity that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new entity is assigned a new GUID and put
     * in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addExternalEntity(String                userId,
                                          String                entityTypeGUID,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          InstanceProperties    initialProperties,
                                          List<Classification>  initialClassifications,
                                          InstanceStatus        initialStatus) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      ClassificationErrorException,
                                                                                      StatusNotSupportedException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String  methodName = "addExternalEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.addExternalEntity(userId,
                                                    entityTypeGUID,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    initialProperties,
                                                    initialClassifications,
                                                    initialStatus);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws InvalidParameterException the entity proxy is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws FunctionNotSupportedException the repository does not support entity proxies as first class elements.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                FunctionNotSupportedException,
                                                                UserNotAuthorizedException
    {
        final String methodName  = "addEntityProxy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.addEntityProxy(userId, entityProxy);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Update the status for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @return EntityDetail showing the current entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              EntityNotKnownException,
                                                                              StatusNotSupportedException,
                                                                              FunctionNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String methodName  = "updateEntityStatus";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.updateEntityStatus(userId, entityGUID, newStatus);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Update selected properties in an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties) throws InvalidParameterException,
                                                                                       RepositoryErrorException,
                                                                                       EntityNotKnownException,
                                                                                       PropertyErrorException,
                                                                                       FunctionNotSupportedException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName  = "updateEntityProperties";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.updateEntityProperties(userId, entityGUID, properties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support undo.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail undoEntityUpdate(String  userId,
                                         String  entityGUID) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    EntityNotKnownException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String methodName  = "undoEntityUpdate";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.undoEntityUpdate(userId, entityGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph but, it is no longer returned
     * on queries.  All homed relationships to the entity are also soft-deleted and will no longer be usable, while any
     * reference copy relationships to the entity will be purged (and will no longer be accessible in this repository).
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete() call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use; however,
     * this will not restore any of the relationships that were soft-deleted as part of the original deleteEntity() call.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity.
     * @return deleted entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       soft-deletes use purgeEntity() to remove the entity permanently.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail   deleteEntity(String userId,
                                       String typeDefGUID,
                                       String typeDefName,
                                       String obsoleteEntityGUID) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         EntityNotKnownException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "deleteEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.deleteEntity(userId, typeDefGUID, typeDefName, obsoleteEntityGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Permanently removes a deleted entity from the metadata collection. All relationships to the entity -- both homed
     * and reference copies -- will also be purged to maintain referential integrity within the repository. This request
     * can not be undone.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to purge.
     * @param typeDefName unique name of the type of the entity to purge.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is not in DELETED status and so can not be purged
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                EntityNotKnownException,
                                                                EntityNotDeletedException,
                                                                FunctionNotSupportedException,
                                                                UserNotAuthorizedException
    {
        final String methodName  = "purgeEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeEntity(userId, typeDefGUID, typeDefName, deletedEntityGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the restored entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is currently not in DELETED status and so it can not be restored
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          EntityNotKnownException,
                                                                          EntityNotDeletedException,
                                                                          FunctionNotSupportedException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "restoreEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.restoreEntity(userId, deletedEntityGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             FunctionNotSupportedException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName  = "classifyEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.classifyEntity(userId, entityGUID, classificationName, classificationProperties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Add the requested classification to a specific entity. If the provided entityProxy does not exist, it should be
     * created, classified, and stored in the repository by this method.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity as a proxy
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     *
     * @return Classification newly added classification
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity proxy was not found and could not be created
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String methodName = "classifyEntityProxy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.classifyEntity(userId, entityProxy, classificationName, classificationProperties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  EntityDetail classifyEntity(String               userId,
                                        String               entityGUID,
                                        String               classificationName,
                                        String               externalSourceGUID,
                                        String               externalSourceName,
                                        ClassificationOrigin classificationOrigin,
                                        String               classificationOriginGUID,
                                        InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                              RepositoryErrorException,
                                                                                              EntityNotKnownException,
                                                                                              ClassificationErrorException,
                                                                                              PropertyErrorException,
                                                                                              UserNotAuthorizedException,
                                                                                              FunctionNotSupportedException
    {
        final String methodName = "classifyEntity (detailed)";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.classifyEntity(userId,
                                                 entityGUID,
                                                 classificationName,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 classificationOrigin,
                                                 classificationOriginGUID,
                                                 classificationProperties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Add the requested classification to a specific entity. If the provided entityProxy does not exist, it should be
     * created, classified, and stored in the repository by this method.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity as a proxy
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification.
     *
     * @return Classification newly added classification
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String methodName = "classifyEntityProxy (detailed)";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.classifyEntity(userId,
                                                 entityProxy,
                                                 classificationName,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 classificationOrigin,
                                                 classificationOriginGUID,
                                                 classificationProperties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            ClassificationErrorException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException
    {
        final String methodName  = "declassifyEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.declassifyEntity(userId, entityGUID, classificationName);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity proxy was not found and could not be created
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public Classification declassifyEntity(String       userId,
                                           EntityProxy  entityProxy,
                                           String       classificationName) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   EntityNotKnownException,
                                                                                   ClassificationErrorException,
                                                                                   UserNotAuthorizedException,
                                                                                   FunctionNotSupportedException
    {
        final String methodName  = "declassifyEntityProxy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.declassifyEntity(userId, entityProxy, classificationName);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName  = "updateEntityClassification";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.updateEntityClassification(userId, entityGUID, classificationName, properties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public Classification updateEntityClassification(String               userId,
                                                     EntityProxy          entityProxy,
                                                     String               classificationName,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String methodName  = "updateEntityProxyClassification";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.updateEntityClassification(userId, entityProxy, classificationName, properties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
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
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   EntityNotKnownException,
                                                                                   StatusNotSupportedException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName  = "addRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.addRelationship(userId,
                                                  relationshipTypeGUID,
                                                  initialProperties,
                                                  entityOneGUID,
                                                  entityTwoGUID,
                                                  initialStatus);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Save a new relationship that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new relationship is assigned a new GUID and put
     * in the requested state.  The new relationship is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status; typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship addExternalRelationship(String               userId,
                                                String               relationshipTypeGUID,
                                                String               externalSourceGUID,
                                                String               externalSourceName,
                                                InstanceProperties   initialProperties,
                                                String               entityOneGUID,
                                                String               entityTwoGUID,
                                                InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           TypeErrorException,
                                                                                           PropertyErrorException,
                                                                                           EntityNotKnownException,
                                                                                           StatusNotSupportedException,
                                                                                           UserNotAuthorizedException,
                                                                                           FunctionNotSupportedException
    {
        final String  methodName = "addExternalRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.addExternalRelationship(userId,
                                                          relationshipTypeGUID,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          initialProperties,
                                                          entityOneGUID,
                                                          entityTwoGUID,
                                                          initialStatus);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Update the status of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param newStatus new InstanceStatus for the relationship.
     * @return Resulting relationship structure with the new status set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    RelationshipNotKnownException,
                                                                                    StatusNotSupportedException,
                                                                                    FunctionNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName  = "updateRelationshipStatus";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.updateRelationshipStatus(userId, relationshipGUID, newStatus);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
     * @return Resulting relationship structure with the new properties set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             RelationshipNotKnownException,
                                                                                             PropertyErrorException,
                                                                                             FunctionNotSupportedException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName  = "updateRelationshipProperties";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.updateRelationshipProperties(userId, relationshipGUID, properties);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the new current header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support undo.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship undoRelationshipUpdate(String  userId,
                                               String  relationshipGUID) throws InvalidParameterException,
                                                                                RepositoryErrorException,
                                                                                RelationshipNotKnownException,
                                                                                FunctionNotSupportedException,
                                                                                UserNotAuthorizedException
    {
        final String methodName  = "undoRelationshipUpdate";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.undoRelationshipUpdate(userId, relationshipGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED, and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @return deleted relationship
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship deleteRelationship(String userId,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String obsoleteRelationshipGUID) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   RelationshipNotKnownException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName  = "deleteRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.deleteRelationship(userId, typeDefGUID, typeDefName, obsoleteRelationshipGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to purge.
     * @param typeDefName unique name of the type of the relationship to purge.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            RelationshipNotKnownException,
                                                                            RelationshipNotDeletedException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException
    {
        final String methodName  = "purgeRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeRelationship(userId, typeDefGUID, typeDefName, deletedRelationshipGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the restored header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      RelationshipNotDeletedException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName  = "restoreRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.restoreRelationship(userId, deletedRelationshipGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
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
     * @return entity new values for this entity, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reIdentifyEntity(String     userId,
                                         String     typeDefGUID,
                                         String     typeDefName,
                                         String     entityGUID,
                                         String     newEntityGUID) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          EntityNotKnownException,
                                                                          FunctionNotSupportedException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "reIdentifyEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.reIdentifyEntity(userId, typeDefGUID, typeDefName, entityGUID, newEntityGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Change an existing entity's type.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
     * @return entity new values for this entity, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws ClassificationErrorException the entity's classifications are not valid for the new type.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeErrorException,
                                                                              PropertyErrorException,
                                                                              ClassificationErrorException,
                                                                              EntityNotKnownException,
                                                                              FunctionNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String methodName  = "reTypeEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.reTypeEntity(userId, entityGUID, currentTypeDefSummary, newTypeDefSummary);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
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
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return entity new values for this entity, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reHomeEntity(String         userId,
                                     String         entityGUID,
                                     String         typeDefGUID,
                                     String         typeDefName,
                                     String         homeMetadataCollectionId,
                                     String         newHomeMetadataCollectionId,
                                     String         newHomeMetadataCollectionName) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          EntityNotKnownException,
                                                                                          FunctionNotSupportedException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName  = "reHomeEntity";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.reHomeEntity(userId,
                                               entityGUID,
                                               typeDefGUID,
                                               typeDefName,
                                               homeMetadataCollectionId,
                                               newHomeMetadataCollectionId,
                                               newHomeMetadataCollectionName);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
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
     * @return relationship new values for this relationship, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reIdentifyRelationship(String     userId,
                                               String     typeDefGUID,
                                               String     typeDefName,
                                               String     relationshipGUID,
                                               String     newRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName  = "reIdentifyRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.reIdentifyRelationship(userId, typeDefGUID, typeDefName, relationshipGUID, newRelationshipGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Change an existing relationship's type.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship used to verify the relationship identity.
     * @param newTypeDefSummary details of this relationship's new TypeDef.
     * @return relationship new values for this relationship, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeErrorException,
                                                                                    PropertyErrorException,
                                                                                    RelationshipNotKnownException,
                                                                                    FunctionNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName  = "reTypeRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.reTypeRelationship(userId, relationshipGUID, currentTypeDefSummary, newTypeDefSummary);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
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
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return relationship new values for this relationship, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reHomeRelationship(String   userId,
                                           String   relationshipGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId,
                                           String   newHomeMetadataCollectionId,
                                           String   newHomeMetadataCollectionName) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          RelationshipNotKnownException,
                                                                                          FunctionNotSupportedException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName  = "reHomeRelationship";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.reHomeRelationship(userId,
                                                     relationshipGUID,
                                                     typeDefGUID,
                                                     typeDefName,
                                                     homeMetadataCollectionId,
                                                     newHomeMetadataCollectionId,
                                                     newHomeMetadataCollectionName);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to save.
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveEntityReferenceCopy(String         userId,
                                        EntityDetail   entity) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      TypeErrorException,
                                                                      PropertyErrorException,
                                                                      HomeEntityException,
                                                                      EntityConflictException,
                                                                      InvalidEntityException,
                                                                      FunctionNotSupportedException,
                                                                      UserNotAuthorizedException
    {
        final String methodName  = "saveEntityReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.saveEntityReferenceCopy(userId, entity);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity is not recognized by this repository
     * @throws UserNotAuthorizedException to calling user is not authorized to retrieve this metadata
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 EntityNotKnownException,
                                                                                 UserNotAuthorizedException,
                                                                                 FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.getHomeClassifications(userId, entityGUID);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity is not recognized by this repository
     * @throws UserNotAuthorizedException to calling user is not authorized to retrieve this metadata
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID,
                                                       Date   asOfTime) throws InvalidParameterException,
                                                                               RepositoryErrorException,
                                                                               EntityNotKnownException,
                                                                               UserNotAuthorizedException,
                                                                               FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications (with history)";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                return omrsClient.getHomeClassifications(userId, entityGUID, asOfTime);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }

        return null;
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to purge.
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  void deleteEntityReferenceCopy(String         userId,
                                           EntityDetail   entity) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         TypeErrorException,
                                                                         PropertyErrorException,
                                                                         HomeEntityException,
                                                                         EntityConflictException,
                                                                         InvalidEntityException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "deleteEntityReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.deleteEntityReferenceCopy(userId, entity);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to purge.
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  void purgeEntityReferenceCopy(String         userId,
                                          EntityDetail   entity) throws InvalidParameterException,
                                                                        RepositoryErrorException,
                                                                        TypeErrorException,
                                                                        PropertyErrorException,
                                                                        HomeEntityException,
                                                                        EntityConflictException,
                                                                        InvalidEntityException,
                                                                        FunctionNotSupportedException,
                                                                        UserNotAuthorizedException
    {
        final String methodName  = "purgeDeleteEntityReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeEntityReferenceCopy(userId, entity);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID the unique identifier for the entity.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the new home repository.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public void purgeEntityReferenceCopy(String   userId,
                                         String   entityGUID,
                                         String   typeDefGUID,
                                         String   typeDefName,
                                         String   homeMetadataCollectionId) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   EntityNotKnownException,
                                                                                   HomeEntityException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName  = "purgeEntityReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeEntityReferenceCopy(userId, entityGUID, typeDefGUID, typeDefName, homeMetadataCollectionId);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
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
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public void refreshEntityReferenceCopy(String   userId,
                                           String   entityGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     EntityNotKnownException,
                                                                                     HomeEntityException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName  = "refreshEntityReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.refreshEntityReferenceCopy(userId, entityGUID, typeDefGUID, typeDefName, homeMetadataCollectionId);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityDetail   entity,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      EntityConflictException,
                                                                                      InvalidEntityException,
                                                                                      PropertyErrorException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String methodName  = "saveClassificationReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.saveClassificationReferenceCopy(userId, entity, classification);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityProxy    entity,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      EntityConflictException,
                                                                                      InvalidEntityException,
                                                                                      PropertyErrorException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String methodName  = "saveClassificationReferenceCopy(proxy)";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.saveClassificationReferenceCopy(userId, entity, classification);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to purge.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityDetail   entity,
                                                  Classification classification) throws InvalidParameterException,
                                                                                        TypeErrorException,
                                                                                        PropertyErrorException,
                                                                                        EntityConflictException,
                                                                                        InvalidEntityException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException,
                                                                                        FunctionNotSupportedException
    {
        final String methodName  = "purgeClassificationReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeClassificationReferenceCopy(userId, entity, classification);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to purge.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityProxy    entity,
                                                  Classification classification) throws InvalidParameterException,
                                                                                        TypeErrorException,
                                                                                        PropertyErrorException,
                                                                                        EntityConflictException,
                                                                                        InvalidEntityException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException,
                                                                                        FunctionNotSupportedException
    {
        final String methodName  = "purgeClassificationReferenceCopy (proxy)";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeClassificationReferenceCopy(userId, entity, classification);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationship relationship to save.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public void saveRelationshipReferenceCopy(String         userId,
                                              Relationship   relationship) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  TypeErrorException,
                                                                                  EntityNotKnownException,
                                                                                  PropertyErrorException,
                                                                                  HomeRelationshipException,
                                                                                  RelationshipConflictException,
                                                                                  InvalidRelationshipException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName  = "saveRelationshipReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.saveRelationshipReferenceCopy(userId, relationship);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to purge.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public  void deleteRelationshipReferenceCopy(String         userId,
                                                 Relationship   relationship) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     TypeErrorException,
                                                                                     EntityNotKnownException,
                                                                                     PropertyErrorException,
                                                                                     HomeRelationshipException,
                                                                                     RelationshipConflictException,
                                                                                     InvalidRelationshipException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName  = "deleteRelationshipReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.deleteRelationshipReferenceCopy(userId, relationship);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to purge.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public  void purgeRelationshipReferenceCopy(String         userId,
                                                Relationship   relationship) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeErrorException,
                                                                                    EntityNotKnownException,
                                                                                    PropertyErrorException,
                                                                                    HomeRelationshipException,
                                                                                    RelationshipConflictException,
                                                                                    InvalidRelationshipException,
                                                                                    FunctionNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName  = "purgeDeleteRelationshipReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeRelationshipReferenceCopy(userId, relationship);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationshipReferenceCopy(String   userId,
                                               String   relationshipGUID,
                                               String   typeDefGUID,
                                               String   typeDefName,
                                               String   homeMetadataCollectionId) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         RelationshipNotKnownException,
                                                                                         HomeRelationshipException,
                                                                                         FunctionNotSupportedException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName  = "purgeRelationshipReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.purgeRelationshipReferenceCopy(userId, relationshipGUID, typeDefGUID, typeDefName, homeMetadataCollectionId);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the serverName is not permitted to perform this operation.
     */
    @Override
    public void refreshRelationshipReferenceCopy(String   userId,
                                                 String   relationshipGUID,
                                                 String   typeDefGUID,
                                                 String   typeDefName,
                                                 String   homeMetadataCollectionId) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           RelationshipNotKnownException,
                                                                                           HomeRelationshipException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName  = "refreshRelationshipReferenceCopy";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.refreshRelationshipReferenceCopy(userId, relationshipGUID, typeDefGUID, typeDefName, homeMetadataCollectionId);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }



    /**
     * Save the entities and relationships supplied in the instance graph as a reference copies.
     * The id of the home metadata collection is already set up in the instances.
     * Any instances from the home metadata collection are ignored.
     *
     * @param userId unique identifier for requesting server.
     * @param instances instances to save.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveInstanceReferenceCopies(String          userId,
                                            InstanceGraph   instances) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeErrorException,
                                                                              EntityNotKnownException,
                                                                              PropertyErrorException,
                                                                              EntityConflictException,
                                                                              RelationshipConflictException,
                                                                              InvalidEntityException,
                                                                              InvalidRelationshipException,
                                                                              FunctionNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String methodName  = "saveInstanceReferenceCopies";

        if (isfunctionSupported(methodName))
        {
            validateClient(methodName);

            try
            {
                omrsClient.saveInstanceReferenceCopies(userId, instances);
            }
            catch (FunctionNotSupportedException error)
            {
                markFunctionUnsupported(methodName);
                throw error;
            }
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }
}
