/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.List;


public class ReportCreator {

    private static final Logger log = LoggerFactory.getLogger(ReportCreator.class);
    private final EntitiesCreatorHelper entitiesCreatorHelper;
    private final LookupHelper lookupHelper;
    private final OMRSAuditLog auditLog;


    public ReportCreator(EntitiesCreatorHelper entitiesCreatorHelper, LookupHelper lookupHelper, OMRSAuditLog auditLog) {
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        this.lookupHelper = lookupHelper;
        this.auditLog = auditLog;
    }

    public void createReportModel(ReportRequestBody payload) throws ReportCreationException {

        try {
            log.info("Creating report based on payload {}", payload);
            URL url = new URL(payload.getReportUrl());
            String networkAddress = url.getHost();
            if (url.getPort() > 0) {
                networkAddress = networkAddress + ":" + url.getPort();
            }

            String qualifiedNameForReport = networkAddress + "." + payload.getId();
            InstanceProperties reportProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForReport)
                    .withStringProperty(Constants.NAME, payload.getReportName())
                    .withStringProperty(Constants.AUTHOR, payload.getAuthor())
                    .withStringProperty(Constants.ID, payload.getId())
                    .withStringProperty(Constants.URL, payload.getReportUrl())
                    .withStringProperty(Constants.LAST_MODIFIER, payload.getLastModifier())
                    .withDateProperty(Constants.LAST_MODIFIED_TIME, payload.getLastModifiedTime())
                    .withDateProperty(Constants.CREATE_TIME, payload.getCreatedTime())
                    .build();
            EntityDetail report = entitiesCreatorHelper.addEntity(Constants.DEPLOYED_REPORT,
                    qualifiedNameForReport, reportProperties, null);


            String qualifiedNameForComplexSchemaType = qualifiedNameForReport + Constants.TYPE_SUFFIX;
            InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                    .build();
            EntityDetail complexSchemaTypeEntity = entitiesCreatorHelper.addEntity(Constants.COMPLEX_SCHEMA_TYPE,
                    qualifiedNameForComplexSchemaType, complexSchemaTypeProperties, null);

            entitiesCreatorHelper.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                    report.getGUID(),
                    complexSchemaTypeEntity.getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());

            addElements(qualifiedNameForReport, complexSchemaTypeEntity, payload.getReportElements());

        } catch (Exception e) {
            log.error("Exception creating report", e);
            InformationViewErrorCode auditCode = InformationViewErrorCode.REPORT_CREATION_EXCEPTION;


            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getErrorMessage(),
                    "json {" + payload + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new ReportCreationException(404,
                    "ReportCreator",
                    "createReport",
                    "Unable to create report: " + e.getMessage(),
                    "The system is unable to process the request.",
                    "Correct the payload submitted to request.",
                    "");//TODO extract to code exception class

        }

    }

    private void addElements(String qualifiedNameForParent, EntityDetail parentSchemaTypeEntity, List<ReportElement> allElements) {
        if (allElements == null || allElements.isEmpty())
            return;
        allElements.stream().forEach(e -> addReportElement(qualifiedNameForParent, parentSchemaTypeEntity, e));
    }

    private void addReportElement(String qualifiedNameForParent, EntityDetail parentSchemaTypeEntity, ReportElement element) {
        try {
            if (element instanceof ReportSection) {
                addReportSection(qualifiedNameForParent, parentSchemaTypeEntity, (ReportSection) element);
            } else if (element instanceof ReportColumn) {
                ReportColumn reportColumn = (ReportColumn) element;
                addDerivedRelationalColumn(parentSchemaTypeEntity, qualifiedNameForParent, reportColumn);
            }//TODO refactor if else if
        } catch (Exception e) {
            log.error("Exception creating report element", e);
            throw new RuntimeException(e);//TODO throw specific exception
        }
    }

    private void addReportSection(String qualifiedNameForParent, EntityDetail parentSchemaTypeEntity, ReportSection element) throws Exception {
        ReportSection reportSection = element;
        String qualifiedNameForType = qualifiedNameForParent + "." + reportSection.getName() + Constants.TYPE_SUFFIX;
        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForType)
                .build();
        EntityDetail typeEntity = entitiesCreatorHelper.addEntity(Constants.DOCUMENT_SCHEMA_TYPE,
                qualifiedNameForType,
                typeProperties);

        String qualifiedNameForSection = qualifiedNameForParent + "." + reportSection.getName();
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSection)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportSection.getName())
                .build();
        EntityDetail sectionEntity = entitiesCreatorHelper.addEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE,
                qualifiedNameForSection,
                sectionProperties);

        entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                sectionEntity.getGUID(),
                typeEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());
        entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                parentSchemaTypeEntity.getGUID(),
                sectionEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());

        addElements(qualifiedNameForSection, typeEntity, reportSection.getElements());
    }


    private void addDerivedRelationalColumn(EntityDetail parentEntity, String parentQualifiedName, ReportColumn reportColumn) throws Exception {

        String qualifiedNameForColumn = parentQualifiedName + "." + reportColumn.getName();

        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
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


        if (reportColumn.getBusinessTerm() != null) {
            String businessTermGuid = reportColumn.getBusinessTerm().getGuid();
            if (StringUtils.isEmpty(businessTermGuid)) {
                EntityDetail businessTerm = entitiesCreatorHelper.getEntity(Constants.BUSINESS_TERM, reportColumn.getBusinessTerm().buildQualifiedName());
                if (businessTerm != null) {
                    businessTermGuid = businessTerm.getGUID();
                }
            }

            if (!StringUtils.isEmpty(businessTermGuid)) {
                entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                        derivedColumnEntity.getGUID(),
                        businessTermGuid,
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());
            } else {
                log.error("business term associated to the report column not found, unable to add relationship " + reportColumn.getName());
            }
        }

        for (Source source : reportColumn.getSources()) {

            String sourceColumnGUID = getSourceGuid(source);
            if (!StringUtils.isEmpty(sourceColumnGUID)) {
                log.info("source {} for report column {} found.", source, reportColumn.getName());

                InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUERY, "")
                        .build();
                entitiesCreatorHelper.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                        derivedColumnEntity.getGUID(),
                        sourceColumnGUID,
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        schemaQueryImplProperties);

            } else {
                log.error("source column not found, unable to add relationships for column " + source.toString());
            }
        }
    }

    private String getSourceGuid(Source source) throws Exception {

        if (!StringUtils.isEmpty(source.getGuid())) {
            return source.getGuid();
        }
        EntityDetail sourceColumn = null;
        if (source instanceof DatabaseColumnSource) {
            sourceColumn = lookupHelper.lookupDatabaseColumn((DatabaseColumnSource) source);
        } else if (source instanceof ReportColumnSource) {
            sourceColumn = entitiesCreatorHelper.getEntity(Constants.SCHEMA_ATTRIBUTE, source.buildQualifiedName());
        }
        if (source != null) {
            return sourceColumn.getGUID();
        }
        return null;
    }

}
