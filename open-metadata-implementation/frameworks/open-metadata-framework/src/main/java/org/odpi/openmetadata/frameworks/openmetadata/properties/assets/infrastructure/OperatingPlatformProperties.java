/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ByteOrdering;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OperatingPlatformProperties describes the properties for the operating platform (operating system and hardware) that a host is running.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OperatingPlatformProperties extends AuthoredReferenceableProperties
{
    private String       operatingSystem          = null;
    private ByteOrdering byteOrdering             = null;
    private String       operatingSystemPatchLevel = null;


    /**
     * Default constructor
     */
    public OperatingPlatformProperties()
    {
        super();
        super.typeName = OpenMetadataType.OPERATING_PLATFORM.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OperatingPlatformProperties(OperatingPlatformProperties template)
    {
        super(template);

        if (template != null)
        {
            operatingSystem           = template.getOperatingSystem();
            byteOrdering              = template.getByteOrdering();
            operatingSystemPatchLevel = template.getOperatingSystemPatchLevel();
        }
    }


    /**
     * Return the name of the operating system running on this operating platform.
     *
     * @return string name
     */
    public String getOperatingSystem()
    {
        return operatingSystem;
    }


    /**
     * Set up the name of the operating system running on this operating platform.
     *
     * @param operatingSystem string name
     */
    public void setOperatingSystem(String operatingSystem)
    {
        this.operatingSystem = operatingSystem;
    }


    /**
     * Return the byte ordering used by the hardware of the operating platform.
     *
     * @return enum
     */
    public ByteOrdering getByteOrdering()
    {
        return byteOrdering;
    }


    /**
     * Set up the byte ordering used by the hardware of the operating platform.
     *
     * @param byteOrdering enum
     */
    public void setByteOrdering(ByteOrdering byteOrdering)
    {
        this.byteOrdering = byteOrdering;
    }


    /**
     * Return the level of patches applied to the operating system.
     *
     * @return string version
     */
    public String getOperatingSystemPatchLevel()
    {
        return operatingSystemPatchLevel;
    }


    /**
     * Set up the level of patches applied to the operating system.
     *
     * @param operatingSystemPatchLevel string version
     */
    public void setOperatingSystemPatchLevel(String operatingSystemPatchLevel)
    {
        this.operatingSystemPatchLevel = operatingSystemPatchLevel;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OperatingPlatformProperties{" +
                "operatingSystem='" + operatingSystem + '\'' +
                ", byteOrdering=" + byteOrdering +
                ", operatingSystemPatchLevel='" + operatingSystemPatchLevel + '\'' +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OperatingPlatformProperties that = (OperatingPlatformProperties) objectToCompare;
        return Objects.equals(operatingSystem, that.operatingSystem) &&
                       byteOrdering == that.byteOrdering &&
                       Objects.equals(operatingSystemPatchLevel, that.operatingSystemPatchLevel);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), operatingSystem, byteOrdering, operatingSystemPatchLevel);
    }
}
