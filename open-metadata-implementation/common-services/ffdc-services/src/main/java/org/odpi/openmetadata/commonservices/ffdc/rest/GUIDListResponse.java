/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GUIDListResponse is the response structure used on the OMAS REST API calls that return a
 * list of unique identifiers (guids) as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GUIDListResponse extends FFDCResponseBase
{
    private List<String> guids = null;


    /**
     * Default constructor
     */
    public GUIDListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GUIDListResponse(GUIDListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.guids = template.getGUIDs();
        }
    }


    /**
     * Return the guids result.
     *
     * @return list of unique identifiers
     */
    public List<String> getGUIDs()
    {
        return guids;
    }


    /**
     * Set up the guids result.
     *
     * @param guids list of unique identifiers
     */
    public void setGUIDs(List<String> guids)
    {
        this.guids = guids;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GUIDListResponse{" +
                "guids=" + guids +
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
        if (!(objectToCompare instanceof GUIDListResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(guids, that.guids);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guids);
    }
}
