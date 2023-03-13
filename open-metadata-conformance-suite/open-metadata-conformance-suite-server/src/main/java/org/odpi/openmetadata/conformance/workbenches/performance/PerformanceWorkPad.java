/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.performance;

import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryPerformanceWorkbenchConfig;
import org.odpi.openmetadata.conformance.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.*;


/**
 * PerformanceWorkPad provides the thread safe place to assemble results from the performance workbench.
 */
public class PerformanceWorkPad extends OpenMetadataConformanceWorkbenchWorkPad
{
    private static final String workbenchId            = "performance-workbench";
    private static final String workbenchName          = "Open Metadata Performance Test Workbench";
    private static final String workbenchVersionNumber = "V1.1";
    private static final String workbenchDocURL        = "https://egeria-project.org/guides/cts/" + workbenchId;
    private static final String tutType                = "Open Metadata Repository";

    private OMRSAuditLog            auditLog;

    private String                  tutServerName               = null;
    private String                  tutMetadataCollectionId     = null;
    private String                  tutServerType               = null;
    private String                  tutOrganization             = null;
    private int                     instancesPerType            = 50;
    private int                     maxSearchResults            = 10;
    private int                     waitBetweenScenarios        = 0;
    private List<String>            profilesToSkip              = Collections.emptyList();
    private List<String>            methodsToSkip               = Collections.emptyList();

    private OMRSRepositoryConnector tutRepositoryConnector      = null;

    private String                  localMetadataCollectionId   = null;
    private OMRSRepositoryConnector localRepositoryConnector    = null;

    private long totalEntitiesCreated      = 0L;
    private long totalRelationshipsCreated = 0L;
    private long totalEntitiesFound        = 0L;
    private long totalRelationshipsFound   = 0L;

    private static final String referenceCopyMetadataCollectionId = UUID.randomUUID().toString();


    /**
     * Constructor receives key information from the configuration services.
     *
     * @param localServerUserId userId that this server should use on requests
     * @param localServerPassword password that this server should use on requests
     * @param maxPageSize maximum number of elements that can be returned on a single call
     * @param auditLog audit log for administrator messages
     * @param configuration configuration for this work pad/workbench
     */
    public PerformanceWorkPad(String                                localServerUserId,
                              String                                localServerPassword,
                              int                                   maxPageSize,
                              OMRSAuditLog                          auditLog,
                              RepositoryPerformanceWorkbenchConfig  configuration)
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
            this.tutServerName = configuration.getTutRepositoryServerName();
            this.instancesPerType = configuration.getInstancesPerType();
            this.maxSearchResults = configuration.getMaxSearchResults();
            this.waitBetweenScenarios = configuration.getWaitBetweenScenarios();
            this.profilesToSkip = configuration.getProfilesToSkip();
            this.methodsToSkip  = configuration.getMethodsToSkip();
            super.tutName = this.tutServerName;
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
     * Return the name of the server being tested.
     *
     * @return server name
     */
    public String getTutServerName()
    {
        return tutServerName;
    }


    /**
     * Return the number of instances that should be created per type definition.
     *
     * @return number of instances to create (per type definition)
     */
    public int getInstancesPerType()
    {
        return instancesPerType;
    }


    /**
     * Return the maximum number of search results that be processed for tests of the repository under test.
     *
     * @return maximum number of search results to process
     */
    public int getMaxSearchResults()
    {
        return maxSearchResults;
    }

    /**
     * Return the amount of time (in seconds) to wait before executing different scenarios of the performance test. This
     * would be useful for example, where you want to simulate a batch-load with an eventually-consistent search index.
     *
     * @return number of seconds to wait between executing different performance test scenarios
     */
    public int getWaitBetweenScenarios()
    {
        return waitBetweenScenarios;
    }

    /**
     * Return the profiles that should be skipped (if any) by the performance test.
     *
     * @return list of profile names to skip
     */
    public List<String> getProfilesToSkip()
    {
        return profilesToSkip;
    }

    /**
     * Return the methods that should be skipped (if any) by the performance test.
     *
     * @return list of method names to skip
     */
    public List<String> getMethodsToSkip()
    {
        return methodsToSkip;
    }

    /**
     * Return the server type of the technology under test.  This is extracted from the registration
     * events.
     *
     * @return string type name
     */
    public synchronized String getTutServerType()
    {
        return tutServerType;
    }


    /**
     * Set up the server type of the technology under test.  This is extracted from the registration
     * events.
     *
     * @param tutServerType string type name
     */
    public synchronized void setTutServerType(String tutServerType)
    {
        this.tutServerType = tutServerType;
    }


    /**
     * Return the owning organization of the technology under test.  This is extracted from the registration
     * event.
     *
     * @return string organization name
     */
    public synchronized String getTutOrganization()
    {
        return tutOrganization;
    }


    /**
     * Set up the owning organization of the technology under test.  This is extracted from the registration
     * event.
     *
     * @param tutOrganization string organization name
     */
    public synchronized void setTutOrganization(String tutOrganization)
    {
        this.tutOrganization = tutOrganization;
    }


    /**
     * Return the metadata collection id of the technology under test (or null if it is not known).
     * This value is populated from the registration events.
     *
     * @return string id
     */
    public synchronized String getTutMetadataCollectionId()
    {
        return tutMetadataCollectionId;
    }


    /**
     * Set up the metadata collection id of the technology under test.
     * This value is populated from the registration events.
     *
     * @param tutMetadataCollectionId string id
     */
    public synchronized void setTutMetadataCollectionId(String tutMetadataCollectionId)
    {
        this.tutMetadataCollectionId = tutMetadataCollectionId;
    }


    /**
     * Return the repository connector for the technology under test.
     *
     * @return OMRSRepositoryConnector
     */
    public synchronized OMRSRepositoryConnector getTutRepositoryConnector()
    {
        return tutRepositoryConnector;
    }


    /**
     * Set up the repository connector for the technology under test.
     *
     * @param tutRepositoryConnector OMRSRepositoryConnector
     */
    public synchronized void setTutRepositoryConnector(OMRSRepositoryConnector tutRepositoryConnector)
    {
        this.tutRepositoryConnector = tutRepositoryConnector;
    }


    /**
     * Return the metadata collection id for the local repository.
     *
     * @return string id
     */
    public synchronized String getLocalMetadataCollectionId()
    {
        return localMetadataCollectionId;
    }


    /**
     * Set up the metadata collection id for the local repository.
     *
     * @param localMetadataCollectionId string id
     */
    public synchronized void setLocalMetadataCollectionId(String localMetadataCollectionId)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Return the connector to the local repository.
     *
     * @return OMRSRepositoryConnector
     */
    public synchronized OMRSRepositoryConnector getLocalRepositoryConnector()
    {
        return localRepositoryConnector;
    }


    /**
     * Set up the connector to the local repository.
     *
     * @param localRepositoryConnector access to the local repository (updated to generate events for the
     *                                 technology under test to respond to)
     */
    public synchronized void setLocalRepositoryConnector(OMRSRepositoryConnector localRepositoryConnector)
    {
        this.localRepositoryConnector = localRepositoryConnector;
    }


    /**
     * {@inheritDoc}
     */
    public synchronized List<String> getProfileNames()
    {
        List<String> list = new ArrayList<>();
        PerformanceProfile[] profiles = PerformanceProfile.values();
        for (PerformanceProfile profile : profiles)
        {
            list.add(profile.getProfileName());
        }
        return list;
    }


    /**
     * {@inheritDoc}
     */
    public synchronized OpenMetadataConformanceProfileResults getProfileResults(String profileName)
    {

        PerformanceProfile[]            profiles     = PerformanceProfile.values();

        OpenMetadataConformanceProfileResults  profileResults  = null;

        for (PerformanceProfile profile : profiles)
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
                    OpenMetadataConformanceRequirementResults       requirementResults = new OpenMetadataConformanceRequirementResults();

                    requirementResults.setId(profile.getProfileId());
                    requirementResults.setName(profile.getProfileName());
                    requirementResults.setDescription(profile.getProfileDescription());
                    requirementResults.setDocumentationURL(profile.getProfileDocumentationURL());

                    List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                    for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence)
                    {
                        if (testEvidenceItem != null)
                        {
                            if (testEvidenceItem.getProfileId().intValue() == requirementResults.getId().intValue())
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

                    if (!positiveTestEvidence.isEmpty())
                    {
                        requirementResults.setPositiveTestEvidence(positiveTestEvidence);
                    }

                    if (!negativeTestEvidence.isEmpty())
                    {
                        requirementResults.setNegativeTestEvidence(negativeTestEvidence);
                    }

                    requirementResultsList.add(requirementResults);
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
    public synchronized List<OpenMetadataConformanceProfileResults> getProfileResults()
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

        PerformanceProfile[]            profiles     = PerformanceProfile.values();

        OpenMetadataConformanceProfileSummary profileSummary = null;

        for (PerformanceProfile profile : profiles)
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
                OpenMetadataConformanceRequirementSummary requirementSummary = new OpenMetadataConformanceRequirementSummary();

                requirementSummary.setId(profile.getProfileId());
                requirementSummary.setName(profile.getProfileName());
                requirementSummary.setDescription(profile.getProfileDescription());
                requirementSummary.setDocumentationURL(profile.getProfileDocumentationURL());

                List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence) {
                    if (testEvidenceItem != null) {
                        if (testEvidenceItem.getProfileId().intValue() == requirementSummary.getId().intValue()) {
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
     * Increment the total number of entities created by the test by the specified amount.
     *
     * @param amount by which to increment the count
     */
    public void incrementEntitiesCreated(int amount)
    {
        totalEntitiesCreated += amount;
    }


    /**
     * Return the total number of entity instances created in the environment.
     *
     * @return the number of entity instances created in the environment
     */
    public long getTotalEntitiesCreated()
    {
        return totalEntitiesCreated;
    }


    /**
     * Increment the total number of relationships created by the test by the specified amount.
     *
     * @param amount by which to increment the count
     */
    public void incrementRelationshipsCreated(int amount)
    {
        totalRelationshipsCreated += amount;
    }


    /**
     * Return the total number of relationship instances created in the environment.
     *
     * @return the number of relationship instances created in the environment
     */
    public long getTotalRelationshipsCreated()
    {
        return totalRelationshipsCreated;
    }


    /**
     * Increment the total number of entities found by the specified amount.
     *
     * @param amount by which to increment the count
     */
    public void incrementEntitiesFound(int amount)
    {
        totalEntitiesFound += amount;
    }


    /**
     * Return the total number of entities found in the environment.
     *
     * @return the number of entity instances found in the environment
     */
    public long getTotalEntitiesFound()
    {
        return totalEntitiesFound;
    }


    /**
     * Increment the total number of relationships found by the specified amount.
     *
     * @param amount by which to increment the count
     */
    public void incrementRelationshipsFound(int amount)
    {
        totalRelationshipsFound += amount;
    }


    /**
     * Return the total number of relationships found in the environment.
     *
     * @return the number of relationship instances found in the environment
     */
    public long getTotalRelationshipsFound()
    {
        return totalRelationshipsFound;
    }


    /**
     * Return the external metadata collection ID that can be used for reference copies.
     *
     * @return a generated external metadata collection ID that will be consistent across all tests
     */
    public String getReferenceCopyMetadataCollectionId()
    {
        return referenceCopyMetadataCollectionId;
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "PerformanceWorkPad{" +
                "workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", workbenchVersionNumber='" + workbenchVersionNumber + '\'' +
                ", workbenchDocURL='" + workbenchDocURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", localServerPassword='" + localServerPassword + '\'' +
                ", tutName='" + tutName + '\'' +
                ", maxSearchResults=" + maxSearchResults +
                ", tutType='" + tutType + '\'' +
                ", maxPageSize=" + maxPageSize +
                ", profilesToSkip=" + profilesToSkip +
                ", methodsToSkip=" + methodsToSkip +
                '}';
    }
}
