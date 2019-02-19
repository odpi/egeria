/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
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

public abstract class ReportBasicOperation extends BasicOperation{

    private static final Logger log = LoggerFactory.getLogger(ReportBasicOperation.class);

    protected final EntityReferenceResolver entityReferenceResolver;
    private LookupHelper lookupHelper;

    public ReportBasicOperation(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, helper, auditLog);
        this.lookupHelper = lookupHelper;
        this.entityReferenceResolver = new EntityReferenceResolver(lookupHelper, omEntityDao);
    }


    public void addElements(String qualifiedNameForParent, String parentGuid, List<ReportElement> allElements) {
        if (allElements == null || allElements.isEmpty())
            return;
        allElements.parallelStream().forEach(e -> addReportElement(qualifiedNameForParent, parentGuid, e));
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
            throw new RuntimeException("Unable to create Report Element due to exception: " + e.getClass().getName() + " and message " + e.getMessage(), e);//TODO throw specific exception
        }
    }

    private void addReportSection(String qualifiedNameForParent, String parentGuid, ReportSection reportSection) throws InvalidParameterException, PropertyErrorException, TypeDefNotKnownException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {
        EntityDetail typeEntity = addSectionAndSectionType(qualifiedNameForParent, parentGuid, reportSection);
        String qualifiedNameForSection = EntityPropertiesUtils.getStringValueForProperty(typeEntity.getProperties(), Constants.QUALIFIED_NAME);
        qualifiedNameForSection = qualifiedNameForSection.substring(0, qualifiedNameForSection.lastIndexOf(Constants.TYPE_SUFFIX));
        addElements(qualifiedNameForSection, typeEntity.getGUID(), reportSection.getElements());
    }


    protected EntityDetail addSectionAndSectionType(String qualifiedNameForParent, String parentGuid, ReportSection reportSection) throws InvalidParameterException, StatusNotSupportedException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, TypeDefNotKnownException {

        String qualifiedNameForSection = qualifiedNameForParent + SEPARATOR + reportSection.getName();
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSection)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportSection.getName())
                .build();
        EntityDetail sectionEntity = omEntityDao.addEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE,
                                                                    qualifiedNameForSection,
                                                                    sectionProperties);


        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                                            parentGuid,
                                            sectionEntity.getGUID(),
                                            Constants.INFORMATION_VIEW_OMAS_NAME,
                                            new InstanceProperties());


        return addSchemaType(qualifiedNameForSection, sectionEntity, Constants.DOCUMENT_SCHEMA_TYPE);
    }


    protected EntityDetail addReportColumn(String parentQualifiedName, String parentGuid, ReportColumn reportColumn) throws InvalidParameterException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException, StatusNotSupportedException {

        String qualifiedNameForColumn = parentQualifiedName + SEPARATOR + reportColumn.getName();

        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
                .withStringProperty(Constants.FORMULA, reportColumn.getFormula())
                .build();
        EntityDetail derivedColumnEntity = omEntityDao.addEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE,
                qualifiedNameForColumn,
                columnProperties);

        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
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

        EntityDetail schemaTypeEntity = omEntityDao.addEntity(schemaAttributeType,
                                                                        qualifiedNameForType,
                                                                        typeProperties);

        omEntityDao.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                                            schemaAttributeEntity.getGUID(),
                                            schemaTypeEntity.getGUID(),
                                            Constants.INFORMATION_VIEW_OMAS_NAME,
                                            new InstanceProperties());
        return schemaTypeEntity;
    }

    private void addBusinessTerm(ReportColumn reportColumn, EntityDetail derivedColumnEntity) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, StatusNotSupportedException, TypeDefNotKnownException, EntityNotKnownException {
        String businessTermGuid = entityReferenceResolver.getBusinessTermGuid(reportColumn.getBusinessTerm());
            if (!StringUtils.isEmpty(businessTermGuid)) {
                omEntityDao.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                                            derivedColumnEntity.getGUID(),
                                            businessTermGuid,
                                            Constants.INFORMATION_VIEW_OMAS_NAME,
                                            new InstanceProperties());
            }
    }

    private void addQueryTargets(ReportColumn reportColumn, EntityDetail derivedColumnEntity) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException {
        for (Source source : reportColumn.getSources()) {

            String sourceColumnGUID = entityReferenceResolver.getSourceGuid(source);
            if (!StringUtils.isEmpty(sourceColumnGUID)) {
                log.info("source {} for report column {} found.", source, reportColumn.getName());

                InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                                                                .withStringProperty(Constants.QUERY, "")
                                                                .build();
                omEntityDao.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                                                    derivedColumnEntity.getGUID(),
                                                    sourceColumnGUID,
                                                    Constants.INFORMATION_VIEW_OMAS_NAME,
                                                    schemaQueryImplProperties);

            } else {
                String message = MessageFormat.format("source column not found, unable to add relationship between column {0} and source {1}", reportColumn.getName(), source.toString());
                log.error(message);
                throw new RuntimeException(message);
            }
        }
    }


    protected EntityDetail addReportSchemaType(EntityDetail reportEntity, String qualifiedNameForComplexSchemaType, InstanceProperties complexSchemaTypeProperties) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException {
        EntityDetail complexSchemaTypeEntity;
        complexSchemaTypeEntity = omEntityDao.addEntity(Constants.COMPLEX_SCHEMA_TYPE,
                                                                  qualifiedNameForComplexSchemaType,
                                                                  complexSchemaTypeProperties,
                                                       null);

        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                                                reportEntity.getGUID(),
                                                complexSchemaTypeEntity.getGUID(),
                                                Constants.INFORMATION_VIEW_OMAS_NAME,
                                                new InstanceProperties());
        return complexSchemaTypeEntity;
    }


    protected void deleteRelationships(List<Relationship> existingAssignments) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, RelationshipNotDeletedException, RelationshipNotKnownException, FunctionNotSupportedException {
        if (existingAssignments != null && !existingAssignments.isEmpty()) {
            for (Relationship relationship : existingAssignments) {
                omEntityDao.purgeRelationship(relationship);
            }
        } else {
            log.info("No relationships to delete");
        }
    }



}
