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
    private static final String                  archiveVersion     = "6.1-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1769277597779L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.1-SNAPSHOT";


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
        OpenMetadataTypesArchive6_0 previousTypes = new OpenMetadataTypesArchive6_0(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        update0010BaseModel();
        update0019MoreInformation();
        update0112Person();
        update0118ActorRoles();
        update0135ActionsForPeople();
        add0145Perspectives();
        update0340Dictionary();
        update0395SupplementaryProperties();
        update0405GovernanceDrivers();
        update0423SecurityDefinitions();
        update0424GovernanceZones();
        update0455ExceptionManagement();
        update0595DesignPatterns();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(getOpenMetadataRootPatch());
    }

    private TypeDefPatch getOpenMetadataRootPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.OPEN_METADATA_ROOT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LEGAL));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0019MoreInformation()
    {
        this.archiveBuilder.addTypeDefPatch(getResourceListRelationshipPatch());
    }

    private TypeDefPatch getResourceListRelationshipPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        TypeDefAttribute typeDefAttribute = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME);
        typeDefAttribute.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(typeDefAttribute);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0112Person()
    {
        this.archiveBuilder.addTypeDefPatch(getPeerRelationshipPatch());
    }

    private TypeDefPatch getPeerRelationshipPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PEER_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;

    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0118ActorRoles()
    {
        this.archiveBuilder.addTypeDefPatch(getActorRolePatch());
        this.archiveBuilder.addTypeDefPatch(deprecateAssetOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateBusinessOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateCommunityMemberPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateComponentOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateCrowdSourcingContributorPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateDataItemOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateDigitalProductManagerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateGovernanceOfficerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateGovernanceRepresentativePatch());
        this.archiveBuilder.addTypeDefPatch(deprecateLocationOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateNoteLogAuthorPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateProjectManagerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateSolutionOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateSubjectAreaOwnerPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateTeamMemberPatch());
        this.archiveBuilder.addTypeDefPatch(deprecateTeamLeaderPatch());
    }

    private TypeDefPatch getActorRolePatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ACTOR_ROLE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTOR_ROLE_GROUPS));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;

    }

    private TypeDefPatch deprecateAssetOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ASSET_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateBusinessOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.BUSINESS_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateCommunityMemberPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.COMMUNITY_MEMBER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateComponentOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.COMPONENT_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateCrowdSourcingContributorPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CROWD_SOURCING_CONTRIBUTOR.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateDataItemOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_ITEM_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateDigitalProductManagerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DIGITAL_PRODUCT_MANAGER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateGovernanceOfficerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_OFFICER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateGovernanceRepresentativePatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_REPRESENTATIVE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateLocationOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.LOCATION_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateNoteLogAuthorPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.NOTE_LOG_AUTHOR.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateProjectManagerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROJECT_MANAGER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateSolutionOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SOLUTION_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateSubjectAreaOwnerPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SUBJECT_AREA_OWNER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }

    private TypeDefPatch deprecateTeamMemberPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TEAM_MEMBER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }


    private TypeDefPatch deprecateTeamLeaderPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TEAM_LEADER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);


        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0145Perspectives()
    {
        this.archiveBuilder.addEntityDef(getPerspectiveEntity());
        this.archiveBuilder.addEntityDef(getSkillSetEntity());
        this.archiveBuilder.addEntityDef(getSkillEntity());

        this.archiveBuilder.addRelationshipDef(getAssociatedSkillSetRelationship());
    }

    private EntityDef getPerspectiveEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PERSPECTIVE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName));
    }

    private EntityDef getSkillSetEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SKILL_SET,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName));
    }

    private EntityDef getSkillEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SKILL,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.AUTHORED_REFERENCEABLE.typeName));
    }

    private RelationshipDef getAssociatedSkillSetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSOCIATED_SKILL_SET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "actorWithSkills";
        final String                     end1AttributeDescription     = "The actor possessing the skills.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "skillSets";
        final String                     end2AttributeDescription     = "The skills of the actor.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SKILL_SET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0340Dictionary()
    {
        this.archiveBuilder.addClassificationDef(getQuestionClassification());
    }

    private ClassificationDef getQuestionClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.QUESTION_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0395SupplementaryProperties()
    {
        this.archiveBuilder.addTypeDefPatch(getSupplementaryPropertiesPatch());
    }

    private TypeDefPatch getSupplementaryPropertiesPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;

    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0405GovernanceDrivers()
    {
        this.archiveBuilder.addRelationshipDef(getRegulatorRelationship());
    }

    private RelationshipDef getRegulatorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REGULATOR_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "enforcesRegulations";
        final String                     end1AttributeDescription     = "The regulations that are the responsibility of the regulator.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REGULATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "assignedRegulators";
        final String                     end2AttributeDescription     = "The organizations named as official regulators for the relationship.  The scope attribute defines where the regulator is active.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ORGANIZATION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0135ActionsForPeople()
    {
        this.archiveBuilder.addEntityDef(getActivityEntryEntity());
        this.archiveBuilder.addEntityDef(getBlogEntryEntity());
        this.archiveBuilder.addEntityDef(getJournalEntryEntity());
    }


    private EntityDef getActivityEntryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ACTIVITY_ENTRY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.NOTIFICATION.typeName));
    }

    private EntityDef getBlogEntryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.BLOG_ENTRY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.NOTIFICATION.typeName));
    }

    private EntityDef getJournalEntryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.JOURNAL_ENTRY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.NOTIFICATION.typeName));
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0423SecurityDefinitions()
    {
        this.archiveBuilder.addEntityDef(getServiceAccessControlEntity());
    }

    private EntityDef getServiceAccessControlEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SERVICE_ACCESS_CONTROL,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAPPING_PROPERTIES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0424GovernanceZones()
    {
        this.archiveBuilder.addTypeDefPatch(updateZoneMembershipProfileClassification());
    }

    private TypeDefPatch updateZoneMembershipProfileClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TOTAL_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TYPE_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHORED_TOTAL_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHORED_TYPE_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ALL_TOTAL_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ALL_TYPE_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANALYSIS_TIME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0455ExceptionManagement()
    {
        this.archiveBuilder.addClassificationDef(getSecurityLogClassification());
        this.archiveBuilder.addEntityDef(getExceptionTypeEntity());
        this.archiveBuilder.addRelationshipDef(getExceptionRelationship());
    }

    private ClassificationDef getSecurityLogClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.SECURITY_LOG_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private EntityDef getExceptionTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXCEPTION_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_CONTROL.typeName));
    }


    private RelationshipDef getExceptionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EXCEPTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "exceptions";
        final String                     end1AttributeDescription     = "Types of exception assigned to this element.";
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
        final String                     end2AttributeName            = "excludedFromRequirements";
        final String                     end2AttributeDescription     = "The elements that are non-compliant with the associated policy.  The exception type defines the nature of the non-compliance.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.EXCEPTION_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LAST_REVIEW_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REVIEW_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONDITIONS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0595DesignPatterns()
    {
        this.archiveBuilder.addTypeDefPatch(updateDesignPatternEntity());

        this.archiveBuilder.addRelationshipDef(getNestedDesignPatternRelationship());
        this.archiveBuilder.addRelationshipDef(getSpecializedDesignPatternRelationship());

    }

    private TypeDefPatch updateDesignPatternEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DESIGN_PATTERN.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getEntityDef(OpenMetadataType.AUTHORED_REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USAGE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private RelationshipDef getNestedDesignPatternRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NESTED_DESIGN_PATTERN_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "consumingDesignPatterns";
        final String                     end1AttributeDescription     = "Parent design pattern.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DESIGN_PATTERN.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "consumedDesignPatterns";
        final String                     end2AttributeDescription     = "Child design pattern.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DESIGN_PATTERN.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getSpecializedDesignPatternRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SPECIALIZED_DESIGN_PATTERN_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "generalizedDesignPatterns";
        final String                     end1AttributeDescription     = "Design patterns that are generalized versions of this design pattern.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DESIGN_PATTERN.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "specializedDesignPatterns";
        final String                     end2AttributeDescription     = "Design patterns that are specialized versions of this design pattern.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DESIGN_PATTERN.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

