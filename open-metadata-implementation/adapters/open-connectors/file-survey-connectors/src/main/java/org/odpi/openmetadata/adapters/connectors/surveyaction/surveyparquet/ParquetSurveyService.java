/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyparquet;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.ParquetFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetColumnProfile;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetFileMetadata;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetSchemaElement;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.ParquetColumnMetric;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.ParquetFileMetric;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.SurveyParquetAnnotationType;
import org.odpi.openmetadata.adapters.connectors.surveyaction.extractors.FileStatsExtractor;
import org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc.SurveyServiceAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.SchemaAnalysisAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ParquetSurveyService is a survey action service implementation for analysing Apache Parquet files to
 * discover their columns and profile the data in them.
 * <br><br>
 * The schema, the measurements and the range of values in each column come from the footer of the file, which is
 * where the writer records the schema, how the values are stored and statistics about them.  This part of the survey
 * takes the same time whatever the size of the file, and it works with every version of the Parquet format, and every
 * encoding and compression algorithm.
 * <br><br>
 * The final analysis step (Profile Data) also reads the values in each top-level column to count how many times each
 * value occurs.  This relies on the DuckDB JDBC driver being available.  If it is not, the survey is completed
 * without these counts.
 */
public class ParquetSurveyService extends SurveyActionServiceConnector
{
    private final static String schemaType = OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName;
    private final static String schemaName = "Parquet";

    /*
     * The counts of each value are only recorded for columns with no more than this number of different values.
     * Beyond that, the column is not a set of codes or categories and the list of values is of little use.
     */
    private final static int    MAX_DISTINCT_VALUES = 100;


    /**
     * Default constructor.
     */
    public ParquetSurveyService()
    {
    }


    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException a problem within the survey action service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            AnnotationStore    annotationStore   = surveyContext.getAnnotationStore();
            OpenMetadataStore  openMetadataStore = surveyContext.getOpenMetadataStore();
            SurveyAssetStore   assetStore        = surveyContext.getAssetStore();

            /*
             * The asset should have a special connector for Parquet files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = super.performCheckAssetAnalysisStep(ParquetFileStoreConnector.class,
                                                            OpenMetadataType.PARQUET_FILE.typeName);

            if (super.isFinalAnalysisStep())
            {
                return;
            }

            OpenMetadataRootElement   assetElement   = assetStore.getAssetProperties();
            ParquetFileStoreConnector assetConnector = (ParquetFileStoreConnector)connector;

            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            /*
             * The footer is read before any annotation is created so that nothing is recorded about a file
             * that turns out not to be a Parquet file.  The connector's exception describes what is wrong
             * with the file.
             */
            File                file         = assetConnector.getFile();
            ParquetFileMetadata fileMetadata = assetConnector.getFileMetadata();

            FileStatsExtractor fileStatsExtractor = new FileStatsExtractor(file,
                                                                           surveyContext.getFileClassifier(null, null, null),
                                                                           this);

            AnnotationProperties measurementAnnotation = fileStatsExtractor.getAnnotation();

            if (measurementAnnotation != null)
            {
                annotationStore.addAnnotation(measurementAnnotation, surveyContext.getAssetGUID());
            }

            annotationStore.addAnnotation(this.getParquetMeasurementAnnotation(fileMetadata), surveyContext.getAssetGUID());

            /*
             * Stopping here records the measurements of the file without describing its columns.
             */
            if (super.isFinalAnalysisStep())
            {
                return;
            }

            /*
             * If a schema type is attached, it must be of the correct type so that information from the asset can
             * be matched to it.
             */
            annotationStore.setAnalysisStep(AnalysisStep.SCHEMA_EXTRACTION.getName());

            String                              schemaTypeGUID;
            List<RelatedMetadataElementSummary> schemaAttributes;
            OpenMetadataRootElement             nestedSchemaType = super.getRootSchemaType(assetElement, schemaType);

            if (nestedSchemaType == null)
            {
                /*
                 * Set up a new schema type.
                 */
                schemaTypeGUID   = createSchemaType(openMetadataStore, assetElement);
                schemaAttributes = null;
            }
            else
            {
                schemaTypeGUID   = nestedSchemaType.getElementHeader().getGUID();
                schemaAttributes = nestedSchemaType.getSchemaAttributes();
            }

            List<ParquetSchemaElement> columns     = fileMetadata.getColumns();
            List<String>               columnNames = fileMetadata.getColumnNames();

            /*
             * Store the key dimensions of the schema.
             */
            SchemaAnalysisAnnotationProperties schemaAnnotation = new SchemaAnalysisAnnotationProperties();

            super.setUpAnnotation(schemaAnnotation, SurveyParquetAnnotationType.SCHEMA_FROM_FOOTER);
            schemaAnnotation.setSchemaName(schemaName);
            schemaAnnotation.setSchemaType(schemaType);

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put("Column Count", Integer.toString(columnNames.size()));
            additionalProperties.put("Column Names", columnNames.toString());

            schemaAnnotation.setAdditionalProperties(additionalProperties);

            annotationStore.addAnnotation(schemaAnnotation, schemaTypeGUID);

            /*
             * Unlike a CSV file, the types of the columns are recorded in the file and so the schema attributes
             * are complete without needing to look at the values.
             */
            List<String> columnGUIDs = this.synchronizeSchemaAttributes(openMetadataStore,
                                                                        assetElement,
                                                                        schemaTypeGUID,
                                                                        schemaAttributes,
                                                                        columns);

            /*
             * Stopping here records the columns found in the file.
             */
            if (super.isFinalAnalysisStep())
            {
                return;
            }

            annotationStore.setAnalysisStep(AnalysisStep.PROFILE_DATA.getName());

            /*
             * The statistics are for the columns that hold values (the leaf columns).  Where the file
             * contains nested data, there may be many of these beneath a single top-level column.
             * Their profiles are all attached to the schema attribute for the top-level column.
             */
            Map<String, String> leafColumnGUIDs = new HashMap<>();

            for (int i = 0; i < columns.size(); i++)
            {
                this.addLeafColumns(columns.get(i), columnGUIDs.get(i), leafColumnGUIDs);
            }

            boolean countValues = assetConnector.isDataAccessAvailable();

            if (! countValues)
            {
                logRecord(methodName,
                          SurveyServiceAuditCode.PARQUET_VALUES_NOT_READ.getMessageDefinition(surveyActionServiceName,
                                                                                               file.getAbsolutePath()));
            }

            for (ParquetColumnProfile columnProfile : fileMetadata.getColumnProfiles())
            {
                String schemaAttributeGUID = leafColumnGUIDs.get(columnProfile.getColumn().getPathName());

                if (schemaAttributeGUID != null)
                {
                    ResourceProfileAnnotationProperties annotation = this.getColumnProfileAnnotation(columnProfile);

                    if (countValues)
                    {
                        this.addValueCounts(assetConnector, file.getAbsolutePath(), columnProfile, annotation);
                    }

                    annotationStore.addAnnotation(annotation, schemaAttributeGUID);
                }
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            super.handleUnexpectedException(methodName, error);
        }
    }


    /**
     * Build the annotation that describes the file as a whole.
     *
     * @param fileMetadata description of the file from its footer
     * @return annotation
     * @throws PropertyServerException unable to convert the measurements to JSON
     * @throws UserNotAuthorizedException the service was disconnected
     */
    private AnnotationProperties getParquetMeasurementAnnotation(ParquetFileMetadata fileMetadata) throws PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        ResourceMeasureAnnotationProperties annotation = new ResourceMeasureAnnotationProperties();

        super.setUpAnnotation(annotation, SurveyParquetAnnotationType.PARQUET_MEASUREMENTS);

        List<String>           writerPropertyNames = new ArrayList<>(fileMetadata.getKeyValueMetadata().keySet());
        ParquetFileMeasurement measurement         = new ParquetFileMeasurement();

        measurement.setFormatVersion(fileMetadata.getFormatVersion());
        measurement.setCreatedBy(fileMetadata.getCreatedBy());
        measurement.setRowCount(fileMetadata.getRowCount());
        measurement.setRowGroupCount(fileMetadata.getRowGroups().size());
        measurement.setColumnCount(fileMetadata.getColumns().size());
        measurement.setLeafColumnCount(fileMetadata.getColumnProfiles().size());
        measurement.setFooterSize(fileMetadata.getFooterLength());
        measurement.setCompressedSize(fileMetadata.getCompressedSize());
        measurement.setUncompressedSize(fileMetadata.getUncompressedSize());
        measurement.setCompressionCodecs(fileMetadata.getCompressionCodecs());
        measurement.setEncodings(fileMetadata.getEncodings());
        measurement.setDataPageVersions(fileMetadata.getDataPageVersions());
        measurement.setTypeAnnotationStyle(fileMetadata.getTypeAnnotationStyle());
        measurement.setEncryptionAlgorithm(fileMetadata.getEncryptionAlgorithm());
        measurement.setWriterPropertyNames(writerPropertyNames);

        Map<String, String> resourceProperties = new HashMap<>();

        resourceProperties.put(ParquetFileMetric.FORMAT_VERSION.displayName, Integer.toString(measurement.getFormatVersion()));
        resourceProperties.put(ParquetFileMetric.ROW_COUNT.displayName, Long.toString(measurement.getRowCount()));
        resourceProperties.put(ParquetFileMetric.ROW_GROUP_COUNT.displayName, Integer.toString(measurement.getRowGroupCount()));
        resourceProperties.put(ParquetFileMetric.COLUMN_COUNT.displayName, Integer.toString(measurement.getColumnCount()));
        resourceProperties.put(ParquetFileMetric.LEAF_COLUMN_COUNT.displayName, Integer.toString(measurement.getLeafColumnCount()));
        resourceProperties.put(ParquetFileMetric.FOOTER_SIZE.displayName, Integer.toString(measurement.getFooterSize()));
        resourceProperties.put(ParquetFileMetric.COMPRESSED_SIZE.displayName, Long.toString(measurement.getCompressedSize()));
        resourceProperties.put(ParquetFileMetric.UNCOMPRESSED_SIZE.displayName, Long.toString(measurement.getUncompressedSize()));
        resourceProperties.put(ParquetFileMetric.COMPRESSION_CODECS.displayName, measurement.getCompressionCodecs().toString());
        resourceProperties.put(ParquetFileMetric.ENCODINGS.displayName, measurement.getEncodings().toString());
        resourceProperties.put(ParquetFileMetric.DATA_PAGE_VERSIONS.displayName, measurement.getDataPageVersions().toString());
        resourceProperties.put(ParquetFileMetric.TYPE_ANNOTATION_STYLE.displayName, measurement.getTypeAnnotationStyle());
        resourceProperties.put(ParquetFileMetric.WRITER_PROPERTY_NAMES.displayName, writerPropertyNames.toString());

        if (measurement.getCreatedBy() != null)
        {
            resourceProperties.put(ParquetFileMetric.CREATED_BY.displayName, measurement.getCreatedBy());
        }

        if (measurement.getEncryptionAlgorithm() != null)
        {
            resourceProperties.put(ParquetFileMetric.ENCRYPTION_ALGORITHM.displayName, measurement.getEncryptionAlgorithm());
        }

        annotation.setResourceProperties(resourceProperties);
        annotation.setJsonProperties(super.getJSONProperties(measurement));

        return annotation;
    }


    /**
     * Build the annotation that describes the values in a column.
     *
     * @param columnProfile summary of the column from the file's footer
     * @return annotation
     * @throws UserNotAuthorizedException the service was disconnected
     */
    private ResourceProfileAnnotationProperties getColumnProfileAnnotation(ParquetColumnProfile columnProfile) throws UserNotAuthorizedException
    {
        ParquetSchemaElement                column     = columnProfile.getColumn();
        ResourceProfileAnnotationProperties annotation = new ResourceProfileAnnotationProperties();

        /*
         * One of these per column, so the column's path name is what tells them apart - see
         * setUpAnnotation(annotation, type, subjectName).
         */
        super.setUpAnnotation(annotation, SurveyParquetAnnotationType.COLUMN_PROFILE, column.getPathName());

        annotation.setDataType(column.getDataType());
        annotation.setValueRangeFrom(columnProfile.getMinValue());
        annotation.setValueRangeTo(columnProfile.getMaxValue());

        if (column.getTypeLength() != null)
        {
            annotation.setLength(column.getTypeLength());
        }

        Map<String, Long> profileCounts = new HashMap<>();

        profileCounts.put(ParquetColumnMetric.VALUE_COUNT.displayName, columnProfile.getValueCount());
        profileCounts.put(ParquetColumnMetric.COMPRESSED_SIZE.displayName, columnProfile.getCompressedSize());
        profileCounts.put(ParquetColumnMetric.UNCOMPRESSED_SIZE.displayName, columnProfile.getUncompressedSize());

        if (columnProfile.getNullCount() != null)
        {
            profileCounts.put(ParquetColumnMetric.NULL_COUNT.displayName, columnProfile.getNullCount());
        }

        if (columnProfile.getDistinctCount() != null)
        {
            profileCounts.put(ParquetColumnMetric.DISTINCT_COUNT.displayName, columnProfile.getDistinctCount());
        }

        annotation.setProfileCounts(profileCounts);

        Map<String, String> profileProperties = new HashMap<>();

        profileProperties.put(ParquetColumnMetric.PATH_NAME.displayName, column.getPathName());
        profileProperties.put(ParquetColumnMetric.PHYSICAL_TYPE.displayName, column.getPhysicalType());
        profileProperties.put(ParquetColumnMetric.REPETITION.displayName, column.getRepetition());
        profileProperties.put(ParquetColumnMetric.COMPRESSION_CODECS.displayName, columnProfile.getCompressionCodecs().toString());
        profileProperties.put(ParquetColumnMetric.ENCODINGS.displayName, columnProfile.getEncodings().toString());
        profileProperties.put(ParquetColumnMetric.DATA_PAGE_VERSIONS.displayName, columnProfile.getDataPageVersions().toString());

        if (column.getLogicalType() != null)
        {
            profileProperties.put(ParquetColumnMetric.LOGICAL_TYPE.displayName, column.getLogicalType());
        }

        if (column.getConvertedType() != null)
        {
            profileProperties.put(ParquetColumnMetric.CONVERTED_TYPE.displayName, column.getConvertedType());
        }

        annotation.setProfileProperties(profileProperties);

        Map<String, Boolean> profileFlags = new HashMap<>();

        profileFlags.put(ParquetColumnMetric.ENCRYPTED.displayName, columnProfile.isEncrypted());

        annotation.setProfileFlags(profileFlags);

        List<String> profilePropertyNames = new ArrayList<>(profileCounts.keySet());

        profilePropertyNames.addAll(profileProperties.keySet());
        profilePropertyNames.addAll(profileFlags.keySet());

        annotation.setProfilePropertyNames(profilePropertyNames);

        return annotation;
    }


    /**
     * Read the values in a column and add the number of times that each one occurs to the column's annotation.
     * This is done for top-level columns that hold a single value in each row, and it is only recorded if the
     * column has a limited number of different values.  A failure to read the values is logged, and leaves
     * the annotation as it is.
     *
     * @param assetConnector connector to the file
     * @param fileName location of the file
     * @param columnProfile summary of the column from the file's footer
     * @param annotation annotation to add to
     */
    private void addValueCounts(ParquetFileStoreConnector           assetConnector,
                                String                              fileName,
                                ParquetColumnProfile                columnProfile,
                                ResourceProfileAnnotationProperties annotation)
    {
        final String methodName = "addValueCounts";

        ParquetSchemaElement column = columnProfile.getColumn();

        /*
         * A column with the same name and path name is a top-level column.  The values nested in lists, maps
         * and structures are not counted.  Neither are binary values, nor columns whose statistics already show
         * that they have too many different values.
         */
        if ((! column.getName().equals(column.getPathName())) ||
            ("REPEATED".equals(column.getRepetition())) ||
            ("binary".equals(column.getDataType())) ||
            (columnProfile.isEncrypted()) ||
            ((columnProfile.getDistinctCount() != null) && (columnProfile.getDistinctCount() > MAX_DISTINCT_VALUES)))
        {
            return;
        }

        try
        {
            Map<String, Long> valueCounts = assetConnector.getValueCounts(column.getName(), MAX_DISTINCT_VALUES);

            if (valueCounts != null)
            {
                Map<String, Integer> valueCount = new HashMap<>();

                for (Map.Entry<String, Long> entry : valueCounts.entrySet())
                {
                    valueCount.put(entry.getKey(), (int) Math.min(entry.getValue(), Integer.MAX_VALUE));
                }

                annotation.setValueCount(valueCount);
                annotation.setValueList(new ArrayList<>(valueCounts.keySet()));

                /*
                 * This is an exact count, which replaces any count from the footer.
                 */
                Map<String, Long> profileCounts = new HashMap<>();

                if (annotation.getProfileCounts() != null)
                {
                    profileCounts.putAll(annotation.getProfileCounts());
                }

                profileCounts.put(ParquetColumnMetric.DISTINCT_COUNT.displayName, (long) valueCounts.size());
                annotation.setProfileCounts(profileCounts);

                List<String> profilePropertyNames = new ArrayList<>();

                if (annotation.getProfilePropertyNames() != null)
                {
                    profilePropertyNames.addAll(annotation.getProfilePropertyNames());
                }

                if (! profilePropertyNames.contains(ParquetColumnMetric.DISTINCT_COUNT.displayName))
                {
                    profilePropertyNames.add(ParquetColumnMetric.DISTINCT_COUNT.displayName);
                }

                annotation.setProfilePropertyNames(profilePropertyNames);
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               SurveyServiceAuditCode.PARQUET_VALUES_READ_FAILED.getMessageDefinition(surveyActionServiceName,
                                                                                                       column.getName(),
                                                                                                       fileName,
                                                                                                       error.getClass().getName(),
                                                                                                       error.getMessage()),
                               error);
        }
    }


    /**
     * Record which schema attribute each of the columns that hold values belongs to.
     *
     * @param schemaElement a field from the file's schema
     * @param schemaAttributeGUID the unique identifier of the schema attribute for the top-level column
     * @param leafColumnGUIDs map of path name to schema attribute unique identifier
     */
    private void addLeafColumns(ParquetSchemaElement schemaElement,
                                String               schemaAttributeGUID,
                                Map<String, String>  leafColumnGUIDs)
    {
        if (schemaElement.isLeaf())
        {
            leafColumnGUIDs.put(schemaElement.getPathName(), schemaAttributeGUID);
        }

        for (ParquetSchemaElement child : schemaElement.getChildren())
        {
            this.addLeafColumns(child, schemaAttributeGUID, leafColumnGUIDs);
        }
    }


    /**
     * Make the schema attributes attached to the schema type match the columns in the file.
     * The schema attributes that already match a column, by name, are updated and so previous
     * annotations and lineage relationships are not lost.  New schema attributes are added for the columns
     * that have no match, and the schema attributes that do not match a column are archived.
     *
     * @param openMetadataStore client to access the open metadata repositories
     * @param assetElement details of the asset
     * @param schemaTypeGUID unique identifier of the schema type
     * @param schemaAttributes the schema attributes currently attached to the schema type (may be null)
     * @param columns the top-level columns from the file
     * @return the unique identifiers of the schema attributes for each of the columns (in the same order)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository not working
     * @throws UserNotAuthorizedException insufficient security
     */
    private List<String> synchronizeSchemaAttributes(OpenMetadataStore                   openMetadataStore,
                                                     OpenMetadataRootElement             assetElement,
                                                     String                              schemaTypeGUID,
                                                     List<RelatedMetadataElementSummary> schemaAttributes,
                                                     List<ParquetSchemaElement>          columns) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        String[] matchingSchemaAttributeGUIDs = new String[columns.size()];
        String[] matchingRelationshipGUIDs    = new String[columns.size()];

        if (schemaAttributes != null)
        {
            for (RelatedMetadataElementSummary schemaAttribute : schemaAttributes)
            {
                boolean found = false;

                if (schemaAttribute.getRelatedElement().getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                {
                    for (int i = 0; i < columns.size(); i++)
                    {
                        if ((matchingSchemaAttributeGUIDs[i] == null) &&
                            (columns.get(i).getName() != null) &&
                            (columns.get(i).getName().equals(schemaAttributeProperties.getDisplayName())))
                        {
                            matchingSchemaAttributeGUIDs[i] = schemaAttribute.getRelatedElement().getElementHeader().getGUID();
                            matchingRelationshipGUIDs[i]    = schemaAttribute.getRelationshipHeader().getGUID();
                            found = true;
                            break;
                        }
                    }
                }

                if (! found)
                {
                    DeleteOptions deleteOptions = new DeleteOptions();

                    deleteOptions.setDeleteMethod(DeleteMethod.ARCHIVE);
                    deleteOptions.setArchiveDate(new Date());
                    deleteOptions.setArchiveProcess(surveyActionServiceName);

                    openMetadataStore.deleteMetadataElementInStore(schemaAttribute.getRelatedElement().getElementHeader().getGUID(),
                                                                   deleteOptions);
                }
            }
        }

        List<String> columnGUIDs = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++)
        {
            if (matchingSchemaAttributeGUIDs[i] == null)
            {
                columnGUIDs.add(this.addSchemaAttributeToSchemaType(openMetadataStore, assetElement, schemaTypeGUID, columns.get(i), i));
            }
            else
            {
                this.updateSchemaAttribute(openMetadataStore, matchingSchemaAttributeGUIDs[i], matchingRelationshipGUIDs[i], columns.get(i), i);

                columnGUIDs.add(matchingSchemaAttributeGUIDs[i]);
            }
        }

        return columnGUIDs;
    }


    /**
     * Return the unique identifier of a new root schema type for the Parquet file.
     *
     * @param openMetadataStore the client to the open metadata store
     * @param assetElement details of the asset.
     * @return string guid
     *
     * @throws InvalidParameterException invalid property
     * @throws PropertyServerException no working metadata repository
     * @throws UserNotAuthorizedException insufficient security
     */
    private String createSchemaType(OpenMetadataStore       openMetadataStore,
                                    OpenMetadataRootElement assetElement) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        if (assetElement.getProperties() instanceof AssetProperties assetProperties)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   assetProperties.getQualifiedName() + "_rootSchemaType");

            String schemaTypeGUID = openMetadataStore.createMetadataElementInStore(schemaType,
                                                                                   new NewElementProperties(elementProperties));
            if (schemaTypeGUID != null)
            {
                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                               assetElement.getElementHeader().getGUID(),
                                                               schemaTypeGUID,
                                                               null,
                                                               null,
                                                               null);
            }

            return schemaTypeGUID;
        }

        return null;
    }


    /**
     * Return the properties that describe a column.
     *
     * @param column the column from the file
     * @return element properties
     */
    private ElementProperties getSchemaAttributeProperties(ParquetSchemaElement column)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               column.getName());

        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                              OpenMetadataProperty.IS_NULLABLE.name,
                                                              column.isNullable());

        if (column.getTypeLength() != null)
        {
            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.LENGTH.name,
                                                              column.getTypeLength());
        }

        if (column.getScale() != null)
        {
            /*
             * The open metadata precision property is the number of digits after the decimal point,
             * which is what Parquet calls the scale.
             */
            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.PRECISION.name,
                                                              column.getScale());
        }

        return elementProperties;
    }


    /**
     * Return the properties of the classification that describes the type of a column.
     *
     * @param column the column from the file
     * @return element properties
     */
    private ElementProperties getTypeClassificationProperties(ParquetSchemaElement column)
    {
        String dataType       = column.getDataType();
        String schemaTypeName = OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName;

        if (dataType.startsWith("map<"))
        {
            schemaTypeName = OpenMetadataType.MAP_SCHEMA_TYPE.typeName;
        }
        else if (dataType.startsWith("struct<"))
        {
            schemaTypeName = OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName;
        }
        else if (dataType.startsWith("array<"))
        {
            schemaTypeName = OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName;
        }

        ElementProperties classificationProperties = propertyHelper.addStringProperty(null,
                                                                                      OpenMetadataProperty.SCHEMA_TYPE_NAME.name,
                                                                                      schemaTypeName);

        return propertyHelper.addStringProperty(classificationProperties,
                                                OpenMetadataProperty.DATA_TYPE.name,
                                                dataType);
    }


    /**
     * Create a new schema attribute.
     *
     * @param openMetadataStore client to access the open metadata repositories
     * @param assetElement details of the asset
     * @param schemaTypeGUID unique identifier of the schema type to link to
     * @param column the column from the file
     * @param position the position of the column in the file
     * @return unique identifier of the schema attribute
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository not working
     * @throws UserNotAuthorizedException insufficient security
     */
    private String addSchemaAttributeToSchemaType(OpenMetadataStore       openMetadataStore,
                                                  OpenMetadataRootElement assetElement,
                                                  String                  schemaTypeGUID,
                                                  ParquetSchemaElement    column,
                                                  int                     position) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        if (assetElement.getProperties() instanceof AssetProperties assetProperties)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(this.getSchemaAttributeProperties(column),
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   assetProperties.getQualifiedName() + "_column_" + position + "_" + column.getName());

            /*
             * The column's position goes on the AttributeForSchema relationship, not on the column itself.
             */
            ElementProperties relationshipProperties = propertyHelper.addIntProperty(null,
                                                                                     OpenMetadataProperty.POSITION.name,
                                                                                     position);

            Map<String, NewElementProperties> initialClassifications = new HashMap<>();

            initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                       new NewElementProperties(this.getTypeClassificationProperties(column)));

            NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(assetElement.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorScopeGUIDs(null);
            newElementOptions.setParentGUID(schemaTypeGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

            return openMetadataStore.createMetadataElementInStore(OpenMetadataType.TABULAR_COLUMN.typeName,
                                                                  newElementOptions,
                                                                  initialClassifications,
                                                                  new NewElementProperties(elementProperties),
                                                                  new NewElementProperties(relationshipProperties));
        }

        return null;
    }


    /**
     * Update the values in an existing schema attribute.
     *
     * @param openMetadataStore client to access the open metadata repositories
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param attributeForSchemaGUID unique identifier of the AttributeForSchema relationship that links the schema
     *                               attribute to its schema type - it holds the column's position
     * @param column the column from the file
     * @param position the position of the column in the file
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository not working
     * @throws UserNotAuthorizedException insufficient security
     */
    private void updateSchemaAttribute(OpenMetadataStore    openMetadataStore,
                                       String               schemaAttributeGUID,
                                       String               attributeForSchemaGUID,
                                       ParquetSchemaElement column,
                                       int                  position) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        openMetadataStore.updateMetadataElementInStore(schemaAttributeGUID, false, this.getSchemaAttributeProperties(column));

        if (attributeForSchemaGUID != null)
        {
            ElementProperties relationshipProperties = propertyHelper.addIntProperty(null,
                                                                                     OpenMetadataProperty.POSITION.name,
                                                                                     position);

            openMetadataStore.updateRelatedElementsInStore(attributeForSchemaGUID,
                                                           openMetadataStore.getUpdateOptions(true),
                                                           relationshipProperties);
        }

        openMetadataStore.reclassifyMetadataElementInStore(schemaAttributeGUID,
                                                           OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                                           false,
                                                           this.getTypeClassificationProperties(column));
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        if (connector != null)
        {
            connector.disconnect();
        }

        super.disconnect();
    }
}
