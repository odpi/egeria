/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.properties.AnnotationStatus;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenMetadataTypesArchive1_2 provides the archive content for the open metadata types that were defined before Release 1.3.  These types
 * should not be changed or extended in this file.  New types, and patches to existing types should be added to OpenMetadataTypes.
 * <p>
 * Details of the open metadata types are documented on the wiki:
 * <a href="https://egeria.odpi.org/open-metadata-publication/website/open-metadata-types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive1_2
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveVersion     = "1.2";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1577886131090L);

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
    public OpenMetadataTypesArchive1_2()
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
    public OpenMetadataTypesArchive1_2(OMRSArchiveBuilder archiveBuilder)
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
     * Add the types from this archive to the archive builder supplied in the
     * constructor.
     */
    public void getOriginalTypes()
    {
        this.addStandardPrimitiveDefs();
        this.addStandardCollectionDefs();
        this.addArea0Types();
        this.addArea1Types();
        this.addArea2Types();
        this.addArea3Types();
        this.addArea4Types();
        this.addArea5Types();
        this.addArea6Types();
    }


    /*
     * ========================================
     * Attribute types
     */


    /**
     * Add the standard primitive types to the archive builder.
     */
    public void addStandardPrimitiveDefs()
    {
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING));
        this.archiveBuilder.addPrimitiveDef(archiveHelper.getPrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE));
    }


    /**
     * Add the standard collection types to the archive builder.
     */
    public void addStandardCollectionDefs()
    {
        this.archiveBuilder.addCollectionDef(getMapStringStringCollectionDef());
        this.archiveBuilder.addCollectionDef(getMapStringBooleanCollectionDef());
        this.archiveBuilder.addCollectionDef(getMapStringIntCollectionDef());
        this.archiveBuilder.addCollectionDef(getMapStringLongCollectionDef());
        this.archiveBuilder.addCollectionDef(getMapStringDoubleCollectionDef());
        this.archiveBuilder.addCollectionDef(getMapStringDateCollectionDef());
        this.archiveBuilder.addCollectionDef(getMapStringObjectCollectionDef());
        this.archiveBuilder.addCollectionDef(getArrayStringCollectionDef());
        this.archiveBuilder.addCollectionDef(getArrayIntCollectionDef());
    }


    /**
     * Defines the {@code map<string,string>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringStringCollectionDef()
    {
        final String guid            = "005c7c14-ac84-4136-beed-959401b041f8";
        final String description     = "A map from string to string.";
        final String descriptionGUID = "f285d0ca-50ab-4564-b129-c7e3ba4e8545";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
    }


    /**
     * Defines the {@code map<string,boolean>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringBooleanCollectionDef()
    {
        final String guid            = "8fa603dd-c2c5-43fc-8ff4-92141f2414ab";
        final String description     = "A map from string to Boolean.";
        final String descriptionGUID = "72d76e44-350c-4ff3-baae-54b837f723c7";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
    }


    /**
     * Defines the {@code map<string,int>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringIntCollectionDef()
    {
        final String guid            = "8fa603dd-c2c5-43fc-8ff4-92141f2414ac";
        final String description     = "A map from string to int.";
        final String descriptionGUID = "47373fd6-be40-439b-97ca-881878eed1f4";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
    }


    /**
     * Defines the {@code map<string,long>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringLongCollectionDef()
    {
        final String guid            = "8fa603dd-c2c5-43fc-8ff4-92141f2414ae";
        final String description     = "A map from string to long.";
        final String descriptionGUID = "039b3466-e28b-4c73-8181-d9a57749c706";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
    }


    /**
     * Defines the {@code map<string,double>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringDateCollectionDef()
    {
        final String guid            = "ee293c68-e34d-4885-a512-f927d35a5893";
        final String description     = "A map from string to date.";
        final String descriptionGUID = "978b8ad1-c7c8-4892-bd83-98c9f07e8028";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
    }


    /**
     * Defines the {@code map<string,double>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringDoubleCollectionDef()
    {
        final String guid            = "17211869-ed39-4ba9-bead-ffd967df65a8";
        final String description     = "A map from string to double.";
        final String descriptionGUID = "ee2cf3d5-3ff8-4d6a-82ed-35c0123dcc89";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE);
    }

    /**
     * Defines the {@code map<string,object>} type.
     *
     * @return CollectionDef for this type
     */
    private CollectionDef getMapStringObjectCollectionDef()
    {
        final String guid            = "8fa603dd-c2c5-43fc-8ff4-92141f2414ad";
        final String description     = "A map from string to object.";
        final String descriptionGUID = "6dd2944b-9107-41c6-a10b-6a938fd6f2f5";

        return archiveHelper.getMapCollectionDef(guid,
                                                 description,
                                                 descriptionGUID,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING,
                                                 PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
    }


    /**
     * Define the {@code array<string>} type.
     *
     * @return CollectionDef for this object
     */
    private CollectionDef getArrayStringCollectionDef()
    {
        final String guid            = "0428b5d3-f824-459c-b7f5-f8151de59707";
        final String description     = "An array of strings.";
        final String descriptionGUID = "4a384611-d8c1-4909-a6f8-1385ffe210e2";

        return archiveHelper.getArrayCollectionDef(guid,
                                                   description,
                                                   descriptionGUID,
                                                   PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
    }


    /**
     * Define the {@code array<int>} type.
     *
     * @return CollectionDef for this object
     */
    private CollectionDef getArrayIntCollectionDef()
    {
        final String guid            = "0103fe10-98b0-4910-8ee0-21d529f7ff6d";
        final String description     = "An array of integers.";
        final String descriptionGUID = "ac772d8b-278e-4017-a7e8-1988c74c091e";

        return archiveHelper.getArrayCollectionDef(guid,
                                                   description,
                                                   descriptionGUID,
                                                   PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
    }


    /*
     * ========================================
     * AREA 0: common types and infrastructure
     */

    /**
     * Add the list of area 0 types
     */
    public void addArea0Types()
    {
        this.add0010BaseModel();
        this.add0015LinkedMediaTypes();
        this.add0017ExternalIdentifiers();
        this.add0019MoreInformation();
        this.add0020PropertyFacets();
        this.add0025Locations();
        this.add0030HostsAndPlatforms();
        this.add0035ComplexHosts();
        this.add0037SoftwareServerPlatforms();
        this.add0040SoftwareServers();
        this.add0042SoftwareServerCapabilities();
        this.add0045ServersAndAssets();
        this.add0050ApplicationsAndProcesses();
        this.add0055DataProcessingEngines();
        this.add0070NetworksAndGateways();
        this.add0090CloudPlatformsAndServices();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0010 Base Model defines the core entities that have been inherited from the original Apache Atlas model.
     * It defines an initial set of asset types that need to be governed (more assets are defined in Area 2).
     */
    private void add0010BaseModel()
    {
        this.archiveBuilder.addEnumDef(getAssetOwnerTypeEnum());

        this.archiveBuilder.addEntityDef(getReferenceableEntity());
        this.archiveBuilder.addEntityDef(getAssetEntity());
        this.archiveBuilder.addEntityDef(getInfrastructureEntity());
        this.archiveBuilder.addEntityDef(getProcessEntity());
        this.archiveBuilder.addEntityDef(getDataAssetEntity());
        this.archiveBuilder.addEntityDef(getDataSetEntity());
    }


    /**
     * The Referenceable entity is the superclass of all the governed open metadata entities.  It specifies that
     * these entities have a unique identifier called OpenMetadataProperty.QUALIFIED_NAME.name.
     *
     * @return Referenceable EntityDef
     */
    private EntityDef getReferenceableEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.REFERENCEABLE, null);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME);

        property.setUnique(true);
        property.setValuesMaxCount(1);
        property.setValuesMinCount(1);
        property.setAttributeCardinality(AttributeCardinality.ONE_ONLY);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * The Asset entity is the root entity for the assets that open metadata and governance is governing.
     *
     * @return Asset EntityDef
     */
    private EntityDef getAssetEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ASSET,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        /* Moved to Ownership classification */
        final String attribute4Name            = "ownerType";
        final String attribute4Description     = "Deprecated. Type of identifier used for owner property.";
        final String attribute4DescriptionGUID = null;
        /* Moved to LatestChange entity  */
        final String attribute6Name            = "latestChange";
        final String attribute6Description     = "Description of the last change to the asset's metadata.";
        final String attribute6DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        /* Moved to Ownership classification */
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OWNER));

        property = archiveHelper.getEnumTypeDefAttribute("AssetOwnerType",
                                                         attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);

        /* Moved to AssetZoneMembership classification */
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ZONE_MEMBERSHIP));

        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * The Infrastructure entity describes an asset that is physical infrastructure or part of the software
     * platform that supports the data and process assets.
     *
     * @return Infrastructure EntityDef
     */
    private EntityDef getInfrastructureEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.INFRASTRUCTURE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));
    }


    /**
     * The Process entity describes a well-defined sequence of activities performed by people or software components.
     *
     * @return Process EntityDef
     */
    private EntityDef getProcessEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.PROCESS,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));

        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA));

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();

        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DELETED);

        entityDef.setValidInstanceStatusList(validInstanceStatusList);
        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }



    /**
     * The DataSet entity describes a collection of related data.
     *
     * @return DataSet EntityDef
     */
    private EntityDef getDataAssetEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_ASSET,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));
    }



    /**
     * The DataSet entity describes a collection of related data.
     *
     * @return DataSet EntityDef
     */
    private EntityDef getDataSetEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_SET,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_ASSET.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0015 Linked Media Types describe different media (like images and documents) that enhance the description
     * of an entity.  Media entities can be added to any Referenceable entities.
     */
    private void add0015LinkedMediaTypes()
    {
        this.archiveBuilder.addEnumDef(getMediaTypeEnum());
        this.archiveBuilder.addEnumDef(getMediaUsageEnum());

        this.archiveBuilder.addEntityDef(getExternalReferenceEntity());
        this.archiveBuilder.addEntityDef(getRelatedMediaEntity());

        this.archiveBuilder.addRelationshipDef(getExternalReferenceLinkRelationship());
        this.archiveBuilder.addRelationshipDef(getMediaReferenceRelationship());

    }


    private EnumDef getMediaTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(MediaType.getOpenTypeGUID(),
                                                        MediaType.getOpenTypeName(),
                                                        MediaType.getOpenTypeDescription(),
                                                        MediaType.getOpenTypeDescriptionGUID(),
                                                        MediaType.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (MediaType enumValue : MediaType.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EnumDef getMediaUsageEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(MediaUsage.getOpenTypeGUID(),
                                                        MediaUsage.getOpenTypeName(),
                                                        MediaUsage.getOpenTypeDescription(),
                                                        MediaUsage.getOpenTypeDescriptionGUID(),
                                                        MediaUsage.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (MediaUsage enumValue : MediaUsage.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getExternalReferenceEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.EXTERNAL_REFERENCE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.URL));

        property = archiveHelper.getStringTypeDefAttribute("version",
                                                           "Version number of the external reference.",
                                                           null);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORGANIZATION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getRelatedMediaEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RELATED_MEDIA,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_REFERENCE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getArrayIntTypeDefAttribute("mediaUsages",
                                                             "Types of recommended media usage.",
                                                             null);
        properties.add(property);

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.MEDIA_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getExternalReferenceLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "relatedItem";
        final String                     end1AttributeDescription     = "Item that is referencing this work.";
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
        final String                     end2AttributeName            = "externalReference";
        final String                     end2AttributeDescription     = "Link to more information from an external source.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFERENCE_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getMediaReferenceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "consumingItem";
        final String                     end1AttributeDescription     = "Item that is referencing this work.";
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
        final String                     end2AttributeName            = "relatedMedia";
        final String                     end2AttributeDescription     = "Link to external media.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.RELATED_MEDIA.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MEDIA_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0017 External Identifiers define identifiers used to identify this entity in other systems.
     */
    private void add0017ExternalIdentifiers()
    {
        this.archiveBuilder.addEnumDef(getKeyPatternEnum());

        this.archiveBuilder.addEntityDef(getExternalIdEntity());

        this.archiveBuilder.addRelationshipDef(getExternalIdScopeRelationship());
        this.archiveBuilder.addRelationshipDef(getExternalIdLinkRelationship());
    }


    private EnumDef getKeyPatternEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(KeyPattern.getOpenTypeGUID(),
                                                        KeyPattern.getOpenTypeName(),
                                                        KeyPattern.getOpenTypeDescription(),
                                                        KeyPattern.getOpenTypeDescriptionGUID(),
                                                        KeyPattern.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (KeyPattern enumValue : KeyPattern.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getExternalIdEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.EXTERNAL_ID,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IDENTIFIER));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.KEY_PATTERN));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getExternalIdScopeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "scopedTo";
        final String                     end1AttributeDescription     = "Identifies where this external identifier is known.";
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
        final String                     end2AttributeName            = "managedResources";
        final String                     end2AttributeDescription     = "Link to details of a resource that this component manages.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_ID.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getExternalIdLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "resources";
        final String                     end1AttributeDescription     = "Resource being identified.";
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
        final String                     end2AttributeName            = "alsoKnownAs";
        final String                     end2AttributeDescription     = "Identifier used in an external system.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_ID.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0019 More information
     */
    private void add0019MoreInformation()
    {
        this.archiveBuilder.addRelationshipDef(getMoreInformationRelationship());
    }


    private RelationshipDef getMoreInformationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.MORE_INFORMATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describes";
        final String                     end1AttributeDescription     = "Describes this core element.";
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
        final String                     end2AttributeName            = "providesMoreInformation";
        final String                     end2AttributeDescription     = "Provides more information about this referenceable.";
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
     * 0020 Property Facets define blocks of properties that are unique to a particular vendor or service.
     */
    private void add0020PropertyFacets()
    {
        this.archiveBuilder.addEntityDef(getPropertyFacetEntity());

        this.archiveBuilder.addRelationshipDef(getReferenceableFacetRelationship());
    }


    private EntityDef getPropertyFacetEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.PROPERTY_FACET,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Version of the property facet schema.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROPERTIES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getReferenceableFacetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REFERENCEABLE_FACET,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "relatedEntity";
        final String                     end1AttributeDescription     = "Identifies which element this property facet belongs to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "facets";
        final String                     end2AttributeDescription     = "Additional properties from different sources.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROPERTY_FACET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0025 Locations define entities that describe physical, logical and cyber locations for Assets.
     */
    private void add0025Locations()
    {
        this.archiveBuilder.addEntityDef(getLocationEntity());

        this.archiveBuilder.addRelationshipDef(getNestedLocationRelationship());
        this.archiveBuilder.addRelationshipDef(getAdjacentLocationRelationship());
        this.archiveBuilder.addRelationshipDef(getAssetLocationRelationship());

        this.archiveBuilder.addClassificationDef(getMobileAssetClassification());
        this.archiveBuilder.addClassificationDef(getFixedLocationClassification());
        this.archiveBuilder.addClassificationDef(getSecureLocationClassification());
        this.archiveBuilder.addClassificationDef(getCyberLocationClassification());
    }


    private EntityDef getLocationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.LOCATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getNestedLocationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NESTED_LOCATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "groupingLocations";
        final String                     end1AttributeDescription     = "Location that is covering the broader area.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "nestedLocations";
        final String                     end2AttributeDescription     = "Location that is nested in this location.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getAdjacentLocationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ADJACENT_LOCATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "peerLocations";
        final String                     end1AttributeDescription     = "Location that is adjacent to this location.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "peerLocations";
        final String                     end2AttributeDescription     = "Location that is adjacent to this location.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getAssetLocationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSET_LOCATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "knownLocations";
        final String                     end1AttributeDescription     = "Places where this asset is sited.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "localAssets";
        final String                     end2AttributeDescription     = "Assets sited at this location.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private ClassificationDef getMobileAssetClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.MOBILE_ASSET_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                  false);
    }

    private ClassificationDef getFixedLocationClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.FIXED_LOCATION_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "address";
        final String attribute2Description     = "Postal address of this location.";
        final String attribute2DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COORDINATES));
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TIME_ZONE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getSecureLocationClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.SECURE_LOCATION_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LEVEL));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getCyberLocationClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CYBER_LOCATION_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "address";
        final String attribute1Description     = "Network address of the location.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0030 Hosts and Platforms describe the Software and Hardware platforms hosting Assets.
     */
    private void add0030HostsAndPlatforms()
    {
        this.archiveBuilder.addEnumDef(getEndiannessEnum());

        this.archiveBuilder.addEntityDef(getITInfrastructureEntity());
        this.archiveBuilder.addEntityDef(getHostEntity());
        this.archiveBuilder.addEntityDef(getOperatingPlatformEntity());

        this.archiveBuilder.addRelationshipDef(getHostLocationRelationship());
        this.archiveBuilder.addRelationshipDef(getHostOperatingPlatformRelationship());
    }

    private EnumDef getEndiannessEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ByteOrdering.getOpenTypeGUID(),
                                                        ByteOrdering.getOpenTypeName(),
                                                        ByteOrdering.getOpenTypeDescription(),
                                                        ByteOrdering.getOpenTypeDescriptionGUID(),
                                                        ByteOrdering.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ByteOrdering byteOrdering : ByteOrdering.values())
        {
            elementDef = archiveHelper.getEnumElementDef(byteOrdering.getOrdinal(),
                                                         byteOrdering.getName(),
                                                         byteOrdering.getDescription(),
                                                         byteOrdering.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (byteOrdering.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef getITInfrastructureEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.IT_INFRASTRUCTURE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.INFRASTRUCTURE.typeName));
    }

    private EntityDef getHostEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.HOST,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName));
    }

    private EntityDef getOperatingPlatformEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.OPERATING_PLATFORM,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute4Name            = "endianness";
        final String attribute4Description     = "Definition of byte ordering.";
        final String attribute4DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OPERATING_SYSTEM));

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getHostLocationRelationship()
    {
        final String guid            = "f3066075-9611-4886-9244-32cc6eb07ea9";
        final String name            = "HostLocation";
        final String description     = "Defines the location of a host.";
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
        final String                     end1AttributeName            = "localHosts";
        final String                     end1AttributeDescription     = "Host sited at this location.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "locations";
        final String                     end2AttributeDescription     = "Locations for this host.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LOCATION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getHostOperatingPlatformRelationship()
    {
        final String guid            = "b9179df5-6e23-4581-a8b0-2919e6322b12";
        final String name            = "HostOperatingPlatform";
        final String description     = "Identifies the operating platform for a host.";
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
        final String                     end1AttributeName            = "hosts";
        final String                     end1AttributeDescription     = "Host supporting this operating platform.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "operatingPlatform";
        final String                     end2AttributeDescription     = "Type of platform supported by this host.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPERATING_PLATFORM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0035 Complex Hosts describe virtualization and clustering options for Hosts.
     */
    private void add0035ComplexHosts()
    {
        this.archiveBuilder.addEntityDef(getHostClusterEntity());
        this.archiveBuilder.addEntityDef(getVirtualContainerEntity());

        this.archiveBuilder.addRelationshipDef(getHostClusterMemberRelationship());
        this.archiveBuilder.addRelationshipDef(getDeployedVirtualContainerRelationship());
    }

    private EntityDef getHostClusterEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.HOST_CLUSTER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName));
    }

    private EntityDef getVirtualContainerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VIRTUAL_CONTAINER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName));
    }

    private RelationshipDef getHostClusterMemberRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "hostCluster";
        final String                     end1AttributeDescription     = "Cluster managing this host.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST_CLUSTER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "managedHosts";
        final String                     end2AttributeDescription     = "Member of the host cluster.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getDeployedVirtualContainerRelationship()
    {
        final String guid            = "4b981d89-e356-4d9b-8f17-b3a8d5a86676";
        final String name            = "DeployedVirtualContainer";
        final String description     = "Identifies the real host where a virtual container is deployed to.";
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
        final String                     end1AttributeName            = "hosts";
        final String                     end1AttributeDescription     = "Deployed host for this container.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "hostedContainers";
        final String                     end2AttributeDescription     = "Virtual containers deployed on this host.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VIRTUAL_CONTAINER.typeName),
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
     * 0037 Software Server Platforms describe the structure of a software server platform.
     */
    private void add0037SoftwareServerPlatforms()
    {
        this.archiveBuilder.addEnumDef(getOperationalStatusEnum());

        this.archiveBuilder.addEntityDef(getSoftwareServerPlatformEntity());

        this.archiveBuilder.addRelationshipDef(getSoftwareServerPlatformDeploymentRelationship());
    }

    private EnumDef getOperationalStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(OperationalStatus.getOpenTypeGUID(),
                                                        OperationalStatus.getOpenTypeName(),
                                                        OperationalStatus.getOpenTypeDescription(),
                                                        OperationalStatus.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (OperationalStatus enumValue : OperationalStatus.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef getSoftwareServerPlatformEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SOFTWARE_SERVER_PLATFORM,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of software server platform.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "version";
        final String attribute2Description     = "Version number of the software server platform.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_ID));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getSoftwareServerPlatformDeploymentRelationship()
    {
        final String guid            = "b909eb3b-5205-4180-9f63-122a65b30738";
        final String name            = "SoftwareServerPlatformDeployment";
        final String description     = "Defines the host that a software server platform is deployed to.";
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
        final String                     end1AttributeName            = "host";
        final String                     end1AttributeDescription     = "Supporting host.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "deployedServerPlatforms";
        final String                     end2AttributeDescription     = "Software server platforms deployed on this host.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName),
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

        final String attribute1Name            = "deploymentTime";
        final String attribute1Description     = "Time that the software server platform was deployed to the host.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "deployer";
        final String attribute2Description     = "Person, organization or engine that deployed the software server platform.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "platformStatus";
        final String attribute3Description     = "The operational status of the software server platform on this host.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_NAME,
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0040 Software Servers describe the structure of a software server and its capabilities.
     */
    private void add0040SoftwareServers()
    {
        this.archiveBuilder.addEntityDef(getSoftwareServerEntity());
        this.archiveBuilder.addEntityDef(getEndpointEntity());

        this.archiveBuilder.addRelationshipDef(getSoftwareServerDeploymentRelationship());
        this.archiveBuilder.addRelationshipDef(getServerEndpointRelationship());
    }

    private EntityDef getSoftwareServerEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SOFTWARE_SERVER,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of software server.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "version";
        final String attribute2Description     = "Version number of the software server.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_ID));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getEndpointEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ENDPOINT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NETWORK_ADDRESS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROTOCOL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCRYPTION_METHOD));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getSoftwareServerDeploymentRelationship()
    {
        final String guid            = "d909eb3b-5205-4180-9f63-122a65b30738";
        final String name            = "SoftwareServerDeployment";
        final String description     = "Defines the platform that a software server is deployed to.";
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
        final String                     end1AttributeName            = "hostingPlatform";
        final String                     end1AttributeDescription     = "Supporting platform for the software server.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "deployedSoftwareServers";
        final String                     end2AttributeDescription     = "Software servers deployed on this platform.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
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

        final String attribute1Name            = "deploymentTime";
        final String attribute1Description     = "Time that the software server was deployed to the platform.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "deployer";
        final String attribute2Description     = "Person, organization or engine that deployed the software server.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "serverStatus";
        final String attribute3Description     = "The operational status of the software server on this platform.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_NAME,
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getServerEndpointRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "servers";
        final String                     end1AttributeDescription     = "Server supporting this endpoint.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "endpoints";
        final String                     end2AttributeDescription     = "Endpoints supported by this server.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENDPOINT.typeName),
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
     * 0042 Software Server Capabilities describe the deployed capabilities on a software server.
     */
    private void add0042SoftwareServerCapabilities()
    {

        this.archiveBuilder.addEntityDef(getSoftwareServerCapabilityEntity());

        this.archiveBuilder.addRelationshipDef(getSoftwareServerSupportedCapabilityRelationship());
    }

    private EntityDef getSoftwareServerCapabilityEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = "type";
        final String attribute3Description     = "Type of the software server capability.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "version";
        final String attribute4Description     = "Version number of the software server capability.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getSoftwareServerSupportedCapabilityRelationship()
    {
        final String guid            = "8b7d7da5-0668-4174-a43b-8f8c6c068dd0";
        final String name            = "SoftwareServerSupportedCapability";
        final String description     = "Identifies a software capability that is deployed to a software server.";
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
        final String                     end1AttributeName            = "servers";
        final String                     end1AttributeDescription     = "Servers hosting this capability.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "capabilities";
        final String                     end2AttributeDescription     = "Capabilities deployed on this software server.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
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

        final String attribute3Name            = "serverCapabilityStatus";
        final String attribute3Description     = "The operational status of the software server capability on this software server.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute(OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_NAME,
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0045 Servers and Assets defines the relationships between SoftwareServers and Assets.
     */
    private void add0045ServersAndAssets()
    {
        this.archiveBuilder.addEnumDef(getServerAssetUseTypeEnum());

        this.archiveBuilder.addRelationshipDef(getServerAssetUseRelationship());
    }


    private EnumDef getServerAssetUseTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ServerAssetUseType.getOpenTypeGUID(),
                                                        ServerAssetUseType.getOpenTypeName(),
                                                        ServerAssetUseType.getOpenTypeDescription(),
                                                        ServerAssetUseType.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ServerAssetUseType enumValue : ServerAssetUseType.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private RelationshipDef getServerAssetUseRelationship()
    {
        final String guid            = "92b75926-8e9a-46c7-9d98-89009f622397";
        final String name            = "AssetServerUse";
        final String description     = "Defines that a server capability is using an asset.";
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
        final String                     end1AttributeName            = "consumedBy";
        final String                     end1AttributeDescription     = "Capability consuming this asset.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "consumedAsset";
        final String                     end2AttributeDescription     = "Asset that this software server capability is dependent on.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
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

        property = archiveHelper.getEnumTypeDefAttribute(ServerAssetUseType.getOpenTypeName(),
                                                         OpenMetadataProperty.USE_TYPE.name,
                                                         OpenMetadataProperty.USE_TYPE.description,
                                                         OpenMetadataProperty.USE_TYPE.descriptionGUID);
        properties.add(property);
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

    private void add0050ApplicationsAndProcesses()
    {
        this.archiveBuilder.addEntityDef(getApplicationEntity());

        this.archiveBuilder.addRelationshipDef(getRuntimeForProcessRelationship());

        this.archiveBuilder.addClassificationDef(getApplicationServerClassification());
        this.archiveBuilder.addClassificationDef(getWebserverClassification());
    }

    private EntityDef getApplicationEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.APPLICATION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }

    private RelationshipDef getRuntimeForProcessRelationship()
    {
        final String guid            = "f6b5cf4f-7b88-47df-aeb0-d80d28ba1ec1";
        final String name            = "RuntimeForProcess";
        final String description     = "Identifies the deployed application that supports a specific automated process.";
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
        final String                     end1AttributeName            = "implementingApplication";
        final String                     end1AttributeDescription     = "Application that contains the process implementation.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.APPLICATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "implementedProcesses";
        final String                     end2AttributeDescription     = "Processes that are implemented by this application.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private ClassificationDef getApplicationServerClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.APPLICATION_SERVER_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                  false);
    }

    private ClassificationDef getWebserverClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.WEBSERVER_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0055DataProcessingEngines()
    {
        this.archiveBuilder.addEntityDef(getEngineEntity());

        this.archiveBuilder.addClassificationDef(getWorkflowEngineClassification());
        this.archiveBuilder.addClassificationDef(getReportingEngineClassification());
        this.archiveBuilder.addClassificationDef(getAnalyticsEngineClassification());
        this.archiveBuilder.addClassificationDef(getDataMovementEngineClassification());
        this.archiveBuilder.addClassificationDef(getDataVirtualizationEngineClassification());
    }

    private EntityDef getEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ENGINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }

    private ClassificationDef getWorkflowEngineClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.WORKFLOW_ENGINE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE.typeName),
                                                  false);
    }

    private ClassificationDef getReportingEngineClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.REPORTING_ENGINE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE.typeName),
                                                  false);
    }

    private ClassificationDef getAnalyticsEngineClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.ANALYTICS_ENGINE,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE.typeName),
                                                  false);
    }

    private ClassificationDef getDataMovementEngineClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.DATA_MOVEMENT_ENGINE,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE.typeName),
                                                  false);
    }

    private ClassificationDef getDataVirtualizationEngineClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.DATA_VIRTUALIZATION_ENGINE,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0070 Networks and Gateways provide a very simple network model.
     */
    private void add0070NetworksAndGateways()
    {
        this.archiveBuilder.addEntityDef(getNetworkEntity());
        this.archiveBuilder.addEntityDef(getNetworkGatewayEntity());

        this.archiveBuilder.addRelationshipDef(getHostNetworkRelationship());
        this.archiveBuilder.addRelationshipDef(getNetworkGatewayLinkRelationship());
    }

    private EntityDef getNetworkEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.NETWORK,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName));
    }


    private EntityDef getNetworkGatewayEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.NETWORK_GATEWAY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }


    private RelationshipDef getHostNetworkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.HOST_NETWORK,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connectedHosts";
        final String                     end1AttributeDescription     = "Hosts connected to this network.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "networkConnections";
        final String                     end2AttributeDescription     = "Connections to different networks.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NETWORK.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getNetworkGatewayLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "gateways";
        final String                     end1AttributeDescription     = "Gateways to other networks.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NETWORK_GATEWAY.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "networkConnections";
        final String                     end2AttributeDescription     = "Connections to different networks.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NETWORK.typeName),
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
     * 0090 Cloud Platforms and Services provides classifications for infrastructure to allow cloud platforms
     * and services to be identified.
     */
    private void add0090CloudPlatformsAndServices()
    {
        this.archiveBuilder.addClassificationDef(getCloudProviderClassification());
        this.archiveBuilder.addClassificationDef(getCloudPlatformClassification());
        this.archiveBuilder.addClassificationDef(getCloudTenantClassification());
        this.archiveBuilder.addClassificationDef(getCloudServiceClassification());
    }


    private ClassificationDef getCloudProviderClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROVIDER_NAME));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getCloudPlatformClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of cloud platform.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getCloudTenantClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CLOUD_TENANT_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "type";
        final String attribute2Description     = "Description of the type of tenant.";
        final String attribute2DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TENANT_NAME));
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getCloudServiceClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "type";
        final String attribute2Description     = "Description of the type of the service.";
        final String attribute2DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OFFERING_NAME));

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    /*
     * ========================================
     * AREA 1: collaboration
     */

    private void addArea1Types()
    {
        this.add0110Actors();
        this.add0112People();
        this.add0115Teams();
        this.add0117EngineProfiles();
        this.add0120Collections();
        this.add0130Projects();
        this.add0135Meetings();
        this.add0137Actions();
        this.add0140Communities();
        this.add0150Feedback();
        this.add0155CrowdSourcing();
        this.add0160Notes();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0110 Actors describe the people and their relationships who are using the Assets.
     */
    private void add0110Actors()
    {
        this.archiveBuilder.addEnumDef(getContactMethodTypeEnum());

        this.archiveBuilder.addEntityDef(getActorProfileEntity());
        this.archiveBuilder.addEntityDef(getUserIdentityEntity());
        this.archiveBuilder.addEntityDef(getContactDetailsEntity());

        this.archiveBuilder.addRelationshipDef(getContactThroughRelationship());
        this.archiveBuilder.addRelationshipDef(getProfileIdentityRelationship());
    }

    private EnumDef getContactMethodTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ContactMethodType.getOpenTypeGUID(),
                                                        ContactMethodType.getOpenTypeName(),
                                                        ContactMethodType.getOpenTypeDescription(),
                                                        ContactMethodType.getOpenTypeDescriptionGUID(),
                                                        ContactMethodType.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ContactMethodType enumValues : ContactMethodType.values())
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

    private EntityDef getActorProfileEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ACTOR_PROFILE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getUserIdentityEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.USER_IDENTITY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));
    }

    private EntityDef getContactDetailsEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONTACT_DETAILS,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.CONTACT_METHOD_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTACT_METHOD_VALUE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTACT_METHOD_SERVICE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getContactThroughRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "contactDetails";
        final String                     end1AttributeDescription     = "Contact details owner.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "contacts";
        final String                     end2AttributeDescription     = "Contact information.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTACT_DETAILS.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getProfileIdentityRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "profile";
        final String                     end1AttributeDescription     = "Description of the person, organization or engine that uses this user identity.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "userIdentities";
        final String                     end2AttributeDescription     = "Authentication identifiers in use by the owner of this profile.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.USER_IDENTITY.typeName),
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
     * 0112 People describe the people, their roles and their peer network.
     */
    private void add0112People()
    {
        this.archiveBuilder.addEntityDef(getPersonEntity());
        this.archiveBuilder.addEntityDef(getContributionRecordEntity());
        this.archiveBuilder.addEntityDef(getPersonRoleEntity());

        this.archiveBuilder.addRelationshipDef(getPersonRoleAppointmentRelationship());
        this.archiveBuilder.addRelationshipDef(getPersonalContributionRelationship());
        this.archiveBuilder.addRelationshipDef(getPeerRelationship());
    }

    private EntityDef getPersonEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.PERSON,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FULL_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.JOB_TITLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getContributionRecordEntity()
    {
       EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONTRIBUTION_RECORD,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.KARMA_POINTS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getPersonRoleEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PERSON_ROLE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));
    }

    private RelationshipDef getPersonRoleAppointmentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "rolePerformers";
        final String                     end1AttributeDescription     = "The people performing this role.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "performsRoles";
        final String                     end2AttributeDescription     = "Roles performed by this person.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getPersonalContributionRelationship() // Deprecated in favour of ContributionRecord
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PERSONAL_CONTRIBUTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "contributor";
        final String                     end1AttributeDescription     = "The person behind the contribution.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "contributionRecord";
        final String                     end2AttributeDescription     = "The record of activity by this person.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTRIBUTION_RECORD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getPeerRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PEER_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "peers";
        final String                     end1AttributeDescription     = "List of this person's peer network.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "peers";
        final String                     end2AttributeDescription     = "List of this person's peer network.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON.typeName),
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
     * 0115 Teams describe the organization of people.
     */
    private void add0115Teams()
    {
        this.archiveBuilder.addEntityDef(getTeamEntity());
        this.archiveBuilder.addEntityDef(getTeamLeaderEntity());
        this.archiveBuilder.addEntityDef(getTeamMemberEntity());

        this.archiveBuilder.addRelationshipDef(getTeamLeadershipRelationship());
        this.archiveBuilder.addRelationshipDef(getTeamMembershipRelationship());
        this.archiveBuilder.addRelationshipDef(getTeamStructureRelationship());
    }

    private EntityDef getTeamEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.TEAM,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TEAM_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getTeamLeaderEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TEAM_LEADER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName));
    }

    private EntityDef getTeamMemberEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TEAM_MEMBER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName));
    }

    private RelationshipDef getTeamStructureRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "superTeam";
        final String                     end1AttributeDescription     = "The aggregating team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "subTeam";
        final String                     end2AttributeDescription     = "The teams where work is delegated to.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
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

        property = archiveHelper.getBooleanTypeDefAttribute(OpenMetadataProperty.DELEGATION_ESCALATION.name,
                                                            OpenMetadataProperty.DELEGATION_ESCALATION.description,
                                                            OpenMetadataProperty.DELEGATION_ESCALATION.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getTeamLeadershipRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "teamLeaders";
        final String                     end1AttributeDescription     = "The leaders of the team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM_LEADER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "leadsTeam";
        final String                     end2AttributeDescription     = "The team lead by this team leader.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ROLE_POSITION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getTeamMembershipRelationship()
    {
        final String guid            = "1ebc4fb2-b62a-4269-8f18-e9237a2119ca";
        final String name            = "TeamMembership";
        final String description     = "Relationship identifying the members of teams.";
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
        final String                     end1AttributeName            = "teamMembers";
        final String                     end1AttributeDescription     = "The members of the team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM_MEMBER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "memberOfTeam";
        final String                     end2AttributeDescription     = "The team that this person is a member of.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ROLE_POSITION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0117 IT profiles defines a special actor profile for an engine
     */
    private void add0117EngineProfiles()
    {
        this.archiveBuilder.addEntityDef(getITProfileEntity());
    }

    private EntityDef getITProfileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.IT_PROFILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0120 Collections defines how to group related Referenceables together
     */
    private void add0120Collections()
    {
        this.archiveBuilder.addEnumDef(getOrderByEnum());

        this.archiveBuilder.addEntityDef(getCollectionEntity());

        this.archiveBuilder.addRelationshipDef(getCollectionMembershipRelationship());
        this.archiveBuilder.addRelationshipDef(getResourceListRelationship());

        this.archiveBuilder.addClassificationDef(getFolderClassification());
        this.archiveBuilder.addClassificationDef(getSetClassification());
    }


    private EnumDef getOrderByEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(OrderBy.getOpenTypeGUID(),
                                                        OrderBy.getOpenTypeName(),
                                                        OrderBy.getOpenTypeDescription(),
                                                        OrderBy.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (OrderBy orderBy : OrderBy.values())
        {
            elementDef = archiveHelper.getEnumElementDef(orderBy.getOrdinal(),
                                                         orderBy.getName(),
                                                         orderBy.getDescription(),
                                                         orderBy.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (orderBy.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getCollectionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.COLLECTION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getCollectionMembershipRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "foundInCollections";
        final String                     end1AttributeDescription     = "Collections that link to this element.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "collectionMembers";
        final String                     end2AttributeDescription     = "Members of this collection.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MEMBERSHIP_RATIONALE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getResourceListRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "resourceListAnchors";
        final String                     end1AttributeDescription     = "Referenceable objects that are using the linked to resource.";
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
        final String                     end2AttributeName            = "supportingResources";
        final String                     end2AttributeDescription     = "Resources identified as of interest to the anchor.";
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

        final String attribute1Name            = "resourceUse";
        final String attribute1Description     = "Identifier that describes the type of resource use.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "resourceUseDescription";
        final String attribute2Description     = "Description of how the resource is used, or why it is useful.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "resourceUseProperties";
        final String attribute3Description     = "Additional properties that explains how to use the resource.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "watchResource";
        final String attribute4Description     = "Indicator whether the anchor should receive notifications of changes to the resource.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute4Name,
                                                            attribute4Description,
                                                            attribute4DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private ClassificationDef getFolderClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.FOLDER,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.COLLECTION_ORDER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORDER_BY_PROPERTY_NAME));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getSetClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.RESULTS_SET,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0130 Projects describes the structure of a project and related collections.
     */
    private void add0130Projects()
    {
        this.archiveBuilder.addEntityDef(getProjectEntity());
        this.archiveBuilder.addEntityDef(getProjectManagerEntity());

        this.archiveBuilder.addRelationshipDef(getProjectHierarchyRelationship());
        this.archiveBuilder.addRelationshipDef(getProjectDependencyRelationship());
        this.archiveBuilder.addRelationshipDef(getProjectTeamRelationship());
        this.archiveBuilder.addRelationshipDef(getProjectScopeRelationship());
        this.archiveBuilder.addRelationshipDef(getProjectManagementRelationship());

        this.archiveBuilder.addClassificationDef(getTaskClassification());
        this.archiveBuilder.addClassificationDef(getCampaignClassification());
    }


    private EntityDef getProjectEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.PROJECT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = "startDate";
        final String attribute3Description     = "Start date of the project.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "plannedEndDate";
        final String attribute4Description     = "Planned completion data for the project.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "status";
        final String attribute5Description     = "Short description on current status of the project.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NAME.name,
                                                           OpenMetadataProperty.NAME.description,
                                                           OpenMetadataProperty.NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getProjectManagerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PROJECT_MANAGER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName));
    }


    private RelationshipDef getProjectHierarchyRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "managingProject";
        final String                     end1AttributeDescription     = "Project that oversees this project.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "managedProject";
        final String                     end2AttributeDescription     = "Project that this project is responsible for managing.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getProjectDependencyRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dependentProject";
        final String                     end1AttributeDescription     = "Projects that are dependent on this project.";
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
        final String                     end2AttributeName            = "dependsOnProjects";
        final String                     end2AttributeDescription     = "Projects that are delivering resources or outcomes needed by this project.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEPENDENCY_SUMMARY));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getProjectTeamRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROJECT_TEAM_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "projectFocus";
        final String                     end1AttributeDescription     = "Projects that a team is working on.";
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
        final String                     end2AttributeName            = "supportingActors";
        final String                     end2AttributeDescription     = "People and teams supporting this project.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TEAM_ROLE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getProjectScopeRelationship()
    {
        final String guid            = "bc63ac45-b4d0-4fba-b583-92859de77dd8";
        final String name            = "ProjectScope";
        final String description     = "The documentation, assets and definitions that are affected by the project.";
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
        final String                     end1AttributeName            = "impactingProjects";
        final String                     end1AttributeDescription     = "The projects that are making changes to these elements.";
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
        final String                     end2AttributeName            = "projectScope";
        final String                     end2AttributeDescription     = "The elements that are being changed by this project.";
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

        final String attribute1Name            = "scopeDescription";
        final String attribute1Description     = "Description of how each item is being changed by the project.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /**
     * Add Task classification
     *
     * @return classification def
     */
    private ClassificationDef getTaskClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.TASK_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                  false);
    }


    private RelationshipDef getProjectManagementRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "projectsManaged";
        final String                     end1AttributeDescription     = "The projects that are being managed by this project manager.";
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
        final String                     end2AttributeName            = "projectManagers";
        final String                     end2AttributeDescription     = "The roles for managing this project.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT_MANAGER.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private ClassificationDef getCampaignClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.CAMPAIGN_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0135 Meetings defines how to record meetings and todos.
     */
    private void add0135Meetings()
    {
        this.archiveBuilder.addEntityDef(getMeetingEntity());

        this.archiveBuilder.addRelationshipDef(getMeetingsRelationship());
    }


    private EntityDef getMeetingEntity()
    {
       EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.MEETING,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TITLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.END_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OBJECTIVE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MINUTES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getMeetingsRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.MEETINGS,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "meetings";
        final String                     end1AttributeDescription     = "Related meetings.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.MEETING.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "meetingOwner";
        final String                     end2AttributeDescription     = "Person, project, community or team that called the meeting.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0137 - Actions for People
     */
    private void add0137Actions()
    {
        this.archiveBuilder.addEnumDef(getToDoStatusEnum());

        this.archiveBuilder.addEntityDef(getToDoEntity());

        this.archiveBuilder.addRelationshipDef(getToDoSourceRelationship());
        this.archiveBuilder.addRelationshipDef(getActionSponsorRelationship());
        this.archiveBuilder.addRelationshipDef(getActionAssignment());
    }


    private EnumDef getToDoStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ToDoStatus.getOpenTypeGUID(),
                                                        ToDoStatus.getOpenTypeName(),
                                                        ToDoStatus.getOpenTypeDescription(),
                                                        ToDoStatus.getOpenTypeDescriptionGUID(),
                                                        ToDoStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ToDoStatus toDoStatus : ToDoStatus.values())
        {
            elementDef = archiveHelper.getEnumElementDef(toDoStatus.getOrdinal(),
                                                         toDoStatus.getName(),
                                                         toDoStatus.getDescription(),
                                                         toDoStatus.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (toDoStatus.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getToDoEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.TO_DO,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CREATION_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRIORITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DUE_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_TIME));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.TO_DO_STATUS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getToDoSourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "actionSource";
        final String                     end1AttributeDescription     = "Source of the to do request.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "actions";
        final String                     end2AttributeDescription     = "Requests to perform actions related to this element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TO_DO.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getActionSponsorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "toDoSponsor";
        final String                     end1AttributeDescription     = "Element such as person, team, rule, incident, project, that is driving the need for the action.";
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
        final String                     end2AttributeName            = "trackedActions";
        final String                     end2AttributeDescription     = "Actions that need to be completed.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TO_DO.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getActionAssignment()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "assignedResources";
        final String                     end1AttributeDescription     = "One or more people assigned to complete the action (to do).";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "toDoList";
        final String                     end2AttributeDescription     = "List of toDos assigned to this person's role.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TO_DO.typeName),
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
     * 0140 Communities describe communities of people who have similar interests.
     */
    private void add0140Communities()
    {
        this.archiveBuilder.addEnumDef(getCommunityMembershipTypeEnum());

        this.archiveBuilder.addEntityDef(getCommunityEntity());
        this.archiveBuilder.addEntityDef(getCommunityMemberEntity());

        this.archiveBuilder.addRelationshipDef(getCommunityMembershipRelationship());
    }

    private EnumDef getCommunityMembershipTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(CommunityMembershipType.getOpenTypeGUID(),
                                                        CommunityMembershipType.getOpenTypeName(),
                                                        CommunityMembershipType.getOpenTypeDescription(),
                                                        CommunityMembershipType.getOpenTypeDescriptionGUID());
        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (GovernanceDomain enumValue : GovernanceDomain.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef getCommunityEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.COMMUNITY,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MISSION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getCommunityMemberEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.COMMUNITY_MEMBER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName));
    }

    private RelationshipDef getCommunityMembershipRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.COMMUNITY_MEMBERSHIP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "memberOfCommunity";
        final String                     end1AttributeDescription     = "Communities that the person is a member of.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COMMUNITY.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "communityMembers";
        final String                     end2AttributeDescription     = "Members of the community.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COMMUNITY_MEMBER.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.MEMBERSHIP_TYPE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0150 Feedback provides all of the collaborative feedback and attachments that can be made by the user
     * community of the Assets.
     */
    private void add0150Feedback()
    {
        this.archiveBuilder.addEnumDef(getStarRatingEnum());
        this.archiveBuilder.addEnumDef(getCommentTypeEnum());

        this.archiveBuilder.addEntityDef(getRatingEntity());
        this.archiveBuilder.addEntityDef(getCommentEntity());
        this.archiveBuilder.addEntityDef(getLikeEntity());
        this.archiveBuilder.addEntityDef(getInformalTagEntity());

        this.archiveBuilder.addRelationshipDef(getAttachedRatingRelationship());
        this.archiveBuilder.addRelationshipDef(getAttachedCommentRelationship());
        this.archiveBuilder.addRelationshipDef(getAttachedLikeRelationship());
        this.archiveBuilder.addRelationshipDef(getAcceptedAnswerRelationship());
        this.archiveBuilder.addRelationshipDef(getAttachedTagRelationship());
    }


    private EnumDef getStarRatingEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(StarRating.getOpenTypeGUID(),
                                                        StarRating.getOpenTypeName(),
                                                        StarRating.getOpenTypeDescription(),
                                                        StarRating.getOpenTypeDescriptionGUID(),
                                                        StarRating.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (StarRating enumValue : StarRating.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EnumDef getCommentTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(CommentType.getOpenTypeGUID(),
                                                        CommentType.getOpenTypeName(),
                                                        CommentType.getOpenTypeDescription(),
                                                        CommentType.getOpenTypeDescriptionGUID(),
                                                        CommentType.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (CommentType commentType : CommentType.values())
        {
            elementDef = archiveHelper.getEnumElementDef(commentType.getOrdinal(),
                                                         commentType.getName(),
                                                         commentType.getDescription(),
                                                         commentType.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (commentType.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef getRatingEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RATING, null);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.STARS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REVIEW));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getCommentEntity()
    {
         EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.COMMENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TEXT));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getLikeEntity()
    {
        EntityDef entityDef =  archiveHelper.getDefaultEntityDef(OpenMetadataType.LIKE, null);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getInformalTagEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.INFORMAL_TAG, null);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TAG_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TAG_DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getAttachedRatingRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTACHED_RATING_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "ratingAnchor";
        final String                     end1AttributeDescription     = "Element that is rated.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "starRatings";
        final String                     end2AttributeDescription     = "Accumulated ratings.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.RATING.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getAttachedCommentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "commentAnchor";
        final String                     end1AttributeDescription     = "Element that this comment relates.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "comments";
        final String                     end2AttributeDescription     = "Accumulated comments.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COMMENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getAttachedLikeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "likeAnchor";
        final String                     end1AttributeDescription     = "Element that is liked.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "likes";
        final String                     end2AttributeDescription     = "Accumulated likes.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.LIKE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getAcceptedAnswerRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "answeredQuestions";
        final String                     end1AttributeDescription     = "Questions that now has an accepted answer.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COMMENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "acceptedAnswers";
        final String                     end2AttributeDescription     = "Accumulated answers.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COMMENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getAttachedTagRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTACHED_TAG_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "taggedElement";
        final String                     end1AttributeDescription     = "Element that is tagged.";
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
        final String                     end2AttributeName            = "tags";
        final String                     end2AttributeDescription     = "Accumulated tags.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.INFORMAL_TAG.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0155 Crowd Sourcing describes the people involved in crowd sourcing new metadata content.
     */
    private void add0155CrowdSourcing()
    {
        this.archiveBuilder.addEnumDef(getCrowdSourcingRoleEnum());
        this.archiveBuilder.addEntityDef(getCrowdSourcingContributorEntity());
        this.archiveBuilder.addRelationshipDef(getCrowdSourcingContributionRelationship());
    }


    private EnumDef getCrowdSourcingRoleEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(CrowdSourcingRole.getOpenTypeGUID(),
                                                        CrowdSourcingRole.getOpenTypeName(),
                                                        CrowdSourcingRole.getOpenTypeDescription(),
                                                        CrowdSourcingRole.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (KeyPattern enumValue : KeyPattern.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef getCrowdSourcingContributorEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.CROWD_SOURCING_CONTRIBUTOR,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName));
    }

    private RelationshipDef getCrowdSourcingContributionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CROWD_SOURCING_CONTRIBUTION,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "contributions";
        final String                     end1AttributeDescription     = "Items that this person has contributed.";
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
        final String                     end2AttributeName            = "contributors";
        final String                     end2AttributeDescription     = "Person contributing content.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CROWD_SOURCING_CONTRIBUTOR.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.ROLE_TYPE));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0160 Notes describes note logs and notes within them.  Notes are kept by the owners/stewards working on the
     * Assets.
     */
    private void add0160Notes()
    {
        this.archiveBuilder.addEntityDef(getNoteEntryEntity());
        this.archiveBuilder.addEntityDef(getNoteLogEntity());
        this.archiveBuilder.addEntityDef(getNoteLogAuthorEntity());

        this.archiveBuilder.addRelationshipDef(getAttachedNoteLogRelationship());
        this.archiveBuilder.addRelationshipDef(getAttachedNoteLogEntryRelationship());
        this.archiveBuilder.addRelationshipDef(getNoteLogAuthorshipRelationship());
    }

    private EntityDef getNoteEntryEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.NOTE_ENTRY,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TITLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TEXT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getNoteLogAuthorEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.NOTE_LOG_AUTHOR,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName));
    }

    private EntityDef getNoteLogEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.NOTE_LOG,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = OpenMetadataProperty.IS_PUBLIC.name;
        final String attribute3Description     = "Is the note log visible to more than the note log authors?";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NAME.name,
                                                           OpenMetadataProperty.NAME.description,
                                                           OpenMetadataProperty.NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute3Name,
                                                            attribute3Description,
                                                            attribute3DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getAttachedNoteLogRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describes";
        final String                     end1AttributeDescription     = "Subject of the note log.";
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
        final String                     end2AttributeName            = "noteLogs";
        final String                     end2AttributeDescription     = "Log of related notes.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NOTE_LOG.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_PUBLIC));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getNoteLogAuthorshipRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NOTE_LOG_AUTHORSHIP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "authors";
        final String                     end1AttributeDescription     = "Person contributing to the note log.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NOTE_LOG_AUTHOR.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "authoredNoteLogs";
        final String                     end2AttributeDescription     = "Note log containing contributions.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NOTE_LOG.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getAttachedNoteLogEntryRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "logs";
        final String                     end1AttributeDescription     = "Logs that this entry relates.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NOTE_LOG.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "entries";
        final String                     end2AttributeDescription     = "Accumulated notes.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.NOTE_ENTRY.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * ========================================
     * AREA 2: connectors and assets
     */

    /**
     * Area 2 covers the different types of Assets and the Connection information used by the Open Connector Framework
     * (OCF).
     */
    private void addArea2Types()
    {
        this.add0201ConnectorsAndConnections();
        this.add0205ConnectionLinkage();
        this.add0210DataStores();
        this.add0212DeployedAPIs();
        this.add0215SoftwareComponents();
        this.add0220FilesAndFolders();
        this.add0221DocumentStores();
        this.add0222GraphStores();
        this.add0223EventsAndLogs();
        this.add0224Databases();
        this.add0225MetadataRepositories();
        this.add0227Keystores();
        this.add0230CodeTables();
        this.add0235InformationView();
        this.add0239Reports();
        this.add0290Ports();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0201 Connectors and Connections defines the details of the Connection that describes the connector type
     * and endpoint for a specific connector instance.
     */
    private void add0201ConnectorsAndConnections()
    {
        this.archiveBuilder.addEntityDef(getConnectionEntity());
        this.archiveBuilder.addEntityDef(getConnectorTypeEntity());

        this.archiveBuilder.addRelationshipDef(getConnectionEndpointRelationship());
        this.archiveBuilder.addRelationshipDef(getConnectionConnectorTypeRelationship());
    }

    private EntityDef getConnectionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONNECTION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SECURED_PROPERTIES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIGURATION_PROPERTIES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CLEAR_PASSWORD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCRYPTED_PASSWORD));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getConnectorTypeEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONNECTOR_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getConnectionEndpointRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONNECTION_ENDPOINT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connectionEndpoint";
        final String                     end1AttributeDescription     = "Server endpoint that provides access to the asset.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENDPOINT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "connections";
        final String                     end2AttributeDescription     = "Connections to this endpoint.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getConnectionConnectorTypeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connections";
        final String                     end1AttributeDescription     = "Connections using this connector type.";
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
        final String                     end2AttributeName            = "connectorType";
        final String                     end2AttributeDescription     = "Type of connector to use for the asset.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTOR_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0205 Connection Links defines the relationship between the connection and an Asset, plus the nesting
     * of connections for information virtualization support.
     */
    private void add0205ConnectionLinkage()
    {
        this.archiveBuilder.addEntityDef(getVirtualConnectionEntity());

        this.archiveBuilder.addRelationshipDef(getEmbeddedConnectionRelationship());
        this.archiveBuilder.addRelationshipDef(getConnectionToAssetRelationship());
    }

    private EntityDef getVirtualConnectionEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VIRTUAL_CONNECTION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTION.typeName));
    }

    private RelationshipDef getEmbeddedConnectionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supportingVirtualConnections";
        final String                     end1AttributeDescription     = "Virtual connections using this connection.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VIRTUAL_CONNECTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "embeddedConnections";
        final String                     end2AttributeDescription     = "Connections embedded in this virtual connection.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ARGUMENTS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef getConnectionToAssetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONNECTION_TO_ASSET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connections";
        final String                     end1AttributeDescription     = "Connections to this asset.";
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
        final String                     end2AttributeName            = "asset";
        final String                     end2AttributeDescription     = "Asset that can be accessed with this connection.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSET_SUMMARY));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0210 DataStores describe physical data store assets.
     */
    private void add0210DataStores()
    {
        this.archiveBuilder.addEntityDef(getDataStoreEntity());

        this.archiveBuilder.addRelationshipDef(getDataContentForDataSetRelationship());

        this.archiveBuilder.addClassificationDef(getDataStoreEncodingClassification());
    }

    private EntityDef getDataStoreEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_STORE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_ASSET.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STORE_CREATE_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STORE_UPDATE_TIME));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getDataContentForDataSetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dataContent";
        final String                     end1AttributeDescription     = "Assets supporting a data set.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "supportedDataSets";
        final String                     end2AttributeDescription     = "Data sets that use this asset.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private ClassificationDef getDataStoreEncodingClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.DATA_STORE_ENCODING_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCODING));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCODING_LANGUAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCODING_DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCODING_PROPERTIES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0212 Deployed APIs defines an API that has been deployed to IT Infrastructure
     */
    private void add0212DeployedAPIs()
    {
        this.archiveBuilder.addEntityDef(getDeployedAPIEntity());

        this.archiveBuilder.addRelationshipDef(getAPIEndpointRelationship());

        this.archiveBuilder.addClassificationDef(getRequestResponseInterfaceClassification());
        this.archiveBuilder.addClassificationDef(getListenerInterfaceClassification());
        this.archiveBuilder.addClassificationDef(getPublisherInterfaceClassification());
    }

    private EntityDef getDeployedAPIEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DEPLOYED_API,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));
    }

    private RelationshipDef getAPIEndpointRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.API_ENDPOINT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supportedAPIs";
        final String                     end1AttributeDescription     = "APIs that can be called from this endpoint.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_API.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "accessEndpoints";
        final String                     end2AttributeDescription     = "Endpoints used to call this API.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENDPOINT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private ClassificationDef getRequestResponseInterfaceClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.REQUEST_RESPONSE_INTERFACE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_API.typeName),
                                                  false);
    }

    private ClassificationDef getListenerInterfaceClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.LISTENER_INTERFACE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_API.typeName),
                                                  false);
    }

    private ClassificationDef getPublisherInterfaceClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.PUBLISHER_INTERFACE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_API.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0215 Software Components defines a generic Asset for a software component.
     */
    private void add0215SoftwareComponents()
    {
        this.archiveBuilder.addEntityDef(getDeployedSoftwareComponentEntity());
    }


    private EntityDef getDeployedSoftwareComponentEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IMPLEMENTATION_LANGUAGE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0220 Files and Folders provides the definitions for describing filesystems and their content
     */
    private void add0220FilesAndFolders()
    {
        this.archiveBuilder.addEntityDef(getFileFolderEntity());
        this.archiveBuilder.addEntityDef(getDataFolderEntity());
        this.archiveBuilder.addEntityDef(getDataFileEntity());
        this.archiveBuilder.addEntityDef(getCSVFileEntity());
        this.archiveBuilder.addEntityDef(getAvroFileEntity());
        this.archiveBuilder.addEntityDef(getJSONFileEntity());

        this.archiveBuilder.addRelationshipDef(getFolderHierarchyRelationship());
        this.archiveBuilder.addRelationshipDef(getNestedFileRelationship());
        this.archiveBuilder.addRelationshipDef(getLinkedFileRelationship());

        this.archiveBuilder.addClassificationDef(getFileSystemClassification());
    }

    private EntityDef getFileFolderEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.FILE_FOLDER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName));
    }

    private EntityDef getDataFolderEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_FOLDER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.FILE_FOLDER.typeName));
    }

    private EntityDef getDataFileEntity()
    {
        EntityDef entityDef =  archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_FILE,
                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FILE_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getCSVFileEntity()
    {
        EntityDef entityDef =  archiveHelper.getDefaultEntityDef(OpenMetadataType.CSV_FILE,
                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELIMITER_CHARACTER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUOTE_CHARACTER));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getAvroFileEntity()
    {
        return  archiveHelper.getDefaultEntityDef(OpenMetadataType.AVRO_FILE,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }

    private EntityDef getJSONFileEntity()
    {
        return  archiveHelper.getDefaultEntityDef(OpenMetadataType.JSON_FILE,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }

    private RelationshipDef getFolderHierarchyRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentFolder";
        final String                     end1AttributeDescription     = "Parent folder.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.FILE_FOLDER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "nestedFolder";
        final String                     end2AttributeDescription     = "Folders embedded in this folder.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.FILE_FOLDER.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getNestedFileRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NESTED_FILE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "homeFolder";
        final String                     end1AttributeDescription     = "Identifies the containing folder of this datafile.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.FILE_FOLDER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "nestedFiles";
        final String                     end2AttributeDescription     = "Files stored in this folder.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getLinkedFileRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.LINKED_FILE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "linkedFolders";
        final String                     end1AttributeDescription     = "Folders that this file is linked to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.FILE_FOLDER.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "linkedFiles";
        final String                     end2AttributeDescription     = "Files linked to the folder.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private ClassificationDef getFileSystemClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.FILE_SYSTEM_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMAT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENCRYPTION));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0221 Document Stores define both simple document stores and content management systems
     */
    private void add0221DocumentStores()
    {
        this.archiveBuilder.addEntityDef(getMediaFileEntity());
        this.archiveBuilder.addEntityDef(getMediaCollectionEntity());
        this.archiveBuilder.addEntityDef(getDocumentEntity());
        this.archiveBuilder.addEntityDef(getDocumentStoreEntity());

        this.archiveBuilder.addRelationshipDef(getGroupedMediaRelationship());
        this.archiveBuilder.addRelationshipDef(getLinkedMediaRelationship());

        this.archiveBuilder.addClassificationDef(getContentManagerClassification());
    }

    private EntityDef getMediaFileEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.MEDIA_FILE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EMBEDDED_METADATA));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getMediaCollectionEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.MEDIA_COLLECTION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName));
    }

    private EntityDef getDocumentEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DOCUMENT,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName));
    }

    private EntityDef getDocumentStoreEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DOCUMENT_STORE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName));
    }

    private RelationshipDef getGroupedMediaRelationship()
    {
        final String guid            = "7d881574-461d-475c-ab44-077451528cb8";
        final String name            = "GroupedMedia";
        final String description     = "Links a media file into a data set.";
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
        final String                     end1EntityType               = "MediaCollection";
        final String                     end1AttributeName            = "dataSetMembership";
        final String                     end1AttributeDescription     = "Identifies the data sets this media file belongs to.";
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
        final String                     end2AttributeName            = "dataSetMembers";
        final String                     end2AttributeDescription     = "Media files that make up this media collection.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getLinkedMediaRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.LINKED_MEDIA_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "linkedMediaFiles";
        final String                     end1AttributeDescription     = "Link to related media files.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "linkedMediaFiles";
        final String                     end2AttributeDescription     = "Link to related media files.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName),
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

    private ClassificationDef getContentManagerClassification()
    {
        final String guid            = "fa4df7b5-cb6d-475c-889e-8f3b7ca564d3";
        final String name            = "ContentManager";
        final String description     = "Identifies a server as a manager of controlled documents and related media.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0222GraphStores()
    {
        this.archiveBuilder.addEntityDef(getGraphStoreEntity());
    }

    private EntityDef getGraphStoreEntity()
    {
        EntityDef entityDef =  archiveHelper.getDefaultEntityDef(OpenMetadataType.GRAPH_STORE,
                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of graph store.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0223 Events and Logs describes events, log files and event management.
     */
    private void add0223EventsAndLogs()
    {
        this.archiveBuilder.addEntityDef(getSubscriberListEntity());
        this.archiveBuilder.addEntityDef(getTopicEntity());
        this.archiveBuilder.addEntityDef(getLogFileEntity());

        this.archiveBuilder.addRelationshipDef(getTopicSubscribersRelationship());

        this.archiveBuilder.addClassificationDef(getNotificationManagerClassification());
    }

    private EntityDef getSubscriberListEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SUBSCRIBER_LIST,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName));
    }

    private EntityDef getTopicEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.TOPIC,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TOPIC_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef getLogFileEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.LOG_FILE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of log file.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getTopicSubscribersRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TOPIC_SUBSCRIBERS_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SubscriberList";
        final String                     end1AttributeName            = "subscribers";
        final String                     end1AttributeDescription     = "The endpoints subscribed to this topic.";
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
        final String                     end2AttributeName            = "topics";
        final String                     end2AttributeDescription     = "The topics used by this subscriber list.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TOPIC.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private ClassificationDef getNotificationManagerClassification()
    {
        final String guid            = "3e7502a7-396a-4737-a106-378c9c94c105";
        final String name            = "NotificationManager";
        final String description     = "Identifies a server capability that is distributing events from a topic to its subscriber list.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0224 Databases describe database servers
     */
    private void add0224Databases()
    {
        this.archiveBuilder.addEntityDef(getDeployedDatabaseSchemaEntity());
        this.archiveBuilder.addEntityDef(getDatabaseEntity());

        this.archiveBuilder.addClassificationDef(getDatabaseServerClassification());
    }

    private EntityDef getDeployedDatabaseSchemaEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName));
    }


    private EntityDef getDatabaseEntity()
    {
        final String guid            = "0921c83f-b2db-4086-a52c-0d10e52ca078";
        final String name            = "Database";
        final String description     = "A data store containing relational data.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of database.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "version";
        final String attribute2Description     = "Version of the database.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INSTANCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IMPORTED_FROM));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private ClassificationDef getDatabaseServerClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.description,
                                                                                 OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of database server.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "version";
        final String attribute2Description     = "Version of the database server software.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0225MetadataRepositories()
    {
        this.archiveBuilder.addEntityDef(getEnterpriseAccessLayerEntity());
        this.archiveBuilder.addEntityDef(getCohortMemberEntity());
        this.archiveBuilder.addEntityDef(getMetadataRepositoryCohortEntity());
        this.archiveBuilder.addEntityDef(getMetadataCollectionEntity());
        this.archiveBuilder.addEntityDef(getMetadataRepositoryEntity());
        this.archiveBuilder.addEntityDef(getCohortRegistryStoreEntity());

        this.archiveBuilder.addRelationshipDef(getMetadataCohortPeerRelationship());
        this.archiveBuilder.addRelationshipDef(getCohortMemberMetadataCollectionRelationship());

        this.archiveBuilder.addClassificationDef(getMetadataServerClassification());
        this.archiveBuilder.addClassificationDef(getRepositoryProxyClassification());
    }


    private EntityDef getEnterpriseAccessLayerEntity()
    {
        final String guid            = "39444bf9-638e-4124-a5f9-1b8f3e1b008b";
        final String name            = "EnterpriseAccessLayer";
        final String description     = "Repository services for the Open Metadata Access Services (OMAS) supporting federated queries and aggregated events from the connected cohorts.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "topicRoot";
        final String attribute1Description     = "Root of topic names used by the Open Metadata access Services (OMASs).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "metadataCollectionId";
        final String attribute2Description     = "Unique identifier for the metadata collection accessed through this enterprise access layer.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getCohortMemberEntity()
    {
        final String guid            = "42063797-a78a-4720-9353-52026c75f667";
        final String name            = "CohortMember";
        final String description     = "A capability enabling a server to access an open metadata repository cohort.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getMetadataRepositoryCohortEntity()
    {
        final String guid            = "43e7dca2-c7b4-4cdf-a1ea-c9d4f7093893";
        final String name            = "MetadataRepositoryCohort";
        final String description     = "A group of collaborating open metadata repositories.";
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

        final String attribute2Name            = "topic";
        final String attribute2Description     = "Name of the topic used to exchange registration, type definitions and metadata instances between the members of the open metadata repository cohort.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getMetadataCollectionEntity()
    {
        final String guid            = "ea3b15af-ed0e-44f7-91e4-bdb299dd4976";
        final String name            = "MetadataCollection";
        final String description     = "A data set containing metadata.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "metadataCollectionId";
        final String attribute1Description     = "Unique identifier for the metadata collection managed in the local repository.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }




    private EntityDef getMetadataRepositoryEntity()
    {
        final String guid            = "c40397bd-eab0-4b2e-bffb-e7fa0f93a5a9";
        final String name            = "MetadataRepository";
        final String description     = "A data store containing metadata.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of metadata repository.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getCohortRegistryStoreEntity()
    {
        final String guid            = "2bfdcd0d-68bb-42c3-ae75-e9fb6c3dff70";
        final String name            = "CohortRegistryStore";
        final String description     = "A data store containing cohort membership registration details.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_STORE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getMetadataCohortPeerRelationship()
    {
        final String guid            = "954cdba1-3d69-4db1-bf0e-d59fd2c25a27";
        final String name            = "MetadataCohortPeer";
        final String description     = "A metadata repository's registration with an open metadata cohort.";
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
        final String                     end1EntityType               = "MetadataRepositoryCohort";
        final String                     end1AttributeName            = "registeredWithCohorts";
        final String                     end1AttributeDescription     = "Identifies which cohorts this cohort member is registered with.";
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
        final String                     end2EntityType               = "CohortMember";
        final String                     end2AttributeName            = "cohortMembership";
        final String                     end2AttributeDescription     = "Members of this cohort.";
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

        final String attribute1Name            = "registrationDate";
        final String attribute1Description     = "Date first registered with the cohort.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getCohortMemberMetadataCollectionRelationship()
    {
        final String guid            = "8b9dd3ea-057b-4709-9b42-f16098523907";
        final String name            = "CohortMemberMetadataCollection";
        final String description     = "The local metadata collection associated with a cohort peer.";
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
        final String                     end1EntityType               = "CohortMember";
        final String                     end1AttributeName            = "cohortMember";
        final String                     end1AttributeDescription     = "Cohort registry representing this metadata collection on the metadata highway.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "MetadataCollection";
        final String                     end2AttributeName            = "localMetadataCollection";
        final String                     end2AttributeDescription     = "Metadata to exchange with the cohorts.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private ClassificationDef getMetadataServerClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.METADATA_SERVER_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.METADATA_SERVER_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.METADATA_SERVER_CLASSIFICATION.description,
                                                                                 OpenMetadataType.METADATA_SERVER_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.METADATA_SERVER_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "format";
        final String attribute1Description     = "format of supported metadata.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "type";
        final String attribute2Description     = "Type of metadata server.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getRepositoryProxyClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.REPOSITORY_PROXY_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.REPOSITORY_PROXY_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.REPOSITORY_PROXY_CLASSIFICATION.description,
                                                                                 OpenMetadataType.REPOSITORY_PROXY_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.REPOSITORY_PROXY_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of repository proxy.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0227Keystores()
    {
        this.archiveBuilder.addEntityDef(getKeystoreFileEntity());
        this.archiveBuilder.addEntityDef(geSecretsCollectionEntity());
    }


    private EntityDef getKeystoreFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.KEYSTORE_FILE.typeGUID,
                                                 OpenMetadataType.KEYSTORE_FILE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName),
                                                 OpenMetadataType.KEYSTORE_FILE.description,
                                                 OpenMetadataType.KEYSTORE_FILE.descriptionGUID,
                                                 OpenMetadataType.KEYSTORE_FILE.wikiURL);
    }


    private EntityDef geSecretsCollectionEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SECRETS_COLLECTION.typeGUID,
                                                 OpenMetadataType.SECRETS_COLLECTION.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                 OpenMetadataType.SECRETS_COLLECTION.description,
                                                 OpenMetadataType.SECRETS_COLLECTION.descriptionGUID,
                                                 OpenMetadataType.SECRETS_COLLECTION.wikiURL);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0230CodeTables()
    {
        this.archiveBuilder.addEntityDef(getReferenceCodeTableEntity());
        this.archiveBuilder.addEntityDef(getReferenceCodeMappingTableEntity());

    }


    private EntityDef getReferenceCodeTableEntity()
    {
        final String guid            = "201f48c5-4e4b-41dc-9c5f-0bc9742190cf";
        final String name            = "ReferenceCodeTable";
        final String description     = "A data set containing code values and their translations.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getReferenceCodeMappingTableEntity()
    {
        final String guid            = "9c6ec0c6-0b26-4414-bffe-089144323213";
        final String name            = "ReferenceCodeMappingTable";
        final String description     = "A data set containing mappings between code values from different data sets.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0235InformationView()
    {
        this.archiveBuilder.addEntityDef(getInformationViewEntity());
    }


    private EntityDef getInformationViewEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.INFORMATION_VIEW.typeGUID,
                                                 OpenMetadataType.INFORMATION_VIEW.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                 OpenMetadataType.INFORMATION_VIEW.description,
                                                 OpenMetadataType.INFORMATION_VIEW.descriptionGUID,
                                                 OpenMetadataType.INFORMATION_VIEW.wikiURL);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0239Reports()
    {
        this.archiveBuilder.addEntityDef(getFormEntity());
        this.archiveBuilder.addEntityDef(getDeployedReportEntity());
    }


    private EntityDef getFormEntity()
    {
        final String guid            = "8078e3d1-0c63-4ace-aafa-68498b39ccd6";
        final String name            = "Form";
        final String description     = "A collection of data items used to request activity.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getDeployedReportEntity()
    {
        final String guid            = "e9077f4f-955b-4d7b-b1f7-12ee769ff0c3";
        final String name            = "DeployedReport";
        final String description     = "A collection if data items that describe a situation.  This is an instance of a report.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "id";
        final String attribute1Description     = "Id of report.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "author";
        final String attribute2Description     = "Author of the report.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "url";
        final String attribute3Description     = "url of the report.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "createdTime";
        final String attribute4Description     = "Report create time.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "lastModifiedTime";
        final String attribute5Description     = "Report last modified time.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "lastModifier";
        final String attribute6Description     = "Report last modifier.";
        final String attribute6DescriptionGUID = null;

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
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private void add0290Ports()
    {
        this.archiveBuilder.addEnumDef(getPortTypeEnum());

        this.archiveBuilder.addEntityDef(getPortEntity());
        this.archiveBuilder.addEntityDef(getPortAliasEntity());
        this.archiveBuilder.addEntityDef(getPortImplementationEntity());


        this.archiveBuilder.addRelationshipDef(getProcessPortRelationship());
        this.archiveBuilder.addRelationshipDef(getPortDelegationRelationship());
    }

    /**
     * The PortType enum describes the type of a port
     *
     * @return PortType EnumDef
     */
    private EnumDef getPortTypeEnum()
    {
        final String guid            = "b57Fbce7-42ac-71D1-D6a6-9f62Cb7C6dc3";
        final String name            = "PortType";
        final String description     = "Descriptor for a port that indicates its type.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "INPUT_PORT";
        final String element1Description     = "Data is passed into the process.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                    element1Value,
                                                    element1Description,
                                                    element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "OUTPUT_PORT";
        final String element2Description     = "Data is produced by the process.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                    element2Value,
                                                    element2Description,
                                                    element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "INOUT_PORT";
        final String element3Description     = "A request-response interface is provided by the process.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                    element3Value,
                                                    element3Description,
                                                    element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 3;
        final String element4Value           = "OUTIN_PORT";
        final String element4Description     = "A request-response call is made by the process.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                    element4Value,
                                                    element4Description,
                                                    element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 99;
        final String element5Value           = "OTHER";
        final String element5Description     = "None of the above.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                    element5Value,
                                                    element5Description,
                                                    element5DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    /**
     * The Port entity describes the input or output of a process
     *
     * @return Port EntityDef
     */
    private EntityDef getPortEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "e3d9FD9F-d5eD-2aed-CC98-0bc21aB6f71C";
        final String name            = "Port";
        final String description     = "An interface where data flows in and/or out of the process.";
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

        final String attribute2Name            = "portType";
        final String attribute2Description     = "Type of port";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);

        property = archiveHelper.getEnumTypeDefAttribute("PortType",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    /**
     * The PortAlias describes the input/output of a higher-level process
     *
     * @return PortAlias EntityDef
     */
    private EntityDef getPortAliasEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "DFa5aEb1-bAb4-c25B-bDBD-B95Ce6fAB7F5";
        final String name            = "PortAlias";
        final String description     = "Entity that describes the port for a composition process.";
        final String descriptionGUID = null;
        final String superTypeName   = "Port";


        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }

    /**
     * The PortImplementation describes the input/output of a lowest-level process
     *
     * @return PortImplementation EntityDef
     */
    private EntityDef getPortImplementationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "ADbbdF06-a6A3-4D5F-7fA3-DB4Cb0eDeC0E";
        final String name            = "PortImplementation";
        final String description     = "Entity that describes a port with a concrete implementation.";
        final String descriptionGUID = null;
        final String superTypeName   = "Port";


        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    /**
     * The PortDelegation relationship describes the relationship between a more granular and a more abstract port
     * @return PortDelegation RelationshipDef
     */
    private RelationshipDef getPortDelegationRelationship()
    {
        /*
         * Build the relationship
         */
        final String guid            = "98bB8BA1-dc6A-eb9D-32Cf-F837bEbCbb8E";
        final String name            = "PortDelegation";
        final String description     = "A relationship between a more granular and a more abstract port";
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
        final String                     end1EntityType               = "Port";
        final String                     end1AttributeName            = "delegatingFrom";
        final String                     end1AttributeDescription     = "Higher level Port";
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
        final String                     end2EntityType               = "Port";
        final String                     end2AttributeName            = "delegatingTo";
        final String                     end2AttributeDescription     = "Lower level port";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }

    /**
     * The ProcessPort relationship describes the link between a port and the process used by the port.
     * @return ProcessPort RelationshipDef
     */
    private RelationshipDef getProcessPortRelationship()
    {
        /*
         * Build the relationship
         */
        final String guid            = "fB4E00CF-37e4-88CE-4a94-233BAdB84DA2";
        final String name            = "ProcessPort";
        final String description     = "A link between a process and one of its ports.";
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
        final String                     end1AttributeName            = "owningProcess";
        final String                     end1AttributeDescription     = "Process linked to the port";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Port";
        final String                     end2AttributeName            = "ports";
        final String                     end2AttributeDescription     = "Port to the process";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }

    /*
     * ========================================
     * AREA 3: glossary
     */

    /**
     * Area 3 covers semantic definitions.
     */
    private void addArea3Types()
    {
        this.add0310Glossary();
        this.add0320CategoryHierarchy();
        this.add0330Terms();
        this.add0340Dictionary();
        this.add0350RelatedTerms();
        this.add0360Contexts();
        this.add0370SemanticAssignment();
        this.add0380SpineObjects();
        this.add0385ControlledGlossaryDevelopment();
        this.add0390GlossaryProject();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0310 Glossary covers the top-level glossary object that organizes glossary terms.
     */
    private void add0310Glossary()
    {
        this.archiveBuilder.addEntityDef(getGlossaryEntity());
        this.archiveBuilder.addEntityDef(getExternalGlossaryLinkEntity());

        this.archiveBuilder.addRelationshipDef(getExternalSourcedGlossaryRelationship());

        this.archiveBuilder.addClassificationDef(getTaxonomyClassification());
        this.archiveBuilder.addClassificationDef(getCanonicalVocabularyClassification());
    }


    private EntityDef getGlossaryEntity()
    {
        final String guid            = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
        final String name            = "Glossary";
        final String description     = "A collection of related glossary terms.";
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


        final String attribute3Name            = "language";
        final String attribute3Description     = "Natural language used in the glossary.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.USAGE.name,
                                                           OpenMetadataProperty.USAGE.description,
                                                           OpenMetadataProperty.USAGE.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getExternalGlossaryLinkEntity()
    {
        final String guid            = "183d2935-a950-4d74-b246-eac3664b5a9d";
        final String name            = "ExternalGlossaryLink";
        final String description     = "The location of a glossary stored outside of the open metadata ecosystem.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_REFERENCE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getExternalSourcedGlossaryRelationship()
    {
        final String guid            = "7786a39c-436b-4538-acc7-d595b5856add";
        final String name            = "ExternallySourcedGlossary";
        final String description     = "Link between an open metadata glossary and a related glossary stored outside of the open metadata ecosystem.";
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
        final String                     end1EntityType               = "Glossary";
        final String                     end1AttributeName            = "localGlossary";
        final String                     end1AttributeDescription     = "Local glossary that relates to this external glossary.";
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
        final String                     end2EntityType               = "ExternalGlossaryLink";
        final String                     end2AttributeName            = "externalGlossaryLink";
        final String                     end2AttributeDescription     = "Link to a related external glossary.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private ClassificationDef getTaxonomyClassification()
    {
        final String guid            = "37116c51-e6c9-4c37-942e-35d48c8c69a0";
        final String name            = "Taxonomy";
        final String description     = "Identifies a glossary that includes a taxonomy.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Glossary";

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "organizingPrinciple";
        final String attribute1Description     = "Characteristics that influence the organization of the taxonomy.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getCanonicalVocabularyClassification()
    {
        final String guid            = "33ad3da2-0910-47be-83f1-daee018a4c05";
        final String name            = "CanonicalVocabulary";
        final String description     = "Identifies a glossary that contains unique terms.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Glossary";

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "scope";
        final String attribute1Description     = "Scope of influence for this canonical glossary.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0320 Category Hierarchy adds categories to the glossary that allow terms to be defined in taxonomies
     * or hierarchies of folders.
     */
    private void add0320CategoryHierarchy()
    {
        this.archiveBuilder.addEntityDef(getGlossaryCategoryEntity());

        this.archiveBuilder.addRelationshipDef(getCategoryAnchorRelationship());
        this.archiveBuilder.addRelationshipDef(getCategoryHierarchyLinkRelationship());
        this.archiveBuilder.addRelationshipDef(getLibraryCategoryReferenceRelationship());

        this.archiveBuilder.addClassificationDef(getSubjectAreaClassification());
    }


    private EntityDef getGlossaryCategoryEntity()
    {
        final String guid            = "e507485b-9b5a-44c9-8a28-6967f7ff3672";
        final String name            = "GlossaryCategory";
        final String description     = "A collection of related glossary terms.";
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


    private RelationshipDef getCategoryAnchorRelationship()
    {
        final String guid            = "c628938e-815e-47db-8d1c-59bb2e84e028";
        final String name            = "CategoryAnchor";
        final String description     = "Connects a glossary category with its owning glossary.";
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
        final String                     end1EntityType               = "Glossary";
        final String                     end1AttributeName            = "anchor";
        final String                     end1AttributeDescription     = "Owning glossary for this category.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "GlossaryCategory";
        final String                     end2AttributeName            = "categories";
        final String                     end2AttributeDescription     = "Categories owned by this glossary.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getCategoryHierarchyLinkRelationship()
    {
        final String guid            = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";
        final String name            = "CategoryHierarchyLink";
        final String description     = "Relationship between two glossary categories used to create nested categories.";
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
        final String                     end1EntityType               = "GlossaryCategory";
        final String                     end1AttributeName            = "superCategory";
        final String                     end1AttributeDescription     = "Identifies the parent category.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "GlossaryCategory";
        final String                     end2AttributeName            = "subcategories";
        final String                     end2AttributeDescription     = "Glossary categories nested inside this category.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getLibraryCategoryReferenceRelationship()
    {
        final String guid            = "3da21cc9-3cdc-4d87-89b5-c501740f00b2";
        final String name            = "LibraryCategoryReference";
        final String description     = "Links a glossary category to a corresponding category in an external glossary.";
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
        final String                     end1EntityType               = "GlossaryCategory";
        final String                     end1AttributeName            = "localCategories";
        final String                     end1AttributeDescription     = "Related local glossary categories.";
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
        final String                     end2EntityType               = "ExternalGlossaryLink";
        final String                     end2AttributeName            = "externalGlossaryCategories";
        final String                     end2AttributeDescription     = "Links to related external glossaries.";
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

        final String attribute1Name            = "identifier";
        final String attribute1Description     = "Identifier of the corresponding element from the external glossary.";
        final String attribute1DescriptionGUID = null;
        final String attribute4Name            = "lastVerified";
        final String attribute4Description     = "Date when this reference was last checked.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private ClassificationDef getSubjectAreaClassification()
    {
        final String guid            = "480e6993-35c5-433a-b50b-0f5c4063fb5d";
        final String name            = "SubjectArea";
        final String description     = "Identifies an element as part of a subject area definition.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NAME.name,
                                                           OpenMetadataProperty.NAME.description,
                                                           OpenMetadataProperty.NAME.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0330 Terms brings in the glossary term that captures a single semantic meaning.
     */
    private void add0330Terms()
    {
        this.archiveBuilder.addEnumDef(getTermRelationshipStatusEnum());

        this.archiveBuilder.addEntityDef(getGlossaryTermEntity());

        this.archiveBuilder.addRelationshipDef(getTermAnchorRelationship());
        this.archiveBuilder.addRelationshipDef(getTermCategorizationRelationship());
        this.archiveBuilder.addRelationshipDef(getLibraryTermReferenceRelationship());
    }

    private EnumDef getTermRelationshipStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(GlossaryTermRelationshipStatus.getOpenTypeGUID(),
                                                        GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                        GlossaryTermRelationshipStatus.getOpenTypeDescription(),
                                                        GlossaryTermRelationshipStatus.getOpenTypeDescriptionGUID(),
                                                        GlossaryTermRelationshipStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (GlossaryTermRelationshipStatus enumValues : GlossaryTermRelationshipStatus.values())
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


    private EntityDef getGlossaryTermEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.GLOSSARY_TERM.typeGUID,
                                                                OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                OpenMetadataType.GLOSSARY_TERM.description,
                                                                OpenMetadataType.GLOSSARY_TERM.descriptionGUID,
                                                                OpenMetadataType.GLOSSARY_TERM.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute4Name            = OpenMetadataProperty.EXAMPLES.name;
        final String attribute4Description     = OpenMetadataProperty.EXAMPLES.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.EXAMPLES.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.ABBREVIATION.name;
        final String attribute5Description     = OpenMetadataProperty.ABBREVIATION.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.ABBREVIATION.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SUMMARY.name,
                                                           OpenMetadataProperty.SUMMARY.description,
                                                           OpenMetadataProperty.SUMMARY.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.USAGE.name,
                                                           OpenMetadataProperty.USAGE.description,
                                                           OpenMetadataProperty.USAGE.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getTermAnchorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TERM_ANCHOR.typeGUID,
                                                                                OpenMetadataType.TERM_ANCHOR.typeName,
                                                                                null,
                                                                                OpenMetadataType.TERM_ANCHOR.description,
                                                                                OpenMetadataType.TERM_ANCHOR.descriptionGUID,
                                                                                OpenMetadataType.TERM_ANCHOR.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "anchor";
        final String                     end1AttributeDescription     = "Owning glossary.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TYPE_NAME),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "terms";
        final String                     end2AttributeDescription     = "Terms owned by this glossary.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getTermCategorizationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TERM_CATEGORIZATION.typeGUID,
                                                                                OpenMetadataType.TERM_CATEGORIZATION.typeName,
                                                                                null,
                                                                                OpenMetadataType.TERM_CATEGORIZATION.description,
                                                                                OpenMetadataType.TERM_CATEGORIZATION.descriptionGUID,
                                                                                OpenMetadataType.TERM_CATEGORIZATION.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "categories";
        final String                     end1AttributeDescription     = "Glossary categories that this term is linked to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "terms";
        final String                     end2AttributeDescription     = "Glossary terms linked to this category.";
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
        final String attribute2Description     = "Status of the relationship.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getLibraryTermReferenceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.LIBRARY_TERM_REFERENCE.typeGUID,
                                                                                OpenMetadataType.LIBRARY_TERM_REFERENCE.typeName,
                                                                                null,
                                                                                OpenMetadataType.LIBRARY_TERM_REFERENCE.description,
                                                                                OpenMetadataType.LIBRARY_TERM_REFERENCE.descriptionGUID,
                                                                                OpenMetadataType.LIBRARY_TERM_REFERENCE.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "localTerms";
        final String                     end1AttributeDescription     = "Related local glossary terms.";
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
        final String                     end2EntityType               = "ExternalGlossaryLink";
        final String                     end2AttributeName            = "externalGlossaryTerms";
        final String                     end2AttributeDescription     = "Links to related external glossaries.";
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

        final String attribute1Name            = "identifier";
        final String attribute1Description     = "Identifier of the corresponding element from the external glossary.";
        final String attribute1DescriptionGUID = null;
        final String attribute4Name            = "lastVerified";
        final String attribute4Description     = "Date when this reference was last checked.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0340 Dictionary provides classifications for a term that define what type of term it is and
     * how it intended to be used.
     */
    private void add0340Dictionary()
    {
        this.archiveBuilder.addEnumDef(getActivityTypeEnum());

        this.archiveBuilder.addClassificationDef(getActivityDescriptionClassification());
        this.archiveBuilder.addClassificationDef(getAbstractConceptClassification());
        this.archiveBuilder.addClassificationDef(getDataValueClassification());
    }


    private EnumDef getActivityTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(GlossaryTermActivityType.getOpenTypeGUID(),
                                                        GlossaryTermActivityType.getOpenTypeName(),
                                                        GlossaryTermActivityType.getOpenTypeDescription(),
                                                        GlossaryTermActivityType.getOpenTypeDescriptionGUID(),
                                                        GlossaryTermActivityType.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (GlossaryTermActivityType enumValues : GlossaryTermActivityType.values())
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


    private ClassificationDef getActivityDescriptionClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.description,
                                                                                 OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM_TYPE_NAME),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ACTIVITY_TYPE.name;
        final String attribute1Description     = "Classification of the activity.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermActivityType.getOpenTypeName(),
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getAbstractConceptClassification()
    {
        final String guid            = "9d725a07-4abf-4939-a268-419d200b69c2";
        final String name            = "AbstractConcept";
        final String description     = "Identifies that this glossary term describes an abstract concept.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    private ClassificationDef getDataValueClassification()
    {
        final String guid            = "ab253e31-3d8a-45a7-8592-24329a189b9e";
        final String name            = "DataValue";
        final String description     = "Identifies that this glossary term describes a data value.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0350 Related Terms provides a selection of semantic relationships
     */
    private void add0350RelatedTerms()
    {
        this.archiveBuilder.addRelationshipDef(getRelatedTermRelationship());
        this.archiveBuilder.addRelationshipDef(getSynonymRelationship());
        this.archiveBuilder.addRelationshipDef(getAntonymRelationship());
        this.archiveBuilder.addRelationshipDef(getPreferredTermRelationship());
        this.archiveBuilder.addRelationshipDef(getReplacementTermRelationship());
        this.archiveBuilder.addRelationshipDef(getTranslationRelationship());
        this.archiveBuilder.addRelationshipDef(getISARelationshipRelationship());
        this.archiveBuilder.addRelationshipDef(getValidValueRelationship());
    }


    private RelationshipDef getRelatedTermRelationship()
    {
        final String guid            = "b1161696-e563-4cf9-9fd9-c0c76e47d063";
        final String name            = "RelatedTerm";
        final String description     = "Link between similar glossary terms.";
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
        final String                     end1AttributeName            = "seeAlso";
        final String                     end1AttributeDescription     = "Related glossary terms.";
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
        final String                     end2AttributeName            = "seeAlso";
        final String                     end2AttributeDescription     = "Related glossary terms.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getSynonymRelationship()
    {
        final String guid            = "74f4094d-dba2-4ad9-874e-d422b69947e2";
        final String name            = "Synonym";
        final String description     = "Link between glossary terms that have the same meaning.";
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
        final String                     end1AttributeName            = "synonyms";
        final String                     end1AttributeDescription     = "Glossary terms with the same meaning.";
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
        final String                     end2AttributeName            = "synonyms";
        final String                     end2AttributeDescription     = "Glossary terms with the same meaning.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getAntonymRelationship()
    {
        final String guid            = "ea5e126a-a8fa-4a43-bcfa-309a98aa0185";
        final String name            = "Antonym";
        final String description     = "Link between glossary terms that have the opposite meaning.";
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
        final String                     end1AttributeName            = "antonyms";
        final String                     end1AttributeDescription     = "Glossary terms with the opposite meaning.";
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
        final String                     end2AttributeName            = "antonyms";
        final String                     end2AttributeDescription     = "Glossary terms with the opposite meaning.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getPreferredTermRelationship()
    {
        final String guid            = "8ac8f9de-9cdd-4103-8a33-4cb204b78c2a";
        final String name            = "PreferredTerm";
        final String description     = "Link to an alternative term that the organization prefer is used.";
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
        final String                     end1AttributeName            = "alternateTerms";
        final String                     end1AttributeDescription     = "Alternative glossary terms.";
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
        final String                     end2AttributeName            = "preferredTerms";
        final String                     end2AttributeDescription     = "Related glossary terms.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getReplacementTermRelationship()
    {
        final String guid            = "3bac5f35-328b-4bbd-bfc9-3b3c9ba5e0ed";
        final String name            = "ReplacementTerm";
        final String description     = "Link to a glossary term that is replacing an obsolete glossary term.";
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
        final String                     end1AttributeName            = "replacedTerms";
        final String                     end1AttributeDescription     = "Replaced glossary terms.";
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
        final String                     end2AttributeName            = "replacementTerms";
        final String                     end2AttributeDescription     = "Replacement glossary terms.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getTranslationRelationship()
    {
        final String guid            = "6ae42e95-efc5-4256-bfa8-801140a29d2a";
        final String name            = "Translation";
        final String description     = "Link between glossary terms that provide different natural language translation of the same concept.";
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
        final String                     end1AttributeName            = "translations";
        final String                     end1AttributeDescription     = "Translations of glossary term.";
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
        final String                     end2AttributeName            = "translations";
        final String                     end2AttributeDescription     = "Translations of glossary term.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getISARelationshipRelationship()
    {
        final String guid            = "50fab7c7-68bc-452f-b8eb-ec76829cac85";
        final String name            = "ISARelationship";
        final String description     = "Link between a more general glossary term and a more specific definition.";
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
        final String                     end1AttributeName            = "classifies";
        final String                     end1AttributeDescription     = "More specific glossary terms.";
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
        final String                     end2AttributeName            = "isA";
        final String                     end2AttributeDescription     = "More general glossary terms.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getValidValueRelationship()
    {
        final String guid            = "707a156b-e579-4482-89a5-de5889da1971";
        final String name            = "ValidValue";
        final String description     = "Link between glossary terms where one defines one of the data values for the another.";
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
        final String                     end1AttributeName            = "validValueFor";
        final String                     end1AttributeDescription     = "Glossary terms for data items that can be set to this value.";
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
        final String                     end2AttributeName            = "validValues";
        final String                     end2AttributeDescription     = "Glossary terms for data values that can be used with data items represented by this glossary term.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0360 Contexts allows Glossary Terms to linked to specific contexts (also defined with Glossary Terms).
     */
    private void add0360Contexts()
    {
        this.archiveBuilder.addRelationshipDef(getUsedInContextRelationship());

        this.archiveBuilder.addClassificationDef(getContextDefinitionClassification());
    }

    private RelationshipDef getUsedInContextRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.USED_IN_CONTEXT.typeGUID,
                                                                                OpenMetadataType.USED_IN_CONTEXT.typeName,
                                                                                null,
                                                                                OpenMetadataType.USED_IN_CONTEXT.description,
                                                                                OpenMetadataType.USED_IN_CONTEXT.descriptionGUID,
                                                                                OpenMetadataType.USED_IN_CONTEXT.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "contextRelevantTerms";
        final String                     end1AttributeDescription     = "Glossary terms used in this specific context.";
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
        final String                     end2AttributeName            = "usedInContexts";
        final String                     end2AttributeDescription     = "Glossary terms describing the contexts where this term is used.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "An expression that explains the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private ClassificationDef getContextDefinitionClassification()
    {
        final String guid            = "54f9f41a-3871-4650-825d-59a41de01330";
        final String name            = "ContextDefinition";
        final String description     = "Identifies a glossary term that describes a context where processing or decisions occur.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "scope";
        final String attribute2Description     = "Scope of influence of the context.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0370 Semantic Assignment defines a relationship between a Glossary Term and an Asset
     */
    private void add0370SemanticAssignment()
    {
        this.archiveBuilder.addEnumDef(getTermAssignmentStatusEnum());

        this.archiveBuilder.addRelationshipDef(getSemanticAssignmentRelationship());
    }


    private EnumDef getTermAssignmentStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(GlossaryTermAssignmentStatus.getOpenTypeGUID(),
                                                        GlossaryTermAssignmentStatus.getOpenTypeName(),
                                                        GlossaryTermAssignmentStatus.getOpenTypeDescription(),
                                                        GlossaryTermAssignmentStatus.getOpenTypeDescriptionGUID(),
                                                        GlossaryTermAssignmentStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (GlossaryTermAssignmentStatus enumValues : GlossaryTermAssignmentStatus.values())
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


    private RelationshipDef getSemanticAssignmentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SEMANTIC_ASSIGNMENT.typeGUID,
                                                                                OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                                                                null,
                                                                                OpenMetadataType.SEMANTIC_ASSIGNMENT.description,
                                                                                OpenMetadataType.SEMANTIC_ASSIGNMENT.descriptionGUID,
                                                                                OpenMetadataType.SEMANTIC_ASSIGNMENT.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "assignedElements";
        final String                     end1AttributeDescription     = "Elements identified as managing data that has the same meaning as this glossary term.";
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
        final String                     end2AttributeName            = "meaning";
        final String                     end2AttributeDescription     = "Semantic definition for this element.";
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

        final String attribute2Name            = "expression";
        final String attribute2Description     = "Expression describing the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name;
        final String attribute3Description     = OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermAssignmentStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
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


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0380 Spine Objects enables relationships to be established between objects and their attributes.
     */
    private void add0380SpineObjects()
    {
        this.archiveBuilder.addRelationshipDef(getTermHASARelationshipRelationship());
        this.archiveBuilder.addRelationshipDef(getTermISATYPEOFRelationshipRelationship());
        this.archiveBuilder.addRelationshipDef(getTermTYPEDBYRelationshipRelationship());

        this.archiveBuilder.addClassificationDef(getSpineObjectClassification());
        this.archiveBuilder.addClassificationDef(getSpineAttributeClassification());
        this.archiveBuilder.addClassificationDef(getObjectIdentifierClassification());
    }


    private RelationshipDef getTermHASARelationshipRelationship()
    {
        final String guid            = "d67f16d1-5348-419e-ba38-b0bb6fe4ad6c";
        final String name            = "TermHASARelationship";
        final String description     = "Defines the relationship between a spine object and a spine attribute.";
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
        final String                     end1AttributeName            = "objects";
        final String                     end1AttributeDescription     = "Objects where this attribute may occur.";
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
        final String                     end2AttributeName            = "attributes";
        final String                     end2AttributeDescription     = "Typical attributes for this object.";
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

        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getTermISATYPEOFRelationshipRelationship()
    {
        final String guid            = "d5d588c3-46c9-420c-adff-6031802a7e51";
        final String name            = "TermISATypeOFRelationship";
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
        final String                     end1AttributeName            = "supertypes";
        final String                     end1AttributeDescription     = "Supertypes for this object.";
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
        final String                     end2AttributeName            = "subtypes";
        final String                     end2AttributeDescription     = "Subtypes for this object.";
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

        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private RelationshipDef getTermTYPEDBYRelationshipRelationship()
    {
        final String guid            = "669e8aa4-c671-4ee7-8d03-f37d09b9d006";
        final String name            = "TermTYPEDBYRelationship";
        final String description     = "Defines the relationship between a spine attribute and its type.";
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
        final String                     end1AttributeName            = "attributesTypedBy";
        final String                     end1AttributeDescription     = "Attributes of this type.";
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
        final String                     end2AttributeName            = "types";
        final String                     end2AttributeDescription     = "Types for this attribute.";
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

        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of or confidence in the relationship.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
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


    private ClassificationDef getSpineObjectClassification()
    {
        final String guid            = "a41ee152-de1e-4533-8535-2f8b37897cac";
        final String name            = "SpineObject";
        final String description     = "Identifies a glossary term that describes a type of spine object.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    private ClassificationDef getSpineAttributeClassification()
    {
        final String guid            = "ccb749ba-34ec-4f71-8755-4d8b383c34c3";
        final String name            = "SpineAttribute";
        final String description     = "Identifies a glossary term that describes an attribute of a spine object.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    private ClassificationDef getObjectIdentifierClassification()
    {
        final String guid            = "3d1e4389-27de-44fa-8df4-d57bfaf809ea";
        final String name            = "ObjectIdentifier";
        final String description     = "Identifies a glossary term that describes an attribute that can be used to identify an instance.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0385 ControlledGlossaryDevelopment covers glossary term objects that are created and
     * maintained through a workflow process.
     */
    private void add0385ControlledGlossaryDevelopment()
    {
        this.archiveBuilder.addEntityDef(getControlledGlossaryTermEntity());
    }


    private EntityDef getControlledGlossaryTermEntity()
    {
        final String guid            = "c04e29b2-2d66-48fc-a20d-e59895de6040";
        final String name            = "ControlledGlossaryTerm";
        final String description     = "Defines a glossary term that is developed through a controlled workflow.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                description,
                                                                descriptionGUID);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);
        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0390 Glossary Project provides a classification for a project to say it is updating glossary terms.
     */
    private void add0390GlossaryProject()
    {
        this.archiveBuilder.addClassificationDef(getGlossaryProjectClassification());
    }


    private ClassificationDef getGlossaryProjectClassification()
    {
        final String guid            = "43be51a9-2d19-4044-b399-3ba36af10929";
        final String name            = "GlossaryProject";
        final String description     = "Identifies a project that is defining new glossary terms and categories or maintaining an existing glossary.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Project";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * ========================================
     * AREA 4: governance
     */

    /**
     * Area 4 models cover the governance entities and relationships.
     */
    private void addArea4Types()
    {
        this.add0401GovernanceDefinitions();
        this.add0405GovernanceDrivers();
        this.add0415GovernanceResponses();
        this.add0417GovernanceProject();
        this.add0420GovernanceControls();
        this.add0421GovernanceConfidentialityLevels();
        this.add0422GovernanceActionClassifications();
        this.add0423SecurityTags();
        this.add0424GovernanceZones();
        this.add0430TechnicalControls();
        this.add0438NamingStandards();
        this.add0440OrganizationalControls();
        this.add0442ProjectCharter();
        this.add0445GovernanceRoles();
        this.add0450GovernanceRollout();
        this.add0455ExceptionManagement();
        this.add0460GovernanceControls();
        this.add0481Licenses();
        this.add0482Certifications();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0401 The definitions that control the governance of data assets are authored in the metadata repository.
     * They are referenceable and they make use of external references for more information.
     */
    private void add0401GovernanceDefinitions()
    {
        this.archiveBuilder.addEnumDef(getGovernanceDomainEnum());

        this.archiveBuilder.addEntityDef(getGovernanceDefinitionEntity());
        this.archiveBuilder.addEntityDef(getGovernanceOfficerEntity());
    }


    private EnumDef getGovernanceDomainEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(GovernanceDomain.getOpenTypeGUID(),
                                                        GovernanceDomain.getOpenTypeName(),
                                                        GovernanceDomain.getOpenTypeDescription(),
                                                        GovernanceDomain.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (GovernanceDomain enumValue : GovernanceDomain.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getGovernanceDefinitionEntity()
    {
        final String guid            = "578a3500-9ad3-45fe-8ada-e4e9572c37c8";
        final String name            = "GovernanceDefinition";
        final String description     = "Defines an aspect of the governance program.";
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

        final String attribute1Name            = "title";
        final String attribute1Description     = "Title describing the governance definition.";
        final String attribute1DescriptionGUID = null;
        final String attribute4Name            = "scope";
        final String attribute4Description     = "Scope of impact for this governance definition.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "domain";
        final String attribute5Description     = "Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "importance";
        final String attribute6Description     = "Relative importance of this governance definition compared to its peers.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "implications";
        final String attribute7Description     = "Impact on the organization, people and services when adopting the recommendation in this governance definition.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "outcomes";
        final String attribute8Description     = "Expected outcomes.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "results";
        final String attribute9Description     = "Actual results.";
        final String attribute9DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TITLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SUMMARY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute7Name,
                                                                attribute7Description,
                                                                attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute8Name,
                                                                attribute8Description,
                                                                attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute9Name,
                                                                attribute9Description,
                                                                attribute9DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);
        entityDef.setInitialStatus(InstanceStatus.DRAFT);

        return entityDef;
    }


    private EntityDef getGovernanceOfficerEntity()
    {
        final String guid            = "578a3510-9ad3-45fe-8ada-e4e9572c37c8";
        final String name            = "GovernanceOfficer";
        final String description     = "Person responsible for a governance domain.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "domain";
        final String attribute1Description     = "Governance domain for this governance officer.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0405 Governance Drivers defines the factors that drive the governance program.
     */
    private void add0405GovernanceDrivers()
    {
        this.archiveBuilder.addEntityDef(getGovernanceDriverEntity());
        this.archiveBuilder.addEntityDef(getGovernanceStrategyEntity());
        this.archiveBuilder.addEntityDef(getRegulationEntity());
    }


    private EntityDef getGovernanceDriverEntity()
    {
        final String guid            = "c403c109-7b6b-48cd-8eee-df445b258b33";
        final String name            = "GovernanceDriver";
        final String description     = "Defines a reason for having the governance program.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDefinition";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getGovernanceStrategyEntity()
    {
        final String guid            = "3c34f121-07a6-4e95-a07d-9b0ef17b7bbf";
        final String name            = "GovernanceStrategy";
        final String description     = "Defines how the governance program and the supporting capabilities are supporting the business strategy.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDriver";

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

        final String attribute1Name            = "businessImperatives";
        final String attribute1Description     = "Goals or required outcomes from the business strategy that is supported by the data strategy.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getRegulationEntity()
    {
        final String guid            = "e3c4293d-8846-4500-b0c0-197d73aba8b0";
        final String name            = "Regulation";
        final String description     = "Identifies a regulation related to data that must be supported.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDriver";

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

        final String attribute1Name            = "jurisdiction";
        final String attribute1Description     = "Issuing authority for the regulation.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0415 Governance Responses lay out the approaches, principles and obligations that follow from the
     * governance drivers.
     */
    private void add0415GovernanceResponses()
    {
        this.archiveBuilder.addEntityDef(getGovernancePolicyEntity());
        this.archiveBuilder.addEntityDef(getGovernancePrincipleEntity());
        this.archiveBuilder.addEntityDef(getGovernanceObligationEntity());
        this.archiveBuilder.addEntityDef(getGovernanceApproachEntity());

        this.archiveBuilder.addRelationshipDef(getGovernancePolicyLinkRelationship());
        this.archiveBuilder.addRelationshipDef(getGovernanceResponseRelationship());
    }


    private EntityDef getGovernancePolicyEntity()
    {
        final String guid            = "a7defa41-9cfa-4be5-9059-359022bb016d";
        final String name            = "GovernancePolicy";
        final String description     = "Defines a goal or outcome expected from the organization.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDefinition";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getGovernancePrincipleEntity()
    {
        final String guid            = "3b7d1325-ec2c-44cb-8db0-ce207beb78cf";
        final String name            = "GovernancePrinciple";
        final String description     = "Defines a principle related to how data is managed or used that the organization should ensure remains true.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernancePolicy";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getGovernanceObligationEntity()
    {
        final String guid            = "0cec20d3-aa29-41b7-96ea-1c544ed32537";
        final String name            = "GovernanceObligation";
        final String description     = "Defines a capability, rule or action that is required by a regulation or external party.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernancePolicy";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getGovernanceApproachEntity()
    {
        final String guid            = "2d03ec9d-bd6b-4be9-8e17-95a7ecdbaa67";
        final String name            = "GovernanceApproach";
        final String description     = "Defines a preferred approach to managing or using data.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernancePolicy";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getGovernancePolicyLinkRelationship()
    {
        final String guid            = "0c42c999-4cac-4da4-afab-0e381f3a818e";
        final String name            = "GovernancePolicyLink";
        final String description     = "Links related governance policies together.";
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
        final String                     end1EntityType               = "GovernancePolicy";
        final String                     end1AttributeName            = "linkingPolicies";
        final String                     end1AttributeDescription     = "Policies that are dependent on this policy.";
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
        final String                     end2EntityType               = "GovernancePolicy";
        final String                     end2AttributeName            = "linkedPolicies";
        final String                     end2AttributeDescription     = "Policies that further define aspects of this policy.";
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getGovernanceResponseRelationship()
    {
        final String guid            = "8845990e-7fd9-4b79-a19d-6c4730dadd6b";
        final String name            = "GovernanceResponse";
        final String description     = "Links a governance policy to a governance driver that it is supporting.";
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
        final String                     end1EntityType               = "GovernanceDriver";
        final String                     end1AttributeName            = "drivers";
        final String                     end1AttributeDescription     = "Drivers that justify this policy.";
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
        final String                     end2EntityType               = "GovernancePolicy";
        final String                     end2AttributeName            = "policies";
        final String                     end2AttributeDescription     = "Governance policies that support this governance driver.";
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

        final String attribute1Name            = "rationale";
        final String attribute1Description     = "Describes the reasoning for defining the policy in support of the driver.";
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
     * 0417 Governance Project provides a classification for a project
     */
    private void add0417GovernanceProject()
    {
        this.archiveBuilder.addClassificationDef(getGovernanceProjectClassification());
    }


    private ClassificationDef getGovernanceProjectClassification()
    {
        final String guid            = "37142317-4125-4046-9514-71dc5031563f";
        final String name            = "GovernanceProject";
        final String description     = "Identifies that a project is rolling out capability to support the governance program.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Project";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0420 Governance Controls define the implementation of the governance policies
     */
    private void add0420GovernanceControls()
    {
        this.archiveBuilder.addEntityDef(getGovernanceControlEntity());
        this.archiveBuilder.addEntityDef(getTechnicalControlEntity());
        this.archiveBuilder.addEntityDef(getOrganizationalControlEntity());

        this.archiveBuilder.addRelationshipDef(getGovernanceImplementationRelationship());
        this.archiveBuilder.addRelationshipDef(getGovernanceControlLinkRelationship());
    }


    private EntityDef getGovernanceControlEntity()
    {
        final String guid            = "c794985e-a10b-4b6c-9dc2-6b2e0a2901d3";
        final String name            = "GovernanceControl";
        final String description     = "An implementation of a governance capability.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDefinition";

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

        final String attribute1Name            = "implementationDescription";
        final String attribute1Description     = "Description of how this governance control should be implemented.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getTechnicalControlEntity()
    {
        final String guid            = "d8f6eb5b-36f0-49bd-9b25-bf16f370d1ec";
        final String name            = "TechnicalControl";
        final String description     = "A governance control that is implemented using technology.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getOrganizationalControlEntity()
    {
        final String guid            = "befa1458-79b8-446a-b813-536700e60fa8";
        final String name            = "OrganizationalControl";
        final String description     = "A governance control that is implemented using organization structure, training, roles manual procedures and reviews.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getGovernanceImplementationRelationship()
    {
        final String guid            = "787eaf46-7cf2-4096-8d6e-671a0819d57e";
        final String name            = "GovernanceImplementation";
        final String description     = "A link between a governance control and the governance driver it is implementing.";
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
        final String                     end1EntityType               = "GovernancePolicy";
        final String                     end1AttributeName            = "policies";
        final String                     end1AttributeDescription     = "The policies that are supported by this control.";
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
        final String                     end2EntityType               = "GovernanceControl";
        final String                     end2AttributeName            = "implementations";
        final String                     end2AttributeDescription     = "The governance controls that implement this policy.";
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

        final String attribute1Name            = "rationale";
        final String attribute1Description     = "The reasons for implementing the policy using this control.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getGovernanceControlLinkRelationship()
    {
        final String guid            = "806933fb-7925-439b-9876-922a960d2ba1";
        final String name            = "GovernanceControlLink";
        final String description     = "A link between two related governance controls.";
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
        final String                     end1EntityType               = "GovernanceControl";
        final String                     end1AttributeName            = "linkingControls";
        final String                     end1AttributeDescription     = "Governance controls that ate dependent on this control.";
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
        final String                     end2EntityType               = "GovernanceControl";
        final String                     end2AttributeName            = "linkedControls";
        final String                     end2AttributeDescription     = "Governance controls that support the implementation of this control.";
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

    /**
     * 0421 Governance Confidentiality Level defines the organization's levels of confidentiality.
     */
    private void add0421GovernanceConfidentialityLevels()
    {
        this.archiveBuilder.addEntityDef(getGovernanceConfidentialityLevelEntity());
    }


    private EntityDef getGovernanceConfidentialityLevelEntity()
    {
        final String guid            = "49dd320b-4850-4838-9b78-f1285f0e6d2f";
        final String name            = "GovernanceConfidentialityLevel";
        final String description     = "A definition of a confidentiality level.";
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

        final String attribute1Name            = "level";
        final String attribute1Description     = "Numeric value for the confidentiality - the higher the number, the more confidential";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
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

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0422 Governance Action Classifications provide the key classification that drive information governance.
     */
    private void add0422GovernanceActionClassifications()
    {
        this.archiveBuilder.addEnumDef(getGovernanceClassificationStatusEnum());
        this.archiveBuilder.addEnumDef(getConfidentialityLevelEnum());
        this.archiveBuilder.addEnumDef(getConfidenceLevelEnum());
        this.archiveBuilder.addEnumDef(getRetentionBasisEnum());
        this.archiveBuilder.addEnumDef(getCriticalityLevelEnum());

        this.archiveBuilder.addClassificationDef(getConfidentialityClassification());
        this.archiveBuilder.addClassificationDef(getConfidenceClassification());
        this.archiveBuilder.addClassificationDef(getRetentionClassification());
        this.archiveBuilder.addClassificationDef(getCriticalityClassification());
    }


    private EnumDef getGovernanceClassificationStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(GovernanceClassificationStatus.getOpenTypeGUID(),
                                                        GovernanceClassificationStatus.getOpenTypeName(),
                                                        GovernanceClassificationStatus.getOpenTypeDescription(),
                                                        GovernanceClassificationStatus.getOpenTypeDescriptionGUID(),
                                                        GovernanceClassificationStatus.getOpenTypeDescriptionWiki());

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

    private EnumDef getConfidentialityLevelEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ConfidentialityLevel.getOpenTypeGUID(),
                                                        ConfidentialityLevel.getOpenTypeName(),
                                                        ConfidentialityLevel.getOpenTypeDescription(),
                                                        ConfidentialityLevel.getOpenTypeDescriptionGUID(),
                                                        ConfidentialityLevel.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ConfidentialityLevel enumValues : ConfidentialityLevel.values())
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

    private EnumDef getConfidenceLevelEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ConfidenceLevel.getOpenTypeGUID(),
                                                        ConfidenceLevel.getOpenTypeName(),
                                                        ConfidenceLevel.getOpenTypeDescription(),
                                                        ConfidenceLevel.getOpenTypeDescriptionGUID(),
                                                        ConfidenceLevel.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ConfidenceLevel enumValues : ConfidenceLevel.values())
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

    private EnumDef getRetentionBasisEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(RetentionBasis.getOpenTypeGUID(),
                                                        RetentionBasis.getOpenTypeName(),
                                                        RetentionBasis.getOpenTypeDescription(),
                                                        RetentionBasis.getOpenTypeDescriptionGUID(),
                                                        RetentionBasis.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (RetentionBasis enumValues : RetentionBasis.values())
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

    private EnumDef getCriticalityLevelEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(CriticalityLevel.getOpenTypeGUID(),
                                                        CriticalityLevel.getOpenTypeName(),
                                                        CriticalityLevel.getOpenTypeDescription(),
                                                        CriticalityLevel.getOpenTypeDescriptionGUID(),
                                                        CriticalityLevel.getOpenTypeDescriptionWiki());

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

    private ClassificationDef getConfidentialityClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.description,
                                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute6Name            = "level";
        final String attribute6Description     = "Level of confidentiality.";
        final String attribute6DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute6Name,
                                                        attribute6Description,
                                                        attribute6DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getConfidenceClassification()
    {
        final String guid            = "25d8f8d5-2998-4983-b9ef-265f58732965";
        final String name            = "Confidence";
        final String description     = "Defines the level of confidence that should be placed in the accuracy of related data items.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute6Name            = "level";
        final String attribute6Description     = "Level of confidence in the quality of this data.";
        final String attribute6DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("ConfidenceLevel",
                                                         attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getRetentionClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.RETENTION_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.RETENTION_CLASSIFICATION.description,
                                                                                 OpenMetadataType.RETENTION_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.RETENTION_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute6Name            = "basis";
        final String attribute6Description     = "Basis on which the retention period is defined.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "associatedGUID";
        final String attribute7Description     = "Related entity used to determine the retention period.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "archiveAfter";
        final String attribute8Description     = "Date when archiving can take place.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "deleteAfter";
        final String attribute9Description     = "Date when delete can take place.";
        final String attribute9DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("RetentionBasis",
                                                         attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute8Name,
                                                         attribute8Description,
                                                         attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute9Name,
                                                         attribute9Description,
                                                         attribute9DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getCriticalityClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.description,
                                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute6Name            = "level";
        final String attribute6Description     = "How critical is this data to the organization.";
        final String attribute6DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("CriticalityLevel",
                                                         attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0423 Security Tags define the labels and properties used by rules in a security engine.
     */
    private void add0423SecurityTags()
    {
        this.archiveBuilder.addClassificationDef(getSecurityTagsClassification());
    }

    private ClassificationDef getSecurityTagsClassification()
    {
        final String guid            = "a0b07a86-9fd3-40ca-bb9b-fe83c6981deb";
        final String name            = "SecurityTags";
        final String description     = "Defines labels and properties used by a security engine.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "securityLabels";
        final String attribute1Description     = "Labels that apply to the referenceable.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "securityProperties";
        final String attribute2Description     = "Properties that apply to the referenceable.";
        final String attribute2DescriptionGUID = null;



        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringObjectTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0424 Governance Zones define the zones used to group assets according to their use.
     */
    private void add0424GovernanceZones()
    {
        this.archiveBuilder.addClassificationDef(getAssetZoneMembershipClassification());

        this.archiveBuilder.addEntityDef(getGovernanceZoneEntity());

        this.archiveBuilder.addRelationshipDef(getZoneGovernanceRelationship());
        this.archiveBuilder.addRelationshipDef(getZoneHierarchyRelationship());
    }


    private ClassificationDef getAssetZoneMembershipClassification()
    {
        final String guid            = "a1c17a86-9fd3-40ca-bb9b-fe83c6981deb";
        final String name            = "AssetZoneMembership";
        final String description     = "Defines the asset's membership of the governance zones.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "zoneMembership";
        final String attribute1Description     = "List of governance zones for the asset.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private EntityDef getGovernanceZoneEntity()
    {
        final String guid            = "290a192b-42a7-449a-935a-269ca62cfdac";
        final String name            = "GovernanceZone";
        final String description     = "Defines a collection of assets that are suitable for a particular usage or are governed by a particular process.";
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

        final String attribute3Name            = "criteria";
        final String attribute3Description     = "Definition of the types of assets that belong in this zone.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getZoneGovernanceRelationship()
    {
        final String guid            = "4c4d1d9c-a9fc-4305-8b71-4e891c0f9ae0";
        final String name            = "ZoneGovernance";
        final String description     = "Links a governance zone to a governance definition that applies to all of the members of the zone.";
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
        final String                     end1EntityType               = "GovernanceZone";
        final String                     end1AttributeName            = "governedZones";
        final String                     end1AttributeDescription     = "The collections of assets governed by this definition.";
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
        final String                     end2EntityType               = "GovernanceDefinition";
        final String                     end2AttributeName            = "governedBy";
        final String                     end2AttributeDescription     = "Governance definitions for this zone.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }


    private RelationshipDef getZoneHierarchyRelationship()
    {
        final String guid            = "ee6cf469-cb4d-4c3b-a4c7-e2da1236d139";
        final String name            = "ZoneHierarchy";
        final String description     = "Creates a controlling hierarchy for governance zones.";
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
        final String                     end1EntityType               = "GovernanceZone";
        final String                     end1AttributeName            = "inheritsFromZone";
        final String                     end1AttributeDescription     = "The zone that provides additional governance requirements.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "GovernanceZone";
        final String                     end2AttributeName            = "controlsZone";
        final String                     end2AttributeDescription     = "The zones that are also governed in the same way.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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
     * 0430 Technical Controls describe specific types of technical controls
     */
    private void add0430TechnicalControls()
    {
        this.archiveBuilder.addEntityDef(getGovernanceRuleEntity());
        this.archiveBuilder.addEntityDef(getGovernanceProcessEntity());

        this.archiveBuilder.addRelationshipDef(getGovernanceRuleImplementationRelationship());
        this.archiveBuilder.addRelationshipDef(getGovernanceProcessImplementationRelationship());
    }


    private EntityDef getGovernanceRuleEntity()
    {
        final String guid            = "8f954380-12ce-4a2d-97c6-9ebe250fecf8";
        final String name            = "GovernanceRule";
        final String description     = "Technical control expressed as a logic expression.";
        final String descriptionGUID = null;

        final String superTypeName = "TechnicalControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getGovernanceProcessEntity()
    {
        final String guid            = "b68b5d9d-6b79-4f3a-887f-ec0f81c54aea";
        final String name            = "GovernanceProcess";
        final String description     = "Technical control expressed as a sequence of tasks.";
        final String descriptionGUID = null;

        final String superTypeName = "TechnicalControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getGovernanceRuleImplementationRelationship()
    {
        final String guid            = "e701a5c8-c1ba-4b75-8257-e0a6569eda48";
        final String name            = "GovernanceRuleImplementation";
        final String description     = "Identifies the implementation of a governance rule.";
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
        final String                     end1EntityType               = "GovernanceRule";
        final String                     end1AttributeName            = "implementsGovernanceRules";
        final String                     end1AttributeDescription     = "The rules that are implemented by this component.";
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
        final String                     end2AttributeName            = "implementations";
        final String                     end2AttributeDescription     = "The software components that implement this governance rule.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName),
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getGovernanceProcessImplementationRelationship()
    {
        final String guid            = "a5a7b08a-73fd-4026-a9dd-d0fe55bea8a4";
        final String name            = "GovernanceProcessImplementation";
        final String description     = "Identifies the implementation of a governance process.";
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
        final String                     end1EntityType               = "GovernanceProcess";
        final String                     end1AttributeName            = "implementsGovernanceProcesses";
        final String                     end1AttributeDescription     = "The processes that are implemented by this component.";
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
        final String                     end2AttributeName            = "implementations";
        final String                     end2AttributeDescription     = "The processes that implement this governance process.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName),
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0438 Naming Standards provides definitions for laying our naming standards for schemas and assets.
     */
    private void add0438NamingStandards()
    {
        this.archiveBuilder.addEntityDef(getNamingStandardRuleEntity());
        this.archiveBuilder.addEntityDef(getNamingStandardRuleSetEntity());

        this.archiveBuilder.addClassificationDef(getPrimeWordClassification());
        this.archiveBuilder.addClassificationDef(getClassWordClassification());
        this.archiveBuilder.addClassificationDef(getModifierClassification());
    }


    private EntityDef getNamingStandardRuleEntity()
    {
        final String guid            = "52505b06-98a5-481f-8a32-db9b02afabfc";
        final String name            = "NamingStandardRule";
        final String description     = "Describes a parsing rule used to create compliant names.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceRule";

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

        final String attribute1Name            = "namePattern";
        final String attribute1Description     = "Format of the naming standard rule.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);


        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getNamingStandardRuleSetEntity()
    {
        final String guid            = "ba70f506-1f81-4890-bb4f-1cb1d99c939e";
        final String name            = "NamingStandardRuleSet";
        final String description     = "Describes a collection of related naming standard rules.";
        final String descriptionGUID = null;

        final String superTypeName = "Collection";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private ClassificationDef getPrimeWordClassification()
    {
        final String guid            = "3ea1ea66-8923-4662-8628-0bacef3e9c5f";
        final String name            = "PrimeWord";
        final String description     = "Describes a primary noun, used in naming standards.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    private ClassificationDef getClassWordClassification()
    {
        final String guid            = "feac4bd9-37d9-4437-82f6-618ce3e2793e";
        final String name            = "ClassWord";
        final String description     = "Describes classifying or grouping noun, using in naming standards.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    private ClassificationDef getModifierClassification()
    {
        final String guid            = "dfc70bed-7e8b-4060-910c-59c7473f23a3";
        final String name            = "NamingConventionRule";
        final String description     = "Describes modifying noun or adverb, used in naming standards.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0440 Organizational Controls describe organizational roles and responsibilities.
     */
    private void add0440OrganizationalControls()
    {
        this.archiveBuilder.addEnumDef(getBusinessCapabilityTypeEnum());

        this.archiveBuilder.addEntityDef(getOrganizationEntity());
        this.archiveBuilder.addEntityDef(getBusinessCapabilityEntity());
        this.archiveBuilder.addEntityDef(getGovernanceResponsibilityEntity());
        this.archiveBuilder.addEntityDef(getGovernanceProcedureEntity());

        this.archiveBuilder.addClassificationDef(getAssetOriginClassification());

        this.archiveBuilder.addRelationshipDef(getOrganizationCapabilityRelationship());
        this.archiveBuilder.addRelationshipDef(getResponsibilityStaffContactRelationship());
        this.archiveBuilder.addRelationshipDef(getBusinessCapabilityControlsRelationship());
    }


    private EnumDef getBusinessCapabilityTypeEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(BusinessCapabilityType.getOpenTypeGUID(),
                                                        BusinessCapabilityType.getOpenTypeName(),
                                                        BusinessCapabilityType.getOpenTypeDescription(),
                                                        BusinessCapabilityType.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (BusinessCapabilityType enumValue : BusinessCapabilityType.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getOrganizationEntity()
    {
        final String guid            = "50a61105-35be-4ee3-8b99-bdd958ed0685";
        final String name            = "Organization";
        final String description     = "Describes a specific organization.";
        final String descriptionGUID = null;

        final String superTypeName = "Team";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getBusinessCapabilityEntity()
    {
        final String guid            = "7cc6bcb2-b573-4719-9412-cf6c3f4bbb15";
        final String name            = "BusinessCapability";
        final String description     = "Describes a function, capability or skill set.";
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

        final String attribute1Name            = "type";
        final String attribute1Description     = "Type of business capability.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("BusinessCapabilityType",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getGovernanceResponsibilityEntity()
    {
        final String guid            = "89a76b24-deb8-45bf-9304-a578a610326f";
        final String name            = "GovernanceResponsibility";
        final String description     = "Describes a responsibility of a person, team or organization that supports the implementation of a governance driver.";
        final String descriptionGUID = null;

        final String superTypeName = "OrganizationalControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getGovernanceProcedureEntity()
    {
        final String guid            = "69055d10-51dc-4c2b-b21f-d76fad3f8ef3";
        final String name            = "GovernanceProcedure";
        final String description     = "Describes set of tasks that a person, team or organization performs to support the implementation of a governance driver.";
        final String descriptionGUID = null;

        final String superTypeName = "OrganizationalControl";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private ClassificationDef getAssetOriginClassification()
    {
        final String guid            = "e530c566-03d2-470a-be69-6f52bfbd5fb7";
        final String name            = "AssetOrigin";
        final String description     = "Describes the origin of an asset.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef =  archiveHelper.getClassificationDef(guid,
                                                                                  name,
                                                                                  null,
                                                                                  description,
                                                                                  descriptionGUID,
                                                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                  false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "organization";
        final String attribute1Description     = "Unique identifier (GUID) of the organization where this asset originated from.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "businessCapability";
        final String attribute2Description     = "Unique identifier (GUID) of the business capability where this asset originated from.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "otherOriginValues";
        final String attribute3Description     = "Descriptive labels describing origin of the asset.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private RelationshipDef getOrganizationCapabilityRelationship()
    {
        final String guid            = "47f0ad39-db77-41b0-b406-36b1598e0ba7";
        final String name            = "OrganizationalCapability";
        final String description     = "Describes the relationship between a team and the business capabilities it supports.";
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
        final String                     end1EntityType               = "BusinessCapability";
        final String                     end1AttributeName            = "supportsBusinessCapabilities";
        final String                     end1AttributeDescription     = "The business capabilities that this team supports.";
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
        final String                     end2AttributeName            = "supportingTeams";
        final String                     end2AttributeDescription     = "The teams that support this business capability.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getResponsibilityStaffContactRelationship()
    {
        final String guid            = "49f2ecb5-6bf7-4324-9824-ac98d595c404";
        final String name            = "ResponsibilityStaffContact";
        final String description     = "Identifies a person, team or engine assigned to a governance responsibility.";
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
        final String                     end1EntityType               = "GovernanceResponsibility";
        final String                     end1AttributeName            = "contactFor";
        final String                     end1AttributeDescription     = "The governance responsibilities that this team or person is assigned to.";
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
        final String                     end2AttributeName            = "assignedStaff";
        final String                     end2AttributeDescription     = "The people, teams and/or engines that are supporting this governance responsibility.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ACTOR_PROFILE.typeName),
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

        final String attribute1Name            = "context";
        final String attribute1Description     = "The context in which this person, team or engine is to be contacted.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getBusinessCapabilityControlsRelationship()
    {
        final String guid            = "b5de932a-738c-4c69-b852-09fec2b9c678";
        final String name            = "BusinessCapabilityControls";
        final String description     = "Identifies a business capability that supports a governance control.";
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
        final String                     end1EntityType               = "GovernanceControl";
        final String                     end1AttributeName            = "implementsControls";
        final String                     end1AttributeDescription     = "The governance controls that this business capability supports.";
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
        final String                     end2EntityType               = "BusinessCapability";
        final String                     end2AttributeName            = "affectedBusinessCapabilities";
        final String                     end2AttributeDescription     = "The business capabilities that implement or support this governance responsibility.";
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

        final String attribute1Name            = "rationale";
        final String attribute1Description     = "Documents reasons for assigning the control to this business capability.";
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

    private void add0442ProjectCharter()
    {
        this.archiveBuilder.addEntityDef(getProjectCharterEntity());

        this.archiveBuilder.addRelationshipDef(getProjectCharterLinkRelationship());
    }


    private EntityDef getProjectCharterEntity()
    {
        final String guid            = "f96b5a32-42c1-4a74-8f77-70a81cec783d";
        final String name            = "ProjectCharter";
        final String description     = "Describes the goals, scope and authority of a project.";
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

        final String attribute1Name            = "mission";
        final String attribute1Description     = "The high-level goal of the project.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "projectType";
        final String attribute2Description     = "Short description of type of the project.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "purposes";
        final String attribute3Description     = "List of purposes for having the project.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute3Name,
                                                                attribute3Description,
                                                                attribute3DescriptionGUID);
        properties.add(property);


        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getProjectCharterLinkRelationship()
    {
        final String guid            = "f081808d-545a-41cb-a9aa-c4f074a16c78";
        final String name            = "ProjectCharterLink";
        final String description     = "Links a Project with its Charter.";
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
        final String                     end1AttributeName            = "projects";
        final String                     end1AttributeDescription     = "The projects guided by this charter.";
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
        final String                     end2EntityType               = "ProjectCharter";
        final String                     end2AttributeName            = "charter";
        final String                     end2AttributeDescription     = "The charter guiding this project.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0445GovernanceRoles()
    {
        // todo call is currently being made from 0010 base model
        /* this.archiveBuilder.addEnumDef(getAssetOwnerTypeEnum()); */

        this.archiveBuilder.addClassificationDef(getAssetOwnershipClassification());

        this.archiveBuilder.addEntityDef(getGovernanceRoleEntity());
        this.archiveBuilder.addEntityDef(getAssetOwnerEntity());
        this.archiveBuilder.addEntityDef(getSubjectAreaOwnerEntity());

        this.archiveBuilder.addRelationshipDef(getGovernanceRoleAssignmentRelationship());
        this.archiveBuilder.addRelationshipDef(getGovernanceResponsibilityAssignmentRelationship());
    }


    private EnumDef getAssetOwnerTypeEnum()
    {
        final String guid            = "9548390c-69f5-4dc6-950d-6feeee257b56";
        final String name            = "AssetOwnerType";
        final String description     = "Defines the type of identifier for an asset's owner.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        List<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef       elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "UserId";
        final String element1Description     = "The owner's userId is specified (default).";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "ProfileId";
        final String element2Description     = "The unique identifier (guid) of the profile of the owner.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);


        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another type of owner identifier, probably not supported by open metadata.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private ClassificationDef getAssetOwnershipClassification()
    {
        final String guid = "d531c566-03d2-470a-be69-6f52cabd5fb9";
        final String name            = "AssetOwnership";
        final String description     = "Describes the ownership of an asset.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef =  archiveHelper.getClassificationDef(guid,
                                                                                  name,
                                                                                  null,
                                                                                  description,
                                                                                  descriptionGUID,
                                                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                  false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "owner";
        final String attribute1Description     = "Identifier of the person or process that owns the asset.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "ownerType";
        final String attribute2Description     = "Type of identifier used for owner property.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("AssetOwnerType",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private EntityDef getGovernanceRoleEntity()
    {
        final String guid            = "de2d7f2e-1759-44e3-b8a6-8af53e8fb0ee";
        final String name            = "GovernanceRole";
        final String description     = "Describes a set of goals, tasks and skills that can be assigned a person and contribute to the governance of a resource.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getAssetOwnerEntity()
    {
        final String guid            = "ac406bf8-e53e-49f1-9088-2af28eeee285";
        final String name            = "AssetOwner";
        final String description     = "A role defining a responsibility to manage an asset.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceRole";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getSubjectAreaOwnerEntity()
    {
        final String guid            = "c6fe40af-cdd6-4ca7-98c4-353d2612921f";
        final String name            = "SubjectAreaOwner";
        final String description     = "A role defining a responsibility to manage the development and maintenance of a subject area.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceRole";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getGovernanceRoleAssignmentRelationship()
    {
        final String guid            = "cb10c107-b7af-475d-aab0-d78b8297b982";
        final String name            = "GovernanceRoleAssignment";
        final String description     = "Identifies a person assigned to perform a specific responsibility for a specific resource.";
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
        final String                     end1AttributeName            = "governanceAssignments";
        final String                     end1AttributeDescription     = "The resources assigned to this person.";
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
        final String                     end2AttributeName            = "governedByRoles";
        final String                     end2AttributeDescription     = "The roles assigned to this element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getGovernanceResponsibilityAssignmentRelationship()
    {
        final String guid            = "cb15c107-b7af-475d-aab0-d78b8297b982";
        final String name            = "GovernanceResponsibilityAssignment";
        final String description     = "Identifies a role that will perform a governance responsibility.";
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
        final String                     end1EntityType               = "GovernanceRole";
        final String                     end1AttributeName            = "performedByRoles";
        final String                     end1AttributeDescription     = "The roles assigned to this responsibility.";
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
        final String                     end2EntityType               = "GovernanceResponsibility";
        final String                     end2AttributeName            = "governanceResponsibilities";
        final String                     end2AttributeDescription     = "The responsibilities performed by this role.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

    private void add0450GovernanceRollout()
    {
        this.archiveBuilder.addEntityDef(getGovernanceMetricEntity());

        this.archiveBuilder.addRelationshipDef(getGovernanceDefinitionMetricRelationship());
        this.archiveBuilder.addRelationshipDef(getGovernanceResultsRelationship());

        this.archiveBuilder.addClassificationDef(getGovernanceMeasurementsDataSetClassification());
    }


    private EntityDef getGovernanceMetricEntity()
    {
        final String guid            = "9ada8e7b-823c-40f7-adf8-f164aabda77e";
        final String name            = "GovernanceMetric";
        final String description     = "A definition for how the effectiveness of the governance program is measured.";
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

        final String attribute3Name            = "measurement";
        final String attribute3Description     = "Format or description of the measurements captured for this metric.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "target";
        final String attribute4Description     = "Definition of the measurement values that the governance definitions are trying to achieve.";
        final String attribute4DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getGovernanceDefinitionMetricRelationship()
    {
        final String guid            = "e076fbb3-54f5-46b8-8f1e-a7cb7e792673";
        final String name            = "GovernanceDefinitionMetric";
        final String description     = "Link between a governance definition and a governance metric used to measure this definition.";
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
        final String                     end1EntityType               = "GovernanceMetric";
        final String                     end1AttributeName            = "metrics";
        final String                     end1AttributeDescription     = "The metrics that measure the landscape against this governance definition.";
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
        final String                     end2EntityType               = "GovernanceDefinition";
        final String                     end2AttributeName            = "measuredDefinitions";
        final String                     end2AttributeDescription     = "The governance definitions that are measured by this metric.";
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

        final String attribute1Name            = "rationale";
        final String attribute1Description     = "Documents reasons for using the metric to measure the governance definition.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getGovernanceResultsRelationship()
    {
        final String guid            = "89c3c695-9e8d-4660-9f44-ed971fd55f88";
        final String name            = "GovernanceResults";
        final String description     = "Link between a governance metric and a data set used to gather measurements from the landscape.";
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
        final String                     end1EntityType               = "GovernanceMetric";
        final String                     end1AttributeName            = "metrics";
        final String                     end1AttributeDescription     = "The governance metrics that are captured in this data set.";
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
        final String                     end2AttributeName            = "measurements";
        final String                     end2AttributeDescription     = "The data set that captures the measurements for this governance metric.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
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

        final String attribute1Name            = "query";
        final String attribute1Description     = "Defines how the data items from the data set are converted in measurements for the metric.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private ClassificationDef getGovernanceMeasurementsDataSetClassification()
    {
        final String guid            = "789f2e89-accd-4489-8eca-dc43b432c022";
        final String name            = "GovernanceMeasurementsResultsDataSet";
        final String description     = "A data file containing measurements for a governance metric.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0455ExceptionManagement()
    {
        this.archiveBuilder.addClassificationDef(getExceptionBacklogClassification());
        this.archiveBuilder.addClassificationDef(getAuditLogClassification());
        this.archiveBuilder.addClassificationDef(getMeteringLogClassification());
        this.archiveBuilder.addClassificationDef(getStewardshipServerClassification());
        this.archiveBuilder.addClassificationDef(getGovernanceDaemonClassification());
    }


    private ClassificationDef getExceptionBacklogClassification()
    {
        final String guid            = "b3eceea3-aa02-4d84-8f11-da4953e64b5f";
        final String name            = "ExceptionBacklog";
        final String description     = "A data set containing exceptions that need to be resolved";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = "process";
        final String attribute3Description     = "Unique identifier of the automated process that processes this exception backlog.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getAuditLogClassification()
    {
        final String guid            = "449be034-6cc8-4f1b-859f-a8b9ff8ee7a1";
        final String name            = "AuditLog";
        final String description     = "A data set of related audit log records.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this exception backlog.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getMeteringLogClassification()
    {
        final String guid            = "161b37c9-1d51-433b-94ce-5a760a198236";
        final String name            = "MeteringLog";
        final String description     = "A data set containing records that can be used to identify usage of resources.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_SET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this exception backlog.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getStewardshipServerClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.STEWARDSHIP_SERVER_CLASSIFICATION.typeGUID,
                                                  OpenMetadataType.STEWARDSHIP_SERVER_CLASSIFICATION.typeName,
                                                  null,
                                                  OpenMetadataType.STEWARDSHIP_SERVER_CLASSIFICATION.description,
                                                  OpenMetadataType.STEWARDSHIP_SERVER_CLASSIFICATION.descriptionGUID,
                                                  OpenMetadataType.STEWARDSHIP_SERVER_CLASSIFICATION.wikiURL,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                  false);
    }


    private ClassificationDef getGovernanceDaemonClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.GOVERNANCE_DAEMON_CLASSIFICATION.typeGUID,
                                                  OpenMetadataType.GOVERNANCE_DAEMON_CLASSIFICATION.typeName,
                                                  null,
                                                  OpenMetadataType.GOVERNANCE_DAEMON_CLASSIFICATION.description,
                                                  OpenMetadataType.GOVERNANCE_DAEMON_CLASSIFICATION.descriptionGUID,
                                                  OpenMetadataType.GOVERNANCE_DAEMON_CLASSIFICATION.wikiURL,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER.typeName),
                                                  false);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0460GovernanceControls()
    {
        this.archiveBuilder.addClassificationDef(getControlPointClassification());
        this.archiveBuilder.addClassificationDef(getVerificationPointClassification());
        this.archiveBuilder.addClassificationDef(getEnforcementPointClassification());
    }


    private ClassificationDef getControlPointClassification()
    {
        final String guid            = "acf8b73e-3545-435d-ba16-fbfae060dd28";
        final String name            = "ControlPoint";
        final String description     = "A task in a process where a person must make a decision on the right action to take.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }


    private ClassificationDef getVerificationPointClassification()
    {
        final String guid            = "12d78c95-3879-466d-883f-b71f6477a741";
        final String name            = "VerificationPoint";
        final String description     = "A governance rule that tests if a required condition is true or raises an exception if not.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }


    private ClassificationDef getEnforcementPointClassification()
    {
        final String guid            = "f4ce104e-7430-4c30-863d-60f6af6394d9";
        final String name            = "EnforcementPoint";
        final String description     = "A governance rule that ensures a required condition is true.";
        final String descriptionGUID = null;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0481Licenses()
    {
        this.archiveBuilder.addEntityDef(getLicenseTypeEntity());

        this.archiveBuilder.addRelationshipDef(getLicenseRelationship());
    }


    private EntityDef getLicenseTypeEntity()
    {
        final String guid            = "046a049d-5f80-4e5b-b0ae-f3cf6009b513";
        final String name            = "LicenseType";
        final String description     = "A type of license that sets out specific terms and conditions for the use of an asset.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDefinition";

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

        final String attribute1Name            = "details";
        final String attribute1Description     = "Description of the rights, terms and conditions associated with the licence.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getLicenseRelationship()
    {
        final String guid            = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
        final String name            = "License";
        final String description     = "Link between an asset and its license.";
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
        final String                     end1AttributeName            = "licensed";
        final String                     end1AttributeDescription     = "Items licensed by this type of license.";
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
        final String                     end2EntityType               = "LicenseType";
        final String                     end2AttributeName            = "licenses";
        final String                     end2AttributeDescription     = "The types of licenses that apply.";
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

        final String attribute1Name            = "licenseGUID";
        final String attribute1Description     = "Unique identifier of the actual license.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "start";
        final String attribute2Description     = "Start date for the license.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "end";
        final String attribute3Description     = "End date for the license.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "conditions";
        final String attribute4Description     = "Any special conditions or endorsements over the basic license type.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "licensedBy";
        final String attribute5Description     = "Person or organization that owns the intellectual property.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "custodian";
        final String attribute6Description     = "The person, engine or organization tht will ensure the license is honored.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "licensee";
        final String attribute7Description     = "The person or organization that holds the license.";
        final String attribute7DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0482Certifications()
    {
        this.archiveBuilder.addEntityDef(getCertificationTypeEntity());

        this.archiveBuilder.addRelationshipDef(getCertificationRelationship());
        this.archiveBuilder.addRelationshipDef(getRegulationCertificationTypeRelationship());
    }


    private EntityDef getCertificationTypeEntity()
    {
        final String guid            = "97f9ffc9-e2f7-4557-ac12-925257345eea";
        final String name            = "CertificationType";
        final String description     = "A specific type of certification required by a regulation.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDefinition";

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

        final String attribute1Name            = "details";
        final String attribute1Description     = "Description of the requirements associated with the certification.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getCertificationRelationship()
    {
        final String guid            = "390559eb-6a0c-4dd7-bc95-b9074caffa7f";
        final String name            = "Certification";
        final String description     = "An awarded certification of a specific type.";
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
        final String                     end1AttributeName            = "certifies";
        final String                     end1AttributeDescription     = "Items certified by this type of certification.";
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
        final String                     end2EntityType               = "CertificationType";
        final String                     end2AttributeName            = "certifications";
        final String                     end2AttributeDescription     = "The types of certifications that apply.";
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

        final String attribute1Name            = "certificateGUID";
        final String attribute1Description     = "Unique identifier of the actual certificate.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "start";
        final String attribute2Description     = "Start date for the certification.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "end";
        final String attribute3Description     = "End date for the certification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "conditions";
        final String attribute4Description     = "Any special conditions or endorsements over the basic certification type.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "certifiedBy";
        final String attribute5Description     = "Person or organization awarded the certification.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "custodian";
        final String attribute6Description     = "The person, engine or organization that will ensure the certification is honored.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "recipient";
        final String attribute7Description     = "The person or organization that received the certification.";
        final String attribute7DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getRegulationCertificationTypeRelationship()
    {
        final String guid            = "be12ff15-0721-4a7e-8c98-334eaa884bdf";
        final String name            = "RegulationCertificationType";
        final String description     = "Identifies a certification required by a regulation.";
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
        final String                     end1EntityType               = "Regulation";
        final String                     end1AttributeName            = "relatedRegulations";
        final String                     end1AttributeDescription     = "Regulations that require this type of certification.";
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
        final String                     end2EntityType               = "CertificationType";
        final String                     end2AttributeName            = "requiredCertifications";
        final String                     end2AttributeDescription     = "The certifications required by this regulation.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * ========================================
     * AREA 5: standards
     */


    /**
     * Area 5 covers information architecture models and schemas.
     */
    private void addArea5Types()
    {
        this.add0501SchemaElements();
        this.add0503AssetSchemas();
        this.add0504ImplementationSnippets();
        this.add0505SchemaAttributes();
        this.add0507BoundedSchemaTypes();
        this.add0510SchemaLinkElements();
        this.add0511SchemaMapElements();
        this.add0512DerivedSchemaAttributes();
        this.add0530TabularSchemas();
        this.add0531DocumentSchemas();
        this.add0532ObjectSchemas();
        this.add0533GraphSchemas();
        this.add0534RelationalSchemas();
        this.add0535EventSchemas();
        this.add0536APISchemas();
        this.add0540DataClasses();
        this.add0545ReferenceData();
        this.add0565DesignModelElements();
        this.add0566DesignModelOrganization();
        this.add0568DesignModelScoping();
        this.add0569DesignModelImplementation();
        this.add0570MetaModel();
        this.add0571ConceptModels();
        this.add0595DesignPatterns();
        this.add0598LineageRelationships();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0501 Schema Elements define the structure of data is described by a schema.  Schemas are nested structures
     * of schema elements.
     */
    private void add0501SchemaElements()
    {
        this.archiveBuilder.addEntityDef(getSchemaElementEntity());
        this.archiveBuilder.addEntityDef(getSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getSchemaTypeChoiceEntity());
        this.archiveBuilder.addEntityDef(getLiteralSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getSimpleSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getPrimitiveSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getEnumSchemaTypeEntity());
        this.archiveBuilder.addRelationshipDef(getSchemaTypeOptionRelationship());
    }


    private EntityDef getSchemaElementEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SCHEMA_ELEMENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID.name,
                                                           OpenMetadataProperty.ANCHOR_GUID.description,
                                                           OpenMetadataProperty.ANCHOR_GUID.descriptionGUID);
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


    private EntityDef getSchemaTypeEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SCHEMA_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "namespace";
        final String attribute1Description     = "Prefix for element names to ensure uniqueness.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "versionNumber";
        final String attribute2Description     = "Version of the schema type.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "author";
        final String attribute3Description     = "User name of the person or process that created the schema type.";
        final String attribute3DescriptionGUID = null;
        final String attribute5Name            = "encodingStandard";
        final String attribute5Description     = "Format of the schema.";
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
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.USAGE.name,
                                                           OpenMetadataProperty.USAGE.description,
                                                           OpenMetadataProperty.USAGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getSchemaTypeChoiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SCHEMA_TYPE_CHOICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName));
    }


    private EntityDef getLiteralSchemaTypeEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.LITERAL_SCHEMA_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "dataType";
        final String attribute1Description     = "Type name for the data stored in this schema element.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "fixedValue";
        final String attribute2Description     = "Fixed value for data stored in this schema element.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getSimpleSchemaTypeEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SIMPLE_SCHEMA_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "dataType";
        final String attribute1Description     = "Type name for the data stored in this schema element.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "defaultValue";
        final String attribute2Description     = "Initial value for data stored in this schema element.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getPrimitiveSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SIMPLE_SCHEMA_TYPE.typeName));
    }


    private EntityDef getEnumSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ENUM_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SIMPLE_SCHEMA_TYPE.typeName));
    }


    private RelationshipDef getSchemaTypeOptionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "schemaOptionalUses";
        final String                     end1AttributeDescription     = "Potential place where this schema type is used.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE_CHOICE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "schemaOptions";
        final String                     end2AttributeDescription     = "Possible structure of the content of this element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
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
     * 0503 Asset Schemas shows how assets are linked to schemas
     */
    private void add0503AssetSchemas()
    {
        this.archiveBuilder.addRelationshipDef(getAssetSchemaTypeRelationship());
    }


    private RelationshipDef getAssetSchemaTypeRelationship()
    {
       RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describesAssets";
        final String                     end1AttributeDescription     = "Asset that conforms to the schema type.";
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
        final String                     end2AttributeName            = "schema";
        final String                     end2AttributeDescription     = "Structure of the content of this asset.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0504 Implementation snippets enable code fragments defining data structures to be linked with the
     * relevant schema to show how the schema should be implemented.
     */
    private void add0504ImplementationSnippets()
    {
        this.archiveBuilder.addEntityDef(getImplementationSnippetEntity());

        this.archiveBuilder.addRelationshipDef(getSchemaTypeSnippetRelationship());
        this.archiveBuilder.addRelationshipDef(getSchemaTypeImplementationRelationship());
    }


    private EntityDef getImplementationSnippetEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.IMPLEMENTATION_SNIPPET,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "snippet";
        final String attribute1Description     = "Concrete implementation of the schema type.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "implementationLanguage";
        final String attribute2Description     = "Type of implementation.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "version";
        final String attribute3Description     = "Version number of the snippet.";
        final String attribute3DescriptionGUID = null;
        final String attribute5Name            = "curator";
        final String attribute5Description     = "User name of the person or process that is maintaining the snippet.";
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
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.USAGE.name,
                                                           OpenMetadataProperty.USAGE.description,
                                                           OpenMetadataProperty.USAGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getSchemaTypeSnippetRelationship()
    {
        final String guid            = "6aab4ec6-f0c6-4c40-9f50-ac02a3483358";
        final String name            = "SchemaTypeSnippet";
        final String description     = "Link between a schema type and an implementation snippet.";
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
        final String                     end1AttributeName            = "templateForSchemaTypes";
        final String                     end1AttributeDescription     = "Logical structure for data.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "implementationSnippets";
        final String                     end2AttributeDescription     = "Template implementation of the schema type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.IMPLEMENTATION_SNIPPET.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getSchemaTypeImplementationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SCHEMA_TYPE_IMPLEMENTATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "implementationSchemaTypes";
        final String                     end1AttributeDescription     = "Logical structure for the data.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "implementations";
        final String                     end2AttributeDescription     = "Concrete implementation of the schema type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName),
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
     * 0505 Schema Attributes begins to fill out the structure of a schema.
     */
    private void add0505SchemaAttributes()
    {
        this.archiveBuilder.addEnumDef(getDataItemSortOrderEnum());

        this.archiveBuilder.addEntityDef(getSchemaAttributeEntity());
        this.archiveBuilder.addEntityDef(getComplexSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getStructSchemaTypeEntity());

        this.archiveBuilder.addRelationshipDef(getAttributeForSchemaRelationship());
        this.archiveBuilder.addRelationshipDef(getSchemaAttributeTypeRelationship());
        this.archiveBuilder.addRelationshipDef(getNestedSchemaAttributeRelationship());

        this.archiveBuilder.addClassificationDef(getTypeEmbeddedAttributeClassification());
    }

    private EnumDef getDataItemSortOrderEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(DataItemSortOrder.getOpenTypeGUID(),
                                                        DataItemSortOrder.getOpenTypeName(),
                                                        DataItemSortOrder.getOpenTypeDescription(),
                                                        DataItemSortOrder.getOpenTypeDescriptionGUID(),
                                                        DataItemSortOrder.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (DataItemSortOrder enumValues : DataItemSortOrder.values())
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


    private EntityDef getSchemaAttributeEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME),
                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.description,
                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.descriptionGUID,
                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "position";
        final String attribute1Description     = "Location of the attribute in the parent schema's list of attributes, starting at zero.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "minCardinality";
        final String attribute2Description     = "Minimum number of occurrences of this attribute allowed (0 = optional, 1+ = mandatory).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "maxCardinality";
        final String attribute3Description     = "Maximum number of occurrences of this attribute allowed (-1 = infinite).";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "allowsDuplicateValues";
        final String attribute4Description     = "When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "orderedValues";
        final String attribute5Description     = "When multiple occurrences are allowed, indicates whether the values are ordered or not.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "defaultValueOverride";
        final String attribute6Description     = "Initial value for the attribute (overriding the default value of its type.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "nativeClass";
        final String attribute7Description     = "Native class used by the client to represent this element.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "name";
        final String attribute8Description     = "Name of schema attribute (deprecated).";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "aliases";
        final String attribute9Description     = "List of aliases for attribute.";
        final String attribute9DescriptionGUID = null;
        final String attribute10Name            = "sortOrder";
        final String attribute10Description     = "Suggested ordering of values in this attribute.";
        final String attribute10DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute4Name,
                                                            attribute4Description,
                                                            attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute5Name,
                                                            attribute5Description,
                                                            attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute8Name,
                                                           attribute8Description,
                                                           attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute9Name,
                                                                attribute9Description,
                                                                attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("DataItemSortOrder",
                                                         attribute10Name,
                                                         attribute10Description,
                                                         attribute10DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getComplexSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName));
    }


    private EntityDef getStructSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.STRUCT_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName));
    }


    private RelationshipDef getAttributeForSchemaRelationship()
    {
         RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentSchemas";
        final String                     end1AttributeDescription     = "Schema types using this attribute.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "attributes";
        final String                     end2AttributeDescription     = "The attributes defining the internal structure of the schema type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getSchemaAttributeTypeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usedInSchemas";
        final String                     end1AttributeDescription     = "Occurrences of this schema type in other schemas.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "type";
        final String                     end2AttributeDescription     = "The structure of this attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getNestedSchemaAttributeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentAttribute";
        final String                     end1AttributeDescription     = "Schema attribute containing this attribute.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "nestedAttributes";
        final String                     end2AttributeDescription     = "The attributes defining the internal structure of the parent attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private ClassificationDef getTypeEmbeddedAttributeClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "encodingStandard";
        final String attribute1Description     = "Format of the schema.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "dataType";
        final String attribute2Description     = "Type name for the data stored in this schema element.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0507 Bounded Schema Types adds more types of schema.
     */
    private void add0507BoundedSchemaTypes()
    {
        this.archiveBuilder.addEntityDef(getBoundedSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getArraySchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getSetSchemaTypeEntity());

        this.archiveBuilder.addRelationshipDef(getBoundedSchemaElementTypeRelationship());
    }


    private EntityDef getBoundedSchemaTypeEntity()
    {
        final String guid            = "77133161-37a9-43f5-aaa3-fd6d7ff92fdb";
        final String name            = "BoundedSchemaType";
        final String description     = "A schema type that limits the number of values that can be stored.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "maximumElements";
        final String attribute1Description     = "Maximum number of values that can be stored - zero for no limit.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getArraySchemaTypeEntity()
    {
        final String guid            = "ba8d29d2-a8a4-41f3-b29f-91ad924dd944";
        final String name            = "ArraySchemaType";
        final String description     = "A schema type that has a list of values of the same type.";
        final String descriptionGUID = null;

        final String superTypeName = "BoundedSchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getSetSchemaTypeEntity()
    {
        final String guid            = "b2605d2d-10cd-443c-b3e8-abf15fb051f0";
        final String name            = "SetSchemaType";
        final String description     = "A schema type that is an unordered group of values of the same type.";
        final String descriptionGUID = null;

        final String superTypeName = "BoundedSchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getBoundedSchemaElementTypeRelationship()
    {
        final String guid            = "3e844049-e59b-45dd-8e62-cde1add59f9e";
        final String name            = "BoundedSchemaElementType";
        final String description     = "The type of the element within the bounded schema type.";
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
        final String                     end1EntityType               = "BoundedSchemaType";
        final String                     end1AttributeName            = "usedInBoundedSchemaType";
        final String                     end1AttributeDescription     = "Use of this element as part of a more complex type.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "boundedSchemaElementType";
        final String                     end2AttributeDescription     = "The structure of the element within this type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0510 Schema Link Elements defines how a link between two schema elements in different schema element
     * hierarchies are linked.
     */
    private void add0510SchemaLinkElements()
    {
        this.archiveBuilder.addEntityDef(getSchemaLinkElementEntity());

        this.archiveBuilder.addRelationshipDef(getLinkedTypeRelationship());
        this.archiveBuilder.addRelationshipDef(getSchemaLinkToTypeRelationship());
    }


    private EntityDef getSchemaLinkElementEntity()
    {
        final String guid            = "67e08705-2d2a-4df6-9239-1818161a41e0";
        final String name            = "SchemaLinkElement";
        final String description     = "A link to a type in a different schema.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "linkName";
        final String attribute1Description     = "Name for the element that bridges between two schemas.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "linkProperties";
        final String attribute2Description     = "Any options needed to describe the link.";
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


    private RelationshipDef getLinkedTypeRelationship()
    {
        final String guid            = "292125f7-5660-4533-a48a-478c5611922e";
        final String name            = "LinkedType";
        final String description     = "Link between a link element and its type.";
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
        final String                     end1EntityType               = "SchemaLinkElement";
        final String                     end1AttributeName            = "linkedBy";
        final String                     end1AttributeDescription     = "External links to this type.";
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
        final String                     end2AttributeName            = "linkedType";
        final String                     end2AttributeDescription     = "Types for this attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getSchemaLinkToTypeRelationship()
    {
        final String guid            = "db9583c5-4690-41e5-a580-b4e30a0242d3";
        final String name            = "SchemaLinkToType";
        final String description     = "Link between a schema attribute and a schema link.";
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
        final String                     end1AttributeName            = "usedIn";
        final String                     end1AttributeDescription     = "Attributes of this type.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "SchemaLinkElement";
        final String                     end2AttributeName            = "externalType";
        final String                     end2AttributeDescription     = "External type for this attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0511 Schema Map Elements defines how a map element is recorded in a schema.
     */
    private void add0511SchemaMapElements()
    {
        this.archiveBuilder.addEntityDef(getMapSchemaTypeEntity());

        this.archiveBuilder.addRelationshipDef(getMapFromElementTypeRelationship());
        this.archiveBuilder.addRelationshipDef(getMapToElementTypeRelationship());
    }


    private EntityDef getMapSchemaTypeEntity()
    {
        final String guid            = "bd4c85d0-d471-4cd2-a193-33b0387a19fd";
        final String name            = "MapSchemaType";
        final String description     = "A schema type for a map between a key and value.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getMapFromElementTypeRelationship()
    {
        final String guid            = "6189d444-2da4-4cd7-9332-e48a1c340b44";
        final String name            = "MapFromElementType";
        final String description     = "Defines the type of the key for a map schema type.";
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
        final String                     end1EntityType               = "MapSchemaType";
        final String                     end1AttributeName            = "parentMapFrom";
        final String                     end1AttributeDescription     = "Used in map.";
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
        final String                     end2AttributeName            = "mapFromElement";
        final String                     end2AttributeDescription     = "Key for this attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getMapToElementTypeRelationship()
    {
        final String guid            = "8b9856b3-451e-45fc-afc7-fddefd81a73a";
        final String name            = "MapToElementType";
        final String description     = "Defines the type of value for a map schema type.";
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
        final String                     end1EntityType               = "MapSchemaType";
        final String                     end1AttributeName            = "parentMapTo";
        final String                     end1AttributeDescription     = "Used in map.";
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
        final String                     end2AttributeName            = "mapToElement";
        final String                     end2AttributeDescription     = "Value for this map.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0512 Derived Schema Attributes defines how one attribute can be derived from another.
     */
    private void add0512DerivedSchemaAttributes()
    {
        this.archiveBuilder.addEntityDef(getDerivedSchemaAttributeEntity());

        this.archiveBuilder.addRelationshipDef(getSchemaQueryImplementationRelationship());
    }


    private EntityDef getDerivedSchemaAttributeEntity()
    {
        final String guid            = "cf21abfe-655a-47ba-b9b6-f73394745c80";
        final String name            = "DerivedSchemaAttribute";
        final String description     = "An attribute that is made up of values from another attribute.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "formula";
        final String attribute1Description     = "Transformation used to create the derived data.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "comment";
        final String attribute2Description     = "Comment from source system (deprecated).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "id";
        final String attribute3Description     = "Id of derived schema attribute (deprecated).";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "aggregatingFunction";
        final String attribute4Description     = "Aggregating function of derived schema attribute (deprecated).";
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

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getSchemaQueryImplementationRelationship()
    {
        final String guid            = "e5d7025d-8b4f-43c7-bcae-1047d650b94a";
        final String name            = "SchemaQueryImplementation";
        final String description     = "Details of how a derived schema attribute is calculated.";
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
        final String                     end1EntityType               = "DerivedSchemaAttribute";
        final String                     end1AttributeName            = "usedBy";
        final String                     end1AttributeDescription     = "Use of an attribute to derive another attribute.";
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
        final String                     end2AttributeName            = "queryTarget";
        final String                     end2AttributeDescription     = "Used to derive this attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
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

        final String attribute1Name            = "query";
        final String attribute1Description     = "Details of how the attribute is retrieved.";
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
     * 0530 describes table oriented schemas such as spreadsheets
     */
    private void add0530TabularSchemas()
    {
        this.archiveBuilder.addEntityDef(getTabularSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getTabularColumnTypeEntity());
        this.archiveBuilder.addEntityDef(getTabularColumnEntity());
    }


    private EntityDef getTabularSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TABULAR_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName));
    }


    private EntityDef getTabularColumnTypeEntity()
    {
        final String guid            = "a7392281-348d-48a4-bad7-f9742d7696fe";
        final String name            = "TabularColumnType";
        final String description     = "A schema type for a column oriented data structure.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getTabularColumnEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TABULAR_COLUMN,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0531 Document Schema provide specialized entities for describing document oriented schemas such as JSON
     */
    private void add0531DocumentSchemas()
    {
        this.archiveBuilder.addEntityDef(getDocumentSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getDocumentSchemaAttributeEntity());
        this.archiveBuilder.addEntityDef(getSimpleDocumentTypeEntity());
        this.archiveBuilder.addEntityDef(getStructDocumentTypeEntity());
        this.archiveBuilder.addEntityDef(getArrayDocumentTypeEntity());
        this.archiveBuilder.addEntityDef(getSetDocumentTypeEntity());
        this.archiveBuilder.addEntityDef(getMapDocumentTypeEntity());
    }


    private EntityDef getDocumentSchemaTypeEntity()
    {
        final String guid            = "33da99cd-8d04-490c-9457-c58908da7794";
        final String name            = "DocumentSchemaType";
        final String description     = "A schema type for a hierarchical data structure.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getDocumentSchemaAttributeEntity()
    {
        final String guid            = "b5cefb7e-b198-485f-a1d7-8e661012499b";
        final String name            = "DocumentSchemaAttribute";
        final String description     = "A schema attribute for a hierarchical data structure.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getSimpleDocumentTypeEntity()
    {
        final String guid            = "42cfccbf-cc68-4980-8c31-0faf1ee002d3";
        final String name            = "SimpleDocumentType";
        final String description     = "A primitive attribute for a hierarchical data structure.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getStructDocumentTypeEntity()
    {
        final String guid            = "f6245c25-8f73-45eb-8fb5-fa17a5f27649";
        final String name            = "StructDocumentType";
        final String description     = "A structure within a hierarchical data structure.";
        final String descriptionGUID = null;


        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getArrayDocumentTypeEntity()
    {
        final String guid            = "ddd29c67-db9a-45ff-92aa-6d17a12a8ee2";
        final String name            = "ArrayDocumentType";
        final String description     = "An array in a hierarchical data structure.";
        final String descriptionGUID = null;

        final String superTypeName = "ArraySchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getSetDocumentTypeEntity()
    {
        final String guid            = "67228a7a-9d8d-4fa7-b217-17474f1f4ac6";
        final String name            = "SetDocumentType";
        final String description     = "A set in a hierarchical data structure.";
        final String descriptionGUID = null;

        final String superTypeName = "SetSchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getMapDocumentTypeEntity()
    {
        final String guid            = "b0f09598-ceb6-415b-befc-563ecadd5727";
        final String name            = "MapDocumentType";
        final String description     = "A map in a hierarchical data structure.";
        final String descriptionGUID = null;

        final String superTypeName = "MapSchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0532 Object Schemas provides the specialized entity for an object schema.
     */
    private void add0532ObjectSchemas()
    {
        this.archiveBuilder.addEntityDef(getObjectSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getObjectAttributeEntity());
    }


    private EntityDef getObjectSchemaTypeEntity()
    {
        final String guid            = "6920fda1-7c07-47c7-84f1-9fb044ae153e";
        final String name            = "ObjectSchemaType";
        final String description     = "A schema attribute for an object.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getObjectAttributeEntity()
    {
        final String guid            = "ccb408c0-582e-4a3a-a926-7082d53bb669";
        final String name            = "ObjectAttribute";
        final String description     = "An attribute in an object schema type.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0533GraphSchemas()
    {
        this.archiveBuilder.addEntityDef(getGraphSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getGraphVertexEntity());
        this.archiveBuilder.addEntityDef(getGraphEdgeEntity());

        this.archiveBuilder.addRelationshipDef(getGraphEdgeLinkRelationship());

    }


    private EntityDef getGraphSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GRAPH_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE_TYPE_NAME));
    }


    private EntityDef getGraphVertexEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GRAPH_VERTEX,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));
    }


    private EntityDef getGraphEdgeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GRAPH_EDGE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName));
    }


    private RelationshipDef getGraphEdgeLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "edges";
        final String                     end1AttributeDescription     = "Edges for this vertex.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GRAPH_EDGE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "vertices";
        final String                     end2AttributeDescription     = "Vertices for this edge.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GRAPH_VERTEX.typeName),
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

        final String attribute1Name            = OpenMetadataProperty.LINK_TYPE_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.LINK_TYPE_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.LINK_TYPE_NAME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.RELATIONSHIP_END.name;
        final String attribute2Description     = OpenMetadataProperty.RELATIONSHIP_END.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.RELATIONSHIP_END.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.RELATIONSHIP_END_NAME.name;
        final String attribute3Description     = OpenMetadataProperty.RELATIONSHIP_END_NAME.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.RELATIONSHIP_END_NAME.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.MIN_CARDINALITY.name;
        final String attribute5Description     = OpenMetadataProperty.MIN_CARDINALITY.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.MIN_CARDINALITY.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.MAX_CARDINALITY.name;
        final String attribute6Description     = OpenMetadataProperty.MAX_CARDINALITY.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.MAX_CARDINALITY.descriptionGUID;


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
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute5Name,
                                                        attribute5Description,
                                                        attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute6Name,
                                                        attribute6Description,
                                                        attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.description,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0534 Relational Schemas extend the tabular schemas to describe a relational database.
     */
    private void add0534RelationalSchemas()
    {
        this.archiveBuilder.addEntityDef(getRelationalDBSchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getRelationalTableTypeEntity());
        this.archiveBuilder.addEntityDef(getRelationalTableEntity());
        this.archiveBuilder.addEntityDef(getRelationalColumnEntity());
        this.archiveBuilder.addEntityDef(getRelationalColumnTypeEntity());
        this.archiveBuilder.addEntityDef(getDerivedRelationalColumnEntity());

        this.archiveBuilder.addClassificationDef(getPrimaryKeyClassification());
        this.archiveBuilder.addClassificationDef(getRelationalViewClassification());

        this.archiveBuilder.addRelationshipDef(getForeignKeyRelationship());
    }


    private EntityDef getRelationalDBSchemaTypeEntity()
    {
        final String guid            = "f20f5f45-1afb-41c1-9a09-34d8812626a4";
        final String name            = "RelationalDBSchemaType";
        final String description     = "A schema type for a relational database.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getRelationalTableTypeEntity()
    {
        final String guid            = "1321bcc0-dc6a-48ed-9ca6-0c6f934b0b98";
        final String name            = "RelationalTableType";
        final String description     = "A table type for a relational database.";
        final String descriptionGUID = null;

        final String superTypeName = "TabularSchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getRelationalTableEntity()
    {
        final String guid            = "ce7e72b8-396a-4013-8688-f9d973067425";
        final String name            = "RelationalTable";
        final String description     = "A table within a relational database schema type.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getRelationalColumnEntity()
    {
        final String guid            = "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9";
        final String name            = "RelationalColumn";
        final String description     = "A column within a relational table.";
        final String descriptionGUID = null;

        final String superTypeName = "TabularColumn";

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
        
        final String attribute3Name            = "fraction";
        final String attribute3Description     = "Number of significant digits to the right of decimal point.";
        final String attribute3DescriptionGUID = null;
        final String attribute5Name            = "isUnique";
        final String attribute5Description     = "Data is unique or not.";
        final String attribute5DescriptionGUID = null;
        
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute5Name,
                                                            attribute5Description,
                                                            attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getRelationalColumnTypeEntity()
    {
        final String guid            = "f0438d80-6eb9-4fac-bcc1-5efee5babcfc";
        final String name            = "RelationalColumnType";
        final String description     = "A type for a relational column.";
        final String descriptionGUID = null;

        final String superTypeName = "TabularColumnType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getDerivedRelationalColumnEntity()
    {
        final String guid            = "a9f7d15d-b797-450a-8d56-1ba55490c019";
        final String name            = "DerivedRelationalColumn";
        final String description     = "A relational column that is derived from other columns.";
        final String descriptionGUID = null;

        final String superTypeName = "DerivedSchemaAttribute";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private ClassificationDef getPrimaryKeyClassification()
    {
        final String guid            = "b239d832-50bd-471b-b17a-15a335fc7f40";
        final String name            = "PrimaryKey";
        final String description     = "A uniquely identifying relational column.";
        final String descriptionGUID = null;

        final String linkedToEntity = "RelationalColumn";

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(
                                                                                         linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.KEY_PATTERN));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getRelationalViewClassification()
    {
        final String guid            = "4814bec8-482d-463d-8376-160b0358e129";
        final String name            = "RelationalView";
        final String description     = "A view within a relational database schema type.";
        final String descriptionGUID = null;

        final String linkedToEntity = "RelationalTable";

        ClassificationDef classificationDef =  archiveHelper.getClassificationDef(guid,
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

        final String attribute1Name            = "expression";
        final String attribute1Description     = "Expression of the view.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;          
    }


    private RelationshipDef getForeignKeyRelationship()
    {
        final String guid            = "3cd4e0e7-fdbf-47a6-ae88-d4b3205e0c07";
        final String name            = "ForeignKey";
        final String description     = "The primary key for another column is stored in a relational column from another table to enable them to be joined.";
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
        final String                     end1EntityType               = "RelationalColumn";
        final String                     end1AttributeName            = "primaryKey";
        final String                     end1AttributeDescription     = "Relational column holding the primary key.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "RelationalColumn";
        final String                     end2AttributeName            = "foreignKey";
        final String                     end2AttributeDescription     = "Use of primary key from another table to enable table joins.";
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NAME.name,
                                                           OpenMetadataProperty.NAME.description,
                                                           OpenMetadataProperty.NAME.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0535 Event Schemas describes event/message structures
     */
    private void add0535EventSchemas()
    {
        this.archiveBuilder.addEntityDef(getEventSetEntity());
        this.archiveBuilder.addEntityDef(getEventTypeEntity());
    }


    private EntityDef getEventSetEntity()
    {
        final String guid            = "bead9aa4-214a-4596-8036-aa78395bbfb1";
        final String name            = "EventSet";
        final String description     = "A collection of related event types.";
        final String descriptionGUID = null;

        final String superTypeName = "Collection";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getEventTypeEntity()
    {
        final String guid            = "8bc88aba-d7e4-4334-957f-cfe8e8eadc32";
        final String name            = "EventType";
        final String description     = "A description of an event (message)";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0536 API schemas define the structure of an API
     */
    private void add0536APISchemas()
    {
        this.archiveBuilder.addEntityDef(getAPISchemaTypeEntity());
        this.archiveBuilder.addEntityDef(getAPIOperationSchemaEntity());

        this.archiveBuilder.addRelationshipDef(getAPIOperationsRelationship());
        this.archiveBuilder.addRelationshipDef(getAPIHeaderRelationship());
        this.archiveBuilder.addRelationshipDef(getAPIRequestRelationship());
        this.archiveBuilder.addRelationshipDef(getAPIResponseRelationship());
    }


    private EntityDef getAPISchemaTypeEntity()
    {
        final String guid            = "b46cddb3-9864-4c5d-8a49-266b3fc95cb8";
        final String name            = "APISchemaType";
        final String description     = "Description of an API.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getAPIOperationSchemaEntity()
    {
        final String guid            = "f1c0af19-2729-4fac-996e-a7badff3c21c";
        final String name            = "APIOperation";
        final String description     = "Description of an API operation.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getAPIOperationsRelationship()
    {
        final String guid            = "03737169-ceb5-45f0-84f0-21c5929945af";
        final String name            = "APIOperations";
        final String description     = "Link between an API and its operations.";
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
        final String                     end1EntityType               = "APISchemaType";
        final String                     end1AttributeName            = "usedInAPI";
        final String                     end1AttributeDescription     = "API that this operation belongs to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "APIOperation";
        final String                     end2AttributeName            = "containsOperations";
        final String                     end2AttributeDescription     = "Operations for this API type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getAPIHeaderRelationship()
    {
        final String guid            = "e8fb46d1-5f75-481b-aa66-f43ad44e2cc6";
        final String name            = "APIHeader";
        final String description     = "Link between an API operation and its header.";
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
        final String                     end1EntityType               = "APIOperation";
        final String                     end1AttributeName            = "usedAsAPIHeader";
        final String                     end1AttributeDescription     = "API operations using this structure as the header.";
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
        final String                     end2AttributeName            = "apiHeader";
        final String                     end2AttributeDescription     = "Header structure for this API operation.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getAPIRequestRelationship()
    {
        final String guid            = "4ab3b466-31bd-48ea-8aa2-75623476f2e2";
        final String name            = "APIRequest";
        final String description     = "Link between an API operation and its request structure.";
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
        final String                     end1EntityType               = "APIOperation";
        final String                     end1AttributeName            = "usedAsAPIRequest";
        final String                     end1AttributeDescription     = "API operations using this structure as the request body.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "apiRequest";
        final String                     end2AttributeDescription     = "Request structure for this API operation.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getAPIResponseRelationship()
    {
        final String guid            = "e8001de2-1bb1-442b-a66f-9addc3641eae";
        final String name            = "APIResponse";
        final String description     = "Link between an API operation and its response structure.";
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
        final String                     end1EntityType               = "APIOperation";
        final String                     end1AttributeName            = "usedAsAPIResponse";
        final String                     end1AttributeDescription     = "API operations using this structure as the response.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "apiResponse";
        final String                     end2AttributeDescription     = "Response structure for this API operation.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0540 Data Class definitions
     */
    private void add0540DataClasses()
    {
        this.archiveBuilder.addEnumDef(getDataClassAssignmentStatusEnum());

        this.archiveBuilder.addEntityDef(getDataClassEntity());

        this.archiveBuilder.addRelationshipDef(getDataClassHierarchyRelationship());
        this.archiveBuilder.addRelationshipDef(getDataClassCompositionRelationship());
        this.archiveBuilder.addRelationshipDef(getDataClassAssignmentRelationship());

    }


    private EnumDef getDataClassAssignmentStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(DataClassAssignmentStatus.getOpenTypeGUID(),
                                                        DataClassAssignmentStatus.getOpenTypeName(),
                                                        DataClassAssignmentStatus.getOpenTypeDescription(),
                                                        DataClassAssignmentStatus.getOpenTypeDescriptionGUID(),
                                                        DataClassAssignmentStatus.getOpenTypeDescriptionWiki());

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


    private EntityDef getDataClassEntity()
    {
       EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_CLASS.typeGUID,
                                                                OpenMetadataType.DATA_CLASS.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                OpenMetadataType.DATA_CLASS.description,
                                                                OpenMetadataType.DATA_CLASS.descriptionGUID,
                                                                OpenMetadataType.DATA_CLASS.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = "classCode";
        final String attribute3Description     = "Name of processing class that can identify the data class.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "userDefined";
        final String attribute4Description     = "Defined by owning organization rather than vendor.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "namespace";
        final String attribute5Description     = "Logical group for this data class.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "specification";
        final String attribute6Description     = "Parsing string used to identify values of this data class.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "specificationDetails";
        final String attribute7Description     = "Additional properties used in the specification.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "dataType";
        final String attribute8Description     = "Typical data type used to store this value.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "defaultThreshold";
        final String attribute9Description     = "Match threshold that a data field is expected to achieve to be assigned this data class.";
        final String attribute9DescriptionGUID = null;
        final String attribute10Name            = "example";
        final String attribute10Description     = "Example of a data value that matches this data class.";
        final String attribute10DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NAME.name,
                                                           OpenMetadataProperty.NAME.description,
                                                           OpenMetadataProperty.NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute4Name,
                                                            attribute4Description,
                                                            attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute8Name,
                                                           attribute8Description,
                                                           attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute9Name,
                                                          attribute9Description,
                                                          attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute10Name,
                                                           attribute10Description,
                                                           attribute10DescriptionGUID);
        properties.add(property);



        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getDataClassHierarchyRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_CLASS_HIERARCHY.typeGUID,
                                                                                OpenMetadataType.DATA_CLASS_HIERARCHY.typeName,
                                                                                null,
                                                                                OpenMetadataType.DATA_CLASS_HIERARCHY.description,
                                                                                OpenMetadataType.DATA_CLASS_HIERARCHY.descriptionGUID,
                                                                                OpenMetadataType.DATA_CLASS_HIERARCHY.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "superDataClass";
        final String                     end1AttributeDescription     = "Data class that is the more abstract.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "subDataClasses";
        final String                     end2AttributeDescription     = "Data classes that are more concrete.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDataClassCompositionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_CLASS_COMPOSITION.typeGUID,
                                                                                OpenMetadataType.DATA_CLASS_COMPOSITION.typeName,
                                                                                null,
                                                                                OpenMetadataType.DATA_CLASS_COMPOSITION.description,
                                                                                OpenMetadataType.DATA_CLASS_COMPOSITION.descriptionGUID,
                                                                                OpenMetadataType.DATA_CLASS_COMPOSITION.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "partOfDataClasses";
        final String                     end1AttributeDescription     = "Data classes that includes other data classes in its definition.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "madeOfDataClasses";
        final String                     end2AttributeDescription     = "Data classes that provide part of another data class's definitions.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDataClassAssignmentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_CLASS_ASSIGNMENT.typeGUID,
                                                                                OpenMetadataType.DATA_CLASS_ASSIGNMENT.typeName,
                                                                                null,
                                                                                OpenMetadataType.DATA_CLASS_ASSIGNMENT.description,
                                                                                OpenMetadataType.DATA_CLASS_ASSIGNMENT.descriptionGUID,
                                                                                OpenMetadataType.DATA_CLASS_ASSIGNMENT.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "elementsAssignedToDataClass";
        final String                     end1AttributeDescription     = "Elements identified as managing data values that match the specification of a data class.";
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
        final String                     end2AttributeName            = "dataClassesAssignedToElement";
        final String                     end2AttributeDescription     = "Logical data type for this element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
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

        final String attribute1Name            = "method";
        final String attribute1Description     = "Method used to identify data class.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "status";
        final String attribute2Description     = "The status of the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "partialMatch";
        final String attribute3Description     = "Are there data values outside of the data class specification?";
        final String attribute3DescriptionGUID = null;
        final String attribute5Name            = "threshold";
        final String attribute5Description     = "What was the threshold result used to determine that the data class matched.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "valueFrequency";
        final String attribute6Description     = "How often does the data class specification match the data values.";
        final String attribute6DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("DataClassAssignmentStatus",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute3Name,
                                                            attribute3Description,
                                                            attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
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


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0545ReferenceData()
    {
        this.archiveBuilder.addClassificationDef(getReferenceDataClassification());

        this.archiveBuilder.addEntityDef(getValidValueDefinitionEntity());
        this.archiveBuilder.addEntityDef(getValidValuesSetEntity());

        this.archiveBuilder.addRelationshipDef(getValidValuesAssignmentRelationship());
        this.archiveBuilder.addRelationshipDef(getValidValuesMemberRelationship());
        this.archiveBuilder.addRelationshipDef(getValidValuesImplementationRelationship());
    }

    private ClassificationDef getReferenceDataClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.REFERENCE_DATA_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                  false);
    }


    private EntityDef getValidValueDefinitionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCOPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PREFERRED_VALUE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getValidValuesSetEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VALID_VALUE_SET,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName));
    }


    private RelationshipDef getValidValuesAssignmentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "validValuesConsumer";
        final String                     end1AttributeDescription     = "The valid values set that this element belongs to.";
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
        final String                     end2AttributeName            = "validValues";
        final String                     end2AttributeDescription     = "A definition of the valid values for this element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
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

        final String attribute1Name            = "strictRequirement";
        final String attribute1Description     = "Only values from the ValidValues set/definition are allowed.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getValidValuesMemberRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "validValuesSet";
        final String                     end1AttributeDescription     = "The valid values set that this element belongs to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_SET.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "memberOfValidValuesSet";
        final String                     end2AttributeDescription     = "Description of a single valid value.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getValidValuesImplementationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "validValues";
        final String                     end1AttributeDescription     = "The valid values set that this element implements.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "validValuesImplementation";
        final String                     end2AttributeDescription     = "The asset where the valid values are implemented.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
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

        final String attribute1Name            = "implementationValue";
        final String attribute1Description     = "Value in the asset that maps to this valid value if different from the preferred value.";
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

    private void add0565DesignModelElements()
    {
        this.archiveBuilder.addEntityDef(getDesignModelElementEntity());
    }


    private EntityDef getDesignModelElementEntity()
    {
        final String guid            = "492e343f-2516-43b8-94b0-5bae0760dda6";
        final String name            = "DesignModelElement";
        final String description     = "An abstract, but well-formed representation of a concept, activity, architecture or other design element.";
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

        final String attribute2Name            = "technicalName";
        final String attribute2Description     = "Technical name (no spaces) that can be used in artifact generation.";
        final String attribute2DescriptionGUID = null;
        final String attribute4Name            = "versionNumber";
        final String attribute4Description     = "Version number of the model element.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "author";
        final String attribute5Description     = "Name of the creator of the model (person or organization).";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0566DesignModelOrganization()
    {
        this.archiveBuilder.addEntityDef(getDesignModelEntity());
        this.archiveBuilder.addEntityDef(getDesignModelGroupEntity());

        this.archiveBuilder.addRelationshipDef(getDesignModelElementOwnershipRelationship());
        this.archiveBuilder.addRelationshipDef(getDesignModelGroupOwnershipRelationship());
        this.archiveBuilder.addRelationshipDef(getDesignModelGroupHierarchyRelationship());
        this.archiveBuilder.addRelationshipDef(getDesignModelGroupMembershipRelationship());
    }


    private EntityDef getDesignModelEntity()
    {
        final String guid            = "bf17143d-8605-48c2-ba80-64c2ac8f8379";
        final String name            = "DesignModel";
        final String description     = "A curated collection of design model elements.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "technicalName";
        final String attribute1Description     = "Technical name (no spaces) that can be used in artifact generation.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "versionNumber";
        final String attribute2Description     = "Version number of the model.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "author";
        final String attribute3Description     = "Name of the creator of the model (person or organization).";
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

        return entityDef;
    }


    private EntityDef getDesignModelGroupEntity()
    {
        final String guid            = "b144ee2a-fa71-4897-b51a-dd5239c26910";
        final String name            = "DesignModelGroup";
        final String description     = "A collection of related design model elements within a model.";
        final String descriptionGUID = null;

        final String superTypeName = "DesignModelElement";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef getDesignModelElementOwnershipRelationship()
    {
        final String guid            = "f3b18ac7-3357-4a0c-8988-77a98adad5b5";
        final String name            = "DesignModelElementOwnership";
        final String description     = "Links design model elements to their owning model.";
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
        final String                     end1EntityType               = "DesignModel";
        final String                     end1AttributeName            = "owningModel";
        final String                     end1AttributeDescription     = "Owning model.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "DesignModelElement";
        final String                     end2AttributeName            = "elementsInModel";
        final String                     end2AttributeDescription     = "List of elements that belong to this model.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDesignModelGroupOwnershipRelationship()
    {
        final String guid            = "4a985162-8130-4559-b68e-6e6a5dc19c2a";
        final String name            = "DesignModelGroupOwnership";
        final String description     = "Links a model to a design model group.";
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
        final String                     end1EntityType               = "DesignModel";
        final String                     end1AttributeName            = "anchorModel";
        final String                     end1AttributeDescription     = "Model that owns this group.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "DesignModelGroup";
        final String                     end2AttributeName            = "groupsInModel";
        final String                     end2AttributeDescription     = "List of groups that belong to this model.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDesignModelGroupHierarchyRelationship()
    {
        final String guid            = "809b7c6c-69f9-4dbf-a5dd-085664499438";
        final String name            = "DesignModelGroupHierarchy";
        final String description     = "Links a model's groups into a hierarchy.";
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
        final String                     end1EntityType               = "DesignModelGroup";
        final String                     end1AttributeName            = "parentModelGroup";
        final String                     end1AttributeDescription     = "Link parent group.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "DesignModelGroup";
        final String                     end2AttributeName            = "childModelGroups";
        final String                     end2AttributeDescription     = "The groups nested in this group.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDesignModelGroupMembershipRelationship()
    {
        final String guid            = "2dcfe62b-341c-4c3d-b336-a94a52c20556";
        final String name            = "DesignModelGroupMembership";
        final String description     = "Links a design model element to a group.";
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
        final String                     end1EntityType               = "DesignModelGroup";
        final String                     end1AttributeName            = "memberOfModelGroups";
        final String                     end1AttributeDescription     = "Link to a list of groups this element is a member of.";
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
        final String                     end2EntityType               = "DesignModelElement";
        final String                     end2AttributeName            = "elementsInGroup";
        final String                     end2AttributeDescription     = "List of elements that belong to this group.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

    private void add0568DesignModelScoping()
    {
        this.archiveBuilder.addEntityDef(getDesignModelScopeEntity());
        this.archiveBuilder.addRelationshipDef(getDesignModelElementsInScopeRelationship());
    }


    private EntityDef getDesignModelScopeEntity()
    {
        final String guid            = "788957f7-a203-45bd-994d-0ab018275821";
        final String name            = "DesignModelScope";
        final String description     = "A selection of design model element needed for a project.";
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

        final String attribute2Name            = "technicalName";
        final String attribute2Description     = "Technical name (no spaces) that can be used in artifact generation.";
        final String attribute2DescriptionGUID = null;
        final String attribute4Name            = "versionNumber";
        final String attribute4Description     = "Version number of the model element.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "author";
        final String attribute5Description     = "UserId of the creator of the model element.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getDesignModelElementsInScopeRelationship()
    {
        final String guid            = "4ff6d91b-3836-4ba2-9ca9-87da91081faa";
        final String name            = "DesignModelElementsInScope";
        final String description     = "Links a model to an implementation.";
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
        final String                     end1EntityType               = "DesignModelScope";
        final String                     end1AttributeName            = "usedInScope";
        final String                     end1AttributeDescription     = "Link to a scope where this element is used.";
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
        final String                     end2EntityType               = "DesignModelElement";
        final String                     end2AttributeName            = "inScopeModelElements";
        final String                     end2AttributeDescription     = "List of elements that belong to this scope.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

    private void add0569DesignModelImplementation()
    {
        this.archiveBuilder.addRelationshipDef(geDesignModelImplementationRelationship());
    }


    private RelationshipDef geDesignModelImplementationRelationship()
    {
        final String guid            = "c5cb1362-07f6-486b-b80b-ba7922cacee9";
        final String name            = "DesignModelImplementation";
        final String description     = "Links a concept model to an implementation.";
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
        final String                     end1AttributeName            = "implementationFollowingModel";
        final String                     end1AttributeDescription     = "Definition of an implementation of the model.";
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
        final String                     end2EntityType               = "DesignModelElement";
        final String                     end2AttributeName            = "modelDescribingBehavior";
        final String                     end2AttributeDescription     = "Descriptive abstraction.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

    private void add0570MetaModel()
    {
        this.archiveBuilder.addClassificationDef(getMetamodelClassification());
    }


    private ClassificationDef getMetamodelClassification()
    {
        final String guid            = "07bd0820-6b14-43b0-a625-2c89f2beb93a";
        final String name            = "MetamodelInstance";
        final String description     = "Identifies the element from a metadata model that this element embodies.";
        final String descriptionGUID = null;

        final String linkedToEntity = "DesignModelElement";

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

        final String attribute1Name            = "metamodelElementGUID";
        final String attribute1Description     = "Element in the metadata model that the attached element embodies.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0571ConceptModels()
    {
        this.archiveBuilder.addEnumDef(getConceptModelAttributeCoverageCategoryEnum());
        this.archiveBuilder.addEnumDef(getConceptModelDecorationEnum());

        this.archiveBuilder.addEntityDef(getConceptModelElementEntity());
        this.archiveBuilder.addEntityDef(getConceptBeadEntity());
        this.archiveBuilder.addEntityDef(getConceptBeadLinkEntity());
        this.archiveBuilder.addEntityDef(getConceptBeadAttributeEntity());

        this.archiveBuilder.addRelationshipDef(getConceptBeadRelationshipEndRelationship());
        this.archiveBuilder.addRelationshipDef(getConceptBeadAttributeLinkRelationship());

        this.archiveBuilder.addClassificationDef(getConceptBeadAttributeCoverageClassification());
    }


    private EnumDef getConceptModelAttributeCoverageCategoryEnum()
    {
        final String guid            = "2c0ac237-e02e-431a-89fd-3107d94d4007";
        final String name            = "ConceptModelAttributeCoverageCategory";
        final String description     = "Describes the type of attribute - this is used in scoping the model.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Unknown";
        final String element1Description     = "The attribute's coverage category is unknown - this is the default.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);
        enumDef.setDefaultValue(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "UniqueIdentifier";
        final String element2Description     = "The attribute uniquely identifies the concept bead.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "Identifier";
        final String element3Description     = "The attribute is a good indicator of the identity of the concept bead but not guaranteed to be unique.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 3;
        final String element4Value           = "CoreDetail";
        final String element4Description     = "The attribute provides information that is typically required by all of the consumers of the concept bead.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 4;
        final String element5Value           = "ExtendedDetail";
        final String element5Description     = "The attribute contains supplementary information that is of interest to specific consumers of the concept bead.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                     element5Value,
                                                     element5Description,
                                                     element5DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EnumDef getConceptModelDecorationEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ConceptModelDecoration.getOpenTypeGUID(),
                                                        ConceptModelDecoration.getOpenTypeName(),
                                                        ConceptModelDecoration.getOpenTypeDescription(),
                                                        ConceptModelDecoration.getOpenTypeDescriptionGUID());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ConceptModelDecoration enumValue : ConceptModelDecoration.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getConceptModelElementEntity()
    {
        final String guid            = "06659195-3111-4c91-8931-a65f655378d9";
        final String name            = "ConceptModelElement";
        final String description     = "An abstract, but well-formed representation of a concept.";
        final String descriptionGUID = null;

        final String superTypeName = "DesignModelElement";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getConceptBeadEntity()
    {
        final String guid            = "f7feb509-bce6-4989-a340-5dc7e3eec313";
        final String name            = "ConceptBead";
        final String description     = "An abstract, but well-formed representation of a person, place or object.";
        final String descriptionGUID = null;

        final String superTypeName = "ConceptModelElement";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getConceptBeadLinkEntity()
    {
        final String guid            = "13defd95-6452-4398-8382-e47f1a271eff";
        final String name            = "ConceptBeadLink";
        final String description     = "A relationship between concept beads.";
        final String descriptionGUID = null;

        final String superTypeName = "ConceptModelElement";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getConceptBeadAttributeEntity()
    {
        final String guid            = "d804d406-ac74-4f92-9bde-2ba0793680ea";
        final String name            = "ConceptBeadAttribute";
        final String description     = "An abstract, but well-formed fact about a concept bead.";
        final String descriptionGUID = null;

        final String superTypeName = "ConceptModelElement";

        return  archiveHelper.getDefaultEntityDef(guid,
                                                  name,
                                                  this.archiveBuilder.getEntityDef(superTypeName),
                                                  description,
                                                  descriptionGUID);
    }


    private RelationshipDef getConceptBeadRelationshipEndRelationship()
    {
        final String guid            = "1a379e55-a4c0-4289-a1a4-b89d257611d1";
        final String name            = "ConceptBeadRelationshipEnd";
        final String description     = "Links one end of a concept bead link relationship to a concept bead.";
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
        final String                     end1EntityType               = "ConceptBeadLink";
        final String                     end1AttributeName            = "relationships";
        final String                     end1AttributeDescription     = "The relationships that the concept bead can be a part of.";
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
        final String                     end2EntityType               = "ConceptBead";
        final String                     end2AttributeName            = "endBeads";
        final String                     end2AttributeDescription     = "The concept beads that are linked via this relationship.";
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

        final String attribute1Name            = "attributeName";
        final String attribute1Description     = "Name for the relationship end.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "decoration";
        final String attribute2Description     = "Usage and lifecycle for this connection between the concept bead and the link.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "position";
        final String attribute3Description     = "Position of this relationship in the concept bead's list of relationships.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "minCardinality";
        final String attribute4Description     = "Minimum number of occurrences of this attribute allowed (0 = optional, 1+ = mandatory).";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "maxCardinality";
        final String attribute5Description     = "Maximum number of occurrences of this attribute allowed (-1 = infinite).";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "uniqueValues";
        final String attribute6Description     = "When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "orderedValues";
        final String attribute7Description     = "When multiple occurrences are allowed, indicates whether the values are ordered or not.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "navigable";
        final String attribute8Description     = "Is it possible to follow the link in this direction.";
        final String attribute8DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("ConceptModelDecoration",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute5Name,
                                                        attribute5Description,
                                                        attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute6Name,
                                                            attribute6Description,
                                                            attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute7Name,
                                                            attribute7Description,
                                                            attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute8Name,
                                                            attribute8Description,
                                                            attribute8DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getConceptBeadAttributeLinkRelationship()
    {
        final String guid            = "5bad1df2-664b-407b-8036-2855e2ede92f";
        final String name            = "ConceptBeadAttributeLink";
        final String description     = "Links a concept bead to its attributes.";
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
        final String                     end1EntityType               = "ConceptBead";
        final String                     end1AttributeName            = "parentBead";
        final String                     end1AttributeDescription     = "Concept bead that this attribute belongs to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "ConceptBeadAttribute";
        final String                     end2AttributeName            = "attributes";
        final String                     end2AttributeDescription     = "Attribute detail for the concept bead.";
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

        final String attribute1Name            = "position";
        final String attribute1Description     = "Position of this relationship in the concept bead's list of relationships.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "minCardinality";
        final String attribute2Description     = "Minimum number of occurrences of this attribute allowed (0 = optional, 1+ = mandatory).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "maxCardinality";
        final String attribute3Description     = "Maximum number of occurrences of this attribute allowed (-1 = infinite).";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "uniqueValues";
        final String attribute4Description     = "When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "orderedValues";
        final String attribute5Description     = "When multiple occurrences are allowed, indicates whether the values are ordered or not.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "navigable";
        final String attribute6Description     = "Is it possible to follow the link in this direction.";
        final String attribute6DescriptionGUID = null;


        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute4Name,
                                                            attribute4Description,
                                                            attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute5Name,
                                                            attribute5Description,
                                                            attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute6Name,
                                                            attribute6Description,
                                                            attribute6DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private ClassificationDef getConceptBeadAttributeCoverageClassification()
    {
        final String guid            = "f8b60afe-ddef-4b6f-9628-82ebfff34d65";
        final String name            = "ConceptBeadAttributeCoverage";
        final String description     = "Identifies the coverage category of a concept bead attribute.";
        final String descriptionGUID = null;

        final String linkedToEntity = "ConceptBeadAttribute";

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

        final String attribute1Name            = "coverageCategory";
        final String attribute1Description     = "Type of role that the attribute plays as part of the concept bead.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("ConceptModelAttributeCoverageCategory",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0595DesignPatterns()
    {
        this.archiveBuilder.addEntityDef(getDesignPatternEntity());

        this.archiveBuilder.addRelationshipDef(getRelatedPatternRelationship());

    }


    private EntityDef getDesignPatternEntity()
    {
        final String guid            = "6b60a73e-47bc-4096-9073-f94cab975958";
        final String name            = "DesignPattern";
        final String description     = "A description of a common solution with details of the problems it solves and its pros and cons.";
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

        final String attribute1Name            = "context";
        final String attribute1Description     = "Description of the situation where this pattern may be useful.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "forces";
        final String attribute2Description     = "Description of the aspects of the situation that make the problem hard to solve.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "problemStatement";
        final String attribute3Description     = "Description of the types of problem that this design pattern provides a solution to.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "problemExample";
        final String attribute4Description     = "One or more examples of the problem and its consequences.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "solutionDescription";
        final String attribute5Description     = "Description of how the solution works.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "solutionExample";
        final String attribute6Description     = "Illustrations of how the solution resolves the problem examples.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "benefits";
        final String attribute7Description     = "The positive outcomes from using this pattern.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "liabilities";
        final String attribute8Description     = "The additional issues that need to be considered when using this pattern.";
        final String attribute8DescriptionGUID = null;

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
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute7Name,
                                                                attribute7Description,
                                                                attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute8Name,
                                                                attribute8Description,
                                                                attribute8DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getRelatedPatternRelationship()
    {
        final String guid            = "6447c9cd-8e5a-461b-97f9-5151bcb97a9e";
        final String name            = "RelatedDesignPattern";
        final String description     = "Links design patterns together.";
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
        final String                     end1EntityType               = "DesignPattern";
        final String                     end1AttributeName            = "relatedDesignPattern";
        final String                     end1AttributeDescription     = "Another design pattern that operates in similar contexts.";
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
        final String                     end2EntityType               = "DesignPattern";
        final String                     end2AttributeName            = "relatedDesignPattern";
        final String                     end2AttributeDescription     = "Another design pattern that operates in similar contexts.";
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private void add0598LineageRelationships()
    {

        this.archiveBuilder.addRelationshipDef(getPortSchemaRelationship());
        this.archiveBuilder.addRelationshipDef(getLineageMappingRelationship());
    }

    /**
     * The PortSchema relationship describes the link between a Port and the SchemaType linked to the Port
     * @return PortSchema RelationshipDef
     */
    private RelationshipDef getPortSchemaRelationship()
    {
        /*
         * Build the relationship
         */
        final String guid            = "B216fA00-8281-F9CC-9911-Ae6377f2b457";
        final String name            = "PortSchema";
        final String description     = "A link between a Port and a SchemaType";
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
        final String                     end1EntityType               = "Port";
        final String                     end1AttributeName            = "port";
        final String                     end1AttributeDescription     = "Port";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "schemaType";
        final String                     end2AttributeDescription     = "Schema Type";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }

    /**
     *  The LineageMapping relationship describes the directional mapping between SchemaTypes
     * @return LineageMapping RelationshipDef
     */
    private RelationshipDef getLineageMappingRelationship()
    {
        /*
         * Build the relationship
         */
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.LINEAGE_MAPPING,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "sourceElement";
        final String                     end1AttributeDescription     = "Source Attribute.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "targetElement";
        final String                     end2AttributeDescription     = "Target Attribute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }


    /*
     * ========================================
     * AREA 6: metadata discovery
     */

    /**
     * Area 6 are the types for the discovery server (and discovery pipelines
     * supporting the Open Discovery Framework (ODF)).
     */
    private void addArea6Types()
    {
        this.add0601OpenDiscoveryEngine();
        this.add0602OpenDiscoveryServices();
        this.add0603OpenDiscoveryPipelines();
        this.add0605OpenDiscoveryAnalysisReports();
        this.add0610Annotations();
        this.add0612AnnotationReviews();
        this.add0615SchemaExtraction();
        this.add0617DataFieldAnalysis();
        this.add0620DataProfiling();
        this.add0625DataClassDiscovery();
        this.add0630SemanticDiscovery();
        this.add0635ClassificationDiscovery();
        this.add0640QualityScores();
        this.add0650RelationshipDiscovery();
        this.add0660ResourceMeasures();
        this.add0690RequestForAction();

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0601 Metadata Discovery Server defines how a metadata discovery server is represented in the metadata repository.
     */
    private void add0601OpenDiscoveryEngine()
    {
        this.archiveBuilder.addEntityDef(getOpenDiscoveryEngineEntity());
    }


    private EntityDef getOpenDiscoveryEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.OPEN_DISCOVERY_ENGINE.typeGUID,
                                                 OpenMetadataType.OPEN_DISCOVERY_ENGINE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                 OpenMetadataType.OPEN_DISCOVERY_ENGINE.description,
                                                 OpenMetadataType.OPEN_DISCOVERY_ENGINE.descriptionGUID,
                                                 OpenMetadataType.OPEN_DISCOVERY_ENGINE.wikiURL);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0602 Open Discovery Services defines information about a specific implementation of an Open Discovery
     * Service.  This is a pluggable component of the Open Discovery Framework (ODF).
     */
    private void add0602OpenDiscoveryServices()
    {
        this.archiveBuilder.addEntityDef(getOpenDiscoveryServiceEntity());
    }


    private EntityDef getOpenDiscoveryServiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.OPEN_DISCOVERY_SERVICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0603 Open Discovery Pipelines defines information about a specific implementation of an Open Discovery
     * Pipeline.  This is a specialized open discovery service that executes other open discovery services.
     */
    private void add0603OpenDiscoveryPipelines()
    {
        this.archiveBuilder.addEntityDef(getOpenDiscoveryPipelineEntity());
    }


    private EntityDef getOpenDiscoveryPipelineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.OPEN_DISCOVERY_PIPELINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_DISCOVERY_SERVICE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0605 Discovery Analysis Reports defines hoe the annotations from a discovery service are grouped
     * together and connected to the Assets they refer to.
     */
    private void add0605OpenDiscoveryAnalysisReports()
    {
        this.archiveBuilder.addEnumDef(getDiscoveryRequestStatusEnum());

        this.archiveBuilder.addEntityDef(getOpenDiscoveryAnalysisReportEntity());

        this.archiveBuilder.addRelationshipDef(getDiscoveryEngineReportRelationship());
        this.archiveBuilder.addRelationshipDef(getDiscoveryInvocationReportRelationship());
        this.archiveBuilder.addRelationshipDef(getAssetDiscoveryReportRelationship());
    }


    private EnumDef getDiscoveryRequestStatusEnum()
    {
        final String guid            = "ecb48ca2-4d29-4de9-99a1-bc4db9816d68";
        final String name            = "DiscoveryRequestStatus";
        final String description     = "Defines the progress or completion of a discovery request.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Waiting";
        final String element1Description     = "Discovery request is waiting to execute.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);
        enumDef.setDefaultValue(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "InProgress";
        final String element2Description     = "Discovery request is executing.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "Failed";
        final String element3Description     = "Discovery request has failed.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 3;
        final String element4Value           = "Completed";
        final String element4Description     = "Discovery request has completed successfully.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element99Ordinal         = 99;
        final String element99Value           = "Unknown";
        final String element99Description     = "Discovery request status is unknown.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getOpenDiscoveryAnalysisReportEntity()
    {
        final String guid = "acc7cbc8-09c3-472b-87dd-f78459323dcb";

        final String name            = "OpenDiscoveryAnalysisReport";
        final String description     = "A set of results from an open discovery service.";
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

        final String attribute3Name            = "executionDate";
        final String attribute3Description     = "Date that the analysis was run.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "analysisParameters";
        final String attribute4Description     = "Additional parameters used to drive the analysis.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "discoveryRequestStatus";
        final String attribute5Description     = "Status of the discovery analysis show in the report.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute4Name,
                                                                    attribute4Description,
                                                                    attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("DiscoveryRequestStatus",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getDiscoveryEngineReportRelationship()
    {
        final String guid = "2c318c3a-5dc2-42cd-a933-0087d852f67f";

        final String name            = "DiscoveryEngineReport";
        final String description     = "A discovery analysis report created by a discovery engine.";
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
        final String                     end1EntityType               = "OpenDiscoveryEngine";
        final String                     end1AttributeName            = "sourceDiscoveryEngine";
        final String                     end1AttributeDescription     = "The discovery engine that produced the report.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "OpenDiscoveryAnalysisReport";
        final String                     end2AttributeName            = "discoveryEngineAnalysisReports";
        final String                     end2AttributeDescription     = "The reports produced by this discovery engine.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDiscoveryInvocationReportRelationship()
    {
        final String guid = "1744d72b-903d-4273-9229-de20372a17e2";

        final String name            = "DiscoveryInvocationReport";
        final String description     = "An analysis report from a discovery service.";
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
        final String                     end1EntityType               = "OpenDiscoveryService";
        final String                     end1AttributeName            = "sourceDiscoveryService";
        final String                     end1AttributeDescription     = "The discovery service that produced the report.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "OpenDiscoveryAnalysisReport";
        final String                     end2AttributeName            = "serviceDiscoveryAnalysisReports";
        final String                     end2AttributeDescription     = "The reports produced by this discovery service.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getAssetDiscoveryReportRelationship()
    {
        final String guid = "7eded424-f176-4258-9ae6-138a46b2845f";

        final String name            = "AssetDiscoveryReport";
        final String description     = "An analysis report from a discovery service.";
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
        final String                     end1AttributeName            = "discoveryReportTarget";
        final String                     end1AttributeDescription     = "The asset that is analyzed in the report.";
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
        final String                     end2EntityType               = "OpenDiscoveryAnalysisReport";
        final String                     end2AttributeName            = "assetDiscoveryAnalysisReports";
        final String                     end2AttributeDescription     = "The reports produced about this asset.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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
     * 0610 Annotations describes the basic structure of an annotation created by a discovery service.
     */
    private void add0610Annotations()
    {
        this.archiveBuilder.addEntityDef(getAnnotationEntity());

        this.archiveBuilder.addRelationshipDef(getDiscoveredAnnotationRelationship());
        this.archiveBuilder.addRelationshipDef(getAnnotationExtensionRelationship());
    }


    private EntityDef getAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ANNOTATION,
                                                                null);
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ANNOTATION_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.ANNOTATION_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ANNOTATION_TYPE.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.CONFIDENCE_LEVEL.name;
        final String attribute3Description     = OpenMetadataProperty.CONFIDENCE_LEVEL.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.CONFIDENCE_LEVEL.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.EXPRESSION.name;
        final String attribute4Description     = OpenMetadataProperty.EXPRESSION.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.EXPRESSION.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.EXPLANATION.name;
        final String attribute5Description     = OpenMetadataProperty.EXPLANATION.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.EXPLANATION.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.ANALYSIS_STEP.name;
        final String attribute6Description     = OpenMetadataProperty.ANALYSIS_STEP.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.ANALYSIS_STEP.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.JSON_PROPERTIES.name;
        final String attribute7Description     = OpenMetadataProperty.JSON_PROPERTIES.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.JSON_PROPERTIES.descriptionGUID;
        final String attribute9Name            = OpenMetadataProperty.ANCHOR_GUID.name;
        final String attribute9Description     = OpenMetadataProperty.ANCHOR_GUID.description;
        final String attribute9DescriptionGUID = OpenMetadataProperty.ANCHOR_GUID.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SUMMARY.name,
                                                           OpenMetadataProperty.SUMMARY.description,
                                                           OpenMetadataProperty.SUMMARY.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.description,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute9Name,
                                                           attribute9Description,
                                                           attribute9DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getDiscoveredAnnotationRelationship()
    {
        final String guid            = "51d386a3-3857-42e3-a3df-14a6cad08b93";
        final String name            = "DiscoveredAnnotation";
        final String description     = "The annotations that make up a discovery analysis report.";
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
        final String                     end1AttributeName            = "reportedAnnotations";
        final String                     end1AttributeDescription     = "The annotations providing the contents for the report.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "OpenDiscoveryAnalysisReport";
        final String                     end2AttributeName            = "fromAnalysisReport";
        final String                     end2AttributeDescription     = "The report that the annotations belong to.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getAnnotationExtensionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "extendedAnnotations";
        final String                     end1AttributeDescription     = "The annotations being extended.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "annotationExtensions";
        final String                     end2AttributeDescription     = "The annotations providing additional information.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName),
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
     * 0612 Annotation Review defines the outcome of a stewardship review of the annotations in a discovery
     * analysis report.
     */
    private void add0612AnnotationReviews()
    {
        this.archiveBuilder.addEnumDef(getAnnotationStatusEnum());

        this.archiveBuilder.addEntityDef(getAnnotationReviewEntity());

        this.archiveBuilder.addRelationshipDef(getAnnotationReviewLinkRelationship());
    }


    private EnumDef getAnnotationStatusEnum()
    {
        final String guid            = AnnotationStatus.getOpenTypeGUID();
        final String name            = AnnotationStatus.getOpenTypeName();
        final String description     = AnnotationStatus.getOpenTypeDescription();
        final String descriptionGUID = AnnotationStatus.getOpenTypeDescriptionGUID();
        final String descriptionWiki = AnnotationStatus.getOpenTypeWikiURL();

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID, descriptionWiki);

        List<EnumElementDef> elementDefs = new ArrayList<>();

        for (AnnotationStatus annotationStatus : AnnotationStatus.values())
        {
            elementDefs.add(archiveHelper.getEnumElementDef(annotationStatus.getOrdinal(),
                                                            annotationStatus.getName(),
                                                            annotationStatus.getDescription(),
                                                            annotationStatus.getDescriptionGUID()));
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private EntityDef getAnnotationReviewEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ANNOTATION_REVIEW,
                                                                null);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.REVIEW_DATE.name;
        final String attribute1Description     = OpenMetadataProperty.REVIEW_DATE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.REVIEW_DATE.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.COMMENT.name;
        final String attribute3Description     = OpenMetadataProperty.COMMENT.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.COMMENT.descriptionGUID;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getAnnotationReviewLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "reviewedAnnotations";
        final String                     end1AttributeDescription     = "The annotations being reviewed.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "annotationReviews";
        final String                     end2AttributeDescription     = "The feedback about the annotations.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION_REVIEW.typeName),
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

        final String attribute1Name            = OpenMetadataProperty.ANNOTATION_STATUS.name;
        final String attribute1Description     = OpenMetadataProperty.ANNOTATION_STATUS.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ANNOTATION_STATUS.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("AnnotationStatus",
                                                         attribute1Name,
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
     * 0615 Schema Extraction creates an annotation to describe a nested schema.
     */
    private void add0615SchemaExtraction()
    {
        this.archiveBuilder.addEntityDef(getSchemaAnalysisAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDataFieldEntity());

        this.archiveBuilder.addRelationshipDef(getSchemaTypeDefinitionRelationship());
        this.archiveBuilder.addRelationshipDef(getDiscoveredDataFieldRelationship());
        this.archiveBuilder.addRelationshipDef(getSchemaAttributeDefinitionRelationship());
        this.archiveBuilder.addRelationshipDef(getDiscoveredNestedDataFieldRelationship());
        this.archiveBuilder.addRelationshipDef(getDataClassDefinitionRelationship());
    }

    private EntityDef getSchemaAnalysisAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.SCHEMA_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.SCHEMA_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.SCHEMA_NAME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.SCHEMA_TYPE.name;
        final String attribute2Description     = OpenMetadataProperty.SCHEMA_TYPE.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.SCHEMA_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDataFieldEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_FIELD.typeGUID,
                                                                OpenMetadataType.DATA_FIELD.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                OpenMetadataType.DATA_FIELD.description,
                                                                OpenMetadataType.DATA_FIELD.descriptionGUID,
                                                                OpenMetadataType.DATA_FIELD.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "dataFieldName";
        final String attribute1Description     = "Display name the data field.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "dataFieldType";
        final String attribute2Description     = "Type name for the data field.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "defaultValue";
        final String attribute3Description     = "Default value that is added to the field if no value is specified.";
        final String attribute3DescriptionGUID = null;
        final String attribute6Name            = "dataFieldDescription";
        final String attribute6Description     = "Optional descriptive information about a data field.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "dataFieldAliases";
        final String attribute7Description     = "Optional list of aliases for the data field.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "dataFieldSortOrder";
        final String attribute8Description     = "Sort order for the values of the data field.";
        final String attribute8DescriptionGUID = null;

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
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute7Name,
                                                                attribute7Description,
                                                                attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("DataItemSortOrder",
                                                         attribute8Name,
                                                         attribute8Description,
                                                         attribute8DescriptionGUID);
        properties.add(property);
        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getSchemaTypeDefinitionRelationship()
    {
        final String guid            = "60f2d263-e24d-4f20-8c0d-b5e24648cd54";
        final String name            = "SchemaTypeDefinition";
        final String description     = "Link between schema analysis annotation and the identified schema type definition.";
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
        final String                     end1AttributeName            = "deployedSchemaTypes";
        final String                     end1AttributeDescription     = "The analysis of the schema type for deployed assets.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "schemaTypeDefinition";
        final String                     end2AttributeDescription     = "Official schema type definition.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDiscoveredDataFieldRelationship()
    {
        final String guid            = OpenMetadataType.DISCOVERED_DATA_FIELD_TYPE_GUID;
        final String name            = OpenMetadataType.DISCOVERED_DATA_FIELD_TYPE_NAME;
        final String description     = "Data field detected in asset during schema analysis.";
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
        final String                     end1AttributeName            = "schemaAnalysisAnnotation";
        final String                     end1AttributeDescription     = "The annotation collecting the results of the schema analysis.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "discoveredDataFields";
        final String                     end2AttributeDescription     = "The data fields discovered during schema analysis.";
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
        TypeDefAttribute       property;

        final String attribute1Name            = "dataFieldPosition";
        final String attribute1Description     = "Location of the data field in the parent annotation's list of data fields.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getSchemaAttributeDefinitionRelationship()
    {
        final String guid            = "60f1e263-e24d-4f20-8c0d-b5e21232cd54";
        final String name            = "SchemaAttributeDefinition";
        final String description     = "Link between data field analysis and the identified schema attribute definition.";
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
        final String                     end1AttributeName            = "deployedSchemaAttributes";
        final String                     end1AttributeDescription     = "The analysis of the equivalent data fields from deployed assets.";
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
        final String                     end2AttributeName            = "schemaAttributeDefinition";
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


    private RelationshipDef getDiscoveredNestedDataFieldRelationship()
    {
        final String guid            = OpenMetadataType.NESTED_DATA_FIELD_TYPE_GUID;
        final String name            = OpenMetadataType.NESTED_DATA_FIELD_TYPE_NAME;
        final String description     = "Nested data fields under a single parent node.";
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
        final String                     end1AttributeName            = "parentDataField";
        final String                     end1AttributeDescription     = "Parent node.";
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
        TypeDefAttribute       property;

        final String attribute1Name            = "dataFieldPosition";
        final String attribute1Description     = "Positional order of the data field with its parent data field.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getDataClassDefinitionRelationship()
    {
        final String guid = "51a2d263-e24d-4f20-8c0d-b5e12356cd54";

        final String name            = "DataClassDefinition";
        final String description     = "Link between schema analysis and the identified data class for a data field.";
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
        final String                     end1AttributeName            = "deployedDataClasses";
        final String                     end1AttributeDescription     = "The mapped data fields from deployed assets.";
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
        final String                     end2AttributeName            = "dataClassDefinition";
        final String                     end2AttributeDescription     = "Official data class definition.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_CLASS.typeName),
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

        final String attribute1Name            = "assetGUID";
        final String attribute1Description     = "Unique identifier for the analyzed asset.";
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
     * 0617 Data Field Analysis defines the structures to record the individual data fields found in an asset.
     */
    private void add0617DataFieldAnalysis()
    {
        this.archiveBuilder.addEntityDef(getDataFieldAnnotationEntity());

        this.archiveBuilder.addRelationshipDef(getDataFieldAnalysisRelationship());
    }


    private EntityDef getDataFieldAnnotationEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));
    }


    private RelationshipDef getDataFieldAnalysisRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_FIELD_ANALYSIS_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dataFieldAnnotations";
        final String                     end1AttributeDescription     = "The annotations for this data field.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "annotatedDataFields";
        final String                     end2AttributeDescription     = "Data fields with addition properties attached.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD.typeName),
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
     * 0620 Profiling describes an annotation that can be attached to a column to describe the profile of its
     * values.
     */
    private void add0620DataProfiling()
    {
        this.archiveBuilder.addEntityDef(getDataProfileAnnotationEntity());
        this.archiveBuilder.addEntityDef(getResourceProfileLogAnnotationEntity());

        this.archiveBuilder.addRelationshipDef(getResourceProfileLogFileRelationship());

    }


    private EntityDef getDataProfileAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RESOURCE_PROFILE_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.LENGTH.name;
        final String attribute1Description     = OpenMetadataProperty.LENGTH.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.LENGTH.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.INFERRED_DATA_TYPE.name;
        final String attribute2Description     = OpenMetadataProperty.INFERRED_DATA_TYPE.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.INFERRED_DATA_TYPE.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.INFERRED_FORMAT.name;
        final String attribute3Description     = OpenMetadataProperty.INFERRED_FORMAT.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.INFERRED_FORMAT.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.INFERRED_LENGTH.name;
        final String attribute4Description     = OpenMetadataProperty.INFERRED_LENGTH.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.INFERRED_LENGTH.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.INFERRED_PRECISION.name;
        final String attribute5Description     = OpenMetadataProperty.INFERRED_PRECISION.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.INFERRED_PRECISION.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.INFERRED_SCALE.name;
        final String attribute6Description     = OpenMetadataProperty.INFERRED_SCALE.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.INFERRED_SCALE.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.PROFILE_PROPERTIES.name;
        final String attribute7Description     = OpenMetadataProperty.PROFILE_PROPERTIES.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.PROFILE_PROPERTIES.descriptionGUID;
        final String attribute8Name            = OpenMetadataProperty.PROFILE_FLAGS.name;
        final String attribute8Description     = OpenMetadataProperty.PROFILE_FLAGS.description;
        final String attribute8DescriptionGUID = OpenMetadataProperty.PROFILE_FLAGS.descriptionGUID;
        final String attribute9Name            = OpenMetadataProperty.PROFILE_COUNTS.name;
        final String attribute9Description     = OpenMetadataProperty.PROFILE_COUNTS.description;
        final String attribute9DescriptionGUID = OpenMetadataProperty.PROFILE_COUNTS.descriptionGUID;
        final String attribute10Name            = OpenMetadataProperty.VALUE_LIST.name;
        final String attribute10Description     = OpenMetadataProperty.VALUE_LIST.description;
        final String attribute10DescriptionGUID = OpenMetadataProperty.VALUE_LIST.descriptionGUID;
        final String attribute11Name            = OpenMetadataProperty.VALUE_COUNT.name;
        final String attribute11Description     = OpenMetadataProperty.VALUE_COUNT.description;
        final String attribute11DescriptionGUID = OpenMetadataProperty.VALUE_COUNT.descriptionGUID;
        final String attribute12Name            = OpenMetadataProperty.VALUE_RANGE_FROM.name;
        final String attribute12Description     = OpenMetadataProperty.VALUE_RANGE_FROM.description;
        final String attribute12DescriptionGUID = OpenMetadataProperty.VALUE_RANGE_FROM.descriptionGUID;
        final String attribute13Name            = OpenMetadataProperty.VALUE_RANGE_TO.name;
        final String attribute13Description     = OpenMetadataProperty.VALUE_RANGE_TO.description;
        final String attribute13DescriptionGUID = OpenMetadataProperty.VALUE_RANGE_TO.descriptionGUID;
        final String attribute14Name            = OpenMetadataProperty.AVERAGE_VALUE.name;
        final String attribute14Description     = OpenMetadataProperty.AVERAGE_VALUE.description;
        final String attribute14DescriptionGUID = OpenMetadataProperty.AVERAGE_VALUE.descriptionGUID;
        final String attribute15Name            = OpenMetadataProperty.PROFILE_DOUBLES.name;
        final String attribute15Description     = OpenMetadataProperty.PROFILE_DOUBLES.description;
        final String attribute15DescriptionGUID = OpenMetadataProperty.PROFILE_DOUBLES.descriptionGUID;
        final String attribute16Name            = OpenMetadataProperty.PROFILE_START_DATE.name;
        final String attribute16Description     = OpenMetadataProperty.PROFILE_START_DATE.description;
        final String attribute16DescriptionGUID = OpenMetadataProperty.PROFILE_START_DATE.descriptionGUID;
        final String attribute17Name            = OpenMetadataProperty.PROFILE_END_DATE.name;
        final String attribute17Description     = OpenMetadataProperty.PROFILE_END_DATE.description;
        final String attribute17DescriptionGUID = OpenMetadataProperty.PROFILE_END_DATE.descriptionGUID;
        final String attribute18Name            = OpenMetadataProperty.PROFILE_DATES.name;
        final String attribute18Description     = OpenMetadataProperty.PROFILE_DATES.description;
        final String attribute18DescriptionGUID = OpenMetadataProperty.PROFILE_DATES.descriptionGUID;
        final String attribute19Name            = OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name;
        final String attribute19Description     = OpenMetadataProperty.PROFILE_PROPERTY_NAMES.description;
        final String attribute19DescriptionGUID = OpenMetadataProperty.PROFILE_PROPERTY_NAMES.descriptionGUID;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
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
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute5Name,
                                                        attribute5Description,
                                                        attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute6Name,
                                                        attribute6Description,
                                                        attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute7Name,
                                                                    attribute7Description,
                                                                    attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute8Name,
                                                                     attribute8Description,
                                                                     attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringLongTypeDefAttribute(attribute9Name,
                                                                  attribute9Description,
                                                                  attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute10Name,
                                                                attribute10Description,
                                                                attribute10DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute11Name,
                                                                 attribute11Description,
                                                                 attribute11DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute12Name,
                                                           attribute12Description,
                                                           attribute12DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute13Name,
                                                           attribute13Description,
                                                           attribute13DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute14Name,
                                                           attribute14Description,
                                                           attribute14DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringDoubleTypeDefAttribute(attribute15Name,
                                                                    attribute15Description,
                                                                    attribute15DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute16Name,
                                                         attribute16Description,
                                                         attribute16DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute17Name,
                                                         attribute17Description,
                                                         attribute17DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringDateTypeDefAttribute(attribute18Name,
                                                                  attribute18Description,
                                                                  attribute18DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute19Name,
                                                                attribute19Description,
                                                                attribute19DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getResourceProfileLogAnnotationEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));
    }


    private RelationshipDef getResourceProfileLogFileRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "resourceProfileAnnotations";
        final String                     end1AttributeDescription     = "The annotations that refer to this log file.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "resourceProfileLogs";
        final String                     end2AttributeDescription     = "Location of the profile information.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
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
     * 0625 Data Class Discovery records potential data classes that match the data field, and the level of
     * error in the match.
     */
    private void add0625DataClassDiscovery()
    {
        this.archiveBuilder.addEntityDef(getDataClassAnnotationEntity());
    }


    private EntityDef getDataClassAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_CLASS_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name;
        final String attribute1Description     = OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.MATCHING_VALUES.name;
        final String attribute2Description     = OpenMetadataProperty.MATCHING_VALUES.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.MATCHING_VALUES.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.NON_MATCHING_VALUES.name;
        final String attribute3Description     = OpenMetadataProperty.NON_MATCHING_VALUES.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.NON_MATCHING_VALUES.descriptionGUID;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0630 Semantic Discovery describes an annotation for a candidate glossary term assignment for a data element.
     */
    private void add0630SemanticDiscovery()
    {
        this.archiveBuilder.addEntityDef(getSemanticAnnotationEntity());
    }

    private EntityDef getSemanticAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SEMANTIC_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.INFORMAL_TERM.name;
        final String attribute1Description     = OpenMetadataProperty.INFORMAL_TERM.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.INFORMAL_TERM.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name;
        final String attribute2Description     = OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.INFORMAL_CATEGORY.name;
        final String attribute3Description     = OpenMetadataProperty.INFORMAL_CATEGORY.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.INFORMAL_CATEGORY.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name;
        final String attribute4Description     = OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
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

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0635 Classification Discovery creates a base class for a classification annotation.
     */
    private void add0635ClassificationDiscovery()
    {
        this.archiveBuilder.addEntityDef(getClassificationAnnotationEntity());
    }


    private EntityDef getClassificationAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CLASSIFICATION_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name;
        final String attribute1Description     = OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.descriptionGUID;

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute1Name,
                                                                    attribute1Description,
                                                                    attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0640 Quality Scores records quality analysis results.  Quality has many different dimensions and
     * the dimension that this score reflects is also recorded with the score.
     */
    private void add0640QualityScores()
    {
        this.archiveBuilder.addEntityDef(getQualityAnnotationEntity());
    }


    private EntityDef getQualityAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.QUALITY_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.QUALITY_DIMENSION.name;
        final String attribute1Description     = OpenMetadataProperty.QUALITY_DIMENSION.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.QUALITY_DIMENSION.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.QUALITY_SCORE.name;
        final String attribute2Description     = OpenMetadataProperty.QUALITY_SCORE.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.QUALITY_SCORE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0650 Relationship Discovery provides a base annotation for describing a relationship between two referenceables.
     */
    private void add0650RelationshipDiscovery()
    {
        this.archiveBuilder.addEntityDef(getRelationshipAdviceAnnotationEntity());

        this.archiveBuilder.addRelationshipDef(getRelationshipAnnotationRelationship());
    }


    private EntityDef getRelationshipAdviceAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name;
        final String attribute2Description     = OpenMetadataProperty.RELATIONSHIP_PROPERTIES.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.RELATIONSHIP_PROPERTIES.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.RELATED_ENTITY_GUID.name;
        final String attribute3Description     = OpenMetadataProperty.RELATED_ENTITY_GUID.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.RELATED_ENTITY_GUID.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getRelationshipAnnotationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.RELATIONSHIP_ANNOTATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "relatedFromObjectAnnotations";
        final String                     end1AttributeDescription     = "The referenceables linked from.";
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
        final String                     end2AttributeName            = "relatedToObjectAnnotations";
        final String                     end2AttributeDescription     = "The referenceables linked to.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
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

        final String attribute1Name            = OpenMetadataProperty.ANNOTATION_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.ANNOTATION_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ANNOTATION_TYPE.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.CONFIDENCE_LEVEL.name;
        final String attribute3Description     = OpenMetadataProperty.CONFIDENCE_LEVEL.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.CONFIDENCE_LEVEL.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.EXPRESSION.name;
        final String attribute4Description     = OpenMetadataProperty.EXPRESSION.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.EXPRESSION.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.EXPLANATION.name;
        final String attribute5Description     = OpenMetadataProperty.EXPLANATION.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.EXPLANATION.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.ANALYSIS_STEP.name;
        final String attribute6Description     = OpenMetadataProperty.ANALYSIS_STEP.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.ANALYSIS_STEP.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.JSON_PROPERTIES.name;
        final String attribute7Description     = OpenMetadataProperty.JSON_PROPERTIES.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.JSON_PROPERTIES.descriptionGUID;
        final String attribute9Name            = OpenMetadataProperty.ANNOTATION_STATUS.name;
        final String attribute9Description     = OpenMetadataProperty.ANNOTATION_STATUS.description;
        final String attribute9DescriptionGUID = OpenMetadataProperty.ANNOTATION_STATUS.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SUMMARY.name,
                                                           OpenMetadataProperty.SUMMARY.description,
                                                           OpenMetadataProperty.SUMMARY.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.description,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("AnnotationStatus",
                                                         attribute9Name,
                                                         attribute9Description,
                                                         attribute9DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0660 Data Source Measurements describe annotations that are measuring characteristics of a data source.
     * This may be a DataSet, DataFeed or DataSource.
     */
    private void add0660ResourceMeasures()
    {
        this.archiveBuilder.addEntityDef(getResourceMeasureAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDataSourcePhysicalStatusAnnotationEntity());
    }


    private EntityDef getResourceMeasureAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.RESOURCE_PROPERTIES.name;
        final String attribute1Description     = OpenMetadataProperty.RESOURCE_PROPERTIES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.RESOURCE_PROPERTIES.descriptionGUID;

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute1Name,
                                                                    attribute1Description,
                                                                    attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDataSourcePhysicalStatusAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName));
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.RESOURCE_CREATE_TIME.name;
        final String attribute1Description     = OpenMetadataProperty.RESOURCE_CREATE_TIME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.RESOURCE_CREATE_TIME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.RESOURCE_UPDATE_TIME.name;
        final String attribute2Description     = OpenMetadataProperty.RESOURCE_UPDATE_TIME.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.RESOURCE_UPDATE_TIME.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.SIZE.name;
        final String attribute3Description     = OpenMetadataProperty.SIZE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.SIZE.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.ENCODING.name;
        final String attribute4Description     = OpenMetadataProperty.ENCODING.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.ENCODING.descriptionGUID;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0690 Request for Action creates an annotation for requesting a stewardship or governance action.
     */
    private void add0690RequestForAction()
    {
        this.archiveBuilder.addEntityDef(getRequestForActionAnnotationEntity());
    }


    private EntityDef getRequestForActionAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ACTION_SOURCE_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.ACTION_SOURCE_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ACTION_SOURCE_NAME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.ACTION_REQUESTED.name;
        final String attribute2Description     = OpenMetadataProperty.ACTION_REQUESTED.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.ACTION_REQUESTED.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.ACTION_PROPERTIES.name;
        final String attribute3Description     = OpenMetadataProperty.ACTION_PROPERTIES.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.ACTION_PROPERTIES.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }
}
