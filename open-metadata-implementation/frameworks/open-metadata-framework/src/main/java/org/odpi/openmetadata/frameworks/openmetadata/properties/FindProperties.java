/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindAssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldQueryProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindProperties provides the base class for find by property requests.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = LevelIdentifierProperties.class, name = "LevelIdentifierProperties"),
                @JsonSubTypes.Type(value = DataFieldQueryProperties.class, name = "DataFieldQueryProperties"),
                @JsonSubTypes.Type(value = FindNameProperties.class, name = "FindNameProperties"),
                @JsonSubTypes.Type(value = FindAssetOriginProperties.class, name = "FindAssetOriginProperties"),
        })
public class FindProperties
{
    /**
     * Default constructor
     */
    public FindProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public FindProperties(FindProperties template)
    {

    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindProperties{}";
    }
}