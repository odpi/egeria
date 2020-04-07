/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.handlers;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.accessservices.dataplatform.utils.Constants.DATA_PLATFORM_OMAS_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.Constants.DATA_PLATFORM_USER_ID;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.PATCH_LEVEL__PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.SOURCE_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.TYPE_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataplatform.utils.SoftwareServerCapabilityMapper.VERSION__PROPERTY_NAME;


/**
 * The type Registration handler.
 */
public class RegistrationHandler {

    private String serviceName;
    private OMRSRepositoryHelper repositoryHelper;
    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Instantiates a new Registration handler.
     *
     * @param serviceName             the service name
     * @param repositoryHelper        the repository helper
     * @param repositoryHandler       the repository handler
     * @param invalidParameterHandler the invalid parameter handler
     */
    public RegistrationHandler(String serviceName, OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler, InvalidParameterHandler invalidParameterHandler) {
        this.serviceName = serviceName;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
    }


    /**
     * Create software server capability entity.
     *
     * @param softwareServerCapability the software server capability
     * @return the string
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     */
    public String createSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        final String methodName = "createExternalDataPlatform";

        String qualifiedNameForSoftwareServer = softwareServerCapability.getQualifiedName();

        invalidParameterHandler.validateUserId(DATA_PLATFORM_USER_ID, methodName);
        invalidParameterHandler.validateName(qualifiedNameForSoftwareServer, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(QUALIFIED_NAME_PROPERTY_NAME, qualifiedNameForSoftwareServer)
                .withStringProperty(PATCH_LEVEL__PROPERTY_NAME, softwareServerCapability.getPatchLevel())
                .withStringProperty(TYPE_PROPERTY_NAME, softwareServerCapability.getDataPlatformType())
                .withStringProperty(VERSION__PROPERTY_NAME, softwareServerCapability.getDataPlatformVersion())
                .withStringProperty(SOURCE_PROPERTY_NAME, softwareServerCapability.getSource())
                .withStringProperty(NAME_PROPERTY_NAME, softwareServerCapability.getDisplayName())
                .withStringProperty(Constants.DESCRIPTION, softwareServerCapability.getDescription())
                .build();

        return repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID,SOFTWARE_SERVER_CAPABILITY_TYPE_NAME).getGUID(),
                SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                softwareServerProperties,
                methodName);
    }


    /**
     * Gets software server capability by qualified name.
     *
     * @param userId        the user id
     * @param qualifiedName the qualified name
     * @return the software server capability entity
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     */
    public SoftwareServerCapability getSoftwareServerCapabilityByQualifiedName(String userId, String qualifiedName) throws InvalidParameterException,UserNotAuthorizedException, PropertyServerException {

        final String methodName = "getExternalDataPlatformByQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(
                serviceName,
                null,
                QUALIFIED_NAME_PROPERTY_NAME,
                qualifiedName,
                methodName);

        EntityDetail entity=repositoryHandler.getUniqueEntityByName(
                userId,
                qualifiedName,
                QUALIFIED_NAME_PROPERTY_NAME,
                properties,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID,SOFTWARE_SERVER_CAPABILITY_TYPE_NAME).getGUID(),
                SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                methodName);
        return buildSoftwareServerCapabilitySource(entity);
    }

    private SoftwareServerCapability buildSoftwareServerCapabilitySource(EntityDetail entity) {
        String methodName= "buildSoftwareServerCapabilitySource";
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setDisplayName(repositoryHelper.getStringProperty(DATA_PLATFORM_OMAS_NAME, NAME_PROPERTY_NAME, entity.getProperties(),  methodName));
        softwareServerCapability.setQualifiedName(repositoryHelper.getStringProperty(DATA_PLATFORM_OMAS_NAME, QUALIFIED_NAME_PROPERTY_NAME, entity.getProperties(),  methodName));
        softwareServerCapability.setDataPlatformVersion(repositoryHelper.getStringProperty(DATA_PLATFORM_OMAS_NAME, VERSION__PROPERTY_NAME, entity.getProperties(),  methodName));
        softwareServerCapability.setPatchLevel(repositoryHelper.getStringProperty(DATA_PLATFORM_OMAS_NAME,PATCH_LEVEL__PROPERTY_NAME, entity.getProperties(),  methodName));
        softwareServerCapability.setDataPlatformType(repositoryHelper.getStringProperty(Constants.DATA_PLATFORM_OMAS_NAME, TYPE_PROPERTY_NAME, entity.getProperties(),  methodName));
        softwareServerCapability.setDescription(repositoryHelper.getStringProperty(DATA_PLATFORM_OMAS_NAME, Constants.DESCRIPTION, entity.getProperties(),  methodName));
        return softwareServerCapability;
    }
}
