/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.liskov.controls.LiskovConfigurationProperty;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LiskovSurveyScheduleFVT checks when the Liskov Data Sharing Hub Manager surveys the members of a data sharing
 * hub.  The hub has one member - a folder this suite built - and the test drives Liskov through its schedule:
 * <ol>
 *     <li>a member that has never been surveyed is surveyed on the first refresh;</li>
 *     <li>nothing more happens until the minimum interval has passed;</li>
 *     <li>each survey that finds no change adds a day to the interval;</li>
 *     <li>a survey that finds a change brings the interval back to the minimum;</li>
 *     <li>the minimum interval can be changed through the catalog target's configuration properties.</li>
 * </ol>
 * Nobody waits days for this.  Liskov measures the time since a survey from the start time recorded in its
 * survey report, so the test moves the reports' start times back to stand in for the days passing, then asks the
 * integration daemon to refresh Liskov and checks whether it requested a survey.
 * <br><br>
 * The comparisons that decide whether a survey found a change are real ones: they rely on a survey of an unchanged
 * folder reporting exactly the same annotations as the survey before it.  So this test also checks that the
 * folder survey reuses its annotations - if the folder survey recorded something that differs on every run, every
 * survey would count as a change and the interval would never grow.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class LiskovSurveyScheduleFVT
{
    private static final IntegrationConnectorDefinition LISKOV = IntegrationConnectorDefinition.LISKOV_DATA_SHARING_HUB_MANAGER;

    private static final RequestTypeDefinition CREATE_FILE_FOLDER = RequestTypeDefinition.CREATE_FILE_FOLDER;
    private static final RequestTypeDefinition SURVEY_FOLDER      = RequestTypeDefinition.SURVEY_FOLDER;

    /**
     * A file system directory registers four surveys.  Only one of them is under test, so the others are turned
     * off through the excludedSurveyRequestTypes configuration property - which is also how a real deployment
     * would avoid running all four.
     */
    private static final List<String> EXCLUDED_SURVEYS = List.of(RequestTypeDefinition.SURVEY_FOLDER_AND_FILES.getGovernanceRequestType(),
                                                                 RequestTypeDefinition.SURVEY_ALL_FOLDERS.getGovernanceRequestType(),
                                                                 RequestTypeDefinition.SURVEY_ALL_FOLDERS_AND_FILES.getGovernanceRequestType());

    private static final long ONE_DAY    = 24L * 60L * 60L * 1000L;
    private static final long ONE_MINUTE = 60L * 1000L;

    private final PropertyHelper propertyHelper = new PropertyHelper();


    @Test
    @DisplayName("Liskov surveys a data sharing hub's member on a schedule that adapts to how often it changes")
    public void testSurveySchedule() throws Exception
    {
        File   folder        = FilesFvtTestSupport.folderUnderTest("liskov");
        String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String folderAssetGUID = null;
        String hubGUID         = null;

        try
        {
            assertTrue(folder.isDirectory(), "The folder under test was not built: " + folder.getAbsolutePath());

            /*
             * The member: a folder asset catalogued from the FileFolder template, so that it carries the
             * deployedImplementationType that leads Liskov to the folder surveys.
             */
            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                                                                                                  new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder)),
                                                                                                  null);

            new EngineActionWaiter().waitForCompletion(createActionGUID, FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            folderAssetGUID = FilesFvtTestSupport.waitForElement(openMetadataStore, qualifiedName, "the folder asset for Liskov's data sharing hub").getElementGUID();

            /*
             * The data sharing hub, with the folder as its member, made a catalog target of Liskov.
             */
            hubGUID = createDataSharingHub(connectorContext, folderAssetGUID);

            String catalogTargetGUID = addCatalogTarget(connectorContext, hubGUID, null);

            /*
             * 1. Never surveyed - the first refresh surveys it.
             */
            refreshLiskov();

            List<OpenMetadataElement> reports = waitForCompletedSurveys(openMetadataStore, folderAssetGUID, 1,
                                                                        "the first survey of a member that has never been surveyed");

            assertExcludedSurveysNotRequested(openMetadataStore, folderAssetGUID);

            /*
             * 2. Within the minimum interval nothing more is requested.
             */
            refreshLiskov();
            assertSurveyCount(openMetadataStore, folderAssetGUID, 1, "a refresh within a day of the first survey");

            /*
             * 3. A day later the second survey runs.  The folder has not changed, so it must report exactly
             *    the annotations the first survey reported - that is what the change detection relies on.
             */
            backdate(openMetadataStore, reports, 1);
            refreshLiskov();

            reports = waitForCompletedSurveys(openMetadataStore, folderAssetGUID, 2, "the survey a day after the first");

            Set<String> firstAnnotations  = getReportedAnnotationGUIDs(openMetadataStore, reports.get(1).getElementGUID());
            Set<String> secondAnnotations = getReportedAnnotationGUIDs(openMetadataStore, reports.get(0).getElementGUID());

            assertEquals(firstAnnotations,
                         secondAnnotations,
                         "Two surveys of an unchanged folder reported different annotations, so every survey looks like a"
                                 + " change and the interval between surveys can never grow.  Only in the first survey: "
                                 + describeAnnotations(openMetadataStore, firstAnnotations, secondAnnotations)
                                 + ".  Only in the second survey: "
                                 + describeAnnotations(openMetadataStore, secondAnnotations, firstAnnotations)
                                 + ".  Every survey report of the folder: " + describeAllSurveyReports(openMetadataStore, folderAssetGUID));

            /*
             * 4. The second survey found no change, so the interval is now two days: a day later nothing happens.
             */
            backdate(openMetadataStore, reports, 1, 2);
            refreshLiskov();
            assertSurveyCount(openMetadataStore, folderAssetGUID, 2, "a refresh one day after a survey that found no change");

            /*
             * 5. Two days later the third survey runs.
             */
            backdate(openMetadataStore, reports, 2, 3);
            refreshLiskov();

            reports = waitForCompletedSurveys(openMetadataStore, folderAssetGUID, 3, "the survey two days after one that found no change");

            /*
             * 6. Two surveys in a row found no change, so the interval is three days.  The folder now changes,
             *    and the survey three days later finds it.
             */
            Files.writeString(new File(folder, "added-for-liskov.txt").toPath(), "Added by LiskovSurveyScheduleFVT.\n");

            backdate(openMetadataStore, reports, 2, 4, 5);
            refreshLiskov();
            assertSurveyCount(openMetadataStore, folderAssetGUID, 3, "a refresh two days after the second unchanged survey");

            backdate(openMetadataStore, reports, 3, 5, 6);
            refreshLiskov();

            reports = waitForCompletedSurveys(openMetadataStore, folderAssetGUID, 4, "the survey three days after the second unchanged survey");

            assertNotEquals(getReportedAnnotationGUIDs(openMetadataStore, reports.get(1).getElementGUID()),
                            getReportedAnnotationGUIDs(openMetadataStore, reports.get(0).getElementGUID()),
                            "A file was added to the folder, but the survey reported the same annotations as before.");

            /*
             * 7. That survey found a change, so the interval is back to one day.
             */
            backdate(openMetadataStore, reports, 1, 4, 6, 7);
            refreshLiskov();

            reports = waitForCompletedSurveys(openMetadataStore, folderAssetGUID, 5, "the survey a day after one that found a change");

            /*
             * 8. The minimum interval is configurable.  The latest survey found no change, so with the default
             *    minimum of one day the interval is two days, and two days later a survey would be due.  With a
             *    minimum of two days the interval is three, so two days is too soon.
             */
            updateCatalogTarget(connectorContext, catalogTargetGUID, 2);

            backdate(openMetadataStore, reports, 2, 3, 6, 8, 9);
            refreshLiskov();
            assertSurveyCount(openMetadataStore, folderAssetGUID, 5, "a refresh two days after an unchanged survey when the minimum interval is two days");

            backdate(openMetadataStore, reports, 3, 4, 7, 9, 10);
            refreshLiskov();

            waitForCompletedSurveys(openMetadataStore, folderAssetGUID, 6, "the survey three days after an unchanged survey when the minimum interval is two days");
        }
        finally
        {
            if (hubGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, hubGUID);
            }

            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }

            Files.deleteIfExists(new File(folder, "added-for-liskov.txt").toPath());
        }
    }


    /**
     * Create the data sharing hub with the folder as its member.
     *
     * @param connectorContext context to create it through
     * @param folderAssetGUID the member
     * @return unique identifier of the hub
     * @throws Exception problem creating it
     */
    private String createDataSharingHub(ConnectorContextBase connectorContext,
                                        String               folderAssetGUID) throws Exception
    {
        CollectionClient collectionClient = connectorContext.getCollectionClient(OpenMetadataType.DATA_SHARING_HUB.typeName);

        DataSharingHubProperties hubProperties = new DataSharingHubProperties();

        hubProperties.setQualifiedName(OpenMetadataType.DATA_SHARING_HUB.typeName + "::" + FilesFvtTestSupport.TEST_MARKER + "::liskov");
        hubProperties.setDisplayName(FilesFvtTestSupport.TEST_MARKER + " Liskov data sharing hub");

        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        String hubGUID = collectionClient.createCollection(newElementOptions, null, hubProperties, null);

        assertNotNull(hubGUID, "The data sharing hub was not created.");

        collectionClient.addToCollection(hubGUID,
                                         folderAssetGUID,
                                         new MakeAnchorOptions(collectionClient.getMetadataSourceOptions()),
                                         new CollectionMembershipProperties());

        return hubGUID;
    }


    /**
     * Make the hub a catalog target of Liskov.
     *
     * @param connectorContext context to create it through
     * @param hubGUID the hub
     * @param minimumSurveyIntervalDays value for the minimumSurveyIntervalDays configuration property, or null
     *                                  to leave it unset
     * @return unique identifier of the catalog target relationship
     * @throws Exception problem creating it
     */
    private String addCatalogTarget(ConnectorContextBase connectorContext,
                                    String               hubGUID,
                                    Integer              minimumSurveyIntervalDays) throws Exception
    {
        AssetClient assetClient = connectorContext.getAssetClient();

        return assetClient.addCatalogTarget(LISKOV.getGUID(),
                                            hubGUID,
                                            new MakeAnchorOptions(assetClient.getMetadataSourceOptions()),
                                            getCatalogTargetProperties(minimumSurveyIntervalDays));
    }


    /**
     * Change the minimum survey interval on the catalog target.
     *
     * @param connectorContext context to update it through
     * @param catalogTargetGUID the catalog target relationship
     * @param minimumSurveyIntervalDays new value for the minimumSurveyIntervalDays configuration property
     * @throws Exception problem updating it
     */
    private void updateCatalogTarget(ConnectorContextBase connectorContext,
                                     String               catalogTargetGUID,
                                     int                  minimumSurveyIntervalDays) throws Exception
    {
        AssetClient assetClient = connectorContext.getAssetClient();

        assetClient.updateCatalogTarget(catalogTargetGUID,
                                        assetClient.getUpdateOptions(false),
                                        getCatalogTargetProperties(minimumSurveyIntervalDays));
    }


    /**
     * Return the properties of Liskov's catalog target.
     *
     * @param minimumSurveyIntervalDays value for the minimumSurveyIntervalDays configuration property, or null
     *                                  to leave it unset
     * @return properties
     */
    private CatalogTargetProperties getCatalogTargetProperties(Integer minimumSurveyIntervalDays)
    {
        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

        catalogTargetProperties.setCatalogTargetName(FilesFvtTestSupport.TEST_MARKER + "-liskov");

        Map<String, Object> configurationProperties = new HashMap<>();

        /*
         * The excluded surveys are given as a list, the form the property's array<string> type describes.  A list
         * held in a catalog target's configuration properties used to be stored in a form that could not be read
         * back, so Liskov ran every survey it was told to skip; a comma-separated string works too.
         */
        configurationProperties.put(LiskovConfigurationProperty.EXCLUDED_SURVEY_REQUEST_TYPES.getName(), EXCLUDED_SURVEYS);

        if (minimumSurveyIntervalDays != null)
        {
            configurationProperties.put(LiskovConfigurationProperty.MINIMUM_SURVEY_INTERVAL_DAYS.getName(), minimumSurveyIntervalDays);
        }

        catalogTargetProperties.setConfigurationProperties(configurationProperties);

        return catalogTargetProperties;
    }


    /**
     * Ask the integration daemon to refresh Liskov, and wait for the refresh to finish.  The daemon records the
     * time of a connector's last refresh when the refresh returns, so a change in that time means the refresh
     * has completed - and any survey Liskov requested has been requested.
     *
     * @throws Exception the refresh did not complete
     */
    private void refreshLiskov() throws Exception
    {
        IntegrationDaemon integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();

        Date previousRefreshTime = getLiskovLastRefreshTime(integrationDaemon);

        integrationDaemon.refreshConnector(LISKOV.getConnectorName());

        FilesFvtTestSupport.waitFor("A refresh of " + LISKOV.getConnectorName(),
                                    "files.fvt.refresh.timeout.seconds",
                                    180,
                                    () ->
                                    {
                                        Date lastRefreshTime = getLiskovLastRefreshTime(integrationDaemon);

                                        return (lastRefreshTime != null) &&
                                                ((previousRefreshTime == null) || (lastRefreshTime.after(previousRefreshTime)));
                                    });
    }


    /**
     * Return when Liskov last finished a refresh.
     *
     * @param integrationDaemon client for the integration daemon
     * @return time, or null if it has not refreshed
     * @throws Exception problem reading the daemon's status
     */
    private Date getLiskovLastRefreshTime(IntegrationDaemon integrationDaemon) throws Exception
    {
        IntegrationGroupSummary summary = integrationDaemon.getIntegrationGroupSummary(OMAGPlatformExtension.LISKOV_INTEGRATION_GROUP.getQualifiedName());

        if ((summary != null) && (summary.getIntegrationConnectorReports() != null))
        {
            for (IntegrationConnectorReport connectorReport : summary.getIntegrationConnectorReports())
            {
                if ((connectorReport != null) && (LISKOV.getConnectorName().equals(connectorReport.getConnectorName())))
                {
                    return connectorReport.getLastRefreshTime();
                }
            }
        }

        return null;
    }


    /**
     * Return the folder survey's reports for the folder, most recent first.
     *
     * @param openMetadataStore store to read through
     * @param folderAssetGUID the folder
     * @return reports
     * @throws Exception problem reading them
     */
    private List<OpenMetadataElement> getFolderSurveyReports(OpenMetadataStore openMetadataStore,
                                                             String            folderAssetGUID) throws Exception
    {
        List<OpenMetadataElement> reports = new ArrayList<>();

        RelatedMetadataElementList relatedReports = openMetadataStore.getRelatedMetadataElements(folderAssetGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName,
                                                                                                 0,
                                                                                                 FilesFvtTestSupport.MAX_PAGE_SIZE);

        if ((relatedReports != null) && (relatedReports.getElementList() != null))
        {
            for (RelatedMetadataElement relatedReport : relatedReports.getElementList())
            {
                if ((relatedReport != null) &&
                        (relatedReport.getElement() != null) &&
                        (SURVEY_FOLDER.getGovernanceRequestType().equals(FilesFvtTestSupport.getStringProperty(relatedReport.getElement(),
                                                                                                                OpenMetadataProperty.PURPOSE.name))))
                {
                    reports.add(relatedReport.getElement());
                }
            }
        }

        reports.sort((report1, report2) -> report2.getVersions().getCreateTime().compareTo(report1.getVersions().getCreateTime()));

        return reports;
    }


    /**
     * Wait for the folder to have the expected number of folder survey reports, all of them complete.
     *
     * @param openMetadataStore store to read through
     * @param folderAssetGUID the folder
     * @param expectedCount number of reports
     * @param description what is being waited for
     * @return the reports, most recent first
     * @throws Exception they did not arrive
     */
    private List<OpenMetadataElement> waitForCompletedSurveys(OpenMetadataStore openMetadataStore,
                                                              String            folderAssetGUID,
                                                              int               expectedCount,
                                                              String            description) throws Exception
    {
        assertSurveyRequests(openMetadataStore, folderAssetGUID, expectedCount, "Liskov did not request " + description + ".");

        List<OpenMetadataElement> holder = new ArrayList<>();

        FilesFvtTestSupport.waitFor(description,
                                    "files.fvt.refresh.timeout.seconds",
                                    180,
                                    () ->
                                    {
                                        List<OpenMetadataElement> reports = getFolderSurveyReports(openMetadataStore, folderAssetGUID);

                                        if (reports.size() == expectedCount)
                                        {
                                            for (OpenMetadataElement report : reports)
                                            {
                                                if (FilesFvtTestSupport.getStringProperty(report, OpenMetadataProperty.COMPLETION_TIME.name) == null)
                                                {
                                                    return false;
                                                }
                                            }

                                            holder.addAll(reports);
                                            return true;
                                        }

                                        return false;
                                    });

        return holder;
    }


    /**
     * Check that none of the surveys named in excludedSurveyRequestTypes has been requested for the folder.
     *
     * @param openMetadataStore store to read through
     * @param folderAssetGUID the folder
     * @throws Exception an excluded survey was requested
     */
    private void assertExcludedSurveysNotRequested(OpenMetadataStore openMetadataStore,
                                                   String            folderAssetGUID) throws Exception
    {
        RelatedMetadataElementList engineActions = openMetadataStore.getRelatedMetadataElements(folderAssetGUID,
                                                                                                2,
                                                                                                OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                                0,
                                                                                                FilesFvtTestSupport.MAX_PAGE_SIZE);

        if ((engineActions != null) && (engineActions.getElementList() != null))
        {
            for (RelatedMetadataElement engineAction : engineActions.getElementList())
            {
                if ((engineAction != null) && (engineAction.getElement() != null))
                {
                    String requestType = FilesFvtTestSupport.getStringProperty(engineAction.getElement(), OpenMetadataProperty.REQUEST_TYPE.name);

                    assertTrue((requestType == null) || (! EXCLUDED_SURVEYS.contains(requestType)),
                               "Liskov requested survey " + requestType + ", which its catalog target excludes through "
                                       + LiskovConfigurationProperty.EXCLUDED_SURVEY_REQUEST_TYPES.getName() + ".");
                }
            }
        }
    }


    /**
     * Check that no survey was requested by the latest refresh.
     *
     * @param openMetadataStore store to read through
     * @param folderAssetGUID the folder
     * @param expectedCount number of surveys there should be
     * @param description the circumstances, for the failure message
     * @throws Exception a survey was requested
     */
    private void assertSurveyCount(OpenMetadataStore openMetadataStore,
                                   String            folderAssetGUID,
                                   int               expectedCount,
                                   String            description) throws Exception
    {
        assertSurveyRequests(openMetadataStore, folderAssetGUID, expectedCount,
                             "Liskov requested a survey after " + description + ", which is too soon.");
    }


    /**
     * Check how many folder surveys have been requested for the folder.  Liskov starts each survey as an
     * engine action with the folder as its action target, during the refresh, so once the refresh has
     * finished the engine actions say exactly how many surveys it has asked for.
     *
     * @param openMetadataStore store to read through
     * @param folderAssetGUID the folder
     * @param expectedCount number of surveys there should be
     * @param message failure message
     * @throws Exception the count is wrong
     */
    private void assertSurveyRequests(OpenMetadataStore openMetadataStore,
                                      String            folderAssetGUID,
                                      int               expectedCount,
                                      String            message) throws Exception
    {
        int surveyRequests = 0;

        RelatedMetadataElementList engineActions = openMetadataStore.getRelatedMetadataElements(folderAssetGUID,
                                                                                                2,
                                                                                                OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                                0,
                                                                                                FilesFvtTestSupport.MAX_PAGE_SIZE);

        if ((engineActions != null) && (engineActions.getElementList() != null))
        {
            for (RelatedMetadataElement engineAction : engineActions.getElementList())
            {
                if ((engineAction != null) &&
                        (engineAction.getElement() != null) &&
                        (propertyHelper.isTypeOf(engineAction.getElement(), OpenMetadataType.ENGINE_ACTION.typeName)) &&
                        (SURVEY_FOLDER.getGovernanceRequestType().equals(FilesFvtTestSupport.getStringProperty(engineAction.getElement(),
                                                                                                                OpenMetadataProperty.REQUEST_TYPE.name))))
                {
                    surveyRequests++;
                }
            }
        }

        if (expectedCount != surveyRequests)
        {
            assertEquals(expectedCount, surveyRequests, message + "  Every survey report of the folder: "
                    + describeAllSurveyReports(openMetadataStore, folderAssetGUID) + ".  Annotations that differ between"
                    + " the latest two folder survey reports: " + describeLatestDifference(openMetadataStore, folderAssetGUID));
        }
    }


    /**
     * Move the start times of the latest survey reports back, to stand in for the time passing.
     *
     * @param openMetadataStore store to update through
     * @param reports the reports, most recent first
     * @param daysAgo for each report in turn, how many days ago it is to have started
     * @throws Exception problem updating them
     */
    private void backdate(OpenMetadataStore         openMetadataStore,
                          List<OpenMetadataElement> reports,
                          int...                    daysAgo) throws Exception
    {
        long now = System.currentTimeMillis();

        for (int reportIndex = 0; (reportIndex < daysAgo.length) && (reportIndex < reports.size()); reportIndex++)
        {
            /*
             * A minute more than the whole number of days, so a report meant to be one day old is not counted
             * as less than a day old by the time Liskov looks at it.
             */
            Date startTime = new Date(now - (daysAgo[reportIndex] * ONE_DAY) - ONE_MINUTE);

            ElementProperties properties = propertyHelper.addDateProperty(null, OpenMetadataProperty.START_TIME.name, startTime);

            openMetadataStore.updateMetadataElementInStore(reports.get(reportIndex).getElementGUID(),
                                                           openMetadataStore.getUpdateOptions(true),
                                                           properties);
        }
    }


    /**
     * Describe the annotations that differ between the latest two folder survey reports, for a failure message.
     *
     * @param openMetadataStore store to read through
     * @param folderAssetGUID the folder
     * @return the differing annotations
     * @throws Exception problem reading them
     */
    private String describeLatestDifference(OpenMetadataStore openMetadataStore,
                                            String            folderAssetGUID) throws Exception
    {
        List<OpenMetadataElement> reports = getFolderSurveyReports(openMetadataStore, folderAssetGUID);
        List<String>              differences = new ArrayList<>();

        for (int reportIndex = 0; reportIndex + 1 < reports.size(); reportIndex++)
        {
            Set<String> newer = getReportedAnnotationGUIDs(openMetadataStore, reports.get(reportIndex).getElementGUID());
            Set<String> older = getReportedAnnotationGUIDs(openMetadataStore, reports.get(reportIndex + 1).getElementGUID());

            differences.add("report " + reportIndex + " vs " + (reportIndex + 1) + ": only newer "
                                    + describeAnnotations(openMetadataStore, newer, older)
                                    + " only older " + describeAnnotations(openMetadataStore, older, newer));
        }

        return differences.toString();
    }


    /**
     * Describe every survey report of an element - whatever survey produced it - for a failure message.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID the surveyed element
     * @return each report's GUID, purpose, start time and annotations
     * @throws Exception problem reading them
     */
    private String describeAllSurveyReports(OpenMetadataStore openMetadataStore,
                                            String            elementGUID) throws Exception
    {
        List<String> descriptions = new ArrayList<>();

        RelatedMetadataElementList relatedReports = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName,
                                                                                                 0,
                                                                                                 FilesFvtTestSupport.MAX_PAGE_SIZE);

        if ((relatedReports != null) && (relatedReports.getElementList() != null))
        {
            for (RelatedMetadataElement relatedReport : relatedReports.getElementList())
            {
                if ((relatedReport != null) && (relatedReport.getElement() != null))
                {
                    OpenMetadataElement report = relatedReport.getElement();
                    List<String>        annotations = new ArrayList<>();

                    for (String annotationGUID : getReportedAnnotationGUIDs(openMetadataStore, report.getElementGUID()))
                    {
                        OpenMetadataElement annotation = openMetadataStore.getMetadataElementByGUID(annotationGUID);

                        annotations.add(annotationGUID + "=" + FilesFvtTestSupport.getStringProperty(annotation, OpenMetadataProperty.ANNOTATION_TYPE.name));
                    }

                    descriptions.add("{" + report.getType().getTypeName() + " " + report.getElementGUID()
                                             + " purpose=" + FilesFvtTestSupport.getStringProperty(report, OpenMetadataProperty.PURPOSE.name)
                                             + " startTime=" + FilesFvtTestSupport.getStringProperty(report, OpenMetadataProperty.START_TIME.name)
                                             + " annotations=" + annotations + "}");
                }
            }
        }

        return descriptions.toString();
    }


    /**
     * Describe the annotations in one set that are not in another, for a failure message.
     *
     * @param openMetadataStore store to read through
     * @param annotationGUIDs annotations to describe
     * @param excludedGUIDs annotations to leave out
     * @return type and properties of each annotation
     * @throws Exception problem reading them
     */
    private String describeAnnotations(OpenMetadataStore openMetadataStore,
                                       Set<String>       annotationGUIDs,
                                       Set<String>       excludedGUIDs) throws Exception
    {
        List<String> descriptions = new ArrayList<>();

        for (String annotationGUID : annotationGUIDs)
        {
            if (! excludedGUIDs.contains(annotationGUID))
            {
                OpenMetadataElement annotation = openMetadataStore.getMetadataElementByGUID(annotationGUID);

                if (annotation != null)
                {
                    descriptions.add(annotation.getType().getTypeName() + " " + annotation.getElementProperties().getPropertiesAsStrings());
                }
            }
        }

        return descriptions.toString();
    }


    /**
     * Return the GUIDs of the annotations a survey report reported.
     *
     * @param openMetadataStore store to read through
     * @param surveyReportGUID the report
     * @return annotation GUIDs
     * @throws Exception problem reading them
     */
    private Set<String> getReportedAnnotationGUIDs(OpenMetadataStore openMetadataStore,
                                                   String            surveyReportGUID) throws Exception
    {
        Set<String> annotationGUIDs = new HashSet<>();

        RelatedMetadataElementList annotations = openMetadataStore.getRelatedMetadataElements(surveyReportGUID,
                                                                                              1,
                                                                                              OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                              0,
                                                                                              FilesFvtTestSupport.MAX_PAGE_SIZE);

        if ((annotations != null) && (annotations.getElementList() != null))
        {
            for (RelatedMetadataElement annotation : annotations.getElementList())
            {
                if ((annotation != null) && (annotation.getElement() != null))
                {
                    annotationGUIDs.add(annotation.getElement().getElementGUID());
                }
            }
        }

        return annotationGUIDs;
    }
}
