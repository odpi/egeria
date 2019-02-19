/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
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

    protected EntityDetail createSchemaType(String schemaAttributeType,
                                            String qualifiedNameForType,
                                            InstanceProperties typeProperties,
                                            String schemaTypeRelationship,
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
        EntityDetail schemaTypeEntity = omEntityDao.addEntity(schemaAttributeType,
                qualifiedNameForType,
                typeProperties);

        omEntityDao.addRelationship(schemaTypeRelationship,
                schemaAttributeGuid,
                schemaTypeEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());
        return schemaTypeEntity;
    }
}
