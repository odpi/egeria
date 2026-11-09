/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.List;
import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFileAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.FileMeasurement;


/**
 * FileMeasurementsTabularDataSetConnector lists the measurements of surveyed files as if they are a tabular data set.
 */
public class FileMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = FileMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public FileMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.FILE_MEASUREMENTS);
    }


    /**
     * One row per measurement annotation, linked to the most recent survey report it appears in.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                                                                 List.of(SurveyFileAnnotationType.MEASUREMENTS.getName())))
        {
            FileMeasurement measurement = getMeasurement(annotation, FileMeasurement.class);
            Map<String, String> values = getMeasurementValues(annotation);

            if ((measurement != null) && (values != null))
            {
                String pathName = measurement.getPathName();

                if (pathName == null)
                {
                    pathName = measurement.getFileName();
                }

                if (pathName == null)
                {
                    continue;
                }

                putValue(values, ProductDataFieldDefinition.PATHNAME, pathName);
                putValue(values, ProductDataFieldDefinition.FILE_NAME, measurement.getFileName());
                putValue(values, ProductDataFieldDefinition.FILE_EXTENSION, measurement.getFileExtension());
                putValue(values, ProductDataFieldDefinition.FILE_TYPE, measurement.getFileType());
                putValue(values, ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE, measurement.getDeployedImplementationType());
                putValue(values, ProductDataFieldDefinition.ENCODING, measurement.getEncoding());
                putValue(values, ProductDataFieldDefinition.CAN_READ, measurement.getCanRead());
                putValue(values, ProductDataFieldDefinition.CAN_WRITE, measurement.getCanWrite());
                putValue(values, ProductDataFieldDefinition.CAN_EXECUTE, measurement.getCanExecute());
                putValue(values, ProductDataFieldDefinition.IS_SYM_LINK, measurement.getSymLink());
                putValue(values, ProductDataFieldDefinition.IS_HIDDEN, measurement.getHidden());
                putValue(values, ProductDataFieldDefinition.FILE_CREATION_TIME, measurement.getCreationTime());
                putValue(values, ProductDataFieldDefinition.LAST_FILE_MODIFICATION_TIME, measurement.getLastModifiedTime());
                putValue(values, ProductDataFieldDefinition.FILE_SIZE, measurement.getFileSize());
                putValue(values, ProductDataFieldDefinition.RECORD_COUNT, measurement.getRecordCount());

                addRow(annotation, values);
            }
        }
    }
}
