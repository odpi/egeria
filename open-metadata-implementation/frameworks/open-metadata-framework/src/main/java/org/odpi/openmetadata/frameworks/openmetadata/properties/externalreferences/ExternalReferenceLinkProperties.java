/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceLinkProperties provides a structure for the properties that link an external reference to an object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceLinkProperties extends RelationshipProperties
{
    private String linkId          = null;
    private String linkDescription = null;
    private String pages           = null;


    /**
     * Default constructor
     */
    public ExternalReferenceLinkProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExternalReferenceLinkProperties(ExternalReferenceLinkProperties template)
    {
        super (template);

        if (template != null)
        {
            this.linkId = template.getLinkId();
            this.linkDescription = template.getLinkDescription();
            this.pages = template.getPages();
        }
    }


    /**
     * Return the identifier that this reference is to be known as with respect to the linked object.
     *
     * @return String identifier
     */
    public String getLinkId()
    {
        return linkId;
    }


    /**
     * Set up the identifier that this reference is to be known as with respect to the linked object.
     *
     * @param linkId String identifier
     */
    public void setLinkId(String linkId)
    {
        this.linkId = linkId;
    }


    /**
     * Return the description of the external reference with respect to the linked object.
     *
     * @return string
     */
    public String getLinkDescription()
    {
        return linkDescription;
    }


    /**
     * Set up the description of the external reference with respect to the linked object.
     *
     * @param linkDescription string
     */
    public void setLinkDescription(String linkDescription)
    {
        this.linkDescription = linkDescription;
    }


    /**
     * Return the page range for the reference.
     *
     * @return string
     */
    public String getPages()
    {
        return pages;
    }


    /**
     * Set up the page range for the reference.
     *
     * @param pages string
     */
    public void setPages(String pages)
    {
        this.pages = pages;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ExternalReferenceLinkProperties{" +
                       "linkId='" + linkId + '\'' +
                       ", linkDescription='" + linkDescription + '\'' +
                       ", pages='" + pages + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof ExternalReferenceLinkProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ExternalReferenceLinkProperties that = (ExternalReferenceLinkProperties) objectToCompare;
        return Objects.equals(linkId, that.linkId) && Objects.equals(linkDescription, that.linkDescription)
                       && Objects.equals(pages, that.pages);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), linkId, linkDescription, pages);
    }
}
