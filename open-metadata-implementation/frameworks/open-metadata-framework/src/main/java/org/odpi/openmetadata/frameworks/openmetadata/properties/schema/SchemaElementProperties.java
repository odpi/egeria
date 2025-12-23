/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaElementProperties carries the common parameters for creating or updating schema elements such as schema types and attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchemaTypeProperties.class, name = "SchemaTypeProperties"),
        @JsonSubTypes.Type(value = SchemaAttributeProperties.class, name = "SchemaAttributeProperties")
              })
public class SchemaElementProperties extends AuthoredReferenceableProperties
{
    /**
     * Default constructor
     */
    public SchemaElementProperties()
    {
        super();
        super.typeName = OpenMetadataType.SCHEMA_ELEMENT.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaElementProperties(AuthoredReferenceableProperties template)
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
        return "SchemaElementProperties{" +
                "} " + super.toString();
    }
}
