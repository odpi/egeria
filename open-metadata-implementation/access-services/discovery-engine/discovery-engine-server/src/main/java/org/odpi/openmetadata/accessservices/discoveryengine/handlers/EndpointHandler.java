/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.builders.EndpointBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.EndpointConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.EndpointMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * EndpointHandler manages the Endpoint entity found within a connection object.  The Endpoint entity
 * describes the network location of a specific asset.  It may belong to multiple connections as well as being
 * part of a SoftWareServer definition.  This means it must be handled assuming it may be part of many constructs.
 * This is particularly important on delete.
 */
class EndpointHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private BasicHandler            basicHandler;
    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param basicHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    EndpointHandler(String serviceName,
                    String serverName,
                    BasicHandler basicHandler,
                    OMRSRepositoryHelper repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.basicHandler = basicHandler;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Find out if the endpoint is already stored in the repository.
     *
     * @param userId     calling user
     * @param endpoint   endpoint to find
     * @param methodName calling method
     * @return unique identifier of the endpoint or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String findEndpoint(String   userId,
                                Endpoint endpoint,
                                String   methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String guidParameterName      = "endpoint.getGUID";
        final String qualifiedNameParameter = "endpoint.getQualifiedName";

        if (endpoint != null)
        {
            invalidParameterHandler.validateName(endpoint.getQualifiedName(), qualifiedNameParameter, methodName);

            if (endpoint.getGUID() != null)
            {
                if (basicHandler.validateEntityGUID(userId,
                                                    endpoint.getGUID(),
                                                    EndpointMapper.ENDPOINT_TYPE_NAME,
                                                    methodName,
                                                    guidParameterName) != null)
                {
                    return endpoint.getGUID();
                }
            }

            EndpointBuilder endpointBuilder = new EndpointBuilder(endpoint.getQualifiedName(),
                                                                  endpoint.getDisplayName(),
                                                                  endpoint.getDescription(),
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

            EntityDetail existingEndpoint = basicHandler.getUniqueEntityByName(userId,
                                                                               endpoint.getQualifiedName(),
                                                                               qualifiedNameParameter,
                                                                               endpointBuilder.getQualifiedNameInstanceProperties(
                                                                                       methodName),
                                                                               EndpointMapper.ENDPOINT_TYPE_GUID,
                                                                               EndpointMapper.ENDPOINT_TYPE_NAME,
                                                                               methodName);
            if (existingEndpoint != null)
            {
                return existingEndpoint.getGUID();
            }
        }

        return null;
    }


    /**
     * Verify that the Endpoint object is stored in the repository and create it if it is not.
     * If the endpoint is located, there is no check that the endpoint values are equal to those in
     * the supplied endpoint object.
     *
     * @param userId   calling userId
     * @param endpoint object to add
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String saveEndpoint(String userId,
                        Endpoint endpoint) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException
    {
        final String methodName = "saveEndpoint";

        String existingEndpoint = this.findEndpoint(userId, endpoint, methodName);
        if (existingEndpoint == null)
        {
            return addEndpoint(userId, endpoint);
        }
        else
        {
            return updateEndpoint(userId, existingEndpoint, endpoint);
        }
    }


    /**
     * Verify that the Endpoint object is stored in the repository and create it if it is not.
     * If the endpoint is located, there is no check that the endpoint values are equal to those in
     * the supplied endpoint object.
     *
     * @param userId   calling userId
     * @param endpoint object to add
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String addEndpoint(String userId,
                               Endpoint endpoint) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String methodName = "addEndpoint";

        EndpointBuilder endpointBuilder = new EndpointBuilder(endpoint.getQualifiedName(),
                                                              endpoint.getDisplayName(),
                                                              endpoint.getDescription(),
                                                              endpoint.getAddress(),
                                                              endpoint.getProtocol(),
                                                              endpoint.getEncryptionMethod(),
                                                              endpoint.getAdditionalProperties(),
                                                              endpoint.getExtendedProperties(),
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);
        return basicHandler.createEntity(userId,
                                         EndpointMapper.ENDPOINT_TYPE_GUID,
                                         EndpointMapper.ENDPOINT_TYPE_NAME,
                                         endpointBuilder.getInstanceProperties(methodName),
                                         methodName);
    }


    /**
     * Update a stored endpoint.
     *
     * @param userId               userId
     * @param existingEndpointGUID unique identifier of the existing endpoint entity
     * @param endpoint             new endpoint values
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String updateEndpoint(String userId,
                                  String existingEndpointGUID,
                                  Endpoint endpoint) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName = "updateEndpoint";

        EndpointBuilder endpointBuilder = new EndpointBuilder(endpoint.getQualifiedName(),
                                                              endpoint.getDisplayName(),
                                                              endpoint.getDescription(),
                                                              endpoint.getAddress(),
                                                              endpoint.getProtocol(),
                                                              endpoint.getEncryptionMethod(),
                                                              endpoint.getAdditionalProperties(),
                                                              endpoint.getExtendedProperties(),
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);
        basicHandler.updateEntity(userId,
                                  existingEndpointGUID,
                                  EndpointMapper.ENDPOINT_TYPE_GUID,
                                  EndpointMapper.ENDPOINT_TYPE_NAME,
                                  endpointBuilder.getInstanceProperties(methodName),
                                  methodName);

        return existingEndpointGUID;
    }


    /**
     * Remove the requested Endpoint if it is no longer connected to any other connection or server
     * definition.
     *
     * @param userId       calling user
     * @param endpointGUID object to delete
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void removeEndpoint(String userId,
                        String endpointGUID) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        final String methodName        = "removeEndpoint";
        final String guidParameterName = "endpointGUID";

        basicHandler.deleteEntityOnLastUse(userId,
                                           endpointGUID,
                                           guidParameterName,
                                           EndpointMapper.ENDPOINT_TYPE_GUID,
                                           EndpointMapper.ENDPOINT_TYPE_NAME,
                                           methodName);
    }


    /**
     * Retrieve the requested endpoint object.
     *
     * @param userId       calling user
     * @param endpointGUID unique identifier of the endpoint object.
     * @return Endpoint bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    Endpoint getEndpoint(String userId,
                         String endpointGUID) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
        final String methodName        = "getEndpoint";
        final String guidParameterName = "endpointGUID";

        EntityDetail endpointEntity = basicHandler.getEntityByGUID(userId,
                                                                   endpointGUID,
                                                                   guidParameterName,
                                                                   EndpointMapper.ENDPOINT_TYPE_NAME,
                                                                   methodName);

        EndpointConverter converter = new EndpointConverter(endpointEntity,
                                                            repositoryHelper,
                                                            serviceName);

        return converter.getBean();
    }
}
