/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.ConnectionDetails;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnReference;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class ReportCreator {

    private static final Logger log = LoggerFactory.getLogger(ReportCreator.class);
    private EntitiesCreatorHelper entitiesCreatorHelper;


    public ReportCreator(EntitiesCreatorHelper entitiesCreatorHelper) {
        this.entitiesCreatorHelper = entitiesCreatorHelper;
    }

    public void createReportModel(ReportRequestBody payload) {

        try {
            log.info("Creating report based on payload {}", payload);
            URL url = new URL(payload.getReportUrl());
            String networkAddress = url.getHost();
            if (url.getPort() > 0) {
                networkAddress = networkAddress + ":" + url.getPort();
            }


            String qualifiedNameForEndpoint = networkAddress;
            InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                    .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                    .withStringProperty(Constants.NETWORK_ADDRESS, networkAddress)
                    .withStringProperty(Constants.PROTOCOL, url.getProtocol())
                    .build();
            EntityDetail endpointEntity = entitiesCreatorHelper.addEntity(Constants.ENDPOINT,
                    qualifiedNameForEndpoint,
                    endpointProperties);


            String qualifiedNameForConnection = qualifiedNameForEndpoint + ".Connection";
            InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                    .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                    .build();
            EntityDetail connectionEntity = entitiesCreatorHelper.addEntity(Constants.CONNECTION,
                    qualifiedNameForConnection, connectionProperties);


            entitiesCreatorHelper.addRelationship(Constants.CONNECTION_TO_ENDPOINT,
                    endpointEntity.getGUID(),
                    connectionEntity.getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());


            String qualifiedNameForReport = networkAddress + "." + payload.getId();
            InstanceProperties reportProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForReport)
                    .withStringProperty(Constants.NAME, payload.getReportName())
                    .withStringProperty(Constants.AUTHOR, payload.getAuthor())
                    .withStringProperty(Constants.ID, payload.getId())
                    .withStringProperty(Constants.URL, payload.getReportUrl())
                    .withStringProperty(Constants.LAST_MODIFIED_TIME, payload.getLastModifiedTime())
                    .withStringProperty(Constants.LAST_MODIFIER, payload.getLastModifier())
                    .withStringProperty(Constants.CREATE_TIME, payload.getCreatedTime())
                    .build();
            EntityDetail report = entitiesCreatorHelper.addEntity(Constants.DEPLOYED_REPORT,
                    qualifiedNameForReport, reportProperties, null);


            entitiesCreatorHelper.addRelationship(Constants.CONNECTION_TO_ASSET,
                    connectionEntity.getGUID(),
                    report.getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());


            String qualifiedNameForComplexSchemaType = qualifiedNameForReport + Constants.TYPE_SUFFIX;
            InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                    .build();
            EntityDetail complexSchemaType = entitiesCreatorHelper.addEntity(Constants.COMPLEX_SCHEMA_TYPE,
                    qualifiedNameForComplexSchemaType, complexSchemaTypeProperties, null);

            entitiesCreatorHelper.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                    report.getGUID(),
                    complexSchemaType.getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());

            addTables(qualifiedNameForComplexSchemaType, complexSchemaType, payload);


        } catch (Exception e) {
            log.error("Exception processing event REST call", e);
            InformationViewErrorCode auditCode = InformationViewErrorCode.PROCESS_EVENT_EXCEPTION;
        }

    }

    private void addTables(String qualifiedNameForComplexSchemaType, EntityDetail complexSchemaType, ReportRequestBody payload) throws Exception {

        Set<String> allSections = payload.getReportColumns().stream().map(e -> e.getSectionName()).collect(Collectors.toSet());

        for (String section : allSections) {

            String qualifiedNameForType = qualifiedNameForComplexSchemaType + "." + section + Constants.TYPE_SUFFIX;//TODO
            InstanceProperties typeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForType)
                    .build();
            EntityDetail typeEntity = entitiesCreatorHelper.addEntity(Constants.COMPLEX_SCHEMA_TYPE,
                    qualifiedNameForType,
                    typeProperties);

            String qualifiedNameForTable = qualifiedNameForComplexSchemaType + "." + section;
            InstanceProperties tableProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTable)
                    .withStringProperty(Constants.ATTRIBUTE_NAME, section)
                    .build();
            EntityDetail tableEntity = entitiesCreatorHelper.addEntity(Constants.SCHEMA_ATTRIBUTE,
                    qualifiedNameForTable,
                    tableProperties);

            entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                    tableEntity.getGUID(),
                    typeEntity.getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());
            entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                    complexSchemaType.getGUID(),
                    tableEntity.getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());

            List<ReportColumn> reportColumnList;
            reportColumnList = payload.getReportColumns().stream().filter(e -> e.getSectionName().equals(section)).collect(Collectors.toList());
            createDerivedRelationalColumn(typeEntity, qualifiedNameForTable, reportColumnList, payload.getSourceConnectionDetails(), payload.getSources());

        }


    }


    private void createDerivedRelationalColumn(EntityDetail parentEntity, String parentQualifiedName, List<ReportColumn> reportColumnList, ConnectionDetails sourceConnectionDetails, Map<String, Source> sources) throws Exception {
        for (ReportColumn reportColumn : reportColumnList) {

            EntityDetail realColumn = entitiesCreatorHelper.getEntity(Constants.RELATIONAL_COLUMN, getQualifiedNameForRealColumn(reportColumn.getRealColumn(), sourceConnectionDetails, sources));
            if (realColumn != null) {

                log.info("real database column found.");
                List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.SEMANTIC_ASSIGNMENT,
                        realColumn.getGUID());


                if (relationships != null && !relationships.isEmpty()) {
                    Relationship relationship = relationships.get(0);
                    EntityProxy btProxy;

                    if (realColumn.getGUID().equals(relationship.getEntityOneProxy().getGUID())) {
                        btProxy = relationship.getEntityTwoProxy();
                    } else {
                        btProxy = relationship.getEntityOneProxy();
                    }

                    EntityDetail btEntity = entitiesCreatorHelper.getEntity(btProxy.getGUID());

                    String businessTermNAme = EntityPropertiesUtils.getStringValueForProperty(btEntity.getProperties(), Constants.DISPLAY_NAME);
                    log.info("business term name:[] " + businessTermNAme);
                    String qualifiedNameForColumn = parentQualifiedName + "." + businessTermNAme;
                    InstanceProperties columnProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                            .withStringProperty(Constants.ATTRIBUTE_NAME, businessTermNAme)
                            .withStringProperty(Constants.FORMULA, reportColumn.getFormula())

                            .build();
                    EntityDetail derivedColumnEntity = entitiesCreatorHelper.addEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE,
                            qualifiedNameForColumn,
                            columnProperties);

                    entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                            parentEntity.getGUID(),
                            derivedColumnEntity.getGUID(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            new InstanceProperties());


                    //TODO here it could be more than one column, request body should change
                    InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUERY, "")
                            .build();
                    entitiesCreatorHelper.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                            derivedColumnEntity.getGUID(),
                            realColumn.getGUID(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            schemaQueryImplProperties);


                    entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                            derivedColumnEntity.getGUID(),
                            btProxy.getGUID(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            new InstanceProperties());
                }
            }
        }
    }

    private String getQualifiedNameForRealColumn(DatabaseColumnReference realColumn, ConnectionDetails sourceConnectionDetails, Map<String, Source> sources) {
        Source source = sources.get(realColumn.getSourceId());
        String qualifiedName = sourceConnectionDetails.getNetworkAddress() + "." + "Connection." + source.getDatabaseName() + "." + source.getSchemaName() + "." + source.getTableName() + ".";
        qualifiedName += realColumn.getName();

        return qualifiedName;
    }


}
