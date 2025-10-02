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
public class OpenMetadataTypesArchive5_3
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "5.3";
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
    public OpenMetadataTypesArchive5_3()
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
    public OpenMetadataTypesArchive5_3(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive5_2 previousTypes = new OpenMetadataTypesArchive5_2(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        this.update0010BaseModel();
        this.update0330GlossaryTerms();
        this.update0017ExternalIds();
        this.add058xDataDictionaries();
        this.update0056ResourceManagers();
        this.update0201Connections();
        this.update0205ConnectionLinkage();
        this.update0730SolutionComponents();
        this.update0770LineageMapping();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateAnchors());
    }

    private TypeDefPatch updateAnchors()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_SCOPE_GUID));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0330GlossaryTerms()
    {
        this.archiveBuilder.addTypeDefPatch(updateGlossaryTerm());
    }

    private TypeDefPatch updateGlossaryTerm()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GLOSSARY_TERM.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ALIASES));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0118 Actor Roles and related updates
     */
    private void update0017ExternalIds()
    {
        this.archiveBuilder.addTypeDefPatch(updateExternalId());
    }

    private TypeDefPatch updateExternalId()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.EXTERNAL_ID.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXT_INSTANCE_TYPE_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0201Connections()
    {
        this.archiveBuilder.addRelationshipDef(getConnectToEndpointRelationship());
    }

    private RelationshipDef getConnectToEndpointRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "consumingConnections";
        final String                     end1AttributeDescription     = "Connections using endpoint.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "connectToEndpoint";
        final String                     end2AttributeDescription     = "Location of digital resource.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENDPOINT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private void update0205ConnectionLinkage()
    {
        this.archiveBuilder.addRelationshipDef(getAssetConnectionRelationship());
    }

    private RelationshipDef getAssetConnectionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connectedAsset";
        final String                     end1AttributeDescription     = "Description of digital resource.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "assetConnections";
        final String                     end2AttributeDescription     = "Connections to digital resource.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTION.typeName),
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
     * 0580/0581 Data Dictionaries, Data Specs and Data Fields
     */
    private void add058xDataDictionaries()
    {
        this.archiveBuilder.addEntityDef(getDataDictionaryEntity());
        this.archiveBuilder.addEntityDef(getDataSpecEntity());
        this.archiveBuilder.addEntityDef(getDataStructureEntity());
        this.archiveBuilder.addEntityDef(getDataFieldEntity());

        this.archiveBuilder.addRelationshipDef(getDataDescriptionRelationship());
        this.archiveBuilder.addRelationshipDef(getMemberDataFieldRelationship());

        this.archiveBuilder.addRelationshipDef(getSchemaAttributeDefinitionRelationship());
        this.archiveBuilder.addRelationshipDef(getNestedDataFieldRelationship());
        this.archiveBuilder.addRelationshipDef(getSchemaTypeDefinitionRelationship());
        this.archiveBuilder.addRelationshipDef(getLinkedDataFieldRelationship());

        this.archiveBuilder.addRelationshipDef(getDataClassMatchRelationship());
        this.archiveBuilder.addRelationshipDef(getDataClassDefinitionRelationship());
        this.archiveBuilder.addRelationshipDef(getDataStructureDefinitionRelationship());
        this.archiveBuilder.addRelationshipDef(getSemanticDefinitionRelationship());
    }

    private EntityDef getDataDictionaryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_DICTIONARY_COLLECTION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName));
    }

    private EntityDef getDataSpecEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_SPEC_COLLECTION,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName));
    }

    private EntityDef getDataStructureEntity()
    {
        EntityDef entityDef = archiveHelper.getDocumentLifecycleEntityDef(OpenMetadataType.DATA_STRUCTURE,
                                                                          this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_DEFINED_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAMESPACE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME_PATTERNS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getDataFieldEntity()
    {
        EntityDef entityDef = archiveHelper.getDocumentLifecycleEntityDef(OpenMetadataType.DATA_FIELD,
                                                                          this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAMESPACE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ALIASES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME_PATTERNS));

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_DEFINED_STATUS));

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEFAULT_VALUE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_NULLABLE));

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UNITS));

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MINIMUM_LENGTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LENGTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRECISION));

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORDERED_VALUES));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.SORT_ORDER));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getDataDescriptionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describesDataFor";
        final String                     end1AttributeDescription     = "Element associated with the data being described.";
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
        final String                     end2AttributeName            = "dataDescription";
        final String                     end2AttributeDescription     = "Description of the data.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
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

    private RelationshipDef getMemberDataFieldRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "partOfDataStructure";
        final String                     end1AttributeDescription     = "Parent data structure(s).";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STRUCTURE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "containsDataFields";
        final String                     end2AttributeDescription     = "Contained data fields.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.COVERAGE_CATEGORY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POSITION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_CARDINALITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_CARDINALITY));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getSchemaAttributeDefinitionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SCHEMA_ATTRIBUTE_DEFINITION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "derivedFromDataField";
        final String                     end1AttributeDescription     = "The data fields using this schema.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "equivalentSchemaAttribute";
        final String                     end2AttributeDescription     = "Official schema attribute definition.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getNestedDataFieldRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentDataFields";
        final String                     end1AttributeDescription     = "Parent data field(s).";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "nestedDataFields";
        final String                     end2AttributeDescription     = "Nested data fields.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.COVERAGE_CATEGORY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POSITION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_CARDINALITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_CARDINALITY));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getLinkedDataFieldRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.LINKED_DATA_FIELD_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "linkFromDataFields";
        final String                     end1AttributeDescription     = "Data field that is linked from.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "linkToDataFields";
        final String                     end2AttributeDescription     = "Data field that is linked to.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RELATIONSHIP_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RELATIONSHIP_END));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_CARDINALITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_CARDINALITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getSchemaTypeDefinitionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SCHEMA_TYPE_DEFINITION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "derivedFromDataStructure";
        final String                     end1AttributeDescription     = "The description of the structure.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STRUCTURE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "equivalentSchemaType";
        final String                     end2AttributeDescription     = "Equivalent schema type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getDataClassDefinitionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_CLASS_DEFINITION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describedByDataClasses";
        final String                     end1AttributeDescription     = "Data classes that includes other data classes in its definition.";
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
        final String                     end2AttributeName            = "dataClassDefinition";
        final String                     end2AttributeDescription     = "Data class that provide the data value specification.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getSemanticDefinitionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "semanticallyAssociatedDataDefinitions";
        final String                     end1AttributeDescription     = "Definition elements that are associated with this meaning.";
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
        final String                     end2AttributeName            = "semanticDefinition";
        final String                     end2AttributeDescription     = "Glossary term that provides the semantic information for the definitions.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getDataStructureDefinitionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "validatedByCertificationType";
        final String                     end1AttributeDescription     = "Identifies the certification type used to certify that data matches this data structure.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CERTIFICATION_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "dataStructureDefinition";
        final String                     end2AttributeDescription     = "Data structure that provides the specification used to certify data for the certification type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STRUCTURE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getDataClassMatchRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_CLASS_MATCH_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dataClassAnnotations";
        final String                     end1AttributeDescription     = "The annotations that have identified the data class as a match.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS_ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "dataClassMatches";
        final String                     end2AttributeDescription     = "Data classes that are matching the analysed data.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
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
     * 0730 Solution components
     */
    private void update0730SolutionComponents()
    {
        this.archiveBuilder.addEntityDef(getSolutionActorRoleEntity());
        this.archiveBuilder.addRelationshipDef(getSolutionComponentActorRelationship());
    }


    private EntityDef getSolutionActorRoleEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SOLUTION_ACTOR_ROLE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_ROLE.typeName));
    }


    private RelationshipDef getSolutionComponentActorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "interactingWithActors";
        final String                     end1AttributeDescription     = "The actors that use this component.";
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
        final String                     end2AttributeName            = "interactingWithSolutionComponents";
        final String                     end2AttributeDescription     = "The solution components used by these actors.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOLUTION_COMPONENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ROLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0056 Resource Managers
     */
    private void update0056ResourceManagers()
    {
        this.archiveBuilder.addTypeDefPatch(updateFileSystemClassification());
    }

    private TypeDefPatch updateFileSystemClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.FILE_SYSTEM_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LOCAL_MOUNT_POINT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CANONICAL_MOUNT_POINT));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0056 Resource Managers
     */
    private void update0770LineageMapping()
    {
        this.archiveBuilder.addRelationshipDef(getDataMappingRelationship());
    }


    private RelationshipDef getDataMappingRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_MAPPING_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "sourceAttributes";
        final String                     end1AttributeDescription     = "The data fields that the data is copied from.";
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
        final String                     end2AttributeName            = "targetAttributes";
        final String                     end2AttributeDescription     = "The data fields that the data is copied to.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUERY_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUERY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUERY_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ISC_QUALIFIED_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

