/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ConnectorTypeBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ConnectorTypeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectorTypeMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * ConnectorTypeHandler manages the ConnectorType entity found within a connection object.  The ConnectorType entity
 * describes the type of connector used to access a specific type of asset.  It may belong to multiple connections.
 * This means it must be handled assuming it may be part of many constructs.
 * This is particularly important on delete.
 */
public class ConnectorTypeHandler extends RootHandler
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName  name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public ConnectorTypeHandler(String                  serviceName,
                                String                  serverName,
                                InvalidParameterHandler invalidParameterHandler,
                                RepositoryHandler       repositoryHandler,
                                OMRSRepositoryHelper    repositoryHelper)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper);
    }


    /**
     * Find out if the connection type is already stored in the repository.
     * 
     * @param userId calling user
     * @param connectorType connectorType to find
     * @param methodName calling method
     *
     * @return unique identifier of the connectorType or null
     *
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String findConnectorType(String        userId,
                             ConnectorType connectorType,
                             String        methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String  guidParameterName = "connectorType.getGUID";
        final String  qualifiedNameParameter = "connectorType.getQualifiedName";

        if (connectorType != null)
        {
            invalidParameterHandler.validateName(connectorType.getQualifiedName(), qualifiedNameParameter, methodName);

            if (connectorType.getGUID() != null)
            {
                if (repositoryHandler.isEntityKnown(userId,
                                                    connectorType.getGUID(),
                                                    ConnectorTypeMapper.CONNECTOR_TYPE_NAME,
                                                    methodName,
                                                    guidParameterName) != null)
                {
                    return connectorType.getGUID();
                }
            }

            ConnectorTypeBuilder connectorTypeBuilder = new ConnectorTypeBuilder(connectorType.getQualifiedName(),
                                                                                 connectorType.getDisplayName(),
                                                                                 connectorType.getDescription(),
                                                                                 repositoryHelper,
                                                                                 serviceName,
                                                                                 serverName);

            EntityDetail existingConnectorType = repositoryHandler.getUniqueEntityByName(userId,
                                                                                         connectorType.getQualifiedName(),
                                                                                         qualifiedNameParameter,
                                                                                         connectorTypeBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                                         ConnectorTypeMapper.CONNECTOR_TYPE_GUID,
                                                                                         ConnectorTypeMapper.CONNECTOR_TYPE_NAME,
                                                                                         methodName);
            if (existingConnectorType != null)
            {
                return existingConnectorType.getGUID();
            }
        }

        return null;
    }


    /**
     * Verify that the ConnectorType object is stored in the repository and create it if it is not.
     * If the connectorType is located, there is no check that the connectorType values are equal to those in
     * the supplied connectorType object.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectorType object to add
     *
     * @return unique identifier of the connectorType in the repository.
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String saveConnectorType(String                 userId,
                                    String                 externalSourceGUID,
                                    String                 externalSourceName,
                                    ConnectorType          connectorType) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String  methodName = "saveConnectorType";

        String existingConnectorType = this.findConnectorType(userId, connectorType, methodName);
        if (existingConnectorType == null)
        {
            return this.addConnectorType(userId, externalSourceGUID, externalSourceName, connectorType);
        }
        else
        {
            return this.updateConnectorType(userId, externalSourceGUID, externalSourceName, existingConnectorType, connectorType);
        }
    }
    
    
    /**
     * Add a new connectorType entity object.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectorType object to add
     *
     * @return unique identifier of the connectorType in the repository.
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String  addConnectorType(String                 userId,
                             String                 externalSourceGUID,
                             String                 externalSourceName,
                             ConnectorType          connectorType) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String  methodName = "addConnectorType";

        ConnectorTypeBuilder connectorTypeBuilder = new ConnectorTypeBuilder(connectorType.getQualifiedName(),
                                                                             connectorType.getDisplayName(),
                                                                             connectorType.getDescription(),
                                                                             connectorType.getConnectorProviderClassName(),
                                                                             connectorType.getRecognizedAdditionalProperties(),
                                                                             connectorType.getRecognizedSecuredProperties(),
                                                                             connectorType.getRecognizedConfigurationProperties(),
                                                                             connectorType.getAdditionalProperties(),
                                                                             connectorType.getExtendedProperties(),
                                                                             repositoryHelper,
                                                                             serviceName,
                                                                             serverName);
        return repositoryHandler.createEntity(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              ConnectorTypeMapper.CONNECTOR_TYPE_GUID,
                                              ConnectorTypeMapper.CONNECTOR_TYPE_NAME,
                                              connectorTypeBuilder.getInstanceProperties(methodName),
                                              methodName);
    }


    /**
     * Update a stored connectorType.
     *
     * @param userId userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param existingConnectorTypeGUID unique identifier for a connector type
     * @param connectorType new connectorType values
     *
     * @return unique identifier of the connectorType in the repository.
     *
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String updateConnectorType(String         userId,
                               String         externalSourceGUID,
                               String         externalSourceName,
                               String         existingConnectorTypeGUID,
                               ConnectorType  connectorType) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String  methodName  = "updateConnectorType";

        ConnectorTypeBuilder connectorTypeBuilder = new ConnectorTypeBuilder(connectorType.getQualifiedName(),
                                                                             connectorType.getDisplayName(),
                                                                             connectorType.getDescription(),
                                                                             connectorType.getConnectorProviderClassName(),
                                                                             connectorType.getRecognizedAdditionalProperties(),
                                                                             connectorType.getRecognizedSecuredProperties(),
                                                                             connectorType.getRecognizedConfigurationProperties(),
                                                                             connectorType.getAdditionalProperties(),
                                                                             connectorType.getExtendedProperties(),
                                                                             repositoryHelper,
                                                                             serviceName,
                                                                             serverName);
        repositoryHandler.updateEntityProperties(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 existingConnectorTypeGUID,
                                                 ConnectorTypeMapper.CONNECTOR_TYPE_GUID,
                                                 ConnectorTypeMapper.CONNECTOR_TYPE_NAME,
                                                 connectorTypeBuilder.getInstanceProperties(methodName),
                                                 methodName);

        return existingConnectorTypeGUID;
    }


    /**
     * Remove the requested ConnectorType if it is no longer connected to any other connection or server
     * definition.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectorTypeGUID object to delete
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeConnectorType(String         userId,
                                    String         externalSourceGUID,
                                    String         externalSourceName,
                                    String         connectorTypeGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String  methodName = "removeConnectorType";
        final String  guidParameterName = "connectorTypeGUID";

        repositoryHandler.removeEntityOnLastUse(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                connectorTypeGUID,
                                                guidParameterName,
                                                ConnectorTypeMapper.CONNECTOR_TYPE_GUID,
                                                ConnectorTypeMapper.CONNECTOR_TYPE_NAME,
                                                methodName);
    }


    /**
     * Retrieve the requested connectorType object.
     *
     * @param userId       calling user
     * @param connectorTypeGUID unique identifier of the connectorType object.
     * @return ConnectorType bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public ConnectorType getConnectorType(String userId,
                                          String connectorTypeGUID) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName        = "getConnectorType";
        final String guidParameterName = "connectorTypeGUID";

        EntityDetail connectorTypeEntity = repositoryHandler.getEntityByGUID(userId,
                                                                             connectorTypeGUID,
                                                                             guidParameterName,
                                                                             ConnectorTypeMapper.CONNECTOR_TYPE_NAME,
                                                                             methodName);

        ConnectorTypeConverter converter = new ConnectorTypeConverter(connectorTypeEntity,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        return converter.getBean();
    }
}
