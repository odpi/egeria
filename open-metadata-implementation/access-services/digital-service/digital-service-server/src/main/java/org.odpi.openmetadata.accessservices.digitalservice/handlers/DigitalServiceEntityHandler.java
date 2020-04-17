/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.handlers;

import org.odpi.openmetadata.accessservices.digitalservice.events.DigitalServiceEvent;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalService;
import org.odpi.openmetadata.accessservices.digitalservice.utils.Constants;
import org.odpi.openmetadata.accessservices.digitalservice.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.accessservices.digitalservice.utils.Constants.DIGITAL_SERVICE_USER_ID;

/**
 * The type DigitalService asset handler.
 */
public class DigitalServiceEntityHandler {

    private String serviceName;
    private String serverName;
    private OMRSRepositoryHelper repositoryHelper;
    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Instantiates a new Deployed database schema asset handler.
     *
     * @param serviceName             the service name
     * @param serverName              the server name
     * @param repositoryHelper        the repository helper
     * @param repositoryHandler       the repository handler
     * @param invalidParameterHandler the invalid parameter handler
     */
    public DigitalServiceEntityHandler(String serviceName, String serverName, OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler, InvalidParameterHandler invalidParameterHandler) {
        this.serviceName=serviceName;
        this.serverName=serverName;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
    }

    /**
     * Create digital service referencable string.
     *
     * @param digitalService the digital service
     * @return the string
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    public String createDigitalServiceEntity(DigitalService digitalService)
            throws PropertyServerException,
            UserNotAuthorizedException,
            InvalidParameterException{

        String methodName = "create Digital Service Entity";

        //TODO qualified name is garbage.
        String qualifiedNameForDigitalService = digitalService.getQualifiedName();

        InstanceProperties digitalServiceProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.URL, digitalService.getURL())
                .withStringProperty(Constants.GUID, digitalService.getGUID())
                .withStringProperty(Constants.TYPE_ID, digitalService.getTypeId())
                .withStringProperty(Constants.TYPE_NAME, digitalService.getTypeName())
                .withLongProperty(Constants.TYPE_VERSION, digitalService.getTypeVersion())
                .withStringProperty(Constants.TYPE_DESCRIPTION, digitalService.getTypeDescription())
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDigitalService)
                .withStringProperty(Constants.DISPLAY_NAME, digitalService.getDisplayName())
                .withStringProperty(Constants.DESCRIPTION, digitalService.getDescription())
                .withIntegerProperty(Constants.IMPLEMENTATION_STYLE_ORDINAL, digitalService.getImplementationStyle().getOrdinal())
                .withStringProperty(Constants.IMPLEMANTATION_STYLE_NAME, digitalService.getImplementationStyle().getName())
                .withStringProperty(Constants.IMPLEMENTATION_STYLE_DISCRIPTION, digitalService.getImplementationStyle().getDescription())
                .build();
                //TODO check handling of embeded types before adding

        return repositoryHandler.createEntity(
                DIGITAL_SERVICE_USER_ID,
                repositoryHelper.getTypeDefByName(DIGITAL_SERVICE_USER_ID, Constants.DIGITAL_SERVICE).getGUID(),
                Constants.DIGITAL_SERVICE,
                digitalServiceProperties,
                methodName);

    }

}

