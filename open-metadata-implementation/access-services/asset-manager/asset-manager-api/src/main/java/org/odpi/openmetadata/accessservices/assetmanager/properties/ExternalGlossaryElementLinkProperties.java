/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalGlossaryLinkProperties describes the properties of URL link to a remote glossary.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalGlossaryElementLinkProperties implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String elementIdentifier  = null;
    private String elementDescription = null;
    private String steward            = null;
    private Date   lastVerified       = null;

    /**
     * Default constructor
     */
    public ExternalGlossaryElementLinkProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template object to copy.
     */
    public ExternalGlossaryElementLinkProperties(ExternalGlossaryElementLinkProperties template)
    {
        if (template != null)
        {
            elementIdentifier = template.getElementIdentifier();
            elementDescription = template.getElementDescription();
            steward = template.getSteward();
            lastVerified = template.getLastVerified();
        }
    }


    /**
     * Return the identifier in the external glossary resource that is equivalent to this element.
     *
     * @return string elementIdentifier
     */
    public String getElementIdentifier()
    {
        return elementIdentifier;
    }


    /**
     * Set up the identifier in the external glossary resource that is equivalent to this element.
     *
     * @param elementIdentifier string
     */
    public void setElementIdentifier(String elementIdentifier)
    {
        this.elementIdentifier = elementIdentifier;
    }


    /**
     * Return a description of the association between the category and the external element from the external glossary resource.
     *
     * @return string text
     */
    public String getElementDescription()
    {
        return elementDescription;
    }


    /**
     * Set up a description of the association between the category and the external element from the external glossary resource.
     *
     * @param description string text
     */
    public void setElementDescription(String description)
    {
        this.elementDescription = description;
    }


    /**
     * Return the identifier for the steward that created the link.
     *
     * @return string identifier
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the identifier for the steward that created the link.
     *
     * @param steward string identifier
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the date that a steward verified the link.
     *
     * @return date/time
     */
    public Date getLastVerified()
    {
        return lastVerified;
    }


    /**
     * Set up the date that a steward verified the link.
     *
     * @param lastVerified date/time
     */
    public void setLastVerified(Date lastVerified)
    {
        this.lastVerified = lastVerified;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalGlossaryElementLinkProperties{" +
                       "elementIdentifier='" + elementIdentifier + '\'' +
                       ", elementDescription='" + elementDescription + '\'' +
                       ", steward='" + steward + '\'' +
                       ", lastVerified=" + lastVerified +
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
        ExternalGlossaryElementLinkProperties that = (ExternalGlossaryElementLinkProperties) objectToCompare;
        return Objects.equals(getElementIdentifier(), that.getElementIdentifier()) &&
                       Objects.equals(getElementDescription(), that.getElementDescription()) &&
                       Objects.equals(getSteward(), that.getSteward()) &&
                       Objects.equals(getLastVerified(), that.getLastVerified());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getElementIdentifier(), getElementDescription(), getSteward(), getLastVerified());
    }
}
