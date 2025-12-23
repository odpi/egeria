/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.RootSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APISchemaTypeProperties provides the root schema type for an API specification that includes the operations,
 * and their parameters, headers and responses.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APISchemaTypeProperties extends RootSchemaTypeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public APISchemaTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.API_SCHEMA_TYPE.typeName;
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public APISchemaTypeProperties(APISchemaTypeProperties template)
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
        return "APISchemaTypeProperties{} " + super.toString();
    }
}