/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementBase provides the common identifier and type information for all properties objects
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
public class ElementBase extends ElementHeader
{
    private static final long     serialVersionUID = 1L;

    /*
     * Common header for first class elements from a metadata repository
     */
    protected String              url  = null;
    protected Map<String, Object> extendedProperties = null;

    /**
     * Default constructor used by subclasses
     */
    public ElementBase()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementBase(ElementBase template)
    {
        super(template);

        if (template != null)
        {
            url                = template.getURL();
            extendedProperties = template.getExtendedProperties();
        }
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
        return "ElementBase{" +
                       "url='" + url + '\'' +
                       ", extendedProperties=" + extendedProperties +
                       ", URL='" + getURL() + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
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
        if (! (objectToCompare instanceof ElementBase))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ElementBase that = (ElementBase) objectToCompare;
        return Objects.equals(url, that.url) && Objects.equals(extendedProperties, that.extendedProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), url, extendedProperties);
    }
}