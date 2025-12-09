/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ClassificationResponse describes the response structure for an OMRS REST API that returns
 * a Classification object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private Classification classification = null;

    /**
     * Default constructor
     */
    public ClassificationResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ClassificationResponse(ClassificationResponse template)
    {
        super(template);

        if (template != null)
        {
            classification = template.getClassification();
        }
    }


    /**
     * Return the resulting entity object.
     *
     * @return entity object
     */
    public Classification getClassification()
    {
        if (classification == null)
        {
            return null;
        }
        else
        {
            return new Classification(classification);
        }
    }


    /**
     * Set up the resulting entity object.
     *
     * @param classification entity object
     */
    public void setClassification(Classification classification)
    {
        this.classification = classification;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ClassificationResponse{" +
                "classification=" + classification +
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
        if (!(objectToCompare instanceof ClassificationResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ClassificationResponse
                that = (ClassificationResponse) objectToCompare;
        return Objects.equals(getClassification(), that.getClassification());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getClassification());
    }
}
