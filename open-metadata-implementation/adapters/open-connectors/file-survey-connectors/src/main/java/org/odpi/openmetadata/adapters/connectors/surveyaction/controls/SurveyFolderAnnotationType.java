/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.FileDirectoryMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The SurveyFolderAnnotationType enum describes the annotation types used by the folder survey action service.
 */
public enum SurveyFolderAnnotationType implements AnnotationType
{
    MEASUREMENTS("Capture File Counts",
                 OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                 AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                 "Count up the number of files and directories under the surveyed directory that have specific characteristics.",
                 "Count the number of directories and files under the starting directory.",
                 FileDirectoryMetric.getMetrics()),

    PROFILE_FILE_EXTENSIONS("Profile File Extensions",
                 OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                            AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                            "Iterate through files under a directory (folder) and count the occurrences of each file extension.",
                 "The file extension often provides a hint as to the type of file.",
                 null),

    PROFILE_FILE_NAMES("Profile File Names to External Log",
                            OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName,
                            AnalysisStep.PRODUCE_INVENTORY,
                            "Iterate through files under a directory (folder) and count the occurrences of each file name.",
                            "Some file names indicate a file of special type/use.  This profile information is likely to be large so it is logged to a CSV File.",
                            null),

    PROFILE_FILE_TYPES("Profile File Types",
                       OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                       AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                       "Iterate through files under a directory (folder) and count the occurrences of each file type.",
                       "The file type is a category of file that describes its use.  The file types are defined as reference data in Egeria.",
                       null),

    PROFILE_ASSET_TYPES("Profile Asset Types",
                       OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                        AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                        "Iterate through files under a directory (folder) and count each potential asset type if they were to be catalogued in open metadata.",
                       "The asset type is an open metadata type.  This annotation identifies the numbers of each type of asset that will be created if the files were catalogued in the open metadata ecosystem.",
                       null),

    PROFILE_DEP_IMPL_TYPES("Profile Deployed Implementation Types",
                        OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                           AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                           "Iterate through files under a directory (folder) and count each potential deployed implementation type if they were to be catalogued in open metadata.",
                        "The deployed implementation type reflects the technology employed within this resource.  The deployed implementation type is defined as reference data in Egeria.",
                        null),

    MISSING_REF_DATA("Missing File Reference Data",
                       OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName,
                       AnalysisStep.PRODUCE_ACTIONS,
                       "List of files that could not be classified using the file reference data.",
                       "The survey service uses reference data to classify the purpose of files based on their file name.  This annotation captures all of the file names that could not be matched to the reference data. If any of these reveal files that are important to your organization then the reference data should be enhanced to include these files.",
                       null),

    INACCESSIBLE_FILES("Inaccessible files",
                     OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName,
                     AnalysisStep.PRODUCE_ACTIONS,
                     "List of files that the survey service was unable to retrieve the basic attributes for.",
                     "The survey service retrieves the basic attributes of a file as part of its profiling effort.  A file I/O error uses reference data to classify the purpose of files based on their file name.  This annotation captures all of the file names that could not be matched to the reference data. If any of these reveal files that are important to your organization then the reference data should be enhanced to include these files.",
                     null),

    ;


    public final String                    name;
    public final String                    openMetadataTypeName;
    public final AnalysisStep              analysisStep;
    public final String                    summary;
    public final String                    explanation;
    public final List<FileDirectoryMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param analysisStep analysis step where this annotation is produced
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param metrics optional metrics
     */
    SurveyFolderAnnotationType(String              name,
                               String              openMetadataTypeName,
                               AnalysisStep        analysisStep,
                               String              summary,
                               String              explanation,
                               List<FileDirectoryMetric>  metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.analysisStep         = analysisStep;
        this.summary              = summary;
        this.explanation          = explanation;
        this.metrics              = metrics;
    }

    /**
     * Return the defined annotation types as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (SurveyFolderAnnotationType atlasAnnotationType : SurveyFolderAnnotationType.values())
        {
            annotationTypeTypes.add(atlasAnnotationType.getAnnotationTypeType());
        }

        return annotationTypeTypes;
    }


    /**
     * Return the name of the annotation type.
     *
     * @return string name
     */
    @Override
    public String getName()
    {
        return name;
    }


    /**
     * Return the analysis step that produces this type of annotation.
     *
     * @return analysis step name
     */
    @Override
    public String getAnalysisStep()
    {
        return AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName();
    }


    /**
     * Return the name of the open metadata type used for this type of annotation.
     *
     * @return type name
     */
    @Override
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Return the short description of the annotation type.
     *
     * @return text
     */
    @Override
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description of the annotation type.
     *
     * @return text
     */
    @Override
    public String getExplanation()
    {
        return explanation;
    }


    /**
     * Return the expression used in the annotation type processing.
     *
     * @return string
     */
    @Override
    public String getExpression()
    {
        return null;
    }


    /**
     * Return the description of this annotation type that can be used in a Connector Provider for a
     * Survey Action Service.
     *
     * @return annotationType type
     */
    public AnnotationTypeType getAnnotationTypeType()
    {
        AnnotationTypeType annotationTypeType = new AnnotationTypeType();

        annotationTypeType.setName(name);
        annotationTypeType.setOpenMetadataTypeName(openMetadataTypeName);
        annotationTypeType.setAnalysisStepName(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());
        annotationTypeType.setSummary(summary);
        annotationTypeType.setExplanation(explanation);

        if (metrics != null)
        {
            Map<String, String> metricsMap = new HashMap<>();

            for (FileDirectoryMetric fileDirectoryMetric : metrics)
            {
                metricsMap.put(fileDirectoryMetric.getDisplayName(), fileDirectoryMetric.getDescription());
            }

            annotationTypeType.setOtherPropertyValues(metricsMap);
        }

        return annotationTypeType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "AnnotationType{ name='" + name + "}";
    }
}
