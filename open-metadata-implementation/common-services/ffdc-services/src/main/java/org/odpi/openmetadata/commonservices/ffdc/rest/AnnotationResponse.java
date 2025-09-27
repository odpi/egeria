/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AnnotationResponse is the response structure used on the OMAS REST API calls that return a
 * AnnotationProperties object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnnotationResponse extends FFDCResponseBase
{
    private AnnotationProperties annotation = null;

    /**
     * Default constructor
     */
    public AnnotationResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnnotationResponse(AnnotationResponse template)
    {
        super(template);

        if (template != null)
        {
            this.annotation = template.getAnnotation();
        }
    }


    /**
     * Return the AnnotationProperties object.
     *
     * @return annotation
     */
    public AnnotationProperties getAnnotation()
    {
        return annotation;
    }


    /**
     * Set up the AnnotationProperties object.
     *
     * @param annotation - annotation object
     */
    public void setAnnotation(AnnotationProperties annotation)
    {
        this.annotation = annotation;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AnnotationResponse{" +
                "annotation=" + annotation +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof AnnotationResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getAnnotation(), that.getAnnotation());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (annotation == null)
        {
            return super.hashCode();
        }
        else
        {
            return annotation.hashCode();
        }
    }
}
