/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * StringMapResponse is the response structure used on the OMAS REST API calls that return a
 * map of names to description as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StringMapResponse extends FFDCResponseBase
{
    private Map<String, String> stringMap = null;


    /**
     * Default constructor
     */
    public StringMapResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StringMapResponse(StringMapResponse template)
    {
        super(template);

        if (template != null)
        {
            this.stringMap = template.getStringMap();
        }
    }


    /**
     * Return the name list result.
     *
     * @return map of strings
     */
    public Map<String, String> getStringMap()
    {
        return stringMap;
    }


    /**
     * Set up the name to description map result.
     *
     * @param stringMap map of strings
     */
    public void setStringMap(Map<String, String> stringMap)
    {
        this.stringMap = stringMap;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "StringMapResponse{" +
                "stringMap=" + stringMap +
                "} " + super.toString();
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof StringMapResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(stringMap, that.stringMap);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(stringMap);
    }
}
