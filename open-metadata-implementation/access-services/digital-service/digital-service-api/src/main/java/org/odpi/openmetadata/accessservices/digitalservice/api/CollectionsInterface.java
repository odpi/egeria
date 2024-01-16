/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.api;


import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.CollectionMember;
import org.odpi.openmetadata.accessservices.digitalservice.properties.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The CollectionsInterface adds methods for managing collections.  Collections are managed lists of elements. They can be used to create a folder
 * type hierarchy for, say, assets and digital products.  They are used to represent digital products and lists of favourites.
 */
public interface CollectionsInterface
{
    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId     userId of user making request
     * @param parentGUID unique identifier of referenceable object (typically a personal profile, project or
     *                   community) that the collections hang off of
     * @param collectionType filter response by collection type - if null, any value will do
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getLinkedCollections(String userId,
                                                 String parentGUID,
                                                 String collectionType,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Returns the list of collections with a particular classification.
     *
     * @param userId     userId of user making request
     * @param classificationName name of the classification - if null, all collections are returned
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getClassifiedCollections(String userId,
                                                     String classificationName,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


    /**
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param userId     userId of user making request
     * @param searchString string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> findCollections(String userId,
                                            String searchString,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;


    /**
     * Returns the list of collections with a particular name.
     *
     * @param userId     userId of user making request
     * @param name       name of the collections to return - match is full text match in qualifiedName or name
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getCollectionsByName(String userId,
                                                 String name,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param userId     userId of user making request
     * @param collectionType the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getCollectionsByType(String userId,
                                                 String collectionType,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;

    /**
     * Return the properties of a specific collection.
     *
     * @param userId            userId of user making request
     * @param collectionGUID    unique identifier of the required collection
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
     * @param optionalClassification classification of the collections - typically RootCollection, Set or Folder
     * @param properties             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createCollection(String               userId,
                            String               optionalClassification,
                            CollectionProperties properties) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;



    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
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
    String createCollectionFromTemplate(String             userId,
                                        String             templateGUID,
                                        TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;



    /**
     * Create a new collection that represents a digital product.
     *
     * @param userId                 userId of user making request.
     * @param digitalServiceGUID     unique identifier of owning digital service (optional)
     * @param collectionProperties   properties for the collection.
     * @param digitalProductProperties properties for the attached DigitalProduct classification
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createDigitalProduct(String                   userId,
                                String                   digitalServiceGUID,
                                CollectionProperties     collectionProperties,
                                DigitalProductProperties digitalProductProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException;


    /**
     * Update the properties of a collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID         unique identifier of the collection (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the collection.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateCollection(String               userId,
                            String               collectionGUID,
                            boolean              replaceAllProperties,
                            CollectionProperties properties) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;


    /**
     * Update the properties of the DigitalProduct classification attached to a collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID         unique identifier of the collection (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the DigitalProduct classification.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateDigitalProduct(String                   userId,
                                String                   collectionGUID,
                                boolean                  replaceAllProperties,
                                DigitalProductProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;


    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param userId          userId of user making request
     * @param collectionGUID  unique identifier of the collection
     * @param parentGUID      unique identifier of referenceable object that the collection should be attached to
     * @param collectionUse   description of how the collection will be used.
     * @param makeAnchor      like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void attachCollection(String                 userId,
                          String                 collectionGUID,
                          String                 parentGUID,
                          ResourceListProperties collectionUse,
                          boolean                makeAnchor) throws InvalidParameterException,
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
     * @param membershipProperties properties describing the membership characteristics.
     * @param elementGUID  unique identifier of the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToCollection(String                         userId,
                          String                         collectionGUID,
                          CollectionMembershipProperties membershipProperties,
                          String                         elementGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


    /**
     * Update an element's membership to a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param membershipProperties properties describing the membership characteristics.
     * @param elementGUID  unique identifier of the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  updateCollectionMembership(String                         userId,
                                     String                         collectionGUID,
                                     boolean                        replaceAllProperties,
                                     CollectionMembershipProperties membershipProperties,
                                     String                         elementGUID) throws InvalidParameterException,
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
