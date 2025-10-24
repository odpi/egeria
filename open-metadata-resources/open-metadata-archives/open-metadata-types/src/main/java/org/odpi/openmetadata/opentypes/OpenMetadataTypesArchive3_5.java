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
public class OpenMetadataTypesArchive3_5
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.5";
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
    public OpenMetadataTypesArchive3_5()
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
    public OpenMetadataTypesArchive3_5(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_4 previousTypes = new OpenMetadataTypesArchive3_4(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update00420045SoftwareCapabilities();
        extend0112Person();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Allow software capabilities to be supported by any type of IT Infrastructure - not just SoftwareServers
     */
    private void update00420045SoftwareCapabilities()
    {
        this.archiveBuilder.addRelationshipDef(getSupportedSoftwareCapabilityRelationship());
        this.archiveBuilder.addEntityDef(addSourceControlLibrary());
        this.archiveBuilder.addEntityDef(addChangeManagementLibrary());
        this.archiveBuilder.addEntityDef(addSoftwareLibrary());
        this.archiveBuilder.addTypeDefPatch(updateCloudServiceClassification());
        this.archiveBuilder.addTypeDefPatch(updateServerAssetUseRelationship());
    }


    private RelationshipDef getSupportedSoftwareCapabilityRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "hostedByDeployedITInfrastructure";
        final String                     end1AttributeDescription     = "IT infrastructure hosting this capability.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "capabilities";
        final String                     end2AttributeDescription     = "Capabilities deployed on this IT infrastructure.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_CAPABILITY.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEPLOYMENT_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEPLOYER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEPLOYER_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEPLOYER_PROPERTY_NAME));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.OPERATIONAL_STATUS));


        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private EntityDef addSourceControlLibrary()
    {
        EntityDef classificationDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SOURCE_CONTROL_LIBRARY,
                                                                        this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MANAGER.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LIBRARY_TYPE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private EntityDef addChangeManagementLibrary()
    {
        EntityDef classificationDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CHANGE_MANAGEMENT_LIBRARY,
                                                                        this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MANAGER.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LIBRARY_TYPE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }



    private EntityDef addSoftwareLibrary()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SOFTWARE_LIBRARY,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MANAGER.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LIBRARY_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private TypeDefPatch updateCloudServiceClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(OpenMetadataType.SOFTWARE_CAPABILITY.typeName)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateServerAssetUseRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "consumedBy";
        final String                     end1AttributeDescription     = "Capability consuming this asset.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_CAPABILITY.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "consumedAsset";
        final String                     end2AttributeDescription     = "Asset that this software capability is dependent on.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
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
     * Extend Person using the field definitions from LDAP
     */
    private void extend0112Person()
    {
        this.archiveBuilder.addTypeDefPatch(updatePersonEntity());
    }

    private TypeDefPatch updatePersonEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PERSON.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COURTESY_TITLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GIVEN_NAMES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SURNAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EMPLOYEE_NUMBER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EMPLOYEE_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PREFERRED_LANGUAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INITIALS));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

