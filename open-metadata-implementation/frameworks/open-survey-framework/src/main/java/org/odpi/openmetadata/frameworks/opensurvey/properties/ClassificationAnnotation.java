/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationAnnotation recommends classifications for either an asset or a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class ClassificationAnnotation extends DataFieldAnnotation
{
    private Map<String, String> candidateClassifications = null;

    /**
     * Default constructor
     */
    public ClassificationAnnotation()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ClassificationAnnotation(ClassificationAnnotation template)
    {
        super(template);

        if (template != null)
        {
            candidateClassifications = template.getCandidateClassifications();
        }
    }


    /**
     * Return a map of candidate classification names to additional characteristics
     *
     * @return map of classification names to string
     */
    public Map<String, String> getCandidateClassifications()
    {
        return candidateClassifications;
    }


    /**
     * Set up a map of candidate classification names to additional characteristics.
     *
     * @param candidateClassifications map of classification names to string
     */
    public void setCandidateClassifications(Map<String, String> candidateClassifications)
    {
        this.candidateClassifications = candidateClassifications;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ClassificationAnnotation{" +
                "candidateClassifications=" + candidateClassifications +
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
        ClassificationAnnotation that = (ClassificationAnnotation) objectToCompare;
        return Objects.equals(candidateClassifications, that.candidateClassifications);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), candidateClassifications);
    }
}
