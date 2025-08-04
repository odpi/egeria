/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeployedDatabaseSchemaProperties is a class for representing the top level database schema.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeployedDatabaseSchemaProperties extends DataSetProperties
{
    /**
     * Default constructor
     */
    public DeployedDatabaseSchemaProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DeployedDatabaseSchemaProperties(DeployedDatabaseSchemaProperties template)
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
        return "DeployedDatabaseSchemaProperties{} " + super.toString();
    }
}
