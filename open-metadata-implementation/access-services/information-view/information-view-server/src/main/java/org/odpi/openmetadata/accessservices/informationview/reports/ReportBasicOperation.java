/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class ReportBasicOperation extends BasicOperation{

    private static final Logger log = LoggerFactory.getLogger(ReportBasicOperation.class);

    public ReportBasicOperation(OMEntityDao omEntityDao,LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao,lookupHelper, helper, auditLog);

    }


    /**
     * @param userId user id of user submitting the request
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param allElements all elements linked to the current element
     */
    public void addElements(String userId, String qualifiedNameForParent, String parentGuid, String registrationGuid,
                            String registrationQualifiedName, List<ReportElement> allElements) {
        if (allElements == null || allElements.isEmpty())
            return;
        allElements.parallelStream().forEach(e -> addReportElement(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, e));
    }


    /**
     * @param userId user id of user submitting the request    
     * @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param element object describing the current element
     */
    public void addReportElement(String userId, String qualifiedNameForParent, String parentGuid, String registrationGuid, String registrationQualifiedName, ReportElement element) {
            if (element instanceof ReportSection) {
                addReportSection(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, (ReportSection) element);
            } else if (element instanceof ReportColumn) {
                addReportColumn(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, (ReportColumn) element);
            }
    }

    /**
     *
     *
     * @param userId user id of user submitting the request* @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param reportSection object describing the current section in the report
     */
    private void addReportSection(String userId, String qualifiedNameForParent, String parentGuid, String registrationGuid, String registrationQualifiedName, ReportSection reportSection){
        EntityDetail typeEntity = addSectionAndSectionType(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, reportSection);
        String qualifiedNameForSection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_ATTRIBUTE, reportSection.getName());
        addElements(userId, qualifiedNameForSection, typeEntity.getGUID(), registrationGuid, registrationQualifiedName, reportSection.getElements());
    }


    /**
     *
     *
     * @param userId user id of user submitting the request* @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid guid of software server capability source
     * @param registrationQualifiedName qualified name of software server capability source
     * @param reportSection object describing the current section in the report
     * @return
     */
    protected EntityDetail addSectionAndSectionType(String userId, String qualifiedNameForParent, String parentGuid,
                                                    String registrationGuid, String registrationQualifiedName, ReportSection reportSection) {

        String qualifiedNameForSection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_ATTRIBUTE, reportSection.getName());
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSection)
                .withStringProperty(Constants.ATTRIBUTE_NAME, reportSection.getName())
                .build();
        EntityDetail sectionEntity = omEntityDao.addExternalEntity(userId,
                                                            Constants.DOCUMENT_SCHEMA_ATTRIBUTE,
                                                            qualifiedNameForSection,
                                                            registrationGuid,
                                                            registrationQualifiedName,
                                                            sectionProperties,
                                                            null,
                                                            false);

        omEntityDao.addExternalRelationship(userId,
                                            Constants.ATTRIBUTE_FOR_SCHEMA,
                                            registrationGuid,
                                            registrationQualifiedName,
                                            parentGuid,
                                            sectionEntity.getGUID(),
                                            new InstanceProperties());

        String qualifiedNameForSectionType = buildQualifiedNameForSchemaType(qualifiedNameForParent, Constants.DOCUMENT_SCHEMA_TYPE, reportSection);
        InstanceProperties schemaAttributeTypeProperties = new InstanceProperties();
        schemaAttributeTypeProperties = helper.addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, schemaAttributeTypeProperties, Constants.QUALIFIED_NAME, qualifiedNameForSectionType, "addSchemaType");
        return createSchemaType(userId, Constants.DOCUMENT_SCHEMA_TYPE,  qualifiedNameForSectionType, registrationGuid, registrationQualifiedName, schemaAttributeTypeProperties, Constants.SCHEMA_ATTRIBUTE_TYPE, sectionEntity.getGUID() );
    }


    /**
     *
     *
     * @param userId user id of user submitting the request* @param qualifiedNameForParent qualified name of the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param reportColumn object describing the current column in the report
     * @return entity representing column
     */
    protected EntityDetail addReportColumn(String userId,
                                           String qualifiedNameForParent,
                                           String parentGuid,
                                           String registrationGuid,
                                           String registrationQualifiedName,
                                           ReportColumn reportColumn) {

        String qualifiedNameForColumn = QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, Constants.DERIVED_SCHEMA_ATTRIBUTE, reportColumn.getName());
        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                                                                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                                                                        .withStringProperty(Constants.ATTRIBUTE_NAME, reportColumn.getName())
                                                                        .withStringProperty(Constants.FORMULA, reportColumn.getFormula())
                                                                        .build();
        EntityDetail derivedColumnEntity = omEntityDao.addExternalEntity(userId,
                                                                        Constants.DERIVED_SCHEMA_ATTRIBUTE,
                                                                        qualifiedNameForColumn,
                                                                        registrationGuid,
                                                                        registrationQualifiedName,
                                                                        columnProperties,
                                                                        null,
                                                                        false);

        omEntityDao.addExternalRelationship(userId,
                                            Constants.ATTRIBUTE_FOR_SCHEMA,
                                            registrationGuid,
                                            registrationQualifiedName,
                                            parentGuid,
                                            derivedColumnEntity.getGUID(),
                                            new InstanceProperties());


        addQueryTargets(userId, registrationGuid, registrationQualifiedName, reportColumn.getSources(), derivedColumnEntity);
        addSemanticAssignments(userId, registrationGuid, registrationQualifiedName, reportColumn.getBusinessTerms(), derivedColumnEntity);
        String qualifiedNameForColumnType = buildQualifiedNameForSchemaType(qualifiedNameForParent, Constants.SCHEMA_TYPE, reportColumn);
        InstanceProperties schemaAttributeTypeProperties = new InstanceProperties();
        schemaAttributeTypeProperties = helper.addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, schemaAttributeTypeProperties, Constants.QUALIFIED_NAME, qualifiedNameForColumnType, "addReportColumn");
        createSchemaType(userId, Constants.SCHEMA_TYPE,  qualifiedNameForColumnType, registrationGuid, registrationQualifiedName, schemaAttributeTypeProperties, Constants.SCHEMA_ATTRIBUTE_TYPE, derivedColumnEntity.getGUID() );

        return derivedColumnEntity;
    }
}