/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.event;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            if (eventObject != null){
                Map<String, String> views = viewGeneratorConnector.processInformationViewEvent(eventObject);
                List<InformationViewEvent> viewEvents = generateViewEvents(eventObject, views);
                for (InformationViewEvent item : viewEvents){
                    ivInTopicConnector.sendEvent(objectMapper.writeValueAsString(item));
                }
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
    public List<InformationViewEvent> generateViewEvents(TableContextEvent tableContextEvent, Map<String, String> createdViews) {
        List<InformationViewEvent> events = new ArrayList<>();
        try {
            if (createdViews == null || createdViews.isEmpty()) {
                log.info("No views were created, nothing to publish");
            } else {
                for (Map.Entry<String, String> entry : createdViews.entrySet()) {
                    if (entry.getValue() == null) {
                        log.info("No view was created for " + entry.getKey());
                    } else {
                        events.add(createInformationViewEvent(entry.getKey(), entry.getValue(), tableContextEvent));
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


    private InformationViewEvent createInformationViewEvent(String viewType, String tableName, TableContextEvent tableContextEvent) {

        InformationViewEvent view = addConnectionDetailsAndTableContext(new InformationViewEvent(), tableName);
        view.setOriginalTableSource(tableContextEvent.getTableSource());
        List<DerivedColumn> derivedColumn = new ArrayList<>();
        for (TableColumn databaseColumn : tableContextEvent.getTableColumns()) {
            if (databaseColumn.getBusinessTerm() != null) {
                DerivedColumn column = new DerivedColumn();
                if (viewType.equals(ConnectorUtils.BUSINESS_PREFIX)) {
                    //column.setName(databaseColumn.getBusinessTerm().getName());
                    column.setName(databaseColumn.getBusinessTerm().getName().replace(" ", "_"));
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
        DatabaseSource databaseSource = new DatabaseSource();
        tableSource.setDatabaseSource(databaseSource);
        databaseSource.setEndpointSource(endpointSource);
        tableSource.setName(tableName);
        databaseSource.setName(databaseName);
        tableSource.setSchemaName(dataSchema);
        informationViewEvent.setTableSource(tableSource);
        return informationViewEvent;
    }

    /**
     * send the notification to Information View OMAS
     *
     * @param views view to be sent to information view omas
     */

}
