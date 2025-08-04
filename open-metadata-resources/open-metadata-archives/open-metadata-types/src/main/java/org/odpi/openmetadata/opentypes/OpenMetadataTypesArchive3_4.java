/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefStatus;
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
public class OpenMetadataTypesArchive3_4
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.4";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
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
    public OpenMetadataTypesArchive3_4()
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
    public OpenMetadataTypesArchive3_4(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_3 previousTypes = new OpenMetadataTypesArchive3_3(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        correct0040SoftwareServers();
        extend0110Actors();
        update0112Team();
        update0115ITProfile();
        extend0423SecurityDefinitions();
        update0223Events();
    }


    /**
     * The ServerEndpoint relationship used to be between an Endpoint and a SoftwareServer.  In 3.1 it was changed to connect
     * an Endpoint with an ITInfrastructure element to allow SoftwareServerPlatforms (like the OMAGServerPlatform)
     * to support endpoints (and hence APIs).  Unfortunately the wrong end was updated and so this change sets up the
     * ends properly.
     */
    private void correct0040SoftwareServers()
    {
        this.archiveBuilder.addTypeDefPatch(updateServerEndpointRelationship());
    }


    private TypeDefPatch updateServerEndpointRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Update end 1.
         */
        final String                     end1AttributeName            = "servers";
        final String                     end1AttributeDescription     = "Server(s) supporting this endpoint.";
        final String                     end1AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);

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
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add support for LDAP distinguished name and extend the ProfileIdentity relationship to help selection of userIdentity..
     */
    private void extend0110Actors()
    {
        this.archiveBuilder.addTypeDefPatch(updateUserIdentityEntity());
        this.archiveBuilder.addTypeDefPatch(updateProfileIdentityRelationship());
    }

    private TypeDefPatch updateUserIdentityEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.USER_IDENTITY.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISTINGUISHED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateProfileIdentityRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ROLE_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ROLE_GUID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Enable any person role to participate in multiple team leadership or team membership relationships.
     */
    private void update0112Team()
    {
        this.archiveBuilder.addTypeDefPatch(updateTeamMembershipRelationship());
        this.archiveBuilder.addTypeDefPatch(updateTeamLeadershipRelationship());
    }


    private TypeDefPatch updateTeamMembershipRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 2.
         */
        final String                     end1AttributeName            = "teamMembers";
        final String                     end1AttributeDescription     = "The members of the team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);


        /*
         * Set up end 1.
         */
        final String                     end2AttributeName            = "memberOfTeam";
        final String                     end2AttributeDescription     = "The team that this role is a member of.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateTeamLeadershipRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 2.
         */
        final String                     end1AttributeName            = "teamLeaders";
        final String                     end1AttributeDescription     = "The leaders of the team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);


        /*
         * Set up end 1.
         */
        final String                     end2AttributeName            = "leadsTeam";
        final String                     end2AttributeDescription     = "The team lead by this person role.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TEAM.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Link an IT profile to the infrastructure it represents.
     */
    private void update0115ITProfile()
    {
        this.archiveBuilder.addRelationshipDef(addITInfrastructureProfileRelationship());
    }


    private RelationshipDef addITInfrastructureProfileRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describedByProfile";
        final String                     end1AttributeDescription     = "The IT asset/software capability that is described by the IT profile.";
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
        final String                     end2AttributeName            = "usedByInfrastructure";
        final String                     end2AttributeDescription     = "Description of the user identifies used by the asset/software capability.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.IT_PROFILE.typeName),
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
     * Add support for security groups.
     */
    private void extend0423SecurityDefinitions()
    {
        this.archiveBuilder.addEntityDef(addSecurityGroupEntity());
        this.archiveBuilder.addClassificationDef(addSecurityGroupMembershipClassification());
    }


    private EntityDef addSecurityGroupEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SECURITY_GROUP,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.TECHNICAL_CONTROL.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISTINGUISHED_NAME));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private ClassificationDef addSecurityGroupMembershipClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.USER_IDENTITY.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GROUPS));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Store the number of partitions and replicas in the KafkaTopic.
     */
    private void update0223Events()
    {
        this.archiveBuilder.addTypeDefPatch(updateKafkaTopic());
    }

    /**
     * Add 2 new attributes to the kafka topic. These are used to store the number of
     * the Kafka topic replicas and partitions.
     * @return the typeDefPatch
     */
    private TypeDefPatch updateKafkaTopic()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.KAFKA_TOPIC.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PARTITIONS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REPLICAS));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

