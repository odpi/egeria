/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * Provides the API for a security connector that implements security rules based on the elements being accessed.
 */
public interface OpenMetadataElementSecurity
{

    /**
     * Tests for whether a specific user should have the right to create an element.
     *
     * @param userId identifier of user
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void validateUserForElementCreate(String               userId,
                                      String               entityTypeGUID,
                                      String               entityTypeName,
                                      InstanceProperties   newProperties,
                                      List<Classification> classifications,
                                      InstanceStatus       instanceStatus,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               methodName) throws UserNotAuthorizedException,
                                                                              InvalidParameterException,
                                                                              PropertyServerException;


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param requestedEntity entity requested by the caller
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void validateUserForElementRead(String               userId,
                                    EntityDetail         requestedEntity,
                                    OMRSRepositoryHelper repositoryHelper,
                                    String               serviceName,
                                    String               methodName) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException;


    /**
     * Tests for whether a specific user should have the right to update the properties of an element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param newEntityProperties new properties
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementDetailUpdate(String               userId,
                                            EntityDetail         originalEntity,
                                            InstanceProperties   newEntityProperties,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update the properties of an element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param newStatus new value for status
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementStatusUpdate(String               userId,
                                            EntityDetail         originalEntity,
                                            InstanceStatus       newStatus,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to link unanchored elements to this element
     *
     * @param userId identifier of user
     * @param startingEntity end 1 details
     * @param attachingEntity end 1 details
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementAttach(String               userId,
                                      EntityDetail         startingEntity,
                                      EntityDetail         attachingEntity,
                                      String               relationshipName,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to link unanchored elements to this element
     *
     * @param userId identifier of user
     * @param startingEntity end 1 details
     * @param detachingEntity end 2 details
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementDetach(String               userId,
                                      EntityDetail         startingEntity,
                                      EntityDetail         detachingEntity,
                                      String               relationshipName,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param feedbackEntity feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementAddFeedback(String               userId,
                                           EntityDetail         originalEntity,
                                           EntityDetail         feedbackEntity,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param feedbackEntity feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementDeleteFeedback(String               userId,
                                              EntityDetail         originalEntity,
                                              EntityDetail         feedbackEntity,
                                              OMRSRepositoryHelper repositoryHelper,
                                              String               serviceName,
                                              String               methodName) throws UserNotAuthorizedException;



    /**
     * Tests for whether a specific user should have the right to add or update a classification on this element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param classificationName name of the classification
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementClassify(String               userId,
                                        EntityDetail         originalEntity,
                                        String               classificationName,
                                        OMRSRepositoryHelper repositoryHelper,
                                        String               serviceName,
                                        String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to remove a classification from this element
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param classificationName name of the classification
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementDeclassify(String               userId,
                                          EntityDetail         originalEntity,
                                          String               classificationName,
                                          OMRSRepositoryHelper repositoryHelper,
                                          String               serviceName,
                                          String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param entity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForElementDelete(String               userId,
                                      EntityDetail         entity,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param newMemberEntity feedback element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorMemberAdd(String               userId,
                                        EntityDetail         anchorEntity,
                                        EntityDetail         newMemberEntity,
                                        String               relationshipName,
                                        OMRSRepositoryHelper repositoryHelper,
                                        String               serviceName,
                                        String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param anchorEntity entity for the anchor (if extracted - may be null)
     * @param requestedEntity entity requested by the caller
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    void validateUserForAnchorMemberRead(String               userId,
                                         EntityDetail         anchorEntity,
                                         EntityDetail         requestedEntity,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an anchor such as glossary terms and categories attached to an element.  These updates could be to their properties,
     * classifications and relationships.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorMemberUpdate(String               userId,
                                           EntityDetail         anchorEntity,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update the instance status of an element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param originalEntity entity being updated
     * @param newStatus new value for status
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorMemberStatusUpdate(String               userId,
                                                 EntityDetail         anchorEntity,
                                                 EntityDetail         originalEntity,
                                                 InstanceStatus       newStatus,
                                                 OMRSRepositoryHelper repositoryHelper,
                                                 String               serviceName,
                                                 String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to link unanchored  elements
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param attachingEntity new element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorAttach(String               userId,
                                     EntityDetail         anchorEntity,
                                     EntityDetail         attachingEntity,
                                     String               relationshipName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to link unanchored  elements
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param detachingEntity obsolete element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorDetach(String               userId,
                                     EntityDetail         anchorEntity,
                                     EntityDetail         detachingEntity,
                                     String               relationshipName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the anchor or member element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param feedbackEntity feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorAddFeedback(String               userId,
                                          EntityDetail         anchorEntity,
                                          EntityDetail         feedbackEntity,
                                          OMRSRepositoryHelper repositoryHelper,
                                          String               serviceName,
                                          String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags and likes, to the anchor or member element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param feedbackEntity feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorDeleteFeedback(String               userId,
                                             EntityDetail         anchorEntity,
                                             EntityDetail         feedbackEntity,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               methodName) throws UserNotAuthorizedException;



    /**
     * Tests for whether a specific user should have the right to add or update a classification on this anchor or member element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param classificationName name of the classification
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorClassify(String               userId,
                                       EntityDetail         anchorEntity,
                                       String               classificationName,
                                       OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName,
                                       String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to remove a classification from this anchor or member element
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param classificationName name of the classification
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorDeclassify(String               userId,
                                         EntityDetail         anchorEntity,
                                         String               classificationName,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param obsoleteEntity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    void validateUserForAnchorMemberDelete(String               userId,
                                           EntityDetail         anchorEntity,
                                           EntityDetail         obsoleteEntity,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException;




    /**
     * Use the security connector to make a choice on which connection to supply to the requesting user.
     *
     * @param userId calling userId
     * @param assetEntity associated asset - may be null
     * @param connectionEntities list of retrieved connections
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return single connection entity, or null
     * @throws UserNotAuthorizedException the user is not able to use any of the connections
     */
    public EntityDetail selectConnection(String               userId,
                                         EntityDetail         assetEntity,
                                         List<EntityDetail>   connectionEntities,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException;
}
