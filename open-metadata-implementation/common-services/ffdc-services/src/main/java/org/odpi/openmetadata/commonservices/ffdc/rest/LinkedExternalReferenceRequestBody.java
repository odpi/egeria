/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import java.util.Objects;

public class LinkedExternalReferenceRequestBody extends ExternalReferenceRequestBody
{
    private String linkId          = null;
    private String linkDescription = null;


    /**
     * Default constructor
     */
    public LinkedExternalReferenceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public LinkedExternalReferenceRequestBody(LinkedExternalReferenceRequestBody template)
    {
        if (template != null)
        {
            linkId = template.getLinkId();
            linkDescription = template.getLinkDescription();
        }
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
        return "LinkedExternalReferenceRequestBody{" +
                "linkId='" + linkId + '\'' +
                ", linkDescription='" + linkDescription + '\'' +
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
        LinkedExternalReferenceRequestBody that = (LinkedExternalReferenceRequestBody) objectToCompare;
        return Objects.equals(linkId, that.linkId) &&
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
        return Objects.hash(super.hashCode(), linkId, linkDescription);
    }
}
