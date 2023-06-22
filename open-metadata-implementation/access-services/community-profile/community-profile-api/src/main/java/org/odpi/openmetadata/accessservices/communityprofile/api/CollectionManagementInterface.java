/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionFolderProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionMembershipProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The CollectionManagementInterface adds methods for managing collections that can be attached to communities
 * and personal profiles.
 */
public interface CollectionManagementInterface
{
    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId     userId of user making request
     * @param parentGUID unique identifier of referenceable object (typically a personal profile, project or
     *                   community) that the collections hang off of.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getCollections(String userId,
                                           String parentGUID,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


    /**
     * Return the properties of a specific collection.
     *
     * @param userId            userId of user making request.
     * @param collectionGUID    unique identifier of the required connection.
     *
     * @return collection properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    CollectionElement getCollection(String userId,
                                    String collectionGUID) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Retrieve the list of collection metadata elements that contain the search string.
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
    List<CollectionElement> findCollections(String userId,
                                            String searchString,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Retrieve the list of collection metadata elements with a matching qualified or display name.
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
    List<CollectionElement> getCollectionsByName(String userId,
                                                 String name,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;



    /**
     * Create a new generic collection.
     *
     * @param userId     userId of user making request.
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param properties description of the collection.
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createCollection(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            CollectionProperties properties) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;


    /**
     * Create a collection that acts like a folder with an order.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param properties             description of the collection.
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createFolderCollection(String                     userId,
                                  String                     externalSourceGUID,
                                  String                     externalSourceName,
                                  CollectionFolderProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException;


    /**
     * Update the metadata element representing a collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateCollection(String               userId,
                          String               externalSourceGUID,
                          String               externalSourceName,
                          String               collectionGUID,
                          boolean              isMergeUpdate,
                          CollectionProperties properties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Delete a collection.  It is deleted from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId   userId of user making request.
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID  unique identifier of the collection.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void removeCollection(String userId,
                          String externalSourceGUID,
                          String externalSourceName,
                          String collectionGUID) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException;


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionMember> getCollectionMembers(String userId,
                                                String collectionGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException;



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
    CollectionMember getCollectionMember(String userId,
                                         String collectionGUID,
                                         String memberGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


    /**
     * Return a list of collections that the supplied element is a member of.
     *
     * @param userId     userId of user making request.
     * @param elementGUID  unique identifier of the collection.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getElementsCollections(String userId,
                                                   String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Add an element to a collection (or update its membership properties).
     *
     * @param userId     userId of user making request.
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID  unique identifier of the collection.
     * @param properties         new properties
     * @param isMergeUpdate      should the properties be merged with the existing properties or replace them?
     * @param elementGUID  unique identifier of the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  updateCollectionMembership(String                         userId,
                                     String                         externalSourceGUID,
                                     String                         externalSourceName,
                                     String                         collectionGUID,
                                     CollectionMembershipProperties properties,
                                     boolean                        isMergeUpdate,
                                     String                         elementGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException;


    /**
     * Remove an element from a collection.
     *
     * @param userId     userId of user making request.
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID  unique identifier of the collection.
     * @param elementGUID  unique identifier of the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromCollection(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String collectionGUID,
                               String elementGUID) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;
}
