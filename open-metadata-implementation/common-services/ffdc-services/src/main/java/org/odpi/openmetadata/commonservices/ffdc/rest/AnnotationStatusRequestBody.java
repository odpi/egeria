/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.AnnotationStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AnnotationStatusRequestBody provides a structure for passing the annotation status of an AnnotationProperties. This value is used to
 * restrict the values that are returned.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnnotationStatusRequestBody
{
    private AnnotationStatus annotationStatus = null;


    /**
     * Default constructor
     */
    public AnnotationStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnnotationStatusRequestBody(AnnotationStatusRequestBody template)
    {
        if (template != null)
        {
            annotationStatus = template.getAnnotationStatus();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param annotationStatus String name
     */
    public void setAnnotationStatus(AnnotationStatus annotationStatus)
    {
        this.annotationStatus = annotationStatus;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return annotationStatus
     */
    public AnnotationStatus getAnnotationStatus()
    {
        return annotationStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AnnotationStatusRequestBody{" +
                "qualifiedName='" + annotationStatus + '\'' +
                '}';
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
        AnnotationStatusRequestBody that = (AnnotationStatusRequestBody) objectToCompare;
        return Objects.equals(getAnnotationStatus(), that.getAnnotationStatus());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAnnotationStatus());
    }
}
