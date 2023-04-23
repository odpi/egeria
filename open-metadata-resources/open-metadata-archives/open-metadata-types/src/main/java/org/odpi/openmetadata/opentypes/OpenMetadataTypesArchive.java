/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefStatus;
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
    private static final String                  archiveVersion     = "4.1";
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
        OpenMetadataTypesArchive4_0 previousTypes = new OpenMetadataTypesArchive4_0(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0011ManagingReferenceables();
        update0210DataStores();
        update0385ControlledGlossaryDevelopment();
        update0423SecurityAccessControl();
        update504ImplementationSnippets();
        update0710DigitalServices();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0011ManagingReferenceables()
    {
        this.archiveBuilder.addTypeDefPatch(updateSourcedFromRelationship());
    }


    private TypeDefPatch updateSourcedFromRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SourcedFrom";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "sourceVersionNumber";
        final String attribute1Description     = "The version number of the template element when the copy was created.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getLongTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0210DataStores()
    {
        this.archiveBuilder.addClassificationDef(getDataFieldValuesClassification());
    }

    private ClassificationDef getDataFieldValuesClassification()
    {
        final String guid            = "740e76e1-77b4-4426-ad52-d0a4ed15fff9";
        final String name            = "DataFieldValues";
        final String description     = "Characterizations of a collection of data values.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

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

        final String attribute1Name            = "defaultValue";
        final String attribute1Description     = "Value that is used when an instance of the data field is created.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "sampleValues";
        final String attribute2Description     = "List of sample values for the data field.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "dataPattern";
        final String attribute3Description     = "A regular expression that characterizes the values in the data field.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "namePattern";
        final String attribute4Description     = "A regular expression that characterizes the name of the data field.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
                                                                attribute2Description,
                                                                attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute3Name,
                                                                attribute3Description,
                                                                attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute4Name,
                                                                attribute4Description,
                                                                attribute4DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0385ControlledGlossaryDevelopment()
    {
        this.archiveBuilder.addClassificationDef(getEditingGlossaryClassification());
        this.archiveBuilder.addClassificationDef(getEditingGlossaryCopyClassification());
        this.archiveBuilder.addTypeDefPatch(deprecateGlossaryTermEvolution());
        this.archiveBuilder.addTypeDefPatch(updateGlossaryTermEntity());
    }

    private ClassificationDef getEditingGlossaryClassification()
    {
        final String guid            = "173614ba-c582-4ecc-8fcc-cde5fb664548";
        final String name            = "EditingGlossary";
        final String description     = "A temporary glossary holding glossary content that is being edited.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Glossary";

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
        final String attribute1Description     = "Description of the updates.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getEditingGlossaryCopyClassification()
    {
        final String guid            = "361fa044-e703-404c-bb83-9402f9221f54";
        final String name            = "EditingGlossaryCopy";
        final String description     = "An element that is part of an editing glossary's scope and is a temporary copy of another element.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private TypeDefPatch deprecateGlossaryTermEvolution()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GlossaryTermEvolution";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch updateGlossaryTermEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GlossaryTerm";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "publishVersionIdentifier";
        final String attribute1Description     = "The author-controlled version identifier for the term.";
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


    private void update0423SecurityAccessControl()
    {
        this.archiveBuilder.addTypeDefPatch(updateSecurityTagsClassification());
        this.archiveBuilder.addEntityDef(addSecurityAccessControlEntity());
        this.archiveBuilder.addRelationshipDef(addAssociatedGroupRelationship());
    }


    private TypeDefPatch updateSecurityTagsClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SecurityTags";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "accessGroups";
        final String attribute1Description     = "Map of access groups.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getMapStringObjectTypeDefAttribute(attribute1Name,
                                                                    attribute1Description,
                                                                    attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private RelationshipDef addAssociatedGroupRelationship()
    {
        final String guid            = "e47a19d0-7e12-4cf7-9ad7-50856da7faa2";
        final String name            = "AssociatedGroup";
        final String description     = "Links a security access control to a security group.";
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
        final String                     end1EntityType               = "SecurityAccessControl";
        final String                     end1AttributeName            = "usedInAccessControls";
        final String                     end1AttributeDescription     = "An access control definition that uses the security group.";
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
        final String                     end2EntityType               = "SecurityGroup";
        final String                     end2AttributeName            = "associatedSecurityGroups";
        final String                     end2AttributeDescription     = "The security groups to use to validate access for the operation.";
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

        final String attribute1Name            = "operationName";
        final String attribute1Description     = "Name of the operation to that is controlled by the linked security group.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private EntityDef addSecurityAccessControlEntity()
    {
        final String guid            = "f53bd594-5f75-4cf9-9f77-f5c35396590e";
        final String name            = "SecurityAccessControl";
        final String description     = "A technical control that defines who has access to the attach element.";
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

    private void update504ImplementationSnippets()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateSchemaTypeSnippet());
        this.archiveBuilder.addRelationshipDef(getAssociatedSnippetRelationship());
    }

    private TypeDefPatch deprecateSchemaTypeSnippet()
    {
        final String typeName = "SchemaTypeSnippet";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private RelationshipDef getAssociatedSnippetRelationship()
    {
        final String guid            = "6f89c320-22aa-4d99-9a97-442e8d214655";
        final String name            = "AssociatedSnippet";
        final String description     = "Link between an element such as a schema type or data class and an implementation snippet.";
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
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "snippetRelevantForElements";
        final String                     end1AttributeDescription     = "Element describing logical structure for data element.";
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
        final String                     end2EntityType               = "ImplementationSnippet";
        final String                     end2AttributeName            = "implementationSnippetsForElement";
        final String                     end2AttributeDescription     = "Template implementation of the element.";
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

    private void update0710DigitalServices()
    {
        this.archiveBuilder.addTypeDefPatch(updateDigitalProductClassification());
    }


    private TypeDefPatch updateDigitalProductClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DigitalProduct";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Update the valid instance statuses
         */
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

        typeDefPatch.setValidInstanceStatusList(validInstanceStatusList);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

