/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementHeader provides the common identifier and type information for all properties objects
 * that link off of the asset and have a guid associated with them.  This typically means it is
 * represented by an entity in the metadata repository.
 *
 * In addition are useful attachments that are found connected to the metadata element.
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
    private String          guid = null;
    private ElementType     type = null;
    private ElementOrigin   origin = null;
    private ElementVersions versions = null;

    private List<ElementClassification> classifications  = null;


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
            guid             = template.getGUID();
            type             = template.getType();
            origin           = template.getOrigin();
            versions         = template.getVersions();
            classifications  = template.getClassifications();
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
     * Return the list of classifications associated with the metadata element.
     *
     * @return Classifications  list of classifications
     */
    public List<ElementClassification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(classifications);
        }
    }


    /**
     * Set up the classifications associated with this metadata element.
     *
     * @param classifications list of classifications
     */
    public void setClassifications(List<ElementClassification> classifications)
    {
        this.classifications = classifications;
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
                       "guid='" + guid + '\'' +
                       ", type=" + type +
                       ", origin=" + origin +
                       ", versions=" + versions +
                       ", classifications=" + classifications +
                       ", GUID='" + getGUID() + '\'' +
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
        ElementHeader that = (ElementHeader) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(origin, that.origin) &&
                       Objects.equals(versions, that.versions) &&
                       Objects.equals(classifications, that.classifications);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, type, origin, versions, classifications);
    }
}