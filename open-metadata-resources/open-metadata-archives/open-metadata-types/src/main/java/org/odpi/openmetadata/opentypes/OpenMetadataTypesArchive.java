/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
 * <a href="https://egeria.odpi.org/open-metadata-publication/website/open-metadata-types/">The Open Metadata Type System</a>
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
    private static final String                  archiveVersion     = "1.6";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache 2.0";
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
            OpenMetadataTypesArchive1_5  previousTypes = new OpenMetadataTypesArchive1_5(archiveBuilder);

            /*
             * Call each of the methods to systematically add the contents of the archive.
             * The original types are added first.
             */
            previousTypes.getOriginalTypes();

            /*
             * Calls for new types go here
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
        update0501SchemaElements();
        update0505SchemaAttributes();
        update0512DerivedSchemaAttributes();
    }


    private void update0501SchemaElements()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaElementEntity());
    }


    /**
     * 0501 - SchemaElement entity is changed to allow a schema element to be marked as deprecated.
     */
    private TypeDefPatch updateSchemaElementEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaElement";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "isDeprecated";
        final String attribute1Description     = "This element may still be used but is flagged that it will be removed at some point in the future.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private void update0505SchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaAttributeEntity());
        this.archiveBuilder.addTypeDefPatch(updateTypeEmbeddedAttributeClassification());
    }


    /**
     * 0505 - SchemaAttribute entity is changed to show deprecated properties
     */
    private TypeDefPatch updateSchemaAttributeEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaAttribute";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "minimumLength";
        final String attribute1Description     = "Minimum length of the data value (zero means unlimited).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "length";
        final String attribute2Description     = "Length of the data field (zero means unlimited).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "significantDigits";
        final String attribute3Description     = "Number of significant digits to the right of decimal point (zero means it is an integer).";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "isNullable";
        final String attribute4Description     = "Accepts null values or not.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = "cardinality";
        final String attribute6Description     = "Number of occurrences of this attribute allowed (deprecated).";
        final String attribute6DescriptionGUID = null;
        final String attribute6ReplacedBy      = "maxCardinality";
        final String attribute7Name            = "name";
        final String attribute7Description     = "Name of schema attribute (deprecated).";
        final String attribute7DescriptionGUID = null;
        final String attribute7ReplacedBy      = "displayName";

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

        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        property.setReplacedByAttribute(attribute6ReplacedBy);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        property.setReplacedByAttribute(attribute7ReplacedBy);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * 0505 - TypeEmbeddedAttribute classification is changed to include more values of schema type
     */
    private TypeDefPatch updateTypeEmbeddedAttributeClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "TypeEmbeddedAttribute";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name             = "schemaTypeName";
        final String attribute1Description      = "Type name for the schema type.";
        final String attribute1DescriptionGUID  = null;
        final String attribute2Name             = "qualifiedName";
        final String attribute2Description      = "Unique name for the schema type.";
        final String attribute2DescriptionGUID  = null;
        final String attribute3Name             = "displayName";
        final String attribute3Description      = "Display name for the schema type.";
        final String attribute3DescriptionGUID  = null;
        final String attribute4Name             = "description";
        final String attribute4Description      = "Description of the schema type.";
        final String attribute4DescriptionGUID  = null;
        final String attribute5Name             = "versionNumber";
        final String attribute5Description      = "Version of the schema type.";
        final String attribute5DescriptionGUID  = null;
        final String attribute6Name             = "author";
        final String attribute6Description      = "User name of the person or process that created the schema type.";
        final String attribute6DescriptionGUID  = null;
        final String attribute7Name             = "usage";
        final String attribute7Description      = "Guidance on how the schema should be used.";
        final String attribute7DescriptionGUID  = null;
        final String attribute8Name             = "defaultValue";
        final String attribute8Description      = "Initial value for data stored in this schema type (primitive and enum types).";
        final String attribute8DescriptionGUID  = null;
        final String attribute9Name             = "fixedValue";
        final String attribute9Description      = "Fixed value for data stored in this schema type (literal schema type).";
        final String attribute9DescriptionGUID  = null;
        final String attribute10Name            = "additionalProperties";
        final String attribute10Description     = "Additional properties for the schema type.";
        final String attribute10DescriptionGUID = null;
        final String attribute11Name            = "isDeprecated";
        final String attribute11Description     = "This element may still be used but is flagged that it will be removed at some point in the " +
                "future.";
        final String attribute11DescriptionGUID = null;

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
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute8Name,
                                                           attribute8Description,
                                                           attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute9Name,
                                                           attribute9Description,
                                                           attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute10Name,
                                                                    attribute10Description,
                                                                    attribute10DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute11Name,
                                                            attribute11Description,
                                                            attribute11DescriptionGUID);
        properties.add(property);
        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    private void update0512DerivedSchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateDerivedSchemaAttributeEntity());
    }

    /**
     * 0512 - SchemaAttribute entity is changed to show deprecated properties
     */
    private TypeDefPatch updateDerivedSchemaAttributeEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DerivedSchemaAttribute";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "comment";
        final String attribute1Description     = "Comment from source system (deprecated).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "id";
        final String attribute2Description     = "Id of derived schema attribute (deprecated).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "aggregatingFunction";
        final String attribute3Description     = "Aggregating function of derived schema attribute (deprecated).";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * 0534 - replace fraction with significantDigit in SchemaAttribute
     */
    private TypeDefPatch updateRelationalColumnEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DerivedSchemaAttribute";

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
     * ========================================================================
     * Below are place holders for types to be introduced in future releases.
     * ========================================================================
     */

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0575ProcessSchemas()
    {
        /* placeholder */
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 580 Solution Blueprints enable the recording of solution component models
     */
    private void add580SolutionBlueprints()
    {
        this.archiveBuilder.addEntityDef(getSolutionBlueprintEntity());
        this.archiveBuilder.addEntityDef(getSolutionBlueprintTemplateEntity());
        this.archiveBuilder.addEntityDef(getNestedSolutionBlueprintEntity());

        this.archiveBuilder.addRelationshipDef(getSolutionTypeRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionBlueprintComponentRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionBlueprintHierarchyRelationship());
    }


    private EntityDef getSolutionBlueprintEntity()
    {
        final String guid = "4aa47799-5128-4eeb-bd72-e357b49f8bfe";

        // TODO
        return null;
    }


    private EntityDef getSolutionBlueprintTemplateEntity()
    {
        final String guid = "f671e1fc-b204-4ee6-a4e2-da1633ecf50e";

        // TODO
        return null;
    }


    private EntityDef getNestedSolutionBlueprintEntity()
    {
        final String guid = "b83f3d42-f3f7-4155-ae65-58fb44ea7644";

        // TODO
        return null;
    }


    private RelationshipDef getSolutionTypeRelationship()
    {
        final String guid = "f1ae975f-f11a-467b-8c7a-b023081e4712";

        // TODO
        return null;
    }


    private RelationshipDef getSolutionBlueprintComponentRelationship()
    {
        final String guid = "a43b4c9c-52c2-4819-b3cc-9d07d49a11f2";

        // TODO
        return null;
    }


    private RelationshipDef getSolutionBlueprintHierarchyRelationship()
    {
        final String guid = "2a9e56c3-bcf6-41de-bbe9-1e63b81d3114";

        // TODO
        return null;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0581 Solution Ports and Wires defines how communication ports are connected to the solution components.
     */
    private void add0581SolutionPortsAndWires()
    {
        this.archiveBuilder.addEntityDef(getSolutionPortEntity());

        this.archiveBuilder.addRelationshipDef(getSolutionLinkingWireRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionPortRelationship());
        this.archiveBuilder.addRelationshipDef(getSolutionPortDelegationRelationship());
    }


    private EntityDef getSolutionPortEntity()
    {
        final String guid = "62ef448c-d4c1-4c94-a565-5e5625f6a57b";

        // TODO
        return null;
    }


    private RelationshipDef getSolutionLinkingWireRelationship()
    {
        final String guid = "892a3d1c-cfb8-431d-bd59-c4d38833bfb0";

        // TODO
        return null;
    }


    private RelationshipDef getSolutionPortRelationship()
    {
        final String guid = "5652d03a-f6c9-411a-a3e4-f490d3856b64";

        // TODO
        return null;
    }


    private RelationshipDef getSolutionPortDelegationRelationship()
    {
        final String guid = "8335e6ed-fd86-4000-9bc5-5203062f28ba";

        // TODO
        return null;
    }

    /*
     * ========================================
     * AREA 7: lineage
     */

    /**
     * Add the types for lineage
     */
    private void addArea7Types()
    {
        /*
         * The types for area 7 are not yet defined, this method is a placeholder.
         */
        // TODO

        /* Spare GUIDs

final String guid = "e8303911-ba1c-4640-974e-c4d57ee1b310";
final String guid = "6dfba6ce-e925-4281-880d-d04100c5b991";
final String guid = "91ff7542-c275-4cd3-b367-97eec3360422";
final String guid = "9e187e1e-2547-46bd-b0ee-c33ac6df4a1f";
final String guid = "79ac27f6-be9c-489f-a7c2-b9add0bf705c";
final String guid = "873e29bd-ca14-4833-a6bb-9ebdf89b5b1b";
final String guid = "fa6de61d-98cb-48c4-b21f-ab7186235fd4";
final String guid = "6d9980b2-5c0b-4314-8d8d-9fa45f8904d1";
final String guid = "fcdccfa3-e9f0-4543-8720-1958799fb6dc";
final String guid = "94715275-0520-43e9-81fe-4fe8ec3d8f3a";
final String guid = "d0dd0ac7-01f4-48e0-ae4d-4f7268573fa8";
final String guid = "b472a2ec-f419-4d3f-86fb-e9d97365f961";
final String guid = "9062df4c-9f4a-4012-a67a-968d7a3f4bcf";
final String guid = "685f91fb-c74b-437b-a9b6-c5e557c6d3b2";
final String guid = "7f53928f-9148-4710-ad37-47633f33cb08";
final String guid = "33ec3aaa-dfb6-4f58-8d5d-c42d077be1b3";
final String guid = "0ac0e793-6727-45d2-9403-06bd19d9ce2e";
final String guid = "1dfdec0f-f206-4db7-bac8-ec344205fb3c";
final String guid = "6ad18aa4-f5fc-47e7-99e1-80acfc536c9a";
        */
    }
}

