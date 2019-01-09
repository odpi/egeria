/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.informationview.events.DerivedColumn;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserErrorCode;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.GaianQueryConstructor;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka.KafkaVirtualiserProducer;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumn;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ViewsConstructor is used to create technical and business views for Information View OMAS. And
 * two json files will be sent out to Information View In topic.
 */
@Service
public class ViewsConstructor {
    private static final Logger log = LoggerFactory.getLogger(ViewsConstructor.class);

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${gaian_url_prefix}")
    private String gaianUrlPrefix;

    @Value("${gaian_server}")
    private String gaianServer;

    @Value("${gaian_port}")
    private String gaianPort;

    @Value("${gaian_db}")
    private String gaianDb;

    @Value("${gaian_schema}")
    private String gaianSchema;

    @Value("${gaian_proxy_username}")
    private String gaianProxyUsername;

    @Value("${gdb_node}")
    private String gdbNode;

    @Value("${connector_provider_name}")
    private String connectorProviderName;

    @Autowired
    private KafkaVirtualiserProducer kafkaVirtualiserProducer;


    /**
     * notify Information View OMAS
     *
     * @param tableContextEvent it contains the information from json which is sent from Information View
     * @param  createdViews the table definitions created in gaian
     *
     * @return list of events published to kafka topic
     */
    public List<InformationViewEvent> notifyIVOMAS(TableContextEvent tableContextEvent, Map<String, String> createdViews) {
        List<InformationViewEvent> events = new ArrayList<>();
        try {
            if (createdViews == null || createdViews.isEmpty()) {
                log.info("No views were created in Gaian, nothing to publish");
            } else {
                for (Map.Entry<String, String> entry : createdViews.entrySet()) {
                    if (entry.getValue() == null) {
                        log.info("No view was created for " + entry.getKey());
                    } else {
                        events.add(createInformationViewEvent(entry.getKey(), entry.getValue(), tableContextEvent));
                    }
                }
                sendNotification(events);
                log.debug("Notification is sent out to Information View OMAS");
            }
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to create views or notify Information View OMAS.", e);

        }
        return events;
    }

    /**
     * create technical view and technical view
     *
     * @param tableContextEvent it contains the information from json which is sent from Information View
     * @return two string which are jsons for technical view and business view
     */


    private InformationViewEvent createInformationViewEvent(String viewType, String tableName, TableContextEvent tableContextEvent) {

        InformationViewEvent view = addConnectionDetailsAndTableContext(new InformationViewEvent(), tableName);

        List<DerivedColumn> derivedColumn = new ArrayList<>();
        for (DatabaseColumn databaseColumn : tableContextEvent.getTableColumns()) {
            if (databaseColumn.getBusinessTerm() != null) {
                DerivedColumn column = new DerivedColumn();
                if (viewType.equals(GaianQueryConstructor.BUSINESS_PREFIX)) {
                    column.setName(databaseColumn.getBusinessTerm().getName());
                } else {
                    column.setName(databaseColumn.getName());
                }
                column.setPosition(databaseColumn.getPosition());
                column.setType(databaseColumn.getType());
                column.setSourceColumn(databaseColumn);
                derivedColumn.add(column);
            }
        }

        view.setDerivedColumns(derivedColumn);

        return view;
    }

    private InformationViewEvent addConnectionDetailsAndTableContext(InformationViewEvent informationViewEvent, String tableName) {

        TableSource tableSource = new TableSource();
        int lastIndexOf = connectorProviderName.lastIndexOf(".");
        tableSource.setConnectorProviderName(connectorProviderName.substring(lastIndexOf + 1, connectorProviderName.length()));
        tableSource.setProtocol(gaianUrlPrefix);
        tableSource.setNetworkAddress(gaianServer + ":" + gaianPort);
        tableSource.setUser(gaianProxyUsername);
        tableSource.setTableName(tableName);
        tableSource.setDatabaseName(gaianDb);
        tableSource.setSchemaName(gaianSchema);
        informationViewEvent.setTableSource(tableSource);
        return informationViewEvent;
    }

    /**
     * send the notification to Information View OMAS
     *
     * @param views view to be sent to information view omas
     */
    private void sendNotification(List<InformationViewEvent> views) throws VirtualiserCheckedException {
        final String methodName = "sendNotification";
        try {
            for (InformationViewEvent event : views) {
                kafkaVirtualiserProducer.runProducer(OBJECT_MAPPER.writeValueAsString(event));
            }
            kafkaVirtualiserProducer.closeProducer();

        } catch (JsonProcessingException e) {
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.JACKSON_PARSE_FAIL;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);


            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }


    }


}
