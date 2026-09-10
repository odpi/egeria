/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single owner of a job.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOwnershipJobFacetOwner
{
    private String name = null;
    private String type = null;


    /**
     * Default constructor
     */
    public OpenLineageOwnershipJobFacetOwner()
    {
    }


    /**
     * Return the identifier of the owner of the job.  It is recommended to define this as a URN, for example application:foo, user:jdoe or team:data.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the identifier of the owner of the job.  It is recommended to define this as a URN, for example application:foo, user:jdoe or team:data.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the type of the owner, for example MAINTAINER.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of the owner, for example MAINTAINER.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageOwnershipJobFacetOwner{" +
                       "name='" + name + '\'' +
                       ", type='" + type + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        OpenLineageOwnershipJobFacetOwner that = (OpenLineageOwnershipJobFacetOwner) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(type, that.type);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, type);
    }
}
