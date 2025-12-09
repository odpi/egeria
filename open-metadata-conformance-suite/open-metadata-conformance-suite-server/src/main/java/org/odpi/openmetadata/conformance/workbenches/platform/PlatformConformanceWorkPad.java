/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.platform;

import org.odpi.openmetadata.adminservices.configuration.properties.PlatformConformanceWorkbenchConfig;
import org.odpi.openmetadata.conformance.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.*;

/**
 * PlatformConformanceWorkPad provides the work pad for the platform workbench
 */
public class PlatformConformanceWorkPad extends OpenMetadataConformanceWorkbenchWorkPad
{
    private static final String workbenchId            = "platform-workbench";
    private static final String workbenchName          = "Open Metadata Platform Conformance Workbench";
    private static final String workbenchVersionNumber = "V1.0 SNAPSHOT";
    private static final String workbenchDocURL        = "https://egeria-project.org/guides/cts/" + workbenchId;
    private static final String tutType                = "Open Metadata and Governance Platform";


    private final OMRSAuditLog auditLog;
    private       String       tutPlatformURLRoot = null;


    /**
     * Constructor receives key information from the configuration services.
     *
     * @param localServerUserId userId that this server should use on requests
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum number of elements that can be returned on a single call
     * @param auditLog audit log for administrator messages
     * @param configuration configuration for this work pad/workbench
     */
    public PlatformConformanceWorkPad(String                                localServerUserId,
                                      String                                localServerSecretsStoreProvider,
                                      String                                localServerSecretsStoreLocation,
                                      String                                localServerSecretsStoreCollection,
                                      int                                   maxPageSize,
                                      OMRSAuditLog                          auditLog,
                                      PlatformConformanceWorkbenchConfig    configuration)
    {
        super(workbenchId,
              workbenchName,
              workbenchVersionNumber,
              workbenchDocURL,
              localServerUserId,
              localServerSecretsStoreProvider,
              localServerSecretsStoreLocation,
              localServerSecretsStoreCollection,
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
     * {@inheritDoc}
     */
    public synchronized List<String> getProfileNames()
    {
        List<String> list = new ArrayList<>();
        PlatformConformanceProfile[] profiles = PlatformConformanceProfile.values();
        for (PlatformConformanceProfile profile : profiles)
        {
            list.add(profile.getProfileName());
        }
        return list;
    }


    /**
     * Accumulate the evidences for a given profile.
     *
     * @param profileName for which to obtain the detailed results
     * @return the test evidence organized by profile and requirement within profile
     */
    public synchronized  OpenMetadataConformanceProfileResults getProfileResults(String profileName)
    {

        PlatformConformanceProfile[]            profiles     = PlatformConformanceProfile.values();
        PlatformConformanceProfileRequirement[] requirements = PlatformConformanceProfileRequirement.values();

        OpenMetadataConformanceProfileResults profileResults = null;

        for (PlatformConformanceProfile profile : profiles)
        {
            String candidateProfile = profile.getProfileName();
            if (candidateProfile.equals(profileName)) {

                profileResults = new OpenMetadataConformanceProfileResults();
                profileResults.setId(profile.getProfileId());
                profileResults.setName(profileName);
                profileResults.setDocumentationURL(profile.getProfileDocumentationURL());
                profileResults.setDescription(profile.getProfileDescription());
                profileResults.setProfilePriority(profile.getProfilePriority());

                List<OpenMetadataConformanceTestEvidence> profileTestEvidence = new ArrayList<>();

                if (testEvidenceList != null) {
                    for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList) {
                        if ((testEvidenceItem != null) && (testEvidenceItem.getProfileId().intValue() == profileResults.getId().intValue())) {
                            profileTestEvidence.add(testEvidenceItem);
                        }
                    }
                }

                if (profileTestEvidence.isEmpty()) {
                    profileResults.setConformanceStatus(OpenMetadataConformanceStatus.UNKNOWN_STATUS);
                } else {
                    List<OpenMetadataConformanceTestEvidence> positiveTestEvidence = new ArrayList<>();
                    List<OpenMetadataConformanceTestEvidence> negativeTestEvidence = new ArrayList<>();

                    profileResults.setConformanceStatus(super.processEvidence(profileTestEvidence,
                            positiveTestEvidence,
                            negativeTestEvidence));

                    List<OpenMetadataConformanceRequirementResults> requirementResultsList = new ArrayList<>();
                    OpenMetadataConformanceRequirementResults requirementResults;


                    for (PlatformConformanceProfileRequirement requirement : requirements) {
                        requirementResults = new OpenMetadataConformanceRequirementResults();

                        requirementResults.setId(requirement.getRequirementId());
                        requirementResults.setName(requirement.getName());
                        requirementResults.setDescription(requirement.getDescription());
                        requirementResults.setDocumentationURL(requirement.getDocumentationURL());

                        List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                        for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence) {
                            if (testEvidenceItem != null) {
                                if (testEvidenceItem.getRequirementId().intValue() == requirementResults.getId().intValue()) {
                                    requirementTestEvidence.add(testEvidenceItem);
                                }
                            }
                        }

                        positiveTestEvidence = new ArrayList<>();
                        negativeTestEvidence = new ArrayList<>();

                        requirementResults.setConformanceStatus(super.processEvidence(requirementTestEvidence,
                                positiveTestEvidence,
                                negativeTestEvidence));

                        if (!positiveTestEvidence.isEmpty()) {
                            requirementResults.setPositiveTestEvidence(positiveTestEvidence);
                        }

                        if (!negativeTestEvidence.isEmpty()) {
                            requirementResults.setNegativeTestEvidence(negativeTestEvidence);
                        }

                        requirementResultsList.add(requirementResults);
                    }

                    profileResults.setRequirementResults(requirementResultsList);
                }
            }
        }

        return profileResults;

    }


    /**
     * Accumulate the evidences for each profile
     *
     * @return the test evidence organized by profile and requirement withing profile
     */
    public synchronized  List<OpenMetadataConformanceProfileResults> getProfileResults()
    {
        List<OpenMetadataConformanceProfileResults>  resultsList = new ArrayList<>();

        for (String profileName : getProfileNames())
        {
            OpenMetadataConformanceProfileResults profileResults = getProfileResults(profileName);
            if (profileResults != null)
            {
                resultsList.add(profileResults);
            }
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
     * Accumulate the summarized evidences for each profile
     *
     * @return the summarized test evidence organized by profile and requirement withing profile
     */
    public synchronized  List<OpenMetadataConformanceProfileSummary> getProfileSummaries()
    {
        List<OpenMetadataConformanceProfileSummary>  summaryList = new ArrayList<>();

        PlatformConformanceProfile[]            profiles     = PlatformConformanceProfile.values();
        PlatformConformanceProfileRequirement[] requirements = PlatformConformanceProfileRequirement.values();

        OpenMetadataConformanceProfileSummary profileSummary = null;

        for (PlatformConformanceProfile profile : profiles)
        {

            profileSummary = new OpenMetadataConformanceProfileSummary();
            profileSummary.setId(profile.getProfileId());
            profileSummary.setName(profile.getProfileName());
            profileSummary.setDocumentationURL(profile.getProfileDocumentationURL());
            profileSummary.setDescription(profile.getProfileDescription());
            profileSummary.setProfilePriority(profile.getProfilePriority());

            List<OpenMetadataConformanceTestEvidence> profileTestEvidence = new ArrayList<>();

            if (testEvidenceList != null) {
                for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList) {
                    if ((testEvidenceItem != null) && (testEvidenceItem.getProfileId().intValue() == profileSummary.getId().intValue())) {
                        profileTestEvidence.add(testEvidenceItem);
                    }
                }
            }

            if (profileTestEvidence.isEmpty()) {
                profileSummary.setConformanceStatus(OpenMetadataConformanceStatus.UNKNOWN_STATUS);
            } else {
                List<OpenMetadataConformanceTestEvidence> positiveTestEvidence = new ArrayList<>();
                List<OpenMetadataConformanceTestEvidence> negativeTestEvidence = new ArrayList<>();

                profileSummary.setConformanceStatus(super.processEvidence(profileTestEvidence,
                            positiveTestEvidence,
                            negativeTestEvidence));

                List<OpenMetadataConformanceRequirementSummary> requirementResultsList = new ArrayList<>();
                OpenMetadataConformanceRequirementSummary requirementSummary;


                for (PlatformConformanceProfileRequirement requirement : requirements) {
                    requirementSummary = new OpenMetadataConformanceRequirementSummary();

                    requirementSummary.setId(requirement.getRequirementId());
                    requirementSummary.setName(requirement.getName());
                    requirementSummary.setDescription(requirement.getDescription());
                    requirementSummary.setDocumentationURL(requirement.getDocumentationURL());

                    List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                    for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence) {
                        if (testEvidenceItem != null) {
                            if (testEvidenceItem.getRequirementId().intValue() == requirementSummary.getId().intValue()) {
                                requirementTestEvidence.add(testEvidenceItem);
                            }
                        }
                    }

                    positiveTestEvidence = new ArrayList<>();
                    negativeTestEvidence = new ArrayList<>();

                    requirementSummary.setConformanceStatus(super.processEvidence(requirementTestEvidence,
                            positiveTestEvidence,
                            negativeTestEvidence));

                        requirementResultsList.add(requirementSummary);
                }

                profileSummary.setRequirementSummary(requirementResultsList);
            }
            summaryList.add(profileSummary);
        }

        if (summaryList.isEmpty())
        {
            return null;
        }
        else
        {
            return summaryList;
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
                ", profileNames=" + getProfileNames() +
                ", profileResults=" + getProfileResults() +
                ", profileSummaries=" + getProfileSummaries() +
                "} " + super.toString();
    }
}
