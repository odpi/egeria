/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.platformservices.properties;

import java.util.Date;
import java.util.Objects;

/**
 * Describes the build information for the platform.
 */
public class BuildProperties
{
    private String name     = null;
    private Date   time     = null;
    private String version  = null;
    private String artifact = null;
    private String group    = null;

    public BuildProperties()
    {
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getArtifact()
    {
        return artifact;
    }

    public void setArtifact(String artifact)
    {
        this.artifact = artifact;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    @Override
    public String toString()
    {
        return "BuildProperties{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", version='" + version + '\'' +
                ", artifact='" + artifact + '\'' +
                ", group='" + group + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        BuildProperties that = (BuildProperties) objectToCompare;
        return Objects.equals(name, that.name) && Objects.equals(time, that.time) && Objects.equals(version, that.version) && Objects.equals(artifact, that.artifact) && Objects.equals(group, that.group);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, time, version, artifact, group);
    }
}
