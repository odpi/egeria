/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * QualityAnnotation records a quality assessment of the data.  It can be attached to the whole asset or a
 * specific data field.  Since there are different aspects of quality, the quality dimension characterizes the
 * type of quality it is measuring.  Then there is a score.  This is an integer and can be used as needed.
 * However, a suggestion is to use it as a percentage score - so a number between 0 and 100 where 100
 * is the best.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class QualityAnnotation extends DataFieldAnnotation
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String qualityDimension = null;
    private int    qualityScore = 0;


    /**
     * Default constructor
     */
    public QualityAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public QualityAnnotation(QualityAnnotation template)
    {
        super(template);

        if (template != null)
        {
            qualityDimension = template.getQualityDimension();
            qualityScore = template.getQualityScore();
        }
    }


    /**
     * Return the type of quality being measured.
     *
     * @return string name
     */
    public String getQualityDimension()
    {
        return qualityDimension;
    }


    /**
     * Set up the type of quality being measured.
     *
     * @param qualityDimension string name
     */
    public void setQualityDimension(String qualityDimension)
    {
        this.qualityDimension = qualityDimension;
    }


    /**
     * Return a quality score between 0 and 100 - 100 is the best.
     *
     * @return int
     */
    public int getQualityScore()
    {
        return qualityScore;
    }


    /**
     * Set up a quality score between 0 and 100 - 100 is the best.
     *
     * @param qualityScore int
     */
    public void setQualityScore(int qualityScore)
    {
        this.qualityScore = qualityScore;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "QualityAnnotation{" +
                "qualityDimension='" + qualityDimension + '\'' +
                ", qualityScore=" + qualityScore +
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
        QualityAnnotation that = (QualityAnnotation) objectToCompare;
        return qualityScore == that.qualityScore &&
                Objects.equals(qualityDimension, that.qualityDimension);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualityDimension, qualityScore);
    }
}
