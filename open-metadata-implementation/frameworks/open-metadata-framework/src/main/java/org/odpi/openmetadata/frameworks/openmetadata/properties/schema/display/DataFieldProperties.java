/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldProperties is a class for representing a data field within a Form, Report or Que
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFieldProperties extends SchemaAttributeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public DataFieldProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DataFieldProperties(DataFieldProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DataFieldProperties(SchemaAttributeProperties template)
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
        return "DataFieldProperties{} " + super.toString();
    }
}
