/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ComplexSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.RootSchemaTypeProperties;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TabularSchemaTypeProperties is a class for representing a tabular schema such as the
 * structure of a CSV file.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TabularSchemaTypeProperties extends RootSchemaTypeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public TabularSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public TabularSchemaTypeProperties(TabularSchemaTypeProperties template)
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
        return "TabularSchemaTypeProperties{} " + super.toString();
    }
}
