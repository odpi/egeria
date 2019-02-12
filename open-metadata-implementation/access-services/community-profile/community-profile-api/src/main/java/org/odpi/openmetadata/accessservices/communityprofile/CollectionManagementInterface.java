/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;


import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Collection;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionOrder;

import java.util.List;
import java.util.Map;

/**
 * The Community Profile Open Metadata Access Service (OMAS) is used by tools and administrators to
 * maintain information associated with individuals and communities.
 * The CollectionManagementInterface adds methods for managing collections that can be attached to communities
 * and personal profiles
 */
public interface CollectionManagementInterface
{
    /**
     * Returns the list of collections that are linked off of the supplied anchor point.
     *
     * @param userId     userId of user making request
     * @param anchorGUID unique identifier of referenceable object (typically a personal profile, project or
     *                   community) that they collections hang off of.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<Collection> getCollections(String    userId,
                                    String    anchorGUID,
                                    int       startFrom,
                                    int       pageSize) throws InvalidParameterException,
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
    Collection     getCollection(String    userId,
                                 String    collectionGUID) throws InvalidParameterException,
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
     * @return the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Collection     createCollection(String              userId,
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
     * @return the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Collection     createFolder(String              userId,
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
     * @return the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Collection     createSet(String              userId,
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
     * @param userId          userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param anchorGUID      unique identifier of referenceable object that the collection should be attached to.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void           attachCollection(String    userId,
                                    String    collectionGUID,
                                    String    anchorGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;


    /**
     * Detach an existing collection from an anchor point.
     *
     * @param userId          userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param anchorGUID      unique identifier of referenceable object that the collection should be attached to.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void           detachCollection(String    userId,
                                    String    collectionGUID,
                                    String    anchorGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;


    /**
     * Delete a collection (the members are not affected).
     *
     * @param userId   userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeCollection(String    userId,
                            String    collectionGUID) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;



}
