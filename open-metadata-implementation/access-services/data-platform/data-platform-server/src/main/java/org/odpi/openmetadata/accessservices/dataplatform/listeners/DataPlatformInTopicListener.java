package org.odpi.openmetadata.accessservices.dataplatform.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.dataplatform.eventprocessor.EventPublisher;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEvent;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.views.DataPlatformAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.views.ViewHandler;
import org.odpi.openmetadata.accessservices.dataplatform.views.beans.DataPlatformAsset;
import org.odpi.openmetadata.accessservices.dataplatform.views.beans.View;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataPlatformInTopicListener implements OpenMetadataTopicListener {


    private static final Logger log = LoggerFactory.getLogger(DataPlatformInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMEntityDao omEntityDao;
    private final OMRSAuditLog auditLog;
    private EventPublisher eventPublisher;
    private OMRSRepositoryHelper helper;

    public DataPlatformInTopicListener(OMEntityDao omEntityDao, OMRSAuditLog auditLog, EventPublisher eventPublisher, OMRSRepositoryHelper helper) {
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.eventPublisher = eventPublisher;
        this.helper = helper;
    }

    /**
     * @param eventAsString contains all the information needed to create or update new assets from data platform like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {

        DataPlatformEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, DataPlatformEvent.class);
        } catch (Exception e) {
            DataPlatformErrorCode auditCode = DataPlatformErrorCode.PARSE_EVENT;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getErrorMessage(),
                    "event {" + eventAsString + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

        }
        if (event != null) {
            try {
                log.info("Started processing event in DataPlatform OMAS");

                DataPlatformAssetHandler dataPlatformAssetHandler = new DataPlatformAssetHandler(event, omEntityDao);
                ViewHandler viewsBuilder = new ViewHandler(event, omEntityDao, helper);
                ExecutorService executor = Executors.newCachedThreadPool();
                Future<DataPlatformAsset> dataPlatformAssetFuture = executor.submit(dataPlatformAssetHandler);
                Future<View> assetCreationFuture = executor.submit(viewsBuilder);

                DataPlatformAsset dataplatformAsset = dataPlatformAssetFuture.get();
                View view = assetCreationFuture.get();
                executor.shutdown();

                if (view.getViewEntity() != null) {
                    omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                            dataplatformAsset.getRelationalDbSchemaType().getGUID(),
                            view.getViewEntity().getGUID(),
                            Constants.DATA_PLATFORM_OMAS_NAME,
                            new InstanceProperties());
                    event.getTableSource().setTableGuid(view.getViewEntity().getGUID());
                }
                eventPublisher.sendEvent(event);
            } catch (Exception e) {
                log.error("Exception processing event from in topic", e);
                DataPlatformErrorCode auditCode = DataPlatformErrorCode.PROCESS_EVENT_EXCEPTION;

                auditLog.logException("processEvent",
                        auditCode.getErrorMessageId(),
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        auditCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
                        e.getMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
            }
        }
    }

}
