/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.RootSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TabularSchemaTypeProperties is a class for representing a tabular schema such as the
 * structure of a CSV file.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = RelationalTableTypeProperties.class, name = "RelationalTableTypeProperties"),
        })
public class TabularSchemaTypeProperties extends RootSchemaTypeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public TabularSchemaTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName;
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
