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
 * ConceptBeadRelationshipProperties describes a concept's relationship in a concept model.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptBeadRelationshipProperties extends ConceptModelElementProperties
{
    /**
     * Default constructor
     */
    public ConceptBeadRelationshipProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConceptBeadRelationshipProperties(ConceptBeadRelationshipProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConceptBeadRelationshipProperties(ConceptModelElementProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConceptBeadRelationshipProperties{} " + super.toString();
    }
}
