/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
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
     * @param payload object describing the report
     * @param reportEntity entity describing the report
     * @throws Exception
     */
    public void createReport(ReportRequestBody payload, EntityDetail reportEntity) throws Exception {
        String qualifiedNameForComplexSchemaType = helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, reportEntity.getProperties(), "createReport") + Constants.TYPE_SUFFIX;
        InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                .build();
        OMEntityWrapper complexSchemaTypeEntityWrapper = omEntityDao.createOrUpdateEntity(Constants.COMPLEX_SCHEMA_TYPE,
                qualifiedNameForComplexSchemaType, complexSchemaTypeProperties, null, true, false);

        log.debug("Created report schema type {}", complexSchemaTypeEntityWrapper.getEntityDetail().getGUID());
        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                reportEntity.getGUID(),
                complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(),
                new InstanceProperties());

        addElements(helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, reportEntity.getProperties(), "createReport"), complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(), payload.getReportElements());
    }





}
