/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasElementDef describes a single valid value in an EnumDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasElementDef
{
    private String value       = null;
    private String description = null;
    private int    ordinal     = 0;


    /**
     * Default constructor
     */
    public AtlasElementDef()
    {
    }


    public String getValue()
    {
        return value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public int getOrdinal()
    {
        return ordinal;
    }


    public void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }


    @Override
    public String toString()
    {
        return "AtlasElementDef{" +
                       "value='" + value + '\'' +
                       ", description='" + description + '\'' +
                       ", ordinal=" + ordinal +
                       '}';
    }
}
