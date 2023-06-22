/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;


import org.odpi.openmetadata.accessservices.assetowner.api.RelatedElementsManagementInterface;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.properties.ClassificationProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.StakeholderProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.ElementStubListResponse;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.ReferenceableProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.RelationshipProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.ResourceListProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;

import java.util.List;

/**
 * AssetOwnerBaseClient supports the common properties and functions for the Community Profile OMAS.
 */
public class AssetOwnerBaseClient extends ConnectedAssetClientBase implements RelatedElementsManagementInterface
{
    protected final AssetOwnerRESTClient restClient;               /* Initialized in constructor */

    protected static final String  serviceURLName = "asset-owner";
    protected static final String  defaultAssetType = "Asset";
    final protected String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}";

    private static final String elementsURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/related-elements";

    protected NullRequestBody nullRequestBody = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, auditLog);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog              logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String   serverName,
                                String   serverPlatformURLRoot,
                                int      maxPageSize,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, maxPageSize, auditLog);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, userId, password);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           pre-initialized parameter limit
     * @param auditLog              logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                int      maxPageSize,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, userId, password, auditLog);

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, userId, password, auditLog);

        this.restClient = new AssetOwnerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public AssetOwnerBaseClient(String               serverName,
                                String               serverPlatformURLRoot,
                                AssetOwnerRESTClient restClient,
                                int                  maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName);

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
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
    public AssetOwnerBaseClient(String               serverName,
                                String               serverPlatformURLRoot,
                                AssetOwnerRESTClient restClient,
                                int                  maxPageSize,
                                AuditLog             auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, auditLog);

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateObject(restClient, "this.restClient", "Asset Owner client constructor");

        this.restClient = restClient;
    }


    /* =====================================================================================================================
     * Basic client methods
     */


    /**
     * Create a new metadata element.
     *
     * @param userId                  calling user
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
    String createReferenceable(String                  userId,
                               ReferenceableProperties properties,
                               String                  propertiesParameterName,
                               String                  urlTemplate,
                               String                  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ReferenceableRequestBody requestBody = new ReferenceableRequestBody();

        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element.
     *
     * @param userId                  calling user
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
    String createReferenceableWithAnchor(String                  userId,
                                         String                  anchorGUID,
                                         String                  anchorGUIDParameterName,
                                         ReferenceableProperties properties,
                                         String                  propertiesParameterName,
                                         String                  urlTemplate,
                                         String                  methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ReferenceableRequestBody requestBody = new ReferenceableRequestBody();

        requestBody.setAnchorGUID(anchorGUID);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Update the metadata element.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId                   calling user
     * @param elementGUID              unique identifier of the metadata element to update
     * @param elementGUIDParameterName name of parameter passing the elementGUID
     * @param isMergeUpdate            should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties               new properties for the metadata element
     * @param propertiesParameterName  name of parameter passing the properties
     * @param urlTemplate              URL to call (no expected placeholders)
     * @param methodName               calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateReferenceable(String                  userId,
                             String                  elementGUID,
                             String                  elementGUIDParameterName,
                             boolean                 isMergeUpdate,
                             ReferenceableProperties properties,
                             String                  propertiesParameterName,
                             String                  urlTemplate,
                             String                  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        ReferenceableRequestBody requestBody = new ReferenceableRequestBody();

        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        isMergeUpdate);
    }


    /**
     * Add or update classification on referenceable.
     *
     * @param userId       calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param elementGUIDParameter parameter name for elementGUID
     * @param properties  properties of security at the site
     * @param urlTemplate URL to call with placeholder for guid
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setReferenceableClassification(String                   userId,
                                        String                   elementGUID,
                                        String                   elementGUIDParameter,
                                        ClassificationProperties properties,
                                        String                   urlTemplate,
                                        String                   methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException

    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        ClassificationRequestBody requestBody = new ClassificationRequestBody();

        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Remove classification from the referenceable.
     *
     * @param userId       calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param elementGUIDParameter parameter name for elementGUID
     * @param urlTemplate URL to call with placeholder for guid
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeReferenceableClassification(String userId,
                                           String elementGUID,
                                           String elementGUIDParameter,
                                           String urlTemplate,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException

    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Create a relationship between a primary element and a secondary element.
     *
     * @param userId                            calling user
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param relationshipName                  type name of relationship
     * @param properties                        describes the properties for the relationship
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupRelationship(String                 userId,
                           String                 primaryElementGUID,
                           String                 primaryElementGUIDParameterName,
                           String                 relationshipName,
                           RelationshipProperties properties,
                           String                 secondaryElementGUID,
                           String                 secondaryElementGUIDParameterName,
                           String                 urlTemplate,
                           String                 methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setRelationshipName(relationshipName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        primaryElementGUID,
                                        secondaryElementGUID);
    }


    /**
     * Create a relationship between a primary element and a secondary element.
     *
     * @param userId                            calling user
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param relationshipName                  type name of relationship
     * @param properties                        describes the properties for the relationship
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param methodName                        calling method
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String setupMultiLinkRelationship(String                 userId,
                                      String                 primaryElementGUID,
                                      String                 primaryElementGUIDParameterName,
                                      String                 relationshipName,
                                      RelationshipProperties properties,
                                      String                 secondaryElementGUID,
                                      String                 secondaryElementGUIDParameterName,
                                      String                 urlTemplate,
                                      String                 methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setRelationshipName(relationshipName);
        requestBody.setProperties(properties);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                urlTemplate,
                                                                requestBody,
                                                                serverName,
                                                                userId,
                                                                primaryElementGUID,
                                                                secondaryElementGUID);

        return response.getGUID();
    }


    /**
     * Update the properties of a multi-link relationship.
     *
     * @param userId calling user
     * @param relationshipGUID                unique identifier of the primary element
     * @param relationshipGUIDParameterName   name of parameter passing the relationshipGUID
     * @param isMergeUpdate                    should the supplied properties overlay the existing properties or replace them
     * @param relationshipName                  type name of relationship
     * @param properties                        describes the properties for the relationship
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateRelationship(String                 userId,
                                   String                 relationshipGUID,
                                   String                 relationshipGUIDParameterName,
                                   boolean                isMergeUpdate,
                                   String                 relationshipName,
                                   RelationshipProperties properties,
                                   String                 urlTemplate,
                                   String                 methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setRelationshipName(relationshipName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        relationshipGUID,
                                        isMergeUpdate);
    }



    /**
     * Remove a relationship multi-link.
     *
     * @param userId                            calling user
     * @param relationshipGUID                  unique identifier of the relationship
     * @param relationshipGUIDParameterName     name of parameter passing the relationshipGUID
     * @param relationshipName                  type name of relationship
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearRelationship(String userId,
                           String relationshipGUID,
                           String relationshipGUIDParameterName,
                           String relationshipName,
                           String urlTemplate,
                           String methodName) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setRelationshipName(relationshipName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Remove a relationship uni-link.
     *
     * @param userId                            calling user
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param relationshipName                  type name of relationship
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearRelationship(String userId,
                           String primaryElementGUID,
                           String primaryElementGUIDParameterName,
                           String relationshipName,
                           String secondaryElementGUID,
                           String secondaryElementGUIDParameterName,
                           String urlTemplate,
                           String methodName) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setRelationshipName(relationshipName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        primaryElementGUID,
                                        secondaryElementGUID);
    }


    /**
     * Retrieve a relationship.
     *
     * @param userId   calling user
     * @param startingElementGUID   unique identifier of the primary element
     * @param startingElementGUIDParameterName   name of parameter passing the startingElementGUID
     * @param urlTemplate  URL to call (no expected placeholders)
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElement> getRelatedElements(String userId,
                                            String startingElementGUID,
                                            String startingElementGUIDParameterName,
                                            String urlTemplate,
                                            int    startFrom,
                                            int    pageSize,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingElementGUIDParameterName, methodName);

        RelatedElementListResponse restResult = restClient.callRelatedElementListGetRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             serverName,
                                                                                             userId,
                                                                                             startingElementGUID,
                                                                                             Integer.toString(startFrom),
                                                                                             Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Retrieve an element stub via a name.
     *
     * @param userId   calling user
     * @param name   unique identifier of the primary element
     * @param nameParameterName   name of parameter passing the name
     * @param urlTemplate  URL to call (no expected placeholders)
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName    calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ElementStub> getElementStubsByName(String userId,
                                            String name,
                                            String nameParameterName,
                                            String urlTemplate,
                                            int    startFrom,
                                            int    pageSize,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        ElementStubListResponse restResult = restClient.callElementStubListGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       name,
                                                                                       Integer.toString(startFrom),
                                                                                       Integer.toString(pageSize));

        return restResult.getElements();
    }



    /**
     * Remove the metadata element.
     *
     * @param userId                   calling user
     * @param elementGUID              unique identifier of the metadata element to remove
     * @param elementGUIDParameterName name of parameter passing the elementGUID
     * @param urlTemplate              URL to call (no expected placeholders)
     * @param methodName               calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeReferenceable(String userId,
                                    String elementGUID,
                                    String elementGUIDParameterName,
                                    String urlTemplate,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Create a "MoreInformation" relationship between an element that is descriptive and one that is providing the detail.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param properties         properties of the relationship
     * @param detailGUID         unique identifier of the element that provides the detail
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupMoreInformation(String                 userId,
                                     String                 elementGUID,
                                     RelationshipProperties properties,
                                     String                 detailGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName               = "setupMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/more-information/{3}";

        this.setupRelationship(userId,
                               elementGUID,
                               elementGUIDParameterName,
                               null,
                               properties,
                               detailGUID,
                               detailGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Remove a "MoreInformation" relationship between two referenceables.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearMoreInformation(String userId,
                                     String elementGUID,
                                     String detailGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName               = "clearMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/more-information/{3}/delete";

        this.clearRelationship(userId,
                               elementGUID,
                               elementGUIDParameterName,
                               null,
                               detailGUID,
                               detailGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Retrieve the detail elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getMoreInformation(String userId,
                                                   String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "getMoreInformation";
        final String guidPropertyName  = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/more-information/by-descriptive-element/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, elementGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param userId             calling user
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getDescriptiveElements(String userId,
                                                       String detailGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName        = "getDescriptiveElements";
        final String guidPropertyName  = "detailGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/more-information/by-detail-element/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, detailGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }




    /**
     * Create a "Stakeholder" relationship between an element and its stakeholder.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param properties         properties of the relationship
     * @param stakeholderGUID    unique identifier of the stakeholder
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupStakeholder(String                userId,
                                 String                elementGUID,
                                 StakeholderProperties properties,
                                 String                stakeholderGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                   = "setupStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/stakeholders/{3}";

        this.setupRelationship(userId,
                               elementGUID,
                               elementGUIDParameterName,
                               null,
                               properties,
                               stakeholderGUID,
                               stakeholderGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Remove a "Stakeholder" relationship between two referenceables.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearStakeholder(String userId,
                                 String elementGUID,
                                 String stakeholderGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                   = "clearStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/stakeholders/{3}/delete";

        this.clearRelationship(userId,
                               elementGUID,
                               elementGUIDParameterName,
                               null,
                               stakeholderGUID,
                               stakeholderGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Retrieve the stakeholder elements linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getStakeholders(String userId,
                                                String elementGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName        = "getStakeholders";
        final String guidPropertyName  = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/stakeholders/by-commissioned-element/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, elementGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the elements commissioned by a stakeholder, linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param userId calling user
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getStakeholderCommissionedElements(String userId,
                                                                   String stakeholderGUID,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName        = "getStakeholderCommissionedElements";
        final String guidPropertyName  = "stakeholderGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/stakeholders/by-stakeholder/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, stakeholderGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }

    
    /**
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param properties properties of the relationship
     * @param resourceGUID unique identifier of the resource
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupResource(String                 userId,
                              String                 elementGUID,
                              ResourceListProperties properties,
                              String                 resourceGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                = "setupResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/resource-list/{3}";

        this.setupRelationship(userId,
                               elementGUID,
                               elementGUIDParameterName,
                               null,
                               properties,
                               resourceGUID,
                               resourceGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Remove a "ResourceList" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearResource(String userId,
                              String elementGUID,
                              String resourceGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName                = "clearResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/resource-list/{3}/delete";

        this.clearRelationship(userId,
                               elementGUID,
                               elementGUIDParameterName,
                               null,
                               resourceGUID,
                               resourceGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getResourceList(String userId,
                                                String elementGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName        = "getResourceList";
        final String guidPropertyName  = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/resource-list/by-assignee/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, elementGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param userId calling user
     * @param resourceGUID unique identifier of the resource
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getSupportedByResource(String userId,
                                                       String resourceGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName        = "getSupportedByResource";
        final String guidPropertyName  = "resourceGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/resource-list/by-resource/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, resourceGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }
}
