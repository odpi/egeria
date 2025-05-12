/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementStub is used to identify an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataElementStub extends ElementControlHeader
{
    private String                       guid            = null;
    private List<AttachedClassification> classifications = null;
    private String                       uniqueName      = null;

    /**
     * Default constructor
     */
    public OpenMetadataElementStub()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public OpenMetadataElementStub(OpenMetadataElementStub template)
    {
        super(template);

        if (template != null)
        {
            guid              = template.getGUID();
            classifications   = template.getClassifications();
            uniqueName        = template.getUniqueName();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public OpenMetadataElementStub(ElementControlHeader template)
    {
        super(template);
    }


    /**
     * Return the unique id for the metadata element.
     *
     * @return String unique identifier
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique id for the metadata element.
     *
     * @param guid String unique identifier
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }

    /**
     * Return the list of classifications associated with the metadata element.
     *
     * @return Classifications  list of classifications
     */
    public List<AttachedClassification> getClassifications()
    {
        return classifications;
    }


    /**
     * Set up the list of classifications associated with the metadata element.
     *
     * @param classifications list of classifications
     */
    public void setClassifications(List<AttachedClassification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Return the unique name - if known
     *
     * @return string name
     */
    public String getUniqueName()
    {
        return uniqueName;
    }


    /**
     * Set up unique name - if known
     *
     * @param uniqueName string name
     */
    public void setUniqueName(String uniqueName)
    {
        this.uniqueName = uniqueName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataElementStub{" +
                "guid='" + guid + '\'' +
                ", classifications=" + classifications +
                ", uniqueName='" + uniqueName + '\'' +
                ", guid='" + getGUID() + '\'' +
                "} " + super.toString();
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataElementStub that = (OpenMetadataElementStub) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(classifications, that.classifications) &&
                Objects.equals(uniqueName, that.uniqueName);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), uniqueName);
    }
}
