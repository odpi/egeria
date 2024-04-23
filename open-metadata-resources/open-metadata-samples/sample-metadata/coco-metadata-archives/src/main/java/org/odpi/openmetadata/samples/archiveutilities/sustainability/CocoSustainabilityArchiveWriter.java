/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * CocoSustainabilityArchiveWriter creates a physical open metadata archive file containing basic definitions for Coco Pharmaceuticals'
 * sustainability initiative.
 */
public class CocoSustainabilityArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoSustainabilityArchive.omarchive";

    private static final String sustainabilitySubjectArea = "SubjectArea:Sustainability";
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "be351568-97ec-4c34-bca5-aff93f326d9e";
    private static final String                  archiveName        = "Coco Pharmaceuticals Sustainability Project";
    private static final String                  archiveDescription = "The base definitions for Coco Pharmaceuticals' sustainability initiative.";


    /**
     * Default constructor initializes the archive.
     */
    public CocoSustainabilityArchiveWriter()
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
        writeFacilityTypeValidValueSet();
        writeGlossary();
        writeRoles();
        writeFacility();
    }


    /**
     * Creates the FacilityType valid value set for tagging physical locations.
     */
    private void writeFacilityTypeValidValueSet()
    {
        String validValueSetQName = OpenMetadataType.VALID_VALUE_SET.typeName + ":" + FacilityTypeDefinition.validValueSetName;

        String validValueSetGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_SET.typeName,
                                                               validValueSetQName,
                                                               FacilityTypeDefinition.validValueSetName,
                                                               FacilityTypeDefinition.validValueSetDescription,
                                                               FacilityTypeDefinition.validValueSetUsage,
                                                               FacilityTypeDefinition.validValueSetScope,
                                                               null,
                                                               false,
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
                                                                    false,
                                                                    null);

                if (validValueGUID != null)
                {
                    archiveHelper.addValidValueMembershipRelationship(validValueSetQName, facilityTypeDefinition.getQualifiedName(), false /* not default value */);
                }
            }
        }
    }


    /**
     * Creates Governance Role definitions.
     */
    private void writeRoles()
    {
        for (SustainabilityRoleDefinition roleDefinition : SustainabilityRoleDefinition.values())
        {
            archiveHelper.addGovernanceRole(roleDefinition.getTypeName(),
                                            roleDefinition.getQualifiedName(),
                                            9,
                                            roleDefinition.getIdentifier(),
                                            roleDefinition.getDisplayName(),
                                            roleDefinition.getDescription(),
                                            roleDefinition.getScope().getPreferredValue(),
                                            roleDefinition.isHeadCountSet(),
                                            roleDefinition.getHeadCount(),
                                            null,
                                            null);

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
                                                                   true);
            }
        }
    }


    private void writeGlossary()
    {
        String glossaryGUID = archiveHelper.addGlossary("Glossary:Sustainability",
                                                        "Sustainability Glossary",
                                                        "Terminology associated with Coco Pharmaceutical's sustainability initiative.",
                                                        "English",
                                                        "For all Coco Pharmaceutical employees wishing to understand more about sustainability and the organization's efforts to improve its operations.",
                                                        null,
                                                        ScopeDefinition.ALL_COCO.getPreferredValue());

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
                                                            "GlossaryTerm:" + glossaryTermDefinition.getName(),
                                                            glossaryTermDefinition.getName(),
                                                            glossaryTermDefinition.getSummary(),
                                                            glossaryTermDefinition.getDescription(),
                                                            null,
                                                            glossaryTermDefinition.getAbbreviation(),
                                                            null,
                                                            false,
                                                            false,
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
                                                   OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                   "ExternalReference:" + glossaryTermDefinition.getName(),
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   0,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   glossaryTermDefinition.getUrl(),
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
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
                                                                       true);
                }
            }
        }
    }
}
