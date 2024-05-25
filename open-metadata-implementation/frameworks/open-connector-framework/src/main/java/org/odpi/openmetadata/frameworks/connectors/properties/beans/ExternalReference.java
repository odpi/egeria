/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReference stores information about an link to an external resource that is relevant to this asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReference extends Referenceable
{
    /*
     * Attributes of an external reference
     */
    protected String referenceId         = null;
    protected String linkDescription     = null;
    protected String displayName         = null;
    protected String uri                 = null;
    protected String resourceDescription = null;
    protected String version             = null;
    protected String organization        = null;


    /**
     * Default constructor
     */
    public ExternalReference()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateExternalReference element to copy
     */
    public ExternalReference(ExternalReference templateExternalReference)
    {
        /*
         * Initialize the super class.
         */
        super(templateExternalReference);

        if (templateExternalReference != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            referenceId = templateExternalReference.getReferenceId();
            linkDescription = templateExternalReference.getLinkDescription();
            displayName = templateExternalReference.getDisplayName();
            uri = templateExternalReference.getURI();
            resourceDescription = templateExternalReference.getResourceDescription();
            version = templateExternalReference.getVersion();
            organization = templateExternalReference.getOrganization();
        }
    }


    /**
     * Return the identifier given to this reference (with respect to this asset).
     *
     * @return String referenceId
     */
    public String getReferenceId() { return referenceId; }


    /**
     * Set up the identifier given to this reference (with respect to this asset).
     *
     * @param referenceId String identifier
     */
    public void setReferenceId(String referenceId)
    {
        this.referenceId = referenceId;
    }


    /**
     * Return the description of the reference (with respect to this asset).
     *
     * @return String link description.
     */
    public String getLinkDescription() { return linkDescription; }


    /**
     * Set up the description of the reference (with respect to the element this reference is linked to).
     *
     * @param linkDescription String description
     */
    public void setLinkDescription(String linkDescription)
    {
        this.linkDescription = linkDescription;
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
     * Return the description of the resource that this external reference represents.
     *
     * @return String resource description
     */
    public String getResourceDescription() { return resourceDescription; }


    /**
     * Set up the description of the resource that this external reference represents.
     *
     * @param resourceDescription String resource description
     */
    public void setResourceDescription(String resourceDescription)
    {
        this.resourceDescription = resourceDescription;
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
        return "ExternalReference{" +
                       "URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", referenceId='" + referenceId + '\'' +
                       ", linkDescription='" + linkDescription + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", uri='" + uri + '\'' +
                       ", resourceDescription='" + resourceDescription + '\'' +
                       ", version='" + version + '\'' +
                       ", organization='" + organization + '\'' +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        ExternalReference that = (ExternalReference) objectToCompare;
        return Objects.equals(getReferenceId(), that.getReferenceId()) &&
                       Objects.equals(getLinkDescription(), that.getLinkDescription()) &&
                       Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(uri, that.uri) &&
                       Objects.equals(getResourceDescription(), that.getResourceDescription()) &&
                       Objects.equals(getVersion(), that.getVersion()) &&
                       Objects.equals(getOrganization(), that.getOrganization());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getReferenceId(), getLinkDescription(), getDisplayName(), uri, getResourceDescription(), getVersion(),
                            getOrganization());
    }
}