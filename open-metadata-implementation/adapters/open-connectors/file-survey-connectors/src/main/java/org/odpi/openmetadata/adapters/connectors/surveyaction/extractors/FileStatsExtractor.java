/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.extractors;

import org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.SurveyFileAnnotationType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.FileMeasurement;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.FileMetric;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourcePhysicalStatusAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileStatsExtractor uses the FileClassifier to extract the stats about a file and then convert them into a list of
 * annotation.
 */
public class FileStatsExtractor
{
    final private File file;
    final private SurveyActionServiceConnector surveyActionServiceConnector;
    final private FileClassifier fileClassifier;


    /**
     * Constructor captures the file that is to be analysed.
     *
     * @param file file to analyse
     */
    public FileStatsExtractor(File                         file,
                              FileClassifier               fileClassifier,
                              SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.file = file;
        this.fileClassifier = fileClassifier;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    public Annotation getAnnotation() throws InvalidParameterException,
                                             PropertyServerException,
                                             UserNotAuthorizedException,
                                             IOException
    {
        FileClassification fileClassification = fileClassifier.classifyFile(file);

        ResourcePhysicalStatusAnnotation measurementAnnotation = new ResourcePhysicalStatusAnnotation();

        measurementAnnotation.setAnnotationType(SurveyFileAnnotationType.MEASUREMENTS.getName());
        measurementAnnotation.setSummary(SurveyFileAnnotationType.MEASUREMENTS.getSummary());
        measurementAnnotation.setExplanation(SurveyFileAnnotationType.MEASUREMENTS.getExplanation());
        measurementAnnotation.setCreateTime(fileClassification.getCreationTime());
        measurementAnnotation.setModifiedTime(fileClassification.getLastModifiedTime());
        measurementAnnotation.setLastAccessedTime(fileClassification.getLastAccessedTime());
        measurementAnnotation.setSize(fileClassification.getFileSize());

        Map<String, String> dataSourceProperties = new HashMap<>();

        dataSourceProperties.put(FileMetric.FILE_NAME.displayName, fileClassification.getFileName());
        dataSourceProperties.put(FileMetric.PATH_NAME.displayName, fileClassification.getPathName());
        dataSourceProperties.put(FileMetric.FILE_EXTENSION.displayName, fileClassification.getFileExtension());
        dataSourceProperties.put(FileMetric.FILE_TYPE.displayName, fileClassification.getFileType());
        dataSourceProperties.put(FileMetric.DEPLOYED_IMPLEMENTATION_TYPE.displayName, fileClassification.getDeployedImplementationType());
        dataSourceProperties.put(FileMetric.ENCODING.displayName, fileClassification.getEncoding());
        dataSourceProperties.put(FileMetric.ASSET_TYPE_NAME.displayName, fileClassification.getAssetTypeName());
        dataSourceProperties.put(FileMetric.CAN_READ.displayName, Boolean.toString(fileClassification.isCanRead()));
        dataSourceProperties.put(FileMetric.CAN_WRITE.displayName, Boolean.toString(fileClassification.isCanWrite()));
        dataSourceProperties.put(FileMetric.CAN_EXECUTE.displayName, Boolean.toString(fileClassification.isCanExecute()));
        dataSourceProperties.put(FileMetric.IS_SYM_LINK.displayName, Boolean.toString(fileClassification.isSymLink()));
        dataSourceProperties.put(FileMetric.IS_HIDDEN.displayName, Boolean.toString(fileClassification.isHidden()));
        dataSourceProperties.put(FileMetric.FILE_SIZE.displayName, Long.toString(fileClassification.getFileSize()));
        if (fileClassification.getCreationTime() != null)
        {
            dataSourceProperties.put(FileMetric.CREATION_TIME.displayName, fileClassification.getCreationTime().toString());
        }
        if (fileClassification.getLastModifiedTime() != null)
        {
            dataSourceProperties.put(FileMetric.LAST_MODIFIED_TIME.displayName, fileClassification.getLastModifiedTime().toString());
        }
        if (fileClassification.getLastAccessedTime() != null)
        {
            dataSourceProperties.put(FileMetric.LAST_ACCESSED_TIME.displayName, fileClassification.getLastAccessedTime().toString());
        }

        measurementAnnotation.setResourceProperties(dataSourceProperties);

        FileMeasurement fileMeasurement = new FileMeasurement();

        fileMeasurement.setFileName(fileClassification.getFileName());
        fileMeasurement.setPathName(fileClassification.getPathName());
        fileMeasurement.setFileExtension(fileClassification.getFileExtension());
        fileMeasurement.setFileType(fileClassification.getFileType());
        fileMeasurement.setDeployedImplementationType(fileClassification.getDeployedImplementationType());
        fileMeasurement.setEncoding(fileClassification.getEncoding());
        fileMeasurement.setFileName(fileClassification.getFileName());
        fileMeasurement.setAssetTypeName(fileClassification.getAssetTypeName());
        fileMeasurement.setCanRead(fileClassification.isCanRead());
        fileMeasurement.setCanWrite(fileClassification.isCanWrite());
        fileMeasurement.setCanExecute(fileClassification.isCanExecute());
        fileMeasurement.setSymLink(fileClassification.isSymLink());
        fileMeasurement.setHidden(fileClassification.isHidden());
        fileMeasurement.setCreationTime(fileClassification.getCreationTime());
        fileMeasurement.setLastModifiedTime(fileClassification.getLastModifiedTime());
        fileMeasurement.setLastAccessedTime(fileClassification.getLastAccessedTime());
        fileMeasurement.setFileSize(fileClassification.getFileSize());

        measurementAnnotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(fileMeasurement));

        return measurementAnnotation;
    }
}
