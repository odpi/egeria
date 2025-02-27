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
public class OpenMetadataTypesArchive4_1
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
    public OpenMetadataTypesArchive4_1()
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
    public OpenMetadataTypesArchive4_1(OMRSArchiveBuilder archiveBuilder)
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
        update0010BasicModel();
        update0011ManagingReferenceables();
        update0210DataStores();
        update0320CategoryHierarchy();
        update0385ControlledGlossaryDevelopment();
        update0423SecurityAccessControl();
        update504ImplementationSnippets();
        update0710DigitalServices();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add support for sample data and remove displayName form process because it is causing confusion.
     */
    private void update0010BasicModel()
    {
        this.archiveBuilder.addRelationshipDef(getSampleDataRelationship());
        this.archiveBuilder.addTypeDefPatch(updateProcess());
    }


    /**
     * Add sample data relationship
     *
     * @return relationship def
     */
    private RelationshipDef getSampleDataRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SAMPLE_DATA_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "sourceOfSample";
        final String                     end1AttributeDescription     = "Represents the resource where the sample was taken from.";
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
        final String                     end2AttributeName            = "sampleData";
        final String                     end2AttributeDescription     = "Describes the location of the resource that holds the sample data.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SAMPLING_METHOD));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /**
     * Remove displayName from process - it was added in error - use name property instead.
     *
     * @return type def patch
     */
    private TypeDefPatch updateProcess()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.PROCESS.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0011ManagingReferenceables()
    {
        this.archiveBuilder.addClassificationDef(getTemplateSubstituteClassification());
        this.archiveBuilder.addTypeDefPatch(updateSourcedFromRelationship());
    }


    private TypeDefPatch updateSourcedFromRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE_VERSION_NUMBER));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private ClassificationDef getTemplateSubstituteClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
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
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.DATA_FIELD_VALUES_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEFAULT_VALUE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SAMPLE_VALUES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_PATTERNS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME_PATTERNS));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0320CategoryHierarchy()
    {
        this.archiveBuilder.addClassificationDef(getRootCategoryClassification());
    }

    private ClassificationDef getRootCategoryClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.ROOT_CATEGORY_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_CATEGORY.typeName),
                                                  false);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0385ControlledGlossaryDevelopment()
    {
        this.archiveBuilder.addClassificationDef(getEditingGlossaryClassification());
        this.archiveBuilder.addClassificationDef(getStagingGlossaryClassification());
        this.archiveBuilder.addTypeDefPatch(deprecateGlossaryTermEvolution());
        this.archiveBuilder.addTypeDefPatch(updateGlossaryTermEntity());
    }

    private ClassificationDef getEditingGlossaryClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getStagingGlossaryClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
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
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GLOSSARY_TERM.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add( archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLISH_VERSION_ID));

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
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACCESS_GROUPS));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private RelationshipDef addAssociatedGroupRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSOCIATED_GROUP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usedInAccessControls";
        final String                     end1AttributeDescription     = "An access control definition that uses the security group.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "associatedSecurityGroups";
        final String                     end2AttributeDescription     = "The security groups to use to validate access for the operation.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SECURITY_GROUP.typeName),
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
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SECURITY_ACCESS_CONTROL,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.TECHNICAL_CONTROL.typeName));
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
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSOCIATED_SNIPPET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "snippetRelevantForElements";
        final String                     end1AttributeDescription     = "Element describing logical structure for data element.";
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
        final String                     end2AttributeName            = "implementationSnippetsForElement";
        final String                     end2AttributeDescription     = "Template implementation of the element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.IMPLEMENTATION_SNIPPET.typeName),
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

    private void update0710DigitalServices()
    {
        this.archiveBuilder.addTypeDefPatch(updateDigitalProductClassification());
    }


    private TypeDefPatch updateDigitalProductClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRODUCT_STATUS));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

