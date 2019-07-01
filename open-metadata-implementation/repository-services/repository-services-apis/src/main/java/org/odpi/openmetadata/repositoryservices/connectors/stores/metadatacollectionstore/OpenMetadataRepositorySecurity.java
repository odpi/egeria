/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

/**
 * OpenMetadataRepositorySecurity defines security checks for accessing and maintaining open metadata types
 * and instances in the local repository.
 *
 * An instance is an entity or a relationship.  There is also a special method for changing classifications
 * added to an entity.
 */
public interface OpenMetadataRepositorySecurity
{
    /**
     * Tests for whether a specific user should have the right to create a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    void  validateUserForTypeCreate(String  userId,
                                    String  metadataCollectionName,
                                    TypeDef typeDef) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    void  validateUserForTypeRead(String  userId,
                                  String  metadataCollectionName,
                                  TypeDef typeDef) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    void  validateUserForTypeUpdate(String userId,
                                    String metadataCollectionName,
                                    TypeDef typeDef) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    void  validateUserForTypeDelete(String  userId,
                                    String  metadataCollectionName,
                                    TypeDef typeDef) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForEntityCreate(String       userId,
                                      String       metadataCollectionName,
                                      EntityDetail instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    void  validateUserForEntityRead(String       userId,
                                    String       metadataCollectionName,
                                    EntityDetail instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    void  validateUserForEntitySummaryRead(String        userId,
                                           String        metadataCollectionName,
                                           EntitySummary instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    void  validateUserForEntityProxyRead(String      userId,
                                         String      metadataCollectionName,
                                         EntityProxy instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForEntityUpdate(String       userId,
                                      String       metadataCollectionName,
                                      EntityDetail instance) throws UserNotAuthorizedException;

    /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classification classification details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForEntityClassificationUpdate(String         userId,
                                                    String         metadataCollectionName,
                                                    EntityDetail   instance,
                                                    Classification classification) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForEntityDelete(String       userId,
                                      String       metadataCollectionName,
                                      EntityDetail instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForRelationshipCreate(String       userId,
                                            String       metadataCollectionName,
                                            Relationship instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    void  validateUserForRelationshipRead(String       userId,
                                          String       metadataCollectionName,
                                          Relationship instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForRelationshipUpdate(String       userId,
                                            String       metadataCollectionName,
                                            Relationship instance) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    void  validateUserForRelationshipDelete(String       userId,
                                            String       metadataCollectionName,
                                            Relationship instance) throws UserNotAuthorizedException;
}
