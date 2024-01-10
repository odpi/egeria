/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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
public class OpenMetadataTypesArchive1_5
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "1.5";
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
    public OpenMetadataTypesArchive1_5()
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
    public OpenMetadataTypesArchive1_5(OMRSArchiveBuilder archiveBuilder)
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
             * Retrieve types
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
        OpenMetadataTypesArchive1_4  previousTypes = new OpenMetadataTypesArchive1_4(archiveBuilder);

        /*
         * Call each of the methods to systematically add the contents of the archive.
         * The original types are added first.
         */
        previousTypes.getOriginalTypes();

        update0298LineageRelationships();
        update0501SchemaElements();
        update0605OpenDiscoveryAnalysisReports();
        update0650RelationshipDiscovery();
    }


    private void update0298LineageRelationships()
    {
        this.archiveBuilder.addEnumDef(getProcessContainmentTypeEnum());
        this.archiveBuilder.addRelationshipDef(getProcessHierarchyRelationship());
    }


    /**
     * The ProcessContainmentType enum describes the type of containment that exists between two processes.
     *
     * @return ProcessContainmentType EnumDef
     */
    private EnumDef getProcessContainmentTypeEnum()
    {
        final String guid            = "1bb4b908-7983-4802-a2b5-91b095552ee9";
        final String name            = "ProcessContainmentType";
        final String description     = "The containment relationship between two processes.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "OWNED";
        final String element1Description     = "The parent process owns the child process in the relationship, such that if the parent is removed the child should also be removed. A child can have at most one such parent.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "USED";
        final String element2Description     = "The child process is simply used by the parent. A child process can have many such relationships to parents.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 99;
        final String element3Value           = "OTHER";
        final String element3Description     = "None of the above.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    /**
     * The ProcessHierarchy relationship describes a parent-child relationship between processes.
     *
     * @return ProcessHierarchy RelationshipDef
     */
    private RelationshipDef getProcessHierarchyRelationship()
    {
        final String guid            = "70dbbda3-903f-49f7-9782-32b503c43e0e";
        final String name            = "ProcessHierarchy";
        final String description     = "A hierarchical relationship between processes.";
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
        final String                     end1AttributeName            = "parentProcess";
        final String                     end1AttributeDescription     = "The more abstract or higher-level process.";
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
        final String                     end2AttributeName            = "childProcess";
        final String                     end2AttributeDescription     = "The more detailed or lower-level process.";
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

        final String attribute1Enum            = "ProcessContainmentType";
        final String attribute1Name            = "containmentType";
        final String attribute1Description     = "The type of containment that exists between the related processes.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute(attribute1Enum,
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private void update0501SchemaElements()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaElementEntity());
    }


    /**
     * 0501 SchemaElement 'anchorGUID' property should refer to an Asset when available, updated description
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

        final String attribute1Name = "anchorGUID";
        final String attribute1Description = "Optional identification of the Asset that this schema element is a part of.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private void update0605OpenDiscoveryAnalysisReports()
    {
        this.archiveBuilder.addEnumDef(getDiscoveryServiceRequestStatusEnum());
        this.archiveBuilder.addTypeDefPatch(updateOpenDiscoveryAnalysisReportEntity());
    }

    private EnumDef getDiscoveryServiceRequestStatusEnum()
    {
        final String guid            = "b2fdeddd-24eb-4e9c-a2a4-2693828d4a69";
        final String name            = "DiscoveryServiceRequestStatus";
        final String description     = "Defines the progress or completion of a requested discovery service.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Waiting";
        final String element1Description     = "Discovery service is waiting to execute.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);
        enumDef.setDefaultValue(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "Activating";
        final String element2Description     = "Discovery service is being initialized in the discovery engine.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);


        final int    element3Ordinal         = 2;
        final String element3Value           = "InProgress";
        final String element3Description     = "Discovery service is executing.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 3;
        final String element4Value           = "Failed";
        final String element4Description     = "Discovery service has failed.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 4;
        final String element5Value           = "Completed";
        final String element5Description     = "Discovery service has completed successfully.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                     element5Value,
                                                     element5Description,
                                                     element5DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element6Ordinal         = 5;
        final String element6Value           = "Other";
        final String element6Description     = "Discovery service has a status that is not covered by this enum.";
        final String element6DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element6Ordinal,
                                                     element6Value,
                                                     element6Description,
                                                     element6DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element99Ordinal         = 99;
        final String element99Value           = "Unknown";
        final String element99Description     = "Discovery service status is unknown.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    /**
     * 0602 SupportedDiscoveryRelationship - replacing discoveryRequestStatus with discoveryServiceRequest
     *      and discoveryRequestStep with discoveryAnalysisStep
     */
    private TypeDefPatch updateOpenDiscoveryAnalysisReportEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "OpenDiscoveryAnalysisReport";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "discoveryRequestStatus";
        final String attribute1Description     = "Deprecated property.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "discoveryServiceStatus";
        final String attribute2Description     = "The status of a requested discovery service.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "discoveryRequestStep";
        final String attribute3Description     = "Deprecated property.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "discoveryAnalysisStep";
        final String attribute4Description     = "The current processing step of a running discovery service.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("DiscoveryRequestStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setReplacedByAttribute(attribute2Name);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("DiscoveryServiceRequestStatus",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setReplacedByAttribute(attribute4Name);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private void update0650RelationshipDiscovery()
    {
        this.archiveBuilder.addTypeDefPatch(updateRelationshipAdviceAnnotation());
    }


    /**
     * 0605 Add relatedEntityGUID to RelationshipAdviceAnnotation
     */
    private TypeDefPatch updateRelationshipAdviceAnnotation()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RelationshipAdviceAnnotation";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name = "relatedEntityGUID";
        final String attribute1Description = "entity that should be linked to the asset being analyzed";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }
}

