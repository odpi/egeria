/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.client;


import org.odpi.openmetadata.accessservices.datamanager.api.SchemaManagerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;


import java.util.List;

/**
 * SchemaManagerClient defines the common methods for managing SchemaTypes and SchemaAttributes. It is incorporated in the
 * EventBrokerClient and the APIManagerClient.
 *
 * SchemaAttributes describe the data fields of the schema. If a schema attribute's type is simple (that is
 * primitive, literal, enum or external, its details are passed with the schema attribute.  Complex schema types (such as Maps,
 * Choices) are constructed first and then their identifiers are attached to the schema attribute.
 * SchemaTypes are used when creating complex schema structures that involve maps, choice and links to externally defined
 * schemas that are, for example, part of a standard.
 */
public abstract class SchemaManagerClient implements SchemaManagerInterface
{
    private static final String schemaTypeURLTemplatePrefix      = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/schema-types";
    private static final String validValueSetsURLTemplatePrefix  = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/valid-value-sets";
    private static final String schemaAttributeURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/schema-attributes";
    private static final String schemaElementURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/schema-elements";

    String   serverName;               /* Initialized in constructor */
    String   serverPlatformURLRoot;    /* Initialized in constructor */
    AuditLog auditLog = null;          /* Initialized in constructor */

    InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    DataManagerRESTClient   restClient;               /* Initialized in constructor */

    static final NullRequestBody nullRequestBody = new NullRequestBody();

    private String schemaAttributeTypeName;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param schemaAttributeTypeName name of default type for the schema attribute
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    SchemaManagerClient(String   schemaAttributeTypeName,
                        String   serverName,
                        String   serverPlatformURLRoot,
                        AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, auditLog);

        this.schemaAttributeTypeName = schemaAttributeTypeName;
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param schemaAttributeTypeName name of default type for the schema attribute
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    SchemaManagerClient(String schemaAttributeTypeName,
                        String serverName,
                        String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot);

        this.schemaAttributeTypeName = schemaAttributeTypeName;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param schemaAttributeTypeName name of default type for the schema attribute
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    SchemaManagerClient(String   schemaAttributeTypeName,
                        String   serverName,
                        String   serverPlatformURLRoot,
                        String   userId,
                        String   password,
                        AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);

        this.schemaAttributeTypeName = schemaAttributeTypeName;
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param schemaAttributeTypeName name of default type for the schema attribute
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    SchemaManagerClient(String                schemaAttributeTypeName,
                        String                serverName,
                        String                serverPlatformURLRoot,
                        DataManagerRESTClient restClient,
                        int                   maxPageSize,
                        AuditLog              auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;

        this.schemaAttributeTypeName = schemaAttributeTypeName;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param schemaAttributeTypeName name of default type for the schema attribute
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    SchemaManagerClient(String schemaAttributeTypeName,
                        String serverName,
                        String serverPlatformURLRoot,
                        String userId,
                        String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password);

        this.schemaAttributeTypeName = schemaAttributeTypeName;
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of a data asset
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createPrimitiveSchemaType(String                        userId,
                                            String                        externalSourceGUID,
                                            String                        externalSourceName,
                                            PrimitiveSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                  = "createPrimitiveSchemaType";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/primitives";

        PrimitiveSchemaTypeRequestBody requestBody = new PrimitiveSchemaTypeRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createLiteralSchemaType(String                      userId,
                                          String                      externalSourceGUID,
                                          String                      externalSourceName,
                                          LiteralSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                  = "createLiteralSchemaType";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/literals";

        LiteralSchemaTypeRequestBody requestBody = new LiteralSchemaTypeRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeProperties properties about the schema type to store
     * @param validValuesSetGUID unique identifier of the valid values set to used
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEnumSchemaType(String                   userId,
                                       String                   externalSourceGUID,
                                       String                   externalSourceName,
                                       EnumSchemaTypeProperties schemaTypeProperties,
                                       String                   validValuesSetGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "createEnumSchemaType";
        final String propertiesParameterName        = "schemaTypeProperties";
        final String qualifiedNameParameterName     = "qualifiedName";
        final String validValueSetGUIDParameterName = "validValuesSetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateGUID(validValuesSetGUID, validValueSetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/enums/valid-values/{2}";

        EnumSchemaTypeRequestBody requestBody = new EnumSchemaTypeRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  validValuesSetGUID);

        return restResult.getGUID();
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ValidValueSetElement> getValidValueSetByName(String userId,
                                                             String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getValidValueSetByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueSetsURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        ValidValueSetsResponse restResult = restClient.callValidValueSetsPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ValidValueSetElement> findValidValueSet(String userId,
                                                        String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                = "findValidValueSet";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueSetsURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        ValidValueSetsResponse restResult = restClient.callValidValueSetsPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createStructSchemaType(String                     userId,
                                         String                     externalSourceGUID,
                                         String                     externalSourceName,
                                         StructSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                  = "createStructSchemaType";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/structs";

        StructSchemaTypeRequestBody requestBody = new StructSchemaTypeRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeProperties properties about the schema type to store
     * @param schemaTypeOptionGUIDs unique identifier for the schema types to choose from
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaTypeChoice(String                     userId,
                                         String                     externalSourceGUID,
                                         String                     externalSourceName,
                                         SchemaTypeChoiceProperties schemaTypeProperties,
                                         List<String>               schemaTypeOptionGUIDs) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                  = "createSchemaTypeChoice";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String optionGUIDsParameterName    = "schemaTypeOptionGUIDs";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateObject(schemaTypeOptionGUIDs, optionGUIDsParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/choices";

        SchemaTypeChoiceRequestBody requestBody = new SchemaTypeChoiceRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setSchemaTypeOptionGUIDs(schemaTypeOptionGUIDs);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeProperties properties about the schema type to store
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createMapSchemaType(String                  userId,
                                      String                  externalSourceGUID,
                                      String                  externalSourceName,
                                      MapSchemaTypeProperties schemaTypeProperties,
                                      String                  mapFromSchemaTypeGUID,
                                      String                  mapToSchemaTypeGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName                  = "createMapSchemaType";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String fromGUIDParameterName       = "mapFromSchemaTypeGUID";
        final String toGUIDParameterName         = "mapToSchemaTypeGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateGUID(mapFromSchemaTypeGUID, fromGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(mapToSchemaTypeGUID, toGUIDParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/maps/from/{2}/to/{3}";

        MapSchemaTypeRequestBody requestBody = new MapSchemaTypeRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  mapFromSchemaTypeGUID,
                                                                  mapToSchemaTypeGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaTypeFromTemplate(String             userId,
                                               String             externalSourceGUID,
                                               String             externalSourceName,
                                               String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                  = "createSchemaTypeFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/from-template/{2}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateSchemaType(String               userId,
                                 String               externalSourceGUID,
                                 String               externalSourceName,
                                 String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "updateSchemaType";
        final String elementGUIDParameterName    = "schemaTypeGUID";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody(schemaTypeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaTypeGUID,
                                        isMergeUpdate);
    }




    /**
     * Remove the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeSchemaType(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName               = "removeSchemaType";
        final String elementGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaTypeGUID);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaTypeElement> findSchemaType(String userId,
                                                  String typeName,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                = "findSchemaType";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/types/{2}/by-search-string?startFrom={3}&pageSize={4}";

        String requestTypeName = "SchemaType";

        if (typeName != null)
        {
            requestTypeName = typeName;
        }

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        SchemaTypesResponse restResult = restClient.callSchemaTypesPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               userId,
                                                                               requestTypeName,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SchemaTypeElement getSchemaTypeForElement(String userId,
                                                     String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName                     = "findSchemaType";
        final String parentElementGUIDParameterName = "parentElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/types/{2}/by-parent-element/{3}";

        String requestTypeName = "Referenceable";

        if (parentElementTypeName != null)
        {
            requestTypeName = parentElementTypeName;
        }

        SchemaTypeResponse restResult = restClient.callSchemaTypeGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             requestTypeName,
                                                                             parentElementGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaTypeElement>   getSchemaTypeByName(String userId,
                                                         String name,
                                                         String typeName,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName        = "getSchemaTypeByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/types/{2}/by-name?startFrom={3}&pageSize={4}";

        String requestTypeName = "SchemaType";

        if (typeName != null)
        {
            requestTypeName = typeName;
        }

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        SchemaTypesResponse restResult = restClient.callSchemaTypesPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               userId,
                                                                               requestTypeName,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SchemaTypeElement getSchemaTypeByGUID(String userId,
                                                 String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/{2}";

        SchemaTypeResponse restResult = restClient.callSchemaTypeGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             schemaTypeGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port) plus qualified name
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ElementStub getSchemaTypeParent(String userId,
                                           String schemaTypeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaTypeURLTemplatePrefix + "/{2}/parent";

        ElementStubResponse restResult = restClient.callElementStubGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               schemaTypeGUID);

        return restResult.getElement();
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param schemaAttributeProperties properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaAttribute(String                    userId,
                                        String                    externalSourceGUID,
                                        String                    externalSourceName,
                                        String                    schemaElementGUID,
                                        SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName                     = "createSchemaAttribute";
        final String propertiesParameterName        = "schemaAttributeProperties";
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/attached-to/{2}";

        if (schemaAttributeProperties.getTypeName() == null)
        {
            schemaAttributeProperties.setTypeName(schemaAttributeTypeName);
        }

        SchemaAttributeRequestBody requestBody = new SchemaAttributeRequestBody(schemaAttributeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  schemaElementGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSchemaAttributeFromTemplate(String             userId,
                                                    String             externalSourceGUID,
                                                    String             externalSourceName,
                                                    String             schemaElementGUID,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                     = "createSchemaAttributeFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String propertiesParameterName        = "templateProperties";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/from-template/{2}/attached-to/{3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  schemaElementGUID);

        return restResult.getGUID();
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param relationshipTypeName name of relationship to create
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupSchemaType(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  relationshipTypeName,
                                String  schemaAttributeGUID,
                                String  schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                        = "setupSchemaType";
        final String schemaAttributeGUIDParameterName  = "schemaAttributeGUID";
        final String schemaTypeGUIDParameterName       = "schemaTypeGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/{2}/schema-types/{3}/relationship-type-name/{4}";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaAttributeGUID,
                                        schemaTypeGUID,
                                        relationshipTypeName);

    }


    /**
     * Remove the linked schema types from a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearSchemaTypes(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String schemaAttributeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                       = "clearSchemaTypes";
        final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/{2}/schema-types/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaAttributeGUID);
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateSchemaAttribute(String                    userId,
                                      String                    externalSourceGUID,
                                      String                    externalSourceName,
                                      String                    schemaAttributeGUID,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                  = "updateSchemaAttribute";
        final String elementGUIDParameterName    = "schemaAttributeGUID";
        final String propertiesParameterName     = "schemaAttributeProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        SchemaAttributeRequestBody requestBody = new SchemaAttributeRequestBody(schemaAttributeProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaAttributeGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeSchemaAttribute(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String schemaAttributeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "removeSchemaAttribute";
        final String elementGUIDParameterName    = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaAttributeGUID);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema attribute - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaAttributeElement> findSchemaAttributes(String userId,
                                                             String searchString,
                                                             String typeName,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                = "findSchemaAttributes";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/types/{2}/by-search-string?startFrom={3}&pageSize={4}";

        String requestTypeName = schemaAttributeTypeName;

        if (typeName != null)
        {
            requestTypeName = typeName;
        }

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        SchemaAttributesResponse restResult = restClient.callSchemaAttributesPostRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          userId,
                                                                                          requestTypeName,
                                                                                          startFrom,
                                                                                          validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param userId calling user
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaAttributeElement> getNestedAttributes(String userId,
                                                            String parentSchemaElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                  = "getNestedAttributes";
        final String elementGUIDParameterName    = "schemaAttributeGUID";

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentSchemaElementGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaElementURLTemplatePrefix + "/{2}/nested-attributes?startFrom={3}&pageSize={4}";

        SchemaAttributesResponse restResult = restClient.callSchemaAttributesGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         parentSchemaElementGUID,
                                                                                         startFrom,
                                                                                         validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param typeName optional type name for the schema attribute - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SchemaAttributeElement> getSchemaAttributesByName(String userId,
                                                                  String name,
                                                                  String typeName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName        = "getSchemaAttributesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/types/{2}/by-name?startFrom={3}&pageSize={4}";

        String requestTypeName = schemaAttributeTypeName;

        if (typeName != null)
        {
            requestTypeName = typeName;
        }

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        SchemaAttributesResponse restResult = restClient.callSchemaAttributesPostRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          userId,
                                                                                          requestTypeName,
                                                                                          startFrom,
                                                                                          validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SchemaAttributeElement getSchemaAttributeByGUID(String userId,
                                                           String schemaAttributeGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getSchemaAttributeByGUID";
        final String guidParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaAttributeURLTemplatePrefix + "/{2}";

        SchemaAttributeResponse restResult = restClient.callSchemaAttributeGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       schemaAttributeGUID);

        return restResult.getElement();
    }


    /* =====================================================================================================================
     * Working with derived values
     */


    /**
     * Classify the schema element to indicate that it describes a calculated value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param formula formula for calculating the value - this may contain placeholders that are identified by the
     *                queryIds used in the queryTarget relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupCalculatedValue(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  schemaElementGUID,
                                     String  formula) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName                     = "setupCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String formulaParameterName           = "formula";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(formula, formulaParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaElementURLTemplatePrefix + "/{2}/calculated-value";

        FormulaRequestBody requestBody = new FormulaRequestBody();

        requestBody.setFormula(formula);
        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaElementGUID);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearCalculatedValue(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String schemaElementGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                     = "clearCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaElementURLTemplatePrefix + "/{2}/calculated-value/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        schemaElementGUID);
    }


    /**
     * Link two schema elements together to show a query target relationship.  The query target provides
     * data values to calculate a derived value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     * @param queryTargetProperties properties for the query target relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupQueryTargetRelationship(String                                 userId,
                                             String                                 externalSourceGUID,
                                             String                                 externalSourceName,
                                             String                                 derivedElementGUID,
                                             String                                 queryTargetGUID,
                                             DerivedSchemaTypeQueryTargetProperties queryTargetProperties) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException

    {
        final String methodName                     = "setupQueryTargetRelationship";
        final String schemaElementGUIDParameterName = "derivedElementGUID";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(derivedElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(queryTargetGUID, queryTargetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaElementURLTemplatePrefix + "/{2}/query-targets/{3}";

        DerivedSchemaTypeQueryTargetRequestBody requestBody = new DerivedSchemaTypeQueryTargetRequestBody(queryTargetProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        derivedElementGUID,
                                        queryTargetGUID);
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     * @param queryTargetProperties properties for the query target relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateQueryTargetRelationship(String                                 userId,
                                              String                                 externalSourceGUID,
                                              String                                 externalSourceName,
                                              String                                 derivedElementGUID,
                                              String                                 queryTargetGUID,
                                              DerivedSchemaTypeQueryTargetProperties queryTargetProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName                     = "updateQueryTargetRelationship";
        final String schemaElementGUIDParameterName = "derivedElementGUID";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(derivedElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(queryTargetGUID, queryTargetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaElementURLTemplatePrefix + "/{2}/query-targets/{3}/update";

        DerivedSchemaTypeQueryTargetRequestBody requestBody = new DerivedSchemaTypeQueryTargetRequestBody(queryTargetProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        derivedElementGUID,
                                        queryTargetGUID);
    }


    /**
     * Remove the query target relationship between two schema elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearQueryTargetRelationship(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String derivedElementGUID,
                                             String queryTargetGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName                     = "clearQueryTargetRelationship";
        final String schemaElementGUIDParameterName = "derivedElementGUID";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(derivedElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(queryTargetGUID, queryTargetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaElementURLTemplatePrefix + "/{2}/query-targets/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        derivedElementGUID,
                                        queryTargetGUID);
    }
}
