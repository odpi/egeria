/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseTableProperties is a class for representing a relational database table.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseViewProperties.class, name = "DatabaseViewProperties"),
        })
public class DatabaseTableProperties extends SchemaElementProperties
{
    private static final long    serialVersionUID = 1L;

    private List<String> aliases = null;


    /**
     * Default constructor
     */
    public DatabaseTableProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DatabaseTableProperties(DatabaseTableProperties template)
    {
        super(template);

    }


    /**
     * Return a list of alternative names for the table.
     *
     * @return list of names
     */
    public List<String> getAliases()
    {
        if (aliases == null)
        {
            return null;
        }
        else if (aliases.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(aliases);
    }


    /**
     * Set up a list of alternative names for the table.
     *
     * @param aliases list of names
     */
    public void setAliases(List<String> aliases)
    {
        this.aliases = aliases;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DatabaseTableProperties{" +
                       "aliases=" + aliases +
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
