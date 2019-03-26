/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     *
     * @param schemaAttributeTypeName - type name for the actual schema type entity to be created
     * @param qualifiedNameForSchemaType - qualifiedName for schema type entity
     * @param schemaAttributeTypeProperties - instance properties for schema attribute
     * @param schemaTypeRelationshipName - type name for the actual schema type entity to be created
     * @param schemaAttributeGuid - guid of the schema attribute for which the schema type was created
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
                                                                               RepositoryErrorException,
                                                                               TypeDefNotKnownException {
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
}
