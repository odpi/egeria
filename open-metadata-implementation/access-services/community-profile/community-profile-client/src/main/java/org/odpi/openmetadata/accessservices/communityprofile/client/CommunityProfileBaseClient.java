/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.RelatedElementsManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssignmentScopeProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ClassificationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ReferenceableProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.RelationshipProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ResourceListProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.StakeholderProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * CommunityProfileBaseClient supports the common properties and functions for the Community Profile OMAS.
 */
public class CommunityProfileBaseClient implements RelatedElementsManagementInterface
{
    final String serverName;               /* Initialized in constructor */
    final String serverPlatformURLRoot;    /* Initialized in constructor */

    final InvalidParameterHandler    invalidParameterHandler = new InvalidParameterHandler();
    final CommunityProfileRESTClient restClient;               /* Initialized in constructor */

    private static final String elementsURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/related-elements";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog              logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CommunityProfileBaseClient(String serverName,
                                      String serverPlatformURLRoot,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CommunityProfileBaseClient(String serverName,
                                      String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CommunityProfileBaseClient(String serverName,
                                      String serverPlatformURLRoot,
                                      String userId,
                                      String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CommunityProfileBaseClient(String serverName,
                                      String serverPlatformURLRoot,
                                      String userId,
                                      String password,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CommunityProfileBaseClient(String serverName,
                                      String serverPlatformURLRoot,
                                      CommunityProfileRESTClient restClient,
                                      int maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }


    /* =====================================================================================================================
     * Basic client methods
     */


    /**
     * Create a new metadata element.
     *
     * @param userId                  calling user
     * @param externalSourceGUID      unique identifier of software capability representing the caller
     * @param externalSourceName      unique name of software capability representing the caller
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
                               String                  externalSourceGUID,
                               String                  externalSourceName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param urlTemplate        URL to call (with placeholders)
     * @param methodName         calling method
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createReferenceableFromTemplate(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             templateGUID,
                                           TemplateProperties templateProperties,
                                           String             urlTemplate,
                                           String             methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";
        final String propertiesParameterName    = "templateProperties";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

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
                                         String                  externalSourceGUID,
                                         String                  externalSourceName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
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
     * @param externalSourceGUID       unique identifier of software capability representing the caller
     * @param externalSourceName       unique name of software capability representing the caller
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
                             String                  externalSourceGUID,
                             String                  externalSourceName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
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
                                        String                   externalSourceGUID,
                                        String                   externalSourceName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
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
                                           String externalSourceGUID,
                                           String externalSourceName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

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
     * @param externalSourceGUID                unique identifier of software capability representing the caller
     * @param externalSourceName                unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
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
                           String                 externalSourceGUID,
                           String                 externalSourceName,
                           String                 primaryElementGUID,
                           String                 primaryElementGUIDParameterName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
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
     * Update the properties of the relationship between two elements.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID                unique identifier of software capability representing the caller
     * @param externalSourceName                unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
     * @param properties                        describes the properties for the relationship
     * @param secondaryElementGUID              unique identifier of the element to connect it to
     * @param secondaryElementGUIDParameterName name of parameter passing the secondaryElementGUID
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param urlTemplate                       URL to call (no expected placeholders)
     * @param methodName                        calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateRelationship(String                 userId,
                                   String                 externalSourceGUID,
                                   String                 externalSourceName,
                                   String                 primaryElementGUID,
                                   String                 primaryElementGUIDParameterName,
                                   RelationshipProperties properties,
                                   String                 secondaryElementGUID,
                                   String                 secondaryElementGUIDParameterName,
                                   boolean                isMergeUpdate,
                                   String                 urlTemplate,
                                   String                 methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryElementGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(secondaryElementGUID, secondaryElementGUIDParameterName, methodName);

        RelationshipRequestBody requestBody = new RelationshipRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, primaryElementGUID, secondaryElementGUID, isMergeUpdate);
    }



    /**
     * Remove a relationship.
     *
     * @param userId                            calling user
     * @param externalSourceGUID                unique identifier of software capability representing the caller
     * @param externalSourceName                unique name of software capability representing the caller
     * @param primaryElementGUID                unique identifier of the primary element
     * @param primaryElementGUIDParameterName   name of parameter passing the primaryElementGUID
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
                           String externalSourceGUID,
                           String externalSourceName,
                           String primaryElementGUID,
                           String primaryElementGUIDParameterName,
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

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

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
     * Remove the metadata element.
     *
     * @param userId                   calling user
     * @param externalSourceGUID       unique identifier of software capability representing the caller
     * @param externalSourceName       unique name of software capability representing the caller
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
                                    String externalSourceGUID,
                                    String externalSourceName,
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

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
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
                                     String                 externalSourceGUID,
                                     String                 externalSourceName,
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
                               externalSourceGUID,
                               externalSourceName,
                               elementGUID,
                               elementGUIDParameterName,
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearMoreInformation(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
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
                               externalSourceGUID,
                               externalSourceName,
                               elementGUID,
                               elementGUIDParameterName,
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
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
                                 String                externalSourceGUID,
                                 String                externalSourceName,
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
                               externalSourceGUID,
                               externalSourceName,
                               elementGUID,
                               elementGUIDParameterName,
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearStakeholder(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
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
                               externalSourceGUID,
                               externalSourceName,
                               elementGUID,
                               elementGUIDParameterName,
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
     * Create an "AssignmentScope" relationship between an element and its scope.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param properties properties of the relationship
     * @param scopeGUID unique identifier of the scope
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupAssignmentScope(String                    userId,
                                     String                    externalSourceGUID,
                                     String                    externalSourceName,
                                     String                    elementGUID,
                                     AssignmentScopeProperties properties,
                                     String                    scopeGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName               = "setupAssignmentScope";
        final String elementGUIDParameterName = "elementGUID";
        final String scopeGUIDParameterName   = "scopeGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/assignment-scopes/{3}";

        this.setupRelationship(userId,
                               externalSourceGUID,
                               externalSourceName,
                               elementGUID,
                               elementGUIDParameterName,
                               properties,
                               scopeGUID,
                               scopeGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


    /**
     * Remove an "AssignmentScope" relationship between two referenceables.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param scopeGUID unique identifier of the scope
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearAssignmentScope(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String elementGUID,
                                     String scopeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName               = "clearAssignmentScope";
        final String elementGUIDParameterName = "elementGUID";
        final String scopeGUIDParameterName   = "scopeGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/assignment-scopes/{3}/delete";

        this.clearRelationship(userId, externalSourceGUID, externalSourceName, elementGUID, elementGUIDParameterName, scopeGUID, scopeGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the assigned scopes linked by the "AssignmentScope" relationship between two referenceables.
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
    public List<RelatedElement> getAssignedScopes(String userId,
                                                  String elementGUID,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName        = "getAssignedScopes";
        final String guidPropertyName  = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/assignment-scopes/by-assigned-actor/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, elementGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the assigned actors linked by the "AssignmentScope" relationship between two referenceables.
     *
     * @param userId calling user
     * @param scopeGUID unique identifier of the scope
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getAssignedActors(String userId,
                                                  String scopeGUID,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName        = "getAssignedActors";
        final String guidPropertyName  = "scopeGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/assignment-scopes/by-assigned-scope/{2}?startFrom={3}&pageSize={4}";

        return this.getRelatedElements(userId, scopeGUID, guidPropertyName, urlTemplate, startFrom, pageSize, methodName);
    }



    /**
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
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
                              String                 externalSourceGUID,
                              String                 externalSourceName,
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
                               externalSourceGUID,
                               externalSourceName,
                               elementGUID,
                               elementGUIDParameterName,
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearResource(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String elementGUID,
                              String resourceGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName                = "clearResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        final String urlTemplate = serverPlatformURLRoot + elementsURLTemplatePrefix + "/{2}/resource-list/{3}/delete";

        this.clearRelationship(userId, externalSourceGUID, externalSourceName, elementGUID, elementGUIDParameterName, resourceGUID, resourceGUIDParameterName, urlTemplate, methodName);
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
