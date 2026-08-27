/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.connections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ResourceConnectionProperties links a metadata element to the connection that describes how to create a
 * connector that can access its digital resource.  The label and description are used to describe the
 * connector/digital resource that is reached through this connection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class",
        defaultImpl = ResourceConnectionProperties.class)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetConnectionProperties.class, name = "AssetConnectionProperties"),
        })
public class ResourceConnectionProperties extends LabeledRelationshipProperties
{
    /**
     * Default constructor
     */
    public ResourceConnectionProperties()
    {
        super();
        super.typeName = OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object to copy
     */
    public ResourceConnectionProperties(ResourceConnectionProperties template)
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
        return "ResourceConnectionProperties{} " + super.toString();
    }
}
