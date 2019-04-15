/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.informationview.views.ViewHandler;
import org.odpi.openmetadata.accessservices.informationview.views.beans.InformationViewAsset;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.views.InformationViewAssetHandler;
import org.odpi.openmetadata.accessservices.informationview.eventprocessor.EventPublisher;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.views.beans.View;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InformationViewInTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(InformationViewInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMEntityDao omEntityDao;
    private final OMRSAuditLog auditLog;
    private EventPublisher eventPublisher;
    private OMRSRepositoryHelper helper;
    private List<String> supportedZones;

    public InformationViewInTopicListener(OMEntityDao omEntityDao, EventPublisher eventPublisher, OMRSRepositoryHelper helper, List<String> supportedZones, OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.eventPublisher =  eventPublisher;
        this.supportedZones = supportedZones;
        this.helper = helper;
    }

    /**
     * @param eventAsString contains all the information needed to build information view like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        InformationViewEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, InformationViewEvent.class);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.PARSE_EVENT;

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
                log.info("Started processing InformationViewEvent");
                InformationViewAssetHandler informationViewAssetHandler = new InformationViewAssetHandler(event, omEntityDao);
                ViewHandler viewsBuilder = new ViewHandler(event, omEntityDao, helper);
                ExecutorService executor = Executors.newCachedThreadPool();
                Future<InformationViewAsset> informationViewFuture = executor.submit(informationViewAssetHandler);
                Future<View> viewCreationFuture = executor.submit(viewsBuilder);
                InformationViewAsset informationViewAsset = informationViewFuture.get();
                View view = viewCreationFuture.get();
                executor.shutdown();
                if(view.getViewEntity() != null) {
                    omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                            informationViewAsset.getRelationalDbSchemaType().getGUID(),
                            view.getViewEntity().getGUID(),
                            new InstanceProperties());
                    event.getTableSource().setGuid(view.getViewEntity().getGUID());
                }
                eventPublisher.sendEvent(event);
            } catch (Exception e) {
                log.error("Exception processing event from in topic", e);
                InformationViewErrorCode auditCode = InformationViewErrorCode.PROCESS_EVENT_EXCEPTION;

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
