/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * CocoSustainabilityArchiveWriter creates a physical open metadata archive file containing basic definitions for Coco Pharmaceuticals'
 * sustainability initiative.
 */
public class CocoSustainabilityArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoSustainabilityArchive.omarchive";

    private static final String sustainabilitySubjectArea = "SubjectArea:Sustainability";
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "be351568-97ec-4c34-bca5-aff93f326d9e";
    private static final String                  archiveName        = "Coco Pharmaceuticals Sustainability Project";
    private static final String                  archiveDescription = "The base definitions for Coco Pharmaceuticals' sustainability initiative.";

    private static final Date                    creationDate       = new Date(1639984840038L);

    /**
     * Default constructor initializes the archive.
     */
    public CocoSustainabilityArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive(),
                                         new CocoOrganizationArchiveWriter().getOpenMetadataArchive(),
                                         new CocoGovernanceProgramArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Add the content to the archive builder.
     */
    @Override
    public void getArchiveContent()
    {
        writeDomains();
        writeSubjectAreaDefinitions();
        writeFacilityTypeValidValueSet();
        writeGlossary();
        writeGovernanceDefinitions();
        writeRoles();
        writeFacility();
    }


    /**
     * Creates the FacilityType valid value set for tagging physical locations.
     */
    private void writeFacilityTypeValidValueSet()
    {
        String validValueSetQName = OpenMetadataType.VALID_VALUE_DEFINITION.typeName + "::" + FacilityTypeDefinition.validValueSetName;

        String validValueSetGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                               validValueSetQName,
                                                               FacilityTypeDefinition.validValueSetName,
                                                               FacilityTypeDefinition.validValueSetDescription,
                                                               FacilityTypeDefinition.validValueSetUsage,
                                                               FacilityTypeDefinition.validValueSetScope,
                                                               null,
                                                               null,
                                                               null);

        if (validValueSetGUID != null)
        {
            archiveHelper.addSubjectAreaClassification(validValueSetGUID, sustainabilitySubjectArea);

            for (FacilityTypeDefinition facilityTypeDefinition : FacilityTypeDefinition.values())
            {
                String validValueGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                    facilityTypeDefinition.getQualifiedName(),
                                                                    facilityTypeDefinition.getDisplayName(),
                                                                    facilityTypeDefinition.getDescription(),
                                                                    FacilityTypeDefinition.validValueSetUsage,
                                                                    FacilityTypeDefinition.validValueSetScope,
                                                                    facilityTypeDefinition.getPreferredValue(),
                                                                    null,
                                                                    null);

                if (validValueGUID != null)
                {
                    archiveHelper.addValidValueMembershipRelationship(validValueSetQName, facilityTypeDefinition.getQualifiedName(), false /* not default value */);
                }
            }
        }
    }


    /**
     * Creates SubjectArea definitions.
     */
    private void writeSubjectAreaDefinitions()
    {
        Map<String, String> subjectAreaMap = new HashMap<>();

        for (SustainabilitySubjectAreaDefinition subjectAreaDefinition : SustainabilitySubjectAreaDefinition.values())
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
                String subjectAreaParentGUID = archiveHelper.queryGUID(subjectAreaDefinition.getParent().getQualifiedName());
                archiveHelper.addSubjectAreaHierarchy(subjectAreaParentGUID, subjectAreaGUID);
            }
        }
    }



    private void writeGovernanceDefinitions()
    {
        for (GovernanceDefinition governanceDefinition : GovernanceDefinition.values())
        {
            archiveHelper.setGUID(governanceDefinition.getQualifiedName(), governanceDefinition.getGUID());
            String governanceDefinitionGUID = archiveHelper.addGovernanceDefinition(governanceDefinition.getType(),
                                                                                    governanceDefinition.getQualifiedName(),
                                                                                    governanceDefinition.getTitle(),
                                                                                    governanceDefinition.getSummary(),
                                                                                    governanceDefinition.getDescription(),
                                                                                    governanceDefinition.getScope().getPreferredValue(),
                                                                                    null,
                                                                                    governanceDefinition.getDomain(),
                                                                                    governanceDefinition.getImportance(),
                                                                                    governanceDefinition.getImplications(),
                                                                                    governanceDefinition.getOutcomes(),
                                                                                    governanceDefinition.getResults(),
                                                                                    null,
                                                                                    null);

            assert governanceDefinition.getGUID().equals(governanceDefinitionGUID);
        }

        for (GovernanceDefinitionLink link : GovernanceDefinitionLink.values())
        {
            archiveHelper.addGovernanceDefinitionDelegationRelationship(link.getRelationshipType(),
                                                                        link.getParentDefinition().getQualifiedName(),
                                                                        link.getChildDefinition().getQualifiedName(),
                                                                        null);
        }
    }


    /**
     * Creates Governance Role definitions and links them to .
     */
    private void writeRoles()
    {
        String communityQName = "Community:: " + SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getQualifiedName();

        for (SustainabilityRoleDefinition roleDefinition : SustainabilityRoleDefinition.values())
        {
            archiveHelper.addGovernanceRole(roleDefinition.getTypeName(),
                                            roleDefinition.getQualifiedName(),
                                            SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                                            roleDefinition.getIdentifier(),
                                            roleDefinition.getDisplayName(),
                                            roleDefinition.getDescription(),
                                            roleDefinition.getScope().getPreferredValue(),
                                            roleDefinition.isHeadCountSet(),
                                            roleDefinition.getHeadCount(),
                                            null,
                                            null);

            archiveHelper.addCommunityMembershipRelationship(communityQName,
                                                             roleDefinition.getQualifiedName(),
                                                             AssignmentType.CONTRIBUTOR.getName());

            if (roleDefinition.getBusinessArea() != null)
            {
                archiveHelper.addAssignmentScopeRelationship(roleDefinition.getQualifiedName(),
                                                             roleDefinition.getBusinessArea().getQualifiedName(),
                                                             null,
                                                             null);
            }

            if (roleDefinition.getAppointee() != null)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(roleDefinition.getAppointee().getQualifiedName(),
                                                                   roleDefinition.getQualifiedName(),
                                                                   false,
                                                                   0);
            }

            if (roleDefinition.getGovernanceResponsibility() != null)
            {
                archiveHelper.addGovernedByRelationship(roleDefinition.getQualifiedName(),
                                                        roleDefinition.getGovernanceResponsibility().getQualifiedName());
            }
        }
    }


    /**
     * Creates Governance Domain definitions.
     */
    private void writeDomains()
    {
        String governanceDomainSetGUID = this.getParentSet(null,
                                                           null,
                                                           OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                           null);

        for (SustainabilityDomainDefinition domainDefinition : SustainabilityDomainDefinition.values())
        {
            this.archiveHelper.addValidValue(null,
                                             governanceDomainSetGUID,
                                             governanceDomainSetGUID,
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                             null,
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                             domainDefinition.getQualifiedName(),
                                             domainDefinition.getDisplayName(),
                                             domainDefinition.getDescription(),
                                             domainDefinition.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             DataType.INT.getName(),
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             Integer.toString(domainDefinition.getDomainIdentifier()),
                                             null,
                                             false,
                                             null);

            String communityQName = "Community:: " + domainDefinition.getQualifiedName();

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


            String governanceOfficerQName = OpenMetadataType.GOVERNANCE_OFFICER.typeName + ":: " + domainDefinition.getQualifiedName();
            archiveHelper.addGovernanceRole(OpenMetadataType.GOVERNANCE_OFFICER.typeName,
                                            governanceOfficerQName,
                                            domainDefinition.getDomainIdentifier(),
                                            "GOV_OFFICER::" + domainDefinition.getDomainIdentifier(),
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
                                                                   false,
                                                                   0);
            }
        }
    }


    private void writeGlossary()
    {
        String glossaryGUID = archiveHelper.addGlossary("Glossary::Sustainability",
                                                        "Sustainability Glossary",
                                                        "Terminology associated with Coco Pharmaceutical's sustainability initiative.",
                                                        "English",
                                                        "For all Coco Pharmaceutical employees wishing to understand more about sustainability and the organization's efforts to improve its operations.",
                                                        null,
                                                        ScopeDefinition.WITHIN_ORGANIZATION.getPreferredValue());

        archiveHelper.addSubjectAreaClassification(glossaryGUID, sustainabilitySubjectArea);

        Map<String, String> categoryLookup = new HashMap<>();
        for (GlossaryCategoryDefinition glossaryCategoryDefinition : GlossaryCategoryDefinition.values())
        {
            String glossaryCategoryGUID = archiveHelper.addGlossaryCategory(glossaryGUID,
                                                                            glossaryCategoryDefinition.getQualifiedName(),
                                                                            glossaryCategoryDefinition.getName(),
                                                                            glossaryCategoryDefinition.getDescription(),
                                                                            null);

            categoryLookup.put(glossaryCategoryDefinition.getName(), glossaryCategoryGUID);
        }

        for (GlossaryTermDefinition glossaryTermDefinition : GlossaryTermDefinition.values())
        {
            String glossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                            null,
                                                            false,
                                                            "GlossaryTerm::" + glossaryTermDefinition.getName(),
                                                            glossaryTermDefinition.getName(),
                                                            glossaryTermDefinition.getSummary(),
                                                            glossaryTermDefinition.getDescription(),
                                                            null,
                                                            glossaryTermDefinition.getAbbreviation(),
                                                            null,
                                                            false,
                                                            null,
                                                            null,
                                                            null,
                                                            null);

            if (glossaryTermDefinition.getCategory() != null)
            {
                archiveHelper.addTermToCategory(categoryLookup.get(glossaryTermDefinition.getCategory().getName()),
                                                glossaryTermGUID);
            }

            if (glossaryTermDefinition.getUrl() != null)
            {
                archiveHelper.addExternalReference(null,
                                                   glossaryTermGUID,
                                                   OpenMetadataType.GLOSSARY_TERM.typeName,
                                                   OpenMetadataType.GLOSSARY_TERM.typeName,
                                                   glossaryGUID,
                                                   OpenMetadataType.EXTERNAL_REFERENCE.typeName + "::" + glossaryTermDefinition.getName(),
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   originatorName,
                                                   null,
                                                   glossaryTermDefinition.getUrl(),
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null);
            }
        }
    }


    /**
     * Creates Facility and links to related elements.
     */
    private void writeFacility()
    {
        for (FacilityDefinition facilityDefinition : FacilityDefinition.values())
        {
            String locationGUID;

            if (facilityDefinition.getAssociatedWorkLocation() != null)
            {
                locationGUID = archiveHelper.addFixedLocation(facilityDefinition.getQualifiedName(),
                                                              facilityDefinition.getIdentifier(),
                                                              facilityDefinition.getDisplayName(),
                                                              facilityDefinition.getDescription(),
                                                              null,
                                                              null,
                                                              facilityDefinition.getAssociatedWorkLocation().getPostalAddress(),
                                                              facilityDefinition.getAssociatedWorkLocation().getTimeZone(),
                                                              null);

                String validValueGUID = archiveHelper.getGUID(openMetadataValidValueSetPrefix + facilityDefinition.getAssociatedWorkLocation().getQualifiedName());

                archiveHelper.addMoreInformationLink(validValueGUID, locationGUID);
            }
            else
            {
                locationGUID = archiveHelper.addFixedLocation(facilityDefinition.getQualifiedName(),
                                                              facilityDefinition.getIdentifier(),
                                                              facilityDefinition.getDisplayName(),
                                                              facilityDefinition.getDescription(),
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              null);
            }

            if (facilityDefinition.getParentSite() != null)
            {
                String parentGUID = archiveHelper.getGUID(facilityDefinition.getParentSite().getQualifiedName());

                archiveHelper.addLocationHierarchy(parentGUID, locationGUID);
            }

            if (facilityDefinition.getFacilityType() != null)
            {
                archiveHelper.addReferenceValueAssignmentRelationship(facilityDefinition.getQualifiedName(),
                                                                      facilityDefinition.getFacilityType().getQualifiedName(),
                                                                      100,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null);
            }

            if (facilityDefinition.getFacilityLeaderRole() != null)
            {
                archiveHelper.addAssignmentScopeRelationship(facilityDefinition.getFacilityLeaderRole().getQualifiedName(),
                                                             facilityDefinition.getQualifiedName(),
                                                             null,
                                                             null);

                if (facilityDefinition.getFacilityLeader() != null)
                {
                    archiveHelper.addPersonRoleAppointmentRelationship(facilityDefinition.getFacilityLeader().getQualifiedName(),
                                                                       facilityDefinition.getFacilityLeaderRole().getQualifiedName(),
                                                                       false,
                                                                       0);
                }
            }
        }
    }
}
