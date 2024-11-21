/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.client;

import org.odpi.openmetadata.accessservices.assetmanager.api.ExternalIdentifierManagerInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.ffdc.AssetManagerErrorCode;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.UpdateRequestBody;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.governanceaction.converters.MetadataElementSummaryConverter;
import org.odpi.openmetadata.frameworks.governanceaction.converters.MetadataRelationshipSummaryConverter;
import org.odpi.openmetadata.frameworks.governanceaction.converters.RelatedMetadataElementSummaryConverter;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AssetManagerBaseClient supports the common properties and functions for the Asset Manager OMAS.
 */
public class AssetManagerBaseClient implements ExternalIdentifierManagerInterface
{
    protected final String assetManagerGUIDParameterName = "assetManagerGUID";
    protected final String assetManagerNameParameterName = "assetManagerName";

    final protected String serverName;               /* Initialized in constructor */
    final protected String serverPlatformURLRoot;    /* Initialized in constructor */

    final protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    final protected PropertyHelper          propertyHelper          = new PropertyHelper();
    final protected AssetManagerRESTClient  restClient;               /* Initialized in constructor */
    final protected OpenMetadataStoreClient openMetadataStoreClient;   /* Initialized in constructor */
    final protected NullRequestBody nullRequestBody = new NullRequestBody();

    final protected String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}";

    final protected MetadataElementSummaryConverter<MetadataElementSummary>               metadataElementSummaryConverter;
    final protected RelatedMetadataElementSummaryConverter<RelatedMetadataElementSummary> relatedMetadataElementSummaryConverter;
    final protected MetadataRelationshipSummaryConverter<MetadataRelationshipSummary>     metadataRelationshipSummaryConverter;

    AuditLog auditLog = null;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetManagerBaseClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  AuditLog auditLog,
                                  int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.auditLog = auditLog;
        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot, auditLog);
        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                     serverName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                   serverName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               serverName);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetManagerBaseClient(String serverName,
                                  String serverPlatformURLRoot,
                                  int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot);

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                     serverName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                   serverName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetManagerBaseClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password,
                                  int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot, userId, password);

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                     serverName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                   serverName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetManagerBaseClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog,
                                  int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.auditLog = auditLog;
        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                     serverName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                   serverName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               serverName);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetManagerBaseClient(String                 serverName,
                                  String                 serverPlatformURLRoot,
                                  AssetManagerRESTClient restClient,
                                  int                    maxPageSize,
                                  AuditLog               auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (with REST client)";

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.auditLog = auditLog;
        this.restClient = restClient;

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                     serverName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                   serverName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               serverName);
    }

    /* ========================================================
     * Metadata correlation setup.
     */


    /**
     * Throw an exception because the asset manager's GUID has not been passed with an external identifier.
     *
     * @param externalIdentifier external identifier
     * @param methodName calling method
     * @throws InvalidParameterException resulting exception
     */
    protected void handleMissingScope(String externalIdentifier,
                                      String methodName) throws InvalidParameterException
    {
        final String externalIdentifierParameterName = "externalIdentifier";

        throw new InvalidParameterException(AssetManagerErrorCode.NO_SCOPE_FOR_EXTERNAL_ID.getMessageDefinition(externalIdentifier,
                                                                                                                methodName),
                                            this.getClass().getName(),
                                            methodName,
                                            externalIdentifierParameterName);
    }


    /**
     * Set up the correlation properties for update.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of the glossary in the external asset manager
     * @param methodName calling method
     * @return filled out correlation properties
     * @throws InvalidParameterException missing external identifier
     */
    protected MetadataCorrelationProperties getCorrelationProperties(String assetManagerGUID,
                                                                     String assetManagerName,
                                                                     String externalIdentifier,
                                                                     String methodName) throws InvalidParameterException
    {
        MetadataCorrelationProperties correlationProperties = new MetadataCorrelationProperties();

        if (assetManagerGUID != null)
        {
            correlationProperties.setExternalScopeGUID(assetManagerGUID);
            correlationProperties.setExternalScopeName(assetManagerName);
            correlationProperties.setExternalIdentifier(externalIdentifier);

            return correlationProperties;
        }
        else if (externalIdentifier != null)
        {
            handleMissingScope(externalIdentifier, methodName);
        }

        return correlationProperties;
    }


    /**
     * Set up the correlation properties for update.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of the glossary in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return filled out correlation properties
     * @throws InvalidParameterException missing external identifier
     */
    protected UpdateRequestBody getUpdateRequestBody(String assetManagerGUID,
                                                     String assetManagerName,
                                                     String externalIdentifier,
                                                     Date   effectiveTime,
                                                     String methodName) throws InvalidParameterException
    {
        UpdateRequestBody requestBody = new UpdateRequestBody();

        requestBody.setMetadataCorrelationProperties(getCorrelationProperties(assetManagerGUID, assetManagerName, externalIdentifier, methodName));
        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }


    /**
     * Set up the correlation properties for request.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param externalIdentifierName name of property for the external identifier in the external asset manager
     * @param externalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param externalIdentifierSource component that issuing this request.
     * @param externalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param methodName calling method
     * @return filled out correlation properties
     * @throws InvalidParameterException missing external identifier
     */
    protected MetadataCorrelationProperties getCorrelationProperties(String              assetManagerGUID,
                                                                     String              assetManagerName,
                                                                     String              externalIdentifier,
                                                                     String              externalIdentifierName,
                                                                     String              externalIdentifierUsage,
                                                                     String              externalIdentifierSource,
                                                                     KeyPattern          externalIdentifierKeyPattern,
                                                                     Map<String, String> mappingProperties,
                                                                     String              methodName) throws InvalidParameterException
    {
        MetadataCorrelationProperties correlationProperties = new MetadataCorrelationProperties();

        if (assetManagerGUID != null)
        {
            correlationProperties.setExternalScopeGUID(assetManagerGUID);
            correlationProperties.setExternalScopeName(assetManagerName);

            if (externalIdentifier != null)
            {
                correlationProperties.setKeyPattern(KeyPattern.LOCAL_KEY);
                correlationProperties.setExternalIdentifier(externalIdentifier);
                correlationProperties.setExternalIdentifierName(externalIdentifierName);
                correlationProperties.setExternalIdentifierUsage(externalIdentifierUsage);
                correlationProperties.setExternalIdentifierSource(externalIdentifierSource);
                correlationProperties.setKeyPattern(externalIdentifierKeyPattern);
                correlationProperties.setMappingProperties(mappingProperties);
            }
        }
        else if (externalIdentifier != null)
        {
            handleMissingScope(externalIdentifier, methodName);
        }

        return correlationProperties;
    }


    /**
     * Set up the correlation properties for request.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param methodName calling method
     * @return filled out correlation properties
     * @throws InvalidParameterException missing external identifier
     */
    protected MetadataCorrelationProperties getCorrelationProperties(String                       assetManagerGUID,
                                                                     String                       assetManagerName,
                                                                     ExternalIdentifierProperties externalIdentifierProperties,
                                                                     String                       methodName) throws InvalidParameterException
    {
        MetadataCorrelationProperties correlationProperties = new MetadataCorrelationProperties(externalIdentifierProperties);

        if (assetManagerGUID != null)
        {
            correlationProperties.setExternalScopeGUID(assetManagerGUID);
            correlationProperties.setExternalScopeName(assetManagerName);
        }
        else if ((externalIdentifierProperties != null) && (externalIdentifierProperties.getExternalIdentifier() != null))
        {
            handleMissingScope(externalIdentifierProperties.getExternalIdentifier(), methodName);
        }

        return correlationProperties;
    }


    /**
     * Return the asset manager identifiers packaged in an appropriate request body (or null if assetManagerGUID is null).
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @return request body
     */
    protected AssetManagerIdentifiersRequestBody getAssetManagerIdentifiersRequestBody(String assetManagerGUID,
                                                                                       String assetManagerName)
    {
        AssetManagerIdentifiersRequestBody requestBody = new AssetManagerIdentifiersRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setAssetManagerGUID(assetManagerGUID);
            requestBody.setAssetManagerName(assetManagerName);
        }

        return requestBody;
    }



    /**
     * Return the asset manager identifiers packaged in an appropriate request body.
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @return request body
     */
    protected EffectiveTimeQueryRequestBody getEffectiveTimeQueryRequestBody(String assetManagerGUID,
                                                                             String assetManagerName,
                                                                             Date   effectiveTime)
    {
        EffectiveTimeQueryRequestBody requestBody = new EffectiveTimeQueryRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setAssetManagerGUID(assetManagerGUID);
            requestBody.setAssetManagerName(assetManagerName);
        }

        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }



    /**
     * Return the asset manager identifiers packaged in an appropriate request body.
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @param relationshipTypeName optional name of relationship
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @return request body
     */
    protected GlossaryTermRelationshipRequestBody getGlossaryTermRelationshipRequestBody(String                               assetManagerGUID,
                                                                                         String                               assetManagerName,
                                                                                         String                               relationshipTypeName,
                                                                                         List<GlossaryTermRelationshipStatus> limitResultsByStatus,
                                                                                         Date                                 effectiveTime)
    {
        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setAssetManagerGUID(assetManagerGUID);
            requestBody.setAssetManagerName(assetManagerName);
        }

        requestBody.setRelationshipTypeName(relationshipTypeName);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }



    /**
     * Return the asset manager identifiers packaged with relevant properties in an appropriate request body.
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param relationshipProperties relationship properties
     * @return request body
     */
    protected RelationshipRequestBody getRelationshipRequestBody(String                 assetManagerGUID,
                                                                 String                 assetManagerName,
                                                                 Date                   effectiveTime,
                                                                 RelationshipProperties relationshipProperties)
    {
        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setExternalSourceGUID(assetManagerGUID);
            requestBody.setExternalSourceName(assetManagerName);
        }

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setProperties(relationshipProperties);

        return requestBody;
    }


    /**
     * Return the asset manager identifiers packaged with relevant properties in an appropriate request body.
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @param name name to search for
     * @param nameParameterName parameter name
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @return request body
     */
    protected NameRequestBody getNameRequestBody(String                 assetManagerGUID,
                                                 String                 assetManagerName,
                                                 String                 name,
                                                 String                 nameParameterName,
                                                 Date                   effectiveTime)
    {
        NameRequestBody requestBody = new NameRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setAssetManagerGUID(assetManagerGUID);
            requestBody.setAssetManagerName(assetManagerName);
        }

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }


    /**
     * Return the asset manager identifiers packaged with relevant properties in an appropriate request body.
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @param name name to search for
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return request body
     * @throws InvalidParameterException blank name
     */
    protected NameRequestBody getNameRequestBody(String                 assetManagerGUID,
                                                 String                 assetManagerName,
                                                 String                 name,
                                                 Date                   effectiveTime,
                                                 String                 methodName) throws InvalidParameterException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        return getNameRequestBody(assetManagerGUID, assetManagerName, name, nameParameterName, effectiveTime);
    }


    /**
     * Return the asset manager identifiers packaged with relevant properties in an appropriate request body.
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique qualifiedName for the asset manager
     * @param qualifiedName qualifiedName to search for
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @return request body
     */
    protected NameRequestBody getQualifiedNameRequestBody(String                 assetManagerGUID,
                                                          String                 assetManagerName,
                                                          String                 qualifiedName,
                                                          Date                   effectiveTime)
    {
        final String nameParameterName = "qualifiedName";

        return getNameRequestBody(assetManagerGUID, assetManagerName, qualifiedName, nameParameterName, effectiveTime);
    }


    /**
     * Return the asset manager identifiers packaged with relevant properties in an appropriate request body.
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @param searchString string to find in the properties
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return request body
     * @throws InvalidParameterException blank name
     */
    protected SearchStringRequestBody getSearchStringRequestBody(String                 assetManagerGUID,
                                                                 String                 assetManagerName,
                                                                 String                 searchString,
                                                                 Date                   effectiveTime,
                                                                 String                 methodName) throws InvalidParameterException
    {
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setAssetManagerGUID(assetManagerGUID);
            requestBody.setAssetManagerName(assetManagerName);
        }

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void addExternalIdentifier(String                       userId,
                                      String                       assetManagerGUID,
                                      String                       assetManagerName,
                                      String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        this.addExternalIdentifier(userId, assetManagerGUID, assetManagerName, OpenMetadataType.INVENTORY_CATALOG.typeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties);
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerTypeName type name of the software capability describing the asset manager
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void addExternalIdentifier(String                       userId,
                                      String                       assetManagerGUID,
                                      String                       assetManagerName,
                                      String                       assetManagerTypeName,
                                      String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName                      = "addExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String openMetadataTypeParameterName   = "openMetadataElementTypeName";
        final String externalIdentifierPropertiesParameterName = "externalIdentifierProperties";
        final String externalIdentifierParameterName = "externalIdentifierProperties.externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(assetManagerName, assetManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateName(openMetadataElementTypeName, openMetadataTypeParameterName, methodName);
        invalidParameterHandler.validateObject(externalIdentifierProperties, externalIdentifierPropertiesParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifierProperties.getExternalIdentifier(), externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/asset-managers/elements/{2}/{3}/external-identifiers/add";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties(externalIdentifierProperties);

        requestBody.setExternalScopeGUID(assetManagerGUID);
        requestBody.setExternalScopeName(assetManagerName);
        requestBody.setExternalScopeTypeName(assetManagerTypeName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void updateExternalIdentifier(String                       userId,
                                         String                       assetManagerGUID,
                                         String                       assetManagerName,
                                         String                       openMetadataElementGUID,
                                         String                       openMetadataElementTypeName,
                                         ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName                      = "updateExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String openMetadataTypeParameterName   = "openMetadataElementTypeName";
        final String externalIdentifierPropertiesParameterName = "externalIdentifierProperties";
        final String externalIdentifierParameterName = "externalIdentifierProperties.externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(assetManagerName, assetManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateName(openMetadataElementTypeName, openMetadataTypeParameterName, methodName);
        invalidParameterHandler.validateObject(externalIdentifierProperties, externalIdentifierPropertiesParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifierProperties.getExternalIdentifier(), externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/asset-managers/elements/{2}/{3}/external-identifiers/update";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties(externalIdentifierProperties);

        requestBody.setExternalScopeGUID(assetManagerGUID);
        requestBody.setExternalScopeName(assetManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void removeExternalIdentifier(String                   userId,
                                         String                   assetManagerGUID,
                                         String                   assetManagerName,
                                         String                   openMetadataElementGUID,
                                         String                   openMetadataElementTypeName,
                                         String                   externalIdentifier) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                      = "removeExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String openMetadataTypeParameterName   = "openMetadataElementTypeName";
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(assetManagerName, assetManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateName(openMetadataElementTypeName, openMetadataTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifier, externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/asset-managers/elements/{2}/{3}/external-identifiers/remove";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties();

        requestBody.setExternalScopeGUID(assetManagerGUID);
        requestBody.setExternalScopeName(assetManagerName);
        requestBody.setExternalIdentifier(externalIdentifier);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void confirmSynchronization(String userId,
                                       String assetManagerGUID,
                                       String assetManagerName,
                                       String openMetadataElementGUID,
                                       String openMetadataElementTypeName,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                      = "confirmSynchronization";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(assetManagerName, assetManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifier, externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/asset-managers/elements/{2}/{3}/synchronized";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties();

        requestBody.setExternalScopeGUID(assetManagerGUID);
        requestBody.setExternalScopeName(assetManagerName);
        requestBody.setExternalIdentifier(externalIdentifier);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID);
    }


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public List<ElementHeader> getElementsForExternalIdentifier(String userId,
                                                                String assetManagerGUID,
                                                                String assetManagerName,
                                                                String externalIdentifier,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                      = "getElementsForExternalIdentifier";
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(assetManagerName, assetManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifier, externalIdentifierParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/asset-managers/external-identifiers/open-metadata-elements?startFrom={2}&pageSize={3}";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties();

        requestBody.setExternalScopeGUID(assetManagerGUID);
        requestBody.setExternalScopeName(assetManagerName);
        requestBody.setExternalIdentifier(externalIdentifier);

        ElementHeadersResponse restResult = restClient.callElementHeadersPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementHeaders();
    }



    /**
     * Assemble the correlation headers attached to the supplied element guid.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     *
     * @return list of correlation headers (note if asset manager identifiers are present, only the matching correlation header is returned)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    protected List<MetadataCorrelationHeader> getMetadataCorrelationHeaders(String userId,
                                                                            String assetManagerGUID,
                                                                            String assetManagerName,
                                                                            String openMetadataElementGUID,
                                                                            String openMetadataElementTypeName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        final String methodName = "getMetadataCorrelationHeaders";
        final String guidParameterName = "openMetadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/asset-managers/elements/{2}/{3}/correlation-headers";

        MetadataCorrelationHeadersResponse restResult = restClient.callMyCorrelationHeadersPostRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, null),
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        openMetadataElementTypeName,
                                                                                                        openMetadataElementGUID);

        return restResult.getElementList();
    }


    /* =====================================================================================================================
     * Basic client methods
     */


    /**
     * Create a new metadata element that does not link to another element on create.
     *
     * @param userId                  calling user
     * @param assetManagerGUID      unique identifier of software capability representing the caller
     * @param assetManagerName      unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param properties              properties about the element to store
     * @param propertiesParameterName name of parameter passing the properties
     * @param urlTemplate             URL to call (no expected placeholders)
     * @param methodName              calling method
     *
     * @return unique identifier of the new element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createReferenceable(String                       userId,
                                         String                       assetManagerGUID,
                                         String                       assetManagerName,
                                         boolean                      assetManagerIsHome,
                                         ExternalIdentifierProperties externalIdentifierProperties,
                                         ReferenceableProperties      properties,
                                         String                       propertiesParameterName,
                                         String                       urlTemplate,
                                         String                       methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ReferenceableRequestBody requestBody = new ReferenceableRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setElementProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId             calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate        URL to call (with placeholders)
     * @param methodName         calling method
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createReferenceableFromTemplate(String                       userId,
                                                     String                       assetManagerGUID,
                                                     String                       assetManagerName,
                                                     boolean                      assetManagerIsHome,
                                                     String                       templateGUID,
                                                     TemplateProperties           templateProperties,
                                                     ExternalIdentifierProperties externalIdentifierProperties,
                                                     String                       urlTemplate,
                                                     String                       methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String propertiesParameterName    = "templateProperties";
        final String qualifiedNameParameterName = "templateProperties.qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId             calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate        URL to call (with placeholders)
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName         calling method
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createReferenceableFromTemplate(String                       userId,
                                                     String                       assetManagerGUID,
                                                     String                       assetManagerName,
                                                     boolean                      assetManagerIsHome,
                                                     String                       templateGUID,
                                                     TemplateProperties           templateProperties,
                                                     ExternalIdentifierProperties externalIdentifierProperties,
                                                     String                       urlTemplate,
                                                     boolean                      deepCopy,
                                                     String                       methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String propertiesParameterName    = "templateProperties";
        final String qualifiedNameParameterName = "templateProperties.qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={3}&deepCopy={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome,
                                                                  deepCopy);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId             calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param parentGUID              unique identifier of the parent element
     * @param parentGUIDParameterName name of parameter passing the parentGUID
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate        URL to call (with placeholders)
     * @param methodName         calling method
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createReferenceableFromTemplateWithParent(String                       userId,
                                                               String                       assetManagerGUID,
                                                               String                       assetManagerName,
                                                               boolean                      assetManagerIsHome,
                                                               String                       parentGUID,
                                                               String                       parentGUIDParameterName,
                                                               String                       templateGUID,
                                                               TemplateProperties           templateProperties,
                                                               ExternalIdentifierProperties externalIdentifierProperties,
                                                               String                       urlTemplate,
                                                               String                       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String propertiesParameterName    = "templateProperties";
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setParentGUID(parentGUID);
        requestBody.setElementProperties(templateProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentGUID,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }



    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId             calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param parentGUID              unique identifier of the parent element
     * @param parentGUIDParameterName name of parameter passing the parentGUID
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate        URL to call (with placeholders)
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName         calling method
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createReferenceableFromTemplateWithParent(String                       userId,
                                                               String                       assetManagerGUID,
                                                               String                       assetManagerName,
                                                               boolean                      assetManagerIsHome,
                                                               String                       parentGUID,
                                                               String                       parentGUIDParameterName,
                                                               String                       templateGUID,
                                                               TemplateProperties           templateProperties,
                                                               ExternalIdentifierProperties externalIdentifierProperties,
                                                               boolean                      deepCopy,
                                                               String                       urlTemplate,
                                                               String                       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String propertiesParameterName    = "templateProperties";
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={4}&deepCopy={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setParentGUID(parentGUID);
        requestBody.setElementProperties(templateProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentGUID,
                                                                  templateGUID,
                                                                  assetManagerIsHome,
                                                                  deepCopy);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId             calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param parentGUID              unique identifier of the parent element
     * @param parentGUIDParameterName name of parameter passing the parentGUID
     * @param isPublic                is this element visible to other people.
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate        URL to call (with placeholders)
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName         calling method
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createFeedbackFromTemplateWithParent(String                       userId,
                                                          String                       assetManagerGUID,
                                                          String                       assetManagerName,
                                                          boolean                      assetManagerIsHome,
                                                          String                       parentGUID,
                                                          String                       parentGUIDParameterName,
                                                          boolean                      isPublic,
                                                          String                       templateGUID,
                                                          TemplateProperties           templateProperties,
                                                          ExternalIdentifierProperties externalIdentifierProperties,
                                                          boolean                      deepCopy,
                                                          String                       urlTemplate,
                                                          String                       methodName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String propertiesParameterName    = "templateProperties";
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={4}&deepCopy={5}&isPublic={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setParentGUID(parentGUID);
        requestBody.setElementProperties(templateProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentGUID,
                                                                  templateGUID,
                                                                  assetManagerIsHome,
                                                                  deepCopy,
                                                                  isPublic);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element that is attached to the parent.
     *
     * @param userId                  calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param parentGUID              unique identifier of the parent element
     * @param parentGUIDParameterName name of parameter passing the parentGUID
     * @param properties              properties about the element to store
     * @param propertiesParameterName name of parameter passing the properties
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate             URL to call (no expected placeholders)
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName              calling method
     *
     * @return unique identifier of the new element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createReferenceableWithParent(String                       userId,
                                                   String                       assetManagerGUID,
                                                   String                       assetManagerName,
                                                   boolean                      assetManagerIsHome,
                                                   String                       parentGUID,
                                                   String                       parentGUIDParameterName,
                                                   ReferenceableProperties      properties,
                                                   String                       propertiesParameterName,
                                                   ExternalIdentifierProperties externalIdentifierProperties,
                                                   String                       urlTemplate,
                                                   Date                         effectiveTime,
                                                   boolean                      forLineage,
                                                   boolean                      forDuplicateProcessing,
                                                   String                       methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?assetManagerIsHome={3}&forLineage={4}&forDuplicateProcessing={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ReferenceableUpdateRequestBody requestBody = new ReferenceableUpdateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setParentGUID(parentGUID);
        requestBody.setElementProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentGUID,
                                                                  assetManagerIsHome,
                                                                  forLineage,
                                                                  forDuplicateProcessing);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element that is attached to the parent.
     *
     * @param userId                  calling user
     * @param assetManagerGUID        unique identifier of software capability representing the caller
     * @param assetManagerName        unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param parentGUID              unique identifier of the parent element
     * @param parentGUIDParameterName name of parameter passing the parentGUID
     * @param isPublic                is this element visible to other people.
     * @param properties              properties about the element to store
     * @param propertiesParameterName name of parameter passing the properties
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param urlTemplate             URL to call (no expected placeholders)
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param forLineage              return elements marked with the Memento classification?
     * @param forDuplicateProcessing  do not merge elements marked as duplicates?
     * @param methodName              calling method
     *
     * @return unique identifier of the new element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected String createFeedbackWithParent(String                       userId,
                                              String                       assetManagerGUID,
                                              String                       assetManagerName,
                                              boolean                      assetManagerIsHome,
                                              String                       parentGUID,
                                              String                       parentGUIDParameterName,
                                              boolean                      isPublic,
                                              ReferenceableProperties      properties,
                                              String                       propertiesParameterName,
                                              ExternalIdentifierProperties externalIdentifierProperties,
                                              String                       urlTemplate,
                                              Date                         effectiveTime,
                                              boolean                      forLineage,
                                              boolean                      forDuplicateProcessing,
                                              String                       methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String requestParamsURLTemplate = "?assetManagerIsHome={3}&isPublic={4}&forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        ReferenceableUpdateRequestBody requestBody = new ReferenceableUpdateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setParentGUID(parentGUID);
        requestBody.setElementProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate + requestParamsURLTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentGUID,
                                                                  assetManagerIsHome,
                                                                  isPublic,
                                                                  forLineage,
                                                                  forDuplicateProcessing);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId                   calling user
     * @param assetManagerGUID         unique identifier of software capability representing the caller
     * @param assetManagerName         unique name of software capability representing the caller
     * @param elementGUID              unique identifier of the metadata element to update
     * @param elementGUIDParameterName name of parameter passing the elementGUID
     * @param externalIdentifierName   optional external identifier
     * @param isMergeUpdate            should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties               new properties for the metadata element
     * @param propertiesParameterName  name of parameter passing the properties
     * @param urlTemplate              URL to call (no expected placeholders)
     * @param effectiveTime            the time that the retrieved elements must be effective for
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param methodName               calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void updateReferenceable(String                       userId,
                                       String                       assetManagerGUID,
                                       String                       assetManagerName,
                                       String                       elementGUID,
                                       String                       elementGUIDParameterName,
                                       String                       externalIdentifierName,
                                       boolean                      isMergeUpdate,
                                       ReferenceableProperties      properties,
                                       String                       propertiesParameterName,
                                       String                       urlTemplate,
                                       Date                         effectiveTime,
                                       boolean                      forLineage,
                                       boolean                      forDuplicateProcessing,
                                       String                       methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        ReferenceableUpdateRequestBody requestBody = new ReferenceableUpdateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierName,
                                                                                   methodName));
        requestBody.setElementProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the metadata element.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId                   calling user
     * @param assetManagerGUID         unique identifier of software capability representing the caller
     * @param assetManagerName         unique name of software capability representing the caller
     * @param elementGUID              unique identifier of the metadata element to update
     * @param elementGUIDParameterName name of parameter passing the elementGUID
     * @param externalIdentifierName   optional external identifier
     * @param isMergeUpdate            should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic                 is this element visible to other people.
     * @param properties               new properties for the metadata element
     * @param propertiesParameterName  name of parameter passing the properties
     * @param urlTemplate              URL to call (no expected placeholders)
     * @param effectiveTime            the time that the retrieved elements must be effective for
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param methodName               calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void updateFeedback(String                       userId,
                                  String                       assetManagerGUID,
                                  String                       assetManagerName,
                                  String                       elementGUID,
                                  String                       elementGUIDParameterName,
                                  String                       externalIdentifierName,
                                  boolean                      isMergeUpdate,
                                  boolean                      isPublic,
                                  ReferenceableProperties      properties,
                                  String                       propertiesParameterName,
                                  String                       urlTemplate,
                                  Date                         effectiveTime,
                                  boolean                      forLineage,
                                  boolean                      forDuplicateProcessing,
                                  String                       methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";
        final String requestParamsURLTemplate   = "?isMergeUpdate={3}&isPublic={4}&forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        ReferenceableUpdateRequestBody requestBody = new ReferenceableUpdateRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierName,
                                                                                   methodName));
        requestBody.setElementProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        isMergeUpdate,
                                        isPublic,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Add or update classification on referenceable.
     *
     * @param userId       calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to classify
     * @param elementGUIDParameter parameter name for elementGUID
     * @param externalIdentifierName optional name used to define an external identifier
     * @param properties  properties of security at the site
     * @param urlTemplate URL to call with placeholder for guid
     * @param effectiveTime    the time that the retrieved elements must be effective for
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void setReferenceableClassification(String                       userId,
                                                  String                       assetManagerGUID,
                                                  String                       assetManagerName,
                                                  String                       elementGUID,
                                                  String                       elementGUIDParameter,
                                                  String                       externalIdentifierName,
                                                  ClassificationProperties     properties,
                                                  String                       urlTemplate,
                                                  Date                         effectiveTime,
                                                  boolean                      forLineage,
                                                  boolean                      forDuplicateProcessing,
                                                  String                       methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException

    {
        final String requestParamsURLTemplate   = "?forLineage={3}&forDuplicateProcessing={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        ClassificationRequestBody requestBody = new ClassificationRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierName,
                                                                                   methodName));
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove classification from the referenceable.
     *
     * @param userId       calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to classify
     * @param elementGUIDParameter parameter name for elementGUID
     * @param externalIdentifierName optional name used to define an external identifier
     * @param urlTemplate URL to call with placeholder for guid
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void removeReferenceableClassification(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  elementGUID,
                                                     String  elementGUIDParameter,
                                                     String  externalIdentifierName,
                                                     String  urlTemplate,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException

    {
        final String requestParamsURLTemplate   = "?forLineage={3}&forDuplicateProcessing={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        ClassificationRequestBody requestBody = new ClassificationRequestBody();

        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierName,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between a primary element and a secondary element.
     *
     * @param userId                            calling user
     * @param assetManagerGUID                  unique identifier of software capability representing the caller
     * @param assetManagerName                  unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param properties                        describes the properties for the relationship
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage                        return elements marked with the Memento classification?
     * @param forDuplicateProcessing            do not merge elements marked as duplicates?
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void setupRelationship(String                 userId,
                                     String                 assetManagerGUID,
                                     String                 assetManagerName,
                                     String                 primaryElementGUID,
                                     String                 primaryElementGUIDParameterName,
                                     RelationshipProperties properties,
                                     String                 secondaryElementGUID,
                                     String                 secondaryElementGUIDParameterName,
                                     String                 urlTemplate,
                                     Date                   effectiveTime,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     String                 methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String requestParamsURLTemplate = "?forLineage={4}&forDuplicateProcessing={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setExternalSourceGUID(assetManagerGUID);
        requestBody.setExternalSourceName(assetManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        primaryElementGUID,
                                        secondaryElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between a primary element and a secondary element.
     *
     * @param userId                            calling user
     * @param assetManagerGUID                unique identifier of software capability representing the caller
     * @param assetManagerName                unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param relationshipTypeName              type of relationship to create
     * @param relationshipTypeNameParameterName name of the parameter passing relationshipTypeName
     * @param properties                        describes the properties for the relationship
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void setupRelationship(String                 userId,
                                     String                 assetManagerGUID,
                                     String                 assetManagerName,
                                     String                 primaryElementGUID,
                                     String                 primaryElementGUIDParameterName,
                                     String                 relationshipTypeName,
                                     String                 relationshipTypeNameParameterName,
                                     RelationshipProperties properties,
                                     String                 secondaryElementGUID,
                                     String                 secondaryElementGUIDParameterName,
                                     String                 urlTemplate,
                                     Date                   effectiveTime,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     String                 methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String requestParamsURLTemplate   = "?forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setExternalSourceGUID(assetManagerGUID);
        requestBody.setExternalSourceName(assetManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        primaryElementGUID,
                                        relationshipTypeName,
                                        secondaryElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship.
     *
     * @param userId                            calling user
     * @param assetManagerGUID                unique identifier of software capability representing the caller
     * @param assetManagerName                unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void clearRelationship(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  primaryElementGUID,
                                     String  primaryElementGUIDParameterName,
                                     String  secondaryElementGUID,
                                     String  secondaryElementGUIDParameterName,
                                     String  urlTemplate,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String requestParamsURLTemplate   = "?forLineage={4}&forDuplicateProcessing={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        primaryElementGUID,
                                        secondaryElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship.
     *
     * @param userId                            calling user
     * @param assetManagerGUID                unique identifier of software capability representing the caller
     * @param assetManagerName                unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param relationshipTypeName              type of relationship to create
     * @param relationshipTypeNameParameterName name of the parameter passing relationshipTypeName
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void clearRelationship(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  primaryElementGUID,
                                     String  primaryElementGUIDParameterName,
                                     String  relationshipTypeName,
                                     String  relationshipTypeNameParameterName,
                                     String  secondaryElementGUID,
                                     String  secondaryElementGUIDParameterName,
                                     String  urlTemplate,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String requestParamsURLTemplate = "?forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        primaryElementGUID,
                                        relationshipTypeName,
                                        secondaryElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve a relationship.
     *
     * @param userId   calling user
     * @param startingElementGUID   unique identifier of the primary element
     * @param startingAtEnd   0=either, or 1 or 2
     * @param relationshipTypeName  URL to call (no expected placeholders)
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected List<RelatedMetadataElementSummary> getRelatedElements(String          userId,
                                                                     String          startingElementGUID,
                                                                     int             startingAtEnd,
                                                                     String          relationshipTypeName,
                                                                     int             startFrom,
                                                                     int             pageSize,
                                                                     Date            effectiveTime,
                                                                     boolean         forLineage,
                                                                     boolean         forDuplicateProcessing,
                                                                     String          methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        List<RelatedMetadataElement> relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                  startingElementGUID,
                                                                                                                  startingAtEnd,
                                                                                                                  relationshipTypeName,
                                                                                                                  forLineage,
                                                                                                                  forDuplicateProcessing,
                                                                                                                  effectiveTime,
                                                                                                                  startFrom,
                                                                                                                  pageSize);

        return relatedMetadataElementSummaryConverter.getNewBeans(RelatedMetadataElementSummary.class,
                                                                  relatedMetadataElements,
                                                                  methodName);
    }


    /**
     * Retrieve a collection of classified elements.
     *
     * @param userId   calling user
     * @param assetManagerGUID                unique identifier of software capability representing the caller
     * @param assetManagerName                unique name of software capability representing the caller
     * @param properties                       values to search on
     * @param urlTemplate  URL to call (no expected placeholders)
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected List<ElementStub> getClassifiedElements(String         userId,
                                                      String         assetManagerGUID,
                                                      String         assetManagerName,
                                                      FindProperties properties,
                                                      String         urlTemplate,
                                                      int            startFrom,
                                                      int            pageSize,
                                                      Date           effectiveTime,
                                                      boolean        forLineage,
                                                      boolean        forDuplicateProcessing,
                                                      String         methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String requestParamsURLTemplate = "?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        FindByPropertiesRequestBody requestBody = new FindByPropertiesRequestBody();

        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setProperties(properties);

        ElementStubsResponse restResult = restClient.callElementStubsPostRESTCall(methodName,
                                                                                  urlTemplate + requestParamsURLTemplate,
                                                                                  requestBody,
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElements();
    }


    /**
     * Retrieve a collection of classified elements that also have specific classification properties.
     * It is also possible to restrict the results using the entity type name.
     *
     * @param userId   calling user
     * @param classificationName  name of the classification to search for
     * @param entityTypeName optional type name to restrict the search by
     * @param propertyValue value to search for in the classification
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected List<MetadataElementSummary> getClassifiedElements(String         userId,
                                                                 String         classificationName,
                                                                 String         entityTypeName,
                                                                 String         propertyValue,
                                                                 String         propertyName,
                                                                 int            startFrom,
                                                                 int            pageSize,
                                                                 Date           effectiveTime,
                                                                 boolean        forLineage,
                                                                 boolean        forDuplicateProcessing,
                                                                 String         methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String classificationNameProperty = "classificationName";
        final String propertyValueProperty = "propertyValue";
        final String propertyNameProperty = "propertyName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);
        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameProperty, methodName);

        List<OpenMetadataElement> elements = openMetadataStoreClient.getMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                      entityTypeName,
                                                                                                                      null,
                                                                                                                      classificationName,
                                                                                                                      propertyName,
                                                                                                                      propertyValue,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                      forLineage,
                                                                                                                      forDuplicateProcessing,
                                                                                                                      effectiveTime,
                                                                                                                      startFrom,
                                                                                                                      pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve a collection of classified elements that also have specific classification properties.
     * It is also possible to restrict the results using the entity type name.
     *
     * @param userId   calling user
     * @param classificationName  name of the classification to search for
     * @param entityTypeName optional type name to restrict the search by
     * @param propertyValue value to search for in the classification
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected List<MetadataElementSummary> getClassifiedElements(String         userId,
                                                                 String         classificationName,
                                                                 String         entityTypeName,
                                                                 int            propertyValue,
                                                                 String         propertyName,
                                                                 int            startFrom,
                                                                 int            pageSize,
                                                                 Date           effectiveTime,
                                                                 boolean        forLineage,
                                                                 boolean        forDuplicateProcessing,
                                                                 String         methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String classificationNameProperty = "classificationName";
        final String propertyValueProperty = "propertyValue";
        final String propertyNameProperty = "propertyName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);
        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameProperty, methodName);

        List<OpenMetadataElement> elements = openMetadataStoreClient.getMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                      entityTypeName,
                                                                                                                      null,
                                                                                                                      classificationName,
                                                                                                                      propertyName,
                                                                                                                      propertyValue,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                      forLineage,
                                                                                                                      forDuplicateProcessing,
                                                                                                                      effectiveTime,
                                                                                                                      startFrom,
                                                                                                                      pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve a collection of classified elements that also have specific classification properties.
     * It is also possible to restrict the results using the entity type name.
     *
     * @param userId   calling user
     * @param classificationName  name of the classification to search for
     * @param entityTypeName optional type name to restrict the search by
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime                     the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected List<MetadataElementSummary> getClassifiedElements(String         userId,
                                                                 String         classificationName,
                                                                 String         entityTypeName,
                                                                 int            startFrom,
                                                                 int            pageSize,
                                                                 Date           effectiveTime,
                                                                 boolean        forLineage,
                                                                 boolean        forDuplicateProcessing,
                                                                 String         methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String classificationNameProperty = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);

        List<OpenMetadataElement> elements = openMetadataStoreClient.getMetadataElementsByClassification(userId,
                                                                                                         entityTypeName,
                                                                                                         null,
                                                                                                         classificationName,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         SequencingOrder.LAST_UPDATE_RECENT,
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         effectiveTime,
                                                                                                         startFrom,
                                                                                                         pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Remove the metadata element.
     *
     * @param userId                   calling user
     * @param assetManagerGUID         unique identifier of software capability representing the caller
     * @param assetManagerName         unique name of software capability representing the caller
     * @param elementGUID              unique identifier of the metadata element to remove
     * @param elementGUIDParameterName name of parameter passing the elementGUID
     * @param urlTemplate              URL to call (no expected placeholders)
     * @param externalIdentifierName   unique identifier of the element in the external asset manager
     * @param effectiveTime            the time that the retrieved elements must be effective for
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param methodName               calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void removeReferenceable(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  elementGUID,
                                       String  elementGUIDParameterName,
                                       String  externalIdentifierName,
                                       String  urlTemplate,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String requestParamsURLTemplate   = "?forLineage={3}&forDuplicateProcessing={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);


        ReferenceableUpdateRequestBody requestBody = new ReferenceableUpdateRequestBody();
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierName,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate + requestParamsURLTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }
}
