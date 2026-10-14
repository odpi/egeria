/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.controls;


import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum SurveyRequestParameter
{
    /**
     * The name of the last analysis step that the survey action service is to run.  The survey stops once this
     * step is complete.  If the survey never reaches the named step, it runs to completion.  The values that a
     * survey action service supports are listed, in the order it runs them, in the validValues property of its
     * finalAnalysisStep supported request parameter.
     */
    FINAL_ANALYSIS_STEP ("finalAnalysisStep", "The name of the last analysis step that the survey action service is to run.  The survey stops once this step is complete.  If the survey never reaches the named step, it runs to completion.  It overrides the configuration property of the same name.", "string", "Measure Resource"),


    /**
     * Provides a list of schema names that should be surveyed.  Other schemas are ignored.  This takes precedence over the exclude list.
     */
    INCLUDE_SCHEMA_NAMES ("includeSchemaNames", "Provides a list of schema names that should be surveyed.  Other schemas are ignored.  This takes precedence over the exclude list.  It overrides the configuration property of the same name.", "array<string>", "schema1,schema2"),


    /**
     * Provides a list of schema names that should not be surveyed.  Only schemas not in this list are surveyed.
     */
    EXCLUDE_SCHEMA_NAMES ("excludeSchemaNames", "Provides a list of schema names that should not be surveyed.  Only schemas not in this list are surveyed.  It overrides the configuration property of the same name.", "array<string>", "schema1,schema2"),

    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     */
    SurveyRequestParameter(String name,
                           String description,
                           String dataType,
                           String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
    }


    /**
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the request parameter.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the request parameter.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the request parameter to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve the request parameters that every survey action service supports.
     *
     * @param finalAnalysisSteps the analysis steps that the survey action service can stop after, in the order
     *                           it runs them; null if they are not known
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getRequestParameterTypes(AnalysisStep[] finalAnalysisSteps)
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(getFinalAnalysisStepType(finalAnalysisSteps));

        return requestParameterTypes;
    }


    /**
     * Retrieve the request parameters supported by a survey action service that can be limited to some of the
     * schemas it finds.
     *
     * @param finalAnalysisSteps the analysis steps that the survey action service can stop after, in the order
     *                           it runs them; null if they are not known
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getSchemaSurveyRequestParameterTypes(AnalysisStep[] finalAnalysisSteps)
    {
        List<RequestParameterType> requestParameterTypes = getRequestParameterTypes(finalAnalysisSteps);

        requestParameterTypes.add(INCLUDE_SCHEMA_NAMES.getRequestParameterType());
        requestParameterTypes.add(EXCLUDE_SCHEMA_NAMES.getRequestParameterType());

        return requestParameterTypes;
    }


    /**
     * Return the description of the finalAnalysisStep request parameter for a particular survey action service.
     * The analysis steps it can stop after are listed in the validValues property, separated by commas, so that
     * a caller can discover them from the service's specification.
     *
     * @param finalAnalysisSteps the analysis steps that the survey action service can stop after, in the order
     *                           it runs them; null if they are not known
     * @return request parameter type
     */
    public static RequestParameterType getFinalAnalysisStepType(AnalysisStep[] finalAnalysisSteps)
    {
        RequestParameterType requestParameterType = FINAL_ANALYSIS_STEP.getRequestParameterType();

        if ((finalAnalysisSteps != null) && (finalAnalysisSteps.length > 0))
        {
            StringBuilder validValues = new StringBuilder();

            for (AnalysisStep analysisStep : finalAnalysisSteps)
            {
                if (! validValues.isEmpty())
                {
                    validValues.append(",");
                }

                validValues.append(analysisStep.getName());
            }

            Map<String, String> otherPropertyValues = new HashMap<>();

            otherPropertyValues.put(RequestParameterType.VALID_VALUES, validValues.toString());

            requestParameterType.setOtherPropertyValues(otherPropertyValues);
            requestParameterType.setExample(finalAnalysisSteps[0].getName());
        }

        return requestParameterType;
    }


    /**
     * Return the description of the finalAnalysisStep configuration property for a particular survey action
     * service.  It sets the final analysis step for every survey the service runs, unless a request overrides it
     * with the request parameter of the same name.
     *
     * @param finalAnalysisSteps the analysis steps that the survey action service can stop after, in the order
     *                           it runs them; null if they are not known
     * @return configuration property type
     */
    public static ConfigurationPropertyType getFinalAnalysisStepConfigurationPropertyType(AnalysisStep[] finalAnalysisSteps)
    {
        RequestParameterType      requestParameterType      = getFinalAnalysisStepType(finalAnalysisSteps);
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(requestParameterType.getName());
        configurationPropertyType.setDescription("The name of the last analysis step that the survey action service is to run in every survey.  The survey stops once this step is complete.  If the survey never reaches the named step, it runs to completion.  A request can override it with the request parameter of the same name.");
        configurationPropertyType.setDataType(requestParameterType.getDataType());
        configurationPropertyType.setExample(requestParameterType.getExample());
        configurationPropertyType.setOtherPropertyValues(requestParameterType.getOtherPropertyValues());

        return configurationPropertyType;
    }


    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return request parameter type
     */
    public RequestParameterType getRequestParameterType()
    {
        RequestParameterType requestParameterType = new RequestParameterType();

        requestParameterType.setName(name);
        requestParameterType.setDescription(description);
        requestParameterType.setDataType(dataType);
        requestParameterType.setExample(example);

        return requestParameterType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SurveyRequestParameter{ name=" + name + "}";
    }
}
