/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.contentpacks.core.DataAssetTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FileSurveyFVT runs the Files content pack's file survey services - the general data file survey, the CSV
 * survey and the Parquet survey - against real files this suite wrote.
 * <br>
 * Each case catalogues one file from its own catalog template and then surveys it, which is the same shape as
 * {@link FolderSurveyFVT} one level down.  The file assets are created straight from the template rather than
 * through a governance action, because the Files content pack ships no create-file action: files are meant to
 * arrive in the catalogue through a folder cataloguer.  That is covered by {@link FolderCatalogFVT}; what is
 * being tested here is the survey, so the asset is made the shortest way that produces a real one.
 * <br>
 * The surveys are separate cases rather than one test with several parts, so that a failure names which
 * survey service failed.  They are also genuinely different services reading genuinely different files - a CSV
 * survey works out the column structure from the values, a Parquet survey reads it from the file's footer and a
 * data file survey does not look inside the file at all - so a pass on one says nothing about the others.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class FileSurveyFVT
{
    /**
     * One case: a file in the tree, the template that catalogues it, and the survey that reads it.
     *
     * @param fileName file inside the folder under test
     * @param template catalog template for that kind of file
     * @param requestType survey to run against it
     * @param expectedColumns the columns the survey should attach to the asset as its schema, in order -
     *                        null for a survey that does not look inside the file
     */
    record FileSurveyCase(String                      fileName,
                          DataAssetTemplateDefinition template,
                          RequestTypeDefinition       requestType,
                          List<String>                expectedColumns)
    {
        @Override
        public String toString()
        {
            return requestType.getGovernanceRequestType() + " on " + fileName;
        }
    }


    /**
     * The file surveys the Files content pack ships, each against a file of the kind it is meant for.
     *
     * @return the cases
     */
    static Stream<FileSurveyCase> fileSurveyCases()
    {
        return Stream.of(new FileSurveyCase("measurements.csv",
                                             DataAssetTemplateDefinition.CSV_FILE_TEMPLATE,
                                             RequestTypeDefinition.SURVEY_CSV_FILE,
                                             List.of("id", "name", "reading")),
                         new FileSurveyCase(FilesFvtTestSupport.PARQUET_FILE_NAME,
                                             DataAssetTemplateDefinition.PARQUET_FILE_TEMPLATE,
                                             RequestTypeDefinition.SURVEY_PARQUET_FILE,
                                             FilesFvtTestSupport.PARQUET_FILE_COLUMNS),
                         new FileSurveyCase("notes.txt",
                                             DataAssetTemplateDefinition.DATA_FILE_TEMPLATE,
                                             RequestTypeDefinition.SURVEY_DATA_FILE,
                                             null));
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("fileSurveyCases")
    void aCataloguedFileCanBeSurveyed(FileSurveyCase testCase) throws Exception
    {
        File file = new File(FilesFvtTestSupport.folderUnderTest("template"), testCase.fileName());

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        String fileAssetGUID = null;

        try
        {
            assertTrue(file.isFile(),
                       "The file under test was not written: " + file.getAbsolutePath());

            /*
             * Catalogue the file from its own template.
             */
            TemplateOptions templateOptions = new TemplateOptions();

            templateOptions.setIsOwnAnchor(true);

            fileAssetGUID = openMetadataStore.createMetadataElementFromTemplate(null,
                                                                                templateOptions,
                                                                                testCase.template().getTemplateGUID(),
                                                                                null,
                                                                                null,
                                                                                FilesFvtTestSupport.fileTemplatePlaceholders(file),
                                                                                null);

            assertNotNull(fileAssetGUID,
                          "Creating a file asset from " + testCase.template().name() + " returned no GUID.");

            /*
             * Checked before the survey is asked for, for the same reason as in FolderSurveyFVT: without a
             * connection the survey cannot open the file however well it is written, and saying so here names
             * the asset rather than reporting it as a survey failure.
             */
            assertTrue(hasConnection(openMetadataStore, fileAssetGUID),
                       "The file asset created from " + testCase.template().name() + " has no "
                               + OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName
                               + ", so nothing can open the file it describes.");

            /*
             * Survey it.  waitForCompletion() throws unless the action reaches COMPLETED, so a survey that
             * cannot open its file stops the test here, quoting the completion message.
             */
            String surveyActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(testCase.requestType()),
                    new HashMap<>(),
                    List.of(FilesFvtTestSupport.newActionTarget(ActionTarget.ANY_ASSET.getName(), fileAssetGUID)));

            assertNotNull(surveyActionGUID,
                          "The Automated Curation service accepted the request to run "
                                  + FilesFvtTestSupport.governanceActionTypeQualifiedName(testCase.requestType())
                                  + " but returned no engine action to follow.");

            var surveyAction = new EngineActionWaiter().waitForCompletion(
                    surveyActionGUID,
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(testCase.requestType()));

            String surveyReportGUID = FilesFvtTestSupport.getRelatedGUID(openMetadataStore,
                                                                          surveyActionGUID,
                                                                          OpenMetadataType.REPORT_ORIGINATOR_RELATIONSHIP.typeName);

            assertNotNull(surveyReportGUID,
                          "The survey completed but no survey report is linked to the engine action that ran it."
                                  + "  Completion message: " + surveyAction.getCompletionMessage());

            List<String> annotationGUIDs = getAnnotationGUIDs(openMetadataStore, surveyReportGUID);

            assertFalse(annotationGUIDs.isEmpty(),
                        "The " + testCase.requestType().getGovernanceRequestType() + " survey produced a report with no"
                                + " annotations on it, although " + testCase.fileName() + " has content in it.");

            /*
             * A survey that looks inside the file leaves more than a report behind: it attaches the columns it
             * found to the asset as its schema, which is what makes the file's structure visible in the
             * catalogue.  Checking the names, in order, is what shows that the survey read this file rather
             * than merely completing.
             */
            if (testCase.expectedColumns() != null)
            {
                assertEquals(testCase.expectedColumns(),
                             getColumnNames(openMetadataStore, fileAssetGUID),
                             "The " + testCase.requestType().getGovernanceRequestType() + " survey completed but the"
                                     + " schema it attached to the asset for " + testCase.fileName() + " does not"
                                     + " list the columns that are in the file.");

                /*
                 * Both of these surveys also read the values in the file to count how many times each value
                 * occurs in a column.  For the Parquet survey that is the part that depends on the DuckDB JDBC
                 * driver, and it is skipped - without failing the survey - when the driver is missing.  This is
                 * what shows that the driver was found and the values were read.
                 */
                assertTrue(hasValueCounts(openMetadataStore, annotationGUIDs),
                           "The " + testCase.requestType().getGovernanceRequestType() + " survey completed but none"
                                   + " of its annotations records how many times each value occurs in a column of "
                                   + testCase.fileName() + ", so the values in the file were not read.");
            }
        }
        finally
        {
            if (fileAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, fileAssetGUID);
            }
        }
    }


    /**
     * Is a connection attached to this asset?
     *
     * @param openMetadataStore store to read through
     * @param assetGUID asset to look at
     * @return true if the asset names a connection
     * @throws Exception problem reading from the repository
     */
    private boolean hasConnection(OpenMetadataStore openMetadataStore,
                                  String            assetGUID) throws Exception
    {
        RelatedMetadataElementList connections =
                openMetadataStore.getRelatedMetadataElements(assetGUID,
                                                              1,
                                                              OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName,
                                                              0,
                                                              0);

        return (connections != null) && (connections.getElementList() != null) && (! connections.getElementList().isEmpty());
    }


    /**
     * Return the names of the columns in the schema attached to an asset, in the order of their positions.
     *
     * @param openMetadataStore store to read through
     * @param assetGUID asset to look at
     * @return column names, empty if the asset has no schema
     * @throws Exception problem reading from the repository
     */
    private List<String> getColumnNames(OpenMetadataStore openMetadataStore,
                                        String            assetGUID) throws Exception
    {
        final String methodName = "getColumnNames";

        PropertyHelper propertyHelper = new PropertyHelper();

        Map<Integer, String> columnNames = new TreeMap<>();

        String schemaTypeGUID = FilesFvtTestSupport.getRelatedGUID(openMetadataStore,
                                                                    assetGUID,
                                                                    OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

        if (schemaTypeGUID != null)
        {
            RelatedMetadataElementList columns =
                    openMetadataStore.getRelatedMetadataElements(schemaTypeGUID,
                                                                  1,
                                                                  OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                  0,
                                                                  FilesFvtTestSupport.MAX_PAGE_SIZE);

            if ((columns != null) && (columns.getElementList() != null))
            {
                for (RelatedMetadataElement column : columns.getElementList())
                {
                    if ((column != null) && (column.getElement() != null))
                    {
                        columnNames.put(propertyHelper.getIntProperty("files-fvt",
                                                                      OpenMetadataProperty.POSITION.name,
                                                                      column.getRelationshipProperties(),
                                                                      methodName),
                                        propertyHelper.getStringProperty("files-fvt",
                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                         column.getElement().getElementProperties(),
                                                                         methodName));
                    }
                }
            }
        }

        return new ArrayList<>(columnNames.values());
    }


    /**
     * Does any of these annotations record how many times each value occurs?
     *
     * @param openMetadataStore store to read through
     * @param annotationGUIDs annotations to look at
     * @return true if one of them has a valueCount property with entries in it
     * @throws Exception problem reading from the repository
     */
    private boolean hasValueCounts(OpenMetadataStore openMetadataStore,
                                   List<String>      annotationGUIDs) throws Exception
    {
        final String methodName = "hasValueCounts";

        PropertyHelper propertyHelper = new PropertyHelper();

        for (String annotationGUID : annotationGUIDs)
        {
            OpenMetadataElement annotation = openMetadataStore.getMetadataElementByGUID(annotationGUID);

            if (annotation != null)
            {
                Map<String, Integer> valueCount = propertyHelper.getIntegerMapFromProperty("files-fvt",
                                                                                           OpenMetadataProperty.VALUE_COUNT.name,
                                                                                           annotation.getElementProperties(),
                                                                                           methodName);

                if ((valueCount != null) && (! valueCount.isEmpty()))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Return the GUIDs of the annotations attached to a survey report.
     *
     * @param openMetadataStore store to read through
     * @param surveyReportGUID report to look at
     * @return annotation GUIDs, empty if the report carries none
     * @throws Exception problem reading from the repository
     */
    private List<String> getAnnotationGUIDs(OpenMetadataStore openMetadataStore,
                                            String            surveyReportGUID) throws Exception
    {
        List<String> annotationGUIDs = new ArrayList<>();

        RelatedMetadataElementList annotations =
                openMetadataStore.getRelatedMetadataElements(surveyReportGUID,
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
