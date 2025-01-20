/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
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
public class OpenMetadataTypesArchive3_13
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.13";
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
    public OpenMetadataTypesArchive3_13()
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
    public OpenMetadataTypesArchive3_13(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_12 previousTypes = new OpenMetadataTypesArchive3_12(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0010BaseModel();
        update0022Translations();
        addArea1Actors();
        add0222DataFilesAndFolders();
        add0223DataFeed();
        add0430ServiceLevelObjectives();
        update0481Licenses();
        add0483TermsAndConditions();
        add0484Agreements();
        add00710DigitalProduct();
        add00711DigitalSubscription();
        update0715DigitalServiceOwnership();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateAsset());
    }


    private TypeDefPatch updateAsset()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ASSET.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.VERSION_IDENTIFIER.name;
        final String attribute1Description     = OpenMetadataProperty.VERSION_IDENTIFIER.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.VERSION_IDENTIFIER.descriptionGUID;

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

    private void update0022Translations()
    {
        this.archiveBuilder.addTypeDefPatch(updateTranslation());
    }


    private TypeDefPatch updateTranslation()
    {
        /*
         * Create the Patch
         */
        final String typeName = "TranslationDetail";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "languageCode";
        final String attribute1Description     = "Code for identifying the language - for example from ISO-639.";
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


    private void addArea1Actors()
    {
        this.archiveBuilder.addEntityDef(getActorEntity());

        this.archiveBuilder.addTypeDefPatch(updateActorProfile());
        this.archiveBuilder.addTypeDefPatch(updateUserIdentity());
        this.archiveBuilder.addTypeDefPatch(updatePersonRole());
        this.archiveBuilder.addTypeDefPatch(updateProjectTeam());
        this.archiveBuilder.addTypeDefPatch(updateToDo());
        this.archiveBuilder.addTypeDefPatch(updateActionAssignment());
        this.archiveBuilder.addTypeDefPatch(updateCrowdSourcingContribution());
    }

    private EntityDef getActorEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ACTOR,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));
    }


    private TypeDefPatch updateActorProfile()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ACTOR_PROFILE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateUserIdentity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.USER_IDENTITY.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName));

        return typeDefPatch;
    }


    private TypeDefPatch updatePersonRole()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PERSON_ROLE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateProjectTeam()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "supportingActors";
        final String                     end2AttributeDescription     = "People and teams supporting this project.";
        final String                     end2AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName),
                                                                                    end2AttributeName,
                                                                                    end2AttributeDescription,
                                                                                    end2AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);


        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateToDo()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TO_DO.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TO_DO_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateActionAssignment()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "assignedActor";
        final String                     end1AttributeDescription     = "The person/people assigned to perform the action(s) requested in the to do.";
        final String                     end1AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);


        typeDefPatch.setEndDef1(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateCrowdSourcingContribution()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CrowdSourcingContribution";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "contributors";
        final String                     end2AttributeDescription     = "The person/people making the contribution.";
        final String                     end2AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName),
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


    private void add0222DataFilesAndFolders()
    {
        this.archiveBuilder.addEntityDef(getParquetFileEntity());
        this.archiveBuilder.addTypeDefPatch(updateDataFileEntity());
    }


    private TypeDefPatch updateDataFileEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_FILE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FILE_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    private EntityDef getParquetFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PARQUET_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0223DataFeed()
    {
        this.archiveBuilder.addEntityDef(getDataFeedEntity());

    }

    private EntityDef getDataFeedEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_FEED,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_ASSET.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0430ServiceLevelObjectives()
    {
        this.archiveBuilder.addEntityDef(getServiceLevelObjectivesEntity());
    }


    private EntityDef getServiceLevelObjectivesEntity()
    {
        final String guid            = "22c4e433-1b87-4446-840a-03f83d2dc113";
        final String name            = "ServiceLevelObjective";
        final String description     = "The set of behavior related objectives that an asset or capability seeks to achieve.";
        final String descriptionGUID = null;

        final String superTypeName = "TechnicalControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0481Licenses()
    {
        this.archiveBuilder.addTypeDefPatch(updateLicenseRelationship());
    }

    private TypeDefPatch updateLicenseRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "License";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "entitlements";
        final String attribute1Description     = "The list of rights and permissions granted.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "restrictions";
        final String attribute2Description     = "The list of limiting conditions or measures imposed.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "obligations";
        final String attribute3Description     = "The list of actions, duties or commitments required.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute1Name,
                                                                    attribute1Description,
                                                                    attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0483TermsAndConditions()
    {
        this.archiveBuilder.addEntityDef(getTermsAndConditionsEntity());
        this.archiveBuilder.addRelationshipDef(getAttachedTermsAndConditionsRelationship());
    }


    private EntityDef getTermsAndConditionsEntity()
    {
        final String guid            = "2ddc42d3-7791-4b4e-a064-91df9300290a";
        final String name            = "TermsAndConditions";
        final String description     = "The set of entitlements, restrictions and obligations associated with an agreement, license etc.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "entitlements";
        final String attribute1Description     = "The list of rights and permissions granted.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "restrictions";
        final String attribute2Description     = "The list of limiting conditions or measures imposed.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "obligations";
        final String attribute3Description     = "The list of actions, duties or commitments required.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute1Name,
                                                                    attribute1Description,
                                                                    attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);
        return entityDef;
    }


    private RelationshipDef getAttachedTermsAndConditionsRelationship()
    {
        final String guid            = "8292343f-6a96-4ca8-a447-38f734c75634";
        final String name            = "AttachedTermsAndConditions";
        final String description     = "The terms and conditions associated with an agreement, license etc.";
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
        final String                     end1AttributeName            = "subjectOfTermsAndConditions";
        final String                     end1AttributeDescription     = "Entity that the terms and condition applied.";
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
        final String                     end2EntityType               = "TermsAndConditions";
        final String                     end2AttributeName            = "termsAndConditions";
        final String                     end2AttributeDescription     = "Entitlements, restrictions and obligations.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0484Agreements()
    {
        this.archiveBuilder.addEntityDef(getAgreementEntity());
        this.archiveBuilder.addRelationshipDef(getAgreementActorRelationship());
        this.archiveBuilder.addRelationshipDef(getAgreementItemRelationship());
        this.archiveBuilder.addRelationshipDef(getContractLinkRelationship());
    }


    private EntityDef getAgreementEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.AGREEMENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.AGREEMENT_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getAgreementActorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.AGREEMENT_ACTOR,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "relatedAgreements";
        final String                     end1AttributeDescription     = "The agreements that include the actor.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.AGREEMENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "agreementActors";
        final String                     end2AttributeDescription     = "The actors that are named in the agreement.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTOR_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getAgreementItemRelationship()
    {
        final String guid            = "a540c361-0ed1-45d6-b525-007592ae806d";
        final String name            = "AgreementItem";
        final String description     = "An identified item in an agreement.";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "Agreement";
        final String                     end1AttributeName            = "agreementContents";
        final String                     end1AttributeDescription     = "The agreement that the item relates to.";
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
        final String                     end2AttributeName            = "agreementItems";
        final String                     end2AttributeDescription     = "Specific items in the agreement.";
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


        final String attribute0Name            = "agreementItemId";
        final String attribute0Description     = "unique identifier for the item within the agreement.";
        final String attribute0DescriptionGUID = null;
        final String attribute1Name            = "entitlements";
        final String attribute1Description     = "The list of rights and permissions granted.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "restrictions";
        final String attribute2Description     = "The list of limiting conditions or measures imposed.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "obligations";
        final String attribute3Description     = "The list of actions, duties or commitments required.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "agreementStart";
        final String attribute4Description     = "Date/time when this item becomes active in the agreement.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "agreementEnd";
        final String attribute5Description     = "Date/time when this item becomes inactive in the agreement.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "usageMeasurements";
        final String attribute6Description     = "Measurements of the actual use of this item under the agreement.";
        final String attribute6DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute0Name,
                                                           attribute0Description,
                                                           attribute0DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute1Name,
                                                                    attribute1Description,
                                                                    attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute6Name,
                                                                    attribute6Description,
                                                                    attribute6DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getContractLinkRelationship()
    {
        final String guid            = "33937ece-5ab6-4cd3-a348-b8196ffc3b4e";
        final String name            = "ContractLink";
        final String description     = "Link to the contract document.";
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
        final String                     end1EntityType               = "Agreement";
        final String                     end1AttributeName            = "agreements";
        final String                     end1AttributeDescription     = "Agreements related to the contract.";
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
        final String                     end2AttributeName            = "contracts";
        final String                     end2AttributeDescription     = "Details of the contract documents.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_REFERENCE.typeName),
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

        final String attribute1Name            = "contractId";
        final String attribute1Description     = "Identifier for the contract used in the agreement.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "contractLiaison";
        final String attribute2Description     = "Identifier of actor to contact with queries relating to the contract.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "contractLiaisonTypeName";
        final String attribute3Description     = "Type name of actor element.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "contractLiaisonPropertyName";
        final String attribute4Description     = "The property from the actor element used as the identifier.";
        final String attribute4DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add00710DigitalProduct()
    {
        this.archiveBuilder.addClassificationDef(addDigitalProductClassification());
    }


    private ClassificationDef addDigitalProductClassification()
    {
        final String guid = OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.typeGUID;
        final String name = OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.typeName;
        final String description = OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.wikiURL;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 descriptionWiki,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute property;

        final String attribute1Name = "syncDatesByKey";
        final String attribute1Description = "Collection of synchronization dates identified by a key";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getMapStringLongTypeDefAttribute(attribute1Name,
                                                                  attribute1Description,
                                                                  attribute1DescriptionGUID);

        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add00711DigitalSubscription()
    {
        this.archiveBuilder.addEntityDef(getDigitalSubscriptionEntity());
        this.archiveBuilder.addRelationshipDef(getDigitalSubscriberRelationship());
    }


    private EntityDef getDigitalSubscriptionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DIGITAL_SUBSCRIPTION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.AGREEMENT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "supportLevel";
        final String attribute1Description     = "Level of support agreed for the subscriber.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "serviceLevels";
        final String attribute2Description     = "Levels of service agreed with the subscriber.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);
        return entityDef;
    }


    private RelationshipDef getDigitalSubscriberRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "digitalSubscribers";
        final String                     end1AttributeDescription     = "The digital subscribers registered under a subscription.";
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
        final String                     end2AttributeName            = "digitalSubscriptions";
        final String                     end2AttributeDescription     = "The digital subscriptions in use by the subscriber.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName),
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

        final String attribute1Name            = "subscriberId";
        final String attribute1Description     = "Unique identifier for the subscriber.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0715DigitalServiceOwnership()
    {
        this.archiveBuilder.addTypeDefPatch(updateDigitalServiceOperator());
    }

    private TypeDefPatch updateDigitalServiceOperator()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DigitalServiceOperator";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "operatesDigitalServices";
        final String                     end1AttributeDescription     = "The digital services that this team/organization operates.";
        final String                     end1AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Team";
        final String                     end2AttributeName            = "digitalServiceOperators";
        final String                     end2AttributeDescription     = "The teams/organizations responsible for operating the digital service.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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
}

