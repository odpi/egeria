/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseSchemaProperties is a class for representing the top level database schema.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseSchemaProperties extends DataSetProperties
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public DatabaseSchemaProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DatabaseSchemaProperties(DatabaseSchemaProperties template)
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
        return "DatabaseSchemaProperties{" +
                "displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerCategory=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOtherOriginValues() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", vendorProperties=" + getVendorProperties() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
