/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.ReportElementCreationException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
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

    public ReportBasicOperation(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, helper, auditLog);
        this.entityReferenceResolver = new EntityReferenceResolver(lookupHelper, omEntityDao);
    }


    /**
     *
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param allElements all elements linked to the current element
     */
    public void addElements(String qualifiedNameForParent, String parentGuid, List<ReportElement> allElements) {
        if (allElements == null || allElements.isEmpty())
            return;
        allElements.parallelStream().forEach(e -> addReportElement(qualifiedNameForParent, parentGuid, e));
    }


    /**
     *
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param element object describing the current element
     */
    public void addReportElement(String qualifiedNameForParent, String parentGuid, ReportElement element) {
        try {
            if (element instanceof ReportSection) {
                addReportSection(qualifiedNameForParent, parentGuid, (ReportSection) element);
            } else if (element instanceof ReportColumn) {
                addReportColumn(qualifiedNameForParent, parentGuid, (ReportColumn) element);
            }
        } catch (PagingErrorException | TypeDefNotKnownException | PropertyErrorException | EntityNotKnownException | UserNotAuthorizedException | StatusNotSupportedException | InvalidParameterException | FunctionNotSupportedException | RepositoryErrorException | TypeErrorException | ClassificationErrorException e) {
            log.error("Exception creating report element", e);
            throw new ReportElementCreationException(ReportBasicOperation.class.getName(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getFormattedErrorMessage(element.toString(), e.getMessage()),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getSystemAction(),
                                                    InformationViewErrorCode.REPORT_ELEMENT_CREATION_EXCEPTION.getUserAction(),
                                                    e);

        }
    }

    /**
     *
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param reportSection object describing the current section in the report
     * @throws InvalidParameterException
     * @throws PropertyErrorException
     * @throws TypeDefNotKnownException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws StatusNotSupportedException
     */
    private void addReportSection(String qualifiedNameForParent, String parentGuid, ReportSection reportSection) throws InvalidParameterException, PropertyErrorException, TypeDefNotKnownException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {
        EntityDetail typeEntity = addSectionAndSectionType(qualifiedNameForParent, parentGuid, reportSection);
        String qualifiedNameForSection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_ATTRIBUTE, reportSection.getName());
        addElements(qualifiedNameForSection, typeEntity.getGUID(), reportSection.getElements());
    }


    /**
     *
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param reportSection object describing the current section in the report
     * @return
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws RepositoryErrorException
     * @throws TypeDefNotKnownException
     */
    protected EntityDetail addSectionAndSectionType(String qualifiedNameForParent, String parentGuid, ReportSection reportSection) throws InvalidParameterException, StatusNotSupportedException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, TypeDefNotKnownException {

        String qualifiedNameForSection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_ATTRIBUTE, reportSection.getName());
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSection)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportSection.getName())
                .build();
        EntityDetail sectionEntity = omEntityDao.addEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE,
                                                                    qualifiedNameForSection,
                                                                    sectionProperties,
                                                        false);


        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                                            parentGuid,
                                            sectionEntity.getGUID(),
                new InstanceProperties());

        String qualifiedNameForSectionType = buildQualifiedNameForSchemaType(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_TYPE, reportSection);
        return addSchemaType(qualifiedNameForSectionType, sectionEntity, Constants.DOCUMENT_SCHEMA_TYPE);
    }


    /**
     *
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param reportColumn object describing the current column in the report
     * @return
     * @throws InvalidParameterException
     * @throws TypeErrorException
     * @throws TypeDefNotKnownException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws StatusNotSupportedException
     */
    protected EntityDetail addReportColumn(String qualifiedNameForParent, String parentGuid, ReportColumn reportColumn) throws InvalidParameterException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException, StatusNotSupportedException {

        String qualifiedNameForColumn = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DERIVED_SCHEMA_ATTRIBUTE, reportColumn.getName());

        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
                .withStringProperty(Constants.FORMULA, reportColumn.getFormula())
                .build();
        EntityDetail derivedColumnEntity = omEntityDao.addEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE,
                                                                qualifiedNameForColumn,
                                                                columnProperties,
                                                    false);

        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                parentGuid,
                derivedColumnEntity.getGUID(),
                new InstanceProperties());

        addBusinessTerm(reportColumn, derivedColumnEntity);
        addQueryTargets(reportColumn, derivedColumnEntity);
        String qualifiedNameForColumnType = buildQualifiedNameForSchemaType(qualifiedNameForParent, Constants.SCHEMA_TYPE, reportColumn);

        addSchemaType(qualifiedNameForColumnType, derivedColumnEntity, Constants.SCHEMA_TYPE);

        return derivedColumnEntity;
    }

    private String buildQualifiedNameForSchemaType(String qualifiedNameForParent, String schemaType, ReportElement element) {
        return QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, schemaType, element.getName() + Constants.TYPE_SUFFIX);
    }

    /**
     *
     * @param qualifiedNameForType qualified name for the schema attribute
     * @param schemaAttributeEntity entity describing the schema entity
     * @param schemaAttributeTypeName typename for the schema type
     * @return
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeDefNotKnownException
     * @throws PagingErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws ClassificationErrorException
     */
    protected EntityDetail addSchemaType(String qualifiedNameForType, EntityDetail schemaAttributeEntity, String schemaAttributeTypeName) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException {


        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                                                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForType)
                                                .build();

        EntityDetail schemaTypeEntity = omEntityDao.addEntity(schemaAttributeTypeName,
                                                                        qualifiedNameForType,
                                                                        typeProperties,
                                                                        false);

        omEntityDao.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                                            schemaAttributeEntity.getGUID(),
                                            schemaTypeEntity.getGUID(),
                                            new InstanceProperties());
        return schemaTypeEntity;
    }

    /**
     *
     * @param reportColumn object describing the report column
     * @param derivedColumnEntity entity describing the derived column
     * @throws UserNotAuthorizedException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     * @throws StatusNotSupportedException
     * @throws TypeDefNotKnownException
     * @throws EntityNotKnownException
     */
    private void addBusinessTerm(ReportColumn reportColumn, EntityDetail derivedColumnEntity) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, StatusNotSupportedException, EntityNotKnownException {
        String businessTermGuid = entityReferenceResolver.getBusinessTermGuid(reportColumn.getBusinessTerm());
            if (!StringUtils.isEmpty(businessTermGuid)) {
                omEntityDao.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                                            derivedColumnEntity.getGUID(),
                                            businessTermGuid,
                        new InstanceProperties());
            }
    }

    /**
     *
     * @param reportColumn object describing the report column
     * @param derivedColumnEntity entity describing the derived column
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeDefNotKnownException
     * @throws PagingErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     */
    private void addQueryTargets(ReportColumn reportColumn, EntityDetail derivedColumnEntity) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException {
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
                                                    schemaQueryImplProperties);

            } else {
                String message = MessageFormat.format("source column not found, unable to add relationship between column {0} and source {1}", reportColumn.getName(), source.toString());
                log.error(message);
                throw new RuntimeException(message);
            }
        }
    }


    /**
     * \
     * @param reportEntity entity describing the report
     * @param qualifiedNameForComplexSchemaType qualified name for complex schema type
     * @param complexSchemaTypeProperties properties of the complex schema type
     * @return
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeDefNotKnownException
     * @throws PagingErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws ClassificationErrorException
     */
    protected EntityDetail addReportSchemaType(EntityDetail reportEntity, String qualifiedNameForComplexSchemaType, InstanceProperties complexSchemaTypeProperties) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException {
        EntityDetail complexSchemaTypeEntity;
        complexSchemaTypeEntity = omEntityDao.addEntity(Constants.COMPLEX_SCHEMA_TYPE,
                                                                  qualifiedNameForComplexSchemaType,
                                                                  complexSchemaTypeProperties,
                                                       null,
                                                    false);

        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                                                reportEntity.getGUID(),
                                                complexSchemaTypeEntity.getGUID(),
                new InstanceProperties());
        return complexSchemaTypeEntity;
    }


    /**
     *
     * @param existingAssignments semantic assignment to be deleted
     * @throws RepositoryErrorException
     * @throws UserNotAuthorizedException
     * @throws InvalidParameterException
     * @throws RelationshipNotDeletedException
     * @throws RelationshipNotKnownException
     * @throws FunctionNotSupportedException
     */
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
