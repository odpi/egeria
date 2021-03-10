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
    private static final String                  archiveVersion     = "2.8";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache 2.0";
    private static final Date                    creationDate       = new Date(1588261366992L);

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
        OpenMetadataTypesArchive2_7  previousTypes = new OpenMetadataTypesArchive2_7(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0025Locations();
        update0030HostsAndOperatingPlatforms();
        update0050Applications();
        addRelationshipSupertypes();
        update0320CategoryHierarchy();
        update0330Terms();
        update0350RelatedTerms();
        update0360Contexts();
        update0370SemanticAssignment();
        update0380SpineObjects();
        update0465DuplicateProcessing();
        update0534RelationalSchemas();
        update0540DataClasses();
        update0545ReferenceData();

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0025Locations()
    {
        this.archiveBuilder.addTypeDefPatch(updateFixedLocation());
        this.archiveBuilder.addTypeDefPatch(updateCyberLocation());
    }


    private TypeDefPatch updateFixedLocation()
    {
        final String typeName = "FixedLocation";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "address";
        final String attribute1Description     = "Postal address of the location (Deprecated).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "postalAddress";
        final String attribute2Description     = "Postal address of the location.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "mapProjection";
        final String attribute3Description     = "The scheme used to define the meaning of the coordinates.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateCyberLocation()
    {
        final String typeName = "CyberLocation";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "address";
        final String attribute1Description     = "Address of the location (Deprecated).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "networkAddress";
        final String attribute2Description     = "Base network address used to connect to the location's endpoint(s).";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
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
     * The HostLocation relationship is superfluous - can use AssetLocation since Host is an Asset
     */
    private void update0030HostsAndOperatingPlatforms()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateHostLocation());
    }

    private TypeDefPatch deprecateHostLocation()
    {
        final String typeName = "HostLocation";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /**
     * The RuntimeForProcess relationship is superfluous - can use ServerAssetUse since Application is a SoftwareServerCapability.
     */
    private void update0050Applications()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateRuntimeForProcess());
    }

    private TypeDefPatch deprecateRuntimeForProcess()
    {
        final String typeName = "RuntimeForProcess";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */
    private void addRelationshipSupertypes()
    {
        this.archiveBuilder.addRelationshipDef(getGovernedRelationship());
    }

    private RelationshipDef getGovernedRelationship()
    {
        final String guid            = "1b8fea93-0de9-4d75-9702-5179863321db";
        final String name            = "GovernedRelationship";
        final String description     = "Defines a relationship that is governed by a steward.";
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
        final String                     end1AttributeName            = "relatedEntity";
        final String                     end1AttributeDescription     = "One side of the relationship that is governed.";
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
        final String                     end2EntityType               = "Referenceable";
        final String                     end2AttributeName            = "relatedEntity";
        final String                     end2AttributeDescription     = "Other side of the relationship that is governed.";
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

        final String attribute1Name            = "steward";
        final String attribute1Description     = "Person responsible for the relationship.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private void update0320CategoryHierarchy()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("LibraryCategoryReference"));
    }

    private void update0330Terms()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("LibraryTermReference"));
    }

    private void update0350RelatedTerms()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("RelatedTerm"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("Synonym"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("Antonym"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("PreferredTerm"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("ReplacementTerm"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("Translation"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("ISARelationship"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("ValidValue"));
    }

    private void update0360Contexts()
    {
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("UsedInContext"));
    }

    private void update0370SemanticAssignment()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("SemanticAssignment"));
    }

    private void update0380SpineObjects()
    {
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("TermHASARelationship"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("TermISATypeOFRelationship"));
        this.archiveBuilder.addTypeDefPatch(addSuperTypeToTermToTermRelationship("TermTYPEDBYRelationship"));
    }

    private void update0534RelationalSchemas()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("ForeignKey"));
    }

    private void update0540DataClasses()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("DataClassAssignment"));
    }

    private void update0545ReferenceData()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("ValidValuesMapping"));
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("ReferenceValueAssignment"));
    }

    private void update0465DuplicateProcessing()
    {
        this.archiveBuilder.addTypeDefPatch(addGovernedRelationshipAsSuperType("KnownDuplicateLink"));
    }

    private TypeDefPatch addGovernedRelationshipAsSuperType(String typeName)
    {
        final String superTypeName = "GovernedRelationship";

        TypeDef superType = archiveBuilder.getTypeDefByName(superTypeName);
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(superType);

        return typeDefPatch;
    }

    private TypeDefPatch addSuperTypeToTermToTermRelationship(String typeName)
    {
        final String superTypeName = "RelatedTerm";

        TypeDef superType = archiveBuilder.getTypeDefByName(superTypeName);
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(superType);

        return typeDefPatch;
    }

}
