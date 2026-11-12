/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.ControlImplementationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenMetadataTypesArchive builds an open metadata archive containing all the standard open metadata types.
 * These types have hardcoded dates and guids so that however many times this archive is rebuilt, it will
 * produce the same content.
 * <p>
 * Details of the open metadata types are documented on the wiki:
 * <a href="https://egeria-project.org/types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "6.3-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1769277597779L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.3-SNAPSHOT";


    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive()
    {
        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     archiveVersion,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     null);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Chained constructor sets up the archive builder.  This in turn sets up the header for the archive.
     *
     * @param archiveBuilder accumulator for types
     */
    public OpenMetadataTypesArchive(OMRSArchiveBuilder archiveBuilder)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Return the unique identifier for this archive.
     *
     * @return String guid
     */
    public String getArchiveGUID()
    {
        return archiveGUID;
    }


    /**
     * Returns the open metadata type archive containing all the standard open metadata types.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (this.archiveBuilder != null)
        {
            /*
             * Build the type archive.
             */
            this.getOriginalTypes();

            /*
             * The completed archive is ready to be packaged up and returned
             */
            return this.archiveBuilder.getOpenMetadataArchive();
        }
        else
        {
            /*
             * This is a logic error since it means the creation of the archive builder threw an exception
             * in the constructor and so this object should not be used.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.ARCHIVE_UNAVAILABLE.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Add the types from this archive to the archive builder supplied in the
     * constructor.
     */
    public void getOriginalTypes()
    {
        OpenMetadataTypesArchive6_2 previousTypes = new OpenMetadataTypesArchive6_2(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        update0795UsageActivity();
        update0540DataValueAssignment();
        update0750LineageRelationship();
        update0210DataSetContent();
        update0512DerivedSchemaTypeQueryTarget();
        add0482StatementsOfApplicability();
        add0665SecurityAnnotations();
        update0239Reports();
        update0537DisplaySchemas();
        update0581DataFieldImplementation();
        add0582ReportSpecifications();
        update0725SmartCollections();
        update0424GovernanceZones();
        update0505SchemaAttributes();
        update0130Projects();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0239Reports()
    {
        this.archiveBuilder.addTypeDefPatch(updateReportEntity());
        this.archiveBuilder.addEntityDef(getDashboardEntity());
    }


    /**
     * Report gains the parameters used to create it and the format of its content.
     *
     * @return patch
     */
    private TypeDefPatch updateReportEntity()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.REPORT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REPORT_PARAMETERS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OUTPUT_FORMAT));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * A Dashboard is a DataSet that is a collection of reports.  It provides the layout information so that the
     * display of the embedded reports is logical and easy to follow.
     *
     * @return entity definition
     */
    private EntityDef getDashboardEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DASHBOARD,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ALIASES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFRESH_TIME_INTERVAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LAYOUT_STYLE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0537DisplaySchemas()
    {
        this.archiveBuilder.addTypeDefPatch(updateDisplayDataContainerEntity());
        this.archiveBuilder.addTypeDefPatch(updateDisplayDataFieldEntity());
    }


    /**
     * DisplayDataContainer gains the attributes that describe how it is rendered and placed in the display.
     *
     * @return patch
     */
    private TypeDefPatch updateDisplayDataContainerEntity()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DISPLAY_DATA_CONTAINER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RENDERER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PLACEMENT_KIND));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SPAN));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRESENTATION_STYLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MARKDOWN_CONTENT));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * DisplayDataField gains the attributes that describe how it is placed in the display.
     *
     * @return patch
     */
    private TypeDefPatch updateDisplayDataFieldEntity()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DISPLAY_DATA_FIELD.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PLACEMENT_KIND));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SPAN));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRESENTATION_STYLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MARKDOWN_CONTENT));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0581DataFieldImplementation()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaAttributeDefinitionRelationship());
        this.archiveBuilder.addTypeDefPatch(updateSchemaTypeDefinitionRelationship());
    }


    /**
     * A data field may describe many schema attributes (for example, the same column in different databases),
     * so end 2 of SchemaAttributeDefinition becomes many and its attribute name becomes plural.
     *
     * @return patch
     */
    private TypeDefPatch updateSchemaAttributeDefinitionRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_ATTRIBUTE_DEFINITION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(OpenMetadataType.SCHEMA_ATTRIBUTE_DEFINITION_RELATIONSHIP.description);
        typeDefPatch.setDescriptionGUID(OpenMetadataType.SCHEMA_ATTRIBUTE_DEFINITION_RELATIONSHIP.descriptionGUID);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "equivalentSchemaAttributes";
        final String                     end2AttributeDescription     = "Schema attributes that implement the data field.";
        final String                     end2AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                                    end2AttributeName,
                                                                                    end2AttributeDescription,
                                                                                    end2AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    /**
     * A data structure may describe many schema types (for example, the same table in different databases),
     * so end 2 of SchemaTypeDefinition becomes many and its attribute name becomes plural.
     *
     * @return patch
     */
    private TypeDefPatch updateSchemaTypeDefinitionRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_TYPE_DEFINITION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(OpenMetadataType.SCHEMA_TYPE_DEFINITION_RELATIONSHIP.description);
        typeDefPatch.setDescriptionGUID(OpenMetadataType.SCHEMA_TYPE_DEFINITION_RELATIONSHIP.descriptionGUID);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "equivalentSchemaTypes";
        final String                     end2AttributeDescription     = "Schema types that implement the data structure.";
        final String                     end2AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                                    end2AttributeName,
                                                                                    end2AttributeDescription,
                                                                                    end2AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0582ReportSpecifications()
    {
        this.archiveBuilder.addEntityDef(getReportSpecEntity());
        this.archiveBuilder.addEntityDef(getMeasurementSpecificationEntity());
        this.archiveBuilder.addRelationshipDef(getReportTypeInstanceRelationship());
        this.archiveBuilder.addRelationshipDef(getBasisDataFieldRelationship());
        this.archiveBuilder.addRelationshipDef(getAsOfDataFieldRelationship());
    }


    /**
     * A ReportSpec is a specialized ReportType for reports about open lineage and related data.
     *
     * @return entity definition
     */
    private EntityDef getReportSpecEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.REPORT_SPEC,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REPORT_TYPE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MICROFLOW));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ALIASES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OUTPUT_TYPES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INCLUDE_PREAMBLE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * A MeasurementSpecification is a type of DataValueSpecification that describes a measurement that is part of a report.
     *
     * @return entity definition
     */
    private EntityDef getMeasurementSpecificationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.MEASUREMENT_SPECIFICATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_VALUE_SPECIFICATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETENESS_DECLARED));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIDENCE_FORM));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UNCERTAINTY_FORM));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ABSENCE_SENTENCES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * ReportTypeInstance links a report type to a report that was generated using this type.
     *
     * @return relationship definition
     */
    private RelationshipDef getReportTypeInstanceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REPORT_TYPE_INSTANCE_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "derivedFromReportType";
        final String                     end1AttributeDescription     = "The report type used to generate the report.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REPORT_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "reportTypeInstances";
        final String                     end2AttributeDescription     = "The reports that were generated using this report type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REPORT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /**
     * BasisDataField links a measurement specification to the data field that describes the basis for the measurement.
     * For example, the denominator for a percentage or the total a share is taken from.
     *
     * @return relationship definition
     */
    private RelationshipDef getBasisDataFieldRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.BASIS_DATA_FIELD_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "providesBasisForMeasurements";
        final String                     end1AttributeDescription     = "The measurements that this data field provides the basis for.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.MEASUREMENT_SPECIFICATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "basisDataField";
        final String                     end2AttributeDescription     = "The data field that describes the basis for the measurement.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /**
     * AsOfDataField links a measurement specification to the data field that describes the time of the measurement.
     *
     * @return relationship definition
     */
    private RelationshipDef getAsOfDataFieldRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.AS_OF_DATA_FIELD_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "providesAsOfTimeForMeasurements";
        final String                     end1AttributeDescription     = "The measurements that this data field provides the time of measurement for.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.MEASUREMENT_SPECIFICATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "asOfDataField";
        final String                     end2AttributeDescription     = "The data field that describes the time of the measurement.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0725SmartCollections()
    {
        this.archiveBuilder.addRelationshipDef(getReportRunOfRelationship());
    }


    /**
     * ReportRunOf links a results set to a report that was generated from it.
     *
     * @return relationship definition
     */
    private RelationshipDef getReportRunOfRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REPORT_RUN_OF_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usesResultsSets";
        final String                     end1AttributeDescription     = "The results sets used to generate the report.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.RESULTS_SET.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "usedInReports";
        final String                     end2AttributeDescription     = "The reports that were generated from the results set.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REPORT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RESULT_STATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RESULT_STATE_REASON));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RESULT_LOCATION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0665SecurityAnnotations()
    {
        this.archiveBuilder.addEntityDef(getSecurityFindingAnnotationEntity());
        this.archiveBuilder.addEntityDef(getVulnerabilityAnnotationEntity());
        this.archiveBuilder.addEntityDef(getPenetrationTestFindingAnnotationEntity());
        this.archiveBuilder.addEntityDef(getSecurityPostureAnnotationEntity());
    }


    /**
     * A weakness found in a resource by a security test, such as a vulnerability scan or a penetration test.
     *
     * @return entity definition
     */
    private EntityDef getSecurityFindingAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SECURITY_FINDING_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SEVERITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CVSS_SCORE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CVSS_VECTOR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.VULNERABILITY_IDENTIFIERS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.WEAKNESS_IDENTIFIERS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.AFFECTED_LOCATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EVIDENCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REMEDIATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLOIT_AVAILABLE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * A known vulnerability in the software used by a resource, typically found by a vulnerability scan.
     *
     * @return entity definition
     */
    private EntityDef getVulnerabilityAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.VULNERABILITY_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SECURITY_FINDING_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PACKAGE_URL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INSTALLED_VERSION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIXED_VERSION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PATCH_AVAILABLE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * A weakness that a penetration test was able to exploit, with how it was exploited and what was gained.
     *
     * @return entity definition
     */
    private EntityDef getPenetrationTestFindingAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.PENETRATION_TEST_FINDING_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SECURITY_FINDING_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ATTACK_VECTOR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ATTACK_TECHNIQUES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRIVILEGES_GAINED));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPROMISED_RESOURCES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REPRODUCTION_STEPS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * A summary of the findings and coverage of one run of a security test.
     *
     * @return entity definition
     */
    private EntityDef getSecurityPostureAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SECURITY_POSTURE_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FINDING_COUNTS_BY_SEVERITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RESOURCES_IN_SCOPE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RESOURCES_TESTED_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NEW_FINDING_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RESOLVED_FINDING_COUNT));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0482StatementsOfApplicability()
    {
        this.archiveBuilder.addEnumDef(getControlImplementationStatusEnum());
        this.archiveBuilder.addEntityDef(getStatementOfApplicabilityEntity());
        this.archiveBuilder.addRelationshipDef(getControlApplicabilityRelationship());
    }


    /**
     * ControlImplementationStatus records how far a control listed in a statement of applicability has been implemented.
     *
     * @return enum definition
     */
    private EnumDef getControlImplementationStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ControlImplementationStatus.getOpenTypeGUID(),
                                                        ControlImplementationStatus.getOpenTypeName(),
                                                        ControlImplementationStatus.getOpenTypeDescription(),
                                                        ControlImplementationStatus.getOpenTypeDescriptionGUID(),
                                                        ControlImplementationStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ControlImplementationStatus controlImplementationStatus : ControlImplementationStatus.values())
        {
            elementDef = archiveHelper.getEnumElementDef(controlImplementationStatus.getOrdinal(),
                                                         controlImplementationStatus.name(),
                                                         controlImplementationStatus.getDescription(),
                                                         controlImplementationStatus.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (controlImplementationStatus.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    /**
     * A StatementOfApplicability records which controls from a standard - such as the Annex A controls of
     * ISO/IEC 27001 - apply to an organization, why, and how far each one has been implemented.  The controls
     * are linked to it with the ControlApplicability relationship.  It is an AuthoredReferenceable so that its
     * authors, version and content status (for example, DRAFT then APPROVED) are recorded as it is reviewed
     * for each audit.
     *
     * @return entity definition
     */
    private EntityDef getStatementOfApplicabilityEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.STATEMENT_OF_APPLICABILITY,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.AUTHORED_REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REVIEW_DATE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * ControlApplicability links a statement of applicability to a control defined by a standard, and records
     * whether the control applies, why, and how far it has been implemented.
     *
     * @return relationship definition
     */
    private RelationshipDef getControlApplicabilityRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONTROL_APPLICABILITY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "statementsOfApplicability";
        final String                     end1AttributeDescription     = "The statements of applicability that assess whether this control applies.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.STATEMENT_OF_APPLICABILITY.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "assessedControls";
        final String                     end2AttributeDescription     = "The controls from a standard whose applicability is recorded in the statement.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_APPLICABLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.JUSTIFICATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INCLUSION_REASONS));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.IMPLEMENTATION_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IMPLEMENTATION_DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REVIEW_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0210DataSetContent()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataSetContentRelationship());
        this.archiveBuilder.addTypeDefPatch(updateDataSetContentSuperType());
    }


    /**
     * DataSetContent gains the derivation attribute, which records how a tool decided that the data set is a view over
     * the content - for example by matching the data set's endpoint and name with the content's.
     *
     * @return patch
     */
    private TypeDefPatch updateDataSetContentRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DERIVATION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * DataSetContent becomes a LineageRelationship: a data set's values come from its content, so the relationship
     * is part of the lineage graph, and a single query for LineageRelationship retrieves it along with the other
     * lineage relationships.  It inherits label and description.  It already defines iscQualifiedName and derivation
     * itself, which LineageRelationship also defines.  This is a separate patch from the derivation patch above
     * because repositories running earlier 6.3 snapshots have already applied that one.
     *
     * @return patch
     */
    private TypeDefPatch updateDataSetContentSuperType()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getRelationshipDef(OpenMetadataType.LINEAGE_RELATIONSHIP.typeName));

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0512DerivedSchemaTypeQueryTarget()
    {
        this.archiveBuilder.addTypeDefPatch(updateDerivedSchemaTypeQueryTargetRelationship());
    }


    /**
     * DerivedSchemaTypeQueryTarget becomes a LineageRelationship: a derived schema element's value comes from the
     * elements it queries, so the relationship is part of the lineage graph.  It inherits label, description and
     * derivation.  It already defines iscQualifiedName itself, which LineageRelationship also defines.
     *
     * @return patch
     */
    private TypeDefPatch updateDerivedSchemaTypeQueryTargetRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getRelationshipDef(OpenMetadataType.LINEAGE_RELATIONSHIP.typeName));

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0750LineageRelationship()
    {
        this.archiveBuilder.addTypeDefPatch(updateLineageRelationship());
    }


    /**
     * LineageRelationship - and so every lineage relationship type - gains the derivation attribute, which records
     * the rule or process that derived a relationship from other lineage.  The label of a lineage relationship
     * summarizes the data that flows; a tool that derives lineage records its own rule in derivation instead, and
     * uses it to recognize the relationships it maintains.
     *
     * @return patch
     */
    private TypeDefPatch updateLineageRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.LINEAGE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DERIVATION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0424GovernanceZones()
    {
        this.archiveBuilder.addTypeDefPatch(updateZoneMembershipProfileClassification());
    }


    /**
     * ZoneMembershipProfile holds the member counts that the zone profiler maintains.  The securityGroups
     * attribute it was first defined with was copied from SecurityListMembership by mistake - no bean,
     * builder or converter has ever carried it - so it is deprecated.
     *
     * @return patch
     */
    private TypeDefPatch updateZoneMembershipProfileClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        TypeDefAttribute typeDefAttribute = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SECURITY_GROUPS);
        typeDefAttribute.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(typeDefAttribute);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0505SchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaAttributeEntity());
    }


    /**
     * SchemaAttributeProperties has always carried defaultValueOverride, and the builder writes it, but the
     * SchemaAttribute type never declared it - so creating any schema attribute with a default value was
     * rejected by the repository.
     *
     * @return patch
     */
    private TypeDefPatch updateSchemaAttributeEntity()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEFAULT_VALUE_OVERRIDE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateProjectEntity());
    }


    /**
     * A project's scope describes what is in and out of scope for the project.
     *
     * @return patch
     */
    private TypeDefPatch updateProjectEntity()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROJECT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0540DataValueAssignment()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataValueAssignmentRelationship());
    }


    /**
     * DataValueAssignment is labelled like the other relationships whose properties extend
     * LabeledRelationshipProperties - its bean offers a label and a description that the type did not define.
     *
     * @return patch
     */
    private TypeDefPatch updateDataValueAssignmentRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_VALUE_ASSIGNMENT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0795UsageActivity()
    {
        this.archiveBuilder.addClassificationDef(getUsageActivityClassification());
        this.archiveBuilder.addClassificationDef(getMemberPopularityProfileClassification());
    }


    /**
     * Add the UsageActivity classification to count the activity around an anchor element and its anchored
     * elements.  It is typically attached to an Asset, DigitalProduct or GlossaryTerm, and so it is valid on any Referenceable.
     *
     * @return classification def
     */
    private ClassificationDef getUsageActivityClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLICIT_GET_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIND_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_PROPERTY_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_FEEDBACK_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_ATTACHMENT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_FEEDBACK_COUNT));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /**
     * Add the MemberPopularityProfile classification to summarize the usage activity around the members of an
     * element.  It is valid on any Referenceable.
     *
     * @return classification def
     */
    private ClassificationDef getMemberPopularityProfileClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.MEMBER_POPULARITY_PROFILE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLICIT_GET_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLICIT_GET_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIND_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIND_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_PROPERTY_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_PROPERTY_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_FEEDBACK_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_FEEDBACK_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_ATTACHMENT_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_ATTACHMENT_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_FEEDBACK_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_FEEDBACK_COUNT_PROFILE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}
