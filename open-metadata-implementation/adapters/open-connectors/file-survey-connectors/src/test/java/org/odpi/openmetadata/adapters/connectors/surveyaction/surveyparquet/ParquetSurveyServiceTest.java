/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyparquet;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.ParquetFileStoreConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.ParquetFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.SchemaAnalysisAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyContext;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;


/**
 * Run the Parquet survey over a real Parquet file, with the open metadata repository mocked, to check the
 * annotations and schema attributes that it produces at each analysis step.  The file has five top-level
 * columns - id, scores (a list), address (a nested structure), counters (a map) and items (a list of structures)
 * - which between them hold values in nine leaf columns.
 */
public class ParquetSurveyServiceTest
{
    private static final String ASSET_GUID       = "parquet-asset-guid";
    private static final String SCHEMA_TYPE_GUID = "schema-type-guid";
    private static final String parquetFileName  = "src/test/resources/nested-v1-gzip.parquet";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    /*
     * What the survey stored - set up by each call to getSurveyContext().
     */
    private List<AnnotationProperties> annotations;
    private List<String>               annotatedElementGUIDs;
    private List<NewElementProperties> newColumnProperties;
    private List<NewElementProperties> newColumnClassifications;
    private List<NewElementOptions>    newColumnOptions;
    private OpenMetadataStore          openMetadataStore;


    /**
     * A complete survey of a file that has no schema attached to it.
     *
     * @throws Exception not expected
     */
    @Test public void testFullSurvey() throws Exception
    {
        this.runSurvey(parquetFileName, null, null);

        /*
         * Two measurements, the schema and a profile for each of the nine leaf columns.
         */
        assertEquals(annotations.size(), 12);

        assertEquals(annotations.get(0).getAnnotationType(), "Extract File Properties");
        assertEquals(annotatedElementGUIDs.get(0), ASSET_GUID);

        ResourceMeasureAnnotationProperties measurements = (ResourceMeasureAnnotationProperties) annotations.get(1);

        assertEquals(measurements.getAnnotationType(), "Extract Parquet File Properties");
        assertEquals(measurements.getAnalysisStep(), AnalysisStep.MEASURE_RESOURCE.getName());
        assertEquals(annotatedElementGUIDs.get(1), ASSET_GUID);
        assertEquals(measurements.getResourceProperties().get("Row Count"), "20");
        assertEquals(measurements.getResourceProperties().get("Row Group Count"), "1");
        assertEquals(measurements.getResourceProperties().get("Column Count"), "5");
        assertEquals(measurements.getResourceProperties().get("Leaf Column Count"), "9");
        assertEquals(measurements.getResourceProperties().get("Format Version"), "1");
        assertEquals(measurements.getResourceProperties().get("Compression Codecs"), "[GZIP]");
        assertEquals(measurements.getResourceProperties().get("Data Page Versions"), "[V1]");
        assertEquals(measurements.getResourceProperties().get("Type Annotation Style"), "LogicalType");
        assertEquals(measurements.getResourceProperties().get("Writer Property Names"), "[origin, purpose]");
        assertTrue(measurements.getResourceProperties().get("Created By").startsWith("DuckDB"));
        assertTrue(measurements.getJsonProperties().contains("\"rowCount\":20"), measurements.getJsonProperties());

        SchemaAnalysisAnnotationProperties schemaAnnotation = (SchemaAnalysisAnnotationProperties) annotations.get(2);

        assertEquals(schemaAnnotation.getAnnotationType(), "Extract Schema From Footer");
        assertEquals(annotatedElementGUIDs.get(2), SCHEMA_TYPE_GUID);
        assertEquals(schemaAnnotation.getSchemaType(), OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName);
        assertEquals(schemaAnnotation.getAdditionalProperties().get("Column Count"), "5");
        assertEquals(schemaAnnotation.getAdditionalProperties().get("Column Names"), "[id, scores, address, counters, items]");

        /*
         * The schema type is created and then a schema attribute for each top-level column.
         */
        verify(openMetadataStore, times(1)).createMetadataElementInStore(eq(OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName), any(NewElementProperties.class));
        verify(openMetadataStore, never()).deleteMetadataElementInStore(anyString(), any(DeleteOptions.class));

        assertEquals(newColumnProperties.size(), 5);

        validateNewColumn(0, "id", "int", OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);
        validateNewColumn(1, "scores", "array<int>", OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName);
        validateNewColumn(2, "address", "struct<street:string,geo:struct<lat:decimal(21,1),lon:decimal(21,1)>>", OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName);
        validateNewColumn(3, "counters", "map<string,long>", OpenMetadataType.MAP_SCHEMA_TYPE.typeName);
        validateNewColumn(4, "items", "array<struct<sku:string,qty:long>>", OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName);

        /*
         * Each profile is attached to the schema attribute for its top-level column, and is named after the leaf column.
         */
        validateProfile(3, "id", "column-guid-0", "int", "0", "19", 20L);
        validateProfile(4, "scores.list.element", "column-guid-1", "int", "0", "21", 60L);
        validateProfile(5, "address.street", "column-guid-2", "string", "road 0", "road 9", 20L);
        validateProfile(6, "address.geo.lat", "column-guid-2", "decimal(21,1)", "0.0", "9.5", 20L);
        validateProfile(7, "address.geo.lon", "column-guid-2", "decimal(21,1)", "-9.5", "0.0", 20L);
        validateProfile(8, "counters.key_value.key", "column-guid-3", "string", "a", "b", 40L);
        validateProfile(9, "counters.key_value.value", "column-guid-3", "long", "0", "38", 40L);
        validateProfile(10, "items.list.element.sku", "column-guid-4", "string", "x0", "x9", 20L);
        validateProfile(11, "items.list.element.qty", "column-guid-4", "long", "0", "19", 20L);

        ResourceProfileAnnotationProperties keyProfile = (ResourceProfileAnnotationProperties) annotations.get(8);

        assertEquals(keyProfile.getProfileCounts().get("Distinct Count"), Long.valueOf(2L));
        assertEquals(keyProfile.getProfileProperties().get("Physical Type"), "BYTE_ARRAY");
        assertEquals(keyProfile.getProfileProperties().get("Compression Codecs"), "[GZIP]");
        assertEquals(keyProfile.getProfileProperties().get("Encodings"), "[PLAIN_DICTIONARY]");
        assertEquals(keyProfile.getProfileFlags().get("Encrypted"), Boolean.FALSE);

        /*
         * The values of the top-level columns that hold a single value are read to count how often each value occurs.
         * The id column has a different value in each of the 20 rows.  The values nested inside the other columns
         * are not counted.
         */
        ResourceProfileAnnotationProperties idProfile = (ResourceProfileAnnotationProperties) annotations.get(3);

        assertEquals(idProfile.getValueCount().size(), 20);
        assertEquals(idProfile.getValueCount().get("7"), Integer.valueOf(1));
        assertEquals(idProfile.getValueList().size(), 20);
        assertEquals(idProfile.getProfileCounts().get("Distinct Count"), Long.valueOf(20L));
        assertTrue(idProfile.getProfilePropertyNames().contains("Distinct Count"));
        assertNull(keyProfile.getValueCount());
        assertNull(((ResourceProfileAnnotationProperties) annotations.get(4)).getValueCount());

        /*
         * The annotation store refuses two annotations with the same qualified name.
         */
        HashSet<String> qualifiedNames = new HashSet<>();

        for (AnnotationProperties annotation : annotations)
        {
            assertNotNull(annotation.getQualifiedName());
            assertTrue(qualifiedNames.add(annotation.getQualifiedName()), annotation.getQualifiedName());
        }
    }


    /**
     * A survey of a file with 5000 rows in three row groups.  The counts of each value are recorded for the
     * columns with up to 100 different values: name has 18 and id has 5000.
     *
     * @throws Exception not expected
     */
    @Test public void testValueCounts() throws Exception
    {
        this.runSurvey("src/test/resources/rowgroups-v2-zstd.parquet", null, null);

        assertEquals(annotations.size(), 6);

        ResourceProfileAnnotationProperties idProfile   = (ResourceProfileAnnotationProperties) annotations.get(3);
        ResourceProfileAnnotationProperties nameProfile = (ResourceProfileAnnotationProperties) annotations.get(4);

        assertNull(idProfile.getValueCount());
        assertNull(idProfile.getValueList());
        assertNull(idProfile.getProfileCounts().get("Distinct Count"));
        assertEquals(idProfile.getValueRangeFrom(), "0");
        assertEquals(idProfile.getValueRangeTo(), "4999");

        assertEquals(nameProfile.getValueCount().size(), 18);
        assertEquals(nameProfile.getValueCount().get("customer-7"), Integer.valueOf(250));
        assertEquals(nameProfile.getValueList().get(0), "customer-1");
        assertEquals(nameProfile.getProfileCounts().get("Distinct Count"), Long.valueOf(18L));
        assertEquals(nameProfile.getProfileCounts().get("Null Count"), Long.valueOf(500L));
        assertEquals(nameProfile.getProfileCounts().get("Value Count"), Long.valueOf(5000L));
    }


    /**
     * A survey that stops once the file is measured does not touch the schema.
     *
     * @throws Exception not expected
     */
    @Test public void testStopAfterMeasureResource() throws Exception
    {
        this.runSurvey(parquetFileName, AnalysisStep.MEASURE_RESOURCE, null);

        assertEquals(annotations.size(), 2);
        assertEquals(annotations.get(1).getAnnotationType(), "Extract Parquet File Properties");
        assertTrue(newColumnProperties.isEmpty());

        verify(openMetadataStore, never()).createMetadataElementInStore(anyString(), any(NewElementProperties.class));
    }


    /**
     * A survey that stops once the schema is extracted creates the columns, and does not profile them.
     *
     * @throws Exception not expected
     */
    @Test public void testStopAfterSchemaExtraction() throws Exception
    {
        this.runSurvey(parquetFileName, AnalysisStep.SCHEMA_EXTRACTION, null);

        assertEquals(annotations.size(), 3);
        assertEquals(annotations.get(2).getAnnotationType(), "Extract Schema From Footer");
        assertEquals(newColumnProperties.size(), 5);
    }


    /**
     * A survey of a file that already has a schema attached.  The existing schema has a column that is still
     * in the file (id), which is updated, and one that is not (obsolete), which is archived.  The other four
     * columns in the file are added.
     *
     * @throws Exception not expected
     */
    @Test public void testExistingSchema() throws Exception
    {
        List<RelatedMetadataElementSummary> existingColumns = new ArrayList<>();

        existingColumns.add(getExistingColumn("obsolete", "existing-obsolete-guid", "existing-obsolete-relationship-guid"));
        existingColumns.add(getExistingColumn("id", "existing-id-guid", "existing-id-relationship-guid"));

        this.runSurvey(parquetFileName, null, existingColumns);

        verify(openMetadataStore, never()).createMetadataElementInStore(eq(OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName), any(NewElementProperties.class));
        verify(openMetadataStore, times(1)).deleteMetadataElementInStore(eq("existing-obsolete-guid"), any(DeleteOptions.class));
        verify(openMetadataStore, never()).deleteMetadataElementInStore(eq("existing-id-guid"), any(DeleteOptions.class));
        verify(openMetadataStore, times(1)).updateMetadataElementInStore(eq("existing-id-guid"), eq(false), any(ElementProperties.class));
        verify(openMetadataStore, times(1)).updateRelatedElementsInStore(eq("existing-id-relationship-guid"), any(), any(ElementProperties.class));
        verify(openMetadataStore, times(1)).reclassifyMetadataElementInStore(eq("existing-id-guid"),
                                                                             eq(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName),
                                                                             anyBoolean(),
                                                                             any(ElementProperties.class));

        assertEquals(newColumnProperties.size(), 4);
        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.DISPLAY_NAME.name, newColumnProperties.get(0), "test"), "scores");

        /*
         * The profile of the id column is attached to the existing schema attribute.
         */
        assertEquals(annotations.size(), 12);
        assertEquals(annotatedElementGUIDs.get(3), "existing-id-guid");
        assertEquals(annotatedElementGUIDs.get(2), SCHEMA_TYPE_GUID);
    }


    /**
     * A survey of a file that is catalogued as a Parquet file, but is not one, fails with a description of the
     * problem with the file and without storing anything.
     *
     * @throws Exception not expected
     */
    @Test public void testNotParquetFile() throws Exception
    {
        File file = Files.createTempFile("ParquetSurveyServiceTest", ".parquet").toFile();

        file.deleteOnExit();
        Files.writeString(file.toPath(), "id,name\n1,this is a CSV file\n", StandardCharsets.UTF_8);

        try
        {
            this.runSurvey(file.getAbsolutePath(), null, null);
            fail("The survey should have failed");
        }
        catch (ConnectorCheckedException error)
        {
            assertEquals(error.getReportedErrorMessageId(), "PARQUET-FILE-CONNECTOR-400-001");
        }

        assertTrue(annotations.isEmpty());
        assertTrue(newColumnProperties.isEmpty());
    }


    /**
     * Check the schema attribute created for a column.
     *
     * @param position which column
     * @param name expected name
     * @param dataType expected data type
     * @param schemaTypeName expected type of schema type
     */
    private void validateNewColumn(int    position,
                                   String name,
                                   String dataType,
                                   String schemaTypeName)
    {
        final String methodName = "validateNewColumn";

        NewElementProperties properties     = newColumnProperties.get(position);
        NewElementProperties classification = newColumnClassifications.get(position);
        NewElementOptions    options        = newColumnOptions.get(position);

        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.DISPLAY_NAME.name, properties, methodName), name);
        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.QUALIFIED_NAME.name, properties, methodName),
                     "ParquetFile::test_column_" + position + "_" + name);
        assertFalse(propertyHelper.getBooleanProperty("test", OpenMetadataProperty.IS_NULLABLE.name, properties, methodName));
        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.DATA_TYPE.name, classification, methodName), dataType);
        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.SCHEMA_TYPE_NAME.name, classification, methodName), schemaTypeName);
        assertEquals(options.getAnchorGUID(), ASSET_GUID);
        assertEquals(options.getParentGUID(), SCHEMA_TYPE_GUID);
        assertEquals(options.getParentRelationshipTypeName(), OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);
    }


    /**
     * Check the profile of a leaf column.
     *
     * @param position which annotation
     * @param pathName expected path name of the leaf column
     * @param schemaAttributeGUID the schema attribute that the annotation should be attached to
     * @param dataType expected data type
     * @param minValue expected smallest value
     * @param maxValue expected largest value
     * @param valueCount expected number of values
     */
    private void validateProfile(int    position,
                                 String pathName,
                                 String schemaAttributeGUID,
                                 String dataType,
                                 String minValue,
                                 String maxValue,
                                 long   valueCount)
    {
        ResourceProfileAnnotationProperties profile = (ResourceProfileAnnotationProperties) annotations.get(position);

        assertEquals(profile.getAnnotationType(), "Profile Column From Statistics");
        assertEquals(profile.getAnalysisStep(), AnalysisStep.PROFILE_DATA.getName());
        assertTrue(profile.getQualifiedName().startsWith(ASSET_GUID + "::Profile Column From Statistics::" + pathName + "::"), profile.getQualifiedName());
        assertEquals(annotatedElementGUIDs.get(position), schemaAttributeGUID, pathName);
        assertEquals(profile.getProfileProperties().get("Path Name"), pathName);
        assertEquals(profile.getDataType(), dataType, pathName);
        assertEquals(profile.getValueRangeFrom(), minValue, pathName);
        assertEquals(profile.getValueRangeTo(), maxValue, pathName);
        assertEquals(profile.getProfileCounts().get("Value Count"), Long.valueOf(valueCount), pathName);
        assertEquals(profile.getProfileCounts().get("Null Count"), Long.valueOf(0L), pathName);
        assertTrue(profile.getProfilePropertyNames().contains("Compressed Size"));
    }


    /**
     * Return the description of a column that is already attached to the file's schema.
     *
     * @param name display name of the column
     * @param guid unique identifier of the schema attribute
     * @param relationshipGUID unique identifier of the relationship to the schema type
     * @return related element
     */
    private RelatedMetadataElementSummary getExistingColumn(String name,
                                                            String guid,
                                                            String relationshipGUID)
    {
        TabularColumnProperties properties = new TabularColumnProperties();

        properties.setDisplayName(name);

        MetadataElementSummary relatedElement = new MetadataElementSummary();

        relatedElement.setElementHeader(getElementHeader(guid, OpenMetadataType.TABULAR_COLUMN.typeName));
        relatedElement.setProperties(properties);

        RelatedMetadataElementSummary existingColumn = new RelatedMetadataElementSummary();

        existingColumn.setRelatedElement(relatedElement);
        existingColumn.setRelationshipHeader(getElementHeader(relationshipGUID, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName));

        return existingColumn;
    }


    /**
     * Return an element header.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @return header
     */
    private ElementHeader getElementHeader(String guid,
                                           String typeName)
    {
        ElementType elementType = new ElementType();

        elementType.setTypeName(typeName);

        ElementHeader elementHeader = new ElementHeader();

        elementHeader.setGUID(guid);
        elementHeader.setType(elementType);

        return elementHeader;
    }


    /**
     * Run the Parquet survey.
     *
     * @param fileName the file to survey
     * @param finalAnalysisStep the step to stop after - or null to run the whole survey
     * @param existingColumns the schema attributes already attached to the asset - or null if it has no schema
     * @throws Exception the survey failed
     */
    private void runSurvey(String                              fileName,
                           AnalysisStep                        finalAnalysisStep,
                           List<RelatedMetadataElementSummary> existingColumns) throws Exception
    {
        ParquetSurveyService survey = new ParquetSurveyService();

        survey.initialize("parquet-survey", new Connection());
        survey.setAuditLog(mock(AuditLog.class));
        survey.setSurveyActionServiceName("parquet-survey");
        survey.setSurveyContext(this.getSurveyContext(fileName, finalAnalysisStep, existingColumns));

        try
        {
            survey.start();
        }
        finally
        {
            survey.disconnect();
        }
    }


    /**
     * A mocked survey context for the Parquet file asset.  The annotations, and the schema attributes, that
     * the survey stores are recorded.
     *
     * @param fileName the file to survey
     * @param finalAnalysisStep the step to stop after - or null to run the whole survey
     * @param existingColumns the schema attributes already attached to the asset - or null if it has no schema
     * @return survey context
     * @throws Exception not expected
     */
    @SuppressWarnings("unchecked")
    private SurveyContext getSurveyContext(String                              fileName,
                                           AnalysisStep                        finalAnalysisStep,
                                           List<RelatedMetadataElementSummary> existingColumns) throws Exception
    {
        annotations              = new ArrayList<>();
        annotatedElementGUIDs    = new ArrayList<>();
        newColumnProperties      = new ArrayList<>();
        newColumnClassifications = new ArrayList<>();
        newColumnOptions         = new ArrayList<>();
        openMetadataStore        = mock(OpenMetadataStore.class);

        SurveyContext   surveyContext   = mock(SurveyContext.class);
        AnnotationStore annotationStore = mock(AnnotationStore.class);
        String[]        currentStep     = new String[1];

        doAnswer(invocation -> { currentStep[0] = invocation.getArgument(0); return null; }).when(annotationStore).setAnalysisStep(anyString());
        doAnswer(invocation -> currentStep[0]).when(annotationStore).getAnalysisStep();
        doAnswer(invocation ->
                 {
                     annotations.add(invocation.getArgument(0));
                     annotatedElementGUIDs.add(invocation.getArgument(1));
                     return "annotation-guid-" + annotations.size();
                 }).when(annotationStore).addAnnotation(any(AnnotationProperties.class), anyString());
        when(annotationStore.getSurveyReportGUID()).thenReturn("survey-report-guid");

        Map<String, String> requestParameters = new HashMap<>();

        if (finalAnalysisStep != null)
        {
            requestParameters.put(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName(), finalAnalysisStep.getName());
        }

        ParquetFileProperties assetProperties = new ParquetFileProperties();

        assetProperties.setQualifiedName("ParquetFile::test");

        OpenMetadataRootElement assetElement = new OpenMetadataRootElement();

        assetElement.setElementHeader(getElementHeader(ASSET_GUID, OpenMetadataType.PARQUET_FILE.typeName));
        assetElement.setProperties(assetProperties);

        if (existingColumns != null)
        {
            MetadataElementSummary schemaTypeSummary = new MetadataElementSummary();

            schemaTypeSummary.setElementHeader(getElementHeader(SCHEMA_TYPE_GUID, OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName));

            RelatedMetadataElementSummary schemaTypeLink = new RelatedMetadataElementSummary();

            schemaTypeLink.setRelatedElement(schemaTypeSummary);

            assetElement.setSchemaType(schemaTypeLink);

            OpenMetadataRootElement schemaTypeElement = new OpenMetadataRootElement();

            schemaTypeElement.setElementHeader(getElementHeader(SCHEMA_TYPE_GUID, OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName));
            schemaTypeElement.setSchemaAttributes(existingColumns);

            SchemaTypeClient schemaTypeClient = mock(SchemaTypeClient.class);

            when(schemaTypeClient.getSchemaTypeByGUID(eq(SCHEMA_TYPE_GUID), any())).thenReturn(schemaTypeElement);
            when(surveyContext.getSchemaTypeClient(anyString())).thenReturn(schemaTypeClient);
        }

        AssetClient assetClient = mock(AssetClient.class);

        when(assetClient.getAssetByGUID(anyString(), any())).thenReturn(assetElement);

        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(fileName);

        Connection connection = new Connection();

        connection.setEndpoint(endpoint);

        ParquetFileStoreConnector fileConnector = new ParquetFileStoreConnector();

        fileConnector.initialize("parquet-file", connection);

        SurveyAssetStore assetStore = mock(SurveyAssetStore.class);

        when(assetStore.getAssetProperties()).thenReturn(assetElement);
        when(assetStore.getConnectorToAsset()).thenReturn(fileConnector);
        when(assetStore.getAssetGUID()).thenReturn(ASSET_GUID);

        FileClassifier fileClassifier = mock(FileClassifier.class);

        when(fileClassifier.classifyFile(any(File.class))).thenAnswer(invocation -> getFileClassification(invocation.getArgument(0)));

        when(openMetadataStore.createMetadataElementInStore(eq(OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName), any(NewElementProperties.class))).thenReturn(SCHEMA_TYPE_GUID);
        when(openMetadataStore.createMetadataElementInStore(eq(OpenMetadataType.TABULAR_COLUMN.typeName),
                                                            any(NewElementOptions.class),
                                                            any(Map.class),
                                                            any(NewElementProperties.class),
                                                            any(NewElementProperties.class))).thenAnswer(invocation ->
                 {
                     Map<String, NewElementProperties> classifications = invocation.getArgument(2);

                     newColumnOptions.add(invocation.getArgument(1));
                     newColumnClassifications.add(classifications.get(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName));
                     newColumnProperties.add(invocation.getArgument(3));

                     return "column-guid-" + (newColumnProperties.size() - 1);
                 });

        when(surveyContext.getAnnotationStore()).thenReturn(annotationStore);
        when(surveyContext.getAssetStore()).thenReturn(assetStore);
        when(surveyContext.getOpenMetadataStore()).thenReturn(openMetadataStore);
        when(surveyContext.getAssetClient(anyString())).thenReturn(assetClient);
        when(surveyContext.getFileClassifier(any(), any(), any())).thenReturn(fileClassifier);
        when(surveyContext.getRequestParameters()).thenReturn(requestParameters);
        when(surveyContext.getAssetGUID()).thenReturn(ASSET_GUID);

        return surveyContext;
    }


    /**
     * The classification of the test file.
     *
     * @param file test file
     * @return classification
     */
    private static FileClassification getFileClassification(File file)
    {
        return new FileClassification("test-file-system",
                                      file.getName(),
                                      file.getAbsolutePath(),
                                      "parquet",
                                      file.getAbsolutePath(),
                                      null,
                                      null,
                                      null,
                                      true,
                                      true,
                                      false,
                                      false,
                                      false,
                                      "Parquet File",
                                      "Parquet Data File",
                                      null,
                                      "ParquetFile",
                                      file.length());
    }
}
