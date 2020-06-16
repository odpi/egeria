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
 * The ElementType bean provides details of the type information associated with a metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementOrigin implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String                sourceServer               = null;
    private ElementOriginCategory originCategory             = ElementOriginCategory.CONFIGURATION;
    private String                homeMetadataCollectionId   = null;
    private String                homeMetadataCollectionName = null;
    private String                license                    = null;


    /**
     * Default constructor
     */
    public ElementOrigin()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param templateType type to clone
     */
    public ElementOrigin(ElementOrigin templateType)
    {
        if (templateType != null)
        {
            sourceServer               = templateType.getSourceServer();
            originCategory             = templateType.getOriginCategory();
            homeMetadataCollectionId   = templateType.getHomeMetadataCollectionId();
            homeMetadataCollectionName = templateType.getHomeMetadataCollectionName();
            license                    = templateType.getLicense();
        }
    }


    /**
     * Set up the name of the server where the element was retrieved from.  Typically this is
     * a server where the OMAS interfaces are activated.  If no name is known for the server then null is returned.
     *
     * @param sourceServer URL of the server
     */
    public void setSourceServer(String sourceServer)
    {
        this.sourceServer = sourceServer;
    }


    /**
     * Return the name of the server where the element was retrieved from.  Typically this is
     * a server where the OMAS interfaces are activated.  If no name is known for the server then null is returned.
     *
     * @return elementSourceServerURL the url of the server where the element came from
     */
    public String getSourceServer()
    {
        return sourceServer;
    }


    /**
     * Set up the details of this element's origin.
     *
     * @param originCategory see ElementOriginCategory enum
     */
    public void setOriginCategory(ElementOriginCategory originCategory)
    {
        this.originCategory = originCategory;
    }


    /**
     * Return the origin of the metadata element.
     *
     * @return ElementOriginCategory enum
     */
    public ElementOriginCategory getOriginCategory() { return originCategory; }


    /**
     * Returns the OMRS identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @return String metadata collection id
     */
    public String getHomeMetadataCollectionId()
    {
        return homeMetadataCollectionId;
    }


    /**
     * Set up the unique identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @param homeMetadataCollectionId String unique identifier for the home metadata repository
     */
    public void setHomeMetadataCollectionId(String homeMetadataCollectionId)
    {
        this.homeMetadataCollectionId = homeMetadataCollectionId;
    }


    /**
     * Return the name of the metadata collection that this asset belongs to.
     *
     * @return name string
     */
    public String getHomeMetadataCollectionName()
    {
        return homeMetadataCollectionName;
    }


    /**
     * Set up the name of the metadata collection that this asset belongs to.
     *
     * @param homeMetadataCollectionName name string
     */
    public void setHomeMetadataCollectionName(String homeMetadataCollectionName)
    {
        this.homeMetadataCollectionName = homeMetadataCollectionName;
    }


    /**
     * Return the license associated with this metadata element (null means none).
     *
     * @return string license name
     */
    public String getLicense()
    {
        return license;
    }


    /**
     * Set up the license associated with this metadata element (null means none)
     *
     * @param license string license name
     */
    public void setLicense(String license)
    {
        this.license = license;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementOrigin{" +
                "sourceServer='" + sourceServer + '\'' +
                ", originCategory=" + originCategory +
                ", homeMetadataCollectionId='" + homeMetadataCollectionId + '\'' +
                ", homeMetadataCollectionName='" + homeMetadataCollectionName + '\'' +
                ", license='" + license + '\'' +
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
        if (!(objectToCompare instanceof ElementOrigin))
        {
            return false;
        }
        ElementOrigin that = (ElementOrigin) objectToCompare;
        return Objects.equals(getSourceServer(), that.getSourceServer()) &&
                getOriginCategory() == that.getOriginCategory() &&
                Objects.equals(getHomeMetadataCollectionId(), that.getHomeMetadataCollectionId()) &&
                Objects.equals(getHomeMetadataCollectionName(), that.getHomeMetadataCollectionName()) &&
                Objects.equals(getLicense(), that.getLicense());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getSourceServer(), getOriginCategory(),
                            getHomeMetadataCollectionId(), getHomeMetadataCollectionName(),
                            getLicense());
    }
}
