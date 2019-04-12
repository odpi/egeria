/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.InformationViewExceptionBase;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;


public class ReportHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportHandler.class);
    private ReportCreator reportCreator;
    private ReportUpdater reportUpdater;
    private org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao;
    private OMRSAuditLog auditLog;

    public ReportHandler(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper repositoryHelper, OMRSAuditLog auditLog) {
        reportCreator = new ReportCreator(omEntityDao, lookupHelper, repositoryHelper, auditLog);
        reportUpdater = new ReportUpdater(omEntityDao, lookupHelper, repositoryHelper, auditLog);
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
    }


    /**
     *
     * @param payload - object describing the report
     * @throws ReportCreationException
     */
    public void submitReportModel(ReportRequestBody payload) throws ReportCreationException {

        try {
            log.debug("Creating report based on payload {}", payload);
            URL url = new URL(payload.getReportUrl());
            String networkAddress = url.getHost();
            if (url.getPort() > 0) {
                networkAddress = networkAddress + ":" + url.getPort();
            }

            String qualifiedNameForReport = networkAddress + BasicOperation.SEPARATOR + payload.getId();
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


            OMEntityWrapper reportWrapper = omEntityDao.createOrUpdateEntity(Constants.DEPLOYED_REPORT,
                                                                            qualifiedNameForReport,
                                                                            reportProperties,
                                                                            null,
                                                                            true,
                                                                            true);


            if (reportWrapper.getEntityStatus().equals(OMEntityWrapper.EntityStatus.NEW)) {
                reportCreator.createReport(payload, reportWrapper.getEntityDetail());
            } else {
                reportUpdater.updateReport(payload, reportWrapper.getEntityDetail());
            }

        } catch (PagingErrorException |  PropertyErrorException | EntityNotKnownException | UserNotAuthorizedException | StatusNotSupportedException | InvalidParameterException | MalformedURLException | FunctionNotSupportedException | RepositoryErrorException | TypeErrorException | ClassificationErrorException | EntityProxyOnlyException | RelationshipNotDeletedException | RelationshipNotKnownException | EntityNotDeletedException | TypeDefNotKnownException e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.REPORT_CREATION_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(payload.toString(), e.getMessage()),
                    "json {" + payload + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new ReportCreationException(500,
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
