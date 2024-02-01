/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Element object holds properties that are used for displaying details of an entity.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Element
{
    /**
     * The unique identifier of the asset.
     */
    private String guid;

    /**
     * The type definition of the asset.
     */
    private Type type;

    /**
     * The name of the asset.
     */
    private String name;

    /**
     * The name of the users that created the asset.
     */
    private String createdBy;

    /**
     * The date when the asset has been created.
     */
    private Date createTime;

    /**
     * The name of the user that updated the asset last time.
     */
    private String updatedBy;

    /**
     * The date when the asset has been created.
     */
    private Date updateTime;

    /**
     * The version of the asset.
     */
    private Long version;

    /**
     * The status of the asset.
     */
    private String status;

    /**
     * The URL for retrieving the asset.
     */
    private String url;

    /**
     * The properties of the element.
     */
    private Map<String, String> properties;

    /**
     * The additional properties of the element.
     */
    private Map<String, String> additionalProperties;

    /**
     * The classifications of the element.
     */
    private List<Classification> classifications;

    /**
     * The anchor of the element.
     */
    private Element anchorElement;

    /**
     * The origin of the element.
     */
    private ElementOrigin origin;


    /**
     * Default constructor.
     */
    public Element()
    {
    }


    /**
     * Return the asset unique identifier.
     *
     * @return String - unique identifier of the asset
     */
    public String getGuid()
    {
        return guid;
    }


    /**
     * Set up the unique identifier of the asset.
     *
     * @param guid of the asset
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    /**
     * Returns the type definition of the asset.
     *
     * @return the type definition of the asset
     */
    public Type getType()
    {
        return type;
    }


    /**
     * Set up the type definition of the asset.
     *
     * @param type the type definition of the asset
     */
    public void setType(Type type)
    {
        this.type = type;
    }


    /**
     * Returns the name of the asset.
     *
     * @return the name of the asset
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the asset.
     *
     * @param name - the name of the asset
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the name of the user that created the asset.
     *
     * @return the name of the users that created the asset
    */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the name of the user that created the asset.
     *
     * @param createdBy - the name of the user that created the asset
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Returns the date when the asset has been created.
     *
     * @return date when for the asset creation
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the date when the asset has been created.
     *
     * @param createTime - creation date of the asset
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    /**
     * Return the name of the user that updated the asset last time.
     *
     * @return string - the name of the user that updated the asset last time
     */
    public String getUpdatedBy()
    {
        return updatedBy;
    }


    /**
     * Set up the name of the user that updated the asset last time.
     *
     * @param updatedBy the name of the user that updated the asset last time
     */
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    /**
     * Returns the date when the asset has been created.
     *
     * @return date - the date when the asset has been created
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the date when the asset has been created.
     *
     * @param updateTime - the date when the asset has been created
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * Returns the version of the asset.
     *
     * @return long - the version of the asset
     */
    public Long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the asset.
     *
     * @param version -  the version of the asset
     */
    public void setVersion(Long version)
    {
        this.version = version;
    }


    /**
     * Returns the status of the asset.
     *
     * @return status
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the asset.
     *
     * @param status - enum that describes the asset's status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Returns the URL.
     *
     * @return the URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the URL.
     *
     * @param url the URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Returns the properties of the element.
     *
     * @return the properties of the element
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the element.
     *
     * @param properties the properties of the element
     */
    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }


    /**
     * Returns the additional properties of the element.
     *
     * @return the additional properties of the element
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties of the element.
     *
     * @param additionalProperties the additional properties of the element
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Returns the classifications of the element.
     *
     * @return the classifications of the element
     */
    public List<Classification> getClassifications()
    {
        return classifications;
    }


    /**
     * Set up the classifications of the element.
     *
     * @param classifications the classifications of the element
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Returns the anchor of the element.
     *
     * @return the anchor of the element
     */
    public Element getAnchorElement()
    {
        return anchorElement;
    }


    /**
     * Set up the anchor of the element.
     *
     * @param anchorElement the anchor of the element
     */
    public void setAnchorElement(Element anchorElement)
    {
        this.anchorElement = anchorElement;
    }


    /**
     * Returns the anchor of the element.
     *
     * @return the anchor of the element
     */
    @Deprecated
    public Element getParentElement()
    {
        return anchorElement;
    }


    /**
     * Set up the anchor of the element.
     *
     * @param anchorElement the anchor of the element
     */
    @Deprecated
    public void setParentElement(Element anchorElement)
    {
        this.anchorElement = anchorElement;
    }

    /**
     * Returns the origin of the element.
     *
     * @return the origin of the element
     */
    public ElementOrigin getOrigin()
    {
        return origin;
    }


    /**
     * Set up the origin of the element.
     *
     * @param origin the origin of the element
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
        return "Element{" +
                       "guid='" + guid + '\'' +
                       ", type=" + type +
                       ", name='" + name + '\'' +
                       ", createdBy='" + createdBy + '\'' +
                       ", createTime=" + createTime +
                       ", updatedBy='" + updatedBy + '\'' +
                       ", updateTime=" + updateTime +
                       ", version=" + version +
                       ", status='" + status + '\'' +
                       ", url='" + url + '\'' +
                       ", properties=" + properties +
                       ", additionalProperties=" + additionalProperties +
                       ", classifications=" + classifications +
                       ", parentElement=" + anchorElement +
                       ", origin=" + origin +
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
        if (! (objectToCompare instanceof Element element))
        {
            return false;
        }
        return Objects.equals(guid, element.guid) && Objects.equals(type, element.type) && Objects.equals(name,
                                                                                                          element.name) && Objects.equals(
                createdBy, element.createdBy) && Objects.equals(createTime, element.createTime) && Objects.equals(updatedBy,
                                                                                                                  element.updatedBy) && Objects.equals(
                updateTime, element.updateTime) && Objects.equals(version, element.version) && Objects.equals(status,
                                                                                                              element.status) && Objects.equals(
                url, element.url) && Objects.equals(properties, element.properties) && Objects.equals(additionalProperties,
                                                                                                      element.additionalProperties) && Objects.equals(
                classifications, element.classifications) && Objects.equals(anchorElement, element.anchorElement) && Objects.equals(
                origin, element.origin);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, type, name, createdBy, createTime, updatedBy, updateTime, version, status, url, properties, additionalProperties,
                            classifications, anchorElement, origin);
    }
}
