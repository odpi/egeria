/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementHeader provides the common identifier and type information for all properties objects
 * that link off of the asset and have a guid associated with them.  This typically means it is
 * represented by an entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementHeader implements Serializable
{
    private static final long     serialVersionUID = 1L;

    /*
     * Common header for first class elements from a metadata repository
     */
    private String        guid = null;
    private ElementType   type = null;
    private ElementOrigin origin = null;


    /**
     * Default constructor used by subclasses
     */
    public ElementHeader()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementHeader(ElementHeader template)
    {
        if (template != null)
        {
            guid   = template.getGUID();
            type   = template.getType();
            origin = template.getOrigin();
        }
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
     * Return the unique id for the properties object.  Null means no guid is assigned.
     *
     * @return String unique id
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the guid for the element.
     *
     * @param guid String unique identifier
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementHeader{" +
                "type=" + type +
                ", origin=" + origin +
                ", guid='" + getGUID() + '\'' +
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
        if (!(objectToCompare instanceof ElementHeader))
        {
            return false;
        }
        ElementHeader that = (ElementHeader) objectToCompare;
        return Objects.equals(getType(), that.getType()) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(getOrigin(), that.getOrigin());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, type, origin);
    }
}