/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportUpdater extends ReportBasicOperation {


    private static final Logger log = LoggerFactory.getLogger(ReportUpdater.class);

    public ReportUpdater(EntitiesCreatorHelper entitiesCreatorHelper, LookupHelper lookupHelper, OMRSAuditLog auditLog) {
        super(entitiesCreatorHelper, lookupHelper, auditLog);
    }

    public void updateReport(ReportRequestBody payload, EntityDetail reportEntity) throws Exception {
        String qualifiedNameForComplexSchemaType = EntityPropertiesUtils.getStringValueForProperty(reportEntity.getProperties(), Constants.QUALIFIED_NAME) + Constants.TYPE_SUFFIX;

        List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportEntity.getGUID());
        //ASSET Schema type relationship can have 1 at max for report
        Relationship schemaTypeRelationship = relationships != null && !relationships.isEmpty() ? relationships.get(0) : null;
        InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                .build();

        String schemaTypeGuid;
        EntityDetail schemaTypeEntity;
        if (schemaTypeRelationship != null) {
            schemaTypeGuid = schemaTypeRelationship.getEntityTwoProxy().getGUID();
            InstanceProperties proxyProperties = schemaTypeRelationship.getEntityTwoProxy().getUniqueProperties();
            if (EntityPropertiesUtils.matchExactlyInstanceProperties(proxyProperties, complexSchemaTypeProperties)) {
                log.info("Entity {} already exists", qualifiedNameForComplexSchemaType);
            } else {
                entitiesCreatorHelper.purgeEntity(schemaTypeRelationship.getEntityTwoProxy());
                schemaTypeEntity = addReportSchemaType(reportEntity, qualifiedNameForComplexSchemaType, complexSchemaTypeProperties);
                schemaTypeGuid = schemaTypeEntity.getGUID();
            }
        } else {
            schemaTypeEntity = addReportSchemaType(reportEntity, qualifiedNameForComplexSchemaType, complexSchemaTypeProperties);
            schemaTypeGuid = schemaTypeEntity.getGUID();
        }

        String qualifiedNameForReport = EntityPropertiesUtils.getStringValueForProperty(reportEntity.getProperties(), Constants.QUALIFIED_NAME);
        createOrUpdateElements(qualifiedNameForReport, schemaTypeGuid, payload.getReportElements());

    }

    private void createOrUpdateElements(String qualifiedNameForParent, String parentGuid, List<ReportElement> reportElements) throws Exception {
        List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);
        List<EntityDetail> matchingEntities = filterMatchingEntities(relationships, reportElements);
        if (reportElements != null && !reportElements.isEmpty()) {
            reportElements.forEach(e -> createOrUpdateReportElement(qualifiedNameForParent, parentGuid, matchingEntities, e));
        }
    }


    private List<EntityDetail> filterMatchingEntities(List<Relationship> relationships, List<ReportElement> reportElements) throws Exception {
        List<EntityDetail> matchingEntities = new ArrayList<>();
        if (relationships != null && !relationships.isEmpty()) {
            for (Relationship relationship : relationships) {
                String entity2Guid = relationship.getEntityTwoProxy().getGUID();
                EntityDetail entity = entitiesCreatorHelper.getEntityByGuid(entity2Guid);
                if (isReportElementDeleted(reportElements, entity.getProperties())) {
                    entitiesCreatorHelper.purgeRelationship(relationship);
                    deleteSection(entity);
                } else {
                    matchingEntities.add(entity);
                }
            }
        }
        return matchingEntities;
    }

    private void deleteSection(EntitySummary entity) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, RelationshipNotDeletedException, RelationshipNotKnownException, FunctionNotSupportedException, EntityNotKnownException, EntityNotDeletedException {

        List<Relationship> typeRelationships = entitiesCreatorHelper.getRelationships(Constants.COMPLEX_SCHEMA_TYPE, entity.getGUID());
         if(typeRelationships != null && !typeRelationships.isEmpty()){
             EntityProxy typeProxy = typeRelationships.get(0).getEntityOneProxy();
                 List<Relationship> childrenRelationships = entitiesCreatorHelper.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, typeProxy.getGUID());
                 if(childrenRelationships != null && !childrenRelationships.isEmpty()){
                     for(Relationship relationship : childrenRelationships)
                     deleteSection(relationship.getEntityTwoProxy());
                 }
                 entitiesCreatorHelper.purgeEntity(typeProxy);
         }
        entitiesCreatorHelper.purgeEntity(entity);


    }

    private boolean isReportElementDeleted(List<ReportElement> reportElements, InstanceProperties properties) {
        String elementName = EntityPropertiesUtils.getStringValueForProperty(properties, Constants.NAME);
        return reportElements != null && !reportElements.isEmpty() && reportElements.stream().noneMatch((e -> e.getName().equals(elementName)));
    }


    private void createOrUpdateReportElement(String qualifiedNameForParent, String parentGuid, List<EntityDetail> existingElements, ReportElement element) {
        try {
            if (element instanceof ReportSection) {
                createOrUpdateReportSection(qualifiedNameForParent, parentGuid, (ReportSection) element, existingElements);
            } else if (element instanceof ReportColumn) {
                createOrUpdateReportColumn(qualifiedNameForParent, parentGuid, (ReportColumn) element, existingElements);
            }
        } catch (Exception e) {
            log.error("Exception creating report element:", e);
            throw new RuntimeException(e);//TODO throw specific exception
        }
    }


    private void createOrUpdateReportSection(String qualifiedNameForParent, String parentGuid, ReportSection reportSection, List<EntityDetail> existingElements) throws Exception {

        EntityDetail matchingSection = findMatchingEntityForSection(reportSection, existingElements);
        if (matchingSection != null) {
            List<Relationship> sectionTypeRelationships;
            String sectionTypeGuid;
            String qualifiedNameForSection = EntityPropertiesUtils.getStringValueForProperty(matchingSection.getProperties(), Constants.QUALIFIED_NAME);
            sectionTypeRelationships = entitiesCreatorHelper.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, matchingSection.getGUID());
            if (sectionTypeRelationships == null || sectionTypeRelationships.isEmpty()) {
                EntityDetail schemaType = addSchemaType(qualifiedNameForSection, matchingSection, Constants.DOCUMENT_SCHEMA_TYPE);
                sectionTypeGuid = schemaType.getGUID();
            } else {
                sectionTypeGuid = sectionTypeRelationships.get(0).getEntityTwoProxy().getGUID();
            }
            createOrUpdateElements(qualifiedNameForSection, sectionTypeGuid, reportSection.getElements());
        } else {
            EntityDetail sectionTypeEntity = addSectionAndSectionType(qualifiedNameForParent, parentGuid, reportSection);
            String qualifiedNameForSection = qualifiedNameForParent + "." + reportSection.getName();
            createOrUpdateElements(qualifiedNameForSection, sectionTypeEntity.getGUID(), reportSection.getElements());
        }
    }


    private EntityDetail findMatchingEntityForSection(ReportSection reportSection, List<EntityDetail> existingElements) {
        List<EntityDetail> matchingSections = existingElements.stream().filter(e -> EntityPropertiesUtils.getStringValueForProperty(e.getProperties(), Constants.NAME).contains(reportSection.getName())).collect(Collectors.toList());
        if (matchingSections != null && !matchingSections.isEmpty()) {
            return matchingSections.get(0);
        }
        return null;
    }


    private void createOrUpdateReportColumn(String parentQualifiedName, String parentGuid, ReportColumn reportColumn, List<EntityDetail> existingElements) throws Exception {

        EntityDetail matchingColumn = findMatchingColumn(reportColumn, existingElements);
        List<Relationship> columnType;
        if (matchingColumn != null) {
            String qualifiedNameForColumn = EntityPropertiesUtils.getStringValueForProperty(matchingColumn.getProperties(), Constants.QUALIFIED_NAME);

            InstanceProperties columnProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                    .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
                    .withStringProperty(Constants.FORMULA, reportColumn.getFormula())
                    .build();

            EntityDetailWrapper wrapper = entitiesCreatorHelper.createOrUpdateEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedNameForColumn, columnProperties, null, true);
            createOrUpdateSemanticAssignment(reportColumn, wrapper.getEntityDetail().getGUID());
            createOrUpdateSchemaQueryImplementation(reportColumn, wrapper.getEntityDetail().getGUID());

            columnType = entitiesCreatorHelper.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, matchingColumn.getGUID());
            if (columnType == null || columnType.isEmpty()) {
                addSchemaType(qualifiedNameForColumn, wrapper.getEntityDetail(), Constants.SCHEMA_TYPE);
            }
        } else {
            addReportColumn(parentQualifiedName, parentGuid, reportColumn);
        }

    }

    private void createOrUpdateSchemaQueryImplementation(ReportColumn reportColumn, String columnGuid) throws Exception {

        List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid);
        List<String> relationshipsToRemove = new ArrayList<>();
        if (relationships != null && !relationships.isEmpty()) {
            relationshipsToRemove = relationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toList());
        }
        for (Source source : reportColumn.getSources()) {
            String sourceColumnGuid = getSourceGuid(source);
            if (!StringUtils.isEmpty(sourceColumnGuid)) {
                log.info("source {} for report column {} found.", source, reportColumn.getName());
                if (relationshipsToRemove != null && relationshipsToRemove.contains(sourceColumnGuid)) {
                    log.info("Relationship already exists and is valid");
                    relationshipsToRemove.remove(sourceColumnGuid);
                } else {
                    InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUERY, "")
                            .build();
                    entitiesCreatorHelper.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                            columnGuid,
                            sourceColumnGuid,
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            schemaQueryImplProperties);
                }
            } else {
                log.error(MessageFormat.format("source column not found, unable to add relationship {0} between column {1} and source {2}", Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid, source.toString()));
            }

            if (relationships != null && !relationships.isEmpty() && relationshipsToRemove != null && !relationshipsToRemove.isEmpty()) {
                for (Relationship relationship : relationships) {
                    if (relationshipsToRemove.contains(relationship.getGUID())) {
                        entitiesCreatorHelper.purgeRelationship(relationship);
                    }
                }
            }
        }
    }

    private void createOrUpdateSemanticAssignment(ReportColumn reportColumn, String columnGuid) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, RelationshipNotKnownException, RelationshipNotDeletedException, StatusNotSupportedException, TypeDefNotKnownException {
        List<Relationship> existingAssignments = entitiesCreatorHelper.getRelationships(Constants.SEMANTIC_ASSIGNMENT, columnGuid);
        if (reportColumn.getBusinessTerm() == null) {
            deleteRelationships(existingAssignments);
        } else {
            String businessTermAssignedToColumnGuid = findAssignedBusinessTermGuid(reportColumn);
            List<Relationship> matchingRelationship = new ArrayList<>();
            if (existingAssignments != null && !existingAssignments.isEmpty()) {
                matchingRelationship = existingAssignments.stream().filter(e -> e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid)).collect(Collectors.toList());
                deleteRelationships(existingAssignments.stream().filter(e -> !e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid)).collect(Collectors.toList()));
            }

            if ((matchingRelationship == null || matchingRelationship.isEmpty()) && !StringUtils.isEmpty(businessTermAssignedToColumnGuid)) {
                entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                        columnGuid,
                        businessTermAssignedToColumnGuid,
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());
            }
        }
    }


    private EntityDetail findMatchingColumn(ReportColumn reportColumn, List<EntityDetail> existingElements) {
        List<EntityDetail> matchingColumns = existingElements.stream().filter(e -> EntityPropertiesUtils.getStringValueForProperty(e.getProperties(), Constants.NAME).contains(reportColumn.getName())).collect(Collectors.toList());
        if (matchingColumns != null && !matchingColumns.isEmpty()) {
            return matchingColumns.get(0);
        }
        return null;
    }

}
