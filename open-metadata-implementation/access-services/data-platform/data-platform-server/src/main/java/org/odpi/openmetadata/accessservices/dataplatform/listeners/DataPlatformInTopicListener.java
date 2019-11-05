/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.beans.InformationViewAsset;
import org.odpi.openmetadata.accessservices.dataplatform.beans.View;
import org.odpi.openmetadata.accessservices.dataplatform.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEventHeader;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEventType;
import org.odpi.openmetadata.accessservices.dataplatform.events.NewDeployedDatabaseSchemaEvent;
import org.odpi.openmetadata.accessservices.dataplatform.events.NewViewEvent;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.DeployedDatabaseSchemaAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.InformationViewAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.ViewHandler;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformServicesInstance;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataPlatformInTopicListener implements OpenMetadataTopicListener {


    private static final Logger log = LoggerFactory.getLogger(DataPlatformInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMEntityDao omEntityDao;
    private final OMRSAuditLog auditLog;
    private OMRSRepositoryHelper repositoryHelper;
    private DataPlatformServicesInstance  instance;



    public DataPlatformInTopicListener(DataPlatformServicesInstance  instance,OMEntityDao omEntityDao, OMRSAuditLog auditLog, OMRSRepositoryHelper repositoryHelper) {
        this.instance = instance;
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.repositoryHelper = repositoryHelper;
    }

    /**
     * @param eventAsString contains all the information needed to create or update new assets from data platform like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {


        try {
            //TODO: Use Strategy Pattern instead of multiple if else conditions
            DataPlatformEventHeader dataPlatformEventHeader = OBJECT_MAPPER.readValue(eventAsString, DataPlatformEventHeader.class);

            if (dataPlatformEventHeader.getEventType() == DataPlatformEventType.NEW_DEPLOYED_DB_SCHEMA_EVENT) {
                NewDeployedDatabaseSchemaEvent newDeployedDatabaseSchemaEvent = OBJECT_MAPPER.readValue(eventAsString, NewDeployedDatabaseSchemaEvent.class);
                log.info("Started processing NewDeployedDatabaseSchemaEvent event in DataPlatform OMAS");

                DeployedDatabaseSchemaAssetHandler handler = instance.getDeployedDatabaseSchemaAssetHandler();
                handler.createDeployedDatabaseSchemaAsset(newDeployedDatabaseSchemaEvent);
                log.info("Processing NewDeployedDatabaseSchemaEvent event finished: {}", newDeployedDatabaseSchemaEvent);


            } else if (dataPlatformEventHeader.getEventType() == DataPlatformEventType.NEW_INFORMATION_VIEW_EVENT) {

                NewViewEvent newViewEvent = OBJECT_MAPPER.readValue(eventAsString, NewViewEvent.class);
                log.info("Started processing NewView event in DataPlatform OMAS");
                InformationViewAssetHandler informationViewAssetHandler = new InformationViewAssetHandler(newViewEvent, omEntityDao);
                ViewHandler viewsBuilder = new ViewHandler(newViewEvent, omEntityDao, repositoryHelper);
                ExecutorService executor = Executors.newCachedThreadPool();
                Future<InformationViewAsset> informationViewAssetFuture = executor.submit(informationViewAssetHandler);
                Future<View> assetCreationFuture = executor.submit(viewsBuilder);

                InformationViewAsset informationViewAsset = informationViewAssetFuture.get();
                View view = assetCreationFuture.get();
                executor.shutdown();

                if (view.getViewEntity() != null) {
                    omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                            informationViewAsset.getRelationalDbSchemaType().getGUID(),
                            view.getViewEntity().getGUID(),
                            new InstanceProperties());
                    newViewEvent.getTableSource().setGuid(view.getViewEntity().getGUID());
                }
            }
            //TODO: optimize exception handling with specific exception details and actions
        } catch (IOException e) {
            DataPlatformErrorCode errorCode = DataPlatformErrorCode.PARSE_EVENT_EXCEPTION;

            auditLog.logException("parseEvent",
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (Exception e) {
            log.error("Exception processing event from in topic", e);
            DataPlatformErrorCode errorCode = DataPlatformErrorCode.PROCESS_EVENT_EXCEPTION;

            auditLog.logException("processEvent",
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }

    }
}