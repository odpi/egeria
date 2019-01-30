/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.contentmanager;

import org.odpi.openmetadata.accessservices.assetlineage.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.assetlineage.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.assetlineage.utils.Constants;
import org.odpi.openmetadata.accessservices.assetlineage.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;


public class ReportHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportHandler.class);
    private ReportCreator reportCreator;
    private ReportUpdater reportUpdater;
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private OMRSAuditLog auditLog;

    public ReportHandler(EntitiesCreatorHelper entitiesCreatorHelper, LookupHelper lookupHelper, OMRSAuditLog auditLog) {
        reportCreator = new ReportCreator(entitiesCreatorHelper, lookupHelper, auditLog);
        reportUpdater = new ReportUpdater(entitiesCreatorHelper, lookupHelper, auditLog);
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        this.auditLog = auditLog;
    }

    public void submitReportModel(ReportRequestBody payload) throws ReportCreationException {

        try {
            log.info("Creating report based on payload {}", payload);
            URL url = new URL(payload.getReportUrl());
            String networkAddress = url.getHost();
            if (url.getPort() > 0) {
                networkAddress = networkAddress + ":" + url.getPort();
            }

            String qualifiedNameForReport = networkAddress + "." + payload.getId();
            InstanceProperties reportProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForReport)
                    .withStringProperty(Constants.NAME, payload.getReportName())
                    .withStringProperty(Constants.AUTHOR, payload.getAuthor())
                    .withStringProperty(Constants.ID, payload.getId())
                    .withStringProperty(Constants.URL, payload.getReportUrl())
                    .withStringProperty(Constants.LAST_MODIFIER, payload.getLastModifier())
                    .withDateProperty(Constants.LAST_MODIFIED_TIME, payload.getLastModifiedTime())
                    .withDateProperty(Constants.CREATE_TIME, payload.getCreatedTime())
                    .build();


            EntityDetailWrapper reportWrapper = entitiesCreatorHelper.createOrUpdateEntity(Constants.DEPLOYED_REPORT,
                    qualifiedNameForReport,
                    reportProperties,
                    null,
                    true);


            if (reportWrapper.getEntityStatus().equals(EntityDetailWrapper.EntityStatus.NEW)) {
                reportCreator.createReport(payload, reportWrapper.getEntityDetail());
            } else {
                reportUpdater.updateReport(payload, reportWrapper.getEntityDetail());
            }

        } catch (Exception e) {
            AssetLineageErrorCode auditCode = AssetLineageErrorCode.REPORT_CREATION_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(payload.toString(), e.getMessage()),
                    "json {" + payload + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new ReportCreationException(404,
                    "ReportHandler",
                    "createReport",
                    "Unable to create report: " + e.getMessage(),
                    "The system is unable to process the request.",
                    "Correct the payload submitted to request.",
                    e,
                    payload.getReportName());
        }
    }


}
