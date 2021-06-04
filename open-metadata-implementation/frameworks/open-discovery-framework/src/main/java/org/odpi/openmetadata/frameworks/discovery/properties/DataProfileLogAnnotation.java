/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataProfileLogAnnotation is an annotation that is used when the profile data generated is too big to store in the metadata server
 * and has been located in log files instead.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProfileLogAnnotation extends DataFieldAnnotation
{
    private static final long    serialVersionUID = 1L;

    private List<String>   dataProfileLogFileNames = null;

    /**
     * Default constructor
     */
    public DataProfileLogAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataProfileLogAnnotation(DataProfileLogAnnotation template)
    {
        super(template);

        if (template != null)
        {
            dataProfileLogFileNames = template.getDataProfileLogFileNames();
        }
    }


    /**
     * Return the names of the log files used to store the profile data.
     *
     * @return list of names of log files
     */
    public List<String> getDataProfileLogFileNames()
    {
        if (dataProfileLogFileNames == null)
        {
            return null;
        }
        else if (dataProfileLogFileNames.isEmpty())
        {
            return null;
        }

        return dataProfileLogFileNames;
    }


    /**
     * Set up the names of the log files used to store the profile data.
     *
     * @param dataProfileLogFileNames list of names of log files
     */
    public void setDataProfileLogFileNames(List<String> dataProfileLogFileNames)
    {
        this.dataProfileLogFileNames = dataProfileLogFileNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProfileLogAnnotation{" +
                "dataProfileLogFileNames=" + dataProfileLogFileNames +
                ", annotationType='" + getAnnotationType() + '\'' +
                ", summary='" + getSummary() + '\'' +
                ", confidenceLevel=" + getConfidenceLevel() +
                ", expression='" + getExpression() + '\'' +
                ", explanation='" + getExplanation() + '\'' +
                ", analysisStep='" + getAnalysisStep() + '\'' +
                ", jsonProperties='" + getJsonProperties() + '\'' +
                ", annotationStatus=" + getAnnotationStatus() +
                ", numAttachedAnnotations=" + getNumAttachedAnnotations() +
                ", reviewDate=" + getReviewDate() +
                ", steward='" + getSteward() + '\'' +
                ", reviewComment='" + getReviewComment() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataProfileLogAnnotation that = (DataProfileLogAnnotation) objectToCompare;
        return Objects.equals(dataProfileLogFileNames, that.dataProfileLogFileNames);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataProfileLogFileNames);
    }
}
