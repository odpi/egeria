/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
public class ErrorHandler
{
    private String                  serviceName;
    private String                  serverName;


    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param serviceName  name of this access service
     * @param serverName  name of this server
     */
    public ErrorHandler(String                    serviceName,
                        String                    serverName)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
    }

    /**
     * Check that there is a repository connector.
     *
     * @param methodName  name of the method being called
     * @param repositoryConnector  connector object
     *
     * @throws PropertyServerException exception thrown if the repository connector
     */
    void validateRepositoryConnector(OMRSRepositoryConnector   repositoryConnector,
                                     String                    methodName) throws PropertyServerException
    {
        if (repositoryConnector == null)
        {
            DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.OMRS_NOT_INITIALIZED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        if (! repositoryConnector.isActive())
        {
            DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.OMRS_NOT_AVAILABLE;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        try
        {
            repositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.NO_METADATA_COLLECTION;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws UserNotAuthorizedException the userId is unauthorised for the request
     */
    void handleUnauthorizedUser(String userId,
                                String methodName) throws UserNotAuthorizedException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.USER_NOT_AUTHORIZED;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(userId,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName);

        throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                             this.getClass().getName(),
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction(),
                                             userId);

    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call
     * @param propertyName  name of the property in error
     *
     * @throws InvalidParameterException invalid property
     */
    public void handleUnsupportedProperty(Throwable  error,
                                          String     methodName,
                                          String     propertyName) throws InvalidParameterException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.INVALID_PROPERTY;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(propertyName,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName,
                                                                                 error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            error,
                                            propertyName);

    }



    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException unexpected exception from property server
     */
    void handleRepositoryError(Throwable  error,
                               String     methodName) throws PropertyServerException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.PROPERTY_SERVER_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(error.getMessage(),
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if there is a problem with the asset guid
     *
     * @param error  caught exception
     * @param entityGUID  unique identifier for the requested entity
     * @param entityTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    void handleUnknownEntity(Throwable  error,
                             String     entityGUID,
                             String     entityTypeName,
                             String     methodName,
                             String     guidParameterName) throws InvalidParameterException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.UNKNOWN_ENTITY;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(entityTypeName,
                                                                                 entityGUID,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName,
                                                                                 error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            error,
                                            guidParameterName);

    }



    /**
     * Throw an exception if there is a problem with the asset guid
     *
     * @param error  caught exception
     * @param entityGUID  unique identifier for the requested entity
     * @param entityTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    void handleEntityProxy(Throwable  error,
                           String     entityGUID,
                           String     entityTypeName,
                           String     methodName,
                           String     guidParameterName) throws InvalidParameterException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.PROXY_ENTITY_FOUND;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityTypeName,
                                                                                                                   entityGUID,
                                                                                                                   serverName,
                                                                                                                   error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            error,
                                            guidParameterName);

    }



    /**
     * Throw an exception if multiple relationships are returned when not expected.
     *
     * @param entityGUID  unique identifier for the anchor entity
     * @param entityTypeName  name of the entity's type
     * @param relationshipTypeName expected type of relationship
     * @param returnedRelationships list of relationships returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    void handleAmbiguousRelationships(String             entityGUID,
                                      String             entityTypeName,
                                      String             relationshipTypeName,
                                      List<Relationship> returnedRelationships,
                                      String             methodName) throws PropertyServerException
    {
        List<String>  relationshipGUIDs = new ArrayList<>();

        if (returnedRelationships != null)
        {
            for (Relationship  relationship: returnedRelationships)
            {
                if (relationship != null)
                {
                    relationshipGUIDs.add(relationship.getGUID());
                }
            }
        }

        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.MULTIPLE_RELATIONSHIPS_FOUND;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipTypeName,
                                                                                                                   entityTypeName,
                                                                                                                   entityGUID,
                                                                                                                   relationshipGUIDs.toString(),
                                                                                                                   methodName,
                                                                                                                   serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if multiple relationships are returned when not expected.
     *
     * @param name  requested name for the entity
     * @param nameParameterName  name of hte parameter
     * @param entityTypeName  name of the entity's type
     * @param returnedEntities list of entities returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    void handleAmbiguousEntityName(String             name,
                                   String             nameParameterName,
                                   String             entityTypeName,
                                   List<EntityDetail> returnedEntities,
                                   String             methodName) throws PropertyServerException
    {
        List<String>  entityGUIDs = new ArrayList<>();

        if (returnedEntities != null)
        {
            for (EntityDetail  entity: returnedEntities)
            {
                if (entity != null)
                {
                    entityGUIDs.add(entity.getGUID());
                }
            }
        }

        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.MULTIPLE_ENTITIES_FOUND;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityTypeName,
                                                                                                                   name,
                                                                                                                   entityGUIDs.toString(),
                                                                                                                   methodName,
                                                                                                                   nameParameterName,
                                                                                                                   serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if no relationships are returned when not expected.
     *
     * @param entityGUID  unique identifier for the anchor entity
     * @param entityTypeName  name of the entity's type
     * @param relationshipTypeName expected type of relationship
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    void handleNoRelationship(String             entityGUID,
                              String             entityTypeName,
                              String             relationshipTypeName,
                              String             methodName) throws PropertyServerException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.NO_RELATIONSHIPS_FOUND;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipTypeName,
                                                                                                                   entityTypeName,
                                                                                                                   entityGUID,
                                                                                                                   methodName,
                                                                                                                   serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }



    /**
     * Throw an exception if no relationships are returned when not expected.
     *
     * @param entityTypeGUID  unique identifier for the entity's type
     * @param entityTypeName  name of the entity's type
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    void handleNoEntity(String             entityTypeGUID,
                        String             entityTypeName,
                        InstanceProperties properties,
                        String             methodName) throws PropertyServerException
    {
        DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.NULL_ENTITY_RETURNED;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                                   serverName,
                                                                                                                   entityTypeName,
                                                                                                                   entityTypeGUID,
                                                                                                                   properties.toString());

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }
}
