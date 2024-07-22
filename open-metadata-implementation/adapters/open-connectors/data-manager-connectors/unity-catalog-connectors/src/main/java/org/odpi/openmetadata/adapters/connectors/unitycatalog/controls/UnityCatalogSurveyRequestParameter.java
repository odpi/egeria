/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum UnityCatalogSurveyRequestParameter
{
    /**
     * Property name to control how much profiling the survey action service does.
     */
    FINAL_ANALYSIS_STEP ("finalAnalysisStep", "Property name to control how much profiling the survey action service does.", "string", "Schema Extraction"),

    CATALOG_NAME (UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                  UnityCatalogPlaceholderProperty.CATALOG_NAME.getDescription(),
                  UnityCatalogPlaceholderProperty.CATALOG_NAME.getDataType(),
                  UnityCatalogPlaceholderProperty.CATALOG_NAME.getExample()),

    SCHEMA_NAME(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(),
                UnityCatalogPlaceholderProperty.SCHEMA_NAME.getDescription(),
                UnityCatalogPlaceholderProperty.SCHEMA_NAME.getDataType(),
                UnityCatalogPlaceholderProperty.SCHEMA_NAME.getExample()),
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
    UnityCatalogSurveyRequestParameter(String name,
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

        for (UnityCatalogSurveyRequestParameter requestParameter : UnityCatalogSurveyRequestParameter.values())
        {
            requestParameterTypes.add(requestParameter.getRequestParameterType());
        }

        return requestParameterTypes;
    }


    /**
     * Retrieve the defined request parameters used by the Server Survey Service
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getServerSurveyRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(FINAL_ANALYSIS_STEP.getRequestParameterType());

        return requestParameterTypes;
    }


    /**
     * Retrieve the defined request parameters used by the Inside Catalog Survey Service
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getInsideCatalogSurveyRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(FINAL_ANALYSIS_STEP.getRequestParameterType());
        requestParameterTypes.add(CATALOG_NAME.getRequestParameterType());

        return requestParameterTypes;
    }


    /**
     * Retrieve the defined request parameters used by the Inside Schema Survey Service
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getInsideSchemaSurveyRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(FINAL_ANALYSIS_STEP.getRequestParameterType());
        requestParameterTypes.add(CATALOG_NAME.getRequestParameterType());
        requestParameterTypes.add(SCHEMA_NAME.getRequestParameterType());

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
