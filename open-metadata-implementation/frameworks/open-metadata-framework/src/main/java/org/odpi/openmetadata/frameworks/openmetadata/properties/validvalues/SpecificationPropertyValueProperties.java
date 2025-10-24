/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SpecificationPropertyValueProperties provides the properties for one of an element's specification properties.
 * The preferredValue is the value that is used in the open metadata property.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SpecificationPropertyValueProperties extends ValidValueDefinitionProperties
{
    /**
     * Constructor
     */
    public SpecificationPropertyValueProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SpecificationPropertyValueProperties(ValidValueDefinitionProperties template)
    {
        super(template);
        super.setTypeName(OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName);
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "SpecificationPropertyValueProperties{" + '}';
    }
}
