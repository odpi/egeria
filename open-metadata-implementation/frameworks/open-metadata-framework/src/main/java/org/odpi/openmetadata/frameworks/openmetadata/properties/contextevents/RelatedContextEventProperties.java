/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedContextEventProperties is a java bean used to describe a link between context events.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedContextEventProperties extends LabeledRelationshipProperties
{
    private int    statusIdentifier       = 0;
    private String source                 = null;
    private int    confidence             = 0;
    private String steward                = null;
    private String stewardTypeName        = null;
    private String stewardPropertyName    = null;
    private String notes                  = null;


    /**
     * Default constructor
     */
    public RelatedContextEventProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public RelatedContextEventProperties(RelatedContextEventProperties template)
    {
        super (template);

        if (template != null)
        {
            statusIdentifier    = template.getStatusIdentifier();
            source              = template.getSource();
            confidence          = template.getConfidence();
            steward             = template.getSteward();
            stewardTypeName     = template.getStewardTypeName();
            stewardPropertyName = template.getStewardPropertyName();
            notes               = template.getNotes();
        }
    }


    /**
     * Return the status identifier for the relationship.
     *
     * @return int
     */
    public int getStatusIdentifier()
    {
        return statusIdentifier;
    }


    /**
     * Set up the status identifier for the relationship.
     *
     * @param statusIdentifier int
     */
    public void setStatusIdentifier(int statusIdentifier)
    {
        this.statusIdentifier = statusIdentifier;
    }


    /**
     * Return the name of the source of knowledge that identified the relationship.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the name of source of knowledge that identified the relationship.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the confidence level (0-100) that the mapping is correct.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the confidence level (0-100) that the mapping is correct.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Returns the id of the steward responsible for the mapping.
     *
     * @return String id
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the id of the steward responsible for the mapping.
     *
     * @param steward String id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the type of element that describes the steward.
     *
     * @return type name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type of element that describes the steward.
     *
     * @param stewardTypeName type name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property that holds the steward's identifier.
     *
     * @return property name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property that holds the steward's identifier.
     *
     * @param stewardPropertyName property name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Return the additional values associated with the symbolic name.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional values associated with the symbolic name.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedContextEventProperties{" +
                "statusIdentifier=" + statusIdentifier +
                ", source='" + source + '\'' +
                ", confidence=" + confidence +
                ", steward='" + steward + '\'' +
                ", stewardTypeName='" + stewardTypeName + '\'' +
                ", stewardPropertyName='" + stewardPropertyName + '\'' +
                ", notes='" + notes + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        RelatedContextEventProperties that = (RelatedContextEventProperties) objectToCompare;
        return statusIdentifier == that.statusIdentifier
                && confidence == that.confidence
                && Objects.equals(source, that.source)
                && Objects.equals(steward, that.steward)
                && Objects.equals(stewardTypeName, that.stewardTypeName)
                && Objects.equals(stewardPropertyName, that.stewardPropertyName)
                && Objects.equals(notes, that.notes);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), confidence, steward, stewardTypeName, stewardPropertyName, notes);
    }
}
