/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ActorRoleGroup;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CocoGovernanceProgramArchiveWriter creates a physical open metadata archive file containing the core definition of Coco Pharmaceuticals'
 * governance program.
 */
public class CocoGovernanceProgramArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoGovernanceProgramArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String archiveGUID        = "ac202586-4042-407b-ae51-8096dfda223e";
    private static final String archiveName        = "Coco Pharmaceuticals Governance Program";
    private static final String archiveDescription = "The core definition of Coco Pharmaceuticals' governance program.";

    private static final Date   creationDate       = new Date(1639984840038L);

    /**
     * Default constructor initializes the archive.
     */
    public CocoGovernanceProgramArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive(),
                                         new CocoOrganizationArchiveWriter().getOpenMetadataArchive() });
    }


    /**
     * Add the content to the archive builder.
     */
    @Override
    public void getArchiveContent()
    {
        writeDomains();
        writeCocoCollections();
        writeLicenseTypes();
        writeCertificationTypes();
        writeDataProcessingPurposes();
        writeSubjectAreaDefinitions();
        writeCommunities();
        writeProjects();
        writeRoles();
    }

    /**
     * Creates Governance Domain definitions.
     */
    private void writeDomains()
    {
        for (GovernanceDomainDefinition domainDefinition : GovernanceDomainDefinition.values())
        {
            super.addValidMetadataValue(null,
                                        domainDefinition.getDisplayName(),
                                        domainDefinition.getDescription(),
                                        OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                        DataType.INT.getDisplayName(),
                                        null,
                                        null,
                                        Integer.toString(domainDefinition.getDomainIdentifier()),
                                        domainDefinition.getDomainIdentifier(),
                                        (domainDefinition.getDomainIdentifier() == 0),
                                        false,
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
                                                      ResourceUse.SUPPORTING_PEOPLE.getResourceUse(),
                                                      null);


            String governanceOfficerQName = OpenMetadataType.GOVERNANCE_ROLE.typeName + ": " + domainDefinition.getQualifiedName();
            archiveHelper.addGovernanceRole(OpenMetadataType.GOVERNANCE_ROLE.typeName,
                                            List.of(ActorRoleGroup.GOVERNANCE_OFFICER.getName()),
                                            governanceOfficerQName,
                                            domainDefinition.getDomainIdentifier(),
                                            "GOV_OFFICER:" + domainDefinition.getDomainIdentifier(),
                                            "Governance Officer for " + domainDefinition.getDisplayName(),
                                            null,
                                            true,
                                            1,
                                            null,
                                            null);

            if (domainDefinition.getGovernanceOfficer() != null)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(domainDefinition.getGovernanceOfficer().getQualifiedName(),
                                                                   governanceOfficerQName,
                                                                   false,
                                                                   0);
            }
        }
    }


    public void writeCocoCollections()
    {
        for (CocoCollectionDefinition collectionDefinition : CocoCollectionDefinition.values())
        {
            archiveHelper.setGUID(collectionDefinition.getQualifiedName(), collectionDefinition.getGUID());

            if (collectionDefinition.getParent() == null)
            {
                archiveHelper.addCollection(collectionDefinition.getTypeName(),
                                            null,
                                            null,
                                            null,
                                            null,
                                            collectionDefinition.getClassificationName(),
                                            collectionDefinition.getQualifiedName(),
                                            collectionDefinition.getDisplayName(),
                                            collectionDefinition.getDescription(),
                                            collectionDefinition.getCategory(),
                                            null,
                                            null,
                                            null);
            }
            else
            {
                archiveHelper.addCollection(collectionDefinition.getTypeName(),
                                            collectionDefinition.getParent().getGUID(),
                                            collectionDefinition.getParent().getTypeName(),
                                            OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                            null,
                                            collectionDefinition.getClassificationName(),
                                            collectionDefinition.getQualifiedName(),
                                            collectionDefinition.getDisplayName(),
                                            collectionDefinition.getDescription(),
                                            collectionDefinition.getCategory(),
                                            null,
                                            null,
                                            null);

                archiveHelper.addMemberToCollection(collectionDefinition.getParent().getGUID(),
                                                    collectionDefinition.getGUID(),
                                                    null);
            }
        }
    }

    /**
     * Creates LicenceType definitions.
     */
    private void writeLicenseTypes()
    {
        for (CocoLicenseTypeDefinition licenseTypeDefinition : CocoLicenseTypeDefinition.values())
        {
            archiveHelper.addGovernanceDefinition(OpenMetadataType.LICENSE_TYPE.typeName,
                                                  licenseTypeDefinition.getQualifiedName(),
                                                  licenseTypeDefinition.getIdentifier(),
                                                  licenseTypeDefinition.getDisplayName(),
                                                  licenseTypeDefinition.getSummary(),
                                                  licenseTypeDefinition.getScope().getPreferredValue(),
                                                  licenseTypeDefinition.getDescription(),
                                                  licenseTypeDefinition.getDetails(),
                                                  0,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null);
        }
    }


    /**
     * Creates CertificationType definitions.
     */
    private void writeCertificationTypes()
    {
        final String methodName = "writeCertificationTypes";

        for (CocoCertificationTypeDefinition certificationTypeDefinition : CocoCertificationTypeDefinition.values())
        {
            archiveHelper.setGUID(certificationTypeDefinition.getQualifiedName(), certificationTypeDefinition.getGUID());

            String guid = archiveHelper.addGovernanceDefinition(OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                                                certificationTypeDefinition.getQualifiedName(),
                                                                certificationTypeDefinition.getIdentifier(),
                                                                certificationTypeDefinition.getDisplayName(),
                                                                certificationTypeDefinition.getSummary(),
                                                                certificationTypeDefinition.getDescription(),
                                                                certificationTypeDefinition.getScope().getPreferredValue(),
                                                                certificationTypeDefinition.getDetails(),
                                                                0,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null);

            assert (certificationTypeDefinition.getGUID().equals(guid));

            if (certificationTypeDefinition.isTemplate())
            {
                archiveHelper.addTemplateClassification(certificationTypeDefinition.getGUID(),
                                                        certificationTypeDefinition.getTemplateName(),
                                                        certificationTypeDefinition.getTemplateDescription(),
                                                        "V1.0",
                                                        null,
                                                        methodName);
            }
        }
    }


    /**
     * Creates DataProcessing definitions.
     */
    private void writeDataProcessingPurposes()
    {
        for (CocoDataProcessingPurposeDefinition dataProcessingPurposeDefinition : CocoDataProcessingPurposeDefinition.values())
        {
            archiveHelper.addGovernanceDefinition(OpenMetadataType.DATA_PROCESSING_PURPOSE.typeName,
                                                  dataProcessingPurposeDefinition.getQualifiedName(),
                                                  null,
                                                  dataProcessingPurposeDefinition.getDisplayName(),
                                                  dataProcessingPurposeDefinition.getSummary(),
                                                  dataProcessingPurposeDefinition.getScope().getPreferredValue(),
                                                  dataProcessingPurposeDefinition.getDescription(),
                                                  dataProcessingPurposeDefinition.getDetails(),
                                                  0,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null);
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
        for (CocoCommunityDefinition communityDefinition : CocoCommunityDefinition.values())
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

                archiveHelper.addActorRole(OpenMetadataType.PERSON_ROLE.typeName,
                                           List.of(ActorRoleGroup.COMMUNITY_MEMBER.getName()),
                                           leaderRoleQName,
                                           "Community Leader of " + communityDefinition.getDisplayName(),
                                           null,
                                           null,
                                           "Community",
                                           false,
                                           0,
                                           null,
                                           null);

                archiveHelper.addCommunityMembershipRelationship(communityDefinition.getQualifiedName(), leaderRoleQName, AssignmentType.LEADER.getDisplayName());

                for (PersonDefinition leader : communityDefinition.getLeaders())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(leader.getQualifiedName(), leaderRoleQName, false, 0);
                }
            }

            if (communityDefinition.getMembers() != null)
            {
                String memberRoleQName = "Member: " + communityDefinition.getQualifiedName();

                archiveHelper.addActorRole(OpenMetadataType.PERSON_ROLE.typeName,
                                           List.of(ActorRoleGroup.COMMUNITY_MEMBER.getName()),
                                           memberRoleQName,
                                           "Community Member of " + communityDefinition.getDisplayName(),
                                           null,
                                           null,
                                           "Community",
                                           false,
                                           0,
                                           null,
                                           null);

                archiveHelper.addCommunityMembershipRelationship(communityDefinition.getQualifiedName(), memberRoleQName, AssignmentType.CONTRIBUTOR.getDisplayName());

                for (PersonDefinition member : communityDefinition.getMembers())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(member.getQualifiedName(), memberRoleQName, false, 0);
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
            List<String> personRoleGroup = null;

            if (roleDefinition.getGroupName() != null)
            {
                personRoleGroup = List.of(roleDefinition.getGroupName());
            }

            archiveHelper.addGovernanceRole(OpenMetadataType.GOVERNANCE_ROLE.typeName,
                                            personRoleGroup,
                                            roleDefinition.getQualifiedName(),
                                            roleDefinition.getDomain().getDomainIdentifier(),
                                            roleDefinition.getIdentifier(),
                                            roleDefinition.getDisplayName(),
                                            roleDefinition.getDescription(),
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
                                                                       false,
                                                                       0);
                }
            }
        }
    }



    /**
     * Creates Project Hierarchy and dependencies.
     */
    private void writeProjects()
    {
        for (CocoProjectDefinition projectDefinition : CocoProjectDefinition.values())
        {
            archiveHelper.addProject(null,
                                     projectDefinition.getQualifiedName(),
                                     projectDefinition.getIdentifier(),
                                     projectDefinition.getDisplayName(),
                                     projectDefinition.getDescription(),
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
                for (CocoProjectDefinition dependentOnProject : projectDefinition.getDependentOn())
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
                for (PersonDefinition member : projectDefinition.getMembers())
                {
                    archiveHelper.addProjectTeamRelationship(projectDefinition.getQualifiedName(),
                                                             member.getQualifiedName(),
                                                             AssignmentType.CONTRIBUTOR.getDisplayName());
                }

            }
        }
    }
}
