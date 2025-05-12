/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.SchemaExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementHeaderResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.PrimaryKeyClassificationRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

import java.util.Date;
import java.util.List;

/**
 * ExchangeClientBase provides the base class for the clients that implement SchemaExchangeInterface
 */
public class SchemaExchangeClientBase extends ExchangeClientBase implements SchemaExchangeInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SchemaExchangeClientBase(String   serverName,
                                    String   serverPlatformURLRoot,
                                    AuditLog auditLog,
                                    int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SchemaExchangeClientBase(String serverName,
                                    String serverPlatformURLRoot,
                                    int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SchemaExchangeClientBase(String   serverName,
                                    String   serverPlatformURLRoot,
                                    String   userId,
                                    String   password,
                                    AuditLog auditLog,
                                    int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SchemaExchangeClientBase(String                 serverName,
                                    String                 serverPlatformURLRoot,
                                    AssetManagerRESTClient restClient,
                                    int                    maxPageSize,
                                    AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SchemaExchangeClientBase(String serverName,
                                    String serverPlatformURLRoot,
                                    String userId,
                                    String password,
                                    int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }



    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaTypeProperties properties about the schema type to store
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaType(String                       userId,
                                   String                       assetManagerGUID,
                                   String                       assetManagerName,
                                   boolean                      assetManagerIsHome,
                                   ExternalIdentifierProperties externalIdentifierProperties,
                                   boolean                      forLineage,
                                   boolean                      forDuplicateProcessing,
                                   SchemaTypeProperties         schemaTypeProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createSchemaType";

        return this.createSchemaType(userId,
                                     assetManagerGUID,
                                     assetManagerName,
                                     assetManagerIsHome,
                                     null,
                                     externalIdentifierProperties,
                                     forLineage,
                                     forDuplicateProcessing,
                                     schemaTypeProperties,
                                     methodName);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaTypeProperties properties about the schema type to store
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAnchoredSchemaType(String                       userId,
                                           String                       assetManagerGUID,
                                           String                       assetManagerName,
                                           boolean                      assetManagerIsHome,
                                           String                       anchorGUID,
                                           ExternalIdentifierProperties externalIdentifierProperties,
                                           boolean                      forLineage,
                                           boolean                      forDuplicateProcessing,
                                           SchemaTypeProperties         schemaTypeProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "createAnchoredSchemaType";

        return this.createSchemaType(userId,
                                     assetManagerGUID,
                                     assetManagerName,
                                     assetManagerIsHome,
                                     anchorGUID,
                                     externalIdentifierProperties,
                                     forLineage,
                                     forDuplicateProcessing,
                                     schemaTypeProperties,
                                     methodName);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaTypeProperties properties about the schema type to store
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private String createSchemaType(String                       userId,
                                    String                       assetManagerGUID,
                                    String                       assetManagerName,
                                    boolean                      assetManagerIsHome,
                                    String                       anchorGUID,
                                    ExternalIdentifierProperties externalIdentifierProperties,
                                    boolean                      forLineage,
                                    boolean                      forDuplicateProcessing,
                                    SchemaTypeProperties         schemaTypeProperties,
                                    String                       methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setElementProperties(schemaTypeProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types?assetManagerIsHome={2}&forLineage={3}&forDuplicateProcessing={4}&anchorGUID={5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  anchorGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaTypeFromTemplate(String                       userId,
                                               String                       assetManagerGUID,
                                               String                       assetManagerName,
                                               boolean                      assetManagerIsHome,
                                               String                       templateGUID,
                                               ExternalIdentifierProperties externalIdentifierProperties,
                                               TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                  = "createSchemaTypeFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types/from-template/{2}?assetManagerIsHome={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param schemaTypeExternalIdentifier unique identifier of the schema type in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateSchemaType(String               userId,
                                 String               assetManagerGUID,
                                 String               assetManagerName,
                                 String               schemaTypeGUID,
                                 String               schemaTypeExternalIdentifier,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties,
                                 Date                 effectiveTime,
                                 boolean              forLineage,
                                 boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                  = "updateSchemaType";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setElementProperties(schemaTypeProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   schemaTypeExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaTypeGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Connect a schema type to a data asset, process or port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param properties properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupSchemaTypeParent(String                 userId,
                                      String                 assetManagerGUID,
                                      String                 assetManagerName,
                                      boolean                assetManagerIsHome,
                                      String                 schemaTypeGUID,
                                      String                 parentElementGUID,
                                      String                 parentElementTypeName,
                                      Date                   effectiveTime,
                                      boolean                forLineage,
                                      boolean                forDuplicateProcessing,
                                      RelationshipProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                     = "setupSchemaTypeParent";
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/parents/{2}/{3}/schema-types/{4}?assetManagerIsHome={5}&forLineage={6}&forDuplicateProcessing={7}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        parentElementGUID,
                                        parentElementTypeName,
                                        schemaTypeGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearSchemaTypeParent(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  schemaTypeGUID,
                                      String  parentElementGUID,
                                      String  parentElementTypeName,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                     = "clearSchemaTypeParent";
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/parents/{2}/{3}/schema-types/{4}/remove?forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        parentElementGUID,
                                        parentElementTypeName,
                                        schemaTypeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupSchemaElementRelationship(String                 userId,
                                               String                 assetManagerGUID,
                                               String                 assetManagerName,
                                               boolean                assetManagerIsHome,
                                               String                 endOneGUID,
                                               String                 endTwoGUID,
                                               String                 relationshipTypeName,
                                               Date                   effectiveTime,
                                               boolean                forLineage,
                                               boolean                forDuplicateProcessing,
                                               RelationshipProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                        = "setupSchemaElementRelationship";
        final String endOneGUIDParameterName           = "endOneGUID";
        final String endTwoGUIDParameterName           = "endTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endOneGUID, endOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(endTwoGUID, endTwoGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/relationships/{3}/schema-elements/{4}?assetManagerIsHome={5}&forLineage={6}&forDuplicateProcessing={7}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        endOneGUID,
                                        relationshipTypeName,
                                        endTwoGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearSchemaElementRelationship(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  endOneGUID,
                                               String  endTwoGUID,
                                               String  relationshipTypeName,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                        = "clearSchemaElementRelationship";
        final String endOneGUIDParameterName           = "endOneGUID";
        final String endTwoGUIDParameterName           = "endTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endOneGUID, endOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(endTwoGUID, endTwoGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/relationships/{3}/schema-elements/{4}/remove?forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        endOneGUID,
                                        relationshipTypeName,
                                        endTwoGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param schemaTypeExternalIdentifier unique identifier of the schema type in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeSchemaType(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  schemaTypeGUID,
                                 String  schemaTypeExternalIdentifier,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                  = "removeSchemaType";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, schemaTypeExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        schemaTypeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaTypeElement> findSchemaType(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  String  searchString,
                                                  int     startFrom,
                                                  int     pageSize,
                                                  Date    effectiveTime,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                = "findSchemaType";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        SchemaTypeElementsResponse restResult = restClient.callMySchemaTypesPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       startFrom,
                                                                                       validatedPageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SchemaTypeElement getSchemaTypeForElement(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  parentElementGUID,
                                                     String  parentElementTypeName,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName        = "getSchemaTypeForElement";
        final String guidParameterName = "parentElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/parents/{2}/{3}/schema-types/retrieve?forLineage={4}&forDuplicateProcessing={5}";

        SchemaTypeElementResponse restResult = restClient.callMySchemaTypePostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                     serverName,
                                                                                     userId,
                                                                                     parentElementTypeName,
                                                                                     parentElementGUID,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaTypeElement>   getSchemaTypeByName(String  userId,
                                                         String  assetManagerGUID,
                                                         String  assetManagerName,
                                                         String  name,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         Date    effectiveTime,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName        = "getSchemaTypeByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        SchemaTypeElementsResponse restResult = restClient.callMySchemaTypesPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       startFrom,
                                                                                       validatedPageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SchemaTypeElement getSchemaTypeByGUID(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  schemaTypeGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-types/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        SchemaTypeElementResponse restResult = restClient.callMySchemaTypePostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                     serverName,
                                                                                     userId,
                                                                                     schemaTypeGUID,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ElementHeader getSchemaTypeParent(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  schemaTypeGUID,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName        = "getSchemaTypeParent";
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/parents/schema-types/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        ElementHeaderResponse restResult = restClient.callElementHeaderPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                    serverName,
                                                                                    userId,
                                                                                    schemaTypeGUID,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing);

        return restResult.getElement();
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaAttributeProperties properties for the schema attribute
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaAttribute(String                       userId,
                                        String                       assetManagerGUID,
                                        String                       assetManagerName,
                                        boolean                      assetManagerIsHome,
                                        String                       schemaElementGUID,
                                        ExternalIdentifierProperties externalIdentifierProperties,
                                        SchemaAttributeProperties    schemaAttributeProperties,
                                        Date                         effectiveTime,
                                        boolean                      forLineage,
                                        boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName                     = "createSchemaAttribute";
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String propertiesParameterName        = "schemaAttributeProperties";
        final String qualifiedNameParameterName     = "schemaAttributeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        SchemaAttributeRequestBody requestBody = new SchemaAttributeRequestBody();
        requestBody.setElementProperties(schemaAttributeProperties);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/schema-attributes?assetManagerIsHome={3}&forLineage={4}&forDuplicateProcessing={5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  schemaElementGUID,
                                                                  assetManagerIsHome,
                                                                  forLineage,
                                                                  forDuplicateProcessing);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaAttributeFromTemplate(String                       userId,
                                                    String                       assetManagerGUID,
                                                    String                       assetManagerName,
                                                    boolean                      assetManagerIsHome,
                                                    String                       schemaElementGUID,
                                                    String                       templateGUID,
                                                    ExternalIdentifierProperties externalIdentifierProperties,
                                                    TemplateProperties           templateProperties,
                                                    Date                         effectiveTime,
                                                    boolean                      forLineage,
                                                    boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName                  = "createSchemaAttributeFromTemplate";
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/schema-attributes/from-template/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  schemaElementGUID,
                                                                  templateGUID,
                                                                  assetManagerIsHome,
                                                                  forLineage,
                                                                  forDuplicateProcessing);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateSchemaAttribute(String                    userId,
                                      String                    assetManagerGUID,
                                      String                    assetManagerName,
                                      String                    schemaAttributeGUID,
                                      String                    schemaAttributeExternalIdentifier,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties,
                                      Date                      effectiveTime,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                       = "updateSchemaAttribute";
        final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";
        final String propertiesParameterName          = "schemaAttributeProperties";
        final String qualifiedNameParameterName       = "schemaAttributeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        SchemaAttributeRequestBody requestBody = new SchemaAttributeRequestBody();
        requestBody.setElementProperties(schemaAttributeProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   schemaAttributeExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaAttributeGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param schemaElementExternalIdentifier unique identifier of the schema element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setSchemaElementAsCalculatedValue(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  boolean assetManagerIsHome,
                                                  String  schemaElementGUID,
                                                  String  schemaElementExternalIdentifier,
                                                  String  formula,
                                                  Date    effectiveTime,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "setSchemaElementAsCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);

        CalculatedValueClassificationRequestBody requestBody = new CalculatedValueClassificationRequestBody();
        requestBody.setFormula(formula);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   schemaElementExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/is-calculated-value?assetManagerIsHome={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaElementGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param schemaElementExternalIdentifier unique identifier of the schema element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearSchemaElementAsCalculatedValue(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  schemaElementGUID,
                                                    String  schemaElementExternalIdentifier,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "clearSchemaElementAsCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/is-calculated-value/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, schemaElementExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        schemaElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the column schema attribute to indicate that it describes a primary key.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param primaryKeyName name of the primary key (if different from the column name)
     * @param primaryKeyPattern key pattern used to maintain the primary key
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupColumnAsPrimaryKey(String     userId,
                                        String     assetManagerGUID,
                                        String     assetManagerName,
                                        boolean    assetManagerIsHome,
                                        String     schemaAttributeGUID,
                                        String     schemaAttributeExternalIdentifier,
                                        String     primaryKeyName,
                                        KeyPattern primaryKeyPattern,
                                        Date       effectiveTime,
                                        boolean    forLineage,
                                        boolean    forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "setupColumnAsPrimaryKey";
        final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        PrimaryKeyClassificationRequestBody requestBody = new PrimaryKeyClassificationRequestBody();

        PrimaryKeyProperties primaryKeyProperties = new PrimaryKeyProperties();
        primaryKeyProperties.setName(primaryKeyName);
        primaryKeyProperties.setKeyPattern(primaryKeyPattern);

        requestBody.setPrimaryKeyProperties(primaryKeyProperties);
        requestBody.setExternalSourceGUID(assetManagerGUID);
        requestBody.setExternalSourceName(assetManagerName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}/is-primary-key?assetManagerIsHome={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaAttributeGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the primary key designation from the schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearColumnAsPrimaryKey(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  schemaAttributeGUID,
                                        String  schemaAttributeExternalIdentifier,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "clearColumnAsPrimaryKey";
        final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}/is-primary-key/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, schemaAttributeExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        schemaAttributeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link two schema attributes together to show a foreign key relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupForeignKeyRelationship(String               userId,
                                            String               assetManagerGUID,
                                            String               assetManagerName,
                                            boolean              assetManagerIsHome,
                                            String               primaryKeyGUID,
                                            String               foreignKeyGUID,
                                            ForeignKeyProperties foreignKeyProperties,
                                            Date                 effectiveTime,
                                            boolean              forLineage,
                                            boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName                  = "setupForeignKeyRelationship";
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        ForeignKeyRequestBody requestBody = new ForeignKeyRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setProperties(foreignKeyProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}/relationships/foreign-keys/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        primaryKeyGUID,
                                        foreignKeyGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateForeignKeyRelationship(String               userId,
                                             String               assetManagerGUID,
                                             String               assetManagerName,
                                             String               primaryKeyGUID,
                                             String               foreignKeyGUID,
                                             ForeignKeyProperties foreignKeyProperties,
                                             Date                 effectiveTime,
                                             boolean              forLineage,
                                             boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                  = "updateForeignKeyRelationship";
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        ForeignKeyRequestBody requestBody = new ForeignKeyRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setProperties(foreignKeyProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}/relationships/foreign-keys/{3}/update?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        primaryKeyGUID,
                                        foreignKeyGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the foreign key relationship between two schema elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearForeignKeyRelationship(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  primaryKeyGUID,
                                            String  foreignKeyGUID,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "clearForeignKeyRelationship";
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/relationships/{3}/foreign-keys/{4}/remove?forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        primaryKeyGUID,
                                        foreignKeyGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeSchemaAttribute(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  schemaAttributeGUID,
                                      String  schemaAttributeExternalIdentifier,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                       = "removeSchemaAttribute";
        final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, schemaAttributeExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        schemaAttributeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaAttributeElement>   findSchemaAttributes(String  userId,
                                                               String  assetManagerGUID,
                                                               String  assetManagerName,
                                                               String  searchString,
                                                               int     startFrom,
                                                               int     pageSize,
                                                               Date    effectiveTime,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName                = "findSchemaAttributes";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        SchemaAttributeElementsResponse restResult = restClient.callMySchemaAttributesPostRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 requestBody,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 startFrom,
                                                                                                 validatedPageSize,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of schema attributes associated with a schema element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaAttributeElement> getNestedSchemaAttributes(String  userId,
                                                                  String  assetManagerGUID,
                                                                  String  assetManagerName,
                                                                  String  parentSchemaElementGUID,
                                                                  int     startFrom,
                                                                  int     pageSize,
                                                                  Date    effectiveTime,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName        = "getNestedSchemaAttributes";
        final String guidParameterName = "parentSchemaElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentSchemaElementGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-elements/{2}/schema-attributes/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        SchemaAttributeElementsResponse restResult = restClient.callMySchemaAttributesPostRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 parentSchemaElementGUID,
                                                                                                 startFrom,
                                                                                                 validatedPageSize,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaAttributeElement>   getSchemaAttributesByName(String  userId,
                                                                    String  assetManagerGUID,
                                                                    String  assetManagerName,
                                                                    String  name,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    Date    effectiveTime,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName        = "getSchemaAttributesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        SchemaAttributeElementsResponse restResult = restClient.callMySchemaAttributesPostRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 requestBody,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 startFrom,
                                                                                                 validatedPageSize,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SchemaAttributeElement getSchemaAttributeByGUID(String userId,
                                                           String assetManagerGUID,
                                                           String assetManagerName,
                                                           String schemaAttributeGUID,
                                                           Date    effectiveTime,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "getSchemaAttributeByGUID";
        final String guidParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schema-attributes/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        SchemaAttributeElementResponse restResult = restClient.callMySchemaAttributePostRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                               serverName,
                                                                                               userId,
                                                                                               schemaAttributeGUID,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing);

        return restResult.getElement();
    }
}
