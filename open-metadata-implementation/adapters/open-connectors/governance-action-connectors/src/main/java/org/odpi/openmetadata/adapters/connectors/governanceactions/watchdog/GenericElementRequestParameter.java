/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum GenericElementRequestParameter
{
    INSTANCE_TO_MONITOR                  ("instanceToMonitor", "Requests that the governance action service monitors a single metadata instance.", "string", ""),
    INTERESTING_TYPE_NAME                ("interestingTypeName", "Defines the type of element that this monitor is focused on.", "string", ""),
    ACTION_TARGET_NAME                   ("actionTargetName", "Override the action target name used for the element that has been created, updated or deleted.  Default is 'receivedElement'.", "string", ""),
    ACTION_TARGET_TWO_NAME               ("actionTargetNameTwo", "Override the action target name used for the related element to the one that has been created, updated or deleted.  Default is 'receivedElementTwo'.", "string", ""),
    NEW_ELEMENT_PROCESS_NAME             ("newElementProcessName", "Qualified name of the process to run each time a new element event is detected.", "string", ""),
    UPDATED_ELEMENT_PROCESS_NAME         ("updatedElementProcessName", "Qualified name of the process to run each time an update element event is detected.", "string", ""),
    DELETED_ELEMENT_PROCESS_NAME         ("deletedElementProcessName", "Qualified name of the process to run each time a delete element event is detected.", "string", ""),
    CLASSIFIED_ELEMENT_PROCESS_NAME      ("classifiedElementProcessName", "Qualified name of the process to run each time a classified element event is detected.", "string", ""),
    RECLASSIFIED_ELEMENT_PROCESS_NAME    ("reclassifiedElementProcessName", "Qualified name of the process to run each time a reclassified element event is detected.", "string", ""),
    DECLASSIFIED_ELEMENT_PROCESS_NAME    ("declassifiedElementProcessName", "Qualified name of the process to run each time a declassified element event is detected.", "string", ""),
    NEW_RELATIONSHIP_PROCESS_NAME        ("newRelationshipProcessName", "Qualified name of the process to run each time a new relationship event is detected.", "string", ""),
    UPDATED_RELATIONSHIP_PROCESS_NAME    ("updatedRelationshipProcessName", "Qualified name of the process to run each time an updated relationship event is detected.", "string", ""),
    DELETED_RELATIONSHIP_PROCESS_NAME    ("deletedRelationshipProcessName", "Qualified name of the process to run each time a deleted relationship event is detected.", "string", ""),
    CHANGED_PROPERTIES                   ("ChangedProperties", "A common separated list of property names that have changed values that are added to the request properties.", "string", ""),

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
    GenericElementRequestParameter(String name,
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

        for (GenericElementRequestParameter requestParameter : GenericElementRequestParameter.values())
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
