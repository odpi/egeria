/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
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
    private static final String                  archiveVersion     = "5.1";
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
        OpenMetadataTypesArchive5_0 previousTypes = new OpenMetadataTypesArchive5_0(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        update0010Base();
        update0201Connections();
        update0210DataStores();
        update0235InformationView();
        update0462GovernanceActionProcesses();
        update0464IntegrationGroups();
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010Base()
    {
        this.archiveBuilder.addTypeDefPatch(updateAsset());
    }


    private TypeDefPatch updateAsset()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ASSET.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.RESOURCE_NAME.name,
                                                           OpenMetadataProperty.RESOURCE_NAME.description,
                                                           OpenMetadataProperty.RESOURCE_NAME.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0201Connections()
    {
        this.archiveBuilder.addTypeDefPatch(updateConnectorType());
    }


    private TypeDefPatch updateConnectorType()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONNECTOR_TYPE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0210DataStores()
    {
        this.archiveBuilder.addClassificationDef(getDataAssetEncodingClassification());
        this.archiveBuilder.addTypeDefPatch(deprecateDataStoreEncodingClassification());
    }

    private ClassificationDef getDataAssetEncodingClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.description,
                                                                                 OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.wikiURL,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_ASSET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ENCODING.name,
                                                           OpenMetadataProperty.ENCODING.description,
                                                           OpenMetadataProperty.ENCODING.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                           OpenMetadataProperty.ENCODING_LANGUAGE.description,
                                                           OpenMetadataProperty.ENCODING_LANGUAGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                           OpenMetadataProperty.ENCODING_DESCRIPTION.description,
                                                           OpenMetadataProperty.ENCODING_DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                                    OpenMetadataProperty.ENCODING_PROPERTIES.description,
                                                                    OpenMetadataProperty.ENCODING_PROPERTIES.descriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private TypeDefPatch deprecateDataStoreEncodingClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataStoreEncoding";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0235InformationView()
    {
        this.archiveBuilder.addEntityDef(getVirtualRelationalTableEntity());
    }


    private EntityDef getVirtualRelationalTableEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeGUID,
                                                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.INFORMATION_VIEW.typeName),
                                                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.description,
                                                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.descriptionGUID,
                                                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.wikiURL);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0462GovernanceActionProcesses()
    {
        this.archiveBuilder.addEntityDef(getGovernanceActionProcessInstanceEntity());
        this.archiveBuilder.addRelationshipDef(getTargetForActionType());
        this.archiveBuilder.addRelationshipDef(getTargetForActionProcess());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionProcessFlow());
    }


    private EntityDef getGovernanceActionProcessInstanceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS.typeName),
                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.description,
                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.descriptionGUID,
                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.wikiURL);
    }

    private RelationshipDef getTargetForActionType()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TARGET_FOR_ACTION_TYPE,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "associatedGovernanceActionType";
        final String                     end1AttributeDescription     = "Describes the governance action that will use this target for action.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "predefinedTargetForAction";
        final String                     end2AttributeDescription     = "Provides a fixed target for action that will be used when this governance action executes.";
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                           OpenMetadataProperty.ACTION_TARGET_NAME.description,
                                                           OpenMetadataProperty.ACTION_TARGET_NAME.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getTargetForActionProcess()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TARGET_FOR_ACTION_PROCESS,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "associatedGovernanceActionProcess";
        final String                     end1AttributeDescription     = "Describes the governance action that will use this target for action.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "predefinedTargetForAction";
        final String                     end2AttributeDescription     = "Provides a fixed target for action that will be used when this governance action executes.";
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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                           OpenMetadataProperty.ACTION_TARGET_NAME.description,
                                                           OpenMetadataProperty.ACTION_TARGET_NAME.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /**
     * Add requestParameter
     *
     * @return patch
     */
    private TypeDefPatch updateGovernanceActionProcessFlow()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getMapStringStringTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                    OpenMetadataProperty.REQUEST_PARAMETERS.description,
                                                                    OpenMetadataProperty.REQUEST_PARAMETERS.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);


        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0464IntegrationGroups()
    {
        this.archiveBuilder.addEnumDef(getDeleteMethodEnum());
        this.archiveBuilder.addTypeDefPatch(updateCatalogTarget());
    }

    private EnumDef getDeleteMethodEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(DeleteMethod.getOpenTypeGUID(),
                                                        DeleteMethod.getOpenTypeName(),
                                                        DeleteMethod.getOpenTypeDescription(),
                                                        DeleteMethod.getOpenTypeDescriptionGUID(),
                                                        DeleteMethod.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (DeleteMethod deleteMethod : DeleteMethod.values())
        {
            elementDef = archiveHelper.getEnumElementDef(deleteMethod.getOrdinal(),
                                                         deleteMethod.getName(),
                                                         deleteMethod.getDescription(),
                                                         deleteMethod.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (deleteMethod.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    /**
     * Add multi-link and new properties
     *
     * @return patch
     */
    private TypeDefPatch updateCatalogTarget()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getEnumTypeDefAttribute(PermittedSynchronization.getOpenTypeName(),
                                                         OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                         OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.description,
                                                         OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(DeleteMethod.getOpenTypeName(),
                                                         OpenMetadataProperty.DELETE_METHOD.name,
                                                         OpenMetadataProperty.DELETE_METHOD.description,
                                                         OpenMetadataProperty.DELETE_METHOD.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);


        return typeDefPatch;
    }
}

