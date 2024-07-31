/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.handlers;

import org.odpi.openmetadata.accessservices.digitalservice.builders.DigitalServiceBuilder;
import org.odpi.openmetadata.accessservices.digitalservice.mappers.DigitalServiceMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalServiceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * The Egeria DigitalServiceProperties entity handler.
 */
public class DigitalServiceEntityHandler
{
    private OMRSRepositoryHelper repositoryHelper;
    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Instantiates a new Digital Service Entity handler.
     *
     * @param repositoryHelper        the repository helper
     * @param repositoryHandler       the repository handler
     * @param invalidParameterHandler the invalid parameter handler
     */
    public DigitalServiceEntityHandler(OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler, InvalidParameterHandler invalidParameterHandler)
    {
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
    }


    /**
     * Create digital service referencable string.
     * @param userId calling user
     * @param serverName              the server name
     * @param digitalServiceProperties the digital service
     * @return the string
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws UserNotAuthorizedException  user not authorized
     */
    public String createDigitalServiceEntity(String                   userId,
                                             String                   serverName,
                                             DigitalServiceProperties digitalServiceProperties) throws PropertyServerException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       InvalidParameterException
    {

        String methodName = "createDigitalServiceEntity";

        invalidParameterHandler.validateUserId(userId, methodName);


        DigitalServiceBuilder digitalServiceBuilder = new DigitalServiceBuilder(digitalServiceProperties.getDisplayName(),
                                                                                digitalServiceProperties.getDescription(),
                                                                                digitalServiceProperties.getVersion(),
                                                                                repositoryHelper,
                                                                                DigitalServiceMapper.SERVICE_NAME,
                                                                                serverName);



        return repositoryHandler.createEntity(userId,
                                              DigitalServiceMapper.DIGITAL_SERVICE_ENTITY_TYPE_GUID,
                                              DigitalServiceMapper.DIGITAL_SERVICE_ENTITY_TYPE_NAME,
                                              null,
                                              null,
                                              digitalServiceBuilder.getInstanceProperties(methodName),
                                              null,
                                              InstanceStatus.ACTIVE,
                                              methodName);


    }

}

