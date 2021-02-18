/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.handlers;

import org.odpi.openmetadata.accessservices.digitalservice.builders.DigitalServiceBuilder;
import org.odpi.openmetadata.accessservices.digitalservice.events.DigitalServiceEvent;
import org.odpi.openmetadata.accessservices.digitalservice.mappers.DigitalServiceMapper;
import org.odpi.openmetadata.accessservices.digitalservice.properties.Classification;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalService;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * The Egeria DigitalService entity handler.
 */
public class DigitalServiceEntityHandler {

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
    public DigitalServiceEntityHandler(OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler, InvalidParameterHandler invalidParameterHandler) {
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
    }

    /**
     * Create digital service referencable string.
     * @param userId
     * @param serverName              the server name*
     * @param digitalService the digital service
     * @return the string
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    public String createDigitalServiceEntity(String userId,
                                             String serverName,
                                             DigitalService digitalService) throws PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   InvalidParameterException {

        String methodName = "createDigitalServiceEntity";

        invalidParameterHandler.validateUserId(userId, methodName);


        DigitalServiceBuilder digitalServiceBuilder = new DigitalServiceBuilder(digitalService.getDisplayName(),
                                                                                digitalService.getDescription(),
                                                                                digitalService.getVersion(),
                                                                                repositoryHelper,
                                                                                DigitalServiceMapper.SERVICE_NAME,
                                                                                serverName);



            String digitalServiceGUID = repositoryHandler.createEntity(userId,
                                                                       DigitalServiceMapper.DIGITAL_SERVICE_ENTITY_TYPE_GUID,
                                                                       DigitalServiceMapper.DIGITAL_SERVICE_ENTITY_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       digitalServiceBuilder.getInstanceProperties(methodName),
                                                                       methodName);


        return digitalServiceGUID;
    }

}

