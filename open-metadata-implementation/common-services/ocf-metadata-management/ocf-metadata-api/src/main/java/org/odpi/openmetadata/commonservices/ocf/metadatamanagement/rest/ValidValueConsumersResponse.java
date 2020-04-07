/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.ValidValueConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  ValidValuesConsumersResponse returns a list of Referenceables from the server that are linked
 *  to a valid values set/definition.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueConsumersResponse extends PagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<ValidValueConsumer> validValueConsumers = null;


    /**
     * Default constructor
     */
    public ValidValueConsumersResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueConsumersResponse(ValidValueConsumersResponse template)
    {
        super(template);

        if (template != null)
        {
            this.validValueConsumers = template.getValidValueConsumers();
        }
    }


    /**
     * Return the list of consumers in the response.
     *
     * @return list of consumers
     */
    public List<ValidValueConsumer> getValidValueConsumers()
    {
        if (validValueConsumers == null)
        {
            return null;
        }
        else if (validValueConsumers.isEmpty())
        {
            return null;
        }
        else
        {
            List<ValidValueConsumer>  clonedList = new ArrayList<>();

            for (ValidValueConsumer  existingElement : validValueConsumers)
            {
                clonedList.add(new ValidValueConsumer(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of assets for the response.
     *
     * @param validValueConsumers list
     */
    public void setValidValueConsumers(List<ValidValueConsumer> validValueConsumers)
    {
        this.validValueConsumers = validValueConsumers;
    }





    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueConsumersResponse{" +
                "validValueConsumers=" + validValueConsumers +
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
        ValidValueConsumersResponse that = (ValidValueConsumersResponse) objectToCompare;
        return Objects.equals(getValidValueConsumers(), that.getValidValueConsumers());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getValidValueConsumers());
    }
}
