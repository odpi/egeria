/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

/**
 * SurveyMetric describes the standard interface for an enum describing a list of metrics.
 */
public interface SurveyMetric
{
    /**
     * Return the property name used to store the measurement.
     *
     * @return name
     */
    String getPropertyName();


    /**
     * Return the data type of the property used to store the measure.
     *
     * @return data type name
     */
    String getDataType();


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    String getDisplayName();


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    String getDescription();
}
