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
 * AtlasObjectId describes a reference to an Atlas entity instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasObjectId
{
    public static final String KEY_GUID              = "guid";
    public static final String KEY_TYPENAME          = "typeName";
    public static final String KEY_UNIQUE_ATTRIBUTES = "uniqueAttributes";

    private String              guid;
    private String              typeName;
    private Map<String, Object> uniqueAttributes;


    public AtlasObjectId()
    {
    }


    public String getGuid()
    {
        return guid;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getTypeName()
    {
        return typeName;
    }


    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    public Map<String, Object> getUniqueAttributes()
    {
        return uniqueAttributes;
    }


    public void setUniqueAttributes(Map<String, Object> uniqueAttributes)
    {
        this.uniqueAttributes = uniqueAttributes;
    }


    @Override
    public String toString()
    {
        return "AtlasObjectId{" +
                       "guid='" + guid + '\'' +
                       ", typeName='" + typeName + '\'' +
                       ", uniqueAttributes=" + uniqueAttributes +
                       '}';
    }
}
