/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientFactory;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The MetadataCollectionServicesClient represents a remote metadata repository that supports the OMRS Repository REST API.
 * Requests to this metadata collection are translated one-for-one to requests to the remote repository since
 * the OMRS REST API has a one-to-one correspondence with the metadata collection.
 * 
 * The URLs for the REST APIs are of this form:
 * 
 * <ul>
 *     <li><i>restURLroot</i> - serverURLroot + "/servers/" + serverName</li>
 *     <li><i>rootServiceNameInURL</i> - "/open-metadata/repository-services"</li>
 *     <li><i>userIdInURL</i> - optional - "/users/{0}"</li>
 *     <li><i>serviceURLMarker</i> - "/" for local repository services and "/enterprise/" for enterprise repository services</li>
 *     <li><i>operationSpecificURL</i> - operation specific part of the URL</li>
 * </ul>
 */
public abstract class MetadataCollectionServicesClient implements AuditLoggingComponent
{
    static final private String rootServiceNameInURL  = "/open-metadata/repository-services";
    static final private String userIdInURL           = "/users/{0}";

    private final String              localServerUserId   = null;
    private final String              localServerPassword = null;

    protected       String              restURLRoot;                /* Initialized in constructor */
    private   final String              serviceURLMarker;           /* Initialized in constructor */
    protected       RESTClientConnector restClient;                 /* Initialized in constructor */
    protected       String              repositoryName;             /* Initialized in constructor */

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    protected AuditLog auditLog = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param repositoryName name of the repository to connect to - used for error messages
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param serviceURLMarker string indicating which repository service it is calling.
     *
     * @throws InvalidParameterException bad input parameters
     */
    MetadataCollectionServicesClient(String repositoryName,
                                     String restURLRoot,
                                     String serviceURLMarker) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(restURLRoot, repositoryName, methodName);
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedHTTPCode(),
                                                error.getReportingClassName(),
                                                error.getReportingActionDescription(),
                                                error.getReportedErrorMessage(),
                                                error.getReportedErrorMessageId(),
                                                error.getReportedErrorMessageParameters(),
                                                error.getReportedSystemAction(),
                                                error.getReportedUserAction(),
                                                error.getClass().getName(),
                                                error.getParameterName(),
                                                error.getRelatedProperties());
        }

        this.repositoryName = repositoryName;
        this.restURLRoot = restURLRoot;
        this.serviceURLMarker = serviceURLMarker;
        this.restClient = this.getRESTClientConnector(repositoryName, restURLRoot, null, null);
        this.repositoryName = repositoryName;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param repositoryName name of the repository to connect to - used for error messages
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param serviceURLMarker string indicating which repository service it is calling.
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    MetadataCollectionServicesClient(String     repositoryName,
                                     String     restURLRoot,
                                     String     serviceURLMarker,
                                     String     userId,
                                     String     password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(restURLRoot, repositoryName, methodName);
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedHTTPCode(),
                                                error.getReportingClassName(),
                                                error.getReportingActionDescription(),
                                                error.getReportedErrorMessage(),
                                                error.getReportedErrorMessageId(),
                                                error.getReportedErrorMessageParameters(),
                                                error.getReportedSystemAction(),
                                                error.getReportedUserAction(),
                                                error.getClass().getName(),
                                                error.getParameterName(),
                                                error.getRelatedProperties());
        }

        this.repositoryName = repositoryName;
        this.restURLRoot = restURLRoot;
        this.serviceURLMarker = serviceURLMarker;
        this.restClient = this.getRESTClientConnector(repositoryName, restURLRoot, userId, password);
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
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
    @Deprecated
    public String getMetadataCollectionId() throws RepositoryErrorException
    {
        final String methodName  = "getMetadataCollectionId";
        final String operationSpecificURL = "metadata-collection-id";

        MetadataCollectionIdResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    MetadataCollectionIdResponse.class,
                                                    restURLRoot + rootServiceNameInURL + serviceURLMarker + operationSpecificURL);
        }
        catch (Exception error)
        {
           throw new RepositoryErrorException(OMRSErrorCode.REMOTE_REPOSITORY_ERROR.getMessageDefinition(methodName,
                                                                                                         repositoryName,
                                                                                                         error.getClass().getSimpleName(),
                                                                                                         error.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }

        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getMetadataCollectionId();
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
    public String getMetadataCollectionId(String   userId) throws RepositoryErrorException
    {
        final String methodName  = "getMetadataCollectionId";
        final String operationSpecificURL = "metadata-collection-id";

        MetadataCollectionIdResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    MetadataCollectionIdResponse.class,
                                                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                    userId);
        }
        catch (Exception error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.REMOTE_REPOSITORY_ERROR.getMessageDefinition(methodName,
                                                                                                          repositoryName,
                                                                                                          error.getClass().getSimpleName(),
                                                                                                          error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }

        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        if (restResult.getMetadataCollectionId() == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION_ID_FROM_REMOTE.getMessageDefinition(repositoryName),
                                               this.getClass().getName(),
                                               methodName);
        }

        return restResult.getMetadataCollectionId();
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
     * @return TypeDefGallery List of different categories of type definitions.
     * @throws InvalidParameterException the userId is null
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefGallery getAllTypes(String userId) throws InvalidParameterException,
                                                            RepositoryErrorException,
                                                            UserNotAuthorizedException
    {
        final String methodName  = "getAllTypes";
        final String operationSpecificURL = "types/all";

        TypeDefGalleryResponse restResult = this.callTypeDefGalleryGetRESTCall(methodName,
                                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                               userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);

        return this.getTypeDefGalleryFromRESTResult(restResult);
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
    public TypeDefGallery findTypesByName(String userId,
                                          String name) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              UserNotAuthorizedException
    {
        final String methodName  = "findTypesByName";
        final String operationSpecificURL = "types/by-name?name={1}";

        TypeDefGalleryResponse restResult = this.callTypeDefGalleryGetRESTCall(methodName,
                                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                               userId,
                                                                               name);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return this.getTypeDefGalleryFromRESTResult(restResult);
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
    public List<TypeDef> findTypeDefsByCategory(String userId,
                                                TypeDefCategory category) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName  = "findTypeDefsByCategory";
        final String operationSpecificURL = "types/typedefs/by-category";

        TypeDefListResponse restResult = this.callTypeDefListPostRESTCall(methodName,
                                                                          restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                          category,
                                                                          userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDefs();
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
    public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String                   userId,
                                                                  AttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                            RepositoryErrorException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName  = "findAttributeTypeDefsByCategory";
        final String operationSpecificURL = "types/attribute-typedefs/by-category";

        AttributeTypeDefListResponse restResult = this.callAttributeTypeDefListPostRESTCall(methodName,
                                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                            category,
                                                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getAttributeTypeDefs();
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
    public List<TypeDef> findTypeDefsByProperty(String            userId,
                                                TypeDefProperties matchCriteria) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName  = "findTypeDefsByProperty";
        final String operationSpecificURL = "types/typedefs/by-property";

        TypeDefListResponse restResult = this.callTypeDefListGetRESTCall(methodName,
                                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                         userId,
                                                                         matchCriteria);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDefs();
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
    public List<TypeDef> findTypesByExternalID(String userId,
                                               String standard,
                                               String organization,
                                               String identifier) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "findTypesByExternalID";
        final String operationSpecificURL = "types/typedefs/by-external-id?standard={1}&organization={2}&identifier={3}";


        TypeDefListResponse restResult = this.callTypeDefListGetRESTCall(methodName,
                                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                         userId,
                                                                         standard,
                                                                         organization,
                                                                         identifier);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDefs();
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
    public List<TypeDef> searchForTypeDefs(String userId,
                                           String searchCriteria) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "searchForTypeDefs";
        final String operationSpecificURL = "types/typedefs/by-property-value?searchCriteria={1}";

        TypeDefListResponse restResult = this.callTypeDefListGetRESTCall(methodName,
                                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                         userId,
                                                                         searchCriteria);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDefs();
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
    public TypeDef getTypeDefByGUID(String userId,
                                    String guid) throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        TypeDefNotKnownException,
                                                        UserNotAuthorizedException
    {
        final String methodName  = "getTypeDefByGUID";
        final String operationSpecificURL = "types/typedef/{1}";

        TypeDefResponse restResult = this.callTypeDefGetRESTCall(methodName,
                                                                 restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                 userId,
                                                                 guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDef();
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
    public AttributeTypeDef getAttributeTypeDefByGUID(String userId,
                                                      String guid) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          TypeDefNotKnownException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "getAttributeTypeDefByGUID";
        final String operationSpecificURL = "types/attribute-typedef/{1}";

        AttributeTypeDefResponse restResult = this.callAttributeTypeDefGetRESTCall(methodName,
                                                                                   restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                   userId,
                                                                                   guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getAttributeTypeDef();
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
    public TypeDef getTypeDefByName(String userId,
                                    String name) throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        TypeDefNotKnownException,
                                                        UserNotAuthorizedException
    {
        final String methodName  = "getTypeDefByName";
        final String operationSpecificURL = "types/typedef/name/{1}";

        TypeDefResponse restResult = this.callTypeDefGetRESTCall(methodName,
                                                                 restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                 userId,
                                                                 name);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDef();
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
    public AttributeTypeDef getAttributeTypeDefByName(String userId,
                                                      String name) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          TypeDefNotKnownException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "getAttributeTypeDefByName";
        final String operationSpecificURL = "types/attribute-typedef/name/{1}";

        AttributeTypeDefResponse restResult = this.callAttributeTypeDefGetRESTCall(methodName,
                                                                                   restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                   userId,
                                                                                   name);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getAttributeTypeDef();
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
        final String operationSpecificURL = "types";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            newTypes,
                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotSupportedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowTypeDefKnownException(methodName, restResult);
        this.detectAndThrowTypeDefConflictException(methodName, restResult);
        this.detectAndThrowInvalidTypeDefException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "types/typedef";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            newTypeDef,
                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotSupportedException(methodName, restResult);
        this.detectAndThrowTypeDefKnownException(methodName, restResult);
        this.detectAndThrowTypeDefConflictException(methodName, restResult);
        this.detectAndThrowInvalidTypeDefException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "types/attribute-typedef";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            newAttributeTypeDef,
                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotSupportedException(methodName, restResult);
        this.detectAndThrowTypeDefKnownException(methodName, restResult);
        this.detectAndThrowTypeDefConflictException(methodName, restResult);
        this.detectAndThrowInvalidTypeDefException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public boolean verifyTypeDef(String  userId,
                                 TypeDef typeDef) throws InvalidParameterException,
                                                         RepositoryErrorException,
                                                         TypeDefNotSupportedException,
                                                         TypeDefConflictException,
                                                         InvalidTypeDefException,
                                                         UserNotAuthorizedException
    {
        final String methodName  = "verifyTypeDef";
        final String operationSpecificURL = "types/typedef/compatibility";

        BooleanResponse restResult = this.callBooleanPostRESTCall(methodName,
                                                                  restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                  typeDef,
                                                                  userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotSupportedException(methodName, restResult);
        this.detectAndThrowTypeDefConflictException(methodName, restResult);
        this.detectAndThrowInvalidTypeDefException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.isFlag();
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
    public boolean verifyAttributeTypeDef(String           userId,
                                          AttributeTypeDef attributeTypeDef) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeDefNotSupportedException,
                                                                                    TypeDefConflictException,
                                                                                    InvalidTypeDefException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName  = "verifyAttributeTypeDef";
        final String operationSpecificURL = "types/attribute-typedef/compatibility";

        BooleanResponse restResult = this.callBooleanPostRESTCall(methodName,
                                                                  restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                  attributeTypeDef,
                                                                  userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotSupportedException(methodName, restResult);
        this.detectAndThrowTypeDefConflictException(methodName, restResult);
        this.detectAndThrowInvalidTypeDefException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.isFlag();
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
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   TypeDefNotKnownException,
                                                                   PatchErrorException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String methodName  = "updateTypeDef";
        final String operationSpecificURL = "types/typedef/update";

        TypeDefResponse restResult = this.callTypeDefPostRESTCall(methodName,
                                                                  restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                  typeDefPatch,
                                                                  userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowPatchErrorException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDef();
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
        final String operationSpecificURL = "types/typedef/{1}/delete";

        TypeDefDeleteRequest requestBody = new TypeDefDeleteRequest();
        requestBody.setObsoleteTypeDefName(obsoleteTypeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            obsoleteTypeDefGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowTypeDefInUseException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "types/attribute-typedef/{1}/delete";

        TypeDefDeleteRequest requestBody = new TypeDefDeleteRequest();
        requestBody.setObsoleteTypeDefName(obsoleteTypeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            obsoleteTypeDefGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowTypeDefInUseException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "types/typedef/{1}/identifier";

        TypeDefReIdentifyRequest requestBody = new TypeDefReIdentifyRequest();
        requestBody.setOriginalTypeDefName(originalTypeDefName);
        requestBody.setNewTypeDefGUID(newTypeDefGUID);
        requestBody.setNewTypeDefName(newTypeDefName);

        TypeDefResponse restResult = this.callTypeDefPostRESTCall(methodName,
                                                                  restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                  requestBody,
                                                                  userId,
                                                                  originalTypeDefGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getTypeDef();
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
        final String operationSpecificURL = "types/attribute-typedef/{1}/identifier";

        TypeDefReIdentifyRequest requestBody = new TypeDefReIdentifyRequest();
        requestBody.setOriginalTypeDefName(originalAttributeTypeDefName);
        requestBody.setNewTypeDefGUID(newAttributeTypeDefGUID);
        requestBody.setNewTypeDefName(newAttributeTypeDefName);

        AttributeTypeDefResponse restResult = this.callAttributeTypeDefPostRESTCall(methodName,
                                                                                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                    requestBody,
                                                                                    userId,
                                                                                    originalAttributeTypeDefGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowTypeDefNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getAttributeTypeDef();
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
    public EntityDetail isEntityKnown(String userId,
                                      String guid) throws InvalidParameterException,
                                                          RepositoryErrorException,
                                                          UserNotAuthorizedException
    {
        final String methodName  = "isEntityKnown";
        final String operationSpecificURL = "instances/entity/{1}/existence";

        EntityDetailResponse restResult = this.callEntityDetailGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntitySummary getEntitySummary(String userId,
                                          String guid) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              EntityNotKnownException,
                                                              UserNotAuthorizedException
    {
        final String methodName  = "getEntitySummary";
        final String operationSpecificURL = "instances/entity/{1}/summary";

        EntitySummaryResponse restResult = this.callEntitySummaryGetRESTCall(methodName,
                                                                             restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                             userId,
                                                                             guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail getEntityDetail(String userId,
                                        String guid) throws InvalidParameterException,
                                                            RepositoryErrorException,
                                                            EntityNotKnownException,
                                                            EntityProxyOnlyException,
                                                            UserNotAuthorizedException
    {
        final String methodName  = "getEntityDetail";
        final String operationSpecificURL = "instances/entity/{1}";

        EntityDetailResponse restResult = this.callEntityDetailGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowEntityProxyOnlyException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
        final String operationSpecificURL = "instances/entity/{1}/history";

        HistoryRequest requestBody = new HistoryRequest();
        requestBody.setAsOfTime(asOfTime);
        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowEntityProxyOnlyException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
        final String methodName           = "getEntityDetailHistory";
        final String operationSpecificURL = "instances/entity/{1}/history/all";

        HistoryRangeRequest requestBody = new HistoryRangeRequest();
        requestBody.setFromTime(fromTime);
        requestBody.setToTime(toTime);
        requestBody.setOffset(startFromElement);
        requestBody.setPageSize(pageSize);
        requestBody.setSequencingOrder(sequencingOrder);
        EntityListResponse restResult = this.callEntityListPostRESTCall(methodName,
                                                                        restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                        requestBody,
                                                                        userId,
                                                                        guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowEntityProxyOnlyException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntities();
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
     * @param limitResultsByStatus    By default, relationships in all statuses are returned.  However, it is possible
     *                                to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                                status values.
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
        RelationshipListResponse restResult;

        if (asOfTime == null)
        {
            final String           operationSpecificURL  = "instances/entity/{1}/relationships";
            TypeLimitedFindRequest findRequestParameters = new TypeLimitedFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                               findRequestParameters,
                                                               userId,
                                                               entityGUID);
        }
        else
        {
            final String                     operationSpecificURL  = "instances/entity/{1}/relationships/history";
            TypeLimitedHistoricalFindRequest findRequestParameters = new TypeLimitedHistoricalFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                               findRequestParameters,
                                                               userId,
                                                               entityGUID);
        }

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationships();
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
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
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
        EntityListResponse restResult;

        if (asOfTime == null)
        {
            final String      operationSpecificURL  = "instances/entities";
            EntityFindRequest findRequestParameters = new EntityFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setSubtypeGUIDs(entitySubtypeGUIDs);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setMatchClassifications(matchClassifications);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                    findRequestParameters,
                    userId);
        }
        else
        {
            final String                operationSpecificURL  = "instances/entities/history";
            EntityHistoricalFindRequest findRequestParameters = new EntityHistoricalFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setSubtypeGUIDs(entitySubtypeGUIDs);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setMatchClassifications(matchClassifications);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                    findRequestParameters,
                    userId);

        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntities();
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
     */
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
        EntityListResponse restResult;

        if (asOfTime == null)
        {
            final String              operationSpecificURL  = "instances/entities/by-property";
            EntityPropertyFindRequest findRequestParameters = new EntityPropertyFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setMatchCriteria(matchCriteria);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId);
        }
        else
        {
            final String                        operationSpecificURL  = "instances/entities/by-property/history";
            EntityPropertyHistoricalFindRequest findRequestParameters = new EntityPropertyHistoricalFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setMatchCriteria(matchCriteria);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId);

        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntities();
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
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
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
     */
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
        EntityListResponse restResult;

        if (asOfTime == null)
        {
            final String             operationSpecificURL  = "instances/entities/by-classification/{1}";
            PropertyMatchFindRequest findRequestParameters = new PropertyMatchFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setMatchProperties(matchClassificationProperties);
            findRequestParameters.setMatchCriteria(matchCriteria);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId,
                                                         classificationName);
        }
        else
        {
            final String                       operationSpecificURL  = "instances/entities/by-classification/{1}/history";
            PropertyMatchHistoricalFindRequest findRequestParameters = new PropertyMatchHistoricalFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setMatchProperties(matchClassificationProperties);
            findRequestParameters.setMatchCriteria(matchCriteria);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId,
                                                         classificationName);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntities();
    }



    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the entities of the supplied type, even if it should be an exact match.
     *                       (Retrieve all entities of the supplied type if this is either null or an empty string.)
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
     */
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
        final String       methodName  = "findEntitiesByPropertyValue";
        EntityListResponse restResult;

        if (asOfTime == null)
        {
            final String              operationSpecificURL  = "instances/entities/by-property-value?searchCriteria={1}";
            EntityPropertyFindRequest findRequestParameters = new EntityPropertyFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId,
                                                         searchCriteria);
        }
        else
        {
            final String                        operationSpecificURL  = "instances/entities/by-property-value/history?searchCriteria={1}";
            EntityPropertyHistoricalFindRequest findRequestParameters = new EntityPropertyHistoricalFindRequest();

            findRequestParameters.setTypeGUID(entityTypeGUID);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId,
                                                         searchCriteria);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntities();
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
    public Relationship isRelationshipKnown(String     userId,
                                            String     guid) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    UserNotAuthorizedException
    {
        final String methodName  = "isRelationshipKnown";
        final String operationSpecificURL = "instances/relationship/{1}/existence";

        RelationshipResponse restResult = this.callRelationshipGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public Relationship getRelationship(String    userId,
                                        String    guid) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               RelationshipNotKnownException,
                                                               UserNotAuthorizedException
    {
        final String methodName  = "getRelationship";
        final String operationSpecificURL = "instances/relationship/{1}";

        RelationshipResponse restResult = this.callRelationshipGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public Relationship getRelationship(String    userId,
                                        String    guid,
                                        Date      asOfTime) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   RelationshipNotKnownException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String methodName  = "getRelationship";
        final String operationSpecificURL = "instances/relationship/{1}/history";

        HistoryRequest requestBody = new HistoryRequest();
        requestBody.setAsOfTime(asOfTime);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            guid);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
        final String methodName           = "getRelationshipHistory";
        final String operationSpecificURL = "instances/relationship/{1}/history/all";

        HistoryRangeRequest requestBody = new HistoryRangeRequest();
        requestBody.setFromTime(fromTime);
        requestBody.setToTime(toTime);
        requestBody.setOffset(startFromElement);
        requestBody.setPageSize(pageSize);
        requestBody.setSequencingOrder(sequencingOrder);
        RelationshipListResponse restResult = this.callRelationshipListPostRESTCall(methodName,
                                                                                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                    requestBody,
                                                                                    userId,
                                                                                    guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationships();
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement the starting element number of the entities to return.
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
     */
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
        final String             methodName  = "findRelationships";
        RelationshipListResponse restResult;

        if (asOfTime == null)
        {
            final String operationSpecificURL = "instances/relationships";

            InstanceFindRequest findRequestParameters = new InstanceFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setSubtypeGUIDs(relationshipSubtypeGUIDs);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                    findRequestParameters,
                    userId);
        }
        else
        {
            final String operationSpecificURL = "instances/relationships/history";

            InstanceHistoricalFindRequest findRequestParameters = new InstanceHistoricalFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setSubtypeGUIDs(relationshipSubtypeGUIDs);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                    restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                    findRequestParameters,
                    userId);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationships();
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
     */
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
        final String             methodName  = "findRelationshipsByProperty";
        RelationshipListResponse restResult;

        if (asOfTime == null)
        {
            final String operationSpecificURL = "instances/relationships/by-property";

            PropertyMatchFindRequest findRequestParameters = new PropertyMatchFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setMatchCriteria(matchCriteria);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                               findRequestParameters,
                                                               userId);
        }
        else
        {
            final String operationSpecificURL = "instances/relationships/by-property/history";

            PropertyMatchHistoricalFindRequest findRequestParameters = new PropertyMatchHistoricalFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setMatchProperties(matchProperties);
            findRequestParameters.setMatchCriteria(matchCriteria);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                               findRequestParameters,
                                                               userId);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationships();
    }


    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the relationships of the supplied type, even if it should be an exact match.
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
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
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
        RelationshipListResponse restResult;

        if (asOfTime == null)
        {
            final String           operationSpecificURL  = "instances/relationships/by-property-value?searchCriteria={1}";
            TypeLimitedFindRequest findRequestParameters = new TypeLimitedFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                               findRequestParameters,
                                                               userId,
                                                               searchCriteria);
        }
        else
        {
            final String                     operationSpecificURL  = "instances/relationships/by-property-value/history?searchCriteria={1}";
            TypeLimitedHistoricalFindRequest findRequestParameters = new TypeLimitedHistoricalFindRequest();

            findRequestParameters.setTypeGUID(relationshipTypeGUID);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromRelationshipElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callRelationshipListPostRESTCall(methodName,
                                                               restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                               findRequestParameters,
                                                               userId,
                                                               searchCriteria);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationships();
    }


    /**
     * Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
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
    public InstanceGraph getLinkingEntities(String                    userId,
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
        InstanceGraphResponse restResult;

        if (asOfTime == null)
        {
            final String       operationSpecificURL  = "instances/entities/from-entity/{1}/by-linkage?endEntityGUID={2}";
            OMRSAPIFindRequest findRequestParameters = new OMRSAPIFindRequest();

            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);

            restResult = this.callInstanceGraphPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            findRequestParameters,
                                                            userId,
                                                            startEntityGUID,
                                                            endEntityGUID);
        }
        else
        {
            final String                 operationSpecificURL  = "instances/entities/from-entity/{1}/by-linkage/history?endEntityGUID={2}";
            OMRSAPIHistoricalFindRequest findRequestParameters = new OMRSAPIHistoricalFindRequest();

            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setAsOfTime(asOfTime);

            restResult = this.callInstanceGraphPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            findRequestParameters,
                                                            userId,
                                                            startEntityGUID,
                                                            endEntityGUID);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return this.getInstanceGraphFromRESTResult(restResult);
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
    public InstanceGraph getEntityNeighborhood(String               userId,
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
        InstanceGraphResponse restResult;

        if (asOfTime == null)
        {
            final String                  operationSpecificURL  = "instances/entities/from-entity/{1}/by-neighborhood?level={2}";
            EntityNeighborhoodFindRequest findRequestParameters = new EntityNeighborhoodFindRequest();

            findRequestParameters.setEntityTypeGUIDs(entityTypeGUIDs);
            findRequestParameters.setRelationshipTypeGUIDs(relationshipTypeGUIDs);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);

            restResult = this.callInstanceGraphPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            findRequestParameters,
                                                            userId,
                                                            entityGUID,
                                                            level);
        }
        else
        {
            final String                            operationSpecificURL  = "instances/entities/from-entity/{1}/by-neighborhood/history?level={2}";
            EntityNeighborhoodHistoricalFindRequest findRequestParameters = new EntityNeighborhoodHistoricalFindRequest();

            findRequestParameters.setEntityTypeGUIDs(entityTypeGUIDs);
            findRequestParameters.setRelationshipTypeGUIDs(relationshipTypeGUIDs);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);

            restResult = this.callInstanceGraphPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            findRequestParameters,
                                                            userId,
                                                            entityGUID,
                                                            level);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return this.getInstanceGraphFromRESTResult(restResult);
    }


    /**
     * Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param entityTypeGUIDs list of types to search for.  Null means any type.
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
        EntityListResponse restResult;

        if (asOfTime == null)
        {
            final String               operationSpecificURL  = "instances/entities/from-entity/{1}/by-relationship";
            RelatedEntitiesFindRequest findRequestParameters = new RelatedEntitiesFindRequest();

            findRequestParameters.setEntityTypeGUIDs(entityTypeGUIDs);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId,
                                                         startEntityGUID);
        }
        else
        {
            final String                         operationSpecificURL  = "instances/entities/from-entity/{1}/by-relationship";
            RelatedEntitiesHistoricalFindRequest findRequestParameters = new RelatedEntitiesHistoricalFindRequest();

            findRequestParameters.setEntityTypeGUIDs(entityTypeGUIDs);
            findRequestParameters.setAsOfTime(asOfTime);
            findRequestParameters.setOffset(fromEntityElement);
            findRequestParameters.setLimitResultsByStatus(limitResultsByStatus);
            findRequestParameters.setLimitResultsByClassification(limitResultsByClassification);
            findRequestParameters.setSequencingOrder(sequencingOrder);
            findRequestParameters.setSequencingProperty(sequencingProperty);
            findRequestParameters.setPageSize(pageSize);

            restResult = this.callEntityListPostRESTCall(methodName,
                                                         restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                         findRequestParameters,
                                                         userId,
                                                         startEntityGUID);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowPagingErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntities();
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
    public EntityDetail addEntity(String               userId,
                                  String               entityTypeGUID,
                                  InstanceProperties   initialProperties,
                                  List<Classification> initialClassifications,
                                  InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             TypeErrorException,
                                                                             PropertyErrorException,
                                                                             ClassificationErrorException,
                                                                             StatusNotSupportedException,
                                                                             FunctionNotSupportedException,
                                                                             UserNotAuthorizedException
    {
        final String methodName  = "addEntity";
        final String operationSpecificURL = "instances/entity";

        EntityCreateRequest requestBody = new EntityCreateRequest();

        requestBody.setEntityTypeGUID(entityTypeGUID);
        requestBody.setInitialClassifications(initialClassifications);
        requestBody.setInitialProperties(initialProperties);
        requestBody.setInitialStatus(initialStatus);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowStatusNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail addExternalEntity(String               userId,
                                          String               entityTypeGUID,
                                          String               externalSourceGUID,
                                          String               externalSourceName,
                                          InstanceProperties   initialProperties,
                                          List<Classification> initialClassifications,
                                          InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     TypeErrorException,
                                                                                     PropertyErrorException,
                                                                                     ClassificationErrorException,
                                                                                     StatusNotSupportedException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String  methodName = "addExternalEntity";

        final String operationSpecificURL = "instances/entity/external";

        EntityCreateRequest requestBody = new EntityCreateRequest();

        requestBody.setEntityTypeGUID(entityTypeGUID);
        requestBody.setMetadataCollectionId(externalSourceGUID);
        requestBody.setMetadataCollectionName(externalSourceName);
        requestBody.setInitialClassifications(initialClassifications);
        requestBody.setInitialProperties(initialProperties);
        requestBody.setInitialStatus(initialStatus);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowStatusNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public void addEntityProxy(String      userId,
                               EntityProxy entityProxy) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               FunctionNotSupportedException,
                                                               UserNotAuthorizedException
    {
        final String methodName  = "addEntityProxy";
        final String operationSpecificURL = "instances/entity-proxy";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            entityProxy,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public EntityDetail updateEntityStatus(String         userId,
                                           String         entityGUID,
                                           InstanceStatus newStatus) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            StatusNotSupportedException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException
    {
        final String methodName  = "updateEntityStatus";
        final String operationSpecificURL = "instances/entity/{1}/status";

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            newStatus,
                                                                            userId,
                                                                            entityGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowStatusNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail updateEntityProperties(String             userId,
                                               String             entityGUID,
                                               InstanceProperties properties) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     EntityNotKnownException,
                                                                                     PropertyErrorException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName  = "updateEntityProperties";
        final String operationSpecificURL = "instances/entity/{1}/properties";

        InstancePropertiesRequest requestBody = new InstancePropertiesRequest();
        requestBody.setInstanceProperties(properties);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail undoEntityUpdate(String  userId,
                                         String  entityGUID) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    EntityNotKnownException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String methodName  = "undoEntityUpdate";
        final String operationSpecificURL = "instances/entity/{1}/previous";

        EntityDetailResponse restResult = this.callEntityDetailGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           entityGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph, but it is no longer returned
     * on queries.  All relationships to the entity are also soft-deleted and will no longer be usable.
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the call to delete().
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use.
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
    public EntityDetail deleteEntity(String userId,
                                     String typeDefGUID,
                                     String typeDefName,
                                     String obsoleteEntityGUID) throws InvalidParameterException,
                                                                       RepositoryErrorException,
                                                                       EntityNotKnownException,
                                                                       FunctionNotSupportedException,
                                                                       UserNotAuthorizedException
    {
        final String methodName  = "deleteEntity";
        final String operationSpecificURL = "instances/entity/{1}/delete";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            obsoleteEntityGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
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
        final String operationSpecificURL = "instances/entity/{1}/purge";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            deletedEntityGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowEntityNotDeletedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          EntityNotKnownException,
                                                                          EntityNotDeletedException,
                                                                          FunctionNotSupportedException,
                                                                          UserNotAuthorizedException
    {
        final String methodName  = "restoreEntity";
        final String operationSpecificURL = "instances/entity/{1}/restore";

        EntityDetailResponse restResult = this.callEntityDetailGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           deletedEntityGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowEntityNotDeletedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail classifyEntity(String             userId,
                                       String             entityGUID,
                                       String             classificationName,
                                       InstanceProperties classificationProperties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName  = "classifyEntity";
        final String operationSpecificURL = "instances/entity/{1}/classification/{2}";

        InstancePropertiesRequest requestBody = new InstancePropertiesRequest();
        requestBody.setInstanceProperties(classificationProperties);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID,
                                                                            classificationName);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail classifyEntity(String               userId,
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
        final String operationSpecificURL = "instances/entity/{1}/classification/{2}/detailed";

        ClassificationRequest requestBody = new ClassificationRequest();
        requestBody.setMetadataCollectionId(externalSourceGUID);
        requestBody.setMetadataCollectionName(externalSourceName);
        requestBody.setClassificationOrigin(classificationOrigin);
        requestBody.setClassificationOriginGUID(classificationOriginGUID);
        requestBody.setClassificationProperties(classificationProperties);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID,
                                                                            classificationName);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
        final String operationSpecificURL = "instances/entity/{1}/classification/{2}/delete";

        OMRSAPIRequest requestBody = new OMRSAPIRequest();

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID,
                                                                            classificationName);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public EntityDetail updateEntityClassification(String             userId,
                                                   String             entityGUID,
                                                   String             classificationName,
                                                   InstanceProperties properties) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         EntityNotKnownException,
                                                                                         ClassificationErrorException,
                                                                                         PropertyErrorException,
                                                                                         FunctionNotSupportedException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName  = "updateEntityClassification";
        final String operationSpecificURL = "instances/entity/{1}/classification/{2}/properties";

        InstancePropertiesRequest requestBody = new InstancePropertiesRequest();
        requestBody.setInstanceProperties(properties);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID,
                                                                            classificationName);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
    public Relationship addRelationship(String             userId,
                                        String             relationshipTypeGUID,
                                        InstanceProperties initialProperties,
                                        String             entityOneGUID,
                                        String             entityTwoGUID,
                                        InstanceStatus     initialStatus) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 TypeErrorException,
                                                                                 PropertyErrorException,
                                                                                 EntityNotKnownException,
                                                                                 StatusNotSupportedException,
                                                                                 FunctionNotSupportedException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName  = "addRelationship";
        final String operationSpecificURL = "instances/relationship";

        RelationshipCreateRequest request = new RelationshipCreateRequest();
        request.setRelationshipTypeGUID(relationshipTypeGUID);
        request.setInitialProperties(initialProperties);
        request.setEntityOneGUID(entityOneGUID);
        request.setEntityTwoGUID(entityTwoGUID);
        request.setInitialStatus(initialStatus);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            request,
                                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowStatusNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public Relationship addExternalRelationship(String             userId,
                                                String             relationshipTypeGUID,
                                                String             externalSourceGUID,
                                                String             externalSourceName,
                                                InstanceProperties initialProperties,
                                                String             entityOneGUID,
                                                String             entityTwoGUID,
                                                InstanceStatus     initialStatus) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         TypeErrorException,
                                                                                         PropertyErrorException,
                                                                                         EntityNotKnownException,
                                                                                         StatusNotSupportedException,
                                                                                         UserNotAuthorizedException,
                                                                                         FunctionNotSupportedException
    {
        final String  methodName = "addExternalRelationship";

        final String operationSpecificURL = "instances/relationship/external";

        RelationshipCreateRequest request = new RelationshipCreateRequest();
        request.setRelationshipTypeGUID(relationshipTypeGUID);
        request.setMetadataCollectionId(externalSourceGUID);
        request.setMetadataCollectionName(externalSourceName);
        request.setInitialProperties(initialProperties);
        request.setEntityOneGUID(entityOneGUID);
        request.setEntityTwoGUID(entityTwoGUID);
        request.setInitialStatus(initialStatus);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            request,
                                                                            userId);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowStatusNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public Relationship updateRelationshipStatus(String         userId,
                                                 String         relationshipGUID,
                                                 InstanceStatus newStatus) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  RelationshipNotKnownException,
                                                                                  StatusNotSupportedException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName  = "updateRelationshipStatus";
        final String operationSpecificURL = "instances/relationship/{1}/status";

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            newStatus,
                                                                            userId,
                                                                            relationshipGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowStatusNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);

        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public Relationship updateRelationshipProperties(String             userId,
                                                     String             relationshipGUID,
                                                     InstanceProperties properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           RelationshipNotKnownException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName  = "updateRelationshipProperties";
        final String operationSpecificURL = "instances/relationship/{1}/properties";

        InstancePropertiesRequest requestBody = new InstancePropertiesRequest();
        requestBody.setInstanceProperties(properties);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            relationshipGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public Relationship undoRelationshipUpdate(String  userId,
                                               String  relationshipGUID) throws InvalidParameterException,
                                                                                RepositoryErrorException,
                                                                                RelationshipNotKnownException,
                                                                                FunctionNotSupportedException,
                                                                                UserNotAuthorizedException
    {
        final String methodName  = "undoRelationshipUpdate";
        final String operationSpecificURL = "instances/relationship/{1}/previous";

        RelationshipResponse restResult = this.callRelationshipGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           relationshipGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
        final String operationSpecificURL = "instances/relationship/{1}/delete";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            obsoleteRelationshipGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
        final String operationSpecificURL = "instances/relationship/{1}/purge";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            deletedRelationshipGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowRelationshipNotDeletedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      RelationshipNotDeletedException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName  = "restoreRelationship";
        final String operationSpecificURL = "instances/relationship/{1}/restore";

        RelationshipResponse restResult = this.callRelationshipGetRESTCall(methodName,
                                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                           userId,
                                                                           deletedRelationshipGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowRelationshipNotDeletedException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
        final String operationSpecificURL = "instances/entity/{1}/identity?newEntityGUID={2}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID,
                                                                            newEntityGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
        final String operationSpecificURL = "instances/entity/{1}/type";

        TypeDefChangeRequest requestBody = new TypeDefChangeRequest();
        requestBody.setCurrentTypeDef(currentTypeDefSummary);
        requestBody.setNewTypeDef(newTypeDefSummary);

        EntityDetailResponse restResult = this.callEntityDetailPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            entityGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowClassificationErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
        final String operationSpecificURL1 = "instances/entity/{1}/home/{2}?newHomeMetadataCollectionId={3}";
        final String operationSpecificURL2 = "instances/entity/{1}/home/{2}?newHomeMetadataCollectionId={3}&newHomeMetadataCollectionName={4}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        EntityDetailResponse restResult;

        if (newHomeMetadataCollectionName == null)
        {
            restResult = this.callEntityDetailPostRESTCall(methodName,
                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL1,
                                                           requestBody,
                                                           userId,
                                                           entityGUID,
                                                           homeMetadataCollectionId,
                                                           newHomeMetadataCollectionId);
        }
        else
        {
            restResult = this.callEntityDetailPostRESTCall(methodName,
                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL2,
                                                           requestBody,
                                                           userId,
                                                           entityGUID,
                                                           homeMetadataCollectionId,
                                                           newHomeMetadataCollectionId,
                                                           newHomeMetadataCollectionName);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getEntity();
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
        final String operationSpecificURL = "instances/relationship/{1}/identity?newRelationshipGUID={2}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            relationshipGUID,
                                                                            newRelationshipGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
        final String operationSpecificURL = "instances/relationship/{1}/type";

        TypeDefChangeRequest requestBody = new TypeDefChangeRequest();
        requestBody.setCurrentTypeDef(currentTypeDefSummary);
        requestBody.setNewTypeDef(newTypeDefSummary);

        RelationshipResponse restResult = this.callRelationshipPostRESTCall(methodName,
                                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                            requestBody,
                                                                            userId,
                                                                            relationshipGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
        final String operationSpecificURL1 = "instances/relationship/{1}/home/{2}?newHomeMetadataCollectionId={3}";
        final String operationSpecificURL2 = "instances/relationship/{1}/home/{2}?newHomeMetadataCollectionId={3}&newHomeMetadataCollectionName={4}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        RelationshipResponse restResult;

        if (newHomeMetadataCollectionName == null)
        {
            restResult = this.callRelationshipPostRESTCall(methodName,
                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL1,
                                                           requestBody,
                                                           userId,
                                                           relationshipGUID,
                                                           homeMetadataCollectionId,
                                                           newHomeMetadataCollectionId);
        }
        else
        {
            restResult = this.callRelationshipPostRESTCall(methodName,
                                                           restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL2,
                                                           requestBody,
                                                           userId,
                                                           relationshipGUID,
                                                           homeMetadataCollectionId,
                                                           newHomeMetadataCollectionId,
                                                           newHomeMetadataCollectionName);
        }

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getRelationship();
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
    public void saveEntityReferenceCopy(String       userId,
                                        EntityDetail entity) throws InvalidParameterException,
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
        final String operationSpecificURL = "instances/entities/reference-copy";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            entity,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowHomeEntityException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 EntityNotKnownException,
                                                                                 UserNotAuthorizedException,
                                                                                 FunctionNotSupportedException
    {
        final String methodName = "getHomeClassifications";
        final String operationSpecificURL = "instances/entity/{1}/home-classifications";

        ClassificationListResponse restResult = this.callClassificationListGetRESTCall(methodName,
                                                                                       restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                       userId,
                                                                                       entityGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getClassifications();
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
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID,
                                                       Date   asOfTime) throws InvalidParameterException,
                                                                               RepositoryErrorException,
                                                                               EntityNotKnownException,
                                                                               UserNotAuthorizedException,
                                                                               FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications (with history)";
        final String operationSpecificURL = "instances/entity/{1}/home-classifications/history";

        HistoryRequest requestBody = new HistoryRequest();
        requestBody.setAsOfTime(asOfTime);
        ClassificationListResponse restResult = this.callClassificationListPostRESTCall(methodName,
                                                                                        restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                                                        requestBody,
                                                                                        userId,
                                                                                        entityGUID);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);

        return restResult.getClassifications();
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.  It is also an opportunity to remove or
     * soft delete relationships attached to the entity.
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
    public  void deleteEntityReferenceCopy(String       userId,
                                           EntityDetail entity) throws InvalidParameterException,
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
        final String operationSpecificURL = "instances/entities/reference-copy/delete";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            entity,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowHomeEntityException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.  It is also an opportunity to remove
     * relationships attached to the entity.
     *
     * This method is called when a remote repository calls the variant of purgeEntity that
     * passes the EntityDetail object.  This is typically used if purge is called without a previous soft-delete.
     * However, it may also be used to purge after a soft-delete.
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
    public  void purgeEntityReferenceCopy(String       userId,
                                          EntityDetail entity) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      TypeErrorException,
                                                                      PropertyErrorException,
                                                                      HomeEntityException,
                                                                      EntityConflictException,
                                                                      InvalidEntityException,
                                                                      FunctionNotSupportedException,
                                                                      UserNotAuthorizedException
    {
        final String methodName  = "purgeEntityReferenceCopy";
        final String operationSpecificURL = "instances/entities/reference-copy/purge";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            entity,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowHomeEntityException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/entities/reference-copy/{1}/purge?homeMetadataCollectionId={2}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            entityGUID,
                                                            homeMetadataCollectionId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowHomeEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/entities/reference-copy/{1}/refresh?homeMetadataCollectionId={2}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            entityGUID,
                                                            homeMetadataCollectionId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowHomeEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/entities/classifications/reference-copy";

        ClassificationWithEntityRequest requestBody = new ClassificationWithEntityRequest();
        requestBody.setEntity(entity);
        requestBody.setClassification(classification);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/entities/classifications/reference-copy";

        ClassificationWithEntityRequest requestBody = new ClassificationWithEntityRequest();
        requestBody.setEntityProxy(entity);
        requestBody.setClassification(classification);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/entities/classifications/reference-copy/purge";

        ClassificationWithEntityRequest requestBody = new ClassificationWithEntityRequest();
        requestBody.setEntity(entity);
        requestBody.setClassification(classification);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public void saveRelationshipReferenceCopy(String       userId,
                                              Relationship relationship) throws InvalidParameterException,
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
        final String operationSpecificURL = "instances/relationships/reference-copy";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            relationship,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowHomeRelationshipException(methodName, restResult);
        this.detectAndThrowRelationshipConflictException(methodName, restResult);
        this.detectAndThrowInvalidRelationshipException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to purge.
     *
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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  void deleteRelationshipReferenceCopy(String       userId,
                                                 Relationship relationship) throws InvalidParameterException,
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
        final String operationSpecificURL = "instances/relationships/reference-copy/delete";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            relationship,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowHomeRelationshipException(methodName, restResult);
        this.detectAndThrowRelationshipConflictException(methodName, restResult);
        this.detectAndThrowInvalidRelationshipException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
    }


    /**
     * This method is called when a remote repository calls the variant of purgeRelationship that
     * passes the relationship object.  This is typically used if purge is called without a previous soft-delete.
     * However, it may also be used to purge after a soft-delete.
     *
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship the purged relationship.
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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  void purgeRelationshipReferenceCopy(String       userId,
                                                Relationship relationship) throws InvalidParameterException,
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
        final String methodName  = "purgeRelationshipReferenceCopy";
        final String operationSpecificURL = "instances/relationships/reference-copy/purge";

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            relationship,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowHomeRelationshipException(methodName, restResult);
        this.detectAndThrowRelationshipConflictException(methodName, restResult);
        this.detectAndThrowInvalidRelationshipException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/relationships/reference-copy/{1}/purge?homeMetadataCollectionId={2}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            relationshipGUID,
                                                            homeMetadataCollectionId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowHomeRelationshipException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
        final String operationSpecificURL = "instances/relationships/reference-copy/{1}/refresh?homeMetadataCollectionId={2}";

        TypeDefValidationForRequest requestBody = new TypeDefValidationForRequest();
        requestBody.setTypeDefGUID(typeDefGUID);
        requestBody.setTypeDefName(typeDefName);

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId,
                                                            relationshipGUID,
                                                            homeMetadataCollectionId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRelationshipNotKnownException(methodName, restResult);
        this.detectAndThrowHomeRelationshipException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
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
    public void saveInstanceReferenceCopies(String        userId,
                                            InstanceGraph instances) throws InvalidParameterException,
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
        final String operationSpecificURL = "instances";

        InstanceGraphRequest requestBody = new InstanceGraphRequest();
        if (instances != null)
        {
            requestBody.setEntityElementList(instances.getEntities());
            requestBody.setRelationshipElementList(instances.getRelationships());
        }

        VoidResponse restResult = this.callVoidPostRESTCall(methodName,
                                                            restURLRoot + rootServiceNameInURL + userIdInURL + serviceURLMarker + operationSpecificURL,
                                                            requestBody,
                                                            userId);

        this.detectAndThrowFunctionNotSupportedException(methodName, restResult);
        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowTypeErrorException(methodName, restResult);
        this.detectAndThrowEntityNotKnownException(methodName, restResult);
        this.detectAndThrowEntityConflictException(methodName, restResult);
        this.detectAndThrowInvalidEntityException(methodName, restResult);
        this.detectAndThrowRelationshipConflictException(methodName, restResult);
        this.detectAndThrowInvalidRelationshipException(methodName, restResult);
        this.detectAndThrowPropertyErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
    }


    /*
     * ===============================
     * REST Client
     * ===============================
     */


    /**
     * Create a REST client to call the remote connector.
     *
     * @param serverName name of the remote server.
     * @param serverPlatformURLRoot name of the URL root for the server.
     * @param userId userId of this server.
     * @param password password for this server.
     * @return REST Client connector
     * @throws InvalidParameterException an unexpected exception - internal logic error as the parameters should have
     * all been checked before this call.
     */
    private RESTClientConnector getRESTClientConnector(String       serverName,
                                                       String       serverPlatformURLRoot,
                                                       String       userId,
                                                       String       password) throws InvalidParameterException
    {
        final String methodName = "getRESTClientConnector";

        RESTClientFactory clientFactory;

        if ((localServerUserId != null) && (localServerPassword != null))
        {
            clientFactory = new RESTClientFactory(serverName,
                                                  serverPlatformURLRoot,
                                                  userId,
                                                  password);
        }
        else
        {
            clientFactory = new RESTClientFactory(serverName,
                                                  serverPlatformURLRoot);
        }

        try
        {
            return clientFactory.getClientConnector();
        }
        catch (Exception error)
        {
           throw new InvalidParameterException(OMRSErrorCode.NO_REST_CLIENT.getMessageDefinition(serverName, error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error,
                                               "client");
        }
    }


    /* =====================
     * Issuing REST Calls
     * =====================
     */


    /**
     * Issue a POST REST call that returns a AttributeTypeDefListResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body for REST Call
     * @param params  a list of parameters that are slotted into the url template
     * @return AttributeTypeDefListResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private AttributeTypeDefListResponse callAttributeTypeDefListPostRESTCall(String    methodName,
                                                                              String    operationSpecificURL,
                                                                              Object    requestBody,
                                                                              Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     AttributeTypeDefListResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a AttributeTypeDefResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return AttributeTypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private AttributeTypeDefResponse callAttributeTypeDefGetRESTCall(String    methodName,
                                                                     String    operationSpecificURL,
                                                                     Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    AttributeTypeDefResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a POST REST call that returns a AttributeTypeDefResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return AttributeTypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private AttributeTypeDefResponse callAttributeTypeDefPostRESTCall(String    methodName,
                                                                      String    operationSpecificURL,
                                                                      Object    requestBody,
                                                                      Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     AttributeTypeDefResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a POST REST call that returns a BooleanResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return BooleanResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private BooleanResponse callBooleanPostRESTCall(String    methodName,
                                                    String    operationSpecificURL,
                                                    Object    requestBody,
                                                    Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     BooleanResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a EntityDetailResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return EntityDetailResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private EntityDetailResponse callEntityDetailGetRESTCall(String    methodName,
                                                             String    operationSpecificURL,
                                                             Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    EntityDetailResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a POST REST call that returns a EntityDetailResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return EntityDetailResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private EntityDetailResponse callEntityDetailPostRESTCall(String    methodName,
                                                              String    operationSpecificURL,
                                                              Object    requestBody,
                                                              Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     EntityDetailResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a ClassificationListResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return EntityDetailResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private ClassificationListResponse callClassificationListGetRESTCall(String    methodName,
                                                                         String    operationSpecificURL,
                                                                         Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    ClassificationListResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a POST REST call that returns a ClassificationListResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return EntityDetailResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private ClassificationListResponse callClassificationListPostRESTCall(String    methodName,
                                                                          String    operationSpecificURL,
                                                                          Object    requestBody,
                                                                          Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     ClassificationListResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a EntitySummaryResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return EntitySummaryResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private EntitySummaryResponse callEntitySummaryGetRESTCall(String    methodName,
                                                               String    operationSpecificURL,
                                                               Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    EntitySummaryResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a GET REST call that returns a RelationshipResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return RelationshipResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private RelationshipResponse callRelationshipGetRESTCall(String    methodName,
                                                             String    operationSpecificURL,
                                                             Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    RelationshipResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a POST REST call that returns a EntityListResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return EntityListResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private EntityListResponse callEntityListPostRESTCall(String    methodName,
                                                          String    operationSpecificURL,
                                                          Object    requestBody,
                                                          Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     EntityListResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }



    /**
     * Issue a POST REST call that returns a RelationshipListResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return RelationshipListResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private RelationshipListResponse callRelationshipListPostRESTCall(String    methodName,
                                                                      String    operationSpecificURL,
                                                                      Object    requestBody,
                                                                      Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     RelationshipListResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a POST REST call that returns a RelationshipResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return RelationshipResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private RelationshipResponse callRelationshipPostRESTCall(String    methodName,
                                                              String    operationSpecificURL,
                                                              Object    requestBody,
                                                              Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     RelationshipResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }

    /**
     * Issue a GET REST call that returns a InstanceGraphResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return InstanceGraphResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private InstanceGraphResponse callInstanceGraphPostRESTCall(String    methodName,
                                                                String    operationSpecificURL,
                                                                Object    requestBody,
                                                                Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     InstanceGraphResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a TypeDefGalleryResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return TypeDefGalleryResponseObject
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private TypeDefGalleryResponse callTypeDefGalleryGetRESTCall(String    methodName,
                                                                 String    operationSpecificURL,
                                                                 Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    TypeDefGalleryResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a GET REST call that returns a TypeDefListResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefListResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private TypeDefListResponse callTypeDefListGetRESTCall(String    methodName,
                                                           String    operationSpecificURL,
                                                           Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    TypeDefListResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a POST REST call that returns a TypeDefListResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefListResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private TypeDefListResponse callTypeDefListPostRESTCall(String    methodName,
                                                            String    operationSpecificURL,
                                                            Object    requestBody,
                                                            Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     TypeDefListResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a TypeDefResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private TypeDefResponse callTypeDefGetRESTCall(String    methodName,
                                                   String    operationSpecificURL,
                                                   Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    TypeDefResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a POST REST call that returns a TypeDefResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return TypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private TypeDefResponse callTypeDefPostRESTCall(String    methodName,
                                                    String    operationSpecificURL,
                                                    Object    requestBody,
                                                    Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     TypeDefResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return VoidResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private VoidResponse callVoidPostRESTCall(String    methodName,
                                              String    operationSpecificURL,
                                              Object    requestBody,
                                              Object... params) throws RepositoryErrorException
    {
        return this.callPostRESTCall(methodName,
                                     VoidResponse.class,
                                     operationSpecificURL,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns the requested object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called
     * @param returnClass class name of response object
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @return TypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private <T> T callGetRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    operationSpecificURL) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName, returnClass, operationSpecificURL, (Object[])null);
    }


    /**
     * Issue a GET REST call that returns a TypeDefResponse object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called
     * @param returnClass class name of response object
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private <T> T callGetRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    operationSpecificURL,
                                  Object... params) throws RepositoryErrorException
    {
        try
        {
            return restClient.callGetRESTCall(methodName,
                                              returnClass,
                                              operationSpecificURL,
                                              params);
        }
        catch (Exception error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                             repositoryName,
                                                                                                             error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }

    /**
     * Issue a POST REST call that returns the requested type of object.
     *
     * @param <T> class name
     * @param methodName name of the method being called
     * @param returnClass class name of response object
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param request request body object
     * @param params a list of parameters that are slotted into the url template
     * @return VoidResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private <T> T callPostRESTCall(String    methodName,
                                   Class<T>  returnClass,
                                   String    operationSpecificURL,
                                   Object    request,
                                   Object... params) throws RepositoryErrorException
    {
        try
        {
            return restClient.callPostRESTCall(methodName,
                                               returnClass,
                                               operationSpecificURL,
                                               request,
                                               params);
        }
        catch (Exception error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                             repositoryName,
                                                                                                             error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /*
     * ============================================
     * Extracting complex types from REST results
     * ============================================
     */


    /**
     * Assemble an InstanceGraph from an InstanceGraphResponse.
     *
     * @param restResult this is the response from the rest call
     * @return equivalent InstanceGraph
     */
    private InstanceGraph getInstanceGraphFromRESTResult(InstanceGraphResponse restResult)
    {
        InstanceGraph instanceGraph = new InstanceGraph();

        instanceGraph.setEntities(restResult.getEntityElementList());
        instanceGraph.setRelationships(restResult.getRelationshipElementList());

        return instanceGraph;
    }


    /**
     * Assemble a TypeDefGallery from a TypeDefGalleryResponse.
     *
     * @param restResult this is the response from the rest call
     * @return equivalent TypeDefGallery
     */
    private TypeDefGallery getTypeDefGalleryFromRESTResult(TypeDefGalleryResponse restResult)
    {
        TypeDefGallery typeDefGallery = new TypeDefGallery();

        typeDefGallery.setAttributeTypeDefs(restResult.getAttributeTypeDefs());
        typeDefGallery.setTypeDefs(restResult.getTypeDefs());

        return typeDefGallery;
    }


    /*
     * ===============================
     * Handling exceptions
     * ===============================
     */


    /**
     * Throw an ClassificationErrorException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws ClassificationErrorException encoded exception from the server
     */
    private void detectAndThrowClassificationErrorException(String          methodName,
                                                            OMRSAPIResponse restResult) throws ClassificationErrorException
    {
        final String   exceptionClassName = ClassificationErrorException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new ClassificationErrorException(restResult.getRelatedHTTPCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   restResult.getExceptionErrorMessage(),
                                                   restResult.getExceptionErrorMessageId(),
                                                   restResult.getExceptionErrorMessageParameters(),
                                                   restResult.getExceptionSystemAction(),
                                                   restResult.getExceptionUserAction(),
                                                   restResult.getExceptionCausedBy(),
                                                   restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an EntityConflictException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws EntityConflictException encoded exception from the server
     */
    private void detectAndThrowEntityConflictException(String          methodName,
                                                       OMRSAPIResponse restResult) throws EntityConflictException
    {
        final String   exceptionClassName = EntityConflictException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new EntityConflictException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionErrorMessageId(),
                                              restResult.getExceptionErrorMessageParameters(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction(),
                                              restResult.getExceptionCausedBy(),
                                              restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an EntityNotDeletedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws EntityNotDeletedException encoded exception from the server
     */
    private void detectAndThrowEntityNotDeletedException(String          methodName,
                                                         OMRSAPIResponse restResult) throws EntityNotDeletedException
    {
        final String   exceptionClassName = EntityNotDeletedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new EntityNotDeletedException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionErrorMessageId(),
                                                restResult.getExceptionErrorMessageParameters(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                restResult.getExceptionCausedBy(),
                                                restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an EntityNotKnownException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws EntityNotKnownException encoded exception from the server
     */
    private void detectAndThrowEntityNotKnownException(String          methodName,
                                                       OMRSAPIResponse restResult) throws EntityNotKnownException
    {
        final String   exceptionClassName = EntityNotKnownException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new EntityNotKnownException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionErrorMessageId(),
                                              restResult.getExceptionErrorMessageParameters(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction(),
                                              restResult.getExceptionCausedBy(),
                                              restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an EntityProxyOnlyException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws EntityProxyOnlyException encoded exception from the server
     */
    private void detectAndThrowEntityProxyOnlyException(String          methodName,
                                                        OMRSAPIResponse restResult) throws EntityProxyOnlyException
    {
        final String   exceptionClassName = EntityProxyOnlyException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new EntityProxyOnlyException(restResult.getRelatedHTTPCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               restResult.getExceptionErrorMessage(),
                                               restResult.getExceptionErrorMessageId(),
                                               restResult.getExceptionErrorMessageParameters(),
                                               restResult.getExceptionSystemAction(),
                                               restResult.getExceptionUserAction(),
                                               restResult.getExceptionCausedBy(),
                                               restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an FunctionNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws FunctionNotSupportedException encoded exception from the server
     */
    private void detectAndThrowFunctionNotSupportedException(String          methodName,
                                                             OMRSAPIResponse restResult) throws FunctionNotSupportedException
    {
        final String   exceptionClassName = FunctionNotSupportedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new FunctionNotSupportedException(restResult.getRelatedHTTPCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    restResult.getExceptionErrorMessage(),
                                                    restResult.getExceptionErrorMessageId(),
                                                    restResult.getExceptionErrorMessageParameters(),
                                                    restResult.getExceptionSystemAction(),
                                                    restResult.getExceptionUserAction(),
                                                    restResult.getExceptionCausedBy(),
                                                    restResult.getExceptionProperties());
        }
    }



    /**
     * Throw a HomeEntityException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws HomeEntityException encoded exception from the server
     */
    private void detectAndThrowHomeEntityException(String          methodName,
                                                   OMRSAPIResponse restResult) throws HomeEntityException
    {
        final String   exceptionClassName = HomeEntityException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new HomeEntityException(restResult.getRelatedHTTPCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          restResult.getExceptionErrorMessage(),
                                          restResult.getExceptionErrorMessageId(),
                                          restResult.getExceptionErrorMessageParameters(),
                                          restResult.getExceptionSystemAction(),
                                          restResult.getExceptionUserAction(),
                                          restResult.getExceptionCausedBy(),
                                          restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a HomeRelationshipException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws HomeRelationshipException encoded exception from the server
     */
    private void detectAndThrowHomeRelationshipException(String          methodName,
                                                         OMRSAPIResponse restResult) throws HomeRelationshipException
    {
        final String   exceptionClassName = HomeRelationshipException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new HomeRelationshipException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionErrorMessageId(),
                                                restResult.getExceptionErrorMessageParameters(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                restResult.getExceptionCausedBy(),
                                                restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an InvalidEntityException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws InvalidEntityException encoded exception from the server
     */
    private void detectAndThrowInvalidEntityException(String          methodName,
                                                      OMRSAPIResponse restResult) throws InvalidEntityException
    {
        final String   exceptionClassName = InvalidEntityException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new InvalidEntityException(restResult.getRelatedHTTPCode(),
                                             this.getClass().getName(),
                                             methodName,
                                             restResult.getExceptionErrorMessage(),
                                             restResult.getExceptionErrorMessageId(),
                                             restResult.getExceptionErrorMessageParameters(),
                                             restResult.getExceptionSystemAction(),
                                             restResult.getExceptionUserAction(),
                                             restResult.getExceptionCausedBy(),
                                             restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws InvalidParameterException encoded exception from the server
     * @throws RepositoryErrorException invalid parameter is "serverName"
     */
    protected void detectAndThrowInvalidParameterException(String          methodName,
                                                           OMRSAPIResponse restResult) throws InvalidParameterException,
                                                                                              RepositoryErrorException
    {
        final String exceptionClassName = InvalidParameterException.class.getName();
        final String propertyName = "parameterName";
        final String serverName = "serverName";


        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String parameterName = null;

            Map<String, Object>   exceptionProperties = restResult.getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get(propertyName);

                if (nameObject != null)
                {
                    parameterName = (String)nameObject;
                }
            }

            if (serverName.equals(parameterName))
            {
                /*
                 * If the platform is up but the server is not running on the platform the response is
                 * InvalidParameterException because the serverName is invalid - so this is turned into
                 * a RepositoryErrorException as if the whole platform is missing.
                 */
                throw new RepositoryErrorException(restResult.getRelatedHTTPCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   restResult.getExceptionErrorMessage(),
                                                   restResult.getExceptionErrorMessageId(),
                                                   restResult.getExceptionErrorMessageParameters(),
                                                   restResult.getExceptionSystemAction(),
                                                   restResult.getExceptionUserAction(),
                                                   restResult.getExceptionCausedBy(),
                                                   restResult.getExceptionProperties());
            }
            else
            {
                throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    restResult.getExceptionErrorMessage(),
                                                    restResult.getExceptionErrorMessageId(),
                                                    restResult.getExceptionErrorMessageParameters(),
                                                    restResult.getExceptionSystemAction(),
                                                    restResult.getExceptionUserAction(),
                                                    restResult.getExceptionCausedBy(),
                                                    parameterName,
                                                    restResult.getExceptionProperties());
            }
        }
    }


    /**
     * Throw an InvalidRelationshipException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws InvalidRelationshipException encoded exception from the server
     */
    private void detectAndThrowInvalidRelationshipException(String          methodName,
                                                            OMRSAPIResponse restResult) throws InvalidRelationshipException
    {
        final String   exceptionClassName = InvalidRelationshipException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new InvalidRelationshipException(restResult.getRelatedHTTPCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   restResult.getExceptionErrorMessage(),
                                                   restResult.getExceptionErrorMessageId(),
                                                   restResult.getExceptionErrorMessageParameters(),
                                                   restResult.getExceptionSystemAction(),
                                                   restResult.getExceptionUserAction(),
                                                   restResult.getExceptionCausedBy(),
                                                   restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an InvalidTypeDefException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws InvalidTypeDefException encoded exception from the server
     */
    private void detectAndThrowInvalidTypeDefException(String          methodName,
                                                       OMRSAPIResponse restResult) throws InvalidTypeDefException
    {
        final String   exceptionClassName = InvalidTypeDefException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new InvalidTypeDefException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionErrorMessageId(),
                                              restResult.getExceptionErrorMessageParameters(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction(),
                                              restResult.getExceptionCausedBy(),
                                              restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a PagingErrorException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws PagingErrorException encoded exception from the server
     */
    private void detectAndThrowPagingErrorException(String          methodName,
                                                    OMRSAPIResponse restResult) throws PagingErrorException
    {
        final String   exceptionClassName = PagingErrorException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PagingErrorException(restResult.getRelatedHTTPCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           restResult.getExceptionErrorMessage(),
                                           restResult.getExceptionErrorMessageId(),
                                           restResult.getExceptionErrorMessageParameters(),
                                           restResult.getExceptionSystemAction(),
                                           restResult.getExceptionUserAction(),
                                           restResult.getExceptionCausedBy(),
                                           restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a PatchErrorException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws PatchErrorException encoded exception from the server
     */
    private void detectAndThrowPatchErrorException(String          methodName,
                                                   OMRSAPIResponse restResult) throws PatchErrorException
    {
        final String   exceptionClassName = PatchErrorException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PatchErrorException(restResult.getRelatedHTTPCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          restResult.getExceptionErrorMessage(),
                                          restResult.getExceptionErrorMessageId(),
                                          restResult.getExceptionErrorMessageParameters(),
                                          restResult.getExceptionSystemAction(),
                                          restResult.getExceptionUserAction(),
                                          restResult.getExceptionCausedBy(),
                                          restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a PropertyErrorException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws PropertyErrorException encoded exception from the server
     */
    private void detectAndThrowPropertyErrorException(String          methodName,
                                                      OMRSAPIResponse restResult) throws PropertyErrorException
    {
        final String   exceptionClassName = PropertyErrorException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PropertyErrorException(restResult.getRelatedHTTPCode(),
                                             this.getClass().getName(),
                                             methodName,
                                             restResult.getExceptionErrorMessage(),
                                             restResult.getExceptionErrorMessageId(),
                                             restResult.getExceptionErrorMessageParameters(),
                                             restResult.getExceptionSystemAction(),
                                             restResult.getExceptionUserAction(),
                                             restResult.getExceptionCausedBy(),
                                             restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a RelationshipConflictException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws RelationshipConflictException encoded exception from the server
     */
    private void detectAndThrowRelationshipConflictException(String          methodName,
                                                             OMRSAPIResponse restResult) throws RelationshipConflictException
    {
        final String   exceptionClassName = RelationshipConflictException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new RelationshipConflictException(restResult.getRelatedHTTPCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    restResult.getExceptionErrorMessage(),
                                                    restResult.getExceptionErrorMessageId(),
                                                    restResult.getExceptionErrorMessageParameters(),
                                                    restResult.getExceptionSystemAction(),
                                                    restResult.getExceptionUserAction(),
                                                    restResult.getExceptionCausedBy(),
                                                    restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a RelationshipNotDeletedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws RelationshipNotDeletedException encoded exception from the server
     */
    private void detectAndThrowRelationshipNotDeletedException(String          methodName,
                                                               OMRSAPIResponse restResult) throws RelationshipNotDeletedException
    {
        final String   exceptionClassName = RelationshipNotDeletedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new RelationshipNotDeletedException(restResult.getRelatedHTTPCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      restResult.getExceptionErrorMessage(),
                                                      restResult.getExceptionErrorMessageId(),
                                                      restResult.getExceptionErrorMessageParameters(),
                                                      restResult.getExceptionSystemAction(),
                                                      restResult.getExceptionUserAction(),
                                                      restResult.getExceptionCausedBy(),
                                                      restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a RelationshipNotKnownException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws RelationshipNotKnownException encoded exception from the server
     */
    private void detectAndThrowRelationshipNotKnownException(String          methodName,
                                                             OMRSAPIResponse restResult) throws RelationshipNotKnownException
    {
        final String   exceptionClassName = RelationshipNotKnownException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new RelationshipNotKnownException(restResult.getRelatedHTTPCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    restResult.getExceptionErrorMessage(),
                                                    restResult.getExceptionErrorMessageId(),
                                                    restResult.getExceptionErrorMessageParameters(),
                                                    restResult.getExceptionSystemAction(),
                                                    restResult.getExceptionUserAction(),
                                                    restResult.getExceptionCausedBy(),
                                                    restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a StatusNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws StatusNotSupportedException encoded exception from the server
     */
    private void detectAndThrowStatusNotSupportedException(String          methodName,
                                                           OMRSAPIResponse restResult) throws StatusNotSupportedException
    {
        final String   exceptionClassName = StatusNotSupportedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new StatusNotSupportedException(restResult.getRelatedHTTPCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  restResult.getExceptionErrorMessage(),
                                                  restResult.getExceptionErrorMessageId(),
                                                  restResult.getExceptionErrorMessageParameters(),
                                                  restResult.getExceptionSystemAction(),
                                                  restResult.getExceptionUserAction(),
                                                  restResult.getExceptionCausedBy(),
                                                  restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a TypeDefConflictException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws TypeDefConflictException encoded exception from the server
     */
    private void detectAndThrowTypeDefConflictException(String          methodName,
                                                        OMRSAPIResponse restResult) throws TypeDefConflictException
    {
        final String   exceptionClassName = TypeDefConflictException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new TypeDefConflictException(restResult.getRelatedHTTPCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               restResult.getExceptionErrorMessage(),
                                               restResult.getExceptionErrorMessageId(),
                                               restResult.getExceptionErrorMessageParameters(),
                                               restResult.getExceptionSystemAction(),
                                               restResult.getExceptionUserAction(),
                                               restResult.getExceptionCausedBy(),
                                               restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a TypeDefInUseException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws TypeDefInUseException encoded exception from the server
     */
    private void detectAndThrowTypeDefInUseException(String          methodName,
                                                     OMRSAPIResponse restResult) throws TypeDefInUseException
    {
        final String   exceptionClassName = TypeDefInUseException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new TypeDefInUseException(restResult.getRelatedHTTPCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            restResult.getExceptionErrorMessage(),
                                            restResult.getExceptionErrorMessageId(),
                                            restResult.getExceptionErrorMessageParameters(),
                                            restResult.getExceptionSystemAction(),
                                            restResult.getExceptionUserAction(),
                                            restResult.getExceptionCausedBy(),
                                            restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a TypeDefKnownException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws TypeDefKnownException encoded exception from the server
     */
    private void detectAndThrowTypeDefKnownException(String          methodName,
                                                     OMRSAPIResponse restResult) throws TypeDefKnownException
    {
        final String   exceptionClassName = TypeDefKnownException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new TypeDefKnownException(restResult.getRelatedHTTPCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            restResult.getExceptionErrorMessage(),
                                            restResult.getExceptionErrorMessageId(),
                                            restResult.getExceptionErrorMessageParameters(),
                                            restResult.getExceptionSystemAction(),
                                            restResult.getExceptionUserAction(),
                                            restResult.getExceptionCausedBy(),
                                            restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a TypeDefNotKnownException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws TypeDefNotKnownException encoded exception from the server
     */
    private void detectAndThrowTypeDefNotKnownException(String          methodName,
                                                        OMRSAPIResponse restResult) throws TypeDefNotKnownException
    {
        final String   exceptionClassName = TypeDefNotKnownException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new TypeDefNotKnownException(restResult.getRelatedHTTPCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               restResult.getExceptionErrorMessage(),
                                               restResult.getExceptionErrorMessageId(),
                                               restResult.getExceptionErrorMessageParameters(),
                                               restResult.getExceptionSystemAction(),
                                               restResult.getExceptionUserAction(),
                                               restResult.getExceptionCausedBy(),
                                               restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a TypeDefNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws TypeDefNotSupportedException encoded exception from the server
     */
    private void detectAndThrowTypeDefNotSupportedException(String          methodName,
                                                            OMRSAPIResponse restResult) throws TypeDefNotSupportedException
    {
        final String   exceptionClassName = TypeDefNotSupportedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new TypeDefNotSupportedException(restResult.getRelatedHTTPCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   restResult.getExceptionErrorMessage(),
                                                   restResult.getExceptionErrorMessageId(),
                                                   restResult.getExceptionErrorMessageParameters(),
                                                   restResult.getExceptionSystemAction(),
                                                   restResult.getExceptionUserAction(),
                                                   restResult.getExceptionCausedBy(),
                                                   restResult.getExceptionProperties());
        }
    }


    /**
     * Throw a TypeErrorException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws TypeErrorException encoded exception from the server
     */
    private void detectAndThrowTypeErrorException(String          methodName,
                                                  OMRSAPIResponse restResult) throws TypeErrorException
    {
        final String   exceptionClassName = TypeErrorException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new TypeErrorException(restResult.getRelatedHTTPCode(),
                                         this.getClass().getName(),
                                         methodName,
                                         restResult.getExceptionErrorMessage(),
                                         restResult.getExceptionErrorMessageId(),
                                         restResult.getExceptionErrorMessageParameters(),
                                         restResult.getExceptionSystemAction(),
                                         restResult.getExceptionUserAction(),
                                         restResult.getExceptionCausedBy(),
                                         restResult.getExceptionProperties());
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    protected void detectAndThrowUserNotAuthorizedException(String          methodName,
                                                            OMRSAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            if (restResult.getExceptionProperties() != null)
            {
                userId = (String)restResult.getExceptionProperties().get("userId");
            }
            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 restResult.getExceptionErrorMessage(),
                                                 restResult.getExceptionErrorMessageId(),
                                                 restResult.getExceptionErrorMessageParameters(),
                                                 restResult.getExceptionSystemAction(),
                                                 restResult.getExceptionUserAction(),
                                                 restResult.getExceptionCausedBy(),
                                                 userId,
                                                 restResult.getExceptionProperties());
        }
    }


    /**
     * This method should be the last "detect and throw" methods that is called since it converts all remaining
     * exceptions to RepositoryErrorExceptions.
     *
     * @param methodName name of method called
     * @param restResult response from the REST API call.
     * @throws RepositoryErrorException resulting exception if response includes an exception.
     */
    protected void detectAndThrowRepositoryErrorException(String          methodName,
                                                          OMRSAPIResponse restResult) throws RepositoryErrorException
    {
        if (restResult == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_RESPONSE_FROM_API.getMessageDefinition(methodName, repositoryName),
                                               this.getClass().getName(),
                                               methodName);
        }
        else if (restResult.getExceptionClassName() != null)
        {
            /*
             * All the other expected exceptions have been processed so default exception to RepositoryErrorException
             */
            throw new RepositoryErrorException(restResult.getRelatedHTTPCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               restResult.getExceptionErrorMessage(),
                                               restResult.getExceptionErrorMessageId(),
                                               restResult.getExceptionErrorMessageParameters(),
                                               restResult.getExceptionSystemAction(),
                                               restResult.getExceptionUserAction(),
                                               restResult.getExceptionCausedBy(),
                                               restResult.getExceptionProperties());
        }
    }
}