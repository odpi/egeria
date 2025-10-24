/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * QueryDataFieldProperties is a class for representing a data field within a Form, Report or Query
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class QueryDataFieldProperties extends SchemaAttributeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public QueryDataFieldProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.QUERY_DATA_FIELD.typeName);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public QueryDataFieldProperties(QueryDataFieldProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public QueryDataFieldProperties(SchemaAttributeProperties template)
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
        return "QueryDataFieldProperties{} " + super.toString();
    }
}
