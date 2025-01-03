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
        update0463EngineActions();
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
        final String typeName = OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

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
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                           OpenMetadataProperty.DISTINGUISHED_NAME.description,
                                                           OpenMetadataProperty.DISTINGUISHED_NAME.descriptionGUID);
        properties.add(property);


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
        TypeDefAttribute       property;


        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ROLE_TYPE_NAME.name,
                                                           OpenMetadataProperty.ROLE_TYPE_NAME.description,
                                                           OpenMetadataProperty.ROLE_TYPE_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ROLE_GUID.name,
                                                           OpenMetadataProperty.ROLE_GUID.description,
                                                           OpenMetadataProperty.ROLE_GUID.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

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
        final String typeName = "TeamMembership";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "teamMembers";
        final String                     end1AttributeDescription     = "The members of the team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Team";
        final String                     end2AttributeName            = "memberOfTeam";
        final String                     end2AttributeDescription     = "The team that this role is a member of.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateTeamLeadershipRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "TeamLeadership";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "teamLeaders";
        final String                     end1AttributeDescription     = "The leaders of the team.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.PERSON_ROLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Team";
        final String                     end2AttributeName            = "leadsTeam";
        final String                     end2AttributeDescription     = "The team lead by this person role.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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
        final String guid = "042d9b5c-677e-477b-811f-1c39bf716759";

        final String name            = "SecurityGroup";
        final String description     = "A collection of users that should be given the same security privileges.";
        final String descriptionGUID = null;

        final String superTypeName = "TechnicalControl";

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

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                           OpenMetadataProperty.DISTINGUISHED_NAME.description,
                                                           OpenMetadataProperty.DISTINGUISHED_NAME.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private ClassificationDef addSecurityGroupMembershipClassification()
    {
        final String guid = "21a16f1e-9231-4983-b371-a0686d555273";

        final String name            = "SecurityGroupMembership";
        final String description     = "Identifies the set of user groups that this user identity is a member of.";
        final String descriptionGUID = null;

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.USER_IDENTITY.typeName));

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 linkedToEntities,
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "groups";
        final String attribute1Description     = "List of user group names.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
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
        final String typeName = "KafkaTopic";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "partitions";
        final String attribute1Description     = "Number of Kafka partitions.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "replicas";
        final String attribute2Description     = "Number of Kafka replicas.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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
     * Deprecate the use of EngineActionExecutor and GovernanceActionProcessStepUse relationships in favour of
     * additional properties in the EngineAction entity.  This is to improve performance.
     */
    private void update0463EngineActions()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateEngineActionExecutorRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateGovernanceActionProcessStepUseRelationship());
        this.archiveBuilder.addTypeDefPatch(updateEngineActionEntity());
    }

    private TypeDefPatch deprecateEngineActionExecutorRelationship()
    {
        final String typeName = "EngineActionExecutor";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateGovernanceActionProcessStepUseRelationship()
    {
        final String typeName = "GovernanceActionProcessStepUse";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch updateEngineActionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.ENGINE_ACTION.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "requestType";
        final String attribute1Description     = "The request type used to call the service.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "requestParameters";
        final String attribute2Description     = "Properties that configure the governance service for this type of request.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "executorEngineGUID";
        final String attribute3Description     = "Unique identifier of the governance engine nominated to run the request.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "executorEngineName";
        final String attribute4Description     = "Unique identifier of the governance engine nominated to run the request.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = OpenMetadataProperty.PROCESS_NAME.name;
        final String attribute5Description     = OpenMetadataProperty.PROCESS_NAME.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.PROCESS_NAME.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.PROCESS_STEP_GUID.name;
        final String attribute6Description     = OpenMetadataProperty.PROCESS_STEP_GUID.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.PROCESS_STEP_GUID.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.PROCESS_STEP_NAME.name;
        final String attribute7Description     = OpenMetadataProperty.PROCESS_STEP_NAME.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.PROCESS_STEP_NAME.descriptionGUID;
        final String attribute8Name            = OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.name;
        final String attribute8Description     = OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.description;
        final String attribute8DescriptionGUID = OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.descriptionGUID;
        final String attribute9Name            = OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.name;
        final String attribute9Description     = OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.description;
        final String attribute9DescriptionGUID = OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.descriptionGUID;

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

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

