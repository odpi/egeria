/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceengine.discoveryservices;

import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnnotationStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryServiceException;
import org.odpi.openmetadata.frameworks.discovery.properties.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ValidatePatientRecordDiscoveryService is a discovery service implementation for analysing patient records.
 */
public class ValidatePatientRecordDiscoveryService extends DiscoveryService
{
    private final static String STRING_TYPE_NAME  = "string";
    private final static String CHAR_TYPE_NAME    = "char";
    private final static String DATE_TYPE_NAME    = "date";
    private final static String INT_TYPE_NAME     = "int";
    private final static String LONG_TYPE_NAME    = "long";
    private final static String BOOLEAN_TYPE_NAME = "boolean";
    private final static String FLOAT_TYPE_NAME   = "float";
    private final static String BOOLEAN_UC_TRUE   = "TRUE";
    private final static String BOOLEAN_LC_TRUE   = "true";
    private final static String BOOLEAN_UC_FALSE  = "FALSE";
    private final static String BOOLEAN_LC_FALSE  = "false";



    /**
     * Return the best guess for the data field type.
     *
     * @param existingType current type guess
     * @param newFieldValue next field value to process
     * @return new best guess
     */
    private String getDataFieldType(String  existingType, String newFieldValue)
    {
        if (STRING_TYPE_NAME.equals(existingType))
        {
            return STRING_TYPE_NAME;
        }

        if (newFieldValue == null)
        {
            return existingType;
        }

        if ((BOOLEAN_LC_TRUE.equals(newFieldValue)) ||
            (BOOLEAN_UC_TRUE.equals(newFieldValue)) ||
            (BOOLEAN_LC_FALSE.equals(newFieldValue)) ||
            (BOOLEAN_UC_FALSE.equals(newFieldValue)))

        {
            if ((existingType == null) || (existingType.equals(BOOLEAN_TYPE_NAME)))
            {
                return BOOLEAN_TYPE_NAME;
            }
            else
            {
                return STRING_TYPE_NAME;
            }
        }
        return STRING_TYPE_NAME;
    }


    /**
     * Return the updated value count for this column.
     *
     * @param existingValueCount current value count
     * @param newFieldValue next field value to process
     * @return new value count
     */
    private Map<String, Integer> getValueCount(Map<String, Integer> existingValueCount, String newFieldValue)
    {
        if (existingValueCount == null)
        {
            Map<String, Integer> newValueCount = new HashMap<>();

            newValueCount.put(newFieldValue, 1);

            return newValueCount;
        }

        Integer   countForValue = existingValueCount.get(newFieldValue);

        if (countForValue == null)
        {
            existingValueCount.put(newFieldValue, 1);
        }
        else
        {
            existingValueCount.put(newFieldValue, countForValue + 1);
        }

        return existingValueCount;
    }


    /**
     * Return the updated value list.
     *
     * @param existingValueList existing value list
     * @param newFieldValue next field value to process
     * @return updated value list
     */
    private List<String> getValueList(List<String> existingValueList, String newFieldValue)
    {
        if (existingValueList == null)
        {
            List<String> newValueList = new ArrayList<>();

            newValueList.add(newFieldValue);

            return newValueList;
        }

        if (! existingValueList.contains(newFieldValue))
        {
            existingValueList.add(newFieldValue);
        }

        return existingValueList;
    }


    /**
     * Indicates that the discovery service is completely configured and can begin processing.
     *
     * @throws DiscoveryServiceException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            CSVFileStoreConnector    assetConnector  = (CSVFileStoreConnector)discoveryContext.getAssetStore().getConnectorToAsset();
            DiscoveryAnnotationStore annotationStore = discoveryContext.getAnnotationStore();
            int                      size            = 0;
            int                      delimiterCount  = 0;
            long                     recordCount     = assetConnector.getRecordCount();

            SchemaAnalysisAnnotation  schemaAnnotation      = new SchemaAnalysisAnnotation();

            schemaAnnotation.setSchemaName("CSV");
            schemaAnnotation.setSchemaTypeName("TabularSchemaTypeProperties");
            schemaAnnotation.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);

            String schemaAnnotationGUID = annotationStore.addAnnotationToDiscoveryReport(schemaAnnotation);

            List<String>                        columnNames = assetConnector.getColumnNames();
            Map<Integer, DataField>             dataFields  = new HashMap<>();
            Map<Integer, DataProfileAnnotation> dataProfiles  = new HashMap<>();

            if (columnNames != null)
            {
                int position = 0;

                for (String  columnName : columnNames)
                {
                    if (columnName != null)
                    {
                        DataField  dataField = new DataField();

                        dataField.setDataFieldPosition(position);
                        dataField.setDataFieldName(columnName);
                        dataFields.put(position, dataField);

                        DataProfileAnnotation dataProfile = new DataProfileAnnotation();

                        dataProfiles.put(position, dataProfile);

                        size = size + columnName.length();
                        position++;
                    }
                }

                if (position > 0)
                {
                    delimiterCount = position - 1;
                }

                size = size + delimiterCount;

                for (int recordNumber=0; recordNumber < recordCount ; recordNumber++)
                {
                    List<String>  recordValues = assetConnector.readRecord(recordNumber);

                    if ((recordValues != null) && (! recordValues.isEmpty()))
                    {
                        int columnPosition = 0;
                        int recordLength = 0;

                        for (String fieldValue : recordValues)
                        {
                            DataField             dataField   = dataFields.get(columnPosition);
                            DataProfileAnnotation dataProfile = dataProfiles.get(columnPosition);

                            dataField.setDataFieldType(this.getDataFieldType(dataField.getDataFieldType(), fieldValue));

                            dataProfile.setValueCount(this.getValueCount(dataProfile.getValueCount(), fieldValue));
                            dataProfile.setValueList(this.getValueList(dataProfile.getValueList(), fieldValue));

                            recordLength = recordLength + fieldValue.length();

                            columnPosition++;
                        }

                        size = size + recordLength + delimiterCount;
                    }
                }

                for (int columnNumber=0 ; columnNumber < columnNames.size(); columnNumber++)
                {
                    String  dataFieldGUID = annotationStore.addDataFieldToDiscoveryReport(schemaAnnotationGUID, dataFields.get(columnNumber));

                    annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfiles.get(columnNumber));
                }
            }

            DataSourcePhysicalStatusAnnotation measurementAnnotation = new DataSourcePhysicalStatusAnnotation();
            Map<String, String>                measurementProperties = new HashMap<>();

            measurementProperties.put("FileName", assetConnector.getFileName());
            measurementProperties.put("RecordCount", Long.toString(recordCount));

            measurementAnnotation.setModifiedTime(assetConnector.getLastUpdateDate());
            measurementAnnotation.setDataSourceProperties(measurementProperties);
            measurementAnnotation.setSize(size);

            annotationStore.addAnnotationToDiscoveryReport(measurementAnnotation);
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            super.handleUnexpectedException(methodName, error);
        }
    }
}
