/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;


import com.fasterxml.jackson.annotation.*;

import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APISchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ComplexSchemaTypeProperties describes a schema with multiple attributes.  Notice it does not contain the attributes.
 * This is because a complex schema type may have literally thousands of attributes
 * and so the attribute contents are retrieved separated through calls that support paging.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = APISchemaTypeProperties.class, name = "APISchemaTypeProperties"),
        @JsonSubTypes.Type(value = TabularSchemaTypeProperties.class, name = "TabularSchemaTypeProperties"),
        @JsonSubTypes.Type(value = DatabaseSchemaTypeProperties.class, name = "DatabaseSchemaTypeProperties"),
        @JsonSubTypes.Type(value = EventTypeProperties.class, name = "EventTypeProperties"),
              })
public class RootSchemaTypeProperties extends ComplexSchemaTypeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public RootSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public RootSchemaTypeProperties(RootSchemaTypeProperties template)
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
        return "ComplexSchemaTypeProperties{" +
                       "versionNumber='" + getVersionNumber() + '\'' +
                       ", author='" + getAuthor() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", encodingStandard='" + getEncodingStandard() + '\'' +
                       ", namespace='" + getNamespace() + '\'' +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}