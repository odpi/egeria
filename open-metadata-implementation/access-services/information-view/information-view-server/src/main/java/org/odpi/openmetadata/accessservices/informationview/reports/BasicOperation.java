/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.NoRegistrationDetailsProvided;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class BasicOperation {

    private static final Logger log = LoggerFactory.getLogger(BasicOperation.class);
    protected final OMEntityDao omEntityDao;
    protected final OMRSAuditLog auditLog;
    protected final OMRSRepositoryHelper helper;
    public static final String SEPARATOR = "::";

    public BasicOperation(OMEntityDao omEntityDao, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.helper = helper;
    }

    /**
     * @param schemaAttributeTypeName       - type name for the actual schema type entity to be created
     * @param qualifiedNameForSchemaType    - qualifiedName for schema type entity
     * @param schemaAttributeTypeProperties - instance properties for schema attribute
     * @param schemaTypeRelationshipName    - type name for the actual schema type entity to be created
     * @param schemaAttributeGuid           - guid of the schema attribute for which the schema type was created
     * @return
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws RepositoryErrorException
     * @throws TypeDefNotKnownException
     */
    protected EntityDetail createSchemaType(String schemaAttributeTypeName,
                                            String qualifiedNameForSchemaType,
                                            InstanceProperties schemaAttributeTypeProperties,
                                            String schemaTypeRelationshipName,
                                            String schemaAttributeGuid) throws InvalidParameterException,
                                                                               StatusNotSupportedException,
                                                                               PropertyErrorException,
                                                                               EntityNotKnownException,
                                                                               FunctionNotSupportedException,
                                                                               PagingErrorException,
                                                                               ClassificationErrorException,
                                                                               UserNotAuthorizedException,
                                                                               TypeErrorException,
                                                                               RepositoryErrorException {
        EntityDetail schemaTypeEntity = omEntityDao.addEntity(schemaAttributeTypeName,
                qualifiedNameForSchemaType,
                schemaAttributeTypeProperties,
                false);

        omEntityDao.addRelationship(schemaTypeRelationshipName,
                schemaAttributeGuid,
                schemaTypeEntity.getGUID(),
                new InstanceProperties());
        return schemaTypeEntity;
    }


    public EntityDetail retrieveSoftwareServerCapability(String registrationGuid, String registrationQualifiedName) {
        EntityDetail softwareServerCapability = null;
        if (StringUtils.isEmpty(registrationGuid) && StringUtils.isEmpty(registrationQualifiedName)) {
            InformationViewErrorCode errorCode = InformationViewErrorCode.NO_REGISTRATION_DETAILS_PROVIDED;
            throw new NoRegistrationDetailsProvided(errorCode.getHttpErrorCode(), BasicOperation.class.getName(),
                    errorCode.getFormattedErrorMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    null);
        }
        if (!StringUtils.isEmpty(registrationGuid)) {
            try {
                return omEntityDao.getEntityByGuid(registrationGuid);
            } catch (RepositoryErrorException | UserNotAuthorizedException | EntityProxyOnlyException | InvalidParameterException | EntityNotKnownException e) {
                throwEntityNotFoundException(Constants.GUID, registrationGuid, Constants.SOFTWARE_SERVER_CAPABILITY);
            }
        }
        try {
            softwareServerCapability = omEntityDao.getEntity(Constants.SOFTWARE_SERVER_CAPABILITY,
                    registrationQualifiedName, false);
        } catch (PagingErrorException | UserNotAuthorizedException | FunctionNotSupportedException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException e) {
            throwEntityNotFoundException(Constants.QUALIFIED_NAME, registrationQualifiedName, Constants.SOFTWARE_SERVER_CAPABILITY);
        }

        if (softwareServerCapability == null) {
            return throwEntityNotFoundException(Constants.QUALIFIED_NAME, registrationQualifiedName, Constants.SOFTWARE_SERVER_CAPABILITY);
        }
        return softwareServerCapability;

    }

    private EntityDetail throwEntityNotFoundException(String propertyName, String propertyValue, String entityType) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION;
        throw new EntityNotFoundException(errorCode.getHttpErrorCode(), BasicOperation.class.getName(),
                errorCode.getFormattedErrorMessage(propertyName, propertyValue, entityType),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }

}
