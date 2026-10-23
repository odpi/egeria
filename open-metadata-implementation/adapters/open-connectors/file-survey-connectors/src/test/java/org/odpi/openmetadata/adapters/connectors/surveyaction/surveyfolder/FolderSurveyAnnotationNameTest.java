/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.FolderRequestParameter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyContext;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * FolderSurveyAnnotationNameTest checks that a deep folder survey gives each of its measurement annotations a
 * distinct qualified name.
 * <br><br>
 * Every annotation in the survey is attached to the surveyed folder's asset, and the annotation store refuses a
 * second annotation with the same qualified name (OMAG-COMMON-409-001).  The measurements of the nested folders
 * and files used to be told apart only by the millisecond they were set up in, and the measurements of small or
 * empty folders and files are set up far faster than that - so a survey of a folder holding an empty
 * sub-folder (such as the .ipynb_checkpoints folder that JupyterLab leaves behind) failed.  These tests survey a
 * tree of empty folders and files over a mocked survey context and check the names that reach the store.
 */
public class FolderSurveyAnnotationNameTest
{
    private static final String ASSET_GUID = "folder-asset-guid";

    private Path rootFolder;


    /**
     * Build a tree of empty folders and files: enough of them that several are measured in the same millisecond.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void createFolderTree() throws Exception
    {
        rootFolder = Files.createTempDirectory("folder-survey-test");

        for (int folderCount = 0; folderCount < 20; folderCount++)
        {
            Path folder = Files.createDirectories(rootFolder.resolve("folder-" + folderCount).resolve("nested"));

            for (int fileCount = 0; fileCount < 5; fileCount++)
            {
                Files.createFile(folder.resolve("file-" + fileCount + ".csv"));
            }
        }

        Files.createDirectory(rootFolder.resolve(".ipynb_checkpoints"));
    }


    /**
     * Remove the tree.
     *
     * @throws Exception not expected
     */
    @AfterMethod public void deleteFolderTree() throws Exception
    {
        try (Stream<Path> paths = Files.walk(rootFolder))
        {
            paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
    }


    /**
     * A survey of all folders names the measurements of each nested folder by its path within the surveyed
     * folder, and leaves the surveyed folder's own measurement without a subject.
     *
     * @throws Exception not expected
     */
    @Test public void testAllFoldersHaveDistinctNames() throws Exception
    {
        List<String> qualifiedNames = this.runSurvey("ALL_FOLDERS");

        assertDistinct(qualifiedNames);
        assertTrue(hasName(qualifiedNames, ASSET_GUID + "::Capture File Counts::.ipynb_checkpoints::"), qualifiedNames.toString());
        assertTrue(hasName(qualifiedNames, ASSET_GUID + "::Capture File Counts::folder-7/nested::"), qualifiedNames.toString());
        assertEquals(qualifiedNames.stream().filter(name -> name.matches(ASSET_GUID + "::Capture File Counts::\\d+")).count(), 1L);
    }


    /**
     * A survey of all folders and files also names the measurements of each file by its path within the
     * surveyed folder.
     *
     * @throws Exception not expected
     */
    @Test public void testAllFoldersAndFilesHaveDistinctNames() throws Exception
    {
        List<String> qualifiedNames = this.runSurvey("ALL_FOLDERS_AND_FILES");

        assertDistinct(qualifiedNames);
        assertTrue(hasName(qualifiedNames, ASSET_GUID + "::Capture File Counts::folder-3/nested::"), qualifiedNames.toString());
        assertEquals(qualifiedNames.stream().filter(name -> name.contains("/nested/file-")).count(), 100L, qualifiedNames.toString());
    }


    /**
     * Run a folder survey over the tree, stopping once the measurements and profiles are stored.
     *
     * @param analysisLevel analysisLevel request parameter
     * @return qualified names of the annotations stored, in order
     * @throws Exception not expected
     */
    private List<String> runSurvey(String analysisLevel) throws Exception
    {
        List<String> qualifiedNames = new ArrayList<>();

        FolderSurveyService survey = new FolderSurveyService();

        survey.initialize("folder-survey", new Connection());
        survey.setAuditLog(mock(AuditLog.class));
        survey.setSurveyContext(this.getSurveyContext(analysisLevel, qualifiedNames));
        survey.start();

        return qualifiedNames;
    }


    /**
     * A mocked survey context for the folder asset.  The annotation store records the qualified name of each
     * annotation it is asked to store.
     *
     * @param analysisLevel analysisLevel request parameter
     * @param qualifiedNames receives the qualified names of the stored annotations
     * @return survey context
     * @throws Exception not expected
     */
    private SurveyContext getSurveyContext(String       analysisLevel,
                                           List<String> qualifiedNames) throws Exception
    {
        SurveyContext   surveyContext   = mock(SurveyContext.class);
        AnnotationStore annotationStore = mock(AnnotationStore.class);
        String[]        currentStep     = new String[1];

        doAnswer(invocation -> { currentStep[0] = invocation.getArgument(0); return null; }).when(annotationStore).setAnalysisStep(anyString());
        doAnswer(invocation -> currentStep[0]).when(annotationStore).getAnalysisStep();
        doAnswer(invocation ->
                 {
                     qualifiedNames.add(((AnnotationProperties)invocation.getArgument(0)).getQualifiedName());
                     return "annotation-guid-" + qualifiedNames.size();
                 }).when(annotationStore).addAnnotation(any(AnnotationProperties.class), isNull());
        when(annotationStore.getSurveyReportGUID()).thenReturn("survey-report-guid");

        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put(FolderRequestParameter.ANALYSIS_LEVEL.getName(), analysisLevel);
        requestParameters.put(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName(), AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

        ElementType elementType = new ElementType();

        elementType.setTypeName(OpenMetadataType.FILE_FOLDER.typeName);

        ElementHeader elementHeader = new ElementHeader();

        elementHeader.setGUID(ASSET_GUID);
        elementHeader.setType(elementType);

        OpenMetadataRootElement assetElement = new OpenMetadataRootElement();

        assetElement.setElementHeader(elementHeader);

        AssetClient assetClient = mock(AssetClient.class);

        when(assetClient.getAssetByGUID(anyString(), any())).thenReturn(assetElement);

        BasicFolderConnector folderConnector = mock(BasicFolderConnector.class);

        when(folderConnector.getFile()).thenReturn(rootFolder.toFile());

        SurveyAssetStore assetStore = mock(SurveyAssetStore.class);

        when(assetStore.getAssetProperties()).thenReturn(assetElement);
        when(assetStore.getConnectorToAsset()).thenReturn(folderConnector);
        when(assetStore.getAssetGUID()).thenReturn(ASSET_GUID);

        FileClassifier fileClassifier = mock(FileClassifier.class);

        when(fileClassifier.classifyFile(any(File.class))).thenAnswer(invocation -> getFileClassification(invocation.getArgument(0)));

        when(surveyContext.getAnnotationStore()).thenReturn(annotationStore);
        when(surveyContext.getAssetStore()).thenReturn(assetStore);
        when(surveyContext.getAssetClient(anyString())).thenReturn(assetClient);
        when(surveyContext.getFileClassifier(any(), any(), any())).thenReturn(fileClassifier);
        when(surveyContext.getRequestParameters()).thenReturn(requestParameters);
        when(surveyContext.getAssetGUID()).thenReturn(ASSET_GUID);

        return surveyContext;
    }


    /**
     * The classification of one of the empty test files.
     *
     * @param file test file
     * @return classification
     */
    private static FileClassification getFileClassification(File file)
    {
        return new FileClassification("test-file-system",
                                      file.getName(),
                                      file.getAbsolutePath(),
                                      "csv",
                                      file.getAbsolutePath(),
                                      null,
                                      null,
                                      null,
                                      true,
                                      true,
                                      false,
                                      false,
                                      false,
                                      "CSV Data File",
                                      "CSV Data File",
                                      null,
                                      "CSVFile",
                                      0L);
    }


    /**
     * Fail if any qualified name was stored more than once - the annotation store would refuse the second one.
     *
     * @param qualifiedNames qualified names of the stored annotations
     */
    private static void assertDistinct(List<String> qualifiedNames)
    {
        Set<String> seen       = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (String qualifiedName : qualifiedNames)
        {
            if (! seen.add(qualifiedName))
            {
                duplicates.add(qualifiedName);
            }
        }

        assertTrue(duplicates.isEmpty(), "Duplicate qualified names: " + duplicates);
    }


    /**
     * Return whether one of the qualified names starts with the prefix and is followed only by a timestamp.
     *
     * @param qualifiedNames qualified names of the stored annotations
     * @param prefix expected name, up to the timestamp
     * @return flag
     */
    private static boolean hasName(List<String> qualifiedNames,
                                   String       prefix)
    {
        return qualifiedNames.stream().anyMatch(name -> name.startsWith(prefix) && name.substring(prefix.length()).matches("\\d+"));
    }
}
