/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBoundaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DesignModelBoundary describes a collection of design model boundaries and design model elements.  It is used
 * to organize the content of a design model into a hierarchical structure to simplify understanding.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SolutionBoundaryProperties.class, name = "SolutionBoundaryProperties"),
        })
public class DesignModelBoundaryProperties extends CollectionProperties
{
    /**
     * Default constructor
     */
    public DesignModelBoundaryProperties()
    {
        super();
        super.typeName = OpenMetadataType.DESIGN_MODEL_BOUNDARY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignModelBoundaryProperties(DesignModelBoundaryProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignModelBoundaryProperties(CollectionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.DESIGN_MODEL_BOUNDARY.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DesignModelBoundaryProperties{" +
                "} " + super.toString();
    }
}
