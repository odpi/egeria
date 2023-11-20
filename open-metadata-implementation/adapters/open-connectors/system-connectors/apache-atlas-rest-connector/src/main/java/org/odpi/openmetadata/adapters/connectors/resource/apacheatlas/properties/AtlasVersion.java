/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasVersion describes the version of the connected Apache Atlas
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasVersion
{
    String description = null;
    String revision = null;
    String version = null;
    String name = null;


    /**
     * Default constructor.
     */
    public AtlasVersion()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template instance to copy
     */
    public AtlasVersion(AtlasVersion template)
    {
        if (template != null)
        {
            this.description = template.getDescription();
            this.revision = template.getRevision();
            this.version = template.getVersion();
            this.name = template.getName();
        }
    }


    /**
     * Return the description of Apache Atlas.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of Apache Atlas.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return whether this is a release or a snapshot.
     *
     * @return string
     */
    public String getRevision()
    {
        return revision;
    }


    /**
     * Set up whether this is a release or a snapshot.
     *
     * @param revision string
     */
    public void setRevision(String revision)
    {
        this.revision = revision;
    }


    /**
     * Return the version of Apache Atlas running.
     *
     * @return string
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of Apache Atlas running.
     *
     * @param version string
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the name of the Apache Atlas runtime.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the Apache Atlas runtime.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AtlasVersion{" +
                       "description='" + description + '\'' +
                       ", revision='" + revision + '\'' +
                       ", version='" + version + '\'' +
                       ", name='" + name + '\'' +
                       '}';
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
        if (! (objectToCompare instanceof AtlasVersion that))
        {
            return false;
        }
        return Objects.equals(description, that.description) && Objects.equals(revision, that.revision) && Objects.equals(
                version, that.version) && Objects.equals(name, that.name);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(description, revision, version, name);
    }
}
