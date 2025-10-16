/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationalColumnProperties describes the properties of a database column.
 * The database column may have a fixed value (inherited from tabular column)
 * or be derived by a formula (stored in calculated value classification).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationalColumnProperties extends TabularColumnProperties
{
    /**
     * Default constructor
     */
    public RelationalColumnProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RELATIONAL_COLUMN.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RelationalColumnProperties(TabularColumnProperties template)
    {
        super(template);
    }



    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RelationalColumnProperties(SchemaAttributeProperties template)
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
        return "RelationalColumnProperties{" +
                "} " + super.toString();
    }
}
