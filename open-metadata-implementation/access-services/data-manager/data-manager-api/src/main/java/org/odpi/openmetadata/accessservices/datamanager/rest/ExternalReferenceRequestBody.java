/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.ExternalReferenceProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ExternalReferenceRequestBody describes the properties of the external reference plus the optional identifiers for an
 * owning element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceRequestBody extends ExternalReferenceProperties
{
    private static final long    serialVersionUID = 1L;

    private String anchorGUID      = null;
    private String linkId          = null;
    private String linkDescription = null;


    /**
     * Default constructor
     */
    public ExternalReferenceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ExternalReferenceRequestBody(ExternalReferenceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            anchorGUID = template.getAnchorGUID();
            linkId = template.getLinkId();
            linkDescription = template.getLinkDescription();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ExternalReferenceRequestBody(ExternalReferenceProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the element that this is to be attached to.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the element that this is to be attached to.
     *
     * @param anchorGUID string guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return the link id.
     *
     * @return string identifier
     */
    public String getLinkId()
    {
        return linkId;
    }


    /**
     * Set up the link id.
     *
     * @param linkId string identifier
     */
    public void setLinkId(String linkId)
    {
        this.linkId = linkId;
    }


    /**
     * Return the link description.
     *
     * @return string description
     */
    public String getLinkDescription()
    {
        return linkDescription;
    }


    /**
     * Set up the link description.
     *
     * @param linkDescription string description
     */
    public void setLinkDescription(String linkDescription)
    {
        this.linkDescription = linkDescription;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ExternalReferenceRequestBody{" +
                       "anchorGUID='" + anchorGUID + '\'' +
                       ", linkId='" + linkId + '\'' +
                       ", linkDescription='" + linkDescription + '\'' +
                       ", resourceId='" + getResourceId() + '\'' +
                       ", resourceDisplayName='" + getResourceDisplayName() + '\'' +
                       ", resourceDescription='" + getResourceDescription() + '\'' +
                       ", resourceURL='" + getResourceURL() + '\'' +
                       ", resourceVersion='" + getResourceVersion() + '\'' +
                       ", owningOrganization='" + getOwningOrganization() + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ExternalReferenceRequestBody that = (ExternalReferenceRequestBody) objectToCompare;
        return Objects.equals(anchorGUID, that.anchorGUID) &&
                       Objects.equals(linkId, that.linkId) &&
                       Objects.equals(linkDescription, that.linkDescription);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchorGUID, linkId, linkDescription);
    }
}
