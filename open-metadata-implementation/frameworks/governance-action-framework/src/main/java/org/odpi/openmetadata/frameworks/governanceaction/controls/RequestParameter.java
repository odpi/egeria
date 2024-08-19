/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.controls;


import java.util.ArrayList;
import java.util.List;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum RequestParameter
{
    PUBLISH_ZONES ("publishZones", "The list of governance zones that defines the asset's zone membership.  These zones replace any zones that were previously set up.", "array<string>", "zone1,zone2"),
    NEW_ZONE ("newZone", "The name of a governance zone to add to an asset's zone membership.  This zone is added to the existing zone membership list.", "string", "zone1"),
    SOURCE_FILE ("sourceFile", "The name of a file to process.", "string", "myFile.csv"),
    SOURCE_DIRECTORY ("sourceDirectory", "The name of a directory (folder) to process.", "string", "directoryA/B/C"),
    DESTINATION_DIRECTORY ("destinationDirectory", "The name of a directory (folder) to add files to.", "string", "directoryA/B/C"),

    DOMAIN_IDENTIFIER ("domainIdentifier", "Identifier for the governance domain; 0 means any/all governance domain(s).", "int", "0"),
    STATUS_IDENTIFIER ("statusIdentifier", "Identifier for the governance classification status.", "int", "0"),
    LEVEL_IDENTIFIER ("levelIdentifier", "Identifier for the level associated with a governance classification.", "int", "0"),
    BASIS_IDENTIFIER ("basisIdentifier", "Identifier for the basis under which the retention date(s) are set.", "int", "2"),
    RETENTION_TIME_TO_ARCHIVE ("timeToArchive", "Number of milliseconds before the data should be archived.", "long", "31556952000"),
    RETENTION_TIME_TO_DELETE ("timeToDelete", "Number of milliseconds before the data should be deleted.", "long", "31556952000"),
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
    RequestParameter(String name,
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

        for (RequestParameter requestParameter : RequestParameter.values())
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
