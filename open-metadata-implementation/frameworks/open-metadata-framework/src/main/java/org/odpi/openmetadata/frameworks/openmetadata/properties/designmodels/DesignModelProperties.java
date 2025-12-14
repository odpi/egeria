/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DesignModel describes a collection of design model elements that make up a model of a design.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ConceptModelProperties.class, name = "ConceptModelProperties"),
                @JsonSubTypes.Type(value = SolutionBlueprintProperties.class, name = "SolutionBlueprintProperties"),
        })
public class DesignModelProperties extends CollectionProperties
{
    /**
     * Default constructor
     */
    public DesignModelProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DESIGN_MODEL.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignModelProperties(CollectionProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DesignModelProperties{" +
                "} " + super.toString();
    }
}
