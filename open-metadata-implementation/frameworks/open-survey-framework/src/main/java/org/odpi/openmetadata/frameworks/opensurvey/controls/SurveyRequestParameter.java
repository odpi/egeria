/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.controls;


import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum SurveyRequestParameter
{
    /**
     * Property name to control how much profiling the survey action service does.
     */
    FINAL_ANALYSIS_STEP ("finalAnalysisStep", "Property name to control how much profiling the survey action service does.", "string", "Schema Extraction"),


    /**
     * Provide a list of analysis steps to ignore.  This has to be used with care because some analysis steps build on the work of earlier analysis steps, and so ignoring one of these earlier steps will prevent later steps from running.
     */
    IGNORE_ANALYSIS_STEPS ("ignoreAnalysisSteps", "Provide a list of analysis steps to ignore.  This has to be used with care because some analysis steps build on the work of earlier analysis steps, and so ignoring one of these earlier steps will prevent later steps from running.", "array<string>", "step3,step4"),

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
     * Retrieve all the defined request parameters
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        for (SurveyRequestParameter requestParameter : SurveyRequestParameter.values())
        {
            requestParameterTypes.add(requestParameter.getRequestParameterType());
        }

        return requestParameterTypes;
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
        return "RequestParameter{ name=" + name + "}";
    }
}
