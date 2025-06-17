/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TabularColumnProperties is a class for representing a column within a table type structure.
 * Tabular columns are schema attributes with a simple type attached
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseColumnProperties.class, name = "DatabaseColumnProperties"),
        })
public class TabularColumnProperties extends SchemaAttributeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public TabularColumnProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TABULAR_COLUMN.typeName);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public TabularColumnProperties(TabularColumnProperties template)
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
        return "TabularColumnProperties{} " + super.toString();
    }
}
