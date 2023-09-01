/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Provides the common properties for an Apache Atlas instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasStruct
{
    public static final String KEY_TYPENAME   = "typeName";
    public static final String KEY_ATTRIBUTES = "attributes";

    private String              typeName   = null;
    private Map<String, Object> attributes = null;


    public AtlasStruct()
    {
    }


    public String getTypeName()
    {
        return typeName;
    }


    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    public Map<String, Object> getAttributes()
    {
        return attributes;
    }


    public void setAttributes(Map<String, Object> attributes)
    {
        this.attributes = attributes;
    }


    @Override
    public String toString()
    {
        return "AtlasStruct{" +
                       "typeName='" + typeName + '\'' +
                       ", attributes=" + attributes +
                       '}';
    }
}
