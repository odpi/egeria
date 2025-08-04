/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SemanticDefinitionProperties describes the link between an element and the related glossary term
 * that identifies the associated meaning.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SemanticDefinitionProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor
     */
    public SemanticDefinitionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public SemanticDefinitionProperties(SemanticDefinitionProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SemanticDefinitionProperties{} " + super.toString();
    }
}
