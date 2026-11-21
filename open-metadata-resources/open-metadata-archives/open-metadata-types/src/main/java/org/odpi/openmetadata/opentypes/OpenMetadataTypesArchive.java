/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
    private static final String                  archiveVersion     = "6.4-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1769277597779L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.4-SNAPSHOT";


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
        OpenMetadataTypesArchive6_3 previousTypes = new OpenMetadataTypesArchive6_3(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        add0018Decisions();
        update0130Projects();
        add0208DataValues();
        update0280SoftwareDevelopmentAssets();
        add0520ProcessSchemas();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Decisions, the assessments that contributed to them, and their outcomes.
     */
    private void add0018Decisions()
    {
        this.archiveBuilder.addEntityDef(getDecisionEntity());

        this.archiveBuilder.addRelationshipDef(getOutcomeRelationship());
        this.archiveBuilder.addRelationshipDef(getAssessmentRelationship());
    }


    /**
     * A Decision documents a decision that has been made.
     *
     * @return entity definition
     */
    private EntityDef getDecisionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DECISION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.AUTHORED_REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SUMMARY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IMPORTANCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPOSITION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IMPLICATIONS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LAST_REVIEW_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NEXT_REVIEW_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPIRY_TIME));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * Outcome links a decision to an element that represents an outcome of the decision.
     *
     * @return relationship definition
     */
    private RelationshipDef getOutcomeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.OUTCOME_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "associatedDecisions";
        final String                     end1AttributeDescription     = "The decisions that this element is an outcome of.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DECISION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "decisionOutcomes";
        final String                     end2AttributeDescription     = "The elements that represent the outcomes of the decision.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IMPORTANCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPOSITION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /**
     * Assessment links an element that was assessed to the decision that the assessment contributed to.
     * It is a multi-link relationship since the same element may be assessed many times for the same decision.
     *
     * @return relationship definition
     */
    private RelationshipDef getAssessmentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSESSMENT_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        /*
         * There can be many relationships of this type between the same two elements.
         */
        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "inputsToDecision";
        final String                     end1AttributeDescription     = "The elements that were assessed to make the decision.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "assessmentDecisions";
        final String                     end2AttributeDescription     = "The decisions that an assessment of this element contributed to.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DECISION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSESSOR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSESSOR_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSESSOR_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSESSMENT_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPIRY_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Elements that a project is working on.
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addRelationshipDef(getProjectTargetRelationship());
    }


    /**
     * ProjectTarget identifies an element that the project is to work on.
     *
     * @return relationship definition
     */
    private RelationshipDef getProjectTargetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROJECT_TARGET_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "workedOnByProjects";
        final String                     end1AttributeDescription     = "The projects that are working on this element.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "projectTargets";
        final String                     end2AttributeDescription     = "The elements that the project is to work on.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Data values that are stored, or referenced, in open metadata.
     */
    private void add0208DataValues()
    {
        this.archiveBuilder.addEntityDef(getDataValueEntity());
        this.archiveBuilder.addEntityDef(getLiteralDataValueEntity());

        this.archiveBuilder.addRelationshipDef(getDataValueSourceRelationship());
        this.archiveBuilder.addRelationshipDef(getProcessingVariableRelationship());
        this.archiveBuilder.addRelationshipDef(getEmbeddedDataValueRelationship());
    }


    /**
     * A DataValue describes a particular data value.  This may be a primitive value or a complex value such as an array,
     * map, nested structure, multidimensional cube, or graph.
     *
     * @return entity definition
     */
    private EntityDef getDataValueEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_VALUE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_FORMAT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LENGTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FINGERPRINT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FINGERPRINT_ALGORITHM));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.HASH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.HASH_ALGORITHM));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * A LiteralDataValue is a data value where the data is stored in the open metadata element.
     *
     * @return entity definition
     */
    private EntityDef getLiteralDataValueEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.LITERAL_DATA_VALUE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_VALUE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_VALUE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_VALUE_LIST));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_VALUE_MAP));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * DataValueSource links a data value to the asset for the digital resource where the data is stored.
     *
     * @return relationship definition
     */
    private RelationshipDef getDataValueSourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_VALUE_SOURCE_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "providesDataValues";
        final String                     end1AttributeDescription     = "The data values that are stored in the digital resource.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_VALUE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "dataValueSources";
        final String                     end2AttributeDescription     = "The assets for the digital resources where the data is stored.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /**
     * ProcessingVariable links an element to a data value that is used to configure its processing.
     *
     * @return relationship definition
     */
    private RelationshipDef getProcessingVariableRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROCESSING_VARIABLE_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dataValueUses";
        final String                     end1AttributeDescription     = "The elements that use the data value to configure their processing.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "processingVariables";
        final String                     end2AttributeDescription     = "The data values used to configure the processing of the element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_VALUE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POSITION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /**
     * EmbeddedDataValue links a data value to another data value that is embedded in it.
     *
     * @return relationship definition
     */
    private RelationshipDef getEmbeddedDataValueRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EMBEDDED_DATA_VALUE_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentDataValues";
        final String                     end1AttributeDescription     = "The data values that this data value is embedded in.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_VALUE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "embeddedDataValues";
        final String                     end2AttributeDescription     = "The data values that are embedded in this data value.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_VALUE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POSITION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Identify the element holding the files for a branch of a source code repository.
     */
    private void update0280SoftwareDevelopmentAssets()
    {
        this.archiveBuilder.addClassificationDef(getBranchClassification());
    }


    /**
     * Branch identifies the element that holds the files for a specific branch of a source code repository.
     *
     * @return classification def
     */
    private ClassificationDef getBranchClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.BRANCH_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PURPOSE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.URL));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * The types of data values that are used to initialize a process when it runs.
     */
    private void add0520ProcessSchemas()
    {
        this.archiveBuilder.addRelationshipDef(getSupportedVariableTypeRelationship());
    }


    /**
     * SupportedVariableType describes a type of data value that is used to initialize/configure an element such as a process.
     *
     * @return relationship definition
     */
    private RelationshipDef getSupportedVariableTypeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SUPPORTED_VARIABLE_TYPE_RELATIONSHIP,
                                                                                this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "processVariableTypeUses";
        final String                     end1AttributeDescription     = "The elements that are configured with data values of this type.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "processVariableTypes";
        final String                     end2AttributeDescription     = "The types of data values used to configure the element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POSITION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_CARDINALITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_CARDINALITY));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


}
