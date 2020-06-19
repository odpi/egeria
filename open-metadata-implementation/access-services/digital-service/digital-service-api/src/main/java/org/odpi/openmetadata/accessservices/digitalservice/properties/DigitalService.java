/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalService is an anchor object for tracking the lifecycle of one of an organization's digital service.
 * The digital service instance is create when the digital service is just a concept.  It is used to record
 * the role and implementation style that it has along with information about how it will operate.
 * As the digital service moved through its lifecycle from implementation to deployment to use, more
 * information is attached to the digital service instance to support the correct management and compliance
 * of the service.
 */

public class DigitalService extends DigitalServiceElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String                            displayName            = null;
    private String                            description            = null;
    private String                            version                = null;
    /**
     * Default constructor
     */
    public DigitalService()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalService(DigitalService template)
    {

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.version = template.getVersion();
        }
    }


    /**
     * Return the version number for this Asset's type.
     *
     * @return String
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version number for this Asset's type.
     *
     * @param version String
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the display name for this asset (normally a shortened for of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this asset (normally a shortened for of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this asset.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this asset.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * {@inheritDoc}
     *
     * JSON-style toString
     */
    @Override
    public String toString()
    {
        return "DigitalService{" +
                "name='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                '}';
    }


    /**
     * {@inheritDoc}
     *
     * Return comparison result based on the content of the properties.
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof DigitalService))
        {
            return false;
        }
        DigitalService asset = (DigitalService) objectToCompare;
        return Objects.equals(getVersion(),asset.getVersion()) &&
                Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getDescription(), asset.getDescription());
    }


    /**
     * {@inheritDoc}
     *
     * Return hash code for this object
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getDisplayName(),
                            getDescription(),
                            getVersion());
    }
}
