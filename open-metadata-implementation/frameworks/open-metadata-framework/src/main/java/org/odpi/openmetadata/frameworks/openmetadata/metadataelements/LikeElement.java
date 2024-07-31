/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LikeElement contains the properties and header for a like entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LikeElement implements MetadataElement
{
    private ElementHeader         elementHeader         = null;
    private LikeProperties        properties            = null;
    private FeedbackTargetElement feedbackTargetElement = null;

    /**
     * Default constructor
     */
    public LikeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LikeElement(LikeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            feedbackTargetElement = template.getFeedbackTargetElement();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the like.
     *
     * @return properties
     */
    public LikeProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the like properties.
     *
     * @param properties  properties
     */
    public void setProperties(LikeProperties properties)
    {
        this.properties = properties;
    }



    /**
     * Return details of the relationship from the element in the request to the like.  This value is null if it
     * was retrieved independently of the linked element.
     *
     * @return associated relationship
     */
    public FeedbackTargetElement getFeedbackTargetElement()
    {
        return feedbackTargetElement;
    }


    /**
     * Set up details of the relationship from the element in the request to the like.  This value is null if it
     * was retrieved independently of the linked element.
     *
     * @param feedbackTargetElement associated relationship
     */
    public void setFeedbackTargetElement(FeedbackTargetElement feedbackTargetElement)
    {
        this.feedbackTargetElement = feedbackTargetElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LikeElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", feedbackTargetElement=" + feedbackTargetElement +
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
        LikeElement that = (LikeElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(feedbackTargetElement, that.feedbackTargetElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, feedbackTargetElement);
    }
}
