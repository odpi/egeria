/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.CollectionMemberStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
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
public class OpenMetadataTypesArchive4_0
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "4.0";
    private static final String                  originatorName     = "Egeria";
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
    public OpenMetadataTypesArchive4_0()
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
    public OpenMetadataTypesArchive4_0(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_15 previousTypes = new OpenMetadataTypesArchive3_15(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0021Collections();
        update0137ToDos();
        add0220DataFileCollectionDataSet();
        add0224TableDataSet();
        add0239DeployedReportType();
        update0385ControlledGlossaries();
        update0462GovernanceActionProcesses();
        create0464DynamicIntegrationGroups();
        update0470IncidentClassifierSet();
        update0484AgreementActor();
        update0545ReferenceData();
        update0720InformationSupplyChains();
        addFormulaTypeAttribute();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0385ControlledGlossaries()
    {
        this.archiveBuilder.addTypeDefPatch(updateControlledGlossaryTermEntity());
        this.archiveBuilder.addRelationshipDef(addGlossaryTermEvolutionRelationship());
    }


    private TypeDefPatch updateControlledGlossaryTermEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONTROLLED_GLOSSARY_TERM.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_DEFINED_STATUS));

        typeDefPatch.setPropertyDefinitions(properties);

        /*
         * Update the valid instance statuses
         */
        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();

        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);

        typeDefPatch.setValidInstanceStatusList(validInstanceStatusList);

        return typeDefPatch;
    }


    /*
     * Deprecated next release
     */
    private RelationshipDef addGlossaryTermEvolutionRelationship()
    {
        final String guid            = "b323c9cf-f254-49c7-a391-11222e9da70f";
        final String name            = "GlossaryTermEvolution";
        final String description     = "Links a live glossary term with a future version of the term.";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "ControlledGlossaryTerm";
        final String                     end1AttributeName            = "glossaryTermUpdates";
        final String                     end1AttributeDescription     = "A glossary term that contains proposed updates to the live glossary term.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "liveGlossaryTerm";
        final String                     end2AttributeDescription     = "The approved term that is in use.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0021Collections()
    {
        this.archiveBuilder.addEnumDef(getMembershipStatusEnum());
        this.archiveBuilder.addTypeDefPatch(updateCollectionMembershipRelationship());
    }

    private EnumDef getMembershipStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(CollectionMemberStatus.getOpenTypeGUID(),
                                                        CollectionMemberStatus.getOpenTypeName(),
                                                        CollectionMemberStatus.getOpenTypeDescription(),
                                                        CollectionMemberStatus.getOpenTypeDescriptionGUID(),
                                                        CollectionMemberStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (CollectionMemberStatus collectionMemberStatus : CollectionMemberStatus.values())
        {
            elementDef = archiveHelper.getEnumElementDef(collectionMemberStatus.getOrdinal(),
                                                         collectionMemberStatus.getName(),
                                                         collectionMemberStatus.getDescription(),
                                                         collectionMemberStatus.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (collectionMemberStatus.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private TypeDefPatch updateCollectionMembershipRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MEMBERSHIP_RATIONALE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPRESSION));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.MEMBERSHIP_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIDENCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0137ToDos()
    {
        this.archiveBuilder.addTypeDefPatch(updateActionTargetRelationship());
    }

    private TypeDefPatch updateActionTargetRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.TO_DO_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_MESSAGE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0220DataFileCollectionDataSet()
    {
        this.archiveBuilder.addEntityDef(getDataFileCollectionDataSetEntity());

    }

    private EntityDef getDataFileCollectionDataSetEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_FILE_COLLECTION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName));
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0224TableDataSet()
    {
        this.archiveBuilder.addEntityDef(getTableDataSetEntity());

    }

    private EntityDef getTableDataSetEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TABLE_DATA_SET,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName));
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0239DeployedReportType()
    {
        this.archiveBuilder.addEntityDef(getDeployedReportTypeEntity());
    }

    private EntityDef getDeployedReportTypeEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DEPLOYED_REPORT_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.AUTHOR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.URL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CREATED_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LAST_MODIFIED_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LAST_MODIFIER));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0462GovernanceActionProcesses()
    {
        this.archiveBuilder.addTypeDefPatch(update0462GovernanceActionExecutorRelationship());
    }

    private TypeDefPatch update0462GovernanceActionExecutorRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETER_FILTER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETER_MAP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_FILTER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_MAP));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void create0464DynamicIntegrationGroups()
    {
        this.archiveBuilder.addEntityDef(addIntegrationGroupEntity());
        this.archiveBuilder.addEntityDef(addIntegrationConnectorEntity());
        this.archiveBuilder.addEntityDef(addIntegrationReportEntity());
        this.archiveBuilder.addRelationshipDef(addRegisteredIntegrationConnectorRelationship());
        this.archiveBuilder.addRelationshipDef(addCatalogTargetRelationship());
        this.archiveBuilder.addRelationshipDef(addRelatedIntegrationReportRelationship());
    }


    private EntityDef addIntegrationGroupEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.INTEGRATION_GROUP,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }

    private EntityDef addIntegrationConnectorEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.INTEGRATION_CONNECTOR,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_CONNECTOR.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USES_BLOCKING_CALLS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addIntegrationReportEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.INTEGRATION_REPORT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SERVER_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFRESH_START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFRESH_COMPLETION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CREATED_ELEMENTS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATED_ELEMENTS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETED_ELEMENTS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addRegisteredIntegrationConnectorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "includedInIntegrationGroups";
        final String                     end1AttributeDescription     = "An integration group that this integration connector is a member of.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.INTEGRATION_GROUP.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "registeredIntegrationConnectors";
        final String                     end2AttributeDescription     = "An integration connector that should run as part of the integration group.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.INTEGRATION_CONNECTOR.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_USER_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFRESH_TIME_INTERVAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STOP_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GENERATE_INTEGRATION_REPORT));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.PERMITTED_SYNCHRONIZATION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addCatalogTargetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "cataloguedByConnectors";
        final String                     end1AttributeDescription     = "An integration connector managing metadata synchronization.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.INTEGRATION_CONNECTOR.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "catalogTargets";
        final String                     end2AttributeDescription     = "An open metadata element that the integration connector is working on.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CATALOG_TARGET_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addRelatedIntegrationReportRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.RELATED_INTEGRATION_REPORT,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "anchorSubject";
        final String                     end1AttributeDescription     = "The anchor entity that the integration report describes.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "integrationReports";
        final String                     end2AttributeDescription     = "A description of the changes made to the anchor entity by an integration " +
                "report.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.INTEGRATION_REPORT.typeName),
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

    private void update0545ReferenceData()
    {
        this.archiveBuilder.addTypeDefPatch(updateReferenceValueAssignment());
    }


    private TypeDefPatch updateReferenceValueAssignment()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ATTRIBUTE_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void addFormulaTypeAttribute()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataSet());
        this.archiveBuilder.addTypeDefPatch(updateProcess());
        this.archiveBuilder.addTypeDefPatch(updateCalculatedValue());
        this.archiveBuilder.addTypeDefPatch(updateProcessCall());
        this.archiveBuilder.addTypeDefPatch(updateDataFlow());
    }


    private TypeDefPatch updateDataSet()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_SET.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateProcess()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROCESS.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateCalculatedValue()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateProcessCall()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROCESS_CALL.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateDataFlow()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_FLOW.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0470IncidentClassifierSet()
    {
        this.archiveBuilder.addTypeDefPatch(updateIncidentClassifierSetClassification());
    }

    private TypeDefPatch updateIncidentClassifierSetClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "IncidentClassifierSet";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "incidentClassifierCategory";
        final String attribute1Description     = "The category of classifiers used to set the incidentClassifiers in IncidentReport.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0484AgreementActor()
    {
        this.archiveBuilder.addTypeDefPatch(updateAgreementActorRelationship());
    }

    private TypeDefPatch updateAgreementActorRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "AgreementActor";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0720InformationSupplyChains()
    {
        this.archiveBuilder.addRelationshipDef(getInformationSupplyChainLinkRelationship());
    }

    private RelationshipDef getInformationSupplyChainLinkRelationship()
    {
        final String guid            = "207e5130-ab7c-4048-9249-a63a43c13d60";
        final String name            = "InformationSupplyChainLink";
        final String description     = "A link between two related information supply chain segments -or to their source or destination.";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supplyFrom";
        final String                     end1AttributeDescription     = "Logical source of the information supply chain.";
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
        final String                     end2AttributeName            = "supplyTo";
        final String                     end2AttributeDescription     = "Logical destination of an information supply chain.";
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
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

