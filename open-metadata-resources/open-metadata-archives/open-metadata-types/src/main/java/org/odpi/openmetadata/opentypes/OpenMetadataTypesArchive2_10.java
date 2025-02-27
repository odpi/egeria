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
public class OpenMetadataTypesArchive2_10
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.10";
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
    public OpenMetadataTypesArchive2_10()
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
    public OpenMetadataTypesArchive2_10(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive2_9 previousTypes = new OpenMetadataTypesArchive2_9(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0130Projects();
        update0360Contexts();
        update04xxGovernanceDefinitions();
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * A variety of changes to improve consistency and flexibility of the governance definitions
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateCampaignClassification());
    }



    /**
     * This change means that the campaign classification connects to a referenceable.  It should have connected to a project -but
     * a mistake connected it to a Collection.  This change allows it to be connected to a Project without breaking backward compatibility.
     *
     * @return patched type
     */
    private TypeDefPatch updateCampaignClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Campaign";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(OpenMetadataType.REFERENCEABLE.typeName)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Enable contexts to be more than just a Glossary term
     */
    private void update0360Contexts()
    {
        this.archiveBuilder.addTypeDefPatch(updateUsedInContextRelationship());
    }



    /**
     * This changes End 2
     *
     * @return patched type
     */
    private TypeDefPatch updateUsedInContextRelationship()
    {
        final String typeName = "UsedInContext";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "usedInContexts";
        final String                     end2AttributeDescription     = "Elements describing the contexts where this term is used.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
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


    /**
     * A variety of changes to improve consistency and flexibility of the governance definitions
     */
    private void update04xxGovernanceDefinitions()
    {
        this.archiveBuilder.addClassificationDef(addOwnershipClassification());
        this.archiveBuilder.addClassificationDef(addIncidentClassifierSetClassification());
        this.archiveBuilder.addEntityDef(addComponentOwnerEntity());
        this.archiveBuilder.addEntityDef(addDataItemOwnerEntity());
        this.archiveBuilder.addEntityDef(addRegulationArticleEntity());
        this.archiveBuilder.addEntityDef(addBusinessImperativeEntity());
        this.archiveBuilder.addRelationshipDef(addGovernanceDriverLinkRelationship());

        this.archiveBuilder.addTypeDefPatch(updateGovernanceResponsibilityAssignmentRelationship());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceDefinitionEntity());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceZoneEntity());
        this.archiveBuilder.addTypeDefPatch(updateSubjectAreaDefinitionEntity());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceMetricEntity());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceRoleEntity());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceOfficer());
        this.archiveBuilder.addTypeDefPatch(updateIncidentClassifier());
        this.archiveBuilder.addTypeDefPatch(updateIncidentReport());
        this.archiveBuilder.addTypeDefPatch(updateAssetOrigin());
        this.archiveBuilder.addTypeDefPatch(updateCertification());
        this.archiveBuilder.addTypeDefPatch(updateLicense());
        this.archiveBuilder.addTypeDefPatch(deprecateAssetOwnershipClassification());
        this.archiveBuilder.addTypeDefPatch(deprecateResponsibilityStaffContact());
    }


    /**
     * Add new ownership classification to link an element to its owner
     *
     * @return new classification definition
     */
    private ClassificationDef addOwnershipClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.OWNERSHIP_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OWNER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OWNER_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OWNER_PROPERTY_NAME));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /**
     * Deprecated
     * Add new IncidentClassifierSet classification to identify a collection as containing IncidentClassifiers
     *
     * @return new classification definition
     */
    private ClassificationDef addIncidentClassifierSetClassification()
    {
        final String guid            = "361158c0-ade1-4c92-a6a7-64f7ac39b87d";
        final String name            = "IncidentClassifierSet";
        final String description     = "A collection of incident classifiers.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Collection";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /**
     * This change means that IncidentClassifier is a Referenceable.  This is to make it consistent with the
     * other classifier definitions.
     *
     * @return patched type
     */
    private TypeDefPatch updateIncidentClassifier()
    {
        /*
         * Create the Patch
         */
        final String typeName = "IncidentClassifier";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        return typeDefPatch;
    }


    /**
     * Deprecate the domain attribute
     *
     * @return patched type
     */
    private TypeDefPatch updateGovernanceDefinitionEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_DEFINITION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Deprecated. Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate the domain attribute
     *
     * @return patched type
     */
    private TypeDefPatch updateGovernanceZoneEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_ZONE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Deprecated. Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate the domain attribute
     *
     * @return patched type
     */
    private TypeDefPatch updateSubjectAreaDefinitionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SubjectAreaDefinition";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Deprecated. Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate the domain attribute
     *
     * @return patched type
     */
    private TypeDefPatch updateGovernanceMetricEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_METRIC.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Deprecated. Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate the domain attribute.
     *
     * @return patched type
     */
    private TypeDefPatch updateGovernanceRoleEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_ROLE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Deprecated. Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * This change means that GovernanceOfficer is just another governance role and it can be managed as such.
     * Also deprecates the domain attribute.
     *
     * @return patched type
     */
    private TypeDefPatch updateGovernanceOfficer()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_OFFICER.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Deprecated. Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * This new subtype of governance role is to recognize an owner of a piece part of one or more assets.
     *
     * @return entity definition
     */
    private EntityDef addComponentOwnerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.COMPONENT_OWNER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));

    }


    /**
     * This new subtype of governance role is to recognize an owner of a data item - this may be stored as data fields in
     * multiple assets and this person typically owns the end to end validation of the values as they move from asset to asset.
     *
     * @return entity definition
     */
    private EntityDef addDataItemOwnerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_ITEM_OWNER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));

    }


    /**
     * This new subtype of governance driver is to represent a single article in a regulation.
     *
     * @return entity definition
     */
    private EntityDef addRegulationArticleEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.REGULATION_ARTICLE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DRIVER.typeName));
    }


    /**
     * This new subtype of governance driver is to represent a business imperative.
     *
     * @return entity definition
     */
    private EntityDef addBusinessImperativeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.BUSINESS_IMPERATIVE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DRIVER.typeName));

    }


    private RelationshipDef addGovernanceDriverLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "linkingDrivers";
        final String                     end1AttributeDescription     = "Governance driver that makes use of another governance driver's requirements.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DRIVER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "linkedDrivers";
        final String                     end2AttributeDescription     = "Governance driver that defines requirements that support another governance driver.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DRIVER.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /**
     * Link GovernanceResponsibility to PersonRole rather than GovernanceRole.
     *
     * @return typeDef patch
     */
    private TypeDefPatch updateGovernanceResponsibilityAssignmentRelationship()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Update end 1.
         */
        final String                     end1AttributeName            = "performedByRoles";
        final String                     end1AttributeDescription     = "The roles assigned to this responsibility.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);

        return typeDefPatch;
    }


    /**
     * Add properties so it is possible to specific the property name use to identify the organization and the business capability.
     *
     * @return typeDef patch
     */
    private TypeDefPatch updateAssetOrigin()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Add properties so it is possible to specific whether involved parties are userIds, roles or profiles.
     *
     * @return typeDef patch
     */
    private TypeDefPatch updateCertification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CUSTODIAN_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECIPIENT_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECIPIENT_PROPERTY_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Add properties so it is possible to specific whether involved parties are userIds, roles or profiles.
     *
     * @return typeDef patch
     */
    private TypeDefPatch updateLicense()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.LICENSE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LICENSED_BY_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LICENSED_BY_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CUSTODIAN_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LICENSEE_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LICENSEE_PROPERTY_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Deprecate the ResponsibilityStaffContact relationship in favour of GovernanceResponsibilityAssignment.
     *
     * @return patch
     */
    private TypeDefPatch deprecateResponsibilityStaffContact()
    {
        final String typeName = "ResponsibilityStaffContact";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /**
     * Deprecate the AssetOwnership classification in favour of the Ownership classification.
     *
     * @return patch
     */
    private TypeDefPatch deprecateAssetOwnershipClassification()
    {
        final String typeName = "AssetOwnership";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /**
     * Deprecate the ownership properties - use Ownership classification instead
     *
     * @return patch
     */
    private TypeDefPatch updateIncidentReport()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.INCIDENT_REPORT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "owner";
        final String attribute1Description     = "Deprecated Attribute. Person, team or engine responsible for this type of action.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "ownerType";
        final String attribute2Description     = "Deprecated Attribute. Type of element representing the owner.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("AssetOwnerType",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }
}

