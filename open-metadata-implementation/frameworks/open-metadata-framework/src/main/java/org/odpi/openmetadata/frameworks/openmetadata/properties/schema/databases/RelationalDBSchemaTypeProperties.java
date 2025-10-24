/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.RootSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationalDBSchemaTypeProperties is a java bean used to create the database schema type associated with the database.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationalDBSchemaTypeProperties extends RootSchemaTypeProperties
{
    /**
     * Default constructor
     */
    public RelationalDBSchemaTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public RelationalDBSchemaTypeProperties(RelationalDBSchemaTypeProperties template)
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
        return "RelationalDBSchemaTypeProperties{} " + super.toString();
    }
}
