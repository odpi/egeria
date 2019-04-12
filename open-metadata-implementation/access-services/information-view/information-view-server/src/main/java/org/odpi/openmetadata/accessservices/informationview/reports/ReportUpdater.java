/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;


import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.ReportElementCreationException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
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


    public ReportUpdater(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, lookupHelper, helper, auditLog);
    }

    /**
     *
     * @param payload  - object describing the report
     * @param reportEntity - entity describing the report
     * @throws Exception
     */
    public void updateReport(ReportRequestBody payload, EntityDetail reportEntity) throws UserNotAuthorizedException,
                                                                                          EntityNotKnownException,
                                                                                          EntityNotDeletedException,
                                                                                          InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          FunctionNotSupportedException,
                                                                                          ClassificationErrorException,
                                                                                          StatusNotSupportedException,
                                                                                          TypeDefNotKnownException,
                                                                                          PropertyErrorException,
                                                                                          TypeErrorException,
                                                                                          PagingErrorException,
                                                                                          RelationshipNotKnownException,
                                                                                          EntityProxyOnlyException,
                                                                                          RelationshipNotDeletedException {
        String qualifiedNameForComplexSchemaType = QualifiedNameUtils.buildQualifiedName("", Constants.ASSET_SCHEMA_TYPE, payload.getId()  + Constants.TYPE_SUFFIX);

        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportEntity.getGUID());
        //ASSET Schema type relationship can have 1 at max for reports
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
                omEntityDao.purgeEntity(schemaTypeRelationship.getEntityTwoProxy());
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

    /**
     *
     * @param qualifiedNameForParent - qualified name of the parent element
     * @param parentGuid - guid of the parent element
     * @param reportElements - elements describing the report
     * @throws Exception
     */
    private void createOrUpdateElements(String qualifiedNameForParent, String parentGuid, List<ReportElement> reportElements) throws
                                                                                                                              InvalidParameterException,
                                                                                                                              RelationshipNotDeletedException,
                                                                                                                              RelationshipNotKnownException,
                                                                                                                              EntityProxyOnlyException,
                                                                                                                              EntityNotDeletedException,
                                                                                                                              EntityNotKnownException,
                                                                                                                              FunctionNotSupportedException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              RepositoryErrorException {
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);
        List<EntityDetail> matchingEntities = filterMatchingEntities(relationships, reportElements);
        if (reportElements != null && !reportElements.isEmpty()) {
            reportElements.forEach(e -> createOrUpdateReportElement(qualifiedNameForParent, parentGuid, matchingEntities, e));
        }
    }

    /**
     *
     * @param relationships - list of existing relationships linking to report elements
     * @param reportElements - list of report elements
     * @return - list of entities matching the report elements
     * @throws Exception
     */
    private List<EntityDetail> filterMatchingEntities(List<Relationship> relationships, List<ReportElement> reportElements) throws
                                                                                                                            RelationshipNotDeletedException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            FunctionNotSupportedException,
                                                                                                                            InvalidParameterException,
                                                                                                                            RepositoryErrorException,
                                                                                                                            RelationshipNotKnownException,
                                                                                                                            EntityProxyOnlyException,
                                                                                                                            EntityNotKnownException,
                                                                                                                            EntityNotDeletedException {
        List<EntityDetail> matchingEntities = new ArrayList<>();
        if (relationships != null && !relationships.isEmpty()) {
            for (Relationship relationship : relationships) {
                String entity2Guid = relationship.getEntityTwoProxy().getGUID();
                EntityDetail entity = omEntityDao.getEntityByGuid(entity2Guid);
                if (isReportElementDeleted(reportElements, entity.getProperties())) {
                    omEntityDao.purgeRelationship(relationship);
                    deleteSection(entity);
                } else {
                    matchingEntities.add(entity);
                }
            }
        }
        return matchingEntities;
    }

    /**
     *
     * @param entity - entity describing the section that no longer exists
     * @throws RepositoryErrorException
     * @throws UserNotAuthorizedException
     * @throws InvalidParameterException
     * @throws RelationshipNotDeletedException
     * @throws RelationshipNotKnownException
     * @throws FunctionNotSupportedException
     * @throws EntityNotKnownException
     * @throws EntityNotDeletedException
     */
    private void deleteSection(EntitySummary entity) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, EntityNotKnownException, EntityNotDeletedException {

        List<Relationship> typeRelationships = omEntityDao.getRelationships(Constants.COMPLEX_SCHEMA_TYPE, entity.getGUID());
         if(typeRelationships != null && !typeRelationships.isEmpty()){
             EntityProxy typeProxy = typeRelationships.get(0).getEntityOneProxy();
                 List<Relationship> childrenRelationships = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, typeProxy.getGUID());
                 if(childrenRelationships != null && !childrenRelationships.isEmpty()){
                     for(Relationship relationship : childrenRelationships)
                     deleteSection(relationship.getEntityTwoProxy());
                 }
                 omEntityDao.purgeEntity(typeProxy);
         }
        omEntityDao.purgeEntity(entity);


    }

    /**
     *
     * @param reportElements - list of defined report elements
     * @param properties - properties of the report element to be checked
     * @return
     */
    private boolean isReportElementDeleted(List<ReportElement> reportElements, InstanceProperties properties) {
        String elementName = EntityPropertiesUtils.getStringValueForProperty(properties, Constants.NAME);
        return reportElements != null && !reportElements.isEmpty() && reportElements.stream().noneMatch((e -> e.getName().equals(elementName)));
    }


    /**
     *
     * @param qualifiedNameForParent qualified name for the parent
     * @param parentGuid guid of the report element
     * @param existingElements entities already defined
     * @param element element in the report
     */
    private void createOrUpdateReportElement(String qualifiedNameForParent, String parentGuid, List<EntityDetail> existingElements, ReportElement element) {
        try {
            if (element instanceof ReportSection) {
                createOrUpdateReportSection(qualifiedNameForParent, parentGuid, (ReportSection) element, existingElements);
            } else if (element instanceof ReportColumn) {
                createOrUpdateReportColumn(qualifiedNameForParent, parentGuid, (ReportColumn) element, existingElements);
            }
        } catch (TypeDefNotKnownException | InvalidParameterException | RelationshipNotDeletedException | FunctionNotSupportedException | EntityNotDeletedException | EntityProxyOnlyException | PagingErrorException | ClassificationErrorException | UserNotAuthorizedException | TypeErrorException | EntityNotKnownException | RepositoryErrorException | RelationshipNotKnownException | StatusNotSupportedException | PropertyErrorException e) {
            throw new ReportElementCreationException(ReportUpdater.class.getName(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getFormattedErrorMessage(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getSystemAction(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getUserAction(), e);
        }
    }


    /**
     *
     * @param qualifiedNameForParent qualified name for the parent
     * @param parentGuid guid of the report element
     * @param reportSection section in the report
     * @param existingElements entities already defined
     * @throws Exception
     */
    private void createOrUpdateReportSection(String qualifiedNameForParent, String parentGuid, ReportSection reportSection, List<EntityDetail> existingElements) throws
                                                                                                                                                                 InvalidParameterException,
                                                                                                                                                                 TypeErrorException,
                                                                                                                                                                 PropertyErrorException,
                                                                                                                                                                 EntityNotKnownException,
                                                                                                                                                                 FunctionNotSupportedException,
                                                                                                                                                                 PagingErrorException,
                                                                                                                                                                 ClassificationErrorException,
                                                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                                                 RepositoryErrorException,
                                                                                                                                                                 StatusNotSupportedException,
                                                                                                                                                                 EntityNotDeletedException,
                                                                                                                                                                 EntityProxyOnlyException,
                                                                                                                                                                 RelationshipNotDeletedException,
                                                                                                                                                                 RelationshipNotKnownException,
                                                                                                                                                                 TypeDefNotKnownException {

        EntityDetail matchingSection = findMatchingEntityForElements(reportSection, existingElements);
        if (matchingSection != null) {
            List<Relationship> sectionTypeRelationships;
            String sectionTypeGuid;
            String qualifiedNameForSection = EntityPropertiesUtils.getStringValueForProperty(matchingSection.getProperties(), Constants.QUALIFIED_NAME);
            String qualifiedNameForSectionType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_TYPE, reportSection.getName() + Constants.TYPE_SUFFIX);
            sectionTypeRelationships = omEntityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, matchingSection.getGUID());
            if (sectionTypeRelationships == null || sectionTypeRelationships.isEmpty()) {
                EntityDetail schemaType = addSchemaType(qualifiedNameForSectionType, matchingSection, Constants.DOCUMENT_SCHEMA_TYPE);
                sectionTypeGuid = schemaType.getGUID();
            } else {
                sectionTypeGuid = sectionTypeRelationships.get(0).getEntityTwoProxy().getGUID();
            }
            createOrUpdateElements(qualifiedNameForSection, sectionTypeGuid, reportSection.getElements());
        } else {
            EntityDetail sectionTypeEntity = addSectionAndSectionType(qualifiedNameForParent, parentGuid, reportSection);
            String qualifiedNameForSection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent,Constants.DOCUMENT_SCHEMA_ATTRIBUTE, reportSection.getName());
            createOrUpdateElements(qualifiedNameForSection, sectionTypeEntity.getGUID(), reportSection.getElements());
        }
    }


    /**
     *
     * @param reportElement
     * @param existingElements
     * @return
     */
    private EntityDetail findMatchingEntityForElements(ReportElement reportElement, List<EntityDetail> existingElements) {
        List<EntityDetail> matchingElements = existingElements.stream().filter(e -> EntityPropertiesUtils.getStringValueForProperty(e.getProperties(), Constants.NAME).contains(reportElement.getName())).collect(Collectors.toList());
        if (matchingElements != null && !matchingElements.isEmpty()) {
            return matchingElements.get(0);
        }
        return null;
    }


    private void createOrUpdateReportColumn(String parentQualifiedName, String parentGuid, ReportColumn reportColumn, List<EntityDetail> existingElements) throws
                                                                                                                                                           InvalidParameterException,
                                                                                                                                                           StatusNotSupportedException,
                                                                                                                                                           PropertyErrorException,
                                                                                                                                                           EntityNotKnownException,
                                                                                                                                                           TypeErrorException,
                                                                                                                                                           FunctionNotSupportedException,
                                                                                                                                                           PagingErrorException,
                                                                                                                                                           ClassificationErrorException,
                                                                                                                                                           UserNotAuthorizedException,
                                                                                                                                                           RepositoryErrorException,
                                                                                                                                                           RelationshipNotKnownException,
                                                                                                                                                           TypeDefNotKnownException,
                                                                                                                                                           RelationshipNotDeletedException {

        EntityDetail matchingColumn = findMatchingEntityForElements(reportColumn, existingElements);
        List<Relationship> columnType;
        if (matchingColumn != null) {
            String qualifiedNameForColumn = EntityPropertiesUtils.getStringValueForProperty(matchingColumn.getProperties(), Constants.QUALIFIED_NAME);


            InstanceProperties columnProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                    .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
                    .withStringProperty(Constants.FORMULA, reportColumn.getFormula())
                    .build();

            OMEntityWrapper wrapper = omEntityDao.createOrUpdateEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedNameForColumn, columnProperties, null, true, false);
            createOrUpdateSemanticAssignment(reportColumn, wrapper.getEntityDetail().getGUID());
            createOrUpdateSchemaQueryImplementation(reportColumn, wrapper.getEntityDetail().getGUID());

            columnType = omEntityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, matchingColumn.getGUID());
            if (columnType == null || columnType.isEmpty()) {
                String qualifiedNameForColumnType = QualifiedNameUtils.buildQualifiedName(parentQualifiedName, Constants.SCHEMA_TYPE, reportColumn.getName());
                addSchemaType(qualifiedNameForColumnType, wrapper.getEntityDetail(), Constants.SCHEMA_TYPE);
            }
        } else {
            addReportColumn(parentQualifiedName, parentGuid, reportColumn);
        }

    }

    private void createOrUpdateSchemaQueryImplementation(ReportColumn reportColumn, String columnGuid) throws
                                                                                                       UserNotAuthorizedException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       InvalidParameterException,
                                                                                                       RepositoryErrorException,
                                                                                                       PropertyErrorException,
                                                                                                       TypeErrorException,
                                                                                                       PagingErrorException,
                                                                                                       StatusNotSupportedException,
                                                                                                       EntityNotKnownException,
                                                                                                       RelationshipNotKnownException,
                                                                                                       RelationshipNotDeletedException {

        List<Relationship> relationships = omEntityDao.getRelationships(Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid);
        List<String> relationshipsToRemove = new ArrayList<>();
        if (relationships != null && !relationships.isEmpty()) {
            relationshipsToRemove = relationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toList());
        }
        for (Source source : reportColumn.getSources()) {
            String sourceColumnGuid = entityReferenceResolver.getSourceGuid(source);
            if (!StringUtils.isEmpty(sourceColumnGuid)) {
                log.info("source {} for reports column {} found.", source, reportColumn.getName());
                if (relationshipsToRemove != null && relationshipsToRemove.contains(sourceColumnGuid)) {
                    log.info("Relationship already exists and is valid");
                    relationshipsToRemove.remove(sourceColumnGuid);
                } else {
                    InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUERY, "")
                            .build();
                    omEntityDao.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                            columnGuid,
                            sourceColumnGuid,
                            schemaQueryImplProperties);
                }
            } else {
                log.error(MessageFormat.format("source column not found, unable to add relationship {0} between column {1} and source {2}", Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid, source.toString()));
            }

            if (relationships != null && !relationships.isEmpty() && relationshipsToRemove != null && !relationshipsToRemove.isEmpty()) {
                for (Relationship relationship : relationships) {
                    if (relationshipsToRemove.contains(relationship.getGUID())) {
                        omEntityDao.purgeRelationship(relationship);
                    }
                }
            }
        }
    }

    private void createOrUpdateSemanticAssignment(ReportColumn reportColumn, String columnGuid) throws UserNotAuthorizedException,
                                                                                                       EntityNotKnownException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       InvalidParameterException,
                                                                                                       RepositoryErrorException,
                                                                                                       PropertyErrorException,
                                                                                                       TypeErrorException,
                                                                                                       PagingErrorException,
                                                                                                       RelationshipNotKnownException,
                                                                                                       RelationshipNotDeletedException,
                                                                                                       StatusNotSupportedException{
        List<Relationship> existingAssignments = omEntityDao.getRelationships(Constants.SEMANTIC_ASSIGNMENT, columnGuid);
        if (reportColumn.getBusinessTerm() == null) {
            deleteRelationships(existingAssignments);
        } else {
            String businessTermAssignedToColumnGuid = entityReferenceResolver.getBusinessTermGuid(reportColumn.getBusinessTerm());
            List<Relationship> matchingRelationship = new ArrayList<>();
            if (existingAssignments != null && !existingAssignments.isEmpty()) {
                matchingRelationship = existingAssignments.stream().filter(e -> e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid)).collect(Collectors.toList());
                deleteRelationships(existingAssignments.stream().filter(e -> !e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid)).collect(Collectors.toList()));
            }

            if ((matchingRelationship == null || matchingRelationship.isEmpty()) && !StringUtils.isEmpty(businessTermAssignedToColumnGuid)) {
                omEntityDao.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                                            columnGuid,
                                            businessTermAssignedToColumnGuid,
                                            new InstanceProperties());
            }
        }
    }


}
