/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
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
public class OpenMetadataTypesArchive3_11
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.11";
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
    public OpenMetadataTypesArchive3_11()
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
    public OpenMetadataTypesArchive3_11(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_9 previousTypes = new OpenMetadataTypesArchive3_9(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0010BaseModel();
        add0022Translations();
        update0110ActorProfile();
        updateResponsibilityAssignments();
        update04xxNewGovernanceRoles();
        update04xxMultiLinkGovernanceImplementationTypes();
        update07xxImplementationRelationships();
        add0735SolutionPortSchemaRelationship();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataSet());
    }

    private TypeDefPatch updateDataSet()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_SET.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0022Translations()
    {
        this.archiveBuilder.addEntityDef(getTranslationDetailEntity());
        this.archiveBuilder.addRelationshipDef(getTranslationLinkRelationship());
    }


    private RelationshipDef getTranslationLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TRANSLATION_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "translates";
        final String                     end1AttributeDescription     = "Entity that is translated.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "translation";
        final String                     end2AttributeDescription     = "Translation of entity for a single language.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TRANSLATION_DETAIL.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private EntityDef getTranslationDetailEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.TRANSLATION_DETAIL,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LANGUAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LANGUAGE_CODE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LOCALE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_TRANSLATIONS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0110ActorProfile()
    {
        this.archiveBuilder.addTypeDefPatch(updateUserIdentity());
        this.archiveBuilder.addTypeDefPatch(updatePerson());
        this.archiveBuilder.addTypeDefPatch(updatePersonRole());
        this.archiveBuilder.addTypeDefPatch(updateContactDetails());
    }


    private TypeDefPatch updateUserIdentity()
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_ID));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updatePerson()
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRONOUNS));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updatePersonRole()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PERSON_ROLE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.HEAD_COUNT));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    private TypeDefPatch updateContactDetails()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONTACT_DETAILS.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTACT_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add new relationship called AssignmentScope to show the scope of someone or a team's responsibility.
     */
    private void updateResponsibilityAssignments()
    {
        this.archiveBuilder.addRelationshipDef(getAssignmentScopeRelationship());
    }


    private RelationshipDef getAssignmentScopeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "assignedActors";
        final String                     end1AttributeDescription     = "Person, team, project or other type of actor that has been assigned.";
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
        final String                     end2AttributeName            = "scopeOfResponsibility";
        final String                     end2AttributeDescription     = "Elements describing the resources or action the the actors are responsible for.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSIGNMENT_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update04xxNewGovernanceRoles()
    {
        archiveBuilder.addEntityDef(getGovernanceRepresentativeEntity());
        archiveBuilder.addEntityDef(getLocationOwnerEntity());
        archiveBuilder.addEntityDef(getBusinessOwnerEntity());
        archiveBuilder.addEntityDef(getSolutionOwnerEntity());
    }

    private EntityDef getGovernanceRepresentativeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_REPRESENTATIVE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));
    }


    private EntityDef getLocationOwnerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.LOCATION_OWNER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));
    }


    private EntityDef getBusinessOwnerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.BUSINESS_OWNER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));
    }


    private EntityDef getSolutionOwnerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SOLUTION_OWNER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ROLE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add multi-link flags and extend properties to be able to record proper attributions.
     */
    private void update04xxMultiLinkGovernanceImplementationTypes()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionProcess());
        this.archiveBuilder.addTypeDefPatch(updateNextGovernanceActionProcessStepRelationship());
        this.archiveBuilder.addTypeDefPatch(updateLicenseRelationship());
        this.archiveBuilder.addTypeDefPatch(updateCertificationRelationship());
    }


    private TypeDefPatch updateGovernanceActionProcess()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateNextGovernanceActionProcessStepRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }

    private TypeDefPatch updateLicenseRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.LICENSE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }

    private TypeDefPatch updateCertificationRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update07xxImplementationRelationships()
    {
        this.archiveBuilder.addRelationshipDef(getImplementedByRelationship());
    }


    private RelationshipDef getImplementedByRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "derivedFrom";
        final String                     end1AttributeDescription     = "Abstract representation.";
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
        final String                     end2AttributeName            = "implementedBy";
        final String                     end2AttributeDescription     = "Resulting refined element.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESIGN_STEP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ROLE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TRANSFORMATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ISC_QUALIFIED_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0735SolutionPortSchemaRelationship()
    {
        this.archiveBuilder.addRelationshipDef(getSolutionPortSchemaRelationship());
    }


    private RelationshipDef getSolutionPortSchemaRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SOLUTION_PORT_SCHEMA_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "describesSolutionPortData";
        final String                     end1AttributeDescription     = "Port that uses the schema type.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOLUTION_PORT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "solutionPortSchema";
        final String                     end2AttributeDescription     = "Structure of the solution port's data.";
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
}

