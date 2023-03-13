/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceProperties describes the properties of URL link to a remote source of information.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ExternalGlossaryLinkProperties.class, name = "ExternalGlossaryLinkProperties")
        })
public class ExternalReferenceProperties extends ReferenceableProperties
{
    private static final long     serialVersionUID = 1L;

    private String displayName  = null;
    private String url          = null;
    private String version      = null;
    private String description  = null;
    private String organization = null;


    /**
     * Default constructor
     */
    public ExternalReferenceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template object to copy.
     */
    public ExternalReferenceProperties(ExternalReferenceProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            url = template.getUrl();
            version = template.getVersion();
            description = template.getDescription();
            organization = template.getOrganization();
        }
    }


    /**
     * Set up name of the external reference.
     *
     * @param name String
     */
    public void setDisplayName(String name)
    {
        this.displayName = name;
    }


    /**
     * Return the name for the external reference.
     *
     * @return String identifier
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the URL for the external reference.
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the URL for the external reference.
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the version of the reference that this link refers to.
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the reference that this link refers to.
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the description of the external reference to help the reader know if they want to click on the link.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the external reference to help the reader know if they want to click on the link.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the name or identifier or url of the organization that owns the external resource.
     *
     * @return string identifier
     */
    public String getOrganization()
    {
        return organization;
    }


    /**
     * Set up the name or identifier or url of the organization that owns the external resource.
     *
     * @param organization string identifier
     */
    public void setOrganization(String organization)
    {
        this.organization = organization;
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
                       "displayName='" + displayName + '\'' +
                       ", url='" + url + '\'' +
                       ", version='" + version + '\'' +
                       ", description='" + description + '\'' +
                       ", organization='" + organization + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getUrl(), that.getUrl()) &&
                       Objects.equals(getVersion(), that.getVersion()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getOrganization(), that.getOrganization());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getUrl(), getVersion(), getDescription(), getOrganization());
    }
}
