/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReference stores information about an link to an external resource that is relevant to a profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReference extends ReferenceableHeader
{
    private String              externalReferenceId  = null;
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
    public ExternalReference()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ExternalReference(ExternalReference template)
    {
        if (template != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            localReferenceId     = template.getLocalReferenceId();
            externalReferenceId  = template.getExternalReferenceId();
            linkDescription      = template.getLinkDescription();
            displayName          = template.getDisplayName();
            uri                  = template.getURI();
            resourceDescription  = template.getResourceDescription();
            version              = template.getVersion();
            organization         = template.getOrganization();
        }
    }


    /**
     * Return the fully qualified name.
     *
     * @return String externalReferenceId
     */
    public String getExternalReferenceId() { return externalReferenceId; }


    /**
     * Set up the fully qualified name.
     *
     * @param externalReferenceId String identifier
     */
    public void setExternalReferenceId(String externalReferenceId)
    {
        this.externalReferenceId = externalReferenceId;
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
        return "ExternalReference{" +
                "externalReferenceId='" + externalReferenceId + '\'' +
                ", localReferenceId='" + localReferenceId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", linkDescription='" + linkDescription + '\'' +
                ", resourceDescription='" + resourceDescription + '\'' +
                ", uri='" + uri + '\'' +
                ", version='" + version + '\'' +
                ", organization='" + organization + '\'' +
                ", URI='" + getURI() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", classifications=" + getClassifications() +
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
        return Objects.equals(getExternalReferenceId(), that.getExternalReferenceId()) &&
                Objects.equals(getLocalReferenceId(), that.getLocalReferenceId()) &&
                Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getLinkDescription(), that.getLinkDescription()) &&
                Objects.equals(getResourceDescription(), that.getResourceDescription()) &&
                Objects.equals(uri, that.uri) &&
                Objects.equals(getVersion(), that.getVersion()) &&
                Objects.equals(getOrganization(), that.getOrganization());
    }


    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getExternalReferenceId(), getLocalReferenceId(), getDisplayName(),
                            getLinkDescription(), getResourceDescription(), uri, getVersion(), getOrganization());
    }
}