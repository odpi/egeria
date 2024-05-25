/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The TypeDefSummary holds basic identifying information for a specific TypeDef.  It is used in
 * the registration process between repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefSummary extends TypeDefLink
{
    @Serial
    private static final long serialVersionUID = 1L;

    protected long            version     = 0L;
    protected String          versionName = null;
    protected TypeDefCategory category    = TypeDefCategory.UNKNOWN_DEF;


    /**
     * Default constructor
     */
    public TypeDefSummary()
    {
        super();
    }


    /**
     * Typical constructor is passed the properties of the typedef being constructed.
     *
     * @param category    category of this TypeDef
     * @param guid        unique id for the TypeDef
     * @param name        unique name for the TypeDef
     * @param version     active version number for the TypeDef
     * @param versionName active version name for the TypeDef
     */
    TypeDefSummary(TypeDefCategory category,
                   String          guid,
                   String          name,
                   long            version,
                   String          versionName)
    {
        super(guid, name);

        this.category = category;
        this.version = version;
        this.versionName = versionName;
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template TypeDefSummary
     */
    public TypeDefSummary(TypeDefSummary template)
    {
        super(template);

        if (template != null)
        {
            this.category = template.getCategory();
            this.versionName = template.getVersionName();
            this.version = template.getVersion();
        }
    }


    /**
     * Return the category of the TypeDef.
     *
     * @return TypeDefCategory enum
     */
    public TypeDefCategory getCategory()
    {
        return category;
    }


    /**
     * Set up the category of the TypeDef.
     *
     * @param category TypeDefCategory enum
     */
    public void setCategory(TypeDefCategory category)
    {
        this.category = category;
    }

    /**
     * Return the version of the TypeDef.  Versions are created when a TypeDef's properties are changed.  If
     * a description is updated, then this does not create a new version.
     *
     * @return String version number
     */
    public long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the TypeDef.  Versions are created when a TypeDef's properties are changed.  If
     * a description is updated, then this does not create a new version.
     *
     * @param version long version number
     */
    public void setVersion(long version)
    {
        this.version = version;
    }


    /**
     * Return the version name is more of a human readable form of the version number.  It can be used to show whether the
     * change is a minor or major update.
     *
     * @return String version name
     */
    public String getVersionName()
    {
        return versionName;
    }


    /**
     * Set up the version name is more of a human readable form of the version number.  It can be used to show whether the
     * change is a minor or major update.
     *
     * @param versionName String version name
     */
    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TypeDefSummary{" +
                "version=" + version +
                ", versionName='" + versionName + '\'' +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        TypeDefSummary that = (TypeDefSummary) objectToCompare;
        return version == that.version &&
                       Objects.equals(versionName, that.versionName) &&
                       category == that.category;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), version, versionName, category);
    }
}
