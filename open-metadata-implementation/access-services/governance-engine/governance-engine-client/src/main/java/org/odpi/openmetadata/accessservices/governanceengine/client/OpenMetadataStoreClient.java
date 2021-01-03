/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceEngineEventListener;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.accessservices.governanceengine.connectors.outtopic.GovernanceEngineOutTopicClientConnector;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceEngineElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc.OMAGOCFErrorCode;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class OpenMetadataStoreClient extends OpenMetadataClient
{
    private String                     serverName;               /* Initialized in constructor */
    private String                     serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceEngineRESTClient restClient;               /* Initialized in constructor */
    private String                     callerId;                 /* Initialized in constructor */
    private String                     userId;                   /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();

    private AuditLog auditLog = null;

    private static final String  serviceName = AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName();
    private GovernanceEngineOutTopicClientConnector configurationEventTopicConnector = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param callerId unique identifier for caller's listener
     * @param userId caller's userId
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public OpenMetadataStoreClient(String serverName,
                                   String serverPlatformURLRoot,
                                   String callerId,
                                   String userId) throws InvalidParameterException
    {
        super(serverPlatformURLRoot, serverName);

        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new GovernanceEngineRESTClient(serverName, serverPlatformURLRoot);
        this.callerId = callerId;
        this.userId = userId;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param serverUserId caller's userId embedded in all HTTP requests
     * @param serverPassword caller's userId embedded in all HTTP requests
     * @param callerId unique identifier for caller's listener
     * @param userId caller's userId
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public OpenMetadataStoreClient(String serverName,
                                   String serverPlatformURLRoot,
                                   String serverUserId,
                                   String serverPassword,
                                   String callerId,
                                   String userId) throws InvalidParameterException
    {
        super(serverPlatformURLRoot, serverName);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new GovernanceEngineRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
        this.callerId = callerId;
        this.userId = userId;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @param callerId unique identifier for caller's listener
     * @param userId caller's userId
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OpenMetadataStoreClient(String                     serverName,
                                   String                     serverPlatformURLRoot,
                                   GovernanceEngineRESTClient restClient,
                                   int                        maxPageSize,
                                   AuditLog                   auditLog,
                                   String                     callerId,
                                   String                     userId) throws InvalidParameterException
    {
        super(serverPlatformURLRoot, serverName);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = restClient;
        this.auditLog = auditLog;
        this.callerId = callerId;
        this.userId = userId;
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByGUID(String elementGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "getMetadataElementByGUID";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/open-metadata-store/elements/{2}";

        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        OpenMetadataElementResponse restResult = restClient.callOpenMetadataElementGetRESTCall(methodName,
                                                                                                serverPlatformURLRoot + urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                elementGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElementsWithString(String searchString,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findMetadataElementsWithString";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/open-metadata-store/elements/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsGetRESTCall(methodName,
                                                                                                 serverPlatformURLRoot + urlTemplate,
                                                                                                 requestBody,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 Integer.toString(startFrom),
                                                                                                 Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<RelatedMetadataElement> getRelatedMetadataElements(String elementGUID,
                                                                   String relationshipTypeName,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getRelatedMetadataElements";
        final String guidParameterName = "elementGUID";
        final String typeNameParameterName = "relationshipTypeName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/open-metadata-store/related-elements/type/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        RelatedMetadataElementsResponse restResult = restClient.callRelatedMetadataElementsGetRESTCall(methodName,
                                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       relationshipTypeName,
                                                                                                       Integer.toString(startFrom),
                                                                                                       Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param matchClassifications Optional list of classifications to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElements(String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeName,
                                                          SearchProperties      searchProperties,
                                                          List<ElementStatus>   limitResultsByStatus,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return null;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           relationshipTypeName,
                                                                                   SearchProperties matchProperties,
                                                                                   Date asOfTime,
                                                                                   String           sequencingProperty,
                                                                                   SequencingOrder  sequencingOrder,
                                                                                   int              startFrom,
                                                                                   int              pageSize) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        return null;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This metadata element will be given an initial status of ACTIVE which is sufficient for most types of elements.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param properties properties of the new metadata element
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all of the attachments such as nested content, schema
     *                     connection etc)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementProperties properties,
                                               String            templateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return null;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param properties properties of the new metadata element
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all of the attachments such as nested content, schema
     *                     connection etc)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               ElementProperties properties,
                                               String            templateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return null;
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all of them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {

    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void updateMetadataElementStatusInStore(String        metadataElementGUID,
                                                    ElementStatus newElementStatus) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {

    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void deleteMetadataElementInStore(String metadataElementGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {

    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void classifyMetadataElementInStore(String            metadataElementGUID,
                                                String            classificationName,
                                                ElementProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {

    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void reclassifyMetadataElementInStore(String            metadataElementGUID,
                                                  String            classificationName,
                                                  boolean           replaceProperties,
                                                  ElementProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {

    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void unclassifyMetadataElementInStore(String metadataElementGUID,
                                                  String classificationName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {

    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createRelatedElementsInStore(String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return null;
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {

    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelatedElementsInStore(String relationshipGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {

    }

    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param actionTargetGUID unique identifier of the governance action service.
     * @param status status enum to show its progress
     * @param startDate date/time that the governance action service started processing the target
     * @param completionDate date/time that the governance process completed processing this target.
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void updateActionTargetStatus(String                 actionTargetGUID,
                                         GovernanceActionStatus status,
                                         Date                   startDate,
                                         Date                   completionDate) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {

    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void recordCompletionStatus(CompletionStatus status,
                                       List<String>     outputGuards) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {

    }




    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargetGUIDs list of action targets for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     * @param requestType request type to identify the governance action service to run
     * @param guards guards to pass on to the requested action
     * @param requestProperties properties to pass to the governance action service
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceAction(String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           List<String>        requestSourceGUIDs,
                                           List<String>        actionTargetGUIDs,
                                           Date                startTime,
                                           String              requestType,
                                           String              guards,
                                           Map<String, String> requestProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return null;
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargetGUIDs list of action targets for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String       processQualifiedName,
                                                  List<String> requestSourceGUIDs,
                                                  List<String> actionTargetGUIDs,
                                                  Date         startTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return null;
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  String createIncidentReport(String                        qualifiedName,
                                        int                           domainIdentifier,
                                        String                        background,
                                        List<IncidentImpactedElement> impactedResources,
                                        List<IncidentDependency>      previousIncidents,
                                        Map<String, Integer>          incidentClassifiers,
                                        Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return null;
    }


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
     * listener and the parameters that control the types of events received while it is running.
     *
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     *
     * If interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     *
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types.
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    @Override
    public  void registerListener(WatchdogGovernanceListener listener,
                                  List<WatchdogEventType>    interestingEventTypes,
                                  List<String>               interestingMetadataTypes) throws InvalidParameterException
    {

    }


    /*
     *==================================================================================
     */
    /**
     * Register a listener object that will be passed each of the events published by
     * the Governance Engine OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(String                        userId,
                                 GovernanceEngineEventListener listener) throws InvalidParameterException,
                                                                                ConnectionCheckedException,
                                                                                ConnectorCheckedException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "registerListener";
        final String nameParameter = "listener";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/topics/out-topic-connection/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(listener, nameParameter, methodName);

        if (configurationEventTopicConnector == null)
        {
            /*
             * The connector is only created if/when a listener is registered to prevent unnecessary load on the
             * event bus.
             */
            ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                                 serverPlatformURLRoot + urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 callerId);

            Connection      topicConnection = restResult.getConnection();
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector       connector       = connectorBroker.getConnector(topicConnection);

            if (connector == null)
            {
                throw new ConnectorCheckedException(OMAGOCFErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                  serviceName,
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            if (connector instanceof GovernanceEngineOutTopicClientConnector)
            {
                configurationEventTopicConnector = (GovernanceEngineOutTopicClientConnector)connector;
                configurationEventTopicConnector.setAuditLog(auditLog);
                configurationEventTopicConnector.start();
            }
            else
            {
                throw new ConnectorCheckedException(OMAGOCFErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                  serviceName,
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot,
                                                                                                                  GovernanceEngineOutTopicClientConnector.class.getName()),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }

        configurationEventTopicConnector.registerListener(userId, listener);
    }


    /**
     * Create a new governance engine definition.
     *
     * @param userId identifier of calling user
     * @param governanceEngineType type of governance engine to create
     * @param qualifiedName unique name for the governance engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of governance services that will be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    public String createGovernanceEngine(String userId,
                                         String governanceEngineType,
                                         String qualifiedName,
                                         String displayName,
                                         String description) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "createGovernanceEngine";
        final String engineTypeParameterName = "governanceEngineType";
        final String nameParameterName = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/new/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceEngineType, engineTypeParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        NewGovernanceEngineRequestBody requestBody = new NewGovernanceEngineRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceEngineType);

        return restResult.getGUID();
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByGUID(String    userId,
                                                             String    guid) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getGovernanceEngineByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        GovernanceEngineElementResponse restResult = restClient.callGovernanceEngineGetRESTCall(methodName,
                                                                                                serverPlatformURLRoot + urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                guid);

        return restResult.getElement();
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public  GovernanceEngineElement getGovernanceEngineByName(String    userId,
                                                              String    name) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getGovernanceEngineByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/by-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        GovernanceEngineElementResponse restResult = restClient.callGovernanceEngineGetRESTCall(methodName,
                                                                                                serverPlatformURLRoot + urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                name);
        return restResult.getElement();
    }


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param governanceEngineType type of governance engine to create
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definitions.
     */
    public  List<GovernanceEngineElement> getAllGovernanceEngines(String userId,
                                                                  String governanceEngineType,
                                                                  int    startingFrom,
                                                                  int    maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getAllGovernanceEngines";
        final String engineTypeParameterName = "governanceEngineType";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceEngineType, engineTypeParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        GovernanceEngineElementsResponse restResult = restClient.callGovernanceEnginesGetRESTCall(methodName,
                                                                                                  serverPlatformURLRoot + urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  governanceEngineType,
                                                                                                  Integer.toString(startingFrom),
                                                                                                  Integer.toString(maximumResults));

        return restResult.getElements();
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName new value for unique name of governance engine.
     * @param displayName new value for the display name.
     * @param description new description for the governance engine.
     * @param typeDescription new description of the type ofg governance engine.
     * @param version new version number for the governance engine implementation.
     * @param patchLevel new patch level for the governance engine implementation.
     * @param source new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    public  void    updateGovernanceEngine(String                userId,
                                           String                guid,
                                           String                qualifiedName,
                                           String                displayName,
                                           String                description,
                                           String                typeDescription,
                                           String                version,
                                           String                patchLevel,
                                           String                source,
                                           Map<String, String>   additionalProperties,
                                           Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "updateGovernanceEngine";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        UpdateGovernanceEngineRequestBody requestBody = new UpdateGovernanceEngineRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setTypeDescription(typeDescription);
        requestBody.setVersion(version);
        requestBody.setPatchLevel(patchLevel);
        requestBody.setSource(source);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        guid);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public  void   deleteGovernanceEngine(String  userId,
                                          String  guid,
                                          String  qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "deleteGovernanceEngine";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        guid);
    }


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param userId identifier of calling user
     * @param governanceServiceType type of the governance service to create
     * @param qualifiedName  unique name for the governance service.
     * @param displayName   display name for the governance service.
     * @param description  description of the analysis provided by the governance service.
     * @param connection   connection to instanciate the governance service implementation.
     *
     * @return unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public String  createGovernanceService(String     userId,
                                           String     governanceServiceType,
                                           String     qualifiedName,
                                           String     displayName,
                                           String     description,
                                           Connection connection) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "createGovernanceService";
        final String serviceTypeParameterName = "governanceServiceType";
        final String nameParameterName = "qualifiedName";
        final String connectionParameterName = "connection";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services/new/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceServiceType, serviceTypeParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        NewGovernanceServiceRequestBody requestBody = new NewGovernanceServiceRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setConnection(connection);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceServiceType);

        return restResult.getGUID();
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public GovernanceServiceElement getGovernanceServiceByGUID(String userId,
                                                               String guid) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "getGovernanceServiceByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        GovernanceServiceElementResponse restResult = restClient.callGovernanceServiceGetRESTCall(methodName,
                                                                                                  serverPlatformURLRoot + urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  guid);

        return restResult.getElement();
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public  GovernanceServiceElement getGovernanceServiceByName(String    userId,
                                                                String    name) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "getGovernanceServiceByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services/by-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        GovernanceServiceElementResponse restResult = restClient.callGovernanceServiceGetRESTCall(methodName,
                                                                                                  serverPlatformURLRoot + urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  name);

        return restResult.getElement();
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definitions.
     */
    public  List<GovernanceServiceElement> getAllGovernanceServices(String  userId,
                                                                    String  governanceServiceType,
                                                                    int     startingFrom,
                                                                    int     maximumResults) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getAllGovernanceServices";
        final String serviceTypeParameterName = "governanceServiceType";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services?startingFrom={2}&maximumResults={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceServiceType, serviceTypeParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        GovernanceServiceElementsResponse restResult = restClient.callGovernanceServicesGetRESTCall(methodName,
                                                                                                    serverPlatformURLRoot + urlTemplate,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    Integer.toString(startingFrom),
                                                                                                    Integer.toString(maximumResults));

        return restResult.getGovernanceServices();
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param userId identifier of calling user
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public  List<String>  getGovernanceServiceRegistrations(String   userId,
                                                            String   governanceServiceGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceServiceRegistrations";
        final String guidParameter = "governanceServiceGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services/{2}/registrations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, guidParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         serverPlatformURLRoot + urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         governanceServiceGUID);

        return restResult.getGUIDs();
    }


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName new value for unique name of governance service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public void updateGovernanceService(String              userId,
                                        String              guid,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        Connection          connection,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "updateGovernanceService";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   connectionParameterName = "connection";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        UpdateGovernanceServiceRequestBody requestBody = new UpdateGovernanceServiceRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setConnection(connection);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        guid);
    }


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public void deleteGovernanceService(String  userId,
                                        String  guid,
                                        String  qualifiedName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "deleteGovernanceService";
        final String guidParameterName = "guid";
        final String nameParameterName = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-services/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        guid);
    }


    /**
     * Register a governance service with a specific governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type that this governance service is able to process.
     * @param requestParameters list of parameters that are passed the the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void registerGovernanceServiceWithEngine(String               userId,
                                                    String               governanceEngineGUID,
                                                    String               governanceServiceGUID,
                                                    String               governanceRequestType,
                                                    Map<String, String>  requestParameters) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "registerGovernanceServiceWithEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String governanceRequestTypesParameter = "governanceRequestType";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/governance-services";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);
        invalidParameterHandler.validateName(governanceRequestType, governanceRequestTypesParameter, methodName);

        GovernanceServiceRegistrationRequestBody requestBody = new GovernanceServiceRegistrationRequestBody();
        requestBody.setGovernanceServiceGUID(governanceServiceGUID);
        requestBody.setRequestType(governanceRequestType);
        requestBody.setRequestParameters(requestParameters);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceEngineGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
    }


    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String  userId,
                                                                             String  governanceEngineGUID,
                                                                             String  governanceServiceGUID) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceService";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/governance-services/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        RegisteredGovernanceServiceResponse restResult = restClient.callRegisteredGovernanceServiceGetRESTCall(methodName,
                                                                                                             serverPlatformURLRoot + urlTemplate,
                                                                                                             serverName,
                                                                                                             userId,
                                                                                                             governanceEngineGUID,
                                                                                                             governanceServiceGUID);

        return restResult.getRegisteredGovernanceService();
    }


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public List<String> getRegisteredGovernanceServices(String  userId,
                                                        String  governanceEngineGUID,
                                                        int     startingFrom,
                                                        int     maximumResults) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceServices";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/governance-services?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         serverPlatformURLRoot + urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         governanceEngineGUID,
                                                                         startingFrom,
                                                                         maximumResults);

        return restResult.getGUIDs();
    }


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void unregisterGovernanceServiceFromEngine(String userId,
                                                      String governanceEngineGUID,
                                                      String governanceServiceGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "unregisterGovernanceServiceFromEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/governance-services/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        governanceEngineGUID,
                                        governanceServiceGUID);

    }
}
