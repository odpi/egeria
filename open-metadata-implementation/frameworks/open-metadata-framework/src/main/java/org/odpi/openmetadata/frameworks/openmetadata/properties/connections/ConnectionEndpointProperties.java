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
 * The ConnectionEndpoint relationship properties as a placeholder in case the relationship has properties added.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionEndpointProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor sets the Connection properties to null.
     */
    public ConnectionEndpointProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object.
     *
     * @param template Connection to copy
     */
    public ConnectionEndpointProperties(ConnectionEndpointProperties template)
    {
        super(template);
    }

    /**
     * Standard toString method. Note SecuredProperties and other credential type properties are not displayed.
     * This is deliberate because there is no knowing where the string will be printed.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectionEndpointProperties{} " + super.toString();
    }
}
