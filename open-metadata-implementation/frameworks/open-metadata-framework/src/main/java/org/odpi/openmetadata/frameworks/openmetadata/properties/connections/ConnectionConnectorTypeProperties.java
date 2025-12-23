/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.connections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ConnectionConnectorType relationship properties as a placeholder in case the relationship has properties added.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionConnectorTypeProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor.
     */
    public ConnectionConnectorTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone Constructor to return a copy of a object.
     *
     * @param template Connection to copy
     */
    public ConnectionConnectorTypeProperties(ConnectionConnectorTypeProperties template)
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
        return "ConnectionConnectorTypeProperties{} " + super.toString();
    }
}
