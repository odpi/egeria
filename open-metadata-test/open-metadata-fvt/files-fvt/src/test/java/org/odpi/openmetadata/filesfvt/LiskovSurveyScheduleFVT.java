/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.liskov.controls.LiskovConfigurationProperty;
import org.odpi.openmetadata.contentpacks.core.DataAssetTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.FileSystemConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
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
    private static final RequestTypeDefinition SURVEY_PARQUET_FILE = RequestTypeDefinition.SURVEY_PARQUET_FILE;
    private static final RequestTypeDefinition CATALOG_FILE_FOLDER = RequestTypeDefinition.CATALOG_FILE_FOLDER;

    /**
     * The CSV file that FilesFvtTestSupport writes to every folder under test.
     */
    private static final String CSV_FILE_NAME = "measurements.csv";

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


    @Test
    @DisplayName("Liskov surveys a Parquet file that is a member of a data sharing hub with the Parquet file survey")
    public void testParquetFileSurvey() throws Exception
    {
        File file = new File(FilesFvtTestSupport.folderUnderTest("template"), FilesFvtTestSupport.PARQUET_FILE_NAME);

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String fileAssetGUID = null;
        String hubGUID       = null;

        try
        {
            assertTrue(file.isFile(), "The Parquet file under test was not written: " + file.getAbsolutePath());

            /*
             * The member: a Parquet file asset catalogued from the Parquet file template, so that it carries the
             * deployedImplementationType that leads Liskov to the surveys for Parquet files.
             */
            TemplateOptions templateOptions = new TemplateOptions();

            templateOptions.setIsOwnAnchor(true);

            fileAssetGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                templateOptions,
                                                                                DataAssetTemplateDefinition.PARQUET_FILE_TEMPLATE.getTemplateGUID(),
                                                                                null,
                                                                                null,
                                                                                FilesFvtTestSupport.fileTemplatePlaceholders(file),
                                                                                null);

            assertNotNull(fileAssetGUID, "Creating a file asset from the Parquet file template returned no GUID.");

            hubGUID = createDataSharingHub(connectorContext, fileAssetGUID);

            addCatalogTarget(connectorContext, hubGUID, null);

            /*
             * The file has never been surveyed, so the first refresh surveys it.  Liskov finds the survey from
             * the technology type of the file, so what matters is that it is the survey that understands Parquet
             * files - not the general data file survey, and not none at all.
             */
            refreshLiskov();

            final String assetGUID = fileAssetGUID;

            FilesFvtTestSupport.waitFor("Liskov's survey of a Parquet file that has never been surveyed",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () ->
                                        {
                                            List<OpenMetadataElement> reports = getSurveyReports(openMetadataStore, assetGUID);

                                            return (! reports.isEmpty()) &&
                                                    (FilesFvtTestSupport.getStringProperty(reports.get(0), OpenMetadataProperty.COMPLETION_TIME.name) != null);
                                        });

            assertEquals(List.of(SURVEY_PARQUET_FILE.getGovernanceRequestType()),
                         getSurveyRequestTypes(openMetadataStore, fileAssetGUID),
                         "Liskov should have requested one survey of the Parquet file, and it should be the Parquet file survey.");

            List<OpenMetadataElement> reports = getSurveyReports(openMetadataStore, fileAssetGUID);

            assertEquals(1, reports.size(), "There should be one survey report for the Parquet file.");
            assertEquals(SURVEY_PARQUET_FILE.getGovernanceRequestType(),
                         FilesFvtTestSupport.getStringProperty(reports.get(0), OpenMetadataProperty.PURPOSE.name),
                         "The survey report for the Parquet file was not produced by the Parquet file survey.");
            assertTrue(getReportedAnnotationGUIDs(openMetadataStore, reports.get(0).getElementGUID()).size() > 1,
                       "The Parquet file survey that Liskov requested did not describe the contents of the file.");

            /*
             * The survey attached the file's columns to the asset.  This refresh is where Liskov reads them to
             * build the data structure for the hub, and it is too soon for another survey.
             */
            refreshLiskov();

            assertEquals(List.of(SURVEY_PARQUET_FILE.getGovernanceRequestType()),
                         getSurveyRequestTypes(openMetadataStore, fileAssetGUID),
                         "Liskov requested another survey of the Parquet file within a day of the first.");
        }
        finally
        {
            if (hubGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, hubGUID);
            }

            if (fileAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, fileAssetGUID);
            }
        }
    }


    @Test
    @DisplayName("Liskov surveys the Parquet and CSV files in a folder that is a member of a data sharing hub, each with its own survey")
    public void testFilesInHubFolderSurvey() throws Exception
    {
        File folder = FilesFvtTestSupport.folderUnderTest("hubfiles");

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String              folderAssetGUID = null;
        String              hubGUID         = null;
        Map<String, String> fileAssetGUIDs  = new HashMap<>();
        List<String>        handCataloguedGUIDs = new ArrayList<>();

        try
        {
            assertTrue(new File(folder, FilesFvtTestSupport.PARQUET_FILE_NAME).isFile(),
                       "The Parquet file under test was not written to " + folder.getAbsolutePath());

            /*
             * The member is the folder.  Nothing in it has been catalogued.
             *
             * The folder asset is created with the path name that is local to this machine, as the rest of
             * this suite does.  The folder cataloguer records the canonical path name of each file it
             * catalogues, by replacing the local mount point (by default, the directory the platform runs in)
             * with the canonical mount point.  Liskov finds a folder's files by looking for the folder's
             * path name in the catalogued files, so it has to convert the folder's path name to the canonical
             * form first - and once that finds the files, it corrects the folder asset to match them.
             */
            String localMountPoint   = new File(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample()).getCanonicalPath();
            String canonicalPathName = folder.getCanonicalPath().replace(localMountPoint, FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getExample());

            assertNotEquals(folder.getCanonicalPath(), canonicalPathName,
                            "The folder under test is not under the directory the platform runs in, so its canonical path name cannot be worked out.");

            Map<String, String> folderPlaceholders = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder));

            String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                                                                                                  folderPlaceholders,
                                                                                                  null);

            new EngineActionWaiter().waitForCompletion(createActionGUID, FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            folderAssetGUID = FilesFvtTestSupport.waitForElement(openMetadataStore, qualifiedName, "the folder asset for Liskov's data sharing hub").getElementGUID();

            /*
             * A folder and a file beneath the hub folder that were catalogued by hand, with path names that are
             * local to this machine.  Neither exists on disk, so the folder cataloguer does not catalogue them
             * again.  The last one is a folder beside the hub folder whose name starts with the hub folder's name -
             * it is not in the hub folder and so Liskov must leave it alone.
             */
            File subFolder     = new File(folder, "archive");
            File archivedFile  = new File(subFolder, "notes.dat");
            File siblingFolder = new File(folder.getParentFile(), folder.getName() + "-other");

            TemplateOptions templateOptions = new TemplateOptions();

            templateOptions.setIsOwnAnchor(true);

            String subFolderGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                       templateOptions,
                                                                                       DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE.getTemplateGUID(),
                                                                                       null,
                                                                                       null,
                                                                                       FilesFvtTestSupport.folderTemplatePlaceholders(subFolder),
                                                                                       null);
            handCataloguedGUIDs.add(subFolderGUID);

            String archivedFileGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                          templateOptions,
                                                                                          DataAssetTemplateDefinition.DATA_FILE_TEMPLATE.getTemplateGUID(),
                                                                                          null,
                                                                                          null,
                                                                                          FilesFvtTestSupport.fileTemplatePlaceholders(archivedFile),
                                                                                          null);
            handCataloguedGUIDs.add(archivedFileGUID);

            String siblingFolderGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                           templateOptions,
                                                                                           DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE.getTemplateGUID(),
                                                                                           null,
                                                                                           null,
                                                                                           FilesFvtTestSupport.folderTemplatePlaceholders(siblingFolder),
                                                                                           null);
            handCataloguedGUIDs.add(siblingFolderGUID);

            /*
             * The same folder and file catalogued a second time, under their canonical path names - as a
             * cataloguer on another machine would have.  Their qualified names are different from the ones
             * catalogued by hand, so nothing stops them being created, and nothing knows they are duplicates
             * until Liskov has given the first two their canonical path names.
             */
            Map<String, String> canonicalFolderPlaceholders = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(subFolder));

            canonicalFolderPlaceholders.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), canonicalPathName + "/" + subFolder.getName());

            String canonicalSubFolderGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                                templateOptions,
                                                                                                DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE.getTemplateGUID(),
                                                                                                null,
                                                                                                null,
                                                                                                canonicalFolderPlaceholders,
                                                                                                null);
            handCataloguedGUIDs.add(canonicalSubFolderGUID);

            Map<String, String> canonicalFilePlaceholders = new HashMap<>(FilesFvtTestSupport.fileTemplatePlaceholders(archivedFile));

            canonicalFilePlaceholders.put(PlaceholderProperty.FILE_PATH_NAME.getName(), canonicalPathName + "/" + subFolder.getName() + "/" + archivedFile.getName());

            String canonicalArchivedFileGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                                   templateOptions,
                                                                                                   DataAssetTemplateDefinition.DATA_FILE_TEMPLATE.getTemplateGUID(),
                                                                                                   null,
                                                                                                   null,
                                                                                                   canonicalFilePlaceholders,
                                                                                                   null);
            handCataloguedGUIDs.add(canonicalArchivedFileGUID);

            assertEquals(subFolder.getAbsolutePath(),
                         FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(subFolderGUID), OpenMetadataProperty.PATH_NAME.name),
                         "The folder catalogued by hand should start with its local path name.");
            assertEquals(archivedFile.getAbsolutePath(),
                         FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(archivedFileGUID), OpenMetadataProperty.PATH_NAME.name),
                         "The file catalogued by hand should start with its local path name.");

            hubGUID = createDataSharingHub(connectorContext, folderAssetGUID);

            addCatalogTarget(connectorContext, hubGUID, null);

            /*
             * 1. The first refresh asks for the folder to be catalogued.  That makes the folder a catalog target
             *    of the folder cataloguer, which catalogues each file as an asset of the file's own type when
             *    it next refreshes.
             */
            refreshLiskov();

            final String folderGUID = folderAssetGUID;
            final List<String> catalogActionGUIDs = new ArrayList<>();

            FilesFvtTestSupport.waitFor("Liskov to ask for the folder in its data sharing hub to be catalogued",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () ->
                                        {
                                            catalogActionGUIDs.clear();
                                            catalogActionGUIDs.addAll(getEngineActionGUIDs(openMetadataStore, folderGUID, CATALOG_FILE_FOLDER.getGovernanceRequestType()));
                                            return ! catalogActionGUIDs.isEmpty();
                                        });

            new EngineActionWaiter().waitForCompletion(catalogActionGUIDs.get(0), FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_FILE_FOLDER));

            OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnectors();

            FilesFvtTestSupport.waitFor("the folder cataloguer to catalogue the Parquet and CSV files in " + folder.getName(),
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () ->
                                        {
                                            fileAssetGUIDs.clear();
                                            fileAssetGUIDs.putAll(getCataloguedFiles(openMetadataStore, folder));
                                            return (fileAssetGUIDs.containsKey(FilesFvtTestSupport.PARQUET_FILE_NAME)) && (fileAssetGUIDs.containsKey(CSV_FILE_NAME));
                                        });

            String parquetAssetGUID = fileAssetGUIDs.get(FilesFvtTestSupport.PARQUET_FILE_NAME);
            String csvAssetGUID     = fileAssetGUIDs.get(CSV_FILE_NAME);

            /*
             * 2. Liskov was not told about these files - it finds them by looking for the catalogued files that
             *    are under the folder.  Each is surveyed with the survey for its own type of file.  Refreshing
             *    twice makes sure that one refresh starts after the files were catalogued.
             */
            refreshLiskov();
            refreshLiskov();

            FilesFvtTestSupport.waitFor("Liskov's surveys of the Parquet and CSV files in the folder in its data sharing hub",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () -> (isSurveyComplete(openMetadataStore, parquetAssetGUID)) && (isSurveyComplete(openMetadataStore, csvAssetGUID)));

            assertEquals(List.of(SURVEY_PARQUET_FILE.getGovernanceRequestType()),
                         getSurveyRequestTypes(openMetadataStore, parquetAssetGUID),
                         "Liskov should have requested one survey of the Parquet file in the folder, and it should be the Parquet file survey.");
            assertEquals(List.of(RequestTypeDefinition.SURVEY_CSV_FILE.getGovernanceRequestType()),
                         getSurveyRequestTypes(openMetadataStore, csvAssetGUID),
                         "Liskov should have requested one survey of the CSV file in the folder, and it should be the CSV file survey.");

            /*
             * Liskov gives the hub folder, and every folder and file catalogued beneath it, its canonical path
             * name.  The qualified names are left alone.
             */
            OpenMetadataElement subFolderAsset    = openMetadataStore.getMetadataElementByGUID(subFolderGUID);
            OpenMetadataElement archivedFileAsset = openMetadataStore.getMetadataElementByGUID(archivedFileGUID);

            assertEquals(canonicalPathName + "/" + subFolder.getName(),
                         FilesFvtTestSupport.getStringProperty(subFolderAsset, OpenMetadataProperty.PATH_NAME.name),
                         "Liskov should have set the canonical path name of the folder catalogued by hand beneath the hub folder.");
            assertEquals(FilesFvtTestSupport.folderAssetQualifiedName(subFolder),
                         FilesFvtTestSupport.getStringProperty(subFolderAsset, OpenMetadataProperty.QUALIFIED_NAME.name),
                         "Liskov should not have changed the qualified name of the folder beneath the hub folder.");
            assertEquals(canonicalPathName + "/" + subFolder.getName() + "/" + archivedFile.getName(),
                         FilesFvtTestSupport.getStringProperty(archivedFileAsset, OpenMetadataProperty.PATH_NAME.name),
                         "Liskov should have set the canonical path name of the file catalogued by hand beneath the hub folder.");
            assertTrue(FilesFvtTestSupport.getStringProperty(archivedFileAsset, OpenMetadataProperty.QUALIFIED_NAME.name).contains(archivedFile.getAbsolutePath()),
                       "Liskov should not have changed the qualified name of the file beneath the hub folder.");
            assertEquals(siblingFolder.getAbsolutePath(),
                         FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(siblingFolderGUID), OpenMetadataProperty.PATH_NAME.name),
                         "Liskov should not have changed the path name of a folder that is beside the hub folder, not in it.");
            /*
             * Now that they have the same path names, Liskov has linked each pair as potential duplicates.
             * Nothing in this deployment validates the links, so they are still DISCOVERED.  The folder
             * beside the hub folder is not linked to anything.
             */
            assertEquals(List.of(canonicalSubFolderGUID), getDiscoveredDuplicateGUIDs(openMetadataStore, subFolderGUID),
                         "Liskov should have linked the two folder assets with the same canonical path name as potential duplicates.");
            assertEquals(List.of(canonicalArchivedFileGUID), getDiscoveredDuplicateGUIDs(openMetadataStore, archivedFileGUID),
                         "Liskov should have linked the two file assets with the same canonical path name as potential duplicates.");
            assertEquals(List.of(), getDiscoveredDuplicateGUIDs(openMetadataStore, siblingFolderGUID),
                         "Liskov should not have linked the folder beside the hub folder to anything.");
            assertEquals(List.of(), getDiscoveredDuplicateGUIDs(openMetadataStore, parquetAssetGUID),
                         "The Parquet file is catalogued once, so Liskov should not have linked it to anything.");

            assertEquals(canonicalPathName + "/" + FilesFvtTestSupport.PARQUET_FILE_NAME,
                         FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(parquetAssetGUID), OpenMetadataProperty.PATH_NAME.name),
                         "The Parquet file catalogued by the folder cataloguer should have its canonical path name.");

            OpenMetadataElement folderAsset = openMetadataStore.getMetadataElementByGUID(folderAssetGUID);

            assertEquals(canonicalPathName,
                         FilesFvtTestSupport.getStringProperty(folderAsset, OpenMetadataProperty.PATH_NAME.name),
                         "Liskov should have set the folder's path name to the canonical path name that matched its files.");
            assertEquals(qualifiedName,
                         FilesFvtTestSupport.getStringProperty(folderAsset, OpenMetadataProperty.QUALIFIED_NAME.name),
                         "Liskov should not have changed the folder's qualified name.");

            List<OpenMetadataElement> reports = getSurveyReports(openMetadataStore, parquetAssetGUID);

            assertEquals(1, reports.size(), "There should be one survey report for the Parquet file.");
            assertEquals(SURVEY_PARQUET_FILE.getGovernanceRequestType(),
                         FilesFvtTestSupport.getStringProperty(reports.get(0), OpenMetadataProperty.PURPOSE.name),
                         "The survey report for the Parquet file was not produced by the Parquet file survey.");

            /*
             * 3. The surveys attached each file's columns to its asset.  On the next refresh Liskov turns them
             *    into data fields for the hub - so a data field for a column that is only in the Parquet file
             *    shows that Liskov read the Parquet file's schema.
             */
            refreshLiskov();

            assertEquals(List.of(canonicalArchivedFileGUID), getDiscoveredDuplicateGUIDs(openMetadataStore, archivedFileGUID),
                         "Liskov should not link the same pair of duplicates again on a later refresh.");

            String parquetOnlyColumn  = FilesFvtTestSupport.PARQUET_FILE_COLUMNS.get(1);
            String dataSharingHubGUID = hubGUID;

            FilesFvtTestSupport.waitFor("Liskov to create a data field for column " + parquetOnlyColumn + " of the Parquet file",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () -> hasDataField(openMetadataStore, dataSharingHubGUID, parquetOnlyColumn));

            assertEquals(List.of(SURVEY_PARQUET_FILE.getGovernanceRequestType()),
                         getSurveyRequestTypes(openMetadataStore, parquetAssetGUID),
                         "Liskov requested another survey of the Parquet file within a day of the first.");
        }
        finally
        {
            if (hubGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, hubGUID);
            }

            for (String handCataloguedGUID : handCataloguedGUIDs)
            {
                if (handCataloguedGUID != null)
                {
                    FilesFvtTestSupport.purgeElement(openMetadataStore, handCataloguedGUID);
                }
            }

            for (String fileAssetGUID : getCataloguedFiles(openMetadataStore, folder).values())
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, fileAssetGUID);
            }

            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }
        }
    }


    /**
     * Return the engine actions with a particular request type that have an element as their action target.
     *
     * @param openMetadataStore store to read through
     * @param assetGUID the element that the engine actions work on
     * @param requestType the request type to look for
     * @return unique identifiers of the engine actions
     * @throws Exception problem reading from the repository
     */
    private List<String> getEngineActionGUIDs(OpenMetadataStore openMetadataStore,
                                              String            assetGUID,
                                              String            requestType) throws Exception
    {
        List<String> engineActionGUIDs = new ArrayList<>();

        RelatedMetadataElementList engineActions = openMetadataStore.getRelatedMetadataElements(assetGUID,
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
                        (requestType.equals(FilesFvtTestSupport.getStringProperty(engineAction.getElement(), OpenMetadataProperty.REQUEST_TYPE.name))))
                {
                    engineActionGUIDs.add(engineAction.getElement().getElementGUID());
                }
            }
        }

        return engineActionGUIDs;
    }


    /**
     * A folder whose files are catalogued under a canonical mount point of its own.  The folder cataloguer is
     * told which mount point to use in the configuration properties of the catalog target relationship that
     * makes the folder its target.  Liskov has to use the same one - its own configuration and the defaults
     * would give a canonical path name that matches nothing.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Liskov uses the mount points that a hub folder is catalogued with")
    public void testHubFolderWithItsOwnMountPoints() throws Exception
    {
        final String sharedMountPoint = "/files-fvt-shared";

        File folder = FilesFvtTestSupport.folderUnderTest("hubmounts");

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String              folderAssetGUID = null;
        String              hubGUID         = null;
        Map<String, String> fileAssetGUIDs  = new HashMap<>();

        try
        {
            String localMountPoint   = new File(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample()).getCanonicalPath();
            String canonicalPathName = folder.getCanonicalPath().replace(localMountPoint, sharedMountPoint);

            assertNotEquals(folder.getCanonicalPath(), canonicalPathName,
                            "The folder under test is not under the directory the platform runs in, so its canonical path name cannot be worked out.");

            /*
             * The folder asset has its local path name.
             */
            String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                                                                                                  new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder)),
                                                                                                  null);

            new EngineActionWaiter().waitForCompletion(createActionGUID, FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            folderAssetGUID = FilesFvtTestSupport.waitForElement(openMetadataStore, qualifiedName, "the folder asset for Liskov's data sharing hub").getElementGUID();

            /*
             * The folder is made a target of the folder cataloguer, which is told to catalogue its files
             * under the shared mount point.
             */
            AssetClient assetClient = connectorContext.getAssetClient();

            CatalogTargetProperties cataloguerTargetProperties = new CatalogTargetProperties();

            cataloguerTargetProperties.setCatalogTargetName(FilesFvtTestSupport.TEST_MARKER + "-hubmounts");

            Map<String, Object> cataloguerConfigurationProperties = new HashMap<>();

            cataloguerConfigurationProperties.put(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName(), sharedMountPoint);

            cataloguerTargetProperties.setConfigurationProperties(cataloguerConfigurationProperties);

            assetClient.addCatalogTarget(IntegrationConnectorDefinition.GENERAL_FOLDER_CATALOGUER.getGUID(),
                                         folderAssetGUID,
                                         new MakeAnchorOptions(assetClient.getMetadataSourceOptions()),
                                         cataloguerTargetProperties);

            hubGUID = createDataSharingHub(connectorContext, folderAssetGUID);

            addCatalogTarget(connectorContext, hubGUID, null);

            OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnectors();

            FilesFvtTestSupport.waitFor("the folder cataloguer to catalogue the CSV file in " + folder.getName(),
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () ->
                                        {
                                            fileAssetGUIDs.clear();
                                            fileAssetGUIDs.putAll(getCataloguedFiles(openMetadataStore, folder));
                                            return fileAssetGUIDs.containsKey(CSV_FILE_NAME);
                                        });

            String csvAssetGUID = fileAssetGUIDs.get(CSV_FILE_NAME);

            assertEquals(canonicalPathName + "/" + CSV_FILE_NAME,
                         FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(csvAssetGUID), OpenMetadataProperty.PATH_NAME.name),
                         "The folder cataloguer should have catalogued the CSV file under the shared mount point.");

            /*
             * Liskov finds the CSV file - and so surveys it - only if it converts the folder's path name with
             * the shared mount point.
             */
            refreshLiskov();
            refreshLiskov();

            FilesFvtTestSupport.waitFor("Liskov's survey of the CSV file catalogued under the shared mount point",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () -> isSurveyComplete(openMetadataStore, csvAssetGUID));

            assertEquals(canonicalPathName,
                         FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(folderAssetGUID), OpenMetadataProperty.PATH_NAME.name),
                         "Liskov should have given the folder the canonical path name that uses the folder's own mount point.");

            assertEquals(List.of(),
                         getEngineActionGUIDs(openMetadataStore, folderAssetGUID, CATALOG_FILE_FOLDER.getGovernanceRequestType()),
                         "The folder is already a target of the folder cataloguer, so Liskov should not have asked for it to be catalogued.");
        }
        finally
        {
            if (hubGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, hubGUID);
            }

            for (String fileAssetGUID : getCataloguedFiles(openMetadataStore, folder).values())
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, fileAssetGUID);
            }

            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }
        }
    }


    /**
     * A folder that belongs to a file system.  The file system that owns it is linked to the folder above it,
     * and has mount points of its own; a second file system that merely uses the files is linked to the same
     * folder, and a third is linked to the folder above that.  Liskov has to walk up the folder hierarchy,
     * stop at the nearest folder that is linked to a file system, set aside the file system that only uses the
     * files, and take the mount points from the owner in preference to everything else.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Liskov takes the mount points from the file system that a hub folder belongs to")
    public void testHubFolderWithFileSystem() throws Exception
    {
        final String fileSystemMountPoint = "/files-fvt-filesystem";

        File folder       = FilesFvtTestSupport.folderUnderTest("hubfilesystem");
        File parentFolder = folder.getParentFile();

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String       folderAssetGUID = null;
        String       hubGUID         = null;
        List<String> otherGUIDs      = new ArrayList<>();

        try
        {
            String localMountPoint   = new File(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample()).getCanonicalPath();
            String canonicalPathName = folder.getCanonicalPath().replace(localMountPoint, fileSystemMountPoint);

            assertNotEquals(folder.getCanonicalPath(), canonicalPathName,
                            "The folder under test is not under the directory the platform runs in, so its canonical path name cannot be worked out.");

            String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                                                                                                  new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder)),
                                                                                                  null);

            new EngineActionWaiter().waitForCompletion(createActionGUID, FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            folderAssetGUID = FilesFvtTestSupport.waitForElement(openMetadataStore, qualifiedName, "the folder asset for Liskov's data sharing hub").getElementGUID();

            /*
             * The folder above, with the hub folder nested in it.
             */
            ElementProperties parentProperties = propertyHelper.addStringProperty(null,
                                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                  OpenMetadataType.FILE_FOLDER.typeName + "::" + FilesFvtTestSupport.TEST_MARKER + "::hubfilesystem-parent");

            parentProperties = propertyHelper.addStringProperty(parentProperties, OpenMetadataProperty.DISPLAY_NAME.name, FilesFvtTestSupport.TEST_MARKER + " parent folder");
            parentProperties = propertyHelper.addStringProperty(parentProperties, OpenMetadataProperty.PATH_NAME.name, parentFolder.getAbsolutePath());

            String parentFolderGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.FILE_FOLDER.typeName, null, null, parentProperties);

            otherGUIDs.add(parentFolderGUID);

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName, parentFolderGUID, folderAssetGUID, null, null, null);

            /*
             * The folder above that.  The file system linked to it also owns its files, but it is further
             * away than the ones linked to the parent folder, so it is not the one to use.
             */
            ElementProperties grandparentProperties = propertyHelper.addStringProperty(null,
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                       OpenMetadataType.FILE_FOLDER.typeName + "::" + FilesFvtTestSupport.TEST_MARKER + "::hubfilesystem-grandparent");

            grandparentProperties = propertyHelper.addStringProperty(grandparentProperties, OpenMetadataProperty.DISPLAY_NAME.name, FilesFvtTestSupport.TEST_MARKER + " grandparent folder");
            grandparentProperties = propertyHelper.addStringProperty(grandparentProperties, OpenMetadataProperty.PATH_NAME.name, parentFolder.getParentFile().getAbsolutePath());

            String grandparentFolderGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.FILE_FOLDER.typeName, null, null, grandparentProperties);

            otherGUIDs.add(grandparentFolderGUID);

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName, grandparentFolderGUID, parentFolderGUID, null, null, null);

            String distantFileSystemGUID = createFileSystem(openMetadataStore, "distant", "/files-fvt-too-far-away");
            otherGUIDs.add(distantFileSystemGUID);

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                           distantFileSystemGUID,
                                                           grandparentFolderGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addEnumProperty(null,
                                                                                          OpenMetadataProperty.USE_TYPE.name,
                                                                                          CapabilityAssetUseType.getOpenTypeName(),
                                                                                          CapabilityAssetUseType.OWNS.name()));

            /*
             * The file system that owns the files, and the one that only uses them, are both linked to the
             * parent folder.  They have different canonical mount points.
             */
            String owningFileSystemGUID = createFileSystem(openMetadataStore, "owner", fileSystemMountPoint);
            otherGUIDs.add(owningFileSystemGUID);

            String usingFileSystemGUID = createFileSystem(openMetadataStore, "user", "/files-fvt-not-this-one");
            otherGUIDs.add(usingFileSystemGUID);

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                           owningFileSystemGUID,
                                                           parentFolderGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addEnumProperty(null,
                                                                                          OpenMetadataProperty.USE_TYPE.name,
                                                                                          CapabilityAssetUseType.getOpenTypeName(),
                                                                                          CapabilityAssetUseType.OWNS.name()));

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                           usingFileSystemGUID,
                                                           parentFolderGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addEnumProperty(null,
                                                                                          OpenMetadataProperty.USE_TYPE.name,
                                                                                          CapabilityAssetUseType.getOpenTypeName(),
                                                                                          CapabilityAssetUseType.USES.name()));

            hubGUID = createDataSharingHub(connectorContext, folderAssetGUID);

            addCatalogTarget(connectorContext, hubGUID, null);

            refreshLiskov();

            final String folderGUID = folderAssetGUID;

            FilesFvtTestSupport.waitFor("Liskov to give the folder the canonical path name that uses its file system's mount point",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () -> canonicalPathName.equals(FilesFvtTestSupport.getStringProperty(openMetadataStore.getMetadataElementByGUID(folderGUID), OpenMetadataProperty.PATH_NAME.name)));

            /*
             * Liskov also asked for the folder to be catalogued.  Let that finish so nothing is left running.
             */
            final List<String> catalogActionGUIDs = new ArrayList<>();

            FilesFvtTestSupport.waitFor("Liskov to ask for the folder in its data sharing hub to be catalogued",
                                        "files.fvt.refresh.timeout.seconds",
                                        180,
                                        () ->
                                        {
                                            catalogActionGUIDs.clear();
                                            catalogActionGUIDs.addAll(getEngineActionGUIDs(openMetadataStore, folderGUID, CATALOG_FILE_FOLDER.getGovernanceRequestType()));
                                            return ! catalogActionGUIDs.isEmpty();
                                        });

            new EngineActionWaiter().waitForCompletion(catalogActionGUIDs.get(0), FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_FILE_FOLDER));
        }
        finally
        {
            if (hubGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, hubGUID);
            }

            for (String fileAssetGUID : getCataloguedFiles(openMetadataStore, folder).values())
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, fileAssetGUID);
            }

            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }

            for (String otherGUID : otherGUIDs)
            {
                if (otherGUID != null)
                {
                    FilesFvtTestSupport.purgeElement(openMetadataStore, otherGUID);
                }
            }
        }
    }


    /**
     * Create a file system with mount points.
     *
     * @param openMetadataStore client for the open metadata store
     * @param role what the file system is to the folder under test - makes its name unique
     * @param canonicalMountPoint its canonical mount point; the local one is the directory the platform runs in
     * @return unique identifier of the file system
     * @throws Exception the file system could not be created
     */
    private String createFileSystem(OpenMetadataStore openMetadataStore,
                                    String            role,
                                    String            canonicalMountPoint) throws Exception
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        OpenMetadataType.FILE_SYSTEM.typeName + "::" + FilesFvtTestSupport.TEST_MARKER + "::" + role);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, FilesFvtTestSupport.TEST_MARKER + " " + role + " file system");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.LOCAL_MOUNT_POINT.name, FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample());
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.CANONICAL_MOUNT_POINT.name, canonicalMountPoint);

        String fileSystemGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.FILE_SYSTEM.typeName, null, null, properties);

        assertNotNull(fileSystemGUID, "The " + role + " file system was not created.");

        return fileSystemGUID;
    }


    /**
     * Return the elements that an element is linked to as a potential duplicate, checking that each link is
     * still waiting for a decision.
     *
     * @param openMetadataStore client for the open metadata store
     * @param elementGUID unique identifier of the element
     * @return unique identifiers of the elements at the other end of its PeerDuplicateLink relationships
     * @throws Exception the links could not be read, or one of them is not DISCOVERED
     */
    private List<String> getDiscoveredDuplicateGUIDs(OpenMetadataStore openMetadataStore,
                                                     String            elementGUID) throws Exception
    {
        List<String> duplicateGUIDs = new ArrayList<>();

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(true);
        queryOptions.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

        RelatedMetadataElementList peers = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                        0,
                                                                                        OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                        queryOptions);

        if ((peers != null) && (peers.getElementList() != null))
        {
            for (RelatedMetadataElement peer : peers.getElementList())
            {
                assertEquals(Integer.toString(StatusIdentifier.DISCOVERED.getOrdinal()),
                             peer.getRelationshipProperties().getPropertiesAsStrings().get(OpenMetadataProperty.STATUS_IDENTIFIER.name),
                             "A duplicate link that Liskov creates should have a status of DISCOVERED.");

                duplicateGUIDs.add(peer.getElement().getElementGUID());
            }
        }

        return duplicateGUIDs;
    }


    /**
     * Return the file assets that have been catalogued for the files in a folder.
     *
     * @param openMetadataStore store to read through
     * @param folder folder whose files are being catalogued
     * @return file name to unique identifier of its asset
     * @throws Exception problem reading from the repository
     */
    private Map<String, String> getCataloguedFiles(OpenMetadataStore openMetadataStore,
                                                   File              folder) throws Exception
    {
        Map<String, String> fileAssetGUIDs = new HashMap<>();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        folder.getName() + "/",
                                                                        PropertyComparisonOperator.LIKE));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.DATA_FILE.typeName);
        queryOptions.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

        List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

        if (found != null)
        {
            for (OpenMetadataElement element : found)
            {
                String elementQualifiedName = FilesFvtTestSupport.getStringProperty(element, OpenMetadataProperty.QUALIFIED_NAME.name);

                if (elementQualifiedName != null)
                {
                    fileAssetGUIDs.put(elementQualifiedName.substring(elementQualifiedName.lastIndexOf('/') + 1), element.getElementGUID());
                }
            }
        }

        return fileAssetGUIDs;
    }


    /**
     * Has the latest survey of an element completed?
     *
     * @param openMetadataStore store to read through
     * @param assetGUID the element that is being surveyed
     * @return true if there is a survey report and it has a completion time
     * @throws Exception problem reading from the repository
     */
    private boolean isSurveyComplete(OpenMetadataStore openMetadataStore,
                                     String            assetGUID) throws Exception
    {
        List<OpenMetadataElement> reports = getSurveyReports(openMetadataStore, assetGUID);

        return (! reports.isEmpty()) &&
                (FilesFvtTestSupport.getStringProperty(reports.get(0), OpenMetadataProperty.COMPLETION_TIME.name) != null);
    }


    /**
     * Has Liskov created a data field with this name for a data sharing hub?  The hub's unique identifier
     * is part of the qualified name of each of its data fields.
     *
     * @param openMetadataStore store to read through
     * @param hubGUID unique identifier of the data sharing hub
     * @param columnName name of the column that the data field describes
     * @return true if the data field exists
     * @throws Exception problem reading from the repository
     */
    private boolean hasDataField(OpenMetadataStore openMetadataStore,
                                 String            hubGUID,
                                 String            columnName) throws Exception
    {
        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        hubGUID,
                                                                        PropertyComparisonOperator.LIKE));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.DATA_FIELD.typeName);
        queryOptions.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

        List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

        if (found != null)
        {
            for (OpenMetadataElement element : found)
            {
                if (columnName.equals(FilesFvtTestSupport.getStringProperty(element, OpenMetadataProperty.DISPLAY_NAME.name)))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Return every survey report for an element, whichever survey produced it, most recent first.
     *
     * @param openMetadataStore store to read through
     * @param assetGUID the element that was surveyed
     * @return survey reports
     * @throws Exception problem reading from the repository
     */
    private List<OpenMetadataElement> getSurveyReports(OpenMetadataStore openMetadataStore,
                                                       String            assetGUID) throws Exception
    {
        List<OpenMetadataElement> reports = new ArrayList<>();

        RelatedMetadataElementList relatedReports = openMetadataStore.getRelatedMetadataElements(assetGUID,
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
                    reports.add(relatedReport.getElement());
                }
            }
        }

        reports.sort((report1, report2) -> report2.getVersions().getCreateTime().compareTo(report1.getVersions().getCreateTime()));

        return reports;
    }


    /**
     * Return the request type of every engine action that has an element as its action target.
     *
     * @param openMetadataStore store to read through
     * @param assetGUID the element that the engine actions work on
     * @return request types
     * @throws Exception problem reading from the repository
     */
    private List<String> getSurveyRequestTypes(OpenMetadataStore openMetadataStore,
                                               String            assetGUID) throws Exception
    {
        List<String> requestTypes = new ArrayList<>();

        RelatedMetadataElementList engineActions = openMetadataStore.getRelatedMetadataElements(assetGUID,
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
                        (propertyHelper.isTypeOf(engineAction.getElement(), OpenMetadataType.ENGINE_ACTION.typeName)))
                {
                    requestTypes.add(FilesFvtTestSupport.getStringProperty(engineAction.getElement(), OpenMetadataProperty.REQUEST_TYPE.name));
                }
            }
        }

        return requestTypes;
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
