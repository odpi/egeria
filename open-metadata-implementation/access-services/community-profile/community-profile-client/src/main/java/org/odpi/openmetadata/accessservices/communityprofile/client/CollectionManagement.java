/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.CollectionManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.CollectionMembersResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.CollectionsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.CollectionMemberResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.CollectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMember;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;

import java.util.List;

/**
 * CollectionManagerClient supports the APIs to maintain collections and their related objects.
 * It issues REST API calls to the Open Metadata Server running Collection Profile OMAS that have a URL that begins:
 * <i>serverPlatformURLRoot</i>/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}/collections.
 */
public class CollectionManagement extends CommunityProfileBaseClient implements CollectionManagementInterface
{
    private static final String collectionURLTemplatePrefix = baseURLTemplatePrefix + "/collections";


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
    public CollectionManagement(String   serverName,
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
    public CollectionManagement(String serverName,
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
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollectionManagement(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password,
                                int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
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
    public CollectionManagement(String   serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollectionManagement(String                     serverName,
                                String                     serverPlatformURLRoot,
                                CommunityProfileRESTClient restClient,
                                int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* =====================================================================================================================
     * A collection is a group of related objects
     */

    /**
     * Create a new metadata element to represent a collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionProperties properties about the collection to store.  The qualifiedName property must be supplied and must be unique.
     *
     * @return unique identifier of the new collection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createCollection(String               userId,
                                   String               externalSourceGUID,
                                   String               externalSourceName,
                                   CollectionProperties collectionProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName              = "createCollection";
        final String propertiesParameterName = "collectionProperties";
        final String urlTemplate             = serverPlatformURLRoot + collectionURLTemplatePrefix;

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, collectionProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a collection that acts like a folder with an order.
     *
     * @param userId             userId of user making request.
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param properties         description of the collection.
     * @return unique identifier of the newly created Collection
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createFolderCollection(String                     userId,
                                         String                     externalSourceGUID,
                                         String                     externalSourceName,
                                         CollectionFolderProperties properties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName                  = "createFolderCollection";
        final String propertiesParameterName     = "properties";
        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/folders";

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, properties, propertiesParameterName, urlTemplate, methodName);

    }


    /**
     * Update the metadata element representing a collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param collectionProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateCollection(String               userId,
                                 String               externalSourceGUID,
                                 String               externalSourceName,
                                 String               collectionGUID,
                                 boolean              isMergeUpdate,
                                 CollectionProperties collectionProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "updateCollection";
        final String elementGUIDParameterName    = "collectionGUID";
        final String propertiesParameterName     = "collectionProperties";
        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        super.updateReferenceable(userId, externalSourceGUID, externalSourceName, collectionGUID, elementGUIDParameterName, isMergeUpdate, collectionProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a membership relationship between a collection and a person role to show that anyone appointed to the role is a member of the collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID unique identifier of the collection
     * @param membershipProperties describes the permissions that the role has in the collection
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param elementGUID unique identifier of the person role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateCollectionMembership(String                         userId,
                                           String                         externalSourceGUID,
                                           String                         externalSourceName,
                                           String                         collectionGUID,
                                           CollectionMembershipProperties membershipProperties,
                                           boolean                        isMergeUpdate,
                                           String                         elementGUID) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "updateCollectionMembership";
        final String collectionGUIDParameterName = "collectionGUID";
        final String memberGUIDParameterName     = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}/collection-roles/{3}?isMergeUpdate={4}";

        super.setupRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                collectionGUID,
                                collectionGUIDParameterName,
                                isMergeUpdate,
                                membershipProperties,
                                elementGUID,
                                memberGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove a membership relationship between a collection and a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID unique identifier of the collection
     * @param elementGUID unique identifier of the person role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeFromCollection(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String collectionGUID,
                                     String elementGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                  = "removeFromCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String memberGUIDParameterName     = "elementGUID";
        final String urlTemplate                 = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}/collection-roles/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                collectionGUID,
                                collectionGUIDParameterName,
                                elementGUID,
                                memberGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove the metadata element representing a collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeCollection(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String collectionGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName               = "removeCollection";
        final String elementGUIDParameterName = "collectionGUID";
        final String urlTemplate              = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}/delete";

        super.removeReferenceable(userId, externalSourceGUID, externalSourceName, collectionGUID, elementGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
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
    public List<CollectionElement> findCollections(String userId,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                = "findCollections";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        CollectionsResponse restResult = restClient.callCollectionListPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   requestBody,
                                                                                   serverName,
                                                                                   userId,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
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
    public List<CollectionElement> getCollectionsByName(String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getCollectionByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        CollectionsResponse restResult = restClient.callCollectionListPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   requestBody,
                                                                                   serverName,
                                                                                   userId,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of all collections defined in open metadata.
     *
     * @param userId calling user
     * @param parentGUID unique identifier of referenceable object (typically a personal profile, project or
     *                   community) that the collections hang off of.
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
    public List<CollectionElement> getCollections(String userId,
                                                  String parentGUID,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "getCollections";
        final String guidPropertyName  = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, guidPropertyName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "?startFrom={2}&pageSize={3}";

        CollectionsResponse restResult = restClient.callCollectionListGetRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize);

        return restResult.getElements();
    }



    /**
     * Return information about the elements linked to a collection.
     *
     * @param userId calling user
     * @param collectionGUID unique identifier for the collection
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<CollectionMember> getCollectionMembers(String userId,
                                                       String collectionGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName        = "getCollectionMembers";
        final String guidPropertyName  = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}/membership?startFrom={3}&pageSize={4}";

        CollectionMembersResponse restResult = restClient.callCollectionMemberListGetRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              serverName,
                                                                                              userId,
                                                                                              collectionGUID,
                                                                                              Integer.toString(startFrom),
                                                                                              Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Return details of the membership between a collection and a specific member of the collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param memberGUID  unique identifier of the element who is a member of the collection.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public CollectionMember getCollectionMember(String userId,
                                                String collectionGUID,
                                                String memberGUID) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName        = "getCollectionMembers";
        final String guidPropertyName  = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}/members/{3}?startFrom={3}&pageSize={4}";

        CollectionMemberResponse restResult = restClient.callCollectionMemberGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         collectionGUID,
                                                                                         memberGUID);

        return restResult.getElement();
    }


    /**
     * Return information about the elements linked to a collection.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the collection
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<CollectionElement> getElementsCollections(String userId,
                                                          String elementGUID,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName        = "getElementsCollections";
        final String guidPropertyName  = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/elements/{2}/collections?startFrom={3}&pageSize={4}";

        CollectionsResponse restResult = restClient.callCollectionListGetRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  serverName,
                                                                                  userId,
                                                                                  elementGUID,
                                                                                  Integer.toString(startFrom),
                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param collectionGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public CollectionElement getCollection(String userId,
                                           String collectionGUID) throws InvalidParameterException, 
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getCollection";
        final String guidParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + collectionURLTemplatePrefix + "/{2}";

        CollectionResponse restResult = restClient.callCollectionGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             collectionGUID);

        return restResult.getElement();
    }
}
