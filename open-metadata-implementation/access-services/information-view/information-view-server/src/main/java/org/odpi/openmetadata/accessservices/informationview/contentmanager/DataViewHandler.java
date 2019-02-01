/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.DataViewCreationException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewHandler {


    private static final Logger log = LoggerFactory.getLogger(DataViewHandler.class);
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private OMRSAuditLog auditLog;
    private DataViewCreator dataViewCreator;
    private DataViewUpdater dataViewUpdater;


    public DataViewHandler(EntitiesCreatorHelper entitiesCreatorHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        dataViewCreator = new DataViewCreator(entitiesCreatorHelper, helper, auditLog);
        dataViewUpdater = new DataViewUpdater(entitiesCreatorHelper, helper, auditLog);
        this.auditLog = auditLog;
    }


    public void createDataView(DataViewRequestBody requestBody) throws DataViewCreationException {

        log.info("Creating data view based on payload {}", requestBody);
        try {
            String qualifiedNameForDataView = requestBody.getEndpointAddress() + "." + requestBody.getId();
            InstanceProperties dataViewProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataView)
                    .withStringProperty(Constants.NAME, requestBody.getName())
                    .withStringProperty(Constants.OWNER, requestBody.getAuthor())
                    .withStringProperty(Constants.ID, requestBody.getId())
                    .withStringProperty(Constants.LAST_MODIFIER, requestBody.getLastModifier())
                    .withDateProperty(Constants.LAST_MODIFIED_TIME, requestBody.getLastModifiedTime())
                    .withDateProperty(Constants.CREATE_TIME, requestBody.getCreatedTime())
                    .withStringProperty(Constants.NATIVE_CLASS, requestBody.getNativeClass())
                    .build();


            EntityDetailWrapper dataViewWrapper = entitiesCreatorHelper.createOrUpdateEntity(Constants.DATA_VIEW,
                                                                            qualifiedNameForDataView,
                                                                            dataViewProperties,
                                                                            null,
                                                                            true);


            dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());

//            if (dataViewWrapper.getEntityStatus().equals(EntityDetailWrapper.EntityStatus.NEW)) {
//                dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } else {
//                dataViewUpdater.updateDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } TODO update not implemented yet


        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.REPORT_CREATION_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(requestBody.toString(), e.getMessage()),
                    "json {" + requestBody + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new DataViewCreationException(404,
                    "ReportHandler",
                    "createReport",
                    "Unable to create data view: " + e.getMessage(),
                    "The system is unable to process the request.",
                    "Correct the payload submitted to request.",
                    e,
                    requestBody.getId());
        }

    }


}
