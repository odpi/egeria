/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.EndMatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstance;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMRSRepositoryRESTServices provides the server-side support for the OMRS Repository REST Services API.
 * It is a minimal wrapper around the OMRSRepositoryConnector for the local server's metadata collection.
 * If localRepositoryConnector is null when a REST call is received, the request is rejected.  Otherwise,
 * it uses the supplied server name to locate the appropriate metadata collection for its local
 * repository and pass it the requested call.
 * <p>
 * OMRSRepositoryRESTServices is itself wrapped by classes that provide REST annotations.  In the Egeria code base
 * OMRSRepositoryResource from the repository-services-spring module uses spring boot annotations.  Other servers
 * may use a different type of REST annotations and so the spring wrapper may be disregarded and replaced.
 * <p>
 * The REST services are based around the OMRSMetadataInstanceStore interface.
 * <p>
 *     OMRSMetadataInstanceStore is the common interface for working with the contents of a metadata repository.
 *     Within a metadata collection are the type definitions (TypeDefs) and metadata instances (Entities and
 *     Relationships).  OMRSMetadataCollectionBase provides empty implementation of the abstract methods of
 *     OMRSMetadataInstanceStore.
 * <p>
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
    private static final String                                serviceName     = CommonServicesDescription.REPOSITORY_SERVICES.getServiceName();
    private static final Logger                                log             = LoggerFactory.getLogger(OMRSRepositoryRESTServices.class);
    private static final OMRSRepositoryServicesInstanceHandler instanceHandler = new OMRSRepositoryServicesInstanceHandler(serviceName);
    private static final RESTCallLogger                        restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(OMRSRepositoryRESTServices.class),
                                                                                                    instanceHandler.getServiceName());

    private static final MessageFormatter messageFormatter = new MessageFormatter();


    /**
     * Set up the local repository connector that will service the local repository REST Calls.
     *
     * @param localServerName               name of this local server
     * @param masterAuditLog                top level audit Log destination
     * @param localRepositoryConnector      link to the local repository responsible for servicing the REST calls
     *                                      If localRepositoryConnector is null when a REST calls is received, the request
     *                                      is rejected
     * @param enterpriseRepositoryConnector link to the repository responsible for servicing the REST calls to the enterprise.
     * @param remoteEnterpriseTopicConnection connection object to pass to client to enable it to listen on enterprise topic events - may be null
     * @param metadataHighwayManager        manager of the cohort managers
     * @param localServerURL                URL of the local server
     * @param auditLog                      auditLog destination
     * @param maxPageSize                   max results to return on a single request.
     */
    public static void setServerRepositories(String                       localServerName,
                                             AuditLog                     masterAuditLog,
                                             LocalOMRSRepositoryConnector localRepositoryConnector,
                                             OMRSRepositoryConnector      enterpriseRepositoryConnector,
                                             Connection                   remoteEnterpriseTopicConnection,
                                             OMRSMetadataHighwayManager   metadataHighwayManager,
                                             String                       localServerURL,
                                             AuditLog                     auditLog,
                                             int                          maxPageSize)
    {
        new OMRSRepositoryServicesInstance(localServerName,
                                           masterAuditLog,
                                           localRepositoryConnector,
                                           enterpriseRepositoryConnector,
                                           remoteEnterpriseTopicConnection,
                                           metadataHighwayManager,
                                           localServerURL,
                                           serviceName,
                                           auditLog,
                                           maxPageSize);
    }


    /**
     * Prevent new incoming requests for a server.
     *
     * @param localServerName name of the server to stop
     */
    public static void stopInboundRESTCalls(String localServerName)
    {
        new OMRSRepositoryServicesInstanceHandler(serviceName).removeInstance(localServerName);
    }


    private final boolean localRepository;

    /**
     * Common constructor
     *
     * @param localRepository is this request for the local repository?
     */
    public OMRSRepositoryRESTServices(boolean localRepository)
    {
        this.localRepository = localRepository;
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
     * @param userId     calling user
     * @param requestBody options to attach to the request.
     * @return String metadata collection id.
     * or RepositoryErrorException a problem communicating with the metadata repository.
     */
    public MetadataCollectionIdResponse getMetadataCollectionId(String     serverName,
                                                                String     userId,
                                                                GetRequest requestBody)
    {
        final String methodName = "getMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        MetadataCollectionIdResponse response = new MetadataCollectionIdResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setMetadataCollectionId(metadataCollection.getMetadataCollectionId(userId));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

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
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param requestBody options to attach to the request.
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * InvalidParameterException the userId is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefGalleryResponse getAllTypes(String     serverName,
                                              String     userId,
                                              GetRequest requestBody)
    {
        final String methodName = "getAllTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefGalleryResponse response = new TypeDefGalleryResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            TypeDefGallery typeDefGallery = metadataCollection.getAllTypes(userId);
            if (typeDefGallery != null)
            {
                response.setAttributeTypeDefs(typeDefGallery.getAttributeTypeDefs());
                response.setTypeDefs(typeDefGallery.getTypeDefs());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Returns a list of type definitions that have the specified name.  Type names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an
     * arbitrary string of characters and ampersand for an arbitrary character.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param name       name of the TypeDefs to return (including wildcard characters).
     * @param requestBody options to attach to the request.
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation or
     * InvalidParameterException the name of the TypeDef is null.
     */
    public TypeDefGalleryResponse findTypesByName(String     serverName,
                                                  String     userId,
                                                  String     name,
                                                  GetRequest requestBody)
    {
        final String methodName = "findTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefGalleryResponse response = new TypeDefGalleryResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            TypeDefGallery typeDefGallery = metadataCollection.findTypesByName(userId, name);
            if (typeDefGallery != null)
            {
                response.setAttributeTypeDefs(typeDefGallery.getAttributeTypeDefs());
                response.setTypeDefs(typeDefGallery.getTypeDefs());
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param requestBody   find parameters used to limit the returned results.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse findTypeDefsByCategory(String                 serverName,
                                                      String                 userId,
                                                      TypeDefCategoryRequest requestBody)
    {
        final String methodName = "findTypeDefsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTypeDefs(metadataCollection.findTypeDefsByCategory(userId, requestBody.getCategory()));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param requestBody   find parameters used to limit the returned results.
     * @return AttributeTypeDefListResponse:
     * AttributeTypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public AttributeTypeDefListResponse findAttributeTypeDefsByCategory(String                          serverName,
                                                                        String                          userId,
                                                                        AttributeTypeDefCategoryRequest requestBody)
    {
        final String methodName = "findAttributeTypeDefsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        AttributeTypeDefListResponse response = new AttributeTypeDefListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setAttributeTypeDefs(metadataCollection.findAttributeTypeDefsByCategory(userId, requestBody.getCategory()));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param serverName    unique identifier for requested server.
     * @param userId        unique identifier for requesting user.
     * @param requestBody TypeDefProperties a list of property names.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the matchCriteria is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse findTypeDefsByProperty(String                   serverName,
                                                      String                   userId,
                                                      TypeDefPropertiesRequest requestBody)
    {
        final String methodName = "findTypeDefsByProperty";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTypeDefs(metadataCollection.findTypeDefsByProperty(userId, requestBody.getTypeDefProperties()));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param serverName   unique identifier for requested server.
     * @param userId       unique identifier for requesting user.
     * @param standard     name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier   identifier of the element in the standard null means any.
     * @param requestBody options to attach to the request.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse findTypesByExternalID(String     serverName,
                                                     String     userId,
                                                     String     standard,
                                                     String     organization,
                                                     String     identifier,
                                                     GetRequest requestBody)
    {
        final String methodName = "findTypesByExternalID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<TypeDef> typeDefs = metadataCollection.findTypesByExternalID(userId, standard, organization, identifier);
            response.setTypeDefs(typeDefs);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param serverName     unique identifier for requested server.
     * @param userId         unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @param requestBody options to attach to the request.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the searchCriteria is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse searchForTypeDefs(String     serverName,
                                                 String     userId,
                                                 String     searchCriteria,
                                                 GetRequest requestBody)
    {
        final String methodName = "searchForTypeDefs";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setTypeDefs(metadataCollection.searchForTypeDefs(userId, searchCriteria));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param guid       String unique id of the TypeDef.
     * @param requestBody options to attach to the request.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeDefNotKnownException The requested TypeDef is not known in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefResponse getTypeDefByGUID(String     serverName,
                                            String     userId,
                                            String     guid,
                                            GetRequest requestBody)
    {
        final String methodName = "getTypeDefByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefResponse response = new TypeDefResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setTypeDef(metadataCollection.getTypeDefByGUID(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param guid       String unique id of the TypeDef
     * @param requestBody options to attach to the request.
     * @return AttributeTypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeDefNotKnownException The requested TypeDef is not known in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public AttributeTypeDefResponse getAttributeTypeDefByGUID(String     serverName,
                                                              String     userId,
                                                              String     guid,
                                                              GetRequest requestBody)
    {
        final String methodName = "getAttributeTypeDefByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        AttributeTypeDefResponse response = new AttributeTypeDefResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setAttributeTypeDef(metadataCollection.getAttributeTypeDefByGUID(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param name       String name of the TypeDef.
     * @param requestBody options to attach to the request.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefResponse getTypeDefByName(String     serverName,
                                            String     userId,
                                            String     name,
                                            GetRequest requestBody)
    {
        final String methodName = "getTypeDefByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefResponse response = new TypeDefResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setTypeDef(metadataCollection.getTypeDefByName(userId, name));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      unique identifier for requesting user.
     * @param name        String name of the TypeDef.
     * @param requestBody options to attach to the request.
     * @return AttributeTypeDefResponse:
     * AttributeTypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public AttributeTypeDefResponse getAttributeTypeDefByName(String     serverName,
                                                              String     userId,
                                                              String     name,
                                                              GetRequest requestBody)
    {
        final String methodName = "getAttributeTypeDefByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        AttributeTypeDefResponse response = new AttributeTypeDefResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setAttributeTypeDef(metadataCollection.getAttributeTypeDefByName(userId, name));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Log the fact that types are being maintained though the API rather than through an open metadata archive.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param methodName calling method
     * @param typeName unique name of the type
     * @param typeGUID unique identifier of the type
     *
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws RepositoryErrorException the service name is not know - indicating a logic error
     */
    private void logDynamicTypeManagement(String serverName,
                                          String userId,
                                          String methodName,
                                          String typeName,
                                          String typeGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  RepositoryErrorException
    {
        OMRSRepositoryServicesInstance instance = instanceHandler.getInstance(userId, serverName, methodName);

        if (instance != null)
        {
            AuditLog auditLog = instance.getAuditLog();

            auditLog.logMessage(methodName,
                                OMRSAuditCode.AD_HOC_TYPE_DEFINITION.getMessageDefinition(typeName, typeGUID, serverName, userId, methodName));
        }
    }

    
    /**
     * Create a collection of related types.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody TypeDefGalleryResponse structure describing the new AttributeTypeDefs and TypeDefs.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the new TypeDef is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefKnownException the TypeDef is already stored in the repository or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  VoidResponse addTypeDefGallery(String                serverName,
                                           String                userId,
                                           TypeDefGalleryRequest requestBody)
    {
        final  String   methodName = "addTypeDefGallery";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.addTypeDefGallery(userId,
                                                     new TypeDefGallery(requestBody.getAttributeTypeDefs(),
                                                                        requestBody.getTypeDefs()));

                List<TypeDef> typeDefs = requestBody.getTypeDefs();
                if (typeDefs != null)
                {
                    for (TypeDef typeDef : typeDefs)
                    {
                        if (typeDef != null)
                        {
                            this.logDynamicTypeManagement(serverName, userId, methodName, typeDef.getName(), typeDef.getGUID());
                        }
                    }
                }

                List<AttributeTypeDef> attributeTypeDefs = requestBody.getAttributeTypeDefs();
                if (attributeTypeDefs != null)
                {
                    for (AttributeTypeDef attributeTypeDef : attributeTypeDefs)
                    {
                        if (attributeTypeDef != null)
                        {
                            this.logDynamicTypeManagement(serverName, userId, methodName, attributeTypeDef.getName(), attributeTypeDef.getGUID());
                        }
                    }
                }
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Create a definition of a new TypeDef.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody TypeDef structure describing the new TypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the new TypeDef is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefKnownException the TypeDef is already stored in the repository or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse addTypeDef(String         serverName,
                                   String         userId,
                                   TypeDefRequest requestBody)
    {
        final  String   methodName = "addTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.addTypeDef(userId, requestBody.getTypeDef());

                this.logDynamicTypeManagement(serverName,
                                              userId,
                                              methodName,
                                              requestBody.getTypeDef().getName(),
                                              requestBody.getTypeDef().getGUID());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody TypeDef structure describing the new TypeDef.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the new TypeDef is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefKnownException the TypeDef is already stored in the repository or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  VoidResponse addAttributeTypeDef(String                  serverName,
                                             String                  userId,
                                             AttributeTypeDefRequest requestBody)
    {
        final  String   methodName = "addAttributeTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.addAttributeTypeDef(userId, requestBody.getAttributeTypeDef());

                this.logDynamicTypeManagement(serverName,
                                              userId,
                                              methodName,
                                              requestBody.getAttributeTypeDef().getName(),
                                              requestBody.getAttributeTypeDef().getGUID());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }
    

    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody TypeDef structure describing the TypeDef to test.
     * @return BooleanResponse:
     * boolean true means the TypeDef matches the local definition false means the TypeDef is not known or
     * InvalidParameterException the TypeDef is null.
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * InvalidTypeDefException the new TypeDef has invalid contents.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public BooleanResponse verifyTypeDef(String         serverName,
                                         String         userId,
                                         TypeDefRequest requestBody)
    {
        final  String   methodName = "verifyTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setFlag(metadataCollection.verifyTypeDef(userId, requestBody.getTypeDef()));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody TypeDef structure describing the TypeDef to test.
     * @return BooleanResponse:
     * boolean true means the TypeDef matches the local definition false means the TypeDef is not known or
     * InvalidParameterException the TypeDef is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotSupportedException the repository is not able to support this TypeDef or
     * TypeDefConflictException the new TypeDef conflicts with an existing TypeDef or
     * InvalidTypeDefException the new TypeDef has invalid contents or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  BooleanResponse verifyAttributeTypeDef(String                  serverName,
                                                   String                  userId,
                                                   AttributeTypeDefRequest requestBody)
    {
        final  String   methodName = "verifyAttributeTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setFlag(metadataCollection.verifyAttributeTypeDef(userId, requestBody.getAttributeTypeDef()));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody TypeDef patch describing change to TypeDef.
     * @return TypeDefResponse:
     * updated TypeDef or
     * InvalidParameterException the TypeDefPatch is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * PatchErrorException the TypeDef can not be updated because the supplied patch is incompatible
     *                               with the stored TypeDef or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefResponse updateTypeDef(String              serverName,
                                         String              userId,
                                         TypeDefPatchRequest requestBody)
    {
        final  String   methodName = "updateTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefResponse response = new TypeDefResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTypeDef(metadataCollection.updateTypeDef(userId, requestBody.getTypeDefPatch()));

                this.logDynamicTypeManagement(serverName,
                                              userId,
                                              methodName,
                                              requestBody.getTypeDefPatch().getTypeDefName(),
                                              requestBody.getTypeDefPatch().getTypeDefGUID());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param serverName          unique identifier for requested server.
     * @param userId              unique identifier for requesting user.
     * @param obsoleteTypeDefGUID  unique identifier for the TypeDef.
     * @param requestBody         options to attach to the request.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the one of TypeDef identifiers is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * TypeDefInUseException the TypeDef can not be deleted because instances of this type appear in
     * the metadata collection.  These instances need to be purged before the
     * TypeDef can be deleted or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse deleteTypeDef(String     serverName,
                                      String     userId,
                                      String     obsoleteTypeDefGUID,
                                      TypeDefDeleteRequest requestBody)
    {
        final String methodName = "deleteTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.deleteTypeDef(userId, obsoleteTypeDefGUID, requestBody.getObsoleteTypeDefName());

                this.logDynamicTypeManagement(serverName, userId, methodName, requestBody.getObsoleteTypeDefName(), obsoleteTypeDefGUID);
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param serverName          unique identifier for requested server.
     * @param userId              unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param requestBody         options to attach to the request.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the one of AttributeTypeDef identifiers is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeDefNotKnownException the requested AttributeTypeDef is not found in the metadata collection.
     * TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in
     * the metadata collection.  These instances need to be purged before the
     * AttributeTypeDef can be deleted or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse deleteAttributeTypeDef(String     serverName,
                                               String     userId,
                                               String     obsoleteTypeDefGUID,
                                               TypeDefDeleteRequest requestBody)
    {
        final String methodName = "deleteAttributeTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.deleteAttributeTypeDef(userId, obsoleteTypeDefGUID, requestBody.getObsoleteTypeDefName());

                this.logDynamicTypeManagement(serverName, userId, methodName, requestBody.getObsoleteTypeDefName(), obsoleteTypeDefGUID);
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the TypeDef identified by the original guid/name is not found
     *                                    in the metadata collection or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  TypeDefResponse reIdentifyTypeDef(String                    serverName,
                                              String                    userId,
                                              String                    originalTypeDefGUID,
                                              TypeDefReIdentifyRequest requestParameters)
    {
        final  String   methodName = "reIdentifyTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestParameters);

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
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setTypeDef(metadataCollection.reIdentifyTypeDef(userId,
                                                                     originalTypeDefGUID,
                                                                     originalTypeDefName,
                                                                     newTypeDefGUID,
                                                                     newTypeDefName));

            this.logDynamicTypeManagement(serverName, userId, methodName, originalTypeDefName, originalTypeDefGUID);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * attributeTypeDef: new values for this AttributeTypeDef, including the new guid/name or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeDefNotKnownException the AttributeTypeDef identified by the original guid/name is not
     *                                    found in the metadata collection or
     * FunctionNotSupportedException the repository does not support this call or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  AttributeTypeDefResponse reIdentifyAttributeTypeDef(String                   serverName,
                                                                String                   userId,
                                                                String                   originalAttributeTypeDefGUID,
                                                                TypeDefReIdentifyRequest requestParameters)
    {
        final  String   methodName = "reIdentifyAttributeTypeDef";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestParameters);

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
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setAttributeTypeDef(metadataCollection.reIdentifyAttributeTypeDef(userId,
                                                                                       originalAttributeTypeDefGUID,
                                                                                       originalAttributeTypeDefName,
                                                                                       newAttributeTypeDefGUID,
                                                                                       newAttributeTypeDefName));

            this.logDynamicTypeManagement(serverName, userId, methodName, originalAttributeTypeDefName, originalAttributeTypeDefGUID);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns a boolean indicating if the entity is stored in the metadata collection.  This entity may be a full
     * entity object, or an entity proxy.
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      unique identifier for requesting user.
     * @param guid        String unique identifier for the entity
     * @param requestBody options to attach to the request.
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * InvalidParameterException the guid is null.
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse isEntityKnown(String     serverName,
                                              String     userId,
                                              String     guid,
                                              GetRequest requestBody)
    {
        final String methodName = "isEntityKnown";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.isEntityKnown(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @param requestBody options to attach to the request.
     * @return EntitySummary structure or
     * InvalidParameterException the guid is null.
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntitySummaryResponse getEntitySummary(String     serverName,
                                                  String     userId,
                                                  String     guid,
                                                  GetRequest requestBody)
    {
        final  String   methodName = "getEntitySummary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntitySummaryResponse response = new EntitySummaryResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.getEntitySummary(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the header, classifications, and properties of a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param requestBody options to attach to the request.
     * @return EntityDetailResponse:
     * EntityDetail structure or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse getEntityDetail(String     serverName,
                                                String     userId,
                                                String     guid,
                                                GetRequest requestBody)
    {
        final  String   methodName = "getEntityDetail";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.getEntityDetail(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a historical version of an entity.  This includes the header, classifications and properties of the entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param requestBody the time used to determine which version of the entity that is desired.
     * @return EntityDetailResponse:
     * EntityDetail structure or
     * InvalidParameterException the guid or date is null or the asOfTime property is for a future time or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested or
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityDetailResponse getEntityDetail(String         serverName,
                                                 String         userId,
                                                 String         guid,
                                                 HistoryRequest requestBody)
    {
        final  String   methodName = "getEntityDetail";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setEntity(metadataCollection.getEntityDetail(userId, guid, requestBody.getAsOfTime()));
            }
            else
            {
                response.setEntity(metadataCollection.getEntityDetail(userId, guid, null));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param requestBody detailing the range of times and paging for the results
     * @return EntityList structure or
     * InvalidParameterException the guid or date is null or fromTime is after the toTime
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * FunctionNotSupportedException the repository does not support history.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse getEntityDetailHistory(String              serverName,
                                                      String              userId,
                                                      String              guid,
                                                      HistoryRangeRequest requestBody)
    {
        final  String   methodName = "getEntityDetailHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityListResponse response = new EntityListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setEntities(metadataCollection.getEntityDetailHistory(userId,
                                                                               guid,
                                                                               requestBody.getFromTime(),
                                                                               requestBody.getToTime(),
                                                                               requestBody.getOffset(),
                                                                               requestBody.getPageSize(),
                                                                               requestBody.getSequencingOrder()));
            }
            else
            {
                response.setEntities(metadataCollection.getEntityDetailHistory(userId,
                                                                               guid,
                                                                               null,
                                                                               null,
                                                                               0,
                                                                               0,
                                                                               HistorySequencingOrder.BACKWARDS));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return all historical versions of an entity's classification within the bounds of the provided timestamps.
     * To retrieve all historical versions of an entity's classification, set both the 'fromTime' and 'toTime' to null.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param classificationName name of the classification within entity
     * @param requestBody detailing the range of times and paging for the results
     * @return {@code List<Classification>} of each historical version of the entity's classification within the bounds, and in the order requested or
     * InvalidParameterException the guid or date is null or fromTime is after the toTime
     * RepositoryErrorException a problem communicating with the metadata repository where the metadata collection is stored.
     * EntityNotKnownException the requested entity instance is not active in the metadata collection at the time requested.
     * EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * FunctionNotSupportedException the repository does not support history.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public ClassificationListResponse getClassificationHistory(String              serverName,
                                                               String              userId,
                                                               String              guid,
                                                               String              classificationName,
                                                               HistoryRangeRequest requestBody)
    {
        final String methodName = "getClassificationHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationListResponse response = new ClassificationListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setClassifications(metadataCollection.getClassificationHistory(userId,
                                                                               guid,
                                                                               classificationName,
                                                                               requestBody.getFromTime(),
                                                                               requestBody.getToTime(),
                                                                               requestBody.getOffset(),
                                                                               requestBody.getPageSize(),
                                                                               requestBody.getSequencingOrder()));
            }
            else
            {
                response.setClassifications(metadataCollection.getClassificationHistory(userId,
                                                                                        guid,
                                                                                        classificationName,
                                                                                        null,
                                                                                        null,
                                                                                        0,
                                                                                        0,
                                                                                        HistorySequencingOrder.BACKWARDS));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * Relationships list.  Null means no relationships associated with the entity or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * PropertyErrorException the sequencing property is not valid for the attached classifications or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipListResponse getRelationshipsForEntity(String                     serverName,
                                                              String                     userId,
                                                              String                     entityGUID,
                                                              TypeLimitedFindRequest     requestBody)
    {
        final  String   methodName = "getRelationshipsForEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               relationshipTypeGUID    = null;
        int                  fromRelationshipElement = 0;
        List<InstanceStatus> limitResultsByStatus    = null;
        String               sequencingProperty      = null;
        SequencingOrder      sequencingOrder         = null;
        int                  pageSize                = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID    = requestBody.getTypeGUID();
            fromRelationshipElement = requestBody.getOffset();
            limitResultsByStatus    = requestBody.getLimitResultsByStatus();
            sequencingProperty      = requestBody.getSequencingProperty();
            sequencingOrder         = requestBody.getSequencingOrder();
            pageSize                = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * Relationships list.  Null means no relationships associated with the entity or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the requested entity instance is not known in the metadata collection or
     * PropertyErrorException the sequencing property is not valid for the attached classifications or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipListResponse getRelationshipsForEntityHistory(String                               serverName,
                                                                     String                               userId,
                                                                     String                               entityGUID,
                                                                     TypeLimitedHistoricalFindRequest     requestBody)
    {
        final  String   methodName = "getRelationshipsForEntityHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               relationshipTypeGUID    = null;
        int                  fromRelationshipElement = 0;
        List<InstanceStatus> limitResultsByStatus    = null;
        Date                 asOfTime                = null;
        String               sequencingProperty      = null;
        SequencingOrder      sequencingOrder         = null;
        int                  pageSize                = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID    = requestBody.getTypeGUID();
            fromRelationshipElement = requestBody.getOffset();
            limitResultsByStatus    = requestBody.getLimitResultsByStatus();
            asOfTime                = requestBody.getAsOfTime();
            sequencingProperty      = requestBody.getSequencingProperty();
            sequencingOrder         = requestBody.getSequencingOrder();
            pageSize                = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities that match the supplied conditions.  The results can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntities(String            serverName,
                                            String            userId,
                                            EntityFindRequest requestBody)
    {
        final  String   methodName = "findEntities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                entityTypeGUID               = null;
        List<String>          entitySubtypeGUIDs           = null;
        boolean               skipSubtypes                 = false;
        SearchProperties      matchProperties              = null;
        int                   fromEntityElement            = 0;
        List<InstanceStatus>  limitResultsByStatus         = null;
        SearchClassifications matchClassifications         = null;
        String                sequencingProperty           = null;
        SequencingOrder       sequencingOrder              = null;
        int                   pageSize                     = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            entitySubtypeGUIDs                = requestBody.getSubtypeGUIDs();
            
            /*
             * An exclusion list arrives in a field of its own, so a request that carries one is asking for
             * those subtypes to be left out rather than to be the only ones included.
             */
            if ((requestBody.getExcludeSubtypeGUIDs() != null) && (! requestBody.getExcludeSubtypeGUIDs().isEmpty()))
            {
                entitySubtypeGUIDs = requestBody.getExcludeSubtypeGUIDs();
                skipSubtypes = true;
            }
            matchProperties                   = requestBody.getMatchProperties();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            matchClassifications              = requestBody.getMatchClassifications();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntities(userId,
                                                                           entityTypeGUID,
                                                                           entitySubtypeGUIDs,
                                                                           skipSubtypes,
                                                                           matchProperties,
                                                                           fromEntityElement,
                                                                           limitResultsByStatus,
                                                                           matchClassifications,
                                                                           null,
                                                                           sequencingProperty,
                                                                           sequencingOrder,
                                                                           pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a count of the entities that match the supplied conditions.  This has the same search semantics as
     * findEntities()/findEntitiesByHistory(), delegating to the metadata collection's countEntities() method
     * rather than fetching the matching entities themselves.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody find parameters used to limit the returned results.
     * @return CountResponse:
     * the number of entities matching the supplied criteria or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support this optional method or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  CountResponse countEntities(String                      serverName,
                                        String                      userId,
                                        EntityHistoricalFindRequest requestBody)
    {
        final  String   methodName = "countEntities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                    entityTypeGUID                    = null;
        List<String>              entitySubtypeGUIDs                = null;
        boolean               skipSubtypes                 = false;
        SearchProperties          matchProperties                   = null;
        int                       fromEntityElement                 = 0;
        List<InstanceStatus>      limitResultsByStatus              = null;
        SearchClassifications     matchClassifications              = null;
        Date                      asOfTime                          = null;
        String                    sequencingProperty                = null;
        SequencingOrder           sequencingOrder                   = null;
        int                       pageSize                          = 0;

        CountResponse response = new CountResponse();

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            entitySubtypeGUIDs                = requestBody.getSubtypeGUIDs();
            
            /*
             * An exclusion list arrives in a field of its own, so a request that carries one is asking for
             * those subtypes to be left out rather than to be the only ones included.
             */
            if ((requestBody.getExcludeSubtypeGUIDs() != null) && (! requestBody.getExcludeSubtypeGUIDs().isEmpty()))
            {
                entitySubtypeGUIDs = requestBody.getExcludeSubtypeGUIDs();
                skipSubtypes = true;
            }
            matchProperties                   = requestBody.getMatchProperties();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            matchClassifications              = requestBody.getMatchClassifications();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            long count = metadataCollection.countEntities(userId,
                                                          entityTypeGUID,
                                                          entitySubtypeGUIDs,
                                                          skipSubtypes,
                                                          matchProperties,
                                                          fromEntityElement,
                                                          limitResultsByStatus,
                                                          matchClassifications,
                                                          asOfTime,
                                                          sequencingProperty,
                                                          sequencingOrder,
                                                          pageSize);
            response.setCount(count);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities that match the supplied conditions.  The results can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByHistory(String                      serverName,
                                                     String                      userId,
                                                     EntityHistoricalFindRequest requestBody)
    {
        final  String   methodName = "findEntitiesByHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                    entityTypeGUID                    = null;
        List<String>              entitySubtypeGUIDs                = null;
        boolean               skipSubtypes                 = false;
        SearchProperties          matchProperties                   = null;
        int                       fromEntityElement                 = 0;
        List<InstanceStatus>      limitResultsByStatus              = null;
        SearchClassifications     matchClassifications              = null;
        Date                      asOfTime                          = null;
        String                    sequencingProperty                = null;
        SequencingOrder           sequencingOrder                   = null;
        int                       pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            entitySubtypeGUIDs                = requestBody.getSubtypeGUIDs();
            
            /*
             * An exclusion list arrives in a field of its own, so a request that carries one is asking for
             * those subtypes to be left out rather than to be the only ones included.
             */
            if ((requestBody.getExcludeSubtypeGUIDs() != null) && (! requestBody.getExcludeSubtypeGUIDs().isEmpty()))
            {
                entitySubtypeGUIDs = requestBody.getExcludeSubtypeGUIDs();
                skipSubtypes = true;
            }
            matchProperties                   = requestBody.getMatchProperties();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            matchClassifications              = requestBody.getMatchClassifications();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntities(userId,
                                                                           entityTypeGUID,
                                                                           entitySubtypeGUIDs,
                                                                           skipSubtypes,
                                                                           matchProperties,
                                                                           fromEntityElement,
                                                                           limitResultsByStatus,
                                                                           matchClassifications,
                                                                           asOfTime,
                                                                           sequencingProperty,
                                                                           sequencingOrder,
                                                                           pageSize);
            response.setEntities(entities);
            if (entities != null)
            {
                response.setOffset(fromEntityElement);
                response.setPageSize(pageSize);
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByProperty(String                    serverName,
                                                      String                    userId,
                                                      EntityPropertyFindRequest requestBody)
    {
        final  String   methodName = "findEntitiesByProperty";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               entityTypeGUID               = null;
        InstanceProperties   matchProperties              = null;
        MatchCriteria        matchCriteria                = null;
        int                  fromEntityElement            = 0;
        List<InstanceStatus> limitResultsByStatus         = null;
        List<String>         limitResultsByClassification = null;
        String               sequencingProperty           = null;
        SequencingOrder      sequencingOrder              = null;
        int                  pageSize                     = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            matchProperties                   = requestBody.getMatchProperties();
            matchCriteria                     = requestBody.getMatchCriteria();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            limitResultsByClassification      = requestBody.getLimitResultsByClassification();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntitiesByProperty(userId,
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
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByPropertyHistory(String                              serverName,
                                                             String                              userId,
                                                             EntityPropertyHistoricalFindRequest requestBody)
    {
        final  String   methodName = "findEntitiesByPropertyHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

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

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            matchProperties                   = requestBody.getMatchProperties();
            matchCriteria                     = requestBody.getMatchCriteria();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            limitResultsByClassification      = requestBody.getLimitResultsByClassification();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntitiesByProperty(userId,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName name of the classification a null is not valid.
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * ClassificationErrorException the classification request is not known to the metadata collection.
     * PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByClassification(String                   serverName,
                                                            String                   userId,
                                                            String                   classificationName,
                                                            PropertyMatchFindRequest requestBody)
    {
        final  String   methodName = "findEntitiesByClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               entityTypeGUID                     = null;
        InstanceProperties   matchClassificationProperties      = null;
        MatchCriteria        matchCriteria                      = null;
        int                  fromEntityElement                  = 0;
        List<InstanceStatus> limitResultsByStatus               = null;
        String               sequencingProperty                 = null;
        SequencingOrder      sequencingOrder                    = null;
        int                  pageSize                           = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                     = requestBody.getTypeGUID();
            matchClassificationProperties      = requestBody.getMatchProperties();
            matchCriteria                      = requestBody.getMatchCriteria();
            fromEntityElement                  = requestBody.getOffset();
            limitResultsByStatus               = requestBody.getLimitResultsByStatus();
            sequencingProperty                 = requestBody.getSequencingProperty();
            sequencingOrder                    = requestBody.getSequencingOrder();
            pageSize                           = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntitiesByClassification(userId,
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
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName name of the classification a null is not valid.
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * ClassificationErrorException the classification request is not known to the metadata collection.
     * PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByClassificationHistory(String                              serverName,
                                                                   String                              userId,
                                                                   String                              classificationName,
                                                                   PropertyMatchHistoricalFindRequest  requestBody)
    {
        final  String   methodName = "findEntitiesByClassificationHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               entityTypeGUID                     = null;
        InstanceProperties   matchClassificationProperties      = null;
        MatchCriteria        matchCriteria                      = null;
        int                  fromEntityElement                  = 0;
        List<InstanceStatus> limitResultsByStatus               = null;
        Date                 asOfTime                           = null;
        String               sequencingProperty                 = null;
        SequencingOrder      sequencingOrder                    = null;
        int                  pageSize                           = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                     = requestBody.getTypeGUID();
            matchClassificationProperties      = requestBody.getMatchProperties();
            matchCriteria                      = requestBody.getMatchCriteria();
            fromEntityElement                  = requestBody.getOffset();
            limitResultsByStatus               = requestBody.getLimitResultsByStatus();
            sequencingProperty                 = requestBody.getSequencingProperty();
            sequencingOrder                    = requestBody.getSequencingOrder();
            pageSize                           = requestBody.getPageSize();
            asOfTime                           = requestBody.getAsOfTime();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntitiesByClassification(userId,
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
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchString String expression of the characteristics of the required relationships.
     * @param startsWith true if the search should be for strings that start with the search string
     * @param endsWith true if the search should be for strings that end with the search string
     * @param ignoreCase true if the search should be case-insensitive
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByPropertyValue(String                    serverName,
                                                           String                    userId,
                                                           String                    searchString,
                                                           boolean                   startsWith,
                                                           boolean                   endsWith,
                                                           boolean                   ignoreCase,
                                                           EntityPropertyFindRequest requestBody)
    {
        final  String   methodName = "findEntitiesByPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                  entityTypeGUID                    = null;
        int                     fromEntityElement                 = 0;
        List<InstanceStatus>    limitResultsByStatus              = null;
        List<String>            limitResultsByClassification      = null;
        String                  sequencingProperty                = null;
        SequencingOrder         sequencingOrder                   = null;
        int                     pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByClassification      = requestBody.getLimitResultsByClassification();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntitiesByPropertyValue(userId,
                                                                                          entityTypeGUID,
                                                                                          searchString,
                                                                                          startsWith,
                                                                                          endsWith,
                                                                                          ignoreCase,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchString String expression of the characteristics of the required relationships.
     * @param startsWith true if the search should be for strings that start with the search string
     * @param endsWith true if the search should be for strings that end with the search string
     * @param ignoreCase true if the search should be case-insensitive
     * @param requestBody find parameters used to limit the returned results.
     * @return EntityListResponse:
     * a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection or
     * InvalidParameterException a parameter is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityListResponse findEntitiesByPropertyValueHistory(String                              serverName,
                                                                  String                              userId,
                                                                  String                              searchString,
                                                                  boolean                             startsWith,
                                                                  boolean                             endsWith,
                                                                  boolean                             ignoreCase,
                                                                  EntityPropertyHistoricalFindRequest requestBody)
    {
        final  String   methodName = "findEntitiesByPropertyValueHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                  entityTypeGUID                    = null;
        int                     fromEntityElement                 = 0;
        List<InstanceStatus>    limitResultsByStatus              = null;
        List<String>            limitResultsByClassification      = null;
        Date                    asOfTime                          = null;
        String                  sequencingProperty                = null;
        SequencingOrder         sequencingOrder                   = null;
        int                     pageSize                          = 0;

        EntityListResponse response = new EntityListResponse();

        if (requestBody != null)
        {
            entityTypeGUID                    = requestBody.getTypeGUID();
            fromEntityElement                 = requestBody.getOffset();
            limitResultsByClassification      = requestBody.getLimitResultsByClassification();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<EntityDetail>  entities = metadataCollection.findEntitiesByPropertyValue(userId,
                                                                                          entityTypeGUID,
                                                                                          searchString,
                                                                                          startsWith,
                                                                                          endsWith,
                                                                                          ignoreCase,
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
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param guid       String unique identifier for the relationship.
     * @param requestBody options to attach to the request.
     * @return RelationshipResponse:
     * relationship details if the relationship is found in the metadata collection; otherwise return null or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse isRelationshipKnown(String     serverName,
                                                    String     userId,
                                                    String     guid,
                                                    GetRequest requestBody)
    {
        final String methodName = "isRelationshipKnown";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipResponse response = new RelationshipResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.isRelationshipKnown(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a requested relationship.
     *
     * @param serverName name of the active server
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param requestBody options to attach to the request.
     * @return RelationshipResponse:
     * a relationship structure or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse getRelationship(String     serverName,
                                                String     userId,
                                                String     guid,
                                                GetRequest requestBody)
    {
        final  String   methodName = "getRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipResponse response = new RelationshipResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.getRelationship(userId, guid));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a historical version of a relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param requestBody the time used to determine which version of the entity that is desired.
     * @return RelationshipResponse:
     * a relationship structure or
     * InvalidParameterException the guid or date is null or the asOfTime property is for a future time or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipResponse getRelationship(String         serverName,
                                                 String         userId,
                                                 String         guid,
                                                 HistoryRequest requestBody)
    {
        final  String   methodName = "getRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipResponse response = new RelationshipResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setRelationship(metadataCollection.getRelationship(userId, guid, requestBody.getAsOfTime()));
            }
            else
            {
                response.setRelationship(metadataCollection.getRelationship(userId, guid, null));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all
     * historical versions of a relationship, set both the 'fromTime' and 'toTime' to null.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param requestBody detailing the range of times and paging for the results
     * @return RelationshipList structure or
     * InvalidParameterException the guid or date is null or fromTime is after the toTime
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * RelationshipNotKnownException the requested relationship instance is not known in the metadata collection
     *                                   at the time requested.
     * FunctionNotSupportedException the repository does not support history.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse getRelationshipHistory(String              serverName,
                                                            String              userId,
                                                            String              guid,
                                                            HistoryRangeRequest requestBody)
    {
        final  String   methodName = "getRelationshipHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipListResponse response = new RelationshipListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setRelationships(metadataCollection.getRelationshipHistory(userId,
                                                                                    guid,
                                                                                    requestBody.getFromTime(),
                                                                                    requestBody.getToTime(),
                                                                                    requestBody.getOffset(),
                                                                                    requestBody.getPageSize(),
                                                                                    requestBody.getSequencingOrder()));
            }
            else
            {
                response.setRelationships(metadataCollection.getRelationshipHistory(userId,
                                                                                    guid,
                                                                                    null,
                                                                                    null,
                                                                                    0,
                                                                                    0,
                                                                                    HistorySequencingOrder.BACKWARDS));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse findRelationships(String                  serverName,
                                                       String                  userId,
                                                       RelationshipFindRequest requestBody)
    {
        final  String   methodName = "findRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               relationshipTypeGUID     = null;
        List<String>         relationshipSubtypeGUIDs = null;
        boolean               skipSubtypes                 = false;
        List<String>         end1EntityGUIDs          = null;
        String               end1EntityTypeGUID       = null;
        List<String>         end2EntityGUIDs          = null;
        String               end2EntityTypeGUID       = null;
        EndMatchCriteria     endMatchCriteria         = null;
        SearchProperties     matchProperties          = null;
        int                  fromRelationshipElement  = 0;
        List<InstanceStatus> limitResultsByStatus     = null;
        String               sequencingProperty       = null;
        SequencingOrder      sequencingOrder          = null;
        int                  pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            relationshipSubtypeGUIDs          = requestBody.getSubtypeGUIDs();
            
            /*
             * An exclusion list arrives in a field of its own, so a request that carries one is asking for
             * those subtypes to be left out rather than to be the only ones included.
             */
            if ((requestBody.getExcludeSubtypeGUIDs() != null) && (! requestBody.getExcludeSubtypeGUIDs().isEmpty()))
            {
                relationshipSubtypeGUIDs = requestBody.getExcludeSubtypeGUIDs();
                skipSubtypes = true;
            }
            end1EntityGUIDs                   = requestBody.getEnd1EntityGUIDs();
            end1EntityTypeGUID                = requestBody.getEnd1EntityTypeGUID();
            end2EntityGUIDs                   = requestBody.getEnd2EntityGUIDs();
            end2EntityTypeGUID                = requestBody.getEnd2EntityTypeGUID();
            endMatchCriteria                  = requestBody.getEndMatchCriteria();
            matchProperties                   = requestBody.getMatchProperties();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship>  relationships = metadataCollection.findRelationships(userId,
                                                                                     relationshipTypeGUID,
                                                                                     relationshipSubtypeGUIDs,
                                                                                     skipSubtypes,
                                                                                     end1EntityGUIDs,
                                                                                     end1EntityTypeGUID,
                                                                                     end2EntityGUIDs,
                                                                                     end2EntityTypeGUID,
                                                                                     endMatchCriteria,
                                                                                     matchProperties,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a count of the relationships that match the requested conditions.  This has the same search
     * semantics as findRelationships()/findRelationshipsByHistory(), delegating to the metadata collection's
     * countRelationships() method rather than fetching the matching relationships themselves.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param requestBody find parameters used to limit the returned results.
     * @return CountResponse:
     * the number of relationships matching the supplied criteria or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support one of the provided parameters or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  CountResponse countRelationships(String                            serverName,
                                             String                            userId,
                                             RelationshipHistoricalFindRequest requestBody)
    {
        final  String   methodName = "countRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               relationshipTypeGUID     = null;
        List<String>         relationshipSubtypeGUIDs = null;
        boolean               skipSubtypes                 = false;
        List<String>         end1EntityGUIDs          = null;
        String               end1EntityTypeGUID       = null;
        List<String>         end2EntityGUIDs          = null;
        String               end2EntityTypeGUID       = null;
        EndMatchCriteria     endMatchCriteria         = null;
        SearchProperties     matchProperties          = null;
        int                  fromRelationshipElement  = 0;
        List<InstanceStatus> limitResultsByStatus     = null;
        Date                 asOfTime                 = null;
        String               sequencingProperty       = null;
        SequencingOrder      sequencingOrder          = null;
        int                  pageSize                 = 0;

        CountResponse response = new CountResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            relationshipSubtypeGUIDs          = requestBody.getSubtypeGUIDs();
            
            /*
             * An exclusion list arrives in a field of its own, so a request that carries one is asking for
             * those subtypes to be left out rather than to be the only ones included.
             */
            if ((requestBody.getExcludeSubtypeGUIDs() != null) && (! requestBody.getExcludeSubtypeGUIDs().isEmpty()))
            {
                relationshipSubtypeGUIDs = requestBody.getExcludeSubtypeGUIDs();
                skipSubtypes = true;
            }
            end1EntityGUIDs                   = requestBody.getEnd1EntityGUIDs();
            end1EntityTypeGUID                = requestBody.getEnd1EntityTypeGUID();
            end2EntityGUIDs                   = requestBody.getEnd2EntityGUIDs();
            end2EntityTypeGUID                = requestBody.getEnd2EntityTypeGUID();
            endMatchCriteria                  = requestBody.getEndMatchCriteria();
            matchProperties                   = requestBody.getMatchProperties();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            long count = metadataCollection.countRelationships(userId,
                                                                relationshipTypeGUID,
                                                                relationshipSubtypeGUIDs,
                                                                skipSubtypes,
                                                                end1EntityGUIDs,
                                                                end1EntityTypeGUID,
                                                                end2EntityGUIDs,
                                                                end2EntityTypeGUID,
                                                                endMatchCriteria,
                                                                matchProperties,
                                                                fromRelationshipElement,
                                                                limitResultsByStatus,
                                                                asOfTime,
                                                                sequencingProperty,
                                                                sequencingOrder,
                                                                pageSize);
            response.setCount(count);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse findRelationshipsByHistory(String                            serverName,
                                                                String                            userId,
                                                                RelationshipHistoricalFindRequest requestBody)
    {
        final  String   methodName = "findRelationshipsByHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String               relationshipTypeGUID     = null;
        List<String>         relationshipSubtypeGUIDs = null;
        boolean               skipSubtypes                 = false;
        List<String>         end1EntityGUIDs          = null;
        String               end1EntityTypeGUID       = null;
        List<String>         end2EntityGUIDs          = null;
        String               end2EntityTypeGUID       = null;
        EndMatchCriteria     endMatchCriteria         = null;
        SearchProperties     matchProperties          = null;
        int                  fromRelationshipElement  = 0;
        List<InstanceStatus> limitResultsByStatus     = null;
        Date                 asOfTime                 = null;
        String               sequencingProperty       = null;
        SequencingOrder      sequencingOrder          = null;
        int                  pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            relationshipSubtypeGUIDs          = requestBody.getSubtypeGUIDs();
            
            /*
             * An exclusion list arrives in a field of its own, so a request that carries one is asking for
             * those subtypes to be left out rather than to be the only ones included.
             */
            if ((requestBody.getExcludeSubtypeGUIDs() != null) && (! requestBody.getExcludeSubtypeGUIDs().isEmpty()))
            {
                relationshipSubtypeGUIDs = requestBody.getExcludeSubtypeGUIDs();
                skipSubtypes = true;
            }
            end1EntityGUIDs                   = requestBody.getEnd1EntityGUIDs();
            end1EntityTypeGUID                = requestBody.getEnd1EntityTypeGUID();
            end2EntityGUIDs                   = requestBody.getEnd2EntityGUIDs();
            end2EntityTypeGUID                = requestBody.getEnd2EntityTypeGUID();
            endMatchCriteria                  = requestBody.getEndMatchCriteria();
            matchProperties                   = requestBody.getMatchProperties();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship>  relationships = metadataCollection.findRelationships(userId,
                                                                                     relationshipTypeGUID,
                                                                                     relationshipSubtypeGUIDs,
                                                                                     skipSubtypes,
                                                                                     end1EntityGUIDs,
                                                                                     end1EntityTypeGUID,
                                                                                     end2EntityGUIDs,
                                                                                     end2EntityTypeGUID,
                                                                                     endMatchCriteria,
                                                                                     matchProperties,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse findRelationshipsByProperty(String                    serverName,
                                                                 String                    userId,
                                                                 PropertyMatchFindRequest  requestBody)
    {
        final  String   methodName = "findRelationshipsByProperty";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                    relationshipTypeGUID     = null;
        InstanceProperties        matchProperties          = null;
        MatchCriteria             matchCriteria            = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            matchProperties                   = requestBody.getMatchProperties();
            matchCriteria                     = requestBody.getMatchCriteria();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship>  relationships = metadataCollection.findRelationshipsByProperty(userId,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse findRelationshipsByPropertyHistory(String                              serverName,
                                                                        String                              userId,
                                                                        PropertyMatchHistoricalFindRequest  requestBody)
    {
        final  String   methodName = "findRelationshipsByPropertyHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

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

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            matchProperties                   = requestBody.getMatchProperties();
            matchCriteria                     = requestBody.getMatchCriteria();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship>  relationships = metadataCollection.findRelationshipsByProperty(userId,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchString String expression of the characteristics of the required relationships.
     * @param startsWith true if the search should be for strings that start with the search string
     * @param endsWith true if the search should be for strings that end with the search string
     * @param ignoreCase true if the search should be case-insensitive
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * PropertyErrorException a problem with one of the other parameters  or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse findRelationshipsByPropertyValue(String                    serverName,
                                                                      String                    userId,
                                                                      String                    searchString,
                                                                      boolean                   startsWith,
                                                                      boolean                   endsWith,
                                                                      boolean                   ignoreCase,
                                                                      TypeLimitedFindRequest    requestBody)
    {
        final  String   methodName = "findRelationshipsByPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                    relationshipTypeGUID     = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship>  relationships = metadataCollection.findRelationshipsByPropertyValue(userId,
                                                                                                    relationshipTypeGUID,
                                                                                                    searchString,
                                                                                                    startsWith,
                                                                                                    endsWith,
                                                                                                    ignoreCase,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return a list of relationships that match the search criteria.  The results can be paged.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param searchString String expression of the characteristics of the required relationships.
     * @param startsWith true if the search should be for strings that start with the search string
     * @param endsWith true if the search should be for strings that end with the search string
     * @param ignoreCase true if the search should be case-insensitive
     * @param requestBody find parameters used to limit the returned results.
     * @return RelationshipListResponse:
     * a list of relationships.  Null means no matching relationships or
     * InvalidParameterException one of the parameters is invalid or null or
     * TypeErrorException the type guid passed on the request is not known by the metadata collection or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * PropertyErrorException a problem with one of the other parameters  or
     * PagingErrorException the paging/sequencing parameters are set up incorrectly or
     * FunctionNotSupportedException the repository does not support asOfTime parameter or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  RelationshipListResponse findRelationshipsByPropertyValueHistory(String                              serverName,
                                                                             String                              userId,
                                                                             String                              searchString,
                                                                             boolean                             startsWith,
                                                                             boolean                             endsWith,
                                                                             boolean                             ignoreCase,
                                                                             TypeLimitedHistoricalFindRequest    requestBody)
    {
        final  String   methodName = "findRelationshipsByPropertyValueHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                    relationshipTypeGUID     = null;
        int                       fromRelationshipElement  = 0;
        List<InstanceStatus>      limitResultsByStatus     = null;
        Date                      asOfTime                 = null;
        String                    sequencingProperty       = null;
        SequencingOrder           sequencingOrder          = null;
        int                       pageSize                 = 0;

        RelationshipListResponse response = new RelationshipListResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID              = requestBody.getTypeGUID();
            fromRelationshipElement           = requestBody.getOffset();
            limitResultsByStatus              = requestBody.getLimitResultsByStatus();
            asOfTime                          = requestBody.getAsOfTime();
            sequencingProperty                = requestBody.getSequencingProperty();
            sequencingOrder                   = requestBody.getSequencingOrder();
            pageSize                          = requestBody.getPageSize();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            List<Relationship>  relationships = metadataCollection.findRelationshipsByPropertyValue(userId,
                                                                                                    relationshipTypeGUID,
                                                                                                    searchString,
                                                                                                    startsWith,
                                                                                                    endsWith,
                                                                                                    ignoreCase,
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
            }

        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type or
     * ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse addEntity(String              serverName,
                                          String              userId,
                                          EntityCreateRequest requestBody)
    {
        final  String   methodName = "addEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

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

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.addEntity(userId,
                                                            entityTypeGUID,
                                                            initialProperties,
                                                            initialClassifications,
                                                            initialStatus));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public EntityDetailResponse addExternalEntity(String              serverName,
                                                  String              userId,
                                                  EntityCreateRequest requestBody)
    {
        final  String   methodName = "addExternalEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String                     entityTypeGUID = null;
        String                     externalSourceGUID = null;
        String                     externalSourceName = null;
        InstanceProperties         initialProperties = null;
        List<Classification>       initialClassifications = null;
        InstanceStatus             initialStatus = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (requestBody != null)
        {
            entityTypeGUID = requestBody.getEntityTypeGUID();
            externalSourceGUID = requestBody.getMetadataCollectionId();
            externalSourceName = requestBody.getMetadataCollectionName();
            initialProperties = requestBody.getInitialProperties();
            initialClassifications = requestBody.getInitialClassifications();
            initialStatus = requestBody.getInitialStatus();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.addExternalEntity(userId,
                                                                    entityTypeGUID,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    initialProperties,
                                                                    initialClassifications,
                                                                    initialStatus));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody details of entity to add.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity proxy is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse addEntityProxy(String             serverName,
                                       String             userId,
                                       EntityProxyRequest requestBody)
    {
        final  String   methodName = "addEntityProxy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.addEntityProxy(userId, requestBody.getEntity());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Update selected properties in an entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param requestBody a list of properties to change.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse updateEntityProperties(String                      serverName,
                                                       String                      userId,
                                                       String                      entityGUID,
                                                       InstancePropertiesRequest   requestBody)
    {
        final  String   methodName = "updateEntityProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.updateEntityProperties(userId,
                                                                         entityGUID,
                                                                         requestBody.getInstanceProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param requestBody options to attach to the request.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties, and classifications or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support undo or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse undoEntityUpdate(String     serverName,
                                                 String     userId,
                                                 String     entityGUID,
                                                 GetRequest requestBody)
    {
        final String methodName = "undoEntityUpdate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.undoEntityUpdate(userId, entityGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return EntityDetailResponse
     * details of the deleted entity or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       soft-deletes (use purgeEntity() to remove the entity permanently)
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse  deleteEntity(String                        serverName,
                                              String                        userId,
                                              String                        obsoleteEntityGUID,
                                              TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "deleteEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.deleteEntity(userId, typeDefGUID, typeDefName, obsoleteEntityGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * EntityNotDeletedException the entity is not in DELETED status and so can not be purged or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse purgeEntity(String                        serverName,
                                    String                        userId,
                                    String                        deletedEntityGUID,
                                    TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "purgeEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.purgeEntity(userId, typeDefGUID, typeDefName, deletedEntityGUID);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @param requestBody options to attach to the request.
     * @return EntityDetailResponse:
     * EntityDetail showing the restored entity header, properties, and classifications or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * EntityNotDeletedException the entity is not in DELETED status and so it cannot be restored or
     * FunctionNotSupportedException the repository does not support soft-delete or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse restoreEntity(String     serverName,
                                              String     userId,
                                              String     deletedEntityGUID,
                                              GetRequest requestBody)
    {
        final  String   methodName = "restoreEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.restoreEntity(userId, deletedEntityGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param requestBody list of properties to set in the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse classifyEntity(String                      serverName,
                                               String                      userId,
                                               String                      entityGUID,
                                               String                      classificationName,
                                               InstancePropertiesRequest   requestBody)
    {
        final  String   methodName = "classifyEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.classifyEntity(userId,
                                                                 entityGUID,
                                                                 classificationName,
                                                                 requestBody.getInstanceProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public ClassificationResponse classifyEntity(String                      serverName,
                                                 String                      userId,
                                                 String                      classificationName,
                                                 ProxyClassificationRequest  requestBody)
    {
        final  String   methodName = "classifyEntityProxy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationResponse response = new ClassificationResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setClassification(metadataCollection.classifyEntity(userId,
                                                                         requestBody.getEntityProxy(),
                                                                         classificationName,
                                                                         requestBody.getInstanceProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param requestBody values for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse  classifyEntity(String                serverName,
                                                String                userId,
                                                String                entityGUID,
                                                String                classificationName,
                                                ClassificationRequest requestBody)
    {
        final String methodName = "classifyEntity (detailed)";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.classifyEntity(userId,
                                                                 entityGUID,
                                                                 classificationName,
                                                                 requestBody.getMetadataCollectionId(),
                                                                 requestBody.getMetadataCollectionName(),
                                                                 requestBody.getClassificationOrigin(),
                                                                 requestBody.getClassificationOriginGUID(),
                                                                 requestBody.getClassificationProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public ClassificationResponse  classifyEntity(String                     serverName,
                                                  String                     userId,
                                                  String                     classificationName,
                                                  ClassificationProxyRequest requestBody)
    {
        final String methodName = "classifyEntityProxy (detailed)";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationResponse response = new ClassificationResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setClassification(metadataCollection.classifyEntity(userId,
                                                                         requestBody.getEntityProxy(),
                                                                         classificationName,
                                                                         requestBody.getMetadataCollectionId(),
                                                                         requestBody.getMetadataCollectionName(),
                                                                         requestBody.getClassificationOrigin(),
                                                                         requestBody.getClassificationOriginGUID(),
                                                                         requestBody.getClassificationProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param requestBody options to attach to the request.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * ClassificationErrorException the requested classification is not set on the entity or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse declassifyEntity(String     serverName,
                                                 String     userId,
                                                 String     entityGUID,
                                                 String     classificationName,
                                                 GetRequest requestBody)
    {
        final  String   methodName = "declassifyEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.declassifyEntity(userId,
                                                                   entityGUID,
                                                                   classificationName));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param classificationName String name for the classification.
     * @param requestBody details of the entity associated with the classification
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * ClassificationErrorException the requested classification is not set on the entity or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public ClassificationResponse declassifyEntity(String             serverName,
                                                   String             userId,
                                                   String             classificationName,
                                                   EntityProxyRequest requestBody)
    {
        final  String   methodName = "declassifyEntityProxy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationResponse response = new ClassificationResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setClassification(metadataCollection.declassifyEntity(userId,
                                                                               requestBody.getEntity(),
                                                                               classificationName));
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param requestBody list of properties for the classification.
     * @return EntityDetailResponse:
     * EntityDetail showing the resulting entity header, properties and classifications or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is not attached to the classification or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse updateEntityClassification(String                      serverName,
                                                           String                      userId,
                                                           String                      entityGUID,
                                                           String                      classificationName,
                                                           InstancePropertiesRequest   requestBody)
    {
        final  String   methodName = "updateEntityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        EntityDetailResponse response = new EntityDetailResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.updateEntityClassification(userId,
                                                                             entityGUID,
                                                                             classificationName,
                                                                             requestBody.getInstanceProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * ClassificationErrorException the requested classification is not attached to the classification or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public ClassificationResponse updateEntityClassification(String                      serverName,
                                                             String                      userId,
                                                             String                      classificationName,
                                                             ProxyClassificationRequest  requestBody)
    {
        final  String   methodName = "updateEntityProxyClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationResponse response = new ClassificationResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setClassification(metadataCollection.updateEntityClassification(userId,
                                                                                     requestBody.getEntityProxy(),
                                                                                     classificationName,
                                                                                     requestBody.getInstanceProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param requestBody parameters used to fill out the new relationship
     * @return RelationshipResponse:
     * Relationship structure with the new header, requested entities and properties or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                 the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type or
     * EntityNotKnownException one of the requested entities is not known in the metadata collection or
     * StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse addRelationship(String                     serverName,
                                                String                     userId,
                                                RelationshipCreateRequest  requestBody)
    {
        final  String   methodName = "addRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String             relationshipTypeGUID = null;
        InstanceProperties initialProperties = null;
        String             entityOneGUID = null;
        String             entityTwoGUID = null;
        InstanceStatus     initialStatus = null;

        RelationshipResponse response = new RelationshipResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID = requestBody.getRelationshipTypeGUID();
            initialProperties = requestBody.getInitialProperties();
            entityOneGUID = requestBody.getEntityOneGUID();
            entityTwoGUID = requestBody.getEntityTwoGUID();
            initialStatus = requestBody.getInitialStatus();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.addRelationship(userId,
                                                                        relationshipTypeGUID,
                                                                        initialProperties,
                                                                        entityOneGUID,
                                                                        entityTwoGUID,
                                                                        initialStatus));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody parameters used to fill out the new relationship
     * @return RelationshipResponse:
     * Relationship structure with the new header, requested entities and properties or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public RelationshipResponse addExternalRelationship(String                     serverName,
                                                        String                     userId,
                                                        RelationshipCreateRequest  requestBody)
    {
        final  String   methodName = "addExternalRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String             relationshipTypeGUID = null;
        String             externalSourceGUID = null;
        String             externalSourceName = null;
        InstanceProperties initialProperties = null;
        String             entityOneGUID = null;
        String             entityTwoGUID = null;
        InstanceStatus     initialStatus = null;

        RelationshipResponse response = new RelationshipResponse();

        if (requestBody != null)
        {
            relationshipTypeGUID = requestBody.getRelationshipTypeGUID();
            externalSourceGUID = requestBody.getMetadataCollectionId();
            externalSourceName = requestBody.getMetadataCollectionName();
            initialProperties = requestBody.getInitialProperties();
            entityOneGUID = requestBody.getEntityOneGUID();
            entityTwoGUID = requestBody.getEntityTwoGUID();
            initialStatus = requestBody.getInitialStatus();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.addExternalRelationship(userId,
                                                                                relationshipTypeGUID,
                                                                                externalSourceGUID,
                                                                                externalSourceName,
                                                                                initialProperties,
                                                                                entityOneGUID,
                                                                                entityTwoGUID,
                                                                                initialStatus));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Update the properties of a specific relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param requestBody list of the properties to update.
     * @return RelationshipResponse:
     * Resulting relationship structure with the new properties set or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse updateRelationshipProperties(String                      serverName,
                                                             String                      userId,
                                                             String                      relationshipGUID,
                                                             InstancePropertiesRequest   requestBody)
    {
        final  String   methodName = "updateRelationshipProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipResponse response = new RelationshipResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.updateRelationshipProperties(userId,
                                                                                     relationshipGUID,
                                                                                     requestBody.getInstanceProperties()));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param requestBody      request body including the optional query parameters
     * @return RelationshipResponse:
     * Relationship structure with the new current header, requested entities and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * FunctionNotSupportedException the repository does not support undo or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse undoRelationshipUpdate(String     serverName,
                                                       String     userId,
                                                       String     relationshipGUID,
                                                       GetRequest requestBody)
    {
        final String methodName = "undoRelationshipUpdate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipResponse response = new RelationshipResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.undoRelationshipUpdate(userId, relationshipGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED, and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return RelationshipResponse:
     * Updated relationship or
     * InvalidParameterException one of the parameters is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     soft-deletes or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse deleteRelationship(String                      serverName,
                                                   String                      userId,
                                                   String                      obsoleteRelationshipGUID,
                                                   TypeDefValidationForRequest requestBody)
    {
        final  String   methodName = "deleteRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        RelationshipResponse response = new RelationshipResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.deleteRelationship(userId,
                                                                           typeDefGUID,
                                                                           typeDefName,
                                                                           obsoleteRelationshipGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * RelationshipNotDeletedException the requested relationship is not in DELETED status or
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse purgeRelationship(String                        serverName,
                                          String                        userId,
                                          String                        deletedRelationshipGUID,
                                          TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "purgeRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.purgeRelationship(userId, typeDefGUID, typeDefName, deletedRelationshipGUID);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param serverName              unique identifier for requested server.
     * @param userId                  unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @param requestBody             request body including the optional query parameters
     * @return RelationshipResponse:
     * Relationship structure with the restored header, requested entities and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * RelationshipNotKnownException the requested relationship is not known in the metadata collection or
     * RelationshipNotDeletedException the requested relationship is not in DELETED status or
     * FunctionNotSupportedException the repository does not support soft-deletes
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse restoreRelationship(String     serverName,
                                                    String     userId,
                                                    String     deletedRelationshipGUID,
                                                    GetRequest requestBody)
    {
        final String methodName = "restoreRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        RelationshipResponse response = new RelationshipResponse();

        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.restoreRelationship(userId, deletedRelationshipGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

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
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID new unique identifier for the entity.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new guid or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-identification or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse reIdentifyEntity(String                        serverName,
                                                 String                        userId,
                                                 String                        entityGUID,
                                                 String                        newEntityGUID,
                                                 TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "reIdentifyEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.reIdentifyEntity(userId,
                                                                   typeDefGUID,
                                                                   typeDefName,
                                                                   entityGUID,
                                                                   newEntityGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Change an existing entity's type.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param requestBody the details of the current and new type.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new type information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException the instance's properties are not valid for the new type.
     * ClassificationErrorException the instance's classifications are not valid for the new type.
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-typing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse reTypeEntity(String               serverName,
                                             String               userId,
                                             String               entityGUID,
                                             TypeDefChangeRequest requestBody)
    {
        final  String   methodName = "reTypeEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefSummary currentTypeDefSummary = null;
        TypeDefSummary newTypeDefSummary = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (requestBody != null)
        {
            currentTypeDefSummary = requestBody.getCurrentTypeDef();
            newTypeDefSummary = requestBody.getNewTypeDef();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.reTypeEntity(userId,
                                                               entityGUID,
                                                               currentTypeDefSummary,
                                                               newTypeDefSummary));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return EntityDetailResponse:
     * entity: new values for this entity, including the new home information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-homing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetailResponse reHomeEntity(String                        serverName,
                                             String                        userId,
                                             String                        entityGUID,
                                             String                        homeMetadataCollectionId,
                                             String                        newHomeMetadataCollectionId,
                                             String                        newHomeMetadataCollectionName,
                                             TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "reHomeEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        EntityDetailResponse response = new EntityDetailResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setEntity(metadataCollection.reHomeEntity(userId,
                                                               entityGUID,
                                                               typeDefGUID,
                                                               typeDefName,
                                                               homeMetadataCollectionId,
                                                               newHomeMetadataCollectionId,
                                                               newHomeMetadataCollectionName));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return RelationshipResponse:
     * relationship: new values for this relationship, including the new guid or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-identification or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse reIdentifyRelationship(String                        serverName,
                                                       String                        userId,
                                                       String                        relationshipGUID,
                                                       String                        newRelationshipGUID,
                                                       TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "reIdentifyRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        RelationshipResponse response = new RelationshipResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.reIdentifyRelationship(userId,
                                                                               typeDefGUID,
                                                                               typeDefName,
                                                                               relationshipGUID,
                                                                               newRelationshipGUID));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Change an existing relationship's type.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param requestBody the details of the current and new type.
     * @return RelationshipResponse:
     * relationship: new values for this relationship, including the new type information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection or
     * PropertyErrorException the instance's properties are not valid for th new type.
     * RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-typing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse reTypeRelationship(String               serverName,
                                                   String               userId,
                                                   String               relationshipGUID,
                                                   TypeDefChangeRequest requestBody)
    {
        final  String   methodName = "reTypeRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        TypeDefSummary currentTypeDefSummary = null;
        TypeDefSummary newTypeDefSummary = null;

        RelationshipResponse response = new RelationshipResponse();

        if (requestBody != null)
        {
            currentTypeDefSummary = requestBody.getCurrentTypeDef();
            newTypeDefSummary = requestBody.getNewTypeDef();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.reTypeRelationship(userId,
                                                                           relationshipGUID,
                                                                           currentTypeDefSummary,
                                                                           newTypeDefSummary));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return RelationshipResponse:
     * relationship: new values for this relationship, including the new home information or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection or
     * FunctionNotSupportedException the repository does not support instance re-homing or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public RelationshipResponse reHomeRelationship(String                        serverName,
                                                   String                        userId,
                                                   String                        relationshipGUID,
                                                   String                        homeMetadataCollectionId,
                                                   String                        newHomeMetadataCollectionId,
                                                   String                        newHomeMetadataCollectionName,
                                                   TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "reHomeRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        RelationshipResponse response = new RelationshipResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            response.setRelationship(metadataCollection.reHomeRelationship(userId,
                                                                           relationshipGUID,
                                                                           typeDefGUID,
                                                                           typeDefName,
                                                                           homeMetadataCollectionId,
                                                                           newHomeMetadataCollectionId,
                                                                           newHomeMetadataCollectionName));
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody details of the entity to save.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse saveEntityReferenceCopy(String              serverName,
                                                String              userId,
                                                EntityDetailRequest requestBody)
    {
        final  String   methodName = "saveEntityReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.saveEntityReferenceCopy(userId, requestBody.getEntity());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @param requestBody get request body
     * @return list of all the classifications for this entity that are homed in this repository or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity is not recognized by this repository or
     * UserNotAuthorizedException to calling user is not authorized to retrieve this metadata or
     * FunctionNotSupportedException this method is not supported
     */
    public ClassificationListResponse getHomeClassifications(String     serverName,
                                                             String     userId,
                                                             String     entityGUID,
                                                             GetRequest requestBody)
    {
        final String methodName = "getHomeClassifications";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationListResponse response = new ClassificationListResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.getHomeClassifications(userId, entityGUID);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity is not recognized by this repository or
     * UserNotAuthorizedException to calling user is not authorized to retrieve this metadata or
     * FunctionNotSupportedException this method is not supported
     */
    public ClassificationListResponse getHomeClassifications(String         serverName,
                                                             String         userId,
                                                             String         entityGUID,
                                                             HistoryRequest requestBody)
    {
        final String  methodName = "getHomeClassifications (with history)";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        ClassificationListResponse response = new ClassificationListResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody == null)
            {
                metadataCollection.getHomeClassifications(userId, entityGUID);
            }
            else
            {
                metadataCollection.getHomeClassifications(userId, entityGUID, requestBody.getAsOfTime());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param requestBody the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse deleteEntityReferenceCopy(String                        serverName,
                                                  String                        userId,
                                                  EntityDetailRequest           requestBody)
    {
        final  String   methodName = "deleteEntityReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.deleteEntityReferenceCopy(userId, requestBody.getEntity());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     unique identifier for requesting server.
     * @param requestBody     the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the entity is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     * the metadata collection is stored or
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     * hosting the metadata collection or
     * PropertyErrorException one or more of the requested properties are not defined, or have different
     * characteristics in the TypeDef for this entity's type or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     * copy would be invalid or
     * EntityConflictException the new entity conflicts with an existing entity or
     * InvalidEntityException the new entity has invalid contents or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse purgeEntityReferenceCopy(String              serverName,
                                                 String              userId,
                                                 EntityDetailRequest requestBody)
    {
        final String methodName = "purgeEntityReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();

        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.purgeEntityReferenceCopy(userId, requestBody.getEntity());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse purgeEntityReferenceCopy(String                        serverName,
                                                 String                        userId,
                                                 String                        entityGUID,
                                                 String                        homeMetadataCollectionId,
                                                 TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "purgeEntityReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.purgeEntityReferenceCopy(userId,
                                                        entityGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        homeMetadataCollectionId);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * EntityNotKnownException the entity identified by the guid is not found in the metadata collection or
     * HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse refreshEntityReferenceCopy(String                        serverName,
                                                   String                        userId,
                                                   String                        entityGUID,
                                                   String                        homeMetadataCollectionId,
                                                   TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "refreshEntityReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.refreshEntityReferenceCopy(userId,
                                                          entityGUID,
                                                          typeDefGUID,
                                                          typeDefName,
                                                          homeMetadataCollectionId);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse saveClassificationReferenceCopy(String                          serverName,
                                                        String                          userId,
                                                        ClassificationWithEntityRequest requestBody)
    {
        final String methodName  = "saveClassificationReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

                if (requestBody.getEntity() != null)
                {
                    metadataCollection.saveClassificationReferenceCopy(userId, requestBody.getEntity(), requestBody.getClassification());
                }
                else
                {
                    metadataCollection.saveClassificationReferenceCopy(userId, requestBody.getEntityProxy(), requestBody.getClassification());
                }
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                          the metadata collection is stored.
     * TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                    hosting the metadata collection.
     * EntityConflictException the new entity conflicts with an existing entity.
     * InvalidEntityException the new entity has invalid contents.
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     * FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public  VoidResponse purgeClassificationReferenceCopy(String                          serverName,
                                                          String                          userId,
                                                          ClassificationWithEntityRequest requestBody)
    {
        final String methodName  = "purgeClassificationReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

                if (requestBody.getEntity() != null)
                {
                    metadataCollection.purgeClassificationReferenceCopy(userId, requestBody.getEntity(), requestBody.getClassification());
                }
                else
                {
                    metadataCollection.purgeClassificationReferenceCopy(userId, requestBody.getEntityProxy(), requestBody.getClassification());
                }
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting userId.
     * @param requestBody relationship to save.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse saveRelationshipReferenceCopy(String              serverName,
                                                      String              userId,
                                                      RelationshipRequest requestBody)
    {
        final  String   methodName = "saveRelationshipReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);


            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.saveRelationshipReferenceCopy(userId, requestBody.getRelationship());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param requestBody the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse deleteRelationshipReferenceCopy(String                        serverName,
                                                        String                        userId,
                                                        RelationshipRequest           requestBody)
    {
        final  String   methodName = "deleteRelationshipReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.deleteRelationshipReferenceCopy(userId, requestBody.getRelationship());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param requestBody the instance to purge.
     * @return VoidResponse:
     * void or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
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
    public VoidResponse purgeRelationshipReferenceCopy(String                        serverName,
                                                       String                        userId,
                                                       RelationshipRequest           requestBody)
    {
        final  String   methodName = "purgeRelationshipReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            if (requestBody != null)
            {
                metadataCollection.deleteRelationshipReferenceCopy(userId, requestBody.getRelationship());
            }
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identifier is not recognized or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse purgeRelationshipReferenceCopy(String                        serverName,
                                                       String                        userId,
                                                       String                        relationshipGUID,
                                                       String                        homeMetadataCollectionId,
                                                       TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "purgeRelationshipReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.purgeRelationshipReferenceCopy(userId,
                                                              relationshipGUID,
                                                              typeDefGUID,
                                                              typeDefName,
                                                              homeMetadataCollectionId);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
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
     * @param requestBody information about the type used to confirm the right instance is specified.
     * @return VoidResponse:
     * void or
     * InvalidParameterException one of the parameters is invalid or null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored or
     * RelationshipNotKnownException the relationship identifier is not recognized or
     * HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid or
     * FunctionNotSupportedException the repository does not support instance reference copies or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public VoidResponse refreshRelationshipReferenceCopy(String                        serverName,
                                                         String                        userId,
                                                         String                        relationshipGUID,
                                                         String                        homeMetadataCollectionId,
                                                         TypeDefValidationForRequest   requestBody)
    {
        final  String   methodName = "refreshRelationshipReferenceCopy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        String    typeDefGUID = null;
        String    typeDefName = null;

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            typeDefGUID = requestBody.getTypeDefGUID();
            typeDefName = requestBody.getTypeDefName();
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.refreshRelationshipReferenceCopy(userId,
                                                                relationshipGUID,
                                                                typeDefGUID,
                                                                typeDefName,
                                                                homeMetadataCollectionId);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Save the entities and relationships supplied in the instance graph as a reference copies.
     * The id of the home metadata collection is already set up in the instances.
     * Any instances from the home metadata collection are ignored.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting server.
     * @param requestBody instances to save
     * @return void response or
     * InvalidParameterException the relationship is null or
     * RepositoryErrorException  a problem communicating with the metadata repository where
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
    public VoidResponse  saveInstanceReferenceCopies(String               serverName,
                                                     String               userId,
                                                     InstanceGraphRequest requestBody)
    {
        final  String   methodName = "saveInstanceReferenceCopies";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName, requestBody);

        InstanceGraph instanceGraph = new InstanceGraph();

        VoidResponse response = new VoidResponse();

        if (requestBody != null)
        {
            instanceGraph.setEntities(requestBody.getEntityElementList());
            instanceGraph.setRelationships(requestBody.getRelationshipElementList());
        }

        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSMetadataCollection metadataCollection = validateRepository(userId, serverName, methodName);

            metadataCollection.saveInstanceReferenceCopies(userId, instanceGraph);
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }




    /*
     * =============================================================
     * Enterprise Methods
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
    public ConnectionResponse getEnterpriseOMRSTopicConnection(String userId,
                                                               String serverName)
    {
        final String methodName = "getEnterpriseOMRSTopicConnection";

        ConnectionResponse response = new ConnectionResponse();

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OMRSRepositoryServicesInstance instance = instanceHandler.getInstance(userId, serverName, methodName);

            response.setConnection(instance.getRemoteEnterpriseOMRSTopicConnection());
        }
        catch (Throwable error)
        {
            this.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /*
     * =============================================================
     * Private methods
     */


    /**
     * Validate that the repository connector is available.
     *
     * @param userId name of the calling user.
     * @param serverName name of the server associated with the request.
     * @param methodName method being called
     * @throws InvalidParameterException unknown servername
     * @throws UserNotAuthorizedException unsupported userId
     * @throws RepositoryErrorException null local repository
     * @return OMRSMetadataCollection object for the local repository
     */
    private OMRSMetadataCollection validateRepository(String userId,
                                                      String serverName,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                RepositoryErrorException
    {
        OMRSMetadataCollection   metadataCollection = null;

        if (serverName != null)
        {
            OMRSRepositoryServicesInstance instance = instanceHandler.getInstance(userId, serverName, methodName);

            if (instance != null)
            {
                if (localRepository)
                {
                    metadataCollection = instance.getLocalMetadataCollection();
                }
                else
                {
                    metadataCollection = instance.getEnterpriseMetadataCollection();
                }
            }
        }


        /*
         * If the local repository is not set up then do not attempt to process the request.
         */
        if (metadataCollection == null)
        {
            if (localRepository)
            {
                throw new RepositoryErrorException(OMRSErrorCode.NO_LOCAL_REPOSITORY.getMessageDefinition(methodName),
                                                   this.getClass().getName(),
                                                   methodName);
            }
            else
            {
               throw new RepositoryErrorException(OMRSErrorCode.NO_ENTERPRISE_REPOSITORY.getMessageDefinition(methodName),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return metadataCollection;
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse         response,
                                         OMFCheckedExceptionBase error,
                                         String                  exceptionClassName)
    {
        this.captureCheckedException(response, error, exceptionClassName, null);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     * @param exceptionProperties property map
     */
    private void captureCheckedException(OMRSAPIResponse         response,
                                         OMFCheckedExceptionBase error,
                                         String                  exceptionClassName,
                                         Map<String, Object>     exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionSubclassName(error.getClass().getName());
        response.setActionDescription(error.getReportingActionDescription());
        response.setExceptionErrorMessage(error.getReportedErrorMessage());
        response.setExceptionErrorMessageId(error.getReportedErrorMessageId());
        response.setExceptionErrorMessageParameters(error.getReportedErrorMessageParameters());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionCausedBy(error.getReportedCaughtExceptionClassName());
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    private void captureExceptions(OMRSAPIResponse response,
                                   Exception       error,
                                   String          methodName,
                                   AuditLog        auditLog)
    {
        if (error instanceof PropertyServerException propertyServerException)
        {
            capturePropertyServerException(response, propertyServerException);
        }
        else if (error instanceof UserNotAuthorizedException userNotAuthorizedException)
        {
            captureUserNotAuthorizedException(response, userNotAuthorizedException);
        }
        else if (error instanceof InvalidParameterException invalidParameterException)
        {
            captureInvalidParameterException(response, invalidParameterException);
        }
        else
        {
            String message = error.getMessage();

            if (message == null)
            {
                message = "null";
            }

            ExceptionMessageDefinition messageDefinition = OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                         methodName,
                                                                                                                         message);

            response.setRelatedHTTPCode(messageDefinition.getHttpErrorCode());
            response.setExceptionClassName(PropertyServerException.class.getName());
            response.setExceptionSubclassName(error.getClass().getName());
            response.setExceptionCausedBy(error.getClass().getName());
            response.setActionDescription(methodName);
            response.setExceptionErrorMessage(messageFormatter.getFormattedMessage(messageDefinition));
            response.setExceptionErrorMessageId(messageDefinition.getMessageId());
            response.setExceptionErrorMessageParameters(messageDefinition.getMessageParams());
            response.setExceptionSystemAction(messageDefinition.getSystemAction());
            response.setExceptionUserAction(messageDefinition.getUserAction());
            response.setExceptionProperties(null);

            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(), methodName, message),
                                      error);
            }
        }
    }

    /**
     * A runtime error occurred.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    public  void captureRuntimeExceptions(OMRSAPIResponse response,
                                          Throwable    error,
                                          String       methodName,
                                          AuditLog     auditLog)
    {
        if (error instanceof Exception exception)
        {
            /*
             * An exception is handleable ...
             */
            captureExceptions(response, exception, methodName, auditLog);
        }
        else
        {
            /*
             * An error exception or worse - this typically means that the JVM is in trouble and the platform
             * can not safely continue.
             */
            System.out.println("Throwable from " + methodName + " causing platform to exit");
            log.error("Throwable from " + methodName + " causing platform to exit", error);

            System.out.println(error.toString());
            System.exit(-1);
        }
    }




    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void captureInvalidParameterException(OMRSAPIResponse              response,
                                                  InvalidParameterException error)
    {
        Map<String, Object>  exceptionProperties = error.getRelatedProperties();

        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            if (exceptionProperties == null)
            {
                exceptionProperties = new HashMap<>();
            }

            exceptionProperties.put("parameterName", parameterName);
        }

        if (exceptionProperties != null)
        {
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, InvalidParameterException.class.getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void capturePropertyServerException(OMRSAPIResponse            response,
                                                PropertyServerException error)
    {
        captureCheckedException(response, error, PropertyServerException.class.getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void captureUserNotAuthorizedException(OMRSAPIResponse               response,
                                                   UserNotAuthorizedException error)
    {
        Map<String, Object>  exceptionProperties = error.getRelatedProperties();

        String  userId = error.getUserId();

        if (userId != null)
        {
            if (exceptionProperties == null)
            {
                exceptionProperties = new HashMap<>();
            }

            exceptionProperties.put("userId", userId);
        }

        if (exceptionProperties != null)
        {
            captureCheckedException(response, error, UserNotAuthorizedException.class.getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, UserNotAuthorizedException.class.getName());
        }
    }
}
