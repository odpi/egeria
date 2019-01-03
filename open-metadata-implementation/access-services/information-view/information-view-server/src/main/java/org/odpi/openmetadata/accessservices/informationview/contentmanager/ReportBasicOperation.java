/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.List;

public abstract class ReportBasicOperation {

    private static final Logger log = LoggerFactory.getLogger(ReportBasicOperation.class);
    protected final EntitiesCreatorHelper entitiesCreatorHelper;
    protected final LookupHelper lookupHelper;
    protected final OMRSAuditLog auditLog;

    public ReportBasicOperation(EntitiesCreatorHelper entitiesCreatorHelper, LookupHelper lookupHelper, OMRSAuditLog auditLog) {
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        this.lookupHelper = lookupHelper;
        this.auditLog = auditLog;
    }


    public void addElements(String qualifiedNameForParent, String parentGuid, List<ReportElement> allElements) {
        if (allElements == null || allElements.isEmpty())
            return;
        allElements.stream().forEach(e -> addReportElement(qualifiedNameForParent, parentGuid, e));
    }


    public void addReportElement(String qualifiedNameForParent, String parentGuid, ReportElement element) {
        try {
            if (element instanceof ReportSection) {
                addReportSection(qualifiedNameForParent, parentGuid, (ReportSection) element);
            } else if (element instanceof ReportColumn) {
                addReportColumn(qualifiedNameForParent, parentGuid, (ReportColumn) element);
            }
        } catch (Exception e) {
            log.error("Exception creating report element", e);
            throw new RuntimeException("Unable to create Report Element", e);//TODO throw specific exception
        }
    }

    private void addReportSection(String qualifiedNameForParent, String parentGuid, ReportSection reportSection) throws InvalidParameterException, PropertyErrorException, TypeDefNotKnownException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {
        EntityDetail typeEntity = addSectionAndSectionType(qualifiedNameForParent, parentGuid, reportSection);
        String qualifiedNameForSection = EntityPropertiesUtils.getStringValueForProperty(typeEntity.getProperties(), Constants.QUALIFIED_NAME);
        qualifiedNameForSection = qualifiedNameForSection.substring(0, qualifiedNameForSection.lastIndexOf(Constants.TYPE_SUFFIX));
        addElements(qualifiedNameForSection, typeEntity.getGUID(), reportSection.getElements());
    }


    protected EntityDetail addSectionAndSectionType(String qualifiedNameForParent, String parentGuid, ReportSection reportSection) throws InvalidParameterException, StatusNotSupportedException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, TypeDefNotKnownException {

        String qualifiedNameForSection = qualifiedNameForParent + "." + reportSection.getName();
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSection)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportSection.getName())
                .build();
        EntityDetail sectionEntity = entitiesCreatorHelper.addEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE,
                                                                    qualifiedNameForSection,
                                                                    sectionProperties);


        entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                                            parentGuid,
                                            sectionEntity.getGUID(),
                                            Constants.INFORMATION_VIEW_OMAS_NAME,
                                            new InstanceProperties());


        return addSchemaType(qualifiedNameForSection, sectionEntity, Constants.DOCUMENT_SCHEMA_TYPE);
    }


    protected EntityDetail addReportColumn(String parentQualifiedName, String parentGuid, ReportColumn reportColumn) throws InvalidParameterException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException, StatusNotSupportedException {

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
                parentGuid,
                derivedColumnEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());

        addBusinessTerm(reportColumn, derivedColumnEntity);
        addQueryTargets(reportColumn, derivedColumnEntity);
        addSchemaType(qualifiedNameForColumn, derivedColumnEntity, Constants.SCHEMA_TYPE);

        return derivedColumnEntity;
    }

    protected EntityDetail addSchemaType(String qualifiedNameOfSchemaAttribute, EntityDetail schemaAttributeEntity, String schemaAttributeType) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException {
        String qualifiedNameForType = qualifiedNameOfSchemaAttribute + Constants.TYPE_SUFFIX;

        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                                                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForType)
                                                .build();

        EntityDetail schemaTypeEntity = entitiesCreatorHelper.addEntity(schemaAttributeType,
                                                                        qualifiedNameForType,
                                                                        typeProperties);

        entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                                            schemaAttributeEntity.getGUID(),
                                            schemaTypeEntity.getGUID(),
                                            Constants.INFORMATION_VIEW_OMAS_NAME,
                                            new InstanceProperties());
        return schemaTypeEntity;
    }

    private void addBusinessTerm(ReportColumn reportColumn, EntityDetail derivedColumnEntity) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, StatusNotSupportedException, TypeDefNotKnownException, EntityNotKnownException {
        String businessTermGuid = findAssignedBusinessTermGuid(reportColumn);
            if (!StringUtils.isEmpty(businessTermGuid)) {
                entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                        derivedColumnEntity.getGUID(),
                        businessTermGuid,
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());
            }
    }

    private void addQueryTargets(ReportColumn reportColumn, EntityDetail derivedColumnEntity) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException {
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
                log.error( MessageFormat.format("source column not found, unable to add relationship between column {0} and source {1}", reportColumn.getName(), source.toString()));
            }
        }
    }


    protected EntityDetail addReportSchemaType(EntityDetail reportEntity, String qualifiedNameForComplexSchemaType, InstanceProperties complexSchemaTypeProperties) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException {
        EntityDetail complexSchemaTypeEntity;
        complexSchemaTypeEntity = entitiesCreatorHelper.addEntity(Constants.COMPLEX_SCHEMA_TYPE,
                                                                  qualifiedNameForComplexSchemaType,
                                                                  complexSchemaTypeProperties,
                                                       null);

        entitiesCreatorHelper.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                                                reportEntity.getGUID(),
                                                complexSchemaTypeEntity.getGUID(),
                                                Constants.INFORMATION_VIEW_OMAS_NAME,
                                                new InstanceProperties());
        return complexSchemaTypeEntity;
    }


    protected String getSourceGuid(Source source) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        if (!StringUtils.isEmpty(source.getGuid())) {
            return source.getGuid();
        }
        EntityDetail sourceColumn = null;
        if (source instanceof DatabaseColumnSource) {
            sourceColumn = lookupHelper.lookupDatabaseColumn((DatabaseColumnSource) source);
        } else if (source instanceof ReportColumnSource) {
            sourceColumn = entitiesCreatorHelper.getEntity(Constants.SCHEMA_ATTRIBUTE, source.buildQualifiedName());
        }
        if (sourceColumn != null) {
            return sourceColumn.getGUID();
        }
        return null;
    }


    protected void deleteRelationships(List<Relationship> existingAssignments) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, RelationshipNotDeletedException, RelationshipNotKnownException, FunctionNotSupportedException {
        if (existingAssignments != null && !existingAssignments.isEmpty()) {
            for (Relationship relationship : existingAssignments) {
                entitiesCreatorHelper.purgeRelationship(relationship);
            }
        } else {
            log.info("No relationships to delete");
        }
    }

    protected String findAssignedBusinessTermGuid(ReportColumn reportColumn) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        if (reportColumn.getBusinessTerm() == null) {
           log.info("no business term is associated to the report column {0}", reportColumn.getName());
            return null;
        }
        String businessTermGuid = reportColumn.getBusinessTerm().getGuid();
        if (StringUtils.isEmpty(businessTermGuid)) {
            EntityDetail businessTerm = entitiesCreatorHelper.getEntity(Constants.BUSINESS_TERM, reportColumn.getBusinessTerm().buildQualifiedName());
            if (businessTerm != null) {
                businessTermGuid = businessTerm.getGUID();
            }else{
                log.error(MessageFormat.format("business term associated to the report column {0} not found", reportColumn.getName()));
            }
        }
        return businessTermGuid;
    }

}
