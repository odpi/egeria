/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;


import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
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
     *
     * @param userId id of user submitting the request
     * @param payload  - object describing the report
     * @param registrationGuid guid of software server capability source
     * @param registrationQualifiedName qualified name of software server capability source
     * @param reportEntity - entity describing the report
     */
    public void updateReport(String userId,
                             ReportRequestBody payload,
                             String registrationGuid,
                             String registrationQualifiedName,
                             EntityDetail reportEntity) throws UserNotAuthorizedException,
                                                                EntityNotKnownException,
                                                                EntityNotDeletedException,
                                                                InvalidParameterException,
                                                                RepositoryErrorException,
                                                                FunctionNotSupportedException {
        String qualifiedNameForComplexSchemaType = QualifiedNameUtils.buildQualifiedName("", Constants.ASSET_SCHEMA_TYPE, payload.getReport().getId()  + Constants.TYPE_SUFFIX);

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
                schemaTypeEntity = addAssetSchemaType(userId, reportEntity.getGUID(), qualifiedNameForComplexSchemaType, registrationGuid, registrationQualifiedName, Constants.COMPLEX_SCHEMA_TYPE, complexSchemaTypeProperties);
                schemaTypeGuid = schemaTypeEntity.getGUID();
            }
        } else {
            schemaTypeEntity = addAssetSchemaType(userId, reportEntity.getGUID(), qualifiedNameForComplexSchemaType, registrationGuid, registrationQualifiedName, Constants.COMPLEX_SCHEMA_TYPE, complexSchemaTypeProperties);
            schemaTypeGuid = schemaTypeEntity.getGUID();
        }

        String qualifiedNameForReport = helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, reportEntity.getProperties(), "updateReport");
        createOrUpdateElements(userId, qualifiedNameForReport, registrationGuid, registrationQualifiedName, schemaTypeGuid, payload.getReport().getReportElements());

    }

    /**
     *
     *
     * @param userId
     * @param qualifiedNameForParent - qualified name of the parent element
     * @param registrationGuid
     * @param registrationQualifiedName
     * @param parentGuid - guid of the parent element
     * @param reportElements - elements describing the report
     */
    private void createOrUpdateElements(String userId,
                                        String qualifiedNameForParent,
                                        String registrationGuid,
                                        String registrationQualifiedName,
                                        String parentGuid,
                                        List<ReportElement> reportElements) throws InvalidParameterException,
                                                                                  EntityNotDeletedException,
                                                                                  EntityNotKnownException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException,
                                                                                  RepositoryErrorException {

        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);
        List<EntityDetail> matchingEntities = filterMatchingEntities(relationships, reportElements);
        if (reportElements != null && !reportElements.isEmpty()) {
            reportElements.forEach(e -> createOrUpdateReportElement(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, matchingEntities, e));
        }
    }

    /**
     *
     * @param relationships - list of existing relationships linking to report elements
     * @param reportElements - list of report elements
     * @return - list of entities matching the report elements
     */
    private List<EntityDetail> filterMatchingEntities(List<Relationship> relationships,
                                                      List<ReportElement> reportElements) throws UserNotAuthorizedException,
                                                                                                FunctionNotSupportedException,
                                                                                                InvalidParameterException,
                                                                                                RepositoryErrorException,
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
     * @throws FunctionNotSupportedException
     * @throws EntityNotKnownException
     * @throws EntityNotDeletedException
     */
    private void deleteSection(EntitySummary entity) throws RepositoryErrorException, UserNotAuthorizedException,
                                                            InvalidParameterException, FunctionNotSupportedException,
                                                            EntityNotKnownException, EntityNotDeletedException {

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
        String elementName = helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, properties, "isReportElementDeleted");
        return reportElements != null && !reportElements.isEmpty() && reportElements.stream().noneMatch((e -> e.getName().equals(elementName)));
    }


    /**
     * @param userId
     * @param qualifiedNameForParent qualified name for the parent
     * @param parentGuid guid of the report element
     * @param registrationGuid
     * @param registrationQualifiedName
     * @param existingElements entities already defined
     * @param element element in the report
     */
    private void createOrUpdateReportElement(String userId, String qualifiedNameForParent, String parentGuid,
                                             String registrationGuid, String registrationQualifiedName,
                                             List<EntityDetail> existingElements, ReportElement element) {
        try {
            if (element instanceof ReportSection) {
                createOrUpdateReportSection(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, (ReportSection) element, existingElements);
            } else if (element instanceof ReportColumn) {
                createOrUpdateReportColumn(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, (ReportColumn) element, existingElements);
            }
        } catch (InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException | PagingErrorException | ClassificationErrorException | UserNotAuthorizedException | TypeErrorException | EntityNotKnownException | RepositoryErrorException | StatusNotSupportedException | PropertyErrorException e) {
            throw new ReportElementCreationException(ReportUpdater.class.getName(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getFormattedErrorMessage(element.getName(), e.getMessage()),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getSystemAction(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getUserAction(),
                                                    e);
        }
    }


    /**
     *
     *
     * @param userId
     * @param qualifiedNameForParent qualified name for the parent
     * @param parentGuid guid of the report element
     * @param registrationGuid
     * @param registrationQualifiedName
     * @param reportSection section in the report
     * @param existingElements entities already defined
     */
    private void createOrUpdateReportSection(String userId, String qualifiedNameForParent, String parentGuid,
                                             String registrationGuid,
                                             String registrationQualifiedName,
                                             ReportSection reportSection,
                                             List<EntityDetail> existingElements) throws InvalidParameterException,
                                                                                         EntityNotKnownException,
                                                                                         FunctionNotSupportedException,
                                                                                         UserNotAuthorizedException,
                                                                                         RepositoryErrorException,
                                                                                         EntityNotDeletedException {

        EntityDetail matchingSection = findMatchingEntityForElements(reportSection, existingElements);
        if (matchingSection != null) {
            List<Relationship> sectionTypeRelationships;
            String sectionTypeGuid;
            String methodName = "createOrUpdateReportSection";
            String qualifiedNameForSection = helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, matchingSection.getProperties(), methodName);
            String qualifiedNameForSectionType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_TYPE, reportSection.getName() + Constants.TYPE_SUFFIX);
            sectionTypeRelationships = omEntityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, matchingSection.getGUID());
            if (sectionTypeRelationships == null || sectionTypeRelationships.isEmpty()) {
                EntityDetail schemaType = createSchemaType(userId, Constants.DOCUMENT_SCHEMA_TYPE,  qualifiedNameForSectionType, registrationGuid, registrationQualifiedName, helper.addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, new InstanceProperties(), Constants.QUALIFIED_NAME, qualifiedNameForSectionType, "addSchemaType"), Constants.SCHEMA_ATTRIBUTE_TYPE, matchingSection.getGUID() );
                sectionTypeGuid = schemaType.getGUID();
            } else {
                sectionTypeGuid = sectionTypeRelationships.get(0).getEntityTwoProxy().getGUID();
            }
            createOrUpdateElements(userId, qualifiedNameForSection, registrationGuid, registrationQualifiedName, sectionTypeGuid, reportSection.getElements());
        } else {
            EntityDetail sectionTypeEntity = addSectionAndSectionType(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, reportSection);
            String qualifiedNameForSection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent,Constants.DOCUMENT_SCHEMA_ATTRIBUTE, reportSection.getName());
            createOrUpdateElements(userId, qualifiedNameForSection, registrationGuid, registrationQualifiedName, sectionTypeEntity.getGUID(), reportSection.getElements());
        }
    }


    /**
     * @param reportElement
     * @param existingElements
     * @return entity matching element
     */
    private EntityDetail findMatchingEntityForElements(ReportElement reportElement, List<EntityDetail> existingElements) {
        List<EntityDetail> matchingElements = existingElements.stream().filter(e -> {
            String methodName = "findMatchingEntityForElements";
            return helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, e.getProperties(), methodName).contains(reportElement.getName());
        }).collect(Collectors.toList());
        if (matchingElements != null && !matchingElements.isEmpty()) {
            return matchingElements.get(0);
        }
        return null;
    }


    private void createOrUpdateReportColumn(String userId, String parentQualifiedName,
                                            String parentGuid,
                                            String registrationGuid,
                                            String registrationQualifiedName,
                                            ReportColumn reportColumn,
                                            List<EntityDetail> existingElements) throws InvalidParameterException,
                                                                                       StatusNotSupportedException,
                                                                                       PropertyErrorException,
                                                                                       EntityNotKnownException,
                                                                                       TypeErrorException,
                                                                                       FunctionNotSupportedException,
                                                                                       PagingErrorException,
                                                                                       ClassificationErrorException,
                                                                                       UserNotAuthorizedException,
                                                                                       RepositoryErrorException {

        EntityDetail matchingColumn = findMatchingEntityForElements(reportColumn, existingElements);
        List<Relationship> columnType;
        if (matchingColumn != null) {
            String qualifiedNameForColumn = helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                                                                    Constants.QUALIFIED_NAME,
                                                                    matchingColumn.getProperties(),
                                                                    "createOrUpdateReportColumn");

            InstanceProperties columnProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                    .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
                    .withStringProperty(Constants.FORMULA, reportColumn.getFormula())
                    .build();

            OMEntityWrapper wrapper = omEntityDao.createOrUpdateEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedNameForColumn, columnProperties, null, true, false);
            createOrUpdateSemanticAssignments(userId, registrationGuid, registrationQualifiedName, reportColumn.getBusinessTerms(), wrapper.getEntityDetail().getGUID());
            createOrUpdateSchemaQueryImplementation(reportColumn.getSources(), wrapper.getEntityDetail().getGUID());

            columnType = omEntityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, matchingColumn.getGUID());
            if (columnType == null || columnType.isEmpty()) {
                String qualifiedNameForColumnType = QualifiedNameUtils.buildQualifiedName(parentQualifiedName, Constants.SCHEMA_TYPE, reportColumn.getName());
                InstanceProperties schemaAttributeTypeProperties = new EntityPropertiesBuilder().withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumnType)
                                                                                                .build();
                createSchemaType(userId, Constants.SCHEMA_TYPE,  qualifiedNameForColumnType, registrationGuid, registrationQualifiedName, schemaAttributeTypeProperties, Constants.SCHEMA_ATTRIBUTE_TYPE, wrapper.getEntityDetail().getGUID() );
            }
        } else {
            addReportColumn(userId, parentQualifiedName, parentGuid, registrationGuid, registrationQualifiedName, reportColumn);
        }
    }
}