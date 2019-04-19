/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.registration;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.RegistrationRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RegistrationException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveEntityException;
import org.odpi.openmetadata.accessservices.informationview.lookup.SoftwareServerCapabilityLookup;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationHandler {


    private static final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);
    private org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao;
    private OMRSRepositoryHelper repositoryHelper;
    private OMRSAuditLog auditLog;
    private SoftwareServerCapabilityLookup lookup;

    public RegistrationHandler(OMEntityDao omEntityDao, OMRSRepositoryConnector enterpriseConnector,
                               OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        this.repositoryHelper = enterpriseConnector.getRepositoryHelper();
        this.auditLog = auditLog;
        this.lookup = new SoftwareServerCapabilityLookup(enterpriseConnector,omEntityDao,null,auditLog);
    }


    public EntityDetail registerTool(RegistrationRequestBody requestBody) {

        SoftwareServerCapabilitySource softwareServerCapability = requestBody.getSoftwareServerCapability();
        String qualifiedNameForSoftwareServer = softwareServerCapability.getName();
        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServer)
                .withStringProperty(Constants.PATCH_LEVEL, softwareServerCapability.getPatchLevel())
                .withStringProperty(Constants.TYPE, softwareServerCapability.getType())
                .withStringProperty(Constants.VERSION, softwareServerCapability.getVersion())
                .withStringProperty(Constants.SOURCE, softwareServerCapability.getSource())
                .withStringProperty(Constants.NAME, softwareServerCapability.getName())
                .withStringProperty(Constants.DESCRIPTION, softwareServerCapability.getDescription())
                .build();

        OMEntityWrapper registration;
        try {
            registration = omEntityDao.createOrUpdateEntity(Constants.SOFTWARE_SERVER_CAPABILITY,
                    qualifiedNameForSoftwareServer,
                    softwareServerProperties,
                    null,
                    true,
                    true);
            return registration.getEntityDetail();
        } catch (InvalidParameterException | StatusNotSupportedException | PropertyErrorException | EntityNotKnownException | TypeErrorException | FunctionNotSupportedException | PagingErrorException | ClassificationErrorException | UserNotAuthorizedException | RepositoryErrorException e) {
            throw new RegistrationException(
                    InformationViewErrorCode.REGISTRATION_EXCEPTION.getHttpErrorCode(),
                    RegistrationHandler.class.getName(),
                    InformationViewErrorCode.REGISTRATION_EXCEPTION.getFormattedErrorMessage(e.getMessage()),
                    InformationViewErrorCode.REGISTRATION_EXCEPTION.getSystemAction(),
                    InformationViewErrorCode.REGISTRATION_EXCEPTION.getUserAction(),
                    null);
        }
    }



    public EntityDetail lookupSoftwareServerCapability(RegistrationRequestBody requestBody) {

        try {
            return lookup.lookupEntity(requestBody.getSoftwareServerCapability());
        } catch (UserNotAuthorizedException | FunctionNotSupportedException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            throw new RetrieveEntityException(RegistrationHandler.class.getName(),
                    InformationViewErrorCode.GET_ENTITY_EXCEPTION.getFormattedErrorMessage(e.getMessage()),
                    InformationViewErrorCode.GET_ENTITY_EXCEPTION.getSystemAction(),
                    InformationViewErrorCode.GET_ENTITY_EXCEPTION.getUserAction(),
                    null);
        }
    }


}
