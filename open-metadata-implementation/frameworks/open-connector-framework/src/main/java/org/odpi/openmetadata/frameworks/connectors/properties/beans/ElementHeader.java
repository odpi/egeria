/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Referenceable.class, name = "Referenceable"),
                @JsonSubTypes.Type(value = InformalTag.class, name = "InformalTag"),
                @JsonSubTypes.Type(value = Like.class, name = "Like"),
                @JsonSubTypes.Type(value = Meaning.class, name = "Meaning"),
                @JsonSubTypes.Type(value = Rating.class, name = "Rating")
        })
public class ElementHeader extends PropertyBase
{
    private static final long     serialVersionUID = 1L;

    /*
     * Common header for first class elements from a metadata repository
     */
    protected ElementType type = null;
    protected String      guid = null;
    protected String      url  = null;

    protected List<ElementClassification> classifications    = null;
    protected Map<String, Object>         extendedProperties = null;

    /**
     * Default constructor used by subclasses
     */
    public ElementHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementHeader(ElementHeader template)
    {
        super(template);

        if (template != null)
        {
            type               = template.getType();
            guid               = template.getGUID();
            url                = template.getURL();
            classifications    = template.getClassifications();
            extendedProperties = template.getExtendedProperties();
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
     * Returns the URL to access the properties object in the metadata repository.
     * If no url is available then null is returned.
     *
     * @return String URL
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Set up the URL of this element.
     *
     * @param url String
     */
    public void setURL(String url)
    {
        this.url = url;
    }


    /**
     * Return the list of classifications associated with the asset.   This is an  list and the
     * pointers are set to the start of the list of classifications
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
     * Set up the classifications associated with this connection.
     *
     * @param classifications list of classifications
     */
    public void setClassifications(List<ElementClassification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
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
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties +
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
                Objects.equals(url, that.url) &&
                Objects.equals(getClassifications(), that.getClassifications()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid);
    }
}