/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
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
public class OpenMetadataTypesArchive1_7
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "1.7";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1516313040008L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private OMRSArchiveBuilder archiveBuilder;
    private OMRSArchiveHelper  archiveHelper;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive1_7()
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
    public OpenMetadataTypesArchive1_7(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive1_6  previousTypes = new OpenMetadataTypesArchive1_6(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();


        /*
         * Calls for new types go here
         */
        update0045ServersAndAssets();
        update0424GovernanceZones();
        update042SubjectAreas();
        add0435PolicyManagementCapabilities();
        update0438NamingStandards();
        update0440OrganizationalControls();
        update0450GovernanceRollout();
        update0534RelationalSchemas();
        update05454ReferenceData();
        addArea7Lineage();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0045 Servers and Assets - update grammar in comment.
     */
    private void update0045ServersAndAssets()
    {
        this.archiveBuilder.addTypeDefPatch(updateAssetServerUseRelationship());
    }


    private TypeDefPatch updateAssetServerUseRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "AssetServerUse";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute1Description     = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.DESCRIPTION.descriptionGUID;

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


    /**
     * 0424 - GovernanceZone is missing scope and domain attributes
     */
    private void update0424GovernanceZones()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceZoneEntity());
    }

    private TypeDefPatch updateGovernanceZoneEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceZone";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Consumable name of this zone for user interfaces and reports.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "scope";
        final String attribute2Description     = "Breadth of applicability of this zone to the assets matching the criteria.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "domain";
        final String attribute3Description     = "Primary governance domain controlling the asset in this zone.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * 0425 - New types to describe subject areas
     */
    private void update042SubjectAreas()
    {
        this.archiveBuilder.addEntityDef(addSubjectAreaDefinitionEntity());

        this.archiveBuilder.addRelationshipDef(getSubjectAreaGovernanceRelationship());
        this.archiveBuilder.addRelationshipDef(getSubjectAreaHierarchyRelationship());
    }

    private EntityDef addSubjectAreaDefinitionEntity()
    {
        final String guid            = "d28c3839-bc6f-41ad-a882-5667e01fea72";
        final String name            = "SubjectAreaDefinition";
        final String description     = "Defines a collection of glossary elements that are related to a topic.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Consumable name for this subject area for user interfaces and reports.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of this subject area.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "usage";
        final String attribute3Description     = "How and where the subject area contents should be used.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "scope";
        final String attribute4Description     = "Breadth of applicability of this subject area to the organization.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "domain";
        final String attribute5Description     = "Primary governance domain controlling the contents of this subject area.";
        final String attribute5DescriptionGUID = null;


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
        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getSubjectAreaGovernanceRelationship()
    {
        final String guid            = "ee8c78a1-a3ae-4824-a4e1-dcb64bc3a45b";
        final String name            = "SubjectAreaGovernance";
        final String description     = "Links a subject area definition to a governance definition that applies to all of the contents of the " +
                "subject area.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SubjectAreaDefinition";
        final String                     end1AttributeName            = "governedSubjectArea";
        final String                     end1AttributeDescription     = "The subject area governed by this definition.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "GovernanceDefinition";
        final String                     end2AttributeName            = "governedBy";
        final String                     end2AttributeDescription     = "Governance definitions for this subject area.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getSubjectAreaHierarchyRelationship()
    {
        final String guid            = "fd3b7eaf-969c-4c26-9e1e-f31c4c2d1e4b";
        final String name            = "SubjectAreaHierarchy";
        final String description     = "Creates a controlling hierarchy for subject areas.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SubjectAreaDefinition";
        final String                     end1AttributeName            = "broaderSubjectArea";
        final String                     end1AttributeDescription     = "The subject area that describes a broader topic.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SubjectAreaDefinition";
        final String                     end2AttributeName            = "nestedSubjectArea";
        final String                     end2AttributeDescription     = "The subdivisions of the broader topic.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }

    /**
     * 0435 - Add classifications for policy management capabilities
     */
    private void add0435PolicyManagementCapabilities()
    {
        this.archiveBuilder.addClassificationDef(getPolicyAdministrationPointClassification());
        this.archiveBuilder.addClassificationDef(getPolicyDecisionPointClassification());
        this.archiveBuilder.addClassificationDef(getPolicyEnforcementPointClassification());
        this.archiveBuilder.addClassificationDef(getPolicyInformationPointClassification());
        this.archiveBuilder.addClassificationDef(getPolicyRetrievalPointClassification());
    }


    private ClassificationDef getPolicyAdministrationPointClassification()
    {
        final String guid = "4f13baa3-31b3-4a85-985e-2abc784900b8";

        final String name            = "PolicyAdministrationPoint";
        final String description     = "Describes the capability where policies are maintained.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the policy administration point.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the policy administration point.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "type";
        final String attribute3Description     = "Descriptive type information about the policy administration point.";
        final String attribute3DescriptionGUID = null;


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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getPolicyDecisionPointClassification()
    {
        final String guid = "bf521975-bfec-4115-a8e3-ed0fee7d4a43";

        final String name            = "PolicyDecisionPoint";
        final String description     = "Describes the capability where policies are evaluated for a specific situation.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the policy decision point.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the policy decision point.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "type";
        final String attribute3Description     = "Descriptive type information about the policy decision point.";
        final String attribute3DescriptionGUID = null;


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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getPolicyEnforcementPointClassification()
    {
        final String guid = "9a68b20b-3f84-4d7d-bc9e-790c4b27e685";

        final String name            = "PolicyEnforcementPoint";
        final String description     = "Describes the capability where the result of a policy decision are enforced.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the policy enforcement point.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the policy enforcement point.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "type";
        final String attribute3Description     = "Descriptive type information about the policy enforcement point.";
        final String attribute3DescriptionGUID = null;


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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getPolicyInformationPointClassification()
    {
        final String guid = "2058ab6f-ddbf-45f9-9136-47354544e282";

        final String name            = "PolicyInformationPoint";
        final String description     = "Describes the capability where additional information used in a policy decision are stored.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the policy information point.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the policy information point.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "type";
        final String attribute3Description     = "Descriptive type information about the policy information point.";
        final String attribute3DescriptionGUID = null;


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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getPolicyRetrievalPointClassification()
    {
        final String guid = "d7367412-7ba6-409f-84db-42b51e859367";

        final String name            = "PolicyRetrievalPoint";
        final String description     = "Describes the capability where policies are retrieved.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the policy retrieval point.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the policy retrieval point.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "type";
        final String attribute3Description     = "Descriptive type information about the policy retrieval point.";
        final String attribute3DescriptionGUID = null;


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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /**
     * 0438 - Modifier classification was defined as NamingConventionRule - typo in 1.2.
     *        A new classification is defined for Modifier, NamingConventionRule is deprecated
     *        and PrimeWord and ClassWord are changed to link to Referenceable so that
     *        Valid Values can be used to build a naming standard rule set.
     */
    private void update0438NamingStandards()
    {
        this.archiveBuilder.addClassificationDef(getModifierClassification());
        this.archiveBuilder.addTypeDefPatch(updateNamingConventionRuleClassification());
        this.archiveBuilder.addTypeDefPatch(updatePrimeWordClassification());
        this.archiveBuilder.addTypeDefPatch(updateClassWordClassification());
    }

    private ClassificationDef getModifierClassification()
    {
        final String guid            = "f662c95a-ae3f-4f71-b442-78ab70f2ee47";
        final String name            = "Modifier";
        final String description     = "Describes modifying noun or adverb, used in naming standards.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    private TypeDefPatch updateNamingConventionRuleClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "NamingConventionRule";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch updatePrimeWordClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PrimeWord";
        final String typeLinkName = OpenMetadataType.REFERENCEABLE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(typeLinkName)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    private TypeDefPatch updateClassWordClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ClassWord";
        final String typeLinkName = OpenMetadataType.REFERENCEABLE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(typeLinkName)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    /**
     * 0440 - Scope attribute was missing.
     */
    private void update0440OrganizationalControls()
    {
        this.archiveBuilder.addTypeDefPatch(updateOrganizationalCapabilityRelationship());
    }


    private TypeDefPatch updateOrganizationalCapabilityRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "OrganizationalCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "scope";
        final String attribute1Description     = "Breadth of applicability in the organization.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * 0450 - Correct spelling mistake in attribute description and add GovernanceDomain.
     */
    private void update0450GovernanceRollout()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceMetricEntity());
    }


    private TypeDefPatch updateGovernanceMetricEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceMetric";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "target";
        final String attribute1Description     = "Definition of the measurement values that the governance definitions are trying to achieve.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "domain";
        final String attribute2Description     = "Primary governance domain that this metric is measuring.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0534 - replace fraction with significantDigit in DerivedSchemaAttribute
     */
    private void update0534RelationalSchemas()
    {
        this.archiveBuilder.addTypeDefPatch(updateRelationalColumnEntity());
    }

    private TypeDefPatch updateRelationalColumnEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RelationalColumn";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "fraction";
        final String attribute1Description     = "Number of significant digits to the right of decimal point (deprecated).";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "significantDigits";
        final String attribute2Name            = "isUnique";
        final String attribute2Description     = "Data is unique or not.";
        final String attribute2DescriptionGUID = null;
        final String attribute2ReplacedBy      = "allowsDuplicateValues";


        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        property = archiveHelper.getBooleanTypeDefAttribute(attribute2Name,
                                                            attribute2Description,
                                                            attribute2DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2ReplacedBy);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0545 - add additional relationships to allow valid values to be used as classifiers and
     * support of mappings between valid values from different sets.
     */
    private void update05454ReferenceData()
    {
        this.archiveBuilder.addRelationshipDef(getValidValuesMappingRelationship());
        this.archiveBuilder.addRelationshipDef(getReferenceValueAssignmentRelationship());
        this.archiveBuilder.addTypeDefPatch(updateValidValuesImplementationRelationship());
    }


    /**
     * The Valid Values Mapping shows mappings between corresponding valid values from different valid value sets.
     *
     * @return ValidValuesMapping RelationshipDef
     */
    private RelationshipDef getValidValuesMappingRelationship()
    {
        final String guid            = OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end1AttributeName            = "matchingValue";
        final String                     end1AttributeDescription     = "A valid value from a different valid value set that is equivalent.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end2AttributeName            = "matchingValue";
        final String                     end2AttributeDescription     = "A valid value from a different valid value set that is equivalent.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "associationDescription";
        final String attribute1Description     = "Brief description describing how they are related.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "confidence";
        final String attribute2Description     = "Number between 0 and 100 indicating the confidence that the match is correct.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "steward";
        final String attribute3Description     = "Person responsible for the mapping.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "notes";
        final String attribute4Description     = "Additional notes on the mapping.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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


    /**
     * The ReferenceValueAssignment allows valid values to be used as tags to help group and locate referenceables.
     *
     * @return ReferenceValueAssignment RelationshipDef
     */
    private RelationshipDef getReferenceValueAssignmentRelationship()
    {
        final String guid            = OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "assignedItem";
        final String                     end1AttributeDescription     = "An element that has been tagged by a valid value.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end2AttributeName            = "referenceValue";
        final String                     end2AttributeDescription     = "A valid value that represents the meaning or classification of the " +
                "assigned item.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "confidence";
        final String attribute2Description     = "Number between 0 and 100 indicating the confidence that the match is correct.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "steward";
        final String attribute3Description     = "Person responsible for the mapping.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "notes";
        final String attribute4Description     = "Additional notes on the mapping.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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


    /**
     * 0545 - Add symbolicName and additionalValues properties
     */
    private TypeDefPatch updateValidValuesImplementationRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "symbolicName";
        final String attribute1Description     = "Name of the value value used in code.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "additionalValues";
        final String attribute2Description     = "Additional values for additional columns or fields in the reference data store.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * ========================================
     * AREA 7: lineage
     */


    /**
     * Area 7 - lineage - adds context around the assets to enable understanding or where data is
     * moving and processing is happening.
     */
    private void addArea7Lineage()
    {
        this.add0710DigitalServices();
        this.add0715DigitalServiceOwnership();
        this.add0717DigitalServiceImplementation();
        this.add0720InformationSupplyChains();
        this.add0730SolutionComponents();
        this.add0735SolutionPortsAndWires();
        this.add0740SolutionBlueprints();
        this.add0750DataPassing();
        this.add0760BusinessLineage();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0710DigitalServices()
    {
        this.archiveBuilder.addEntityDef(getDigitalServiceEntity());
        this.archiveBuilder.addRelationshipDef(getDigitalServiceDependencyRelationship());
    }


    private EntityDef getDigitalServiceEntity()
    {
        final String guid = "f671e1fc-b204-4ee6-a4e2-da1633ecf50e";

        final String name            = "DigitalService";
        final String description     = "A business function implemented using IT.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the digital service.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "description";
        final String attribute2Description     = "Brief description of the digital service.";
        final String attribute2DescriptionGUID = null;

        final String attribute3Name            = "versionNumber";
        final String attribute3Description     = "Version number (major.minor) of the component.";
        final String attribute3DescriptionGUID = null;

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

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.APPROVED_CONCEPT);
        validInstanceStatusList.add(InstanceStatus.UNDER_DEVELOPMENT);
        validInstanceStatusList.add(InstanceStatus.DEVELOPMENT_COMPLETE);
        validInstanceStatusList.add(InstanceStatus.APPROVED_FOR_DEPLOYMENT);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DISABLED);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private RelationshipDef getDigitalServiceDependencyRelationship()
    {
        final String guid = "e8303911-ba1c-4640-974e-c4d57ee1b310";

        final String name            = "DigitalServiceDependency";
        final String description     = "Relationship identifying dependencies between digital services.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "callsDigitalServices";
        final String                     end1AttributeDescription     = "The digital services dependent on the others.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "DigitalService";
        final String                     end2AttributeName            = "calledByDigitalServices";
        final String                     end2AttributeDescription     = "The digital services that the others depends on.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "delegationEscalationAuthority";
        final String attribute1Description     = "Can delegations and escalations flow on this relationship.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0715DigitalServiceOwnership()
    {
        this.archiveBuilder.addEntityDef(getDigitalServiceManagerEntity());
        this.archiveBuilder.addRelationshipDef(getDigitalServiceManagementRelationship());
        this.archiveBuilder.addRelationshipDef(getDigitalSupportRelationship());
        this.archiveBuilder.addRelationshipDef(getDigitalServiceOperatorRelationship());
    }


    private EntityDef getDigitalServiceManagerEntity()
    {
        final String guid = "6dfba6ce-e925-4281-880d-d04100c5b991";

        final String name            = "DigitalServiceManager";
        final String description     = "Person managing a digital service.";
        final String descriptionGUID = null;

        final String superTypeName = "PersonRole";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getDigitalServiceManagementRelationship()
    {
        final String guid = "91ff7542-c275-4cd3-b367-97eec3360422";

        final String name            = "DigitalServiceManagement";
        final String description     = "Relationship identifying the individual responsible for each digital service.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "managesDigitalServices";
        final String                     end1AttributeDescription     = "The digital services that this individual manages.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "DigitalServiceManager";
        final String                     end2AttributeName            = "digitalServiceManager";
        final String                     end2AttributeDescription     = "The individual responsible for the digital services.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDigitalSupportRelationship()
    {
        final String guid = "9e187e1e-2547-46bd-b0ee-c33ac6df4a1f";

        final String name            = "DigitalSupport";
        final String description     = "Relationship identifying the digital services supporting each business capability.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "usesDigitalServices";
        final String                     end1AttributeDescription     = "The digital services that this business capability depends on.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "BusinessCapability";
        final String                     end2AttributeName            = "consumingBusinessCapabilities";
        final String                     end2AttributeDescription     = "The business capabilities that depend on the digital services.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDigitalServiceOperatorRelationship()
    {
        final String guid = "79ac27f6-be9c-489f-a7c2-b9add0bf705c";

        final String name            = "DigitalServiceOperator";
        final String description     = "Relationship identifying the organizations responsible for operating the digital services.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "operatesDigitalServices";
        final String                     end1AttributeDescription     = "The digital services that this organization operates.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Organization";
        final String                     end2AttributeName            = "digitalServiceOperators";
        final String                     end2AttributeDescription     = "The organizations that support the digital service's operations.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "scope";
        final String attribute1Description     = "The extent to which this operator is responsible for the digital service operations.";
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


    private void add0717DigitalServiceImplementation()
    {
        this.archiveBuilder.addRelationshipDef(getDigitalServiceImplementationRelationship());
    }


    private RelationshipDef getDigitalServiceImplementationRelationship()
    {
        final String guid = "873e29bd-ca14-4833-a6bb-9ebdf89b5b1b";

        final String name            = "DigitalServiceImplementation";
        final String description     = "Relationship identifying the implementation of a digital service.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "partOfDigitalServices";
        final String                     end1AttributeDescription     = "The digital services that use this capability.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SoftwareServerCapability";
        final String                     end2AttributeName            = "implementedBy";
        final String                     end2AttributeDescription     = "The list of software server capabilities that support this digital service.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Details of how the software server capability supports the needs of the digital service.";
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

    private void add0720InformationSupplyChains()
    {
        this.archiveBuilder.addEntityDef(getInformationSupplyChainEntity());
        this.archiveBuilder.addEntityDef(getInformationSupplyChainSegmentEntity());
        this.archiveBuilder.addRelationshipDef(getInformationSupplyChainCompositionRelationship());
        this.archiveBuilder.addRelationshipDef(getInformationSupplyChainImplementationRelationship());
    }


    private EntityDef getInformationSupplyChainEntity()
    {
        final String guid = "fa6de61d-98cb-48c4-b21f-ab7186235fd4";

        final String name            = "InformationSupplyChain";
        final String description     = "A description of a managed flow of information between multiple systems.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the information supply chain.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "description";
        final String attribute2Description     = "Brief description of the information supply chain.";
        final String attribute2DescriptionGUID = null;

        final String attribute3Name            = "scope";
        final String attribute3Description     = "Breadth of applicability of the information supply chain to the organization.";
        final String attribute3DescriptionGUID = null;

        final String attribute4Name            = "purposes";
        final String attribute4Description     = "Reasons to have this information supply chain.";
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

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute4Name,
                                                                attribute4Description,
                                                                attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DISABLED);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private EntityDef getInformationSupplyChainSegmentEntity()
    {
        final String guid = "6d9980b2-5c0b-4314-8d8d-9fa45f8904d1";

        final String name            = "InformationSupplyChainSegment";
        final String description     = "A section of an information supply chain that has common characteristics.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the segment.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "description";
        final String attribute2Description     = "Brief description of the segment.";
        final String attribute2DescriptionGUID = null;

        final String attribute3Name            = "scope";
        final String attribute3Description     = "Breadth of applicability of this segment to the organization.";
        final String attribute3DescriptionGUID = null;

        final String attribute4Name            = "integrationStyle";
        final String attribute4Description     = "Mechanism to flow data along the segment.";
        final String attribute4DescriptionGUID = null;

        final String attribute5Name            = "estimatedVolumetrics";
        final String attribute5Description     = "Properties that describe the expected volumes of data flowing through this segment.";
        final String attribute5DescriptionGUID = null;

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

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute5Name,
                                                                    attribute5Description,
                                                                    attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DISABLED);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private RelationshipDef getInformationSupplyChainCompositionRelationship()
    {
        final String guid = "fcdccfa3-e9f0-4543-8720-1958799fb6dc";

        final String name            = "InformationSupplyChainComposition";
        final String description     = "Relationship identifying the segments in an information supply chain.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "InformationSupplyChain";
        final String                     end1AttributeName            = "informationSupplyChains";
        final String                     end1AttributeDescription     = "Owning information supply chain.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "InformationSupplyChainSegment";
        final String                     end2AttributeName            = "segments";
        final String                     end2AttributeDescription     = "A role performed by this person.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getInformationSupplyChainImplementationRelationship()
    {
        final String guid = "94715275-0520-43e9-81fe-4fe8ec3d8f3a";

        final String name            = "InformationSupplyChainImplementation";
        final String description     = "Implementation components for an information supply chain segment.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "InformationSupplyChainSegment";
        final String                     end1AttributeName            = "partOfSegments";
        final String                     end1AttributeDescription     = "A related section of an information supply chain .";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.PROCESS.typeName;
        final String                     end2AttributeName            = "implementedByProcesses";
        final String                     end2AttributeDescription     = "Processes that implement the information supply chain.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0730SolutionComponents()
    {

        this.archiveBuilder.addEntityDef(getSolutionComponentEntity());

        this.archiveBuilder.addRelationshipDef(getSolutionCompositionRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionComponentImplementationRelationship());
    }


    private EntityDef getSolutionComponentEntity()
    {
        final String guid = "b83f3d42-f3f7-4155-ae65-58fb44ea7644";

        final String name            = "SolutionComponent";
        final String description     = "Description of a well-defined capability within a solution.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the component.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Brief description of the component.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "versionNumber";
        final String attribute3Description     = "Version number (major.minor) of the component.";
        final String attribute3DescriptionGUID = null;

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

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DISABLED);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private RelationshipDef getSolutionCompositionRelationship()
    {
        final String guid = "2a9e56c3-bcf6-41de-bbe9-1e63b81d3114";

        final String name            = "SolutionComposition";
        final String description     = "Relationship showing the nesting structure of solution components.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SolutionComponent";
        final String                     end1AttributeName            = "usedInSolutionComponents";
        final String                     end1AttributeDescription     = "The solution components that embed this component.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SolutionComponent";
        final String                     end2AttributeName            = "nestedSolutionComponents";
        final String                     end2AttributeDescription     = "The sub-parts of this solution component.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getSolutionComponentImplementationRelationship()
    {
        final String guid = "d0dd0ac7-01f4-48e0-ae4d-4f7268573fa8";

        final String name            = "SolutionComponentImplementation";
        final String description     = "Relationship identifying a solution component's implementation.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SolutionComponent";
        final String                     end1AttributeName            = "partOfComponentDesigns";
        final String                     end1AttributeDescription     = "A person performing this role.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SoftwareServerCapability";
        final String                     end2AttributeName            = "implementedBy";
        final String                     end2AttributeDescription     = "One of the implementation parts of the solution component.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of how the software server capability supports the solution component.";
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

    /**
     * 0735 Solution Ports and Wires defines how communication ports are connected to the solution components.
     */
    private void add0735SolutionPortsAndWires()
    {
        this.archiveBuilder.addEnumDef(getSolutionPortDirectionEnum());

        this.archiveBuilder.addEntityDef(getSolutionPortEntity());

        this.archiveBuilder.addRelationshipDef(getSolutionLinkingWireRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionComponentPortRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionPortDelegationRelationship());
    }



    private EnumDef getSolutionPortDirectionEnum()
    {
        final String guid            = "4879c96e-26c7-48af-ba92-8277632be733";
        final String name            = "SolutionPortDirection";
        final String description     = "Defines the direction of flow of information through a solution port.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Unknown";
        final String element1Description     = "The direction of flow is unknown.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "Output";
        final String element2Description     = "The process is producing information through this port.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "Input";
        final String element3Description     = "The process is consuming information through this port.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 3;
        final String element4Value           = "InOut";
        final String element4Description     = "The process has a call interface attached to this port.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 4;
        final String element5Value           = "OutIn";
        final String element5Description     = "The process is issuing a call to an external API through this port.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                     element5Value,
                                                     element5Description,
                                                     element5DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element6Ordinal         = 99;
        final String element6Value           = "Other";
        final String element6Description     = "Another direction.";
        final String element6DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element6Ordinal,
                                                     element6Value,
                                                     element6Description,
                                                     element6DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getSolutionPortEntity()
    {
        final String guid = "62ef448c-d4c1-4c94-a565-5e5625f6a57b";

        final String name            = "SolutionPort";
        final String description     = "An external endpoint for a solution.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the port.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the port.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "versionNumber";
        final String attribute3Description     = "Version number (major.minor) of the port.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "direction";
        final String attribute4Description     = "Which way is data flowing?";
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
        property = archiveHelper.getEnumTypeDefAttribute("SolutionPortDirection",
                                                         attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DISABLED);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private RelationshipDef getSolutionLinkingWireRelationship()
    {
        final String guid = "892a3d1c-cfb8-431d-bd59-c4d38833bfb0";

        final String name            = "SolutionLinkingWire";
        final String description     = "Connection between two solution ports that shows how data flows.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SolutionPort";
        final String                     end1AttributeName            = "connectedPorts";
        final String                     end1AttributeDescription     = "Port that the wire connects to.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SolutionPort";
        final String                     end2AttributeName            = "connectedPorts";
        final String                     end2AttributeDescription     = "Port that the wire connects to.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "informationSupplyChainSegmentGUIDs";
        final String attribute1Description     = "Unique identifier of information supply chain segments that this wire belongs to " +
                "(typically only one).";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getSolutionComponentPortRelationship()
    {
        final String guid = "5652d03a-f6c9-411a-a3e4-f490d3856b64";

        final String name            = "SolutionComponentPort";
        final String description     = "Link between a solution component and its ports.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SolutionComponent";
        final String                     end1AttributeName            = "solutionComponent";
        final String                     end1AttributeDescription     = "Owning solution component that this port belongs to.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SolutionPort";
        final String                     end2AttributeName            = "solutionPorts";
        final String                     end2AttributeDescription     = "List ports for this solution component.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getSolutionPortDelegationRelationship()
    {
        final String guid = "8335e6ed-fd86-4000-9bc5-5203062f28ba";

        final String name            = "SolutionPortDelegation";
        final String description     = "Aligns ports from nested components with the parent's.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SolutionPort";
        final String                     end1AttributeName            = "alignsToPort";
        final String                     end1AttributeDescription     = "Encapsulating solution component's port";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SolutionPort";
        final String                     end2AttributeName            = "delegationPorts";
        final String                     end2AttributeDescription     = "Ports from nested components that align with the port from the " +
                "encapsulating solution component.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0740SolutionBlueprints()
    {
        this.archiveBuilder.addEntityDef(getSolutionBlueprintEntity());

        this.archiveBuilder.addRelationshipDef(getSolutionBlueprintCompositionRelationship());
        this.archiveBuilder.addRelationshipDef(getDigitalServiceDesignRelationship());
    }

    private EntityDef getSolutionBlueprintEntity()
    {
        final String guid = "4aa47799-5128-4eeb-bd72-e357b49f8bfe";

        final String name            = "SolutionBlueprint";
        final String description     = "Collection of solution components that make up a digital service.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the solution.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the solution.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "versionNumber";
        final String attribute3Description     = "Version number (major.minor) of the solution.";
        final String attribute3DescriptionGUID = null;

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

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DISABLED);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private RelationshipDef getSolutionBlueprintCompositionRelationship()
    {
        final String guid = "f1ae975f-f11a-467b-8c7a-b023081e4712";

        final String name            = "SolutionBlueprintComposition";
        final String description     = "Link between a solution blueprint and a solution component.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SolutionBlueprint";
        final String                     end1AttributeName            = "usedInSolutionBlueprints";
        final String                     end1AttributeDescription     = "The solutions where this component features.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SolutionComponent";
        final String                     end2AttributeName            = "containsSolutionComponents";
        final String                     end2AttributeDescription     = "List of solution components that make up the solution.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the solution component's role in the solution.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getDigitalServiceDesignRelationship()
    {
        final String guid = "a43b4c9c-52c2-4819-b3cc-9d07d49a11f2";

        final String name            = "DigitalServiceDesign";
        final String description     = "Relationship identifying the solution blueprint for a digital service.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "DigitalService";
        final String                     end1AttributeName            = "describesDigitalService";
        final String                     end1AttributeDescription     = "Digital service described by the blueprint.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SolutionBlueprint";
        final String                     end2AttributeName            = "digitalServiceDesigns";
        final String                     end2AttributeDescription     = "The difference versions of the digital service's designs.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0750DataPassing()
    {
        this.archiveBuilder.addRelationshipDef(getProcessCallRelationship());
        this.archiveBuilder.addRelationshipDef(getProcessInputRelationship());
        this.archiveBuilder.addRelationshipDef(getProcessOutputRelationship());
    }


    private RelationshipDef getProcessCallRelationship()
    {
        final String guid            = "af904501-6347-4f52-8378-da50e8d74828";
        final String name            = "ProcessCall";
        final String description     = "Shows a call between 2 assets.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.ASSET.typeName;
        final String                     end1AttributeName            = "calls";
        final String                     end1AttributeDescription     = "Caller asset.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ASSET.typeName;
        final String                     end2AttributeName            = "calledBy";
        final String                     end2AttributeDescription     = "Called asset.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description and purpose of the call.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getProcessInputRelationship()
    {
        final String guid            = "d1a9a79f-4c9c-4dff-837e-1353ba51b607";
        final String name            = "ProcessInput";
        final String description     = "The feed of data into a process.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.PROCESS.typeName;
        final String                     end1AttributeName            = "consumedByProcess";
        final String                     end1AttributeDescription     = "Process that is receiving the information from the asset.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ASSET.typeName;
        final String                     end2AttributeName            = "processInputData";
        final String                     end2AttributeDescription     = "Asset supplying input data.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the data feed.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getProcessOutputRelationship()
    {
        final String guid            = "e3e40f99-70fe-478c-9676-78a50cded70b";
        final String name            = "ProcessOutput";
        final String description     = "The feed of data from a process.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.PROCESS.typeName;
        final String                     end1AttributeName            = "producedByProcess";
        final String                     end1AttributeDescription     = "Process that is creating and updating the information in the asset.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ASSET.typeName;
        final String                     end2AttributeName            = "processOutputData";
        final String                     end2AttributeDescription     = "Asset receiving output data.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the data feed.";
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


    private void add0760BusinessLineage()
    {
        this.archiveBuilder.addClassificationDef(getBusinessSignificantClassification());
    }


    private ClassificationDef getBusinessSignificantClassification()
    {
        final String guid            = "085febdd-f129-4f4b-99aa-01f3e6294e9f";
        final String name            = "BusinessSignificant";
        final String description     = "A referenceable item that is meaningful to business users.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the item in business terms.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "scope";
        final String attribute2Description     = "Scope of where this item is meaningful.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "businessCapabilityGUID";
        final String attribute3Description     = "Unique identifier of the business capability that this relevant to.";
        final String attribute3DescriptionGUID = null;

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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * ========================================================================
     * Below are place holders for types to be introduced in future releases.
     * ========================================================================
     */


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0217 Automated Processes defines a Process is automated (as opposed to manual procedure).
     */
    private void add0217AutomatedProcesses()
    {
        /* placeholder */
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0260Transformations()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0265AnalyticsAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0270IoTAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0280ModelAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0435 Governance Rules define details of a governance rule implementation.
     */
    private void add0435GovernanceRules()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0447GovernanceProcesses()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0452GovernanceDaemons()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0480RightsManagement()
    {
        /* placeholder */
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0550LogicSpecificationModel()
    {
        /* placeholder */
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0560MappingModel()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

