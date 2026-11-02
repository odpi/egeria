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
        add0482StatementsOfApplicability();
        add0665SecurityAnnotations();
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
    }


    /**
     * DataSetContent gains the derivation attribute, which records how a tool decided that the data set is a view over
     * the content - for example by matching the data set's endpoint and name with the content's.  DataSetContent is
     * not a lineage relationship, so it does not inherit the attribute from LineageRelationship.
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
