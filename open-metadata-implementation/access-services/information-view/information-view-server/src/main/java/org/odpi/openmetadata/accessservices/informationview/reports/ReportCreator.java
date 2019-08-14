/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReportCreator extends ReportBasicOperation {

    private static final Logger log = LoggerFactory.getLogger(ReportCreator.class);

    public ReportCreator(org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, lookupHelper, helper, auditLog);
    }


    /**
     *
     *
     * @param userId
     * @param payload object describing the report
     * @param reportEntity entity describing the report
     */
    public void createReport(String userId, ReportRequestBody payload, EntityDetail reportEntity) {
        String qualifiedNameForComplexSchemaType =  QualifiedNameUtils.buildQualifiedName("", Constants.ASSET_SCHEMA_TYPE, payload.getReport().getId()  + Constants.TYPE_SUFFIX);
        InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                .build();
        OMEntityWrapper complexSchemaTypeEntityWrapper = omEntityDao.createOrUpdateExternalEntity(userId,
                                                                                        Constants.COMPLEX_SCHEMA_TYPE,
                                                                                        qualifiedNameForComplexSchemaType,
                                                                                        payload.getRegistrationGuid(),
                                                                                        payload.getRegistrationQualifiedName(),
                                                                                        complexSchemaTypeProperties,
                                                                            null,
                                                                                 true,
                                                                                         false);

        log.debug("Created report schema type {}", complexSchemaTypeEntityWrapper.getEntityDetail().getGUID());
        omEntityDao.addExternalRelationship(userId,
                                            Constants.ASSET_SCHEMA_TYPE,
                                            payload.getRegistrationGuid(),
                                            payload.getRegistrationQualifiedName(),
                                            reportEntity.getGUID(),
                                            complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(),
                                            new InstanceProperties());

        addElements(userId, helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, reportEntity.getProperties(), "createReport"), complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(), payload.getRegistrationGuid(), payload.getRegistrationQualifiedName(), payload.getReport().getReportElements());
    }
}