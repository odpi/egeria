/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEvent;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.utils.ConnectorUtils;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.ViewGeneratorConnectorBase;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is the event listener for the virtualizer. When the out topic of the IV OMAS is received,
 * the listener will consume the event and process correspondingly.
 */

public class VirtualizerTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(VirtualizerTopicListener.class);

    private OpenMetadataTopicConnector ivInTopicConnector;
    private ViewGeneratorConnectorBase viewGeneratorConnector;
    private EndpointSource endpointSource;
    private String databaseName;
    private String dataSchema;


    /**
     * Default constructor.
     * @param ivInTopicConnector the event connector to sent out the processed topic
     */
    public VirtualizerTopicListener(OpenMetadataTopicConnector ivInTopicConnector,
                                    ViewGeneratorConnectorBase viewGeneratorConnector,
                                    EndpointSource endpointSource,
                                    String databaseName,
                                    String dataSchema){
        this.ivInTopicConnector         = ivInTopicConnector;
        this.viewGeneratorConnector     = viewGeneratorConnector;
        this.endpointSource             = endpointSource;
        this.databaseName               = databaseName;
        this.dataSchema                 = dataSchema;

    }

    /**
     * Process the received event.
     * @param event inbound event
     */
    @Override
    public void processEvent (String event){
        log.info("The following event is received: " + event);
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            TableContextEvent eventObject = objectMapper.readValue(event, TableContextEvent.class);
            Map<String, String> views = viewGeneratorConnector.processInformationViewEvent(eventObject);
            List<DataPlatformEvent> viewEvents = generateViewEvents(eventObject, views);
            for (DataPlatformEvent item : viewEvents){
                ivInTopicConnector.sendEvent(objectMapper.writeValueAsString(item));
            }
        }catch (Exception e){
            log.error("Error in processing the event from Information View OMAS", e);
        }
    }

    /**
     * notify Information View OMAS
     *
     * @param tableContextEvent it contains the information from json which is sent from Information View
     * @param  createdViews the table definitions created in gaian
     *
     * @return list of events published to kafka topic
     */
    public List<DataPlatformEvent> generateViewEvents(TableContextEvent tableContextEvent, Map<String, String> createdViews) {
        List<DataPlatformEvent> events = new ArrayList<>();
        try {
            if (createdViews == null || createdViews.isEmpty()) {
                log.info("No views were created, nothing to publish");
            } else {
                for (Map.Entry<String, String> entry : createdViews.entrySet()) {
                    if (entry.getValue() == null) {
                        log.info("No view was created for " + entry.getKey());
                    } else {
                        events.add(createDataPlatformEvent(entry.getKey(), entry.getValue(), tableContextEvent));
                    }
                }
                log.debug("Notification is sent out to Information View OMAS");
            }
        } catch (Exception e) {
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


    private DataPlatformEvent createDataPlatformEvent(String viewType, String tableName, TableContextEvent tableContextEvent) {
        DataPlatformEvent view = addConnectionDetailsAndTableContext(new DataPlatformEvent(), tableName);
        view.setOriginalTableSource(convertTableSource(tableContextEvent.getTableSource()));
        List<org.odpi.openmetadata.accessservices.dataplatform.events.DerivedColumn> derivedColumn = new ArrayList<>();
        for (TableColumn databaseColumn : tableContextEvent.getTableColumns()) {
            if (databaseColumn.getBusinessTerm() != null) {
                org.odpi.openmetadata.accessservices.dataplatform.events.DerivedColumn column = new org.odpi.openmetadata.accessservices.dataplatform.events.DerivedColumn();
                if (viewType.equals(ConnectorUtils.BUSINESS_PREFIX)) {
                    //column.setName(databaseColumn.getBusinessTerm().getName());
                    column.setName(databaseColumn.getBusinessTerm().getName().replace(" ", "_"));
                } else {
                    column.setName(databaseColumn.getName());
                }
                column.setPosition(databaseColumn.getPosition());
                column.setType(databaseColumn.getType());
                column.setSourceColumn(convertSourceColumn(databaseColumn));
                derivedColumn.add(column);
            }
        }

        view.setDerivedColumns(derivedColumn);

        return view;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.events.TableColumn convertSourceColumn(TableColumn databaseColumn) {
        org.odpi.openmetadata.accessservices.dataplatform.events.TableColumn tableColumn = new org.odpi.openmetadata.accessservices.dataplatform.events.TableColumn();
        tableColumn.setName(databaseColumn.getName());
        tableColumn.setCardinality(databaseColumn.getCardinality());
        tableColumn.setGuid(databaseColumn.getGuid());
        tableColumn.setPosition(databaseColumn.getPosition());
        tableColumn.setPrimaryKey(databaseColumn.isPrimaryKey());
        tableColumn.setDefaultValueOverride(databaseColumn.getDefaultValueOverride());
        tableColumn.setDefaultValueOverride(databaseColumn.getDefaultValueOverride());
        tableColumn.setDefaultValueOverride(databaseColumn.getDefaultValueOverride());
        tableColumn.setDefaultValueOverride(databaseColumn.getDefaultValueOverride());
        return tableColumn;
    }

    private DataPlatformEvent addConnectionDetailsAndTableContext(DataPlatformEvent informationViewEvent, String tableName) {
        org.odpi.openmetadata.accessservices.dataplatform.events.TableSource tableSource = new org.odpi.openmetadata.accessservices.dataplatform.events.TableSource();
        org.odpi.openmetadata.accessservices.dataplatform.events.DatabaseSource databaseSource = new org.odpi.openmetadata.accessservices.dataplatform.events.DatabaseSource();
        tableSource.setDatabaseSource(databaseSource);
        org.odpi.openmetadata.accessservices.dataplatform.events.EndpointSource endpointSource1 = convertEndpointSource(endpointSource);
        databaseSource.setEndpointSource(endpointSource1);
        tableSource.setName(tableName);
        databaseSource.setName(databaseName);
        tableSource.setSchemaName(dataSchema);
        informationViewEvent.setTableSource(tableSource);
        return informationViewEvent;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.events.TableSource convertTableSource(TableSource tableSource) {
        org.odpi.openmetadata.accessservices.dataplatform.events.TableSource tableSource1 = new org.odpi.openmetadata.accessservices.dataplatform.events.TableSource();
        tableSource1.setName(tableSource.getName());
        tableSource1.setSchemaName(tableSource.getSchemaName());
        tableSource1.setGuid(tableSource.getGuid());
        tableSource1.setQualifiedName(tableSource.getQualifiedName());
        tableSource1.setGuid(tableSource.getGuid());
        tableSource1.setAdditionalProperties(tableSource.getAdditionalProperties());
        tableSource1.setDatabaseSource(convertDatabaseSource(tableSource.getDatabaseSource()));
        return tableSource1;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.events.DatabaseSource convertDatabaseSource(DatabaseSource databaseSource) {
        org.odpi.openmetadata.accessservices.dataplatform.events.DatabaseSource databaseSource1 = new org.odpi.openmetadata.accessservices.dataplatform.events.DatabaseSource();
        databaseSource1.setName(databaseSource.getName());
        databaseSource1.setGuid(databaseSource.getGuid());
        databaseSource1.setQualifiedName(databaseSource.getQualifiedName());
        databaseSource1.setAdditionalProperties(databaseSource.getAdditionalProperties());
        databaseSource1.setEndpointSource(convertEndpointSource(databaseSource.getEndpointSource()));
        return databaseSource1;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.events.EndpointSource convertEndpointSource(EndpointSource endpointSource) {
        org.odpi.openmetadata.accessservices.dataplatform.events.EndpointSource endpointSource1 = new org.odpi.openmetadata.accessservices.dataplatform.events.EndpointSource();
        endpointSource1.setConnectorProviderName(endpointSource.getConnectorProviderName());
        endpointSource1.setNetworkAddress(endpointSource.getNetworkAddress());
        endpointSource1.setProtocol(endpointSource.getProtocol());
        endpointSource1.setUser(endpointSource.getUser());
        endpointSource1.setAdditionalProperties(endpointSource.getAdditionalProperties());
        endpointSource1.setGuid(endpointSource.getGuid());
        endpointSource1.setQualifiedName(endpointSource.getQualifiedName());
        return endpointSource1;
    }


}
