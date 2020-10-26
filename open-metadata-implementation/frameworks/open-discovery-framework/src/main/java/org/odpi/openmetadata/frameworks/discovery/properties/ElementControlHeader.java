/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
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
                @JsonSubTypes.Type(value = ElementType.class, name = "ElementType")
        })
public class ElementControlHeader implements Serializable
{
    public static final long CURRENT_AUDIT_HEADER_VERSION = 1;

    private static final long     serialVersionUID = 1L;

    /*
     * Version number for this header.  This is used to ensure that all of the critical header information
     * in read in a back-level version of the OCF.  The default is 0 to indicate that the instance came from
     * a version of the OCF that does not have a version number encoded.
     */
    private long headerVersion = 0;


    private String                    elementSourceServer           = null;
    private ElementOrigin             elementOrigin                 = ElementOrigin.CONFIGURATION;
    private String                    elementMetadataCollectionId   = null;
    private String                    elementMetadataCollectionName = null;
    private String                    elementLicense                = null;
    private String                    elementCreatedBy              = null;
    private String                    elementUpdatedBy              = null;
    private List<String>              elementMaintainedBy           = null;
    private Date                      elementCreateTime             = null;
    private Date                      elementUpdateTime             = null;
    private long                      elementVersion                = 0L;
    private ElementStatus             elementStatus                 = null;
    private Map<String, Serializable> mappingProperties             = null;


    /**
     * Default constructor
     */
    public ElementControlHeader()
    {

    }


    /**
     * Copy/clone constructor
     *
     * @param template type to clone
     */
    public ElementControlHeader(ElementControlHeader template)
    {
        if (template != null)
        {
            this.headerVersion                 = template.getHeaderVersion();
            this.elementSourceServer           = template.getElementSourceServer();
            this.elementOrigin                 = template.getElementOrigin();
            this.elementMetadataCollectionId   = template.getElementMetadataCollectionId();
            this.elementMetadataCollectionName = template.getElementMetadataCollectionName();
            this.elementLicense                = template.getElementLicense();
            this.elementCreatedBy              = template.getElementCreatedBy();
            this.elementUpdatedBy              = template.getElementUpdatedBy();
            this.elementMaintainedBy           = template.getElementMaintainedBy();
            this.elementCreateTime             = template.getElementCreateTime();
            this.elementUpdateTime             = template.getElementUpdateTime();
            this.elementVersion                = template.getElementVersion();
            this.elementStatus                 = template.getStatus();
            this.mappingProperties             = template.getMappingProperties();
        }
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all of the header properties.
     *
     * @return long version number - the value is incremented each time a new non-informational field is added
     * to the audit header.
     */
    public long getHeaderVersion()
    {
        return headerVersion;
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all of the header properties.
     *
     * @param headerVersion long version number - the value is incremented each time a new non-informational field is added
     * to the audit header.
     */
    public void setHeaderVersion(long headerVersion)
    {
        this.headerVersion = headerVersion;
    }


    /**
     * Set up the URL of the server where the element was retrieved from.  Typically this is
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
     * Set up the category of this element's origin.
     *
     * @param elementOrigin see ElementOrigin enum
     */
    public void setElementOrigin(ElementOrigin elementOrigin)
    {
        this.elementOrigin = elementOrigin;
    }


    /**
     * Return the origin category of the metadata element.
     *
     * @return ElementOrigin enum
     */
    public ElementOrigin getElementOrigin() { return elementOrigin; }


    /**
     * Returns the unique identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @return String metadata collection id
     */
    public String getElementMetadataCollectionId()
    {
        return elementMetadataCollectionId;
    }


    /**
     * Set up the unique identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @param elementMetadataCollectionId String unique identifier for the home metadata repository
     */
    public void setElementMetadataCollectionId(String elementMetadataCollectionId)
    {
        this.elementMetadataCollectionId = elementMetadataCollectionId;
    }


    /**
     * Return the name of the metadata collection that this element belongs to.
     *
     * @return name string
     */
    public String getElementMetadataCollectionName()
    {
        return elementMetadataCollectionName;
    }


    /**
     * Set up the name of the metadata collection that this asset belongs to.
     *
     * @param elementMetadataCollectionName name string
     */
    public void setElementMetadataCollectionName(String elementMetadataCollectionName)
    {
        this.elementMetadataCollectionName = elementMetadataCollectionName;
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
     * Return the status of this instance.
     *
     * @return InstanceStatus
     */
    public ElementStatus getStatus() { return elementStatus; }


    /**
     * Set up the status of this instance.
     *
     * @param newStatus InstanceStatus
     */
    public void setStatus(ElementStatus newStatus) { this.elementStatus = newStatus; }


    /**
     * Return the name of the user that created this instance.
     *
     * @return String user name
     */
    public String getElementCreatedBy() { return elementCreatedBy; }


    /**
     * Set up the name of the user that created this instance.
     *
     * @param elementCreatedBy String user name
     */
    public void setElementCreatedBy(String elementCreatedBy) { this.elementCreatedBy = elementCreatedBy; }


    /**
     * Return the name of the user that last updated this instance.
     *
     * @return String user name
     */
    public String getElementUpdatedBy() { return elementUpdatedBy; }


    /**
     * Set up the name of the user that last updated this instance.
     *
     * @param elementUpdatedBy String user name
     */
    public void setElementUpdatedBy(String elementUpdatedBy) { this.elementUpdatedBy = elementUpdatedBy; }


    /**
     * Return the list of users responsible for maintaining this instance.
     *
     * @return list of user identifiers
     */
    public List<String> getElementMaintainedBy()
    {
        if (elementMaintainedBy == null)
        {
            return null;
        }
        else if (elementMaintainedBy.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(elementMaintainedBy);
        }
    }


    /**
     * Set up the list of users responsible for maintaining this instance.
     *
     * @param elementMaintainedBy list of user identifiers
     */
    public void setElementMaintainedBy(List<String> elementMaintainedBy)
    {
        this.elementMaintainedBy = elementMaintainedBy;
    }


    /**
     * Return the date/time that this instance was created.
     *
     * @return Date/Time of creation
     */
    public Date getElementCreateTime()
    {
        if (elementCreateTime == null)
        {
            return null;
        }
        else
        {
            return new Date(elementCreateTime.getTime());
        }
    }


    /**
     * Set up the time that this instance was created.
     *
     * @param elementCreateTime Date/Time of creation
     */
    public void setElementCreateTime(Date elementCreateTime) { this.elementCreateTime = elementCreateTime; }


    /**
     * Return what was the late time this instance was updated.
     *
     * @return Date/Time last updated
     */
    public Date getElementUpdateTime()
    {
        if (elementUpdateTime == null)
        {
            return null;
        }
        else
        {
            return new Date(elementUpdateTime.getTime());
        }
    }


    /**
     * Set up the last update time for this instance.
     *
     * @param elementUpdateTime Date/Time last updated
     */
    public void setElementUpdateTime(Date elementUpdateTime) { this.elementUpdateTime = elementUpdateTime; }


    /**
     * Return the version number for this instance.
     *
     * @return Long version number
     */
    public long getElementVersion() { return elementVersion; }


    /**
     * Set up the version number for this instance.
     *
     * @param elementVersion Long version number
     */
    public void setElementVersion(long elementVersion) { this.elementVersion = elementVersion; }




    /**
     * Return the additional properties used by the master repository to map to stored instances.
     *
     * @return property map
     */
    public Map<String, Serializable> getMappingProperties()
    {
        if (mappingProperties == null)
        {
            return null;
        }
        else if (mappingProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(mappingProperties);
        }
    }


    /**
     * Set up the additional properties used by the master repository to map to stored instances.
     *
     * @param mappingProperties property map
     */
    public void setMappingProperties(Map<String, Serializable> mappingProperties)
    {
        this.mappingProperties = mappingProperties;
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
                "elementSourceServer='" + elementSourceServer + '\'' +
                ", elementOrigin=" + elementOrigin +
                ", elementMetadataCollectionId='" + elementMetadataCollectionId + '\'' +
                ", elementMetadataCollectionName='" + elementMetadataCollectionName + '\'' +
                ", elementLicense='" + elementLicense + '\'' +
                ", elementCreatedBy='" + elementCreatedBy + '\'' +
                ", elementUpdatedBy='" + elementUpdatedBy + '\'' +
                ", elementMaintainedBy=" + elementMaintainedBy +
                ", elementCreateTime=" + elementCreateTime +
                ", elementUpdateTime=" + elementUpdateTime +
                ", elementVersion=" + elementVersion +
                ", elementStatus=" + elementStatus +
                ", mappingProperties=" + mappingProperties +
                ", status=" + getStatus() +
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
        ElementControlHeader that = (ElementControlHeader) objectToCompare;
        return elementVersion == that.elementVersion &&
                Objects.equals(elementSourceServer, that.elementSourceServer) &&
                elementOrigin == that.elementOrigin &&
                Objects.equals(elementMetadataCollectionId, that.elementMetadataCollectionId) &&
                Objects.equals(elementMetadataCollectionName, that.elementMetadataCollectionName) &&
                Objects.equals(elementLicense, that.elementLicense) &&
                Objects.equals(elementCreatedBy, that.elementCreatedBy) &&
                Objects.equals(elementUpdatedBy, that.elementUpdatedBy) &&
                Objects.equals(elementMaintainedBy, that.elementMaintainedBy) &&
                Objects.equals(elementCreateTime, that.elementCreateTime) &&
                Objects.equals(elementUpdateTime, that.elementUpdateTime) &&
                elementStatus == that.elementStatus &&
                Objects.equals(mappingProperties, that.mappingProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementSourceServer, elementOrigin, elementMetadataCollectionId, elementMetadataCollectionName, elementLicense,
                            elementCreatedBy, elementUpdatedBy, elementMaintainedBy, elementCreateTime, elementUpdateTime, elementVersion,
                            elementStatus, mappingProperties);
    }
}
