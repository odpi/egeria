/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserErrorCode;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.GaianQueryConstructor;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka.KafkaVirtualiserProducer;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnDetails;
import org.odpi.openmetadata.accessservices.informationview.events.ConnectionDetails;
import org.odpi.openmetadata.accessservices.informationview.events.DerivedColumnDetail;
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
     * @param columnContextEvent it contains the information from json which is sent from Information View
     * @param  createdViews the table definitions created in gaian
     *
     * @return list of events published to kafka topic
     */
    public List<InformationViewEvent> notifyIVOMAS(ColumnContextEvent columnContextEvent, Map<String, String> createdViews) {
        List<InformationViewEvent> events = new ArrayList<>();
        try {
            if (createdViews == null || createdViews.isEmpty()) {
                log.info("No views were created in Gaian, nothing to publish");
            } else {
                for (Map.Entry<String, String> entry : createdViews.entrySet()) {
                    if (entry.getValue() == null) {
                        log.info("No view was created for " + entry.getKey());
                    } else {
                        events.add(createInformationViewEvent(entry.getKey(), entry.getValue(), columnContextEvent));
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
     * @param columnContextEvent it contains the information from json which is sent from Information View
     * @return two string which are jsons for technical view and business view
     */


    private InformationViewEvent createInformationViewEvent(String viewType, String tableName, ColumnContextEvent columnContextEvent) {

        InformationViewEvent view = addConnectionDetailsAndTableContext(new InformationViewEvent(), tableName);

        List<DerivedColumnDetail> derivedColumn = new ArrayList<>();
        for (ColumnDetails columnDetails : columnContextEvent.getTableColumns()) {
            if (columnDetails.getBusinessTerm() != null) {
                DerivedColumnDetail column = new DerivedColumnDetail();
                if (viewType.equals(GaianQueryConstructor.BUSINESS_PREFIX)) {
                    column.setAttributeName(columnDetails.getBusinessTerm().getName());
                } else {
                    column.setAttributeName(columnDetails.getAttributeName());
                }
                column.setPosition(columnDetails.getPosition());
                column.setType(columnDetails.getType());
                column.setRealColumn(columnDetails);
                derivedColumn.add(column);
            }
        }

        view.setDerivedColumns(derivedColumn);

        return view;
    }

    private InformationViewEvent addConnectionDetailsAndTableContext(InformationViewEvent informationViewEvent, String tableName) {

        ConnectionDetails connectionDetails = new ConnectionDetails();
        connectionDetails.setConnectorProviderQualifiedName(connectorProviderName);
        int lastIndexOf = connectorProviderName.lastIndexOf(".");
        connectionDetails.setConnectorProviderName(connectorProviderName.substring(lastIndexOf + 1, connectorProviderName.length()));
        connectionDetails.setProtocol(gaianUrlPrefix);
        connectionDetails.setNetworkAddress(gaianServer + ":" + gaianPort);
        connectionDetails.setEndpointQualifiedName(connectionDetails.getNetworkAddress());
        connectionDetails.setConnectionQualifiedName(connectionDetails.getEndpointQualifiedName() + ".Connection.");//TODO
        connectionDetails.setUser(gaianProxyUsername);
        informationViewEvent.setConnectionDetails(connectionDetails);
        informationViewEvent.getTableContext().setTableName(tableName);
        informationViewEvent.getTableContext().setDatabaseName(gaianDb);
        informationViewEvent.getTableContext().setSchemaName(gaianSchema);
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
