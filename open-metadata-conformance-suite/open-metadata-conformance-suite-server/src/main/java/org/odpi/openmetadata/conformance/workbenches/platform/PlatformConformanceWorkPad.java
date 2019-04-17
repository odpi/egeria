/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.platform;

import org.odpi.openmetadata.adminservices.configuration.properties.PlatformConformanceWorkbenchConfig;
import org.odpi.openmetadata.conformance.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.*;

public class PlatformConformanceWorkPad extends OpenMetadataConformanceWorkbenchWorkPad
{
    private static final String workbenchId            = "platform-workbench";
    private static final String workbenchName          = "Open Metadata Platform Conformance Workbench";
    private static final String workbenchVersionNumber = "V1.0 SNAPSHOT";
    private static final String workbenchDocURL        = "https://egeria.odpi.org/open-metadata-conformance-suite/docs/" + workbenchId;
    private static final String tutType                = "Open Metadata and Governance Platform";


    private OMRSAuditLog                       auditLog;
    private String                             tutPlatformURLRoot = null;


    /**
     * Constructor receives key information from the configuration services.
     *
     * @param localServerUserId userId that this server should use on requests
     * @param localServerPassword password that this server should use on requests
     * @param maxPageSize maximum number of elements that can be returned on a single call
     * @param auditLog audit log for administrator messages
     * @param configuration configuration for this work pad/workbench
     */
    public PlatformConformanceWorkPad(String                                localServerUserId,
                                      String                                localServerPassword,
                                      int                                   maxPageSize,
                                      OMRSAuditLog                          auditLog,
                                      PlatformConformanceWorkbenchConfig    configuration)
    {
        super(workbenchId,
              workbenchName,
              workbenchVersionNumber,
              workbenchDocURL,
              localServerUserId,
              localServerPassword,
              tutType,
              maxPageSize);

        this.auditLog = auditLog;

        if (configuration != null)
        {
            this.tutPlatformURLRoot = configuration.getTutPlatformURLRoot();
            super.tutName = this.tutPlatformURLRoot;
        }
    }

    /**
     * Return the audit log for this server.
     *
     * @return audit log object.
     */
    public OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Return the URL of the platform to test.
     *
     * @return URL root
     */
    public String getTutPlatformURLRoot()
    {
        return tutPlatformURLRoot;
    }


    /**
     * Accumulate the evidences for each profile
     *
     * @return the test evidence organized by profile and requirement withing profile
     */
    public synchronized  List<OpenMetadataConformanceProfileResults> getProfileResults()
    {
        List<OpenMetadataConformanceProfileResults>  resultsList = new ArrayList<>();

        PlatformConformanceProfile[]            profiles     = PlatformConformanceProfile.values();
        PlatformConformanceProfileRequirement[] requirements = PlatformConformanceProfileRequirement.values();

        for (PlatformConformanceProfile profile : profiles)
        {
            OpenMetadataConformanceProfileResults  profileResults = new OpenMetadataConformanceProfileResults();

            profileResults.setId(profile.getProfileId());
            profileResults.setName(profile.getProfileName());
            profileResults.setDocumentationURL(profile.getProfileDocumentationURL());
            profileResults.setDescription(profile.getProfileDescription());
            profileResults.setProfilePriority(profile.getProfilePriority());

            List<OpenMetadataConformanceTestEvidence>   profileTestEvidence = new ArrayList<>();

            if (testEvidenceList != null)
            {
                for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList)
                {
                    if ((testEvidenceItem != null) && (testEvidenceItem.getProfileId().intValue() == profileResults.getId().intValue()))
                    {
                        profileTestEvidence.add(testEvidenceItem);
                    }
                }
            }

            if (profileTestEvidence.isEmpty())
            {
                profileResults.setConformanceStatus(OpenMetadataConformanceStatus.UNKNOWN_STATUS);
            }
            else
            {
                List<OpenMetadataConformanceTestEvidence>       positiveTestEvidence = new ArrayList<>();
                List<OpenMetadataConformanceTestEvidence>       negativeTestEvidence = new ArrayList<>();

                profileResults.setConformanceStatus(super.processEvidence(profileTestEvidence,
                                                                          positiveTestEvidence,
                                                                          negativeTestEvidence));

                List<OpenMetadataConformanceRequirementResults> requirementResultsList = new ArrayList<>();
                OpenMetadataConformanceRequirementResults       requirementResults;


                for (PlatformConformanceProfileRequirement requirement : requirements)
                {
                    requirementResults = new OpenMetadataConformanceRequirementResults();

                    requirementResults.setId(requirement.getRequirementId());
                    requirementResults.setName(requirement.getName());
                    requirementResults.setDescription(requirement.getDescription());
                    requirementResults.setDocumentationURL(requirement.getDocumentationURL());

                    List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                    for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence)
                    {
                        if (testEvidenceItem != null)
                        {
                            if (testEvidenceItem.getRequirementId().intValue() == requirementResults.getId().intValue())
                            {
                                requirementTestEvidence.add(testEvidenceItem);
                            }
                        }
                    }

                    positiveTestEvidence = new ArrayList<>();
                    negativeTestEvidence = new ArrayList<>();

                    requirementResults.setConformanceStatus(super.processEvidence(requirementTestEvidence,
                                                                                  positiveTestEvidence,
                                                                                  negativeTestEvidence));

                    if (! positiveTestEvidence.isEmpty())
                    {
                        requirementResults.setPositiveTestEvidence(positiveTestEvidence);
                    }

                    if (! negativeTestEvidence.isEmpty())
                    {
                        requirementResults.setNegativeTestEvidence(negativeTestEvidence);
                    }

                    requirementResultsList.add(requirementResults);
                }

                profileResults.setRequirementResults(requirementResultsList);
            }

            resultsList.add(profileResults);
        }


        if (resultsList.isEmpty())
        {
            return null;
        }
        else
        {
            return resultsList;
        }
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "PlatformConformanceWorkPad{" +
                "auditLog=" + auditLog +
                ", tutPlatformURLRoot='" + tutPlatformURLRoot + '\'' +
                ", workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", workbenchVersionNumber='" + workbenchVersionNumber + '\'' +
                ", workbenchDocURL='" + workbenchDocURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", localServerPassword='" + localServerPassword + '\'' +
                ", tutName='" + tutName + '\'' +
                ", tutType='" + tutType + '\'' +
                ", maxPageSize=" + maxPageSize +
                ", testEvidenceList=" + testEvidenceList +
                ", testCaseMap=" + testCaseMap +
                '}';
    }
}
