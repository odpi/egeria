/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The OpenMetadataTypeDefSummary holds basic identifying information for a specific OpenMetadataTypeDef.  It is used in
 * the registration process between repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefSummary extends OpenMetadataTypeDefLink
{
    protected long                        version     = 0L;
    protected String                      versionName = null;
    protected OpenMetadataTypeDefCategory category    = OpenMetadataTypeDefCategory.UNKNOWN_DEF;


    /**
     * Default constructor
     */
    public OpenMetadataTypeDefSummary()
    {
        super();
    }


    /**
     * Typical constructor is passed the properties of the typedef being constructed.
     *
     * @param category    category of this OpenMetadataTypeDef
     * @param guid        unique id for the OpenMetadataTypeDef
     * @param name        unique name for the OpenMetadataTypeDef
     * @param version     active version number for the OpenMetadataTypeDef
     * @param versionName active version name for the OpenMetadataTypeDef
     */
    OpenMetadataTypeDefSummary(OpenMetadataTypeDefCategory category,
                               String                      guid,
                               String                      name,
                               long                        version,
                               String                      versionName)
    {
        super(guid, name);

        this.category = category;
        this.version = version;
        this.versionName = versionName;
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template OpenMetadataTypeDefSummary
     */
    public OpenMetadataTypeDefSummary(OpenMetadataTypeDefSummary template)
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
     * Return the category of the OpenMetadataTypeDef.
     *
     * @return OpenMetadataTypeDefCategory enum
     */
    public OpenMetadataTypeDefCategory getCategory()
    {
        return category;
    }


    /**
     * Set up the category of the OpenMetadataTypeDef.
     *
     * @param category OpenMetadataTypeDefCategory enum
     */
    public void setCategory(OpenMetadataTypeDefCategory category)
    {
        this.category = category;
    }


    /**
     * Return the version of the OpenMetadataTypeDef.  Versions are created when a OpenMetadataTypeDef's properties are changed.  If
     * a description is updated, then this does not create a new version.
     *
     * @return String version number
     */
    public long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the OpenMetadataTypeDef.  Versions are created when a OpenMetadataTypeDef's properties are changed.  If
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
        return "OpenMetadataTypeDefSummary{" +
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
        OpenMetadataTypeDefSummary that = (OpenMetadataTypeDefSummary) objectToCompare;
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
