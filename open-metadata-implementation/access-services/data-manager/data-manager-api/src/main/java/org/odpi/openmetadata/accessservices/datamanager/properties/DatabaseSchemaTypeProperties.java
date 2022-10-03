/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseSchemaTypeProperties is a java bean used to create the database schema type associated with the database.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseSchemaTypeProperties extends ReferenceableProperties
{
    private static final long     serialVersionUID = 1L;

    private String qualifiedName = null;



    /**
     * Default constructor
     */
    public DatabaseSchemaTypeProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public DatabaseSchemaTypeProperties(DatabaseSchemaTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
        }
    }


    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the stored display name property for the asset.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DatabaseSchemaTypeProperties{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof DatabaseSchemaTypeProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DatabaseSchemaTypeProperties that = (DatabaseSchemaTypeProperties) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualifiedName);
    }
}
