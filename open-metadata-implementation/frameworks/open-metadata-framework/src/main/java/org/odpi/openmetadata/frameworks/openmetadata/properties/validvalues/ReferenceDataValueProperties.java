/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceDataValueProperties describes a valid value for external reference data.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ReferenceDataSetProperties.class, name = "ReferenceDataSetProperties"),
        })
public class ReferenceDataValueProperties extends ValidValueDefinitionProperties
{
    /**
     * Constructor
     */
    public ReferenceDataValueProperties()
    {
        super();
        super.typeName = OpenMetadataType.REFERENCE_DATA_VALUE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceDataValueProperties(ValidValueDefinitionProperties template)
    {
        super(template);
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ReferenceDataValueProperties{" + '}';
    }
}
