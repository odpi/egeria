/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementControlHeader bean provides details of the origin and changes associated with the element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ElementClassificationHeader.class, name = "ElementClassificationHeader"),
                @JsonSubTypes.Type(value = ElementHeader.class, name = "ElementHeader"),
        })
public class ElementControlHeader extends PropertyBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * Common header for first class elements from a metadata repository
     */
    private ElementStatus   status   = null;
    private ElementType     type     = null;
    private ElementOrigin   origin   = null;
    private ElementVersions versions = null;


    /**
     * Default constructor used by subclasses
     */
    public ElementControlHeader()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementControlHeader(ElementControlHeader template)
    {
        super(template);

        if (template != null)
        {
            status           = template.getStatus();
            type             = template.getType();
            origin           = template.getOrigin();
            versions         = template.getVersions();
        }
    }


    /**
     * Return the current status of the element - typically ACTIVE.
     *
     * @return status enum
     */
    public ElementStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the current status of the element - typically ACTIVE.
     *
     * @param status status enum
     */
    public void setStatus(ElementStatus status)
    {
        this.status = status;
    }


    /**
     * Return the element type properties for this properties object.  These values are set up by the metadata repository
     * and define details to the metadata entity used to represent this element.
     *
     * @return ElementType type information.
     */
    public ElementType getType()
    {
        if (type == null)
        {
            return null;
        }
        else
        {
            return type;
        }
    }


    /**
     * Set up the type of this element.
     *
     * @param type element type properties
     */
    public void setType(ElementType type)
    {
        this.type = type;
    }


    /**
     * Return information about the origin of the element. This includes the metadata collection and license.
     *
     * @return element origin object
     */
    public ElementOrigin getOrigin()
    {
        return origin;
    }


    /**
     * Set up information about the origin of the element. This includes the metadata collection and license.
     *
     * @param origin element origin object
     */
    public void setOrigin(ElementOrigin origin)
    {
        this.origin = origin;
    }


    /**
     * Return detail of the element's current version and the users responsible for maintaining it.
     *
     * @return ElementVersion object
     */
    public ElementVersions getVersions()
    {
        return versions;
    }


    /**
     * Set up detail of the element's current version and the users responsible for maintaining it.
     *
     * @param versions ElementVersion object
     */
    public void setVersions(ElementVersions versions)
    {
        this.versions = versions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementControlHeader{" +
                       "status=" + status +
                       ", type=" + type +
                       ", origin=" + origin +
                       ", versions=" + versions +
                       ", headerVersion=" + getHeaderVersion() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ElementControlHeader that = (ElementControlHeader) objectToCompare;
        return status == that.status && Objects.equals(type, that.type)
                       && Objects.equals(origin, that.origin) &&
                       Objects.equals(versions, that.versions);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(status, type, origin, versions);
    }
}
