/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;


import org.odpi.openmetadata.frameworks.openmetadata.definitions.ActorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.ProjectDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ActorRoleGroup;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.Category;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;


/**
 * CocoBaseArchiveWriter provides a base class for utilities creating a physical open metadata archive file containing  definitions for
 * Coco Pharmaceuticals.
 */
public abstract class EgeriaBaseArchiveWriter extends OMRSArchiveWriter
{
    protected static final String                  archiveLicense     = "Apache 2.0";
    protected static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    protected static final String                  originatorName     = "Egeria Project";

    protected static final String guidMapFileName = "EgeriaContentPacksGUIDMap.json";

    /*
     * Specific values for initializing TypeDefs
     */
    protected static final String versionName   = "6.1-SNAPSHOT";

    private final Map<String, String> parentValidValueQNameToGUIDMap = new HashMap<>();

    protected       OMRSArchiveBuilder      archiveBuilder;
    protected       GovernanceArchiveHelper archiveHelper;
    protected final String                  archiveGUID;
    protected final String                  archiveName;
    protected final Date                    creationDate;
    protected final String                  archiveFileName;


    /**
     * Constructor for an archive.
     *
     * @param archiveGUID unique identifier of the archive
     * @param archiveName name of the archive
     * @param archiveDescription description of archive
     * @param creationDate date/time this archive writer ran
     * @param archiveFileName name of file to write archive to
     */
    protected EgeriaBaseArchiveWriter(String                  archiveGUID,
                                      String                  archiveName,
                                      String                  archiveDescription,
                                      Date                    creationDate,
                                      String                  archiveFileName)
    {
        this(archiveGUID, archiveName, archiveDescription, creationDate, archiveFileName, null);
    }


    /**
     * Constructor for an archive.
     *
     * @param archiveGUID unique identifier of the archive
     * @param archiveName name of the archive
     * @param archiveDescription description of archive
     * @param creationDate date/time this archive writer ran
     * @param archiveFileName name of file to write archive to
     * @param additionalDependencies archive that this archive is dependent on
     */
    protected EgeriaBaseArchiveWriter(String                archiveGUID,
                                      String                archiveName,
                                      String                archiveDescription,
                                      Date                  creationDate,
                                      String                archiveFileName,
                                      OpenMetadataArchive[] additionalDependencies)
    {
        this.archiveGUID = archiveGUID;
        this.archiveName = archiveName;
        this.creationDate = creationDate;

        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the definitions to be based on the existing open metadata types and definitions.
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        /*
         * Add in any additional dependencies.
         */
        if (additionalDependencies != null)
        {
            dependentOpenMetadataArchives.addAll(Arrays.asList(additionalDependencies));
        }

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        /*
         * Note: the version number is based off of the build time to ensure the
         * latest version of the archive elements is loaded.
         */
        this.archiveHelper = new GovernanceArchiveHelper(archiveBuilder,
                                                         archiveGUID,
                                                         archiveName,
                                                         archiveDescription,
                                                         originatorName,
                                                         creationDate,
                                                         new Date().getTime(),
                                                         versionName,
                                                         guidMapFileName);

        this.archiveFileName = archiveFileName;
    }


    /**
     * Provide an alternative archive builder.  Used when consolidating archives.
     *
     * @param archiveBuilder new archive builder
     */
    public void setArchiveBuilder(OMRSArchiveBuilder      archiveBuilder,
                                  GovernanceArchiveHelper archiveHelper)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveHelper = archiveHelper;
    }


    /**
     * Returns the open metadata archive containing new metadata entities.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        getArchiveContent();

        /*
         * The completed archive is ready to be packaged up and returned
         */
        return this.archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Implemented by subclass to add the content.
     */
    public abstract void getArchiveContent();


    /**
     * Find or create the parent set for a valid value.
     *
     * @param propertyName name of the property (can be null)
     * @return unique identifier (GUID) of the parent set
     */
    protected String getParentSet(String propertyName)
    {
        final String parentDescription = "Organizing set for valid metadata values";

        String parentQualifiedName = constructValidValueQualifiedName(null, propertyName, null, null);
        String parentSetGUID = parentValidValueQNameToGUIDMap.get(parentQualifiedName);

        if (parentSetGUID == null)
        {
            String parentDisplayName = parentQualifiedName.substring(26);

            parentSetGUID =  archiveHelper.addValidValue(null,
                                                         null,
                                                         null,
                                                         OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                         OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                                         null,
                                                         OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                         parentQualifiedName,
                                                         Category.VALID_METADATA_VALUES.getName(),
                                                         propertyName,
                                                         parentDisplayName,
                                                         parentDescription,
                                                         null,
                                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                         null,
                                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                         null,
                                                         null,
                                                         false,
                                                         0,
                                                         false,
                                                         null);

            parentValidValueQNameToGUIDMap.put(parentQualifiedName, parentSetGUID);

            return parentSetGUID;
        }
        else
        {
            return parentSetGUID;
        }
    }




    /**
     * Add an annotation type as a valid metadata value to the archive.
     *
     * @param annotationType annotation type to add
     */
    protected void addAnnotationType(AnnotationType annotationType)
    {
        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                null,
                                                                annotationType.getName());

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(OpenMetadataProperty.EXPLANATION.name, annotationType.getExplanation());
        additionalProperties.put(OpenMetadataProperty.EXPRESSION.name, annotationType.getExpression());

        if (annotationType.getMetrics() != null)
        {
            for (String metricName : annotationType.getMetrics().keySet())
            {
                additionalProperties.put("metric." + metricName, annotationType.getMetrics().get(metricName));
            }
        }

        this.addValidMetadataValue(archiveHelper.getGUID(qualifiedName),
                                   annotationType.getName(),
                                   annotationType.getSummary(),
                                   OpenMetadataProperty.ANNOTATION_TYPE.name,
                                   DataType.STRING.getDisplayName(),
                                   annotationType.getOpenMetadataTypeName(),
                                   null,
                                   annotationType.getName(),
                                   0,
                                   false,
                                   true,
                                   additionalProperties);


        if (annotationType.getAnalysisStep() != null)
        {
            String analysisStepQName = constructValidValueQualifiedName(null,
                                                                        OpenMetadataProperty.ANALYSIS_STEP.name,
                                                                        null,
                                                                        annotationType.getAnalysisStep());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, analysisStepQName);
        }
    }




    /**
     * Add a new valid value to an open metadata valid value set.
     *
     * @param displayName human-readable name
     * @param description description of the value
     * @param propertyName name of property
     * @param typeName type name - if values only apply to one type
     * @param mapName name of property if stored in a map
     * @param preferredValue preferred value to use
     */
    protected void addValidMetadataValue(String displayName,
                                         String description,
                                         String propertyName,
                                         String typeName,
                                         String mapName,
                                         String preferredValue)
    {
        this.addValidMetadataValue(null, displayName, description, propertyName, DataType.STRING.getDisplayName(), typeName, mapName, preferredValue, 0, false, true, null);
    }


    /**
     * Add a new valid value to an open metadata valid value set.
     *
     * @param displayName    human-readable name
     * @param description    description of the value
     * @param propertyName   name of property
     * @param dataType       type of property
     * @param typeName       type name - if values only apply to one type
     * @param mapName        name of property if stored in a map
     * @param preferredValue preferred value to use
     * @param ordinal        ordinal value for the valid value
     */
    protected void addValidMetadataValue(String displayName,
                                         String description,
                                         String propertyName,
                                         String dataType,
                                         String typeName,
                                         String mapName,
                                         String preferredValue,
                                         int    ordinal)
    {
        this.addValidMetadataValue(null, displayName, description, propertyName, dataType, typeName, mapName, preferredValue, ordinal, false, true, null);
    }


    /**
     * Add a new valid value to an open metadata valid value set.
     *
     * @param suppliedGUID predefined guid
     * @param displayName human-readable name
     * @param description description of the value
     * @param propertyName name of property
     * @param dataType type of property
     * @param typeName type name - if values only apply to one type
     * @param mapName name of property if stored in a map
     * @param preferredValue preferred value to use
     * @param ordinal ordinal value for the valid value
     * @param isDefaultValue is this the default value?
     * @param isCaseSensitive is this case sensitive?
     * @param additionalProperties additional properties or null
     */
    protected void addValidMetadataValue(String             suppliedGUID,
                                         String             displayName,
                                         String             description,
                                         String             propertyName,
                                         String             dataType,
                                         String             typeName,
                                         String             mapName,
                                         String             preferredValue,
                                         int                ordinal,
                                         boolean            isDefaultValue,
                                         boolean            isCaseSensitive,
                                         Map<String,String> additionalProperties)
    {
        String validValueSetGUID = this.getParentSet(propertyName);

        if (validValueSetGUID != null)
        {
            String qualifiedName = constructValidValueQualifiedName(typeName,
                                                                    propertyName,
                                                                    mapName,
                                                                    preferredValue);

            this.archiveHelper.addValidValue(suppliedGUID,
                                             validValueSetGUID,
                                             validValueSetGUID,
                                             OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                             OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                             null,
                                             OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                             qualifiedName,
                                             Category.VALID_METADATA_VALUES.getName(),
                                             propertyName,
                                             displayName,
                                             description,
                                             mapName,
                                             typeName,
                                             dataType,
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             preferredValue,
                                             null,
                                             isCaseSensitive,
                                             ordinal,
                                             isDefaultValue,
                                             additionalProperties);
        }
    }



    /**
     * Creates Project Hierarchy and dependencies.
     *
     * @param projectDefinitions - list of project definitions to create.
     */
    protected void writeProjects(ProjectDefinition[] projectDefinitions)
    {
        for (ProjectDefinition projectDefinition : projectDefinitions)
        {
            archiveHelper.addProject(null,
                                     projectDefinition.getQualifiedName(),
                                     projectDefinition.getIdentifier(),
                                     projectDefinition.getDisplayName(),
                                     projectDefinition.getMission(),
                                     projectDefinition.getDescription(),
                                     projectDefinition.getURL(),
                                     new Date(),
                                     null,
                                     null,
                                     null,
                                     projectDefinition.getProjectStatus().getName(),
                                     projectDefinition.isCampaign(),
                                     projectDefinition.isTask(),
                                     projectDefinition.getProjectTypeClassification(),
                                     null,
                                     null,
                                     null);

            String projectManagerQName = projectDefinition.getQualifiedName() + ":ProjectManager";

            archiveHelper.addActorRole(OpenMetadataType.PERSON_ROLE.typeName,
                                       List.of(ActorRoleGroup.PROJECT_MANAGER.getName()),
                                       projectManagerQName,
                                       projectDefinition.getIdentifier() + ":ProjectManager",
                                       projectDefinition.getDisplayName() + " project manager",
                                       "Person responsible for ensuring the project stays on track, meeting its goals with the time and resources budget and reporting status and issues to the sponsor.",
                                       ScopeDefinition.WITHIN_ORGANIZATION.getPreferredValue(),
                                       true,
                                       1,
                                       null,
                                       null);

            String projectTeamQName = projectDefinition.getQualifiedName() + ":ProjectTeam";

            if ((projectDefinition.getMembers() != null) || (! projectDefinition.isTask()))
            {
                archiveHelper.addActorRole(OpenMetadataType.PERSON_ROLE.typeName,
                                           List.of(ActorRoleGroup.TEAM_MEMBER.getName()),
                                           projectTeamQName,
                                           projectDefinition.getIdentifier() + ":ProjectTeam",
                                           projectDefinition.getDisplayName() + " project team",
                                           "Project team members that perform the assign tasks to meet the projects goals, paying attention to their use of time and resources as directed by the project leader.",
                                           ScopeDefinition.WITHIN_ORGANIZATION.getPreferredValue(),
                                           false,
                                           0,
                                           null,
                                           null);
            }

            String projectSponsorQName = projectDefinition.getQualifiedName() + ":ProjectSponsor";

            archiveHelper.addActorRole(OpenMetadataType.PERSON_ROLE.typeName,
                                       List.of(ActorRoleGroup.EXECUTIVE_SPONSOR.getName()),
                                       projectSponsorQName,
                                       projectDefinition.getIdentifier() + ":ProjectSponsor",
                                       projectDefinition.getDisplayName() + " project sponsor",
                                       "Person responsible for funding the project and overseeing the project's progress and use of resources.  May act a mentor to the project leader.",
                                       ScopeDefinition.WITHIN_ORGANIZATION.getPreferredValue(),
                                       true,
                                       1,
                                       null,
                                       null);

            archiveHelper.addProjectManagementRelationship(projectDefinition.getQualifiedName(),
                                                           projectManagerQName);

            if (projectDefinition.getControllingProject() != null)
            {
                archiveHelper.addProjectHierarchyRelationship(projectDefinition.getControllingProject().getQualifiedName(),
                                                              projectDefinition.getQualifiedName());
            }

            if (projectDefinition.getDependentOn() != null)
            {
                for (ProjectDefinition dependentOnProject : projectDefinition.getDependentOn())
                {
                    archiveHelper.addProjectDependencyRelationship(dependentOnProject.getQualifiedName(),
                                                                   projectDefinition.getQualifiedName(),
                                                                   null);
                }
            }

            if (projectDefinition.getLeader() != null)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(projectDefinition.getLeader().getQualifiedName(),
                                                                   projectManagerQName,
                                                                   false,
                                                                   0);

            }

            if (projectDefinition.getMembers() != null)
            {
                for (ActorDefinition member : projectDefinition.getMembers())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(member.getQualifiedName(),
                                                                       projectTeamQName,
                                                                       false,
                                                                       0);
                }
            }

            if (projectDefinition.getSponsor() != null)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(projectDefinition.getSponsor().getQualifiedName(),
                                                                   projectSponsorQName,
                                                                   false,
                                                                   0);

            }
        }
    }


    /**
     * Generates and writes out the open metadata archive created in the builder.
     */
    public void writeOpenMetadataArchive()
    {
        writeOpenMetadataArchive(null);
    }


    /**
     * Generates and writes out the open metadata archive created in the builder.
     *
     * @param folderName name of the folder to add the archive into
     */
    public void writeOpenMetadataArchive(String folderName)
    {
        try
        {
            String pathName = archiveFileName;

            if (folderName != null)
            {
                pathName = folderName + "/" + archiveFileName;
            }

            System.out.println("Writing to file: " + pathName);
            super.writeOpenMetadataArchive(pathName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }

        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }
}
