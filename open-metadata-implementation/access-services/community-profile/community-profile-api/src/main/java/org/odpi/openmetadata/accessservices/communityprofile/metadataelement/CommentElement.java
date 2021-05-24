/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.metadataelement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CommentProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommentElement contains the properties and header for a comment retrieved from the metadata repository.
 * It is used for personal messages.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader     elementHeader = null;
    private CommentProperties properties    = null;
    private List<ElementStub> answeredBy    = null;
    private List<ElementStub> answers       = null;

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
            properties = template.getProperties();
            answeredBy = template.getAnsweredBy();
            answers = template.getAnswers();
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
     * Return the properties of the comment.
     *
     * @return properties
     */
    public CommentProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the comment properties.
     *
     * @param properties  properties
     */
    public void setProperties(CommentProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of comments that answer a question posed in this comment.
     *
     * @return list of related comments
     */
    public List<ElementStub> getAnsweredBy()
    {
        return answeredBy;
    }


    /**
     * Set up the list of comments that answer a question posed in this comment.
     *
     * @param answeredBy list of related comments
     */
    public void setAnsweredBy(List<ElementStub> answeredBy)
    {
        this.answeredBy = answeredBy;
    }


    /**
     * Return the list of unique identifiers (guids) for comments that contain a question that this comment answers.
     *
     * @return list of related comments
     */
    public List<ElementStub> getAnswers()
    {
        return answers;
    }


    /**
     * Set up the list of comments that contain a question that this comment answers.
     *
     * @param answers list of related comments
     */
    public void setAnswers(List<ElementStub> answers)
    {
        this.answers = answers;
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
                       ", properties=" + properties +
                       ", answeredBy=" + answeredBy +
                       ", answers=" + answers +
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
        CommentElement that = (CommentElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(answeredBy, that.answeredBy) &&
                       Objects.equals(answers, that.answers);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, answeredBy, answers);
    }
}
