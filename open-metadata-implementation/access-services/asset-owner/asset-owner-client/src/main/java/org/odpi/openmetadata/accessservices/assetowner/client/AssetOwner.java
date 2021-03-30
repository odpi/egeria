/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.accessservices.assetowner.api.*;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaTypeProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;

import java.util.List;
import java.util.Map;

/**
 * AssetOwner provides the generic client-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other clients that provide specialized methods for specific types of Asset.
 *
 * This client is initialized with the URL and name of the server that is running the Asset Owner OMAS.
 * This server is responsible for locating and managing the asset owner's definitions exchanged with this client.
 */
public class AssetOwner extends ConnectedAssetClientBase implements AssetKnowledgeInterface,
                                                                    AssetOnboardingInterface,
                                                                    AssetClassificationInterface,
                                                                    AssetReviewInterface,
                                                                    AssetDecommissioningInterface,
                                                                    AssetDuplicateManagementInterface

{
    protected AssetOwnerRESTClient restClient;               /* Initialized in constructor */

    private static final String  serviceURLName = "asset-owner";
    private static final String  defaultAssetType = "Asset";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetOwner(String   serverName,
                      String   serverPlatformRootURL,
                      AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetOwner(String serverName,
                      String serverPlatformRootURL) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetOwner(String   serverName,
                      String   serverPlatformRootURL,
                      String   userId,
                      String   password,
                      AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetOwner(String serverName,
                      String serverPlatformRootURL,
                      String userId,
                      String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetOwner(String               serverName,
                      String               serverPlatformRootURL,
                      AssetOwnerRESTClient restClient,
                      int                  maxPageSize,
                      AuditLog             auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }


    /**
     * Return the asset subtype names.
     *
     * @param userId calling user
     * @return list of type names that are subtypes of asset
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String>  getTypesOfAsset(String userId) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String   methodName = "getTypesOfAsset";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/sub-types";

        invalidParameterHandler.validateUserId(userId, methodName);

        NameListResponse restResult = restClient.callNameListGetRESTCall(methodName,
                                                                         serverPlatformRootURL + urlTemplate,
                                                                         serverName,
                                                                         userId);

        return restResult.getNames();
    }


    /**
     * Return the asset subtype names.
     *
     * @param userId calling user
     * @return list of type names that are subtypes of asset
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public Map<String, String>  getTypesOfAssetWithDescriptions(String userId) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getTypesOfAssetWithDescriptions";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/sub-types/descriptions";

        invalidParameterHandler.validateUserId(userId, methodName);

        StringMapResponse restResult = restClient.callStringMapGetRESTCall(methodName,
                                                                           serverPlatformRootURL + urlTemplate,
                                                                           serverName,
                                                                           userId);

        return restResult.getStringMap();
    }


    /*
     * ==============================================
     * AssetOnboardingInterface
     * ==============================================
     */


    /**
     * Add a simple asset description to the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param qualifiedName unique name for the asset in the catalog
     * @param displayName display name for the asset in the catalog
     * @param description description of the asset in the catalog
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String  addAssetToCatalog(String               userId,
                                     String               typeName,
                                     String               qualifiedName,
                                     String               displayName,
                                     String               description,
                                     Map<String, String>  additionalProperties,
                                     Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName             = "addAssetToCatalog";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        AssetProperties assetProperties = new AssetProperties();

        assetProperties.setTypeName(typeName);
        assetProperties.setQualifiedName(qualifiedName);
        assetProperties.setDisplayName(displayName);
        assetProperties.setDescription(description);
        assetProperties.setAdditionalProperties(additionalProperties);
        assetProperties.setExtendedProperties(extendedProperties);

        return this.addAssetToCatalog(userId, assetProperties);
    }


    /**
     * Add a comprehensive asset description to the catalog.
     *
     * @param userId calling user
     * @param assetProperties properties for the asset
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String  addAssetToCatalog(String          userId,
                                     AssetProperties assetProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                   = "addAssetToCatalog";
        final String assetPropertiesParameterName = "assetProperties";
        final String qualifiedNameParameter       = "assetProperties.getQualifiedName()";
        final String urlTemplate                  = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(assetProperties, assetPropertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameter, methodName);

        if (assetProperties.getTypeName() == null)
        {
            assetProperties.setTypeName(defaultAssetType);
        }

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  assetProperties,
                                                                  serverName,
                                                                  userId,
                                                                  assetProperties.getTypeName());

        return restResult.getGUID();
    }



    /**
     * Create a new metadata element to represent an asset using an existing asset as a template.
     *
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String addAssetToCatalogUsingTemplate(String             userId,
                                                 String             templateGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                  = "createDatabaseFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";
        final String urlTemplate                 = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/from-template/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }



    /**
     * Stores the supplied schema details in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.  If more attributes need to be added in addition to the
     * ones supplied then this can be done with addSchemaAttributesToSchemaType().
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema type to create and attach directly to the asset.
     * @param schemaAttributes optional schema attributes.
     *
     * @return guid of the schema type or
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String   addCombinedSchemaToAsset(String                          userId,
                                             String                          assetGUID,
                                             SchemaTypeProperties            schemaType,
                                             List<SchemaAttributeProperties> schemaAttributes) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String   methodName = "addCombinedSchemaToAsset";
        final String   assetGUIDParameter = "assetGUID";
        final String   schemaTypeParameter = "schemaType";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas/with-attributes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateObject(schemaType, schemaTypeParameter, methodName);

        CombinedSchemaRequestBody requestBody = new CombinedSchemaRequestBody();

        requestBody.setSchemaType(schemaType);
        requestBody.setSchemaAttributes(schemaAttributes);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        return restResult.getGUID();
    }



    /**
     * Stores the supplied schema type in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema type to create and attach directly to the asset.
     *
     * @return guid of the new schema type or
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String   addSchemaTypeToAsset(String               userId,
                                         String               assetGUID,
                                         SchemaTypeProperties schemaType) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "addSchemaTypeToAsset";
        final String   assetGUIDParameter = "assetGUID";
        final String   schemaTypeParameter = "schemaType";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateObject(schemaType, schemaTypeParameter, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  schemaType,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        return restResult.getGUID();
    }


    /**
     * Links the supplied schema type directly to the asset.  If this schema is either not found, or
     * already attached to an asset, then an error occurs.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaTypeGUID unique identifier of the schema type to attach
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void   attachSchemaTypeToAsset(String            userId,
                                          String            assetGUID,
                                          String            schemaTypeGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "attachSchemaTypeToAsset";
        final String   assetGUIDParameter = "assetGUID";
        final String   schemaTypeParameter = "schemaTypeGUID";
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas/{3}/attach";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeParameter, methodName);


        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        schemaTypeGUID);
    }


    /**
     * Unlinks the schema from the asset but does not delete it.  This means it can be be reattached to a different asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @return guid of detached schema type
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String detachSchemaTypeFromAsset(String          userId,
                                            String          assetGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   methodName = "detachSchemaTypeFromAsset";
        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas/detach";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        return restResult.getGUID();
    }


    /**
     * Detaches and deletes an asset's schema.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  deleteAssetSchemaType(String          userId,
                                       String          assetGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "deleteAssetSchemaType";
        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        restClient.callGUIDPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Adds attributes to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param schemaAttributes list of schema attribute objects.
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void addSchemaAttributes(String                          serverName,
                                    String                          userId,
                                    String                          assetGUID,
                                    String                          parentGUID,
                                    List<SchemaAttributeProperties> schemaAttributes) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   methodName = "addSchemaAttributesToSchemaType";
        final String   assetGUIDParameter = "assetGUID";
        final String   parentGUIDParameter = "parentGUID";
        final String   schemaAttributesParameter = "schemaAttributes";
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas/{3}/schema-attributes/list";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameter, methodName);
        invalidParameterHandler.validateObject(schemaAttributes, schemaAttributesParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        schemaAttributes,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        parentGUID);
    }


    /**
     * Adds a schema attribute to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.  The GUID returned can be used to add
     * nested attributes.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param schemaAttribute schema attribute object to add.
     *
     * @return unique identifier for the new schema attribute or
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String addSchemaAttribute(String                    serverName,
                                     String                    userId,
                                     String                    assetGUID,
                                     String                    parentGUID,
                                     SchemaAttributeProperties schemaAttribute) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "addSchemaAttributes";
        final String   assetGUIDParameter = "assetGUID";
        final String   parentGUIDParameter = "parentGUID";
        final String   schemaAttributeParameter = "schemaAttribute";
        final String   qualifiedNameParameter = "schemaAttribute.getQualifiedName()";
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/schemas/{3}/schema-attributes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameter, methodName);
        invalidParameterHandler.validateObject(schemaAttribute, schemaAttributeParameter, methodName);
        invalidParameterHandler.validateName(schemaAttribute.getQualifiedName(), qualifiedNameParameter, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  schemaAttribute,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID,
                                                                  parentGUID);

        return restResult.getGUID();
    }


    /**
     * Adds a connection to an asset.  Assets can have multiple connections attached.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the attest to attach the connection to
     * @param assetSummary summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connection connection object.  If the connection is already stored (matching guid)
     *                   then the existing connection is used.
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void addConnectionToAsset(String     userId,
                                     String     assetGUID,
                                     String     assetSummary,
                                     Connection connection) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "addConnectionToAsset";

        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/connection";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        ConnectionRequestBody requestBody = new ConnectionRequestBody();

        requestBody.setShortDescription(assetSummary);
        requestBody.setConnection(connection);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /*
     * ==============================================
     * AssetClassificationInterface
     * ==============================================
     */


    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *                         If null then the assetGUID is used.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  addSemanticAssignment(String    userId,
                                       String    assetGUID,
                                       String    glossaryTermGUID,
                                       String    assetElementGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String   methodName = "addSemanticAssignment";
        final String   assetGUIDParameter = "assetGUID";
        final String   glossaryTermParameter = "glossaryTermGUID";
        final String   assetURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/meanings/{3}";
        final String   elementURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/attachments/{3}/meanings/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermParameter, methodName);

        if (assetElementGUID == null)
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + assetURLTemplate, nullRequestBody, serverName, userId, assetGUID, glossaryTermGUID);
        }
        else
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + elementURLTemplate, nullRequestBody, serverName, userId, assetGUID, assetElementGUID, glossaryTermGUID);
        }
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *                         If null then the assetGUID is used.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  removeSemanticAssignment(String    userId,
                                          String    assetGUID,
                                          String    glossaryTermGUID,
                                          String    assetElementGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String   methodName = "addSemanticAssignment";
        final String   assetGUIDParameter = "assetGUID";
        final String   glossaryTermParameter = "glossaryTermGUID";
        final String   assetURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/meanings/delete";
        final String   elementURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/attachments/{3}/meanings/{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermParameter, methodName);

        if (assetElementGUID == null)
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + assetURLTemplate, nullRequestBody, serverName, userId, assetGUID, glossaryTermGUID);
        }
        else
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + elementURLTemplate, nullRequestBody, serverName, userId, assetGUID, assetElementGUID, glossaryTermGUID);
        }
    }


    /**
     * Add the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  addAssetOrigin(String                userId,
                                String                assetGUID,
                                String                organizationGUID,
                                String                businessCapabilityGUID,
                                Map<String, String>   otherOriginValues) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   methodName = "addAssetOrigin";
        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/origin";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        OriginRequestBody requestBody = new OriginRequestBody();
        requestBody.setBusinessCapabilityGUID(businessCapabilityGUID);
        requestBody.setOrganizationGUID(organizationGUID);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  removeAssetOrigin(String                userId,
                                   String                assetGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String   methodName = "removeAssetOrigin";
        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/origin/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Update the zones for a specific asset to the zone list specified in the publishZones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void publishAsset(String                userId,
                             String                assetGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "publishAsset";
        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/publish";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Update the zones for a specific asset to the zone list specified in the defaultZones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void withdrawAsset(String                userId,
                              String                assetGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "withdrawAsset";
        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/withdraw";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Update the zones for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to no zones.
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateAssetZones(String        userId,
                                 String        assetGUID,
                                 List<String>  assetZones) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String   methodName = "updateAssetZones";

        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/governance-zones";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        assetZones,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Update the owner information for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param ownerId userId or profileGUID of the owner - or null to clear the field
     * @param ownerType indicator of the type of Id provides above - or null to clear the field
     * @throws InvalidParameterException userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateAssetOwner(String    userId,
                                 String    assetGUID,
                                 String    ownerId,
                                 OwnerType ownerType) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String   methodName = "updateAssetOwner";

        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/owner";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        OwnerRequestBody requestBody = new OwnerRequestBody();
        requestBody.setOwnerId(ownerId);
        requestBody.setOwnerType(ownerType);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *                         If null then the assetGUID is used.
     * @param securityLabels list of security labels defining the security characteristics of the element
     * @param securityProperties Descriptive labels describing origin of the asset
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  addSecurityTags(String                userId,
                                 String                assetGUID,
                                 String                assetElementGUID,
                                 List<String>          securityLabels,
                                 Map<String, Object>   securityProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   methodName = "addSecurityTags";
        final String   assetGUIDParameter = "assetGUID";
        final String   assetURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/security-tags";
        final String   elementURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/attachments/{3}/security-tags";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        SecurityTagsRequestBody requestBody = new SecurityTagsRequestBody();
        requestBody.setSecurityLabels(securityLabels);
        requestBody.setSecurityProperties(securityProperties);

        if (assetElementGUID == null)
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + assetURLTemplate, requestBody, serverName, userId, assetGUID);
        }
        else
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + elementURLTemplate, requestBody, serverName, userId, assetGUID, assetElementGUID);
        }
    }


    /**
     * Remove the security tags classification to an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element where the security tags need to be removed.
     *                         If null then the assetGUID is used.
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  removeSecurityTags(String                userId,
                                    String                assetGUID,
                                    String                assetElementGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "removeSecurityTags";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   assetURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/security-tags/delete";
        final String   elementURLTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/attachments/{3}/security-tags/delete";

        if (assetElementGUID == null)
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + assetURLTemplate, nullRequestBody, serverName, userId, assetGUID);
        }
        else
        {
            restClient.callVoidPostRESTCall(methodName, serverPlatformRootURL + elementURLTemplate, nullRequestBody, serverName, userId, assetGUID, assetElementGUID);
        }
    }


    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to classify
     * @param name name of the template
     * @param description description of when, where and how to use the template
     * @param additionalProperties any additional properties
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateClassification(String              userId,
                                          String              assetGUID,
                                          String              name,
                                          String              description,
                                          Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String   methodName = "addTemplateClassification";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/template-classification";

        TemplateClassificationRequestBody requestBody = new TemplateClassificationRequestBody();

        requestBody.setName(name);
        requestBody.setDescription(description);
        requestBody.setAdditionalProperties(additionalProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Remove the classification that indicates that this asset can be used as a template.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to declassify
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String userId,
                                             String assetGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "removeTemplateClassification";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/template-classification/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /*
     * ==============================================
     * AssetReviewInterface
     * ==============================================
     */


    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    @Override
    public List<AssetElement> getAssetsByName(String   userId,
                                              String   name,
                                              int      startFrom,
                                              int      pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/by-name?startFrom={2}&pageSize={3}";
        final String   methodName = "getAssetsByName";
        final String   nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        AssetElementsResponse restResult = restClient.callAssetElementsPostRESTCall(methodName,
                                                                                    serverPlatformRootURL + urlTemplate,
                                                                                    name,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    pageSize);

        return restResult.getAssets();
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    @Override
    public List<AssetElement>  findAssets(String   userId,
                                          String   searchString,
                                          int      startFrom,
                                          int      pageSize) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/by-search-string?startFrom={2}&pageSize={3}";
        final String   methodName = "findAssets";
        final String   searchParameter = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchParameter, methodName);

        AssetElementsResponse restResult = restClient.callAssetElementsPostRESTCall(methodName,
                                                                                    serverPlatformRootURL + urlTemplate,
                                                                                    searchString,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    pageSize);

        return restResult.getAssets();
    }


    /**
     * Return the basic attributes of an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return basic asset properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    @Override
    public AssetElement getAssetSummary(String  userId,
                                        String  assetGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getAssetSummary";
        final String assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}";

        AssetElementResponse restResult = restClient.callAssetElementGetRESTCall(methodName,
                                                                   serverPlatformRootURL + urlTemplate,
                                                                   serverName,
                                                                   userId,
                                                                   assetGUID);

        return restResult.getAsset();
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public AssetUniverse getAssetProperties(String userId,
                                            String assetGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return super.getAssetProperties(serviceURLName, userId, assetGUID);
    }


    /**
     * Return a connector for the asset to enable the calling user to access the content.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return connector object
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    @Override
    public Connector getConnectorToAsset(String  userId,
                                         String  assetGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "getConnectorToAsset";

        final String assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        try
        {
            return super.getConnectorForConnection(restClient,
                                                   serviceURLName,
                                                   userId,
                                                   getConnectionForAsset(restClient, serviceURLName, userId, assetGUID),
                                                   methodName);
        }
        catch (ConnectionCheckedException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error, "connection to asset " + assetGUID);
        }
        catch (ConnectorCheckedException  error)
        {
            throw new PropertyServerException(error);
        }
    }



    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     * @return list of discovery analysis reports
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    @Override
    public List<DiscoveryAnalysisReport>   getDiscoveryAnalysisReports(String  userId,
                                                                       String  assetGUID,
                                                                       int     startingFrom,
                                                                       int     maximumResults) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String   methodName = "getDiscoveryAnalysisReports";

        final String   assetGUIDParameter = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/discovery-analysis-reports?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        DiscoveryAnalysisReportListResponse restResult = restClient.callDiscoveryAnalysisReportListGetRESTCall(methodName,
                                                                                                               serverPlatformRootURL + urlTemplate,
                                                                                                               serverName,
                                                                                                               userId,
                                                                                                               assetGUID,
                                                                                                               Integer.toString(startingFrom),
                                                                                                               Integer.toString(maximumResults));

        return restResult.getDiscoveryAnalysisReports();
    }


    /**
     * Return the annotation subtype names.
     *
     * @param userId calling user
     * @return list of type names that are subtypes of annotation
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String>  getTypesOfAnnotation(String userId) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String   methodName = "getTypesOfAnnotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/annotations/sub-types";

        invalidParameterHandler.validateUserId(userId, methodName);

        NameListResponse restResult = restClient.callNameListGetRESTCall(methodName,
                                                                         serverPlatformRootURL + urlTemplate,
                                                                         serverName,
                                                                         userId);

        return restResult.getNames();
    }


    /**
     * Return the annotation subtype names mapped to their descriptions.
     *
     * @param userId calling user
     * @return map of type names that are subtypes of annotation to their descriptions
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public Map<String, String>  getTypesOfAnnotationWithDescriptions(String userId) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String   methodName = "getTypesOfAnnotationWithDescriptions";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/annotations/sub-types/descriptions";

        invalidParameterHandler.validateUserId(userId, methodName);

        StringMapResponse restResult = restClient.callStringMapGetRESTCall(methodName,
                                                                           serverPlatformRootURL + urlTemplate,
                                                                           serverName,
                                                                           userId);

        return restResult.getStringMap();
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    @Override
    public List<Annotation> getDiscoveryReportAnnotations(String           userId,
                                                          String           discoveryReportGUID,
                                                          AnnotationStatus annotationStatus,
                                                          int              startingFrom,
                                                          int              maximumResults) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        final String   discoveryReportGUIDParameter = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/discovery-analysis-reports/{2}?startingFrom={3}&maxPageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, discoveryReportGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        StatusRequestBody requestBody = new StatusRequestBody();

        requestBody.setAnnotationStatus(annotationStatus);

        AnnotationListResponse restResult = restClient.callAnnotationListPostRESTCall(methodName,
                                                                                      serverPlatformRootURL + urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      discoveryReportGUID,
                                                                                      Integer.toString(startingFrom),
                                                                                      Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    @Override
    public List<Annotation>  getExtendedAnnotations(String           userId,
                                                    String           annotationGUID,
                                                    AnnotationStatus annotationStatus,
                                                    int              startingFrom,
                                                    int              maximumResults) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "getExtendedAnnotations";

        final String   annotationGUIDParameter = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/annotations/{2}?startingFrom={3}&maxPageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        StatusRequestBody requestBody = new StatusRequestBody();

        requestBody.setAnnotationStatus(annotationStatus);

        AnnotationListResponse restResult = restClient.callAnnotationListPostRESTCall(methodName,
                                                                                      serverPlatformRootURL + urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      annotationGUID,
                                                                                      Integer.toString(startingFrom),
                                                                                      Integer.toString(maximumResults));

         return restResult.getAnnotations();
    }


    /*
     * ==============================================
     * AssetDecommissioningInterface
     * ==============================================
     */

    /**
     * Deletes an asset and all of its associated elements such as schema, connections (unless they are linked to
     * another asset), discovery reports and associated feedback.
     *
     * Given the depth of the delete performed by this call, it should be used with care.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the attest to attach the connection to
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteAsset(String        userId,
                            String        assetGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "deleteAsset";

        final String assetGUIDParameter = "assetGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /*
     * ==============================================
     * AssetDuplicateManagementInterface
     * ==============================================
     */

    /**
     * Create a simple relationship between two elements in an Asset description (typically the asset itself or
     * attributes in their schema).
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element

     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  linkElementsAsDuplicates(String userId,
                                          String element1GUID,
                                          String element2GUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/duplicate-of/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        element1GUID,
                                        element2GUID);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  unlinkElementsAsDuplicates(String userId,
                                            String element1GUID,
                                            String element2GUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "unlinkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/duplicate-of/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        element1GUID,
                                        element2GUID);
    }
}
