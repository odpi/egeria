/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.ElementClassification;
import org.odpi.openmetadata.accessservices.assetowner.properties.MeaningProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.SecurityTagsProperties;

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

    private List<ElementClassification> classifications = null;
    private List<MeaningProperties>     meanings        = null;
    private SecurityTagsProperties      securityTags    = null;

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
            guid            = template.getGUID();
            type            = template.getType();
            origin          = template.getOrigin();
            classifications = template.getClassifications();
            meanings        = template.getMeanings();
            securityTags    = template.getSecurityTags();
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
     * Return the assigned meanings for this metadata entity.
     *
     * @return list of meanings
     */
    public List<MeaningProperties> getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else if (meanings.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(meanings);
        }
    }


    /**
     * Set up the assigned meanings for this metadata entity.
     *
     * @param meanings list of meanings
     */
    public void setMeanings(List<MeaningProperties> meanings)
    {
        this.meanings = meanings;
    }


    /**
     * Return the information used by security engines to secure access to the asset's content.
     *
     * @return security labels and properties
     */
    public SecurityTagsProperties getSecurityTags()
    {
        return securityTags;
    }


    /**
     *  Set up the information used by security engines to secure access to the asset's content.
     *
     * @param securityTags security labels and properties
     */
    public void setSecurityTags(SecurityTagsProperties securityTags)
    {
        this.securityTags = securityTags;
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
                ", classifications=" + classifications +
                ", meanings=" + meanings +
                ", securityTags=" + securityTags +
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
                Objects.equals(classifications, that.classifications) &&
                Objects.equals(meanings, that.meanings) &&
                Objects.equals(securityTags, that.securityTags);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, type, origin, classifications, meanings, securityTags);
    }
}