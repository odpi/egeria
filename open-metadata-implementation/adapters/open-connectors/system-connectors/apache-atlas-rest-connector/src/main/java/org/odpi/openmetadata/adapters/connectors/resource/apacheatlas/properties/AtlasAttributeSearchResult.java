/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasAttributeSearchResult describes matching attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasAttributeSearchResult
{
    private List<String>       name   = null;
    private List<List<Object>> values = null;


    public AtlasAttributeSearchResult()
    {
    }


    public List<String> getName()
    {
        return name;
    }


    public void setName(List<String> name)
    {
        this.name = name;
    }


    public List<List<Object>> getValues()
    {
        return values;
    }


    public void setValues(List<List<Object>> values)
    {
        this.values = values;
    }


    @Override
    public String toString()
    {
        return "AtlasAttributeSearchResult{" +
                       "name=" + name +
                       ", values=" + values +
                       '}';
    }
}
