/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceProperties stores information about an link to an external resource that is relevant to an Asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceProperties
{
    private String              linkId               = null;
    private String              linkDescription      = null;
    private String              resourceId           = null;
    private String              resourceDisplayName  = null;
    private String              resourceDescription  = null;
    private String              resourceURL          = null;
    private String              resourceVersion      = null;
    private String              owningOrganization   = null;
    private String              typeName             = null;
    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public ExternalReferenceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ExternalReferenceProperties(ExternalReferenceProperties template)
    {
        if (template != null)
        {
            linkId               = template.getLinkId();
            linkDescription      = template.getLinkDescription();
            resourceId           = template.getResourceId();
            resourceDisplayName  = template.getResourceDisplayName();
            resourceURL          = template.getResourceURL();
            resourceDescription  = template.getResourceDescription();
            resourceVersion      = template.getResourceVersion();
            owningOrganization   = template.getOwningOrganization();
            typeName             = template.getTypeName();
            extendedProperties   = template.getExtendedProperties();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the identifier given to this reference (with respect to this element).
     *
     * @return linkId
     */
    public String getLinkId()
    {
        return linkId;
    }


    /**
     * Set up the identifier given to this reference (with respect to this element).
     *
     * @param linkId String name
     */
    public void setLinkId(String linkId)
    {
        this.linkId = linkId;
    }


    /**
     * Return the description of the reference (with respect to the element that this reference is linked to).
     *
     * @return String link description.
     */
    public String getLinkDescription() { return linkDescription; }


    /**
     * Set up the description of the reference (with respect to the element that this reference is linked to).
     *
     * @param linkDescription String description
     */
    public void setLinkDescription(String linkDescription)
    {
        this.linkDescription = linkDescription;
    }


    /**
     * Return the fully qualified name.
     *
     * @return String resourceId
     */
    public String getResourceId() { return resourceId; }


    /**
     * Set up the fully qualified name.
     *
     * @param resourceId String identifier
     */
    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }


    /**
     * Return the display name of this external reference.
     *
     * @return String display name.
     */
    public String getResourceDisplayName() { return resourceDisplayName; }


    /**
     * Set up the display name of this external reference.
     *
     * @param name - string name
     */
    public void setResourceDisplayName(String name)
    {
        this.resourceDisplayName = name;
    }



    /**
     * Return the description of the resource that this external reference represents.
     *
     * @return String description
     */
    public String getResourceDescription() { return resourceDescription; }


    /**
     * Set up the description of the resource that this external reference represents.
     *
     * @param resourceDescription String description
     */
    public void setResourceDescription(String resourceDescription)
    {
        this.resourceDescription = resourceDescription;
    }


    /**
     * Return the URL used to retrieve the resource that this external reference represents.
     *
     * @return String URL
     */
    public String getResourceURL() { return resourceURL; }


    /**
     * Set up the URL used to retrieve the resource that this external reference represents.
     *
     * @param url String URL
     */
    public void setResourceURL(String url)
    {
        this.resourceURL = url;
    }



    /**
     * Return the version of the resource that this external reference represents.
     *
     * @return String version identifier
     */
    public String getResourceVersion() { return resourceVersion; }


    /**
     * Set up the version of the resource that this external reference represents.
     *
     * @param version String identifier
     */
    public void setResourceVersion(String version)
    {
        this.resourceVersion = version;
    }


    /**
     * Return the name of the organization that owns the resource that this external reference represents.
     *
     * @return String organization name
     */
    public String getOwningOrganization() { return owningOrganization; }


    /**
     * Set up the name of the organization that owns the resource that this external reference represents.
     *
     * @param owningOrganization String name
     */
    public void setOwningOrganization(String owningOrganization)
    {
        this.owningOrganization = owningOrganization;
    }


    /**
     * Return the type name of the external reference = null for default.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the type name of the external reference = null for default.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
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
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> additionalProperties)
    {
        this.extendedProperties = additionalProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalReferenceProperties{" +
                       "linkId='" + linkId + '\'' +
                       ", linkDescription='" + linkDescription + '\'' +
                       ", resourceId='" + resourceId + '\'' +
                       ", resourceDisplayName='" + resourceDisplayName + '\'' +
                       ", resourceDescription='" + resourceDescription + '\'' +
                       ", resourceURL='" + resourceURL + '\'' +
                       ", resourceVersion='" + resourceVersion + '\'' +
                       ", owningOrganization='" + owningOrganization + '\'' +
                       ", typeName='" + typeName + '\'' +
                       ", extendedProperties=" + extendedProperties +
                       ", additionalProperties=" + additionalProperties +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ExternalReferenceProperties that = (ExternalReferenceProperties) objectToCompare;
        return Objects.equals(getResourceId(), that.getResourceId()) &&
                Objects.equals(getLinkId(), that.getLinkId()) &&
                Objects.equals(getResourceDisplayName(), that.getResourceDisplayName()) &&
                Objects.equals(getLinkDescription(), that.getLinkDescription()) &&
                Objects.equals(getResourceDescription(), that.getResourceDescription()) &&
                Objects.equals(getResourceURL(), that.getResourceURL()) &&
                Objects.equals(getResourceVersion(), that.getResourceVersion()) &&
                Objects.equals(getOwningOrganization(), that.getOwningOrganization()) &&
                Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }



    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getResourceId(), getLinkId(), getResourceDisplayName(),
                            getLinkDescription(), getResourceDescription(), getResourceURL(), getResourceVersion(), getOwningOrganization(),
                            getTypeName(), getExtendedProperties(), getAdditionalProperties());
    }
}