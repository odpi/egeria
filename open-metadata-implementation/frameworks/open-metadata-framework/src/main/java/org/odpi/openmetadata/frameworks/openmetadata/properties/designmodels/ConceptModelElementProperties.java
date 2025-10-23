/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConceptModelElement describes a concept in a concept model.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ConceptBeadProperties.class, name = "ConceptBeadProperties"),
                @JsonSubTypes.Type(value = ConceptBeadAttributeProperties.class, name = "ConceptBeadAttributeProperties"),
                @JsonSubTypes.Type(value = ConceptBeadRelationshipProperties.class, name = "ConceptBeadRelationshipProperties"),
        })
public class ConceptModelElementProperties extends DesignModelElementProperties
{
    /**
     * Default constructor
     */
    public ConceptModelElementProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONCEPT_MODEL_ELEMENT.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConceptModelElementProperties(DesignModelElementProperties template)
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
        return "ConceptModelElementProperties{} " + super.toString();
    }
}
