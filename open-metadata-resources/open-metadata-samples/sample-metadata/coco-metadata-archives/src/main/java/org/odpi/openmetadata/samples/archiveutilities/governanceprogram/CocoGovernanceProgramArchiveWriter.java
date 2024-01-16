/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.ResourceUse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * CocoGovernanceProgramArchiveWriter creates a physical open metadata archive file containing the core definition of Coco Pharmaceuticals'
 * governance program.
 */
public class CocoGovernanceProgramArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoGovernanceProgramArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String archiveGUID        = "ac202586-4042-407b-ae51-8096dfda223e";
    private static final String archiveName        = "Coco Pharmaceuticals Governance Program";
    private static final String archiveDescription = "The core definition of Coco Pharmaceuticals' governance program.";


    /**
     * Default constructor initializes the archive.
     */
    public CocoGovernanceProgramArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              new Date(),
              archiveFileName,
              new OpenMetadataArchive[]{ new CocoOrganizationArchiveWriter().getOpenMetadataArchive() });
    }


    /**
     * Add the content to the archive builder.
     */
    public void getArchiveContent()
    {
        writeDomains();
        writeZones();
        writeSubjectAreaDefinitions();
        writeCommunities();
        writeProjectStatusValidValueSet();
        writeProjects();
        writeRoles();
    }

    /**
     * Creates Governance Domain definitions.
     */
    private void writeDomains()
    {
        String governanceDomainSetGUID = archiveHelper.addCollection(null,
                                                                     OpenMetadataType.GOVERNANCE_DOMAIN_SET_CLASSIFICATION_NAME,
                                                                     "GovernanceDomainSet:Coco Pharmaceuticals",
                                                                     "Coco Pharmaceuticals Governance Domains",
                                                                     "List of active governance domains at Coco Pharmaceuticals.",
                                                                     null,
                                                                     null);

        for (GovernanceDomainDefinition domainDefinition : GovernanceDomainDefinition.values())
        {
            archiveHelper.addGovernanceDomainDescription(governanceDomainSetGUID,
                                                         domainDefinition.getQualifiedName(),
                                                         domainDefinition.getDomainIdentifier(),
                                                         domainDefinition.getDisplayName(),
                                                         domainDefinition.getDescription(),
                                                         null);

            String communityQName = "Community: " + domainDefinition.getQualifiedName();

            archiveHelper.addCommunity(null,
                                       communityQName,
                                       domainDefinition.getCommunityName(),
                                       "Community supporting " + domainDefinition.getDisplayName() + " that is lead by the governance domain leader and includes all the people supporting the domain.",
                                       "To provide a mechanism for communication and coordination of work across Coco Pharmaceuticals that supports this governance domain.",
                                       null,
                                       null);


            archiveHelper.addResourceListRelationship(domainDefinition.getQualifiedName(),
                                                      communityQName,
                                                      ResourceUse.SUPPORTING_PEOPLE.getResourceUse());


            String governanceOfficerQName = OpenMetadataType.GOVERNANCE_OFFICER_TYPE_NAME + ": " + domainDefinition.getQualifiedName();
            archiveHelper.addGovernanceRole(OpenMetadataType.GOVERNANCE_OFFICER_TYPE_NAME,
                                            governanceOfficerQName,
                                            domainDefinition.getDomainIdentifier(),
                                            "GOV_OFFICER:" + domainDefinition.getDomainIdentifier(),
                                            "Governance Officer for " + domainDefinition.getDisplayName(),
                                            null,
                                            null,
                                            true,
                                            1,
                                            null,
                                            null);

            if (domainDefinition.getGovernanceOfficer() != null)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(domainDefinition.getGovernanceOfficer().getQualifiedName(),
                                                                   governanceOfficerQName,
                                                                   true);
            }
        }
    }



    /**
     * Creates Governance Zone definitions.
     */
    private void writeZones()
    {
        for (CocoGovernanceZoneDefinition zoneDefinition : CocoGovernanceZoneDefinition.values())
        {
            archiveHelper.addGovernanceZone(zoneDefinition.getQualifiedName(),
                                            zoneDefinition.getZoneName(),
                                            zoneDefinition.getDisplayName(),
                                            zoneDefinition.getDescription(),
                                            zoneDefinition.getCriteria(),
                                            "Coco Pharmaceuticals",
                                            0,
                                            null
                                            );
        }
    }



    /**
     * Creates SubjectArea definitions.
     */
    private void writeSubjectAreaDefinitions()
    {
        Map<String, String> subjectAreaMap = new HashMap<>();

        for (CocoSubjectAreaDefinition subjectAreaDefinition : CocoSubjectAreaDefinition.values())
        {
            String subjectAreaGUID = archiveHelper.addSubjectAreaDefinition(subjectAreaDefinition.getQualifiedName(),
                                                                            subjectAreaDefinition.getSubjectAreaName(),
                                                                            subjectAreaDefinition.getDisplayName(),
                                                                            subjectAreaDefinition.getDescription(),
                                                                            subjectAreaDefinition.getScope(),
                                                                            subjectAreaDefinition.getUsage(),
                                                                            subjectAreaDefinition.getDomain(),
                                                                            null,
                                                                            null);

            subjectAreaMap.put(subjectAreaDefinition.getSubjectAreaName(), subjectAreaGUID);

            if (subjectAreaDefinition.getParent() != null)
            {
                archiveHelper.addSubjectAreaHierarchy(subjectAreaMap.get(subjectAreaDefinition.getParent().getSubjectAreaName()),
                                                      subjectAreaGUID);
            }
        }
    }


    /**
     * Creates Communities and related elements.
     */
    private void writeCommunities()
    {
        for (CommunityDefinition communityDefinition : CommunityDefinition.values())
        {
            archiveHelper.addCommunity(null,
                                       communityDefinition.getQualifiedName(),
                                       communityDefinition.getDisplayName(),
                                       communityDefinition.getDescription(),
                                       null,
                                       null,
                                       null);

            if (communityDefinition.getLeaders() != null)
            {
                String leaderRoleQName = "Leader: " + communityDefinition.getQualifiedName();

                archiveHelper.addPersonRole(OpenMetadataType.COMMUNITY_MEMBER_TYPE_NAME,
                                            leaderRoleQName,
                                            "Community Leader",
                                            null,
                                            null,
                                            "Community",
                                            false,
                                            0,
                                            null,
                                            null);

                archiveHelper.addCommunityMembershipRelationship(communityDefinition.getQualifiedName(), leaderRoleQName, OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_LEADER);

                for (PersonDefinition leader : communityDefinition.getLeaders())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(leader.getQualifiedName(), leaderRoleQName, true);
                }
            }

            if (communityDefinition.getMembers() != null)
            {
                String memberRoleQName = "Member: " + communityDefinition.getQualifiedName();

                archiveHelper.addPersonRole(OpenMetadataType.COMMUNITY_MEMBER_TYPE_NAME,
                                            memberRoleQName,
                                            "CommunityMember",
                                            null,
                                            null,
                                            "Community",
                                            false,
                                            0,
                                            null,
                                            null);

                archiveHelper.addCommunityMembershipRelationship(communityDefinition.getQualifiedName(), memberRoleQName, OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_CONTRIBUTOR);

                for (PersonDefinition member : communityDefinition.getMembers())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(member.getQualifiedName(), memberRoleQName, true);
                }
            }
        }
    }


    /**
     * Creates PersonRoles and related elements.
     */
    private void writeRoles()
    {
        for (GovernanceRoleDefinition roleDefinition : GovernanceRoleDefinition.values())
        {
            archiveHelper.addGovernanceRole(roleDefinition.getTypeName(),
                                            roleDefinition.getQualifiedName(),
                                            roleDefinition.getDomain().getDomainIdentifier(),
                                            roleDefinition.getIdentifier(),
                                            roleDefinition.getDisplayName(),
                                            roleDefinition.getDescription(),
                                            roleDefinition.getScope(),
                                            roleDefinition.isHeadCountSet(),
                                            roleDefinition.getHeadCount(),
                                            null,
                                            null);

            if (roleDefinition.getAppointees() != null)
            {
                for (PersonDefinition appointee : roleDefinition.getAppointees())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(appointee.getQualifiedName(),
                                                                       roleDefinition.getQualifiedName(),
                                                                       true);
                }
            }
        }
    }


    /**
     * Creates ProjectStatus valid value sets to show status for projects.
     */
    private void writeProjectStatusValidValueSet()
    {
        String validValueSetQName = openMetadataValidValueSetPrefix + ProjectStatusDefinition.validValueSetName;
        String validValueSetGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                               validValueSetQName,
                                                               ProjectStatusDefinition.validValueSetName,
                                                               ProjectStatusDefinition.validValueSetDescription,
                                                               ProjectStatusDefinition.validValueSetUsage,
                                                               ProjectStatusDefinition.validValueSetScope,
                                                               null,
                                                               false,
                                                               null);

        if (validValueSetGUID != null)
        {
            for (ProjectStatusDefinition projectStatusDefinition : ProjectStatusDefinition.values())
            {
                String validValueQName = openMetadataValidValueSetPrefix + ProjectStatusDefinition.validValueSetName + "." + projectStatusDefinition.getPreferredValue();

                String validValueGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                    validValueQName,
                                                                    projectStatusDefinition.getDisplayName(),
                                                                    null,
                                                                    ProjectStatusDefinition.validValueSetUsage,
                                                                    ProjectStatusDefinition.validValueSetScope,
                                                                    projectStatusDefinition.getPreferredValue(),
                                                                    false,
                                                                    null);

                if (validValueGUID != null)
                {
                    archiveHelper.addValidValueMembershipRelationship(validValueSetQName, validValueQName, false /* not default value */);
                }
            }
        }
    }


    /**
     * Creates Project Hierarchy and dependencies.
     */
    private void writeProjects()
    {
        for (ProjectDefinition projectDefinition : ProjectDefinition.values())
        {
            archiveHelper.addProject(null,
                                     projectDefinition.getQualifiedName(),
                                     projectDefinition.getIdentifier(),
                                     projectDefinition.getDisplayName(),
                                     projectDefinition.getDescription(),
                                     new Date(),
                                     null,
                                     projectDefinition.getProjectStatus().getPreferredValue(),
                                     projectDefinition.isCampaign(),
                                     projectDefinition.isTask(),
                                     projectDefinition.getProjectTypeClassification(),
                                     null,
                                     null);

            String projectManagerQName = projectDefinition.getQualifiedName() + ":ProjectManager";

            archiveHelper.addPersonRole(OpenMetadataType.PROJECT_MANAGER_TYPE_NAME,
                                        projectManagerQName,
                                        projectDefinition.getIdentifier() + ":ProjectManager",
                                        projectDefinition.getDisplayName() + " project manager",
                                        null,
                                        null,
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
                                                                   true);

            }

            if (projectDefinition.getMembers() != null)
            {
                for (PersonDefinition member : projectDefinition.getMembers())
                {
                    archiveHelper.addProjectTeamRelationship(projectDefinition.getQualifiedName(),
                                                             member.getQualifiedName(),
                                                             null);
                }

            }
        }
    }
}
