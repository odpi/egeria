/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationReviewProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReviewProperties is used to record a steward's assessment of one or more elements linked as action targets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AnnotationReviewProperties.class, name = "AnnotationReviewProperties"),
})
public class ReviewProperties extends ActionProperties
{
    private Date   reviewDate = null;
    private String comment    = null;


    /**
     * Default constructor used by subclasses
     */
    public ReviewProperties()
    {
        super();
        super.typeName = OpenMetadataType.REVIEW.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ReviewProperties(ReviewProperties template)
    {
        super(template);

        if (template != null)
        {

            this.reviewDate = template.getReviewDate();
            this.comment    = template.getComment();
        }
    }


    /**
     * Return the date that this annotation was reviewed.  If no review has taken place then this property is null.
     *
     * @return Date review date
     */
    public Date getReviewDate()
    {
        return reviewDate;
    }


    /**
     * Set up the date that this annotation was reviewed.  If no review has taken place then this property is null.
     *
     * @param reviewDate review date
     */
    public void setReviewDate(Date reviewDate)
    {
        this.reviewDate = reviewDate;
    }


    /**
     * Return any comments made by the steward during the review.
     *
     * @return String review comment
     */
    public String getComment()
    {
        return comment;
    }


    /**
     * Set up any comments made by the steward during the review.
     *
     * @param comment review comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReviewProperties{" +
                "reviewDate=" + reviewDate +
                ", comment='" + comment + '\'' +
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
        ReviewProperties that = (ReviewProperties) objectToCompare;
        return Objects.equals(reviewDate, that.reviewDate) && Objects.equals(comment, that.comment);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), reviewDate, comment);
    }
}
