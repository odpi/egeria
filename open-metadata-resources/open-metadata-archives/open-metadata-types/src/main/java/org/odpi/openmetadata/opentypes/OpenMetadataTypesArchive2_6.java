/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
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
 * OpenMetadataTypesArchive builds an open metadata archive containing all of the standard open metadata types.
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
public class OpenMetadataTypesArchive2_6
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.6";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1588261366992L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive2_6()
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
    public OpenMetadataTypesArchive2_6(OMRSArchiveBuilder archiveBuilder)
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
     * Returns the open metadata type archive containing all of the standard open metadata types.
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
        OpenMetadataTypesArchive2_5  previousTypes = new OpenMetadataTypesArchive2_5(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0010ManagingMemento();
        update0215MoreProcessTypes();
        update0422GovernanceActionClassifications();
        update0460GovernanceExecutionPoints();
        add0461GovernanceActionEngines();
        add0462GovernanceActionProcesses();
        add0463EngineActions();
        add0465DuplicateProcessing();
        add0470IncidentReports();
        add0550InstanceMetadata();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0010 Add the Memento classification
     */
    private void update0010ManagingMemento()
    {
        this.archiveBuilder.addClassificationDef(addMementoClassification());
    }

    private ClassificationDef addMementoClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.MEMENTO_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARCHIVE_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARCHIVE_USER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARCHIVE_PROCESS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARCHIVE_SERVICE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARCHIVE_METHOD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARCHIVE_PROPERTIES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0010 Add the EmbeddedProcess and TransientEmbeddedProcess entities
     */
    private void update0215MoreProcessTypes()
    {
        this.archiveBuilder.addEntityDef(addEmbeddedProcessEntity());
        this.archiveBuilder.addEntityDef(addTransientEmbeddedProcessEntity());
    }


    private EntityDef addEmbeddedProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EMBEDDED_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ACTION.typeName));
    }


    private EntityDef addTransientEmbeddedProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EMBEDDED_PROCESS.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0422 Add the Impact classification
     */
    private void update0422GovernanceActionClassifications()
    {
        this.archiveBuilder.addEnumDef(getImpactSeverityEnum());
        this.archiveBuilder.addClassificationDef(addImpactClassification());
    }

    private EnumDef getImpactSeverityEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ImpactSeverity.getOpenTypeGUID(),
                                                        ImpactSeverity.getOpenTypeName(),
                                                        ImpactSeverity.getOpenTypeDescription(),
                                                        ImpactSeverity.getOpenTypeDescriptionGUID(),
                                                        ImpactSeverity.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ImpactSeverity enumValues : ImpactSeverity.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValues.getOrdinal(),
                                                         enumValues.getName(),
                                                         enumValues.getDescription(),
                                                         enumValues.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValues.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private ClassificationDef addImpactClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.IMPACT_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIDENCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    public void update0460GovernanceExecutionPoints()
    {


        this.archiveBuilder.addTypeDefPatch(updateControlPointClassification());
        this.archiveBuilder.addTypeDefPatch(updateVerificationPointClassification());
        this.archiveBuilder.addTypeDefPatch(updateEnforcementPointClassification());

    }

    private TypeDefPatch updateControlPointClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONTROL_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateVerificationPointClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.VERIFICATION_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateEnforcementPointClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ENFORCEMENT_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0461 Describe Governance Action Engines and Services
     */
    private void add0461GovernanceActionEngines()
    {
        this.archiveBuilder.addEntityDef(addGovernanceEngineEntity());
        this.archiveBuilder.addEntityDef(addGovernanceServiceEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionEngineEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionServiceEntity());

        this.archiveBuilder.addRelationshipDef(addSupportedGovernanceServiceRelationship());
    }

    private EntityDef addGovernanceEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ENGINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_CAPABILITY.typeName));
    }

    private EntityDef addGovernanceServiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_SERVICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_CONNECTOR.typeName));
    }

    private EntityDef addGovernanceActionEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_ENGINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName));
    }

    private EntityDef addGovernanceActionServiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_SERVICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_SERVICE.typeName));
    }

    private RelationshipDef addSupportedGovernanceServiceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "calledFromGovernanceEngines";
        final String                     end1AttributeDescription     = "Governance Engine making use of the governance service.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "supportedGovernanceServices";
        final String                     end2AttributeDescription     = "Governance service that is part of the governance engine.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_SERVICE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETERS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0462 Describe Governance Action Process Steps
     */
    private void add0462GovernanceActionProcesses()
    {
        this.archiveBuilder.addEntityDef(addGovernanceActionProcessEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionTypeEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionProcessStepEntity());

        this.archiveBuilder.addRelationshipDef(addGovernanceActionProcessFlowRelationship());
        this.archiveBuilder.addRelationshipDef(addNextGovernanceActionProcessStepRelationship());
        this.archiveBuilder.addRelationshipDef(addGovernanceActionExecutorRelationship());
    }

    private EntityDef addGovernanceActionProcessEntity()
    {
        /*
         * Build the Entity
         */
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION.typeName));
    }


    private EntityDef addGovernanceActionTypeEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.WAIT_TIME));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addGovernanceActionProcessStepEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addGovernanceActionProcessFlowRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "triggeredFrom";
        final String                     end1AttributeDescription     = "Governance process that describes the set of process steps.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "firstStep";
        final String                     end2AttributeDescription     = "First step to execute in a governance action process.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addNextGovernanceActionProcessStepRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dependedOnProcessSteps";
        final String                     end1AttributeDescription     = "Governance Action Process Step caller.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "followOnProcessSteps";
        final String                     end2AttributeDescription     = "Governance Action Process Step called.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GUARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MANDATORY_GUARD));


        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addGovernanceActionExecutorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supportsGovernanceActions";
        final String                     end1AttributeDescription     = "Governance action that drives calls to a governance engine.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "governanceActionExecutor";
        final String                     end2AttributeDescription     = "Governance engine that will run the requested action.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETERS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0463EngineActions()
    {
        this.archiveBuilder.addEntityDef(addEngineActionEntity());
    }


    private EntityDef addEngineActionEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ENGINE_ACTION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ACTION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETERS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUESTER_USER_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXECUTOR_ENGINE_GUID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXECUTOR_ENGINE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS_STEP_GUID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS_STEP_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MANDATORY_GUARDS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECEIVED_GUARDS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESSING_ENGINE_USER_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_GUARDS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_MESSAGE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add types for duplicate processing
     */
    public void add0465DuplicateProcessing()
    {
        this.archiveBuilder.addClassificationDef(addKnowDuplicateClassification());
    }

    private ClassificationDef addKnowDuplicateClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  true);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add incident reports
     */
    public void add0470IncidentReports()
    {
        this.archiveBuilder.addEnumDef(getIncidentReportStatusEnum());
        this.archiveBuilder.addEntityDef(addIncidentReportEntity());

        this.archiveBuilder.addRelationshipDef(addImpactedResourceRelationship());
    }


    private EnumDef getIncidentReportStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(IncidentReportStatus.getOpenTypeGUID(),
                                                        IncidentReportStatus.getOpenTypeName(),
                                                        IncidentReportStatus.getOpenTypeDescription(),
                                                        IncidentReportStatus.getOpenTypeDescriptionGUID(),
                                                        IncidentReportStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (CriticalityLevel enumValues : CriticalityLevel.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValues.getOrdinal(),
                                                         enumValues.getName(),
                                                         enumValues.getDescription(),
                                                         enumValues.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValues.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef addIncidentReportEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.INCIDENT_REPORT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REPORT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BACKGROUND));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INCIDENT_CLASSIFIERS));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.INCIDENT_STATUS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef addImpactedResourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "impactedResources";
        final String                     end1AttributeDescription     = "Resources impacted by the incident.";
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
        final String                     end2AttributeName            = "incidentReports";
        final String                     end2AttributeDescription     = "Descriptions of incidents affecting this resource and the action taken.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.INCIDENT_REPORT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add instance metadata classification
     */
    public void add0550InstanceMetadata()
    {
        this.archiveBuilder.addClassificationDef(addInstanceMetadataClassification());
    }

    private ClassificationDef addInstanceMetadataClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INSTANCE_METADATA_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

