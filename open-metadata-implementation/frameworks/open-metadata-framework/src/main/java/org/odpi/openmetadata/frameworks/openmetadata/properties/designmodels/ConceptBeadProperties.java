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
 * ConceptBeadProperties describes a concept in a concept model.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptBeadProperties extends ConceptModelElementProperties
{
    /**
     * Default constructor
     */
    public ConceptBeadProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONCEPT_BEAD.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConceptBeadProperties(ConceptBeadProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConceptBeadProperties(ConceptModelElementProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.CONCEPT_BEAD.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConceptBeadProperties{} " + super.toString();
    }
}
