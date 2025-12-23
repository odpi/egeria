/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConceptBeadAttributeProperties describes a concept's attribute in a concept model.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptBeadAttributeProperties extends ConceptModelElementProperties
{
    /**
     * Default constructor
     */
    public ConceptBeadAttributeProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONCEPT_BEAD_ATTRIBUTE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConceptBeadAttributeProperties(ConceptModelElementProperties template)
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
        return "ConceptBeadAttributeProperties{} " + super.toString();
    }
}
