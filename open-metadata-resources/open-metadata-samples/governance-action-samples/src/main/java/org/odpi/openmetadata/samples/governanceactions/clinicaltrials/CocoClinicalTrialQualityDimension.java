/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * CocoClinicalTrialQualityDimension describes the quality dimensions for the
 * CocoClinicalTrialCertifyWeeklyMeasurements Survey Action Service.
 */
public enum CocoClinicalTrialQualityDimension implements SurveyMetric
{
    NO_OF_COLUMNS_MATCH ("matchingColumnCount", DataType.STRING.getName(), "Matching Column Count", "Do the number of columns described in the schema match the number of columns in the resource?  Zero means a perfect match.  A negative value means the resource has less columns and a positive value means that the resource has more columns."),
    NAMES_OF_COLUMNS_MATCH ("matchingColumnNames", DataType.STRING.getName(),"Matching Column Names", "Do the column names in the schema match those in the resource.  The order is important. Zero means a perfect match.  A negative value means there are mismatched columns."),
    VALID_PATIENT_ID ("patientIdInRange", DataType.STRING.getName(), "Patient Identifier In Range", "Is the patient identifier valid?  Zero means it is in range.  A negative value indicates the number of rows where it is out of range."),
    VALID_DATE ("dateInRange", DataType.STRING.getName(), "Date In Range", "Is the date of the measurement in date for the clinical trial?  Zero means it is in range.  A negative value indicates the number of rows where it is out of range."),
    VALID_LEFT_ANGLE ("leftFootAngleInRange", DataType.STRING.getName(), "Left FootAngle In Range", "Is the measured angle between -90 degrees and +90 degrees?  Zero means it is in range.  A negative value indicates the number of rows where it is out of range."),
    VALID_RIGHT_ANGLE ("rightFootAngleInRange", DataType.STRING.getName(), "Right Foot Angle In Range", "Is the measured angle between -90 degrees and +90 degrees?  Zero means it is in range.  A negative value indicates the number of rows where it is out of range."),

    ;

    public final String propertyName;
    public final String dataType;
    public final String displayName;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param propertyName name of the property used to store the measurement
     * @param dataType data type of property
     * @param displayName name of the request type
     * @param description description of the request type
     */
    CocoClinicalTrialQualityDimension(String propertyName,
                                      String dataType,
                                      String displayName,
                                      String description)
    {
        this.propertyName = propertyName;
        this.dataType     = dataType;
        this.displayName  = displayName;
        this.description  = description;
    }


    /**
     * Return the property name used to store the measurement.
     *
     * @return name
     */
    @Override
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Return the data type of the property used to store the measure.
     *
     * @return data type name
     */
    @Override
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getQualityDimensions()
    {
        return new ArrayList<>(List.of(CocoClinicalTrialQualityDimension.values()));
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getSchemaValidationQualityDimensions()
    {
        List<SurveyMetric> surveyMetrics = new ArrayList<>();

        surveyMetrics.add(NO_OF_COLUMNS_MATCH);
        surveyMetrics.add(NAMES_OF_COLUMNS_MATCH);

        return surveyMetrics;
    }



    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getPatientIdValidationQualityDimensions()
    {
        List<SurveyMetric> surveyMetrics = new ArrayList<>();

        surveyMetrics.add(VALID_PATIENT_ID);

        return surveyMetrics;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getDateValidationQualityDimensions()
    {
        List<SurveyMetric> surveyMetrics = new ArrayList<>();

        surveyMetrics.add(VALID_DATE);

        return surveyMetrics;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getAngleValidationQualityDimensions()
    {
        List<SurveyMetric> surveyMetrics = new ArrayList<>();

        surveyMetrics.add(VALID_LEFT_ANGLE);
        surveyMetrics.add(VALID_RIGHT_ANGLE);

        return surveyMetrics;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CocoClinicalTrialQualityDimension{" + displayName + "}";
    }
}
