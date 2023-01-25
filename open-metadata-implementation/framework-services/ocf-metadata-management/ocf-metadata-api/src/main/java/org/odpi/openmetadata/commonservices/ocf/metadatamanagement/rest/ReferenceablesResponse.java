/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  ReferenceablesResponse returns a list of referenceables from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceablesResponse extends PagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<Referenceable> referenceables = null;


    /**
     * Default constructor
     */
    public ReferenceablesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceablesResponse(ReferenceablesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.referenceables = template.getReferenceables();
        }
    }


    /**
     * Return the list of referenceables in the response.
     *
     * @return list of beans
     */
    public List<Referenceable> getReferenceables()
    {
        if (referenceables == null)
        {
            return null;
        }
        else if (referenceables.isEmpty())
        {
            return null;
        }
        else
        {
            List<Referenceable>  clonedList = new ArrayList<>();

            for (Referenceable  existingElement : referenceables)
            {
                clonedList.add(new Referenceable(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of referenceables for the response.
     *
     * @param referenceables list
     */
    public void setReferenceables(List<Referenceable> referenceables)
    {
        this.referenceables = referenceables;
    }





    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceablesResponse{" +
                "referenceables=" + referenceables +
                ", startingFromElement=" + getStartingFromElement() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ReferenceablesResponse that = (ReferenceablesResponse) objectToCompare;
        return Objects.equals(getReferenceables(), that.getReferenceables());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getReferenceables());
    }
}
