/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.*;

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
                @JsonSubTypes.Type(value = LevelIdentifierQueryProperties.class, name = "LevelIdentifierQueryProperties"),
                @JsonSubTypes.Type(value = FindDigitalResourceOriginProperties.class, name = "FindDigitalResourceOriginProperties"),
                @JsonSubTypes.Type(value = FindPropertyNamesProperties.class, name = "FindPropertyNamesProperties"),
                @JsonSubTypes.Type(value = SecurityTagQueryProperties.class, name = "SecurityTagQueryProperties"),
                @JsonSubTypes.Type(value = SemanticAssignmentQueryProperties.class, name = "SemanticAssignmentQueryProperties"),
        })
public class FindProperties extends QueryOptions
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
    public FindProperties(QueryOptions template)
    {
        super (template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindProperties{" +
                "} " + super.toString();
    }
}