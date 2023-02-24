/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceProperties stores information about an link to an external resource that is relevant to this element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceProperties extends ReferenceableProperties
{
    private static final long   serialVersionUID     = 1L;

    private String              localReferenceId     = null;
    private String              displayName          = null;
    private String              linkDescription      = null;
    private String              resourceDescription  = null;
    private String              uri                  = null;
    private String              version              = null;
    private String              organization         = null;


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
        super(template);

        if (template != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            localReferenceId     = template.getLocalReferenceId();
            linkDescription      = template.getLinkDescription();
            displayName          = template.getDisplayName();
            uri                  = template.getURI();
            resourceDescription  = template.getResourceDescription();
            version              = template.getVersion();
            organization         = template.getOrganization();
        }
    }


    /**
     * Return the identifier given to this reference (with respect to this governance definition).
     *
     * @return localReferenceId
     */
    public String getLocalReferenceId()
    {
        return localReferenceId;
    }


    /**
     * Set up the identifier given to this reference (with respect to this governance definition).
     *
     * @param localReferenceId String name
     */
    public void setLocalReferenceId(String localReferenceId)
    {
        this.localReferenceId = localReferenceId;
    }


    /**
     * Return the display name of this external reference.
     *
     * @return String display name.
     */
    public String getDisplayName() { return displayName; }


    /**
     * Set up the display name of this external reference.
     *
     * @param displayName - string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the reference (with respect to this governance definition).
     *
     * @return String link description.
     */
    public String getLinkDescription() { return linkDescription; }


    /**
     * Set up the description of the reference (with respect to the governance definition this reference is linked to).
     *
     * @param linkDescription String description
     */
    public void setLinkDescription(String linkDescription)
    {
        this.linkDescription = linkDescription;
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
     * Return the URI used to retrieve the resource that this external reference represents.
     *
     * @return String URI
     */
    public String getURI() { return uri; }


    /**
     * Set up the URI used to retrieve the resource that this external reference represents.
     *
     * @param uri String URI
     */
    public void setURI(String uri)
    {
        this.uri = uri;
    }



    /**
     * Return the version of the resource that this external reference represents.
     *
     * @return String version identifier
     */
    public String getVersion() { return version; }


    /**
     * Set up the version of the resource that this external reference represents.
     *
     * @param version String identifier
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the name of the organization that owns the resource that this external reference represents.
     *
     * @return String organization name
     */
    public String getOrganization() { return organization; }


    /**
     * Set up the name of the organization that owns the resource that this external reference represents.
     *
     * @param organization String name
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
                       "localReferenceId='" + localReferenceId + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", linkDescription='" + linkDescription + '\'' +
                       ", resourceDescription='" + resourceDescription + '\'' +
                       ", uri='" + uri + '\'' +
                       ", version='" + version + '\'' +
                       ", organization='" + organization + '\'' +
                       ", URI='" + getURI() + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        return Objects.equals(localReferenceId, that.localReferenceId) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(linkDescription, that.linkDescription) &&
                       Objects.equals(resourceDescription, that.resourceDescription) &&
                       Objects.equals(uri, that.uri) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(organization, that.organization);
    }


    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(localReferenceId, displayName, linkDescription, resourceDescription, uri, version, organization);
    }
}