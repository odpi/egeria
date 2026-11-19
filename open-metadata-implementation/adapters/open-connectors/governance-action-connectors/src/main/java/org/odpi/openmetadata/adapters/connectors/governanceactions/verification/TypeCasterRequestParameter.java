/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;


import org.odpi.openmetadata.frameworks.opengovernance.controls.RequestParameter;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * TypeCasterRequestParameter describes the request parameter (and configuration property) used by the
 * Type Caster Governance Action Service to list the types that it chooses between when it describes the type of its
 * action target.  The request parameter overrides any value set in the configuration properties.
 */
public enum TypeCasterRequestParameter
{
    CANDIDATE_TYPE_NAMES (RequestParameter.CANDIDATE_TYPE_NAMES.name,
                          RequestParameter.CANDIDATE_TYPE_NAMES.description,
                          RequestParameter.CANDIDATE_TYPE_NAMES.dataType,
                          RequestParameter.CANDIDATE_TYPE_NAMES.example),
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
    TypeCasterRequestParameter(String name,
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

        for (TypeCasterRequestParameter requestParameter : TypeCasterRequestParameter.values())
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
        return "TypeCasterRequestParameter{ name=" + name + "}";
    }
}
