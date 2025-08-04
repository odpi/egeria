/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
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
public class TabularFileColumnProperties extends TabularColumnProperties
{
    /**
     * Default constructor used by subclasses
     */
    public TabularFileColumnProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TABULAR_FILE_COLUMN.typeName);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public TabularFileColumnProperties(TabularFileColumnProperties template)
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
        return "TabularFileColumnProperties{} " + super.toString();
    }
}
