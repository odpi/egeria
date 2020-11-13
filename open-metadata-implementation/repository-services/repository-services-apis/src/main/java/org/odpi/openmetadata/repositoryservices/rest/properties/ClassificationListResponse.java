/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationListResponse support an OMRS REST API response that returns a list of Classification objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationListResponse extends OMRSAPIPagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<Classification> classifications = null;


    /**
     * Default constructor
     */
    public ClassificationListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ClassificationListResponse(ClassificationListResponse template)
    {
        super(template);

        if (template != null)
        {
            classifications = template.getClassifications();
        }
    }


    /**
     * Return the list of classifications.
     *
     * @return classifications list
     */
    public List<Classification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            List<Classification>  clonedClassifications = new ArrayList<>();

            for (Classification  classification : classifications)
            {
                clonedClassifications.add(new Classification(classification));
            }

            return clonedClassifications;
        }
    }


    /**
     * Set up the list of entities.
     *
     * @param classifications entity list
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ClassificationListResponse{" +
                "classifications=" + classifications +
                ", nextPageURL='" + nextPageURL + '\'' +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", actionDescription='" + actionDescription + '\'' +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionCausedBy='" + exceptionCausedBy + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionErrorMessageId='" + exceptionErrorMessageId + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(exceptionErrorMessageParameters) +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                ", exceptionProperties=" + exceptionProperties +
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
        if (!(objectToCompare instanceof ClassificationListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ClassificationListResponse that = (ClassificationListResponse) objectToCompare;
        return Objects.equals(getClassifications(), that.getClassifications());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getClassifications());
    }
}
