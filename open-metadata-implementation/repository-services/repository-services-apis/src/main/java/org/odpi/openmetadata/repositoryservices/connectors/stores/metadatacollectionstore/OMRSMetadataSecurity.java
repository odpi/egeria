/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

public class OMRSMetadataSecurity implements OpenMetadataRepositorySecurity
{
    private OpenMetadataRepositorySecurity connector = null;


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityConnector new security verifier
     */
    public void setSecurityVerifier(OpenMetadataRepositorySecurity securityConnector)
    {
        if (securityConnector != null)
        {
            this.connector = securityConnector;
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    public void  validateUserForTypeUpdate(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
        }
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a user not authorized exception on all access/update
     * requests.
     */

    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForEntityCreate(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityCreate(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    public void  validateUserForEntityRead(String          userId,
                                           String          metadataCollectionName,
                                           EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityRead(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntitySummaryRead(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityProxyRead(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityUpdate(userId, metadataCollectionName, instance);
        }
    }


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
    public void  validateUserForEntityClassificationUpdate(String          userId,
                                                           String          metadataCollectionName,
                                                           EntityDetail    instance,
                                                           Classification classification) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityClassificationUpdate(userId, metadataCollectionName, instance, classification);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityDelete(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForRelationshipCreate(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipCreate(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    public void  validateUserForRelationshipRead(String          userId,
                                                 String          metadataCollectionName,
                                                 Relationship    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipRead(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipUpdate(userId, metadataCollectionName, instance);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipDelete(userId, metadataCollectionName, instance);
        }
    }
}
