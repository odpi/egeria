/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;

import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.ReportSubmitException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode.REPORT_SUBMIT_EXCEPTION;


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
     * @throws ReportSubmitException
     */
    public void submitReportModel(ReportRequestBody payload) throws ReportSubmitException {

        try {
            if(log.isDebugEnabled()) {
                log.debug("Creating report based on payload {}", payload);
            }
            URL url = new URL(payload.getReport().getReportUrl());
            String networkAddress = url.getHost();
            if (url.getPort() > 0) {
                networkAddress = networkAddress + ":" + url.getPort();
            }

            String qualifiedNameForReport = QualifiedNameUtils.buildQualifiedName("", Constants.DEPLOYED_REPORT,networkAddress + BasicOperation.SEPARATOR + payload.getReport().getId());
            InstanceProperties reportProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForReport)
                    .withStringProperty(Constants.NAME, payload.getReport().getReportName())
                    .withStringProperty(Constants.AUTHOR, payload.getReport().getAuthor())
                    .withStringProperty(Constants.ID, payload.getReport().getId())
                    .withStringProperty(Constants.URL, payload.getReport().getReportUrl())
                    .withStringProperty(Constants.LAST_MODIFIER, payload.getReport().getLastModifier())
                    .withDateProperty(Constants.LAST_MODIFIED_TIME, payload.getReport().getLastModifiedTime())
                    .withDateProperty(Constants.CREATE_TIME, payload.getReport().getCreatedTime())
                    .build();


            OMEntityWrapper reportWrapper = omEntityDao.saveEntityReferenceCopy(payload.getRegistrationGuid(),
                                                                                Constants.DEPLOYED_REPORT,
                                                                                qualifiedNameForReport,
                                                                                reportProperties,
                                                                                true,
                                                                                true);


            if (reportWrapper.getEntityStatus().equals(OMEntityWrapper.EntityStatus.NEW)) {
                reportCreator.createReport(payload, reportWrapper.getEntityDetail());
            } else {
                reportUpdater.updateReport(payload, reportWrapper.getEntityDetail());
            }

        } catch (PagingErrorException |  PropertyErrorException | EntityNotKnownException | UserNotAuthorizedException | StatusNotSupportedException | InvalidParameterException | MalformedURLException | FunctionNotSupportedException | RepositoryErrorException | TypeErrorException | ClassificationErrorException | EntityProxyOnlyException | RelationshipNotDeletedException | RelationshipNotKnownException | EntityNotDeletedException | TypeDefNotKnownException | InvalidEntityException | EntityConflictException | HomeEntityException e) {

            throw new ReportSubmitException(500,
                    ReportHandler.class.getName(),
                    REPORT_SUBMIT_EXCEPTION.getFormattedErrorMessage( e.getMessage()),
                    REPORT_SUBMIT_EXCEPTION.getUserAction(),
                    REPORT_SUBMIT_EXCEPTION.getSystemAction(),
                    e,
                    payload.getReport().getReportName());
        }

    }


    public void getReport(String userId, String registrationGuid, String reportId){

    }


}
