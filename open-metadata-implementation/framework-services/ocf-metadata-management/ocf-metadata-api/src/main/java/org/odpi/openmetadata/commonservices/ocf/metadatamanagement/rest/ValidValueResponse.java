/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.LastAttachment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ValidValue;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ValidValueResponse is the response structure used on the OMAS REST API calls that return a
 * valid value bean object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueResponse extends OCFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private ValidValue validValue = null;

    /**
     * Default constructor
     */
    public ValidValueResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueResponse(ValidValueResponse template)
    {
        super(template);

        if (template != null)
        {
            this.validValue                 = template.getValidValue();
        }
    }


    /**
     * Return the asset result.
     *
     * @return unique identifier
     */
    public ValidValue getValidValue()
    {
        return validValue;
    }


    /**
     * Set up the asset result.
     *
     * @param validValue  unique identifier
     */
    public void setValidValue(ValidValue validValue)
    {
        this.validValue = validValue;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueResponse{" +
                "validValue=" + validValue +
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
        ValidValueResponse that = (ValidValueResponse) objectToCompare;
        return Objects.equals(getValidValue(), that.getValidValue());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getValidValue());
    }
}
