/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.opengovernance.controls.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * ProvisionTabularDataSetRequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum ProvisionTabularDataSetRequestParameter
{
    NO_LINEAGE( "noLineage", "If this property is set to any value, do not produce lineage as part of the provisioning process.", "string", ""),
    TOP_LEVEL_PROCESS_NAME( "topLevelProcessQualifiedName", "Qualified name to use for the top level process that represents this governance action service.  It overrides the default value of 'Egeria:MoveCopyDeleteFileGovernanceActionService'.", "string", ""),
    TOP_LEVEL_PROCESS_TEMPLATE_NAME("topLevelProcessTemplateQualifiedName", "Qualified name of the template to use for the top level process that represents this governance action service.  If it is not specified, no template is used.", "", ""),
    TOP_LEVEL_PROCESS_ONLY_LINEAGE("topLevelProcessLineageOnly", "If this property is set, lineage mappings are connected to the top level process representing this governance action service.", "", ""),
    IGNORE_COLUMN_LEVEL_LINEAGE("ignoreColumnLevelLineage", "If this property is set, the lineage relationships between schema elements are not created.", "", ""),
    INFORMATION_SUPPLY_CHAIN_QUALIFIED_NAME("informationSupplyChain", "If this property is set, the value it is set to is the qualified name of the information supply chain that this provisioning process belongs to.  It is used to set the iscQualifiedName in the lineage relationships.", DataType.STRING.getName(), "InformationSupplyChain:Onboard My Data"),

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
    ProvisionTabularDataSetRequestParameter(String name,
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

        for (ProvisionTabularDataSetRequestParameter requestParameter : ProvisionTabularDataSetRequestParameter.values())
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
        return "MoveCopyFileRequestParameter{ name=" + name + "}";
    }
}
