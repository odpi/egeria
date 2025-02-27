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
public class OpenMetadataTypesArchive2_11
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.11";
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
    public OpenMetadataTypesArchive2_11()
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
    public OpenMetadataTypesArchive2_11(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive2_10 previousTypes = new OpenMetadataTypesArchive2_10(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0010BaseModel();
        update0050ApplicationsAndProcesses();
        update0380SubjectArea();
        update04xxGovernanceDefinitions();
        update0530TabularSchema();
        update0531DocumentSchema();
        update0534RelationalSchema();
        update0535EventSchemas();
        update0536APISchemas();
        update0537DisplaySchemas();
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Mark deprecated field in Asset
     */
    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateAsset());
    }

    /**
     * Deprecate the ownership properties - use Ownership classification instead
     *
     * @return patch
     */
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

        final String attribute1Name            = "owner";
        final String attribute1Description     = "Deprecated Attribute. Person, team or engine responsible for this type of action. Use Ownership classification";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "ownerType";
        final String attribute2Description     = "Deprecated Attribute. Type of element representing the owner. Use Ownership classification";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "zoneMembership";
        final String attribute3Description     = "Deprecated Attribute. The list of zones that this asset belongs to. Use AssetZoneMembership classification";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "latestChange";
        final String attribute4Description     = "Deprecated Attribute. Description of the last change to the asset's metadata. Use LatestChange classification";
        final String attribute4DescriptionGUID = null;

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
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute3Name,
                                                                attribute3Description,
                                                                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add new data managers
     */
    private void update0050ApplicationsAndProcesses()
    {

        this.archiveBuilder.addEntityDef(addAPIManagerEntity());
        this.archiveBuilder.addEntityDef(addEventBrokerEntity());
    }



    /**
     * This new subtype of software server capability for API managers.
     *
     * @return entity definition
     */
    private EntityDef addAPIManagerEntity()
    {
        final String guid            = OpenMetadataType.API_MANAGER.typeGUID;
        final String name            = OpenMetadataType.API_MANAGER.typeName;
        final String description     = OpenMetadataType.API_MANAGER.description;
        final String descriptionGUID = OpenMetadataType.API_MANAGER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.API_MANAGER.wikiURL;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);

    }


    /**
     * This new subtype of software server capability for Event Brokers.
     *
     * @return entity definition
     */
    private EntityDef addEventBrokerEntity()
    {
        final String guid            = OpenMetadataType.EVENT_BROKER.typeGUID;
        final String name            = OpenMetadataType.EVENT_BROKER.typeName;
        final String description     = OpenMetadataType.EVENT_BROKER.description;
        final String descriptionGUID = OpenMetadataType.EVENT_BROKER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.EVENT_BROKER.wikiURL;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0380SubjectArea()

    {
        this.archiveBuilder.addRelationshipDef(addIsATypeOfRelationship());
        this.archiveBuilder.addTypeDefPatch(updateTermIsATypeOfRelationship());
    }

    /**
     * Defines an inheritance relationship between two spine objects. It provides a type for a Spine Object.
     * @return RelationshipDef
     */
    private RelationshipDef addIsATypeOfRelationship()
    {
        final String guid            = "9b6a91b5-a339-4245-b208-040805f95a75";
        final String name            = "IsATypeOfRelationship";
        final String description     = "Defines an inheritance relationship between two spine objects.";
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
        final String                     end1AttributeName            = "Inherited";
        final String                     end1AttributeDescription     = "Inherited (Subtypes) for this object.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "InheritedFrom";
        final String                     end2AttributeDescription     = "Inherited from type (Supertypes) for this object.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
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

        final String attribute2Name            = "status";
        final String attribute2Description     = "The status of or confidence in the relationship.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("TermRelationshipStatus",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;

    }
    /**
     * Deprecate the TermIsATypeOfRelationship - use TermTypeOFRelationship
     *
     * @return patch
     */
    private TypeDefPatch updateTermIsATypeOfRelationship()
    {
        final String typeName = "TermISATypeOFRelationship";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

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

        this.archiveBuilder.addEntityDef(addThreatEntity());

        this.archiveBuilder.addRelationshipDef(addGovernanceDefinitionScopeRelationship());

    }


    /**
     * This relationships allows the scope of a governance definition to be set up.
     *
     * @return  relationship definition
     */
    private RelationshipDef addGovernanceDefinitionScopeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "definitionAppliesTo";
        final String                     end1AttributeDescription     = "Elements defining the scope that the governance definition applies to.";
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
        final String                     end2AttributeName            = "associatedGovernanceDefinitions";
        final String                     end2AttributeDescription     = "Governance definitions for this scope.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }



    /**
     * This new subtype of governance driver is to document a specific threat.
     *
     * @return entity definition
     */
    private EntityDef addThreatEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.THREAT,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DRIVER.typeName));

    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * The TabularColumnType only allows for a column to be primitive - could be a literal - so deprecate.
     * Add TabularFileColumn to be able to distinguish between a tabular column in a file and a relational column
     */
    private void update0530TabularSchema()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateTabularColumnType());
        this.archiveBuilder.addEntityDef(addTabularFileColumnEntity());
    }

    private TypeDefPatch deprecateTabularColumnType()
    {
        final String typeName = "TabularColumnType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /**
     * This new subtype of TabularColumn is to document a column in a tabular file.  This is to allow a distinction between
     * a tabular column in a file and a relational database column.
     *
     * @return entity definition
     */
    private EntityDef addTabularFileColumnEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TABULAR_FILE_COLUMN,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.TABULAR_COLUMN.typeName));

    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Deprecate the specialist SchemaTypes for documents since the offer little value.
     */
    private void update0531DocumentSchema()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateSimpleDocumentType());
        this.archiveBuilder.addTypeDefPatch(deprecateStructDocumentType());
        this.archiveBuilder.addTypeDefPatch(deprecateMapDocumentType());
    }

    private TypeDefPatch deprecateSimpleDocumentType()
    {
        final String typeName = "SimpleDocumentType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateStructDocumentType()
    {
        final String typeName = "StructDocumentType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateMapDocumentType()
    {
        final String typeName = "MapDocumentType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Change superclass of RelationshipTableType to be ComplexSchemaType
     */
    private void update0534RelationalSchema()
    {
        this.archiveBuilder.addTypeDefPatch(updateRelationalTableTypeEntity());
    }


    private TypeDefPatch updateRelationalTableTypeEntity()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.RELATIONAL_TABLE_TYPE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName));

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * A variety of changes to improve definition of EventType schemas
     */
    private void update0535EventSchemas()
    {
        this.archiveBuilder.addEntityDef(addEventTypeListEntity());
        this.archiveBuilder.addEntityDef(addEventSchemaAttributeEntity());
    }


    /**
     * This new subtype of schema type choice for events to make searching easier.
     *
     * @return entity definition
     */
    private EntityDef addEventTypeListEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EVENT_TYPE_LIST,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE_CHOICE.typeName));

    }


    /**
     * This new subtype of schema attribute for events to make searching easier.
     *
     * @return entity definition
     */
    private EntityDef addEventSchemaAttributeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EVENT_SCHEMA_ATTRIBUTE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * A variety of changes to improve definition of API schemas
     */
    private void update0536APISchemas()
    {

        this.archiveBuilder.addEntityDef(addAPIParameterListEntity());
        this.archiveBuilder.addEntityDef(addAPIParameterEntity());
    }



    /**
     * This new subtype of schema type that describes a list of parameters for an API.
     *
     * @return entity definition
     */
    private EntityDef addAPIParameterListEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.API_PARAMETER_LIST,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUIRED));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }




    /**
     * This new subtype of SchemaAttribute is used for describing a data value that is part of a API definition.
     *
     * @return entity definition
     */
    private EntityDef addAPIParameterEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.API_PARAMETER,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PARAMETER_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * A variety of changes to improve definition of schemas that represent displayed data
     */
    private void update0537DisplaySchemas()
    {

        this.archiveBuilder.addEntityDef(addDisplayDataSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(addDisplayDataContainerEntity());
        this.archiveBuilder.addEntityDef(addDisplayDataFieldEntity());
        this.archiveBuilder.addEntityDef(addQuerySchemaTypeEntity());
        this.archiveBuilder.addEntityDef(addQueryDataContainerEntity());
        this.archiveBuilder.addEntityDef(addQueryDataFieldEntity());
    }



    /**
     * This new subtype of schema type that describes a report or form.
     *
     * @return entity definition
     */
    private EntityDef addDisplayDataSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DISPLAY_DATA_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName));
    }


    /**
     * This new subtype of schema type that describes a list of parameters for an API.
     *
     * @return entity definition
     */
    private EntityDef addDisplayDataContainerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DISPLAY_DATA_CONTAINER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));


    }


    /**
     * This new subtype of schema type that describes a list of parameters for an API.
     *
     * @return entity definition
     */
    private EntityDef addDisplayDataFieldEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DISPLAY_DATA_FIELD,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INPUT_FIELD));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * This new subtype of schema type that describes a report or form.
     *
     * @return entity definition
     */
    private EntityDef addQuerySchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.QUERY_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName));
    }


    /**
     * This new subtype of schema attribute that describes a field in an information view.
     *
     * @return entity definition
     */
    private EntityDef addQueryDataContainerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.QUERY_DATA_CONTAINER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));
    }


    /**
     * This new subtype of schema attribute that describes a field in an information view.
     *
     * @return entity definition
     */
    private EntityDef addQueryDataFieldEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.QUERY_DATA_FIELD,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));
    }
}

