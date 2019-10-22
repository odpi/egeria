/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.events.NewViewEvent;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.utils.ConnectorUtils;
import org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.ViewGeneratorConnectorBase;
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

    private OpenMetadataTopicConnector virtualizerOutboundTopicConnector;
    private ViewGeneratorConnectorBase viewGeneratorConnector;
    private EndpointSource endpointSource;
    private String databaseName;
    private String dataSchema;


    /**
     * Default constructor.
     * @param virtualizerOutboundTopicConnector the event connector to sent out the processed topic
     */
    public VirtualizerTopicListener(OpenMetadataTopicConnector virtualizerOutboundTopicConnector,
                                    ViewGeneratorConnectorBase viewGeneratorConnector,
                                    EndpointSource endpointSource,
                                    String databaseName,
                                    String dataSchema){
        this.virtualizerOutboundTopicConnector = virtualizerOutboundTopicConnector;
        this.viewGeneratorConnector            = viewGeneratorConnector;
        this.endpointSource                    = endpointSource;
        this.databaseName                      = databaseName;
        this.dataSchema                        = dataSchema;

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
            TableContextEvent eventObject;
            try {
                eventObject = objectMapper.readValue(event, TableContextEvent.class);
            } catch (Exception e){
                log.info("An event is not Table Context Event, discarded!");
                eventObject = null;
            }
            if (eventObject != null){
                Map<String, String> views = viewGeneratorConnector.processInformationViewEvent(eventObject);
                List<NewViewEvent> viewEvents = generateViewEvents(eventObject, views);
                for (NewViewEvent item : viewEvents){
                    virtualizerOutboundTopicConnector.sendEvent(objectMapper.writeValueAsString(item));
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
    public List<NewViewEvent> generateViewEvents(TableContextEvent tableContextEvent, Map<String, String> createdViews) {
        List<NewViewEvent> events = new ArrayList<>();
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


    private NewViewEvent createDataPlatformEvent(String viewType, String tableName, TableContextEvent tableContextEvent) {
        NewViewEvent view = addConnectionDetailsAndTableContext(new NewViewEvent(), tableName);
        view.setOriginalTableSource(convertTableSource(tableContextEvent.getTableSource()));
        List<org.odpi.openmetadata.accessservices.dataplatform.properties.DerivedColumn> derivedColumn = new ArrayList<>();
        for (TableColumn databaseColumn : tableContextEvent.getTableColumns()) {
            if (databaseColumn.getBusinessTerms() != null && !databaseColumn.getBusinessTerms().isEmpty()) {
                org.odpi.openmetadata.accessservices.dataplatform.properties.DerivedColumn column = new org.odpi.openmetadata.accessservices.dataplatform.properties.DerivedColumn();
                if (viewType.equals(ConnectorUtils.BUSINESS_PREFIX)) {
                    column.setName(databaseColumn.getBusinessTerms().get(0).getName().replace(" ", "_"));//TODO logic for handling multiple business terms
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

    private List<org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm> convertBusinessTerm(List<BusinessTerm> businessTerms) {
        if(businessTerms != null && !businessTerms.isEmpty()) {
            List<org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm> businessTermList= new ArrayList<>();

            for (org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm businessTerm : businessTerms)
            {
                org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm businessTermCount = new org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm();
                businessTermCount.setAbbreviation(businessTerm.getAbbreviation());
                businessTermCount.setDescription(businessTerm.getDescription());
                businessTermCount.setDisplayName(businessTerm.getDisplayName());
                businessTermCount.setExamples(businessTerm.getExamples());
                businessTermCount.setGuid(businessTerm.getGuid());
                businessTermCount.setQuery(businessTerm.getQuery());
                businessTermCount.setName(businessTerm.getName());
                businessTermList.add(businessTermCount);
            }
            return businessTermList;
        }
        return null;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.properties.TableColumn convertSourceColumn(TableColumn databaseColumn) {
        org.odpi.openmetadata.accessservices.dataplatform.properties.TableColumn tableColumn = new org.odpi.openmetadata.accessservices.dataplatform.properties.TableColumn();
        tableColumn.setName(databaseColumn.getName());
        tableColumn.setCardinality(databaseColumn.getCardinality());
        tableColumn.setGuid(databaseColumn.getGuid());
        tableColumn.setPosition(databaseColumn.getPosition());
        tableColumn.setPrimaryKey(databaseColumn.isPrimaryKey());
        tableColumn.setDefaultValueOverride(databaseColumn.getDefaultValueOverride());
        tableColumn.setNullable(databaseColumn.isNullable());
        tableColumn.setQualifiedName(databaseColumn.getQualifiedName());
        tableColumn.setType(databaseColumn.getType());
        tableColumn.setUnique(databaseColumn.isUnique());
        tableColumn.setBusinessTerms(convertBusinessTerm(databaseColumn.getBusinessTerms()));
        return tableColumn;
    }

    private NewViewEvent addConnectionDetailsAndTableContext(NewViewEvent informationViewEvent, String tableName) {
        org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource tableSource = new org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource();
        org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource databaseSource = new org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource();
        tableSource.setDatabaseSource(databaseSource);
        org.odpi.openmetadata.accessservices.dataplatform.properties.EndpointSource endpointSource1 = convertEndpointSource(endpointSource);
        databaseSource.setEndpointSource(endpointSource1);
        tableSource.setName(tableName);
        databaseSource.setName(databaseName);
        tableSource.setSchemaName(dataSchema);
        informationViewEvent.setTableSource(tableSource);
        return informationViewEvent;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource convertTableSource(TableSource tableSource) {
        org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource tableSource1 = new org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource();
        tableSource1.setName(tableSource.getName());
        tableSource1.setSchemaName(tableSource.getSchemaName());
        tableSource1.setGuid(tableSource.getGuid());
        tableSource1.setQualifiedName(tableSource.getQualifiedName());
        tableSource1.setGuid(tableSource.getGuid());
        tableSource1.setAdditionalProperties(tableSource.getAdditionalProperties());
        tableSource1.setDatabaseSource(convertDatabaseSource(tableSource.getDatabaseSource()));
        return tableSource1;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource convertDatabaseSource(DatabaseSource databaseSource) {
        org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource databaseSource1 = new org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource();
        databaseSource1.setName(databaseSource.getName());
        databaseSource1.setGuid(databaseSource.getGuid());
        databaseSource1.setQualifiedName(databaseSource.getQualifiedName());
        databaseSource1.setAdditionalProperties(databaseSource.getAdditionalProperties());
        databaseSource1.setEndpointSource(convertEndpointSource(databaseSource.getEndpointSource()));
        return databaseSource1;
    }

    private org.odpi.openmetadata.accessservices.dataplatform.properties.EndpointSource convertEndpointSource(EndpointSource endpointSource) {
        org.odpi.openmetadata.accessservices.dataplatform.properties.EndpointSource endpointSource1 = new org.odpi.openmetadata.accessservices.dataplatform.properties.EndpointSource();
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
