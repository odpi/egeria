/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * CocoOrganizationArchiveWriter creates a physical open metadata archive file containing basic definitions for Coco Pharmaceuticals'
 * featured persona.  This includes the definition of the organizations they work with and
 */
public class CocoOrganizationArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoOrganizationArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String archiveGUID        = "3250f0f9-99fc-4af5-bed9-b746577f119b";
    private static final String archiveName        = "Coco Pharmaceuticals' organization and operations";
    private static final String archiveDescription = "The base definitions for Coco Pharmaceuticals' organization and business capabilities.";

    private static final Date   creationDate       = new Date(1639984840038L);

    /**
     * Default constructor initializes the archive.
     */
    public CocoOrganizationArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName);
    }


    /**
     * Add the content to the archive builder.
     */
    @Override
    public void getArchiveContent()
    {
        writeContactTypesValidValueSet();
        writeCountryCodesValidValueSet();
        writeEmployeeTypeValidValueSet();
        writeJobLevelDefinitionValidValueSet();
        writeOrganizationTypeValidValueSet();
        writeWorkLocationsValidValueSet();

        writeBusinessAreas();
        writeOrganizations();
        writePersonProfiles();
        writeTeams();
    }


    /**
     * Creates the ContactType valid value set for the contactType additional property of a Person entity
     */
    private void writeContactTypesValidValueSet()
    {
        for (ContactTypeDefinition contactTypeDefinition : ContactTypeDefinition.values())
        {
            this.addValidMetadataValue(contactTypeDefinition.getDisplayName(),
                                       contactTypeDefinition.getDescription(),
                                       OpenMetadataProperty.CONTACT_TYPE.name,
                                       null,
                                       null,
                                       contactTypeDefinition.getPreferredValue());
        }
    }


    /**
     * Creates the JobLevel valid value set for the jobLevel additional property of a Person entity
     */
    private void writeJobLevelDefinitionValidValueSet()
    {
        for (JobLevelDefinition jobLevelDefinition : JobLevelDefinition.values())
        {
            this.addValidMetadataValue(jobLevelDefinition.getDisplayName(),
                                       jobLevelDefinition.getDescription(),
                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                       null,
                                       JobLevelDefinition.validValueSetPropertyName,
                                       jobLevelDefinition.getPreferredValue());
        }
    }


    /**
     * Creates the EmployeeType valid value set for the employeeType property of a Person entity
     */
    private void writeEmployeeTypeValidValueSet()
    {
        for (EmployeeTypeDefinition employeeTypeDefinition : EmployeeTypeDefinition.values())
        {
            this.addValidMetadataValue(employeeTypeDefinition.getDisplayName(),
                                       employeeTypeDefinition.getDescription(),
                                       OpenMetadataProperty.EMPLOYEE_TYPE.name,
                                       null,
                                       null,
                                       employeeTypeDefinition.getPreferredValue());
        }
    }


    /**
     * Creates the OrganizationType valid value set for the teamType property of a Team/Organization entity
     */
    private void writeOrganizationTypeValidValueSet()
    {
        for (OrganizationTypeDefinition organizationTypeDefinition : OrganizationTypeDefinition.values())
        {
            this.addValidMetadataValue(organizationTypeDefinition.getDisplayName(),
                                       organizationTypeDefinition.getDescription(),
                                       OpenMetadataProperty.TEAM_TYPE.name,
                                       null,
                                       null,
                                       organizationTypeDefinition.getPreferredValue());
        }
    }


    /**
     * Creates WorkLocation valid value set to show where employees work.
     */
    private void writeWorkLocationsValidValueSet()
    {
        for (WorkLocationDefinition workLocationDefinition : WorkLocationDefinition.values())
        {
            this.addValidMetadataValue(workLocationDefinition.getDisplayName(),
                                       workLocationDefinition.getPostalAddress(),
                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                       null,
                                       WorkLocationDefinition.validValueSetPropertyName,
                                       workLocationDefinition.getWorkLocationId());
        }
    }


    /**
     * Creates CountryCode valid value set to show associated country for employees and locations.
     */
    private void writeCountryCodesValidValueSet()
    {
        for (CountryCodeDefinition countryCodeDefinition : CountryCodeDefinition.values())
        {
            this.addValidMetadataValue(countryCodeDefinition.getDisplayName(),
                                       null,
                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                       null,
                                       CountryCodeDefinition.validValueSetPropertyName,
                                       countryCodeDefinition.getPreferredValue());
        }
    }


    /**
     * Creates Person actor profiles, user identities and contact details.
     */
    private void writePersonProfiles()
    {
        for (PersonDefinition personDefinition : PersonDefinition.values())
        {
            String userIdQualifiedName = "UserIdentity:" + personDefinition.getDistinguishedName();

            Map<String, String> additionalProperties = new HashMap<>();

            if (personDefinition.getJobLevel() != null)
            {
                additionalProperties.put(JobLevelDefinition.validValueSetPropertyName, personDefinition.getJobLevel().getPreferredValue());
            }
            if (personDefinition.getOrganization() != null)
            {
                additionalProperties.put(OrganizationDefinition.propertyName, personDefinition.getOrganization().getDisplayName());
            }
            if (personDefinition.getWorkLocation() != null)
            {
                additionalProperties.put(WorkLocationDefinition.validValueSetPropertyName, personDefinition.getWorkLocation().getWorkLocationId());
            }
            if (personDefinition.getCountryCode() != null)
            {
                additionalProperties.put(CountryCodeDefinition.validValueSetPropertyName, personDefinition.getCountryCode().getPreferredValue());
            }

            String profileGUID = archiveHelper.addPerson(personDefinition.getQualifiedName(),
                                                         personDefinition.getDisplayName(),
                                                         personDefinition.getPronouns(),
                                                         null,
                                                         personDefinition.getInitials(),
                                                         personDefinition.getTitle(),
                                                         personDefinition.getGivenNames(),
                                                         personDefinition.getSurname(),
                                                         personDefinition.getFullName(),
                                                         personDefinition.getJobTitle(),
                                                         personDefinition.getEmployeeNumber(),
                                                         personDefinition.getEmployeeType().getPreferredValue(),
                                                         null,
                                                         additionalProperties);

            String userIdGUID  = archiveHelper.addUserIdentity(userIdQualifiedName, personDefinition.getUserId(), personDefinition.getDistinguishedName(), null);

            archiveHelper.addProfileIdentity(profileGUID, userIdGUID, null, null, null);

            if (personDefinition.getEmail() != null)
            {
                archiveHelper.addContactDetails(profileGUID,
                                                OpenMetadataType.PERSON.typeName,
                                                ContactTypeDefinition.COMPANY_EMAIL.getDisplayName(),
                                                ContactTypeDefinition.COMPANY_EMAIL.getPreferredValue(),
                                                ContactMethodType.EMAIL.getOrdinal(),
                                                ContactTypeDefinition.COMPANY_EMAIL.getDescription(),
                                                personDefinition.getEmail());
            }
        }
    }


    /**
     * Creates Business capabilities for each of Coco's business areas.
     */
    private void writeBusinessAreas()
    {
        for (BusinessAreaDefinition businessAreaDefinition : BusinessAreaDefinition.values())
        {
            archiveHelper.addBusinessArea(businessAreaDefinition.getQualifiedName(),
                                          businessAreaDefinition.getIdentifier(),
                                          businessAreaDefinition.getDisplayName(),
                                          businessAreaDefinition.getDescription(),
                                          null);
        }
    }


    /**
     * Creates Organization actor profiles and related elements.
     */
    private void writeOrganizations()
    {
        for (OrganizationDefinition organizationDefinition : OrganizationDefinition.values())
        {
            archiveHelper.addTeam(OpenMetadataType.ORGANIZATION.typeName,
                                  organizationDefinition.getQualifiedName(),
                                  organizationDefinition.getDisplayName(),
                                  organizationDefinition.getDescription(),
                                  organizationDefinition.getOrganizationType().getPreferredValue(),
                                  null,
                                  null);
        }
    }


    /**
     * Creates Team actor profiles and related elements.
     */
    private void writeTeams()
    {
        for (DeptDefinition deptDefinition : DeptDefinition.values())
        {
            archiveHelper.addTeam(null,
                                  deptDefinition.getQualifiedName(),
                                  deptDefinition.getDisplayName(),
                                  deptDefinition.getDescription(),
                                  OrganizationTypeDefinition.DEPT.getPreferredValue(),
                                  deptDefinition.getTeamId(),
                                  null);

            String superTeamQName;
            if (deptDefinition.getSuperTeam() == null)
            {
                superTeamQName = "Organization::" + OrganizationDefinition.COCO.getDisplayName();
            }
            else
            {
                superTeamQName = deptDefinition.getSuperTeam().getQualifiedName();
            }

            archiveHelper.addTeamStructureRelationship(superTeamQName, deptDefinition.getQualifiedName(), true);

            String leadershipRoleQName = "TeamLeader::" + deptDefinition.getQualifiedName();
            archiveHelper.addActorRole(OpenMetadataType.TEAM_LEADER.typeName,
                                       leadershipRoleQName,
                                       leadershipRoleQName,
                                       null,
                                       null,
                                       null,
                                       true,
                                       1,
                                       null,
                                       null);

            archiveHelper.addTeamLeadershipRelationship(leadershipRoleQName, deptDefinition.getQualifiedName(), AssignmentType.LEADER.getName());

            if (deptDefinition.getLeaders() != null)
            {
                for (PersonDefinition appointee : deptDefinition.getLeaders())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(appointee.getQualifiedName(),
                                                                       leadershipRoleQName,
                                                                       false,
                                                                       0);
                }
            }

            String membershipRoleQName = "TeamMembers::" + deptDefinition.getQualifiedName();
            archiveHelper.addActorRole(OpenMetadataType.TEAM_MEMBER.typeName,
                                       membershipRoleQName,
                                       membershipRoleQName,
                                       null,
                                       null,
                                       null,
                                       false,
                                       0,
                                       null,
                                       null);

            archiveHelper.addTeamMembershipRelationship(membershipRoleQName, deptDefinition.getQualifiedName(), AssignmentType.CONTRIBUTOR.getName());

            if (deptDefinition.getMembers() != null)
            {
                for (PersonDefinition appointee : deptDefinition.getLeaders())
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(appointee.getQualifiedName(),
                                                                       membershipRoleQName,
                                                                       false,
                                                                       0);
                }
            }

            if (deptDefinition.getAdditionalMembers() != 0)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(getAnonymousMember(deptDefinition.getWorkLocation()),
                                                                   membershipRoleQName,
                                                                   false,
                                                                   0);
            }

            if (deptDefinition.getBusinessArea() != null)
            {
                archiveHelper.addBusinessCapabilityTeamRelationship(deptDefinition.getBusinessArea().getQualifiedName(),
                                                                    deptDefinition.getQualifiedName(),
                                                                    AssignmentType.OWNER.getName(),
                                                                    deptDefinition.getBusinessAreaScope().getPreferredValue());
            }
        }
    }


    final static String distinguishedNamePattern = "cn={0},ou=people,ou=users,o=cocoPharma";
    final static String userIdPattern = "ANON{0, number, 000000}";

    private int anonymousEmployeeCount = 0;


    private String getAnonymousMember(WorkLocationDefinition workLocationDefinition)
    {
        anonymousEmployeeCount ++;

        Object[] messageParameters = { anonymousEmployeeCount };

        MessageFormat mf = new MessageFormat(userIdPattern);

        String userId = mf.format(messageParameters);

        Object[] dnMessageParameters = { userId };

        mf = new MessageFormat(distinguishedNamePattern);
        String distinguishedName = mf.format(dnMessageParameters);

        String userIdQualifiedName = "UserIdentity:" + distinguishedName;

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(OrganizationDefinition.propertyName, OrganizationDefinition.COCO.getDisplayName());
        additionalProperties.put(WorkLocationDefinition.validValueSetPropertyName,workLocationDefinition.getWorkLocationId());

        String personQName = "Person:Anon:" + userId;
        String profileGUID = archiveHelper.addPerson(personQName,
                                                     userId,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     userId,
                                                     EmployeeTypeDefinition.FULL_TIME_PERMANENT.getPreferredValue(),
                                                     null,
                                                     additionalProperties);

        String userIdGUID  = archiveHelper.addUserIdentity(userIdQualifiedName, userId, distinguishedName, null);

        archiveHelper.addProfileIdentity(profileGUID, userIdGUID, null, null, null);

        return personQName;
    }
}
