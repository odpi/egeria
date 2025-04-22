/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FeedbackTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedBy;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommentElement contains the properties and header for a Comment entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentElement implements CorrelatedMetadataElement
{
    private ElementHeader                   elementHeader         = null;
    private List<MetadataCorrelationHeader> correlationHeaders    = null;
    private CommentProperties               properties            = null;
    private FeedbackTargetElement feedbackTargetElement = null;
    private List<RelatedBy>       acceptedAnswers       = null;
    private List<RelatedBy>       questionsAnswered     = null;


    /**
     * Default constructor
     */
    public CommentElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommentElement(CommentElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            correlationHeaders = template.getCorrelationHeaders();
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
     * Return the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @return list of correlation properties objects
     */
    @Override
    public List<MetadataCorrelationHeader> getCorrelationHeaders()
    {
        if (correlationHeaders == null)
        {
            return null;
        }
        else if (correlationHeaders.isEmpty())
        {
            return null;
        }

        return correlationHeaders;
    }


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @param correlationHeaders list of correlation properties objects
     */
    @Override
    public void setCorrelationHeaders(List<MetadataCorrelationHeader> correlationHeaders)
    {
        this.correlationHeaders = correlationHeaders;
    }


    /**
     * Return the properties of the comment.
     *
     * @return properties bean
     */
    public CommentProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the comment.
     *
     * @param properties properties bean
     */
    public void setProperties(CommentProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the relationship from the element in the request to the comment.  This value is null if the comment was retrieved independently
     * of any commented element.
     *
     * @return associated relationship
     */
    public FeedbackTargetElement getFeedbackTargetElement()
    {
        return feedbackTargetElement;
    }


    /**
     * Set up details of the relationship from the element in the request to the comment.  This value is null if the comment was retrieved independently
     * of any commented element.
     *
     * @param feedbackTargetElement associated relationship
     */
    public void setFeedbackTargetElement(FeedbackTargetElement feedbackTargetElement)
    {
        this.feedbackTargetElement = feedbackTargetElement;
    }


    /**
     * Return details of any comment that provides an accepted answer to the question posed in this comment.
     *
     * @return list of comment headers
     */
    public List<RelatedBy> getAcceptedAnswers()
    {
        return acceptedAnswers;
    }


    /**
     * Set up details of any comment that provides an accepted answer to the question posed in this comment.
     *
     * @param acceptedAnswers  list of comment headers
     */
    public void setAcceptedAnswers(List<RelatedBy> acceptedAnswers)
    {
        this.acceptedAnswers = acceptedAnswers;
    }


    /**
     * Return details of any comment that poses a question that this comment answers.
     *
     * @return  list of comment headers
     */
    public List<RelatedBy> getQuestionsAnswered()
    {
        return questionsAnswered;
    }


    /**
     * Set up details of any comment that poses a question that this comment answers.
     *
     * @param questionsAnswered  list of comment headers
     */
    public void setQuestionsAnswered(List<RelatedBy> questionsAnswered)
    {
        this.questionsAnswered = questionsAnswered;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommentElement{" +
                       "elementHeader=" + elementHeader +
                       ", correlationHeaders=" + correlationHeaders +
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
        if (! (objectToCompare instanceof CommentElement that))
        {
            return false;
        }
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(correlationHeaders, that.correlationHeaders) &&
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
        return Objects.hash(elementHeader, correlationHeaders, properties, feedbackTargetElement);
    }
}
