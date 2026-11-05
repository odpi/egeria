/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;


import org.odpi.openmetadata.frameworks.opengovernance.controls.RequestParameter;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * OriginSetterRequestParameter describes the request parameters (and configuration properties) used by the
 * Origin Setter Governance Action Service to describe the origin to set on its action targets.  The request parameters
 * override any values set in the configuration properties.
 */
public enum OriginSetterRequestParameter
{
    ORGANIZATION (RequestParameter.ORGANIZATION.name,
                  RequestParameter.ORGANIZATION.description,
                  RequestParameter.ORGANIZATION.dataType,
                  RequestParameter.ORGANIZATION.example),

    ORGANIZATION_PROPERTY_NAME (RequestParameter.ORGANIZATION_PROPERTY_NAME.name,
                                RequestParameter.ORGANIZATION_PROPERTY_NAME.description,
                                RequestParameter.ORGANIZATION_PROPERTY_NAME.dataType,
                                RequestParameter.ORGANIZATION_PROPERTY_NAME.example),

    BUSINESS_CAPABILITY (RequestParameter.BUSINESS_CAPABILITY.name,
                         RequestParameter.BUSINESS_CAPABILITY.description,
                         RequestParameter.BUSINESS_CAPABILITY.dataType,
                         RequestParameter.BUSINESS_CAPABILITY.example),

    BUSINESS_CAPABILITY_PROPERTY_NAME (RequestParameter.BUSINESS_CAPABILITY_PROPERTY_NAME.name,
                                       RequestParameter.BUSINESS_CAPABILITY_PROPERTY_NAME.description,
                                       RequestParameter.BUSINESS_CAPABILITY_PROPERTY_NAME.dataType,
                                       RequestParameter.BUSINESS_CAPABILITY_PROPERTY_NAME.example),
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
    OriginSetterRequestParameter(String name,
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

        for (OriginSetterRequestParameter requestParameter : OriginSetterRequestParameter.values())
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
        return "OriginSetterRequestParameter{ name=" + name + "}";
    }
}
