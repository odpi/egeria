/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.client;

import org.odpi.openmetadata.commonservices.gaf.api.MetadataElementInterface;
import org.odpi.openmetadata.commonservices.gaf.api.MultiLanguageInterface;
import org.odpi.openmetadata.commonservices.gaf.api.SpecialGovernanceActionInterface;
import org.odpi.openmetadata.commonservices.gaf.api.ValidMetadataValuesInterface;
import org.odpi.openmetadata.commonservices.gaf.client.rest.OpenMetadataStoreRESTClient;
import org.odpi.openmetadata.commonservices.gaf.properties.TranslationDetail;
import org.odpi.openmetadata.commonservices.gaf.properties.ValidMetadataValue;
import org.odpi.openmetadata.commonservices.gaf.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenMetadataStoreClientBase sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public abstract class OpenMetadataStoreClientBase implements MetadataElementInterface,
                                                             SpecialGovernanceActionInterface,
                                                             MultiLanguageInterface,
                                                             ValidMetadataValuesInterface
{
    private final String                      serverName;               /* Initialized in constructor */
    private final String                      serviceURLMarker;         /* Initialized in constructor */
    private final String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private final OpenMetadataStoreRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final PropertyHelper  propertyHelper  = new PropertyHelper();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataStoreClientBase(String serviceURLMarker,
                                       String serverName,
                                       String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new OpenMetadataStoreRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataStoreClientBase(String serviceURLMarker,
                                       String serverName,
                                       String serverPlatformURLRoot,
                                       String serverUserId,
                                       String serverPassword) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new OpenMetadataStoreRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OpenMetadataStoreClientBase(String            serviceURLMarker,
                                       String            serverName,
                                       String            serverPlatformURLRoot,
                                       OpenMetadataStoreRESTClient restClient,
                                       int               maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = restClient;
    }


    /**
     * Convert the date into a long to send in URL.
     *
     * @param effectiveTime date/time object or null
     *
     * @return long representing milliseconds since epoch
     */
    private long getEffectiveTimeAsLong(Date effectiveTime)
    {
        long effectiveTimeLong = 0;

        if (effectiveTime != null)
        {
            effectiveTimeLong = effectiveTime.getTime();
        }

        return effectiveTimeLong;
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId      caller's userId
     * @param elementGUID unique identifier for the metadata element
     *
     * @return metadata element properties
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByGUID(String  userId,
                                                        String  elementGUID,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName        = "getMetadataElementByGUID";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}?forLineage={4}&forDuplicateProcessing={5}&effectiveTime={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        OpenMetadataElementResponse restResult = restClient.callOpenMetadataElementGetRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               serverName,
                                                                                               serviceURLMarker,
                                                                                               userId,
                                                                                               elementGUID,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing,
                                                                                               this.getEffectiveTimeAsLong(effectiveTime));

        return restResult.getElement();
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByUniqueName(String  userId,
                                                              String  uniqueName,
                                                              String  uniquePropertyName,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName          = "getMetadataElementByUniqueName";
        final String defaultPropertyName = "qualifiedName";
        final String nameParameterName   = "uniqueName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/by-unique-name?forLineage={3}&forDuplicateProcessing={4}&effectiveTime={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(uniqueName);
        requestBody.setNameParameterName(nameParameterName);

        if (uniquePropertyName != null)
        {
            requestBody.setNamePropertyName(uniquePropertyName);
        }
        else
        {
            requestBody.setNamePropertyName(defaultPropertyName);
        }

        OpenMetadataElementResponse restResult = restClient.callOpenMetadataElementPostRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                requestBody,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                userId,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                this.getEffectiveTimeAsLong(effectiveTime));

        return restResult.getElement();
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId                 caller's userId
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public String getMetadataElementGUIDByUniqueName(String  userId,
                                                     String  uniqueName,
                                                     String  uniquePropertyName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName          = "getMetadataElementGUIDByUniqueName";
        final String defaultPropertyName = "qualifiedName";
        final String nameParameterName   = "uniqueName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/guid-by-unique-name?forLineage={3}&forDuplicateProcessing={4}&effectiveTime={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(uniqueName);
        requestBody.setNameParameterName(nameParameterName);

        if (uniquePropertyName != null)
        {
            requestBody.setNamePropertyName(uniquePropertyName);
        }
        else
        {
            requestBody.setNamePropertyName(defaultPropertyName);
        }

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  this.getEffectiveTimeAsLong(effectiveTime));

        return restResult.getGUID();
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param userId                 caller's userId
     * @param searchString           name to retrieve
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElementsWithString(String  userId,
                                                                    String  searchString,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing,
                                                                    Date    effectiveTime,
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                = "findMetadataElementsWithString";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/by-search-string?forLineage={3}&forDuplicateProcessing={4}&effectiveTime={5}&startFrom={6}&pageSize={7}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  this.getEffectiveTimeAsLong(effectiveTime),
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId                 caller's userId
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<RelatedMetadataElement> getRelatedMetadataElements(String  userId,
                                                                   String  elementGUID,
                                                                   int     startingAtEnd,
                                                                   String  relationshipTypeName,
                                                                   boolean forLineage,
                                                                   boolean forDuplicateProcessing,
                                                                   Date    effectiveTime,
                                                                   int     startFrom,
                                                                   int     pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName            = "getRelatedMetadataElements";
        final String guidParameterName     = "elementGUID";
        final String typeNameParameterName = "relationshipTypeName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/type/{4}?startingAtEnd={5}&forLineage={6}&forDuplicateProcessing={7}&effectiveTime={8}&startFrom={9}&pageSize={10}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        RelatedMetadataElementListResponse restResult = restClient.callRelatedMetadataElementListGetRESTCall(methodName,
                                                                                                             urlTemplate,
                                                                                                             serverName,
                                                                                                             serviceURLMarker,
                                                                                                             userId,
                                                                                                             elementGUID,
                                                                                                             relationshipTypeName,
                                                                                                             Integer.toString(startingAtEnd),
                                                                                                             forLineage,
                                                                                                             forDuplicateProcessing,
                                                                                                             this.getEffectiveTimeAsLong(effectiveTime),
                                                                                                             Integer.toString(startFrom),
                                                                                                             Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param searchProperties           Optional list of entity property conditions to match.
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param matchClassifications       Optional list of classifications to match.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElements(String                userId,
                                                          String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeName,
                                                          SearchProperties      searchProperties,
                                                          List<ElementStatus>   limitResultsByStatus,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
                                                          boolean               forLineage,
                                                          boolean               forDuplicateProcessing,
                                                          Date                  effectiveTime,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "findMetadataElements";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/by-search-specification?forLineage={3}&forDuplicateProcessing={4}&effectiveTime={5}&startFrom={6}&pageSize={7}";

        invalidParameterHandler.validateUserId(userId, methodName);

        FindRequestBody requestBody = new FindRequestBody();

        requestBody.setMetadataElementTypeName(metadataElementTypeName);
        requestBody.setMetadataElementSubtypeName(metadataElementSubtypeName);
        requestBody.setSearchProperties(searchProperties);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setMatchClassifications(matchClassifications);
        requestBody.setSequencingProperty(sequencingProperty);
        requestBody.setSequencingOrder(sequencingOrder);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  this.getEffectiveTimeAsLong(effectiveTime),
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId                 caller's userId
     * @param relationshipTypeName   relationship's type.  Null means all types
     *                               (but may be slow so not recommended).
     * @param searchProperties       Optional list of relationship property conditions to match.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           userId,
                                                                                  String           relationshipTypeName,
                                                                                  SearchProperties searchProperties,
                                                                                  String           sequencingProperty,
                                                                                  SequencingOrder  sequencingOrder,
                                                                                  boolean          forLineage,
                                                                                  boolean          forDuplicateProcessing,
                                                                                  Date             effectiveTime,
                                                                                  int              startFrom,
                                                                                  int              pageSize) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String methodName = "findRelationshipsBetweenMetadataElements";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/by-search-specification?forLineage={3}&forDuplicateProcessing={4}&effectiveTime={5}&startFrom={6}&pageSize={7}";

        invalidParameterHandler.validateUserId(userId, methodName);

        FindRequestBody requestBody = new FindRequestBody();

        requestBody.setMetadataElementTypeName(relationshipTypeName);
        requestBody.setSearchProperties(searchProperties);
        requestBody.setSequencingProperty(sequencingProperty);
        requestBody.setSequencingOrder(sequencingOrder);

        RelatedMetadataElementsListResponse restResult = restClient.callRelatedMetadataElementsListPostRESTCall(methodName,
                                                                                                                urlTemplate,
                                                                                                                requestBody,
                                                                                                                serverName,
                                                                                                                serviceURLMarker,
                                                                                                                userId,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                this.getEffectiveTimeAsLong(effectiveTime),
                                                                                                                Integer.toString(startFrom),
                                                                                                                Integer.toString(pageSize));

        return restResult.getElementList();
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId                  caller's userId
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus           initial status of the metadata element
     * @param effectiveFrom           the date when this element is active - null for active on creation
     * @param effectiveTo             the date when this element becomes inactive - null for active until deleted
     * @param properties              properties of the new metadata element
     * @param templateGUID            the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                connection etc)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String            userId,
                                               String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               String            templateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName               = "createMetadataElementInStore";
        final String elementTypeParameterName = "metadataElementTypeName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(metadataElementTypeName, elementTypeParameterName, methodName);

        NewMetadataElementRequestBody requestBody = new NewMetadataElementRequestBody();

        requestBody.setTypeName(metadataElementTypeName);
        requestBody.setInitialStatus(initialStatus);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setProperties(properties);
        requestBody.setTemplateGUID(templateGUID);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties             new properties for the metadata element
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementInStore(String            userId,
                                             String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "updateMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setReplaceProperties(replaceProperties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param newElementStatus       new status value - or null to leave as is
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementStatusInStore(String        userId,
                                                   String        metadataElementGUID,
                                                   boolean       forLineage,
                                                   boolean       forDuplicateProcessing,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName        = "updateMetadataElementStatusInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/update-status";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateStatusRequestBody requestBody = new UpdateStatusRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setNewStatus(newElementStatus);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementEffectivityInStore(String        userId,
                                                        String        metadataElementGUID,
                                                        boolean       forLineage,
                                                        boolean       forDuplicateProcessing,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo,
                                                        Date          effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName        = "updateMetadataElementEffectivityInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void deleteMetadataElementInStore(String  userId,
                                             String  metadataElementGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName        = "deleteMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateRequestBody requestBody = new UpdateRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this classification is active - null for active now
     * @param effectiveTo            the date when this classification becomes inactive - null for active until deleted
     * @param properties             properties to store in the new classification.  These must conform to the valid properties associated with the
     *                               classification name
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElementInStore(String            userId,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "classifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setProperties(properties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties             new properties for the classification
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void reclassifyMetadataElementInStore(String            userId,
                                                 String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 boolean           forLineage,
                                                 boolean           forDuplicateProcessing,
                                                 ElementProperties properties,
                                                 Date              effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "reclassifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setReplaceProperties(replaceProperties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateClassificationEffectivityInStore(String  userId,
                                                       String  metadataElementGUID,
                                                       String  classificationName,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                  = "updateClassificationEffectivityInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to remove
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void unclassifyMetadataElementInStore(String  userId,
                                                 String  metadataElementGUID,
                                                 String  classificationName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                  = "unclassifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdateRequestBody requestBody = new UpdateRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId                 caller's userId
     * @param relationshipTypeName   name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                               related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID   unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID   unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param properties             the properties of the relationship
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createRelatedElementsInStore(String            userId,
                                               String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName               = "createRelatedElementsInStore";
        final String elementTypeParameterName = "relationshipTypeName";
        final String end1ParameterName        = "metadataElement1GUID";
        final String end2ParameterName        = "metadataElement2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, elementTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody();

        requestBody.setTypeName(relationshipTypeName);
        requestBody.setMetadataElement1GUID(metadataElement1GUID);
        requestBody.setMetadataElement2GUID(metadataElement2GUID);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsInStore(String            userId,
                                             String            relationshipGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateRelatedElementsInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setReplaceProperties(replaceProperties);
        requestBody.setProperties(properties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void updateRelatedElementsEffectivityInStore(String  userId,
                                                         String  relationshipGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveFrom,
                                                         Date    effectiveTo,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateRelatedElementsEffectivityInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelatedElementsInStore(String  userId,
                                             String  relationshipGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "deleteRelatedElementsInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdateRequestBody requestBody = new UpdateRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }



    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  String createIncidentReport(String                        userId,
                                        String                        qualifiedName,
                                        int                           domainIdentifier,
                                        String                        background,
                                        List<IncidentImpactedElement> impactedResources,
                                        List<IncidentDependency>      previousIncidents,
                                        Map<String, Integer>          incidentClassifiers,
                                        Map<String, String>           additionalProperties,
                                        String                        originatorGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createIncidentReport";
        final String qualifiedNameParameterName = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/incident-reports";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        IncidentReportRequestBody requestBody = new IncidentReportRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDomainIdentifier(domainIdentifier);
        requestBody.setBackground(background);
        requestBody.setImpactedResources(impactedResources);
        requestBody.setPreviousIncidents(previousIncidents);
        requestBody.setIncidentClassifiers(incidentClassifiers);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setOriginatorGUID(originatorGUID);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Create a To-Do request for someone to work on.
     *
     * @param userId caller's userId
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignTo qualified name of the PersonRole element for the recipient
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String openToDo(String userId,
                           String toDoQualifiedName,
                           String title,
                           String instructions,
                           int    priority,
                           Date   dueDate,
                           String assignTo) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        final String methodName = "openToDo";

        final String todoTypeName             = "ToDo";
        final String personRoleTypeName       = "PersonRole";
        final String actionAssignmentTypeName = "ActionAssignment";

        final String qualifiedNamePropertyName = "qualifiedName";
        final String titlePropertyName         = "name";
        final String instructionsPropertyName  = "description";
        final String priorityPropertyName      = "priority";
        final String dueDatePropertyName       = "dueTime";
        final String statusPropertyName        = "status";
        final String statusPropertyTypeName    = "ToDoStatus";
        final String openEnumPropertyValue     = "Open";

        final String toDoQualifiedNameParameterName = "toDoQualifiedName";
        final String assignToParameterName          = "assignTo";

        propertyHelper.validateMandatoryName(toDoQualifiedName, toDoQualifiedNameParameterName, methodName);
        propertyHelper.validateMandatoryName(assignTo, assignToParameterName, methodName);

        SearchProperties           searchProperties           = new SearchProperties();
        List<PropertyCondition>    conditions                 = new ArrayList<>();
        PropertyCondition          condition                  = new PropertyCondition();
        PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();

        primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        primitiveTypePropertyValue.setPrimitiveValue(assignTo);
        primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(qualifiedNamePropertyName);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitiveTypePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        /*
         * Validate that there is a person role to assign the "to do" to
         */
        List<OpenMetadataElement> personRoleMatches = this.findMetadataElements(userId,
                                                                                personRoleTypeName,
                                                                                             null,
                                                                                             searchProperties,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             false,
                                                                                             false,
                                                                                             new Date(),
                                                                                             0,
                                                                                             0);

        if ((personRoleMatches == null) || personRoleMatches.isEmpty())
        {
            throw new InvalidParameterException(GAFErrorCode.UNKNOWN_ELEMENT.getMessageDefinition(toDoQualifiedName,
                                                                                                  toDoQualifiedNameParameterName,
                                                                                                  methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                toDoQualifiedNameParameterName);
        }
        else if (personRoleMatches.size() > 1)
        {
            List<String> matchingGUIDs = new ArrayList<>();

            for (OpenMetadataElement element : personRoleMatches)
            {
                if (element != null)
                {
                    matchingGUIDs.add(element.getElementGUID());
                }
            }

            throw new InvalidParameterException(GAFErrorCode.DUPLICATE_ELEMENT.getMessageDefinition(toDoQualifiedName,
                                                                                                    toDoQualifiedNameParameterName,
                                                                                                    methodName,
                                                                                                    matchingGUIDs.toString()),
                                                this.getClass().getName(),
                                                methodName,
                                                toDoQualifiedNameParameterName);
        }

        OpenMetadataElement personRoleElement = personRoleMatches.get(0);
        String personRoleGUID = null;

        if ((personRoleElement != null) && (personRoleElement.getElementGUID() != null))
        {
            personRoleGUID = personRoleElement.getElementGUID();
        }

        /*
         * Create the to do entity
         */
        ElementProperties properties = propertyHelper.addStringProperty(null, qualifiedNamePropertyName, toDoQualifiedName);

        if (title != null)
        {
            properties = propertyHelper.addStringProperty(properties, titlePropertyName, title);
        }

        if (instructions != null)
        {
            properties = propertyHelper.addStringProperty(properties, instructionsPropertyName, instructionsPropertyName);
        }

        if (dueDate != null)
        {
            properties = propertyHelper.addDateProperty(properties, dueDatePropertyName, dueDate);
        }

        properties = propertyHelper.addIntProperty(properties, priorityPropertyName, priority);
        properties = propertyHelper.addEnumProperty(properties, statusPropertyName, statusPropertyTypeName, openEnumPropertyValue);

        String todoGUID = this.createMetadataElementInStore(userId, todoTypeName, ElementStatus.ACTIVE, null, null, properties, null);

        /*
         * Link the "to do" and the person role
         */
        this.createRelatedElementsInStore(userId, actionAssignmentTypeName, personRoleGUID, todoGUID, false, false,null, null, null, new Date());

        return todoGUID;
    }


    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param userId caller's userId
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param setKnownDuplicate boolean flag indicating whether the KnownDuplicate classification should be set on the linked entities.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void linkElementsAsPeerDuplicates(String  userId,
                                             String  metadataElement1GUID,
                                             String  metadataElement2GUID,
                                             int     statusIdentifier,
                                             String  steward,
                                             String  stewardTypeName,
                                             String  stewardPropertyName,
                                             String  source,
                                             String  notes,
                                             boolean setKnownDuplicate) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "linkElementsAsPeerDuplicates";
        final String end1ParameterName = "metadataElement1GUID";
        final String end2ParameterName = "metadataElement2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/link-as-peer-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        PeerDuplicatesRequestBody requestBody = new PeerDuplicatesRequestBody();

        requestBody.setMetadataElement1GUID(metadataElement1GUID);
        requestBody.setMetadataElement2GUID(metadataElement2GUID);
        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);
        requestBody.setSetKnownDuplicate(setKnownDuplicate);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId);
    }


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     *
     * @param userId caller's userId
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void linkConsolidatedDuplicate(String       userId,
                                          String       consolidatedElementGUID,
                                          int          statusIdentifier,
                                          String       steward,
                                          String       stewardTypeName,
                                          String       stewardPropertyName,
                                          String       source,
                                          String       notes,
                                          List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "linkConsolidatedDuplicate";
        final String consolidatedElementGUIDParameterName = "consolidatedElementGUID";
        final String sourceElementGUIDsParameterName = "sourceElementGUIDs";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/related-elements/link-as-consolidated-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedElementGUID, consolidatedElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(sourceElementGUIDs, sourceElementGUIDsParameterName, methodName);

        ConsolidatedDuplicatesRequestBody requestBody = new ConsolidatedDuplicatesRequestBody();

        requestBody.setConsolidatedElementGUID(consolidatedElementGUID);
        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);
        requestBody.setSourceElementGUIDs(sourceElementGUIDs);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId);
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setTranslation(String            userId,
                               String            elementGUID,
                               TranslationDetail translationDetail) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "setTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String translationDetailParameterName = "translationDetail";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/multi-language/set-translation/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(translationDetail, translationDetailParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        translationDetail,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        elementGUID);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @throws InvalidParameterException  the language is null or not known or not unique (add locale)
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void clearTranslation(String userId,
                                 String elementGUID,
                                 String language,
                                 String locale) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "clearTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String languageParameterName = "language";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/multi-language/clear-translation/{3}?language={4}&locale={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(language, languageParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        elementGUID,
                                        language,
                                        locale);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public TranslationDetail getTranslation(String userId,
                                            String elementGUID,
                                            String language,
                                            String locale) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String languageParameterName = "language";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/multi-language/get-translation/{3}?language={4}&locale={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(language, languageParameterName, methodName);

        TranslationDetailResponse response = restClient.callTranslationDetailGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         serviceURLMarker,
                                                                                         userId,
                                                                                         elementGUID,
                                                                                         language,
                                                                                         locale);

        return response.getElement();
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<TranslationDetail> getTranslations(String userId,
                                                   String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "getTranslations";
        final String elementGUIDParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/multi-language/get-translations/{3}?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        TranslationListResponse response = restClient.callTranslationListGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     new NullRequestBody(),
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     userId,
                                                                                     elementGUID,
                                                                                     startFrom,
                                                                                     pageSize);

        return response.getElementList();
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setUpValidMetadataValue(String             userId,
                                        String             typeName,
                                        String             propertyName,
                                        ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setUpValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/setup-valid-metadata-value/{3}?typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        typeName);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void clearValidMetadataValue(String userId,
                                        String typeName,
                                        String propertyName,
                                        String preferredValue) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "clearValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/clear-valid-metadata-value/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        preferredValue,
                                        typeName);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public boolean validateMetadataValue(String userId,
                                         String typeName,
                                         String propertyName,
                                         String actualValue) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "validateMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/validate/{3}?actualValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     new NullRequestBody(),
                                                                     serverName,
                                                                     serviceURLMarker,
                                                                     userId,
                                                                     propertyName,
                                                                     actualValue,
                                                                     typeName);

        return response.getFlag();
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public ValidMetadataValue getValidMetadataValue(String userId,
                                                    String typeName,
                                                    String propertyName,
                                                    String preferredValue) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/get-valid-metadata-value/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           new NullRequestBody(),
                                                                                           serverName,
                                                                                           serviceURLMarker,
                                                                                           userId,
                                                                                           propertyName,
                                                                                           preferredValue,
                                                                                           typeName);

        return response.getElement();
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<ValidMetadataValue> getValidMetadataValues(String userId,
                                                           String typeName,
                                                           String propertyName,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "getValidMetadataValues";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/common-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/get-valid-metadata-values/{3}?typeName={4}?startFrom={5}&pageSize={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueListResponse response = restClient.callValidMetadataValueListGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   new NullRequestBody(),
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   userId,
                                                                                                   propertyName,
                                                                                                   typeName,
                                                                                                   startFrom,
                                                                                                   pageSize);

        return response.getElementList();
    }
}
