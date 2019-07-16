/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.registration;

import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.exceptions.RegistrationException;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.exceptions.RetrieveEntityException;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataplatform.responses.RegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


public class RegistrationHandler {

    private String serviceName;
    private OMRSRepositoryHelper repositoryHelper;
    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;

    public RegistrationHandler(String serviceName, OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler, InvalidParameterHandler invalidParameterHandler) {
        this.serviceName = serviceName;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
    }

    /**
     * Create software server capability entity.
     *
     * @param registrationRequestBody the registration event
     * @return the string
     * @throws RegistrationException the registration exception
     */
    public String createSoftwareServerCapability(RegistrationRequestBody registrationRequestBody) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        final String methodName = "createSoftwareServerCapability";

        SoftwareServerCapability softwareServerCapability = registrationRequestBody.getSoftwareServerCapability();
        String qualifiedNameForSoftwareServer = softwareServerCapability.getQualifiedName();

        invalidParameterHandler.validateUserId(Constants.DATA_PLATFORM_USER_ID, methodName);
        invalidParameterHandler.validateName(qualifiedNameForSoftwareServer, Constants.QUALIFIED_NAME, methodName);

        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServer)
                .withStringProperty(Constants.PATCH_LEVEL, softwareServerCapability.getPatchLevel())
                .withStringProperty(Constants.TYPE, softwareServerCapability.getDataPlatformType())
                .withStringProperty(Constants.VERSION, softwareServerCapability.getDataPlatformVersion())
                .withStringProperty(Constants.SOURCE, softwareServerCapability.getSource())
                .withStringProperty(Constants.NAME, softwareServerCapability.getDisplayName())
                .withStringProperty(Constants.DESCRIPTION, softwareServerCapability.getDescription())
                .build();

        return repositoryHandler.createEntity(
                Constants.DATA_PLATFORM_USER_ID,
                Constants.SOFTWARE_SERVER_CAPABILITY_GUID,
                Constants.SOFTWARE_SERVER_CAPABILITY,
                softwareServerProperties,
                methodName);
    }


    /**
     * Gets software server capability guid by qualified name .
     *
     * @param userId        the user id
     * @param qualifiedName the qualified name
     * @return the software server capability guid
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     */
    public String getSoftwareServerCapabilityGuidByQualifiedName(String userId, String qualifiedName) throws InvalidParameterException,UserNotAuthorizedException, PropertyServerException {

        final String methodName = "getSoftwareServerCapabilityByQualifiedName";


        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, Constants.QUALIFIED_NAME, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(
                serviceName,
                null,
                Constants.QUALIFIED_NAME,
                qualifiedName,
                methodName);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(
                userId,
                qualifiedName,
                Constants.QUALIFIED_NAME,
                properties,
                Constants.SOFTWARE_SERVER_CAPABILITY_GUID,
                Constants.SOFTWARE_SERVER_CAPABILITY,
                methodName);

        return retrievedEntity.getGUID();
    }
}
