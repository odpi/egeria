/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.List;
import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFolderAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.FileDirectoryMeasurement;


/**
 * FolderMeasurementsTabularDataSetConnector lists the measurements of surveyed file directories (folders) as if they are a tabular data set.
 */
public class FolderMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = FolderMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public FolderMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.FOLDER_MEASUREMENTS);
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
                                                                 List.of(SurveyFolderAnnotationType.MEASUREMENTS.getName())))
        {
            FileDirectoryMeasurement measurement = getMeasurement(annotation, FileDirectoryMeasurement.class);
            Map<String, String> values = getMeasurementValues(annotation);

            if ((measurement != null) && (values != null))
            {
                String pathName = measurement.getDirectoryName();

                if (pathName == null)
                {
                    pathName = getDisplayName(annotation);
                }

                if (pathName == null)
                {
                    continue;
                }

                putValue(values, ProductDataFieldDefinition.PATHNAME, pathName);
                putValue(values, ProductDataFieldDefinition.FILE_COUNT, measurement.getFileCount());
                putValue(values, ProductDataFieldDefinition.TOTAL_FILE_SIZE, measurement.getTotalFileSize());
                putValue(values, ProductDataFieldDefinition.SUB_DIRECTORY_COUNT, measurement.getSubDirectoryCount());
                putValue(values, ProductDataFieldDefinition.READABLE_FILE_COUNT, measurement.getReadableFileCount());
                putValue(values, ProductDataFieldDefinition.WRITEABLE_FILE_COUNT, measurement.getWriteableFileCount());
                putValue(values, ProductDataFieldDefinition.EXECUTABLE_FILE_COUNT, measurement.getExecutableFileCount());
                putValue(values, ProductDataFieldDefinition.SYM_LINK_FILE_COUNT, measurement.getSymLinkFileCount());
                putValue(values, ProductDataFieldDefinition.HIDDEN_FILE_COUNT, measurement.getHiddenFileCount());
                putValue(values, ProductDataFieldDefinition.FILE_NAME_COUNT, measurement.getFileNameCount());
                putValue(values, ProductDataFieldDefinition.FILE_EXTENSION_COUNT, measurement.getFileExtensionCount());
                putValue(values, ProductDataFieldDefinition.FILE_TYPE_COUNT, measurement.getFileTypeCount());
                putValue(values, ProductDataFieldDefinition.ASSET_TYPE_COUNT, measurement.getAssetTypeCount());
                putValue(values, ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE_COUNT, measurement.getDeployedImplementationTypeCount());
                putValue(values, ProductDataFieldDefinition.UNCLASSIFIED_FILE_COUNT, measurement.getUnclassifiedFileCount());
                putValue(values, ProductDataFieldDefinition.INACCESSIBLE_FILE_COUNT, measurement.getInaccessibleFileCount());
                putValue(values, ProductDataFieldDefinition.LAST_FILE_CREATION_TIME, measurement.getLastFileCreationTime());
                putValue(values, ProductDataFieldDefinition.LAST_FILE_MODIFICATION_TIME, measurement.getLastFileModificationTime());

                addRow(annotation, values);
            }
        }
    }
}
