/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionOrder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

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
     * Create a new generic collection.
     *
     * @param userId                 userId of user making request.
     * @param qualifiedName          unique name of the collection.
     * @param displayName            short displayable name for the collection.
     * @param description            description of the collection.
     * @param collectionUse          description of how the collection is to be used.
     * @param additionalProperties   additional arbitrary properties.
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createCollection(String              userId,
                            String              qualifiedName,
                            String              displayName,
                            String              description,
                            String              collectionUse,
                            Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


    /**
     * Create a collection that acts like a folder with an order.
     *
     * @param userId                 userId of user making request.
     * @param qualifiedName          unique name of the collection.
     * @param displayName            short displayable name for the collection.
     * @param description            description of the collection.
     * @param collectionUse          description of how the collection will be used.
     * @param collectionOrder        description of how the members in the collection should be organized.
     * @param additionalProperties   additional arbitrary properties.
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createFolder(String              userId,
                        String              qualifiedName,
                        String              displayName,
                        String              description,
                        String              collectionUse,
                        CollectionOrder     collectionOrder,
                        Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;

    /**
     * Create a collection that acts like a set (this does not allow duplicate entries).
     *
     * @param userId                 userId of user making request.
     * @param qualifiedName          unique name of the collection.
     * @param displayName            short displayable name for the collection.
     * @param description            description of the collection.
     * @param collectionUse          description of how the collection will be used.
     * @param additionalProperties   additional arbitrary properties.
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createSet(String              userId,
                     String              qualifiedName,
                     String              displayName,
                     String              description,
                     String              collectionUse,
                     Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException;


    /**
     * Connect an existing collection to an anchor point.
     *
     * @param userId          userId of user making request
     * @param collectionGUID  unique identifier of the collection
     * @param parentGUID      unique identifier of referenceable object that the collection should be attached to
     * @param makeAnchor      like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void attachCollection(String  userId,
                          String  collectionGUID,
                          String  parentGUID,
                          boolean makeAnchor) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param userId          userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param parentGUID      unique identifier of referenceable object that the collection should be attached to.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void detachCollection(String userId,
                          String collectionGUID,
                          String parentGUID) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException;


    /**
     * Delete a collection.  It is detected from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId   userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteCollection(String userId,
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
     * Add an element to a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param elementGUID  unique identifier of the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToCollection(String userId,
                          String collectionGUID,
                          String elementGUID) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;


    /**
     * Remove an element from a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param elementGUID  unique identifier of the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromCollection(String userId,
                               String collectionGUID,
                               String elementGUID) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;
}
