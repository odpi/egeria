/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementType bean provides details of the type information associated with a metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementType extends PropertyBase
{
    protected String        elementTypeId                   = null;
    protected String        elementTypeName                 = null;
    protected long          elementTypeVersion              = 0;
    protected String        elementTypeDescription          = null;
    protected String        elementSourceServer             = null;
    protected ElementOrigin elementOrigin                   = ElementOrigin.CONFIGURATION;;
    protected String        elementHomeMetadataCollectionId = null;
    protected String        elementLicense                  = null;


    /**
     * Default constructor
     */
    public ElementType()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param templateType type to clone
     */
    public ElementType(ElementType templateType)
    {
        super(templateType);

        if (templateType != null)
        {
            elementTypeId = templateType.getElementTypeId();
            elementTypeName = templateType.getElementTypeName();
            elementTypeVersion = templateType.getElementTypeVersion();
            elementTypeDescription = templateType.getElementTypeDescription();
            elementSourceServer = templateType.getElementSourceServer();
            elementOrigin = templateType.getElementOrigin();
            elementHomeMetadataCollectionId = templateType.getElementHomeMetadataCollectionId();
            elementLicense = templateType.getElementLicense();
        }
    }


    /**
     * Set up the unique identifier for the element's type.
     *
     * @param elementTypeId String identifier
     */
    public void setElementTypeId(String elementTypeId)
    {
        this.elementTypeId = elementTypeId;
    }


    /**
     * Return unique identifier for the element's type.
     *
     * @return element type id
     */
    public String getElementTypeId()
    {
        return elementTypeId;
    }


    /**
     * Set up the name of this element's type
     *
     * @param elementTypeName String name
     */
    public void setElementTypeName(String elementTypeName)
    {
        this.elementTypeName = elementTypeName;
    }


    /**
     * Return name of element's type.
     *
     * @return elementTypeName
     */
    public String getElementTypeName()
    {
        return elementTypeName;
    }


    /**
     * Set up the version number for this element's type
     *
     * @param elementTypeVersion version number for the element type.
     */
    public void setElementTypeVersion(long elementTypeVersion)
    {
        this.elementTypeVersion = elementTypeVersion;
    }


    /**
     * Return the version number for this element's type.
     *
     * @return elementTypeVersion version number for the element type.
     */
    public long getElementTypeVersion()
    {
        return elementTypeVersion;
    }


    /**
     *
     * @param elementTypeDescription set up the description for this element's type
     */
    public void setElementTypeDescription(String elementTypeDescription)
    {
        this.elementTypeDescription = elementTypeDescription;
    }


    /**
     * Return the description for this element's type.
     *
     * @return elementTypeDescription String description for the element type
     */
    public String getElementTypeDescription()
    {
        return elementTypeDescription;
    }


    /**
     * the URL of the server where the element was retrieved from.  Typically this is
     * a server where the OMAS interfaces are activated.  If no URL is known for the server then null is returned.
     *
     * @param elementSourceServer URL of the server
     */
    public void setElementSourceServer(String elementSourceServer)
    {
        this.elementSourceServer = elementSourceServer;
    }



    /**
     * Return the URL of the server where the element was retrieved from.  Typically this is
     * a server where the OMAS interfaces are activated.  If no URL is known for the server then null is returned.
     *
     * @return elementSourceServerURL the url of the server where the element came from
     */
    public String getElementSourceServer()
    {
        return elementSourceServer;
    }


    /**
     * Set up the details of this element's origin.
     *
     * @param elementOrigin see ElementOrigin enum
     */
    public void setElementOrigin(ElementOrigin elementOrigin)
    {
        this.elementOrigin = elementOrigin;
    }


    /**
     * Return the origin of the metadata element.
     *
     * @return ElementOrigin enum
     */
    public ElementOrigin getElementOrigin() { return elementOrigin; }


    /**
     * Returns the OMRS identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @return String metadata collection id
     */
    public String getElementHomeMetadataCollectionId()
    {
        return elementHomeMetadataCollectionId;
    }


    /**
     * Set up the OMRS identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @param elementHomeMetadataCollectionId String unique identifier for the home metadata repository
     */
    public void setElementHomeMetadataCollectionId(String elementHomeMetadataCollectionId)
    {
        this.elementHomeMetadataCollectionId = elementHomeMetadataCollectionId;
    }


    /**
     * Return the license associated with this metadata element (null means none).
     *
     * @return string license name
     */
    public String getElementLicense()
    {
        return elementLicense;
    }


    /**
     * Set up the license associated with this metadata element (null means none)
     *
     * @param elementLicense string license name
     */
    public void setElementLicense(String elementLicense)
    {
        this.elementLicense = elementLicense;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementType{" +
                "elementTypeId='" + elementTypeId + '\'' +
                ", elementTypeName='" + elementTypeName + '\'' +
                ", elementTypeVersion=" + elementTypeVersion +
                ", elementTypeDescription='" + elementTypeDescription + '\'' +
                ", elementSourceServer='" + elementSourceServer + '\'' +
                ", elementOrigin=" + elementOrigin +
                ", elementHomeMetadataCollectionId='" + elementHomeMetadataCollectionId + '\'' +
                ", elementLicense='" + elementLicense + '\'' +
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
        if (!(objectToCompare instanceof ElementType))
        {
            return false;
        }
        ElementType that = (ElementType) objectToCompare;
        return getElementTypeVersion() == that.getElementTypeVersion() &&
                Objects.equals(getElementTypeId(), that.getElementTypeId()) &&
                Objects.equals(getElementTypeName(), that.getElementTypeName()) &&
                Objects.equals(getElementTypeDescription(), that.getElementTypeDescription()) &&
                Objects.equals(getElementSourceServer(), that.getElementSourceServer()) &&
                getElementOrigin() == that.getElementOrigin() &&
                Objects.equals(getElementHomeMetadataCollectionId(), that.getElementHomeMetadataCollectionId()) &&
                Objects.equals(getElementLicense(), that.getElementLicense());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getElementTypeId(), getElementHomeMetadataCollectionId());
    }
}
